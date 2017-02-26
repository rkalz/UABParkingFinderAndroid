package net.rofael.uabparkingfinder;

import java.util.ArrayList;
import java.util.Collections;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.lang.reflect.Array;
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

        lot = (Parking) getIntent().getParcelableExtra("Parking");
        TextView setParkingName = (TextView) findViewById(R.id.parking_name);
        TextView setParkingStatus = (TextView) findViewById(R.id.parking_status);

        setParkingName.setText(lot.toString());
        setParkingStatus.setText(lot.viewStatus());

        final Spinner dropDownBox = (Spinner) findViewById(R.id.status_selection);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.reports_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropDownBox.setAdapter(adapter);

        dropDownBox.setOnItemSelectedListener(this);
        drop = dropDownBox;

        final ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, reportData);
        final GridView list = (GridView) findViewById(R.id.recent_reports_table);
        gridList = list;
        stringListAdapter = adapter2;
        list.setNumColumns(2);
        list.setAdapter(adapter2);
        reportData.add("Time");
        reportData.add("Report");
        for (int i = 0; i < 20; i++)
        {
            reportData.add("");
        }

        SharedPreferences sharedPrefs = getPreferences(Context.MODE_PRIVATE);
        final SharedPreferences.Editor edit = sharedPrefs.edit();
        String commitName;

        mDatabase = FirebaseDatabase.getInstance().getReference();
        checkFirebase();

        Button confirmClick = (Button) findViewById(R.id.send_staus);
        confirmClick.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                addToList();
            }
        });

        final SwipeRefreshLayout listSwipe = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_report_list);

        listSwipe.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        if (reports.size() > 0)
                        {
                            for (int i = 2; i < reportData.size(); i = i + 2)
                            {
                                reportData.set(i, reports.get((i / 2) - 1).readableLastReportTime());
                            }

                            adapter2.notifyDataSetChanged();
                            listSwipe.setRefreshing(false);
                        }
                        listSwipe.setRefreshing(false);
                    }
                }
        );


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

    public void addToList() {
        reportType = drop.getSelectedItemPosition() - 1;
        if (reportType > -1 && reportType < 3) {
            reports.add(new Report(lot, reportType));
            mDatabase.child(lot.toString()).push().setValue(reports.get(reports.size() - 1));
            checkFirebase();
        }



    }

    public void checkFirebase()
    {

        mDatabase.child(lot.toString()).limitToLast(10).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot reportSnapshot: dataSnapshot.getChildren())
                {
                    long reportTime = (long) reportSnapshot.child("reportTime").getValue();
                    long reportStatus = (long) reportSnapshot.child("status").getValue();
                    int reportStat = Integer.parseInt(Long.toString(reportStatus));
                    Report rep = new Report(lot,reportStat,reportTime);
                    if (!reports.contains(rep))
                    {
                        reports.add(rep);
                    }
                }

                Collections.sort(reports, new ReportComparator());
                if (reports.size() > 0)
                {
                    for (int i = 2; i < reportData.size(); i = i + 2) {
                        reportData.set(i, reports.get((i / 2) - 1).readableLastReportTime());
                        reportData.set(i + 1, reports.get((i / 2) - 1).viewStatus());
                        stringListAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    private int reportType;
    private ArrayList<Report> reports = new ArrayList<Report>();
    private ArrayList<String> reportData = new ArrayList<String>();
    private GridView gridList;
    private ArrayAdapter<String> stringListAdapter;
    private Spinner drop;
    private DatabaseReference mDatabase;
}
