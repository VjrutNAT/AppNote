package taro.rikkeisoft.com.assignment.model;

import java.io.Serializable;

/**
 * Created by VjrutNAT on 10/25/2017.
 */

public class Note implements Serializable {

    private long id;
    private String title;
    private String content;
    private long createAlarm;
    private long notifyAlarm ;
    private int color;

    public Note(long id, String title, String content, long createAlarm, long notifyAlarm, int color) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createAlarm = createAlarm;
        this.notifyAlarm = notifyAlarm;
        this.color = color;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public long getCreateAlarm() {
        return createAlarm;
    }

    public long getNotifyAlarm() {
        return notifyAlarm;
    }

    public int getColor() {
        return color;
    }
}
