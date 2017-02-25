package net.rofael.uabparkingfinder;

import android.os.Parcelable;
import android.os.Parcel;

/**
 * Created by Rofael on 2/24/2017.
 */

public class Parking implements Parcelable {
    private String name;
    private int status;

    public Parking(String lotName)
    {
        name = lotName;
        status = -1;
    }

    protected Parking(Parcel in) {
        name = in.readString();
        status = in.readInt();
    }

    public String toString()
    {
        return name;
    }

    public void changeStatus(int newStat)
    {
        status = newStat;
    }

    public int getStatus()
    {
        return status;
    }

    public String viewStatus()
    {
        if (status == -1)
        {
            return "Pending";
        }
        else if (status == 0)
        {
            return "Somewhat empty";
        }
        else if (status == 1)
        {
            return "Filling up";
        }
        else if (status == 2)
        {
            return "Almost full";
        }

        return null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(status);
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

