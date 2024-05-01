package dbo.notifier.services;

import dbo.notifier.services.enumeration.ScheduledEventNames;

import java.util.Date;

public interface IScrapper {
    Date getClosestDate(ScheduledEventNames eventName) throws Exception;
    void launch();
    void resume(String html);
}
