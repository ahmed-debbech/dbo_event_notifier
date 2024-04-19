package dbo.notifier.services;

import dbo.notifier.model.User;

import java.util.List;

public interface IDatabaseApi {
    void createUser(User user);

    void updateUser(User us);

    void addNewEvent(String eventName, String time);

    int getAll();

    void addNewWorldBoss(String toString);
}
