package com.adc.da.research.project.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.research.project.entity.CheckTargetEO;
import com.adc.da.research.project.page.CheckTargetEOPage;
import com.adc.da.research.project.service.CheckTargetEOService;
import com.adc.da.util.http.PageInfo;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping("/${restPath}/research/project/checkTarget")
@Api(tags = "科研系统|科研项目考核指标信息|CheckTargetEOController")
public class CheckTargetEOController extends BaseController<CheckTargetEO> {

    private static final Logger logger = LoggerFactory.getLogger(CheckTargetEOController.class);

    @Autowired
    private CheckTargetEOService checkTargetEOService;

	@ApiOperation(value = "|CheckTargetEO|分页查询")
    @GetMapping("/page")
//    @RequiresPermissions("research.project:checkTarget:page")
    public ResponseMessage<PageInfo<CheckTargetEO>> page(CheckTargetEOPage page) throws Exception {
        List<CheckTargetEO> rows = checkTargetEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|CheckTargetEO|查询")
    @GetMapping("")
//    @RequiresPermissions("research.project:checkTarget:list")
    public ResponseMessage<Map<String,List<CheckTargetEO>>> list(CheckTargetEOPage page) throws Exception {
        Map<String,List<CheckTargetEO>> map= checkTargetEOService.queryByPart(page);

        return Result.success(map);
	}

    @ApiOperation(value = "|CheckTargetEO|详情")
    @GetMapping("/{id}")
//    @RequiresPermissions("research.project:checkTarget:get")
    public ResponseMessage<CheckTargetEO> find(@PathVariable String id) throws Exception {
        return Result.success(checkTargetEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|CheckTargetEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("research.project:checkTarget:save")
    public ResponseMessage<CheckTargetEO> create(@RequestBody CheckTargetEO checkTargetEO) throws Exception {
        checkTargetEOService.insertCheckTargetEO(checkTargetEO);
        return Result.success(checkTargetEO);
    }

    @ApiOperation(value = "|CheckTargetEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("research.project:checkTarget:update")
    public ResponseMessage<CheckTargetEO> update(@RequestBody CheckTargetEO checkTargetEO) throws Exception {
        checkTargetEOService.updateCheckTargetEO(checkTargetEO);
        return Result.success(checkTargetEO);
    }

    @ApiOperation(value = "|CheckTargetEO|删除")
    @DeleteMapping("/{id}")
//    @RequiresPermissions("research.project:checkTarget:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        checkTargetEOService.deleteByPrimaryKey(id);
        logger.info("delete from RS_CHECK_TARGET where id = {}", id);
        return Result.success();
    }

    @ApiOperation(value = "|CheckTargetEO|批量新增")
    @PostMapping("/batchInsertSelective")
    public ResponseMessage<CheckTargetEO> batchInsertSelective(@RequestBody List<CheckTargetEO> checkTargetEOS) throws Exception {
        checkTargetEOService.batchInsertSelective(checkTargetEOS);
        return Result.success();
    }

    @ApiOperation(value = "|CheckTargetEO|批量新增基础指标数")
    @PostMapping("/batchInsertBasicTarget")
    public ResponseMessage<List<CheckTargetEO>> batchInsertBasicTarget(@RequestBody List<CheckTargetEO> checkTargetEOList) throws Exception {
        List<CheckTargetEO> checkTargetEOS = checkTargetEOService.batchInsertBasicTarget(checkTargetEOList);
        return Result.success(checkTargetEOS);
    }

}
