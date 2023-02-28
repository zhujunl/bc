package com.bc.sdk.model.bean;

import java.util.List;

/**
 * @author ZJL
 * @date 2022/12/14 11:13
 * @des
 * @updateAuthor
 * @updateDes
 */
public class Bean<T> {
    private int code;
    private String message;
    private T data;
    private List<?> errors;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
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
        return "Bean{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                ", errors=" + errors +
                '}';
    }
}
