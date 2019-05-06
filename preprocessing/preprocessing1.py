#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Mon Apr 22 00:13:06 2019

@author: akshunjhingan
"""
import pandas as pd

def preprocess1():
    data1 = pd.read_csv("./yellow_tripdata_2016-05.csv")
    data1 = data1.drop(["VendorID","passenger_count","RatecodeID","store_and_fwd_flag","payment_type","fare_amount","mta_tax","tip_amount","trip_distance","tolls_amount","improvement_surcharge","total_amount","extra"],axis=1)
    data1.to_csv("yellow_tripdata_2016-May.csv", index = False)
    
preprocess1()