### StatisticsAPI

#### Features
StatisticsAPI allows you to obtain the following statistical data.

###### For a user 
- **Sum/Average/Distribution** of running distance of a user during **day/week/month**
- **Sum/Average/Distribution** of running duration of a user during **day/week/month**
- **Average/Distribution** of average speed of a user during **day/week/month**

###### For group/domain 
- **Sum/Average/Distribution** of total running distance in the group/domain during **day/week/month**
- **Sum/Average/Distribution** of total running duration in the group/domain during **day/week/month**
- **Average/Distribution** of average speed in the group/domain during **day/week/month**

#### Usage

For a starter, let's get the distributions of total running duration of week in the domain 1234. 

```java
	 // Get ready!
	boolean useLive = false; // Specifying whether you want to use live server or stagint server
    SenseStatisticsAPI statisticsAPI = new SenseStatisticsAPI(useLive);
    statisticsAPI.setSessionId(sessionId);
    
    // get the distribution of running distance within the damain with id 1234.
    int contextId = 1234
    String measurement = "distance"
    JSONArray statistics = statisticsAPI.getStatistics(SenseStatisticsContext.DOMAIN,
    											 	contextId,
    											 	AggregationType.DISTRIBUTION,
    											 	Period.WEEK,
    											 	measurement,
    											 	);
```
`getStatistics(....)` method has the following composary parameters:

##### Context:
*Context* allows you to specify the desired scope from which you want to obtain the statistics. Possible entries are **user**, **group** and **domain**. 

##### ContextID:
*ContextId* allows you to specify which individual *context* that you want to obtain the statistics from. Possible entries can be obtained by ```getContextIds(...)```.

##### Aggregation:
*Aggregation* allows you to specify the desired type of the aggregation that should be performed.
Possible entries are **sum**, **distribution** and **average**.

##### Period: 
*Period* allows you to specify the desired interval over which the aggregation of data should be performed.
Possible entries are **day**, **week**, **month**.

##### Measurement:
*Measurement* allows you to specify the type of measurement for which teh aggregation of data should be performed. The available measurement can be obtained by ```getAvailableMeasurementType(...)```.


#### More Examples

There are a number of available convinient functions to get the parameters dynamically. In actual use cases, you can use `StatisticsAPI` as follows:

```java
	 // Get ready to get the average running speed per week of this user over the last 10 weeks!
	boolean useLive = false; 
    SenseStatisticsAPI statisticsAPI = new SenseStatisticsAPI(useLive);
    statisticsAPI.setSessionId(sessionId);
    
    //prepare query
    SenseStatisticsQuery query = new SenseStatisticsQuery().setLimit(10);

    // get available Ids in user context
    JSONArray arrayOfContextIds = statisticsAPI.getContextIds(SenseStatisticsContext.USER);
    
    // get available statistics type
    JSONArray arrayOfMeasurementType = statisticsAPI.getAvailableMeasurementType(SenseStatisticsContext.USER, arrayOfContextIds.getInt(0));
    
    // get statistics
    JSONArray statistics = statisticsAPI.getStatistics(SenseStatisticsContext.USER,
    											 	arrayOfContextIds.getInt(0), 
    											 	SenseStatisticsAggregationType.AVERAGE,
    											 	SenseStatisticsPeriod.WEEK
    											 	arrayOfStatisticsType.getString(0), //let's assume the first element is "speed"
    											  	query);
```