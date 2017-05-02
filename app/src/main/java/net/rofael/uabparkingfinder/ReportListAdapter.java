package net.rofael.uabparkingfinder;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by aleez on 5/1/2017.
 */

public class ReportListAdapter extends ArrayAdapter<Report> {

    ReportListAdapter(Activity context, ArrayList<Report> replist)
    {
        super(context,R.layout.parking_report_list,replist);

        this.context = context;
        this.reportList = replist;
    }

    public View getView(final int position, View view, ViewGroup parent)
    {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.parking_report_list,null,true);

        //Recover layout components
        ImageView status = (ImageView) rowView.findViewById(R.id.status_indicator);
        TextView statusText = (TextView) rowView.findViewById(R.id.status_text);
        TextView reportText = (TextView) rowView.findViewById(R.id.time_of_report);

        Report rep = reportList.get(position);

        status.setImageResource(R.drawable.unk);
        statusText.setText(rep.readableLastReportTime());
        reportText.setText(R.string.status_pending);

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

        reportText.setText(rep.viewStatus());

        return rowView;
    }

    private Activity context;
    private ArrayList<Report> reportList;
}
