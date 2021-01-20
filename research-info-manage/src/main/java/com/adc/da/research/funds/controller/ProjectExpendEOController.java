package com.adc.da.research.funds.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.adc.da.base.web.BaseController;
import com.adc.da.login.util.UserUtils;
import com.adc.da.research.funds.entity.ProjectExpendEO;
import com.adc.da.research.funds.eum.RoleType;
import com.adc.da.research.funds.page.ProjectExpendEOPage;
import com.adc.da.research.funds.service.ProjectExpendEOService;
import com.adc.da.research.funds.vo.expend.ErrorExpendVO;
import com.adc.da.research.funds.vo.expend.ProjectExpendVO;
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
@RequestMapping("/${restPath}/research/funds/expenditure")
@Api(tags = "科研系统|经费支出|ProjectExpendEOController")
public class ProjectExpendEOController extends BaseController<ProjectExpendEO>{

    private static final Logger logger = LoggerFactory.getLogger(ProjectExpendEOController.class);

    @Autowired
    private ProjectExpendEOService projectExpendEOService;

	@ApiOperation(value = "|ProjectExpendEO|分页查询")
    @GetMapping("/page")
/*
    @RequiresPermissions("funds:projectExpend:page")
*/
    public ResponseMessage<PageInfo<ProjectExpendEO>> page(ProjectExpendEOPage page) throws Exception {
        List<ProjectExpendEO> rows = projectExpendEOService.queryByPagePermission(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|ProjectExpendEO|查询")
    @GetMapping("")
/*
    @RequiresPermissions("funds:projectExpend:list")
*/
    public ResponseMessage<List<ProjectExpendEO>> list(ProjectExpendEOPage page) throws Exception {
        return Result.success(projectExpendEOService.queryByList(page));
	}

    @ApiOperation(value = "|ProjectExpendEO|详情")
    @GetMapping("/{id}")
/*
    @RequiresPermissions("funds:projectExpend:get")
*/
    public ResponseMessage<ProjectExpendEO> find(@PathVariable String id) throws Exception {
        return Result.success(projectExpendEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|ProjectExpendEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
/*
    @RequiresPermissions("funds:projectExpend:save")
*/
    public ResponseMessage<ProjectExpendEO> create(@RequestBody ProjectExpendEO projectExpendEO) throws Exception {
        projectExpendEOService.insertSelective(projectExpendEO);
        return Result.success(projectExpendEO);
    }

    @ApiOperation(value = "|ProjectExpendEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
/*
    @RequiresPermissions("funds:projectExpend:update")
*/
    public ResponseMessage<ProjectExpendEO> update(@RequestBody ProjectExpendEO projectExpendEO) throws Exception {
        projectExpendEOService.updateByPrimaryKeySelective(projectExpendEO);
        return Result.success(projectExpendEO);
    }

/*    @ApiOperation(value = "|ProjectExpendEO|删除")
    @DeleteMapping("/{id}")
    @RequiresPermissions("funds:projectExpend:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        projectExpendEOService.deleteByPrimaryKey(id);
        logger.info("delete from RS_PROJECT_EXPEND where id = {}", id);
        return Result.success();
    }*/

    @ApiOperation(value = "|ProjectIncomeEO|删除")
    @PostMapping("/logicDelete")
/*
    @RequiresPermissions("funds:projectIncome:delete")
*/
    public ResponseMessage logicDelete(@RequestBody List<String> ids) throws Exception {
        projectExpendEOService.logicDeleteByPrimaryKeys(ids);
        return Result.success();
    }

    /***
    * @Description: 导出
    * @Param: [file]
    * @return: com.adc.da.util.http.ResponseMessage
    * @Author: yanyujie
    * @Date: 2020/11/12
    */
    @PostMapping("/Import")
    @ApiOperation(value = "经费支出导入")
    public ResponseMessage excelImport(HttpServletResponse response,@RequestParam("file") MultipartFile file) {
        try {
            InputStream is = file.getInputStream();
            final List errList = projectExpendEOService.excelImport(is);

            if (CollectionUtils.isEmpty(errList)){
                return Result.success();
            }
            String finalFileName = "经费支出导入错误列表.xlsx";
            response.setHeader("Content-Disposition", "attachment; filename=" + Encodes.urlEncode(finalFileName));
            response.setContentType("application/force-download");
            ExportParams exportParams = new ExportParams();
            exportParams.setType(ExcelType.XSSF);
            try {
                OutputStream outputStream = response.getOutputStream();
                Workbook workbook = ExcelExportUtil.exportExcel(exportParams, ErrorExpendVO.class,errList);
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
    public ResponseMessage exportExpertGroup(HttpServletResponse response, String fileName, @RequestBody ProjectExpendEOPage page)
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
            finalFileName = "经费支出导出数据.xlsx";
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
            Workbook workbook = projectExpendEOService.export(exportParams,page);
            workbook.write(outputStream);
            outputStream.flush();
        } catch (Exception e){
            logger.error(e.getMessage(), e);
            throw new AdcDaBaseException("导出失败，请重试");
        }
        return Result.success();
    }

    @ApiOperation(value = "查看支出详情（单一）")
    @GetMapping("/details")
    public ResponseMessage details(String id) throws Exception {
        final ProjectExpendVO projectExpendVO = projectExpendEOService.details(id);
        if (Objects.nonNull(projectExpendVO)){
            return Result.success(projectExpendVO);
        }
        return Result.error();
    }

    @ApiOperation(value = "查看入账详情（列表）")
    @GetMapping("/detailslist")
    public ResponseMessage detailsList(String projectId) throws Exception {
        final List<ProjectExpendVO> projectExpendVOS = projectExpendEOService.detailsList(projectId);
        if (CollectionUtils.isNotEmpty(projectExpendVOS)){
            return Result.success(projectExpendVOS);
        }
        return Result.error();
    }

}
