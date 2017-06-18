package net.rofael.uabparkingfinder;

import java.util.ArrayList;
import java.util.Collections;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.AdapterView.*;
import android.widget.AdapterView;
import android.widget.GridView;
import android.view.View;

import android.widget.Button;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.database.*;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.Locale;
import java.util.Map;

public class ParkingActivity extends AppCompatActivity implements OnItemSelectedListener {

    private Parking lot;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking);

        // Receives name of parking lot from selection in main menu
        lot = (Parking) getIntent().getParcelableExtra("Parking");
        TextView setParkingName = (TextView) findViewById(R.id.parking_name);
        TextView setParkingStatus = (TextView) findViewById(R.id.parking_status);

        // Sets text based on the selection
        setParkingName.setText(lot.toString());
        setParkingStatus.setText(R.string.parking_status);

        // Sets up the drop down box for report selection
        final Spinner dropDownBox = (Spinner) findViewById(R.id.status_selection);
        ArrayAdapter<CharSequence> dropDownBoxAdapter = ArrayAdapter.createFromResource(this, R.array.reports_array, android.R.layout.simple_spinner_item);
        dropDownBoxAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropDownBox.setAdapter(dropDownBoxAdapter);
        dropDownBox.setOnItemSelectedListener(this);
        drop = dropDownBox;

        // Initializes connection to Google Firebase and checks for reports from server
        mDatabase = FirebaseDatabase.getInstance().getReference();
        checkFirebase();

        // Initializes the Send button
        Button confirmClick = (Button) findViewById(R.id.send_staus);
        confirmClick.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                addToList();
            }
        });

        reportListAdapter = new ReportListAdapter(this,reports);
        final ListView reportList = (ListView) findViewById(R.id.recent_reports_table);
        reportList.setAdapter(reportListAdapter);

        // Code for the swipe refresh
        final SwipeRefreshLayout listSwipe = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_report_list);
        listSwipe.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        reportListAdapter.notifyDataSetChanged();
                        listSwipe.setRefreshing(false);
                    }
                }
        );

        // Populates map image
        ImageView map = (ImageView) findViewById(R.id.directions);
        map.setImageResource(R.drawable.unk);

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_DENIED) {
            currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            double currlat = currentLocation.getLatitude();
            double currlon = currentLocation.getLongitude();
            String build = String.format(Locale.ENGLISH,"https://maps.googleapis.com/maps/api/staticmap?markers=color:blue||%f,%f&markers=color:red|%f,%f&size=300x300",currlat,currlon,lot.getLat(),lot.getLon());
            Picasso.with(this).load(build).into(map);
        }
        else
        {
            String build = String.format(Locale.ENGLISH,"https://maps.googleapis.com/maps/api/staticmap?center=%f,%f&zoom=13&size=300x300&markers=color:red|%f,%f",lot.getLat(),lot.getLon(),lot.getLat(),lot.getLon());
            Picasso.with(this).load(build).into(map);
        }


        map.bringToFront();

        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String gmmIntentUri = String.format(Locale.ENGLISH,"https://www.google.com/maps/search/?api=1&query=%f,%f",lot.getLat(),lot.getLon());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(gmmIntentUri));
                startActivity(mapIntent);
            }
        });

    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Parking Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {

    }

    public void onNothingSelected(AdapterView<?> parent) {

    }

    // Creates a new report and sends it to Firebase
    public void addToList() {
        reportType = drop.getSelectedItemPosition() - 1;
        Report rep = new Report(lot,reportType);
        if (reportType > -1 && reportType < 3) {
            mDatabase.child(lot.toString()).push().setValue(rep);
            checkFirebase();
        }



    }

    // Checks reports from Google Firebase. Downloads them if we don't have them.
    private void checkFirebase()
    {

        mDatabase.child(lot.toString()).limitToLast(10).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot reportSnapshot: dataSnapshot.getChildren())
                {
                    // Adds a report from Firebase to the list if it hasn't been downloaded
                    long reportTime = (long) reportSnapshot.child("reportTime").getValue();
                    long reportStatus = (long) reportSnapshot.child("status").getValue();
                    int reportStat = Integer.parseInt(Long.toString(reportStatus));
                    Report rep = new Report(lot,reportStat,reportTime);
                    if (!reports.contains(rep))
                    {
                        reports.add(rep);
                    }
                }

                // Populates the text list of reports based on the report list
                Collections.sort(reports, new ReportComparator());
                reportListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    private int reportType;
    private ArrayList<Report> reports = new ArrayList<Report>();
    private ReportListAdapter reportListAdapter;
    private Spinner drop;
    private DatabaseReference mDatabase;
    private Location currentLocation;
}
