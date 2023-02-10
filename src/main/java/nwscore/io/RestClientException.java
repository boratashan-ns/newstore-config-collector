package nwscore.io;

import kong.unirest.UnirestException;

public class RestClientException extends Exception {
    public RestClientException(UnirestException e) {
        super(e);
    }

    public RestClientException(String message) {
        super(message);
    }
}
