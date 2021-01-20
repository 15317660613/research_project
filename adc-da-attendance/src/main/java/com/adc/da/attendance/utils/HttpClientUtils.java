package com.adc.da.attendance.utils;


import com.adc.da.util.exception.AdcDaBaseException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * HttpClient 工具包
 */
public class HttpClientUtils {

    private static final Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);

    /**
     * httpPost 请求封装
     * @param url
     * @param params
     * @return
     */
    public static String getHttpPost(String url,String params){
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);// 创建httpPost
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-Type", "application/json");
        String charSet = "UTF-8";
        StringEntity entity = new StringEntity(params, charSet);
        System.out.println("entity:"+entity.toString());
        httpPost.setEntity(entity);
        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpPost);
            StatusLine status = response.getStatusLine();
            int state = status.getStatusCode();
            if (state == HttpStatus.SC_OK) {
                HttpEntity responseEntity = response.getEntity();
                String jsonString = EntityUtils.toString(responseEntity);
                return jsonString;
            }
            else{
                System.out.println("调用接口state："+state);
            }
        }catch (Exception e){
            throw new  AdcDaBaseException("调用出错");
        }
        finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    logger.error(e.getMessage());
                }
            }
            try {
                httpclient.close();
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
        return null;
    }

    /**
     * httpGet 请求
     * @param url
     * @param paramMap
     * @return
     */
    public static String getHttpGet(String url,Map paramMap){
        String resultJson = "";
        try{
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet();
            String param = paramMap.toString().substring(1,paramMap.toString().length()-1).replace(",","&").replace(" ","");
            httpGet.setURI(new URI(url+"?"+param));
            CloseableHttpResponse response = httpClient.execute(httpGet);
            HttpEntity resEntity = response.getEntity();
            //获取返回字符串
            resultJson = EntityUtils.toString(resEntity, Charset.forName("utf-8"));
        }catch (Exception e ){
            throw new  AdcDaBaseException("调用出错");
        }
        return resultJson;
    }
}
