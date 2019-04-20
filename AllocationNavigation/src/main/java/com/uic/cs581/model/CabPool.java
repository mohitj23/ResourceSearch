package com.uic.cs581.model;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

@Slf4j
public class CabPool {

    private static final List<Cab> entireCabPool = new LinkedList<>();

    private static List<Cab> availableCabs;

    public static void initialize(int noOfCabs, int cabSpeed) {
        for (int i = 0; i < noOfCabs; i++) {
            entireCabPool.add(Cab.builder()
                    .id(i)
                    .searchPaths(new ArrayList<>())
                    .currentZone(ZoneMap.getRandomZoneIndex())
                    .speed(cabSpeed)
                    .build());
        }
    }

    public static List<Cab> getEntireCabPool() {
        return entireCabPool;
    }

    public static List<Cab> getAvailableCabs() {
        return availableCabs;
    }

    /**
     * Get all cabs from the entireCabPool which don't have a resourceId tagged or(and)
     * next available time is <= current simulation time.
     */
    public static void findAvailableCabs() {
        ListIterator<Cab> entireCabsItr = entireCabPool.listIterator();
        availableCabs = new ArrayList<>();

        while (entireCabsItr.hasNext()) {
            Cab cab = entireCabsItr.next();
            //cab is available
            //TODO check which condition will be provided
            if (cab.getResourceId() <= 0 || cab.getNextAvailableTime() <= SimulationClock.getSimCurrentTime()) {
                availableCabs.add(cab);
            }
        }
//        return availableCabs;
    }

    private CabPool() {

    }
}
