package com.adc.da.capital.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.capital.entity.HiCapitalDetailEO;
import com.adc.da.capital.page.HiCapitalDetailEOPage;
import com.adc.da.capital.service.HiCapitalDetailEOService;
import com.adc.da.util.http.PageInfo;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
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
@RequestMapping("/${restPath}/capital/hiCapitalDetail")
@Api(tags = "|科研类项目模块-资金相关-变更|")
@Slf4j
public class HiCapitalDetailEOController extends BaseController<HiCapitalDetailEO> {

    @Autowired
    private HiCapitalDetailEOService hiCapitalDetailEOService;

    @ApiOperation(value = "|HiCapitalDetailEO|分页查询")
    @GetMapping("/page")
    //@RequiresPermissions("capital:hiCapitalDetail:page")
    public ResponseMessage<PageInfo<HiCapitalDetailEO>> page(HiCapitalDetailEOPage page) throws Exception {
        List<HiCapitalDetailEO> rows = hiCapitalDetailEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

    @ApiOperation(value = "|HiCapitalDetailEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    //@RequiresPermissions("capital:hiCapitalDetail:save")
    public ResponseMessage<HiCapitalDetailEO> create(@RequestBody HiCapitalDetailEO hiCapitalDetailEO)
        throws Exception {
        hiCapitalDetailEOService.insertSelective(hiCapitalDetailEO);
        return Result.success(hiCapitalDetailEO);
    }

    /**
     *
     * @param eo
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "|CapitalExpenditureDetailEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    //@RequiresPermissions"capital:capitalExpenditureDetail:update")
    public ResponseMessage<HiCapitalDetailEO> update(
        @RequestBody HiCapitalDetailEO eo) throws Exception {
        hiCapitalDetailEOService.updateAndSetMask(eo);
        return Result.success(eo);
    }

    /**
     * 根据projectId和key 删除
     * @param id
     * @param businessKey
     * @return
     */
    @ApiOperation(value = "|CapitalExpenditureDetailEO|删除")
    @DeleteMapping("/{id}/{businessKey}")
    //@RequiresPermissions"capital:capitalExpenditureDetail:delete")
    public ResponseMessage delete(@PathVariable String id, @PathVariable String businessKey) {
        hiCapitalDetailEOService.deleteByPrimaryKey(id, businessKey);
        log.info("delete from RS_CAPITAL_EXPENDITURE_DETAIL where id = {},key = {} ", id, businessKey);
        return Result.success();
    }

}
