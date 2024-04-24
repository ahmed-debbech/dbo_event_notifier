package dbo.notifier.services.firebase;

import com.google.firebase.database.*;
import dbo.notifier.dto.NewsMessage;
import dbo.notifier.model.FirebaseEvents;
import dbo.notifier.model.User;
import dbo.notifier.services.IDatabaseApi;
import dbo.notifier.utils.FirebaseEventsConverter;
import dbo.notifier.utils.ResultRetreiver;
import dbo.notifier.utils.UUIDGen;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FirebaseRealtimeDb implements IDatabaseApi {
    @Override
    public void createUser(User user) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("/users/"+user.getFcmToken());

        ref.setValueAsync(user);
    }

    @Override
    public void updateUser(User user) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("/users/"+user.getFcmToken()+"/lastOpening");

        ref.setValueAsync(user.getLastOpening());
    }

    @Override
    public void addNewEvent(String eventName, String time) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("/events/"+ UUIDGen.generate());
        Map<String, String> map = new HashMap<>();
        map.put("name", eventName);
        map.put("time", time);
        ref.setValueAsync(map);
    }

    @Override
    public int getAll() {
        int n = UUIDGen.fourNumbers();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("events");
        ref.addListenerForSingleValueEvent((new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.err.println(dataSnapshot);
                Map<?,Map<String,String>> map = (Map<?, Map<String,String>>) dataSnapshot.getValue();
                List<FirebaseEvents> fes = FirebaseEventsConverter.convert(map);
                ResultRetreiver.getInstance().add(n, fes);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        }));
        return n;
    }

    @Override
    public void addNewWorldBoss(String time) {
        String uuid = UUIDGen.generate();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("/boss/"+uuid);
        Map<String, String> map = new HashMap<>();
        map.put("time", time);
        ref.setValueAsync(map);
    }

    @Override
    public int allBoss() {
        int n = UUIDGen.fourNumbers();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("boss");
        ref.addListenerForSingleValueEvent((new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<?,Map<?, String>> map = (Map<?,Map<?,String>>) dataSnapshot.getValue();
                List<String> ss = new ArrayList<>();
                for(Map<?, String> innerMap : map.values()){
                    ss.addAll(innerMap.values());
                }
                List<String> fes = new ArrayList<String>(ss);

                ResultRetreiver.getInstance().add(n, fes);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        }));
        return n;
    }

    @Override
    public boolean addNews(NewsMessage newsMessage) {
        try {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("/news/" + UUIDGen.generate());
            newsMessage.setTime(String.valueOf(new Date().getTime()));
            ref.setValueAsync(newsMessage);
        }catch (Exception e){
            return false;
        }
        return true;
    }

    @Override
    public int getNews() {
        int n = UUIDGen.fourNumbers();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("news");
        ref.addListenerForSingleValueEvent((new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.err.println(dataSnapshot);
                Map<String,NewsMessage> map = (Map<String,NewsMessage>) dataSnapshot.getValue();
                List<NewsMessage> fes = new ArrayList<NewsMessage>(map.values());
                ResultRetreiver.getInstance().add(n, fes);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        }));
        return n;
    }

    @Override
    public int getAllFcm() {
        int n = UUIDGen.fourNumbers();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("/users");
        ref.addListenerForSingleValueEvent((new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.err.println(dataSnapshot);
                List<String> fcms = ((Map<String, ?>) dataSnapshot.getValue()).keySet().stream().collect(Collectors.toList());
                System.err.println(fcms);
                ResultRetreiver.getInstance().add(n, fcms);
            }
            @Override public void onCancelled(DatabaseError databaseError) {}
        }));
        return n;
    }

    @Override
    public void registerOrRefreshUser(String fcmToken) {
        User us = new User();
        us.setFcmToken(fcmToken);
        us.setLastOpening(String.valueOf(new Date().getTime()));
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("/users/" + fcmToken);
        ref.addListenerForSingleValueEvent((new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.err.println(dataSnapshot);
                if(dataSnapshot.getValue() != null) {
                    String timest = ((Map<String, String>) dataSnapshot.getValue()).get("createdAt");
                    us.setCreatedAt(timest);
                }else {
                    us.setCreatedAt(String.valueOf(new Date().getTime()));
                }
                createUser(us);
            }
            @Override public void onCancelled(DatabaseError databaseError) {}
        }));
    }


}
