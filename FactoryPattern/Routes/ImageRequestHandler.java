package FactoryPattern.Routes;
import java.io.FileInputStream;
import java.util.Collections;

import FactoryPattern.Requests.HttpRequest;
import FactoryPattern.HttpRequestHandler;
import FactoryPattern.Responses.HttpResponse;

/**
 * Handles HTTP requests for the "/image" endpoint.
 */
public class ImageRequestHandler implements HttpRequestHandler {
    @Override
    public HttpResponse handleRequest(HttpRequest request) throws Exception {
        if (request.getMethod().equals("GET")) {
            FileInputStream image = new FileInputStream("FactoryPattern/Routes/image.jpg");
            byte[] imageData = image.readAllBytes();
            image.close();

            String statusMessage = "HTTP/1.1 200 OK\r\n";
            String contentType = "Content-Type: image/jpg\r\n";
            String contentLength = "Content-Length: " + imageData.length + "\r\n";

            String body = statusMessage + contentType + contentLength + "\r\n" + new String(imageData);

            return new HttpResponse(request.getVersion(), 200, "OK", Collections.emptyMap(), body.getBytes());
        } else {
            return new HttpResponse(request.getVersion(), 405, "Method Not Allowed", Collections.emptyMap(), null);
        }
    }
}
