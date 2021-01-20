package com.adc.da.finance.handler;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.afterturn.easypoi.handler.inter.IExcelVerifyHandler;
import com.adc.da.finance.dto.ReceivablesManagementDto;
import com.adc.da.util.utils.StringUtils;

public class ReceivablesManagementDtoHandler implements IExcelVerifyHandler<ReceivablesManagementDto> {
    @Override
    public ExcelVerifyHandlerResult verifyHandler(ReceivablesManagementDto dto) {
        ExcelVerifyHandlerResult result = new ExcelVerifyHandlerResult(true);
        if (StringUtils.isEmpty(dto.getDepartName())
                && StringUtils.isEmpty(dto.getRemYear())
                && StringUtils.isEmpty(dto.getRemMonth())
                && StringUtils.isEmpty(dto.getBusinessGonfigName())
                && StringUtils.isEmpty(dto.getRemMoney())
        ){
            result.setSuccess(false);
            result.setMsg("无效数据");
            return result;
        }
        return result;
    }
}
