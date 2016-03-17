package nl.sense_os.userdataapi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by tatsuya on 17/03/16.
 */
public class SenseUserDataAPI {

    private String mBaseUrl = null;
    private String mAppKey = null;
    private String mSessionId = null;

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
     * TODO: update it when the underlying function's documentation is finalized.
     * @return
     */
    public JSONArray getUsersData(){
        return getUsersData(null, null, null);
    }

    /**
     * Get multiple `User Data` in one domain. This can be used only by domain manager.
     * @param userIds    JSONArray containing int for userId of the user whose data should be returned. Optional.
     * @param page      Integer for page?? Optional.
     * @param per_page  integer for the number of users that a page should contain?? Optional.
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
    public JSONArray getUsersData(JSONArray userIds, Integer page, Integer per_page){
        //TODO: to be implemented
        return new JSONArray();
    }

    /**
     * Put multiple `UserData`. This can be used only by domain manager.
     *
     * @param userData JSONArray containing `UserData`, structured as:
     *                 [
     *                    {
     *                      user_id: integer,
     *                      user_data: { first_name: string, last_name: string }
     *                    },
     *                    ...
     *                 ]
     * TODO: add exceptions
     */
    public void putUsersData(JSONArray userData){
        //TODO: to be implemented
    }

    /**
     * TODO: update it when the underlying function's documentation is finalized.
     * @return
     */
    public JSONObject getUserData(int userId){
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
    public JSONObject getUserData(int userId, JSONArray query){
        //TODO: to be implemented
        return new JSONObject();
    }



    /**
     * Put `UserData` of a user specified by the userId.
     *
     * @param userId int for the user ID of the user whose data should be updated.
     * @param userData JSONArray containing userData, structured as:
     *                    { first_name: string, last_name: string }
     * TODO: add exceptions
     */
    public void putUserData(int userId, JSONArray userData){
        //TODO: to be implemented
    }

    /**
     * Delete `UserData` of a user specified by the userId.
     *
     * @param userId int for the user ID of the user whose data should be deleted.
     * TODO: add exceptions
     */
    public void deleteUserData(int userId){
        //TODO: to be implemented
    }
}
