package com.adc.da.research.funds.controller;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.hutool.core.util.ObjectUtil;
import com.adc.da.base.web.BaseController;
import com.adc.da.login.util.UserUtils;
import com.adc.da.research.funds.eum.FundsListType;
import com.adc.da.research.funds.eum.RoleType;
import com.adc.da.research.funds.service.FundsListService;
import com.adc.da.research.funds.vo.fundsList.FundsListVO;
import com.adc.da.research.project.page.ProjectDataEOPage;
import com.adc.da.research.project.service.ProjectDataEOService;
import com.adc.da.sys.entity.RoleEO;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.http.PageInfo;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.Encodes;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Auther: yanyujie
 * @Date: 2020/11/10
 * @Description:
 */

@RestController
@RequestMapping("/${restPath}/research/funds/fundsList")
@Api(tags = "科研系统|科研经费一览|FundsListController")
public class FundsListController extends BaseController<FundsListVO>{
    private static final Logger logger = LoggerFactory.getLogger(FundsListController.class);

    @Autowired
    private FundsListService fundsListService;
    @Autowired
    private ProjectDataEOService projectDataEOService;

    @ApiOperation(value = "|科技经费一览|分页查询")
    @GetMapping("/page")
    public ResponseMessage<PageInfo<FundsListVO>> page(ProjectDataEOPage page) throws Exception {
        //得到当前用户权限列表

        final List<RoleEO> roleList = UserUtils.getRoleList();
        if (CollectionUtils.isEmpty(roleList)) {
            throw new AdcDaBaseException("此用户无权限查看");
        }
        final Set<String> roleInfoSet = roleList.stream().map(RoleEO::getExtInfo).collect(Collectors.toSet());
        if (!roleInfoSet.contains(RoleType.RS_ADMIN.getCode())){

            UserEO user = UserUtils.getUser();
            if (ObjectUtil.isNull(user)){
                throw new AdcDaBaseException("登陆可能过期，请登录！");
            }
            page.setTechnicalDirector(user.getUsid());
        }

        Integer rowCount = projectDataEOService.queryByCountForFunds(page);
        page.getPager().setRowCount(rowCount);

        List<FundsListVO> listVOPageInfo=fundsListService.page(page, FundsListType.PAGE.getValue());

        return Result.success(getPageInfo(page.getPager(), listVOPageInfo));
    }


    @ApiOperation(value = "导出经费一览数据")
    @PostMapping("/export")
    public ResponseMessage exportExpertGroup(HttpServletResponse response, String fileName, @RequestBody ProjectDataEOPage page)
            throws Exception{
        //得到当前用户权限列表
        final List<RoleEO> roleList = UserUtils.getRoleList();
        if (CollectionUtils.isEmpty(roleList)) {
            throw new AdcDaBaseException("此用户无权限查看");
        }
        final Set<String> roleInfoSet = roleList.stream().map(RoleEO::getExtInfo).collect(Collectors.toSet());
        if (!roleInfoSet.contains(RoleType.RS_ADMIN.getCode())){

            UserEO user = UserUtils.getUser();
            if (ObjectUtil.isNull(user)){
                throw new AdcDaBaseException("登陆可能过期，请登录！");
            }
            page.setTechnicalDirector(user.getUsid());
        }

        String finalFileName = "";
        if (StringUtils.isEmpty(fileName)){
            finalFileName = "经费一览导出数据.xlsx";
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
            Workbook workbook = fundsListService.exportProjectData(exportParams,page);
            workbook.write(outputStream);
            outputStream.flush();
        } catch (Exception e){
            logger.error(e.getMessage(), e);
            throw new AdcDaBaseException("导出失败，请重试");
        }
        return Result.success();
    }

}
