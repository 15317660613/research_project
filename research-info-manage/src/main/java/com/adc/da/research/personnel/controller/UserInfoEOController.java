package com.adc.da.research.personnel.controller;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.adc.da.base.web.BaseController;
import com.adc.da.research.personnel.entity.UserInfoEO;
import com.adc.da.research.personnel.handler.UserInfoEODtoHandler;
import com.adc.da.research.personnel.page.UserInfoEOPage;
import com.adc.da.research.personnel.service.UserInfoEOService;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.http.PageInfo;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.utils.Encodes;
import com.adc.da.util.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

/**
 * 科研管理--->科研人员
 */
@RestController
@RequestMapping("/${restPath}/personnel/userInfo")
@Api(tags = "科研系统|科研人员|UserInfoEOController")
public class UserInfoEOController extends BaseController<UserInfoEO>{

    private static final Logger logger = LoggerFactory.getLogger(UserInfoEOController.class);

    @Autowired
    private UserInfoEOService userInfoEOService;

	@ApiOperation(value = "|UserInfoEO|分页查询")
    @GetMapping("/page")
   // @RequiresPermissions("personnel:userInfo:page")
    public ResponseMessage<PageInfo<UserInfoEO>> page(UserInfoEOPage page) throws Exception {
	    page.setUserIdOperator("like");
	    page.setUserNameOperator("like");
        List<UserInfoEO> rows = userInfoEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|UserInfoEO|查询")
    @GetMapping("")
   // @RequiresPermissions("personnel:userInfo:list")
    public ResponseMessage<List<UserInfoEO>> list(UserInfoEOPage page) throws Exception {
        return Result.success(userInfoEOService.queryByList(page));
	}

    @ApiOperation(value = "|UserInfoEO|详情")
    @GetMapping("/get/{id}")
   // @RequiresPermissions("personnel:userInfo:get")
    public ResponseMessage<UserInfoEO> find(@PathVariable String id) throws Exception {
        return Result.success(userInfoEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|UserInfoEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
   // @RequiresPermissions("personnel:userInfo:save")
    public ResponseMessage<UserInfoEO> create(@RequestBody UserInfoEO userInfoEO) throws Exception {
      //  userInfoEOService.insertSelective(userInfoEO);
        userInfoEOService.create(userInfoEO);
        return Result.success(userInfoEO);
    }

    @ApiOperation(value = "|UserInfoEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
   // @RequiresPermissions("personnel:userInfo:update")
    public ResponseMessage<UserInfoEO> update(@RequestBody UserInfoEO userInfoEO) throws Exception {
        //userInfoEOService.updateByPrimaryKeySelective(userInfoEO);
        userInfoEOService.updateAndUser(userInfoEO);
        return Result.success(userInfoEO);
    }

    @ApiOperation(value = "|UserInfoEO|删除")
    @DeleteMapping("/{id}")
    //@RequiresPermissions("personnel:userInfo:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        userInfoEOService.deleteByPrimaryKey(id);
        logger.info("delete from RS_USER_INFO where id = {}", id);
        return Result.success();
    }

    @ApiOperation(value = "|UserInfoEO|单条/批量逻辑删除")
    @DeleteMapping("/logicDelete/{ids}")
    //@RequiresPermissions("finance:businessGonfig:delete")
    public ResponseMessage logicDelete(@PathVariable List<String> ids) throws Exception {
        userInfoEOService.logicDelete(ids);
        return Result.success();
    }

    @ApiOperation(value = "|UserInfoEO|导出科研人员数据")
    @GetMapping("/exportUserInfoEO")
    public ResponseMessage importRevenueManagement(HttpServletResponse response, String fileName, UserInfoEOPage eoPage) throws Exception{
        String finalFileName = "";
        if (StringUtils.isEmpty(fileName)){
            finalFileName = "科研人员导出数据.xlsx";
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
            Workbook workbook = userInfoEOService.exportRevenueManagement(exportParams,eoPage);
            workbook.write(outputStream);
            outputStream.flush();
        } catch (Exception e){
            logger.error(e.getMessage(), e);
            throw new AdcDaBaseException("导出失败，请重试");
        }
        return Result.success();
    }

    @ApiOperation(value = "|RevenueManagementEO|导入科研人员数据")
    @PostMapping("/importRevenueManagement")
    public ResponseMessage importRevenueManagement(@RequestParam("file") MultipartFile file) throws Exception{
        InputStream inputStream = file.getInputStream();
        ImportParams importParams = new ImportParams();
        importParams.setNeedVerfiy(true);
        importParams.setVerifyHandler(new UserInfoEODtoHandler());
        String errors = userInfoEOService.importRevenueManagement(inputStream, importParams);
        if (StringUtils.isNotEmpty(errors)){
            return Result.error(errors);
        }
        return Result.success();
    }

}
