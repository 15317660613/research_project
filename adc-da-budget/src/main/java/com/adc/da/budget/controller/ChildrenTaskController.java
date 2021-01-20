package com.adc.da.budget.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.budget.dto.PageDTO;
import com.adc.da.budget.entity.Business;
import com.adc.da.budget.entity.ChildrenTask;
import com.adc.da.budget.entity.ChildrenTaskWithResult;
import com.adc.da.budget.entity.TaskResultEO;
import com.adc.da.budget.enums.ProjectStatusEnums;
import com.adc.da.budget.service.ChildrenTaskService;
import com.adc.da.budget.vo.ChildrenTaskVO;
import com.adc.da.budget.vo.TaskResultVO;
import com.adc.da.excel.poi.excel.entity.ExportParams;
import com.adc.da.excel.poi.excel.entity.ImportParams;
import com.adc.da.excel.poi.excel.entity.enums.ExcelType;
import com.adc.da.file.entity.FileEO;
import com.adc.da.file.service.FileEOService;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.sys.service.UserEOService;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.utils.BeanMapper;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.Encodes;
import com.adc.da.util.utils.IOUtils;
import com.adc.da.util.utils.StringUtils;
import com.adc.da.workflow.service.ActivitiProcessService;
import com.adc.da.workflow.vo.ActivitiProcessInstanceVO;
import com.adc.da.workflow.vo.ProcessInstanceCreateRequestVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
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
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

/**
 * @Auther: ZHAIKAIXUAN
 * @Date: 2018/11/19 13:09
 * @Description: /*
 */
@RestController
@RequestMapping("/${restPath}/budget/childrentask")
@Api("|ChildrenTask|")
public class ChildrenTaskController extends BaseController<Business> {
    private static final Logger logger = LoggerFactory.getLogger(ChildrenTaskController.class);

    @Autowired
    private ChildrenTaskService taskService;

    @Autowired
    private ActivitiProcessService activitiProcessService;

    @Autowired
    private FileEOService fileEOService;

    @Autowired
    private BeanMapper beanMapper;

    @Autowired
    private UserEOService userEOService;

    /**
     * @Description:增删改
     * @auther: ZHAIKAIXUAN
     * @params:
     * @return:
     * @date: 2018/11/19 13:10
     */

    @ApiOperation(value = "|ChildrenTask|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("fin:subtask:save")
    public ResponseMessage<ChildrenTask> insert(@RequestBody @Valid ChildrenTaskVO taskVO) throws Exception {
        ChildrenTask task = taskService.insert(taskVO);
        return Result.success(task);
    }

    @ApiOperation("|ChildrenTask|删除")
    @DeleteMapping("/{ids}")
    @RequiresPermissions("fin:subtask:delete")
    public ResponseMessage<String> delete(@PathVariable(value = "ids") String ids) throws NoSuchMethodException {
        return new ResponseMessage<>("200", taskService.deleteBatch(ids), true);
    }

    @Deprecated
    @ApiOperation("清空数据,删除已改为软删除，慎用此方法")
    @DeleteMapping("/delete/all")
    public ResponseMessage<String> deleteAll() {
        taskService.deleteAll();
        return Result.success();
    }

    @ApiOperation("|ChildrenTask|修改")
    @RequiresPermissions("fin:subtask:update")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage<ChildrenTask> update(@RequestBody @Valid ChildrenTaskVO taskVO) throws Exception{
        return Result.success(taskService.update(taskVO));
    }


    @ApiOperation("任务状态-不区分任务或子任务(0 正常；1 已隐藏；2 已完成；-1 不存在)")
    @GetMapping("/queryTaskStatusById")
    public ResponseMessage<Integer> queryTaskStatusById(@RequestParam("taskId") String taskId) throws Exception {
        return Result.success(taskService.queryTaskStatus(taskId));
    }

    @ApiOperation("|ChildrenTask|详情")
    @GetMapping("/query/{id}")
    @RequiresPermissions("fin:subtask:get")
    public ResponseMessage<ChildrenTask> query(@PathVariable("id") String id) throws Exception {
        return Result.success(taskService.querySingle(id));
    }

    @ApiOperation("|ChildrenTask|详情")
    @GetMapping("/queryWithResult/{id}")
    @RequiresPermissions("fin:subtask:get")
    public ResponseMessage<ChildrenTaskWithResult> queryWithResult(@PathVariable("id") String id) throws Exception {
        ChildrenTask childrenTask = taskService.querySingle(id);
        List<TaskResultVO> taskResultVOList = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(childrenTask.getTaskResultEOList())) {
            for (TaskResultEO eo : childrenTask.getTaskResultEOList()) {
                TaskResultVO taskResultVO = beanMapper.map(eo, TaskResultVO.class);
                List<com.adc.da.file.entity.FileEO> fileEOList = fileEOService.selectByForeignId(eo.getId());
                if (CollectionUtils.isNotEmpty(fileEOList)) {
                    FileEO fileEO = fileEOList.get(0);
                    UserEO userEO = userEOService.selectByPrimaryKey(fileEO.getUserId());
                    if (null != userEO) {
                        taskResultVO.setUploadUserName(userEO.getUsname());
                    }
                    taskResultVO.setFileId(fileEO.getFileId());
                    taskResultVO.setFileType(fileEO.getFileType());
                    taskResultVO.setFileName(fileEO.getFileName());
                    taskResultVO.setFileSize(fileEO.getFileSize());
                    taskResultVO.setUploadTime(fileEO.getCreateTime());
                    taskResultVO.setUploadUserId(fileEO.getUserId());
                }
                taskResultVOList.add(taskResultVO);
            }
        }
        ChildrenTaskWithResult childrenTaskWithResult = new ChildrenTaskWithResult();
        childrenTaskWithResult.setChildrenTask(childrenTask);
        childrenTaskWithResult.setTaskResultVOList(taskResultVOList);

        return Result.success(childrenTaskWithResult);
    }

