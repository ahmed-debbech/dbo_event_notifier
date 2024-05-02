package dbo.notifier.model;


public class User {

    private String fcmToken; //id
    private String createdAt;
    private String lastOpening;
    private NotifConfig notif_config;

    public User() {
    }

    @Override
    public String toString() {
        return "User{" +
                "fcmToken='" + fcmToken + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", lastOpening='" + lastOpening + '\'' +
                ", notif_config=" + notif_config +
                '}';
    }

    public NotifConfig getNotif_config() {
        return notif_config;
    }

    public void setNotif_config(NotifConfig notif_config) {
        this.notif_config = notif_config;
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
