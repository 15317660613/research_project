package com.adc.da.budget.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.util.ArrayList;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import com.adc.da.base.page.Pager;
import com.adc.da.budget.dto.ProcessInstanceIdUpdateDTO;
import com.adc.da.login.util.UserUtils;
import com.adc.da.sys.entity.OrgEO;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.sys.service.OrgEOService;
import com.adc.da.sys.service.UserEOService;
import com.adc.da.sys.vo.UserVO;
import com.adc.da.util.utils.*;
import com.adc.da.excel.poi.excel.entity.ExportParams;
import com.adc.da.excel.poi.excel.entity.ImportParams;
import com.adc.da.excel.poi.excel.entity.enums.ExcelType;
import com.adc.da.util.exception.AdcDaBaseException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.adc.da.base.web.BaseController;
import com.adc.da.budget.entity.BuinessPerfonmanceContractEO;
import com.adc.da.budget.page.BuinessPerfonmanceContractEOPage;
import com.adc.da.budget.service.BuinessPerfonmanceContractEOService;

import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.http.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/${restPath}/budget/buinessPerfonmanceContract")
@Api("|BuinessPerfonmanceContractEO|")
public class BuinessPerfonmanceContractEOController extends BaseController<BuinessPerfonmanceContractEO>{

    private static final Logger logger = LoggerFactory.getLogger(BuinessPerfonmanceContractEOController.class);

    @Autowired
    private BuinessPerfonmanceContractEOService buinessPerfonmanceContractEOService;
    @Autowired
    BeanMapper beanMapper;
    @Autowired
    private OrgEOService orgEOService;
    
