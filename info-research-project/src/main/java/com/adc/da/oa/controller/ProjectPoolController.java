package com.adc.da.oa.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.budget.entity.Project;
import com.adc.da.excel.poi.excel.entity.ExportParams;
import com.adc.da.excel.poi.excel.entity.enums.ExcelType;
import com.adc.da.oa.page.ProjectPoolPage;
import com.adc.da.oa.service.ProjectPoolExportService;
import com.adc.da.oa.service.ProjectPoolService;
import com.adc.da.oa.vo.ClaimVO;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.http.PageInfo;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.utils.Encodes;
import com.adc.da.util.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

/**
 * OA数据同步
 */
@RestController
@RequestMapping("/${restPath}/project/pool")
@Api(tags = "|Project项目池|")
@Slf4j
public class ProjectPoolController extends BaseController<Project> {

    @Autowired
    private ProjectPoolService projectPoolService;

    @ApiOperation(value = "项目池查询")
    @PostMapping(value = "/page", consumes = APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage<PageInfo<Project>> page(@RequestBody ProjectPoolPage queryPage) {

        return Result.success(projectPoolService.queryByPage(queryPage));
    }

    @ApiOperation(value = "申领合同")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage<List<Project>> page(@RequestBody List<ClaimVO> voList) throws Exception {

        try {
            return Result.success(projectPoolService.claimProject(voList));

        } catch (AdcDaBaseException e) {

            return Result.error("-1", e.getMessage(), null);
        }
    }

    /**
     * 仅限管理员
     *
     * @param projectList
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "修改合同所属部门")
    @PostMapping(value = "/updateDeptId", consumes = APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage<List<Project>> changeDept(@RequestBody List<Project> projectList) throws Exception {

        try {
            return Result.success(projectPoolService.changeDept(projectList));

        } catch (AdcDaBaseException e) {

            return Result.error("-1", e.getMessage(), null);
        }
    }

    @Autowired
    ProjectPoolExportService projectPoolExportService;

    @ApiOperation(value = "项目池导出")
    @GetMapping("/projectPoolExport")
    //@RequiresPermissions("fin:project:export")
    public ResponseMessage excelImport(HttpServletResponse response, String fileName, Long startLong, Long endLong) {
        String resultName;
        if (null == startLong || null == endLong) {
            throw new AdcDaBaseException("请指定时间范围");
        }

        if (StringUtils.isEmpty(fileName)) {
            resultName = "项目池.xlsx";
        } else {
            if (!fileName.contains(".xlsx")) {
                resultName = fileName + ".xlsx";
            } else {
                resultName = fileName;
            }
        }
        response.setHeader(
            "Content-Disposition",
            "attachment; filename=" + Encodes.urlEncode(resultName));
        response.setContentType("application/force-download");
        ExportParams params = new ExportParams();
        params.setType(ExcelType.XSSF);
        try (OutputStream os = response.getOutputStream();
             Workbook workbook = projectPoolExportService.excelExport(params)) {
            workbook.write(os);
            os.flush();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new AdcDaBaseException("导出失败，请重试");
        }
        return Result.success();
    }

}
