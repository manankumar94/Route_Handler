import java.net.Socket;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;

public class http_server{
    public static void main(String[] args) throws Exception{
        // start receiving messages - ready to receive messages6
        try(ServerSocket serverSocket = new ServerSocket(8080)){
            System.out.println("Server Started.\nListening for messages");

            while(true){
                // Hande a new incoming message
                try(Socket client = serverSocket.accept()){
                    // client <-- messages queued up in it!!
                    System.out.println("Debug: got new message " + client.toString());

                    // Read the request - Listen to the message
                    /*
                     * The below line justs reads bytes from the input stream and decodes them 
                     * into characters
                     */
                    InputStreamReader isr = new InputStreamReader(client.getInputStream());

                    /*
                     * then, Why using the `BufferedReader`
                     * This is done to improve the efficiency of reading data from the input stream.
                     * 
                     * Here's a breakdown of why using a BufferedReader is beneficial:
                     * 
                     * 1) Buffering: The BufferedReader reads data in larger chunks (typically a buffer of characters)
                     * from the underlying InputStreamReader. This reduces the number of read operations
                     * on the underlying stream, which can be relatively slow.
                     * 
                     * 2) Line-oriented reading: The BufferedReader provides a convenient readLine() method that 
                     * reads a line of text from the stream, including the newline character.
                     *  This simplifies the process of parsing text-based data.
                     */
                    BufferedReader reader = new BufferedReader(isr);

                    /*
                     * Why use the `StringBuilder` line 
                     * 
                     * When concatenating strings repeatedly, the JVM creates new string objects for 
                     * each concatenation. This can be inefficient, especially for large strings. 
                     * The StringBuilder class avoids this by modifying the existing string object in-place.
                     */
                    StringBuilder request = new StringBuilder();
                    String line;    // Temp variable called line that holds one line at a time of our message
                    line = reader.readLine();
                    while(!line.isBlank()){
                        request.append(line + "\r\n");
                        line = reader.readLine();
                    }

                    System.out.println("--REQUEST--");
                    // System.out.println("Response Sent");
                    System.out.println(request);

                    OutputStream clientOutput = client.getOutputStream();
                    
                    // Decide how er'd like to respond
                    
                    // Change response based on route?
                    
                    // Get the first line of the request
                    String firstLine = request.toString().split("\n")[0];
                    // Get the second thing "resource" from the first line (separated by spaces)
                    String resource = firstLine.split(" ")[1];
                    
                    String[] parts = firstLine.split("/");
                    /*
                     * EG: GET /echo/hello HTTP/1.1
                     * parts[0] = ""
                     * parts[1] = "echo"
                     * parts[2] = "hello"
                     */

                    // implement the /echo/{str} endpoint, which accepts a string and returns it in the response body.
                    if(parts.length > 1 && parts[1].equals("echo")){
                        String customEndpoint = firstLine.split("/echo")[1];
                        /*
                         * [0] = [" "]
                         * [1] = ["/hello HTTP/1.1"]
                         */
                        customEndpoint = customEndpoint.split(" ")[0];
                        /*
                         * [0] = ["/hello"]
                         * [1] = ["HTTP/1.1"]
                         */
                        clientOutput.write("HTTP/1.1 200 OK \r\n".getBytes());
                        clientOutput.write("\r\n".getBytes());
                        clientOutput.write(customEndpoint.getBytes());
                        System.out.println("I'm in the custom endpoint");
                    }else{
                        if(resource.equals("/hello")){
                            // Just send back a simple "Hello World"
                            clientOutput.write("HTTP/1.1 200 OK\r\n".getBytes());
                            clientOutput.write("\r\n".getBytes());
                            clientOutput.write(("Hello World").getBytes());
                        } else if(resource.equals("/image")){
                            // Load the image from the filesystem
                            // Send back an image
                            FileInputStream image = new FileInputStream("FactoryPattern/Routes/image.jpg");
                            // System.out.println(image.toString());
                            clientOutput.write("HTTP/1.1 200 OK\r\n".getBytes());
                            clientOutput.write("\r\n".getBytes());
                            clientOutput.write((image).readAllBytes());
                            image.close();
                        } else {
                            clientOutput.write("HTTP/1.1 200 OK \r\n".getBytes());
                            clientOutput.write("\r\n".getBytes());
                            clientOutput.write("What ya lookin at? ".getBytes());
                        }
                    }
                    // System.out.println(customEndpoint);
                    // Get ready for the next message
                    client.close();
                }
            }
        }
    }
}