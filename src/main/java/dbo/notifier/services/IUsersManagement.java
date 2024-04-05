package dbo.notifier.services;

public interface IUsersManagement {
    void registerNewUser(String fcmToken);

    void refreshExistingUser(String fcmToken);
}
