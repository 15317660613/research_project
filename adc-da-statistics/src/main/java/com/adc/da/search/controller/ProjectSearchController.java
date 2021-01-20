package com.adc.da.search.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.budget.entity.Project;
import com.adc.da.budget.query.project.ProjectQuery;
import com.adc.da.search.service.ProjectSearchService;
import com.adc.da.util.http.PageInfo;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Lee Kwanho 李坤澔
 *     date 2019-10-18
 */
@RestController
@RequestMapping("/${restPath}/budget/project")
@Api(tags = "|Search|")
public class ProjectSearchController extends BaseController<Project> {
    @Autowired
    private ProjectSearchService projectSearchService;

    @ApiOperation("项目搜索-项目查询页")
    @PostMapping("/findByPage")
    public ResponseMessage<PageInfo<Project>> newFindByPage(@RequestBody ProjectQuery page) {
        List<Project> rows = projectSearchService.newFindByPage(page);

        PageInfo<Project> pageInfo = new PageInfo<>();
        pageInfo.setList(rows);
        pageInfo.setCount((long) page.getPager().getRowCount());
        pageInfo.setPageSize(page.getPager().getPageSize());
        pageInfo.setPageCount((long) page.getPager().getPageCount());
        pageInfo.setPageNo(page.getPager().getPageId());
        return Result.success(pageInfo);
    }

    @ApiOperation("项目搜索-项目查询页")
    @PostMapping("/newFindByPage4Form")
    public ResponseMessage<PageInfo<Project>> newFindByPage4Form(@RequestBody ProjectQuery page) {
        List<Project> rows = projectSearchService.newFindByPage4Form(page);

        PageInfo<Project> pageInfo = new PageInfo<>();
        pageInfo.setList(rows);
        pageInfo.setCount((long) page.getPager().getRowCount());
        pageInfo.setPageSize(page.getPager().getPageSize());
        pageInfo.setPageCount((long) page.getPager().getPageCount());
        pageInfo.setPageNo(page.getPager().getPageId());
        return Result.success(pageInfo);
    }
}
