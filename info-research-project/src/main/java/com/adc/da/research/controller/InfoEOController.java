package com.adc.da.research.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.login.util.UserUtils;
import com.adc.da.research.entity.InfoEO;
import com.adc.da.research.page.InfoEOPage;
import com.adc.da.research.service.InfoEOService;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.http.PageInfo;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.utils.StringUtils;
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

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

/**
 * 基本信息模块
 */
@RestController
@RequestMapping("/${restPath}/research/info")
@Api(tags = "|科研类项目模块|")
@Slf4j
public class InfoEOController extends BaseController<InfoEO> {

    @Autowired
    private InfoEOService infoEOService;

    @ApiOperation(value = "|InfoEO|分页查询-待发列表")
    @GetMapping("/page")
//  @RequiresPermissions"research:info:page")
    public ResponseMessage<PageInfo<InfoEO>> page(Integer page, Integer pageSize) {
        //('/api/research/info/page?orderBy=EXT_INFO_5_ DESC&extInfo4=0&extInfo1='+account.usid), //
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }

        InfoEOPage queryPage = new InfoEOPage();
        queryPage.setPage(page);
        queryPage.setPageSize(pageSize);
        queryPage.setStartIndex(-1);
        queryPage.setEndIndex(-1);
        queryPage.setExtInfo1(userId);

        List<InfoEO> rows = infoEOService.getPendingProcess(queryPage);

        return Result.success(getPageInfo(queryPage.getPager(), rows));
    }

    @ApiOperation(value = "|InfoEO|查询")
    @GetMapping("")
//    ////@RequiresPermissions"research:info:list")
    public ResponseMessage<List<InfoEO>> list(InfoEOPage page) throws Exception {
        if (StringUtils.isEmpty(page.getResearchProjectId())){
            return Result.success(new ArrayList<>());
        }
        return Result.success(infoEOService.queryByList(page));
    }

    @ApiOperation(value = "|InfoEO|详情")
    @GetMapping("/{researchProjectId}")
    ////@RequiresPermissions"research:info:get")
    public ResponseMessage<InfoEO> find(@PathVariable String researchProjectId) throws Exception {
        return Result.success(infoEOService.selectByPrimaryKey(researchProjectId));
    }

    @ApiOperation(value = "|InfoEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    ////@RequiresPermissions"research:info:save")
    public ResponseMessage<InfoEO> create(@RequestBody InfoEO infoEO) throws Exception {
        InfoEOPage infoEOPage = new InfoEOPage();
        infoEOPage.setResearchProjectName(infoEO.getResearchProjectName());
        infoEOPage.setBusinessId(infoEO.getBusinessId());
        if (infoEOService.queryByCount(infoEOPage) > 0) {
            return Result.error("1", "该科研项目名称已存在", infoEO);
        }
        return Result.success(infoEOService.save(infoEO));
    }

    @ApiOperation(value = "|InfoEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    ////@RequiresPermissions"research:info:update")
    public ResponseMessage<InfoEO> update(@RequestBody InfoEO infoEO) throws Exception {

        if (-1 == infoEOService.updateByPrimaryKeySelective(infoEO)) {
            return Result.error("1", "该科研项目名称已存在", infoEO);
        } else {
            return Result.success(infoEO);
        }
    }

    @ApiOperation(value = "|InfoEO|删除")
    @DeleteMapping("/{researchProjectId}")
    ////@RequiresPermissions"research:info:delete")
    public ResponseMessage delete(@PathVariable String researchProjectId) throws Exception {
        infoEOService.deleteByPrimaryKey(researchProjectId);
        log.info("delete from RS_PROJECT_INFO where researchProjectId = {}", researchProjectId);
        return Result.success();
    }

}
