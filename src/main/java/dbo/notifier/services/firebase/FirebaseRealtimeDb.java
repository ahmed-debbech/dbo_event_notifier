package dbo.notifier.services.firebase;

import com.google.firebase.database.*;
import dbo.notifier.model.FirebaseEvent;
import dbo.notifier.model.FirebaseEvents;
import dbo.notifier.model.User;
import dbo.notifier.services.IDatabaseApi;
import dbo.notifier.utils.FirebaseEventsConverter;
import dbo.notifier.utils.ResultRetreiver;
import dbo.notifier.utils.UUIDGen;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("/boss/");
        Map<String, String> map = new HashMap<>();
        map.put(UUIDGen.generate(), time);
        ref.setValueAsync(map);
    }

}
