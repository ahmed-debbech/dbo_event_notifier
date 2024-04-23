package dbo.notifier.services;

import com.google.firebase.database.*;
import dbo.notifier.model.FirebaseEvents;
import dbo.notifier.model.User;
import dbo.notifier.utils.FirebaseEventsConverter;
import dbo.notifier.utils.ResultRetreiver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class UsersManagement implements IUsersManagement{


    @Autowired
    private IDatabaseApi databaseApi;

    @Override
    public void registerOrRefresh(String fcmToken) {
        int[] i = {0};
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
                databaseApi.createUser(us);
            }
            @Override public void onCancelled(DatabaseError databaseError) {}
        }));
    }
}
