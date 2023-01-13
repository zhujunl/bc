package com.example.demo_bckj.model.utility;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;

import com.example.demo_bckj.model.bean.SignInfoBean;

import org.json.JSONObject;

import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;

import androidx.core.app.ActivityCompat;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * @author WangKun
 * @description:
 * @date :2022/10/24 17:31
 */
public class DeviceIdUtil {

    /**
     * 获取当前手机系统版本号
     *
     * @return 系统版本号
     */

    public static String getSystemVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 获得设备的AndroidId
     *
     * @param context 上下文
     * @return 设备的AndroidId
     */
    public static String getAndroidId(Context context) {
        try {
            return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    /**
     * 获得设备的DeviceId
     *
     * @return 设备的DeviceId
     */
    public static String getDeviceId() {
        String m_szDevIDShort = "35" + (Build.BOARD.length() % 10) + (Build.BRAND.length() % 10) + (Build.CPU_ABI.length() % 10) + (Build.DEVICE.length() % 10) + (Build.MANUFACTURER.length() % 10) + (Build.MODEL.length() % 10) + (Build.PRODUCT.length() % 10);
        String serial = null;
        try {
            serial = Build.class.getField("SERIAL").get(null).toString();
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            serial = "serial";
        }
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();

        //        TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        //
        //        String DeviceId = tm.getDeviceId();
        //
        //        Log.d("tag", "Cocos2dxActivity TelephonyManager DeviceId：" + DeviceId);
    }

    /**
     * @param context
     * @return 当前应用的版本信息
     */
    public static String getVersionName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param context
     * @return 当前应用的包名
     */
    public static String getTopPackage(Context context) {
        try {
            ActivityManager am = (ActivityManager) context.getSystemService(Activity.ACTIVITY_SERVICE);
            ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
            return cn.getPackageName();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    //    String androidId = Settings.Secure.getString(context.getContentResolver(),
    //            Settings.Secure.ANDROID_ID);
    //
    //    UUID androidId_UUID = UUID
    //            .nameUUIDFromBytes(androidId.getBytes("utf8"));
    //
    //    String unique_id = androidId_UUID.toString();

    /**
     * IMEI 1号
     *
     * @param context
     * @return
     */
    public static String getIMEI_1(Context context) {
        UUID.randomUUID().toString();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return "";
        }
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        Class clazz = tm.getClass();
        try {
            Method getImei = clazz.getDeclaredMethod("getImei", int.class);
            Object invoke = getImei.invoke(tm, 0);
            return invoke == null ? "" : invoke.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * IMEI 2号
     *
     * @param context
     * @return
     */
    public static String getIMEI_2(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        Class clazz = tm.getClass();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return "";
        }
        try {
            Method getImei = clazz.getDeclaredMethod("getImei", int.class);
            Object invoke = getImei.invoke(tm, 1);
            return invoke == null ? "" : invoke.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return " ";
        }
    }

    public static String getIMSI(Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return "";
        }
        try {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
            if (tm == null)
                return "";
            return tm.getSubscriberId();
        } catch (Exception e) {
            e.printStackTrace();
            return " ";
        }
    }


    /**
     * 获得设备序列号（如：WTK7N16923005607）, 个别设备无法获取
     *
     * @return 设备序列号
     */
    public static String getSERIAL() {
        try {
            return Build.SERIAL;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    /**
     * 获取手机序列号
     *
     * @return 手机序列号
     */
    @SuppressLint({"NewApi", "MissingPermission"})
    public static String getSerialNumber() {
        String serial = "";
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {//9.0+
                serial = Build.getSerial();
            } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {//8.0+
                serial = Build.SERIAL;
            } else {//8.0-
                Class<?> c = Class.forName("android.os.SystemProperties");
                Method get = c.getMethod("get", String.class);
                serial = (String) get.invoke(c, "ro.serialno");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("tag", "读取设备序列号异常：" + e.toString());
        }
        return serial;
    }

    /**
     * 获取sim卡序列号
     */
    public static String getSimSerial(Context context) {
        //        //返回SIM卡的序列号
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return "";
        }
        TelephonyManager tm = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);  //拿到电话管理器

        return tm.getSimSerialNumber();
    }

    /**
     * 将ip的整数形式转换成ip形式
     */
    public static String intToIp(int i) {
        return (i & 0xFF) + "." +
                ((i >> 8) & 0xFF) + "." +
                ((i >> 16) & 0xFF) + "." +
                (i >> 24 & 0xFF);
    }

    /**
     * Get Ip address 获取IP地址
     *
     * @throws Exception
     */
    public static String getNetworkIp() {
        try {
            List<NetworkInterface> infos = Collections.list(NetworkInterface.getNetworkInterfaces());

            for (NetworkInterface info : infos) {
                if (!info.getName().equals("wlan0")) {
                    continue;
                }

                String ipAddr = "0.0.0.0";
                Enumeration<InetAddress> addrs = info.getInetAddresses();
                while (addrs.hasMoreElements()) {
                    InetAddress addr = addrs.nextElement();
                    // 过滤ipv6
                    if (addr.toString().length() <= 16) {
                        ipAddr = addr.toString().subSequence(1, addr.toString().length()).toString();
                    }
                }
                return ipAddr;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "0.0.0.0";
    }

    /**
     * Get Ip address 获取Mac地址
     *
     * @throws Exception
     */
    public static String getNetworkMac() {
        try {
            List<NetworkInterface> infos = Collections.list(NetworkInterface.getNetworkInterfaces());

            for (NetworkInterface info : infos) {
                if (!info.getName().equals("wlan0")) {
                    continue;
                }

                byte[] macBytes = info.getHardwareAddress();
                List<String> macByteList = new ArrayList<>();
                for (Byte byt : macBytes) {
                    macByteList.add(String.format("%02X", byt));
                }
                String macAddr = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    macAddr = String.join(":", macByteList);
                }
                return macAddr;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "00:00:00:00:00:00";
    }

    /**
     * 获取手机当前网络类型
     *
     * @return 手机当前网络类型
     */
    public static String getNetworkType(Context context) {
        String strNetworkType = "UnKnown";
        final NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (activeNetworkInfo != null && activeNetworkInfo.getType() == 1) {
            strNetworkType = "WIFI";
        } else if (activeNetworkInfo != null && activeNetworkInfo.getType() == 0) {
            String subtypeName = activeNetworkInfo.getSubtypeName();
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                switch (((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getNetworkType()) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
                        strNetworkType = "2G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
                    case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
                    case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
                        strNetworkType = "3G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_LTE:
                        strNetworkType = "4G";
                        break;
                    default:
                        if (subtypeName.equalsIgnoreCase("TD-SCDMA") || subtypeName.equalsIgnoreCase("WCDMA") || subtypeName.equalsIgnoreCase("CDMA2000")) {
                            strNetworkType = "3G";
                            break;
                        }
                        strNetworkType = subtypeName;
                        break;
                }
                return strNetworkType;
            }

        }
        return strNetworkType;
    }

    //验证手机号是否合法
    public static boolean isMobileNO(String mobile) {
        if (mobile.length() != 11) {
            return false;
        } else {
            return true;
        }
    }

    public static String getSimOperator(String operator) {
        switch (operator) {
            case "46005":
                return "中国电信";
            case "46006":
                return "中国联通";
            case "46007":
                return "中国移动";
            default:
                return "";
        }
    }


    //获取sign和info
    public static SignInfoBean getSign(Context context) throws Exception {
        String nonce = Encryptutility.getRandomString(32);
        Log.d("tag", "当前随机数：" + nonce);
        char[] charArray = nonce.toCharArray();

        long currentTimeMillis = System.currentTimeMillis();
        Log.d("tag", "当前时间戳：" + currentTimeMillis);

        double rand = Encryptutility.nextDouble(0, 1);
        Log.d("tag", "当前随机浮点数：" + rand);

        String requestId = currentTimeMillis + "_" + rand;
        Log.d("tag", "当前唯一标识符为:" + requestId);
        char[] chars = String.valueOf(rand).toCharArray();

        char aChar = chars[2];
        String valueOf = String.valueOf(aChar);
        int achar = Integer.parseInt(valueOf);
        Log.d("tag", "当前aChar为:" + aChar);
        Log.d("tag", "当前aChar1为:" + achar);
        int length = String.valueOf(rand).length();
        String randNonce = "";
        for (int index = 2; index < length; index++) {
            int e = (Integer.parseInt(String.valueOf(chars[index])) * 9 + index) % nonce.length();
            //            Log.d("tag", "当前index为："+ index);
            //            Log.d("tag", "当前e为："+ e);

            randNonce += charArray[e];
            //            Log.d("tag", "当前randNonce为："+ randNonce);
        }
        Log.d("tag", "当前randNonce为：" + randNonce);
        Log.d("tag", "当前拼接值为：" + randNonce + requestId);

        String strMD5 = Encryptutility.strMD5(randNonce + requestId);

        Log.d("tag", "当前MD5：" + strMD5);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("nonce", nonce);
        jsonObject.put("request_id", requestId);
        jsonObject.put("sign", strMD5);
        String string = jsonObject.toString();
        Log.d("tag", "json串为：" + string);

        String sign = Base64.encodeToString(string.getBytes(StandardCharsets.UTF_8), Base64.NO_WRAP);
        Log.d("tag", "Base64编码sign为：" + sign);
        String json1 = StrUtil.getJson(context, "bc_sdk_config.json");
        JSONObject jsonConfig = json1!=null?new JSONObject(json1):new JSONObject();;//json数据
        String type = StrUtil.getValue(jsonConfig, context,"type");
        String game = StrUtil.getValue(jsonConfig, context,"game");
        String channel = StrUtil.getValue(jsonConfig, context,"channel");
        String plan = StrUtil.getValue(jsonConfig, context,"plan");
        String pack = StrUtil.getValue(jsonConfig, context,"package");
        //        String j="{\n" +
        //                "    \"type\": \"ML\",\n" +
        //                "    \"game\": \"f60e429f71e147eb817f233f9fca4cce\",\n" +
        //                "    \"channel\": \"\",\n" +
        //                "    \"package\": \"\",\n" +
        //                "    \"plan\": \"\",\n" +
        //                "    \"material\": \"\",\n" +
        //                "    \"device\": {\n" +
        //                "        \"os\": \"Android\",\n" +
        //                "        \"android\": {\n" +
        //                "            \"system_version\": \"10\",\n" +
        //                "            \"android_id\": \"3559324b96572744\",\n" +
        //                "            \"android_q\": {\n" +
        //                "                \"aaid\": \"748d89ad-dd0d-424a-83df-d934519f0489\",\n" +
        //                "                \"oaid\": \"92e60edb3de3f9d0\",\n" +
        //                "                \"vaid\": \"416bffbb34374591\"\n" +
        //                "            },\n" +
        //                "            \"id\": \"QKQ1.200419.002\",\n" +
        //                "            \"imei\": [\"\", \"\"],\n" +
        //                "            \"imsi\": \"\",\n" +
        //                "            \"model\": \"M2007J1SC\",\n" +
        //                "            \"product\": \"cas\",\n" +
        //                "            \"brand\": \"Xiaomi\",\n" +
        //                "            \"game_package_name\": \"com.aaaa.bbbb\",\n" +
        //                "            \"game_version\": \"1.0.2\",\n" +
        //                "            \"sdk_package_name\": \"com.dsadads.ewqreqwrqwe\",\n" +
        //                "            \"sdk_version\": \"v2.1.6.1583728\",\n" +
        //                "            \"serial\": \"unknown\",\n" +
        //                "            \"sim_serial\": [\"\"]\n" +
        //                "        },\n" +
        //                "        \"network\": {\n" +
        //                "            \"code\": 46002,\n" +
        //                "            \"intranet_ip\": \"10.48.6.16\",\n" +
        //                "            \"mac\": \"e0:1f:88:33:01:f0\",\n" +
        //                "            \"name\": \"那就这样\",\n" +
        //                "            \"type\": \"wifi\"\n" +
        //                "        }\n" +
        //                "    }\n" +
        //                "}";

        String j = "{\n" +
                "    \"type\": \"" + type + "\",\n" +
                "    \"game\": \"" + game + "\",\n" +
                "    \"channel\": \"" + channel + "\",\n" +
                "    \"package\": \"" + pack + "\",\n" +
                "    \"plan\": \"" + plan + "\",\n" +
                "    \"material\": \"\",\n" +
                "    \"device\": {\n" +
                "        \"os\": \"Android\",\n" +
                "        \"android\": {\n" +
                "            \"system_version\": \"" + DeviceIdUtil.getSystemVersion() + "\",\n" +
                "            \"android_id\": \"" + DeviceIdUtil.getAndroidId(context) + "\",\n" +
                "            \"android_q\": {\n" +
                "                \"aaid\": \"" + "748d89ad-dd0d-424a-83df-d934519f0489" + "\",\n" +
                "                \"oaid\": \"" + "92e60edb3de3f9d0" + "\",\n" +
                "                \"vaid\": \"" + "416bffbb34374591" + "\"\n" +
                "            },\n" +
                "            \"id\": \"" + "QKQ1.200419.002" + "\",\n" +
                "            \"imei\": [" + DeviceIdUtil.getIMEI_1(context) + "," + DeviceIdUtil.getIMEI_2(context) + "],\n" +
                "            \"imsi\": \"" + DeviceIdUtil.getIMSI(context) + "\",\n" +
                "            \"model\": \"" + Build.MODEL + "\",\n" +
                "            \"product\": \"" + "cas" + "\",\n" +
                "            \"brand\": \"" + Build.BRAND + "\",\n" +
                "            \"game_package_name\": \"" + "" + "\",\n" +
                "            \"game_version\": \"" + "" + "\",\n" +
                "            \"sdk_package_name\": \"" + DeviceIdUtil.getTopPackage(context) + "\",\n" +
                "            \"sdk_version\": \"" + DeviceIdUtil.getVersionName(context) + "\",\n" +
                "            \"serial\": \"" + DeviceIdUtil.getSERIAL() + "\",\n" +
                "            \"sim_serial\": [" + DeviceIdUtil.getSimSerial(context) + "]\n" +
                "        },\n" +
                "        \"network\": {\n" +
                "            \"code\": 46002,\n" +
                "            \"intranet_ip\": \"10.48.6.16\",\n" +
                "            \"mac\": \"e0:1f:88:33:01:f0\",\n" +
                "            \"name\": \"那就这样\",\n" +
                "            \"type\": \"wifi\"\n" +
                "        }\n" +
                "    }\n" +
                "}";
        JSONObject json = new JSONObject(j);
        Log.d("tag", "设备信息的字符串为" + json);
        //加密
        String key = Encryptutility.strMD5(sign);
        Log.d("tag", "加密的字符串keyString值为" + key);
        String info = AESUtils.encrypt(json.toString(), key, key.substring(0, 16));
        Log.d("tag", "加密的字符串info为" + info);

        SignInfoBean signInfoBean = new SignInfoBean();
        signInfoBean.sign = sign;
        signInfoBean.info = info;

        return signInfoBean;
    }

}

