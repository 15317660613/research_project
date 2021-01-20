package com.adc.da.research.funds.controller;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.hutool.core.util.ObjectUtil;
import com.adc.da.base.page.Pager;
import com.adc.da.base.web.BaseController;
import com.adc.da.login.util.UserUtils;
import com.adc.da.research.funds.eum.RoleType;
import com.adc.da.research.funds.service.FundsPerformService;
import com.adc.da.research.funds.vo.perform.FundsPerformVO;
import com.adc.da.research.funds.vo.perform.PageFundsVO;
import com.adc.da.research.funds.vo.perform.SubjectProgressVO;
import com.adc.da.research.funds.vo.perform.WarnVO;
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
 * @Date: 2020/11/22
 * @Description:
 */

@RestController
@RequestMapping("/${restPath}/research/funds/fundsPerform")
@Api(tags = "科研系统|经费执行")
public class FundsPerformController extends BaseController<FundsPerformVO> {

    private static final Logger logger = LoggerFactory.getLogger(FundsPerformController.class);

    @Autowired
    private FundsPerformService fundsPerformService;
    @Autowired
    private ProjectDataEOService projectDataEOService;

    @ApiOperation(value = "经费执行计算与查询")
    @GetMapping("/page")
    public ResponseMessage<PageInfo<FundsPerformVO>> page(ProjectDataEOPage page) throws Exception{
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
        final PageFundsVO pageFundsVO = fundsPerformService.page(page);
        List<FundsPerformVO> listVOPageInfo=pageFundsVO.getFundsPerformVOS();
        return Result.success(getPageInfoNew(page.getPager(), listVOPageInfo,pageFundsVO.getSize()));
    }

    @ApiOperation(value = "导出经费执行数据")
    @PostMapping("/export")
    public ResponseMessage exportExpertGroup(HttpServletResponse response, String fileName, @RequestBody ProjectDataEOPage page)
            throws Exception{
        //得到当前用户权限列表
        final List<RoleEO> roleList = UserUtils.getRoleList();
        if (com.adc.da.util.utils.CollectionUtils.isEmpty(roleList)) {
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
            finalFileName = "经费结算导出数据.xlsx";
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
            Workbook workbook = fundsPerformService.exportProjectData(exportParams,page);
            workbook.write(outputStream);
            outputStream.flush();
        } catch (Exception e){
            logger.error(e.getMessage(), e);
            throw new AdcDaBaseException("导出失败，请重试");
        }
        return Result.success();
    }

    @ApiOperation(value = "科目进度")
    @GetMapping("/subjectProgress")
    public ResponseMessage<List<SubjectProgressVO>> subjectProgress(String projectId) throws Exception{
        //得到当前用户权限列表
        final List<SubjectProgressVO> subjectProgressVOList = fundsPerformService.subjectProgress(projectId);
        return Result.success(subjectProgressVOList);
    }

    @ApiOperation(value = "报警")
    @PostMapping("/earlyWarning")
    public ResponseMessage<List<SubjectProgressVO>> earlyWarning(@RequestBody WarnVO warnVO) throws Exception{
        fundsPerformService.earlyWarning(warnVO);
        return Result.success();
    }

    public PageInfo<FundsPerformVO> getPageInfoNew(Pager pager, List<FundsPerformVO> rows,Long size) {
        PageInfo<FundsPerformVO> pageInfo = new PageInfo();
        pageInfo.setList(rows);
        pageInfo.setCount(size);
        pageInfo.setPageSize(pager.getPageSize());
        pageInfo.setPageCount((long)pager.getPageCount());
        pageInfo.setPageNo(pager.getPageId());
        return pageInfo;
    }
}
