package dbo.notifier.utils;

import dbo.notifier.model.FirebaseEvent;
import dbo.notifier.model.FirebaseEvents;
import dbo.notifier.model.NotifConfig;
import dbo.notifier.model.User;

import java.util.*;

public class FirebaseConverter {

    public static List<FirebaseEvents> convert(Map<?,Map<String,String>> map){
        List<FirebaseEvents> fes = new ArrayList<>();

        for (Map.Entry<?,Map<String,String>> entry : map.entrySet()){
            FirebaseEvents fi = new FirebaseEvents();
            fi.uuid = (String) entry.getKey();
            //fi.fe = (FirebaseEvent) entry.getValue();
            fi.fe = new FirebaseEvent(
                    new ArrayList<>(entry.getValue().entrySet()).get(0).getValue(),
                    new ArrayList<>(entry.getValue().entrySet()).get(1).getValue()
            );
            //System.err.println(entry.getValue().toString());
            fes.add(fi);
        }
        return fes;
    }
    public static List<User> convertUsers(Map<String, Map<String, ?>> map){
        List<User> fes = new ArrayList<>();

        for (Map.Entry<?,Map<String,?>> entry : map.entrySet()){
            User fi = new User();
            fi.setCreatedAt((String) entry.getValue().get("createdAt"));
            fi.setFcmToken((String) entry.getValue().get("fcmToken"));
            fi.setLastOpening((String) entry.getValue().get("lastOpening"));
            fi.setNotif_config(null);
            NotifConfig nc = new NotifConfig();
            nc.adult_party = ((Map<String, Boolean>) entry.getValue().get("notif_config")).get("adult_party");
            nc.adult_solo = ((Map<String, Boolean>) entry.getValue().get("notif_config")).get("adult_solo");
            nc.kid_party = ((Map<String, Boolean>) entry.getValue().get("notif_config")).get("kid_party");
            nc.kid_solo = ((Map<String, Boolean>) entry.getValue().get("notif_config")).get("kid_solo");
            nc.db_scramble = ((Map<String, Boolean>) entry.getValue().get("notif_config")).get("db_scramble");
            nc.dojo_war = ((Map<String, Boolean>) entry.getValue().get("notif_config")).get("dojo_war");
            fi.setNotif_config(nc);
            //System.err.println(entry.getValue().toString());
            fes.add(fi);
        }
        return fes;
    }
}
