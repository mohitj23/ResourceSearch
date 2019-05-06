package com.uic.cs581.allocation;

import com.uber.h3core.H3Core;
import com.uber.h3core.exceptions.DistanceUndefinedException;
import com.uic.cs581.model.*;
import com.uic.cs581.utils.Havershine;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;

@Slf4j
public class ResourceAllocation {

    private static H3Core h3;

    public static void assignCabsToResources() throws IOException {

        h3 = Optional.ofNullable(h3).orElse(H3Core.newInstance());

        List<Resource> currentResourcePool = ResourcePool.getCurrentPool();
        List<Cab> availableCabs = CabPool.getAvailableCabs();

        ListIterator<Resource> currResourcePoolItr = currentResourcePool.listIterator();
        ListIterator<Cab> availableCabsItr;
        Optional<Cab> nearestCab;
        double minDistanceFromCabToRes = Double.MAX_VALUE;


        while (currResourcePoolItr.hasNext()) {
            Resource tempRes = currResourcePoolItr.next();
//            nearestCab = Optional.empty();
//            availableCabsItr = availableCabs.listIterator();

            nearestCab = availableCabs.parallelStream().min(Comparator.comparing(o -> Havershine.distance(o.getCurrentZone(), tempRes.getPickUpH3Index())));

            nearestCab = nearestCab.filter(cab -> {
                        double distanceToCover = Havershine.distance(cab.getCurrentZone(), tempRes.getPickUpH3Index());
                        long timeToCover = (long) ((distanceToCover / cab.getSpeed()) * 60 * 60 * 1000.0);
                        log.debug("For min cab" + cab.getId() + " distanceToCover:" + distanceToCover + ",timeToCover:" + timeToCover + ",exp:" + tempRes.getExpirationTimeLeftInMillis());
                        return tempRes.getExpirationTimeLeftInMillis() >= timeToCover;
                    }
            );

            tempRes.setExpirationTimeLeftInMillis(tempRes.getExpirationTimeLeftInMillis() - SimulationClock.getSimIncrInMillis());

            //remove nearest cab from cab pool, else it becomes available for other resources too
            nearestCab.ifPresent(cab -> {
                // calculate next available time for this cab
                // total distance is from current zone to the resource zone and from there to the destination zone
                final double cabDistanceToRes = Havershine.distance(cab.getCurrentZone(), tempRes.getPickUpH3Index());
                double cabDistanceFromPickupToDropOff =
                        Havershine.distance(tempRes.getPickUpH3Index(), tempRes.getDropOffH3Index());
                double totalDistance = cabDistanceToRes + cabDistanceFromPickupToDropOff;
                log.debug("For min cab" + cab.getId() + " cabDistanceToRes:" + cabDistanceToRes + ",totalDistance:" + totalDistance + ",exp:" + tempRes.getExpirationTimeLeftInMillis());


                cab.setNextAvailableTime(SimulationClock.getSimCurrentTime() + (long) ((totalDistance / cab.getSpeed()) * 60 * 60 * 1000.0));
                cab.setFuturePath(null);        //set future path to null in allocation, required for navigation module
                cab.setResourceId(tempRes.getResourceId());
                cab.setResourcesPickedUp(cab.getResourcesPickedUp() + 1);
                cab.setTotalTimeToDropOff(cab.getTotalTimeToDropOff() + Math.round((cabDistanceFromPickupToDropOff / cab.getSpeed()) * 60 * 60 * 1000.0));
                cab.setTotalTimeToResFromCurZone(cab.getTotalTimeToResFromCurZone() + Math.round((cabDistanceToRes / cab.getSpeed()) * 60 * 60 * 1000.0));


                //since the cab is assigned a resource remove it from the available list of cabs.
                availableCabs.remove(cab);  // Id is initialized from 1 and its an ArrayList.

                tempRes.setExpirationTimeLeftInMillis(0L);
                tempRes.setCabId(cab.getId());
            });
        }

        // find the shortest distance for the current resource

        // update the resource and cab object IMMEDIATELY
        //else the cab will be considered again

        // calculate the next available time

    }

    private static int getDistanceFromH3(String zone1, String zone2) {
        int distanceInHops = -1;
        try {
            // hit h3 Api for each resource and each cab
            distanceInHops = h3.h3Distance(zone1, zone2);
        } catch (DistanceUndefinedException e) {
            log.error("H3 Distance cannot be calculated between the indexes:" + zone1 + "," + zone2);
        }
        return distanceInHops;
    }
}


