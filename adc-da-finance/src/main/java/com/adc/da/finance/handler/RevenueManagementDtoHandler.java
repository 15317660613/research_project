package com.adc.da.finance.handler;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.afterturn.easypoi.handler.inter.IExcelVerifyHandler;
import com.adc.da.finance.dto.RevenueManagementDto;
import com.adc.da.util.utils.StringUtils;

public class RevenueManagementDtoHandler implements IExcelVerifyHandler<RevenueManagementDto> {
    @Override
    public ExcelVerifyHandlerResult verifyHandler(RevenueManagementDto dto) {
        ExcelVerifyHandlerResult result = new ExcelVerifyHandlerResult(true);
        if (StringUtils.isEmpty(dto.getDepartName())
                && StringUtils.isEmpty(dto.getRmYear())
                && StringUtils.isEmpty(dto.getRmMonth())
                && StringUtils.isEmpty(dto.getRmSubjectName())
                && StringUtils.isEmpty(dto.getBusinessGonfigName())
                && StringUtils.isEmpty(dto.getRmMoney())
                && StringUtils.isEmpty(dto.getRmAbstract())
        ){
            result.setSuccess(false);
            result.setMsg("无效数据");
            return result;
        }
        return result;
    }
}
