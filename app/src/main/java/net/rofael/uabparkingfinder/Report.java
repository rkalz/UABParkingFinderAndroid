package net.rofael.uabparkingfinder;

/**
 * Created by Rofael on 2/24/2017.
 */

class Report
{
    private Parking parking;
    private int status;
    private long time;

    Report(Parking lot, int stat)
    {
        parking = lot;
        status = stat;
        time = System.currentTimeMillis();
    }

    Report(Parking lot, int stat, long timeSent)
    {
        parking = lot;
        status = stat;
        time = timeSent;
    }

    long getReportTime()
    {
        return time;
    }

    String viewStatus()
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

    String readableLastReportTime()
    {
        long timeDif = System.currentTimeMillis() - getReportTime();
        long seconds = (timeDif / 1000);
        long minutes = (seconds / 60);
        long hours = (minutes / 60);
        long days = hours / 24;

        String left;
        String right;

        if (days > 0)
        {
            hours = hours % 24;
            if (days == 1)
            {
                left = " day and ";
            }
            else
            {
                left = " days and ";
            }
            if (hours == 1)
            {
                right = " hour ago";
            }
            else
            {
                right = " hours ago";
            }
            return Long.toString(days) + left + Long.toString(hours) + right;
        }
        else if (hours > 0)
        {
            minutes = minutes % 60;
            if (hours == 1)
            {
                left = " hour and ";
            }
            else
            {
                left = " hours and ";
            }
            if (minutes == 1)
            {
                right = " minute ago";
            }
            else
            {
                right = " minutes ago";
            }
            return Long.toString(hours) + left + Long.toString(minutes) + right;
        }
        else if (minutes > 0)
        {
            seconds = seconds % 60;
            if (minutes == 1)
            {
                left = " minute and ";
            }
            else
            {
                left = " minutes and ";
            }
            if (seconds == 1)
            {
                right = " second ago";
            }
            else
            {
                right = " seconds ago";
            }
            return Long.toString(minutes) + left + Long.toString(seconds) + right;
        }
        else
        {
            if (seconds == 1)
            {
                right = " second ago";
            }
            else
            {
                right = " seconds ago";
            }
            return Long.toString(seconds) + right;
        }


    }

    public boolean equals(Object o2)
    {
        return ((o2 instanceof Report) && (this.getReportTime() == ((Report) o2).getReportTime()));
    }


}
