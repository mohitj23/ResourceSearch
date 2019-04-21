package com.uic.cs581.model;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.Optional;

@Slf4j
@Getter
@Setter
public final class SimulationClock {

    private long startTime;

    private int incrementInMillis;

    private int noOfIterations; // default count is 0

    private long currentSimTime;

    private static SimulationClock sc;

    public static void initializeSimulationClock(long startTime, int incrementInMillis) {
        sc = Optional.ofNullable(sc).orElse(new SimulationClock(startTime, incrementInMillis));
        log.info("Simulation Time start from " + new Date(sc.getStartTime()));
    }

    public static void incrementSimulationTime() {
        if (sc == null) {
            log.error("Simulation clock should be initialized first.");
            System.exit(1);
        }
        sc.setNoOfIterations(sc.getNoOfIterations() + 1);
        sc.setCurrentSimTime(sc.getCurrentSimTime() + sc.getIncrementInMillis());

        //TODO new Date should be removed for performance
        log.debug("Simulation Time incremented by " + sc.getIncrementInMillis() + "millis to " + new Date(sc.getCurrentSimTime()));

//        return sc.getCurrentSimTime();
    }

    public static long getSimCurrentTime() {
        return sc.getCurrentSimTime();
    }

    public static int getSimIncrInMillis() {
        return sc.getIncrementInMillis();
    }

    public static long getSimStartTime() {
        return sc.getStartTime();
    }

    private SimulationClock(long startTime, int incrementInMillis) {
        this.startTime = startTime;
        this.incrementInMillis = incrementInMillis;
        this.currentSimTime = startTime;
    }
}
