package com.uic.cs581.allocation;

import com.uber.h3core.H3Core;
import com.uic.cs581.model.Cab;
import com.uic.cs581.model.CabPool;
import com.uic.cs581.model.Resource;
import com.uic.cs581.model.ResourcePool;

import java.io.IOException;
import java.util.List;
import java.util.ListIterator;

public class ResourceAllocation {

    private static H3Core h3;

    public static void assignCabsToResources() throws IOException {

        h3= H3Core.newInstance();

        List<Resource> currentResourcePool= ResourcePool.getCurrentPool();
        List<Cab> availableCabs= CabPool.getAvailableCabs();

        ListIterator currResourcePoolItr = currentResourcePool.listIterator();

        // hit h3 Api for each resource and each cab

        // find the shortest distance for the current resource

        // update the resource and cab object IMMEDIATELY
        //else the cab will be considered again

        // calculate the next available time

        //TODO set future path to null in allocation
    }
}
