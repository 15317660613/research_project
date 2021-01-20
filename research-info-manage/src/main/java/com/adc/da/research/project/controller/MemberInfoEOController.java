package com.adc.da.research.project.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.research.project.entity.MemberInfoEO;
import com.adc.da.research.project.page.MemberInfoEOPage;
import com.adc.da.research.project.service.MemberInfoEOService;
import com.adc.da.research.project.vo.MemberInfoVO;
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
@RequestMapping("/${restPath}/research/project/memberInfo")
@Api(tags = "科研系统|项目成员|MemberInfoEO")
public class MemberInfoEOController extends BaseController<MemberInfoEO>{

    private static final Logger logger = LoggerFactory.getLogger(MemberInfoEOController.class);

    @Autowired
    private MemberInfoEOService memberInfoEOService;

	@ApiOperation(value = "|MemberInfoEO|分页查询")
    @GetMapping("/page")
//    @RequiresPermissions("research.project:memberInfo:page")
    public ResponseMessage<PageInfo<MemberInfoEO>> page(MemberInfoEOPage page) throws Exception {
        List<MemberInfoEO> rows = memberInfoEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|MemberInfoEO|查询")
    @GetMapping("")
//    @RequiresPermissions("research.project:memberInfo:list")
    public ResponseMessage<List<MemberInfoEO>> list(MemberInfoEOPage page) throws Exception {
        return Result.success(memberInfoEOService.queryByList(page));
	}

    @ApiOperation(value = "|MemberInfoEO|详情")
    @GetMapping("/{id}")
//    @RequiresPermissions("research.project:memberInfo:get")
    public ResponseMessage<MemberInfoEO> find(@PathVariable String id) throws Exception {
        return Result.success(memberInfoEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|MemberInfoEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("research.project:memberInfo:save")
    public ResponseMessage<MemberInfoEO> create(@RequestBody MemberInfoEO memberInfoEO) throws Exception {
        memberInfoEOService.insertSelective(memberInfoEO);
        return Result.success(memberInfoEO);
    }

    @ApiOperation(value = "|MemberInfoEO|新增")
    @PostMapping("/createMember")
//    @RequiresPermissions("research.project:memberInfo:save")
    public ResponseMessage<MemberInfoEO> createMember(@RequestBody MemberInfoVO memberInfoVO) throws Exception {
        MemberInfoEO memberInfoEO = memberInfoEOService.saveMember(memberInfoVO);
        return Result.success(memberInfoEO);
    }

    @ApiOperation(value = "|MemberInfoEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("research.project:memberInfo:update")
    public ResponseMessage<MemberInfoEO> update(@RequestBody MemberInfoEO memberInfoEO) throws Exception {
        memberInfoEOService.updateByPrimaryKeySelective(memberInfoEO);
        return Result.success(memberInfoEO);
    }

    @ApiOperation(value = "|MemberInfoEO|修改")
    @PutMapping("/updateMember")
//    @RequiresPermissions("research.project:memberInfo:update")
    public ResponseMessage<MemberInfoEO> updateMember(@RequestBody MemberInfoVO memberInfoVO) throws Exception {
        MemberInfoEO memberInfoEO = memberInfoEOService.updateMember(memberInfoVO);
        return Result.success(memberInfoEO);
    }

    @ApiOperation(value = "|MemberInfoEO|删除")
    @DeleteMapping("/{id}")
//    @RequiresPermissions("research.project:memberInfo:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        memberInfoEOService.deleteByPrimaryKey(id);
        logger.info("delete from RS_MEMBER_INFO where id = {}", id);
        return Result.success();
    }

    @ApiOperation(value = "|MemberInfoEO|查询统计人员职称学历")
    @GetMapping("/queryCountByJob")
    public ResponseMessage< List<MemberInfoEO>> queryCountByJob(MemberInfoEOPage page) throws Exception {
        return Result.success(memberInfoEOService.queryCountByJob(page));
    }

    @ApiOperation(value = "|MemberInfoEO|根据项目id查询统计人员职称学历")
    @GetMapping("/queryCountJobByProjectId")
    public ResponseMessage queryCountJobByProjectId(String projectId) throws Exception {
        return Result.success(memberInfoEOService.queryCountJobByProjectId(projectId));
    }

    @ApiOperation(value = "|MemberInfoEO|批量新增")
    @PostMapping("/batchInsertSelective")
    public ResponseMessage<MemberInfoEO> batchInsertSelective(@RequestBody List<MemberInfoEO> memberInfoEOS) throws Exception {
        memberInfoEOService.batchInsertSelective(memberInfoEOS);
        return Result.success();
    }

}
