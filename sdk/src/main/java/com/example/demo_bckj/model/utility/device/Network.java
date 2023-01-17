package com.example.demo_bckj.model.utility.device;

/**
 * @author ZJL
 * @date 2023/1/16 10:40
 * @des
 * @updateAuthor
 * @updateDes
 */


public class Network {
    private int code;
    private String intranet_ip;
    private String mac;
    private String name;
    private String type;

    public Network() {
        code = 46002;
        intranet_ip = "10.48.6.16";
        mac = "e0:1f:88:33:01:f0";
        name = "那就这样";
        type = "wifi";
    }

    public Network(Builder build) {
        setCode(build.code);
        setIntranet_ip(build.intranet_ip);
        setMac(build.mac);
        setName(build.name);
        setType(build.type);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getIntranet_ip() {
        return intranet_ip;
    }

    public void setIntranet_ip(String intranet_ip) {
        this.intranet_ip = intranet_ip;
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

    public static class Builder {
        private int code;
        private String intranet_ip;
        private String mac;
        private String name;
        private String type;

        public Builder code(int val) {
            this.code = val;
            return this;
        }

        public Builder intranet_ip(String val) {
            this.intranet_ip = val;
            return this;
        }

        public Builder mac(String val) {
            this.mac = val;
            return this;
        }

        public Builder name(String val) {
            this.name = val;
            return this;
        }

        public Builder type(String val) {
            this.type = val;
            return this;
        }

        public Network build() {
            return new Network(this);
        }
    }

    @Override
    public String toString() {
        return "{" +
                "\"code\": " + code + "," +
                "\"intranet_ip\": \"" + intranet_ip + "\"," +
                "\"mac\": \"" + mac + "\"," +
                "\"name\": \"" + name + "\"," +
                "\"type\": \"" + type + "\"" +
                '}';
    }

}
