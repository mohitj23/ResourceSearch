import com.uic.cs581.model.Zone;
import com.uic.cs581.utils.BasicCSVReader;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

@Slf4j
public class App {

    public static void main(String args[]) throws IOException, ParseException {

        log.info(TimeZone.getDefault().toString());
        TimeZone.setDefault(TimeZone.getTimeZone("America/New_York"));
        log.info(TimeZone.getDefault().toString());

        // Read the test data csv
        BasicCSVReader.getResourcesFromTestData("test.csv");

        // Read zone data from JSON file
        HashMap<String,Zone> result =
                new ObjectMapper().readValue(
                        new File("./src/main/resources/manhattan_zones_lat_lon_3.json"),
                        new TypeReference<Map<String, Zone>>() {});

        // append h3Index to the test data

        // provide random locations to the cabs from the list of h3Indices

        //Initialize the simulation time entity

        //read the appropriate entries from the test data

        // run resource allocation component

        //run the navigation component

    }

}
