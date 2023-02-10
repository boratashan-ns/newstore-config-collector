package nwscore.io;

import kong.unirest.HttpResponse;

import java.io.IOException;
import java.util.Map;

public interface RestClient {
    HttpResponse<String> post(String path, String query) throws RestClientException, InvalidCredentialsException;
    HttpResponse<String> post(String path, Map<String, Object> bodyFields) throws RestClientException, InvalidCredentialsException;
    HttpResponse<String> put(String path, String body) throws RestClientException, InvalidCredentialsException;
    HttpResponse<String> patch(String path, String body) throws RestClientException, InvalidCredentialsException;
    HttpResponse<String> get(String path, Map<String, Object> query, String body) throws InvalidCredentialsException, RestClientException;
     HttpResponse<String> put(String path, byte[] body) throws RestClientException, InvalidCredentialsException;
}
