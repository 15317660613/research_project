package com.adc.da.finance.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.finance.entity.CostManagementEO;
import com.adc.da.finance.page.CostManagementEOPage;
import com.adc.da.finance.page.MyCostManagementEOPage;
import com.adc.da.finance.service.CostManagementEOService;
import com.adc.da.login.util.UserUtils;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.http.PageInfo;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.utils.Encodes;
import com.adc.da.util.utils.IOUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping("/${restPath}/finance/costManagement")
@Api(tags = "财务管理|支出|CostManagementEO|")
public class CostManagementEOController extends BaseController<CostManagementEO> {

    private static final Logger logger = LoggerFactory.getLogger(CostManagementEOController.class);

    @Autowired
    private CostManagementEOService costManagementEOService;

    @ApiOperation(value = "|CostManagementEO|分页查询")
    @GetMapping("/page")
//    @RequiresPermissions("finance:costManagement:page")
    public ResponseMessage<PageInfo<CostManagementEO>> page(CostManagementEOPage page) throws Exception {
        List<CostManagementEO> rows = costManagementEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

    @ApiOperation(value = "|CostManagementEO|分页查询")
    @GetMapping("/pageByLoginUser/page")
//    @RequiresPermissions("finance:costManagement:page")
    public ResponseMessage<PageInfo<CostManagementEO>> pageByLoginUser(MyCostManagementEOPage page) throws Exception {
        UserEO userEO = UserUtils.getUser();
        if (null == userEO) {
            throw new AdcDaBaseException("登陆过期，请从新登陆！");
        }
        Subject subject = SecurityUtils.getSubject();
        if (!subject.hasRole("超级管理员") && !subject.hasRole("管理员") && !subject.hasRole("主任")) {
            page.setUserId(userEO.getUsid());
        }
        List<CostManagementEO> rows = costManagementEOService.pageByLoginUser(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

    @ApiOperation(value = "|CostManagementEO|查询")
    @GetMapping("")
//    @RequiresPermissions("finance:costManagement:list")
    public ResponseMessage<List<CostManagementEO>> list(CostManagementEOPage page) throws Exception {
        return Result.success(costManagementEOService.queryByList(page));
    }

    @ApiOperation(value = "|CostManagementEO|详情")
    @GetMapping("/{id}")
//    @RequiresPermissions("finance:costManagement:get")
    public ResponseMessage<CostManagementEO> find(@PathVariable String id) throws Exception {
        return Result.success(costManagementEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|CostManagementEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("finance:costManagement:save")
    public ResponseMessage<CostManagementEO> create(@RequestBody CostManagementEO costManagementEO) throws Exception {
        costManagementEOService.myCreate(costManagementEO);
        return Result.success(costManagementEO);
    }

    @ApiOperation(value = "|CostManagementEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("finance:costManagement:update")
    public ResponseMessage<CostManagementEO> update(@RequestBody CostManagementEO costManagementEO) throws Exception {
        costManagementEOService.myUpdateByPrimaryKeySelective(costManagementEO);
        return Result.success(costManagementEO);
    }

    @ApiOperation(value = "|CostManagementEO|删除")
    @DeleteMapping("/{id}")
//    @RequiresPermissions("finance:costManagement:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        costManagementEOService.deleteByPrimaryKey(id);
        logger.info("delete from F_COST_MANAGEMENT where id = {}", id);
        return Result.success();
    }

    @ApiOperation(value = "|CostManagementEO|批量逻辑删除")
    @DeleteMapping("/logicDelete/{ids}")
//    @RequiresPermissions("finance:costManagement:delete")
    public ResponseMessage logicDelete(@PathVariable String[] ids) throws Exception {
        costManagementEOService.logicDeleteByPrimaryKey(Arrays.asList(ids));
        logger.info("delete from F_COST_MANAGEMENT where id = {}", ids);
        return Result.success();
    }

    @ApiOperation(value = "|CostManagementEO|根据id退回")
    @PutMapping("/goBackByPrimaryKey")
//    @RequiresPermissions("finance:costManagement:delete")
    public ResponseMessage goBackByPrimaryKey(@RequestParam("id") String id) throws Exception {
        costManagementEOService.goBackByPrimaryKey(id);
        return Result.success();
    }

    @ApiOperation(value = "|CostManagementEO|根据id批量认领")
    @PutMapping("/clamList")
//    @RequiresPermissions("finance:costManagement:delete")
    public ResponseMessage clamList(@RequestBody CostManagementEO[] costManagementEOArr) throws Exception {
        costManagementEOService.clamList(costManagementEOArr);
        return Result.success();
    }

    /**
     * 导入
     */
    @PostMapping("/import")
    @ApiOperation(value = "|CostManagementEO|导入")
    public ResponseMessage excelImport(@RequestParam("file") MultipartFile file) {
        try {
            InputStream is = file.getInputStream();
            costManagementEOService.excelImport(is);
            return Result.success();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    @ApiOperation(value = "|CostManagementEO|导出")
    @GetMapping("/export")
    public ResponseMessage export(HttpServletResponse response, MyCostManagementEOPage costManagementEOPage) {
        costManagementEOPage.setPageSize(15000);
        String fileName = "成本数据表.xlsx";
        OutputStream os = null;
        Workbook workbook = null;
        UserEO userEO = UserUtils.getUser();
        if (null == userEO) {
            throw new AdcDaBaseException("登陆过期，请从新登陆！");
        }
        Subject subject = SecurityUtils.getSubject();
        if (!subject.hasRole("超级管理员") && !subject.hasRole("管理员") && !subject.hasRole("主任")) {
            costManagementEOPage.setUserId(userEO.getUsid());
        }

        try {
            response.setHeader(
                    "Content-Disposition",
                    "attachment; filename=" + Encodes.urlEncode(fileName));
            response.setContentType("application/force-download");

            workbook = costManagementEOService.excelExport(costManagementEOPage);
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

}
