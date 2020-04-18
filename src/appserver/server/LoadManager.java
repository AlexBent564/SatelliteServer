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
        
        int numberSatellites = satellites.length();
        int index;
        
        synchronized (satellites) {
            // implement policy that returns the satellite name according to a round robin methodology
            // ...
            //loop through array list of sattelites (sattelitemanager has connectivity info)
            //until rach end of array list, if at end of array list restart to begin at beginning
            
            for( index = 0; index < numberSatellites; index++ )
            {
                return satellites[index];
            }
        }
        //filler value 'null'; return something else
        return null; // ... name of satellite who is supposed to take job
        
    }
}
