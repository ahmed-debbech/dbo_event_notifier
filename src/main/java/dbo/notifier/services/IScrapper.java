package dbo.notifier.services;

import dbo.notifier.model.ScheduledEventNames;

import java.util.Date;
import java.util.List;

public interface IScrapper {
    Date getClosestDate(ScheduledEventNames eventName) throws Exception;
    void launch();
    void resume(String html);
}
