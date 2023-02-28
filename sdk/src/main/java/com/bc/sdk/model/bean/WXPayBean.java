package com.bc.sdk.model.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author ZJL
 * @date 2022/12/27 10:19
 * @des 微信支付类
 * @updateAuthor
 * @updateDes
 */
public class WXPayBean {
    @SerializedName("code")
    private Integer code;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private DataBean data;
    @SerializedName("errors")
    private List<?> errors;

    public static class DataBean {
        @SerializedName("code")
        private String code;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "code='" + code + '\'' +
                    '}';
        }
    }

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

    @Override
    public String toString() {
        return "PayBean{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                ", errors=" + errors +
                '}';
    }
}
