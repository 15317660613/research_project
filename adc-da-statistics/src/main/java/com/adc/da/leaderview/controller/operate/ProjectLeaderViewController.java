package com.adc.da.leaderview.controller.operate;

import com.adc.da.base.page.BasePage;
import com.adc.da.base.page.Pager;
import com.adc.da.budget.constant.Role;
import com.adc.da.budget.dto.PageDTO;
import com.adc.da.budget.entity.BudgetEO;
import com.adc.da.budget.entity.Project;
import com.adc.da.budget.entity.UserWithProjects;
import com.adc.da.budget.query.project.ProjectQuery;
import com.adc.da.budget.repository.UserWithProjectsRepository;
import com.adc.da.leaderview.service.operate.ProjectLeaderViewService;
import com.adc.da.leaderview.vo.ProjectLeaderViewVO;
import com.adc.da.login.util.UserUtils;
import com.adc.da.statistics.entity.BusinessWorktimeEO;
import com.adc.da.statistics.page.BusinessWorktimeEOPage;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.http.PageInfo;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/${restPath}/operate/projectLeaderView")
@Api(tags = "|领导视角|")
public class ProjectLeaderViewController {

    @Autowired
    private ProjectLeaderViewService projectLeaderViewService ;

//    @ApiOperation(value = "||查询")
//    @GetMapping("/searchByLoginUser")
//    //@RequiresPermissions("statistics:businessWorktime:list")
//    public ResponseMessage<PageDTO> searchByLoginUser(int page , int size) {
//        String userId = UserUtils.getUserId();
//        if (StringUtils.isEmpty(userId)) {
//            throw new AdcDaBaseException("登陆可能过期，请登录！");
//        }
//        Subject subject = SecurityUtils.getSubject();
//
//        return Result.success(projectLeaderViewService.searchByLoginUser(userId,subject,page,size));
//    }

    @ApiOperation("管理项目查询页")
    @PostMapping("/findManagerByPage")
    public ResponseMessage<PageInfo<ProjectLeaderViewVO>> findManagerByPage(@RequestBody ProjectQuery page) throws  Exception{
        List<ProjectLeaderViewVO> rows = projectLeaderViewService.searchManagerPageByUserIdList(page);
        return Result.success(getPageInfo(page.getPager(),rows));
    }

    @ApiOperation("参与项目查询页")
    @PostMapping("/findJoinByPage")
    public ResponseMessage<PageInfo<ProjectLeaderViewVO>> findJoinByPage(@RequestBody ProjectQuery page) throws  Exception{
        List<ProjectLeaderViewVO> rows = projectLeaderViewService.searchJoinPageByUserIdList(page);

        return Result.success(getPageInfo(page.getPager(),rows));
    }

    @ApiOperation("部门提交科研项目（课题）")
    @PostMapping("/findDeptCommitProject")
    public ResponseMessage<PageInfo<ProjectLeaderViewVO>> findDeptCommitProject(@RequestBody ProjectQuery page) throws Exception{
        List<ProjectLeaderViewVO> rows = projectLeaderViewService.findDeptCommitProject(page);

        return Result.success(getPageInfo(page.getPager(),rows));
    }

    @ApiOperation("中心科研项目（课题）")
    @PostMapping("/findDeptRunProject")
    public ResponseMessage<PageInfo<ProjectLeaderViewVO>> findDeptRunProject(@RequestBody ProjectQuery page) throws Exception{
        List<ProjectLeaderViewVO> rows = projectLeaderViewService.findDeptCommitProject(page);

        return Result.success(getPageInfo(page.getPager(),rows));
    }

    private PageInfo<ProjectLeaderViewVO> getPageInfo(Pager pager ,  List<ProjectLeaderViewVO> rows){
        PageInfo<ProjectLeaderViewVO> pageInfo = new PageInfo();
        pageInfo.setList(rows);
        pageInfo.setCount((long)pager.getRowCount());
        pageInfo.setPageSize(pager.getPageSize());
        pageInfo.setPageCount((long)pager.getPageCount());
        pageInfo.setPageNo(pager.getPageId());
        return pageInfo ;
    }


}
