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

    public int getStatus()
    {
        return status;
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
        int seconds = ((int) timeDif / 1000);
        int minutes = (seconds / 60);
        int hours = (minutes / 60);
        int days = hours / 24;

        if (days > 0)
        {
            hours = hours % 24;
            return Integer.toString(days) + " days and " + Integer.toString(hours) + " hours ago";
        }
        else if (hours > 0)
        {
            minutes = minutes % 60;
            return Integer.toString(hours) + " hours and " + Integer.toString(minutes) + " minutes ago";
        }
        else if (minutes > 0)
        {
            seconds = seconds % 60;
            return Integer.toString(minutes) + " minutes and " + Integer.toString(seconds) + " seconds ago";
        }
        else
        {
            return Integer.toString(seconds) + " seconds ago";
        }


    }

    public boolean equals(Object o2)
    {
        return (this.getReportTime() == ((Report) o2).getReportTime());
    }


}
