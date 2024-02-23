package dbo.notifier;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.Local;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

@org.springframework.stereotype.Service
public class Service {

    @Value("${telegram.url}")
    String urlTelegram;

    String htmlBody;
    List<Event> events;

    Date nextNotif = null;

    @Autowired
    private Logger out;

    public void run(){
        grab();
        jparse();
        least();
        schedule();
    }

    private void schedule(){
        if(nextNotif == null) {
            nextNotif = new Date();
            nextNotif.setSeconds(nextNotif.getSeconds() + 10);
        }
    }

    public void notifyUsers(){
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
                url += URLEncoder.encode("HEY", StandardCharsets.UTF_8.toString());
                //restTemplate.getForEntity(url, String.class);
                nextNotif = null;
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

    private void least(){
        List<Date> d = getByCountdown();
        d = getBySchedule(d);
        System.err.println(d);
    }

    private void jparse(){
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
    }

    private void grab(){
        out.log("Grabbing html ...");
        RestTemplate restTemplate = new RestTemplate();
        String fooResourceUrl = "https://dboglobal.to/events";
        ResponseEntity<String> response = restTemplate.getForEntity(fooResourceUrl , String.class);
        htmlBody = response.getBody();
        out.log("done grabbing html.");
    }
}