    @Autowired
    private UserEOService userService;
	@ApiOperation(value = "|BuinessPerfonmanceContractEO|分页查询")
    @GetMapping("/page")
    @RequiresPermissions("budget:buinessPerfonmanceContract:page")
    public ResponseMessage<PageInfo<BuinessPerfonmanceContractEO>> page
            (BuinessPerfonmanceContractEOPage page) throws Exception {
        List<BuinessPerfonmanceContractEO> rows = buinessPerfonmanceContractEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|BuinessPerfonmanceContractEO|查询")
    @GetMapping("")
    @RequiresPermissions("budget:buinessPerfonmanceContract:list")
    public ResponseMessage<List<BuinessPerfonmanceContractEO>> list
            (BuinessPerfonmanceContractEOPage page, Boolean myDepartment) throws Exception {
        if( myDepartment != null && myDepartment ) {
            UserEO user = UserUtils.getUser();
            String id = user.getUsid();
            UserVO userVO = this.beanMapper.map(this.userService.getUserWithRoles(id), UserVO.class);
            List<OrgEO> orgEOList = new ArrayList<>();
            orgEOList.addAll(userVO.getOrgs());
            for (int i = 0; i < userVO.getOrgs().size(); i++) {
                List<OrgEO> orgEOS = orgEOService.getOrgEOByPid(userVO.getOrgs().get(i).getId());
                orgEOList.addAll(orgEOS);
            }
            List<String> orgNames = new ArrayList<>();
            for (OrgEO o : orgEOList) {
                if (!orgNames.contains(o.getName())) {
                    orgNames.add(o.getName());
                }
            }

            if (orgNames.size() > 0) {
                page.setOrgNames(orgNames);
            }
        }
        if(StringUtils.isEmpty(page.getPager().getOrderField())){
            Pager p = new Pager();
            p.setOrderField("startTimre");
            p.setOrderDirection(false);
            page.setPager(p);
        }
        return Result.success(buinessPerfonmanceContractEOService.queryByList(page));
	}

    @ApiOperation(value = "|BuinessPerfonmanceContractEO|详情")
    @GetMapping("/{id}")
    @RequiresPermissions("budget:buinessPerfonmanceContract:get")
    public ResponseMessage<BuinessPerfonmanceContractEO> find(@PathVariable String id) throws Exception {
        return Result.success(buinessPerfonmanceContractEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|BuinessPerfonmanceContractEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("budget:buinessPerfonmanceContract:save")
    public ResponseMessage<BuinessPerfonmanceContractEO> create
            (@RequestBody BuinessPerfonmanceContractEO buinessPerfonmanceContractEO) throws Exception {
        buinessPerfonmanceContractEO.setId(UUID.randomUUID(10));
        if(buinessPerfonmanceContractEO.getIslock() == null){
            buinessPerfonmanceContractEO.setIslock("否");
        }
        buinessPerfonmanceContractEO.setStartTimre(new Date());
        buinessPerfonmanceContractEOService.insertSelective(buinessPerfonmanceContractEO);
        return Result.success(buinessPerfonmanceContractEO);
    }

    @ApiOperation(value = "|BuinessComfirmContractEO|批量更新")
    @PutMapping(value = "/updateprocessInstanceIdByIdList",consumes = APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("budget:buinessComfirmContract:update")
    public ResponseMessage updateprocessInstanceIdByIdList
            (@RequestBody ProcessInstanceIdUpdateDTO processInstanceIdUpdateDTO) throws Exception {
        if(processInstanceIdUpdateDTO == null || processInstanceIdUpdateDTO.getIdList() == null
                || processInstanceIdUpdateDTO.getIdList().size() == 0) {
            return Result.error("缺少参数");
        }
        buinessPerfonmanceContractEOService.updateprocessInstanceIdByIdList(processInstanceIdUpdateDTO);
        return Result.success();
    }

    @ApiOperation(value = "|BuinessPerfonmanceContractEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("budget:buinessPerfonmanceContract:update")
    public ResponseMessage<BuinessPerfonmanceContractEO> update
            (@RequestBody BuinessPerfonmanceContractEO buinessPerfonmanceContractEO) throws Exception {
        buinessPerfonmanceContractEO.setUpdatetTime(new Date());
        buinessPerfonmanceContractEOService.updateByPrimaryKeySelective(buinessPerfonmanceContractEO);
        return Result.success(buinessPerfonmanceContractEO);
    }

    @ApiOperation(value = "|BuinessPerfonmanceContractEO|删除")
    @DeleteMapping("/{id}")
    @RequiresPermissions("budget:buinessPerfonmanceContract:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        buinessPerfonmanceContractEOService.deleteByPrimaryKey(id);
        logger.info("delete from BUINESS_PERFONMANCE_CONTRACT where id = {}", id);
        return Result.success();
    }
    @ApiOperation(value = "单个sheet的Excel导出")
    @GetMapping("/excelExport")
    public ResponseMessage excelExport(HttpServletResponse response,
                                       String fileName,BuinessPerfonmanceContractEOPage page) throws Exception{
        if(StringUtils.isEmpty(fileName)){
            fileName = "数据表格.xlsx";
        } else {
            if(!fileName.contains(".xlsx")){
                fileName += ".xlsx";
            }
        }
        OutputStream os = null;
        Workbook workbook = null;
        try {
            response.setHeader("Content-Disposition",
                    "attachment; filename=" + Encodes.urlEncode(fileName));
            response.setContentType("application/force-download");
            ExportParams params = new ExportParams();
            params.setType(ExcelType.XSSF);
            workbook = buinessPerfonmanceContractEOService.excelExport(params,page);
            os = response.getOutputStream();
            workbook.write(os);
            os.flush();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new AdcDaBaseException("导出失败，请重试");
        } finally {
            IOUtils.closeQuietly(os);
        }
        return Result.success();
    }
    @ApiOperation(value = "无验证的Excel导入")
    @PostMapping("/excelImport")
    public ResponseMessage excelImport(@RequestParam("file") MultipartFile file) throws Exception{
        InputStream is = file.getInputStream();
        ImportParams params = new ImportParams();
        buinessPerfonmanceContractEOService.excelImport(is,params);
        return Result.success();
    }

    @ApiOperation(value = "|BuinessPerfonmanceContractEO|批量删除")
    @DeleteMapping("/deleteBatch")
@RequiresPermissions("budget:buinessPerfonmanceContract:delete")
    public ResponseMessage deleteAll( @RequestBody(required = true) List<String> ids) throws Exception {
	    try {
            buinessPerfonmanceContractEOService.deleteInBatch(ids);
            logger.info(" delete from TS_BUDGET where id in", ids.toString());
        } catch (Exception e){
            logger.info(e.getMessage());
	        //return Result.error(e.getMessage());
            return Result.error("批量删除异常失败");
        }
        return Result.success();
    }
}
