package com.adc.da.customerresourcemanage.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.adc.da.budget.vo.DashBoardVO;
import com.adc.da.customerresourcemanage.dto.ContactsDto;
import com.adc.da.excel.poi.excel.entity.enums.ExcelType;
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
import com.adc.da.customerresourcemanage.entity.ContactsEO;
import com.adc.da.customerresourcemanage.page.ContactsEOPage;
import com.adc.da.customerresourcemanage.service.ContactsEOService;

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
@RequestMapping("/${restPath}/customerresourcemanage/contacts")
@Api(description = "|ContactsEO|联系人")
@Slf4j
public class ContactsEOController extends BaseController<ContactsEO>{

    private static final Logger logger = LoggerFactory.getLogger(ContactsEOController.class);

    @Autowired
    private ContactsEOService contactsEOService;

	@ApiOperation(value = "|ContactsEO|分页查询")
    @GetMapping("/page")
    //@RequiresPermissions("customerresourcemanage:contacts:page")
    public ResponseMessage<PageInfo<ContactsEO>> page(ContactsEOPage page) throws Exception {
        List<ContactsEO> rows = contactsEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|ContactsEO|查询")
    @GetMapping("/list")
    //@RequiresPermissions("customerresourcemanage:contacts:list")
    public ResponseMessage<List<ContactsEO>> list(ContactsEOPage page) throws Exception {
        return Result.success(contactsEOService.queryByList(page));
	}

    @ApiOperation(value = "|ContactsEO|详情")
    @GetMapping("/find/{id}")
    //@RequiresPermissions("customerresourcemanage:contacts:get")
    public ResponseMessage<ContactsEO> find(@PathVariable String id) throws Exception {
        return Result.success(contactsEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|ContactsEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE,value = "/create")
    //@RequiresPermissions("customerresourcemanage:contacts:save")
    public ResponseMessage<ContactsEO> create(@RequestBody ContactsEO contactsEO) throws Exception {
        contactsEOService.create(contactsEO);
        return Result.success(contactsEO);
    }

    @ApiOperation(value = "|ContactsEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE,value = "/update")
    //@RequiresPermissions("customerresourcemanage:contacts:update")
    public ResponseMessage<ContactsEO> update(@RequestBody ContactsEO contactsEO) throws Exception {
        contactsEOService.update(contactsEO);
        return Result.success(contactsEO);
    }

    @ApiOperation(value = "|ContactsEO|删除")
    @DeleteMapping("/delete/{id}")
    //@RequiresPermissions("customerresourcemanage:contacts:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        contactsEOService.logicDeleteByPrimaryKey(id);
        return Result.success();
    }
    @ApiOperation(value = "|ContactsEO|批量逻辑删除")
    @DeleteMapping("/batchLogicDelete/{ids}")
    public ResponseMessage batchLogicDelete(@NotNull @PathVariable("ids") String[] ids) throws Exception{
        List<String> enterpriseIds = Arrays.asList(ids);
        contactsEOService.batchLogicDeleteByPrimaryKeys(enterpriseIds);
        return Result.success();
    }


    @ApiOperation(value = "|ContactsEO|导入联系人")
    @PostMapping("/excelImportContacts")
    public ResponseMessage excelImportContacts(@RequestParam("file") MultipartFile file) throws Exception{
        InputStream inputStream = file.getInputStream();
        ImportParams importParams = new ImportParams();
        importParams.setHeadRows(2);
        List<ContactsDto> errors = contactsEOService.excelImportContacts(inputStream, importParams);
        ResponseMessage result = Result.success();
        if (errors.size()>0){
            result.setMessage(errors.toString());
        }
	    return result;
    }

    @ApiOperation(value = "|ContactsEO|导出联系人")
    @GetMapping("/excelExportContacts")
    public ResponseMessage excelExportContacts(HttpServletResponse response,String fileName) throws Exception{
        String finalFileName = "";
        if (StringUtils.isEmpty(fileName)){
            finalFileName = "联系人导出数据.xlsx";
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
        params.setType(cn.afterturn.easypoi.excel.entity.enmus.ExcelType.XSSF);
        try {
            OutputStream ot = response.getOutputStream();
            Workbook workbook = contactsEOService.exportContacts(params);
            workbook.write(ot);
            ot.flush();
        } catch (Exception e){
            log.error(e.getMessage(), e);
            throw new AdcDaBaseException("导出失败，请重试");
        }
        return Result.success();
    }

    @ApiOperation(value = "|ContactsEO|联系人总数、本年新增联系人数、核心决策人数")
    @GetMapping("/contactsStatistics")
    public ResponseMessage<Map<String,Integer>> contactsStatistics(
            @RequestParam(value = "year",required = false) String year,
            @RequestParam(value = "effectName",required = false) String effectName) throws Exception{
	    Integer total = contactsEOService.contactsStatistics(year,effectName);
	    Map<String,Integer> map = new HashMap<>();
	    map.put("contactsTotalNum",total);
	    return  Result.success(map);
    }

    @ApiOperation(value = "|ContactsEO|projectOwnerNum-企业合作总数，" +
            "currentYearIncreaseProjectOwnerNum-本年新增企业合作数，" +
            "contactNum-联系人总数，" +
            "currentYearContactIncreaseNum-本年新增联系人数、" +
            "policymakerNum-核心决策人数")
    @GetMapping("/newContactsStatistics/{year}")
    public ResponseMessage contactsStatistics(@PathVariable("year") Integer year) throws Exception{
        DashBoardVO dashBoardVO = contactsEOService.contactsStatistics(year);
        return  Result.success(dashBoardVO);
    }
}
