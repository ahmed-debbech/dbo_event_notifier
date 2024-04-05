package dbo.notifier.services;

import dbo.notifier.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UsersManagement implements IUsersManagement{


    @Autowired
    private IDatabaseApi databaseApi;

    @Override
    public void registerNewUser(String fcmToken) {
        User us = new User();
        us.setFcmToken(fcmToken);
        us.setCreatedAt(LocalDateTime.now());
        us.setLastOpening(LocalDateTime.now());

        databaseApi.createUser(us);
    }

    @Override
    public void refreshExistingUser(String fcmToken) {
        User us = new User();
        us.setFcmToken(fcmToken);
        us.setLastOpening(LocalDateTime.now());
        databaseApi.updateUser(us);
    }
}
