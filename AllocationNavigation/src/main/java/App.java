import com.uic.cs581.model.*;
import com.uic.cs581.utils.BasicCSVReader;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.TimeZone;

@Slf4j
public class App {
    private static final long ZONE_SCORE_UPDATE_INTERVAL = 30000;

    private static final double ZONE_DIAMETER_MILES = 0.108;                 //Resolution 9 -> converted to miles

    public static void main(String[] args) throws IOException, ParseException {

        if (args.length != 3) {
            log.error("Please provide the required command line parameters");
            System.exit(1);
        }
        int noOfCabs = Integer.parseInt(args[0]);
        int cabSpeed = Integer.parseInt(args[1]);     //in mph
        // TODO String csvFileName = args[2];
        long runningTimeInMins = Long.parseLong(args[2]);
        long systemEndTime = System.currentTimeMillis() + runningTimeInMins * 60 * 100;
        int simTimeIncrementsInMillis = (int) Math.ceil((ZONE_DIAMETER_MILES / cabSpeed) * 60 * 100);

        log.debug("Default Timezone:" + TimeZone.getDefault().toString());
        TimeZone.setDefault(TimeZone.getTimeZone("America/New_York"));
        log.debug("Default Timezone changed:" + TimeZone.getDefault().toString());

        // Read the test data csv, get the resource list from ResourcePool
        BasicCSVReader.readResourcesFromTestData("test.csv");

        //Initialize the simulation time entity
        SimulationClock.initializeSimulationClock(BasicCSVReader.MIN_REQUEST_TIME, simTimeIncrementsInMillis);

        //Hit Python App and get the zone score map
        long prevZoneScoreUpdateTime = SimulationClock.getSimCurrentTime();
        // Read zone data from JSON file and update with the zoneScore
        // todo: uncomment
        ZoneMap.updateZonesWithScores(new HashMap<>());

        //provide random locations to the cabs from the list of h3Indices
        CabPool.initialize(noOfCabs, cabSpeed);

        //entire pool & current pool for resources is empty
        boolean resourcesLeft = true;

        while (resourcesLeft && System.currentTimeMillis() < systemEndTime) {

            //Simulation time increment, start iteration
            SimulationClock.incrementSimulationTime();

            //Zone score update
            if (SimulationClock.getSimCurrentTime() - prevZoneScoreUpdateTime >= ZONE_SCORE_UPDATE_INTERVAL) {
                //hit python api and update the score
            }

            //revisit all Cab to check its availability from Cab pool
            CabPool.findAvailableCabs(); // creates a static list of available cabs which can be used by other modules

            //read the appropriate entries from the test data
            resourcesLeft = ResourcePool.updateCurrentPool();

            // run resource allocation component on cab pool and current resource pool

            //run the navigation component
        }

        //calculate the required metrics

        //dump all the data into json file
    }
}
