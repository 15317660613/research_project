package com.adc.da.oa.controller;

import com.adc.da.budget.entity.BudgetEO;
import com.adc.da.budget.entity.Project;
import com.adc.da.excel.poi.excel.entity.ImportParams;
import com.adc.da.oa.service.DataSyncBudgetService;
import com.adc.da.oa.service.DataSyncContractService;
import com.adc.da.oa.service.DataSyncProjectService;
import com.adc.da.oa.service.ProjectOAIOService;
import com.adc.da.oa.vo.ContractInvoiceListVO;
import com.adc.da.oa.vo.OAProjectVO;
import com.adc.da.processform.entity.ProjectContractInvoiceEO;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.google.gson.Gson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

/**
 * OA数据同步
 */
@Slf4j
@RestController
@RequestMapping("/${restPath}/dataSync")
@Api(tags = "|OA数据同步接口|")
public class DataSyncController {

    @Autowired
    private DataSyncBudgetService budgetSyncService;

    @Autowired
    private DataSyncProjectService projectSyncService;

    @Autowired
    private DataSyncContractService syncContractService;

    @ApiOperation(value = "业务修改/新增（OA项目）")
    @PostMapping(value = "/budget", consumes = APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage<List<BudgetEO>> update(@RequestBody String json) throws Exception {
        try {
            log.info("业务修改/新增（OA项目）json = " + json);
            return Result.success(budgetSyncService.updateMultiJson(json));
        } catch (AdcDaBaseException e) {
            log.error("业务修改/新增（OA项目）失败json = " + json , e);
            return Result.error("-1", e.getMessage(), null);
        }
    }

    @ApiOperation(value = "业务删除（OA项目）")
    @DeleteMapping(value = "/budget/{field4}")
    public ResponseMessage delete(@PathVariable String[] field4) throws Exception {
        try {
            log.info("业务删除（OA项目）DomainId = " + new Gson().toJson(field4));
            budgetSyncService.deleteBudgetIdByDomainId(new HashSet<>(Arrays.asList(field4)));
            return Result.success();
        } catch (AdcDaBaseException e) {
            log.error("业务删除（OA项目）失败 DomainId = " + new Gson().toJson(field4) , e);
            return Result.error("-1", e.getMessage(), null);
        }
    }

    @ApiOperation(value = "项目修改/新增（OA合同）")
    @PostMapping(value = "/project", consumes = APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage<Project> updateProject(@RequestBody OAProjectVO vo) throws Exception {
        try {
            log.info("项目修改/新增（OA合同） OAProjectVO = " + new Gson().toJson(vo));
            return Result.success(projectSyncService.checkVO(vo));
        } catch (AdcDaBaseException e) {
            return Result.error("-1", e.getMessage(), null);
        }
    }

    @ApiOperation(value = "项目删除（OA合同）")
    @DeleteMapping(value = "/project/{contractNoArray}")
    public ResponseMessage deleteProject(@PathVariable String[] contractNoArray) throws Exception {
        try {
            log.info("项目删除（OA合同） contractNoArray = " + new Gson().toJson(contractNoArray));
            projectSyncService.deleteBudgetIdByDomainId(new HashSet<>(Arrays.asList(contractNoArray)));
            return Result.success();
        } catch (AdcDaBaseException e) {
            log.info("项目删除（OA合同）失败 contractNoArray = " + new Gson().toJson(contractNoArray) , e);
            return Result.error("-1", e.getMessage(), null);
        }

    }

    /**
     * 结合合同编号与项目编号进行删除工作
     *
     * @param contractNO
     * @param projectNO
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "项目删除（OA合同）")
    @DeleteMapping(value = "/project/{contractNO}/{projectNO}")
    public ResponseMessage deleteProject(@PathVariable String contractNO, @PathVariable String projectNO)
        throws Exception {
        try {
            projectSyncService.deleteBudgetIdByDomainId(contractNO, projectNO);
            return Result.success();
        } catch (AdcDaBaseException e) {
            return Result.error("-1", e.getMessage(), null);
        }

    }

    @ApiOperation(value = "开票信息修改/新增 ")
    @PostMapping(value = "/contractInvoice", consumes = APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage<List<ProjectContractInvoiceEO>> updateContractInvoice(
        @RequestBody ContractInvoiceListVO vo) {
        try {
            return Result.success(syncContractService.update(vo));
        } catch (AdcDaBaseException e) {
            return Result.error("-1", e.getMessage(), null);
        }
    }

    @ApiOperation(value = "开票信息删除（OA合同）-根据合同编号全部删除")
    @DeleteMapping(value = "/contractInvoice/{contractNoArray}")
    public ResponseMessage deleteContractInvoice(@PathVariable String[] contractNoArray) {
        try {
            return Result.success(syncContractService.delete(Arrays.asList(contractNoArray)));
        } catch (AdcDaBaseException e) {
            return Result.error("-1", e.getMessage(), null);
        }
    }

    @ApiOperation(value = "开票信息删除（OA合同）-指定id")
    @DeleteMapping(value = "/deleteByContractInvoiceId/{ids}")
    public ResponseMessage deleteByContractInvoiceId(@PathVariable String[] ids) {
        try {
            return Result.success(syncContractService.deleteByContractInvoiceId(Arrays.asList(ids)));
        } catch (AdcDaBaseException e) {
            return Result.error("-1", e.getMessage(), null);
        }
    }

    @Autowired
    private ProjectOAIOService projectOAIOService;

    @ApiOperation(value = "|Project|旧数据同步")
    @PostMapping("/excelImport")
    //@RequiresPermissions("fin:project:import")
    public ResponseMessage excelImport(@RequestParam("file") MultipartFile file) throws Exception {
        InputStream is = file.getInputStream();
        ImportParams params = new ImportParams();
        Map<String, String> message = projectOAIOService.excelImport(is, params);

        return Result.success(message);

    }
}
