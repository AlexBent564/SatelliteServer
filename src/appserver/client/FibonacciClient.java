
package appserver.client;

import appserver.comm.Message;
import appserver.comm.MessageTypes;
import static appserver.comm.MessageTypes.JOB_REQUEST;
import appserver.job.Job;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Properties;

import utils.PropertyHandler;

/**
 * Class [PlusOneClient] A primitive POC client that uses the PlusOne tool
 * 
 * @author Dr.-Ing. Wolf-Dieter Otte
 */
public class FibonacciClient extends Thread implements MessageTypes{
    
    String host = null;
    int port;
    int fibonacciNumber;

    Properties properties;

    public FibonacciClient(String serverPropertiesFile, int number) {
        try {
            properties = new PropertyHandler(serverPropertiesFile);
            host = properties.getProperty("HOST");
            System.out.println("[FibonacciClient.FibonacciClient] Host: " + host);
            port = Integer.parseInt(properties.getProperty("PORT"));
            System.out.println("[FibonacciClient.FibonacciClient] Port: " + port);
            this.fibonacciNumber = number;
        } catch (Exception ex) {
            System.out.println( "[FibonacciClient.FibonacciClient] Could not find properties file, bailing now..." );
            ex.printStackTrace();
            System.exit( 1 );
        }
    }
    
    public void run() {
        try { 
            // connect to application server
            System.out.println( "[FibonacciClient.run] Attempting to connect to server..." );
            Socket server = new Socket(host, port);
            System.out.println( "[FibonacciClient.run] Connected to server!" );
            
            // hard-coded string of class, aka tool name ... plus one argument
            String classString = "appserver.job.impl.Fibonacci";
            Integer number = fibonacciNumber; // change this
            
            // create job and job request message
            System.out.println( "[FibonacciClient.run] Creating job request message now..." );
            Job job = new Job(classString, number);
            Message message = new Message(JOB_REQUEST, job);
            System.out.println( "[FibonacciClient.run] Message sent successfully!" );
            
            // sending job out to the application server in a message
            System.out.println( "[FibonacciClient.run] Sending out job requeset now...");
            ObjectOutputStream writeToNet = new ObjectOutputStream(server.getOutputStream());
            writeToNet.writeObject(message);
            System.out.println( "[FibonacciClient.run] Job request successfully sent!" );
            
            // reading result back in from application server
            // for simplicity, the result is not encapsulated in a message
            System.out.println( "[FibonacciClient.run] Reading result now..." );
            ObjectInputStream readFromNet = new ObjectInputStream(server.getInputStream());
            Integer result = (Integer) readFromNet.readObject();
            System.out.println("Fibonacci of " + number + ": " + result);
        } catch (Exception ex) {
            System.err.println("[FibonacciClient.run] Error occurred");
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        
        // Spawns off 48 threads computing numbers from 1-48
        for( int index = 10; index > 0; index-- )
        {
            (new FibonacciClient( "../../config/Server.properties", index ) ).start();
        }
    }  
}
