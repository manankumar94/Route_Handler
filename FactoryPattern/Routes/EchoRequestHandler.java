package FactoryPattern.Routes;
import java.util.*;
import FactoryPattern.Requests.HttpRequest;
import FactoryPattern.HttpRequestHandler;
import FactoryPattern.Responses.HttpResponse;

/**
 * Handles HTTP requests for the "/echo" endpoint.
 */
public class EchoRequestHandler implements HttpRequestHandler{
    @Override
    public HttpResponse handleRequest(HttpRequest request) throws Exception{
        if (request.getMethod().equals("GET")) {
            String ResponseBodyLine1 = "HTTP/1.1 \r\n";
            String ResponseBodyLine2 = "\r\n";

            /*
             * Extracting the String after `/echo/` endpoint
             */
            String path = request.getPath();
            String[] pathParts = path.split("/");
            String message = pathParts.length > 2 ? pathParts[2] : "";
            
            String responseBody = ResponseBodyLine1 + ResponseBodyLine2 + "Hello " + message;
            return new HttpResponse(request.getVersion(), 200, "OK", Collections.emptyMap(), responseBody.getBytes());
        } else {
            return new HttpResponse(request.getVersion(), 405, "Method Not Allowed", Collections.emptyMap(), null);
        }
    }
}
