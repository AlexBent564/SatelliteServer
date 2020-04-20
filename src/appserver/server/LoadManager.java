package appserver.server;

import java.util.ArrayList;
import appserver.satellite.Satellite;

/**
 *
 * @author Dr.-Ing. Wolf-Dieter Otte
 */
public class LoadManager
{

    static ArrayList satellites = null;
    static int lastSatelliteIndex;
    
    int nextIndex = -1;
    


    public LoadManager()
    {
        satellites = new ArrayList<String>();
    }

    public void satelliteAdded(String satelliteName)
    {
        // add satellite
        // ...
        
        if(!satellites.contains(satelliteName))
        {
            satellites.add(satelliteName);
            System.out.println("Satellite added: " + satelliteName);
            lastSatelliteIndex = (satellites.size() - 1);
        }
    }


    public String nextSatellite() throws Exception
    {

        nextIndex += 1;
        int numberSatellites = satellites.size();
//      int nextIndex = index + 1;
        int firstIndex = 0;
        String satelliteName; 
//      String satelliteNext = (String) satellites.get(index + 1);


        for( int test = 0; test < numberSatellites - 1; test ++ )
        {
            System.out.println(satelliteName);

        }
        
//        for( int test = 0; test < numberSatellites - 1; test ++ )
//        {
//            System.out.println(satelliteName);
//
//        }

        synchronized (satellites)
        {
            // implement policy that returns the satellite name according to a round robin methodology
            // ...
            
            if( nextIndex > lastSatelliteIndex )
            {
                nextIndex = firstIndex;
                satelliteName = (String) satellites.get(nextIndex);
                return satelliteName;
            }
                
            else
            {
                return satelliteName;
            }
            
        }
             // ... name of satellite who is supposed to take job
            
    }
        //filler value 'null'; return something else
        
}