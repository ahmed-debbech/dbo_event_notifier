package dbo.notifier.services.firebase;

import com.google.firebase.database.*;
import dbo.notifier.dto.NewsMessage;
import dbo.notifier.logger.LogFirebase;
import dbo.notifier.model.FirebaseEvents;
import dbo.notifier.model.NotifConfig;
import dbo.notifier.model.User;
import dbo.notifier.utils.FirebaseEventsConverter;
import dbo.notifier.utils.ResultRetreiver;
import dbo.notifier.utils.UUIDGen;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FirebaseRealtimeDb implements IDatabaseApi {

    private LogFirebase out = new LogFirebase();

    @Value("${firebase.root}")
    private String rootPath;

    @Override
    public void createUser(User user) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(rootPath +"/users/"+user.getFcmToken());

        ref.setValueAsync(user);
    }

    @Override
    public void updateUser(User user) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(rootPath + "/users/"+user.getFcmToken()+"/lastOpening");

        ref.setValueAsync(user.getLastOpening());
    }

    @Override
    public void addNewEvent(String eventName, String time) {
        out.log("a new event is being added");
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(rootPath+"/events/"+ UUIDGen.generate());
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
        DatabaseReference ref = database.getReference(rootPath+"/events");
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
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(rootPath+"/boss/"+uuid);
        Map<String, String> map = new HashMap<>();
        map.put("time", time);
        ref.setValueAsync(map);
    }

    @Override
    public int allBoss() {
        out.log("retreiving all bosses");
        int n = UUIDGen.fourNumbers();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference(rootPath+"/boss");
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
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference(rootPath+"/news/" + UUIDGen.generate());
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
        DatabaseReference ref = database.getReference(rootPath+"/news");
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
        DatabaseReference ref = database.getReference(rootPath+"/users");
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
        DatabaseReference ref = database.getReference(rootPath+"/users/" + fcmToken);
        ref.addListenerForSingleValueEvent((new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.err.println(dataSnapshot);
                if(dataSnapshot.getValue() != null) {
                    out.log("User: [[" + fcmToken + "]] has entered.");
                    String timest = ((Map<String, String>) dataSnapshot.getValue()).get("createdAt");
                    Map<String, Boolean> nc = (((Map<String, Map<String, Boolean>>) dataSnapshot.getValue()).get("notif_config"));
                    NotifConfig ncc = new NotifConfig(nc);
                    us.setNotif_config(ncc);
                    us.setCreatedAt(timest);
                }else {
                    out.log("Creating new user with : [" +fcmToken+"]" );
                    NotifConfig nc = new NotifConfig();
                    us.setNotif_config(nc);
                    us.setCreatedAt(String.valueOf(new Date().getTime()));
                }
                createUser(us);
            }
            @Override public void onCancelled(DatabaseError databaseError) {}
        }));
    }

    @Override
    public int getNotificationsConfigForUser(String fcmToken) {
        out.log("get notification config for user [["+ fcmToken+"]]");
        int n = UUIDGen.fourNumbers();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference(rootPath+"/users/" + fcmToken + "/notif_config");
        ref.addListenerForSingleValueEvent((new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.err.println(dataSnapshot);
                if (dataSnapshot.getValue() != null) {
                    out.log("got user notification config successfully");
                    Map<String, Boolean> nc = (Map<String, Boolean>) dataSnapshot.getValue();
                    NotifConfig ncc = new NotifConfig(nc);
                    ResultRetreiver.getInstance().add(n, ncc);
                } else {
                    out.log("no notif config found for user " + fcmToken);
                    ResultRetreiver.getInstance().add(n, null);
                }
            }
            @Override public void onCancelled(DatabaseError databaseError) {}
        }));
        return n;
    }

    @Override
    public void changeNotif(String userId, String field, boolean activated) {
        out.log("changing notification to "+activated+" for user " + userId);
        try {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference(rootPath+"/users/" + userId + "/notif_config/"+field);
            ref.setValueAsync(activated);
            out.log("done changing notif");
        }catch (Exception e){
            out.log("error happened while changing notif");
        }
    }

    @Override
    public int getUsers() {
        out.log("getting all users from databases");
        int n = UUIDGen.fourNumbers();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference(rootPath+"/users");
        ref.addListenerForSingleValueEvent((new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //System.err.println(dataSnapshot);
                //List<String> fcms = ((Map<String, ?>) dataSnapshot.getValue()).keySet().stream().collect(Collectors.toList());
                Map<String, ?> map = ((Map<String, ?>) dataSnapshot.getValue());
                List<User> fcms = (List<User>) new ArrayList<>(map.values());
                ResultRetreiver.getInstance().add(n, fcms);
            }
            @Override public void onCancelled(DatabaseError databaseError) {}
        }));
        return n;
    }


}
