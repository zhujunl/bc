package com.example.demo_bckj.model.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author ZJL
 * @date 2022/12/19 13:17
 * @des
 * @updateAuthor
 * @updateDes
 */
public class PlayBean {
    @SerializedName("code")
    private Integer code;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private DataBean data;
    @SerializedName("errors")
    private List<?> errors;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public List<?> getErrors() {
        return errors;
    }

    public void setErrors(List<?> errors) {
        this.errors = errors;
    }

    public static class DataBean {
        @SerializedName("account")
        private String account;
        @SerializedName("password")
        private String  password;

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "account='" + account + '\'' +
                    ", password=" + password +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "PlayBean{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                ", errors=" + errors +
                '}';
    }
}
