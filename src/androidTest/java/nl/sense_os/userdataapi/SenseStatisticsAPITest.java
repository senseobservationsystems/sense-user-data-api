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
public class SenseStatisticsAPITest {

    public static final String  TAG = "StatisticsAPIUnitTest";

    public static final boolean  useLive = false;

    @Before
    public void setup() {
        Log.v(TAG, "Setup SenseStatisticsAPIUnitTest");
    }

    @After
    public void tearDown() throws Exception {
        Log.v(TAG, "Tearing Down SenseStatisticsAPIUnitTest");
    }

    @Test
    public void testGetContext() {
        Log.d(TAG, "testGetContext started!");
        CSUtils csUtils = new CSUtils(useLive);
        try {
            // Arrange: login as domain manager
            String sessionId = csUtils.loginUser("tatsuya+jetlag@sense-os.nl@brightr-generic", "Test1234");
            SenseStatisticsAPI statisticsAPI = new SenseStatisticsAPI(useLive);
            statisticsAPI.setSessionId(sessionId);

            // Act: get an array of contexts
            JSONArray arrayOfContext = statisticsAPI.getContext();

            // Assert: check that the arrayOfContext contains domain, group and user
            JSONArray expectedArray = new JSONArray();
            expectedArray.put(SenseStatisticsContext.DOMAIN);
            expectedArray.put(SenseStatisticsContext.GROUP);
            expectedArray.put(SenseStatisticsContext.USER);
            JSONAssert.assertEquals(expectedArray, arrayOfContext, false);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (SenseResponseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetContextIds() {
        Log.d(TAG, "testGetContextIds started!");
        CSUtils csUtils = new CSUtils(useLive);
        try {
            // Arrange: login as a user and get the user id
            String sessionId = csUtils.loginUser("tatsuya+jetlag@sense-os.nl@brightr-generic", "Test1234");
            SenseStatisticsAPI statisticsAPI = new SenseStatisticsAPI(useLive);
            statisticsAPI.setSessionId(sessionId);

            // Act: get an array of context IDs
            JSONArray arrayOfContextIds = statisticsAPI.getContextIds(SenseStatisticsContext.USER);

            // Assert: domain ID should be more than 0.
            Log.d(TAG, "Domain ID:" + arrayOfContextIds.toString());
            Assert.assertTrue(arrayOfContextIds.length() > 0);

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
    public void testGetActiveStatisticsType() {
        Log.d(TAG, "testGetActiveStatisticsType started!");
        CSUtils csUtils = new CSUtils(useLive);
        try {
            // Arrange: login as a user and get the user id
            String sessionId = csUtils.loginUser("tatsuya+jetlag@sense-os.nl@brightr-generic", "Test1234");
            SenseStatisticsAPI statisticsAPI = new SenseStatisticsAPI(useLive);
            statisticsAPI.setSessionId(sessionId);

            // Act: get an array of context IDs
            JSONArray arrayOfContextIds = statisticsAPI.getContextIds(SenseStatisticsContext.USER);
            JSONArray arrayOfStatisticsType = statisticsAPI.getActiveStatisticsType(SenseStatisticsContext.USER, arrayOfContextIds.getInt(0));

            // Assert: available statisticsType should be more than 0 for this user
            Log.d(TAG, "Available statisticsType:" + arrayOfStatisticsType.toString());
            Assert.assertTrue(arrayOfStatisticsType.length() > 0);

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
    public void testGetStatistics() {
        Log.d(TAG, "testGetStatistics started!");
        CSUtils csUtils = new CSUtils(useLive);
        try {
            // Arrange: login as a user and get the user id
            String sessionId = csUtils.loginUser("tatsuya+jetlag@sense-os.nl@brightr-generic", "Test1234");
            SenseStatisticsAPI statisticsAPI = new SenseStatisticsAPI(useLive);
            statisticsAPI.setSessionId(sessionId);

            // Act: get statistics
            JSONArray arrayOfContextIds = statisticsAPI.getContextIds(SenseStatisticsContext.USER);
            JSONArray arrayOfStatisticsType = statisticsAPI.getActiveStatisticsType(SenseStatisticsContext.USER, arrayOfContextIds.getInt(0));
            JSONArray statistics = statisticsAPI.getStatistics(SenseStatisticsContext.USER, arrayOfContextIds.getInt(0), arrayOfStatisticsType.getString(0));

            // Assert: TODO: assert properly
            Log.d(TAG, statistics.toString());
            //JSONAssert.assertEquals(expectedArray, arrayOfStatisticsType, false);

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
    public void testGetStatisticsWithQuery() {
        Log.d(TAG, "testGetStatistics started!");
        CSUtils csUtils = new CSUtils(useLive);
        try {
            // Arrange: login as a user and get the user id
            String sessionId = csUtils.loginUser("tatsuya+jetlag@sense-os.nl@brightr-generic", "Test1234");
            SenseStatisticsAPI statisticsAPI = new SenseStatisticsAPI(useLive);
            statisticsAPI.setSessionId(sessionId);
            //prepare query
            SenseStatisticsQuery query = new SenseStatisticsQuery()
                    .setStartTime(0l)
                    .setEndTime(System.currentTimeMillis())
                    .setLimit(100);

            // Act: get statistics
            JSONArray arrayOfContextIds = statisticsAPI.getContextIds(SenseStatisticsContext.USER);
            JSONArray arrayOfStatisticsType = statisticsAPI.getActiveStatisticsType(SenseStatisticsContext.USER, arrayOfContextIds.getInt(0));
            JSONArray statistics = statisticsAPI.getStatistics(SenseStatisticsContext.USER, arrayOfContextIds.getInt(0), arrayOfStatisticsType.getString(0), query);

            // Assert: TODO: assert properly
            Log.d(TAG, statistics.toString());
            //JSONAssert.assertEquals(expectedArray, arrayOfStatisticsType, false);

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
