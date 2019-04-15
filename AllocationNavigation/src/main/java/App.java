import com.uic.cs581.model.Cab;
import com.uic.cs581.model.ZoneMap;
import com.uic.cs581.utils.BasicCSVReader;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;

@Slf4j
public class App {

    public static void main(String[] args) throws IOException, ParseException {

        //TODO: args[0]= no. of cabs

        log.info(TimeZone.getDefault().toString());
        TimeZone.setDefault(TimeZone.getTimeZone("America/New_York"));
        log.info(TimeZone.getDefault().toString());

        // Read the test data csv
        BasicCSVReader.getResourcesFromTestData("test.csv");

        //Hit Python App and get the zone score map

        // Read zone data from JSON file and update with the zoneScore
// todo: uncomment        ZoneMap.updateZonesWithScores(new HashMap<>());
        // append h3Index to the test data

        //TODO: provide random locations to the cabs from the list of h3Indices
        List<Cab> cabList=new LinkedList<>();
        for(int i=0; i<1000;i++){
            cabList.add(Cab.builder().currentZone(ZoneMap.getRandomZoneIndex()).build());
        }
//        log.info(cabList.toString());
        //Initialize the simulation time entity

        //read the appropriate entries from the test data

        // run resource allocation component

        //run the navigation component

    }

}
