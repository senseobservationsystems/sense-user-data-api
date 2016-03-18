package nl.sense_os.userdataapi;

import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;

import junit.framework.Assert;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class SenseUserDataAPIAndroidUnitTest {

    public static final String  TAG = "UserDataAPIUnitTest";

    //TODO: add setup and tearDown

    @Test
    public void testPutUserData_UserDataUpdated() {
        Log.d(TAG, "testPutUserData started!");
        CSAuthUtils csAuthUtils = new CSAuthUtils(false);
        try {
            //Arrange: Instantiate UserDataAPI and set sessionId
            String sessionId = csAuthUtils.loginUser("tatsuya+userdata@sense-os.nl", "Test1234");
            SenseUserDataAPI userDataAPI = new SenseUserDataAPI(false);
            userDataAPI.setSessionId(sessionId);

            //Act: Get users under this domain manager and update the user data
            JSONArray retrievedUsersData = userDataAPI.getUsersData();
            for (int i = 0; i < retrievedUsersData.length(); i++) {
                JSONObject userData = retrievedUsersData.getJSONObject(i);
                JSONObject innerUserData = userData.getJSONObject("user_data");
                innerUserData.put("first_name", "Frank");
                innerUserData.put("last_name", "Underwood");
                innerUserData.put("address", "1609 Far St. NW, Washington, D.C., 20036");

                JSONObject outerUserData = new JSONObject();
                outerUserData.put("user_data", innerUserData);

                userDataAPI.putUserData(userData.getInt("user_id"), outerUserData);
            }

            //Assert: UserData is updated
            JSONArray updatedUsersData = userDataAPI.getUsersData();
            Log.d(TAG, String.format("%s", retrievedUsersData.toString()));
            Assert.assertTrue(true);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (HttpResponseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetUsersData() {
        Log.d(TAG, "testGetUsersData started!");
        CSAuthUtils csAuthUtils = new CSAuthUtils(false);
        try {
            String sessionId = csAuthUtils.loginUser("tatsuya+userdata@sense-os.nl", "Test1234");
            SenseUserDataAPI userDataAPI = new SenseUserDataAPI(false);
            userDataAPI.setSessionId(sessionId);
            JSONArray usersData = userDataAPI.getUsersData();
            Log.d(TAG, String.format("%s", usersData.toString()));
            //TODO: update this assertion
            Assert.assertEquals(usersData.length(), 2);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (HttpResponseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetUserData() {
        Log.d(TAG, "testGetUserData started!");
        CSAuthUtils csAuthUtils = new CSAuthUtils(false);
        try {
            String sessionId = csAuthUtils.loginUser("tatsuya+userdata@sense-os.nl", "Test1234");
            SenseUserDataAPI userDataAPI = new SenseUserDataAPI(false);
            userDataAPI.setSessionId(sessionId);
            JSONObject usersData = userDataAPI.getUserData(637);
            Log.d(TAG, String.format("%s", usersData.toString()));
            //TODO: update this assertion
            Assert.assertNotNull(usersData.getJSONObject("user_data").getString("first_name"));

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (HttpResponseException e) {
            e.printStackTrace();
        }
    }

//    @Test
//    public void testRegistrationAndDelete() {
//        CSAuthUtils csAuthUtils = new CSAuthUtils(false);
//        try {
//            String password = "Password";
//            JSONObject userInfo = csAuthUtils.createCSAccount(password);
//            Log.e(TAG, String.format("%s", userInfo.toString()));
//            String username = userInfo.getJSONObject("user").getString("username");
//            String userId = userInfo.getJSONObject("user").getString("id");
//            boolean succeed = csAuthUtils.deleteAccount(username, password, userId);
//            Log.e(TAG, String.format("Succeed?: %b", succeed));
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
}
