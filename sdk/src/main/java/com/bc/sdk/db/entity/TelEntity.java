package com.bc.sdk.db.entity;

/**
 * @author ZJL
 * @date 2023/1/31 9:58
 * @des
 * @updateAuthor
 * @updateDes
 */
public class TelEntity {
    private int id;
    private String telNumber;
    private long time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTelNumber() {
        return telNumber;
    }

    public void setTelNumber(String telNumber) {
        this.telNumber = telNumber;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "TelEntity{" +
                "id=" + id +
                ", telNumber='" + telNumber + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
