package com.uic.cs581.allocation;

import com.uic.cs581.model.Cab;
import com.uic.cs581.model.CabPool;
import com.uic.cs581.model.Resource;
import com.uic.cs581.model.ResourcePool;

import java.util.List;

public class ResourceAllocation {

    public static void assignCabsToResources(){
        List<Resource> currentResourcePool= ResourcePool.getCurrentPool();
        List<Cab> availableCabs= CabPool.getAvailableCabs();
    }
}
