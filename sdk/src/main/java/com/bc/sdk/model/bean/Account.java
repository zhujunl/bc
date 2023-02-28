package com.bc.sdk.model.bean;

import com.google.gson.annotations.SerializedName;

/**
 * @author ZJL
 * @date 2023/1/3 11:38
 * @des 用户信息
 * @updateAuthor
 * @updateDes
 */
public class Account {
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

    public Boolean getAuthenticated() {
        return isAuthenticated;
    }

    public void setAuthenticated(Boolean authenticated) {
        isAuthenticated = authenticated;
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
        return "Account{" +
                "account='" + account + '\'' +
                ", tel='" + tel + '\'' +
                ", slug='" + slug + '\'' +
                ", nickName='" + nickName + '\'' +
                ", isAuthenticated=" + isAuthenticated +
                ", realname='" + realname + '\'' +
                ", birthday='" + birthday + '\'' +
                ", age=" + age +
                '}';
    }
}
