package com.adc.da.research.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.research.entity.HiBaseEO;
import com.adc.da.research.page.HiBaseEOPage;
import com.adc.da.research.service.HiBaseEOService;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

/**
 * todo 启动流程 更新info表状态
 * todo 待发 删除 相关调整 更改info状态
 * todo 发起流程后 锁定info状态
 */
@RestController
@RequestMapping("/${restPath}/research/hiBase")
@Api(tags = "|科研类项目模块-变更流程|")
@Slf4j
public class HiBaseEOController extends BaseController<HiBaseEO> {
    @Autowired
    private HiBaseEOService hiBaseEOService;

    @ApiOperation(value = "|HiBaseEO|查询")
    @GetMapping("")
    //@RequiresPermissions("research:hiBase:list")
    public ResponseMessage<List<HiBaseEO>> list(String procBusinessKey) throws Exception {
        HiBaseEOPage page = new HiBaseEOPage();
        page.setProcBusinessKey(procBusinessKey);
        return Result.success(hiBaseEOService.queryByList(page));
    }

    @ApiOperation(value = "|HiBaseEO|获取BusinessKey")
    @GetMapping("/lastKey/{projectId}")
    public ResponseMessage<String> findByProjectId(@PathVariable String projectId, String procInstId) {
        String result;
        if (StringUtils.isNotEmpty(procInstId)) {
            result = hiBaseEOService.getBusinessKeyByProcInstId(procInstId);
        } else {
            result = hiBaseEOService.getLastBusinessKey(projectId);
        }
        return Result.success(result);
    }

    @ApiOperation(value = "|HiBaseEO|获取变更菜单名称")
    @GetMapping("/menu/{projectId}/{businessKey}")
    public ResponseMessage<int[]> findMenuByBusinessKey(@PathVariable String businessKey,
        @PathVariable String projectId) {
        return Result.success(hiBaseEOService.getMenu(businessKey, projectId));
    }

    @ApiOperation(value = "|HiBaseEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    //@RequiresPermissions("research:hiBase:update")
    public ResponseMessage<HiBaseEO> update(@RequestBody HiBaseEO hiBaseEO) throws Exception {
        hiBaseEOService.updateByPrimaryKeySelective(hiBaseEO);
        return Result.success(hiBaseEO);
    }

}
