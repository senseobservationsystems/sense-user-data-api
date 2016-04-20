# sense-user-data-api

## Features

### SenseUserDataAPI
 - Get a user's data as the user himself 
 - Get multiple users' data as a domain manager
 - Insert/Update a user's data as the user himself
 - Insert/Update a user's data as a domain manager (No bulk insert/update)
 - Delete a user's data as the user himself
 - Delete a user's data as a domain manager (No bulk delete)

### SenseStatisticsAPI
StatisticsAPI allows you to obtain the following statistical data.

###### For a user 
- **Sum/Average** of distance of a user during **week/month**
- **Sum/Average** of duration of a user during **week/month**
- **Average** of average speed of a user during **week/month**
- **Sum** of number of days where a user ran during **week/month**

###### For group/domain 
- **Average/Distribution** of total distance in the group/domain during **week/month**
- **Average/Distribution** of total duration in the group/domain during **week/month**
- **Average/Distribution** of average speed in the group/domain during **week/month**
- **Average/Distribution** of number of days where users in the group/domain ran during **week/month**  

## Dependencies
sense-user-data-api makes use of third party libraries. You can import them using gradle.


- [square/okhttp](https://github.com/square/okhttp)

####For Testing
If you are not developing this module, you don't have to import this.

- [skyscreamer/JSONassert](https://github.com/skyscreamer/JSONassert)
 

## Recipe
These are the most obvious use case of the API. For more detailed description of each class/method, please refer to the inline documentation of each method.

### SenseUserDataAPI
```java
	 // Get ready!
	boolean useLive = false; // Specifying whether you want to use live server or stagint server 
    SenseUserDataAPI userDataAPI = new SenseUserDataAPI(useLive);
    userDataAPI.setSessionId(sessionId);
    
    // prepare JSONObject contains the user data for this user.
    // There is no format checking under "user_data" in this module or in the backend, so you can add any thing you like.
    // For now, we are creating a json object structured as:
    /** 
    	{
			"first_name": "Frank",
			"last_name": "Underwood",
 			"address": "1609 Far St. NW, Washington, D.C., 20036"
    	}
    **/ 
    JSONObject innerUserData = new JSONObject();
    userData.put("first_name", "Frank");
    userData.put("last_name", "Underwood");
    userData.put("address", "1609 Far St. NW, Washington, D.C., 20036");

	 // Sending PUT request with the user data
    userDataAPI.putUserData(userId, userData);
    
    // Get user data for this user
    JSONObject userData = userDataAPI.getUsersData(userId);
```

### SenseStatisticsAPI
For a starter, let's get the distributions of total duration of week in the domain 1234. 

```java
	 // Get ready!
	boolean useLive = false; // Specifying whether you want to use live server or stagint server
    SenseStatisticsAPI statisticsAPI = new SenseStatisticsAPI(useLive);
    statisticsAPI.setSessionId(sessionId);
    
    // get the distribution of running distance within the damain with id 1234.
    int contextId = 1234
    String measurement = "totalDistance"
    JSONArray statistics = statisticsAPI.getStatistics(SenseStatisticsContext.DOMAIN,
    											 	contextId,
    											 	AggregationType.DISTRIBUTION,
    											 	Period.WEEK,
    											 	measurement
    											 	);
```
`getStatistics(....)` method has the following composary parameters:

##### Context:
*Context* allows you to specify the desired scope from which you want to obtain the statistics. Use enum `SenseStatisticsContext`. 

##### ContextID:
*ContextId* allows you to specify which individual *context* that you want to obtain the statistics from. Possible entries can be obtained by ```getContextIds(...)```.

##### Aggregation:
*Aggregation* allows you to specify the desired type of the aggregation that should be performed.
Use enum `AggreagationType`

##### Period: 
*Period* allows you to specify the desired interval over which the aggregation of data should be performed. Use enum `Period`.

##### Measurement:
*Measurement* allows you to specify the type of measurement for which teh aggregation of data should be performed. The available measurement can be obtained by ```getAvailableMeasurementType(...)```.

#### More About Statistics API

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
    JSONArray arrayOfMeasurementTypes = statisticsAPI.getAvailableMeasurementType(SenseStatisticsContext.USER, arrayOfContextIds.getInt(0));
    
    // get statistics
    JSONArray statistics = statisticsAPI.getStatistics(SenseStatisticsContext.USER,
    											 	arrayOfContextIds.getInt(0), 
    											 	AggregationType.AVERAGE,
    											 	Period.WEEK
    											 	arrayOfMeasurementTypes.getString(0), //let's assume the first element is "averageSpeed"
    											  	query);
```

## Updating User Data

putUserData will update user data or create a new one if there is no data for such user yet. Update will only overwrite particular field in the existing user data. If the new field value is null, then the field will be removed from the user data.

| Parameter | Description        | Type   | Example                                                                                                          |
|-----------|--------------------|--------|------------------------------------------------------------------------------------------------------------------|
| user_id   | the id of user     | int    | 1                                                                                                                |
| new_data  | the new user data  | object | {"first name": "Bob"} <br> {"place of birth": "Bandung", "date of birth": "Apr 1 1980"} <br> {"last name": null} |

The update is not returning any data.

#### Example

##### Example 1 - Update non-exists user data

<table>
  <tbody>
    <tr>
      <td> Initial Data </td>
      <td>
        <table>
          <tbody>
            <tr>
              <th> user_id </th>
              <th> user_data </th>
            </tr>
            <tr>
              <td> 1 </td>
              <td> {"first name": "Bob", "last_name": "Marley"} </td>
            </tr>
          </tbody>
        </table>
      </td>
    </tr>
    <tr>
      <td> Input Parameter </td>
      <td>
        <table>
          <tbody>
            <tr>
              <td> user_id </td>
              <td> 2 </td>
            </tr>
            <tr>
              <td> new_data </td>
              <td> {"first name": "Charlie", "last_name": "Sheen"} </td>
            </tr>
          </tbody>
        </table>      
      </td>
    </tr>
    <tr>
      <td> Final Data </td>
      <td>
        <table>
          <tbody>
v            <tr>
              <th> user_id </th>
              <th> user_data </th>
            </tr>
            <tr>
              <td> 1 </td>
              <td> {"first name": "Bob", "last_name": "Marley"} </td>
            </tr>
            <tr>
              <td> <b>2</b> </td>
              <td> <b>{"first name": "Charlie", "last_name": "Sheen"}</b> </td>
            </tr>
          </tbody>
        </table>
      </td>
    </tr>
  </tbody>
</table>
      
##### Example 2 - Update exists user data with non-exists field

<table>
  <tbody>
    <tr>
      <td> Initial Data </td>
      <td>
        <table>
          <tbody>
            <tr>
              <th> user_id </th>
              <th> user_data </th>
            </tr>
            <tr>
              <td> 1 </td>
              <td> {"first name": "Bob", "last_name": "Marley"} </td>
            </tr>
            <tr>
              <td> 2 </td>
              <td> {"first name": "Charlie", "last_name": "Sheen"} </td>
            </tr>
          </tbody>
        </table>
      </td>
    </tr>
    <tr>
      <td> Input Parameter </td>
      <td>
        <table>
          <tbody>
            <tr>
              <td> user_id </td>
              <td> 1 </td>
            </tr>
            <tr>
              <td> new_data </td>
              <td> {"date of birth": "Apr 1 1980"} </td>
            </tr>
          </tbody>
        </table>      
      </td>
    </tr>
    <tr>
      <td> Final Data </td>
      <td>
        <table>
          <tbody>
            <tr>
              <th> user_id </th>
              <th> user_data </th>
            </tr>
            <tr>
              <td> 1 </td>
              <td> {"first name": "Bob", "last_name": "Marley", <b>"date of birth": "Apr 1 1980"</b>} </td>
            </tr>
            <tr>
              <td> 2 </td>
              <td> {"first name": "Charlie", "last_name": "Sheen"} </td>
            </tr>
          </tbody>
        </table>
      </td>
    </tr>
  </tbody>
</table>

##### Example 3 - Update exists user data with exists field

<table>
  <tbody>
    <tr>
      <td> Initial Data </td>
      <td>
        <table>
          <tbody>
            <tr>
              <th> user_id </th>
              <th> user_data </th>
            </tr>
            <tr>
              <td> 1 </td>
              <td> {"first name": "Bob", "last_name": "Marley", "date of birth": "Apr 1 1980"} </td>
            </tr>
            <tr>
              <td> 2 </td>
              <td> {"first name": "Charlie", "last_name": "Sheen"} </td>
            </tr>
          </tbody>
        </table>
      </td>
    </tr>
    <tr>
      <td> Input Parameter </td>
      <td>
        <table>
          <tbody>
            <tr>
              <td> user_id </td>
              <td> 1 </td>
            </tr>
            <tr>
              <td> new_data </td>
              <td> {"date of birth": "Feb 29 1970"} </td>
            </tr>
          </tbody>
        </table>      
      </td>
    </tr>
    <tr>
      <td> Final Data </td>
      <td>
        <table>
          <tbody>
            <tr>
              <th> user_id </th>
              <th> user_data </th>
            </tr>
            <tr>
              <td> 1 </td>
              <td> {"first name": "Bob", "last_name": "Marley", <b>"date of birth": "Feb 29 1970"</b>} </td>
            </tr>
            <tr>
              <td> 2 </td>
              <td> {"first name": "Charlie", "last_name": "Sheen"} </td>
            </tr>
          </tbody>
        </table>
      </td>
    </tr>
  </tbody>
</table>

##### Example 4 - Update will new value of null (delete data)

<table>
  <tbody>
    <tr>
      <td> Initial Data </td>
      <td>
        <table>
          <tbody>
            <tr>
              <th> user_id </th>
              <th> user_data </th>
            </tr>
            <tr>
              <td> 1 </td>
              <td> {"first name": "Bob", "last_name": "Marley", "date of birth": "Feb 29 1970"} </td>
            </tr>
            <tr>
              <td> 2 </td>
              <td> {"first name": "Charlie", "last_name": "Sheen"} </td>
            </tr>
          </tbody>
        </table>
      </td>
    </tr>
    <tr>
      <td> Input Parameter </td>
      <td>
        <table>
          <tbody>
            <tr>
              <td> user_id </td>
              <td> 1 </td>
            </tr>
            <tr>
              <td> new_data </td>
              <td> {"date of birth": null} </td>
            </tr>
          </tbody>
        </table>      
      </td>
    </tr>
    <tr>
      <td> Final Data </td>
      <td>
        <table>
          <tbody>
            <tr>
              <th> user_id </th>
              <th> user_data </th>
            </tr>
            <tr>
              <td> 1 </td>
              <td> {"first name": "Bob", "last_name": "Marley"} </td>
            </tr>
            <tr>
              <td> 2 </td>
              <td> {"first name": "Charlie", "last_name": "Sheen"} </td>
            </tr>
          </tbody>
        </table>
      </td>
    </tr>
  </tbody>
</table>

