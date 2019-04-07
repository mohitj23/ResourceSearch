package com.uic.cs581.model;

import lombok.*;

import java.util.List;

@Builder
public class Cab {

    private int id;
    private int resourceId;
    private List<String> searchPaths;
    private String futurePath;
    private long nextAvailableTime;
    private long totalSearchTime;
}
