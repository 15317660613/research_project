package com.adc.da.smallprogram.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.util.Date;
import java.util.List;

import com.adc.da.smallprogram.entity.ScheduleMeetEO;
import com.adc.da.smallprogram.entity.ScheduleSupportUserEO;
import com.adc.da.smallprogram.page.MeetPage;
import com.adc.da.smallprogram.page.SupportPage;
import com.adc.da.smallprogram.service.ScheduleSupportUserEOService;
import com.adc.da.smallprogram.vo.MeetVO;
import com.adc.da.smallprogram.vo.SupportVO;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.adc.da.base.web.BaseController;
import com.adc.da.smallprogram.entity.ScheduleSupportEO;
import com.adc.da.smallprogram.page.ScheduleSupportEOPage;
import com.adc.da.smallprogram.service.ScheduleSupportEOService;

import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.http.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/${restPath}/smallprogram/scheduleSupport")
@Api(description = "|ScheduleSupportEO|")
public class ScheduleSupportEOController extends BaseController<ScheduleSupportEO>{

    private static final Logger logger = LoggerFactory.getLogger(ScheduleSupportEOController.class);

    @Autowired
    private ScheduleSupportEOService scheduleSupportEOService;
    @Autowired
    private ScheduleSupportUserEOService scheduleSupportUserEOService ;

	@ApiOperation(value = "|ScheduleSupportEO|分页查询")
    @GetMapping("/page")
//    @RequiresPermissions("smallprogram:scheduleSupport:page")
    public ResponseMessage<PageInfo<ScheduleSupportEO>> page(ScheduleSupportEOPage page) throws Exception {
        List<ScheduleSupportEO> rows = scheduleSupportEOService.queryByPageNew(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|ScheduleSupportEO|查询")
    @GetMapping("")
//    @RequiresPermissions("smallprogram:scheduleSupport:list")
    public ResponseMessage<List<ScheduleSupportEO>> list(ScheduleSupportEOPage page) throws Exception {
        return Result.success(scheduleSupportEOService.queryByList(page));
	}

    @ApiOperation(value = "|ScheduleSupportEO|详情")
    @GetMapping("/{id}")
//    @RequiresPermissions("smallprogram:scheduleSupport:get")
    public ResponseMessage<ScheduleSupportEO> find(@PathVariable String id) throws Exception {
        return Result.success(scheduleSupportEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|ScheduleSupportEO|详情")
    @GetMapping("/selectBySupportIdAndReceiveUserId/{supportId}/{receiveUserId}")
//    @RequiresPermissions("smallprogram:scheduleSupport:get")
    public ResponseMessage<ScheduleSupportEO> selectBySupportIdAndReceiveUserId(@PathVariable("supportId") String supportId,
            @PathVariable("receiveUserId") String receiveUserId) throws Exception {
        return Result.success(scheduleSupportEOService.selectBySupportIdAndReceiveUserId(supportId,receiveUserId));
    }


    @ApiOperation(value = "|ScheduleSupportEO|详情")
    @GetMapping("/getSupportVO/{id}")
//    @RequiresPermissions("smallprogram:scheduleSupport:get")
    public ResponseMessage<SupportVO> getSupportVO(@PathVariable String id) throws Exception {
        SupportVO supportVO = new SupportVO() ;
        ScheduleSupportEO scheduleSupportEO = scheduleSupportEOService.selectByPrimaryKey(id) ;
        List<ScheduleSupportUserEO>  scheduleSupportUserEOList = scheduleSupportUserEOService.selectBySupportId(id);
        if (CollectionUtils.isNotEmpty(scheduleSupportUserEOList)){
            scheduleSupportEO.setStatus(scheduleSupportUserEOList.get(0).getStatus());
        }

        supportVO.setScheduleSupportEO(scheduleSupportEO);
        supportVO.setScheduleSupportUserEOList(scheduleSupportUserEOList);
        return Result.success(supportVO);
    }

    @ApiOperation(value = "|ScheduleSupportEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("smallprogram:scheduleSupport:save")
    public ResponseMessage<ScheduleSupportEO> create(@RequestBody ScheduleSupportEO scheduleSupportEO) throws Exception {
        scheduleSupportEOService.save(scheduleSupportEO);
        return Result.success(scheduleSupportEO);
    }
    @ApiOperation(value = "|MeetVO|新增")
    @PostMapping("/saveSupportVO")
//    @RequiresPermissions("smallprogram:scheduleMeet:save")
    public ResponseMessage saveSupportVO(@RequestBody SupportVO supportVO) throws Exception {
        scheduleSupportEOService.saveSupportVO(supportVO);
        return Result.success();
    }

    @ApiOperation(value = "|MeetVO|发送")
    @PostMapping("/send")
//    @RequiresPermissions("smallprogram:scheduleMeet:save")
    public ResponseMessage send(@RequestBody SupportVO supportVO) throws Exception {
        scheduleSupportEOService.send(supportVO);
        return Result.success();
    }


    @ApiOperation(value = "|ScheduleMeetEO|分页查询根据userId")
    @GetMapping("/selectPageByReceiveUserId")
//    @RequiresPermissions("smallprogram:scheduleMeet:page")
    public ResponseMessage<PageInfo<ScheduleSupportEO>> selectPageByReceiveUserId(SupportPage page) throws Exception {
        List<ScheduleSupportEO> rows = scheduleSupportEOService.queryByPageWithReceiveUserId(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

    @ApiOperation(value = "|ScheduleMeetEO|查询全部")
    @GetMapping("/queryByPageAll")
//    @RequiresPermissions("smallprogram:scheduleMeet:page")
    public ResponseMessage<PageInfo<ScheduleSupportEO>> queryByPageAll(SupportPage page) throws Exception {
        List<ScheduleSupportEO> rows = scheduleSupportEOService.queryByPageAll(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

    @ApiOperation(value = "|ScheduleSupportEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("smallprogram:scheduleSupport:update")
    public ResponseMessage<ScheduleSupportEO> update(@RequestBody ScheduleSupportEO scheduleSupportEO) throws Exception {
	    if (null!=scheduleSupportEO.getStatus()&&scheduleSupportEO.getStatus()!=0){
	        scheduleSupportEO.setFinishedTime(new Date());
        }
        scheduleSupportEOService.updateByPrimaryKeySelective(scheduleSupportEO);
        return Result.success(scheduleSupportEO);
    }

    @ApiOperation(value = "|ScheduleSupportEO|删除")
    @DeleteMapping("/{id}")
//    @RequiresPermissions("smallprogram:scheduleSupport:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        scheduleSupportEOService.logicDelete(id);
        logger.info("delete from TS_SCHEDULE_SUPPORT where id = {}", id);
        return Result.success();
    }
}
