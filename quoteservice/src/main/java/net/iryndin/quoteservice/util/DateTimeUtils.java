package net.iryndin.quoteservice.util;

import java.util.Calendar;
import java.util.Date;

public class DateTimeUtils {

    public static Date getNextMinuteStart() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.add(Calendar.MINUTE, 1);
        return calendar.getTime();
    }

    public static Date getCurrentMinuteStart() {
        return getCurrentMinuteStartAsCalendar().getTime();
    }

    public static Date getPrevMinuteStart() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.add(Calendar.MINUTE, -1);
        return calendar.getTime();
    }

    public static Calendar getCurrentMinuteStartAsCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar;
    }

    public static Date getPrevHourStart() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.add(Calendar.HOUR_OF_DAY, -1);
        return calendar.getTime();
    }

    public static Date getPrevDayStart() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.add(Calendar.DATE, -1);
        return calendar.getTime();
    }


}
