package dbo.notifier.services;

import dbo.notifier.dto.NewsMessage;
import dbo.notifier.model.User;

import java.util.List;

public interface IDatabaseApi {
    void createUser(User user);

    void updateUser(User us);

    void addNewEvent(String eventName, String time);

    int getAll();

    void addNewWorldBoss(String toString);

    int allBoss();

    boolean addNews(NewsMessage newsMessage);

    int getNews();
    int getAllFcm();

    void registerOrRefreshUser(String fcmToken);
}
