package com.adc.da.research.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.research.entity.DeclarationEO;
import com.adc.da.research.page.DeclarationEOPage;
import com.adc.da.research.service.DeclarationEOService;
import com.adc.da.research.vo.DeclarationPageVO;
import com.adc.da.util.http.PageInfo;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
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
import java.util.Date;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping("/${restPath}/research/declaration")
@Api(tags = "|科研类项目模块-申报|")
@Slf4j
public class DeclarationEOController extends BaseController<DeclarationEO> {

    @Autowired
    private DeclarationEOService declarationEOService;

    private static final int INT_BEGIN = 0;

    private static final int INT_END = 1;

    private Date[] spitTime(String timeArea) {

        String[] x = timeArea.split(" - ");
        Date[] resultDate = new Date[2];
        resultDate[INT_BEGIN] = new Date(Long.parseLong(x[INT_BEGIN]));
        resultDate[INT_END] = new Date(Long.parseLong(x[INT_END]));
        return resultDate;
    }

    @ApiOperation(value = "|分页查询|")
    @GetMapping("/page")
    //  @RequiresPermissions("research:declaration:page")
    public ResponseMessage<PageInfo<DeclarationEO>> page(DeclarationPageVO pageVO)
        throws Exception {
        DeclarationEOPage queryPage = new DeclarationEOPage();

        if (null != pageVO.getPageNo()) {
            queryPage.setPage(pageVO.getPageNo());
        }
        if (null != pageVO.getPageSize()) {
            queryPage.setPageSize(pageVO.getPageSize());
        }

        if (StringUtils.isNotEmpty(pageVO.getUndertakingId())) {
            queryPage.setUndertakingId(pageVO.getUndertakingId());
        }

        if (StringUtils.isNotEmpty(pageVO.getProjectTypeId())) {
            queryPage.setProjectTypeId(pageVO.getProjectTypeId());
        }
        /*
         *  模糊匹配
         */
        String creatorName = pageVO.getCreatorName();
        if (StringUtils.isNotEmpty(creatorName)) {
            queryPage.setCreatorName(creatorName);
        }
        /*
         *  模糊匹配
         */
        String projectName = pageVO.getProjectName();
        if (StringUtils.isNotEmpty(projectName)) {
            queryPage.setProjectName(projectName);
        }

        /*
         *  模糊匹配
         */
        String leaderName = pageVO.getLeaderName();
        if (StringUtils.isNotEmpty(leaderName)) {
            queryPage.setLeaderName(leaderName);
        }
        /*
         *  模糊匹配
         */
        String deptName = pageVO.getDeptName();
        if (StringUtils.isNotEmpty(deptName)) {
            queryPage.setDeptName(deptName);
        }

        String createdTimeArea = pageVO.getCreatedTimeArea();
        if (StringUtils.isNotEmpty(createdTimeArea)) {
            Date[] createdTime = spitTime(createdTimeArea);
            queryPage.setCreateTime1(createdTime[INT_BEGIN]);
            queryPage.setCreateTime2(createdTime[INT_END]);
        }

        String submissionTimeArea = pageVO.getSubmissionTimeArea();
        if (StringUtils.isNotEmpty(submissionTimeArea)) {
            Date[] committedTime = spitTime(submissionTimeArea);
            queryPage.setSubmissionTimeBegin(committedTime[INT_BEGIN]);
            queryPage.setSubmissionTimeEnd(committedTime[INT_END]);

        }

        queryPage.setStartIndex(-1);
        queryPage.setEndIndex(-1);
        String orderByStr = pageVO.getOrderBy();
        /*
         * 排序字段
         */
        if (StringUtils.isNotEmpty(orderByStr)) {
            String[] order = orderByStr.split(" - ");
            String filed = DeclarationEO.fieldToColumn(order[0]);
            queryPage.setOrderBy(filed + " " + order[1] + ", create_time_ DESC");
        } else {
            queryPage.setOrderBy("create_time_ DESC");
        }

        List<DeclarationEO> rows = declarationEOService.queryByPage(queryPage);
        return Result.success(getPageInfo(queryPage.getPager(), rows));
    }

    /**
     * 更新id 创建人 创建时间
     *
     * @param declarationEO
     * @return
     */
    @ApiOperation(value = "|新增|")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    //  @RequiresPermissions("research:declaration:save")
    public ResponseMessage<DeclarationEO> create(@RequestBody DeclarationEO declarationEO) {
        declarationEOService.insertSelective(declarationEO);
        return Result.success(declarationEO);
    }

    @ApiOperation(value = "|修改|")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    //  @RequiresPermissions("research:declaration:update")
    public ResponseMessage<DeclarationEO> update(@RequestBody DeclarationEO declarationEO) {
        declarationEOService.updateByPrimaryKeySelective(declarationEO);
        return Result.success(declarationEO);
    }

    @ApiOperation(value = "|删除|")
    @DeleteMapping("/{id}")
    //  @RequiresPermissions("research:declaration:delete")
    public ResponseMessage delete(@PathVariable String id) {
        declarationEOService.deleteById(id);
        return Result.success();
    }

    //导出
    @ApiOperation(value = "|导出word|")
    @GetMapping(value = "/downloadDoc/{id}")
    //@RequiresPermissions("order:projectPlan:update")
    public ResponseMessage downLoadWeekReport(HttpServletResponse response, @PathVariable String id) {
        return declarationEOService.downloadDoc(id, response);
    }

}
