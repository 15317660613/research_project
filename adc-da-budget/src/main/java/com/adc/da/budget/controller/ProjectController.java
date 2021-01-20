package com.adc.da.budget.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.budget.dao.UserEPDao;
import com.adc.da.budget.dto.CheckDeptDTO;
import com.adc.da.budget.dto.PageDTO;
import com.adc.da.budget.dto.ProjectMemberIdsDTO;
import com.adc.da.budget.entity.*;
import com.adc.da.budget.enums.ProjectStatusEnums;
import com.adc.da.budget.page.FileEOPage;
import com.adc.da.budget.repository.ProjectRepository;
import com.adc.da.budget.schedule.BudgetScheduleService;
import com.adc.da.budget.schedule.ProjectScheduleServiceForProjectMembers;
import com.adc.da.budget.service.*;
import com.adc.da.budget.vo.BudgetVO;
import com.adc.da.budget.vo.ProjectMinVO;
import com.adc.da.budget.vo.ProjectVO;
import com.adc.da.excel.poi.excel.entity.ImportParams;
import com.adc.da.login.util.UserUtils;
import com.adc.da.superadmin.service.SuperAdminService;
import com.adc.da.sys.entity.OrgEO;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.http.PageInfo;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.StringUtils;
import com.adc.da.workflow.service.ActivitiProcessService;
import com.adc.da.workflow.vo.ActivitiProcessInstanceVO;
import com.adc.da.workflow.vo.ProcessInstanceCreateRequestVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.adc.da.budget.constant.ProjectType.BUSINESS_PROJECT;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

/**
 * @Auther: ZHAIKAIXUAN
 * @Date: 2018/11/19 13:09
 * @Description: /*
 */
