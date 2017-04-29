package net.rofael.uabparkingfinder;

import android.net.Uri;
import java.util.ArrayList;
import java.util.Collections;

import android.app.Activity;
import android.media.Image;
import android.provider.ContactsContract;
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

/**
 * Created by aleez on 4/28/2017.
 */

public class MainMenuListAdapter extends ArrayAdapter<Parking> {

    public MainMenuListAdapter(Activity context, ArrayList<Parking> lotList)
    {
        super(context,R.layout.main_menu_list,lotList);

        this.context = context;
        this.lotList = lotList;
    }

    public View getView(final int position, View view, ViewGroup parent)
    {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.main_menu_list,null,true);

        ImageView mapImg = (ImageView) rowView.findViewById(R.id.map);
        TextView name = (TextView) rowView.findViewById(R.id.parking_name);
        final TextView lastReport = (TextView) rowView.findViewById(R.id.last_report_time);
        ImageView status = (ImageView) rowView.findViewById(R.id.status_indicator) ;

        mapImg.setImageResource(R.drawable.unk);
        name.setText(lotList.get(position).toString());
        status.setImageResource(R.drawable.unk);


        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child(lotList.get(position).toString()).limitToLast(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot reportSnapshot : dataSnapshot.getChildren()) {
                    // Adds a report from Firebase to the list if it hasn't been downloaded
                    long reportTime = (long) reportSnapshot.child("reportTime").getValue();
                    long reportStatus = (long) reportSnapshot.child("status").getValue();
                    int reportStat = Integer.parseInt(Long.toString(reportStatus));
                    Report rep = new Report(lotList.get(position), reportStat, reportTime);
                    if (rep != null) {
                        lastReport.setText("Last report: " + rep.readableLastReportTime());
                    }
                    else
                    {
                        lastReport.setText("No reports yet");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });

        return rowView;
    }

    private Activity context;
    private ArrayList<Parking> lotList;
}
