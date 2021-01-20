package com.adc.da.research.config.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.research.config.entity.GradingRulesEO;
import com.adc.da.research.config.page.GradingRulesEOPage;
import com.adc.da.research.config.service.GradingRulesEOService;
import com.adc.da.util.http.PageInfo;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.validation.constraints.NotNull;
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
@RequestMapping("/${restPath}/research/gradingRules")
@Api(tags = "科研系统|评分细则信息|GradingRulesEOController")
public class GradingRulesEOController extends BaseController<GradingRulesEO> {

    private static final Logger logger = LoggerFactory.getLogger(GradingRulesEOController.class);

    @Autowired
    private GradingRulesEOService gradingRulesEOService;

    @ApiOperation(value = "|GradingRulesEO|分页查询")
    @GetMapping("/page")
//    @RequiresPermissions("research:gradingRules:page")
    public ResponseMessage<PageInfo<GradingRulesEO>> page(GradingRulesEOPage page) throws Exception {
        List<GradingRulesEO> rows = gradingRulesEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

    @ApiOperation(value = "|GradingRulesEO|查询")
    @GetMapping("")
//    @RequiresPermissions("research:gradingRules:list")
    public ResponseMessage<List<GradingRulesEO>> list(GradingRulesEOPage page) throws Exception {
        return Result.success(gradingRulesEOService.queryByList(page));
    }

    @ApiOperation(value = "|GradingRulesEO|详情")
    @GetMapping("/{id}")
//    @RequiresPermissions("research:gradingRules:get")
    public ResponseMessage<GradingRulesEO> find(@PathVariable String id) throws Exception {
        return Result.success(gradingRulesEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|GradingRulesEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("research:gradingRules:save")
    public ResponseMessage<GradingRulesEO> create(@RequestBody GradingRulesEO gradingRulesEO) throws Exception {
        gradingRulesEOService.insertSelective(gradingRulesEO);
        return Result.success(gradingRulesEO);
    }

    @ApiOperation(value = "|GradingRulesEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("research:gradingRules:update")
    public ResponseMessage<GradingRulesEO> update(@RequestBody GradingRulesEO gradingRulesEO) throws Exception {
        gradingRulesEOService.updateByPrimaryKeySelective(gradingRulesEO);
        return Result.success(gradingRulesEO);
    }

    @ApiOperation(value = "|GradingRulesEO|删除")
    @DeleteMapping("/{id}")
//    @RequiresPermissions("research:gradingRules:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        gradingRulesEOService.deleteByPrimaryKey(id);
        logger.info("delete from RS_GRADING_RULES where id = {}", id);
        return Result.success();
    }

    @ApiOperation(value = "|批量删除|")
    @DeleteMapping("/batchDeleteByIds/{ids}")
    public ResponseMessage batchDeleteByIds(@NotNull @PathVariable("ids") List<String> ids) throws Exception {
        gradingRulesEOService.batchDeleteByIds(ids);
        return Result.success();
    }

    @ApiOperation(value = "|GradingRulesEO|批量新增")
    @PostMapping("/batchInsertSelective")
    public ResponseMessage<GradingRulesEO> batchInsertSelective(@RequestBody List<GradingRulesEO> gradingRulesEOS) throws Exception {
        gradingRulesEOService.batchInsertSelective(gradingRulesEOS);
        return Result.success();
    }

}
