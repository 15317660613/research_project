package com.adc.da.research.project.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.research.project.entity.BudgetDetailHistoryEO;
import com.adc.da.research.project.page.BudgetDetailHistoryEOPage;
import com.adc.da.research.project.service.BudgetDetailHistoryEOService;
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
@RequestMapping("/${restPath}/project/budgetDetailHistory")
@Api(description = "|BudgetDetailHistoryEO|")
public class BudgetDetailHistoryEOController extends BaseController<BudgetDetailHistoryEO>{

    private static final Logger logger = LoggerFactory.getLogger(BudgetDetailHistoryEOController.class);

    @Autowired
    private BudgetDetailHistoryEOService budgetDetailHistoryEOService;

	@ApiOperation(value = "|BudgetDetailHistoryEO|分页查询")
    @GetMapping("/page")
    public ResponseMessage<PageInfo<BudgetDetailHistoryEO>> page(BudgetDetailHistoryEOPage page) throws Exception {
        List<BudgetDetailHistoryEO> rows = budgetDetailHistoryEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|BudgetDetailHistoryEO|查询")
    @GetMapping("")
    public ResponseMessage<List<BudgetDetailHistoryEO>> list(BudgetDetailHistoryEOPage page) throws Exception {
        return Result.success(budgetDetailHistoryEOService.queryByList(page));
	}

    @ApiOperation(value = "|BudgetDetailHistoryEO|详情")
    @GetMapping("/{id}")
    public ResponseMessage<BudgetDetailHistoryEO> find(@PathVariable String id) throws Exception {
        return Result.success(budgetDetailHistoryEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|BudgetDetailHistoryEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage<BudgetDetailHistoryEO> create(@RequestBody BudgetDetailHistoryEO budgetDetailHistoryEO) throws Exception {
        budgetDetailHistoryEOService.insertSelective(budgetDetailHistoryEO);
        return Result.success(budgetDetailHistoryEO);
    }

    @ApiOperation(value = "|BudgetDetailHistoryEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage<BudgetDetailHistoryEO> update(@RequestBody BudgetDetailHistoryEO budgetDetailHistoryEO) throws Exception {
        budgetDetailHistoryEOService.updateByPrimaryKeySelective(budgetDetailHistoryEO);
        return Result.success(budgetDetailHistoryEO);
    }

    @ApiOperation(value = "|BudgetDetailHistoryEO|删除")
    @DeleteMapping("/{id}")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        budgetDetailHistoryEOService.deleteByPrimaryKey(id);
        logger.info("delete from RS_BUDGET_DETAIL_HISTORY where id = {}", id);
        return Result.success();
    }

    @ApiOperation(value = "|BudgetDetailHistoryEO|新增年份明细")
    @PostMapping(value = "/insertByYear",consumes = APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage<Map<String, List<Map<String,Object>>>> insertByYear(@RequestBody Map<String, List<Map<String,Object>>> listMap) throws Exception {

        return Result.success(budgetDetailHistoryEOService.insertByYear(listMap));

    }

    @ApiOperation(value = "|BudgetDetailHistoryEO|分类查询")
    @GetMapping("/queryByArr")
    public ResponseMessage< Map<String, List<Map<String,Object>>>> queryByArr(BudgetDetailHistoryEOPage page) throws Exception {

        Map<String, List<Map<String,Object>>> rows = budgetDetailHistoryEOService.queryByArr(page);
        return Result.success(rows);
    }




}
