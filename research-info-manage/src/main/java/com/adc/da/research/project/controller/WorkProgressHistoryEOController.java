package com.adc.da.research.project.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.research.project.entity.WorkProgressHistoryEO;
import com.adc.da.research.project.page.WorkProgressHistoryEOPage;
import com.adc.da.research.project.service.WorkProgressHistoryEOService;
import com.adc.da.util.http.PageInfo;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping("/${restPath}/project/workProgressHistory")
@Api(description = "|WorkProgressHistoryEO|")
public class WorkProgressHistoryEOController extends BaseController<WorkProgressHistoryEO>{

    private static final Logger logger = LoggerFactory.getLogger(WorkProgressHistoryEOController.class);

    @Autowired
    private WorkProgressHistoryEOService workProgressHistoryEOService;

	@ApiOperation(value = "|WorkProgressHistoryEO|分页查询")
    @GetMapping("/page")
    public ResponseMessage<PageInfo<WorkProgressHistoryEO>> page(WorkProgressHistoryEOPage page) throws Exception {
        List<WorkProgressHistoryEO> rows = workProgressHistoryEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|WorkProgressHistoryEO|查询")
    @GetMapping("")
    public ResponseMessage<List<WorkProgressHistoryEO>> list(WorkProgressHistoryEOPage page) throws Exception {
        return Result.success(workProgressHistoryEOService.queryByList(page));
	}

    @ApiOperation(value = "|WorkProgressHistoryEO|详情")
    @GetMapping("/{id}")
    public ResponseMessage<WorkProgressHistoryEO> find(@PathVariable String id) throws Exception {
        return Result.success(workProgressHistoryEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|WorkProgressHistoryEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage<WorkProgressHistoryEO> create(@RequestBody WorkProgressHistoryEO workProgressHistoryEO) throws Exception {
        workProgressHistoryEOService.insertSelective(workProgressHistoryEO);
        return Result.success(workProgressHistoryEO);
    }

    @ApiOperation(value = "|WorkProgressHistoryEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage<WorkProgressHistoryEO> update(@RequestBody WorkProgressHistoryEO workProgressHistoryEO) throws Exception {
        workProgressHistoryEOService.updateByPrimaryKeySelective(workProgressHistoryEO);
        return Result.success(workProgressHistoryEO);
    }

    @ApiOperation(value = "|WorkProgressHistoryEO|删除")
    @DeleteMapping("/{id}")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        workProgressHistoryEOService.deleteByPrimaryKey(id);
        logger.info("delete from RS_WORK_PROGRESS_HISTORY where id = {}", id);
        return Result.success();
    }

    @ApiOperation(value = "|WorkProgressHistoryEO|批量新增")
    @PostMapping("/batchInsertSelective")
    public ResponseMessage batchInsertSelective(@RequestBody List<WorkProgressHistoryEO> workProgressEOS) throws Exception {
        workProgressHistoryEOService.batchInsertSelective(workProgressEOS);
        return Result.success();
    }
    @ApiOperation(value = "|ADC项目合同书考核指标")
    @GetMapping("/getCheckTargetList")
    public ResponseMessage<Map<String,List<WorkProgressHistoryEO>>> getCheckTargetList(WorkProgressHistoryEOPage page) throws Exception {
        return Result.success(workProgressHistoryEOService.getCheckTargetList(page));
    }


}
