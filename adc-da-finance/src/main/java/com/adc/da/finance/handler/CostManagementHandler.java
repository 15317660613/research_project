package com.adc.da.finance.handler;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.afterturn.easypoi.handler.inter.IExcelVerifyHandler;
import com.adc.da.finance.entity.CostManagementEO;
import com.adc.da.util.utils.StringUtils;

public class CostManagementHandler implements IExcelVerifyHandler<CostManagementEO> {
    @Override
    public ExcelVerifyHandlerResult verifyHandler(CostManagementEO costManagementEO) {
        ExcelVerifyHandlerResult result = new ExcelVerifyHandlerResult(true);
        if (StringUtils.isEmpty(costManagementEO.getOrgName())
                || StringUtils.isEmpty(costManagementEO.getYear())
                || StringUtils.isEmpty(costManagementEO.getMonth())
                || StringUtils.isEmpty(costManagementEO.getDay())
                || StringUtils.isEmpty(costManagementEO.getDeadlineTime())
                || StringUtils.isEmpty(costManagementEO.getAmount())
        ) {

            result.setSuccess(false);
            result.setMsg("无效数据\n");
            return result;
        }
        return result;
    }
}
