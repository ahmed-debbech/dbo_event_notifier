package dbo.notifier.services;

import dbo.notifier.dto.ScheduledEvents;

public interface IEventsService {
    ScheduledEvents getState();
    void checkScheduledEvents();
}
