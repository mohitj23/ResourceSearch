import com.uic.cs581.utils.BasicCSVReader;

import java.io.IOException;
import java.text.ParseException;
import java.util.TimeZone;

public class App {

    public static void main(String args[]) throws IOException, ParseException {

//        System.out.println(TimeZone.getDefault());
        TimeZone.setDefault(TimeZone.getTimeZone("America/New_York"));
        System.out.println(TimeZone.getDefault());
        // Read the test data csv
        BasicCSVReader.getResourcesFromTestData("test.csv");

        // append h3Index to the test data

        // provide random locations to the cabs from the list of h3Indices

        //Initialize the simulation time entity

        //read the appropriate entries from the test data

        // run resource allocation component

        //run the navigation component

    }

}
