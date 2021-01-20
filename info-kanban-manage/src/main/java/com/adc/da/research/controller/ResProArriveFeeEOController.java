package com.adc.da.research.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import cn.afterturn.easypoi.exception.excel.ExcelImportException;
import com.adc.da.login.util.UserUtils;
import com.adc.da.pad.page.PadOperationManageEOPage;
import com.adc.da.research.entity.ResearchProjectEO;
import com.adc.da.research.page.ResProFeeDetailEOPage;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.Encodes;
import com.adc.da.util.utils.IOUtils;
import com.adc.da.util.utils.UUID;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.adc.da.base.web.BaseController;
import com.adc.da.research.entity.ResProArriveFeeEO;
import com.adc.da.research.page.ResProArriveFeeEOPage;
import com.adc.da.research.service.ResProArriveFeeEOService;

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
@RequestMapping("/${restPath}/research/resProArriveFee")
@Api(tags = "科研项目资金到帐表|ResProArriveFeeEO|")
public class ResProArriveFeeEOController extends BaseController<ResProArriveFeeEO>{

    private static final Logger logger = LoggerFactory.getLogger(ResProArriveFeeEOController.class);

    @Autowired
    private ResProArriveFeeEOService resProArriveFeeEOService;

	@ApiOperation(value = "|ResProArriveFeeEO|分页查询")
    @GetMapping("/page")
//    @RequiresPermissions("research:resProArriveFee:page")
    public ResponseMessage<PageInfo<ResProArriveFeeEO>> page(ResProArriveFeeEOPage page) throws Exception {
        List<ResProArriveFeeEO> rows = resProArriveFeeEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|ResProArriveFeeEO|查询")
    @GetMapping("")
//    @RequiresPermissions("research:resProArriveFee:list")
    public ResponseMessage<List<ResProArriveFeeEO>> list(ResProArriveFeeEOPage page) throws Exception {
        return Result.success(resProArriveFeeEOService.queryByList(page));
	}

    @ApiOperation(value = "|ResProArriveFeeEO|详情")
    @GetMapping("/{id}")
//    @RequiresPermissions("research:resProArriveFee:get")
    public ResponseMessage<ResProArriveFeeEO> find(@PathVariable String id) throws Exception {
        return Result.success(resProArriveFeeEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|ResProArriveFeeEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("research:resProArriveFee:save")
    public ResponseMessage<ResProArriveFeeEO> create(@RequestBody ResProArriveFeeEO resProArriveFeeEO) throws Exception {
        resProArriveFeeEOService.insertSelective(resProArriveFeeEO);
        return Result.success(resProArriveFeeEO);
    }

    @ApiOperation(value = "|ResProArriveFeeEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("research:resProArriveFee:update")
    public ResponseMessage<ResProArriveFeeEO> update(@RequestBody ResProArriveFeeEO resProArriveFeeEO) throws Exception {
        resProArriveFeeEOService.updateByPrimaryKeySelective(resProArriveFeeEO);
        return Result.success(resProArriveFeeEO);
    }

    @ApiOperation(value = "|ResProArriveFeeEO|删除")
    @DeleteMapping("/{id}")
//    @RequiresPermissions("research:resProArriveFee:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        resProArriveFeeEOService.deleteByPrimaryKey(id);
        logger.info("delete from DB_RES_PRO_ARRIVE_FEE where id = {}", id);
        return Result.success();
    }
    /**
     * 导入
     */
    @PostMapping("/import")
    @ApiOperation(value = "|ResProArriveFeeEO|导入")
    public ResponseMessage excelImport(@RequestParam("file") MultipartFile file) {
        try {
            InputStream is = file.getInputStream();
            resProArriveFeeEOService.excelImport(is);
            return Result.success();
        } catch (ExcelImportException ex){
            logger.error(ex.getMessage());
            return Result.error("请检查日期、年月等数据格式，按照模板填写！");
        } catch (Exception e) {
            logger.error(e.getMessage());
            return Result.error(e.getMessage());
        }
    }



    @ApiOperation(value = "|ResProArriveFeeEO|导出")
    @GetMapping("/export")
    public ResponseMessage export(HttpServletResponse response, ResProArriveFeeEOPage page) {
        String fileName = "科研项目到账表导出数据.xlsx";
        OutputStream os = null;
        Workbook workbook = null;
        try {
            response.setHeader(
                    "Content-Disposition",
                    "attachment; filename=" + Encodes.urlEncode(fileName));
            response.setContentType("application/force-download");

            workbook = resProArriveFeeEOService.excelExport(page);
            os = response.getOutputStream();
            workbook.write(os);
            os.flush();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new AdcDaBaseException("导出失败，请重试");
        } finally {
            IOUtils.closeQuietly(os);
        }
        return Result.success();
    }

    @ApiOperation(value = "批量逻辑删除")
    @DeleteMapping("/del/{ids}")
//    @RequiresPermissions("pad:padOperationManage:delete")
    public ResponseMessage deleteLogicInBatch(@NotNull @PathVariable("ids") String[] ids){
        List<String> list = Arrays.asList(ids);
        resProArriveFeeEOService.deleteLogicInBatch(list);
        return Result.success();
    }

    @ApiOperation(value = "清空全部")
    @DeleteMapping("/deleteAll")
    public ResponseMessage deleteLogicAll(){
        resProArriveFeeEOService.deleteLogicAll();
        return Result.success();
    }

}
