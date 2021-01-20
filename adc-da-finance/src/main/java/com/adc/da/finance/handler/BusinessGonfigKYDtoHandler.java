package com.adc.da.finance.handler;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.afterturn.easypoi.handler.inter.IExcelVerifyHandler;
import com.adc.da.finance.dto.BusinessGonfigDto;
import com.adc.da.finance.dto.BusinessGonfigKYDto;
import com.adc.da.util.utils.StringUtils;

public class BusinessGonfigKYDtoHandler implements IExcelVerifyHandler<BusinessGonfigKYDto> {
    @Override
    public ExcelVerifyHandlerResult verifyHandler(BusinessGonfigKYDto businessGonfigDto) {
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
