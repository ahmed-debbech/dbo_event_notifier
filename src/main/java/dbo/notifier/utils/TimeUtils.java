package dbo.notifier.utils;

import dbo.notifier.model.Event;

import java.time.LocalDateTime;
import java.util.*;

public class TimeUtils {
    public static List<Date> getByCountdown(List<Event> events){
        List<Date> d = new ArrayList<>();

        for(Event ev : events){
            Date r = new Date();
            int count = Integer.parseInt(ev.getCountdown().split(" ")[0]);
            String countUnit = ev.getCountdown().split(" ")[1].substring(0,2);
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(r);

            long milliseconds = calendar.getTimeInMillis();
            Date date = new Date(milliseconds);
            if(countUnit.equals("da")) {
                calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + count);
            }
            if(countUnit.equals("ho")) {
                calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + count);
            }
            if(countUnit.equals("mi")) {
                calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) + count);
            }
            if(countUnit.equals("se")) {
                calendar.set(Calendar.SECOND, calendar.get(Calendar.SECOND) + count);
            }

            long milliseconds1 = calendar.getTimeInMillis();
            Date date1 = new Date(milliseconds1);
            d.add(date1);
        }
        //System.err.println(d);
        return d;
    }

    public static Date parseTime(String time, Date g){
        //System.err.println("fff " + g);
        String h = "";
        String m = "";
        int s = 0;
        int hn = 0;
        int mn = 0;

        if(time.contains("am")){
            if(!time.contains(":")){
                h += time.substring(0,time.length()-2);
                hn = Integer.parseInt(h);
            }else{
                h+= time.split(":")[0];
                m+=time.split(":")[1].substring(0, time.split(":")[1].length()-2);
                hn = Integer.parseInt(h) ;
                mn = Integer.parseInt(m);
            }
        }
        if(time.contains("pm")){
            if(!time.contains(":")){
                h += time.substring(0,time.length()-2);
                hn = Integer.parseInt(h) + 12;
            }else{
                h+= time.split(":")[0];
                m+=time.split(":")[1].substring(0, time.split(":")[1].length()-2);
                hn = Integer.parseInt(h) + 12;
                mn = Integer.parseInt(m);
            }
        }
        //Calendar.set(year + 1900, month, date, hrs, min, sec)
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(g);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) ;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month); // Note: Calendar.MONTH is zero-based
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hn); // 24-hour clock
        calendar.set(Calendar.MINUTE, mn);
        calendar.set(Calendar.SECOND, s);
        long milliseconds = calendar.getTimeInMillis();
        Date date = new Date(milliseconds);
        return date;
    }
    public static List<Date> getBySchedule(List<Date> d, List<Event> events){
        int i = 0;
        for(Event ev : events){
            String time = ev.getSchedule();
            Date dd = parseTime(time, d.get(i));
            d.set(i, dd);
            i++;
        }
        return d;
    }

    public static String removeMillisecondsPart(String timestamp){
        String str = timestamp.substring(0, timestamp.length() - 3);
        return str;
    }
}
