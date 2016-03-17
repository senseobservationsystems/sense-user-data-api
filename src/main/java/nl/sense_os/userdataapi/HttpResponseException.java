package nl.sense_os.userdataapi;

/**
 * Created by tatsuya on 17/03/16.
 */
public class HttpResponseException extends Exception {
    private final int statusCode;

    public HttpResponseException(int statusCode, final String s) {
        super(s);
        this.statusCode = statusCode;
    }
    public int getStatusCode() {
        return this.statusCode;
    }
}
