package dbo.notifier;

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
}
