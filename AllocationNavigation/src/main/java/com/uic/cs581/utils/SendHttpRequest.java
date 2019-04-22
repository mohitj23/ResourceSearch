package com.uic.cs581.utils;

import com.uic.cs581.model.Zone;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

@Slf4j
public class SendHttpRequest {

    public static void main(String args[]) {
//        ZoneMap.checkIfNewZoneScoreIsRequried(1451624400000L);
        TimeZone.setDefault(TimeZone.getTimeZone("America/New_York"));
        SendHttpRequest.getRequest(1451635510000L, false);
    }

    public static Map<String, Double> getRequest(long millis, boolean readJson) {
        BufferedReader in = null;
        HttpURLConnection con = null;

        if (readJson) {
            try {
                return new ObjectMapper().readValue(
                        new File("scores.json"),
                        new TypeReference<Map<String, Double>>() {
                        });
            } catch (IOException e) {
                log.error("Zones score file cannot be found/read.Please solve this issue first");
                System.exit(1);
                return null;
            }
        }

        try {
//            DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            log.info("Python Api hit:" + millis);
            URL url = new URL("http://127.0.0.1:5000/?times=" + millis);
            con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("GET");
//            con.setRequestProperty("Content-Type", "application/json");

            in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            con.disconnect();
            log.info("Python Api response success for:" + millis);
            return new ObjectMapper().readValue(content.toString(),
                    new TypeReference<Map<String, Double>>() {
                    });

        } catch (IOException e) {
            log.error("Exception with fetch data from Python api.", e);
        }
        return new HashMap<>();
    }


}

class ParameterStringBuilder {
    public static String getParamsString(Map<String, String> params)
            throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();

        for (Map.Entry<String, String> entry : params.entrySet()) {
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            result.append("&");
        }

        String resultString = result.toString();
        return resultString.length() > 0
                ? resultString.substring(0, resultString.length() - 1)
                : resultString;
    }
}

class FullResponseBuilder {
    public static String getFullResponse(HttpURLConnection con) throws IOException {
        StringBuilder fullResponseBuilder = new StringBuilder();

        // read status and message
        fullResponseBuilder.append(con.getResponseCode())
                .append(" ")
                .append(con.getResponseMessage())
                .append("\n");
        // read headers
        con.getHeaderFields().entrySet().stream()
                .filter(entry -> entry.getKey() != null)
                .forEach(entry -> {
                    fullResponseBuilder.append(entry.getKey()).append(": ");
                    List headerValues = entry.getValue();
                    Iterator it = headerValues.iterator();
                    if (it.hasNext()) {
                        fullResponseBuilder.append(it.next());
                        while (it.hasNext()) {
                            fullResponseBuilder.append(", ").append(it.next());
                        }
                    }
                    fullResponseBuilder.append("\n");
                });

        // read response content

        return fullResponseBuilder.toString();
    }
}