package com.example.demo_bckj.model.utility.device;

import java.util.Iterator;
import java.util.Map;

import androidx.annotation.NonNull;

/**
 * @author ZJL
 * @date 2023/1/16 10:39
 * @des
 * @updateAuthor
 * @updateDes
 */

public class DeviceInfo {
    private Map<String, String> map;
    private Device device;

    public DeviceInfo(Map<String, String> gameConfig, Device device) {
        this.map = gameConfig;
        this.device = device;
    }

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    @NonNull
    @Override
    public String toString() {
        Iterator<String> iterator = map.keySet().iterator();
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        while (iterator.hasNext()) {
            String next = iterator.next();
            String s = map.get(next);
            sb.append("\"").append(next).append("\":\"").append(s).append("\",");
        }
        return sb.toString() +
                "\"device\":" + device +
                '}';
    }
}
