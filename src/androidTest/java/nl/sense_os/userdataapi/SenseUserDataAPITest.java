package nl.sense_os.userdataapi;

import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;

import junit.framework.Assert;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;

import java.io.IOException;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class SenseUserDataAPITest {

    public static final String  TAG = "UserDataAPIUnitTest";

    public static final boolean  useLive = true;

    @Before
    public void setup() {
        Log.v(TAG, "Setup SenseUserDataAPITest");
    }

    @After
    public void tearDown() throws Exception {
        Log.v(TAG, "Tearing Down SenseUserDataAPITest");
        //clearUsersData();
    }

    @Test
    public void testGetUserDataAsUser() {
        Log.d(TAG, "testGetUserData started!");
        CSUtils csUtils = new CSUtils(useLive);
        try {
            // Arrange: login as a user and get the user id
            String sessionId = csUtils.loginUser("tatsuya+jetlag@sense-os.nl@brightr-generic", "Test1234");
            int userId = csUtils.getUserId(sessionId);
            SenseUserDataAPI userDataAPI = new SenseUserDataAPI(useLive);
            userDataAPI.setSessionId(sessionId);

            // Act: call get for a single user using the user Id
            JSONObject userData = userDataAPI.getUserData(userId);

            // Assert: the user_data to be empty
            JSONAssert.assertEquals(new JSONObject(), userData.getJSONObject("user_data"), false);

        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail();
        } catch (JSONException e) {
            e.printStackTrace();
            Assert.fail();
        } catch (SenseResponseException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testPutUserDataAsUser_UserDataUpdated() {
        Log.d(TAG, "testPutUserDataAsUser_UserDataUpdated As a user started!");
        CSUtils csUtils = new CSUtils(useLive);
        try {
            //Arrange: login as a domain manager
            String sessionId = csUtils.loginUser("tatsuya+jetlag@sense-os.nl@brightr-generic", "Test1234");
            int userId = csUtils.getUserId(sessionId);
            SenseUserDataAPI userDataAPI = new SenseUserDataAPI(useLive);
            userDataAPI.setSessionId(sessionId);

            //Act: update the user data for this user
            JSONObject innerUserData = new JSONObject();
            innerUserData.put("first_name", "Tatsuya");
            innerUserData.put("last_name", "Underwood");
            innerUserData.put("address", "Address of Tatsuya 111");

            JSONObject outerUserData = new JSONObject();
            outerUserData.put("user_data", innerUserData);

            userDataAPI.putUserData(userId, outerUserData);

            //Assert: check the data is updated
            JSONObject userData = userDataAPI.getUserData(userId);
            JSONAssert.assertEquals(userData.getJSONObject("user_data"), innerUserData, false);

        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail();
        } catch (JSONException e) {
            e.printStackTrace();
            Assert.fail();
        } catch (SenseResponseException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }

    @Test
    public void testPutUserDataAsDomainManager_UsersDataUpdated() {
        Log.d(TAG, "testPutUserDataAsDomainManager_UserDataUpdated started!");
        CSUtils csUtils = new CSUtils(useLive);
        try {
            //Arrange: login as a domain manager
            String sessionId = csUtils.loginUser("tatsuya+userdata@sense-os.nl", "Test1234");
            SenseUserDataAPI userDataAPI = new SenseUserDataAPI(useLive);
            userDataAPI.setSessionId(sessionId);
            // prepare template
            JSONObject innerUserData = new JSONObject();
            innerUserData.put("first_name", "Frank");
            innerUserData.put("last_name", "Underwood");
            innerUserData.put("address", "1609 Far St. NW, Washington, D.C., 20036");

            //Act: update the user data for multiple users under this domain manager
            JSONArray retrievedUsersData = userDataAPI.getUsersData();
            for (int i = 0; i < retrievedUsersData.length(); i++) {
                JSONObject userData = retrievedUsersData.getJSONObject(i);


                JSONObject outerUserData = new JSONObject();
                outerUserData.put("user_data", innerUserData);

                userDataAPI.putUserData(userData.getInt("user_id"), outerUserData);
            }

            //Assert: UserData is updated
            JSONArray updatedUsersData = userDataAPI.getUsersData();
            Log.d(TAG, String.format("%s", retrievedUsersData.toString()));
            for (int i = 0; i < updatedUsersData.length(); i++) {
                JSONObject userData = updatedUsersData.getJSONObject(i);
                JSONAssert.assertEquals(innerUserData, userData.getJSONObject("user_data"), false);
            }

        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail();
        } catch (JSONException e) {
            e.printStackTrace();
            Assert.fail();
        } catch (SenseResponseException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }



    @Test
    public void testDeleteUserDataAsUser() {
        Log.d(TAG, "testDeleteUserData started!");
        CSUtils csUtils = new CSUtils(useLive);
        try {
            // Arrange
            String sessionId = csUtils.loginUser("tatsuya+jetlag@sense-os.nl@brightr-generic", "Test1234");
            int userId = csUtils.getUserId(sessionId);
            SenseUserDataAPI userDataAPI = new SenseUserDataAPI(useLive);
            userDataAPI.setSessionId(sessionId);

            // Act : delete a user info
            userDataAPI.deleteUserData(userId);

            // Assert : make sure retrieved user_data is empty
            JSONObject retrievedUserData = userDataAPI.getUserData(userId);
            Log.d(TAG, String.format("%s", retrievedUserData.toString()));
            JSONAssert.assertEquals(new JSONObject(), retrievedUserData.getJSONObject("user_data"), false);

        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail();
        } catch (JSONException e) {
            e.printStackTrace();
            Assert.fail();
        } catch (SenseResponseException e) {
            e.printStackTrace();
            Assert.fail();

        }
    }

    private void clearUsersData(){
        //TODO: this gets internal server error for some reasons. Fix it.
        // delete users data
        CSUtils csUtils = new CSUtils(useLive);
        String sessionId = null;
        try {
            sessionId = csUtils.loginUser("tatsuya+userdata@sense-os.nl", "Test1234");

            SenseUserDataAPI userDataAPI = new SenseUserDataAPI(false);
            userDataAPI.setSessionId(sessionId);
            JSONArray usersData = userDataAPI.getUsersData();
            Log.d(TAG, String.format("%s", usersData.toString()));
            for (int i = 0; i < usersData.length(); i++) {
                int userId = usersData.getJSONObject(i).getInt("user_id");
                userDataAPI.deleteUserData(userId);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail();
        } catch (JSONException e) {
            e.printStackTrace();
            Assert.fail();
        } catch (SenseResponseException e) {
            e.printStackTrace();
            Assert.fail();
        }
    }
}
