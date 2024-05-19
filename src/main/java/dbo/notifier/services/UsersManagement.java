package dbo.notifier.services;

import dbo.notifier.model.NotifConfig;
import dbo.notifier.model.User;
import dbo.notifier.services.enumeration.EventType;
import dbo.notifier.services.firebase.IDatabaseApi;
import dbo.notifier.utils.ResultRetreiver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
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

    @Override
    public boolean updateNotif(String userId, String field, boolean activated) {
        Class<?> objectClass = new NotifConfig().getClass();
        for (Field field1 : objectClass.getFields()) {
            if (field1.getName().equals(field)) {
                databaseApi.changeNotif(userId, field, activated);
                return true;
            }
        }
        return false;
    }

    @Override
    public NotifConfig getNotificationsConfig(String userId) {
        int pid = databaseApi.getNotificationsConfigForUser(userId);
        NotifConfig nc = (NotifConfig) ResultRetreiver.getInstance().waitFor(pid);
        if(nc == null) return null;
        return nc;
    }

    @Override
    public List<User> getAllUsers() {
        int pid = databaseApi.getUsers();
        return (List<User>) ResultRetreiver.getInstance().waitFor(pid);
    }

    @Override
    public boolean notifyFor(EventType event, NotifConfig nc) {
        boolean m = false;
        switch (event){
            case DOJO_WAR: m = nc.dojo_war;
                break;
            case DB_SCRAMBLE: m = nc.db_scramble;
                break;
            case BUDO_ADULT_TEAM: m = nc.adult_party;
                break;
            case BUDO_KID_TEAM: m = nc.kid_party;
                break;
            case BUDO_ADULT_SOLO: m = nc.adult_solo;
                break;
            case BUDO_KID_SOLO: m = nc.kid_solo;
                break;
        }
        return m;
    }
}
