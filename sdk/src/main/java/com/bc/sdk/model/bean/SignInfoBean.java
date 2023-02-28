package com.bc.sdk.model.bean;

/**
 * @author ZJL
 * @date 2022/12/14 17:35
 * @des sign与info实体类
 * @updateAuthor
 * @updateDes
 */
public class SignInfoBean {
  public String sign;
  public String info;

  @Override
  public String toString() {
    return "SignInfoBean{" +
            "sign='" + sign + '\'' +
            ", info='" + info + '\'' +
            '}';
  }
}
