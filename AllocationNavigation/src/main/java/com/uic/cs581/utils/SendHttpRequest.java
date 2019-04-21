package com.uic.cs581.utils;

import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Slf4j
public class SendHttpRequest {

    public  static void  main(String args[]){
        SendHttpRequest.getRequest();
    }

    public static Map<String, Double> getRequest() {
        BufferedReader in = null;
        HttpURLConnection con = null;
        try {
            URL url = new URL("http://example.com");
            con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");

            in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            con.disconnect();

            return new ObjectMapper().readValue(content.toString(),
                    new TypeReference<Map<String, Double>>() {
                    });

        } catch (IOException e) {
            log.error("Exception with fetch data from Python api.");
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