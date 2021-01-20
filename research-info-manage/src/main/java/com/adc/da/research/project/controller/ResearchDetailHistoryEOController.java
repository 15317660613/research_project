package com.adc.da.research.project.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.research.project.entity.ResearchDetailHistoryEO;
import com.adc.da.research.project.page.ResearchDetailHistoryEOPage;
import com.adc.da.research.project.service.ResearchDetailHistoryEOService;
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
@RequestMapping("/${restPath}/project/researchBudgetDetailHistory")
@Api(description = "|ResearchDetailHistoryEO|")
public class ResearchDetailHistoryEOController extends BaseController<ResearchDetailHistoryEO>{

    private static final Logger logger = LoggerFactory.getLogger(ResearchDetailHistoryEOController.class);

    @Autowired
    private ResearchDetailHistoryEOService researchDetailHistoryEOService;

    @ApiOperation(value = "|ResearchDetailHistoryEO|分页查询")
    @GetMapping("/page")
    public ResponseMessage<PageInfo<ResearchDetailHistoryEO>> page(ResearchDetailHistoryEOPage page) throws Exception {
        List<ResearchDetailHistoryEO> rows = researchDetailHistoryEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

    @ApiOperation(value = "|ResearchDetailHistoryEO|查询")
    @GetMapping("")
    public ResponseMessage<List<ResearchDetailHistoryEO>> list(ResearchDetailHistoryEOPage page) throws Exception {
        return Result.success(researchDetailHistoryEOService.queryByList(page));
    }

    @ApiOperation(value = "|ResearchDetailHistoryEO|详情")
    @GetMapping("/{id}")
    public ResponseMessage<ResearchDetailHistoryEO> find(@PathVariable String id) throws Exception {
        return Result.success(researchDetailHistoryEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|ResearchDetailHistoryEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage<ResearchDetailHistoryEO> create(@RequestBody ResearchDetailHistoryEO researchDetailHistoryEO) throws Exception {
        researchDetailHistoryEOService.insertSelective(researchDetailHistoryEO);
        return Result.success(researchDetailHistoryEO);
    }

    @ApiOperation(value = "|ResearchDetailHistoryEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage<ResearchDetailHistoryEO> update(@RequestBody ResearchDetailHistoryEO researchDetailHistoryEO) throws Exception {
        researchDetailHistoryEOService.updateByPrimaryKeySelective(researchDetailHistoryEO);
        return Result.success(researchDetailHistoryEO);
    }

    @ApiOperation(value = "|ResearchDetailHistoryEO|删除")
    @DeleteMapping("/{id}")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        researchDetailHistoryEOService.deleteByPrimaryKey(id);
        logger.info("delete from RS_RESEARCH_DETAIL_HISTORY where id = {}", id);
        return Result.success();
    }
    @ApiOperation(value = "|ResearchDetailHistoryEO|批量新增")
    @PostMapping("/batchInsertSelective")
    public ResponseMessage<ResearchDetailHistoryEO> batchInsertSelective(@RequestBody List<ResearchDetailHistoryEO> researchBudgetDetailEOS) throws Exception {
        researchDetailHistoryEOService.batchInsertSelective(researchBudgetDetailEOS);
        return Result.success();
    }

}
