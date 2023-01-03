package com.example.demo_bckj.model.utility;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author ZJL
 * @date 2022/12/20 16:21
 * @des
 * @updateAuthor
 * @updateDes
 */
public class StrUtil {
    /**
     * 判断字符串中是否为非中文字符
     *
     * @param str 待校验字符串
     * @return 是否为非中文字符
     */
    public static boolean isNotContainChinese(String str) {
        return !isContainChinese(str);
    }

    /**
     * 判断字符串中是否包含中文
     *
     * @param str 待校验字符串
     * @return 是否为中文
     * @warn 不能校验是否为中文标点符号
     */
    public static boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }

    /**
     * 判断一个字符是否是汉字
     * 中文汉字的编码范围：[\u4e00-\u9fa5]
     *
     * @param c 需要判断的字符
     * @return 是汉字(true), 不是汉字(false)
     */
    public static boolean isChineseChar(char c) {
        return String.valueOf(c).matches("[\u4e00-\u9fa5]");
    }

    /**
     * 过滤掉中文
     *
     * @param str 待过滤中文的字符串
     * @return 过滤掉中文后字符串
     */
    public static String filterChinese(String str) {
        StringBuffer nonChineseCharacters = new StringBuffer();
        if (isContainChinese(str)) {
            char[] charArray = str.toCharArray();
            for (char tempSaveChar : charArray) {
                boolean checkIsChinese = isChineseChar(tempSaveChar);
                if (!checkIsChinese) {
                    nonChineseCharacters.append(tempSaveChar);
                }
            }
            return nonChineseCharacters.toString();
        }
        return str;
    }

    /**
     * 提取中文
     *
     * @param str 待提取中文的字符串
     * @return 提取中文后字符串
     */
    public static String extractChinese(String str) {
        char[] chars = str.toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        for (char aChar : chars) {
            int length = String.valueOf(aChar).getBytes().length;
            if (length == 3) {
                stringBuilder.append(aChar);
            }
        }
        return stringBuilder.toString();
    }

    /**
     * 验证身份证真假
     * @param  carNumber 身份证号
     * @return boolean*/
    public static boolean isCard(String carNumber) {
        //判断输入身份证号长度是否合法
        if (carNumber.length() != 18) {
            return false;
        }
        //校验身份证真假
        int sum = 0;
        int[] weight = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};//将加权因子定义为数组
        //遍历weight数组 求身份证前17项系数和
        for (int i = 0; i < weight.length; i++) {
            int n = carNumber.charAt(i) - 48;//获取 身份证对应数
            int w = weight[i];
            sum += w * n;
        }
        //对11求余
        int index = sum % 11;
        //校验码
        String m = "10X98765432";
        //获取身份证最后一位进行比对
        return m.charAt(index) == carNumber.charAt(17);
    }

    /**分转元*/
    public static String changeF2Y(String amount){
        return new BigDecimal(amount).divide(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP).toString();

    }

}
