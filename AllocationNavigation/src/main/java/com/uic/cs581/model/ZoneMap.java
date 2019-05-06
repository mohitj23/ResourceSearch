package com.uic.cs581.model;

import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

@Slf4j
public class ZoneMap {

    private static Map<String, Zone> zoneMap;
    private static final Random random = new Random();
    private static Set<String> keys;

    private ZoneMap() {

    }

    private static Map<String, Zone> getInstance() {
        zoneMap = Optional.ofNullable(zoneMap).orElse(readDataFromFile());
        return zoneMap;
    }

    /**
     * Read the JSON data into the Map zoneIds and Zone
     *
     * @return HashMap <String,Zone>
     */
    private static Map<String, Zone> readDataFromFile() {
        try {
            return new ObjectMapper().readValue(
                    new File("./manhattan_zones_lat_lon_3.json"),
                    new TypeReference<Map<String, Zone>>() {
                    });
        } catch (IOException e) {
            log.error("Zones file cannot be found/read.Please solve this issue first");
            System.exit(1);
            return null;
        }
    }

    public static void updateZonesWithScores(Map<String, Double> zoneScores, boolean randomScores) {
        updateScores(getInstance(), zoneScores, randomScores);
    }

    private static void updateScores(Map<String, Zone> zones, Map<String, Double> scores, boolean randomScores) {
        if (scores.size() < 5) {
            log.info("Zone scores r update");
        }
        zones.keySet().parallelStream()
                .forEach(h3Index -> {
                            if (randomScores || scores.size() < 5) {
                                zones.get(h3Index).setScore(random.nextInt(100) * 1.0);
                            } else {
                                zones.get(h3Index).setScore(scores.getOrDefault(h3Index, 0.0));
                            }

                        }
                );

        log.info("Zone scores updated");
    }

    public static Zone getZone(String h3Index) {
        return getInstance().get(h3Index);
    }

    static String getRandomZoneIndex() {
        if (keys == null) {
            keys = getInstance().keySet();
        }
        return (String) keys.toArray()[random.nextInt(keys.size())];
    }

    public static boolean checkIfNewZoneScoreIsRequried(long time) {
        return SimulationClock.checkMinsIs30or00(time);
    }
}