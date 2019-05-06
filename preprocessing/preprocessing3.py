#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Mon Apr 22 00:29:07 2019

@author: akshunjhingan
"""
import datetime as dt
from h3 import h3
import json
from datetime import date
import time
import collections
import csv
import pandas as pd

def preprocess1(data1, json_filename):
    count_dict = collections.OrderedDict()
    h3_coordinates = read_json(json_filename)
    hours = get_hours()     
    count=0
    #### Preprocessing starts from here    
    for index, row in data1.iterrows():
        if(count == 20000):
            print(str(index),"items preprocessed")
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
        dropoff_h3 = h3.geo_to_h3(row['dropoff_latitude'],row['dropoff_longitude'],9)
        
        ###check is the co-ordinates are in Manhattan
        if(pickup_h3 not in h3_coordinates or dropoff_h3 not in h3_coordinates):
            count+=1
            continue

        weekday = 0
        weekend = 0
        
        type_day = now.weekday()
        if(type_day<5):
            weekday = 1
            weekend = 0

        else:
            weekday = 1
            weekend = 0


        ### Update Count Dictionary
        timeslot = label_race(row,hours)
        pickup_zone = pickup_h3
        date = now.strftime(('%Y-%m-%d'))
        
        if((timeslot,pickup_zone,date) in count_dict.keys()):
            (cnt,t_time,wkday,wkend,season) = count_dict.get((timeslot,pickup_zone,date))
            count_dict[(timeslot,pickup_zone,date)] = (cnt+1,t_time +diff,wkday,wkend,season)
        else:
            count_dict[(timeslot,pickup_zone,date)] = (1,diff,weekday,weekend, get_season(row)) 
        count=count+1
    #print(count)
    return count_dict

def write_dict_to_csv(dictionary):
    ml1_file = open("ml1_temp.csv","a")
    ml2_file = open("ml2_temp.csv","a")
    ml1 = csv.writer(ml1_file)
    ml2 = csv.writer(ml2_file)
    a = ["weekday","weekend","pickup-zone","season","pickup_time_slot","count"]
    b = ["weekday","weekend","pickup-zone","season","pickup_time_slot","avg_trip_time"]
    ml1.writerow(a)
    ml2.writerow(b)
    for key, value in dictionary.items():
        (timeslot,pickup_zone,date) = key
        (cnt,t_time,wkday,wkend,season) = value
        count_data = [wkday,wkend,pickup_zone,season,timeslot,cnt]
        avg_trip_data = [wkday,wkend,pickup_zone,season,timeslot,int(t_time/cnt)]
        ml1.writerow(count_data)
        ml2.writerow(avg_trip_data)
    ml1_file.close()
    ml2_file.close()
    
def read_json(filename):
    f = open(filename, "r")
    json_data = f.read()
    data_2 = json.dumps(json_data)
    h3_coordinates = json.loads(data_2)
    return h3_coordinates

def get_hours():
    start_time = '00:00'
    end_time = '23:59'
    slot_time = 30
    c = 1
    hours = {}
    time = dt.datetime.strptime(start_time, '%H:%M')
    end = dt.datetime.strptime(end_time, '%H:%M')
    while time <= end:
        hours[time.time()] = c
        time += dt.timedelta(minutes=slot_time)
        c+=1
    return hours

def label_race(row, hours):
    timeDetail = row['tpep_pickup_datetime'].split()[1]
    timeMinute = int(timeDetail.split(':')[1])
    convertTime = timeDetail.split(':')[0] + timeDetail.split(':')[1]
    time = ''
    if timeMinute > 30:
        timeMinute = 30
        convertTime = timeDetail.split(':')[0] + ':' + str(30)
        time = dt.datetime.strptime(convertTime, '%H:%M').time()
    else:
        timeMinute = 0
        convertTime = timeDetail.split(':')[0] + ':' + str(00)
        time = dt.datetime.strptime(convertTime, '%H:%M').time()
    return hours[time]

def get_season(row):
    dayDetail = dt.datetime.strptime(row['tpep_pickup_datetime'].split()[0], "%Y-%m-%d").date()
    Y = 2016
    seasons = [('winter', (date(Y, 1, 1), date(Y, 2, 29))),
               ('spring', (date(Y, 3, 1), date(Y, 5, 31))),
               ('summer', (date(Y, 6, 1), date(Y, 9, 30))),
               ('fall', (date(Y, 9, 1), date(Y, 11, 30))),
               ('winter', (date(Y, 12, 1), date(Y, 12, 31)))]
    return next(season for season, (start, end) in seasons
                if start <= dayDetail <= end)
def main():
    data = pd.read_csv("./yellow_tripdata_2016-May.csv")
    #data=data.sample(n=8000000, random_state=1)
    count_dict = preprocess1(data,"manhattan_zones_lat_lon_3.json")
    write_dict_to_csv(count_dict)
main()
