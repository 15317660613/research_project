package com.adc.da.epis.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.adc.da.base.web.BaseController;
import com.adc.da.epis.entity.BuinessCodeDetailInfoEO;
import com.adc.da.epis.page.BuinessCodeDetailInfoEOPage;
import com.adc.da.epis.service.BuinessCodeDetailInfoEOService;

import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.http.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;

@RestController
@RequestMapping("/${restPath}/common/buinessCodeDetailInfo")
@Api(description = "|BuinessCodeDetailInfoEO|")
public class BuinessCodeDetailInfoEOController extends BaseController<BuinessCodeDetailInfoEO>{

    private static final Logger logger = LoggerFactory.getLogger(BuinessCodeDetailInfoEOController.class);

    @Autowired
    private BuinessCodeDetailInfoEOService buinessCodeDetailInfoEOService;

	@ApiOperation(value = "|BuinessCodeDetailInfoEO|分页查询")
    @GetMapping("/page")
    @RequiresPermissions("epis:buinessCodeDetailInfo:page")
    public ResponseMessage<PageInfo<BuinessCodeDetailInfoEO>> page(BuinessCodeDetailInfoEOPage page) throws Exception {
        List<BuinessCodeDetailInfoEO> rows = buinessCodeDetailInfoEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|BuinessCodeDetailInfoEO|查询")
    @GetMapping("")
    @RequiresPermissions("epis:buinessCodeDetailInfo:list")
    public ResponseMessage<List<BuinessCodeDetailInfoEO>> list(BuinessCodeDetailInfoEOPage page) throws Exception {
        return Result.success(buinessCodeDetailInfoEOService.queryByList(page));
	}

    @ApiOperation(value = "|BuinessCodeDetailInfoEO|详情")
    @GetMapping("/{id}")
    @RequiresPermissions("epis:buinessCodeDetailInfo:get")
    public ResponseMessage<BuinessCodeDetailInfoEO> find(@PathVariable String id) throws Exception {
        return Result.success(buinessCodeDetailInfoEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|BuinessCodeDetailInfoEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("epis:buinessCodeDetailInfo:save")
    public ResponseMessage<BuinessCodeDetailInfoEO> create(@RequestBody BuinessCodeDetailInfoEO buinessCodeDetailInfoEO) throws Exception {
        buinessCodeDetailInfoEOService.insertSelective(buinessCodeDetailInfoEO);
        return Result.success(buinessCodeDetailInfoEO);
    }

    @ApiOperation(value = "|BuinessCodeDetailInfoEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("epis:buinessCodeDetailInfo:update")
    public ResponseMessage<BuinessCodeDetailInfoEO> update(@RequestBody BuinessCodeDetailInfoEO buinessCodeDetailInfoEO) throws Exception {
        buinessCodeDetailInfoEOService.updateByPrimaryKeySelective(buinessCodeDetailInfoEO);
        return Result.success(buinessCodeDetailInfoEO);
    }

    @ApiOperation(value = "|BuinessCodeDetailInfoEO|删除")
    @DeleteMapping("/{id}")
    @RequiresPermissions("epis:buinessCodeDetailInfo:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        buinessCodeDetailInfoEOService.deleteByPrimaryKey(id);
        logger.info("delete from BUINESS_CODE_DETAIL_INFO where id = {}", id);
        return Result.success();
    }

}
