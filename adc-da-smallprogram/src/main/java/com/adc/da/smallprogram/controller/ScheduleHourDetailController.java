package com.adc.da.smallprogram.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import com.adc.da.smallprogram.entity.ScheduleDetailEO;
import com.adc.da.smallprogram.service.ScheduleDetailEOService;
import com.adc.da.smallprogram.service.ScheduleHourDetailService;
import com.adc.da.smallprogram.vo.ScheduleHourDetailVO;
import com.adc.da.util.utils.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/${restPath}/smallprogram/scheduleDetail")
@Api(description = "小程序日程详情|ScheduleHourDetail|")
public class ScheduleHourDetailController {

    private static final Logger logger = LoggerFactory.getLogger(ScheduleHourDetailController.class);

    @Autowired
    private ScheduleHourDetailService scheduleHourDetailService;

    @Autowired
    private ScheduleDetailEOService scheduleDetailEOService;

    @ApiOperation(value = "|ScheduleHourDetail|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("smallprogram:scheduleDetail:save")
    public ResponseMessage<ScheduleHourDetailVO> create(@RequestBody ScheduleHourDetailVO scheduleHourDetailVO) throws Exception {
        return Result.success(scheduleHourDetailService.saveOrUpdate(scheduleHourDetailVO));
    }

    @ApiOperation(value = "|ScheduleDetailEO|删除")
    @DeleteMapping("/{id}")
//    @RequiresPermissions("smallprogram:scheduleDetail:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        scheduleDetailEOService.logicDelete(id);
        logger.info("delete from TS_SCHEDULE_DETAIL where id = {}", id);
        return Result.success();
    }

    @ApiOperation(value = "|ScheduleHourDetail|详情")
    @PostMapping("/find")
//    @RequiresPermissions("smallprogram:scheduleDetail:get")
    public ResponseMessage<ScheduleHourDetailVO> find(@RequestBody ScheduleHourDetailVO scheduleHourDetailVO) throws Exception {
        ScheduleHourDetailVO detailVO = scheduleHourDetailService.find(scheduleHourDetailVO.getId(), scheduleHourDetailVO.getUserId(),scheduleHourDetailVO.getDestUserId()) ;
        return Result.success(detailVO);
    }

    @ApiOperation(value = "|ScheduleHourDetail|findOther")
    @PostMapping("/findOther")
    public ResponseMessage<ScheduleHourDetailVO> findOther(@RequestBody ScheduleHourDetailVO scheduleHourDetailVO) throws Exception {
        ScheduleHourDetailVO detailVO = scheduleHourDetailService.findOther(scheduleHourDetailVO.getUserId(),scheduleHourDetailVO.getDestUserId(),scheduleHourDetailVO.getScheduleDate(),scheduleHourDetailVO.getScheduleHour());
        return Result.success(detailVO);
    }

    @ApiOperation(value = "|ScheduleHourDetail|findMe")
    @GetMapping("/findMe/{id}")
    public ResponseMessage<ScheduleHourDetailVO> findMe(@PathVariable String id) throws Exception {
        return Result.success(scheduleHourDetailService.find(id, false));
    }
}
