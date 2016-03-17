package nl.sense_os.userdataapi;

import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;

import junit.framework.Assert;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class SenseUserDataAPIAndroidUnitTest {

    public static final String  TAG = "UserDataAPIUnitTest";

    @Test
    public void testLogin() {
        Log.e("TEST", "HelloWorld");
        CSAuthUtils csAuthUtils = new CSAuthUtils(false);
        try {
            String sessionId = csAuthUtils.loginUser("tatsuya+vc@sense-os.nl", "Test1234");
            Log.e(TAG, String.format("%s", sessionId));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRegistrationAndDelete() {
        CSAuthUtils csAuthUtils = new CSAuthUtils(false);
        try {
            String password = "Password";
            JSONObject userInfo = csAuthUtils.createCSAccount(password);
            Log.e(TAG, String.format("%s", userInfo.toString()));
            String username = userInfo.getJSONObject("user").getString("username");
            String userId = userInfo.getJSONObject("user").getString("id");
            boolean succeed = csAuthUtils.deleteAccount(username, password, userId);
            Log.e(TAG, String.format("Succeed?: %b", succeed));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
