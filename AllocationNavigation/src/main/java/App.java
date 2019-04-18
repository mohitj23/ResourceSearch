import com.uic.cs581.model.Cab;
import com.uic.cs581.model.Resource;
import com.uic.cs581.model.SimulationClock;
import com.uic.cs581.model.ZoneMap;
import com.uic.cs581.utils.BasicCSVReader;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;

import static com.uic.cs581.utils.BasicCSVReader.getResourcesFromTestData;

@Slf4j
public class App {

    public static void main(String[] args) throws IOException, ParseException {

        if (args.length != 2) {
            log.error("Please provide the requried command line parameters");
            System.exit(1);
        }
        int noOfCabs = Integer.parseInt(args[0]);
        int simTimeIncrements = Integer.parseInt(args[1]);

        log.info("Default Timezone:" + TimeZone.getDefault().toString());
        TimeZone.setDefault(TimeZone.getTimeZone("America/New_York"));
        log.info("Default Timezone changed:" + TimeZone.getDefault().toString());

        // Read the test data csv
        List<Resource> resources = BasicCSVReader.getResourcesFromTestData("test.csv");

        //Initialize the simulation time entity
        SimulationClock.initializeSimulationClock(BasicCSVReader.MIN_REQUEST_TIME, simTimeIncrements);

        //start iteration

        //Hit Python App and get the zone score map

        // Read zone data from JSON file and update with the zoneScore
// todo: uncomment        ZoneMap.updateZonesWithScores(new HashMap<>());
        // append h3Index to the test data

        //TODO: provide random locations to the cabs from the list of h3Indices
        List<Cab> cabList = new LinkedList<>();
        for (int i = 0; i < noOfCabs; i++) {
            cabList.add(Cab.builder().currentZone(ZoneMap.getRandomZoneIndex()).build());
        }
//        log.info(cabList.toString());


        //read the appropriate entries from the test data

        // run resource allocation component

        //run the navigation component

    }
}
