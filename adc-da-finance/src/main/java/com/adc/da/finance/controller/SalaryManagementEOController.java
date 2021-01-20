package com.adc.da.finance.controller;

import cn.hutool.core.date.DateTime;
import com.adc.da.base.web.BaseController;
import com.adc.da.finance.entity.SalaryManagementEO;
import com.adc.da.finance.page.SalaryManagementEOPage;
import com.adc.da.finance.service.SalaryManagementEOService;
import com.adc.da.login.util.UserUtils;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.http.PageInfo;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.utils.Encodes;
import com.adc.da.util.utils.IOUtils;
import com.adc.da.util.utils.StringUtils;
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
import java.util.Arrays;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping("/${restPath}/finance/salaryManagement")
@Api(tags = "财务管理|薪酬|SalaryManagementEO|")
public class SalaryManagementEOController extends BaseController<SalaryManagementEO> {

    private static final Logger logger = LoggerFactory.getLogger(SalaryManagementEOController.class);

    @Autowired
    private SalaryManagementEOService salaryManagementEOService;

    @ApiOperation(value = "|SalaryManagementEO|分页查询")
    @GetMapping("/page")
//    @RequiresPermissions("finance:salaryManagement:page")
    public ResponseMessage<PageInfo<SalaryManagementEO>> page(SalaryManagementEOPage page) throws Exception {
        List<SalaryManagementEO> rows = salaryManagementEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

    @ApiOperation(value = "|SalaryManagementEO|查询")
    @GetMapping("")
//    @RequiresPermissions("finance:salaryManagement:list")
    public ResponseMessage<List<SalaryManagementEO>> list(SalaryManagementEOPage page) throws Exception {
        return Result.success(salaryManagementEOService.queryByList(page));
    }

    @ApiOperation(value = "|SalaryManagementEO|详情")
    @GetMapping("/{id}")
//    @RequiresPermissions("finance:salaryManagement:get")
    public ResponseMessage<SalaryManagementEO> find(@PathVariable String id) throws Exception {
        return Result.success(salaryManagementEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|SalaryManagementEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("finance:salaryManagement:save")
    public ResponseMessage<SalaryManagementEO> create(@RequestBody SalaryManagementEO salaryManagementEO) throws Exception {
        salaryManagementEO.setId(UUID.randomUUID10());
        UserEO userEO = UserUtils.getUser();
        if (null == userEO) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        salaryManagementEO.setUpdateTime(new DateTime());
        salaryManagementEO.setUpdateUserName(userEO.getUsname());
        salaryManagementEO.setUpdateUserId(userEO.getUsid());
        if (StringUtils.isNotEmpty(salaryManagementEO.getOrgName())) {
            salaryManagementEO.setOrgInitial(salaryManagementEO.getFirstSpell());
        }
        salaryManagementEOService.myCreate(salaryManagementEO);
        return Result.success(salaryManagementEO);
    }

    @ApiOperation(value = "|SalaryManagementEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("finance:salaryManagement:update")
    public ResponseMessage<SalaryManagementEO> update(@RequestBody SalaryManagementEO salaryManagementEO) throws Exception {
        UserEO userEO = UserUtils.getUser();
        if (null == userEO) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        salaryManagementEO.setUpdateTime(new DateTime());
        salaryManagementEO.setUpdateUserName(userEO.getUsname());
        salaryManagementEO.setUpdateUserId(userEO.getUsid());
        salaryManagementEOService.myUpdateByPrimaryKeySelective(salaryManagementEO);
        return Result.success(salaryManagementEO);
    }

    @ApiOperation(value = "|SalaryManagementEO|删除")
    @DeleteMapping("/{id}")
//    @RequiresPermissions("finance:salaryManagement:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        salaryManagementEOService.deleteByPrimaryKey(id);
        logger.info("delete from F_SALARY_MANAGEMENT where id = {}", id);
        return Result.success();
    }

    @ApiOperation(value = "|SalaryManagementEO|批量逻辑删除")
    @DeleteMapping("/logicDelete/{ids}")
//    @RequiresPermissions("finance:salaryManagement:delete")
    public ResponseMessage delete(@PathVariable String[] ids) throws Exception {
        salaryManagementEOService.logicDeleteByPrimaryKey(Arrays.asList(ids));
        logger.info("logic delete from F_SALARY_MANAGEMENT where id = {}", ids);
        return Result.success();
    }

    /**
     * 导入
     */
    @PostMapping("/import")
    @ApiOperation(value = "|salaryManagementEOPage|导入")
    public ResponseMessage excelImport(@RequestParam("file") MultipartFile file) {
        try {
            InputStream is = file.getInputStream();
            salaryManagementEOService.excelImport(is);
            return Result.success();
        } catch (Exception e) {
            logger.error(e.getMessage());
            return Result.error(e.getMessage());
        }
    }

    @ApiOperation(value = "|salaryManagementEOPage|导出")
    @GetMapping("/export")
    public ResponseMessage export(HttpServletResponse response, SalaryManagementEOPage salaryManagementEOPage) {
        String fileName = "薪酬数据表.xlsx";
        OutputStream os = null;
        Workbook workbook = null;

        try {
            response.setHeader(
                    "Content-Disposition",
                    "attachment; filename=" + Encodes.urlEncode(fileName));
            response.setContentType("application/force-download");

            workbook = salaryManagementEOService.excelExport(salaryManagementEOPage);
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
