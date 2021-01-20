package com.adc.da.budget.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.adc.da.base.page.Pager;
import com.adc.da.budget.entity.ChildrenTask;
import com.adc.da.budget.entity.Task;
import com.adc.da.budget.entity.TaskWithResult;
import com.adc.da.budget.repository.ChildTaskRepository;
import com.adc.da.budget.repository.TaskRepository;
import com.adc.da.budget.service.ChildrenTaskService;
import com.adc.da.budget.service.TaskService;
import com.adc.da.budget.vo.TaskResultDetailVO;
import com.adc.da.budget.vo.TaskResultVO;
import com.adc.da.file.entity.FileEO;
import com.adc.da.file.page.MyFilePage;
import com.adc.da.file.service.FileEOService;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.sys.service.UserEOService;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.BeanMapper;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.StringUtils;
import com.adc.da.util.utils.UUID;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.adc.da.base.web.BaseController;
import com.adc.da.budget.entity.TaskResultEO;
import com.adc.da.budget.page.TaskResultEOPage;
import com.adc.da.budget.service.TaskResultEOService;

import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.http.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/${restPath}/budget/taskResult")
@Api(description = "|TaskResultEO|")
public class TaskResultEOController extends BaseController<TaskResultEO>{

    private static final Logger logger = LoggerFactory.getLogger(TaskResultEOController.class);

    @Autowired
    private TaskResultEOService taskResultEOService;

    @Autowired
    private FileEOService fileEOService ;

    @Autowired
    private TaskService taskService ;

    @Autowired
    private ChildrenTaskService childrenTaskService ;

    @Autowired
    private BeanMapper beanMapper ;

    @Autowired
    private TaskRepository taskRepository ;

    @Autowired
    private ChildTaskRepository childTaskRepository;

    @Autowired
    private UserEOService userEOService ;



