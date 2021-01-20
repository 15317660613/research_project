package com.adc.da.research.funds.controller;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.adc.da.base.web.BaseController;
import com.adc.da.login.util.UserUtils;
import com.adc.da.research.funds.entity.ProjectOverEO;
import com.adc.da.research.funds.eum.RoleType;
import com.adc.da.research.funds.page.ProjectOverEOPage;
import com.adc.da.research.funds.service.ProjectOverEOService;
import com.adc.da.research.funds.vo.over.ProjectOverVO;
import com.adc.da.sys.entity.RoleEO;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.http.PageInfo;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.utils.Encodes;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping("/${restPath}/research/funds/projectOver")
@Api(tags = "科研系统|科研项目经费结转|ProjectOverEOController")
public class ProjectOverEOController extends BaseController<ProjectOverEO> {

    private static final Logger logger = LoggerFactory.getLogger(ProjectOverEOController.class);

    @Autowired
    private ProjectOverEOService projectOverEOService;

    @ApiOperation(value = "|ProjectOverEO|分页查询")
    @GetMapping("/page")
/*
    @RequiresPermissions("funds:projectOver:page")
*/
    public ResponseMessage<PageInfo<ProjectOverEO>> page(ProjectOverEOPage page) throws Exception {
        //得到当前用户权限列表
        final List<RoleEO> roleList = UserUtils.getRoleList();
        if (com.adc.da.util.utils.CollectionUtils.isEmpty(roleList)) {
            throw new AdcDaBaseException("此用户无权限查看");
        }
        final Set<String> roleInfoSet = roleList.stream().map(RoleEO::getExtInfo).collect(Collectors.toSet());
        if (!roleInfoSet.contains(RoleType.RS_ADMIN.getCode())){
            page.setCreateUserId(UserUtils.getUserId());
        }

        List<ProjectOverEO> rows = projectOverEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

    @ApiOperation(value = "|ProjectOverEO|查询")
    @GetMapping("")
/*
    @RequiresPermissions("funds:projectOver:list")
*/
    public ResponseMessage<List<ProjectOverEO>> list(ProjectOverEOPage page) throws Exception {
        return Result.success(projectOverEOService.queryByList(page));
    }

    @ApiOperation(value = "|ProjectOverEO|详情")
    @GetMapping("/{id}")
/*
    @RequiresPermissions("funds:projectOver:get")
*/
    public ResponseMessage<ProjectOverEO> find(@PathVariable String id) throws Exception {
        return Result.success(projectOverEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|ProjectOverEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
/*
    @RequiresPermissions("funds:projectOver:save")
*/
    public ResponseMessage<ProjectOverEO> create(@RequestBody ProjectOverEO projectOverEO) throws Exception {
        projectOverEOService.insertSelective(projectOverEO);
        return Result.success(projectOverEO);
    }

    @ApiOperation(value = "|ProjectOverEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
/*
    @RequiresPermissions("funds:projectOver:update")
*/
    public ResponseMessage<ProjectOverEO> update(@RequestBody ProjectOverEO projectOverEO) throws Exception {
        projectOverEOService.updateByPrimaryKeySelective(projectOverEO);
        return Result.success(projectOverEO);
    }

    @ApiOperation(value = "|ProjectOverEO|删除")
    @DeleteMapping("/{id}")
/*
    @RequiresPermissions("funds:projectOver:delete")
*/
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        projectOverEOService.deleteByPrimaryKey(id);
        logger.info("delete from RS_PROJECT_OVER where id = {}", id);
        return Result.success();
    }

    @ApiOperation(value = "|ProjectOverEO|计算")
    @GetMapping("/calculate")
    public ResponseMessage calculate(String year, String startDate, String endDate) throws Exception {
        projectOverEOService.calculate(year, startDate, endDate);
        return Result.success();
    }

    @ApiOperation(value = "结转功能（显示）")
    @GetMapping("/carryover")
    public ResponseMessage carryover(String id) throws Exception {
        final ProjectOverVO projectExpendVO = projectOverEOService.carryover(id);
        return Result.success(projectExpendVO);

    }

    @ApiOperation(value = "结转功能（执行）")
    @GetMapping("/carryoveract")
    public ResponseMessage carryoverAct(String id, String percentage) throws Exception {
        final Integer mark = projectOverEOService.carryoverAct(id, percentage);
        if (mark > 0) {
            return Result.success();
        }
        return Result.error();
    }

    @ApiOperation(value = "查看")
    @GetMapping("/details")
    public ResponseMessage details(String id) throws Exception {
        ProjectOverVO projectOverVO = projectOverEOService.details(id);
        return Result.success(projectOverVO);
    }

    @ApiOperation(value = "导出经费入账数据")
    @PostMapping("/export")
    public ResponseMessage exportExpertGroup(HttpServletResponse response, String fileName, @RequestBody ProjectOverEOPage page)
            throws Exception{
        //得到当前用户权限列表
        final List<RoleEO> roleList = UserUtils.getRoleList();
        if (com.adc.da.util.utils.CollectionUtils.isEmpty(roleList)) {
            throw new AdcDaBaseException("此用户无权限查看");
        }
        final Set<String> roleInfoSet = roleList.stream().map(RoleEO::getExtInfo).collect(Collectors.toSet());
        if (!roleInfoSet.contains(RoleType.RS_ADMIN.getCode())){
            page.setCreateUserId(UserUtils.getUserId());
        }

        String finalFileName = "";
        if (StringUtils.isEmpty(fileName)){
            finalFileName = "经费结算导出数据.xlsx";
        }else{
            if (!fileName.contains(".xlsx") || !fileName.contains("xls")){
                finalFileName = fileName + ".xlsx";
            }else{
                finalFileName = fileName;
            }
        }

        response.setHeader("Content-Disposition", "attachment; filename=" + Encodes.urlEncode(finalFileName));
        response.setContentType("application/force-download");
        ExportParams exportParams = new ExportParams();
        exportParams.setType(ExcelType.XSSF);
        try {
            OutputStream outputStream = response.getOutputStream();
            Workbook workbook = projectOverEOService.export(exportParams,page);
            workbook.write(outputStream);
            outputStream.flush();
        } catch (Exception e){
            logger.error(e.getMessage(), e);
            throw new AdcDaBaseException("导出失败，请重试");
        }
        return Result.success();
    }
}
