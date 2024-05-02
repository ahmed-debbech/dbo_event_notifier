package dbo.notifier.services;

import dbo.notifier.model.User;
import dbo.notifier.model.NotifConfig;
import dbo.notifier.services.enumeration.EventType;
import dbo.notifier.services.enumeration.ScheduledEventNames;

import java.util.List;

public interface IUsersManagement {
    void registerOrRefresh(String fcmToken);
    List<String> getAllFcm();
    boolean updateNotif(String userId, String field, boolean activated);

    NotifConfig getNotificationsConfig(String userId);

    List<User> getAllUsers();
    boolean notifyFor(EventType event, NotifConfig nc);
}
