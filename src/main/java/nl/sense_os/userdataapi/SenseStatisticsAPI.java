package nl.sense_os.userdataapi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import nl.sense_os.userdataapi.SenseResponseException;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by tatsuya on 22/03/16.
 */
public class SenseStatisticsAPI {
    public static final String  TAG = "SenseUserDataAPI";

    private static String SCHEME_BASE;				  //The base scheme to use, will differ based on whether to use live or staging server
    private static String URL_BASE;				  //The base url to use, will differ based on whether to use live or staging server
    public static final String SCHEME_LIVE                   = "https";
    public static final String SCHEME_STAGING                = "http";
    public static final String BASE_URL_LIVE                   = "statistics-api.sense-os.nl";
    public static final String BASE_URL_STAGING                = "statistics-api.staging.sense-os.nl";
    public static final String URL_STATS               = "stats";
    private String mSessionId = null;

    public static final String QUERY_USERS_ID               = "users_id";

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private final OkHttpClient client = new OkHttpClient();

    public SenseStatisticsAPI(boolean useLiveServer)
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
     * @exception SenseResponseException
     * @exception JSONException
     * @exception IOException
     */
    public JSONArray getContext() throws SenseResponseException, JSONException, IOException {
        HttpUrl url = new HttpUrl.Builder()
                .scheme(SCHEME_BASE)
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
        if (!response.isSuccessful()) throw new SenseResponseException(getExceptionMessage(response));

        // Handle response
        return new JSONArray(response.body().string());
    }

    /**
     * Get a list of `contextId` available for this context and this user.
     * @param context JSONArray for `context`. enum of String consists of "user", "group", "domain".
     * @return JSONArray containing integers for available `contextId`.
     * @exception SenseResponseException
     * @exception JSONException
     * @exception IOException
     */
    public JSONArray getContextIds(SenseStatisticsContext context) throws SenseResponseException, JSONException, IOException {
        HttpUrl url = new HttpUrl.Builder()
                .scheme(SCHEME_BASE)
                .host(URL_BASE)
                .addPathSegment(URL_STATS)
                .addPathSegment(context.toString())
                .build();

        // Construct request
        Request request = new Request.Builder()
                .url(url)
                .addHeader("SESSION-ID", mSessionId)
                .build();

        // Send Request
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new SenseResponseException(getExceptionMessage(response));

        // Handle response
        return new JSONArray(response.body().string());
    }

    /**
     * Get list of currently active statistics_type for this contextId.
     * @param context SenseStatisticsContext. The value can be "user", "group" and "domain".
     * @param contextId integer for the context ID.
     * @return JSONArray containing String for available `statistics_type`.
     *          The value can be "registered_user", "active_user", "time_active", "sleep_time" and "etc".
     * @exception SenseResponseException
     * @exception JSONException
     * @exception IOException
     */
    public JSONArray getAvailableMeasurementType(SenseStatisticsContext context, int contextId) throws SenseResponseException, JSONException, IOException {
        HttpUrl url = new HttpUrl.Builder()
                .scheme(SCHEME_BASE)
                .host(URL_BASE)
                .addPathSegment(URL_STATS)
                .addPathSegment(context.toString())
                .addPathSegment(Integer.toString(contextId))
                .build();

        // Construct request
        Request request = new Request.Builder()
                .url(url)
                .addHeader("SESSION-ID", mSessionId)
                .build();

        // Send Request
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new SenseResponseException(getExceptionMessage(response));

        // Handle response
        return new JSONArray(response.body().string());
    }

    /**
     * Get statistics result of the given statistics type.
     * @param context SenseStatisticsContext. for `context`. The value can be "user", "group" and "domain".
     * @param contextId int for the context ID.
     * @param statisticsType String for statisticsType that should be returned.
     * @return JSONArray structured as:
     *              [
     *                  {
     *                      "time": long,
     *                      "value": {
     *                          ??? //TODO: figure this out or add reference.
     *                      }
     *                  },
     *                  ...
     *              ]
     *
     * @exception SenseResponseException
     * @exception JSONException
     * @exception IOException
     */
    public JSONArray getStatistics(SenseStatisticsContext context, int contextId, String statisticsType) throws SenseResponseException, JSONException, IOException {
       return getStatistics(context, contextId, statisticsType, null);
    }

    /**
     * Get statistics result of the given statistics type.
     * @param context SenseStatisticsContext. The value can be "user", "group" and "domain".
     * @param contextId int for the context ID.
     * @param statisticsType String for statisticsType that should be returned.
     * @param query SenseStatisticsQuery for specifying desired condition for the query.
     * @return JSONArray structured as:
     *              [
     *                  {
     *                      "time": long,
     *                      "value": {
     *                          ??? //TODO: figure this out or add reference.
     *                      }
     *                  },
     *                  ...
     *              ]
     *
     * @exception SenseResponseException
     * @exception JSONException
     * @exception IOException
     */
    public JSONArray getStatistics(SenseStatisticsContext context, int contextId, String statisticsType, SenseStatisticsQuery query) throws SenseResponseException, JSONException, IOException {
        HttpUrl url = new HttpUrl.Builder()
                .scheme(SCHEME_BASE)
                .host(URL_BASE)
                .addPathSegment(URL_STATS)
                .addPathSegment(context.toString())
                .addPathSegment(Integer.toString(contextId))
                .addPathSegment(statisticsType)
                .build();

        if (query != null){
            url = addQueryParameters(url, query);
        }

        // Construct request
        Request request = new Request.Builder()
                .url(url)
                .addHeader("SESSION-ID", mSessionId)
                .build();

        // Send Request
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new SenseResponseException(getExceptionMessage(response));

        // Handle response
        return new JSONArray(response.body().string());
    }

    private HttpUrl addQueryParameters(HttpUrl url, SenseStatisticsQuery query){
        if (query.getStartTime() != null) {
            url.newBuilder().addQueryParameter("start_time", Long.toString(query.getStartTime())).build();
        }
        if (query.getEndTime() != null) {
            url.newBuilder().addQueryParameter("end_time", Long.toString(query.getEndTime())).build();
        }
        if (query.getSortOrder() != null) {
            url.newBuilder().addQueryParameter("sort", query.getSortOrder()).build();
        }
        if (query.getLimit() != null) {
            url.newBuilder().addQueryParameter("limit", Integer.toString(query.getLimit())).build();
        }
        if (query.getRunning() != null) {
            url.newBuilder().addQueryParameter("aggregation", Boolean.toString(query.getRunning())).build();
        }
        return url;
    }

    private String getExceptionMessage(Response response){
        return "Unexpected code " + response + " for " + response.request();
    }
}


