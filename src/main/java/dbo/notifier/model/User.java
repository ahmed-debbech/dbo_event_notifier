package dbo.notifier.model;

import java.time.LocalDateTime;

public class User {

    private String fcmToken; //id
    private LocalDateTime createdAt;
    private LocalDateTime lastOpening;

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


    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getLastOpening() {
        return lastOpening;
    }

    public void setLastOpening(LocalDateTime lastOpening) {
        this.lastOpening = lastOpening;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }
}
