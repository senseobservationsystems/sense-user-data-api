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
    		"user_data": {
    			"first_name": "Frank",
    			"last_name": "Underwood",
     			"address": "1609 Far St. NW, Washington, D.C., 20036"
    		}
    	 }
    **/ 
    JSONObject innerUserData = new JSONObject();
    innerUserData.put("first_name", "Frank");
    innerUserData.put("last_name", "Underwood");
    innerUserData.put("address", "1609 Far St. NW, Washington, D.C., 20036");

    JSONObject outerUserData = new JSONObject();
    outerUserData.put("user_data", innerUserData);

	 // Sending PUT request with the user data
    userDataAPI.putUserData(userId, outerUserData);
    
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