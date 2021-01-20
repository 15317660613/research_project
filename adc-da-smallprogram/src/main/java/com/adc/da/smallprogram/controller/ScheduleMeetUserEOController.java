package com.adc.da.smallprogram.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.util.List;
import java.util.Map;

import com.adc.da.smallprogram.entity.ScheduleMeetEO;
import com.adc.da.smallprogram.service.ScheduleMeetEOService;
import com.adc.da.util.utils.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.adc.da.base.web.BaseController;
import com.adc.da.smallprogram.entity.ScheduleMeetUserEO;
import com.adc.da.smallprogram.page.ScheduleMeetUserEOPage;
import com.adc.da.smallprogram.service.ScheduleMeetUserEOService;

import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.http.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;

@RestController
@RequestMapping("/${restPath}/smallprogram/scheduleMeetUser")
@Api(description = "小程序会议接收人|ScheduleMeetUserEO|")
public class ScheduleMeetUserEOController extends BaseController<ScheduleMeetUserEO>{

    private static final Logger logger = LoggerFactory.getLogger(ScheduleMeetUserEOController.class);

    @Autowired
    private ScheduleMeetUserEOService scheduleMeetUserEOService;
    @Autowired
    private ScheduleMeetEOService scheduleMeetEOService;

	@ApiOperation(value = "|ScheduleMeetUserEO|分页查询")
    @GetMapping("/page")
//    @RequiresPermissions("smallprogram:scheduleMeetUser:page")
    public ResponseMessage<PageInfo<ScheduleMeetUserEO>> page(ScheduleMeetUserEOPage page) throws Exception {
        List<ScheduleMeetUserEO> rows = scheduleMeetUserEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|ScheduleMeetUserEO|查询")
    @GetMapping("")
//    @RequiresPermissions("smallprogram:scheduleMeetUser:list")
    public ResponseMessage<List<ScheduleMeetUserEO>> list(ScheduleMeetUserEOPage page) throws Exception {
        return Result.success(scheduleMeetUserEOService.queryByList(page));
	}

    @ApiOperation(value = "|ScheduleMeetUserEO|详情")
    @GetMapping("/{id}")
//    @RequiresPermissions("smallprogram:scheduleMeetUser:get")
    public ResponseMessage<ScheduleMeetUserEO> find(@PathVariable String id) throws Exception {
        return Result.success(scheduleMeetUserEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|ScheduleMeetUserEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("smallprogram:scheduleMeetUser:save")
    public ResponseMessage<ScheduleMeetUserEO> create(@RequestBody ScheduleMeetUserEO scheduleMeetUserEO) throws Exception {
	    scheduleMeetUserEO.setId(UUID.randomUUID10());
        scheduleMeetUserEOService.insertSelective(scheduleMeetUserEO);
        return Result.success(scheduleMeetUserEO);
    }

    @ApiOperation(value = "|ScheduleMeetUserEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("smallprogram:scheduleMeetUser:update")
    public ResponseMessage<ScheduleMeetUserEO> update(@RequestBody ScheduleMeetUserEO scheduleMeetUserEO) throws Exception {
        scheduleMeetUserEOService.updateByPrimaryKeySelective(scheduleMeetUserEO);
        return Result.success(scheduleMeetUserEO);
    }

    @ApiOperation(value = "|ScheduleMeetUserEO|修改")
    @PutMapping("/finish")
//    @RequiresPermissions("smallprogram:scheduleMeetUser:update")
    public ResponseMessage<ScheduleMeetUserEO> finish(@RequestBody ScheduleMeetUserEO scheduleMeetUserEO) throws Exception {
        scheduleMeetUserEOService.updateByMeetIdAndUserIdSelective(scheduleMeetUserEO);
        ScheduleMeetUserEOPage scheduleMeetUserEOPage = new ScheduleMeetUserEOPage();
        scheduleMeetUserEOPage.setMeetId(scheduleMeetUserEO.getMeetId());
        List<ScheduleMeetUserEO> scheduleMeetUserEOList = scheduleMeetUserEOService.queryByList(scheduleMeetUserEOPage);
        int finishedCount = 0 ;
        for (ScheduleMeetUserEO meetUserEO : scheduleMeetUserEOList){
            if (null != meetUserEO.getStatus()&&0 != meetUserEO.getStatus()){
                finishedCount ++ ;
            }
        }
        if (finishedCount == scheduleMeetUserEOList.size()){
            ScheduleMeetEO scheduleMeetEO = new ScheduleMeetEO();
            scheduleMeetEO.setId(scheduleMeetUserEO.getMeetId());
            scheduleMeetEO.setStatus(1);
            scheduleMeetEOService.updateByPrimaryKeySelective(scheduleMeetEO);
        }else {
            ScheduleMeetEO scheduleMeetEO = new ScheduleMeetEO();
            scheduleMeetEO.setId(scheduleMeetUserEO.getMeetId());
            scheduleMeetEO.setStatus(0);
            scheduleMeetEOService.updateByPrimaryKeySelective(scheduleMeetEO);
        }


        return Result.success(scheduleMeetUserEO);
    }



    @ApiOperation(value = "|ScheduleMeetUserEO|删除")
    @DeleteMapping("/{id}")
//    @RequiresPermissions("smallprogram:scheduleMeetUser:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        scheduleMeetUserEOService.deleteByPrimaryKey(id);
        logger.info("delete from TS_SCHEDULE_MEET_USER where id = {}", id);
        return Result.success();
    }

}
