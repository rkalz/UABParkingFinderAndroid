package net.rofael.uabparkingfinder;

import android.os.Parcelable;
import android.os.Parcel;

/**
 * Created by Rofael on 2/24/2017.
 */

class Parking implements Parcelable {
    private String name;
    private int status;
    private double lat;
    private double lon;

    Parking(String lotName)
    {
        name = lotName;
        status = -1;
        lat = 33.5021227;
        lon = -86.8086334;
    }

    Parking(String inName, double inLat, double inLon)
    {
        name = inName;
        status = -1;
        lat = inLat;
        lon = inLon;
    }

    Parking(Parcel in) {
        name = in.readString();
        status = in.readInt();
        lat = in.readDouble();
        lon = in.readDouble();
    }

    public String toString()
    {
        return name;
    }

    public double getLat() { return lat; }

    public double getLon() { return lon; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(status);
        dest.writeDouble(lat);
        dest.writeDouble(lon);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Parking> CREATOR = new Parcelable.Creator<Parking>() {
        @Override
        public Parking createFromParcel(Parcel in) {
            return new Parking(in);
        }

        @Override
        public Parking[] newArray(int size) {
            return new Parking[size];
        }
    };
}

