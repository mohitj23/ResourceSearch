package com.uic.cs581.navigation;

import java.io.File;
import java.io.FileReader;

public class Navigation {

    public static String oneHop(String currZone)    {

        //TODO Get scores of neighbors from Uber-API: (API results stored in zones folder)

        String basePath = new File("").getAbsolutePath();

        try(FileReader reader = new FileReader(basePath+"/navigation/manhattan_zones_lat_lon_3.json"))  {

        } catch (Exception e)   {
            
        }

        //TODO pick  one neighbor non-deterministically

        //return h3-index of picked zone

        return "";
    }
}