    @ApiOperation("|ChildrenTask|列表")
    @GetMapping("/query/all")
    @RequiresPermissions("fin:subtask:list")
    public ResponseMessage<List<ChildrenTask>> queryAll() {
        return Result.success(taskService.queryAll());
    }

    @ApiOperation(value = "分页查询")
    @GetMapping("/page")
    public ResponseMessage<PageDTO> page(@RequestParam Integer page, @RequestParam Integer size,
        String taskID, @RequestParam int type) {
        return Result.success(taskService.queryByPage(page == null ? 1 : page, size == null ? 10 : size, taskID, type));

    }

    @ApiOperation(value = "|ChildrenTask|导出")
    @GetMapping("/export")
    @RequiresPermissions("fin:subtask:export")
    public ResponseMessage excelImport(HttpServletResponse response, String fileName, @RequestParam int type) {
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

    @ApiOperation(value = "|ChildrenTask|无验证的Excel单sheet导入")
    @PostMapping("/excelImport")
    @RequiresPermissions("fin:subtask:import")
    public ResponseMessage excelImport(@RequestParam("file") MultipartFile file) throws Exception {
        InputStream is = file.getInputStream();
        ImportParams params = new ImportParams();
        taskService.excelImport(is, params);
        return Result.success();
    }

    @ApiOperation(value = "修改子任务状态")
    @PutMapping("/status")
    public ResponseMessage<String> page(@RequestParam String id, @RequestParam Boolean status) {
        boolean res = taskService.setStatus(id, status);
        if (res) {
            return Result.success();
        } else {
            return Result.error();
        }
    }

    @ApiOperation(value = "修改子任务审批状态")
    @GetMapping("/changeChildTaskStatus1")
    public ResponseMessage<String> changeChildTaskStatus1(@RequestParam String childTaskId,
        @RequestParam String btnType) {
        return Result.success(taskService.changeChildTaskStatus(childTaskId, btnType));
    }

    @ApiOperation(value = "修改子任务审批状态")
    @PostMapping("/changeChildTaskStatus")
    public ResponseMessage<String> changeChildTaskStatus(@RequestBody @ApiParam("流程信息")
        ProcessInstanceCreateRequestVO request, HttpSession session) {
        logger.debug("request param:{processVO:{}}", request);
        request.setInitiator((String) session.getAttribute("LOGIN_USER_ID"));
        String childTaskId1 = request.getBusinessKey();
        try {
            String flag = taskService.changeChildTaskStatus(childTaskId1, ProjectStatusEnums.EXECUTE.getStatus());
            ActivitiProcessInstanceVO activitiProcessInstanceVO = this.activitiProcessService.startProcess(request);
            return Result.success();
        } catch (Exception var4) {
            taskService.changeChildTaskStatus(childTaskId1, "err");
            logger.error("流程启动失败", var4);
            return Result.error("-1", "流程启动失败，请联系系统管理员！");
        }
    }

    @ApiOperation(value = "刷新子任务")
    @GetMapping("/refreshChildTaskByChildTaskId")
    public ResponseMessage<String> refreshChildTaskByChildTaskId(@RequestParam String childTaskId) {

        return Result.success(taskService.refreshChildTask(childTaskId));
    }

    @ApiOperation(value = "刷新所有的子任务")
    @GetMapping("/refreshChildTaskAll")
    public ResponseMessage<String> refreshChildTaskAll() {
        return Result.success(taskService.refreshChildTask());
    }

}
