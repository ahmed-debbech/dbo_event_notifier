package dbo.notifier;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@org.springframework.stereotype.Service
public class ScheduledBudokaiService {

    @Value("${telegram.url}")
    String urlTelegram;

    String htmlBody = null;
    List<Event> events;

    Date nextEvent = null;
    Date nextNotif = null;
    String comparator = null;

    boolean eventIsDone = false;

    private boolean chromeOpened = false;

    private LogScheduled out = new LogScheduled() ;


    public void start(){
        out.log("Launching chrome... (connectiong to dboglobal.to)");
        try {
            SystemUtils.tryStartChromeBrowser("https://dboglobal.to/events");
            chromeOpened = true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void resume(String html){
        out.log("Checking server for updates");
        htmlBody = html;
        jparse();
        Date nextEvent = least();
        System.err.println(nextEvent);
        schedule(nextEvent);
        out.log("finish checking server");
    }

    private void schedule(Date d){

        if(eventIsDone){
            if(!this.comparator.equals(d.toString())){
                eventIsDone = false;
            }else{
                return;
            }
        }
        this.nextEvent = d;
        this.comparator = nextEvent.toString();
        this.nextNotif = new Date(this.nextEvent.getTime());
        this.nextNotif.setMinutes(this.nextNotif.getMinutes() - 10);
        out.log("Next event will be: " + this.nextEvent);
        out.log("Next event reminder will be: " + this.nextNotif);
        out.log("Finish getting the closest event");
    }
    public void notifyUsers(){
        if(!chromeOpened) start();

        if(eventIsDone) return;
        if(nextNotif == null) return;

        RestTemplate restTemplate = new RestTemplate();
        String url = urlTelegram;
        try {
            Calendar date = new GregorianCalendar(2024, nextNotif.getMonth()+1, nextNotif.getDate());

            String dateTime = date.get(Calendar.YEAR) + "-" + String.format("%02d", date.get(Calendar.MONTH)) + "-" +
                    String.format("%02d", date.get(Calendar.DAY_OF_MONTH)) + "T"
                    + String.format("%02d",nextNotif.getHours()) + ":" + String.format("%02d",nextNotif.getMinutes())
                    + ":" + String.format("%02d",nextNotif.getSeconds());


            LocalDateTime notifTime = LocalDateTime.parse(dateTime);
            if(LocalDateTime.now().isAfter(notifTime)) {
                url += URLEncoder.encode("A new Budokai - Adult Solo event is about to start in 10 mins.", StandardCharsets.UTF_8.toString());
                System.err.println("NOTIFY");
                restTemplate.getForEntity(url, String.class);
                nextNotif = null;
                this.eventIsDone = true;
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Date> getByCountdown(){
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

    private Date parseTime(String time, Date g){
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
    private List<Date> getBySchedule(List<Date> d){
        int i = 0;
        for(Event ev : events){
            String time = ev.getSchedule();
            Date dd = parseTime(time, d.get(i));
            d.set(i, dd);
            i++;
        }
        return d;
    }

    private Date least(){
        out.log("Getting the closest next event...");
        List<Date> d = getByCountdown();
        d = getBySchedule(d);
        //System.err.println(d);
        Date min = d.get(0);
        for(int i =1; i<=d.size()-1; i++){
            if(d.get(i).before(min)){
                min = d.get(i);
            }
        }
        return min;
    }

    private void jparse(){
        out.log("Parsing html ...");
        if(htmlBody == null) {
            out.log("not result from html");
            return;
        }
        Document doc = Jsoup.parse(htmlBody);
        Elements info = doc.select("#budokaiadultsolo");
        Elements trs = info.select("tr");
        this.events = new ArrayList<>();

        boolean head = true;
        for(Element tr : trs){
            if(!head) {
                Elements tds = tr.children();
                Event ev = new Event(tds.get(0).text(),
                        tds.get(1).text(),
                        tds.get(2).text(),
                        tds.get(3).text());
                events.add(ev);
            }
            head = false;
        }
        out.log(events.toString());
        out.log("Finish mapping and parsing html.");
    }


}
