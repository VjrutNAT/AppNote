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

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getCreateAlarm() {
        return createAlarm;
    }

    public void setCreateAlarm(long createAlarm) {
        this.createAlarm = createAlarm;
    }

    public long getNotifyAlarm() {
        return notifyAlarm;
    }

    public void setNotifyAlarm(long notifyAlarm) {
        this.notifyAlarm = notifyAlarm;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
