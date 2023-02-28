package com.bc.sdk.model.request;

/**
 * @author ZJL
 * @date 2023/2/9 14:42
 * @des
 * @updateAuthor
 * @updateDes
 */
public class BindPhoneRequest {
    private String tel;
    private String code;

    public BindPhoneRequest(String tel, String code) {
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
        return "BindPhoneRequest{" +
                "tel='" + tel + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
