package appserver.server;

import appserver.comm.Message;
import static appserver.comm.MessageTypes.JOB_REQUEST;
import static appserver.comm.MessageTypes.REGISTER_SATELLITE;
import appserver.comm.ConnectivityInfo;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import utils.PropertyHandler;

/**
 *
 * @author Dr.-Ing. Wolf-Dieter Otte
 */
public class Server {

    // Singleton objects - there is only one of them. For simplicity, this is not enforced though ...
    static SatelliteManager satelliteManager = null;
    static LoadManager loadManager = null;
    static ServerSocket serverSocket = null;
    
    // port we will create server socket with
    int port;

    public Server(String serverPropertiesFile) {

        // create satellite manager and load manager
        satelliteManager = new SatelliteManager();
        loadManager = new LoadManager();
        
        // read server properties and create server socket
        try
        {
            Properties serverProperties = new PropertyHandler( serverPropertiesFile );
            port = Integer.parseInt( serverProperties.getProperty( "PORT" ) );
            serverSocket = new ServerSocket( port );
            System.out.println( "[Server.Server] Created server socket on port #" + port );
        }
        catch( IOException e )
        {
            System.out.println( "[Server.Server] Could not find server properties file, bailing now..." );
            e.printStackTrace();
            System.exit( 1 );
        }
        this.port = port;
    }

    public void run() {
        // serve clients in server loop ...
        // when a request comes in, a ServerThread object is spawned
        try
        {
            while( true )
            {
                System.out.println( "[Server.run] Waiting for connections on port #" + port );
                serverSocket.accept();
                System.out.println( "[Server.run] Client connection established!" );
                // is this where the serverThread runs?
                    // definitely not sure about this 
                ServerThread serverThread = new ServerThread(serverSocket.accept());
                serverThread.run();
            }
        }
        catch( IOException e )
        {
            System.out.println( "[Server.run] Error occured while accepting clients, bailing now..." );
            e.printStackTrace();
            System.exit( 1 );
        }
    }

    // objects of this helper class communicate with satellites or clients
    private class ServerThread extends Thread {

        
        Socket client = null;
        ObjectInputStream readFromNet = null;
        ObjectOutputStream writeToNet = null;
        Message message = null;
        
        //added variables that seemed necessary
        ObjectInputStream readFromSatellite = null;
        ObjectOutputStream writeToSatellite = null;
        
        // created this variable to keep track of the satelliteInfo, NOT SURE if that is correct
        ConnectivityInfo satelliteInfo;

        private ServerThread(Socket client) {
            this.client = client;
        }

        @Override
        public void run() {
            // set up object streams and read message
            try
            {
                readFromNet = new ObjectInputStream( client.getInputStream() );
                writeToNet = new ObjectOutputStream( client.getOutputStream() );
                System.out.println( "[ServerThread.run] Object streams successfully set up, reading message now..." );
                message = ( Message )readFromNet.readObject();
                System.out.println( "[ServerThread.run] Message read successfully!" );
            }
            catch( IOException | ClassNotFoundException e )
            {
                System.out.println( "[ServerThread.run] Error creating object streams or reading message, bailing now..." );
                e.printStackTrace();
                System.exit( 1 );
            }

            
            // process message
            switch (message.getType()) {
                // not sure what file sends the REGISTER_MESSAGE message
                case REGISTER_SATELLITE:
                    // read satellite info
                    
                    satelliteInfo = (ConnectivityInfo) message.getContent();
                    // register satellite
                    synchronized (Server.satelliteManager) {
                        Server.satelliteManager.registerSatellite(satelliteInfo);
                    }

                    // add satellite to loadManager
                    synchronized (Server.loadManager) {
                        Server.loadManager.satelliteAdded(satelliteInfo.getName());
                    }

                    break;

                case JOB_REQUEST:
                    System.err.println("\n[ServerThread.run] Received job request");

                    String satelliteName = null;
                    synchronized (Server.loadManager) {
                        // get next satellite from load manager
                        try 
                        {
                            satelliteName = Server.loadManager.nextSatellite();
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                            System.out.println("[ServerThread.java] There was a problem locating the next satellite");
                        }
                        
                        // get connectivity info for next satellite from satellite manager
                        satelliteInfo = Server.satelliteManager.getSatelliteForName(satelliteName);
                    }

                    Socket satellite = null;
                    // connect to satellite
                    try
                    {
                        satellite = new Socket(satelliteInfo.getHost(), satelliteInfo.getPort());
                    }
                    catch (Exception e)
                    {
                        System.out.println("[ServerThread.java] there was a problem connecting to the satellite");
                        e.printStackTrace();
                    }

                    // open object streams,
                    // forward message (as is) to satellite,
                    // receive result from satellite and
                    // write result back to client
                    try
                    {
                        readFromSatellite = new ObjectInputStream(satellite.getInputStream());
                        writeToSatellite = new ObjectOutputStream(satellite.getOutputStream());
                        writeToSatellite.writeObject(message);
                        message = (Message)readFromSatellite.readObject();
                        writeToNet.writeObject(message);
                    }
                    catch (Exception ex)
                    {
                        System.out.println("[ServerThread.java] There was a problem reading and writing from the satellite");
                        ex.printStackTrace();
                    }
                    

                    break;

                default:
                    System.err.println("[ServerThread.run] Warning: Message type not implemented");
            }
        }
    }

    // main()
    public static void main(String[] args) {
        // start the application server
        Server server = null;
        if(args.length == 1) {
            server = new Server(args[0]);
        } else {
            server = new Server("../../config/Server.properties");
        }
        server.run();
    }
}