package com.uic.cs581.model;

import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class ZoneMap {

    private static Map<String, Zone> zoneMap;

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
            log.info("Working Directory = " +
                    System.getProperty("user.dir"));

            return new ObjectMapper().readValue(
                    new File("./src/main/resources/manhattan_zones_lat_lon_3.json"),
                    new TypeReference<Map<String, Zone>>() {
                    });
        } catch (IOException e) {
            log.error("Zones file cannot be found/read.Please solve this issue first");
            System.exit(1);
            return null;
        }
    }

    public static Map<String, Zone> updateZonesWithScores(Map<String, Double> zoneScores) {
        return updateScores(getInstance(), zoneScores);
    }

    private static Map<String, Zone> updateScores(Map<String, Zone> zones, Map<String, Double> scores) {
        final int[] count = {0};
        zones.entrySet().stream()
                .filter(h3Index -> {
                    if (scores.containsKey(h3Index)) {
                        log.info("h3Index found scoresMap - keep the record");
                        return true;
                    }
                    log.error("h3Index not present in scores");
                    return false;
                })
                .forEach(h3Index -> {
                            zones.get(h3Index).setScore(scores.get(h3Index));
                            count[0]++;
                        }
                );

        if (count[0] != zones.size()) {
            log.error("Size of ZoneMap updated is different from scoresMap");
            System.exit(1); //TODO remove only after its verified that its ok to continue
        }

        return zones;
    }

    public static Zone getZone(String h3Index) {
        return getInstance().get(h3Index);
    }
}