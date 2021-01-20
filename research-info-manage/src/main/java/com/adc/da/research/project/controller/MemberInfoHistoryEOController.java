package com.adc.da.research.project.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.research.project.entity.MemberInfoHistoryEO;
import com.adc.da.research.project.page.MemberInfoHistoryEOPage;
import com.adc.da.research.project.service.MemberInfoHistoryEOService;
import com.adc.da.util.http.PageInfo;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RequestMapping("/${restPath}/project/memberInfoHistory")
@Api(description = "|MemberInfoHistoryEO|")
public class MemberInfoHistoryEOController extends BaseController<MemberInfoHistoryEO>{

    private static final Logger logger = LoggerFactory.getLogger(MemberInfoHistoryEOController.class);

    @Autowired
    private MemberInfoHistoryEOService memberInfoHistoryEOService;

	@ApiOperation(value = "|MemberInfoHistoryEO|分页查询")
    @GetMapping("/page")
    public ResponseMessage<PageInfo<MemberInfoHistoryEO>> page(MemberInfoHistoryEOPage page) throws Exception {
        List<MemberInfoHistoryEO> rows = memberInfoHistoryEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|MemberInfoHistoryEO|查询")
    @GetMapping("")
    public ResponseMessage<List<MemberInfoHistoryEO>> list(MemberInfoHistoryEOPage page) throws Exception {
        return Result.success(memberInfoHistoryEOService.queryByList(page));
	}

    @ApiOperation(value = "|MemberInfoHistoryEO|详情")
    @GetMapping("/{id}")
    public ResponseMessage<MemberInfoHistoryEO> find(@PathVariable String id) throws Exception {
        return Result.success(memberInfoHistoryEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|MemberInfoHistoryEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage<MemberInfoHistoryEO> create(@RequestBody MemberInfoHistoryEO memberInfoHistoryEO) throws Exception {
        memberInfoHistoryEOService.insertSelective(memberInfoHistoryEO);
        return Result.success(memberInfoHistoryEO);
    }

    @ApiOperation(value = "|MemberInfoHistoryEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage<MemberInfoHistoryEO> update(@RequestBody MemberInfoHistoryEO memberInfoHistoryEO) throws Exception {
        memberInfoHistoryEOService.updateByPrimaryKeySelective(memberInfoHistoryEO);
        return Result.success(memberInfoHistoryEO);
    }

    @ApiOperation(value = "|MemberInfoHistoryEO|删除")
    @DeleteMapping("/{id}")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        memberInfoHistoryEOService.deleteByPrimaryKey(id);
        logger.info("delete from RS_MEMBER_INFO_HISTORY where id = {}", id);
        return Result.success();
    }
    @ApiOperation(value = "|MemberInfoEO|批量新增")
    @PostMapping("/batchInsertSelective")
    public ResponseMessage batchInsertSelective(@RequestBody List<MemberInfoHistoryEO> memberInfoEOS) throws Exception {
        memberInfoHistoryEOService.batchInsertSelective(memberInfoEOS);
        return Result.success();
    }

}
