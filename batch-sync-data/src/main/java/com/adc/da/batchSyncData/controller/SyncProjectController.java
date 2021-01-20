package com.adc.da.batchSyncData.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.batchSyncData.entity.ProjectEO;
import com.adc.da.batchSyncData.service.ProjectEOService;
import com.adc.da.batchSyncData.service.TaskUpdateService;
import com.adc.da.budget.entity.Project;
import com.adc.da.budget.entity.Task;
import com.adc.da.budget.repository.ProjectRepository;
import com.adc.da.budget.repository.TaskRepository;
import com.adc.da.budget.service.BudgetEOService;
import com.adc.da.budget.vo.ProjectVO;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping("/${restPath}/syncProjectData")
@Api(tags = "|ProjectEO_DATAUPDATE|")
public class SyncProjectController extends BaseController<ProjectEO> {
    private static final Logger logger = LoggerFactory.getLogger(SyncProjectController.class);

    @Autowired
    private ProjectEOService projectEOService;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private BudgetEOService budgetEOService;

    @Autowired
    private TaskUpdateService taskUpdateService;

    @ApiOperation("|Project|批量更新")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    //@RequiresPermissions("fin:project:update")
    public ResponseMessage<String> update(@RequestBody @Valid ProjectVO projectVO) throws Exception {
        projectEOService.batchUpdateProject(projectVO.getProjectType());
        return Result.success();
    }

    @Deprecated
    @ApiOperation(value = "更新旧project，关于项目类型的")
    @GetMapping("/updateOldProject")
    public void updateProject(String property, String name) {
        List<String> budgetIds = budgetEOService.queryAllBudgetByTypeName(property, name);
        List<Project> rows = projectRepository.findByBudgetIdIn(budgetIds);
        int type = -1;
        if (StringUtils.equals(property, "0")) {
            type = -1;
        }
        if (StringUtils.equals(property, "1")) {
            type = -2;
        }
        if (StringUtils.equals(property, "2")) {
            type = -3;
        }
        for (Project project : rows) {
            project.setProjectType(type);
           /* if(project.getBusinessId().equals("NFA4VVKFHZ")){
                continue;
            }*/
        }
        projectRepository.save(rows);
    }

//    @Deprecated
//    @ApiOperation("|Task|更新task增加任务标识字段")
//    @GetMapping("/updateTask")
//    public ResponseMessage<Boolean> updateTask(Integer projectType) {
//        //1，查询经营类项目id
//        List<String> projectIds = projectEOService.getProjectIdsByProjectType(projectType);
//        List<String> budgetIds = budgetEOService.queryAllBudgetByType(String.valueOf(projectType));
//
//        List<Task> taskList = taskRepository.findByBudgetIdInAndDelFlagNot(budgetIds, true);
//
//        List<List<String>> resultList = CommonUtil.splitList(new ArrayList<>(projectIds), 100);
//        for (List<String> list : resultList) {
//            List<Task> tasks = taskRepository.findByProjectIdInAndDelFlagNot(list, true);
//            for (Task task : tasks) {
//                task.setProjectType(projectType);
////                addMemberNames(task);
//                if (task.getName().contains("360")) {
//                    continue;
//                }
//
//            }
//            taskRepository.save(tasks);
//        }
//        for (Task task : taskList) {
//            //addMemberNames(task);
//            task.setProjectType(projectType);
//        }
//        taskRepository.save(taskList);
//        System.out.println("dfmaljfdl;as");
//
//        return Result.success(false);
//    }

    private void addMemberNames(Task task) {
        if (CollectionUtils.isNotEmpty(task.getMemberNames())) {
            task.setMemberNameString(StringUtils.join(task.getMemberNames(), ","));
        }
    }

    @ApiOperation("|Task|批量更新任务类型")
    @PutMapping(value = "/updateTaskType", consumes = APPLICATION_JSON_UTF8_VALUE)
    //@RequiresPermissions("fin:project:update")
    public ResponseMessage<String> updateTaskType(@RequestBody @Valid ProjectVO projectVO) throws Exception {
        projectEOService.batchUpdateTaskType();
        return Result.success();
    }

    @Deprecated
    @ApiOperation(value = "更新旧task，关于项目类型的")
    @GetMapping("/updateOldTaskType")
    public void updateOldTaskType(String property, String name) {
        //更新旧业务任务
        if (StringUtils.isNotEmpty(name)) {
            List<String> budgetIds = budgetEOService.queryAllBudgetByTypeName(property, name);
            List<Task> rows = taskRepository.findByBudgetIdIn(budgetIds);
            Integer type = -1;
            if (StringUtils.equals(property, "0")) {
                type = -1;
            }
            if (StringUtils.equals(property, "1")) {
                type = -2;
            }
            if (StringUtils.equals(property, "2")) {
                type = -3;
            }
            for (Task task : rows) {
                task.setProjectType(type);
            }
            taskRepository.save(rows);
        } else {
            List<String> projectIds = projectEOService.getProjectIdsByProjectType(Integer.valueOf(property));
            List<Task> tasks = taskRepository.findByProjectIdInAndDelFlagNot(projectIds, true);
            for (Task task : tasks) {
                task.setProjectType(Integer.valueOf(property));
            }
            taskRepository.save(tasks);
        }
    }

    @ApiOperation(value = "更新旧project，关于所属部门")
    @GetMapping("/updateOldProjectDept")
    public void updateProjectDept() {
        projectEOService.updateProjectDept();
    }

    @ApiOperation(value = "更新经营类项目，关于domainId")
    @GetMapping("/updateDomainId")
    public void updateDomainId() {
        projectEOService.updateDomainId();
    }

    @ApiOperation(value = "删除脏数据-包含userWithProject")
    @DeleteMapping("/deleteOldRSProject")
    public void deleteOrgProject() {
        projectEOService.a();
    }

    @ApiOperation(value = "initUserWithProject")
    @GetMapping("/initUserWithProject")
    public void css() {
        projectEOService.initUserWithProject();
    }

    @ApiOperation(value = "更新旧Task，关于BudgetSearchId")
    @GetMapping("/updateOldTaskBudgetSearchId")
    public void updateOldTaskBudgetSearchId() {
        taskUpdateService.updateBudgetSearchId();
    }

    @ApiOperation(value = "checkDuplicateData")
    @GetMapping("/checkDuplicateData")
    public void checkDuplicateData() {
        projectEOService.checkDuplicateData();
    }
}
