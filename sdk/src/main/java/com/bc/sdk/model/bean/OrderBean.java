package com.bc.sdk.model.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author ZJL
 * @date 2022/12/27 10:15
 * @des 创建订单类
 * @updateAuthor
 * @updateDes
 */
public class OrderBean {
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
        @SerializedName("number")
        private String number;
        @SerializedName("money")
        private String money;

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "number='" + number + '\'' +
                    ", money='" + money + '\'' +
                    '}';
        }
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
