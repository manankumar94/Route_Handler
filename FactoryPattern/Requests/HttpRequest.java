package FactoryPattern.Requests;
import java.util.*;

/**
 * Represents an HTTP request.
 */
public class HttpRequest{
    private final String version;
    private final String method;
    private final String path;
    private static Map<String, String> headers = null;
    private final String body;

    /**
     * Constructs an HTTP request object.
     *
     * @param version the HTTP version (e.g., "HTTP/1.1")
     * @param method the HTTP method (e.g., "GET", "POST")
     * @param path the request path
     * @param headers the request headers
     * @param body the request body
     */
    public HttpRequest(String version, String method, String path, Map<String, String> headers, String body){
        this.version = version;
        this.method = method;
        this.path = path;
        HttpRequest.headers = headers;
        this.body = body;
    }

    public String getVersion(){
        return version;
    }

    public String getMethod(){
        return method;
    }

    public String getPath(){
        return path;
    } 

    public Map<String, String> getHeaders(){
        return headers;
    }

    public String getBody(){
        return body;
    }
}