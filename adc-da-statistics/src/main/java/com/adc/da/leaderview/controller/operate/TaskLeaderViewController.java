package com.adc.da.leaderview.controller.operate;

import com.adc.da.base.page.Pager;
import com.adc.da.budget.constant.Role;
import com.adc.da.budget.dto.PageDTO;
import com.adc.da.budget.entity.BudgetEO;
import com.adc.da.budget.entity.Task;
import com.adc.da.budget.entity.UserWithProjects;
import com.adc.da.budget.query.task.TaskQuery;
import com.adc.da.budget.repository.UserWithProjectsRepository;
import com.adc.da.budget.service.TaskService;
import com.adc.da.leaderview.service.operate.ProjectLeaderViewService;
import com.adc.da.leaderview.service.operate.TaskLeaderViewService;
import com.adc.da.leaderview.vo.ProjectLeaderViewVO;
import com.adc.da.leaderview.vo.TaskLeaderViewVO;
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
@RequestMapping("/${restPath}/operate/taskLeaderView")
@Api(tags = "|领导视角|")
public class TaskLeaderViewController {

    @Autowired
    private TaskLeaderViewService taskLeaderViewService ;


//    @ApiOperation(value = "||查询")
//    @GetMapping("/searchByLoginUser")
//    //@RequiresPermissions("statistics:businessWorktime:list")
//    public ResponseMessage<PageDTO> searchByLoginUser(int page , int size) {
//        String userId = UserUtils.getUserId();
//        if (StringUtils.isEmpty(userId)) {
//            throw new AdcDaBaseException("登陆可能过期，请登录！");
//        }
//        Subject subject = SecurityUtils.getSubject();
//        //projectLeaderViewService.searchByLoginUser(userId,subject);
//
//        return Result.success(taskLeaderViewService.searchByLoginUser(userId,subject,page,size));
//    }

    @ApiOperation("参与任务任务查询页")
    @PostMapping("/findByPage")
    public ResponseMessage<PageInfo<TaskLeaderViewVO>> findByPage(@RequestBody TaskQuery page){
        List<TaskLeaderViewVO> rows = taskLeaderViewService.searchByLoginUser(page);

        return Result.success(getPageInfo(page.getPager(), rows));
    }

    /**
     *
     * @param pager
     * @param rows
     * @return
     */
    public PageInfo<TaskLeaderViewVO> getPageInfo(Pager pager, List<TaskLeaderViewVO> rows) {
        PageInfo<TaskLeaderViewVO> pageInfo = new PageInfo();
        pageInfo.setList(rows);
        pageInfo.setCount((long)pager.getRowCount());
        pageInfo.setPageSize(pager.getPageSize());
        pageInfo.setPageCount((long)pager.getPageCount());
        pageInfo.setPageNo(pager.getPageId());
        return pageInfo;
    }


}
