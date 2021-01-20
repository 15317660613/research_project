package com.adc.da.smallprogram.utils;

/**
 * @author sunjianguang
 * @description:TODO
 * @date :2019/3/8  9:23
 */
public class WeiXinUtils {

    //小程序appid
    private static final String appId = "wx2d22dbe581c9914c";
    //小程序appid对应秘钥
    private static final String secret = "7900b0ecf5abc56bd726eeb6fa7106e5";
    //消息推送模板
    private static final String template_id = "trvjDfGYTTklbKUGAr_ovMNDS5zB8tJfoAyZls8IOdw";

    public static String getAppId() {
        return appId;
    }

    public static String getSecret() {
        return secret;
    }

    public static String getTemplate_id() {
        return template_id;
    }
}
