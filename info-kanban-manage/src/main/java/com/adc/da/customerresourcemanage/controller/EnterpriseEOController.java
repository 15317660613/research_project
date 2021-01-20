package com.adc.da.customerresourcemanage.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.adc.da.excel.poi.excel.entity.ExportParams;
import com.adc.da.excel.poi.excel.entity.ImportParams;
import com.adc.da.excel.poi.excel.entity.enums.ExcelType;
import com.adc.da.excel.poi.excel.entity.result.ExcelVerifyHanlderErrorResult;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.Encodes;
import com.adc.da.util.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.adc.da.base.web.BaseController;
import com.adc.da.customerresourcemanage.entity.EnterpriseEO;
import com.adc.da.customerresourcemanage.page.EnterpriseEOPage;
import com.adc.da.customerresourcemanage.service.EnterpriseEOService;

import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.http.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/${restPath}/customerresourcemanage/enterprise")
@Api(description = "|EnterpriseEO|企业")
@Slf4j
public class EnterpriseEOController extends BaseController<EnterpriseEO>{

    private static final Logger logger = LoggerFactory.getLogger(EnterpriseEOController.class);

    @Autowired
    private EnterpriseEOService enterpriseEOService;

	@ApiOperation(value = "|EnterpriseEO|分页查询")
    @GetMapping("/page")
    //@RequiresPermissions("customerresourcemanage:enterprise:page")
    public ResponseMessage<PageInfo<EnterpriseEO>> page(EnterpriseEOPage page) throws Exception {
        List<EnterpriseEO> rows = enterpriseEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|EnterpriseEO|查询")
    @GetMapping("/list")
    //@RequiresPermissions("customerresourcemanage:enterprise:list")
    public ResponseMessage<List<EnterpriseEO>> list(EnterpriseEOPage page) throws Exception {
        return Result.success(enterpriseEOService.queryByList(page));
	}

    @ApiOperation(value = "|EnterpriseEO|详情")
    @GetMapping("/find/{id}")
    //@RequiresPermissions("customerresourcemanage:enterprise:get")
    public ResponseMessage<EnterpriseEO> find(@PathVariable String id) throws Exception {
        return Result.success(enterpriseEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|EnterpriseEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE,value = "/create")
    //@RequiresPermissions("customerresourcemanage:enterprise:save")
    public ResponseMessage<EnterpriseEO> create(@RequestBody EnterpriseEO enterpriseEO) throws Exception {
        enterpriseEOService.create(enterpriseEO);
        return Result.success(enterpriseEO);
    }

    @ApiOperation(value = "|EnterpriseEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    //@RequiresPermissions("customerresourcemanage:enterprise:update")
    public ResponseMessage<EnterpriseEO> update(@RequestBody EnterpriseEO enterpriseEO) throws Exception {
        enterpriseEOService.update(enterpriseEO);
        return Result.success(enterpriseEO);
    }

    @ApiOperation(value = "|EnterpriseEO|逻辑删除")
    @DeleteMapping("/logicDelete/{id}")
    //@RequiresPermissions("customerresourcemanage:enterprise:delete")
    public ResponseMessage logicDelete(@PathVariable String id) throws Exception {
        enterpriseEOService.logicDeleteByPrimaryKey(id);
        return Result.success();
    }
    @ApiOperation(value = "|EnterpriseEO|批量逻辑删除")
    @DeleteMapping("/batchLogicDelete/{ids}")
    public ResponseMessage batchLogicDelete(@NotNull @PathVariable("ids") String[] ids) throws Exception{
        List<String> enterpriseIds = Arrays.asList(ids);
        enterpriseEOService.batchLogicDeleteByPrimaryKeys(enterpriseIds);
        return Result.success();
    }

    @ApiOperation(value = "|EnterpriseEO|导入企业信息")
    @PostMapping("/excelImportEnterprise")
    public ResponseMessage excelImportEnterprise(@RequestParam("file") MultipartFile file) throws Exception{
        InputStream inputStream = file.getInputStream();
        ImportParams importParams = new ImportParams();
        List<ExcelVerifyHanlderErrorResult> errors =
                enterpriseEOService.importEnterprise(inputStream, importParams);
        ResponseMessage result = Result.success();
        if (errors!=null && !errors.isEmpty()){
            result.setMessage(errors.toString());
        }
        return result;
    }

    @ApiOperation(value = "|EnterpriseEO|导出企业信息")
    @GetMapping("/excelExportEnterprise")
    public ResponseMessage excelExportEnterprise(HttpServletResponse response, String fileName){
        String finalFileName = "";
        if (StringUtils.isEmpty(fileName)){
            finalFileName = "企业导出数据.xlsx";
        }else{
            if (!fileName.contains(".xlsx") || !fileName.contains("xls")){
                finalFileName = fileName + ".xlsx";
            }else{
                finalFileName = fileName;
            }
        }

        response.setHeader("Content-Disposition", "attachment; filename=" + Encodes.urlEncode(finalFileName));
        response.setContentType("application/force-download");
        ExportParams params = new ExportParams();
        params.setType(ExcelType.XSSF);
        try {
            OutputStream os = response.getOutputStream();
            Workbook workbook = enterpriseEOService.excelExportEnterprise(params);
            workbook.write(os);
            os.flush();
        }catch (Exception e){
            log.error(e.getMessage(), e);
            throw new AdcDaBaseException("导出失败，请重试");
        }
        return Result.success();
    }

    @ApiOperation(value = "|EnterpriseEO|验证企业名称重复")
    @GetMapping("/verifyEnterpriseName")
    public ResponseMessage verifyEnterpriseName (@RequestParam("enterpriseName") String enterpriseName) throws Exception{
	    enterpriseEOService.verifyEnterpriseName(enterpriseName);
        return Result.success();
    }

}
