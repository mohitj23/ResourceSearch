package com.uic.cs581.model;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.Optional;

@Slf4j
@Getter
@Setter
public class SimulationClock {

    private long startTime;

    private int incrementInMillis;

    private int noOfIncrements;

    private long currentSimTime;

    private static SimulationClock sc;

    public static void initializeSimulationClock(long startTime, int incrementInMillis) {
        sc = Optional.ofNullable(sc).orElse(new SimulationClock(startTime, incrementInMillis));
    }

    public static long incrementSimulationTime() {
        if (sc == null) {
            log.error("Simulation clock should be initialized first.");
            System.exit(1);
        }
        sc.setNoOfIncrements(sc.getNoOfIncrements() + 1);
        sc.setCurrentSimTime(sc.getCurrentSimTime() + sc.getIncrementInMillis());
        return sc.getCurrentSimTime();
    }

    private SimulationClock() {

    }

    private SimulationClock(long startTime, int incrementInMillis) {
        this.startTime = startTime;
        this.incrementInMillis = incrementInMillis;
        this.currentSimTime = startTime;
    }

}
