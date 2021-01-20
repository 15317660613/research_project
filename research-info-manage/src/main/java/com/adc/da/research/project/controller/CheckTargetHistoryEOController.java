package com.adc.da.research.project.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.research.project.entity.CheckTargetHistoryEO;
import com.adc.da.research.project.page.CheckTargetHistoryEOPage;
import com.adc.da.research.project.service.CheckTargetHistoryEOService;
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

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping("/${restPath}/project/checkTargetHistory")
@Api(description = "|CheckTargetHistoryEO|")
public class CheckTargetHistoryEOController extends BaseController<CheckTargetHistoryEO>{

    private static final Logger logger = LoggerFactory.getLogger(CheckTargetHistoryEOController.class);

    @Autowired
    private CheckTargetHistoryEOService checkTargetHistoryEOService;

	@ApiOperation(value = "|CheckTargetHistoryEO|分页查询")
    @GetMapping("/page")
    public ResponseMessage<PageInfo<CheckTargetHistoryEO>> page(CheckTargetHistoryEOPage page) throws Exception {
        List<CheckTargetHistoryEO> rows = checkTargetHistoryEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|CheckTargetHistoryEO|查询")
    @GetMapping("")
    public ResponseMessage<List<CheckTargetHistoryEO>> list(CheckTargetHistoryEOPage page) throws Exception {
        return Result.success(checkTargetHistoryEOService.queryByList(page));
	}

    @ApiOperation(value = "|CheckTargetHistoryEO|详情")
    @GetMapping("/{id}")
    public ResponseMessage<CheckTargetHistoryEO> find(@PathVariable String id) throws Exception {
        return Result.success(checkTargetHistoryEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|CheckTargetHistoryEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage<CheckTargetHistoryEO> create(@RequestBody CheckTargetHistoryEO checkTargetHistoryEO) throws Exception {
        checkTargetHistoryEOService.insertSelective(checkTargetHistoryEO);
        return Result.success(checkTargetHistoryEO);
    }

    @ApiOperation(value = "|CheckTargetHistoryEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage<CheckTargetHistoryEO> update(@RequestBody CheckTargetHistoryEO checkTargetHistoryEO) throws Exception {
        checkTargetHistoryEOService.updateByPrimaryKeySelective(checkTargetHistoryEO);
        return Result.success(checkTargetHistoryEO);
    }

    @ApiOperation(value = "|CheckTargetHistoryEO|删除")
    @DeleteMapping("/{id}")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        checkTargetHistoryEOService.deleteByPrimaryKey(id);
        logger.info("delete from RS_CHECK_TARGET_HISTORY where id = {}", id);
        return Result.success();
    }
    @ApiOperation(value = "|CheckTargetEO|批量新增")
    @PostMapping("/batchInsertSelective")
    public ResponseMessage batchInsertSelective(@RequestBody List<CheckTargetHistoryEO> checkTargetEOS) throws Exception {
        checkTargetHistoryEOService.batchInsertSelective(checkTargetEOS);
        return Result.success();
    }

}
