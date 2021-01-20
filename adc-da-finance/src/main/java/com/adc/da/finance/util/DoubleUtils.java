package com.adc.da.finance.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

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

    /**
     * BigDecimal 保留两位小数
     * @param amount
     * @return
     */
    public static BigDecimal getScale2BybigDecimal(BigDecimal amount){
        if (null == amount) {
            return new BigDecimal(0);
        }
        return amount.setScale(2,BigDecimal.ROUND_HALF_UP);
    }



}
