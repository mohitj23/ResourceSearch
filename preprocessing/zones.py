#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Mon Apr 22 08:46:33 2019

@author: akshunjhingan
"""
import pandas as pd

def get_zones():
    data = pd.read_csv("./ml1_temp.csv")
    data = data.drop(['weekday', 'weekend','pickup_time_slot','season','count'],axis = 1)
    data.to_csv("zones.csv",index = False)

get_zones()