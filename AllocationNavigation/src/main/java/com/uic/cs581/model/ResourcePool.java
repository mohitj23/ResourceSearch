package com.uic.cs581.model;

import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class ResourcePool {

    // entire resource pool
    private static final List<Resource> entirePool = new ArrayList<>();
    private static ListIterator<Resource> entirePoolIterator;

    // current resource pool based on the simulation time
    private static final List<Resource> currentPool = new ArrayList<>();

    // completed resource pool - either reached destination or expired
    private static final List<Resource> assignedPool = new ArrayList<>();

    public static List<Resource> getEntirePool() {
        return entirePool;
    }

    public static List<Resource> getCurrentPool() {
        return currentPool;
    }

    public static List<Resource> getAssignedPool() {
        return assignedPool;
    }


    /**
     * Current pool resources is updated based on MLT left and cab assignment.
     * Fetch new resources from entire pool based on Simulation current time.
     *
     * @return boolean: true if entire pool has resources left, else false.
     */
    public static boolean updateCurrentPool() {

        //iterate over current pool, remove expired resources to completed pool.
        //later use this iterator to add element form entire pool
        Iterator<Resource> currentPoolItr = currentPool.listIterator();

        while (currentPoolItr.hasNext()) {
            Resource temp = currentPoolItr.next();

            //expiration time(mlt) is exhausted then remove and add to assigned pool
            // or a resource is assigned a cab
            //TODO update expired time left attribute
            if (temp.getExpirationTimeLeftInMillis() <= 0 || temp.getCabId() > 0) {
                //TODO expired pool shoudl be different
                currentPoolItr.remove();
                assignedPool.add(temp);
            }
        }

        //iterate over entire pool from last breakpoint , remove them and move to current pool.
        entirePoolIterator = Optional.ofNullable(entirePoolIterator).orElse(entirePool.listIterator());

        while (entirePoolIterator.hasNext()) {
            Resource temp = entirePoolIterator.next();
            // add to current pool if Request time within simulation start and current time
            if (temp.getRequestTimeInMillis() >= SimulationClock.getSimStartTime() &&
                    temp.getRequestTimeInMillis() <= SimulationClock.getSimCurrentTime()) {
                entirePoolIterator.remove();
                currentPool.add(temp);

            } else {
                log.debug("Resource ReqTime is:" + new Date(temp.getRequestTimeInMillis()) +
                        "and Sim curr time is:" +
                        new Date(SimulationClock.getSimCurrentTime()));
                entirePoolIterator.previous();
                break;
            }

        }
        return entirePoolIterator.hasNext();
        //TODO if required sort current pool based on MLT left
    }

}
