package com.uic.cs581.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Builder
@ToString
@Getter
@Setter
public class Cab implements CommonInterface {

    private int id;
    private int resourceId;
    private int resourcesPickedUp;
    private List<List<String>> searchPaths;
    private List<String> futurePath;
    private long nextAvailableTime; //based on simulation time
    private long totalSearchTime; // based on simulation time
    private long totalIdleTime;
    private long totalTimeToResFromCurZone;
    private long totalTimeToDropOff;
    private String currentZone;
    //    private String targetZone;
    private int speed;

}
