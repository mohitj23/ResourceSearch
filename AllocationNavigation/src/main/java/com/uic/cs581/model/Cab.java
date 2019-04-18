package com.uic.cs581.model;

import lombok.*;

import java.util.List;

@Builder
@ToString
@Getter
@Setter
public class Cab {

    private int id;
    private int resourceId;
    private List<List<String>> searchPaths;
    private List<String> futurePath;
    private long nextAvailableTime; //based on simulation time
    private long totalSearchTime; // based on simulation time
    private String currentZone;
    private String targetZone;
    private Double speed = SPEED;

    private static final Double SPEED = 40.0;
}
