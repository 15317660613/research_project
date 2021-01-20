package com.adc.da.finance.handler;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.afterturn.easypoi.handler.inter.IExcelVerifyHandler;
import com.adc.da.finance.dto.BusinessGonfigDto;
import com.adc.da.util.utils.StringUtils;

public class BusinessGonfigDtoHandler implements IExcelVerifyHandler<BusinessGonfigDto> {
    @Override
    public ExcelVerifyHandlerResult verifyHandler(BusinessGonfigDto businessGonfigDto) {
        ExcelVerifyHandlerResult result = new ExcelVerifyHandlerResult(true);
        if (StringUtils.isEmpty(businessGonfigDto.getBgName())
                && StringUtils.isEmpty(businessGonfigDto.getBgNumber())
                && StringUtils.isEmpty(businessGonfigDto.getBgStatusName())
                && StringUtils.isEmpty(businessGonfigDto.getDepartName())){
            result.setSuccess(false);
            result.setMsg("无效数据");
            return result;
        }
        return result;
    }
}
