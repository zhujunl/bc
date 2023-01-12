package com.example.demo_bckj.db.entity;

/**
 * @author ZJL
 * @date 2023/1/12 9:49
 * @des
 * @updateAuthor
 * @updateDes
 */
public class ConfigEntity {
    private int id;
    private String Authorization;

    public ConfigEntity( String authorization) {
        Authorization = authorization;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthorization() {
        return Authorization;
    }

    public void setAuthorization(String authorization) {
        Authorization = authorization;
    }

    @Override
    public String toString() {
        return "ConfigEntity{" +
                "id=" + id +
                ", Authorization='" + Authorization + '\'' +
                '}';
    }
}