	@ApiOperation(value = "|TaskResultEO|分页查询")
    @GetMapping("/page")
//    @RequiresPermissions("budget:taskResult:page")
    public ResponseMessage<PageInfo<TaskResultEO>> page(TaskResultEOPage page) throws Exception {
        List<TaskResultEO> rows = taskResultEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|TaskResultEO|查询")
    @GetMapping("")
//    @RequiresPermissions("budget:taskResult:list")
    public ResponseMessage<List<TaskResultEO>> list(TaskResultEOPage page) throws Exception {
        return Result.success(taskResultEOService.queryByList(page));
	}

    @ApiOperation(value = "|TaskResultEO|详情")
    @GetMapping("/{id}")
//    @RequiresPermissions("budget:taskResult:get")
    public ResponseMessage<TaskResultEO> find(@PathVariable String id) throws Exception {
        return Result.success(taskResultEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|TaskResultEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("budget:taskResult:save")
    public ResponseMessage<TaskResultEO> create(@RequestBody TaskResultEO taskResultEO) throws Exception {
	    taskResultEO.setId(UUID.randomUUID10());
        taskResultEOService.insertSelective(taskResultEO);
        return Result.success(taskResultEO);
    }

    @ApiOperation(value = "|TaskResultEO|批量新增")
    @PostMapping(value = "/createList")
//    @RequiresPermissions("budget:taskResult:save")
    public ResponseMessage<TaskResultEO[]> createList(@RequestBody TaskResultEO[] taskResultEOS) throws Exception {
        taskResultEOService.insertList(taskResultEOS);
        return Result.success(taskResultEOS);
    }

    @ApiOperation(value = "|TaskResultEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("budget:taskResult:update")
    public ResponseMessage<TaskResultEO> update(@RequestBody TaskResultEO taskResultEO) throws Exception {
        taskResultEOService.updateByPrimaryKeySelective(taskResultEO);
        return Result.success(taskResultEO);
    }

    @ApiOperation(value = "|TaskResultEO|批量修改")
    @PutMapping(value = "/updateList")
//    @RequiresPermissions("budget:taskResult:update")
    public ResponseMessage<TaskResultEO[]> updateList(@RequestBody TaskResultEO[] taskResultEOS) throws Exception {
        taskResultEOService.updateList(taskResultEOS);
        return Result.success(taskResultEOS);
    }

    @ApiOperation(value = "|TaskResultEO|删除")
    @DeleteMapping("/{id}")
//    @RequiresPermissions("budget:taskResult:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        taskResultEOService.deleteByPrimaryKey(id);
        logger.info("delete from PF_TASK_RESULT where id = {}", id);
        return Result.success();
    }

//    @ApiOperation("|Task|根据任务id查询成果物下的文件")
//    @GetMapping("/queryTaskResultFileByTaskId/{id}")
//    public ResponseMessage<List<FileEO>> queryTaskResultFileByTaskId(@PathVariable String id) {
//        List<TaskResultEO> taskResultEOList = taskResultEOService.selectByTaskId(id);
//        List<FileEO> retFileList = new ArrayList<>() ;
//        for (TaskResultEO eo : taskResultEOList) {
//            List<FileEO> fileEOList  =   fileEOService.selectByForeignId(eo.getId());
//            if (CollectionUtils.isNotEmpty(fileEOList)){
//                FileEO fileEO = fileEOList.get(0);
//                retFileList.add(fileEO);
//            }
//        }
//        retFileList.addAll(fileEOService.selectByForeignId(id));
//        return Result.success(retFileList);
//    }




    @ApiOperation("|Task/childTask|查找")
    @GetMapping("/queryWithResult/{id}")
    //@RequiresPermissions("fin:task:get")
    @RequiresAuthentication
    public ResponseMessage<TaskResultDetailVO> queryWithResult(@PathVariable("id") @NotNull String id)
        throws Exception {
        TaskResultDetailVO taskResultDetailVO = new TaskResultDetailVO();
        Task task = taskRepository.findByIdAndDelFlagNot(id, Boolean.TRUE);
        ChildrenTask childrenTask;
        List<TaskResultEO> taskResultEOList = taskResultEOService.selectByTaskId(id);
        List<TaskResultVO> taskResultVOList = new ArrayList<>();
        if (null == task) {
            childrenTask = childTaskRepository.findByIdAndDelFlagNot(id, Boolean.TRUE);
            if (null == childrenTask) {
                throw new AdcDaBaseException("当前id查询不到任务或子任务，请与管理员联系！");
            } else {
                taskResultDetailVO.setId(childrenTask.getId());
                taskResultDetailVO.setTaskId(childrenTask.getTaskId());
                taskResultDetailVO.setMemberNames(Arrays.asList(childrenTask.getMemberNames()));
                taskResultDetailVO.setProjectLeaderId(childrenTask.getProjectLeaderId());
                taskResultDetailVO.setPm(childrenTask.getPm());
                taskResultDetailVO.setPlanStartTime(childrenTask.getPlanStartTime());
                taskResultDetailVO.setPlanEndTime(childrenTask.getPlanEndTime());
                taskResultDetailVO.setTaskName(childrenTask.getChildTaskName());
                taskResultDetailVO.setTaskTarget(childrenTask.getTaskTarget());
                taskResultDetailVO.setFinishedTime(childrenTask.getActualFinishedTime());
            }
        } else {

            taskResultDetailVO.setId(task.getId());
            taskResultDetailVO.setMemberNames(Arrays.asList(task.getMemberNames()));
            taskResultDetailVO.setProjectLeaderId(task.getProjectLeaderId());
            taskResultDetailVO.setPm(task.getPm());
            taskResultDetailVO.setPlanStartTime(task.getPlanStartTime());
            taskResultDetailVO.setPlanEndTime(task.getPlanEndTime());
            taskResultDetailVO.setTaskName(task.getName());
            taskResultDetailVO.setTaskTarget(task.getTaskTarget());
            taskResultDetailVO.setFinishedTime(task.getFinishedActualTime());
        }

        if (CollectionUtils.isNotEmpty(taskResultEOList)) {

            for (TaskResultEO eo : taskResultEOList) {
                TaskResultVO taskResultVO = beanMapper.map(eo, TaskResultVO.class);
                List<FileEO> fileEOList = fileEOService.selectByForeignId(eo.getId());
                if (CollectionUtils.isNotEmpty(fileEOList)) {
                    FileEO fileEO = fileEOList.get(0);
                    UserEO userEO = userEOService.selectByPrimaryKey(fileEO.getUserId());
                    if (null != userEO){
                        taskResultVO.setUploadUserName(userEO.getUsname());
                    }
                    taskResultVO.setFileId(fileEO.getFileId());
                    taskResultVO.setFileType(fileEO.getFileType());
                    taskResultVO.setFileSize(fileEO.getFileSize());
                    taskResultVO.setUploadTime(fileEO.getCreateTime());
                    taskResultVO.setFileName(fileEO.getFileName());
                    taskResultVO.setUploadUserId(fileEO.getUserId());
                }
                taskResultVOList.add(taskResultVO);
            }
        }

        taskResultDetailVO.setTaskResultVOList(taskResultVOList);
        return Result.success(taskResultDetailVO);
    }

    @ApiOperation("|Task|根据任务id查询成果物下的文件")
    @GetMapping("/queryTaskResultFilePageByTaskId/{id}/{pageIndex}/{size}")
    public ResponseMessage<PageInfo<TaskResultVO>> queryTaskResultFileByPage(@PathVariable("id") String id ,
            @PathVariable("pageIndex")int pageIndex  , @PathVariable("size")int size){
         return Result.success(taskResultEOService.queryTaskResultFileByPage(id, pageIndex, size));
    }









}
