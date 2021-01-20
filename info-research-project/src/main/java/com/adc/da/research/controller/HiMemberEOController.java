package com.adc.da.research.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.budget.entity.ChildrenTask;
import com.adc.da.budget.entity.Project;
import com.adc.da.budget.entity.Task;
import com.adc.da.budget.repository.ChildTaskRepository;
import com.adc.da.budget.repository.ProjectRepository;
import com.adc.da.budget.repository.TaskRepository;
import com.adc.da.budget.utils.CommonUtil;
import com.adc.da.research.entity.HiMemberEO;
import com.adc.da.research.page.HiMemberEOPage;
import com.adc.da.research.service.HiMemberEOService;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.http.PageInfo;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping("/${restPath}/research/hiProjectMember")
@Api(tags = "|科研类项目模块-变更流程|")
@Slf4j
public class HiMemberEOController extends BaseController<HiMemberEO> {

    @Autowired
    private HiMemberEOService hiProjectMemberEOService;

    @ApiOperation(value = "|HiProjectMemberEO|分页查询")
    @GetMapping("/page")
    //@RequiresPermissions("research:hiProjectMember:page")
    public ResponseMessage<PageInfo<HiMemberEO>> page(HiMemberEOPage page) throws Exception {
        List<HiMemberEO> rows = hiProjectMemberEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

    @ApiOperation(value = "|HiProjectMemberEO|查询")
    @GetMapping("")
    //@RequiresPermissions("research:hiProjectMember:list")
    public ResponseMessage<List<HiMemberEO>> list(String businessKey) throws Exception {
        HiMemberEOPage page = new HiMemberEOPage();
        page.setProcBusinessKey(businessKey);
        return Result.success(hiProjectMemberEOService.queryByList(page));
    }

    @ApiOperation(value = "|HiProjectMemberEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    //@RequiresPermissions("research:hiProjectMember:save")
    public ResponseMessage<HiMemberEO> create(@RequestBody HiMemberEO hiProjectMemberEO)
        throws Exception {
        if (hiProjectMemberEOService.insertSelective(hiProjectMemberEO) != -1) {
            return Result.success(hiProjectMemberEO);
        } else {
            return Result.error("1", "用户已存在", hiProjectMemberEO);
        }
    }

    @ApiOperation(value = "|HiProjectMemberEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    //@RequiresPermissions("research:hiProjectMember:update")
    public ResponseMessage<HiMemberEO> update(@RequestBody HiMemberEO hiProjectMemberEO)
        throws Exception {
        if (hiProjectMemberEOService.updateAndSetMask(hiProjectMemberEO) != -1) {
            return Result.success(hiProjectMemberEO);
        } else {
            return Result.error("1", "用户已存在", hiProjectMemberEO);
        }
    }

    @ApiOperation(value = "|HiProjectMemberEO|删除")
    @DeleteMapping("/{id}/{businessKey}")
    //@RequiresPermissions("research:hiProjectMember:delete")
    public ResponseMessage delete(@PathVariable String id, @PathVariable String businessKey) throws Exception {

        hiProjectMemberEOService.deleteByPrimaryKey(id, businessKey);
        log.info("delete from RS_HI_PROJECT_MEMBER where id = {}", id);
        return Result.success();
    }

}
