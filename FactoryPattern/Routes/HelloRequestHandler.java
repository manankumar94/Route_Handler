package FactoryPattern.Routes;
import java.util.Collections;

import FactoryPattern.Requests.HttpRequest;
import FactoryPattern.HttpRequestHandler;
import FactoryPattern.Responses.HttpResponse;

/**
 * Handles HTTP requests for the "/hello" endpoint.
 */
public class HelloRequestHandler implements HttpRequestHandler{
    @Override
    public HttpResponse handleRequest(HttpRequest request) throws Exception{
        if(request.getMethod().equals("GET")){
            String ResponseBodyLine1 = "HTTP/1.1 \r\n";
            String ResponseBodyLine2 = "\r\n";
            byte[] body = (ResponseBodyLine1 + ResponseBodyLine2 + "Hello World").getBytes();
            return new HttpResponse(request.getVersion(), 200, "OK", Collections.emptyMap(), body);
        }else{
            return new HttpResponse(request.getVersion(), 405, "Method Not Allowed", Collections.emptyMap(), null);
        }
    }
}
