package appserver.server;

import java.util.ArrayList;
import appserver.satellite.Satellite;

/**
 *
 * @author Dr.-Ing. Wolf-Dieter Otte
 */
public class LoadManager{

    static ArrayList satellites = null;
    static int lastSatelliteIndex = -1;


    public LoadManager() {
        satellites = new ArrayList<String>();
    }

    public void satelliteAdded(String satelliteName) {
        // add satellite
        // ...
        satellites.add(satelliteName);
    }


    public String nextSatellite() throws Exception {
        
        int numberSatellites = satellites.size();
        int index = 0;
        
        synchronized (satellites) {
            // implement policy that returns the satellite name according to a round robin methodology
            // ...
            //loop through array list of sattelites (sattelitemanager has connectivity info)
            //until rach end of array list, if at end of array list restart to begin at beginning
            
           
                
        while( true ) //TODO:now I just need to properly implement round robin
        {
            boolean done = true;
        
            for( index = 0; index < numberSatellites; index++ )
            {
                System.out.println( (String) satellites.get(index)); //test print
                return (String) satellites.get(index);
            }
            index = 0;
            done = false;
        }
             // ... name of satellite who is supposed to take job

            
        }
        //filler value 'null'; return something else
        
    }
}
