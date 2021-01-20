package com.adc.da.smallprogram.service;

import com.adc.da.smallprogram.controller.WeixinController;
import com.adc.da.smallprogram.utils.WeiXinUtils;
import com.adc.da.smallprogram.vo.*;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.utils.GsonUtil;
import com.adc.da.util.utils.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ：mengqingchen
 * @date ：Created in 2019/2/21 15:26
 * @description：微信业务
 * @modified By：
 */
@Service("weiXinService")
public class WeiXinService {
    /**
     * Log4j
     */
    private static final Logger logger = LoggerFactory.getLogger(WeiXinService.class);

    public ResponseMessage getOpenId(OpenIdReqVO accessTokenReqVO){
       try{
           //组装请求信息
           Map<String,Object> map = new HashMap<String, Object>();
           map.put("appid",accessTokenReqVO.getAppId());
           map.put("secret",accessTokenReqVO.getSecret());
           map.put("js_code",accessTokenReqVO.getJs_code());
           map.put("grant_type","authorization_code");
           String url = "https://api.weixin.qq.com/sns/jscode2session";
           String resJson = HttpUtil.get(url,map);
           OpenIdResVO openIdResVO = GsonUtil.fromJson(resJson,OpenIdResVO.class);
           return Result.success(openIdResVO.getOpenid());
       }catch (Exception e){
           logger.error("获取openId失败" + e.getMessage());
           return Result.error("获取openId失败");
       }

    }
    public ResponseMessage getAccessToken(OpenIdReqVO accessTokenReqVO){
        try{
            //1.组装url
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("grant_type","client_credential");
            map.put("appid",accessTokenReqVO.getAppId());
            map.put("secret",accessTokenReqVO.getSecret());
            String result = HttpUtil.get("https://api.weixin.qq.com/cgi-bin/token",map);
            Access_TokenVO access_tokenVO = GsonUtil.fromJson(result,Access_TokenVO.class);
            return Result.success(access_tokenVO);
        }catch (Exception e){
            logger.error("获取AccessToken失败" + e.getMessage());
            return Result.error("获取AccessToken失败");
        }
    }

    public ResponseMessage sendMessage(SendMessageReqVO sendMessageReqVO){
        logger.info("推送消息开始,入参:"+GsonUtil.t2Json(sendMessageReqVO));
        String tepUrl = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/send?access_token="+ sendMessageReqVO.getAccess_token();

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(tepUrl);
        // 装配post请求参数
        JSONObject json = new JSONObject();
        json.put("touser", sendMessageReqVO.getTouser());
        json.put("template_id", sendMessageReqVO.getTemplate_id());
        json.put("form_id", sendMessageReqVO.getForm_id());
        json.put("emphasis_keyword", sendMessageReqVO.getEmphasis_keyword());
        json.put("page","pages/index/index");
        JSONObject dataJson = new JSONObject();
        json.put("data", sendMessageReqVO.getData());
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("appid", WeiXinUtils.getAppId());
        map.put("pagepath", "pages/index/index");
        json.put("miniprogram", map);
        try {
            StringEntity myEntity = new StringEntity(json.toJSONString(), ContentType.APPLICATION_JSON);
            // 设置post求情参数
            httpPost.setEntity(myEntity);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                // 发送成功
                String resutlEntity = EntityUtils.toString(httpResponse.getEntity());
                SendMessageResVO resultTemplateDate = JSONObject.parseObject(resutlEntity, SendMessageResVO.class);
                if (resultTemplateDate.getErrcode()==40037) {
                   return  Result.error("template_id不正确");
                }
                if (resultTemplateDate.getErrcode()==41028) {
                    return  Result.error("form_id不正确，或者过期");
                }
                if (resultTemplateDate.getErrcode()==41029) {
                    return  Result.error("form_id已被使用");
                }
                if (resultTemplateDate.getErrcode()==41030) {
                    return  Result.error("page不正确");
                }
                if (resultTemplateDate.getErrcode()==45009) {
                    return  Result.error("接口调用超过限额（目前默认每个帐号日调用限额为100万）");
                }
                return Result.success();
            } else {
                return  Result.error("发送失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return  Result.error("发送失败,"+e.getMessage());
        } finally {
            try {
                if (httpClient != null) {
                    // 释放资源
                    httpClient.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void checkToken(HttpServletRequest request, HttpServletResponse response){
        logger.info("开始消息推送验证!");
        //boolean isGet = request.getMethod().toLowerCase().equals("get");
        PrintWriter print;
        //if (isGet) {//下面从请求中获取校验需要的参数
            // 微信加密签名
            String signature = request.getParameter("signature");
            // 时间戳
            String timestamp = request.getParameter("timestamp");
            // 随机数
            String nonce = request.getParameter("nonce");
            // 随机字符串
            String echostr = request.getParameter("echostr");
            // 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
            if (signature != null && checkSignature(signature, timestamp, nonce)) {
                try {
                    print = response.getWriter();
                    print.write(echostr);
                    print.flush();
                    logger.info("消息推送验证结束!");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        //}
    }
    public boolean checkSignature(String signature, String timestamp, String nonce)
    {
        String token = "meeting";

        String[] arr = { token, timestamp, nonce };

        Arrays.sort(arr);

        StringBuilder content = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            content.append(arr[i]);
        }
        MessageDigest md = null;
        String tmpStr = null;
        try
        {
            md = MessageDigest.getInstance("SHA-1");

            byte[] digest = md.digest(content.toString().getBytes());
            tmpStr = byteToStr(digest);
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }

        return tmpStr != null ? tmpStr.equals(signature.toUpperCase()) : false;
    }

    private String byteToStr(byte[] byteArray)
    {
        String strDigest = "";
        for (int i = 0; i < byteArray.length; i++) {
            strDigest = strDigest + byteToHexStr(byteArray[i]);
        }
        return strDigest;
    }

    private  String byteToHexStr(byte mByte)
    {
        char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
        char[] tempArr = new char[2];
        tempArr[0] = Digit[(mByte >>> 4 & 0xF)];
        tempArr[1] = Digit[(mByte & 0xF)];
        String s = new String(tempArr);
        return s;
    }

    public  void sort(String[] a)
    {
        for (int i = 0; i < a.length - 1; i++) {
            for (int j = i + 1; j < a.length; j++) {
                if (a[j].compareTo(a[i]) < 0)
                {
                    String temp = a[i];
                    a[i] = a[j];
                    a[j] = temp;
                }
            }
        }
    }
}
