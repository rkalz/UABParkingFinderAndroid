package net.rofael.uabparkingfinder;

/**
 * Created by Rofael on 2/24/2017.
 */

public class Report
{
    private Parking parking;
    private int status;
    private long time;

    public Report(Parking lot, int stat)
    {
        parking = lot;
        status = stat;
        time = System.currentTimeMillis();
    }

    public Report(Parking lot, int stat, long timeSent)
    {
        parking = lot;
        status = stat;
        time = timeSent;
    }

    public long getReportTime()
    {
        return time;
    }

    public String viewStatus()
    {
        if (status == -1)
        {
            return "How full is the parking lot?";
        }
        else if (status == 0)
        {
            return "Somewhat empty";
        }
        else if (status == 1)
        {
            return "Filling up";
        }
        else
        {
            return "Almost full";
        }
    }

    public String readableLastReportTime()
    {
        long timeDif = System.currentTimeMillis() - getReportTime();
        int seconds = ((int) timeDif / 1000) % 60;
        int minutes = (seconds / 60) % 60;
        int hours = (minutes / 60) % 24;
        int days = hours / 24;

        if (days > 0)
        {
            return Integer.toString(days) + " days and " + Integer.toString(hours) + " hours ago";
        }
        else if (hours > 0)
        {
            return Integer.toString(hours) + " hours and " + Integer.toString(minutes) + " minutes ago";
        }
        else if (minutes > 0)
        {
            return Integer.toString(minutes) + " minutes and " + Integer.toString(seconds) + " seconds ago";
        }
        else
        {
            return Integer.toString(seconds) + " seconds ago";
        }


    }


}
