package net.rofael.uabparkingfinder;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by aleez on 4/29/2017.
 */

class GMapsImage extends AsyncTask<String, Void, Bitmap> {

    protected Bitmap doInBackground(String... urls) {
        try {
            URL src = new URL(urls[0]);
            HttpsURLConnection conn = (HttpsURLConnection) src.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream in = conn.getInputStream();
            return BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void onPostExecute(Bitmap bmp)
    {

    }





}
