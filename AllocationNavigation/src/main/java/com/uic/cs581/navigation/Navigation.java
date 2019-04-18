package com.uic.cs581.navigation;

import com.uic.cs581.model.Cab;
import com.uic.cs581.model.CabPool;
import com.uic.cs581.model.Zone;
import com.uic.cs581.model.ZoneMap;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class Navigation {

    private final static int k = 1;

    public static void main(String[] args)  {

        //kHops("892a10089dbffff");
        //kHops(ZoneMap.getRandomZoneIndex());
        //oneHop(ZoneMap.getRandomZoneIndex());
        //oneHop("892a10089dbffff");

        CabPool.initialize(5);

        for(Cab cab: CabPool.getEntireCabPool())
            navigateCab(cab);
    }

    private static void navigateCab(Cab cab) {

        //TODO: check edge cases (general reminder)

        //only if cab doesn't have a resource
        if(cab.getTargetZone()!=null)// || cab.getTargetZone().length()!=0)
            return;

        /*
            if future Path is empty
                calculate future by kHops
                if currZone != end of searchPath
                    //this means cab had a resource in the gap observed
                    add new searchPath
        */
        if(cab.getFuturePath()==null || cab.getFuturePath().size()==0) {

            List<String> nextZones;

            //if no previous paths traversed
            if(cab.getSearchPaths().size()==0) {
                nextZones = kHops(cab.getCurrentZone());
                cab.getSearchPaths().add(new ArrayList<>());
            }
            else {
                List<String> lastPath = cab.getSearchPaths().get(cab.getSearchPaths().size()-1);

                //if resource was just dropped (means restart search)
                if (!cab.getCurrentZone().equals(lastPath.get(lastPath.size() - 1))) {
                    nextZones = kHops(cab.getCurrentZone());
                    cab.getSearchPaths().add(new ArrayList<>());
                } else
                    nextZones = kHops(cab.getCurrentZone(), lastPath.get(lastPath.size() - 1));
            }
            cab.setFuturePath(nextZones);

        }

        //Transfer one zone from future to last-search-path
        String z = cab.getFuturePath().remove(0);
        cab.getSearchPaths().get(cab.getSearchPaths().size()-1).add(z);
        cab.setCurrentZone(z);

    }

    private static List<String> kHops(String currZone)   {

        List<String> nextPath = new ArrayList<>();

        for(int i=0;i<k;i++)    {
            currZone = oneHop(currZone);
            nextPath.add(currZone);
        }
        log.debug(String.join(", ", nextPath));
        return nextPath;

        //int rand = (int)(Math.random() * 6);
        //return kHops(currZone, ZoneMap.getZone(currZone).kRingNeighbors.get(1).get(rand));
    }

    private static List<String> kHops(String currZone, String prevZone)    {

        List<String> nextPath = new ArrayList<>();

        for(int i=0;i<k;i++)    {
            String tempZone = currZone;
            currZone = oneHop(currZone, prevZone);
            prevZone = tempZone;
            nextPath.add(currZone);
        }
        log.debug(String.join(", ", nextPath));
        return nextPath;
    }

    private static String oneHop(String currZone, String prevZone)   {

        Zone zone = ZoneMap.getZone(currZone);
        List<String> kRing1 = zone.kRingNeighbors.get(1);

        if(!kRing1.contains(prevZone)) {
            log.error("prevZone not in currZone's neighbors");
            //throw new MissingResourceException("prevZone not in currZone's neighbors", "String", "prevZone");
            return oneHop(currZone);
        }

        //TODO: [-90,+90] neighbor selection
        return oneHop(currZone);
    }

    private static String oneHop(String currZone)  {

        Zone zone = ZoneMap.getZone(currZone);
        List<String> kRing1 = zone.kRingNeighbors.get(1);

        //log.debug("Zone: "+currZone);

        //Get scores of neighbors from Uber-API: (API results stored in zones folder)
        Double[] zoneScores = new Double[kRing1.size()];
        for(int i=0;i<kRing1.size();i++)    {
            zoneScores[i]=ZoneMap.getZone(kRing1.get(i)).getScore();

            //TODO: remove below 2 lines.......placed because scores are null at the time of coding
            //or replace a default value, current default value is the array-index
            if(zoneScores[i]==null)
                zoneScores[i]=(double)i;
        }
        log.debug(Arrays.toString(zoneScores));
        //zoneScores = new Double[]{6.0, 5.0, 3.0, 1.0, 2.0, 4.0};

        //Pick one neighbor non-deterministically
        int selectedIndex = nonDeterministic(zoneScores);
        log.debug("Selected index: "+selectedIndex);


        //Return h3-index of picked zone
        return kRing1.get(selectedIndex);

        //random index
        //int rand = (int)(Math.random() * 6);
        //return oneHop(currZone, ZoneMap.getZone(currZone).kRingNeighbors.get(1).get(rand));
    }

    private static int nonDeterministic(Double[] scores)   {

        //Cumulate scores to have ranges indirectly
        for(int i=1;i<scores.length;i++)
            scores[i] += scores[i-1];

        //select random value in full range
        double rand = Math.random() * (scores[scores.length-1] + 1);
        log.debug("Random value: "+rand);

        //search where this index belongs
        //TODO replace with binary search
        for(int i=0;i<scores.length;i++)
            if(rand<scores[i])
                return i;

        return 0;
    }
}
