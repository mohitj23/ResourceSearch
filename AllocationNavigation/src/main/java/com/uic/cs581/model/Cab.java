package com.uic.cs581.model;

import lombok.*;

import java.util.List;

@Builder
@ToString
@Getter
@Setter
public class Cab implements CommonInterface {

    private int id;
    private int resourceId;
    private List<List<String>> searchPaths;
    private List<String> futurePath;
    private long nextAvailableTime; //based on simulation time
    private long totalSearchTime; // based on simulation time
    private long totalIdleTime;
    private String currentZone;
    private String targetZone;
    private int speed;

//    private static final Double SPEED = 40.0;
}
