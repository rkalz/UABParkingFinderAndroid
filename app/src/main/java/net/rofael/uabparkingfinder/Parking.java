package net.rofael.uabparkingfinder;

import android.os.Parcelable;
import android.os.Parcel;

import java.util.ArrayList;

/**
 * Created by Rofael on 2/24/2017.
 */

class Parking implements Parcelable {
    private String name;
    private int status;

    Parking(String lotName)
    {
        name = lotName;
        status = -1;
    }

    Parking(Parcel in) {
        name = in.readString();
        status = in.readInt();
    }

    public String toString()
    {
        return name;
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

