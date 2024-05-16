package dbo.notifier.services;

import dbo.notifier.services.enumeration.ScheduledEventNames;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class LastEvents implements ILastEvents{

    private Map<ScheduledEventNames, String> lastEvents = new HashMap<>();

    @Override
    public Map<ScheduledEventNames, String> getLastEvents() {
        return lastEvents;
    }

    @Override
    public void setEvent(ScheduledEventNames name, String timestamp) {
        this.lastEvents.put(name, timestamp);
    }
}
