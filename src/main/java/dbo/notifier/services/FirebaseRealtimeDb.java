package dbo.notifier.services;

import com.google.firebase.database.*;
import dbo.notifier.model.User;
import org.springframework.stereotype.Service;

@Service
public class FirebaseRealtimeDb implements IDatabaseApi{
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
}
