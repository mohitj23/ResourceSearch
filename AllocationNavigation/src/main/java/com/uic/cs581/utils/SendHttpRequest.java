package com.uic.cs581.utils;

import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

@Slf4j
public class SendHttpRequest {

    public static void main(String args[]) {
        TimeZone.setDefault(TimeZone.getTimeZone("America/New_York"));
        SendHttpRequest.getRequest(1451635510000L, false);
    }

    public static Map<String, Double> getRequest(long millis, boolean readJson) {
        BufferedReader in;
        HttpURLConnection con;

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
            log.info("Python Api hit:" + millis);
            URL url = new URL("http://127.0.0.1:5000/?times=" + millis);
            con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("GET");

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