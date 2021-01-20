package com.adc.da.finance.controller;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.adc.da.base.web.BaseController;
import com.adc.da.finance.entity.BusinessGonfigEO;
import com.adc.da.finance.handler.BusinessGonfigDtoHandler;
import com.adc.da.finance.page.BusinessGonfigEOPage;
import com.adc.da.finance.page.MyBusinessGonfigEOPage;
import com.adc.da.finance.service.BusinessGonfigEOService;
import com.adc.da.login.util.UserUtils;
import com.adc.da.sys.entity.UserEO;
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
@RequestMapping("/${restPath}/finance/businessGonfig")
@Api(description = "|BusinessGonfigEO|经营业务配置")
@Slf4j
public class BusinessGonfigEOController extends BaseController<BusinessGonfigEO>{

    private static final Logger logger = LoggerFactory.getLogger(BusinessGonfigEOController.class);

    @Autowired
    private BusinessGonfigEOService businessGonfigEOService;

	@ApiOperation(value = "|BusinessGonfigEO|分页查询")
    @GetMapping("/page")
    //@RequiresPermissions("finance:businessGonfig:page")
    public ResponseMessage<PageInfo<BusinessGonfigEO>> page(BusinessGonfigEOPage page) throws Exception {
        List<BusinessGonfigEO> rows = businessGonfigEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|BusinessGonfigEO|查询")
    @GetMapping("/list")
    //@RequiresPermissions("finance:businessGonfig:list")
    public ResponseMessage<List<BusinessGonfigEO>> list(BusinessGonfigEOPage page) throws Exception {
        return Result.success(businessGonfigEOService.queryByList(page));
	}

    @ApiOperation(value = "|BusinessGonfigEO|查询")
    @GetMapping("/getBusinessPageByLoginUser")
    public ResponseMessage<List<BusinessGonfigEO>> getBusinessListByLoginUser() throws Exception {
        UserEO userEO = UserUtils.getUser();
        if (null == userEO) {
            throw new AdcDaBaseException("登陆可能过期！");
        }
        return Result.success(businessGonfigEOService.getBusinessListByLoginUser(userEO.getUsid()));
    }

    @ApiOperation(value = "|BusinessGonfigEO|查询")
    @GetMapping("/getBusinessPage")
    public ResponseMessage<PageInfo<BusinessGonfigEO>> getBusinessPage(MyBusinessGonfigEOPage page) throws Exception {
        UserEO userEO = UserUtils.getUser();
        if (null == userEO) {
            throw new AdcDaBaseException("登陆可能过期！");
        }
        page.setUserId(userEO.getUsid());
        List<BusinessGonfigEO> rows = businessGonfigEOService.getBusinessPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

    @ApiOperation(value = "|BusinessGonfigEO|详情")
    @GetMapping("/find/{id}")
    //@RequiresPermissions("finance:businessGonfig:get")
    public ResponseMessage<BusinessGonfigEO> find(@PathVariable String id) throws Exception {
        return Result.success(businessGonfigEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|BusinessGonfigEO|新增")
    @PostMapping(value = "/create",consumes = APPLICATION_JSON_UTF8_VALUE)
    //@RequiresPermissions("finance:businessGonfig:save")
    public ResponseMessage<BusinessGonfigEO> create(@RequestBody BusinessGonfigEO businessGonfigEO) throws Exception {
        businessGonfigEOService.create(businessGonfigEO);
        return Result.success(businessGonfigEO);
    }

    @ApiOperation(value = "|BusinessGonfigEO|修改")
    @PutMapping(value = "/update",consumes = APPLICATION_JSON_UTF8_VALUE)
    //@RequiresPermissions("finance:businessGonfig:update")
    public ResponseMessage<BusinessGonfigEO> update(@RequestBody BusinessGonfigEO businessGonfigEO) throws Exception {
        businessGonfigEOService.update(businessGonfigEO);
        return Result.success(businessGonfigEO);
    }

    @ApiOperation(value = "|BusinessGonfigEO|单条/批量逻辑删除")
    @DeleteMapping("/logicDelete/{ids}")
    //@RequiresPermissions("finance:businessGonfig:delete")
    public ResponseMessage logicDelete(@PathVariable List<String> ids) throws Exception {
        businessGonfigEOService.logicDelete(ids);
        return Result.success();
    }

    @ApiOperation(value = "|BusinessGonfigEO|导入经营业务/科研业务/成本数据")
    @PostMapping("/importBusinessGonfig")
    public ResponseMessage importBusinessGonfig(@RequestParam("file")MultipartFile file,String bgType) throws Exception{
        InputStream inputStream = file.getInputStream();
        ImportParams importParams = new ImportParams();
        //设置验证
        importParams.setNeedVerfiy(true);
        String errors = businessGonfigEOService.importBusinessGonfig(inputStream, importParams,bgType);
        ResponseMessage result = Result.success();
        if(StringUtils.isNotEmpty(errors)){
            return Result.error(errors);
        }
        return result;
    }

    @ApiOperation(value = "|BusinessGonfigEO|导出业务数据/科研业务/成本数据")
    @GetMapping("/exportBusinessGonfig")
    public ResponseMessage exportBusinessGonfig(HttpServletResponse response,String fileName,BusinessGonfigEOPage businessGonfigEOPage) throws Exception{
        String finalFileName = "";
        if (StringUtils.isEmpty(fileName)){
            if(businessGonfigEOPage.getBgType().equals("0")){
                finalFileName = "经营业务导出数据.xlsx";
            }else if (businessGonfigEOPage.getBgType().equals("1")){
                finalFileName = "科研课题导出数据.xlsx";
            }else{
                finalFileName = "成本导出数据.xlsx";
            }
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
            Workbook workBook = businessGonfigEOService.exportBusinessGonfig(exportParams,businessGonfigEOPage);
            workBook.write(outputStream);
            outputStream.flush();
        } catch (Exception e){
            log.error(e.getMessage(), e);
            throw new AdcDaBaseException("导出失败，请重试");
        }
        return Result.success();
    }

}
