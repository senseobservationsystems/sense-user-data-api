package nl.sense_os.userdataapi;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by tatsuya on 17/03/16.
 */
public class SenseUserDataAPI {

    public static final String  TAG = "SenseUserDataAPI";

    private static String SCHEME_BASE;				  //The base scheme to use, will differ based on whether to use live or staging server
    private static String URL_BASE;				  //The base url to use, will differ based on whether to use live or staging server
    public static final String SCHEME_LIVE                   = "https";
    public static final String SCHEME_STAGING                = "http";
    public static final String BASE_URL_LIVE                   = "user-data-api.sense-os.nl";
    public static final String BASE_URL_STAGING                = "user-data-api.staging.sense-os.nl";
    public static final String URL_USERDATA               = "user_data";
    private String mSessionId = null;

    public static final String QUERY_USERS_ID               = "users_id";

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private final OkHttpClient client = new OkHttpClient();

    public SenseUserDataAPI(boolean useLiveServer)
    {
        if(useLiveServer) {
            SCHEME_BASE = SCHEME_LIVE;
            URL_BASE = BASE_URL_LIVE;
        } else {
            SCHEME_BASE = SCHEME_STAGING;
            URL_BASE = BASE_URL_STAGING;
        }
    }

    /**
     * Get the currently set session id
     * @return Returns the sessionId, or null if not set
     */
    public String getSessionId() {
        return mSessionId;
    }

    /**
     * Set the session id of the current user
     * @param sessionId   The session id of the current user.
     */
    public void setSessionId(final String sessionId) {
        this.mSessionId = sessionId;
    }

    /**
     * Get multiple `User Data` in one domain. This can be used only by domain manager.
     * @return JSONArray containing usersData, structured as:
     *          [
     *                    {
     *                      user_id: integer,
     *                      user_data: { first_name: string, last_name: string }
     *                    },
     *                    ...
     *          ]
     * TODO: add exceptions
     */
    public JSONArray getUsersData() throws HttpResponseException, JSONException, IOException {
        return getUsersData(new JSONArray());
    }

    /**
     * Get multiple `User Data` in one domain. This can be used only by domain manager.
     * @param userIds    JSONArray containing int for userId of the user whose data should be returned. Optional.
     * @return JSONArray containing usersData, structured as:
     *          [
     *                    {
     *                      user_id: integer,
     *                      user_data: { first_name: string, last_name: string }
     *                    },
     *                    ...
     *          ]
     * TODO: add exceptions
     */
    public JSONArray getUsersData(JSONArray userIds) throws HttpResponseException, JSONException, IOException {
        HttpUrl url = new HttpUrl.Builder()
                .scheme(SCHEME_STAGING)
                .host(URL_BASE)
                .addPathSegment(URL_USERDATA)
                .addQueryParameter("users_id", userIds.toString())
                .build();

        // Construct request
        Request request = new Request.Builder()
                .url(url)
                .addHeader("SESSION-ID", mSessionId)
                .build();

        // Send Request
        Log.e(TAG, String.format("[SenseUserDataAPI] Sending GET Request to %s", url.toString()));
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        // Handle response
        JSONArray responseJSON = null;
        responseJSON = new JSONArray(response.body().string());
        return responseJSON;
    }

    /**
     * TODO: update it when the underlying function's documentation is finalized.
     * @return
     */
    public JSONObject getUserData(int userId) throws HttpResponseException, IOException, JSONException {
        return getUserData(userId, null);
    }

    /**
     * Get `UserData` of a user by userId.
     *
     * @param userId int for the user ID of the user whose data should be returned.
     * @param query JSONArray containing string for the fields that should be returned. If not given, all fields will be included in the returned `UserData`. Optional.
     * @return JSONObject containing `UserData` of a user by userId. If query was given, the retuned object contains only the fields selected by the query.
     * TODO: add exceptions
     */
    public JSONObject getUserData(int userId, JSONArray query) throws HttpResponseException, IOException, JSONException {
        HttpUrl url = new HttpUrl.Builder()
                .scheme(SCHEME_STAGING)
                .host(URL_BASE)
                .addPathSegment(URL_USERDATA)
                .addPathSegment(Integer.toString(userId))
                .build();

        // Construct request
        Request request = new Request.Builder()
                .url(url)
                .addHeader("SESSION-ID", mSessionId)
                .build();

        // Send Request
        Log.e(TAG, String.format("[SenseUserDataAPI] Sending GET Request to %s", url.toString()));
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        return new JSONObject(response.body().string());
    }



    /**
     * Put `UserData` of a user specified by the userId.
     *
     * @param userId int for the user ID of the user whose data should be updated.
     * @param userData JSONArray containing userData, structured as:
     *                    { first_name: string, last_name: string }
     * TODO: add exceptions
     */
    public void putUserData(int userId, JSONObject userData) throws HttpResponseException, IOException {
        HttpUrl url = new HttpUrl.Builder()
                .scheme(SCHEME_STAGING)
                .host(URL_BASE)
                .addPathSegment(URL_USERDATA)
                .addPathSegment(Integer.toString(userId))
                .build();

        // Construct request
        Request request = new Request.Builder()
                .url(url)
                .put(RequestBody.create(JSON, userData.toString()))
                .addHeader("SESSION-ID", mSessionId)
                .build();

        // Send Request
        Log.d(TAG, String.format("[SenseUserDataAPI] Sending PUT Request to %s", url.toString()));
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
    }

    /**
     * Delete `UserData` of a user specified by the userId.
     *
     * @param userId int for the user ID of the user whose data should be deleted.
     * TODO: add exceptions
     */
    public void deleteUserData(int userId) throws HttpResponseException, IOException {
        HttpUrl url = new HttpUrl.Builder()
                .scheme(SCHEME_STAGING)
                .host(URL_BASE)
                .addPathSegment(URL_USERDATA)
                .addPathSegment(Integer.toString(userId))
                .build();

        // Construct request
        Request request = new Request.Builder()
                .url(url)
                .delete()
                .addHeader("SESSION-ID", mSessionId)
                .build();

        // Send Request
        Log.e(TAG, String.format("[SenseUserDataAPI] Sending DELETE Request to %s", url.toString()));
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
    }
}
