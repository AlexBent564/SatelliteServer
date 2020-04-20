package appserver.server;

import appserver.comm.ConnectivityInfo;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 *
 * @author Dr.-Ing. Wolf-Dieter Otte
 */
public class SatelliteManager {

    // (the one) hash table that contains the connectivity information of all satellite servers
    static private Hashtable<String, ConnectivityInfo> satellites = null;

    public SatelliteManager() {
        satellites = new Hashtable<>();
    }

    public void registerSatellite(ConnectivityInfo satelliteInfo) {
        if (satellites.contains(satelliteInfo))
        {
            System.out.println("[SatelliteManager] this satellite already exists");
        }
        else
        {
            satellites.put(satelliteInfo.getName(), satelliteInfo);
        }
        
    }

    public ConnectivityInfo getSatelliteForName(String satelliteName) {
        
        // find the satellite in the hashtable and return it
        return satellites.get(satelliteName);
    }
}