package com.adc.da.research.project.controller;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.adc.da.base.web.BaseController;
import com.adc.da.research.project.entity.ProjectDataEO;
import com.adc.da.research.project.page.ProjectDataEOPage;
import com.adc.da.research.project.service.ProjectDataEOService;
import com.adc.da.research.project.vo.ProjectContractInfoVO;
import com.adc.da.research.project.vo.ProjectDataVO;
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

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

/**
 * 项目信息
 */
@RestController
@RequestMapping("/${restPath}/research/project/projectData")
@Api(tags = "科研系统|项目管理-->项目信息|ProjectDataEOController")
public class ProjectDataEOController extends BaseController<ProjectDataEO> {

    private static final Logger logger = LoggerFactory.getLogger(ProjectDataEOController.class);

    @Autowired
    private ProjectDataEOService projectDataEOService;

	@ApiOperation(value = "|ProjectDataEO|分页项目一览查询")
    @GetMapping("/page")
    public ResponseMessage<PageInfo<ProjectDataEO>> page(ProjectDataEOPage page) throws Exception {

        List<ProjectDataEO> rows = projectDataEOService.queryByExpert(page);

        return Result.success(getPageInfo(page.getPager(), rows));
    }

    @ApiOperation(value = "|ProjectDataEO|分页项目申报查询")
    @GetMapping("/declarePage")
    public ResponseMessage<PageInfo<ProjectDataEO>> declarePage(ProjectDataEOPage page) throws Exception {

        List<ProjectDataEO> rows = projectDataEOService.queryBydeclare(page);

        return Result.success(getPageInfo(page.getPager(), rows));
    }

    @ApiOperation(value = "|ProjectDataEo|分页立项签约查询")
    @GetMapping("/setPage")
    public ResponseMessage<PageInfo<ProjectDataEO>> setPage(ProjectDataEOPage page) throws Exception{

	    List<ProjectDataEO> list=projectDataEOService.queryBySet(page);

        return Result.success(getPageInfo(page.getPager(), list));
    }


    @ApiOperation(value = "|ProjectDataEo|分页项目执行查询")
    @GetMapping("/performPage")
    public ResponseMessage<PageInfo<ProjectDataEO>> performPage(ProjectDataEOPage page) throws Exception {

        List<ProjectDataEO> list = projectDataEOService.queryByPerform(page);

        return Result.success(getPageInfo(page.getPager(), list));
    }
    @ApiOperation(value = "|ProjectDataEo|分页项目验收查询")
    @GetMapping("/acceptancePage")
    public ResponseMessage<PageInfo<ProjectDataEO>> acceptancePage(ProjectDataEOPage page) throws Exception{
	    List<ProjectDataEO> list=projectDataEOService.queryByacceptancePage(page);

	    return Result.success(getPageInfo(page.getPager(),list));

    }


    @ApiOperation(value = "|ProjectDataEo|分页项目变更查询")
    @GetMapping("/changePage")
    public ResponseMessage<PageInfo<ProjectDataEO>> queryByChangePage(ProjectDataEOPage page) throws Exception{

        List<ProjectDataEO> list=projectDataEOService.queryByChangePage(page);

        return Result.success(getPageInfo(page.getPager(), list));
    }

    @ApiOperation(value = "|ProjectDataEO|项目申报分页查询接口查询-项目编号：projectCode 、 " +
            "项目名称：projectName 、 项目类别：projectTypeId 、 资金区间 ：totalFundingStart - totalFundingEnd，申报时间 applicationTime1-applicationTime2 、" +
            "申报单位：reportingUnitName 、阶段:stageName")
    @GetMapping("/declarePage4Tips")
    public ResponseMessage<PageInfo<ProjectDataEO>> declarePage4Tips(ProjectDataEOPage page) throws Exception {

        List<ProjectDataEO> rows = projectDataEOService.declarePage4Tips(page);

        return Result.success(getPageInfo(page.getPager(), rows));
    }


	@ApiOperation(value = "|ProjectDataEO|查询")
    @GetMapping("")
    public ResponseMessage<List<ProjectDataEO>> list(ProjectDataEOPage page) throws Exception {
        return Result.success(projectDataEOService.queryByList(page));
	}

