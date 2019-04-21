package com.uic.cs581.utils;

import com.uic.cs581.model.Cab;
import com.uic.cs581.model.CabPool;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Results {

    public static void avgSearchTimeOfAgents() {
        double avgSearchTime = CabPool.getEntireCabPool().stream()
                .reduce(0.0, (subtotal, element) -> subtotal + element.getTotalSearchTime(), Double::sum) / CabPool.getEntireCabPool().size();
        log.info("Average Search Time:\t " + avgSearchTime);
    }

    public static void avgIdleTimeOfAgents() {
        double avgIdleTime = CabPool.getEntireCabPool().stream()
                .reduce(0.0, (subtotal, element) -> subtotal + element.getTotalIdleTime(), Double::sum) / CabPool.getEntireCabPool().size();
        log.info("Average Idle Time:\t " + avgIdleTime);
    }
}
