package net.rofael.uabparkingfinder;

import java.util.Comparator;

/**
 * Created by Rofael on 2/24/2017.
 */

public class ReportComparator implements Comparator
{
    public int compare(Object o1, Object o2)
    {
        return Long.compare(((Report) o1).getReportTime(),((Report) o2).getReportTime());
    }
}