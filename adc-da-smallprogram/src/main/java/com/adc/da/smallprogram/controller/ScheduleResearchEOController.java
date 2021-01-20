package com.adc.da.smallprogram.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.util.List;

import com.adc.da.base.page.Pager;
import com.adc.da.smallprogram.entity.ScheduleResearchEO;
import com.adc.da.smallprogram.page.MyScheduleResearchEOPage;
import com.adc.da.smallprogram.page.ScheduleResearchEOPage;
import com.adc.da.smallprogram.service.ScheduleResearchEOService;
import com.adc.da.smallprogram.vo.ResearchVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.adc.da.base.web.BaseController;

import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.http.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/${restPath}/smallprogram/scheduleResearch")
@Api(tags = "小程序科委会主题|ScheduleResearchEO|")
public class ScheduleResearchEOController extends BaseController<ScheduleResearchEO>{

    private static final Logger logger = LoggerFactory.getLogger(ScheduleResearchEOController.class);

    @Autowired
    private ScheduleResearchEOService scheduleResearchEOService;

	@ApiOperation(value = "|ScheduleResearchEO|分页查询")
    @GetMapping("/page")
    //@RequiresPermissions("smallprogram:scheduleResearch:page")
    public ResponseMessage<PageInfo<ScheduleResearchEO>> page(ScheduleResearchEOPage page) throws Exception {
        List<ScheduleResearchEO> rows = scheduleResearchEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

    @ApiOperation(value = "|ScheduleResearchEO|分页查询")
    @GetMapping("/getResearchVOByPage")
    //@RequiresPermissions("smallprogram:scheduleResearch:page")
    public ResponseMessage<PageInfo<ResearchVO>> getResearchVOByPage(MyScheduleResearchEOPage page) throws Exception {
        List<ResearchVO> rows = scheduleResearchEOService.getResearchVOByPage(page);
        return Result.success(myGetPageInfo(page.getPager(), rows));
    }

    public PageInfo<ResearchVO> myGetPageInfo(Pager pager, List<ResearchVO> rows) {
        PageInfo<ResearchVO> pageInfo = new PageInfo();
        pageInfo.setList(rows);
        pageInfo.setCount((long)pager.getRowCount());
        pageInfo.setPageSize(pager.getPageSize());
        pageInfo.setPageCount((long)pager.getPageCount());
        pageInfo.setPageNo(pager.getPageId());
        return pageInfo;
    }

	@ApiOperation(value = "|ScheduleResearchEO|查询")
    @GetMapping("")
    //@RequiresPermissions("smallprogram:scheduleResearch:list")
    public ResponseMessage<List<ScheduleResearchEO>> list(ScheduleResearchEOPage page) throws Exception {
        return Result.success(scheduleResearchEOService.queryByList(page));
	}

    @ApiOperation(value = "|ScheduleResearchEO|详情")
    @GetMapping("/{id}")
    //@RequiresPermissions("smallprogram:scheduleResearch:get")
    public ResponseMessage<ScheduleResearchEO> find(@PathVariable String id) throws Exception {
        return Result.success(scheduleResearchEOService.selectByPrimaryKey(id));
    }


    @ApiOperation(value = "|ScheduleResearchEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    //@RequiresPermissions("smallprogram:scheduleResearch:save")
    public ResponseMessage<ScheduleResearchEO> create(@RequestBody ScheduleResearchEO scheduleResearchEO) throws Exception {
        scheduleResearchEOService.insertSelective(scheduleResearchEO);
        return Result.success(scheduleResearchEO);
    }

    @ApiOperation(value = "|ScheduleResearchEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    //@RequiresPermissions("smallprogram:scheduleResearch:update")
    public ResponseMessage<ScheduleResearchEO> update(@RequestBody ScheduleResearchEO scheduleResearchEO) throws Exception {
        scheduleResearchEOService.updateByPrimaryKeySelective(scheduleResearchEO);
        return Result.success(scheduleResearchEO);
    }

    @ApiOperation(value = "|ScheduleResearchEO|删除")
    @DeleteMapping("/{id}")
    //@RequiresPermissions("smallprogram:scheduleResearch:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        scheduleResearchEOService.deleteByPrimaryKey(id);
        logger.info("delete from TS_SCHEDULE_RESEARCH where id = {}", id);
        return Result.success();
    }

}
