package com.adc.da.crm.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.adc.da.base.web.BaseController;
import com.adc.da.crm.entity.BTravelApprovalEO;
import com.adc.da.crm.page.BTravelApprovalEOPage;
import com.adc.da.crm.service.BTravelApprovalEOService;

import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.http.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;

@RestController
@RequestMapping("/${restPath}/crm/bTravelApproval")
@Api(description = "|BTravelApprovalEO|")
public class BTravelApprovalEOController extends BaseController<BTravelApprovalEO>{

    private static final Logger logger = LoggerFactory.getLogger(BTravelApprovalEOController.class);

    @Autowired
    private BTravelApprovalEOService bTravelApprovalEOService;

	@ApiOperation(value = "|BTravelApprovalEO|分页查询")
    @GetMapping("/page")
    @RequiresPermissions("crm:bTravelApproval:page")
    public ResponseMessage<PageInfo<BTravelApprovalEO>> page(BTravelApprovalEOPage page) throws Exception {
        List<BTravelApprovalEO> rows = bTravelApprovalEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|BTravelApprovalEO|查询")
    @GetMapping("")
    @RequiresPermissions("crm:bTravelApproval:list")
    public ResponseMessage<List<BTravelApprovalEO>> list(BTravelApprovalEOPage page) throws Exception {
        return Result.success(bTravelApprovalEOService.queryByList(page));
	}

    @ApiOperation(value = "|BTravelApprovalEO|详情")
    @GetMapping("/{id}")
    @RequiresPermissions("crm:bTravelApproval:get")
    public ResponseMessage<BTravelApprovalEO> find(@PathVariable String id) throws Exception {
        return Result.success(bTravelApprovalEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|BTravelApprovalEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("crm:bTravelApproval:save")
    public ResponseMessage<BTravelApprovalEO> create(@RequestBody BTravelApprovalEO bTravelApprovalEO) throws Exception {
        bTravelApprovalEOService.insertSelective(bTravelApprovalEO);
        return Result.success(bTravelApprovalEO);
    }

    @ApiOperation(value = "|BTravelApprovalEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("crm:bTravelApproval:update")
    public ResponseMessage<BTravelApprovalEO> update(@RequestBody BTravelApprovalEO bTravelApprovalEO) throws Exception {
        bTravelApprovalEOService.updateByPrimaryKeySelective(bTravelApprovalEO);
        return Result.success(bTravelApprovalEO);
    }

    @ApiOperation(value = "|BTravelApprovalEO|删除")
    @DeleteMapping("/{id}")
    @RequiresPermissions("crm:bTravelApproval:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        bTravelApprovalEOService.deleteByPrimaryKey(id);
        logger.info("delete from B_TRAVEL_APPROVAL where id = {}", id);
        return Result.success();
    }

}
