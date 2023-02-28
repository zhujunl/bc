package com.bc.sdk.model.request;

/**
 * @author ZJL
 * @date 2023/2/9 14:41
 * @des
 * @updateAuthor
 * @updateDes
 */
public class BindPhoneCodeRequest {
    private String tel;

    public BindPhoneCodeRequest(String tel) {
        this.tel = tel;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}
