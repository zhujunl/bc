package com.bc.sdk.model.request;

/**
 * @author ZJL
 * @date 2023/2/9 14:41
 * @des
 * @updateAuthor
 * @updateDes
 */
public class ModifyPwdRequest {
    private String password_old;
    private String password;
    private String password_confirmation;

    public ModifyPwdRequest(String password_old, String password, String password_confirmation) {
        this.password_old = password_old;
        this.password = password;
        this.password_confirmation = password_confirmation;
    }

    public String getPassword_old() {
        return password_old;
    }

    public void setPassword_old(String password_old) {
        this.password_old = password_old;
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
        return "ModifyPwdRequest{" +
                "password_old='" + password_old + '\'' +
                ", password='" + password + '\'' +
                ", password_confirmation='" + password_confirmation + '\'' +
                '}';
    }
}
