package com.uic.cs581.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class ResourcePool {

    // entire resource pool
    private static final List<Resource> entirePool = new ArrayList<>();
    private static Iterator<Resource> entirePoolIterator;

    // current resource pool based on the simulation time
    private static final List<Resource> currentPool = new ArrayList<>();

    // completed resource pool - either reached destination or expired
    private static final List<Resource> completedPool = new ArrayList<>();

    public static List<Resource> getEntirePool() {
        return entirePool;
    }

    public static List<Resource> getCurrentPool() {
        return currentPool;
    }

    public static List<Resource> getCompletedPool() {
        return completedPool;
    }

    public static void updateCurrentPool() {

        //iterate over current pool, remove expired resources to completed pool.
        //later use this iterator to add element form entire pool
        Iterator<Resource> currentPoolItr = currentPool.listIterator();

        while (currentPoolItr.hasNext()) {
            Resource temp = currentPoolItr.next();

            //expiration time(mlt) is exhausted then remove and add to completed pool
            if (temp.getExpirationTimeLeftInMillis() <= 0) {
                currentPoolItr.remove();
                completedPool.add(temp);
            }
        }

        //iterate over entire pool from last breakpoint , remove them and move to current pool.
        entirePoolIterator = Optional.of(entirePoolIterator).orElse(entirePool.listIterator());

        while (entirePoolIterator.hasNext()) {
            Resource temp = entirePoolIterator.next();
            // add to current pool if Request time within simulation start and current time
            if (temp.getRequestTimeInMillis() >= SimulationClock.getSimStartTime() &&
                    temp.getRequestTimeInMillis() <= SimulationClock.getSimCurrentTime()) {
                entirePoolIterator.remove();
                currentPool.add(temp);
            }

        }
    }

}
