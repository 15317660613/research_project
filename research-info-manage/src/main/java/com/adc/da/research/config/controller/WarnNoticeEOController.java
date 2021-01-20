package com.adc.da.research.config.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.research.config.entity.WarnNoticeEO;
import com.adc.da.research.config.page.WarnNoticeEOPage;
import com.adc.da.research.config.service.WarnNoticeEOService;
import com.adc.da.util.http.PageInfo;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping("/${restPath}/research/warnNotice")
@Api(tags = "科研系统|经费预警-项目预警公告配置|WarnNoticeEOController|")
public class WarnNoticeEOController extends BaseController<WarnNoticeEO> {

    private static final Logger logger = LoggerFactory.getLogger(WarnNoticeEOController.class);

    @Autowired
    private WarnNoticeEOService warnNoticeEOService;

	@ApiOperation(value = "|WarnNoticeEO|分页查询")
    @GetMapping("/page")
    // @RequiresPermissions("research:warnNotice:page")
    public ResponseMessage<PageInfo<WarnNoticeEO>> page(WarnNoticeEOPage page) throws Exception {
        try {
            page.setTitleOperator("LIKE");
            page.setWarnCotentOperator("LIKE");
            page.setDelFlag("0");
            List<WarnNoticeEO> rows = warnNoticeEOService.queryByPage(page);
            return Result.success(getPageInfo(page.getPager(), rows));
        } catch (Exception e) {
            return Result.error("查询失败!");
        }
    }

	@ApiOperation(value = "|WarnNoticeEO|查询")
    @GetMapping("")
    // @RequiresPermissions("research:warnNotice:list")
    public ResponseMessage<List<WarnNoticeEO>> list(WarnNoticeEOPage page) throws Exception {
        try {
            page.setTitleOperator("LIKE");
            page.setWarnCotentOperator("LIKE");
            page.setDelFlag("0");
            return Result.success(warnNoticeEOService.queryByList(page));
        } catch (Exception e) {
            return Result.error("查询失败!");
        }
    }

    @ApiOperation(value = "|WarnNoticeEO|详情")
    @GetMapping("/{id}")
    // @RequiresPermissions("research:warnNotice:get")
    public ResponseMessage<WarnNoticeEO> find(@PathVariable String id) throws Exception {
        return Result.success(warnNoticeEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|WarnNoticeEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    // @RequiresPermissions("research:warnNotice:save")
    public ResponseMessage<WarnNoticeEO> create(@RequestBody WarnNoticeEO warnNoticeEO) throws Exception {
        try {
            warnNoticeEOService.saveWarnNotice(warnNoticeEO);
            return Result.success(warnNoticeEO);
        } catch (Exception e) {
            return Result.error("添加失败!");
        }
    }

    @ApiOperation(value = "|WarnNoticeEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    // @RequiresPermissions("research:warnNotice:update")
    public ResponseMessage<WarnNoticeEO> update(@RequestBody WarnNoticeEO warnNoticeEO) throws Exception {
        try {
            warnNoticeEOService.update(warnNoticeEO);
            return Result.success(warnNoticeEO);
        } catch (Exception e) {
            return Result.error("修改失败！");
        }
    }

    @ApiOperation(value = "|WarnNoticeEO|批量删除")
    @DeleteMapping("/{ids}")
    // @RequiresPermissions("research:warnNotice:delete")
    public ResponseMessage deleteByIds(@NotNull @ApiParam(value = "ids", required = true) @PathVariable("ids")String[] ids) throws Exception {
        warnNoticeEOService.deleteByIds(ids);
        return Result.success();
    }

}
