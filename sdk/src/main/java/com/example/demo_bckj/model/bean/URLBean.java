package com.example.demo_bckj.model.bean;

/**
 * @author ZJL
 * @date 2023/1/5 13:18
 * @des
 * @updateAuthor
 * @updateDes
 */

//{
// "code":0,
// "message":"OK",
// "data":{
//     "protocol_url":{
//          "register":"https://static.infinite-game.cn/resources/protocol/register.html",
//          "privacy":"https://static.infinite-game.cn/resources/protocol/device.html",
//          "device":"https://static.infinite-game.cn/resources/protocol/device.html"
//          }
//    },
// "errors":[]}
public class URLBean {
    private protocol protocol_url;

    public protocol getProtocol_url() {
        return protocol_url;
    }

    public void setProtocol_url(protocol protocol_url) {
        this.protocol_url = protocol_url;
    }

    public static class protocol{
        private String register;
        private String privacy;
        private String device;

        public String getRegister() {
            return register;
        }

        public void setRegister(String register) {
            this.register = register;
        }

        public String getPrivacy() {
            return privacy;
        }

        public void setPrivacy(String privacy) {
            this.privacy = privacy;
        }

        public String getDevice() {
            return device;
        }

        public void setDevice(String device) {
            this.device = device;
        }

        @Override
        public String toString() {
            return "protocol{" +
                    "register='" + register + '\'' +
                    ", privacy='" + privacy + '\'' +
                    ", device='" + device + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "URLBean{" +
                "protocol_url=" + protocol_url +
                '}';
    }
}
