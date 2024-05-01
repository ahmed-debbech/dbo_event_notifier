package dbo.notifier.utils;

import dbo.notifier.model.Event;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TimeUtils {
    public static List<Date> getByCountdown(List<Event> events){
        List<Date> d = new ArrayList<>();

        for(Event ev : events){
            Date r = new Date();
            int count = Integer.parseInt(ev.getCountdown().split(" ")[0]);
            String countUnit = ev.getCountdown().split(" ")[1].substring(0,2);
            Calendar c = Calendar.getInstance();
            if(countUnit.equals("da")) {
                r.setDate(r.getDate() + count);
            }
            if(countUnit.equals("ho")) {
                r.setHours(r.getHours() + count);
            }
            if(countUnit.equals("mi")) {
                r.setMinutes(r.getMinutes() + count);
            }
            if(countUnit.equals("se")) {
                r.setSeconds(r.getSeconds() + count);
            }
            d.add(r);
        }
        System.err.println(d);
        return d;
    }

    public static Date parseTime(String time, Date g){
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
        g.setHours(hn);
        g.setMinutes(mn);
        g.setSeconds(s);
        return g;
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
