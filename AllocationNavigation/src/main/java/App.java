import com.uic.cs581.model.CabPool;
import com.uic.cs581.model.ResourcePool;
import com.uic.cs581.model.SimulationClock;
import com.uic.cs581.utils.BasicCSVReader;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.text.ParseException;
import java.util.TimeZone;

@Slf4j
public class App {

    public static void main(String[] args) throws IOException, ParseException {

        if (args.length != 3) {
            log.error("Please provide the required command line parameters");
            System.exit(1);
        }
        int noOfCabs = Integer.parseInt(args[0]);
        int simTimeIncrementsInMillis = Integer.parseInt(args[1]);
        // TODO String csvFileName = args[2];
        long runningTimeInMins = Long.parseLong(args[2]);
        long systemEndTime = System.currentTimeMillis() + runningTimeInMins * 60 * 100;

        log.debug("Default Timezone:" + TimeZone.getDefault().toString());
        TimeZone.setDefault(TimeZone.getTimeZone("America/New_York"));
        log.debug("Default Timezone changed:" + TimeZone.getDefault().toString());

        // Read the test data csv, get the resource list from ResourcePool
        BasicCSVReader.readResourcesFromTestData("test.csv");

        //Initialize the simulation time entity
        SimulationClock.initializeSimulationClock(BasicCSVReader.MIN_REQUEST_TIME, simTimeIncrementsInMillis);

        //start iteration

        //Hit Python App and get the zone score map

        // Read zone data from JSON file and update with the zoneScore
        // todo: uncomment        ZoneMap.updateZonesWithScores(new HashMap<>());

        //provide random locations to the cabs from the list of h3Indices
        CabPool.initialize(noOfCabs);

        while (System.currentTimeMillis() < systemEndTime) {

            //Simulation time increment

            //Zone score update

            //read the appropriate entries from the test data
            ResourcePool.updateCurrentPool();

            // run resource allocation component

            //run the navigation component
            SimulationClock.incrementSimulationTime();
        }
    }
}
