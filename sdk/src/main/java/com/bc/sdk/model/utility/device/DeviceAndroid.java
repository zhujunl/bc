package com.bc.sdk.model.utility.device;

import android.content.Context;
import android.os.Build;

import com.bc.sdk.BuildConfig;
import com.bc.sdk.model.utility.DeviceIdUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * @author ZJL
 * @date 2023/1/16 10:50
 * @des
 * @updateAuthor
 * @updateDes
 */

public class DeviceAndroid {
    private String system_version;
    private String android_id;
    private DeviceAndroidQ android_q;
    private String id;
    private List<Object> imei;
    private String imsi;
    private String model;
    private String product;
    private String brand;
    private String game_package_name;
    private String game_version;
    private String sdk_package_name;
    private String sdk_version;
    private String serial;
    private List<Object> sim_serial;

    public DeviceAndroid(Context context) {
        system_version = DeviceIdUtil.getSystemVersion();
        android_id = DeviceIdUtil.getAndroidId(context);
        id = DeviceIdUtil.getBasebandVersion();
        android_q = new DeviceAndroidQ();
        imei = new ArrayList<>(Arrays.asList(DeviceIdUtil.getIMEI_1(context), DeviceIdUtil.getIMEI_2(context)));
        imsi = DeviceIdUtil.getIMSI(context);
        model = Build.MODEL;
        product = Build.MANUFACTURER;
        brand = Build.BRAND;
        game_package_name = DeviceIdUtil.getTopPackage(context);
        game_version = DeviceIdUtil.getVersionName(context);
        sdk_package_name = "com.infinite.game";
        sdk_version = BuildConfig.version;
        serial = DeviceIdUtil.getSERIAL();
        sim_serial = new ArrayList<>(Arrays.asList(DeviceIdUtil.getSimSerial(context)));
    }

    public DeviceAndroid(Builder build) {
        setSystem_version(build.system_version);
        setAndroid_id(build.android_id);
        setAndroid_q(build.android_q);
        setId(build.id);
        setImei(build.imei);
        setImsi(build.imsi);
        setModel(build.model);
        setProduct(build.product);
        setBrand(build.brand);
        setGame_package_name(build.game_package_name);
        setGame_version(build.game_version);
        setSdk_package_name(build.sdk_package_name);
        setSdk_version(build.sdk_version);
        setSerial(build.serial);
        setSim_serial(build.sim_serial);
    }

    public void setSystem_version(String system_version) {
        this.system_version = system_version;
    }

    public void setAndroid_id(String android_id) {
        this.android_id = android_id;
    }

    public void setAndroid_q(DeviceAndroidQ android_q) {
        this.android_q = android_q;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setImei(List<Object> imei) {
        this.imei = imei;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setGame_package_name(String game_package_name) {
        this.game_package_name = game_package_name;
    }

    public void setGame_version(String game_version) {
        this.game_version = game_version;
    }

    public void setSdk_package_name(String sdk_package_name) {
        this.sdk_package_name = sdk_package_name;
    }

    public void setSdk_version(String sdk_version) {
        this.sdk_version = sdk_version;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public void setSim_serial(List<Object> sim_serial) {
        this.sim_serial = sim_serial;
    }

    public String getSystem_version() {
        return system_version;
    }

    public String getAndroid_id() {
        return android_id;
    }

    public DeviceAndroidQ getAndroid_q() {
        return android_q;
    }

    public String getId() {
        return id;
    }

    public List<Object> getImei() {
        return imei;
    }

    public String getImsi() {
        return imsi;
    }

    public String getModel() {
        return model;
    }

    public String getProduct() {
        return product;
    }

    public String getBrand() {
        return brand;
    }

    public String getGame_package_name() {
        return game_package_name;
    }

    public String getGame_version() {
        return game_version;
    }

    public String getSdk_package_name() {
        return sdk_package_name;
    }

    public String getSdk_version() {
        return sdk_version;
    }

    public String getSerial() {
        return serial;
    }

    public List<Object> getSim_serial() {
        return sim_serial;
    }

    public static class Builder {
        private String system_version;
        private String android_id;
        private DeviceAndroidQ android_q;
        private String id;
        private List<Object> imei;
        private String imsi;
        private String model;
        private String product;
        private String brand;
        private String game_package_name;
        private String game_version;
        private String sdk_package_name;
        private String sdk_version;
        private String serial;
        private List<Object> sim_serial;

        public Builder system_version(String val) {
            this.system_version = val;
            return this;
        }

        public Builder android_id(String val) {
            this.android_id = val;
            return this;
        }

        public Builder android_q(DeviceAndroidQ val) {
            this.android_q = val;
            return this;
        }

        public Builder id(String val) {
            this.id = val;
            return this;
        }

        public Builder imei(List<Object> val) {
            this.imei = val;
            return this;
        }

        public Builder imsi(String val) {
            this.imsi = val;
            return this;
        }

        public Builder model(String val) {
            this.model = val;
            return this;
        }

        public Builder product(String val) {
            this.product = val;
            return this;
        }

        public Builder brand(String val) {
            this.brand = val;
            return this;
        }

        public Builder game_package_name(String val) {
            this.game_package_name = val;
            return this;
        }

        public Builder game_version(String val) {
            this.game_version = val;
            return this;
        }

        public Builder sdk_package_name(String val) {
            this.sdk_package_name = val;
            return this;
        }

        public Builder sdk_version(String val) {
            this.sdk_version = val;
            return this;
        }

        public Builder serial(String val) {
            this.serial = val;
            return this;
        }

        public Builder sim_serial(List<Object> val) {
            this.sim_serial = val;
            return this;
        }

        public DeviceAndroid build() {
            return new DeviceAndroid(this);
        }
    }

    @Override
    public String toString() {
        return "{" +
                "\"system_version\": \"" + system_version + "\"," +
                "\"android_id\": \"" + android_id + "\"," +
                "\"android_q\":" + android_q + "," +
                "\"id\": \"" + id + "\"," +
                "\"imei\": " + ListToString(imei) + "," +
                "\"imsi\": \"" + imsi + "\"," +
                "\"model\": \"" + model + "\"," +
                "\"product\": \"" + product + "\"," +
                "\"brand\": \"" + brand + "\"," +
                "\"game_package_name\": \"" + game_package_name + "\"," +
                "\"game_version\": \"" + game_version + "\"," +
                "\"sdk_package_name\": \"" + sdk_package_name + "\"," +
                "\"sdk_version\": \"" + sdk_version + "\"," +
                "\"serial\": \"" + serial + "\"," +
                "\"sim_serial\": " + ListToString(sim_serial) + "" +
                "}";
    }


    public String ListToString(List<Object> lists) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("[");
        for (int i = 0; i < lists.size(); i++) {
            Object o = lists.get(i);
            stringBuffer.append("\"").append(o.toString()).append("\"");
            if (i < lists.size() - 1)
                stringBuffer.append(",");
        }
        stringBuffer.append("]");
        return stringBuffer.toString();
    }
}
