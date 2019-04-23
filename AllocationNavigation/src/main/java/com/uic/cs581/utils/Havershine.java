package com.uic.cs581.utils;

import com.uic.cs581.model.Zone;
import com.uic.cs581.model.ZoneMap;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class Havershine {

    public static void main(String args[]) {

        try {
            Map<String, Double> result = new HashMap<>();
            Map<String, Zone> zoneMap = new ObjectMapper().readValue(
                    new File("./manhattan_zones_lat_lon_3.json"),
                    new TypeReference<Map<String, Zone>>() {
                    });

            zoneMap.keySet().parallelStream().forEach(zone1 -> {
                zoneMap.keySet().parallelStream().forEach(zone2 -> {
                    if (!(result.containsKey(zone1 + zone2) || result.containsKey(zone2 + zone1))) {
                        result.put(zone1 + zone2, Havershine.distance(zone1, zone2));
                    }
                });
            });

            try {
                new ObjectMapper().writeValue(new File("havershine"), result);
            } catch (IOException e) {
                log.error("Error while writing JSON to file.");
            }
        } catch (IOException e) {
            log.error("Zones file cannot be found/read.Please solve this issue first");
            System.exit(1);

        }


    }

    private static final int EARTH_RADIUS = 6371; // Approx Earth radius in KM

    private static Map<String, Double> PREV_DIST = new HashMap<>();

    public static void loadDistance() {
        try {
            PREV_DIST = new ObjectMapper().readValue(
                    new File("./havershine.json"),
                    new TypeReference<Map<String, Double>>() {
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Double distance(String start,
                                  String end) {
        if (PREV_DIST.get(start + end) != null) {
            return PREV_DIST.get(start + end);
        }
        if (PREV_DIST.get(end + start) != null) {
            return PREV_DIST.get(end + start);
        }

        double startLat = ZoneMap.getZone(start).getLat();
        double startLong = ZoneMap.getZone(start).getLong1();

        double endLat = ZoneMap.getZone(end).getLat();
        double endLong = ZoneMap.getZone(end).getLong1();

        double dLat = Math.toRadians((endLat - startLat));
        double dLong = Math.toRadians((endLong - startLong));

        startLat = Math.toRadians(startLat);
        endLat = Math.toRadians(endLat);

        double a = haversin(dLat) + Math.cos(startLat) * Math.cos(endLat) * haversin(dLong);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double result = Math.abs(EARTH_RADIUS * c);
        PREV_DIST.put(start + end, result);
        return result; // <-- d
    }

    private static double haversin(double val) {
        return Math.pow(Math.sin(val / 2), 2);
    }
}
