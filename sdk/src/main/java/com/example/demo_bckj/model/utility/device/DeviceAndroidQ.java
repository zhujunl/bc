package com.example.demo_bckj.model.utility.device;

import androidx.annotation.NonNull;

/**
 * @author ZJL
 * @date 2023/1/16 10:51
 * @des
 * @updateAuthor
 * @updateDes
 */

public class DeviceAndroidQ {
    private String aaid;
    private String oaid;
    private String vaid;

    public DeviceAndroidQ() {
        aaid = "";
        oaid = "";
        vaid = "";
    }

    public DeviceAndroidQ(String aaid, String oaid, String vaid) {
        this.aaid = aaid;
        this.oaid = oaid;
        this.vaid = vaid;
    }

    public String getAaid() {
        return aaid;
    }

    public void setAaid(String aaid) {
        this.aaid = aaid;
    }

    public String getOaid() {
        return oaid;
    }

    public void setOaid(String oaid) {
        this.oaid = oaid;
    }

    public String getVaid() {
        return vaid;
    }

    public void setVaid(String vaid) {
        this.vaid = vaid;
    }

    @NonNull
    @Override
    public String toString() {
        return "{" +
                "\"aaid\": \"" + aaid + '\"' +
                ",\"oaid\": \"" + oaid + '\"' +
                ",\"vaid\": \"" + vaid + '\"' +
                '}';
    }
}
