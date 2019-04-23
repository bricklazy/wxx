package com.wlx.data;

public class Data {

    /**
     * message : http://work.zanykj.com/img/LLLLLLL.jpg
     * id : 14
     * time : 1555663047
     * imei : 11111111
     * messageType : 2
     */

    private String message;
    private int id;
    private int time;
    private String imei;
    private int messageType;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTime() {
        return time*1000l;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public int getMessageType() {
        return messageType;
    }

    public void setMessageType(int messageType) {
        this.messageType = messageType;
    }
}