    @ApiOperation(value = "|ProjectDataEO|详情")
    @GetMapping("/{id}")
    public ResponseMessage<ProjectDataEO> find(@PathVariable String id) throws Exception {
        return Result.success(projectDataEOService.selectByPrimaryKey(id));
    }
    @ApiOperation(value = "|ProjectDataEO|项目信息详情")
    @GetMapping("/findById/{id}")
    public ResponseMessage<ProjectDataEO> findById(@PathVariable String id) throws Exception {
        return Result.success(projectDataEOService.selectByProjectId(id));
    }

    @ApiOperation(value = "|ProjectDataEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage<ProjectDataEO> create(@RequestBody ProjectDataEO projectDataEO) throws Exception {
        projectDataEOService.insertSelective(projectDataEO);
        return Result.success(projectDataEO);
    }

    @ApiOperation(value = "|ProjectDataEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage<String> update(@RequestBody ProjectDataEO projectDataEO) throws Exception {
        projectDataEOService.updateByPrimaryKeySelective(projectDataEO);
        return Result.success("success");
    }

    @ApiOperation(value = "|ProjectDataEO|删除")
    @DeleteMapping("/{id}")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        projectDataEOService.deleteByPrimaryKey(id);
        logger.info("delete from RS_PROJECT_DATA where id = {}", id);
        return Result.success();
    }

    @ApiOperation(value = "|ProjectDataEO|导出项目一览数据")
    @GetMapping("/exportProjectDataEO")
    public ResponseMessage exportExpertGroup(HttpServletResponse response, String fileName, ProjectDataEOPage eoPage){
        String finalFileName = "";
        if (StringUtils.isEmpty(fileName)){
            finalFileName = "项目一览导出数据.xlsx";
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
            Workbook workbook = projectDataEOService.exportProjectData(exportParams,eoPage);
            workbook.write(outputStream);
            outputStream.flush();
        } catch (Exception e){
            logger.error(e.getMessage(), e);
            throw new AdcDaBaseException("导出失败，请重试");
        }
        return Result.success();
    }


    @ApiOperation(value = "新增和修改")
    @PostMapping("/addOrUpdate")
    public ResponseMessage<ProjectDataEO> addOrUpdate(@RequestBody ProjectDataVO projectDataVO) throws Exception {
        projectDataEOService.addOrUpdate(projectDataVO);
        return Result.success();
    }

    @ApiOperation(value = "科研项目合同信息查询")
    @GetMapping("/getProjectContractInfo")
    public ResponseMessage getProjectContractInfo( ProjectContractInfoVO page ) {

        List<ProjectDataEOPage> projectContractInfo = projectDataEOService.getProjectContractInfo(page);

        return Result.success(projectContractInfo);
    }



    @ApiOperation(value = "项目提交")
    @PostMapping("/projectSubmit")
    public ResponseMessage projectSubmit(@RequestBody ProjectDataEO projectDataEO) {
        projectDataEOService.projectSubmit(projectDataEO);
        return Result.success("提交成功");
    }




    @ApiOperation(value = "|科研项目合同书")
/*
    @PostMapping(value = "/downLoadResearch")
*/
    @PostMapping(value = "/downLoadCatarc")
    //@RequiresPermissions("order:projectPlan:update")
    public void downLoadMonthReport(HttpServletResponse response, String projectId, String fileName) throws Exception{
        projectDataEOService.downLoadMonthReport(response,projectId,fileName);

    }

    @ApiOperation(value = "|中汽中心科研项目合同书")
/*
    @PostMapping(value = "/downLoadCatarc")
*/
    @PostMapping(value = "/downLoadResearch")
    //@RequiresPermissions("order:projectPlan:update")
    public void downLoadCatarc(HttpServletResponse response, String projectId, String fileName) throws Exception{
        projectDataEOService.downLoadCatarc(response,projectId,fileName);

    }


    @ApiOperation(value = "立项撤回")
    @PostMapping("/projectRecall")
    public ResponseMessage projectRecall(@RequestBody ProjectDataEO projectDataEO) {
        projectDataEOService.projectRecall(projectDataEO);
        return Result.success("撤回成功");
    }
    @ApiOperation(value = "立项删除")
    @PostMapping("/projectDelete")
    public ResponseMessage projectDelete(@RequestBody ProjectDataEO projectDataEO) {
        projectDataEOService.projectDelete(projectDataEO);
        return Result.success("删除成功");
    }

}
