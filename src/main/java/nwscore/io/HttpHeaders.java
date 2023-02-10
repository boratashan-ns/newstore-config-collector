package nwscore.io;

import java.util.HashMap;
import java.util.Map;

public class HttpHeaders {
    private final Map<String, String> headers = new HashMap<>();

    public HttpHeaders add(String key, String value) {
        headers.put(key, value);
        return this;
    }

    public Map<String, String> getHeaders() {
        return this.headers;
    }

    public static HttpHeaders create() {
        return new HttpHeaders();
    }

}
