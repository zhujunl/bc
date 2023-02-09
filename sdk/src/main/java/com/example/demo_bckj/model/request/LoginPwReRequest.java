package com.example.demo_bckj.model.request;

/**
 * @author ZJL
 * @date 2023/2/9 14:34
 * @des
 * @updateAuthor
 * @updateDes
 */
public class LoginPwReRequest {
    private String account;
    private String password;
    private String password_confirmation;

    public LoginPwReRequest(String username, String password, String password_confirmation) {
        this.account = username;
        this.password = password;
        this.password_confirmation = password_confirmation;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
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
        return "LoginPwReRequest{" +
                "account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", password_confirmation='" + password_confirmation + '\'' +
                '}';
    }
}
