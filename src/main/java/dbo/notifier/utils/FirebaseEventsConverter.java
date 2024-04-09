package dbo.notifier.utils;

import dbo.notifier.model.FirebaseEvent;
import dbo.notifier.model.FirebaseEvents;

import java.util.*;
import java.util.stream.Collectors;

public class FirebaseEventsConverter {

    public static List<FirebaseEvents> convert(Map<?,Map<String,String>> map){
        List<FirebaseEvents> fes = new ArrayList<>();

        for (Map.Entry<?,Map<String,String>> entry : map.entrySet()){
            FirebaseEvents fi = new FirebaseEvents();
            fi.uuid = (String) entry.getKey();
            //fi.fe = (FirebaseEvent) entry.getValue();
            fi.fe = new FirebaseEvent(new ArrayList<>(entry.getValue().entrySet()).get(0).getValue(), new ArrayList<>(entry.getValue().entrySet()).get(1).getValue());
            //System.err.println(entry.getValue().toString());
            fes.add(fi);
        }
        return fes;
    }
}
