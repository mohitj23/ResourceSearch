package com.uic.cs581.model;

import lombok.*;

import java.util.List;

@Builder
@ToString
public class Cab {

    private int id;
    private int resourceId;
    private List<String> searchPaths;
    private String futurePath;
    private long nextAvailableTime;
    private long totalSearchTime;
    private String currentZone;
    private String targetZone;
    private Double speed = SPEED;

    private static final Double SPEED = 40.0;
}
