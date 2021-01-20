package com.adc.da.research.handler;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.afterturn.easypoi.handler.inter.IExcelVerifyHandler;
import com.adc.da.research.entity.ResProArriveFeeEO;
import com.adc.da.util.utils.StringUtils;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResProArriveFeeEOHandler implements IExcelVerifyHandler<ResProArriveFeeEO> {
        @Override
        public ExcelVerifyHandlerResult verifyHandler(ResProArriveFeeEO eo) {
                ExcelVerifyHandlerResult result = new ExcelVerifyHandlerResult(true);
                if (StringUtils.isEmpty(eo.getResearchProjectId())){
                        result.setSuccess(false);
                        return result;
                }
                if (StringUtils.isEmpty(eo.getProjectNo())){
                        result.setSuccess(false);
                        return result;
                }
                if(!validate(eo.getArriveFee(),9,6)) {
                        result.setSuccess(false);
                        return result;
                }

                if (StringUtils.isEmpty(eo.getArriveMonth())){
                        result.setSuccess(false);
                        return result;
                }
                if (StringUtils.isEmpty(eo.getArriveYear())){
                        result.setSuccess(false);
                        return result;
                }
                return result;
        }

        public static  boolean validate(BigDecimal bigDecimal, int intLength, int floatLength){
                if (bigDecimal == null){
                        return false;
                }
                if (bigDecimal.compareTo(BigDecimal.ZERO) == 0){
                        return false;
                }
                String data = bigDecimal.toString();
                if (data.indexOf(".")<0){
                        return true;
                }
                int intLen = data.substring(0,data.indexOf(".")).length();
                int floatLen = data.substring(data.indexOf(".")+1,data.length()).length();
                if(intLen > intLength){
                        return false;
                }
                return intLen <= intLength && floatLen <= floatLength ;
        }


        /**
         * 利用正则表达式判断字符串是否是数字
         * @param str
         * @return
         */
        public  static  boolean isSuitableYear(String str){
                if(null == str){
                     return false;
                }
                Pattern pattern = Pattern.compile("^[0-9]{4}$");
                Matcher isNum = pattern.matcher(str);
                if( !isNum.matches() ){
                        return false;
                }
                if (str.length()>4){
                        return  false;
                }
                int year = Integer.valueOf(str);
                if (year<1900 || year>3000){
                        return false;
                }
                return true;
        }

        public static void main(String[] args) {
                BigDecimal bigDecimal = new BigDecimal("23.0");
                String data = bigDecimal.toString();
                int intLen = data.substring(0,data.indexOf(".")).length();
                int floatLen = data.substring(data.indexOf(".")+1,data.length()).length();
                System.out.println(intLen+"   "+floatLen);
        }

}
