package com.bc.sdk.model.request;

/**
 * @author ZJL
 * @date 2023/2/9 14:34
 * @des
 * @updateAuthor
 * @updateDes
 */
public class PhoneLoginRequest {
    private String tel;
    private String code;

    public PhoneLoginRequest(String tel, String code) {
        this.tel = tel;
        this.code = code;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "PhoneLoginRequest{" +
                "tel='" + tel + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
