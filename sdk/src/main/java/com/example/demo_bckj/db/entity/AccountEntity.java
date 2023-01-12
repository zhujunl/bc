package com.example.demo_bckj.db.entity;

/**
 * @author ZJL
 * @date 2023/1/11 13:41
 * @des
 * @updateAuthor
 * @updateDes
 */


public class AccountEntity {

    public int id;
    private String account;
    private String tel;
    private String slug;
    private String nickName;
    private Boolean isAuthenticated;
    private String realName;
    private String birthday;
    private int age;
    private String password;

    public AccountEntity(int id, String account, String tel, String slug, String nickName,
                         Boolean isAuthenticated, String realName, String birthday, int age,
                         String password) {
        this.id = id;
        this.account = account;
        this.tel = tel;
        this.slug = slug;
        this.nickName = nickName;
        this.isAuthenticated = isAuthenticated;
        this.realName = realName;
        this.birthday = birthday;
        this.age = age;
        this.password = password;
    }

    public AccountEntity(Builder builder) {
        setAccount(builder.account);
        setTel(builder.tel);
        setSlug(builder.slug);
        setNickName(builder.nickName);
        setAuthenticated(builder.isAuthenticated);
        setRealName(builder.realName);
        setBirthday(builder.birthday);
        setAge(builder.age);
        setPassword(builder.password);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public boolean getAuthenticated() {
        return isAuthenticated;
    }

    public void setAuthenticated(Boolean authenticated) {
        isAuthenticated = authenticated;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public static final class Builder{
        private String account;
        private String tel;
        private String slug;
        private String nickName;
        private boolean isAuthenticated;
        private String realName;
        private String birthday;
        private int age;
        private String password;


        public Builder() {
        }

        public Builder account(String var){
            account=var;
            return this;
        }

        public Builder tel(String var){
            tel=var;
            return this;
        }

        public Builder slug(String var){
            slug=var;
            return this;
        }

        public Builder nickName(String var){
            nickName=var;
            return this;
        }

        public Builder isAuthenticated(boolean var){
            isAuthenticated=var;
            return this;
        }

        public Builder realName(String var){
            realName=var;
            return this;
        }

        public Builder birthday(String var){
            birthday=var;
            return this;
        }

        public Builder age(int var){
            age=var;
            return this;
        }

        public Builder password(String var){
            password=var;
            return this;
        }

        public AccountEntity build(){
            return new AccountEntity(this);
        }
    }

    @Override
    public String toString() {
        return "AccountPwBeanEntity{" +
                "id=" + id +
                ", account='" + account + '\'' +
                ", tel='" + tel + '\'' +
                ", slug='" + slug + '\'' +
                ", nickName='" + nickName + '\'' +
                ", isAuthenticated=" + isAuthenticated +
                ", realName='" + realName + '\'' +
                ", birthday='" + birthday + '\'' +
                ", age=" + age +
                ", password='" + password + '\'' +
                '}';
    }
}
