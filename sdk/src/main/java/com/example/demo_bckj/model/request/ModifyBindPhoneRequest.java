package com.example.demo_bckj.model.request;

/**
 * @author ZJL
 * @date 2023/2/9 14:43
 * @des
 * @updateAuthor
 * @updateDes
 */
public class ModifyBindPhoneRequest {
    private String code_old;
    private String code_new;
    private String tel;

    public ModifyBindPhoneRequest(String code_old, String code_new, String tel) {
        this.code_old = code_old;
        this.code_new = code_new;
        this.tel = tel;
    }

    public String getCode_old() {
        return code_old;
    }

    public void setCode_old(String code_old) {
        this.code_old = code_old;
    }

    public String getCode_new() {
        return code_new;
    }

    public void setCode_new(String code_new) {
        this.code_new = code_new;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    @Override
    public String toString() {
        return "ModifyBindPhoneRequest{" +
                "code_old='" + code_old + '\'' +
                ", code_new='" + code_new + '\'' +
                ", tel='" + tel + '\'' +
                '}';
    }
}
