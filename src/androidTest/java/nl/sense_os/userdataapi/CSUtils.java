package nl.sense_os.userdataapi;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by tatsuya on 17/03/16.
 */
public class CSUtils {

    public static final String APP_KEY = "wRgE7HZvhDsRKaRm6YwC3ESpIqqtakeg";
    public static final String APP_KEY_LIVE = "s1iS3CDK7Xb9rRjeJD5LM1XS67dP7pkl";
    public static final String  TAG = "CSUtils";

    private static String URL_BASE;				  //The base url to use, will differ based on whether to use live or staging server

    public static final String BASE_URL_LIVE                   = "https://api.sense-os.nl";
    public static final String BASE_URL_STAGING                = "http://api.staging.sense-os.nl";

    public static final String URL_USERS                       = "users";
    public static final String URL_LOGIN                       = "login";
    public static final String URL_LOGOUT                      = "logout";
    public static final String URL_CURRENT                       = "current";

    public static final String RESPONSE_CODE = "http response code";

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private final OkHttpClient client = new OkHttpClient();

    public CSUtils(boolean useLiveServer)
    {
        if(useLiveServer) {
            URL_BASE = BASE_URL_LIVE;
        } else {
            URL_BASE = BASE_URL_STAGING;
        }
    }

    public String loginUser( String username, String password) throws IOException, RuntimeException, JSONException {
        if(username == null || username.isEmpty() || password == null || password.isEmpty()) {
            throw new IllegalArgumentException("invalid input of username or password");
        }

        final String url = URL_BASE + "/" + URL_LOGIN;
        // Construct body
        final JSONObject postBody = new JSONObject();
        postBody.put("username", username);
        postBody.put("password", password);

        // Construct request
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(JSON, postBody.toString()))
                .addHeader("APPLICATION-KEY", APP_KEY)
                .build();

        // Send Request
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        // Handle response
        JSONObject responseJSON = null;
        responseJSON = new JSONObject(response.body().string());

        return responseJSON.getString("session_id");
    }

    //creates random account on CommonSense.
    // @ return map{"email", email  ;  "username", <username>  ; "password" <password> }
    public JSONObject createCSAccount(String password) throws IOException, JSONException {

        String urlString = "http://api.staging.sense-os.nl/users.json";
        final String url = URL_BASE + "/" + URL_USERS;

        // Construct Body
        JSONObject user = new JSONObject();
        JSONObject postBody = new JSONObject();

        long now = System.currentTimeMillis();
        String username = "spam+"+now+"@sense-os.nl";
        String email = "spam+"+now+"@sense-os.nl";
        String userid = "";

        user.put("username", username);
        user.put("email", email);
        user.put("password", password);
        postBody.put("user", user);

        // Construct request
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(JSON, postBody.toString()))
                .addHeader("APPLICATION-KEY", APP_KEY_LIVE)
                .build();

        // Send Request
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        // Handle response
        JSONObject responseJSON = null;
        responseJSON = new JSONObject(response.body().string());

        return responseJSON;
    }

    public int getUserId(String sessionId) throws JSONException, IOException {

        final String url = URL_BASE + "/" + URL_USERS + "/" + URL_CURRENT;

        // Construct request
        Request request = new Request.Builder()
                .url(url)
                .addHeader("SESSION-ID", sessionId)
                .build();

        // Send Request
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        // Handle response
        JSONObject responseJSON = null;
        responseJSON = new JSONObject(response.body().string());
        String userIdString = responseJSON.getJSONObject("user").getString("id");
        return Integer.parseInt(userIdString);
    }

    public boolean deleteAccount(String userName, String password, String userId) throws JSONException, IOException {

        String sessionId = loginUser(userName, password);
        final String url = URL_BASE + "/" + URL_LOGOUT;

        // Construct request
        Request request = new Request.Builder()
                .url(url)
                .delete()
                .addHeader("APPLICATION-KEY", APP_KEY_LIVE)
                .build();

        // Send Request
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        return true;
    }


    /**
     * Verify the response code
     *
     * @param responseCode	The response code returned from the request
     * @param method		The method that requires code checking
     *
     * @result	The integer value of the result
     *
     */
    public int checkResponseCode(String responseCode, String method){
        if ("403".equalsIgnoreCase(responseCode)) {
            Log.w(TAG, "CommonSense" + method + "refused! Response: forbidden!");
            return -2;
        } else if ("201".equalsIgnoreCase(responseCode)) {
            Log.v(TAG, "CommonSense" + method + "created! Response: " + responseCode);
            return 1;
        } else if (!"200".equalsIgnoreCase(responseCode)) {
            Log.v(TAG, "CommonSense" + method + "failed! Response: " + responseCode);
            return -1;
        } else {
            // received 200 response
            Log.d(TAG, "CommonSense" + method + "OK! ");
            return  0;
        }
    }
}
