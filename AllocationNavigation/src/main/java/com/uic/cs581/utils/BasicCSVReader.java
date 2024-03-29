package com.uic.cs581.utils;

import com.uber.h3core.H3Core;
import com.uic.cs581.model.Resource;
import com.uic.cs581.model.ResourcePool;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.time.DateUtils;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
public class BasicCSVReader {
    private static final String CSV_FILE_PATH = "./";
    private static int resourceCount = 1;

    private static H3Core h3;

    public static final int RESOLUTION_LEVEL = 9;

    private static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

//    private static final long REQUEST_TIME_DIFFERENCE = 0; //600000= 10 minutes in milli seconds

//    public static final long EXPIRATION_TIME_MILLIS = 300000;     //600000= 10 minutes max life time of the resource

    public static long MIN_REQUEST_TIME = Long.MAX_VALUE;

    public static void readResourcesFromTestData(String fileName, long expirationTime, long requestTimeDifference) throws IOException, ParseException {

        log.debug("Working Directory = " +
                System.getProperty("user.dir"));

        //get a reference to the final list and add the resources to this list.

        List<Resource> temp = new ArrayList<>();
        h3 = H3Core.newInstance();
        try (
                Reader reader = Files.newBufferedReader(Paths.get(CSV_FILE_PATH + fileName), StandardCharsets.UTF_8);
                CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader()
                        .withIgnoreHeaderCase()
                        .withTrim())
        ) {
            for (CSVRecord csvRecord : csvParser) {
                // Accessing Values by Column Index
                log.debug("Record No - " + csvRecord.getRecordNumber());
                log.debug("---------------");
                log.debug(csvRecord.toString());

                long pickupTime = DateUtils.parseDate(csvRecord.get(0), TIME_FORMAT).getTime();
                long requestTime = pickupTime - requestTimeDifference;
                if (MIN_REQUEST_TIME > requestTime) {
                    MIN_REQUEST_TIME = requestTime;
                }

                Resource r = Resource.builder()
                        .resourceId(resourceCount++)
//                        .dropOffLat(csvRecord.get(10))
//                        .dropOffLong(csvRecord.get(9))
                        .dropOffTimeInMillis(DateUtils.parseDate(csvRecord.get(1), TIME_FORMAT).getTime())
                        .dropOffH3Index(csvRecord.get(3))
                        .expirationTimeLeftInMillis(expirationTime)
//                        .pickUpLat(csvRecord.get(6))
//                        .pickUpLong(csvRecord.get(5))
                        .pickUpH3Index(csvRecord.get(2))
                        .pickupTimeInMillis(pickupTime)
                        .requestTimeInMillis(requestTime).build();

                log.debug(r.toString());
                log.debug("---------------\n\n");
                temp.add(r);
            }
            temp.sort(Comparator.comparing(Resource::getRequestTimeInMillis));
            ResourcePool.getEntirePool().addAll(temp);
        }
    }
}