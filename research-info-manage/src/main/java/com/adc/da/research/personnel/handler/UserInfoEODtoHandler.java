package com.adc.da.research.personnel.handler;


import cn.afterturn.easypoi.excel.entity.result.ExcelVerifyHandlerResult;
import cn.afterturn.easypoi.handler.inter.IExcelVerifyHandler;
import com.adc.da.research.personnel.dto.UserInfoEODto;
import com.adc.da.util.utils.StringUtils;


public class UserInfoEODtoHandler implements IExcelVerifyHandler<UserInfoEODto> {
    @Override
    public ExcelVerifyHandlerResult verifyHandler(UserInfoEODto dto) {
        ExcelVerifyHandlerResult result = new ExcelVerifyHandlerResult(true);
        if (StringUtils.isEmpty(dto.getId())
                && StringUtils.isEmpty(dto.getExt1())
                && StringUtils.isEmpty(dto.getEmail())
                && StringUtils.isEmpty(dto.getSex())
                && StringUtils.isEmpty(dto.getLastDegree())
                && StringUtils.isEmpty(dto.getTitleOf())
                && StringUtils.isEmpty(dto.getUserName())
                && StringUtils.isEmpty(dto.getOverseaExperience())
                && StringUtils.isEmpty(dto.getResearchExperience())
                && StringUtils.isEmpty(dto.getEducationExperience())
                && StringUtils.isEmpty(dto.getCreateTime())
        ){
            result.setSuccess(false);
            result.setMsg("无效数据");
            return result;
        }
        return result;
    }
}