package com.adc.da.research.funds.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.adc.da.base.web.BaseController;
import com.adc.da.login.util.UserUtils;
import com.adc.da.research.funds.entity.ProjectIncomeEO;
import com.adc.da.research.funds.eum.RoleType;
import com.adc.da.research.funds.page.ProjectIncomeEOPage;
import com.adc.da.research.funds.service.ProjectIncomeEOService;
import com.adc.da.research.funds.vo.income.ErrorIncomeVO;
import com.adc.da.research.funds.vo.income.ProjectIncomeVO;
import com.adc.da.sys.entity.RoleEO;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.http.PageInfo;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.utils.Encodes;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping("/${restPath}/research/funds/credit")
@Api(tags = "科研系统|入账查看接口|ProjectIncomeEOController")
public class ProjectIncomeEOController extends BaseController<ProjectIncomeEO>{

    private static final Logger logger = LoggerFactory.getLogger(ProjectIncomeEOController.class);

    @Autowired
    private ProjectIncomeEOService projectIncomeEOService;

	@ApiOperation(value = "|ProjectIncomeEO|分页查询")
    @GetMapping("/page")
/*
    @RequiresPermissions("funds:projectIncome:page")
*/
    public ResponseMessage<PageInfo<ProjectIncomeEO>> page(ProjectIncomeEOPage page) throws Exception {
        List<ProjectIncomeEO> rows = projectIncomeEOService.queryByPagePermission(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|ProjectIncomeEO|查询")
    @GetMapping("")
/*
    @RequiresPermissions("funds:projectIncome:list")
*/
    public ResponseMessage<List<ProjectIncomeEO>> list(ProjectIncomeEOPage page) throws Exception {
        return Result.success(projectIncomeEOService.queryByList(page));
	}

    @ApiOperation(value = "|ProjectIncomeEO|详情")
    @GetMapping("/{id}")
/*
    @RequiresPermissions("funds:projectIncome:get")
*/
    public ResponseMessage<ProjectIncomeEO> find(@PathVariable String id) throws Exception {
        return Result.success(projectIncomeEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|ProjectIncomeEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
/*
    @RequiresPermissions("funds:projectIncome:save")
*/
    public ResponseMessage<ProjectIncomeEO> create(@RequestBody ProjectIncomeEO projectIncomeEO) throws Exception {
        projectIncomeEOService.insertSelective(projectIncomeEO);
        return Result.success(projectIncomeEO);
    }

    @ApiOperation(value = "|ProjectIncomeEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
/*
    @RequiresPermissions("funds:projectIncome:update")
*/
    public ResponseMessage<ProjectIncomeEO> update(@RequestBody ProjectIncomeEO projectIncomeEO) throws Exception {
        projectIncomeEOService.updateByPrimaryKeySelective(projectIncomeEO);
        return Result.success(projectIncomeEO);
    }

/*    @ApiOperation(value = "|ProjectIncomeEO|删除")
    @DeleteMapping("/{id}")
    @RequiresPermissions("funds:projectIncome:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        projectIncomeEOService.deleteByPrimaryKey(id);
        logger.info("delete from RS_PROJECT_INCOME where id = {}", id);
        return Result.success();
    }*/

    @ApiOperation(value = "|ProjectIncomeEO|删除")
    @PostMapping("/logicDelete")
/*
    @RequiresPermissions("funds:projectIncome:delete")
*/
    public ResponseMessage logicDelete(@RequestBody List<String> ids) throws Exception {
        projectIncomeEOService.logicDeleteByPrimaryKeys(ids);
        return Result.success();
    }

    @PostMapping("/Import")
    @ApiOperation(value = "|CostManagementEO|导入")
    public ResponseMessage excelImport(HttpServletResponse response,@RequestParam("file") MultipartFile file) {
        try {
            InputStream is = file.getInputStream();
            final List errList = projectIncomeEOService.excelImport(is);

            if (CollectionUtils.isEmpty(errList)){
                return Result.success();
            }
            String finalFileName = "经费入账导入错误列表.xlsx";
            response.setHeader("Content-Disposition", "attachment; filename=" + Encodes.urlEncode(finalFileName));
            response.setContentType("application/force-download");
            ExportParams exportParams = new ExportParams();
            exportParams.setType(ExcelType.XSSF);
            try {
                OutputStream outputStream = response.getOutputStream();
                Workbook workbook = ExcelExportUtil.exportExcel(exportParams, ErrorIncomeVO.class,errList);
                workbook.write(outputStream);
                outputStream.flush();
            } catch (Exception e){
                logger.error(e.getMessage(), e);
                throw new AdcDaBaseException("导出失败，请重试");
            }
            return Result.error();

        } catch (Exception e) {
            logger.error(e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    @ApiOperation(value = "导出经费入账数据")
    @PostMapping("/export")
    public ResponseMessage exportExpertGroup(HttpServletResponse response
            , String fileName
            , @RequestBody ProjectIncomeEOPage page)
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
            finalFileName = "经费入账导出数据.xlsx";
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
            Workbook workbook = projectIncomeEOService.export(exportParams,page);
            workbook.write(outputStream);
            outputStream.flush();
        } catch (Exception e){
            logger.error(e.getMessage(), e);
            throw new AdcDaBaseException("导出失败，请重试");
        }
        return Result.success();
    }

    @ApiOperation(value = "查看入账详情（单一）")
    @GetMapping("/details")
    public ResponseMessage details(String id) throws Exception {
        final ProjectIncomeVO projectIncomeVO = projectIncomeEOService.details(id);
        if (Objects.nonNull(projectIncomeVO)){
            return Result.success(projectIncomeVO);
        }
        return Result.error();
    }

    @ApiOperation(value = "查看入账详情（列表）")
    @GetMapping("/detailslist")
    public ResponseMessage detailsList(String projectId) throws Exception {
        final List<ProjectIncomeVO> projectIncomeVOS = projectIncomeEOService.detailsList(projectId);
        if (CollectionUtils.isNotEmpty(projectIncomeVOS)){
            return Result.success(projectIncomeVOS);
        }
        return Result.error();
    }

}
