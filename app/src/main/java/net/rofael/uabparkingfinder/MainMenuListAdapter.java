package net.rofael.uabparkingfinder;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by aleez on 4/28/2017.
 */

class MainMenuListAdapter extends ArrayAdapter<Parking> {

    MainMenuListAdapter(Activity context, ArrayList<Parking> lotList)
    {
        super(context,R.layout.main_menu_list,lotList);

        this.context = context;
        this.lotList = lotList;
    }

    @NonNull
    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.main_menu_list, null, true);

        // Recover the different components of our layour
        ImageView mapImg = (ImageView) rowView.findViewById(R.id.map);
        TextView name = (TextView) rowView.findViewById(R.id.parking_name);
        final TextView lastReport = (TextView) rowView.findViewById(R.id.last_report_time);
        ImageView status = (ImageView) rowView.findViewById(R.id.status_indicator);
        TextView cats = (TextView) rowView.findViewById(R.id.categories);
        TextView dist = (TextView) rowView.findViewById(R.id.distance);

        // Assign values to the different components
        mapImg.setImageResource(R.drawable.unk);
        name.setText(lotList.get(position).toString());
        status.setImageResource(R.drawable.unk);
        cats.setText(R.string.categories);
        dist.setText(R.string.distance);
        lastReport.setText(R.string.no_reports);

        // Recover the time of the newest report
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child(lotList.get(position).toString()).limitToLast(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot reportSnapshot : dataSnapshot.getChildren()) {
                    // Downloads the latest report from Firebase
                    long reportTime = (long) reportSnapshot.child("reportTime").getValue();
                    long reportStatus = (long) reportSnapshot.child("status").getValue();
                    int reportStat = Integer.parseInt(Long.toString(reportStatus));
                    Report rep = new Report(lotList.get(position), reportStat, reportTime);

                    lastReport.setText("Last report: " + rep.readableLastReportTime());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // Set photo to map location
        try {
            GMapsImage dl = new GMapsImage();
            dl.execute("https://maps.googleapis.com/maps/api/staticmap?center=University+of+Alabama+at+Birmingham&zoom=13&size=75x75");
            Bitmap bmp = dl.get();
            mapImg.setImageBitmap(bmp);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return rowView;
    }

    private Activity context;
    private ArrayList<Parking> lotList;
}
