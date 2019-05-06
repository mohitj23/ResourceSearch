#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Sat Apr 20 20:12:09 2019

@author: akshunjhingan
"""
import pandas as pd
import pickle
from sklearn import datasets, linear_model
from sklearn.metrics import mean_squared_error, r2_score
from sklearn.preprocessing import PolynomialFeatures

def run_ml2():
    data = pd.read_csv("./ml2_temp.csv")
    data = pd.get_dummies(data, columns=['pickup-zone','season'])
    Y = data['avg_trip_time'].values
    X = data.drop('avg_trip_time',axis=1).values

    ### For using RandomForest
    #rf = RandomForestRegressor(n_estimators = 100)
    #rf = RandomForestRegressor(n_estimators = 5)
    #rf.fit(X, Y);
    #pred = rf.predict(X)
    #print("Mean Square Error", mean_squared_error(Y, pred))
    
    #For using Polynomial Regression
    #polynomial_features= PolynomialFeatures(degree=2)
    #x_poly = polynomial_features.fit_transform(X)
    regr = linear_model.LinearRegression()
    #regr.fit(x_poly, Y)
    regr.fit(X, Y)
    pickle.dump(regr, open('ml2.sav', 'wb'))
    #pred= regr.predict(x_poly)
    pred= regr.predict(X)
    data = data.drop('avg_trip_time',axis = 1)
    data1 = pd.DataFrame(columns = data.columns.tolist())
    data1.to_csv("header.csv", sep = ',', index = False)
    print(mean_squared_error(Y, pred))
    #print(pred)
    
run_ml2()
