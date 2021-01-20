package com.adc.da.dashboard.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.dashboard.entity.ProvinceAreaEO;
import com.adc.da.dashboard.page.ProvinceAreaEOPage;
import com.adc.da.dashboard.service.ProvinceAreaEOService;
import com.adc.da.util.http.PageInfo;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping("/${restPath}/dashboard/provinceArea")
@Api(description = "|ProvinceAreaEO|")
public class ProvinceAreaEOController extends BaseController<ProvinceAreaEO> {

    private static final Logger logger = LoggerFactory.getLogger(ProvinceAreaEOController.class);

    @Autowired
    private ProvinceAreaEOService provinceAreaEOService;

    @ApiOperation(value = "|ProvinceAreaEO|分页查询")
    @GetMapping("/page")
//    @RequiresPermissions("dashboard:provinceArea:page")
    public ResponseMessage<PageInfo<ProvinceAreaEO>> page(ProvinceAreaEOPage page) throws Exception {
        List<ProvinceAreaEO> rows = provinceAreaEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

    @ApiOperation(value = "|ProvinceAreaEO|查询")
    @GetMapping("")
//    @RequiresPermissions("dashboard:provinceArea:list")
    public ResponseMessage<List<ProvinceAreaEO>> list(ProvinceAreaEOPage page) throws Exception {
        return Result.success(provinceAreaEOService.queryByList(page));
    }

    @ApiOperation(value = "|ProvinceAreaEO|详情")
    @GetMapping("/{id}")
//    @RequiresPermissions("dashboard:provinceArea:get")
    public ResponseMessage<ProvinceAreaEO> find(@PathVariable Integer id) throws Exception {
        return Result.success(provinceAreaEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|ProvinceAreaEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("dashboard:provinceArea:save")
    public ResponseMessage<ProvinceAreaEO> create(@RequestBody ProvinceAreaEO provinceAreaEO) throws Exception {
        provinceAreaEOService.insertSelective(provinceAreaEO);
        return Result.success(provinceAreaEO);
    }

    @ApiOperation(value = "|ProvinceAreaEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("dashboard:provinceArea:update")
    public ResponseMessage<ProvinceAreaEO> update(@RequestBody ProvinceAreaEO provinceAreaEO) throws Exception {
        provinceAreaEOService.updateByPrimaryKeySelective(provinceAreaEO);
        return Result.success(provinceAreaEO);
    }

    @ApiOperation(value = "|ProvinceAreaEO|删除")
    @DeleteMapping("/{id}")
//    @RequiresPermissions("dashboard:provinceArea:delete")
    public ResponseMessage delete(@PathVariable Integer id) throws Exception {
        provinceAreaEOService.deleteByPrimaryKey(id);
        logger.info("delete from DB_PROVINCE_AREA where id = {}", id);
        return Result.success();
    }

}
