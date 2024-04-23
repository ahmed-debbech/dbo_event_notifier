package dbo.notifier.services;

public interface IUsersManagement {
    void registerOrRefresh(String fcmToken);
}
