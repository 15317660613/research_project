package com.adc.da.order.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.util.List;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.adc.da.base.web.BaseController;
import com.adc.da.order.entity.OrderEO;
import com.adc.da.order.page.OrderEOPage;
import com.adc.da.order.service.OrderEOService;

import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.http.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;

@RestController
@RequestMapping("/${restPath}/order/order")
@Api(description = "|OrderEO|")
public class OrderEOController extends BaseController<OrderEO>{

    private static final Logger logger = LoggerFactory.getLogger(OrderEOController.class);

    @Autowired
    private OrderEOService orderEOService;

	@ApiOperation(value = "|OrderEO|分页查询")
    @GetMapping("/page")
    @RequiresPermissions("order:order:page")
    public ResponseMessage<PageInfo<OrderEO>> page(OrderEOPage page) throws Exception {
        List<OrderEO> rows = orderEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|OrderEO|查询")
    @GetMapping("")
    @RequiresPermissions("order:order:list")
    public ResponseMessage<List<OrderEO>> list(OrderEOPage page) throws Exception {
        return Result.success(orderEOService.queryByList(page));
	}

    @ApiOperation(value = "|OrderEO|详情")
    @GetMapping("/{orderId}")
    @RequiresPermissions("order:order:get")
    public ResponseMessage<OrderEO> find(@PathVariable Long orderId) throws Exception {
        return Result.success(orderEOService.selectByPrimaryKey(orderId));
    }

    @ApiOperation(value = "|OrderEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("order:order:save")
    public ResponseMessage<OrderEO> create(@RequestBody OrderEO orderEO) throws Exception {
        orderEOService.insertSelective(orderEO);
        return Result.success(orderEO);
    }

    @ApiOperation(value = "|OrderEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("order:order:update")
    public ResponseMessage<OrderEO> update(@RequestBody OrderEO orderEO) throws Exception {
        orderEOService.updateByPrimaryKeySelective(orderEO);
        return Result.success(orderEO);
    }

    @ApiOperation(value = "|OrderEO|删除")
    @DeleteMapping("/{orderId}")
    @RequiresPermissions("order:order:delete")
    public ResponseMessage delete(@PathVariable Long orderId) throws Exception {
        orderEOService.deleteByPrimaryKey(orderId);
        logger.info("delete from TS_ORDER where orderId = {}", orderId);
        return Result.success();
    }

}
