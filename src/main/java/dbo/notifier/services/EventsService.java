package dbo.notifier.services;

import dbo.notifier.dto.ScheduledEvents;
import dbo.notifier.model.ScheduledEventNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventsService implements IEventsService {

    @Autowired
    private IScrapper scrapper;

    @Override
    public ScheduledEvents getState() {
        ScheduledEvents scheduledEvents = new ScheduledEvents();
        scheduledEvents.setCurrent_events(null);
        scheduledEvents.setEvent_of_the_week(null);
        scheduledEvents.setAdult_solo_budokai(String.valueOf(scrapper.getClosestDate(ScheduledEventNames.ADULT_SOLO_BUDOKAI).getTime()));
        scheduledEvents.setAdult_party_budokai(String.valueOf(scrapper.getClosestDate(ScheduledEventNames.ADULT_PARTY_BUDOKAI).getTime()));
        scheduledEvents.setKid_solo_budokai(String.valueOf(scrapper.getClosestDate(ScheduledEventNames.KID_SOLO_BUDOKAI).getTime()));
        scheduledEvents.setKid_party_budokai(String.valueOf(scrapper.getClosestDate(ScheduledEventNames.KID_PARTY_BUDOKAI).getTime()));
        scheduledEvents.setDb_scramble(String.valueOf(scrapper.getClosestDate(ScheduledEventNames.DB_SCRAMBLE).getTime()));
        scheduledEvents.setDojo_war(String.valueOf(scrapper.getClosestDate(ScheduledEventNames.DOJO_WAR).getTime()));
        return scheduledEvents;
    }
}
