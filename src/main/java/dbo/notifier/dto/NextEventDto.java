package dbo.notifier.dto;

public class NextEventDto {
    private String nextEvent;
    private String nextNotification;


    @Override
    public String toString() {
        return "NextEventDto{" +
                "nextEvent='" + nextEvent + '\'' +
                ", nextNotification='" + nextNotification + '\'' +
                '}';
    }

    public String getNextEvent() {
        return nextEvent;
    }

    public void setNextEvent(String nextEvent) {
        this.nextEvent = nextEvent;
    }

    public String getNextNotification() {
        return nextNotification;
    }

    public void setNextNotification(String nextNotification) {
        this.nextNotification = nextNotification;
    }
}
