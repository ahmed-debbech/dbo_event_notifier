package dbo.notifier.model;

import java.time.LocalDateTime;

public class User {

    private String fcmToken; //id
    private String createdAt;
    private String lastOpening;

    public User() {
    }

    @Override
    public String toString() {
        return "User{" +
                ", createdAt=" + createdAt +
                ", lastOpening=" + lastOpening +
                ", fcmToken='" + fcmToken + '\'' +
                '}';
    }


    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getLastOpening() {
        return lastOpening;
    }

    public void setLastOpening(String lastOpening) {
        this.lastOpening = lastOpening;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }
}
