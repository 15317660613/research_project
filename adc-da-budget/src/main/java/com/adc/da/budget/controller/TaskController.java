package com.adc.da.budget.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.budget.dto.PageDTO;
import com.adc.da.budget.entity.Project;
import com.adc.da.budget.entity.Task;
import com.adc.da.budget.entity.TaskResultEO;
import com.adc.da.budget.entity.TaskWithResult;
import com.adc.da.budget.schedule.BudgetScheduleService;
import com.adc.da.budget.service.MyService;
import com.adc.da.budget.service.TaskIOService;
import com.adc.da.budget.service.TaskInsertUpdateService;
import com.adc.da.budget.service.TaskService;
import com.adc.da.budget.vo.TaskResultVO;
import com.adc.da.budget.vo.TaskVO;
import com.adc.da.budget.vo.TreeTaskVO;
import com.adc.da.excel.poi.excel.entity.ExportParams;
import com.adc.da.excel.poi.excel.entity.ImportParams;
import com.adc.da.excel.poi.excel.entity.enums.ExcelType;
import com.adc.da.file.entity.FileEO;
import com.adc.da.file.service.FileEOService;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.sys.service.UserEOService;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.http.PageInfo;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.utils.BeanMapper;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.Encodes;
import com.adc.da.util.utils.IOUtils;
import com.adc.da.util.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

/**
 * @Auther: ZHAIKAIXUAN
 * @Date: 2018/11/19 13:09
 * @Description: /*
 */
@RestController
@RequestMapping("/${restPath}/budget/task")
@Api("|Task|")
public class TaskController extends BaseController<Task> {
    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

    @Autowired
    private TaskService taskService;

    @Autowired
    private MyService myService;

    @Autowired
    private UserEOService userEOService;

    @Autowired
    private TaskInsertUpdateService taskInsertUpdateService;

    @Autowired
    private FileEOService fileEOService;

    @Autowired
    private BeanMapper beanMapper;

    /**
     * @Description:增删改
     * @auther: ZHAIKAIXUAN
     * @params:
     * @return:
     * @date: 2018/11/19 13:10
     */

