package com.example.demo_bckj.model.bean;

/**
 * @author ZJL
 * @date 2022/12/15 15:20
 * @des 客服聊天内容
 * @updateAuthor
 * @updateDes
 */
public class ChatBean {
    private boolean isMine;//消息来源
    private String txt;//消息内容

    public ChatBean(boolean isMine, String txt) {
        this.isMine = isMine;
        this.txt = txt;
    }

    public boolean isMine() {
        return isMine;
    }

    public void setMine(boolean mine) {
        isMine = mine;
    }

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    @Override
    public String toString() {
        return "ChatBean{" +
                "isMine=" + isMine +
                ", txt='" + txt + '\'' +
                '}';
    }
}
