package com.adc.da.smallprogram.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.util.List;
import java.util.Map;

import com.adc.da.login.util.UserUtils;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.util.exception.AdcDaBaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.adc.da.base.web.BaseController;
import com.adc.da.smallprogram.entity.ScheudlePermissionEO;
import com.adc.da.smallprogram.page.ScheudlePermissionEOPage;
import com.adc.da.smallprogram.service.ScheudlePermissionEOService;

import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.http.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;

@RestController
@RequestMapping("/${restPath}/smallprogram/scheudlePermission")
@Api(description = "小程序日程权限|ScheudlePermissionEO|")
public class ScheudlePermissionEOController extends BaseController<ScheudlePermissionEO>{

    private static final Logger logger = LoggerFactory.getLogger(ScheudlePermissionEOController.class);

    @Autowired
    private ScheudlePermissionEOService scheudlePermissionEOService;

    @ApiOperation(value = "|ScheudlePermissionEO|分页查询")
    @GetMapping("/page")
//    @RequiresPermissions("smallprogram:scheudlePermission:page")
    public ResponseMessage<PageInfo<ScheudlePermissionEO>> page(ScheudlePermissionEOPage page) throws Exception {
        List<ScheudlePermissionEO> rows = scheudlePermissionEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

    @ApiOperation(value = "|ScheudlePermissionEO|查询")
    @GetMapping("")
//    @RequiresPermissions("smallprogram:scheudlePermission:list")
    public ResponseMessage<List<ScheudlePermissionEO>> list(ScheudlePermissionEOPage page) throws Exception {
        return Result.success(scheudlePermissionEOService.queryByList(page));
    }

    @ApiOperation(value = "|ScheudlePermissionEO|详情")
    @GetMapping("/{id}")
//    @RequiresPermissions("smallprogram:scheudlePermission:get")
    public ResponseMessage<ScheudlePermissionEO> find(@PathVariable String id) throws Exception {
        return Result.success(scheudlePermissionEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|ScheudlePermissionEO|查询权限")
    @GetMapping("/checkPermission")
//    @RequiresPermissions("smallprogram:scheudlePermission:get")
    public ResponseMessage<Integer> checkPermission(String origin , String destUserId) throws Exception {
        return Result.success(scheudlePermissionEOService.selectByOriginIdAndDestUserIdLike(origin,destUserId));
    }

    @ApiOperation(value = "|ScheudlePermissionEO|查询代理列表")
    @GetMapping("/getAgentList")
//    @RequiresPermissions("smallprogram:scheudlePermission:get")
    public ResponseMessage<List<ScheudlePermissionEO>> getAgentList(ScheudlePermissionEOPage page) throws Exception {
        return Result.success(scheudlePermissionEOService.queryByList(page));
    }


    @ApiOperation(value = "|ScheudlePermissionEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("smallprogram:scheudlePermission:save")
    public ResponseMessage<ScheudlePermissionEO> create(@RequestBody ScheudlePermissionEO scheudlePermissionEO) throws Exception {
        scheudlePermissionEOService.insertSelective(scheudlePermissionEO);
        return Result.success(scheudlePermissionEO);
    }

    @ApiOperation(value = "|ScheudlePermissionEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("smallprogram:scheudlePermission:update")
    public ResponseMessage<ScheudlePermissionEO> update(@RequestBody ScheudlePermissionEO scheudlePermissionEO) throws Exception {
        scheudlePermissionEOService.updateByPrimaryKeySelective(scheudlePermissionEO);
        return Result.success(scheudlePermissionEO);
    }

    @ApiOperation(value = "|ScheudlePermissionEO|新增或修改")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE,value = "/createAndUpdate")
//    @RequiresPermissions("smallprogram:scheudlePermission:save")
    public ResponseMessage<ScheudlePermissionEO> createAndUpdate(@RequestBody ScheudlePermissionEO scheudlePermissionEO) throws Exception {
        scheudlePermissionEOService.createAndUpdate(scheudlePermissionEO);
        return Result.success(scheudlePermissionEO);
    }



    @ApiOperation(value = "|ScheudlePermissionEO|删除")
    @DeleteMapping("/{id}")
//    @RequiresPermissions("smallprogram:scheudlePermission:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        scheudlePermissionEOService.deleteByPrimaryKey(id);
        logger.info("delete from TS_SCHEUDLE_PERMISSION where id = {}", id);
        return Result.success();
    }


    @ApiOperation(value = "当前登录用户可修改的领导列表")
    @GetMapping(value = "/getMaintenanceUserList")
//    @RequiresPermissions("smallprogram:scheudlePermission:save")
    public ResponseMessage<List<UserEO>> getMaintenanceUserList() throws Exception {
        return Result.success(scheudlePermissionEOService.getMaintenanceUserList());
    }


}
