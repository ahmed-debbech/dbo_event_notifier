package dbo.notifier.services.firebase;

import com.google.firebase.database.*;
import dbo.notifier.dto.NewsMessage;
import dbo.notifier.logger.FBLogger;
import dbo.notifier.logger.LogSurprise;
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

    private FBLogger out = new FBLogger();

    @Override
    public void createUser(User user) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("/v0.9/users/"+user.getFcmToken());

        ref.setValueAsync(user);
    }

    @Override
    public void updateUser(User user) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("/v0.9/users/"+user.getFcmToken()+"/lastOpening");

        ref.setValueAsync(user.getLastOpening());
    }

    @Override
    public void addNewEvent(String eventName, String time) {
        out.log("a new event is being added");
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("/v0.9/events/"+ UUIDGen.generate());
        Map<String, String> map = new HashMap<>();
        map.put("name", eventName);
        map.put("time", time);
        ref.setValueAsync(map);
    }

    @Override
    public int getAll() {
        out.log("getting all events");
        int n = UUIDGen.fourNumbers();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("/v0.9/events");
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
        out.log("a new world boss is being added");
        String uuid = UUIDGen.generate();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("/v0.9/boss/"+uuid);
        Map<String, String> map = new HashMap<>();
        map.put("time", time);
        ref.setValueAsync(map);
    }

    @Override
    public int allBoss() {
        out.log("retreiving all bosses");
        int n = UUIDGen.fourNumbers();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("/v0.9/boss");
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
        out.log("adding new News Message");
        try {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("/v0.9/news/" + UUIDGen.generate());
            newsMessage.setTime(String.valueOf(new Date().getTime()));
            ref.setValueAsync(newsMessage);
        }catch (Exception e){
            return false;
        }
        return true;
    }

    @Override
    public int getNews() {
        out.log("getting all news");
        int n = UUIDGen.fourNumbers();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("/v0.9/news");
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
        out.log("getting all fcm tokens");
        int n = UUIDGen.fourNumbers();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("/v0.9/users");
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
        out.log("registering or refreshing a new user");
        User us = new User();
        us.setFcmToken(fcmToken);
        us.setLastOpening(String.valueOf(new Date().getTime()));
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("/v0.9/users/" + fcmToken);
        ref.addListenerForSingleValueEvent((new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.err.println(dataSnapshot);
                if(dataSnapshot.getValue() != null) {
                    out.log("User: [[" + fcmToken + "]] has entered.");
                    String timest = ((Map<String, String>) dataSnapshot.getValue()).get("createdAt");
                    us.setCreatedAt(timest);
                }else {
                    out.log("Creating new user with : [" +fcmToken+"]" );
                    us.setCreatedAt(String.valueOf(new Date().getTime()));
                }
                createUser(us);
            }
            @Override public void onCancelled(DatabaseError databaseError) {}
        }));
    }


}
