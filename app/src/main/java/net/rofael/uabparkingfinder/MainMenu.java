package net.rofael.uabparkingfinder;

import android.content.Context;
import android.net.Uri;
import android.os.PersistableBundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;


import java.util.ArrayList;

public class MainMenu extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        // Adds testlots to list of lots
        lots.add(testLot1);
        lots.add(testLot2);
        lots.add(testLot3);

        // Initializes table holding list of lots
        final MainMenuListAdapter adapter = new MainMenuListAdapter(this,lots);
        final ListView list = (ListView) findViewById(R.id.parking_list);

        list.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        accessNewLotMenu(list);

        // Refreshes list of lots, adding of testLot4 is for testing purposes
        final SwipeRefreshLayout mainSwipe = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_main);

        mainSwipe.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        mainSwipe.setRefreshing(false);
                        lots.add(testLot4);
                        adapter.notifyDataSetChanged();
                        accessNewLotMenu(list);
                    }
                }
        );
    }

    // When a lot is pressed, changes to that lot's menu
    private void accessNewLotMenu(ListView list)
    {
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(MainMenu.this, ParkingActivity.class);

                intent.putExtra("Parking",lots.get(position));
                startActivity(intent);

            }
        });
    }

    private ArrayList<Parking> lots = new ArrayList<>();
    private Parking testLot1 = new Parking("testLot1");
    private Parking testLot2 = new Parking("testLot2");
    private Parking testLot3 = new Parking("testLot3");
    private Parking testLot4 = new Parking("testLot4");


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("MainMenu Page") // TODO: Define a title for the content shown.
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
}
