package FactoryPattern;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import FactoryPattern.Requests.HttpRequest;
import FactoryPattern.Responses.HttpResponse;

/**
 * Main class for the HTTP server. Listens for incoming connections on a specific port,
 * creates a request handler based on the request path, and sends the response back to the client.
 */
public class MainHttpServer {

    public static void main(String[] args) throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(5); // Adjust thread pool size as needed
        RequestHandlerFactory factory = new RequestHandlerFactory(executorService);

        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            System.out.println("Server started on port 8080");

            while (true) {
                try (Socket clientSocket = serverSocket.accept()) {
                    System.out.println("Client connected");

                    HttpRequest request = RequestHandlerFactory.parse(new InputStreamReader(clientSocket.getInputStream()), clientSocket);

                    // Submit the request to the factory for handling
                    Future<HttpResponse> futureResponse = factory.submitRequest(request);
                    HttpResponse response = futureResponse.get(); // Wait for the response

                    OutputStream outputStream = clientSocket.getOutputStream();
                    outputStream.write(response.getBody());
                    clientSocket.close();
                }
            }
        } finally {
            executorService.shutdown(); // Shutdown the thread pool when finished
        }
    }
}