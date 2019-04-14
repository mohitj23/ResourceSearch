package com.uic.cs581.navigation;

import com.uic.cs581.model.Zone;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class Navigation {

    public static void main(String[] args) throws IOException {

        oneHop("89f05ab410bffff");
    }

    static String oneHop(String currZone) throws IOException {

        //TODO Get scores of neighbors from Uber-API: (API results stored in zones folder)

        /*
        String absPath = new File("").getAbsolutePath();
        //absPath = "\""+absPath+"\"";
        //System.out.println("absPath: "+absPath);
        String basePath = new File(absPath).getParent();
        //System.out.println(basePath);


        try(FileReader reader = new FileReader(basePath+"\\zones\\manhattan_zones_lat_lon_3.json"))  {

            //System.out.println(basePath);
            System.out.println("in try");


        } catch (Exception e)   {
            //System.out.println(basePath+" catch");
        }
        */

        HashMap<String, Zone> result =
                new ObjectMapper().readValue(
                        new File("./src/main/resources/manhattan_zones_lat_lon_3.json"),
                        new TypeReference<Map<String, Zone>>() {});

        Double[] zoneScores;
        //TODO
        //Commented since scores are not available yet
        /*
            zoneScoreRanges= new Double[6];
            int i=0;
            zoneScoreRanges[i] = result.get(result.get(currZone).kRingNeighbors.get(1).get(0)).getScore();
            for(String neighbor: result.get(currZone).kRingNeighbors.get(1).subList(1, 5))    {
                zoneScoreRanges[++i] = zoneScoreRanges[i-1]+result.get(neighbor).getScore();
            }
         */

        zoneScores = new Double[]{6.0, 5.0, 3.0, 1.0, 2.0, 4.0};

        //Pick one neighbor non-deterministically
        int selectedIndex = nonDeterministic(zoneScores);
        log.debug("Selected index: "+selectedIndex);

        //System.out.println(Arrays.toString(zoneScores));
        log.debug(Arrays.toString(zoneScores));

        //return h3-index of picked zone
        return result.get(currZone).kRingNeighbors.get(1).get(selectedIndex);
    }

    private static int nonDeterministic(Double[] scores)   {

        //cumulate scores to have ranges indirectly
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
