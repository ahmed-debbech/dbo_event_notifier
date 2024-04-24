package dbo.notifier.services;

import java.util.List;

public interface IUsersManagement {
    void registerOrRefresh(String fcmToken);
    List<String> getAllFcm();
}
