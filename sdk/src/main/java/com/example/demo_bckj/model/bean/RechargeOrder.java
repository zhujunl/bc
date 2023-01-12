package com.example.demo_bckj.model.bean;

/**
 * @author ZJL
 * @date 2023/1/3 14:43
 * @des  订单信息
 * @updateAuthor
 * @updateDes
 */
public class RechargeOrder {
    //游戏订单号
    private String number_game;
    //物品名称
    private String props_name;
    //区服 ID
    private String server_id;
    //区服名称
    private String server_name;
    //角色 ID
    private String role_id;
    //角色名称
    private String role_name;
    //回调地址
    private String callback_url;
    //订单金额
    private int money;
    //扩展字段
    private String extend_data;

    public RechargeOrder(String number_game, String props_name, String server_id, String server_name,
                         String role_id, String role_name, String callback_url, int money, String extend_data) {
        this.number_game = number_game;
        this.props_name = props_name;
        this.server_id = server_id;
        this.server_name = server_name;
        this.role_id = role_id;
        this.role_name = role_name;
        this.callback_url = callback_url;
        this.money = money;
        this.extend_data = extend_data;
    }

    public RechargeOrder() {
    }

    private RechargeOrder(Builder builder) {
        setNumber_game(builder.number_game);
        setProps_name(builder.props_name);
        setServer_id(builder.server_id);
        setServer_name(builder.server_name);
        setRole_id(builder.role_id);
        setRole_name(builder.role_name);
        setCallback_url(builder.callback_url);
        setExtend_data(builder.extend_data);
        setMoney(builder.money);
    }


    public String getNumber_game() {
        return number_game;
    }

    public void setNumber_game(String number_game) {
        this.number_game = number_game;
    }

    public String getProps_name() {
        return props_name;
    }

    public void setProps_name(String props_name) {
        this.props_name = props_name;
    }

    public String getServer_id() {
        return server_id;
    }

    public void setServer_id(String server_id) {
        this.server_id = server_id;
    }

    public String getServer_name() {
        return server_name;
    }

    public void setServer_name(String server_name) {
        this.server_name = server_name;
    }

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }

    public String getRole_name() {
        return role_name;
    }

    public void setRole_name(String role_name) {
        this.role_name = role_name;
    }

    public String getCallback_url() {
        return callback_url;
    }

    public void setCallback_url(String callback_url) {
        this.callback_url = callback_url;
    }

    public String getExtend_data() {
        return extend_data;
    }

    public void setExtend_data(String extend_data) {
        this.extend_data = extend_data;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public static final class Builder {
        private String number_game;
        private String props_name;
        private String server_id;
        private String server_name;
        private String role_id;
        private String role_name;
        private String callback_url;
        private String extend_data;
        private int money;

        public Builder() {
        }

        public Builder number_game(String number_game) {
            this.number_game=number_game;
            return this;
        }

        public Builder props_name(String props_name) {
            this.props_name=props_name;
            return this;
        }

        public Builder server_id(String server_id) {
            this.server_id=server_id;
            return this;
        }

        public Builder server_name(String server_name) {
            this.server_name=server_name;
            return this;
        }

        public Builder role_id(String role_id) {
            this.role_id=role_id;
            return this;
        }

        public Builder role_name(String role_name) {
            this.role_name=role_name;
            return this;
        }

        public Builder callback_url(String callback_url) {
            this.callback_url=callback_url;
            return this;
        }

        public Builder extend_data(String extend_data) {
            this.extend_data=extend_data;
            return this;
        }

        public Builder money(int money) {
            this.money=money;
            return this;
        }

        public RechargeOrder build() {
            return new RechargeOrder(this);
        }
    }

    @Override
    public String toString() {
        return "RechargeOrder{" +
                "number_game='" + number_game + '\'' +
                ", props_name='" + props_name + '\'' +
                ", server_id='" + server_id + '\'' +
                ", server_name='" + server_name + '\'' +
                ", role_id='" + role_id + '\'' +
                ", role_name='" + role_name + '\'' +
                ", callback_url='" + callback_url + '\'' +
                ", extend_data='" + extend_data + '\'' +
                '}';
    }
}
