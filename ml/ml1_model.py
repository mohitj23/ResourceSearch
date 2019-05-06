#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
@author: akshunjhingan
"""
import pandas as pd
from sklearn import datasets, linear_model
from sklearn.metrics import mean_squared_error, r2_score
from sklearn.preprocessing import PolynomialFeatures
from sklearn.ensemble import RandomForestRegressor

import numpy as np
from sklearn.ensemble import RandomForestRegressor
from sklearn.datasets import make_regression
import pickle

def run_ml1():
    data = pd.read_csv("./ml1_temp.csv")
    #data = data[:50000]
    #print(data.head())
    data = pd.get_dummies(data, columns=['pickup-zone','season'])
    Y = data['count'].values
    X = data.drop('count',axis=1).values
    
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
    regr.fit(X, Y)
    #regr.fit(x_poly, Y)
    pickle.dump(regr, open('ml1.sav', 'wb'))
    #pred= regr.predict(x_poly)
    pred= regr.predict(X)
    #pred = [abs(x) for x in pred]
    print(mean_squared_error(Y, pred))
    #print(pred)
    
run_ml1()
