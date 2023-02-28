package com.bc.sdk.model.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author WangKun
 * @description:
 * @date :2022/10/25 18:06
 */
public class DateUpBean {


    @SerializedName("code")
    private Integer code;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private List<?> data;
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

    public List<?> getData() {
        return data;
    }

    public void setData(List<?> data) {
        this.data = data;
    }

    public List<?> getErrors() {
        return errors;
    }

    public void setErrors(List<?> errors) {
        this.errors = errors;
    }
}
