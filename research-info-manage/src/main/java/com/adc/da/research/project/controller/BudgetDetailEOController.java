package com.adc.da.research.project.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.research.project.entity.BudgetDetailEO;
import com.adc.da.research.project.page.BudgetDetailEOPage;
import com.adc.da.research.project.service.BudgetDetailEOService;
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
@RequestMapping("/${restPath}/research/project/budgetDetail")
@Api(tags = "科研系统|科研项目预算明细|BudgetDetailEOController")
public class BudgetDetailEOController extends BaseController<BudgetDetailEO> {

    private static final Logger logger = LoggerFactory.getLogger(BudgetDetailEOController.class);

    @Autowired
    private BudgetDetailEOService budgetDetailEOService;

	@ApiOperation(value = "|BudgetDetailEO|分页查询")
    @GetMapping("/page")
//    @RequiresPermissions("research.project:budgetDetail:page")
    public ResponseMessage<PageInfo<BudgetDetailEO>> page(BudgetDetailEOPage page) throws Exception {
        List<BudgetDetailEO> rows = budgetDetailEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }
    @ApiOperation(value = "|BudgetDetailEO|分类查询")
    @GetMapping("/queryByArr")
//    @RequiresPermissions("research.project:budgetDetail:page")
    public ResponseMessage< Map<String, List<Map<String,Object>>>> queryByArr(BudgetDetailEOPage page) throws Exception {

        Map<String, List<Map<String,Object>>> rows = budgetDetailEOService.queryByArr(page);
        return Result.success(rows);
    }
	@ApiOperation(value = "|BudgetDetailEO|查询")
    @GetMapping("")
//    @RequiresPermissions("research.project:budgetDetail:list")
    public ResponseMessage<List<BudgetDetailEO>> list(BudgetDetailEOPage page) throws Exception {
        return Result.success(budgetDetailEOService.queryByList(page));
	}

    @ApiOperation(value = "|BudgetDetailEO|详情")
    @GetMapping("/{id}")
//    @RequiresPermissions("research.project:budgetDetail:get")
    public ResponseMessage<BudgetDetailEO> find(@PathVariable String id) throws Exception {
        return Result.success(budgetDetailEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|BudgetDetailEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("research.project:budgetDetail:save")
    public ResponseMessage<BudgetDetailEO> create(@RequestBody BudgetDetailEO budgetDetailEO) throws Exception {
        budgetDetailEOService.saveBudgetDetail(budgetDetailEO);
        return Result.success(budgetDetailEO);
    }

    @ApiOperation(value = "|BudgetDetailEO|新增")
    @PostMapping(value = "/batchSaveBudgetDetailEO",consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("research.project:budgetDetail:save")
    public ResponseMessage<List<BudgetDetailEO>> batchSaveBudgetDetailEO(@RequestBody List<BudgetDetailEO> budgetDetailEOList) throws Exception {
        List<BudgetDetailEO> budgetDetailEOS = budgetDetailEOService.batchSaveBudgetDetailEO(budgetDetailEOList);
        return Result.success(budgetDetailEOS);
    }

    @ApiOperation(value = "|BudgetDetailEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("research.project:budgetDetail:update")
    public ResponseMessage<BudgetDetailEO> update(@RequestBody BudgetDetailEO budgetDetailEO) throws Exception {
        budgetDetailEOService.updateByPrimaryKeySelective(budgetDetailEO);
        return Result.success(budgetDetailEO);
    }

    @ApiOperation(value = "|BudgetDetailEO|删除")
    @DeleteMapping("/{id}")
//    @RequiresPermissions("research.project:budgetDetail:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        budgetDetailEOService.deleteByPrimaryKey(id);
        logger.info("delete from RS_BUDGET_DETAIL where id = {}", id);
        return Result.success();
    }

    @ApiOperation(value = "|BudgetDetailEO|新增年份明细")
    @PostMapping(value = "/insertByYear",consumes = APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage<Map<String, List<Map<String,Object>>>> insertByYear(@RequestBody Map<String, List<Map<String,Object>>> listMap) throws Exception {

	    return Result.success(budgetDetailEOService.insertByYear(listMap));

    }

}
