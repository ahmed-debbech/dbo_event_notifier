package dbo.notifier.services;

import dbo.notifier.model.User;

public interface IDatabaseApi {
    void createUser(User user);

    void updateUser(User us);
}
