package com.adc.da.research.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.research.entity.MemberEO;
import com.adc.da.research.page.MemberEOPage;
import com.adc.da.research.service.MemberEOService;
import com.adc.da.util.http.PageInfo;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
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
@RequestMapping("/${restPath}/research/member")
@Api(tags = "|科研类项目模块|")
@Slf4j

public class MemberEOController extends BaseController<MemberEO> {

    @Autowired
    private MemberEOService memberEOService;

    @ApiOperation(value = "|MemberEO|分页查询")
    @GetMapping("/page")
    ////@RequiresPermissions"research:member:page")
    public ResponseMessage<PageInfo<MemberEO>> page(MemberEOPage page) throws Exception {
        List<MemberEO> rows = memberEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

    @ApiOperation(value = "|MemberEO|查询")
    @GetMapping("")
    ////@RequiresPermissions"research:member:list")
    public ResponseMessage<List<MemberEO>> list(MemberEOPage page) throws Exception {
        return Result.success(memberEOService.queryByList(page));
    }

    @ApiOperation(value = "|MemberEO|详情")
    @GetMapping("/{id}")
    ////@RequiresPermissions"research:member:get")
    public ResponseMessage<MemberEO> find(@PathVariable String id) throws Exception {
        return Result.success(memberEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|MemberEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    ////@RequiresPermissions"research:member:save")
    public ResponseMessage<MemberEO> create(@RequestBody MemberEO memberEO) throws Exception {
        memberEO.setId(UUID.randomUUID10());

        if (memberEOService.insertSelective(memberEO) != -1) {
            return Result.success(memberEO);
        } else {
            return Result.error("1", "用户已存在", memberEO);
        }
    }

    @ApiOperation(value = "|MemberEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    ////@RequiresPermissions"research:member:update")
    public ResponseMessage<MemberEO> update(@RequestBody MemberEO memberEO) throws Exception {

        if (memberEOService.updateByPrimaryKeySelective(memberEO) != -1) {
            return Result.success(memberEO);
        } else {
            return Result.error("1", "用户已存在", memberEO);
        }

    }

    @ApiOperation(value = "|MemberEO|删除")
    @DeleteMapping("/{id}")
    ////@RequiresPermissions"research:member:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        memberEOService.deleteByPrimaryKey(id);
        log.info("delete from RS_PROJECT_MEMBER where id = {}", id);
        return Result.success();
    }

}
