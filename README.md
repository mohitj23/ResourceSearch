# ResourceSearch

### Prerequisites


```
python 3.7
pip 19.0.3
Flask
pandas
scikit-learn
numpy
Apache Maven 3
```



### Installing

Install pyton 3.7  as you would normally install a contributed python module using this link https://realpython.com/installing-python/

pip 19.0.3 considering to upgrade pip by using this command on your terminal python -m pip install --upgrade pip

Flask to install use the command pip install flask. The version we used is 1.0.2 follow tutorial http://flask.pocoo.org/docs/1.0/tutorial/factory/ if needed. When system is running it shows this as output 

* Serving Flask app "flaskr"
* Environment: development
* Debug mode: on
* Running on http://127.0.0.1:5000/ (Press CTRL+C to quit)
* Restarting with stat
* Debugger is active!
* Debugger PIN: 855-212-761

numpy to install use the command pip install numpy. The version we used is 1.15.4

pandas to install use the command pip install pandas. The version we used is 0.24.2

scikit-learn to install use the command pip install pandas. The version we used is 0.20.1

### Setup the Environment

First, we need to build the jar from the source code.
Assuming Apache Maven 3 with Java 8 is installed on the system, open the AllocationNavigation folder and run the following command from the terminal:
 
 `mvn clean package`

Now we need to transfer this jar to AWS EC2 instance either using WinSCP (for Windows) or scp from Ubuntu / OS X.
We also need to copy the required resource files to the same location. The files required are:

- havershine.json : Contains precomputed distance between all H3 zones inside Manhattan. This speeds up the simulation considerably.
- manhattan_zones_lat_lon_3.json: The Zone information required in the form of a map (key, value pairs).
- preprocessed.csv: The preprocessed CSV which we will provide to the simulation.

### Run the JAR

To execute the JAR, we run the following command:

`nohup java -Xmx40G -jar resource-search-1.0-SNAPSHOT-jar-with-dependencies.jar 1000 25 5 600000 0 5 false false false &`

- nohup - since we want our process to be detached from the current shell session
- -Xmx40G - We make 40 GB of RAM available for the JVM to execute this jar.

We provide 9 commandline arguments:

1. Number of cabs to consider.
2. Speed of the cab in kph (kilometer per hour)
3. Number minutes to run the simulation
4. The expiration time for each resource in minutes
5. Number of minutes before the pickup time, which would be the request time.
6. The value of k, used for navigation. We calculate k hops in advance but navigate only 1 hop i.e. 1 zone in each simulation iteration for navigation.
7. Boolean flag for reading zone score from the file. Used while doing dry runs. It should be set to false.
8. Boolean flag to consider all the records of the CSV. If set to true the parameter 3 is no longer considered.
9. Boolean flag to generate random scores for the zone. Used while doing dry runs. It should be set to false.


## Built With

* [Flask](http://flask.pocoo.org/docs/1.0/) - The web framework used
* [Maven](https://maven.apache.org/) - Dependency Management

### Authors
- [Amrish Jhaveri](https://github.com/AmrishJhaveri)
- [Akshun Jhingan](https://github.com/AkshunJhingan)
- [Mohitkumar Paritosh Ghia](https://github.com/mohitj23/)
- [Chinmay Gangal](https://github.com/chinmay2312)
