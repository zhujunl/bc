package com.bc.sdk.db.entity;

/**
 * @author ZJL
 * @date 2023/1/31 9:57
 * @des
 * @updateAuthor
 * @updateDes
 */
public class AccountLoginEntity {
    private int id;
    private String account;
    private String password;
    private long time;

    public AccountLoginEntity(Builder builder){
        setId(builder.id);
        setAccount(builder.account);
        setPassword(builder.password);
        setTime(builder.time);
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public static class Builder{
        private int id;
        private String account;
        private String password;
        private long time;

        public Builder() {
        }

        public Builder id(int var){
            this.id=var;
            return this;
        }

        public Builder account(String var){
            this.account=var;
            return this;
        }

        public Builder password(String var){
            this.password=var;
            return this;
        }

        public Builder time(long var){
            this.time=var;
            return this;
        }

        public AccountLoginEntity build(){
            return new AccountLoginEntity(this);
        }
    }

    @Override
    public String toString() {
        return "AccountLoginEntity{" +
                "id=" + id +
                ", account='" + account + '\'' +
                ", password='" + password + '\'' +
                ", time=" + time +
                '}';
    }
}
