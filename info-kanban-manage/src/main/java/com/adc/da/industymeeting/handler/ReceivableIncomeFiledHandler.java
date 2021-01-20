package com.adc.da.industymeeting.handler;

import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.afterturn.easypoi.handler.inter.IExcelVerifyHandler;
import com.adc.da.industymeeting.dto.ReceivableIncomeFiledDTO;
import com.adc.da.util.utils.StringUtils;

public class ReceivableIncomeFiledHandler implements IExcelVerifyHandler<ReceivableIncomeFiledDTO> {
    @Override
    public ExcelVerifyHandlerResult verifyHandler(ReceivableIncomeFiledDTO receivableIncomeFiledDTO) {
        ExcelVerifyHandlerResult result = new ExcelVerifyHandlerResult(true);
        if (StringUtils.isEmpty(receivableIncomeFiledDTO.getArea())
                || StringUtils.isEmpty(receivableIncomeFiledDTO.getYear())
                || StringUtils.isEmpty(receivableIncomeFiledDTO.getMonth())
                || StringUtils.isEmpty(receivableIncomeFiledDTO.getCompany())
                || StringUtils.isEmpty(receivableIncomeFiledDTO.getIncome())
                || StringUtils.isEmpty(receivableIncomeFiledDTO.getReceivableBalance())
        ) {

            result.setSuccess(false);
            result.setMsg("无效数据\n");
            return result;
        }
        return result;
    }
}
