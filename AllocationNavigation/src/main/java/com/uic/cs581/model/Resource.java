package com.uic.cs581.model;

import lombok.Builder;
import lombok.ToString;

@Builder
@ToString
public class Resource {
    private int resourceId;
    private String pickUpH3Index;           //provided by the Uber H3 Api
    private String dropOffH3Index;          //provided by the Uber H3 Api
    private String pickUpLat;
    private String pickUpLong;
    private String dropOffLat;
    private String dropOffLong;
    private int cabId;
    private long expirationTimeLeftInMillis;
    private long pickupSimTimeInMillis;     // based on the simulation clock
    private long dropOffSimTimeInMillis;    // based on the simulation clock
    private long requestSimTimeInMillis;    // based on the simulation clock
    private long pickupTimeInMillis;        // provided in the data at runtime
    private long dropOffTimeInMillis;       // provided in the data at runtime

}
