
import com.uber.h3core.H3Core;
import com.uber.h3core.LengthUnit;
import com.uic.cs581.allocation.ResourceAllocation;
import com.uic.cs581.model.CabPool;
import com.uic.cs581.model.ResourcePool;
import com.uic.cs581.model.SimulationClock;
import com.uic.cs581.model.ZoneMap;
import com.uic.cs581.navigation.Navigation;
import com.uic.cs581.utils.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.text.ParseException;
import java.util.TimeZone;

@Slf4j
public class App {
    private static final String FILE_PATH_FOR_JSON_WRITE = "./";

    public static void main(String[] args) throws IOException, ParseException {

//        SendHttpRequest.getRequest();

        final double ZONE_DIAMETER_MILES = 2 * H3Core.newInstance()
                .edgeLength(BasicCSVReader.RESOLUTION_LEVEL, LengthUnit.km); //Resolution 9 edge length in miles

        if (args.length != 9) {
            log.error("Please provide the required command line parameters");
            System.exit(1);
        }
        int noOfCabs = Integer.parseInt(args[0]);
        int cabSpeed = Integer.parseInt(args[1]);     //in kph
        // TODO String csvFileName = args[2];
        long runningTimeInMins = Long.parseLong(args[2]);
        long expirationTimeInMillis = Long.parseLong(args[3]);
        long requestDifferenceTimeInMillis = Long.parseLong(args[4]);
        int noOfHopsPreCalculate = Integer.parseInt(args[5]);
        boolean readJsonScores = Boolean.parseBoolean(args[6]);
        boolean dataExhaustive = Boolean.parseBoolean(args[7]);
        boolean randomScores = Boolean.parseBoolean(args[8]);

        int simTimeIncrementsInMillis = (int) Math.ceil((ZONE_DIAMETER_MILES / cabSpeed) * 60 * 60 * 1000);

        log.debug("Default Timezone:" + TimeZone.getDefault().toString());
        TimeZone.setDefault(TimeZone.getTimeZone("America/New_York"));
        log.debug("Default Timezone changed:" + TimeZone.getDefault().toString());

        // Read the test data csv, get the resource list from ResourcePool
        BasicCSVReader.readResourcesFromTestData("preprocessed.csv", expirationTimeInMillis, requestDifferenceTimeInMillis);

        Havershine.loadDistance();


        //Initialize the simulation time entity
        SimulationClock.initializeSimulationClock(BasicCSVReader.MIN_REQUEST_TIME, simTimeIncrementsInMillis);

        //Hit Python App and get the zone score map
//        long prevZoneScoreUpdateTime = SimulationClock.getSimCurrentTime();

        // Read zone data from JSON file and update with the zoneScore
        // todo: uncomment
        ZoneMap.updateZonesWithScores(SendHttpRequest.getRequest(SimulationClock.getSimCurrentTime(), readJsonScores), randomScores);
        int prevUpdateOnIteration = SimulationClock.getSimIterations();
        //provide random locations to the cabs from the list of h3Indices
        CabPool.initialize(noOfCabs, cabSpeed);

        //entire pool & current pool for resources is empty
        boolean resourcesLeft = true;
        Results.simulationStarted();
        long systemEndTime = System.currentTimeMillis() + runningTimeInMins * 60 * 1000;
        while (resourcesLeft && (dataExhaustive || System.currentTimeMillis() < systemEndTime)) {

            //Simulation time increment, start iteration
            SimulationClock.incrementSimulationTime();

            //Zone score update
            if (!randomScores && (SimulationClock.getSimIterations() - prevUpdateOnIteration) > 5 &&
                    ZoneMap.checkIfNewZoneScoreIsRequried(SimulationClock.getSimCurrentTime())) {
                //hit python api and update the score
                //1215,1230,1 scenario handled.
                ZoneMap.updateZonesWithScores(SendHttpRequest.getRequest(SimulationClock.getSimCurrentTime(), readJsonScores), randomScores);
                prevUpdateOnIteration = SimulationClock.getSimIterations();
            }

            //revisit all Cab to check its availability from Cab pool
            CabPool.findAvailableCabs(); // creates a static list of available cabs which can be used by other modules

            //read the appropriate entries from the test data
            resourcesLeft = ResourcePool.updateCurrentPool();

            // run resource allocation component on cab pool and current resource pool
            ResourceAllocation.assignCabsToResources();

            //run the navigation component
            Navigation.navigate(noOfHopsPreCalculate);
            Results.runTimeTillNow();
        }
        Results.simulationCompleted();

        //calculate the required metrics
        Results.avgSearchTimeOfAgents();
        Results.avgIdleTimeOfAgents();
        Results.percentageExpiredRsources();
        Results.percentageAssignedResources();
        Results.currentPoolResources();
        Results.totalResourcesConsidered();

        //dump all the data into json file
        JsonUtility.writeToFile(FILE_PATH_FOR_JSON_WRITE + "Expired_resources.json", ResourcePool.getExpiredPool());
        JsonUtility.writeToFile(FILE_PATH_FOR_JSON_WRITE + "Assigned_resources.json", ResourcePool.getAssignedPool());
        JsonUtility.writeToFile(FILE_PATH_FOR_JSON_WRITE + "Cab_pool.json", CabPool.getEntireCabPool());
//        JsonUtility.writeToFile(FILE_PATH_FOR_JSON_WRITE + "Resources_pool_left.json", ResourcePool.getEntirePool());
    }
}
