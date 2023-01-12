package com.example.demo_bckj.model.bean;


import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author WangKun
 * @description:
 * @date :2022/9/27 16:28
 */
public class DemoBean {

    @SerializedName("type")
    private String type;
    @SerializedName("game")
    private String game;
    @SerializedName("channel")
    private String channel;
    @SerializedName("package")
    private String packageX;
    @SerializedName("plan")
    private String plan;
    @SerializedName("material")
    private String material;
    @SerializedName("device")
    private DeviceBean device;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getPackageX() {
        return packageX;
    }

    public void setPackageX(String packageX) {
        this.packageX = packageX;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public DeviceBean getDevice() {
        return device;
    }

    public void setDevice(DeviceBean device) {
        this.device = device;
    }

    public static class DeviceBean {
        @SerializedName("os")
        private String os;
        @SerializedName("android")
        private AndroidBean android;
        @SerializedName("network")
        private NetworkBean network;

        public String getOs() {
            return os;
        }

        public void setOs(String os) {
            this.os = os;
        }

        public AndroidBean getAndroid() {
            return android;
        }

        public void setAndroid(AndroidBean android) {
            this.android = android;
        }

        public NetworkBean getNetwork() {
            return network;
        }

        public void setNetwork(NetworkBean network) {
            this.network = network;
        }

        public static class AndroidBean {
            @SerializedName("system_version")
            private String systemVersion;
            @SerializedName("android_id")
            private String androidId;
            @SerializedName("android_q")
            private AndroidQBean androidQ;
            @SerializedName("id")
            private String id;
            @SerializedName("imsi")
            private String imsi;
            @SerializedName("model")
            private String model;
            @SerializedName("product")
            private String product;
            @SerializedName("brand")
            private String brand;
            @SerializedName("game_package_name")
            private String gamePackageName;
            @SerializedName("game_version")
            private String gameVersion;
            @SerializedName("sdk_package_name")
            private String sdkPackageName;
            @SerializedName("sdk_version")
            private String sdkVersion;
            @SerializedName("serial")
            private String serial;
            @SerializedName("imei")
            private List<String> imei;
            @SerializedName("sim_serial")
            private List<String> simSerial;

            public String getSystemVersion() {
                return systemVersion;
            }

            public void setSystemVersion(String systemVersion) {
                this.systemVersion = systemVersion;
            }

            public String getAndroidId() {
                return androidId;
            }

            public void setAndroidId(String androidId) {
                this.androidId = androidId;
            }

            public AndroidQBean getAndroidQ() {
                return androidQ;
            }

            public void setAndroidQ(AndroidQBean androidQ) {
                this.androidQ = androidQ;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getImsi() {
                return imsi;
            }

            public void setImsi(String imsi) {
                this.imsi = imsi;
            }

            public String getModel() {
                return model;
            }

            public void setModel(String model) {
                this.model = model;
            }

            public String getProduct() {
                return product;
            }

            public void setProduct(String product) {
                this.product = product;
            }

            public String getBrand() {
                return brand;
            }

            public void setBrand(String brand) {
                this.brand = brand;
            }

            public String getGamePackageName() {
                return gamePackageName;
            }

            public void setGamePackageName(String gamePackageName) {
                this.gamePackageName = gamePackageName;
            }

            public String getGameVersion() {
                return gameVersion;
            }

            public void setGameVersion(String gameVersion) {
                this.gameVersion = gameVersion;
            }

            public String getSdkPackageName() {
                return sdkPackageName;
            }

            public void setSdkPackageName(String sdkPackageName) {
                this.sdkPackageName = sdkPackageName;
            }

            public String getSdkVersion() {
                return sdkVersion;
            }

            public void setSdkVersion(String sdkVersion) {
                this.sdkVersion = sdkVersion;
            }

            public String getSerial() {
                return serial;
            }

            public void setSerial(String serial) {
                this.serial = serial;
            }

            public List<String> getImei() {
                return imei;
            }

            public void setImei(List<String> imei) {
                this.imei = imei;
            }

            public List<String> getSimSerial() {
                return simSerial;
            }

            public void setSimSerial(List<String> simSerial) {
                this.simSerial = simSerial;
            }

            public static class AndroidQBean {
                @SerializedName("aaid")
                private String aaid;
                @SerializedName("oaid")
                private String oaid;
                @SerializedName("vaid")
                private String vaid;

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
            }
        }

        public static class NetworkBean {
            @SerializedName("code")
            private int code;
            @SerializedName("intranet_ip")
            private String intranetIp;
            @SerializedName("mac")
            private String mac;
            @SerializedName("name")
            private String name;
            @SerializedName("type")
            private String type;

            public int getCode() {
                return code;
            }

            public void setCode(int code) {
                this.code = code;
            }

            public String getIntranetIp() {
                return intranetIp;
            }

            public void setIntranetIp(String intranetIp) {
                this.intranetIp = intranetIp;
            }

            public String getMac() {
                return mac;
            }

            public void setMac(String mac) {
                this.mac = mac;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }
        }
    }
}
