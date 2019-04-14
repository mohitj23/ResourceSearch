package com.uic.cs581.model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Zone {
    String h3Index;
    Double lat;
    Double long1;
    Double score;
    public List<List<String>> kRingNeighbors;
}
