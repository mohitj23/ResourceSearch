package com.uic.cs581.utils;

import com.uic.cs581.model.Cab;
import com.uic.cs581.model.CabPool;
import com.uic.cs581.model.ResourcePool;
import com.uic.cs581.model.SimulationClock;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
public class Results {

    private static long SYSTEM_START_TIME;
    private static long SYSTEM_END_TIME;

    public static void avgSearchTimeOfAgents() {
        double avgSearchTime = CabPool.getEntireCabPool()
                .stream()
                .peek(cab -> cab.setTotalSearchTime((cab.getTotalIdleTime() + cab.getTotalTimeToResFromCurZone()) / (cab.getResourcesPickedUp() == 0 ? 1 : cab.getResourcesPickedUp())))
                .collect(Collectors.averagingLong(Cab::getTotalSearchTime));
        log.info("Average Search Time:\t " + avgSearchTime / 1000.0 + " s");
        log.info("Average Search Time:\t " + (avgSearchTime / (1000.0 * 60)) + " mins");
    }

    public static void avgIdleTimeOfAgents() {
        double avgIdleTime = CabPool.getEntireCabPool()
                .stream()
                .peek(cab -> cab.setTotalIdleTime(cab.getTotalIdleTime() / (cab.getResourcesPickedUp() == 0 ? 1 : cab.getResourcesPickedUp())))
                .collect(Collectors.averagingLong(Cab::getTotalIdleTime));
        log.info("Average Idle Time:\t " + avgIdleTime / 1000.0 + " s");
        log.info("Average Idle Time:\t " + (avgIdleTime / (1000.0 * 60)) + " mins");

        double avgResourcesPuckedUpPerCab = CabPool.getEntireCabPool()
                .stream().collect(Collectors.averagingInt(Cab::getResourcesPickedUp));
        log.info("Average resources picked up per cab:" + avgResourcesPuckedUpPerCab);

        double avgResourcesDropOffTime = CabPool.getEntireCabPool()
                .stream()
                .peek(cab -> cab.setTotalTimeToDropOff(cab.getTotalTimeToDropOff() / (cab.getResourcesPickedUp() == 0 ? 1 : cab.getResourcesPickedUp())))
                .collect(Collectors.averagingLong(Cab::getTotalTimeToDropOff));
        log.info("Average resources drop off time per cab:" + avgResourcesDropOffTime / 1000.0 + " s");
        log.info("Average resources drop off time per cab:" + (avgResourcesDropOffTime / (1000.0 * 60)) + " mins");
    }

    public static void percentageExpiredRsources() {
        log.info("Expired Resource count: " + ResourcePool.getExpiredPool().size());
        log.info("% Expired Resources: " +
                ((ResourcePool.getExpiredPool().size() * 100.0) / (ResourcePool.getExpiredPool().size() + ResourcePool.getAssignedPool().size())));
    }

    public static void percentageAssignedResources() {
        log.info("Assigned Resource count: " + ResourcePool.getAssignedPool().size());
        log.info("% Assigned Resources:" +
                ((ResourcePool.getAssignedPool().size() * 100.0) / (ResourcePool.getExpiredPool().size() + ResourcePool.getAssignedPool().size())));
    }

    public static void currentPoolResources() {
        log.info("Current Resource count: " + ResourcePool.getCurrentPool().size());
    }

    public static void totalResourcesConsidered() {
        log.info("Total resources not arrived yet: " + ResourcePool.getEntirePool().size());
        log.info("Total resources in the csv:" + (ResourcePool.getEntirePool().size() + ResourcePool.getAssignedPool().size() + ResourcePool.getExpiredPool().size() + ResourcePool.getCurrentPool().size()));
    }

    public static void simulationStarted() {
        SYSTEM_START_TIME = System.currentTimeMillis();
        log.info("System started at: " + new Date(SYSTEM_START_TIME));
    }

    public static void simulationCompleted() {
        SYSTEM_END_TIME = System.currentTimeMillis();
        log.info("System end time: " + new Date(SYSTEM_END_TIME));
        log.info("Simulated for  : " + (SimulationClock.getSimCurrentTime() - SimulationClock.getSimStartTime()) / 1000.0 + " s");
        log.info("Simulated for  : " + (SimulationClock.getSimCurrentTime() - SimulationClock.getSimStartTime()) / (60 * 1000.0) + " mins");
        log.info("No. of Simulated iterations: " + SimulationClock.getSimIterations());
        log.info("System ran for : " + (SYSTEM_END_TIME - SYSTEM_START_TIME) / 1000.0 + " s");
        log.info("System ran for : " + (SYSTEM_END_TIME - SYSTEM_START_TIME) / (1000.0 * 60) + " mins");

    }

    public static void runTimeTillNow() {
        log.info("Running for: " + (System.currentTimeMillis() - SYSTEM_START_TIME) / (1000.0 * 60) + " mins");
    }
}
