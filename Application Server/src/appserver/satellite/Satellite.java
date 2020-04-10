package appserver.satellite;

import appserver.job.Job;
import appserver.comm.ConnectivityInfo;
import appserver.job.UnknownToolException;
import appserver.comm.Message;
import static appserver.comm.MessageTypes.JOB_REQUEST;
import static appserver.comm.MessageTypes.REGISTER_SATELLITE;
import appserver.job.Tool;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.PropertyHandler;

/**
 * Class [Satellite] Instances of this class represent computing nodes that execute jobs by
 * calling the callback method of tool a implementation, loading the tool's code dynamically over a network
 * or locally from the cache, if a tool got executed before.
 *
 * @author Dr.-Ing. Wolf-Dieter Otte
 */
public class Satellite extends Thread {

    private ConnectivityInfo satelliteInfo = new ConnectivityInfo();
    private ConnectivityInfo serverInfo = new ConnectivityInfo();
    private HTTPClassLoader classLoader = null;
    private Hashtable toolsCache = null;
    static ServerSocket serverSocket;
    static int port;
    protected Socket socket;

    public Satellite(String satellitePropertiesFile, String classLoaderPropertiesFile, String serverPropertiesFile) {

        // read this satellite's properties and populate satelliteInfo object,
        // which later on will be sent to the server
        try
        {
            PropertyHandler satelliteConfiguration = new PropertyHandler( satellitePropertiesFile );
            // get properties
            String satelliteName = satelliteConfiguration.getProperty( "NAME" );
            int satellitePort = Integer.parseInt( satelliteConfiguration.getProperty( "PORT" ) );
            // populate satelliteInfo object
            satelliteInfo.setName( satelliteName );
            satelliteInfo.setPort( satellitePort );
        }
        catch( IOException e )
        {
            // couldn't find satellite config file so exit
            System.out.println( "[Satellite.Satellite] Could not find sattelite config file, bailing now..." );
            e.printStackTrace();
            System.exit( 1 );
        }
        
        
        // read properties of the application server and populate serverInfo object
        // other than satellites, the as doesn't have a human-readable name, so leave it out
        try
        {
            PropertyHandler serverConfiguration = new PropertyHandler( serverPropertiesFile );
            // get properties
            String serverHost = serverConfiguration.getProperty( "HOST" );
            int serverPort = Integer.parseInt( serverConfiguration.getProperty( "PORT" ) );
            // populate serverInfo object
            serverInfo.setHost( serverHost );
            serverInfo.setPort( serverPort );
        }
        catch( IOException e )
        {
            // couldn't find server config file so exit
            System.out.println( "[Satellite.Satellite] Could not find server properties file, bailing now..." );
            e.printStackTrace();
            System.exit( 1 );
        }
        
        
        // read properties of the code server and create class loader
        // -------------------
        try
        {
            PropertyHandler classLoaderConfiguration = new PropertyHandler( classLoaderPropertiesFile );
            // get properties
            String classLoaderHost = classLoaderConfiguration.getProperty( "HOST" );
            int classLoaderPort = Integer.parseInt( classLoaderConfiguration.getProperty( "PORT" ) );
            // create class loader object
            classLoader = new HTTPClassLoader( classLoaderHost, classLoaderPort );
        }
        catch( IOException e )
        {
            // couldn't find server config file so exit
            System.out.println( "[Satellite.Satellite] Could not find class loader properties file, bailing now..." );
            e.printStackTrace();
            System.exit( 1 );
        }
        // check if classLoader is empty
        if( classLoader == null )
        {
            System.out.println( "[Satellite.Satellite] Could not create HTTPClassLoader, bailing now..." );
            System.exit( 1 );
        }

        
        // create tools cache
        // -------------------
        
        
    }

    @Override
    public void run() {
        
        // ignore registering the satellite for now
        // register this satellite with the SatelliteManager on the server
        // ---------------------------------------------------------------
        // ...
        
        
        // create server socket
        // ---------------------------------------------------------------
        try
        {
            port = serverInfo.getPort();
            serverSocket = new ServerSocket( port );
            System.out.println( "[Satellite.run] Server socket created on port #" + port );
        }
        catch( IOException e )
        {
            System.out.println( "[Satellite.run] Could not create server socket..." );
            e.printStackTrace();
            System.exit( 1 );
        }
        
        
        // start taking job requests in a server loop
        // ---------------------------------------------------------------
        while( true )
        {
            try
            {
                System.out.println( "[Satellite.run] Waiting for connections on port #" + port );
                socket = serverSocket.accept();
                System.out.println( "[Satellite.run] A connection has been established!" );
                
            }
            catch( IOException e )
            {
                System.out.println( "[Satellite.run] Could not accept any connections on port #" + port );
                e.printStackTrace();
                System.exit( 1 );
            }
        }
    }

    // inner helper class that is instanciated in above server loop and processes single job requests
    private class SatelliteThread extends Thread {

        Satellite satellite = null;
        Socket jobRequest = null;
        ObjectInputStream readFromNet = null;
        ObjectOutputStream writeToNet = null;
        Message message = null;

        SatelliteThread(Socket jobRequest, Satellite satellite) {
            this.jobRequest = jobRequest;
            this.satellite = satellite;
        }

        @Override
        public void run() {
            // setting up object streams
            // ...
            
            // reading message
            // ...
            
            switch (message.getType()) {
                case JOB_REQUEST:
                    // processing job request
                    // ...
                    break;

                default:
                    System.err.println("[SatelliteThread.run] Warning: Message type not implemented");
            }
        }
    }

    /**
     * Aux method to get a tool object, given the fully qualified class string
     * If the tool has been used before, it is returned immediately out of the cache,
     * otherwise it is loaded dynamically
     */
    public Tool getToolObject(String toolClassString) throws UnknownToolException, ClassNotFoundException, InstantiationException, IllegalAccessException {

        Tool toolObject = null;

        // ...
        
        return toolObject;
    }

    public static void main(String[] args) {
        // start the satellite
        Satellite satellite = new Satellite(args[0], args[1], args[2]);
        satellite.run();
    }
}
