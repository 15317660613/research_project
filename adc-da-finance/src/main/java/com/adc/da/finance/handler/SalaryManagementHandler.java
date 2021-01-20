package com.adc.da.finance.handler;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.afterturn.easypoi.handler.inter.IExcelVerifyHandler;
import com.adc.da.finance.entity.SalaryManagementEO;
import com.adc.da.util.utils.StringUtils;

public class SalaryManagementHandler implements IExcelVerifyHandler<SalaryManagementEO> {
    @Override
    public ExcelVerifyHandlerResult verifyHandler(SalaryManagementEO salaryManagementEO) {
        ExcelVerifyHandlerResult result = new ExcelVerifyHandlerResult(true);
        if (StringUtils.isEmpty(salaryManagementEO.getBusinessName())
                || StringUtils.isEmpty(salaryManagementEO.getOrgName())
                || StringUtils.isEmpty(salaryManagementEO.getYear())
                || StringUtils.isEmpty(salaryManagementEO.getMonth())
                || StringUtils.isEmpty(salaryManagementEO.getAmount())
        ) {
            result.setSuccess(false);
            result.setMsg("无效数据\n");
            return result;
        }
        return result;
    }
}
