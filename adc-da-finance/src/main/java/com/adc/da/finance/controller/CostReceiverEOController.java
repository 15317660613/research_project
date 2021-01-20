package com.adc.da.finance.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.finance.entity.CostReceiverEO;
import com.adc.da.finance.page.CostReceiverEOPage;
import com.adc.da.finance.service.CostReceiverEOService;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.http.PageInfo;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.utils.StringUtils;
import com.adc.da.util.utils.UUID;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping("/${restPath}/finance/costReceiver")
@Api(tags = "财务管理|成本接收人|CostReceiverEO|")
public class CostReceiverEOController extends BaseController<CostReceiverEO> {

    private static final Logger logger = LoggerFactory.getLogger(CostReceiverEOController.class);

    @Autowired
    private CostReceiverEOService costReceiverEOService;

    @ApiOperation(value = "|CostReceiverEO|分页查询")
    @GetMapping("/page")
//    @RequiresPermissions("finance:costReceiver:page")
    public ResponseMessage<PageInfo<CostReceiverEO>> page(CostReceiverEOPage page) throws Exception {
        List<CostReceiverEO> rows = costReceiverEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

    @ApiOperation(value = "|CostReceiverEO|查询")
    @GetMapping("")
//        @RequiresPermissions("finance:costReceiver:list")
    public ResponseMessage<List<CostReceiverEO>> list(CostReceiverEOPage page) throws Exception {
        return Result.success(costReceiverEOService.queryByList(page));
    }

    @ApiOperation(value = "|CostReceiverEO|详情")
    @GetMapping("/{id}")
//        @RequiresPermissions("finance:costReceiver:get")
    public ResponseMessage<CostReceiverEO> find(@PathVariable String id) throws Exception {
        return Result.success(costReceiverEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|CostReceiverEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    //       @RequiresPermissions("finance:costReceiver:save")
    public ResponseMessage<CostReceiverEO> create(@RequestBody CostReceiverEO costReceiverEO) throws Exception {
        costReceiverEO.setId(UUID.randomUUID10());
        CostReceiverEO eo = costReceiverEOService.selectByOrgName(costReceiverEO.getOrgName());
        if (null != eo) {
            throw new AdcDaBaseException("已经存在该部门的接收人配置信息，请勿重复新增！");
        }
        costReceiverEOService.insertSelective(costReceiverEO);
        return Result.success(costReceiverEO);
    }

    @ApiOperation(value = "|CostReceiverEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//        @RequiresPermissions("finance:costReceiver:update")
    public ResponseMessage<CostReceiverEO> update(@RequestBody CostReceiverEO costReceiverEO) throws Exception {
        CostReceiverEO eo = costReceiverEOService.selectByOrgName(costReceiverEO.getOrgName());
        if (null != eo && !StringUtils.equals(costReceiverEO.getId(), eo.getId())) {
            throw new AdcDaBaseException("已经存在部门：" + costReceiverEO.getOrgName() + "的接收人配置信息，请勿修改为同部门名！");
        }
        costReceiverEOService.myUpdate(costReceiverEO);
        return Result.success(costReceiverEO);
    }

    @ApiOperation(value = "|CostReceiverEO|删除")
    @DeleteMapping("/{id}")
    //      @RequiresPermissions("finance:costReceiver:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        costReceiverEOService.deleteByPrimaryKey(id);
        logger.info("delete from F_COST_RECEIVER where id = {}", id);
        return Result.success();
    }

}
