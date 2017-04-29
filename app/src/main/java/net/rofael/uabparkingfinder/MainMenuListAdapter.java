package net.rofael.uabparkingfinder;

import android.net.Uri;
import java.util.ArrayList;
import android.app.Activity;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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

    public View getView(int position, View view, ViewGroup parent)
    {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.main_menu_list,null,true);

        ImageView mapImg = (ImageView) rowView.findViewById(R.id.map);
        TextView name = (TextView) rowView.findViewById(R.id.parking_name);

        Uri placeholder = Uri.parse("https://maps.googleapis.com/maps/api/staticmap?center=Brooklyn+Bridge,New+York,NY");
        mapImg.setImageURI(placeholder);
        name.setText(lotList.get(position).toString());
        return rowView;
    }

    private Activity context;
    private ArrayList<Parking> lotList;
}
