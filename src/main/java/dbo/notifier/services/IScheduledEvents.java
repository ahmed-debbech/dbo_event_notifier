package dbo.notifier.services;

import dbo.notifier.dto.ScheduledEvents;
import dbo.notifier.services.enumeration.EventType;

public interface IScheduledEvents {
    ScheduledEvents getState();
    void checkScheduledEvents();
    void setEventOfTheWeek(EventType event);
}
