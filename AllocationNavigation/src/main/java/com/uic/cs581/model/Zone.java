package com.uic.cs581.model;

import lombok.Builder;

import java.util.List;

@Builder
public class Zone {
    private String h3Index;
    private List<List<String>> neighbours;
}
