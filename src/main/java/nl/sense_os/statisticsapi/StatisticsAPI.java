package nl.sense_os.statisticsapi;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;

import nl.sense_os.userdataapi.HttpResponseException;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by tatsuya on 22/03/16.
 */
public class StatisticsAPI {
    public static final String  TAG = "SenseUserDataAPI";

    private static String SCHEME_BASE;				  //The base scheme to use, will differ based on whether to use live or staging server
    private static String URL_BASE;				  //The base url to use, will differ based on whether to use live or staging server
    public static final String SCHEME_LIVE                   = "https";
    public static final String SCHEME_STAGING                = "http";
    public static final String BASE_URL_LIVE                   = "stats-api.sense-os.nl";
    public static final String BASE_URL_STAGING                = "stats-api.staging.sense-os.nl";
    public static final String URL_STATS               = "stats";
    private String mSessionId = null;

    public static final String QUERY_USERS_ID               = "users_id";

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private final OkHttpClient client = new OkHttpClient();

    public StatisticsAPI(boolean useLiveServer)
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
     * Get the currently set session id.
     * @return Returns the sessionId, or null if not set.
     */
    public String getSessionId() {
        return mSessionId;
    }

    /**
     * Set the session id of the current user.
     * @param sessionId   The session id of the current user.
     */
    public void setSessionId(final String sessionId) {
        this.mSessionId = sessionId;
    }

    /**
     * Get a list of `context` available for this user.
     * @return JSONArray containing available `context`. enum of String consists of "user", "group", "domain".
     *
     * TODO: add exceptions
     */
    public JSONArray getContext() throws HttpResponseException, JSONException, IOException {
        HttpUrl url = new HttpUrl.Builder()
                .scheme(SCHEME_STAGING)
                .host(URL_BASE)
                .addPathSegment(URL_STATS)
                .build();

        // Construct request
        Request request = new Request.Builder()
                .url(url)
                .addHeader("SESSION-ID", mSessionId)
                .build();

        // Send Request
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        // Handle response
        JSONArray responseJSON = null;
        responseJSON = new JSONArray(response.body().string());
        return responseJSON;
    }

    /**
     * Get a list of `contextId` available for this context and this user.
     * @param context JSONArray for `context`. enum of String consists of "user", "group", "domain".
     * @return JSONArray containing integers for available `contextId`.
     *
     * TODO: add exceptions
     * TODO: Make the parameter context enum
     */
    public JSONArray getContextIds(String context) throws HttpResponseException, JSONException, IOException {
        HttpUrl url = new HttpUrl.Builder()
                .scheme(SCHEME_STAGING)
                .host(URL_BASE)
                .addPathSegment(URL_STATS)
                .addPathSegment(context)
                .build();

        // Construct request
        Request request = new Request.Builder()
                .url(url)
                .addHeader("SESSION-ID", mSessionId)
                .build();

        // Send Request
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        // Handle response
        JSONArray responseJSON = null;
        responseJSON = new JSONArray(response.body().string());
        return responseJSON;
    }

    /**
     * Get list of currently active statistics_type for this contextId.
     * @param context JSONArray for `context`. The value can be "user", "group" and "domain".
     * @param contextId integer for the context ID.
     * @return JSONArray containing String for available `statistics_type`.
     *          The value can be "registered_user", "active_user", "time_active", "sleep_time" and "etc".
     *
     * TODO: add exceptions
     * TODO: Make the parameter context enum
     */
    public JSONArray getActiveStatisticsType(String context, int contextId) throws HttpResponseException, JSONException, IOException {
        HttpUrl url = new HttpUrl.Builder()
                .scheme(SCHEME_STAGING)
                .host(URL_BASE)
                .addPathSegment(URL_STATS)
                .addPathSegment(context)
                .addPathSegment(Integer.toString(contextId))
                .build();

        // Construct request
        Request request = new Request.Builder()
                .url(url)
                .addHeader("SESSION-ID", mSessionId)
                .build();

        // Send Request
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        // Handle response
        JSONArray responseJSON = null;
        responseJSON = new JSONArray(response.body().string());
        return responseJSON;
    }
}