@RestController
@RequestMapping("/${restPath}/budget/project")
@Api(tags = "|Project|")
public class ProjectController extends BaseController<FileEO> {
    private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);

    @Autowired
    private ProjectService projectService;

    @Autowired
    private FileEPService fileEPService;

    @Autowired
    private ActivitiProcessService activitiProcessService;

    @Autowired
    private ProjectIOService projectIOService;

    @Autowired
    private BudgetEOService budgetEOService;

    @Autowired
    private MyProjectContractInvoiceEOService myProjectContractInvoiceEOService;

    @Autowired
    private SuperAdminService superAdminService;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectScheduleServiceForProjectMembers projectScheduleServiceForProjectMembers;

    @Autowired
    private BudgetScheduleService budgetScheduleService;

    @Value("${file.path}")
    private String basePath;

    @ApiOperation("|Project|删除可批量删除")
    @DeleteMapping("/{ids}")
    //@RequiresPermissions("fin:project:delete")
    public ResponseMessage<String> delete(@PathVariable(value = "ids") String ids) throws Exception {
        return Result.success(projectService.deleteBatch(ids));
    }

    @Deprecated
    @ApiOperation("|Project|删除可批量删除,谨慎使用")
    @DeleteMapping("/deleteByIds")
    //@RequiresPermissions("fin:project:delete")
    public ResponseMessage<String> deleteByIds(@RequestBody String[] ids) throws Exception {
        projectRepository.deleteByIdIn(ids);
        return Result.success();
    }

    @Deprecated
    @ApiOperation("|Project|清空数据.删除已改为软删除，慎用此方法")
    @DeleteMapping("/delete/all")
    public ResponseMessage<String> deleteAll() {
        projectService.deleteAll();
        return Result.success();
    }
    @ApiOperation("|Project|刷新业务类型")
    @GetMapping("/refreshProjectInBusiness")
    public ResponseMessage<String> refreshProjectInBusiness() {
        projectService.refreshProjectInBusiness();
        return Result.success();
    }

    @ApiOperation("|Project|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    //@RequiresPermissions("fin:project:update")
    public ResponseMessage<Project> update(@RequestBody @Valid
                                                   ProjectVO projectVO) throws Exception {
        return Result.success(projectService.update(projectVO));
    }

    @ApiOperation("|Project|申领重置")
    @PutMapping(value = "/resetClaim",consumes = APPLICATION_JSON_UTF8_VALUE)
    //@RequiresPermissions("fin:project:update")
    public ResponseMessage<Project> resetClaim(@RequestBody
                                                       ProjectVO projectVO){

        return Result.success(projectService.resetClaim(projectVO));
    }

    @ApiOperation("|Project|查找")
    @GetMapping("/query/{id}")
    // @RequiresPermissions("fin:project:get")
    @RequiresAuthentication
    public ResponseMessage<Project> query(@PathVariable("id") @NotNull String id) throws Exception {
        List<ProjectContractInvoiceEO> projectContractInvoiceEOList;
        Project project = projectService.querySingle(id);
        if (StringUtils.isEmpty(project.getContractAmountStr())) {

            project.setContractAmountStr(String.valueOf(project.getContractAmount()));
        }
        BudgetVO budgetVO = budgetEOService.findOne(project.getBudgetId());
        if (BUSINESS_PROJECT.toString().equals(budgetVO.getProperty())) {
            projectContractInvoiceEOList = myProjectContractInvoiceEOService.queryByProjectId(project.getId());
            if (CollectionUtils.isEmpty(projectContractInvoiceEOList)) {
                ProjectContractInvoiceEO projectContractInvoiceEO = new ProjectContractInvoiceEO();
                projectContractInvoiceEO.setProjectId(project.getId());
                projectContractInvoiceEOList.add(projectContractInvoiceEO);
            }
            project.setProjectContractInvoiceEOList(projectContractInvoiceEOList);
        }
        return Result.success(project);
    }

    @ApiOperation("|Project|查找")
    @GetMapping("/newquery/{id}")
    // @RequiresPermissions("fin:project:get")
    @RequiresAuthentication
    public ResponseMessage<Project> newquery(@PathVariable("id") @NotNull String id) throws Exception {
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        Project project = projectService.querySingle(id);
        if (!userId.equals(project.getProjectLeaderId()) && !superAdminService.isSuperAdmin()) {

            return Result.error("您不是" + project.getName() + " 业务/项目的负责人或管理员，无权创建任务 ！");
        }
        return Result.success(project);
    }


    @ApiOperation("|Project|检查是否可以变更成员")
    @PostMapping("/checkChangeMember")
    // @RequiresPermissions("fin:project:get")
    @RequiresAuthentication
    public ResponseMessage checkChangeMember(@RequestBody ProjectVO projectVO) {
        return Result.success(projectService.checkChangeMember(projectVO.getId(),projectVO.getProjectMemberIds()));
    }


    @ApiOperation("|Project|检查业务下项目重名问题")
    @GetMapping("/checkProjectNameRepeat/{budgetId}/{projectName}")
    // @RequiresPermissions("fin:project:get")
    @RequiresAuthentication
    public ResponseMessage<Project> checkRepeat(@PathVariable("budgetId") @NotNull String budgetId,
                                                @PathVariable("projectName") @NotNull String projectName) {
        List<Project> projectList = projectRepository.findByBudgetIdAndDelFlagNot(budgetId, true);
        for (Project project : projectList) {
            if (StringUtils.equals(projectName, project.getName())) {
                return Result.error("当前选择的业务下有同名称项目，修改项目名称！");
            }
        }
        return Result.success();
    }
    @ApiOperation("根据用户重构用户业务树的关联关系")
    @GetMapping("/rebuildUserWithProjectByUserId")
    public ResponseMessage rebuildUserWithProjectByUserId(String userId){
        projectService.rebuildUserWithProjectByUserId(userId);
        return Result.success();
    }

    /**
     * 新增项目任务权限判断
     *
     * @param id
     * @param type
     * @return
     * @throws Exception
     */
    @ApiOperation("|Project|判断权限")
    @GetMapping("/hasPermissions/{id}/{type}")
    // @RequiresPermissions("fin:project:get")
    @RequiresAuthentication
    public ResponseMessage<Project> hasPermissions(@PathVariable("id") @NotNull String id,
                                                   @PathVariable("type") @NotNull String type) throws Exception {
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        Project project = projectService.querySingle(id);
        BudgetEO budgetEO = budgetEOService.selectByPrimaryKey(project.getBudgetId());
        if (StringUtils.equals(type, "task")) {
            if (!userId.equals(project.getProjectLeaderId())
                    && !superAdminService.isSuperAdmin()
                    && !userId.equals(project.getProjectAdminId())
                    && !userId.equals(budgetEO.getPm())
                    && !userId.equals(budgetEO.getBusinessAdminId())
            ) {
                return Result.error("您不是" + project.getName() + " 业务/项目的负责人或管理员，无权创建任务 ！");
            }
        } else if (StringUtils.equals(type, "childTask")) {
            List<String> memberIds = Arrays.asList(project.getProjectMemberIds());
            if (!memberIds.contains(userId) && !superAdminService.isSuperAdmin()
                    && !userId.equals(project.getProjectAdminId())
                    && !userId.equals(project.getProjectLeaderId())
                    && !userId.equals(budgetEO.getPm())
                    && !userId.equals(budgetEO.getBusinessAdminId())
            ) {
                return Result.error("您不是" + project.getName() + " 项目的成员，无权创建子任务 ！");
            }
        }
        return Result.success(project);
    }

    /**
     * 编辑任务权限判断
     *
     * @param id
     * @param type
     * @return
     * @throws Exception
     */
    @ApiOperation("|Project|判断权限")
    @GetMapping("/hasPermissionsV2/{id}/{type}")
    // @RequiresPermissions("fin:project:get")
    @RequiresAuthentication
    public ResponseMessage<Project> hasPermissionsV2(@PathVariable("id") @NotNull String id,
                                                     @PathVariable("type") @NotNull String type) throws Exception {
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        Project project = projectService.querySingle(id);
        return Result.success(project);
    }

    @ApiOperation("|Project|查找所有信息")
    @GetMapping("/query/all")
    // @RequiresPermissions("fin:project:list")
    public ResponseMessage<List<Project>> queryAll() {
        return Result.success(projectService.queryAll());
    }

    @ApiOperation("|Project|查找所有经营类信息")
    @GetMapping("/query/allNotOld")
    // @RequiresPermissions("fin:project:list")
    public ResponseMessage<List<Project>> queryAllNotOld(String[] property) {
        return Result.success(projectService.queryAllNotOld(property));
    }

    @ApiOperation("|Project|查找所有经营类信息")
    @GetMapping("/query/allNotOldNew")
    // @RequiresPermissions("fin:project:list")
    public ResponseMessage<List<ProjectMinVO>> queryAllNotOldNew(String[] property) {
        return Result.success(projectService.queryAllNotOldNew(property));
    }

    @ApiOperation(value = "|Project|分页查询")
    @GetMapping("/page")
    @RequiresPermissions("fin:project:list")
    public ResponseMessage<PageDTO> page(@RequestParam Integer page, @RequestParam Integer size, String id) {
        PageDTO pageDTO = projectService.queryByPage(page == null ? 1 : page, size == null ? 10 : size, id);
        projectService.setDataList(pageDTO.getDataList());
        return Result.success(pageDTO);
    }

    @ApiOperation(value = "|Project|无验证的Excel单sheet导入")
    @PostMapping("/excelImport")
    //@RequiresPermissions("fin:project:import")
    public ResponseMessage excelImport(@RequestParam("file") MultipartFile file, Boolean saveDataFlag, String baseId)
            throws Exception {
        InputStream is = file.getInputStream();
        ImportParams params = new ImportParams();
        boolean saveData = true;
        if (saveDataFlag == null || !saveDataFlag) {
            saveData = false;
        }
        Map<String, String> message = projectIOService.excelImport(is, params, saveData, baseId);

        return Result.success(message);

    }

    @ApiOperation(value = "日常项目导入|Project|无验证的Excel单sheet导入")
    @PostMapping("/excelImportDailyProject")
    //@RequiresPermissions("fin:project:import")
    public ResponseMessage excelImportDailyProject(@RequestParam("file") MultipartFile file) throws Exception {
        InputStream is = file.getInputStream();
        ImportParams params = new ImportParams();
        projectIOService.excelImportDaily(is, params);
        return Result.success();

    }

    @ApiOperation(value = "日常项目刷新去重|Project|updateDailyProject")
    @PostMapping("/updateDailyProject")
    //@RequiresPermissions("fin:project:import")
    public ResponseMessage updateDailyProject() throws Exception {
        projectIOService.updateDailyProject();
        return Result.success();

    }



    @ApiOperation(value = "日常项目 刷新")
    @PostMapping("/refreshDailyProject")
    //@RequiresPermissions("fin:project:import")
    public ResponseMessage refreshDailyProject(String projectId,String deptId) throws Exception {
        projectIOService.refreshDailyProject(projectId,deptId);
        return Result.success();

    }



    // 测试用户接口

    @Autowired
    private UserEPDao userEPDao;

    @ApiOperation(value = "|Project|用户接口的核对")
    @GetMapping(value = "/user")
    public List<UserEPEntity> queryUser(String nameArrayString) {
        String[] split = nameArrayString.split(",");
        return userEPDao.checkUserExist(split);
    }

    @ApiOperation("根据项目查询任务")
    @GetMapping("/query/task/page")
    public ResponseMessage<PageDTO> queryTaskList(@RequestParam String projectId,
                                                  @RequestParam Integer page,
                                                  @RequestParam Integer size) {
        return Result.success(projectService.queryTaskByProjectId(projectId, page, size));
    }

    //TODO 迁移
    @ApiOperation("|Project|添加成员")
    @PostMapping("/{id}/member")
    //@RequiresPermissions("fin:project:update")
    public ResponseMessage<Project> addMember(@PathVariable("id") String id,
                                              @RequestBody @Valid ProjectMemberIdsDTO projectMemberIdsDTO) throws Exception {
        ProjectVO projectVO = new ProjectVO();
        projectVO.setId(id);
        projectVO.setProjectMemberIds(projectMemberIdsDTO.getProjectMemberIds());
        return Result.success(projectService.update(projectVO));
    }

    @ApiOperation("|Project|根据所属业务查询项目")
    @GetMapping("/query/{id}/budget")
    //@RequiresPermissions("fin:project:list")
    public ResponseMessage<List<Project>> queryByBudgetId(@PathVariable("id") String id) {
        return Result.success(projectService.queryByBudgetId(id));
    }

    @ApiOperation("|Task|检测项目成员是否同一部门")
    @PostMapping("/checkDept")
    public ResponseMessage currentUserProject(@RequestBody @Valid CheckDeptDTO checkDeptDTO) {
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }

        return Result.success(projectService.checkDept(checkDeptDTO.getPersonIds(), userId));
    }

    @ApiOperation("|Project|查询数据中心组织结构")
    @GetMapping("/query/org/adc")
    //@RequiresPermissions("fin:project:list")
    public ResponseMessage<List<OrgEO>> queryOrgByADC() {
        return Result.success(projectService.queryOrgByADC());
    }

    @ApiOperation("|Project|查询个人的中心组织结构")
    @GetMapping("/query/org/adc/own")
