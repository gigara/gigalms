package classes;// Created by Gigara on 10/26/2018

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class DateTime {
    @Column(columnDefinition="integer default 0")
    private int date;

    @Column(columnDefinition="integer default 0")
    private int month;

    @Column(columnDefinition="integer default 0")
    private int year;

    @Column(columnDefinition="integer default 0")
    private int minute;

    @Column(columnDefinition="integer default 0")
    private int hour;

    public DateTime(int date, int month, int year, int minute, int hour) {
        this.date = date;
        this.month = month;
        this.year = year;
        this.minute = minute;
        this.hour = hour;
    }

    public int getDate() {
        return date;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public double getMinute() {
        return minute;
    }

    public double getHour() {
        return hour;
    }



    //Method to find the timestamp
    private final static long TicksInSecond = 10000L * 1000L;
    private final static long TicksInMillisecond = 10000L;
    private static final int[]  DaysToMonth365 = new int[] { 0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334, 365 };
    private static final int[] DaysToMonth366 =  new int[] { 0, 31, 60, 91, 121, 152, 182, 213, 244, 274, 305, 335, 366 };

    public long TimeStamp() throws Exception {
        long ticks =  (dateToTicks(year, this.month, this.date) + timeToTicks(this.hour, this.minute, 0));

        return (ticks - 621355968000000000L)/10000000;
    }

    public static boolean isLeapYear(int year) throws Exception {
        if ((year < 1) || (year > 9999))
            throw new Exception(year +" Bad year.");

        if ((year % 4) != 0)
            return false;

        if ((year % 100) == 0)
            return ((year % 400) == 0);

        return true;
    }

    private static long dateToTicks(int year, int month, int day) throws Exception {
        if (((year >= 1) && (year <= 9999)) && ((month >= 1) && (month <= 12)))
        {
            int[] daysToMonth = isLeapYear(year) ? DaysToMonth366 : DaysToMonth365;
            if ((day >= 1) && (day <= (daysToMonth[month] - daysToMonth[month - 1])))
            {
                int previousYear = year - 1;
                int daysInPreviousYears = ((((previousYear * 365) + (previousYear / 4)) - (previousYear / 100)) + (previousYear / 400));

                int totalDays = ((daysInPreviousYears + daysToMonth[month - 1]) + day) - 1;
                return (totalDays * 0xc92a69c000L);
            }
        }
        throw new Exception();
    }

    private static long timeToTicks(int hour, int minute, int second) throws Exception {
        long totalSeconds = ((hour * 3600L) + (minute * 60L)) + second;
        if ((totalSeconds > 0xd6bf94d5e5L) || (totalSeconds < -922337203685L))
            throw new Exception();

        return (totalSeconds * TicksInSecond);
    }

    //calculate the differance
    public static int hourDiffrence(DateTime d1, DateTime d2) throws Exception {
        //in seconds
        long diff = d2.TimeStamp() - d1.TimeStamp();

        long diffHours = diff / (60 * 60);

        return Math.toIntExact(diffHours);
    }

    //calculate the difference with the system time
    public static int hourDiffrence(DateTime d1) throws Exception {
        //in seconds
        long diff = System.currentTimeMillis()/1000 - d1.TimeStamp();

        long diffHours = diff / (60 * 60);

        return Math.toIntExact(diffHours);
    }


}
