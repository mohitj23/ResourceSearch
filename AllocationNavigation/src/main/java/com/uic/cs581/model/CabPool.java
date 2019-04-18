package com.uic.cs581.model;

import java.util.LinkedList;
import java.util.List;

public class CabPool {

    private static final List<Cab> cabList = new LinkedList<>();

    public static void initialize(int noOfCabs) {
        for (int i = 0; i < noOfCabs; i++) {
            cabList.add(Cab.builder().currentZone(ZoneMap.getRandomZoneIndex()).build());
        }
    }

    public static List<Cab> getEntireCabPool() {
        return cabList;
    }

    private CabPool() {

    }
}
