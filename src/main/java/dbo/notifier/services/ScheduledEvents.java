package dbo.notifier.services;

import dbo.notifier.logger.LogScheduled;
import dbo.notifier.services.enumeration.ScheduledEventNames;
import dbo.notifier.services.enumeration.EventType;
import dbo.notifier.services.enumeration.ServiceType;
import dbo.notifier.utils.TimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class ScheduledEvents implements IScheduledEvents {

    private LogScheduled out =  new LogScheduled();

    @Autowired
    private INotifier notifierAgent;

    @Autowired
    private IScrapper scrapper;
    @Autowired
    private ILiveEvents liveEvents;
    private EventType event_of_the_week = null;

    private Map<ScheduledEventNames, String> lastDate = new HashMap<>();

    @Override
    public dbo.notifier.dto.ScheduledEvents getState() {
        out.log("getting state...");
        dbo.notifier.dto.ScheduledEvents scheduledEvents = new dbo.notifier.dto.ScheduledEvents();
        scheduledEvents.setCurrent_events(liveEvents.getList());

        if(event_of_the_week!=null)
            scheduledEvents.setEvent_of_the_week(event_of_the_week.name());
        else
            scheduledEvents.setEvent_of_the_week(null);
        try {
            scheduledEvents.setAdult_solo_budokai(String.valueOf(scrapper.getClosestDate(ScheduledEventNames.ADULT_SOLO_BUDOKAI).getTime()));
            scheduledEvents.setAdult_party_budokai(String.valueOf(scrapper.getClosestDate(ScheduledEventNames.ADULT_PARTY_BUDOKAI).getTime()));
            scheduledEvents.setKid_solo_budokai(String.valueOf(scrapper.getClosestDate(ScheduledEventNames.KID_SOLO_BUDOKAI).getTime()));
            scheduledEvents.setKid_party_budokai(String.valueOf(scrapper.getClosestDate(ScheduledEventNames.KID_PARTY_BUDOKAI).getTime()));
            scheduledEvents.setDb_scramble(String.valueOf(scrapper.getClosestDate(ScheduledEventNames.DB_SCRAMBLE).getTime()));
            scheduledEvents.setDojo_war(String.valueOf(scrapper.getClosestDate(ScheduledEventNames.DOJO_WAR).getTime()));
        }catch (Exception e){
            out.log("CRASHED during getting state!");
            return null;
        }
        out.log("exist getState()");
        return scheduledEvents;
    }

    @Override
    public void checkScheduledEvents(){
        out.log("checking timeout of scheduled events");
        try {
            Date adult_solo = scrapper.getClosestDate(ScheduledEventNames.ADULT_SOLO_BUDOKAI);
            Date adult_party = scrapper.getClosestDate(ScheduledEventNames.ADULT_PARTY_BUDOKAI);
            Date kid_solo = scrapper.getClosestDate(ScheduledEventNames.KID_SOLO_BUDOKAI);
            Date kid_party = scrapper.getClosestDate(ScheduledEventNames.KID_PARTY_BUDOKAI);
            Date dojo_war = scrapper.getClosestDate(ScheduledEventNames.DOJO_WAR);
            Date db_scramble = scrapper.getClosestDate(ScheduledEventNames.DB_SCRAMBLE);

            out.log("next adult_solo will be: " + adult_solo);
            out.log("next adult_party will be: " + adult_party);
            out.log("next kid_solo will be: " + kid_solo);
            out.log("next kid_party will be: " + kid_party);
            out.log("next dojo_war will be: " + dojo_war);
            out.log("next db_scramble will be: " + db_scramble);

            out.log("done getting closest date for each event");

            Date now = new Date();
            if(now.after(adult_solo)){
                if(lastDate.containsKey(ScheduledEventNames.ADULT_SOLO_BUDOKAI)) {
                    if (!lastDate.get(ScheduledEventNames.ADULT_SOLO_BUDOKAI).equals(TimeUtils.removeMillisecondsPart(String.valueOf(adult_solo.getTime()))))
                        notifierAgent.broadcastNotificationThroughEveryMedium(ServiceType.EVENT, EventType.BUDO_ADULT_SOLO);
                }else{
                    notifierAgent.broadcastNotificationThroughEveryMedium(ServiceType.EVENT, EventType.BUDO_ADULT_SOLO);
                }
                lastDate.put(ScheduledEventNames.ADULT_SOLO_BUDOKAI, TimeUtils.removeMillisecondsPart(String.valueOf(adult_solo.getTime())));
            }
            if(now.after(adult_party)){
                if(lastDate.containsKey(ScheduledEventNames.ADULT_PARTY_BUDOKAI)) {
                    if (!lastDate.get(ScheduledEventNames.ADULT_PARTY_BUDOKAI).equals(TimeUtils.removeMillisecondsPart(String.valueOf(adult_party.getTime()))))
                        notifierAgent.broadcastNotificationThroughEveryMedium(ServiceType.EVENT, EventType.BUDO_ADULT_TEAM);
                }else{
                    notifierAgent.broadcastNotificationThroughEveryMedium(ServiceType.EVENT, EventType.BUDO_ADULT_TEAM);
                }
                lastDate.put(ScheduledEventNames.ADULT_PARTY_BUDOKAI, TimeUtils.removeMillisecondsPart(String.valueOf(adult_party.getTime())));
            }
            if(now.after(kid_solo)){
                if(lastDate.containsKey(ScheduledEventNames.KID_SOLO_BUDOKAI)) {
                    if (!lastDate.get(ScheduledEventNames.KID_SOLO_BUDOKAI).equals(TimeUtils.removeMillisecondsPart(String.valueOf(kid_solo.getTime()))))
                        notifierAgent.broadcastNotificationThroughEveryMedium(ServiceType.EVENT, EventType.BUDO_KID_SOLO);
                }else{
                    notifierAgent.broadcastNotificationThroughEveryMedium(ServiceType.EVENT, EventType.BUDO_KID_SOLO);
                }
                lastDate.put(ScheduledEventNames.KID_SOLO_BUDOKAI, TimeUtils.removeMillisecondsPart(String.valueOf(kid_solo.getTime())));
            }
            if(now.after(kid_party)){
                if(lastDate.containsKey(ScheduledEventNames.KID_PARTY_BUDOKAI)) {
                    if (!lastDate.get(ScheduledEventNames.KID_PARTY_BUDOKAI).equals(TimeUtils.removeMillisecondsPart(String.valueOf(kid_party.getTime()))))
                        notifierAgent.broadcastNotificationThroughEveryMedium(ServiceType.EVENT, EventType.BUDO_KID_TEAM);
                }else{
                    notifierAgent.broadcastNotificationThroughEveryMedium(ServiceType.EVENT, EventType.BUDO_KID_TEAM);
                }
                lastDate.put(ScheduledEventNames.KID_PARTY_BUDOKAI, TimeUtils.removeMillisecondsPart(String.valueOf(kid_party.getTime())));
            }
            if(now.after(dojo_war)){
                if(lastDate.containsKey(ScheduledEventNames.DOJO_WAR)) {
                    if (!lastDate.get(ScheduledEventNames.DOJO_WAR).equals(TimeUtils.removeMillisecondsPart(String.valueOf(dojo_war.getTime()))))
                        notifierAgent.broadcastNotificationThroughEveryMedium(ServiceType.EVENT, EventType.DOJO_WAR);
                }else{
                    notifierAgent.broadcastNotificationThroughEveryMedium(ServiceType.EVENT, EventType.DOJO_WAR);
                }
                lastDate.put(ScheduledEventNames.DOJO_WAR, TimeUtils.removeMillisecondsPart(String.valueOf(dojo_war.getTime())));
            }
            if(now.after(db_scramble)){
                if(lastDate.containsKey(ScheduledEventNames.DB_SCRAMBLE)) {
                    if (!lastDate.get(ScheduledEventNames.DB_SCRAMBLE).equals(TimeUtils.removeMillisecondsPart(String.valueOf(db_scramble.getTime()))))
                        notifierAgent.broadcastNotificationThroughEveryMedium(ServiceType.EVENT, EventType.DB_SCRAMBLE);
                }else{
                    notifierAgent.broadcastNotificationThroughEveryMedium(ServiceType.EVENT, EventType.DB_SCRAMBLE);
                }
                lastDate.put(ScheduledEventNames.DB_SCRAMBLE, TimeUtils.removeMillisecondsPart(String.valueOf(db_scramble.getTime())));
            }

            //System.err.println(lastDate.values().toString());

        }catch (Exception e){
            System.err.println(e);
        }
    }

    @Override
    public void setEventOfTheWeek(EventType event_of_the_week) {
        out.log("setting event of the week");
        this.event_of_the_week = event_of_the_week;
    }
}
