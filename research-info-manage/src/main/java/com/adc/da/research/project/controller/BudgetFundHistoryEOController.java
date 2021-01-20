package com.adc.da.research.project.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.research.project.entity.BudgetFundHistoryEO;
import com.adc.da.research.project.page.BudgetFundHistoryEOPage;
import com.adc.da.research.project.service.BudgetFundHistoryEOService;
import com.adc.da.research.project.vo.BudgetFundVO;
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
@RequestMapping("/${restPath}/project/budgetFundHistory")
@Api(description = "|BudgetFundHistoryEO|")
public class BudgetFundHistoryEOController extends BaseController<BudgetFundHistoryEO>{

    private static final Logger logger = LoggerFactory.getLogger(BudgetFundHistoryEOController.class);

    @Autowired
    private BudgetFundHistoryEOService budgetFundHistoryEOService;

	@ApiOperation(value = "|BudgetFundHistoryEO|分页查询")
    @GetMapping("/page")
    public ResponseMessage<PageInfo<BudgetFundHistoryEO>> page(BudgetFundHistoryEOPage page) throws Exception {
        List<BudgetFundHistoryEO> rows = budgetFundHistoryEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|BudgetFundHistoryEO|查询")
    @GetMapping("")
    public ResponseMessage<List<BudgetFundHistoryEO>> list(BudgetFundHistoryEOPage page) throws Exception {
        return Result.success(budgetFundHistoryEOService.queryByList(page));
	}

    @ApiOperation(value = "|BudgetFundHistoryEO|详情")
    @GetMapping("/{id}")
    public ResponseMessage<BudgetFundHistoryEO> find(@PathVariable String id) throws Exception {
        return Result.success(budgetFundHistoryEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|BudgetFundHistoryEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage<BudgetFundHistoryEO> create(@RequestBody BudgetFundHistoryEO budgetFundHistoryEO) throws Exception {
        budgetFundHistoryEOService.insertSelective(budgetFundHistoryEO);
        return Result.success(budgetFundHistoryEO);
    }

    @ApiOperation(value = "|BudgetFundHistoryEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage<BudgetFundHistoryEO> update(@RequestBody BudgetFundHistoryEO budgetFundHistoryEO) throws Exception {
        budgetFundHistoryEOService.updateByPrimaryKeySelective(budgetFundHistoryEO);
        return Result.success(budgetFundHistoryEO);
    }

    @ApiOperation(value = "|BudgetFundHistoryEO|删除")
    @DeleteMapping("/{id}")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        budgetFundHistoryEOService.deleteByPrimaryKey(id);
        logger.info("delete from RS_BUDGET_FUND_HISTORY where id = {}", id);
        return Result.success();
    }

    @ApiOperation(value = "|BudgetFundHistoryEO|根据年份新增")
    @PostMapping(value = "/insertByYear",consumes = APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage<List<Map<String,Object>>> insertByYear(@RequestBody List<Map<String,Object>> listMap) throws Exception {
        budgetFundHistoryEOService.insertByYear(listMap);
        return Result.success(listMap);
    }
    @ApiOperation(value = "|BudgetFundHistoryEO|根据年份查询列表")
    @GetMapping("/queryByBudgetFundYear")
    public ResponseMessage<List<Map<String,Object>>> queryByBudgetFundYear(BudgetFundHistoryEOPage page) throws Exception {
        return Result.success(budgetFundHistoryEOService.queryByBudgetFundYear(page));
    }

    @ApiOperation(value = "|BudgetDetailHistoryEO|分类查询")
    @GetMapping("/queryByChange")
    public ResponseMessage< List<BudgetFundVO>> queryByChange(BudgetFundHistoryEOPage page) throws Exception {

        List<BudgetFundVO> rows = budgetFundHistoryEOService.queryChange(page);
        return Result.success(rows);
    }

}
