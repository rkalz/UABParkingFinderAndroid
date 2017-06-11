package net.rofael.uabparkingfinder;


import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
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
import com.squareup.picasso.Picasso;


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

        // Recover the different components of our layout
        ImageView gmap = (ImageView) rowView.findViewById(R.id.map);
        TextView name = (TextView) rowView.findViewById(R.id.parking_name);
        final TextView lastReport = (TextView) rowView.findViewById(R.id.last_report_time);
        final ImageView status = (ImageView) rowView.findViewById(R.id.status_indicator);
        TextView cats = (TextView) rowView.findViewById(R.id.categories);
        TextView dist = (TextView) rowView.findViewById(R.id.distance);

        // Assign values to the different components
        gmap.setImageResource(R.drawable.unk);
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
                    switch (rep.getStatus())
                    {
                        case 0:
                            status.setImageResource(R.drawable.low);
                            break;
                        case 1:
                            status.setImageResource(R.drawable.med);
                            break;
                        case 2:
                            status.setImageResource(R.drawable.high);
                            break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        int locPerm = ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION);
        if (locPerm == PackageManager.PERMISSION_DENIED)
        {
            dist.setText("");
        }
        else
        {
            currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            parkingLocation = new Location(LocationManager.GPS_PROVIDER);
            parkingLocation.setLatitude(lotList.get(position).getLat());
            parkingLocation.setLongitude(lotList.get(position).getLon());
            float distance = currentLocation.distanceTo(parkingLocation);
            distance = distance / (float) 0.3048;
            if (distance > 5280) {
                distance = distance / (float) 5280;
                distance = (float) Math.round(distance * 100) / 100;
                dist.setText(Float.toString(distance) + "mi");
            }
            else {
                distance = (float) Math.round(distance * 100) / 100;
                dist.setText(Float.toString(distance) + "ft");
            }

        }

        // Set photo to map location
        String url = "https://maps.googleapis.com/maps/api/staticmap?center=";
        if (parkingLocation == null)
        {
            url = "https://maps.googleapis.com/maps/api/staticmap?center=University+of+Alabama+at+Birmingham&zoom=13&size=75x75";
        }
        else
        {
            url = url + Double.toString(parkingLocation.getLatitude()) + "," + Double.toString(parkingLocation.getLongitude()) + "&zoom=13&size=75x75";
        }
        Picasso.with(context).load(url).into(gmap);

        return rowView;
    }

    private Activity context;
    private ArrayList<Parking> lotList;
    private Location currentLocation;
    private Location parkingLocation;
}
