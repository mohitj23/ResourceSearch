package com.uic.cs581.navigation;

import com.uic.cs581.model.Zone;
import com.uic.cs581.model.ZoneMap;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import java.util.Arrays;
import java.util.List;
import java.util.MissingResourceException;

@Slf4j
public class Navigation {

    public static void main(String[] args)  {

        oneHop(ZoneMap.getRandomZoneIndex());
        //oneHop("892a10089dbffff");
    }

    static String kHops(String currZone, String prevZone, int k)    {

        if(k==1)
            return oneHop(currZone, prevZone);

        return currZone;
    }

    static String oneHop(String currZone, String prevZone)   {

        //TODO: [-90,+90] neighbor selection

        Zone zone = ZoneMap.getZone(currZone);
        List<String> kRing1 = zone.kRingNeighbors.get(1);

        //TODO: replace with appropriate exception
        if(!kRing1.contains(prevZone))
            throw new MissingResourceException("prevZone not in currZone's neighbors", "String", "prevZone");

        //Get scores of neighbors from Uber-API: (API results stored in zones folder)
        Double[] zoneScores = new Double[6];
        for(int i=0;i<6;i++)    {
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
    }

    static String oneHop(String currZone)  {

        //random index
        int rand = (int)(Math.random() * 6);
        return oneHop(currZone, ZoneMap.getZone(currZone).kRingNeighbors.get(1).get(rand));
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
