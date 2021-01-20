package com.adc.da.group.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.group.entity.CustomGroupEO;
import com.adc.da.group.page.CustomGroupEOPage;
import com.adc.da.group.service.CustomGroupEOService;
import com.adc.da.login.util.UserUtils;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.http.PageInfo;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.utils.StringUtils;
import com.adc.da.util.utils.UUID;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/${restPath}/group/customGroup")
@Api(tags = "|自定义组|")
@Slf4j
public class CustomGroupEOController extends BaseController<CustomGroupEO> {

    @Autowired
    private CustomGroupEOService customGroupEOService;

    @ApiOperation(value = "|CustomGroupEO|分页查询")
    @GetMapping("/page")
//    @RequiresPermissions("group:customGroup:page")
    public ResponseMessage<PageInfo<CustomGroupEO>> page(CustomGroupEOPage page) throws Exception {
        List<CustomGroupEO> rows = customGroupEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

    @ApiOperation(value = "|CustomGroupEO|查询")
    @GetMapping("")
//    @RequiresPermissions("group:customGroup:list")
    public ResponseMessage<List<CustomGroupEO>> list(CustomGroupEOPage page) throws Exception {
        return Result.success(customGroupEOService.queryByList(page));
    }

    @ApiOperation(value = "|CustomGroupEO|查询当前用户下所有的组")
    @GetMapping("/getLoginUserGroup")
//    @RequiresPermissions("group:userCustom:list")
    public ResponseMessage<List<CustomGroupEO>> getLoginUserGroup() {
        String loginUserId = UserUtils.getUserId();
        if (StringUtils.isEmpty(loginUserId)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        return Result.success(customGroupEOService.queryByCreateUserId(loginUserId));
    }

    @ApiOperation(value = "|CustomGroupEO|详情")
    @GetMapping("/{id}")
//    @RequiresPermissions("group:customGroup:get")
    public ResponseMessage<CustomGroupEO> find(@PathVariable String id) throws Exception {
        return Result.success(customGroupEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|CustomGroupEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("group:customGroup:save")
    public ResponseMessage<CustomGroupEO> create(@RequestBody CustomGroupEO customGroupEO) throws Exception {
        customGroupEOService.insertSelective(customGroupEO);
        customGroupEO.setId(UUID.randomUUID10());
        return Result.success(customGroupEO);
    }

    @ApiOperation(value = "|CustomGroupEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("group:customGroup:update")
    public ResponseMessage<CustomGroupEO> update(@RequestBody CustomGroupEO customGroupEO) throws Exception {
        customGroupEOService.updateByPrimaryKeySelective(customGroupEO);
        return Result.success(customGroupEO);
    }

    @ApiOperation(value = "|CustomGroupEO|删除")
    @DeleteMapping("/{id}")
//    @RequiresPermissions("group:customGroup:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        customGroupEOService.deleteByPrimaryKey(id);
        log.info("delete from TS_CUSTOM_GROUP where id = {}", id);
        return Result.success();
    }

}
