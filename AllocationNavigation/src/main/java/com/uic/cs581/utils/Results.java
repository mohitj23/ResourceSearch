package com.uic.cs581.utils;

import com.uic.cs581.model.Cab;
import com.uic.cs581.model.CabPool;
import com.uic.cs581.model.ResourcePool;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Slf4j
public class Results {

    private static long SYSTEM_START_TIME;
    private static long SYSTEM_END_TIME;

    public static void avgSearchTimeOfAgents() {
        double avgSearchTime = CabPool.getEntireCabPool().parallelStream()
                .reduce(0.0, (subtotal, element) -> subtotal + element.getTotalSearchTime(), Double::sum) / CabPool.getEntireCabPool().size();
        log.info("Average Search Time:\t " + avgSearchTime);
    }

    public static void avgIdleTimeOfAgents() {
        double avgIdleTime = CabPool.getEntireCabPool().parallelStream()
                .reduce(0.0, (subtotal, element) -> subtotal + element.getTotalIdleTime(), Double::sum) / CabPool.getEntireCabPool().size();
        log.info("Average Idle Time:\t " + avgIdleTime);
    }

    public static void percentageExpiredRsources() {
        log.info("% Expired Resources:" +
                (ResourcePool.getExpiredPool().size() / (ResourcePool.getExpiredPool().size() + ResourcePool.getAssignedPool().size())));
    }

    public static void percentageAssignedResources() {
        log.info("% Expired Resources:" +
                (ResourcePool.getAssignedPool().size() / (ResourcePool.getExpiredPool().size() + ResourcePool.getAssignedPool().size())));
    }

    public static void SimulationStarted() {
        SYSTEM_START_TIME = System.currentTimeMillis();
        log.info("System started at:" + new Date(SYSTEM_START_TIME));
    }

    public static void SimulationCompleted() {
        SYSTEM_END_TIME = System.currentTimeMillis();
        log.info("System end time:" + new Date(SYSTEM_END_TIME));
        log.info("System ran for :" + (SYSTEM_END_TIME - SYSTEM_START_TIME) + " ms");
        log.info("System ran for :" + (SYSTEM_END_TIME - SYSTEM_START_TIME) / 1000 + " s");
        log.info("System ran for :" + (SYSTEM_END_TIME - SYSTEM_START_TIME) / (1000 * 60) + " mins");

    }
}
