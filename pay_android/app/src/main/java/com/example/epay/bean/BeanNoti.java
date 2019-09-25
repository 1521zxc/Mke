package com.example.epay.bean;


/**
 * 各类通知
 * Created by liujin on 2015/7/31.
 */
public class BeanNoti  {

    private int type;
    private String title = "";
    private String msg = "";
    private long time;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "BeanNoti{" +
                "type=" + type +
                ", title='" + title + '\'' +
                ", msg='" + msg + '\'' +
                ", time=" + time +
                '}';
    }
}
