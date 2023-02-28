package com.bc.sdk.model.request;

/**
 * @author ZJL
 * @date 2023/2/9 14:41
 * @des
 * @updateAuthor
 * @updateDes
 */
public class ResetPwdRequest {
    private String tel;
    private String code;
    private String password;
    private String password_confirmation;

    public ResetPwdRequest(String tel, String code, String password, String password_confirmation) {
        this.tel = tel;
        this.code = code;
        this.password = password;
        this.password_confirmation = password_confirmation;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword_confirmation() {
        return password_confirmation;
    }

    public void setPassword_confirmation(String password_confirmation) {
        this.password_confirmation = password_confirmation;
    }

    @Override
    public String toString() {
        return "ResetPwdRequest{" +
                "tel='" + tel + '\'' +
                ", code='" + code + '\'' +
                ", password='" + password + '\'' +
                ", password_confirmation='" + password_confirmation + '\'' +
                '}';
    }
}
