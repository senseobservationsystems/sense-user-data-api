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
 - Get a list of active statistics type eg) registered_user, active_user, time_active, sleep_time
 - Get statistics   

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
```java
	 // Get ready!
	boolean useLive = false; // Specifying whether you want to use live server or stagint server
    SenseStatisticsAPI statisticsAPI = new SenseStatisticsAPI(useLive);
    statisticsAPI.setSessionId(sessionId);
    
    //prepare query
    long now = System.currentTimeMillis() ;
    long queryStartTime = now - 24 * 60 * 60 * 1000; // 24 hours ago
    SenseStatisticsQuery query = new SenseStatisticsQuery()
            .setStartTime(queryStartTime)
            .setEndTime(now)
            .setLimit(100);

    // get available Ids in user context
    JSONArray arrayOfContextIds = statisticsAPI.getContextIds(SenseStatisticsContext.USER);
    
    // get available statistics type
    JSONArray arrayOfStatisticsType = statisticsAPI.getActiveStatisticsType(SenseStatisticsContext.USER, arrayOfContextIds.getInt(0));
    
    // get statistics
    JSONArray statistics = statisticsAPI.getStatistics(SenseStatisticsContext.USER, arrayOfContextIds.getInt(0), arrayOfStatisticsType.getString(0), query);
```

## Update User Data

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

