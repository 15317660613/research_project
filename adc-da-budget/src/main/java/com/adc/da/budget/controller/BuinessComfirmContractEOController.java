package com.adc.da.budget.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.adc.da.base.page.Pager;
import com.adc.da.budget.dto.ProcessInstanceIdUpdateDTO;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.adc.da.base.web.BaseController;
import com.adc.da.budget.entity.BuinessComfirmContractEO;
import com.adc.da.budget.page.BuinessComfirmContractEOPage;
import com.adc.da.budget.service.BuinessComfirmContractEOService;

import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.http.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.multipart.MultipartFile;

import com.adc.da.login.util.UserUtils;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/${restPath}/budget/buinessComfirmContract")
@Api(description = "|BuinessComfirmContractEO|")
public class BuinessComfirmContractEOController extends BaseController<BuinessComfirmContractEO> {

    private static final Logger logger = LoggerFactory.getLogger(BuinessComfirmContractEOController.class);

    @Autowired
    BeanMapper beanMapper;

    @Autowired
    private UserEOService userService;
    @Autowired
    private OrgEOService orgEOService;

    @Autowired
    private BuinessComfirmContractEOService buinessComfirmContractEOService;

	@ApiOperation(value = "|BuinessComfirmContractEO|分页查询")
    @GetMapping("/page")
    //@RequiresPermissions("budget:buinessComfirmContract:page")
    public ResponseMessage<PageInfo<BuinessComfirmContractEO>> page(BuinessComfirmContractEOPage page) throws Exception {
        List<BuinessComfirmContractEO> rows = buinessComfirmContractEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|BuinessComfirmContractEO|查询")
    @GetMapping("")
    //@RequiresPermissions("budget:buinessComfirmContract:list")
    public ResponseMessage<List<BuinessComfirmContractEO>> list(BuinessComfirmContractEOPage page, Boolean myDepartment) throws Exception {
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
	        p.setOrderField("stratTime");
	        p.setOrderDirection(false);
	        page.setPager(p);
        }
        return Result.success(buinessComfirmContractEOService.queryByList(page));
	}

    @ApiOperation(value = "|BuinessComfirmContractEO|详情")
    @GetMapping("/{id}")
    //@RequiresPermissions("budget:buinessComfirmContract:get")
    public ResponseMessage<BuinessComfirmContractEO> find(@PathVariable String id) throws Exception {
        return Result.success(buinessComfirmContractEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|BuinessComfirmContractEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    //@RequiresPermissions("budget:buinessComfirmContract:save")
    public ResponseMessage<BuinessComfirmContractEO> create(@RequestBody BuinessComfirmContractEO buinessComfirmContractEO) throws Exception {
       buinessComfirmContractEO.setId(UUID.randomUUID(10));
       if(buinessComfirmContractEO.getIslock() == null){
           buinessComfirmContractEO.setIslock("否");
       }
        buinessComfirmContractEO.setStratTime(new Date());
        buinessComfirmContractEOService.insertSelective(buinessComfirmContractEO);
        return Result.success(buinessComfirmContractEO);
    }

    @ApiOperation(value = "|BuinessComfirmContractEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    //@RequiresPermissions("budget:buinessComfirmContract:update")
    public ResponseMessage<BuinessComfirmContractEO> update(@RequestBody BuinessComfirmContractEO buinessComfirmContractEO) throws Exception {
        buinessComfirmContractEO.setUpdateTime(new Date());
        buinessComfirmContractEOService.updateByPrimaryKeySelective(buinessComfirmContractEO);
        return Result.success(buinessComfirmContractEO);
    }


    @ApiOperation(value = "|BuinessComfirmContractEO|批量更新")
    @PutMapping(value = "/updateprocessInstanceIdByIdList",consumes = APPLICATION_JSON_UTF8_VALUE)
    //@RequiresPermissions("budget:buinessComfirmContract:update")
    public ResponseMessage updateprocessInstanceIdByIdList(@RequestBody ProcessInstanceIdUpdateDTO processInstanceIdUpdateDTO) throws Exception {
	    if(processInstanceIdUpdateDTO == null || processInstanceIdUpdateDTO.getIdList() == null
                || processInstanceIdUpdateDTO.getIdList().size() == 0) {
	        return Result.error("缺少参数");
        }
        buinessComfirmContractEOService.updateprocessInstanceIdByIdList(processInstanceIdUpdateDTO);
        return Result.success();
    }

    @ApiOperation(value = "|BuinessComfirmContractEO|删除")
    @DeleteMapping("/{id}")
    //@RequiresPermissions("budget:buinessComfirmContract:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        buinessComfirmContractEOService.deleteByPrimaryKey(id);
        logger.info("delete from BUINESS_COMFIRM_CONTRACT where id = {}", id);
        return Result.success();
    }
    @ApiOperation(value = "|BuinessComfirmContractEO|批量删除")
    @DeleteMapping("/deleteBatch" )
    public ResponseMessage deleteAll(@RequestBody(required = true) List<String> ids) throws Exception {
	    try {
            buinessComfirmContractEOService.deleteInBatch(ids);
            logger.info(" delete from TS_BUDGET  where id in()", ids.toString());
        } catch (Exception e){
            logger.info(e.getMessage());
	        //return Result.error(e.getMessage());
            return Result.error("批量删除异常失败");
        }
        return Result.success();
    }

    @ApiOperation(value = "单个sheet的Excel导出")
    @GetMapping("/excelExport")
    public ResponseMessage excelExport(HttpServletResponse response, String fileName,BuinessComfirmContractEOPage page) throws Exception{
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
            workbook = buinessComfirmContractEOService.excelExport(params,page);
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
    @ApiOperation(value = "BuinessComfirmContractEO无验证的Excel单sheet导入")
    @PostMapping("/excelImport")
    public ResponseMessage excelImport(@RequestParam("file") MultipartFile file) throws Exception{
        InputStream is = file.getInputStream();
        ImportParams params = new ImportParams();
        //FIXME 需要修复导入校验title必须匹配excel功能，期待easypoi新版本能够实现
        buinessComfirmContractEOService.excelImport(is,params);
        return Result.success();
    }
    @Deprecated
    @ApiOperation(value = "无验证的Excel导入-未实现")
    @PostMapping("/multiExcelImport")
    public ResponseMessage multiExcelImport(@RequestParam("file") MultipartFile file) {
	    //TODO 需要实现 BuinessComfirmContractEO 和 BuinessPerfonmanceContractEO 多sheet导入功能
        return Result.success();
    }
}
