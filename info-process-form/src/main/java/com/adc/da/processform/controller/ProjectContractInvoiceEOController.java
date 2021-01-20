package com.adc.da.processform.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.excel.poi.excel.ExcelExportUtil;
import com.adc.da.excel.poi.excel.entity.ExportParams;
import com.adc.da.excel.poi.excel.entity.ImportParams;
import com.adc.da.excel.poi.excel.entity.enums.ExcelType;
import com.adc.da.processform.entity.ProjectContractInvoiceEO;
import com.adc.da.processform.page.ProjectContractInvoiceEOPage;
import com.adc.da.processform.service.ProjectContractInvoiceEOService;
import com.adc.da.processform.vo.ContractInvoiceVO;
import com.adc.da.util.http.PageInfo;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.utils.Encodes;
import com.adc.da.util.utils.UUID;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping("/${restPath}/processform/projectContractInvoice")
@Api(description = "|ProjectContractInvoiceEO|")
public class ProjectContractInvoiceEOController extends BaseController<ProjectContractInvoiceEO> {

    private static final Logger logger = LoggerFactory.getLogger(ProjectContractInvoiceEOController.class);

    @Autowired
    private ProjectContractInvoiceEOService projectContractInvoiceEOService;

    @ApiOperation(value = "|ProjectContractInvoiceEO|分页查询")
    @GetMapping("/page")
//    @RequiresPermissions("processform:projectContractInvoice:page")
    public ResponseMessage<PageInfo<ProjectContractInvoiceEO>> page(ProjectContractInvoiceEOPage page)
        throws Exception {
        List<ProjectContractInvoiceEO> rows = projectContractInvoiceEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

    @ApiOperation(value = "|ProjectContractInvoiceEO|查询")
    @GetMapping("")
//    @RequiresPermissions("processform:projectContractInvoice:list")
    public ResponseMessage<List<ProjectContractInvoiceEO>> list(ProjectContractInvoiceEOPage page) throws Exception {
        return Result.success(projectContractInvoiceEOService.queryByList(page));
    }

    @ApiOperation(value = "|ProjectContractInvoiceEO|详情")
    @GetMapping("/{id}")
//    @RequiresPermissions("processform:projectContractInvoice:get")
    public ResponseMessage<ProjectContractInvoiceEO> find(@PathVariable String id) throws Exception {
        return Result.success(projectContractInvoiceEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|ProjectContractInvoiceEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("processform:projectContractInvoice:save")
    public ResponseMessage<ProjectContractInvoiceEO> create(
        @RequestBody ProjectContractInvoiceEO projectContractInvoiceEO) throws Exception {
        projectContractInvoiceEOService.insertSelective(projectContractInvoiceEO);
        return Result.success(projectContractInvoiceEO);
    }

    @ApiOperation(value = "|ProjectContractInvoiceEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("processform:projectContractInvoice:update")
    public ResponseMessage<ProjectContractInvoiceEO> update(
        @RequestBody ProjectContractInvoiceEO projectContractInvoiceEO) throws Exception {
        projectContractInvoiceEOService.updateByPrimaryKeySelective(projectContractInvoiceEO);
        return Result.success(projectContractInvoiceEO);
    }

    @ApiOperation(value = "|ProjectContractInvoiceEO|批量修改")
    @PutMapping(value = "/updateAndInsertList", consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("processform:projectContractInvoice:update")
    public ResponseMessage<List<ProjectContractInvoiceEO>> updateList(
        @RequestBody List<ProjectContractInvoiceEO> projectContractInvoiceEOList) throws Exception {
        for (ProjectContractInvoiceEO projectContractInvoiceEO : projectContractInvoiceEOList) {
            if (null == projectContractInvoiceEO.getId()) {
                projectContractInvoiceEO.setId(UUID.randomUUID10());
                projectContractInvoiceEOService.insertSelective(projectContractInvoiceEO);
            } else {
                projectContractInvoiceEOService.updateByPrimaryKey(projectContractInvoiceEO);
            }
        }
        return Result.success(projectContractInvoiceEOList);
    }

    @ApiOperation(value = "|ProjectContractInvoiceEO|删除")
    @DeleteMapping("/{ids}")
//    @RequiresPermissions("processform:projectContractInvoice:delete")
    public ResponseMessage delete(@PathVariable String[] ids) throws Exception {
        for (String id : ids) {
            projectContractInvoiceEOService.deleteByPrimaryKey(id);
            logger.info("delete from PF_PROJECT_CONTRACT_INVOICE where id = {}", id);
        }
        return Result.success();
    }

    /**
     * 导入
     */
    @PostMapping("/check")
    @ApiOperation(value = "|ProjectContractInvoiceEO|导入")
//    @RequiresPermissions("fin:daily:import")
    public ResponseMessage excelImport(@RequestParam("file") MultipartFile file, HttpServletResponse response) throws Exception {
        String fileName = "问题开票列表.xlsx";
        OutputStream os = null;
        Workbook workbook = null;

        try {
            InputStream is = file.getInputStream();
            ImportParams params = new ImportParams();
//            params.setHeadRows(3);
            List<ContractInvoiceVO> contractInvoiceVOList = projectContractInvoiceEOService.check(is, params);
            response.setHeader(
                    "Content-Disposition",
                    "attachment; filename=" + Encodes.urlEncode(fileName));
            response.setContentType("application/force-download");
            ExportParams eparams = new ExportParams();
            eparams.setType(ExcelType.XSSF);
            workbook = ExcelExportUtil.exportExcel(eparams, ContractInvoiceVO.class, contractInvoiceVOList);
            os = response.getOutputStream();
            workbook.write(os);
            os.flush();
            return Result.success();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return Result.error("导入失败");
        }
    }

}
