#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Sun Apr 21 23:55:51 2019

@author: akshunjhingan
"""
import pandas as pd
import datetime as dt
from h3 import h3
import json
import csv

def preprocess2(data1, json_filename):
    prep_file = open("preprocessed1.csv","a")
    h3_coordinates = read_json(json_filename)
    count=0
    prep = csv.writer(prep_file)
    final_data = []
    prep.writerow(['tpep_pickup_datetime','tpep_dropoff_datetime','pickup-zone','dropoff-zone'])
    #### Preprocessing starts from here    
    for index, row in data1.iterrows():
        if(count == 10000):
            print(str(index) ," items preprocessed")
            count = 0
            
    ### check if the latitude and longitude is correct
        if (row['pickup_latitude'] == 0) or (row['pickup_longitude'] == 0) or (row['dropoff_latitude'] == 0) or (row['dropoff_longitude'] == 0):
            count = count+1
            continue
        
        
        ### get the date and add it
        now = dt.datetime.strptime(row['tpep_pickup_datetime'], '%Y-%m-%d %H:%M:%S')
        then = dt.datetime.strptime(row['tpep_dropoff_datetime'], '%Y-%m-%d %H:%M:%S')
        diff = then - now
        diff = diff.total_seconds()
        if diff <= 60 or diff>10800:
            count = count+1
            continue
                
        ### get the zone co-ordinates
        pickup_h3 = h3.geo_to_h3(row['pickup_latitude'],row['pickup_longitude'],9)
        #print(h3.geo_to_h3(row['pickup_latitude'],row['pickup_longitude'],9))
        dropoff_h3 = h3.geo_to_h3(row['dropoff_latitude'],row['dropoff_longitude'],9)
        
        ###check is the co-ordinates are in Manhattan
        if(pickup_h3 not in h3_coordinates or dropoff_h3 not in h3_coordinates):
            count+=1
            continue
        final_data.append([row['tpep_pickup_datetime'],row['tpep_dropoff_datetime'],pickup_h3, dropoff_h3])
    prep.writerows(final_data)
    prep_file.close()
    
def read_json(filename):
    f = open(filename, "r")
    json_data = f.read()
    data_2 = json.dumps(json_data)
    h3_coordinates = json.loads(data_2)
    return h3_coordinates

def main():
    data = pd.read_csv("./yellow_tripdata_2016-May.csv")
    preprocess2(data,"manhattan_zones_lat_lon_3.json")
    
main()