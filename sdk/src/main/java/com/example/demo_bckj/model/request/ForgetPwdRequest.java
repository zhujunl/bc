package com.example.demo_bckj.model.request;

/**
 * @author ZJL
 * @date 2023/2/9 14:41
 * @des
 * @updateAuthor
 * @updateDes
 */
public class ForgetPwdRequest {
    private String tel;

    public ForgetPwdRequest(String tel) {
        this.tel = tel;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}
