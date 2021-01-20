package com.adc.da.budget.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * @ClassName DoubleUtils
 * @Description: 对double 保留两位小数
 * @Author 丁强
 * @Date 2020/4/14
 * @Version V1.0
 **/
public class DoubleUtils {
    public static Double getScale2(Double amount) {
        if (null == amount) {
            return 0.0D;
        }
        return BigDecimal.valueOf(amount).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    /**
     * @Description: 以万为单位，保留两位小数
     * @Param:
     * @Return:
     * @Author: 丁强
     * @Date: 15:02
     **/
    public static Double getDivideTenThousandScale2(Double amount) {
        if (null == amount) {
            return 0.0D;
        }
        return BigDecimal.valueOf(amount).divide(BigDecimal.valueOf(10000)).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    /**
     * @Description: 以万为单位，保留两位小数
     * @Param:
     * @Return:
     * @Author: 丁强
     * @Date: 15:02
     **/
    public static BigDecimal getDivideTenThousandScale2(BigDecimal amount) {
        if (null == amount) {
            return new BigDecimal(0);
        }
        return amount.divide(BigDecimal.valueOf(10000)).setScale(2, RoundingMode.HALF_UP);
    }


    public static String formatToNumber(BigDecimal obj) {
        DecimalFormat df = new DecimalFormat("#.00");
        if(obj.compareTo(BigDecimal.ZERO)==0) {
            return "0.00";
        }else if(obj.compareTo(BigDecimal.ZERO)>0&&obj.compareTo(new BigDecimal(1))<0){
            return "0"+df.format(obj).toString();
        }else {
            return df.format(obj).toString();
        }
    }

}
