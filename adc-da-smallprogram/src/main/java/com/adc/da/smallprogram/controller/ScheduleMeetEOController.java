package com.adc.da.smallprogram.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.util.List;
import java.util.Map;

import com.adc.da.smallprogram.page.MeetPage;
import com.adc.da.smallprogram.vo.MeetVO;
import org.jboss.logging.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.adc.da.base.web.BaseController;
import com.adc.da.smallprogram.entity.ScheduleMeetEO;
import com.adc.da.smallprogram.page.ScheduleMeetEOPage;
import com.adc.da.smallprogram.service.ScheduleMeetEOService;

import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.http.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;

@RestController
@RequestMapping("/${restPath}/smallprogram/scheduleMeet")
@Api(description = "小程序会议部分|ScheduleMeetEO|")
public class ScheduleMeetEOController extends BaseController<ScheduleMeetEO>{

    private static final Logger logger = LoggerFactory.getLogger(ScheduleMeetEOController.class);

    @Autowired
    private ScheduleMeetEOService scheduleMeetEOService;

	@ApiOperation(value = "|ScheduleMeetEO|分页查询")
    @GetMapping("/page")
    @RequiresPermissions("smallprogram:scheduleMeet:page")
    public ResponseMessage<PageInfo<ScheduleMeetEO>> page(ScheduleMeetEOPage page) throws Exception {
        List<ScheduleMeetEO> rows = scheduleMeetEOService.queryByPage(page);

        return Result.success(getPageInfo(page.getPager(), rows));
    }


    @ApiOperation(value = "|ScheduleMeetEO|分页查询根据")
    @GetMapping("/queryByPageAdmin")
//    @RequiresPermissions("smallprogram:scheduleMeet:page")
    public ResponseMessage<PageInfo<ScheduleMeetEO>> queryByPageAdmin(MeetPage page) throws Exception {
        List<ScheduleMeetEO> rows = scheduleMeetEOService.queryByPageAdmin(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

    @ApiOperation(value = "|ScheduleMeetEO|分页查询根据userId")
    @GetMapping("/selectPageByReceiveUserId")
//    @RequiresPermissions("smallprogram:scheduleMeet:page")
    public ResponseMessage<PageInfo<ScheduleMeetEO>> selectPageByReceiveUserId(MeetPage page) throws Exception {
        List<ScheduleMeetEO> rows = scheduleMeetEOService.queryByPageWithReceiveUserId(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

    @ApiOperation(value = "|ScheduleMeetEO|查询全部")
    @GetMapping("/queryByPageAll")
//    @RequiresPermissions("smallprogram:scheduleMeet:page")
    public ResponseMessage<PageInfo<ScheduleMeetEO>> queryByPageAll(MeetPage page) throws Exception {
        List<ScheduleMeetEO> rows = scheduleMeetEOService.queryByPageAll(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|ScheduleMeetEO|查询")
    @GetMapping("")
    @RequiresPermissions("smallprogram:scheduleMeet:list")
    public ResponseMessage<List<ScheduleMeetEO>> list(ScheduleMeetEOPage page) throws Exception {
        return Result.success(scheduleMeetEOService.queryByList(page));
	}



    @ApiOperation(value = "|ScheduleMeetEO|查询")
    @GetMapping("/selectByMeetId/{id}")
//    @RequiresPermissions("smallprogram:scheduleMeet:list")
    public ResponseMessage<MeetVO> list(@PathVariable("id") String id ) throws Exception {
        return Result.success(scheduleMeetEOService.selectById(id));
    }

    @ApiOperation(value = "|ScheduleMeetEO|查询")
    @GetMapping("/selectById/{id}/{userId}")
//    @RequiresPermissions("smallprogram:scheduleMeet:list")
    public ResponseMessage<MeetVO> list(@PathVariable("id") String id,@PathVariable("userId") String userId ) throws Exception {
        return Result.success(scheduleMeetEOService.selectById(id,userId));
    }

    @ApiOperation(value = "|ScheduleMeetEO|详情")
    @GetMapping("/{id}")
    @RequiresPermissions("smallprogram:scheduleMeet:get")
    public ResponseMessage<ScheduleMeetEO> find(@PathVariable String id) throws Exception {
        return Result.success(scheduleMeetEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|ScheduleMeetEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("smallprogram:scheduleMeet:save")
    public ResponseMessage<ScheduleMeetEO> create(@RequestBody ScheduleMeetEO scheduleMeetEO) throws Exception {
        scheduleMeetEOService.insertSelective(scheduleMeetEO);
        return Result.success(scheduleMeetEO);
    }

    @ApiOperation(value = "|MeetVO|新增")
    @PostMapping("/saveMeetVO")
//    @RequiresPermissions("smallprogram:scheduleMeet:save")
    public ResponseMessage saveMeetVO(@RequestBody MeetVO meetVO) throws Exception {
        scheduleMeetEOService.saveMeetVO(meetVO);
        return Result.success();
    }

	 @ApiOperation(value = "|MeetVO|发送")
    @PostMapping("/send")
//    @RequiresPermissions("smallprogram:scheduleMeet:save")
    public ResponseMessage send(@RequestBody MeetVO meetVO) throws Exception {
        scheduleMeetEOService.send(meetVO);
        return Result.success();
    }

    @ApiOperation(value = "|MeetVO|发送")
    @GetMapping("/getByReceiveUserId/{id}")
//    @RequiresPermissions("smallprogram:scheduleMeet:save")
    public ResponseMessage getByReceiveUserId(@PathVariable("id") String id) throws Exception {

        return Result.success(scheduleMeetEOService.selectByReceiveUserId(id));
    }



    @ApiOperation(value = "|ScheduleMeetEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("smallprogram:scheduleMeet:update")
    public ResponseMessage<ScheduleMeetEO> update(@RequestBody ScheduleMeetEO scheduleMeetEO) throws Exception {
        scheduleMeetEOService.updateByPrimaryKeySelective(scheduleMeetEO);
        return Result.success(scheduleMeetEO);
    }

    @ApiOperation(value = "|ScheduleMeetEO|删除")
    @DeleteMapping("/{id}")
//    @RequiresPermissions("smallprogram:scheduleMeet:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        scheduleMeetEOService.deleteByPrimaryKey(id);
        logger.info("delete from TS_SCHEDULE_MEET where id = {}", id);
        return Result.success();
    }

    @ApiOperation(value = "|ScheduleMeetEO|删除")
    @DeleteMapping("/softDelete/{id}")
//    @RequiresPermissions("smallprogram:scheduleMeet:delete")
    public ResponseMessage softDelete(@PathVariable String id) throws Exception {
        scheduleMeetEOService.softDelete(id);
        logger.info("delete from TS_SCHEDULE_MEET where id = {}", id);
        return Result.success();
    }

}
