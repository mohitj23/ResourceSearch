from flask import Flask, request, jsonify
import pickle
import datetime
from datetime import date
import pandas
app = Flask(__name__)
import json
import csv
import math
import pytz
import random

import os, time
from sklearn.preprocessing import PolynomialFeatures


def get_daytype(now):
    if now < 5:
        return 0, 1
    else:
        return 1, 0


def get_season(now):
    Y = 2016
    seasons = [('winter', (date(Y, 1, 1), date(Y, 2, 29))),
               ('spring', (date(Y, 3, 1), date(Y, 5, 31))),
               ('summer', (date(Y, 6, 1), date(Y, 6, 30)))]

    now = now.replace(year=Y)
    return next(season for season, (start, end) in seasons
                if start <= now <= end)


def get_timeslot(now):
    start_time = '00:00'
    end_time = '23:59'
    slot_time = 30
    c = 1
    hours = {}
    time = datetime.datetime.strptime(start_time, '%H:%M')
    end = datetime.datetime.strptime(end_time, '%H:%M')
    while time <= end:
        hours[time.time()] = c
        time += datetime.timedelta(minutes=slot_time)
        c += 1

    timeMinute = now.minute
    timeHour = now.hour
    convertTime = str(timeMinute) + str(timeHour)
    time = ''
    if timeMinute > 30:
        timeMinute = 30
        convertTime = str(timeHour) + ':' + str(30)
        time = datetime.datetime.strptime(convertTime, '%H:%M').time()
    else:
        timeMinute = 0
        convertTime = str(timeHour) + ':' + str(00)
        time = datetime.datetime.strptime(convertTime, '%H:%M').time()
    return hours[time]


@app.route('/', methods=['GET'])
def get():
    date_time_str = int(request.args.get('times'))/1000
    date_time_obj = datetime.datetime.fromtimestamp(int(date_time_str))
    # tz_src = pytz.timezone("America/Chicago")
    # tz_dest = pytz.timezone("America/New_York")
    # d_aware = tz_src.localize(date_time_obj)
    # date_time_obj = d_aware.astimezone(tz_dest)
    fileObject = open('ml2.sav', 'rb')
    model = pickle.load(fileObject)
    df_header = pandas.read_csv('header.csv')
    h3_Contents = {}
    zones_h3_Contents = {}
    with open('zones.csv', 'r') as f:
        zones_h3_Contents = {row[0]:0 for row in csv.reader(f)}
    del zones_h3_Contents['pickup-zone']

    zone_Scores = {}
    h3_Content = {}
    with open('manhattan_zones_lat_lon_3.json') as json_file:
        h3_Contents = json.load(json_file)
        for k in h3_Contents.keys():
            if k in zones_h3_Contents:
                h3_Content['pickup-zone_'+k] = [0]
        seasons = ['winter', 'spring', 'summer']
        d = date_time_obj.date()
        t = date_time_obj.time()
        # season = get_season(d)
        timeslot = get_timeslot(t)
        weekend, weekday = get_daytype(date_time_obj.weekday())
        seasons_dict = {}
        seasons_dict['season_spring'] = [1]
        # for s in seasons:
        #     if s == season:
        #         seasons_dict['season_'+s] = [1]

        weekend_dict = {'weekend': [weekend], 'weekday': [weekday]}

        timeslot_dict = {'pickup_time_slot': [timeslot]}
        h3_Content.update(weekend_dict)
        h3_Content.update(seasons_dict)
        h3_Content.update(timeslot_dict)

        df = pandas.DataFrame.from_dict(h3_Content)
        df_formator = df_header

        for k in df_formator.keys():
            df_formator[k] = df[k]
        del df

        mean_S = 0
        # polynomial_features = PolynomialFeatures(degree=2)
        scores = {}
        for zone in h3_Contents.keys():
            if zone in zones_h3_Contents:
                df3 = df_formator
                df3['pickup-zone_'+zone] = 1
                # x_poly = polynomial_features.fit_transform([df3.iloc[0]])
                time_p = abs(math.ceil(model.predict(df3.values)[0]))
                scores1 = [10,20,30,40,50]
                if time_p > 30:
                    time_p = random.choice(scores1)
                mean_S += time_p
                scores[zone] = time_p
                df3['pickup-zone_'+zone] = 0
                del df3
            else:
                scores[zone] = 0

        mean_S = mean_S/len(scores)
        fileObject = open('ml1.sav', 'rb')
        model2 = pickle.load(fileObject)
        # polynomial_features_model2 = PolynomialFeatures(degree=2)
        cumm_m2_scores = {}
        for zone in h3_Contents.keys():
            if zone in zones_h3_Contents:
                df3 = df_formator
                df3['pickup-zone_' + zone] = 1
                # x_poly = polynomial_features_model2.fit_transform([df3.iloc[0]])
                count_p = abs(math.ceil(model2.predict(df3.values)[0]))
                if count_p > 100:
                    count_p = 30
                cumm_m2_scores[zone] = int(count_p/2) + int(scores[zone]/mean_S)
                df3['pickup-zone_' + zone] = 0
                del df3
            else:
                cumm_m2_scores[zone] = abs(0 + scores[zone])

    return jsonify(cumm_m2_scores)


if __name__ == '__main__':
    os.environ['TZ'] = 'America/New_York'
    time.tzset()
    app.run(debug=True)