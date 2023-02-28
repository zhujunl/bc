package com.bc.sdk.model;

/**
 * @author ZJL
 * @date 2022/12/27 10:29
 * @des
 * @updateAuthor
 * @updateDes
 */
public class PayBus {
    private String url;

    public PayBus(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
