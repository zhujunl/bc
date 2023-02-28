package com.bc.sdk.model.request;

/**
 * @author ZJL
 * @date 2023/2/9 15:04
 * @des
 * @updateAuthor
 * @updateDes
 */
public class AliH5PayRequest {
    private String number;
    private String type;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "AliH5PayRequest{" +
                "number='" + number + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
