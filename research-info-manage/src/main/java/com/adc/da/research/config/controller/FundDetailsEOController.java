package com.adc.da.research.config.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.research.config.entity.FundDetailsEO;
import com.adc.da.research.config.page.FundDetailsEOPage;
import com.adc.da.research.config.service.FundDetailsEOService;
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
@RequestMapping("/${restPath}/config/fundDetails")
@Api(tags = "科目管理|FundDetailsEOController")
public class FundDetailsEOController extends BaseController<FundDetailsEO> {

    private static final Logger logger = LoggerFactory.getLogger(FundDetailsEOController.class);

    @Autowired
    private FundDetailsEOService fundDetailsEOService;

    @ApiOperation(value = "|FundDetailsEO|分页查询")
    @GetMapping("/page")
//    @RequiresPermissions("config:fundDetails:page")
    public ResponseMessage<PageInfo<FundDetailsEO>> page(FundDetailsEOPage page) throws Exception {
        List<FundDetailsEO> rows = fundDetailsEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

    @ApiOperation(value = "|FundDetailsEO|查询")
    @GetMapping("")
//    @RequiresPermissions("config:fundDetails:list")
    public ResponseMessage<List<FundDetailsEO>> list(FundDetailsEOPage page) throws Exception {
        return Result.success(fundDetailsEOService.queryByList(page));
    }

    @ApiOperation(value = "|FundDetailsEO|详情")
    @GetMapping("/{id}")
//    @RequiresPermissions("config:fundDetails:get")
    public ResponseMessage<FundDetailsEO> find(@PathVariable String id) throws Exception {
        return Result.success(fundDetailsEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|FundDetailsEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("config:fundDetails:save")
    public ResponseMessage<FundDetailsEO> create(@RequestBody FundDetailsEO fundDetailsEO) throws Exception {
        fundDetailsEOService.insertSelective(fundDetailsEO);
        return Result.success(fundDetailsEO);
    }

    @ApiOperation(value = "|FundDetailsEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("config:fundDetails:update")
    public ResponseMessage<FundDetailsEO> update(@RequestBody FundDetailsEO fundDetailsEO) throws Exception {
        fundDetailsEOService.updateByPrimaryKeySelective(fundDetailsEO);
        return Result.success(fundDetailsEO);
    }

    @ApiOperation(value = "|FundDetailsEO|删除")
    @DeleteMapping("/{id}")
//    @RequiresPermissions("config:fundDetails:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        fundDetailsEOService.deleteByPrimaryKey(id);
        logger.info("delete from RS_FUND_DETAILS where id = {}", id);
        return Result.success();
    }

}
