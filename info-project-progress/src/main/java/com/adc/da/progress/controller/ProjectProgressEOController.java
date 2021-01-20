package com.adc.da.progress.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.progress.entity.ProjectMilepostEO;
import com.adc.da.progress.entity.ProjectMilepostResultEO;
import com.adc.da.progress.entity.ProjectProgressEO;
import com.adc.da.progress.page.ProjectMilepostEOPage;
import com.adc.da.progress.page.ProjectMilepostResultEOPage;
import com.adc.da.progress.service.ProjectMilepostEOService;
import com.adc.da.progress.service.ProjectMilepostResultEOService;
import com.adc.da.progress.service.ProjectProgressEOService;
import com.adc.da.progress.vo.TreeVO;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * describe:
 *
 * @author 李坤澔
 *     date 2019-07-18
 */
@RestController
@RequestMapping("/${restPath}/progress/detail")
@Api(tags = "|项目进度|")
public class ProjectProgressEOController extends BaseController<ProjectProgressEO> {

    @Autowired
    private ProjectProgressEOService projectProgressEOService;

    @Autowired
    private ProjectMilepostEOService projectMilepostEOService ;

    @Autowired
    private ProjectMilepostResultEOService projectMilepostResultEOService ;

    @ApiOperation(value = "|ProjectNameEO|获取项目进度图所需的数据")
    @GetMapping("/list")
    public ResponseMessage<List<ProjectProgressEO>> page(String projectId) throws Exception {
        if (StringUtils.isNotEmpty(projectId)) {
            List<ProjectProgressEO> rows = projectProgressEOService.getProjectProgressPage(projectId);
            return Result.success(rows);

        } else {
            throw new AdcDaBaseException("projectId is null");
        }

    }


    @ApiOperation(value = "|ProjectNameEO|获取项目进度树")
    @GetMapping("/tree")
    public ResponseMessage<List<TreeVO>> tree(String projectId) throws Exception {


            return Result.success(projectProgressEOService.getTree(projectId));

    }





}
