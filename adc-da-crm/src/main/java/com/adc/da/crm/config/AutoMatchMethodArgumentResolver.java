package com.adc.da.crm.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.adc.da.util.utils.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.adc.da.crm.annotation.AutoMatch;
import com.adc.da.crm.annotation.MatchField;
import com.adc.da.crm.controller.ContractApprovalEOController;
import com.adc.da.crm.util.JsonUtil;
import com.adc.da.form.entity.AdcFormEO;
import com.adc.da.form.service.CustomFormService;

/**
 * 自定义参数解析，用于解析前端传过来的JSON串
 * created by chenhaidong 2018/11/26
 */
@Component
public class AutoMatchMethodArgumentResolver implements HandlerMethodArgumentResolver {

    private static final Logger logger = LoggerFactory.getLogger(ContractApprovalEOController.class);

    @Autowired
    CustomFormService customFormService;


    /**
     * 处理注解触发时的逻辑
     * @param parameter
     * @param container
     * @param request
     * @param binderFactory
     * @return
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer container, NativeWebRequest request,WebDataBinderFactory binderFactory){
        BufferedReader br = null;
        try {
            //获取http请求对象
            HttpServletRequest req = request.getNativeRequest(HttpServletRequest.class);
            //从请求的输入流中获取字符串并拼接成JSON串
            StringBuilder sb = new StringBuilder("");
            String s = null;
            br = new BufferedReader(new InputStreamReader(req.getInputStream()));
            while ((s = br.readLine()) != null) sb.append(s);
            s = sb.toString();
            //解析JSON串转为Map对象保存
            Map map = JsonUtil.jsonToBean(s, HashMap.class);
            if(map == null || map.size() <= 1 || map.get("formId") == null) return null;
            //todo 处理map中formContent数据
            Map formContent = JsonUtil.jsonToBean(map.get("formContent").toString(), HashMap.class);
            if(formContent == null || formContent.isEmpty()) return null;
            //todo 处理formContent数据
//            Map finalMap = MapDataHandlingUtil.dealExpandData(formContent);


            //生成注解参数对应的类对象
            Class autoMatchClass = Class.forName(parameter.getParameterType().getName());
            //获取所生成类的所有字段
            Field[] field = autoMatchClass.getDeclaredFields();
            if(field == null || field.length == 0) return null;
            //通过表单id获取表单信息
            AdcFormEO adcFormEO = customFormService.selectByPrimaryKey((String)map.get("formId"));
            if(adcFormEO == null || adcFormEO.getColumnName() == null || adcFormEO.getColumnID() == null || adcFormEO.getColumnName().trim().isEmpty()) return null;
            //拆分表单字段名与字段id
            String[] columnName = adcFormEO.getColumnName().split(",");
            String[] columnId= adcFormEO.getColumnID().split(",");
            //获取类实例
            Object o = autoMatchClass.newInstance();
            int index = -1;
            for(Field fie : field){

                //如果字段上有@MatchField注解
                if(fie.isAnnotationPresent(MatchField.class)){
                    MatchField userField = fie.getAnnotation(MatchField.class);
                    //如果字段注解名称存在
                    if((index = indexOfArray(columnName,userField.value())) != -1){
                        //字段权限设置为可访问
                        fie.setAccessible(true);
                        //获取参数值
                        Object v = formContent.get(columnId[index]);
                        //判断字段是否是日期类型 如果是需要转化参数值格式
                        String type = fie.getType().getSimpleName();
                        if(StringUtils.equals(type, "Date")){
                            Date date = DateUtils.stringToDate(v.toString(), "yyyy-MM-dd");
                            //给字段赋值
                            fie.set(o,date);
                        }else {
                            //给字段赋值
                            fie.set(o, v);
                        }
                    }
                }
            }
            return o;
        } catch (IOException e) {
            logger.error(e.getMessage());
        }catch (ClassNotFoundException e) {
            logger.error(e.getMessage());
        }catch (InstantiationException e) {
            logger.error(e.getMessage());
        } catch (IllegalAccessException e) {
            logger.error(e.getMessage());
        }catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    logger.error("关闭bufferReader失败", e);
                }
            }
        }
        return null;
    }


    private int indexOfArray(String[] arr,String s){
        for(int i = 0;i < arr.length;i++){
            if(arr[i].equals(s)) return i;
        }
        return -1;
    }

    //如果包含@CurrentUser注解，则需要解析参数
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AutoMatch.class);

    }
}
