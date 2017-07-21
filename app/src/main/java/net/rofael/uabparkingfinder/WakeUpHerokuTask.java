package net.rofael.uabparkingfinder;

import android.os.AsyncTask;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by aleez on 7/21/2017.
 */

public class WakeUpHerokuTask extends AsyncTask<Void, Void, Void> {

    protected Void doInBackground(Void... voids) {

        try {
            URL url = new URL("http://uab-parking-finder-server.herokuapp.com");
            URLConnection connect = url.openConnection();
            InputStream data = connect.getInputStream();

            data.close();
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }
}
