package dbo.notifier.services;

import dbo.notifier.services.firebase.IDatabaseApi;
import dbo.notifier.utils.ResultRetreiver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsersManagement implements IUsersManagement{


    @Autowired
    private IDatabaseApi databaseApi;

    @Override
    public void registerOrRefresh(String fcmToken) {
        databaseApi.registerOrRefreshUser(fcmToken);
    }

    @Override
    public List<String> getAllFcm() {
        int pid = databaseApi.getAllFcm();
        return (List<String>) ResultRetreiver.getInstance().waitFor(pid);
    }
}
