package com.adc.da.carsales.page;

import com.adc.da.base.page.BasePage;
import lombok.Data;

import java.util.Date;

/**
 * <b>功能：</b>DB_CAR_SALES CarSalesEOPage<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-04-03 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Data
public class CarSalesEOPage extends BasePage {

    private String id;
    private String idOperator = "=";
    private String enterpriseId;
    private String enterpriseIdOperator = "=";
    private String monthSales;
    private String monthSalesOperator = "=";
    private String growthRate;
    private String growthRateOperator = "=";
    private String createUserId;
    private String createUserIdOperator = "=";
    private String createUserName;
    private String createUserNameOperator = "=";
    private String updateTime;
    private String updateTime1;
    private String updateTime2;
    private String updateTimeOperator = "=";
    private String delFlag;
    private String delFlagOperator = "=";
    private String ext1;
    private String ext1Operator = "=";
    private String ext2;
    private String ext2Operator = "=";
    private String ext3;
    private String ext3Operator = "=";
    private String ext4;
    private String ext4Operator = "=";
    private String ext5;
    private String ext5Operator = "=";

    private String enterpriseName;
    private String enterpriseNameOperator = "=";

}
