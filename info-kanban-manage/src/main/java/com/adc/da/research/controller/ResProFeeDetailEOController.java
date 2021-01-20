package com.adc.da.research.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.adc.da.login.util.UserUtils;
import com.adc.da.pad.page.PadOperationManageEOPage;
import com.adc.da.research.entity.ResearchProjectEO;
import com.adc.da.research.service.ResearchProjectEOService;
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
import com.adc.da.research.entity.ResProFeeDetailEO;
import com.adc.da.research.page.ResProFeeDetailEOPage;
import com.adc.da.research.service.ResProFeeDetailEOService;

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
@RequestMapping("/${restPath}/research/resProFeeDetail")
@Api(tags = "科研项目资金使用情况表|ResProFeeDetailEO|")
public class ResProFeeDetailEOController extends BaseController<ResProFeeDetailEO>{

    private static final Logger logger = LoggerFactory.getLogger(ResProFeeDetailEOController.class);

    @Autowired
    private ResProFeeDetailEOService resProFeeDetailEOService;

	@ApiOperation(value = "|ResProFeeDetailEO|分页查询")
    @GetMapping("/page")
//    @RequiresPermissions("research:resProFeeDetail:page")
    public ResponseMessage<PageInfo<ResProFeeDetailEO>> page(ResProFeeDetailEOPage page) throws Exception {
        List<ResProFeeDetailEO> rows = resProFeeDetailEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|ResProFeeDetailEO|查询")
    @GetMapping("")
//    @RequiresPermissions("research:resProFeeDetail:list")
    public ResponseMessage<List<ResProFeeDetailEO>> list(ResProFeeDetailEOPage page) throws Exception {
        return Result.success(resProFeeDetailEOService.queryByList(page));
	}

    @ApiOperation(value = "|ResProFeeDetailEO|详情")
    @GetMapping("/{id}")
//    @RequiresPermissions("research:resProFeeDetail:get")
    public ResponseMessage<ResProFeeDetailEO> find(@PathVariable String id) throws Exception {
        return Result.success(resProFeeDetailEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|ResProFeeDetailEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("research:resProFeeDetail:save")
    public ResponseMessage<ResProFeeDetailEO> create(@RequestBody ResProFeeDetailEO resProFeeDetailEO) throws Exception {
        resProFeeDetailEOService.insertSelective(resProFeeDetailEO);
        return Result.success(resProFeeDetailEO);
    }

    @ApiOperation(value = "|ResProFeeDetailEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("research:resProFeeDetail:update")
    public ResponseMessage<ResProFeeDetailEO> update(@RequestBody ResProFeeDetailEO resProFeeDetailEO) throws Exception {
        resProFeeDetailEOService.updateByPrimaryKeySelective(resProFeeDetailEO);
        return Result.success(resProFeeDetailEO);
    }

    @ApiOperation(value = "|ResProFeeDetailEO|删除")
    @DeleteMapping("/{id}")
//    @RequiresPermissions("research:resProFeeDetail:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        resProFeeDetailEOService.deleteByPrimaryKey(id);
        logger.info("delete from DB_RES_PRO_FEE_DETAIL where id = {}", id);
        return Result.success();
    }


    /**
     * 导入
     */
    @PostMapping("/import")
    @ApiOperation(value = "|ResProFeeDetailEO|导入")
    public ResponseMessage excelImport(@RequestParam("file") MultipartFile file) {
        try {
            InputStream is = file.getInputStream();
            resProFeeDetailEOService.excelImport(is);
            return Result.success();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return Result.error(e.getMessage());
        }
    }


    @ApiOperation(value = "|ResProFeeDetailEO|导出")
    @GetMapping("/export")
    public ResponseMessage export(HttpServletResponse response, ResProFeeDetailEOPage page) {
        String fileName = "科研项目资金一览表导出数据.xlsx";
        OutputStream os = null;
        Workbook workbook = null;
        try {
            response.setHeader(
                    "Content-Disposition",
                    "attachment; filename=" + Encodes.urlEncode(fileName));
            response.setContentType("application/force-download");

            workbook = resProFeeDetailEOService.excelExport(page);
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
        resProFeeDetailEOService.deleteLogicInBatch(list);
        return Result.success();
    }

    @ApiOperation(value = "清空全部")
    @DeleteMapping("/deleteAll")
    public ResponseMessage deleteLogicAll(){
        resProFeeDetailEOService.deleteLogicAll();
        return Result.success();
    }
}
