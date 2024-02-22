package dbo.notifier;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@org.springframework.stereotype.Service
public class Service {

    String htmlBody;
    List<Event> events;

    @Autowired
    private Logger out;

    public void run(){
        grab();
        jparse();

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
