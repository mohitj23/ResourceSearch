package com.uic.cs581.model;

import lombok.Builder;

@Builder
public class Resource {
    private int resourceId;
    private String pickUpH3Index;
    private String dropOffH3Index;
    private String pickUpLat;
    private String pickUpLong;
    private String dropOffLat;
    private String dropOffLong;
    private int cabId;
}
