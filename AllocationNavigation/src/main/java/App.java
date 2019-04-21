import com.uber.h3core.H3Core;
import com.uber.h3core.LengthUnit;
import com.uic.cs581.allocation.ResourceAllocation;
import com.uic.cs581.model.*;
import com.uic.cs581.navigation.Navigation;
import com.uic.cs581.utils.BasicCSVReader;
import com.uic.cs581.utils.JsonUtility;
import com.uic.cs581.utils.Results;
import com.uic.cs581.utils.SendHttpRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.TimeZone;

@Slf4j
public class App {
    private static final long ZONE_SCORE_UPDATE_INTERVAL = 30000;
    private static final String FILE_PATH_FOR_JSON_WRITE = "./src/main/resources/";

    public static void main(String[] args) throws IOException, ParseException {

        SendHttpRequest.getRequest();

        final double ZONE_DIAMETER_MILES = H3Core.newInstance()
                .edgeLength(BasicCSVReader.RESOLUTION_LEVEL, LengthUnit.km); //Resolution 9 edge length in miles

        if (args.length != 3) {
            log.error("Please provide the required command line parameters");
            System.exit(1);
        }
        int noOfCabs = Integer.parseInt(args[0]);
        int cabSpeed = Integer.parseInt(args[1]);     //in kph
        // TODO String csvFileName = args[2];
        long runningTimeInMins = Long.parseLong(args[2]);
        long systemEndTime = System.currentTimeMillis() + runningTimeInMins * 60 * 100;
        int simTimeIncrementsInMillis = (int) Math.ceil((ZONE_DIAMETER_MILES / cabSpeed) * 60 * 100);

        log.debug("Default Timezone:" + TimeZone.getDefault().toString());
        TimeZone.setDefault(TimeZone.getTimeZone("America/New_York"));
        log.debug("Default Timezone changed:" + TimeZone.getDefault().toString());

        // Read the test data csv, get the resource list from ResourcePool
        BasicCSVReader.readResourcesFromTestData("preprocessed.csv");

        //Initialize the simulation time entity
        SimulationClock.initializeSimulationClock(BasicCSVReader.MIN_REQUEST_TIME, simTimeIncrementsInMillis);

        //Hit Python App and get the zone score map
        long prevZoneScoreUpdateTime = SimulationClock.getSimCurrentTime();
        // Read zone data from JSON file and update with the zoneScore
        // todo: uncomment
//        ZoneMap.updateZonesWithScores(new HashMap<>());

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
                //TODO 12,1230,1 scenario to be handled.
            }

            //revisit all Cab to check its availability from Cab pool
            CabPool.findAvailableCabs(); // creates a static list of available cabs which can be used by other modules

            //read the appropriate entries from the test data
            resourcesLeft = ResourcePool.updateCurrentPool();

            // run resource allocation component on cab pool and current resource pool
            ResourceAllocation.assignCabsToResources();

            //run the navigation component
            Navigation.navigate();
        }

        //calculate the required metrics
        Results.avgSearchTimeOfAgents();
        Results.avgIdleTimeOfAgents();

        //dump all the data into json file
        JsonUtility.writeToFile(FILE_PATH_FOR_JSON_WRITE + "Expired_resources.json", ResourcePool.getExpiredPool());
        JsonUtility.writeToFile(FILE_PATH_FOR_JSON_WRITE + "Assigned_resources.json", ResourcePool.getAssignedPool());
        JsonUtility.writeToFile(FILE_PATH_FOR_JSON_WRITE + "Cab_pool.json", CabPool.getEntireCabPool());
    }
}
