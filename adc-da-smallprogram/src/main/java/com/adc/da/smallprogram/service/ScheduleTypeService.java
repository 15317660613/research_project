package com.adc.da.smallprogram.service;

import com.adc.da.smallprogram.entity.ScheduleTypeEO;
import com.adc.da.smallprogram.enums.DeleteCodeEnum;
import com.adc.da.smallprogram.page.ScheduleTypeEOPage;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service("scheduleTypeService")
public class ScheduleTypeService {
    private static final Logger logger = LoggerFactory.getLogger(ScheduleTypeService.class);

    @Autowired
    private ScheduleTypeEOService scheduleTypeEOService;
    /**
     * 新增类型
     * @param scheduleTypeEO
     * @return
     * @throws Exception
     */
    public ResponseMessage save(ScheduleTypeEO scheduleTypeEO) throws Exception{
        //如果id为空,新增
        if(StringUtils.isBlank(scheduleTypeEO.getId())){
            //判断必填
            if(StringUtils.isBlank(scheduleTypeEO.getTypeName())){
                return Result.error("类型名称必填!");
            }
            scheduleTypeEO.setDelFlag(DeleteCodeEnum.NORMAL.getCode());
            scheduleTypeEOService.insertSelective(scheduleTypeEO);
        }else{
            //更新
            scheduleTypeEOService.updateByPrimaryKeySelective(scheduleTypeEO);
        }
        return Result.success();
    }

    /**
     * 根据id获取类型
     * @param scheduleHourEOId
     * @return
     * @throws Exception
     */
    public ResponseMessage getById(String scheduleHourEOId) throws Exception{
        if(StringUtils.isBlank(scheduleHourEOId)){
            return Result.error("请输入类型id");
        }
        ScheduleTypeEO scheduleTypeEO = scheduleTypeEOService.selectByPrimaryKey(scheduleHourEOId);
        return Result.success(scheduleTypeEO);
    }

    /**
     * 通过id删除
     * @param scheduleHourEOId
     * @return
     * @throws Exception
     */
    public ResponseMessage deleteById(String scheduleHourEOId) throws Exception{
        if(StringUtils.isBlank(scheduleHourEOId)){
            return Result.error("请输入日程id");
        }
        scheduleTypeEOService.deleteByPrimaryKey(scheduleHourEOId);
        return Result.success();
    }

    /**
     * 分页查询类型
     * @param scheduleHourEOPage
     * @return
     * @throws Exception
     */
    public List<ScheduleTypeEO> page(ScheduleTypeEOPage scheduleHourEOPage) throws Exception{
        List<ScheduleTypeEO> rows = scheduleTypeEOService.queryByPage(scheduleHourEOPage);
        return rows;
    }

    /**
     * 不分页查询
     * @param scheduleHourEOPage
     * @return
     * @throws Exception
     */
    public ResponseMessage list(ScheduleTypeEOPage scheduleHourEOPage) throws Exception{
        List<ScheduleTypeEO> scheduleTypeEOS = scheduleTypeEOService.queryByList(scheduleHourEOPage);
        return Result.success(scheduleTypeEOS);
    }
}

