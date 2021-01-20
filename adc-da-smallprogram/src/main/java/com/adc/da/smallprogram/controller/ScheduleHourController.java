package com.adc.da.smallprogram.controller;

import com.adc.da.budget.dto.StaffScheduleRequestDTO;
import com.adc.da.login.util.UserUtils;
import com.adc.da.smallprogram.entity.ScheduleDetailEO;
import com.adc.da.smallprogram.entity.ScheduleHourEO;
import com.adc.da.smallprogram.service.ScheduleHourService;
import com.adc.da.smallprogram.vo.ScheduleGetVO;
import com.adc.da.smallprogram.vo.ScheduleHourVO;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.utils.RequestUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping("/${restPath}/smallProgram/ScheduleHourController")
@Api(description = "小程序日程相关|ScheduleHourController|")
public class ScheduleHourController {
    private static final Logger logger = LoggerFactory.getLogger(ScheduleHourController.class);

    @Autowired
    private ScheduleHourService scheduleHourService;


    @ApiOperation("新增日程")
    @PostMapping("/save")
    public ResponseMessage save(@RequestBody ScheduleHourEO scheduleHourEO) throws Exception{
        return scheduleHourService.save(scheduleHourEO);
    }



    @ApiOperation("通过id获取日程")
    @GetMapping("/getById/{scheduleHourId}")
    public ResponseMessage getById(@PathVariable String scheduleHourId) throws Exception{
        return scheduleHourService.getById(scheduleHourId);
    }

    @ApiOperation("通过id删除日程")
    @GetMapping("/deleteById/{scheduleHourId}")
    public ResponseMessage deleteById(@PathVariable String scheduleHourId) throws Exception{
        return scheduleHourService.deleteById(scheduleHourId);
    }

    @ApiOperation("通过时间段获取日程数据")
    @GetMapping("/list/{userId}/{weeknum}")
    public ResponseMessage list(@PathVariable String userId,@PathVariable Integer weeknum) throws Exception{
        return scheduleHourService.list(userId,weeknum);
    }

    @ApiOperation(value = "获取一个日程")
    @PostMapping("/getSchedule")
    public ResponseMessage getSchedule(@RequestBody ScheduleGetVO scheduleGetVO) {
        return Result.success(scheduleHourService.getSchedule(scheduleGetVO));
    }

    /**
     * 查询
     */
    @PostMapping(value = "/calenderQuery", consumes = APPLICATION_JSON_UTF8_VALUE)
    @ApiOperation(value = "查询日历接口")
    public ResponseMessage<List<ScheduleHourVO>> newQuery(@RequestBody StaffScheduleRequestDTO staffScheduleRequestDTO) {

        return Result.success(scheduleHourService.newQueryStaffSchedule(staffScheduleRequestDTO));

    }

    @ApiOperation(value = "|ScheduleHour|删除")
    @DeleteMapping("/{id}")
//    @RequiresPermissions("smallprogram:ScheduleHour:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        scheduleHourService.logicDelete(id);
        logger.info("delete from TS_ScheduleHour where id = {}", id);
        return Result.success();
    }

}
