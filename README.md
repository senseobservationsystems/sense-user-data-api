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

## Recipe

### SenseUserDataAPI
```java
	 // Get ready!
    SenseUserDataAPI userDataAPI = new SenseUserDataAPI(useLive);
    userDataAPI.setSessionId(sessionId);
    
    // prepare JSONObject contains the user data for this user
    JSONObject innerUserData = new JSONObject();
    innerUserData.put("first_name", "Frank");
    innerUserData.put("last_name", "Underwood");
    innerUserData.put("address", "1609 Far St. NW, Washington, D.C., 20036");

    JSONObject outerUserData = new JSONObject();
    outerUserData.put("user_data", innerUserData);

	 // Insert/Update the user data
    userDataAPI.putUserData(userId, outerUserData);
    
    // Get user data for this user
    JSONObject userData = userDataAPI.getUsersData(userId);
```


### SenseStatisticsAPI
```java
	 // Get ready!
    SenseStatisticsAPI statisticsAPI = new SenseStatisticsAPI(useLive);
    statisticsAPI.setSessionId(sessionId);
    
    //prepare query
    SenseStatisticsQuery query = new SenseStatisticsQuery()
            .setStartTime(0l)
            .setEndTime(System.currentTimeMillis())
            .setLimit(100);

    // get available Ids in user context
    JSONArray arrayOfContextIds = statisticsAPI.getContextIds(SenseStatisticsContext.USER);
    
    // get available statistics type
    JSONArray arrayOfStatisticsType = statisticsAPI.getActiveStatisticsType(SenseStatisticsContext.USER, arrayOfContextIds.getInt(0));
    
    // get statistics
    JSONArray statistics = statisticsAPI.getStatistics(SenseStatisticsContext.USER, arrayOfContextIds.getInt(0), arrayOfStatisticsType.getString(0), query);
```