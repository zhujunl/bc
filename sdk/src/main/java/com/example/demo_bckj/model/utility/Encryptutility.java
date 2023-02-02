package com.example.demo_bckj.model.utility;

import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * @author WangKun
 * @description: 加密工具类
 * @date :2022/9/23 9:53
 */
public class Encryptutility {

    //MD5
    public static String strMD5(String content) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(content.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("NoSuchAlgorithmException",e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UnsupportedEncodingException", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10){
                hex.append("0");
            }
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }
    /**
     * 生成max到min范围的浮点数
     **/
    public static double nextDouble(final double min, final double max) {
        return min + ((max - min) * new Random().nextDouble());
    }

    /**
     * 获取一条随机字符串
     **/
    public static String getRandomString(int length) { //length表示生成字符串的长度
        String base = "0123456789abcdef";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    public static String Base64Encode(String  encode){
        return TextUtils.isEmpty(encode)||encode==null?null:new String(Base64.encode(encode.getBytes(StandardCharsets.UTF_8)));
    }

    public static String Base64Decode(String decode){
        return  TextUtils.isEmpty(decode)||decode==null?null:new String(Base64.decode(decode));
    }
}
