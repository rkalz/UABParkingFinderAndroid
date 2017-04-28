package net.rofael.uabparkingfinder;

import java.util.Comparator;

/**
 * Created by Rofael on 2/24/2017.
 */

// Sorts reports in descending order (newest before oldest)
public class ReportComparator implements Comparator
{
    public int compare(Object o1, Object o2)
    {
        long time1 = ((Report) o1).getReportTime();
        long time2 = ((Report) o2).getReportTime();

        if (time1 < time2)
        {
            return 1;
        }
        else if (time1 == time2)
        {
            return 0;
        }
        else
        {
            return -1;
        }


    }
}
