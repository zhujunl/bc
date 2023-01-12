package com.example.demo_bckj.model.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author WangKun
 * @description: 响应登录请求类
 * @date :2022/10/26 11:49
 */
public class AccountPwBean {

    @SerializedName("code")
    private Integer code;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private Account data;
    @SerializedName("errors")
    private List<?> errors;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Account getData() {
        return data;
    }

    public void setData(Account data) {
        this.data = data;
    }

    public List<?> getErrors() {
        return errors;
    }

    public void setErrors(List<?> errors) {
        this.errors = errors;
    }

    public static class DataBean {
        @SerializedName("account")
        private String account;
        @SerializedName("tel")
        private String tel;
        @SerializedName("slug")
        private String slug;
        @SerializedName("nick_name")
        private String nickName;
        @SerializedName("is_authenticated")
        private Boolean isAuthenticated;
        @SerializedName("realname")
        private String realname;
        @SerializedName("birthday")
        private String birthday;
        @SerializedName("age")
        private Integer age;

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public String getSlug() {
            return slug;
        }

        public void setSlug(String slug) {
            this.slug = slug;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public Boolean getIsAuthenticated() {
            return isAuthenticated;
        }

        public void setIsAuthenticated(Boolean isAuthenticated) {
            this.isAuthenticated = isAuthenticated;
        }

        public String getRealname() {
            return realname;
        }

        public void setRealname(String realname) {
            this.realname = realname;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "account='" + account + '\'' +
                    ", tel=" + tel +
                    ", slug='" + slug + '\'' +
                    ", nickName='" + nickName + '\'' +
                    ", isAuthenticated=" + isAuthenticated +
                    ", realname=" + realname +
                    ", birthday=" + birthday +
                    ", age=" + age +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "AccountPwBean{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                ", errors=" + errors +
                '}';
    }
}
