package dbo.notifier.services;

import dbo.notifier.dto.ScheduledEvents;
import dbo.notifier.model.ScheduledEventNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventsService implements IEventsService {

    @Autowired
    private IScrapper scrapper;
    @Autowired
    private ILiveEvents liveEvents;

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
}
