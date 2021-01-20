package com.adc.da.carsales.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.io.InputStream;
import java.io.OutputStream;
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
import com.adc.da.carsales.entity.CarSalesEO;
import com.adc.da.carsales.page.CarSalesEOPage;
import com.adc.da.carsales.service.CarSalesEOService;

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
@RequestMapping("/${restPath}/carsales/carSales")
@Api(description = "|CarSalesEO|车企月度销量")
@Slf4j
public class CarSalesEOController extends BaseController<CarSalesEO>{

    private static final Logger logger = LoggerFactory.getLogger(CarSalesEOController.class);

    @Autowired
    private CarSalesEOService carSalesEOService;

	@ApiOperation(value = "|CarSalesEO|分页查询")
    @GetMapping("/page")
    //@RequiresPermissions("carsales:carSales:page")
    public ResponseMessage<PageInfo<CarSalesEO>> page(CarSalesEOPage page) throws Exception {
        List<CarSalesEO> rows = carSalesEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|CarSalesEO|不分页查询")
    @GetMapping("/list")
    //@RequiresPermissions("carsales:carSales:list")
    public ResponseMessage<List<CarSalesEO>> list(CarSalesEOPage page) throws Exception {
        return Result.success(carSalesEOService.queryByList(page));
	}

    @ApiOperation(value = "|CarSalesEO|详情")
    @GetMapping("/{id}")
    //@RequiresPermissions("carsales:carSales:get")
    public ResponseMessage<CarSalesEO> find(@PathVariable String id) throws Exception {
        return Result.success(carSalesEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|CarSalesEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE,value = "/create")
    //@RequiresPermissions("carsales:carSales:save")
    public ResponseMessage<CarSalesEO> create(@RequestBody CarSalesEO carSalesEO) throws Exception {
        carSalesEOService.create(carSalesEO);
        return Result.success(carSalesEO);
    }

    @ApiOperation(value = "|CarSalesEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE,value = "/update")
    //@RequiresPermissions("carsales:carSales:update")
    public ResponseMessage<CarSalesEO> update(@RequestBody CarSalesEO carSalesEO) throws Exception {
        carSalesEOService.update(carSalesEO);
        return Result.success(carSalesEO);
    }

    @ApiOperation(value = "|CarSalesEO|删除")
    @DeleteMapping("/delete/{id}")
    //@RequiresPermissions("carsales:carSales:delete")
    public ResponseMessage delete(@NotNull @PathVariable String id) throws Exception {
        carSalesEOService.logicDeleteByPrimaryKey(id);
        return Result.success();
    }

    @ApiOperation(value = "|CarSalesEO|批量删除")
    @DeleteMapping("/batchDelete/{ids}")
    public ResponseMessage batchDelete(@NotNull @PathVariable("ids") List<String> ids) throws Exception{
        carSalesEOService.batchLogicDeleteByIds(ids);
        return Result.success();
    }

    @ApiOperation(value="|CarSalesEO|逻辑删除全部")
    @DeleteMapping("/deleteAll")
    public ResponseMessage deleteAll() throws Exception{
	    carSalesEOService.deleteAll();
	    return Result.success();
    }

    @ApiOperation(value = "|CarSalesEO|导入车企月度销量")
    @PostMapping("/excelImportCarSales")
    public ResponseMessage excelImportCarSales(@RequestParam("file")MultipartFile file) throws Exception{
        InputStream inputStream = file.getInputStream();
        ImportParams importParams = new ImportParams();
        List<ExcelVerifyHanlderErrorResult> errors = carSalesEOService.excelImportCarSales(inputStream, importParams);
        ResponseMessage result = Result.success();
        if (errors.size()!=0){
            result.setMessage(errors.toString());
        }
        return result;
    }

    @ApiOperation(value = "|CarSalesEO|导出车企月度销量")
    @GetMapping("/excelExportCarSales")
    public ResponseMessage excelExportCarSales(HttpServletResponse response,String fileName) throws  Exception{
	    String finalFileName = "";
	    if (StringUtils.isEmpty(fileName)){
	        finalFileName = "车企月度销量导出数据.xlsx";
        }else {
	        if (!fileName.contains(".xlsx") || !fileName.contains(".xls")){
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
            OutputStream ot = response.getOutputStream();
            Workbook workbook = carSalesEOService.excelExportCarSales(params);
            workbook.write(ot);
            ot.flush();
        } catch (Exception e){
            log.error(e.getMessage(), e);
            throw new AdcDaBaseException("导出失败，请重试");
        }
        return Result.success();
    }

    @ApiOperation(value = "|CarSalesEO|车企月度销量排行")
    @GetMapping("/carSalesRanking")
    public ResponseMessage<List<CarSalesEO>> carSalesRanking () throws Exception{
        List<CarSalesEO> list = carSalesEOService.carSalesRanking();
	    return Result.success(list);
    }

}
