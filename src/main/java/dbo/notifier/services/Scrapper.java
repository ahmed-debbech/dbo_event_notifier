package dbo.notifier.services;

import dbo.notifier.logger.LogScrapper;
import dbo.notifier.model.Event;
import dbo.notifier.services.enumeration.ScheduledEventNames;
import dbo.notifier.utils.SystemUtils;
import dbo.notifier.utils.TimeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class Scrapper implements IScrapper{

    private final LogScrapper out = new LogScrapper();

    String htmlBody = null;

    List<Event> adult_solo_budo_events;
    List<Event> adult_party_budo_events;
    List<Event> kid_solo_budo_events;
    List<Event> kid_party_budo_events;
    List<Event> dojo_war;
    List<Event> db_scramble;


    @Override
    public Date getClosestDate(ScheduledEventNames eventname) throws Exception{
        Date closest = null;

        switch (eventname){
            case KID_SOLO_BUDOKAI:
                closest = least(kid_solo_budo_events);
                break;
            case KID_PARTY_BUDOKAI:
                closest = least(kid_party_budo_events);
                break;
            case ADULT_SOLO_BUDOKAI:
                closest = least(adult_solo_budo_events);
                break;
            case ADULT_PARTY_BUDOKAI:
                closest = least(adult_party_budo_events);
                break;
            case DB_SCRAMBLE:
                closest = least(db_scramble);
                break;
            case DOJO_WAR:
                closest = least(dojo_war);
                break;
        }
        if(closest == null) throw new Exception("null date");
        return closest;
    }

    @Override
    public void launch(){
        out.log("Launching chrome... (connectiong to dboglobal.to)");
        try {
            SystemUtils.tryStartChromeBrowser("https://dboglobal.to/events");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void resume(String html){
        out.log("Checking dboglobal.to for event updates...");
        htmlBody = html;

        jparse_adult_solo_budo();
        jparse_adult_party_budo();
        jparse_kid_solo_budo();
        jparse_kid_party_budo();
        jparse_db_scramble();
        jparse_dojo_war();

        out.log("finish checking dboglobal.to");
    }

    private Date least(List<Event> events){
        out.log("Getting the closest next event...");
        if(events == null || events.isEmpty()){
            out.log("empty events.. nothing parsed");
            return null;
        }

        List<Date> d = TimeUtils.getByCountdown(events);
        d = TimeUtils.getBySchedule(d,events);
        Date min = d.get(0);
        for(int i =1; i<=d.size()-1; i++){
            if((d.get(i).before(min))){
                min = d.get(i);
            }
        }
        return min;
    }

    private void jparse_adult_solo_budo(){
        out.log("Parsing html ...");
        if(htmlBody == null) {
            out.log("not result from html");
            return;
        }
        Document doc = Jsoup.parse(htmlBody);
        Elements info = doc.select("#budokaiadultsolo");
        Elements trs = info.select("tr");
        this.adult_solo_budo_events = new ArrayList<>();

        boolean head = true;
        for(Element tr : trs){
            if(!head) {
                Elements tds = tr.children();
                Event ev = new Event(tds.get(0).text(),
                        tds.get(1).text(),
                        tds.get(2).text(),
                        tds.get(3).text());
                adult_solo_budo_events.add(ev);
            }
            head = false;
        }
        out.log(adult_solo_budo_events.toString());
        out.log("Finish mapping and parsing html for adult solo budo.");
    }
    private void jparse_adult_party_budo(){
        out.log("Parsing html ...");
        if(htmlBody == null) {
            out.log("not result from html");
            return;
        }
        Document doc = Jsoup.parse(htmlBody);
        Elements info = doc.select("#budokaiadultparty");
        Elements trs = info.select("tr");
        this.adult_party_budo_events = new ArrayList<>();

        boolean head = true;
        for(Element tr : trs){
            if(!head) {
                Elements tds = tr.children();
                Event ev = new Event(tds.get(0).text(),
                        tds.get(1).text(),
                        tds.get(2).text(),
                        tds.get(3).text());
                adult_party_budo_events.add(ev);
            }
            head = false;
        }
        out.log(adult_party_budo_events.toString());
        out.log("Finish mapping and parsing html for adult party budo.");
    }
    private void jparse_kid_solo_budo(){
        out.log("Parsing html ...");
        if(htmlBody == null) {
            out.log("not result from html");
            return;
        }
        Document doc = Jsoup.parse(htmlBody);
        Elements info = doc.select("#budokaikidsolo");
        Elements trs = info.select("tr");
        this.kid_solo_budo_events = new ArrayList<>();

        boolean head = true;
        for(Element tr : trs){
            if(!head) {
                Elements tds = tr.children();
                Event ev = new Event(tds.get(0).text(),
                        tds.get(1).text(),
                        tds.get(2).text(),
                        tds.get(3).text());
                kid_solo_budo_events.add(ev);
            }
            head = false;
        }
        out.log(kid_solo_budo_events.toString());
        out.log("Finish mapping and parsing html for kid solo budo.");
    }
    private void jparse_kid_party_budo(){
        out.log("Parsing html ...");
        if(htmlBody == null) {
            out.log("not result from html");
            return;
        }
        Document doc = Jsoup.parse(htmlBody);
        Elements info = doc.select("#budokaikidparty");
        Elements trs = info.select("tr");
        this.kid_party_budo_events = new ArrayList<>();

        boolean head = true;
        for(Element tr : trs){
            if(!head) {
                Elements tds = tr.children();
                Event ev = new Event(tds.get(0).text(),
                        tds.get(1).text(),
                        tds.get(2).text(),
                        tds.get(3).text());
                kid_party_budo_events.add(ev);
            }
            head = false;
        }
        out.log(kid_party_budo_events.toString());
        out.log("Finish mapping and parsing html for kid party budo.");
    }
    private void jparse_db_scramble(){
        out.log("Parsing html ...");
        if(htmlBody == null) {
            out.log("not result from html");
            return;
        }
        Document doc = Jsoup.parse(htmlBody);
        Elements info = doc.select("#dbscramble");
        Elements trs = info.select("tr");
        this.db_scramble = new ArrayList<>();

        boolean head = true;
        for(Element tr : trs){
            if(!head) {
                Elements tds = tr.children();
                Event ev;
                if(tds.get(2).text().startsWith("Cur"))
                    ev = null;
                else
                    ev = new Event(
                            "db_scramble",
                            tds.get(0).text(),
                            tds.get(1).text().split("-", 2)[0].trim(),
                            tds.get(2).text());

                if(ev != null)
                    db_scramble.add(ev);
            }
            head = false;
        }
        out.log(db_scramble.toString());
        out.log("Finish mapping and parsing html for db scramble.");
    }

    private void jparse_dojo_war(){
        out.log("Parsing html ...");
        if(htmlBody == null) {
            out.log("not result from html");
            return;
        }
        Document doc = Jsoup.parse(htmlBody);
        Elements info = doc.select("#dojowar");
        Elements trs = info.select("tr");
        this.dojo_war = new ArrayList<>();

        boolean head = true;
        for(Element tr : trs){
            if(!head) {
                Elements tds = tr.children();
                Event ev = new Event(
                        "dojo_war",
                        tds.get(0).text(),
                        tds.get(1).text(),
                        tds.get(2).text());
                dojo_war.add(ev);
            }
            head = false;
        }
        out.log(dojo_war.toString());
        out.log("Finish mapping and parsing html for dojo war.");
    }
}
