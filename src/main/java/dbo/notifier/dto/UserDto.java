package dbo.notifier.dto;

import java.time.LocalDateTime;

public class UserDto {
    private String fcmToken;
    private LocalDateTime lastOpening;

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

    @Override
    public String toString() {
        return "UserDto{" +
                "fcmToken='" + fcmToken + '\'' +
                ", lastOpening=" + lastOpening +
                '}';
    }
}
