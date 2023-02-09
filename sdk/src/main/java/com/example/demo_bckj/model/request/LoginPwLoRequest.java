package com.example.demo_bckj.model.request;

/**
 * @author ZJL
 * @date 2023/2/9 14:34
 * @des
 * @updateAuthor
 * @updateDes
 */
public class LoginPwLoRequest {
    private String username;
    private String password;

    public LoginPwLoRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


}
