# WeatherForecast
# 1. About


Weather forecast is a web application for getting the weather forecast of a city for next three days. A detailed description of API is as follow:

## 1. Get Weather Forecast

-  This API will allow user to get the weather forecast data for a city

- REST API URL & sample response JSON:-
GET [http://localhost:808](http://localhost:808)_0 _/data/{cityName}_
```
[
{
"date": "2018- 12 - 15",
"dayTemperature": -1.7,
"nightTemperature": - 1.9,
"averageTemperature": -1.8,
"pressure": 1036.
},
{
"date": "2018- 12 - 14",
"dayTemperature": -3.3,
"nightTemperature": -2.9,
"averageTemperature": -3.1,
"pressure": 1036.
},
{
"date": "2018- 12 - 13",
"dayTemperature": 0.4,
"nightTemperature": -2.6,
"averageTemperature": -1.1,
"pressure": 1033.
}
]
```

**Validations:**
- City Name is mandatory.

# 2. Libraries used:

- Java 8
- Spring-Boot 2.1.
- Spring-Cloud Feign API
- Lombok
- Swagger

# 3. Approach:


OpenWeatherAPI provides forecast for 40 intervals of 3 hours each spanning over 5 days.


- Step1: As a first step, intervals are filtered to 24 only (from 40) which is required to calculate
three days average. This helps in dealing with smaller and required data set only.

- Step2: In this step, 24 intervals (of 3 hours each) are grouped for day and night intervals.
This will further funnel down the data into 6 groups. By doing so, forecast data for a Date
can be easily grouped for day and night separately.

- Step3: After Step2, there is a collection of Day and night weather forecast grouped by Date.
Now, these can be merged to create a final weather forecast for each day.

Above approach works on the principle of funnelling where, relevant data is filtered,
grouped and merged at each step. This will help in maintaining the code in future and make
it more readable.

Also, number of parameters are made configurable through properties file, which leads to
extensible design. These parameters are:

- Temperature unit
- OpenWeather API URL
- API Authentication Key

# 3. How to build & run the application


JDK 8 and maven should be installed on your machine. For building the application please follow the instructions as below:

- Run _‘mvn clean install’_ for building the application
- After successful installation, it will create a target folder.
- Run the command _"java -jar weatherforecast-0.0.1-SNAPSHOT.jar"_ which will start the application.
- Open the URL: [http://localhost:8080/swagger-ui.html#/](http://localhost:8080/swagger-ui.html#/) Navigate to the _‘getWeatherForecast’_ API in _‘Forecast Controller’_. Execute the request as mentioned in ‘ **Get Weather Forecast** ’ and click on _'Try it out!_ '
Above request will get weather forecast data.


# 4. Areas of improvement


Below is some of the improvement area across the application.

- Application runs well in UTC time zone. However, special handling is required if different
    time zones needs to be considered.
- Integration tests should be added.
- Exception handling can be improved further by creating more custom exception classes.
- Configuration file - _application.yml_ can be made environment specific.