//    @RequiresPermissions("fin:project:list")
    public ResponseMessage<List<OrgEO>> queryOrgByADCown() {
//        return Result.success(projectService.queryOrgByADCown1());
        return Result.success(projectService.newQueryOrgByADCown1());
    }

    @ApiOperation("|Project|查询个人的中心组织结构")
    @GetMapping("/query/org/adc/newOwn")
//    @RequiresPermissions("fin:project:list")
    public ResponseMessage<List<OrgEO>> newQueryOrgByADCown() {
        return Result.success(projectService.newQueryOrgByADCown1());
    }

    @ApiOperation(value = "我的项目")
    @GetMapping("/myProject")
    @RequiresPermissions("fin:project:list")
    public ResponseMessage<PageDTO> getMyProject(@RequestParam Integer page, @RequestParam Integer size) {
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        PageDTO pageDTO = projectService.getMyProject(page == null ? 1 : page, size == null ? 10 : size, userId);
        return Result.success(pageDTO);
    }

    @ApiOperation(value = "项目人员信息")
    @GetMapping("/{projectId}/members")
    public ResponseMessage<List<UserEO>> getMyProject(@PathVariable("projectId") String projectId) throws Exception {
        return Result.success(projectService.getMemberInfo(projectId));
    }

    @ApiOperation(value = "获取项目工时")
    @GetMapping("/{projectId}/getWorkTime")
    public ResponseMessage<BigDecimal> getWorkTime(@PathVariable("projectId") String id) {
        return Result.success(projectService.getWorkTime(id));
    }

    @ApiOperation("|FileEO|交付物列表")
    @PostMapping({"/getDeliveryFile"})
    public ResponseMessage<PageInfo<FileEO>> getDeliveryFile(@RequestBody FileEOPage page) throws Exception {
        if (!this.basePath.endsWith("\\") && !this.basePath.endsWith("/")) {
            this.basePath = this.basePath + File.separator;
        }
        List<FileEO> rows = fileEPService.queryByPage(page);
        List<String> userIdList = new ArrayList<>();
        Map userNameMap = null;
        for (FileEO fileEO : rows) {
            userIdList.add(fileEO.getUserId());
        }
        if (CollectionUtils.isNotEmpty(userIdList)) {
            userNameMap = userEPDao.queryUserIdAndNameByIds(userIdList);
        }

        for (FileEO fileEO : rows) {
            if (null != userNameMap) {
                Map tempUserMap = (Map) userNameMap.get(fileEO.getUserId());
                if (null == tempUserMap) {
                    continue;
                }
                String userName = tempUserMap.get("USNAME").toString();
                fileEO.setUploadUser(userName);
            }

            String fullPath = this.basePath + fileEO.getSavePath();

            File file = new File(fullPath);

            long fileLength = file.length();
            long length = 0;
            if (fileLength >= 1000000) {
                length = fileLength >> 20;
                fileEO.setFileSize(String.valueOf(length) + "MB");
            } else if (fileLength < 1000000 && fileLength >= 1000) {
                length = fileLength >> 10;
                fileEO.setFileSize(String.valueOf(length) + "KB");
            } else {
                fileEO.setFileSize(String.valueOf(fileLength) + "B");
            }
        }

        return Result.success(getPageInfo(page.getPager(), rows));
    }

    @DeleteMapping("/file/{fileId}")
    public ResponseMessage deleteByFileId(@PathVariable String fileId) throws Exception {
        fileEPService.deleteByPrimaryKey(fileId);
        return Result.success();
    }

    @ApiOperation("|Project|改变项目完成状态")
    @PostMapping({"/changeFinshStatus"})
    public ResponseMessage<String> changeFinshStatus(
            @RequestBody @ApiParam("流程信息") ProcessInstanceCreateRequestVO request, HttpSession session) {
        String projectId = request.getBusinessKey();
        logger.debug("request param:{processVO:{}}", request);
        request.setInitiator((String) session.getAttribute("LOGIN_USER_ID"));
        try {
            //进行中变审批中
            String flag = projectService.changeFinshStatus(
                    projectId,
                    ProjectStatusEnums.EXECUTE.getStatus(),
                    ProjectStatusEnums.AUDIT.getStatus());
            ActivitiProcessInstanceVO activitiProcessInstanceVO = this.activitiProcessService.startProcess(request);
            return Result.success();
        } catch (Exception var4) {
            //审批中变进行中
            projectService.changeFinshStatus(
                    projectId,
                    ProjectStatusEnums.AUDIT.getStatus(),
                    ProjectStatusEnums.EXECUTE.getStatus());
            logger.error("流程启动失败", var4);
            return Result.error("-1", "流程启动失败，请联系系统管理员！");
        }
    }

    @Deprecated
    @ApiOperation(value = "更新project，关于项目类型的")
    @GetMapping("/updateProject")
    public void updateProject(String property) {
        List<String> budgetIds = budgetEOService.queryAllBudgetByType(property);
        List<Project> rows = projectRepository.findByBudgetIdIn(budgetIds);
        for (Project project : rows) {
            project.setProjectType(Integer.valueOf(property));
            if (project.getBusinessId().equals("NFA4VVKFHZ")) {
                continue;
            }
        }
        projectRepository.save(rows);
    }

    @Deprecated
    @ApiOperation(value = "更新project，关于项目参与人员")
    @GetMapping("/updateProject4MemberNames")
    public void updateProject4MemberNames(Integer projectType) {
        projectService.updateProjectMembers(projectType);
    }

    @ApiOperation("Check项目名重复")
    @GetMapping("/checkName")
    public ResponseMessage checkProjectName(@RequestParam(required = false) String id,
                                            @RequestParam String projectName,
                                            @RequestParam String budgetId,
                                            @RequestParam String projectLeaderId) {
        List<Project> projects = projectService
                .findByBudgetIdAndProjectLeaderIdAndDelFlagNot(budgetId, projectLeaderId);
        for (Project project : projects) {
            if (StringUtils.isEmpty(id) && projectName.equals(project.getName())) {
                return Result.error("新增项目时，所属业务及项目负责人相同情况下，项目名称不允许相同");
            } else if (StringUtils.isNotEmpty(id) && projectName.equals(project.getName()) && !id
                    .equals(project.getId())) {
                return Result.error("修改项目时，所属业务及项目负责人相同情况下，项目名称不允许相同");
            }
        }
        return Result.success("没发现重复");
    }


    @ApiOperation(value = "合作企业总数")
    @GetMapping("/projectOwnerStatistics")
    public ResponseMessage projectOwnerStatistics(@RequestParam(value = "projectYear",required = false) Integer projectYear) {
        return  Result.success(projectService.projectOwnerStatistics(projectYear));
    }


    @ApiOperation(value = "企业年度合同总额TOP10")
    @GetMapping("/getProjectContractAmount")
    public ResponseMessage getProjectContractAmount() {
        return  Result.success(projectService.getProjectContractAmount());
    }
    @ApiOperation(value = "企业年度合同总额TOP10-Pad端")
    @GetMapping("/getPadProjectContractAmount")
    public ResponseMessage getPadProjectContractAmount() {
        return  Result.success(projectService.getPadProjectContractAmount());
    }
    @ApiOperation(value = "完成项目及相关的任务子任务-不通过工作流")
    @PutMapping("/closeProjectAndTaskWithoutWorkFlow")
    public ResponseMessage closeProjectAndTaskWithoutWorkFlow(String projectId){
        projectIOService.closeProjectAndTaskWithoutWorkFlow(projectId);
        return Result.success();
    }

    @ApiOperation(value = "完成项目及相关的任务子任务-不通过工作流")
    @PutMapping("/closeProjectAndTaskWithoutWorkFlowProjectType")
    public ResponseMessage closeProjectAndTaskWithoutWorkFlowByProjectType(int projectType,String name){
        projectIOService.closeProjectAndTaskWithoutWorkFlowByProjectType(projectType,name);
        return Result.success();
    }


    @ApiOperation(value = "回退-完成项目及相关的任务子任务-不通过工作流的操作")
    @PutMapping("/undoCloseProjectAndTaskWithoutWorkFlow")
    public ResponseMessage undoCloseProjectAndTaskWithoutWorkFlow(String projectId){
        projectIOService.undoCloseProjectAndTaskWithoutWorkFlow(projectId);
        return Result.success();
    }

    @ApiOperation(value = "更新ES中project、Task、childTask这三个type中项目组成员")
    @GetMapping("/updateProjectMembers")
    public ResponseMessage updateProjectMembers(){
        //projectScheduleServiceForProjectMembers.projectMemberUpdatingFunction();

        try{
            budgetScheduleService.doTask();
        }catch (Exception ex){

        }
        return Result.success();
    }


    @ApiOperation(value = "重构userWithProjects,执行时间稍长，请等待")
    @GetMapping("/userWithProjectsRebuilding")
    public ResponseMessage userWithProjectsRebuilding(){
        String responseMessage=projectService.userWithProjectsRebuilding();
        return Result.success(responseMessage);
    }



    @ApiOperation(value = "处理project内部数据不一致问题,执行时间稍长，请等待")
    @GetMapping("/refreshProject")
    public ResponseMessage refreshProject(){
        String responseMessage=projectService.updateProjectForConsistency();
        return Result.success(responseMessage);
    }


}
