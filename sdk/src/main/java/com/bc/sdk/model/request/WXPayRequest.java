package com.bc.sdk.model.request;

/**
 * @author ZJL
 * @date 2023/3/22 10:11
 * @des
 * @updateAuthor
 * @updateDes
 */
public class WXPayRequest {
    private String number;
    private String type;
    private String exception;

    public WXPayRequest(String number, String type) {
        this.number = number;
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public String getType() {
        return type;
    }

    public String getException() {
        return exception;
    }

    @Override
    public String toString() {
        return "WXPayRequest{" +
                "number='" + number + '\'' +
                ", type='" + type + '\'' +
                ", exception='" + exception + '\'' +
                '}';
    }
}
