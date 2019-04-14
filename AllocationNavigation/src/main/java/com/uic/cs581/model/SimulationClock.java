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

    private Date startTime;

    private Integer incrementInMillis;

    private static SimulationClock sc;

    public static SimulationClock getInstance(Date startTime, Integer incrementInMillis) {
        return Optional.ofNullable(sc).orElse(new SimulationClock(startTime, incrementInMillis));
    }

    public static SimulationClock incrementSimulationTime() {
        if (sc == null) {
            log.error("Simulation clock should be initialized first.");
            System.exit(1);
        }
        sc.setStartTime(new Date(sc.getStartTime().getTime() + sc.getIncrementInMillis()));
        return sc;
    }

    private SimulationClock() {

    }

    private SimulationClock(Date startTime, Integer incrementInMillis) {
        this.startTime = startTime;
        this.incrementInMillis = incrementInMillis;
    }

}
