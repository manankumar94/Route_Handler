package FactoryPattern.Responses;
import java.util.*;

/**
 * Represents an HTTP response.
 */
public class HttpResponse{
    private final String version;
    private final int StatusCode;
    private final String StatusMessage;
    private final Map<String, String> headers;
    private final byte[] body;

    /**
     * Constructs an HTTP request object.
     *
     * @param version the HTTP version (e.g., "HTTP/1.1")
     * @param method the HTTP method (e.g., "GET", "POST")
     * @param path the request path
     * @param headers the request headers
     * @param body the request body
     */
    public HttpResponse(String version, int StatusCode, String StatusMessage, Map<String, String> headers, byte[] body){
        this.version = version;
        this.StatusCode = StatusCode;
        this.StatusMessage = StatusMessage;
        this.headers = headers;
        this.body = body;
    }

    public String getVersion(){
        return version;
    }

    public int getStatusCode(){
        return StatusCode;
    }

    public String getStatusMessage(){
        return StatusMessage;
    } 

    public Map<String, String> getHeaders(){
        return headers;
    }

    public byte[] getBody(){
        return body;
    }
}