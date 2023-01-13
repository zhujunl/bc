package com.example.demo_bckj.model.bean;

/**
 * @author ZJL
 * @date 2023/1/12 13:54
 * @des
 * @updateAuthor
 * @updateDes
 */
public class User {
    private String account;//账号
    private String slug;//账号唯一标识
    private boolean isAuthenticated;//是否实名
    private Integer age;//年龄
    private String token;//token

    public User(Builder builder) {
        setAccount(builder.account);
        setSlug(builder.slug);
        setAuthenticated(builder.isAuthenticated);
        setAge(builder.age);
        setToken(builder.token);
    }

    private void setAccount(String account) {
        this.account = account;
    }

    private void setSlug(String slug) {
        this.slug = slug;
    }

    private void setAuthenticated(boolean authenticated) {
        isAuthenticated = authenticated;
    }

    private void setAge(Integer age) {
        this.age = age;
    }

    private void setToken(String token) {
        this.token = token;
    }

    public String getAccount() {
        return account;
    }

    public String getSlug() {
        return slug;
    }

    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    public int getAge() {
        return age;
    }

    public String getToken(boolean isBearer) {
        return isBearer?"Bearer "+token:token;
    }

    public static class Builder{
        private String account;
        private String slug;
        private boolean isAuthenticated;
        private Integer age;
        private String token;

        public Builder() {
        }

        public Builder account(String var){
            account=var;
            return this;
        }
        public Builder slug(String var){
            slug=var;
            return this;
        }
        public Builder isAuthenticated(boolean var){
            isAuthenticated=var;
            return this;
        }
        public Builder age(Integer var){
            age=var;
            return this;
        }
        public Builder token(String var){
            token=var;
            return this;
        }

        public User build(){
            return new User(this);
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "account='" + account + '\'' +
                ", slug='" + slug + '\'' +
                ", isAuthenticated=" + isAuthenticated +
                ", age=" + age +
                ", token='" + token + '\'' +
                '}';
    }
}
