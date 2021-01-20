package com.adc.da.research.personnel.controller;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.adc.da.base.web.BaseController;
import com.adc.da.research.personnel.entity.ExpertUserInfoListEO;
import com.adc.da.research.personnel.page.ExpertUserInfoVOPage;
import com.adc.da.research.personnel.service.ExpertUserInfoTempService;
import com.adc.da.util.http.PageInfo;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.utils.Encodes;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.List;

/**
 * @description
 * @date 2020/10/27 17:20
 * @auth zhn
 */
@RestController
@RequestMapping("/${restPath}/personnel/expertUserInfoTemp")
@Api(tags = "科研系统|专家人员|ExpertUserInfoTempController")
public class ExpertUserInfoTempController extends BaseController<ExpertUserInfoListEO> {
    @Autowired
    private ExpertUserInfoTempService expertUserInfoTempService;

    @ApiOperation(value = "分页查询")
    @GetMapping("/page")
//    @RequiresPermissions("personnel:expertUserInfo:page")
    public ResponseMessage<PageInfo<ExpertUserInfoListEO>> page(ExpertUserInfoVOPage page) throws Exception {
//        page.setUserNameOperator("LIKE");
//        page.setCompanyNameOperator("LIKE");
//        page.setJobTitleOperator("LIKE");
//        page.setLastDegreeOperator("LIKE");
        List<ExpertUserInfoListEO> rows = expertUserInfoTempService.queryByPageUnion(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

    @ApiOperation(value = "导出专家人员数据")
    @PostMapping("/exportUserInfo")
    public ResponseMessage exportUserInfo(HttpServletResponse response,ExpertUserInfoVOPage eoPage){
        String finalFileName = "专家人员导出数据.xlsx";;
        response.setHeader("Content-Disposition", "attachment; filename=" + Encodes.urlEncode(finalFileName));
        response.setContentType("application/force-download");
        ExportParams exportParams = new ExportParams();
        exportParams.setType(ExcelType.XSSF);
        try {
            OutputStream outputStream = response.getOutputStream();
            Workbook workbook = expertUserInfoTempService.exportUserInfo(exportParams,eoPage);
            workbook.write(outputStream);
            outputStream.flush();
            return Result.success("导出成功!");
        } catch (Exception e){
            return Result.error(e.getMessage());
        }
    }
}
