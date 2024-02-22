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
    List<String> partHtmlLines;
    List<Event> events;

    @Autowired
    private Logger out;

    public void run(){
        grab();
        //parse();
        //map();
        jparse();
    }

    private void jparse(){
        Document doc = Jsoup.parse(htmlBody);
        Element body = doc.body();
        Elements info = doc.select("#budokaiadultsolo");
        Elements trs = info.select("tr");
        for(Element tr : trs){

        }
        out.log(trs.toString());
    }
    private void map(){
        List<String> trs = new ArrayList<>();
        int bodyTr = 0;
        String tr = "";
        for(String h : partHtmlLines){
            if(h.contains("<tr>")){
                bodyTr = 1;
                tr = "";
            }
            if(bodyTr == 1){
                tr += h;
            }
            if(h.contains("</tr>")){
                bodyTr = 0;
                tr += h;
                trs.add(tr);
            }
        }


        for(String h : trs){
            if(h.contains("<td>")){
                bodyTr = 1;
                tr = "";
            }
            if(bodyTr == 1){
                tr += h;
            }
            if(h.contains("</tr>")){
                bodyTr = 0;
                tr += h;
                trs.add(tr);
            }
        }
    }
    private void parse(){
        partHtmlLines = new ArrayList<>();
        out.log("Parsing html...");
        String[] lines = htmlBody.split("\n");
        boolean startParse = false;

        for(String l : lines){
            if(l.contains("id=\"budokaiadultsolo\"")){
                startParse = true;
            }
            if(startParse){
                l = l.trim();
                partHtmlLines.add(l);
            }
            if(l.contains("id=\"budokaiadultparty\"")){
                startParse = false;
            }
        }
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