    @ApiOperation(value = "|Task|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    //@RequiresPermissions("fin:task:save")
    public ResponseMessage<Task> insert(@RequestBody @Valid TaskVO taskVO) throws Exception {
        Task task = taskInsertUpdateService.insert(taskVO);
        return Result.success(task);
    }

    @ApiOperation("|Task|删除可批量删除")
    @DeleteMapping("/{ids}")
    //@RequiresPermissions("fin:task:delete")
    public ResponseMessage<String> delete(@PathVariable(value = "ids") String ids) throws Exception {
        return Result.success(taskService.deleteBatch(ids));
    }

    @Deprecated
    @ApiOperation("|Task|清空数据.删除已修改为软删除，慎用此方法")
    @DeleteMapping("/delete/all")
    public ResponseMessage<String> deleteAll() {
        taskService.deleteAll();
        return Result.success();
    }

    @ApiOperation("|Task|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    //@RequiresPermissions("fin:task:update")
    public ResponseMessage<Task> update(@RequestBody @Valid TaskVO taskVO) {
        return Result.success(taskInsertUpdateService.update(taskVO));
    }

    @ApiOperation("|Task|查找")
    @GetMapping("/query/{id}")
    //@RequiresPermissions("fin:task:get")
    @RequiresAuthentication
    public ResponseMessage<Task> query(@PathVariable("id") @NotNull String id) throws Exception {
        return Result.success(taskService.querySingle(id));
    }

    @ApiOperation("|Task|查找")
    @GetMapping("/queryWithResult/{id}")
    //@RequiresPermissions("fin:task:get")
    @RequiresAuthentication
    public ResponseMessage<TaskWithResult> queryWithResult(@PathVariable("id") @NotNull String id) throws Exception {
        TaskWithResult taskWithResult = new TaskWithResult();
        Task task = taskService.querySingle(id);
        List<TaskResultVO> taskResultVOList = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(task.getTaskResultEOList())) {
            for (TaskResultEO eo : task.getTaskResultEOList()) {
                TaskResultVO taskResultVO = beanMapper.map(eo, TaskResultVO.class);
                List<FileEO> fileEOList = fileEOService.selectByForeignId(eo.getId());
                if (CollectionUtils.isNotEmpty(fileEOList)) {
                    FileEO fileEO = fileEOList.get(0);
                    UserEO userEO = userEOService.selectByPrimaryKey(fileEO.getUserId());
                    if (null != userEO) {
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
        taskWithResult.setTask(task);
        taskWithResult.setTaskResultVOList(taskResultVOList);
        return Result.success(taskWithResult);
    }

    @ApiOperation("|Task|判断当前任务是否存在")
    @GetMapping("/existTask/{id}")
    //@RequiresPermissions("fin:task:get")
    @RequiresAuthentication
    public ResponseMessage<Boolean> existTask(@PathVariable("id") @NotNull String id) {
        return Result.success(taskService.existTask(id));
    }

    @ApiOperation("|Task|查找所有")
    @GetMapping("/query/all")
    //@RequiresPermissions("fin:task:list")
    public ResponseMessage<List<Task>> queryAll(@RequestParam Integer type) {
        return Result.success(taskService.queryAll(type));
    }

    @ApiOperation(value = "|Task|分页查询")
    @GetMapping("/page")
    public ResponseMessage<PageDTO> page(@RequestParam Integer page, @RequestParam Integer size, String projectId,
        String budgetId, Integer type) {
        //查询任务列表并返回s
        return Result.success(taskService
            .queryByPage(page == null ? 1 : page, size == null ? 10 : size, projectId, budgetId, type));
    }

    @ApiOperation(value = "|Task|导出")
    @GetMapping("/export")
//    @RequiresPermissions("fin:task:export")
    public ResponseMessage excelImport(HttpServletResponse response, String fileName, @RequestParam Integer type) {
        if (StringUtils.isEmpty(fileName)) {
            fileName = "数据表格.xlsx";
        } else {
            if (!fileName.contains(".xlsx")) {
                fileName += ".xlsx";
            }
        }
        OutputStream os = null;
        Workbook workbook = null;

        try {
            response.setHeader(
                "Content-Disposition",
                "attachment; filename=" + Encodes.urlEncode(fileName));
            response.setContentType("application/force-download");
            ExportParams params = new ExportParams();
            params.setType(ExcelType.XSSF);
            workbook = taskService.excelExport(params, type);
            os = response.getOutputStream();
            workbook.write(os);
            os.flush();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new AdcDaBaseException("导出失败，请重试");
        } finally {
            IOUtils.closeQuietly(os);
        }
        return Result.success();
    }

    @Autowired
    private TaskIOService taskIOService;

    @ApiOperation(value = "|Task|无验证的Excel单sheet导入")
    @PostMapping("/excelImport")
    //@RequiresPermissions("fin:task:import")
    public ResponseMessage<Map<String, String>> excelImport(@RequestParam("file") MultipartFile file,
        Boolean saveDataFlag, String baseId) throws Exception {
        InputStream is = file.getInputStream();
        ImportParams params = new ImportParams();
        boolean flag = true;
        if (null == saveDataFlag || !saveDataFlag) {
            flag = false;
        }
        return Result.success(taskIOService.excelImport(is, params, flag, baseId));
    }


    @ApiOperation(value = "日常类任务导入|Task|无验证的Excel单sheet导入")
    @PostMapping("/excelImportDailyTask")
    //@RequiresPermissions("fin:task:import")
    public ResponseMessage<Map<String, String>> excelImportDailyTask(@RequestParam("file") MultipartFile file) throws Exception {
        InputStream is = file.getInputStream();
        ImportParams params = new ImportParams();
        taskIOService.excelImportDailyTask(is, params);
        return Result.success();
    }


    /**
     * 根据任务ID查询所属子任务
     *
     * @param taskId
     * @return
     */
    @ApiOperation("|Task|根据任务ID查询所属子任务")
    @GetMapping("/findChildrenTasksByTaskId")
    public ResponseMessage<PageDTO> findChildrenTasksByTaskId(@RequestParam Integer page, @RequestParam Integer size,
        @RequestParam String taskId) {
        return Result
            .success(taskService.findChildrenTasksByTaskId(page == null ? 1 : page, size == null ? 10 : size, taskId));

    }

    /**
     * 获取添加日报的任务树
     *
     * @return
     * @throws Exception
     */
    @ApiOperation("|Task|添加日报的任务树")
    @GetMapping("/tree")
    public ResponseMessage<List<TreeTaskVO>> getTaskTree(
        @RequestParam(required = false, value = "userId") String userId,
        @RequestParam(required = false, value = "keyword") String keyword) throws Exception {
        return Result.success(taskService.getTaskTree(userId, keyword));
    }

    /**
     * 当前登录用户是否有任务
     */
    @ApiOperation("当前登录用户是否有任务")
    @GetMapping("/haveTask")
    public ResponseMessage<Boolean> haveTask() throws Exception {
        return Result.success(taskService.haveTask());
    }

    @ApiOperation("|Task|查询当前用户的项目")
    @GetMapping("/currentUserProject")
    public ResponseMessage<List<Project>> currentUserProject() {
        return Result.success(taskService.currentUserProject());
    }

    @ApiOperation("|Task|按页查询当前用户的项目")
    @GetMapping("/currentUserProjectByPage")
    public PageInfo currentUserProjectByPage(int pageSize, int pageIndex,String name) {
        PageDTO pageDTO=taskService.currentUserProjectByPage(pageSize,pageIndex,name);
        PageInfo result=new PageInfo();
        result.setList(pageDTO.getDataList());
        result.setPageNo(pageIndex);
        result.setPageSize(pageSize);
        result.setCount(pageDTO.getCount());
        return result;
    }

    @ApiOperation("|Task|查询ES中经营和科研项目ID")
    @GetMapping("/getProjectIdsInElasticSearch")
    public String getProjectIdsInElasticSearch() {
      return taskService.getAllProjectIdsInEs();
    }


    @ApiOperation("|Task|查询当前用户的任务")
    @GetMapping("/getMyTask")
    public ResponseMessage<List<Task>> getMyTask() {
        return Result.success(taskService.getMyTask());
    }

    @ApiOperation("|Task|移动子任务到另一个任务")
    @GetMapping("/moveChildTask2Task")
    public ResponseMessage moveChildTask2Task(String childTaskId , String targetTaskId) {
        taskService.moveChildTask2Task(childTaskId,targetTaskId);
        return Result.success();
    }

    @ApiOperation("|Task|移动任务到另一个项目")
    @GetMapping("/moveTask2Project")
    public ResponseMessage moveTask2Project(String TaskId , String targetProjectId) {
        taskService.moveTask2Project(TaskId,targetProjectId);
        return Result.success();
    }

    @ApiOperation("|Task|临时方法设置用户项目关联")
    @GetMapping("/setUserWithProjectsData")
    public ResponseMessage<String> setUserWithProjectsData() {
        return Result.success(taskService.setUserWithProjectsData());
    }

    @ApiOperation("|Task|改变任务的完成状态")
    @GetMapping("/changeTaskStatus/{taskId}/{btnType}")
    public ResponseMessage<String> changeTaskStatus(@PathVariable(value = "taskId") String taskId,
        @PathVariable(value = "btnType") String btnType) {
        return Result.success(taskService.changeTaskStatus(taskId, btnType));
    }

    @ApiOperation("|Task|根据业务查询任务")
    @GetMapping("/queryTasksByBudgetId/{id}")
    public ResponseMessage<List<Task>> queryTasksByBudgetId(@PathVariable String id) {
        return Result.success(taskService.getTasksByBudgetId(id));
    }

    @ApiOperation("|Task|判断当前任务下有无子任务或者子任务有无全完成")
    @GetMapping("/queryTaskStatusById/{taskId}")
    public ResponseMessage<Boolean> queryTaskStatusById(@PathVariable String taskId) {
        int count = taskService.queryTaskStatusById(taskId);
        if (count == 0) {
            return Result.success(true);
        }
        return Result.success(false);
    }

    @ApiOperation("|Task|将商务经理刷进项目成员中")
    @GetMapping("/refreshProject")
    //@RequiresPermissions("fin:task:get")

    public ResponseMessage<String> refreshProject() throws Exception {
        return Result.success(taskService.refreshProject());
    }

    @ApiOperation("|Task|根据任务id和人员id 检查任务的子任务是否存在该人员,返回bool值")
    @GetMapping("/checkUserInChildTask/{taskId}/{userId}")
    //@RequiresPermissions("fin:task:get")
    public ResponseMessage<Boolean> checkUserInChildTask(@PathVariable("taskId")String taskId , @PathVariable("userId") String userId) throws Exception {
        return Result.success(taskService.checkUserInChildTask(taskId,userId));
    }
    @Autowired
    private BudgetScheduleService budgetScheduleService ;


    @ApiOperation("将被离职的员工，从所在的任务，项目，子任务中移除掉")
    @GetMapping("/doTask")
    //@RequiresPermissions("fin:task:get")
    public ResponseMessage doTask() throws Exception {
        budgetScheduleService.doTask();
        return Result.success();
    }

}
