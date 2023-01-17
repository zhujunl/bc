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
     *
     * @param carNumber 身份证号
     * @return boolean
     */
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

    /**
     * 分转元
     */
    public static String changeF2Y(String amount) {
        return new BigDecimal(amount).divide(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP).toString();

    }


    public static String httpCode(int code) {
        String style = "请求成功";
        switch (code) {
            case 200:
                style = "请求成功";
                break;
            case 201:
                style = "201 Created 请求已经被实现，⽽且有⼀个新的资源已经依据请求的需要⽽建⽴ 通常是在 POST 请求，或是某些 PUT 请求之后创建了内容, 进行的返回的响应";
                break;
            case 202:
                style = "202 Accepted 请求服务器已接受，但是尚未处理，不保证完成请求 适合异步任务或者说需要处理时间比较长的请求，避免 HTTP 连接一直占用";
                break;
            case 204:
                style = "204 No content 表示请求成功，但响应报⽂不含实体的主体部分";
                break;
            case 206:
                style = "206 Partial Content 进⾏的是范围请求, 表示服务器已经成功处理了部分 GET 请求 响应头中会包含获取的内容范围";
                break;
            case 301:
                style = "301 Moved Permanently 永久性重定向";
                break;
            case 302:
                style = "302 Found 临时性重定向";
                break;
            case 303:
                style = "303 See Other";
                break;
            case 304:
                style = "304 Not Modified";
                break;
            case 307:
                style = "307 Temporary Redirect ";
                break;
            case 400:
                style = "400 Bad Request 请求报⽂存在语法错误（传参格式不正确）";
                break;
            case 401:
                style = "401 UnAuthorized 权限认证未通过(没有权限)";
                break;
            case 403:
                style = "403 Forbidden 表示对请求资源的访问被服务器拒绝";
                break;
            case 404:
                style = "404 Not Found 表示在服务器上没有找到请求的资源";
                break;
            case 408:
                style = "408 Request Timeout 客户端请求超时";
                break;
            case 409:
                style = "409 Confict 请求的资源可能引起冲突";
                break;
            case 500:
                style = "500 Internal Sever Error 表示服务器端在执⾏请求时发⽣了错误";
                break;
            case 501:
                style = "501 Not Implemented 请求超出服务器能⼒范围，例如服务器不⽀持当前请求所需要的某个功能， 或者请求是服务器不⽀持的某个⽅法";
                break;
            case 503:
                style = "503 Service Unavailable 表明服务器暂时处于超负载或正在停机维护，⽆法处理请求";
                break;
            case 505:
                style = "505 Http Version Not Supported 服务器不⽀持，或者拒绝⽀持在请求中使⽤的 HTTP 版本";
                break;
            default:
                style = "无响应内容";
                break;
        }
        return style;
    }
}
