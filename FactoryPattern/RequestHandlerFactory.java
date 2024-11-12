package FactoryPattern;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import FactoryPattern.Requests.HttpRequest;
import FactoryPattern.Responses.HttpResponse;
import FactoryPattern.Routes.*;

/**
 * Factory class responsible for creating request handlers and submitting requests.
 */
public class RequestHandlerFactory {

    private final ExecutorService executorService;

    /**
     * Constructs a RequestHandlerFactory with the provided executor service.
     *
     * @param executorService the executor service for handling requests concurrently
     */
    public RequestHandlerFactory(ExecutorService executorService) {
        this.executorService = executorService;
    }

    /**
     * Creates a Callable object that handles the request based on its path.
     *
     * @param request the HTTP request object
     * @param path the request path
     * @return a Callable that handles the request
     */
    public Callable<HttpResponse> createHandler(HttpRequest request, String path){
        switch (path){
            case "/hello":
                return () -> new HelloRequestHandler().handleRequest(request);
            case "/image":
                return () -> new ImageRequestHandler().handleRequest(request);
            case "/echo/":
                return () -> new EchoRequestHandler().handleRequest(request);
            default:
                return () -> new DefaultRequestHandler().handleRequest(request);
        }
    }

    /**
     * Submits a request for handling using the ExecutorService and returns a Future object.
     * @param request The HttpRequest object to be handled.
     * @return A Future object that can be used to retrieve the HttpResponse later.
     * @throws Exception If there's an error during request processing.
     */
    public Future<HttpResponse> submitRequest(HttpRequest request) throws Exception {
        Callable<HttpResponse> handlerCallable = createHandler(request, request.getPath());
        return executorService.submit(handlerCallable);
    }

    /**
     * Handles a client request by parsing the request, creating a handler, and sending the response.
     * @param clientSocket The Socket object representing the client connection.
     * @throws Exception If there's an error during request processing.
     */
    public void handleClientRequest(Socket clientSocket) throws Exception{

        // Create a request object from the incoming socket
        HttpRequest request = parse(new InputStreamReader(clientSocket.getInputStream()), clientSocket);

        HttpRequestHandler handler = (HttpRequestHandler) createHandler(request, request.getPath());

        // Handle the request and send the response
        HttpResponse response = handler.handleRequest(request);

        OutputStream outputStream = clientSocket.getOutputStream();

        // outputStream.write("HTTP/1.1".getBytes());
        outputStream.write(response.getBody());
        clientSocket.close();
    }

    /**
     * Parses the request from the client socket and creates an HttpRequest object.
     * @param inputStreamReader The InputStreamReader object for reading the request data.
     * @param client The Socket object representing the client connection.
     * @return An HttpRequest object containing the parsed request information.
     * @throws Exception If there's an error during request parsing.
     */
    public static HttpRequest parse(InputStreamReader inputStreamReader, Socket client) throws Exception {
        inputStreamReader = new InputStreamReader(client.getInputStream());
        BufferedReader reader = new BufferedReader(inputStreamReader);
    
        StringBuilder requestLine = new StringBuilder();
        String line;
        line = reader.readLine();
        while(!line.isBlank()){
            requestLine.append(line + "\r\n");
            line = reader.readLine();
        }

        // This line will contain the first line 
        String[] ArrayOfRequestLines = requestLine.toString().split("/n");

        // The below 2 lines will extract the Method and path from the first line
        String method = ArrayOfRequestLines[0].split(" ")[0];
        String path = ArrayOfRequestLines[0].split(" ")[1];
        String version = ArrayOfRequestLines[0].split(" ")[2];
        
        /*
         * Extracting the headers part
         */
        Map<String, String> headers = new HashMap<>();
        for(String item : ArrayOfRequestLines){
            if(item.contains(": ")){
                String[] parts = item.split(": ");
                headers.put(parts[0], parts[1]);
            }
        }

        StringBuilder body = new StringBuilder();
        if (headers.containsKey("Content-Length")) {
            int contentLength = Integer.parseInt(headers.get("Content-Length"));
            for (int i = 0; i < contentLength; i++) {
                body.append((char) reader.read());
            }
        }
    
        return new HttpRequest(version, method, path, headers, body.toString());
    }
}
