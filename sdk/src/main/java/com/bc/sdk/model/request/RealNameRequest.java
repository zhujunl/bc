package com.bc.sdk.model.request;

/**
 * @author ZJL
 * @date 2023/2/9 14:40
 * @des
 * @updateAuthor
 * @updateDes
 */
public class RealNameRequest {
    private String id_code;
    private String realname;

    public RealNameRequest(String id_code, String realname) {
        this.id_code = id_code;
        this.realname = realname;
    }

    public String getId_code() {
        return id_code;
    }

    public void setId_code(String id_code) {
        this.id_code = id_code;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    @Override
    public String toString() {
        return "RealNameRequest{" +
                "id_code='" + id_code + '\'' +
                ", realname='" + realname + '\'' +
                '}';
    }
}
