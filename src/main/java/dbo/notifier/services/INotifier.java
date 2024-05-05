package dbo.notifier.services;

import dbo.notifier.services.enumeration.EventType;
import dbo.notifier.services.enumeration.ServiceType;

public interface INotifier {
    void broadcastNotificationThroughEveryMedium(ServiceType serviceType);
    void broadcastNotificationThroughEveryMedium(ServiceType serviceType, EventType eventType);
}
