package dbo.notifier.services;

import dbo.notifier.dto.ScheduledEvents;
import dbo.notifier.services.enumeration.ScheduledEventNames;
import dbo.notifier.services.enumeration.EventType;
import dbo.notifier.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class EventsService implements IEventsService {

    @Autowired
    private IScrapper scrapper;
    @Autowired
    private ILiveEvents liveEvents;

    private Map<ScheduledEventNames, String> lastDate = new HashMap<>();

    @Override
    public ScheduledEvents getState() {
        ScheduledEvents scheduledEvents = new ScheduledEvents();
        if(liveEvents.getList() == null){
            scheduledEvents.setCurrent_events(null);
        }else {
            scheduledEvents.setCurrent_events(liveEvents.getList());
        }
        scheduledEvents.setEvent_of_the_week("ahmed");
        try {
            scheduledEvents.setAdult_solo_budokai(String.valueOf(scrapper.getClosestDate(ScheduledEventNames.ADULT_SOLO_BUDOKAI).getTime()));
            scheduledEvents.setAdult_party_budokai(String.valueOf(scrapper.getClosestDate(ScheduledEventNames.ADULT_PARTY_BUDOKAI).getTime()));
            scheduledEvents.setKid_solo_budokai(String.valueOf(scrapper.getClosestDate(ScheduledEventNames.KID_SOLO_BUDOKAI).getTime()));
            scheduledEvents.setKid_party_budokai(String.valueOf(scrapper.getClosestDate(ScheduledEventNames.KID_PARTY_BUDOKAI).getTime()));
            scheduledEvents.setDb_scramble(String.valueOf(scrapper.getClosestDate(ScheduledEventNames.DB_SCRAMBLE).getTime()));
            scheduledEvents.setDojo_war(String.valueOf(scrapper.getClosestDate(ScheduledEventNames.DOJO_WAR).getTime()));
        }catch (Exception e){
            return null;
        }
        return scheduledEvents;
    }

    @Override
    public void checkScheduledEvents(){
        try {
            Date adult_solo = scrapper.getClosestDate(ScheduledEventNames.ADULT_SOLO_BUDOKAI);
            Date adult_party = scrapper.getClosestDate(ScheduledEventNames.ADULT_PARTY_BUDOKAI);
            Date kid_solo = scrapper.getClosestDate(ScheduledEventNames.KID_SOLO_BUDOKAI);
            Date kid_party = scrapper.getClosestDate(ScheduledEventNames.KID_PARTY_BUDOKAI);
            Date dojo_war = scrapper.getClosestDate(ScheduledEventNames.DOJO_WAR);
            Date db_scramble = scrapper.getClosestDate(ScheduledEventNames.DB_SCRAMBLE);

            Date now = new Date();
            if(now.after(adult_solo)){
                if(lastDate.containsKey(ScheduledEventNames.ADULT_SOLO_BUDOKAI)) {
                    if (!lastDate.get(ScheduledEventNames.ADULT_SOLO_BUDOKAI).equals(TimeUtils.removeMillisecondsPart(String.valueOf(adult_solo.getTime()))))
                        liveEvents.notifyUsers(EventType.BUDO_ADULT_SOLO);
                }else{
                    liveEvents.notifyUsers(EventType.BUDO_ADULT_SOLO);
                }
                lastDate.put(ScheduledEventNames.ADULT_SOLO_BUDOKAI, TimeUtils.removeMillisecondsPart(String.valueOf(adult_solo.getTime())));
            }
            if(now.after(adult_party)){
                if(lastDate.containsKey(ScheduledEventNames.ADULT_PARTY_BUDOKAI)) {
                    if (!lastDate.get(ScheduledEventNames.ADULT_PARTY_BUDOKAI).equals(TimeUtils.removeMillisecondsPart(String.valueOf(adult_party.getTime()))))
                        liveEvents.notifyUsers(EventType.BUDO_ADULT_TEAM);
                }else{
                    liveEvents.notifyUsers(EventType.BUDO_ADULT_TEAM);
                }
                lastDate.put(ScheduledEventNames.ADULT_PARTY_BUDOKAI, TimeUtils.removeMillisecondsPart(String.valueOf(adult_party.getTime())));
            }
            if(now.after(kid_solo)){
                if(lastDate.containsKey(ScheduledEventNames.KID_SOLO_BUDOKAI)) {
                    if (!lastDate.get(ScheduledEventNames.KID_SOLO_BUDOKAI).equals(TimeUtils.removeMillisecondsPart(String.valueOf(kid_solo.getTime()))))
                        liveEvents.notifyUsers(EventType.BUDO_KID_SOLO);
                }else{
                    liveEvents.notifyUsers(EventType.BUDO_KID_SOLO);
                }
                lastDate.put(ScheduledEventNames.KID_SOLO_BUDOKAI, TimeUtils.removeMillisecondsPart(String.valueOf(kid_solo.getTime())));
            }
            if(now.after(kid_party)){
                if(lastDate.containsKey(ScheduledEventNames.KID_PARTY_BUDOKAI)) {
                    if (!lastDate.get(ScheduledEventNames.KID_PARTY_BUDOKAI).equals(TimeUtils.removeMillisecondsPart(String.valueOf(kid_party.getTime()))))
                        liveEvents.notifyUsers(EventType.BUDO_KID_TEAM);
                }else{
                    liveEvents.notifyUsers(EventType.BUDO_KID_TEAM);
                }
                lastDate.put(ScheduledEventNames.KID_PARTY_BUDOKAI, TimeUtils.removeMillisecondsPart(String.valueOf(kid_party.getTime())));
            }
            if(now.after(dojo_war)){
                if(lastDate.containsKey(ScheduledEventNames.DOJO_WAR)) {
                    if (!lastDate.get(ScheduledEventNames.DOJO_WAR).equals(TimeUtils.removeMillisecondsPart(String.valueOf(dojo_war.getTime()))))
                        liveEvents.notifyUsers(EventType.DOJO_WAR);
                }else{
                    liveEvents.notifyUsers(EventType.DOJO_WAR);
                }
                lastDate.put(ScheduledEventNames.DOJO_WAR, TimeUtils.removeMillisecondsPart(String.valueOf(dojo_war.getTime())));
            }
            if(now.after(db_scramble)){
                if(lastDate.containsKey(ScheduledEventNames.DB_SCRAMBLE)) {
                    if (!lastDate.get(ScheduledEventNames.DB_SCRAMBLE).equals(TimeUtils.removeMillisecondsPart(String.valueOf(db_scramble.getTime()))))
                        liveEvents.notifyUsers(EventType.DB_SCRAMBLE);
                }else{
                    liveEvents.notifyUsers(EventType.DB_SCRAMBLE);
                }
                lastDate.put(ScheduledEventNames.DB_SCRAMBLE, TimeUtils.removeMillisecondsPart(String.valueOf(db_scramble.getTime())));
            }

            System.err.println(lastDate.values().toString());

        }catch (Exception e){
            System.err.println(e);
        }
    }
}
