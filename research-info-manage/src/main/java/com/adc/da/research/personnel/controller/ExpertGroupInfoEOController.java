package com.adc.da.research.personnel.controller;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.adc.da.base.web.BaseController;
import com.adc.da.research.personnel.dto.BindUserInfoDto;
import com.adc.da.research.personnel.entity.ExpertGroupInfoEO;
import com.adc.da.research.personnel.page.ExpertGroupInfoEOPage;
import com.adc.da.research.personnel.service.ExpertGroupInfoEOService;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.http.PageInfo;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.utils.Encodes;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
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
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.OutputStream;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping("/${restPath}/personnel/expertGroupInfo")
@Api(tags = "科研系统|专家组|ExpertGroupInfoEOController")
public class ExpertGroupInfoEOController extends BaseController<ExpertGroupInfoEO>{

    private static final Logger logger = LoggerFactory.getLogger(ExpertGroupInfoEOController.class);

    @Autowired
    private ExpertGroupInfoEOService expertGroupInfoEOService;


	@ApiOperation(value = "|ExpertGroupInfoEO|分页查询")
    @GetMapping("/page")
//    @RequiresPermissions("personnel:expertGroupInfo:page")
    public ResponseMessage<PageInfo<ExpertGroupInfoEO>> page(ExpertGroupInfoEOPage page) throws Exception {
        List<ExpertGroupInfoEO> rows = expertGroupInfoEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|ExpertGroupInfoEO|查询")
    @GetMapping("")
//    @RequiresPermissions("personnel:expertGroupInfo:list")
    public ResponseMessage<List<ExpertGroupInfoEO>> list(ExpertGroupInfoEOPage page) throws Exception {
        return Result.success(expertGroupInfoEOService.queryByList(page));
	}

    @ApiOperation(value = "|ExpertGroupInfoEO|详情")
    @GetMapping("/{id}")
//    @RequiresPermissions("personnel:expertGroupInfo:get")
    public ResponseMessage<ExpertGroupInfoEO> find(@PathVariable String id) throws Exception {
        return Result.success(expertGroupInfoEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|ExpertGroupInfoEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("personnel:expertGroupInfo:save")
    public ResponseMessage<ExpertGroupInfoEO> create(@RequestBody ExpertGroupInfoEO expertGroupInfoEO) throws Exception {
      //  expertGroupInfoEOService.insertSelective(expertGroupInfoEO);
        expertGroupInfoEOService.create(expertGroupInfoEO);
        return Result.success(expertGroupInfoEO);
    }

    @ApiOperation(value = "|ExpertGroupInfoEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("personnel:expertGroupInfo:update")
    public ResponseMessage<ExpertGroupInfoEO> update(@RequestBody ExpertGroupInfoEO expertGroupInfoEO) throws Exception {
        expertGroupInfoEOService.updateByPrimaryKeySelective(expertGroupInfoEO);
        return Result.success(expertGroupInfoEO);
    }

    @ApiOperation(value = "|ExpertGroupInfoEO|删除")
    @DeleteMapping("/{id}")
//    @RequiresPermissions("personnel:expertGroupInfo:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        expertGroupInfoEOService.deleteByPrimaryKey(id);
        logger.info("delete from RS_EXPERT_GROUP_INFO where id = {}", id);
        return Result.success();
    }

    @ApiOperation(value = "|ExpertGroupInfoEO|批量删除")
    @DeleteMapping("/logicDelete/{ids}")
    // @RequiresPermissions("research:warnNotice:delete")
    public ResponseMessage deleteByIds(@NotNull @ApiParam(value = "ids", required = true) @PathVariable("ids")String[] ids) throws Exception {
        expertGroupInfoEOService.deleteByIds(ids);
        return Result.success();
    }

    @ApiOperation(value = "|ExpertGroupEO|导出专家组数据")
    @GetMapping("/exportExpertGroupEO")
    public ResponseMessage exportExpertGroup(HttpServletResponse response, String fileName, ExpertGroupInfoEOPage eoPage) throws Exception{
        String finalFileName = "";
        if (StringUtils.isEmpty(fileName)){
            finalFileName = "专家组导出数据.xlsx";
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
            Workbook workbook = expertGroupInfoEOService.exportExpertGroup(exportParams,eoPage);
            // Non-static method 'exportExpertGroup(cn.afterturn.easypoi.excel.entity.ExportParams, com.adc.da.research.personnel.page.ExpertGroupInfoEOPage)' cannot be referenced from a static context
            workbook.write(outputStream);
            outputStream.flush();
        } catch (Exception e){
            logger.error(e.getMessage(), e);
            throw new AdcDaBaseException("导出失败，请重试");
        }
        return Result.success();
    }

    @ApiOperation(value = "获取专家组绑定用户情况")
    @GetMapping("/getBindUserInfo")
    public ResponseMessage<BindUserInfoDto> getBindUserInfo(String id){
	    try{
	        if (StringUtils.isEmpty(id)){
                return Result.error("专家组id不能为空!");
            }
            BindUserInfoDto bindUserInfoDto = expertGroupInfoEOService.getBindUserInfoDto(id);
            return Result.success(bindUserInfoDto);
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return Result.error(e.getMessage());
        }
    }


    @ApiOperation(value = "新增专家组绑定用户情况")
    @PutMapping("/saveBindUserInfo")
    public ResponseMessage saveBindUserInfo(@RequestBody BindUserInfoDto bindUserInfoDto){
	    try {
            expertGroupInfoEOService.saveBindUserInfo(bindUserInfoDto);
            return Result.success("绑定成功!");
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return Result.error("绑定失败" + e.getMessage());
        }
    }


}
