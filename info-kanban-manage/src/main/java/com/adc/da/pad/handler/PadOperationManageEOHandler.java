package com.adc.da.pad.handler;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.afterturn.easypoi.handler.inter.IExcelVerifyHandler;
import com.adc.da.pad.entity.PadOperationManageEO;
import com.adc.da.util.utils.StringUtils;

import java.math.BigDecimal;

public class PadOperationManageEOHandler implements IExcelVerifyHandler<PadOperationManageEO> {
    @Override
    public ExcelVerifyHandlerResult verifyHandler(PadOperationManageEO padOperationManageEO) {
        ExcelVerifyHandlerResult result = new ExcelVerifyHandlerResult(true);
        if (StringUtils.isEmpty(padOperationManageEO.getBigOrgName())
                || StringUtils.isEmpty(padOperationManageEO.getYear())
                || StringUtils.isEmpty(padOperationManageEO.getMonth())
                || StringUtils.isEmpty(padOperationManageEO.getContractAmount())
                || StringUtils.isEmpty(padOperationManageEO.getInvoiceAmount())
                || StringUtils.isEmpty(padOperationManageEO.getIncomeAmount())
        ) {

            result.setSuccess(false);
            result.setMsg("无效数据\n");
            return result;
        }
        if (!validate(padOperationManageEO.getContractAmount().setScale(2,BigDecimal.ROUND_HALF_UP))){
            result.setSuccess(false);
            result.setMsg("无效数据\n");
            return result;
        }
        if (!validate(padOperationManageEO.getInvoiceAmount().setScale(2,BigDecimal.ROUND_HALF_UP))){
            result.setSuccess(false);
            result.setMsg("无效数据\n");
            return result;
        }
        if (!validate(padOperationManageEO.getIncomeAmount().setScale(2,BigDecimal.ROUND_HALF_UP))){
            result.setSuccess(false);
            result.setMsg("无效数据\n");
            return result;
        }
        return result;
    }

    public static  boolean validate(BigDecimal bigDecimal){
        if (bigDecimal.compareTo(BigDecimal.ZERO) == 0){
            return true;
        }
        String data = bigDecimal.toString();
        int intLen = data.substring(0,data.indexOf(".")).length();
        int floatLen = data.substring(data.indexOf("."),data.length()-1).length();
        return intLen<12 && floatLen <3 ;
    }

//    public static void main(String[] args) {
//        BigDecimal bigDecimal = new BigDecimal(0).setScale(2,BigDecimal.ROUND_HALF_UP);
//        bigDecimal = new BigDecimal(0).setScale(2,BigDecimal.ROUND_HALF_UP);
//        bigDecimal = new BigDecimal(0).setScale(2,BigDecimal.ROUND_HALF_UP);
//        System.out.println(validate(bigDecimal));
//    }
}