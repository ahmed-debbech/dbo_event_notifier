package dbo.notifier.services;

import dbo.notifier.dto.ScheduledEvents;

public interface IScheduledEvents {
    ScheduledEvents getState();
    void checkScheduledEvents();
}
