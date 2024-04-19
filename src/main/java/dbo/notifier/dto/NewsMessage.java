package dbo.notifier.dto;

import java.util.Date;

public class NewsMessage {

    private String title;
    private String text;
    private String time;

    @Override
    public String toString() {
        return "NewsMessage{" +
                "title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", time=" + time +
                '}';
    }

    public NewsMessage() {
    }

    public NewsMessage(String title, String text, String time) {
        this.title = title;
        this.text = text;
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
