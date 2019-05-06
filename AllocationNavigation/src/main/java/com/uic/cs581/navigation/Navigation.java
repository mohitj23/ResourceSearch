package com.uic.cs581.navigation;

import com.uic.cs581.model.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Slf4j
public class Navigation {

    private static int k = 5; // overridden by the parameter passed

    public static void main(String[] args) {

        //kHops("892a10089dbffff");
        //kHops(ZoneMap.getRandomZoneIndex());
        //oneHop(ZoneMap.getRandomZoneIndex());
        //oneHop("892a10089dbffff");

        CabPool.initialize(5, 40);

        navigate(k);
    }

    public static void navigate(int noOfHopsPreCalculate) {
        k = noOfHopsPreCalculate;
        CabPool.getAvailableCabs().parallelStream().forEach(Navigation::navigateCab);
        log.info("Navigation completed for current Sim time:" + new Date(SimulationClock.getSimCurrentTime()) + ",iteration:" + SimulationClock.getSimIterations());
    }

    private static void navigateCab(Cab cab) {

        log.debug("Navigating cab: " + cab.getId());

        if (cab.getFuturePath() == null || cab.getFuturePath().size() == 0) {

            List<String> nextZones;

            //if no previous paths traversed
            if (cab.getSearchPaths().size() == 0) {
                nextZones = kHops(cab.getCurrentZone());
                cab.getSearchPaths().add(new ArrayList<>());
            } else {
                List<String> lastPath = cab.getSearchPaths().get(cab.getSearchPaths().size() - 1);

                //if resource was just dropped (means restart search)
                if (!cab.getCurrentZone().equals(lastPath.get(lastPath.size() - 1))) {
                    nextZones = kHops(cab.getCurrentZone());
                    cab.getSearchPaths().add(new ArrayList<>());
                    //Record current zone as start of new search path
                    cab.getSearchPaths().get(cab.getSearchPaths().size() - 1).add(cab.getCurrentZone());
                } else
                    nextZones = kHops(cab.getCurrentZone(), lastPath.get(lastPath.size() - 1));
            }
            cab.setFuturePath(nextZones);

        }

        //Transfer one zone from future to last-search-path
        String z = cab.getFuturePath().remove(0);
        cab.getSearchPaths().get(cab.getSearchPaths().size() - 1).add(z);
        cab.setCurrentZone(z);
        cab.setTotalIdleTime(cab.getTotalIdleTime() + SimulationClock.getSimIncrInMillis());
    }

    private static List<String> kHops(String currZone) {

        List<String> nextPath = new ArrayList<>();

        for (int i = 0; i < k; i++) {
            currZone = oneHop(currZone);
            nextPath.add(currZone);
        }
        log.debug(String.join(", ", nextPath));
        return nextPath;

    }

    private static List<String> kHops(String currZone, String prevZone) {

        List<String> nextPath = new ArrayList<>();

        for (int i = 0; i < k; i++) {
            String tempZone = currZone;
            currZone = oneHop(currZone, prevZone);
            prevZone = tempZone;
            nextPath.add(currZone);
        }
        log.debug(String.join(", ", nextPath));
        return nextPath;
    }

    private static String oneHop(String currZone, String prevZone) {

        Zone zone = ZoneMap.getZone(currZone);
        List<String> kRing1 = zone.kRingNeighbors.get(1);

        //t o d o: [-90,+90] neighbor selection
        return oneHop(currZone);
    }

    private static String oneHop(String currZone) {

        Zone zone = ZoneMap.getZone(currZone);
        List<String> kRing1 = zone.kRingNeighbors.get(1);

        if (kRing1.size() < 1) {
            log.error("Zone has no neighbors");
            return currZone;
        }

        //Get scores of neighbors from Uber-API: (API results stored in zones folder)
        Double[] zoneScores = new Double[kRing1.size()];
        for (int i = 0; i < kRing1.size(); i++) {
            zoneScores[i] = ZoneMap.getZone(kRing1.get(i)).getScore();

            //TODO: remove below 2 lines.......placed because scores are null at the time of coding
            //or replace a default value, current default value is the array-index
            if (zoneScores[i] == null || zoneScores[i] == 0.0)
                zoneScores[i] = (double) i;
        }
        log.debug(Arrays.toString(zoneScores));

        //Pick one neighbor non-deterministically
        int selectedIndex = nonDeterministic(zoneScores);
        log.debug("Selected index: " + selectedIndex);


        //Return h3-index of picked zone
        return kRing1.get(selectedIndex);

    }

    private static int nonDeterministic(Double[] scores) {

        //Cumulate scores to have ranges indirectly
        for (int i = 1; i < scores.length; i++)
            scores[i] += scores[i - 1];

        //select random value in full range
        double rand = Math.random() * (scores[scores.length - 1] + 1);
        log.debug("Random value: " + rand);

        //search where this index belongs
        //TODO replace with binary search
        for (int i = 0; i < scores.length; i++)
            if (rand < scores[i])
                return i;

        return 0;
    }
}
