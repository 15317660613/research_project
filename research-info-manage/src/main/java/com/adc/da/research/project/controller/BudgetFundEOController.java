package com.adc.da.research.project.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.research.project.entity.BudgetFundEO;
import com.adc.da.research.project.page.BudgetFundEOPage;
import com.adc.da.research.project.service.BudgetFundEOService;
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
@RequestMapping("/${restPath}/research/project/budgetFund")
@Api(tags = "科研系统|科研项目预算|BudgetFundEOController")
public class BudgetFundEOController extends BaseController<BudgetFundEO> {

    private static final Logger logger = LoggerFactory.getLogger(BudgetFundEOController.class);

    @Autowired
    private BudgetFundEOService budgetFundEOService;

	@ApiOperation(value = "|BudgetFundEO|分页查询")
    @GetMapping("/page")
//    @RequiresPermissions("research.project:budgetFund:page")
    public ResponseMessage<PageInfo<BudgetFundEO>> page(BudgetFundEOPage page) throws Exception {
        List<BudgetFundEO> rows = budgetFundEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|BudgetFundEO|查询")
    @GetMapping("")
//    @RequiresPermissions("research.project:budgetFund:list")
    public ResponseMessage<List<Map<Object, Object>>> list(BudgetFundEOPage page) throws Exception {
        return Result.success(budgetFundEOService.queryByYear(page));
	}
    @ApiOperation(value = "|BudgetFundEO|根据年份查询列表")
    @GetMapping("/queryByBudgetFundYear")
//    @RequiresPermissions("research.project:budgetFund:list")
    public ResponseMessage<List<Map<String,Object>>> queryByBudgetFundYear(BudgetFundEOPage page) throws Exception {
        return Result.success(budgetFundEOService.queryByBudgetFundYear(page));
    }

    @ApiOperation(value = "|BudgetFundEO|详情")
    @GetMapping("/{id}")
//    @RequiresPermissions("research.project:budgetFund:get")
    public ResponseMessage<BudgetFundEO> find(@PathVariable String id) throws Exception {
        return Result.success(budgetFundEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|BudgetFundEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("research.project:budgetFund:save")
    public ResponseMessage<BudgetFundEO> create(@RequestBody BudgetFundEO budgetFundEO) throws Exception {
        budgetFundEOService.insertSelective(budgetFundEO);
        return Result.success(budgetFundEO);
    }


    @ApiOperation(value = "|BudgetFundEO|根据年份新增")
    @PostMapping(value = "/insertByYear",consumes = APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage<List<Map<String,Object>>> insertByYear(@RequestBody List<Map<String,Object>> listMap) throws Exception {
        budgetFundEOService.insertByYear(listMap);
        return Result.success(listMap);
    }


    @ApiOperation(value = "|BudgetFundEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("research.project:budgetFund:update")
    public ResponseMessage<BudgetFundEO> update(@RequestBody BudgetFundEO budgetFundEO) throws Exception {
        budgetFundEOService.updateByPrimaryKeySelective(budgetFundEO);
        return Result.success(budgetFundEO);
    }

    @ApiOperation(value = "|BudgetFundEO|删除")
    @DeleteMapping("/{id}")
//    @RequiresPermissions("research.project:budgetFund:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        budgetFundEOService.deleteByPrimaryKey(id);
        logger.info("delete from RS_BUDGET_FUND where id = {}", id);
        return Result.success();
    }


    @ApiOperation(value = "|BudgetFundEO|批量新增")
    @PostMapping("/batchInsertSelective")
    public ResponseMessage<BudgetFundEO> batchInsertSelective(@RequestBody List<BudgetFundEO> budgetFundEOS) throws Exception {
        budgetFundEOService.batchInsertSelective(budgetFundEOS);
        return Result.success();
    }

}
