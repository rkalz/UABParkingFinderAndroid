package net.rofael.uabparkingfinder;

import android.os.AsyncTask;
import android.util.JsonReader;

import org.json.JSONArray;
import org.json.JSONTokener;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

/**
 * Created by aleez on 7/22/2017.
 */

public class GetParkingDataTask extends AsyncTask<Void, Void, JSONArray> {

    @Override
    protected JSONArray doInBackground(Void... voids)
    {
        JSONArray out = null;
        try {
            URL url = new URL("http://rofael.net/projects/uabpf/parking.json");
            URLConnection connect = url.openConnection();
            InputStream dataStream = connect.getInputStream();

            Scanner s = new Scanner(dataStream).useDelimiter("\\A");
            String data = s.hasNext() ? s.next() : "";
            s.close();
            dataStream.close();

            out = new JSONArray(data);
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return out;
    }
}
