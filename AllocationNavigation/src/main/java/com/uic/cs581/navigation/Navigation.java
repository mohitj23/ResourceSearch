package com.uic.cs581.navigation;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Navigation {

    public static void main(String[] args) throws IOException {
        oneHop("curr");
    }

    public static String oneHop(String currZone) throws IOException {

        //TODO Get scores of neighbors from Uber-API: (API results stored in zones folder)

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

        //TODO pick  one neighbor non-deterministically

        //return h3-index of picked zone

        return "";
    }
}
