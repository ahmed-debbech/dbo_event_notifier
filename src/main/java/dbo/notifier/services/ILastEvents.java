package dbo.notifier.services;

import dbo.notifier.services.enumeration.ScheduledEventNames;

import java.util.Map;

public interface ILastEvents {
    Map<ScheduledEventNames, String> getLastEvents();
    void setEvent(ScheduledEventNames name, String timestamp);
}
