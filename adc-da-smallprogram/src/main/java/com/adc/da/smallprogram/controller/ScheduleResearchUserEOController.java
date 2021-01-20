package com.adc.da.smallprogram.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import com.adc.da.excel.poi.excel.entity.ExportParams;
import com.adc.da.excel.poi.excel.entity.enums.ExcelType;
import com.adc.da.login.util.UserUtils;
import com.adc.da.smallprogram.entity.ScheduleResearchEO;
import com.adc.da.smallprogram.service.ScheduleResearchEOService;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.Encodes;
import com.adc.da.util.utils.IOUtils;
import com.adc.da.util.utils.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.adc.da.base.web.BaseController;
import com.adc.da.smallprogram.entity.ScheduleResearchUserEO;
import com.adc.da.smallprogram.page.ScheduleResearchUserEOPage;
import com.adc.da.smallprogram.service.ScheduleResearchUserEOService;

import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.http.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/${restPath}/smallprogram/scheduleResearchUser")
@Api(tags = "小程序科委用户填写内容|ScheduleResearchUserEO|")
public class ScheduleResearchUserEOController extends BaseController<ScheduleResearchUserEO>{

    private static final Logger logger = LoggerFactory.getLogger(ScheduleResearchUserEOController.class);

    @Autowired
    private ScheduleResearchUserEOService scheduleResearchUserEOService;
    @Autowired
    private ScheduleResearchEOService scheduleResearchEOService;

	@ApiOperation(value = "|ScheduleResearchUserEO|分页查询")
    @GetMapping("/page")
    //@RequiresPermissions("smallprogram:scheduleResearchUser:page")
    public ResponseMessage<PageInfo<ScheduleResearchUserEO>> page(ScheduleResearchUserEOPage page) throws Exception {
        List<ScheduleResearchUserEO> rows = scheduleResearchUserEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }
    @ApiOperation(value = "|ScheduleResearchUserEO|完成情况，传researchid")
    @GetMapping("/queryFinishList")
    //@RequiresPermissions("smallprogram:scheduleResearchUser:page")
    public ResponseMessage<List<ScheduleResearchUserEO>> queryFinishList(ScheduleResearchUserEOPage page) throws Exception {
        return Result.success(scheduleResearchUserEOService.queryFinishList(page));
    }

    @ApiOperation(value = "|ScheduleResearchUserEO|完成情况，传researchId  status")
    @GetMapping("/selectByResearchIdAndStatus")
    public ResponseMessage<List<ScheduleResearchUserEO>> selectByResearchIdAndStatus(@RequestParam("researchId") String researchId ,@RequestParam("status")Integer status){
        return Result.success( scheduleResearchUserEOService.selectByResearchIdAndStatus(researchId, status));
    }

	@ApiOperation(value = "|ScheduleResearchUserEO|查询")
    @GetMapping("")
    //@RequiresPermissions("smallprogram:scheduleResearchUser:list")
    public ResponseMessage<List<ScheduleResearchUserEO>> list(ScheduleResearchUserEOPage page) throws Exception {
        return Result.success(scheduleResearchUserEOService.queryByList(page));
	}

    @ApiOperation(value = "|ScheduleResearchUserEO|详情")
    @GetMapping("/{id}")
    //@RequiresPermissions("smallprogram:scheduleResearchUser:get")
    public ResponseMessage<ScheduleResearchUserEO> find(@PathVariable String id) throws Exception {
        return Result.success(scheduleResearchUserEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|ScheduleResearchUserEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    //@RequiresPermissions("smallprogram:scheduleResearchUser:save")
    public ResponseMessage<ScheduleResearchUserEO> create(@RequestBody ScheduleResearchUserEO scheduleResearchUserEO) throws Exception {
        scheduleResearchUserEOService.insertSelective(scheduleResearchUserEO);
        return Result.success(scheduleResearchUserEO);
    }

    @ApiOperation(value = "|ScheduleResearchUserEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    //@RequiresPermissions("smallprogram:scheduleResearchUser:update")
    public ResponseMessage<ScheduleResearchUserEO> update(@RequestBody ScheduleResearchUserEO scheduleResearchUserEO) throws Exception {
        scheduleResearchUserEOService.myUpdateByPrimaryKeySelective(scheduleResearchUserEO);
        return Result.success(scheduleResearchUserEO);
    }

    @ApiOperation(value = "|ScheduleResearchUserEO|删除")
    @DeleteMapping("/{id}")
    //@RequiresPermissions("smallprogram:scheduleResearchUser:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        scheduleResearchUserEOService.deleteByPrimaryKey(id);
        logger.info("delete from TS_SCHEDULE_RESEARCH_USER where id = {}", id);
        return Result.success();
    }
    @ApiOperation(value = "|工作要点导出|导出")
    @GetMapping("/export")
    public ResponseMessage export(HttpServletResponse response, ScheduleResearchUserEOPage page)throws Exception {
        String fileName = "数据表格.xlsx";
        ExportParams params = new ExportParams();
        params.setType(ExcelType.XSSF);params.setCreateHeadRows(true);
        if (StringUtils.isNotEmpty(page.getResearchId())){
            ScheduleResearchEO scheduleResearchEO = scheduleResearchEOService.selectByPrimaryKey(page.getResearchId());
            if (null != scheduleResearchEO){
                fileName = scheduleResearchEO.getTitle()+".xlsx";
                params.setTitle(scheduleResearchEO.getTitle());
            }else {
                throw new AdcDaBaseException("导出失败，该主题可能不存在！");
            }
        }
        OutputStream os = null;
        try {
            response.setHeader(
                    "Content-Disposition",
                    "attachment; filename=" + Encodes.urlEncode(fileName));
            response.setContentType("application/force-download");
            Workbook workbook = scheduleResearchUserEOService.excelExport(page,params);
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
