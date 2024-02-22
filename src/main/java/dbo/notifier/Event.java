package dbo.notifier;


public class Event {
    private String name;
    private String day;
    private String schedule;
    private String countdown;

    public Event(String name, String day, String schedule, String countdown) {
        this.name = name;
        this.day = day;
        this.schedule = schedule;
        this.countdown = countdown;
    }


    @Override
    public String toString() {
        return "Event{" +
                "name='" + name + '\'' +
                ", day='" + day + '\'' +
                ", schedule='" + schedule + '\'' +
                ", countdown='" + countdown + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getCountdown() {
        return countdown;
    }

    public void setCountdown(String countdown) {
        this.countdown = countdown;
    }
}
