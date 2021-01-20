package com.adc.da.budget.service;

import com.adc.da.budget.constant.Import;
import com.adc.da.budget.dao.BudgetEODao;
import com.adc.da.budget.dao.UserEPDao;
import com.adc.da.budget.dto.TaskDTO;
import com.adc.da.budget.dto.TaskExcelData;
import com.adc.da.budget.entity.BudgetEO;
import com.adc.da.budget.entity.Project;
import com.adc.da.budget.entity.Task;
import com.adc.da.budget.entity.TaskResultEO;
import com.adc.da.budget.entity.UserWithProjects;
import com.adc.da.budget.enums.ProjectStatusEnums;
import com.adc.da.budget.repository.ProjectRepository;
import com.adc.da.budget.repository.TaskRepository;
import com.adc.da.budget.repository.UserWithProjectsRepository;
import com.adc.da.budget.utils.CommonUtil;
import com.adc.da.excel.poi.excel.ExcelImportUtil;
import com.adc.da.excel.poi.excel.entity.ImportParams;
import com.adc.da.sys.entity.OrgEO;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.sys.service.OrgEOService;
import com.adc.da.sys.service.UserEOService;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.StringUtils;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * describe:
 *
 * @author 李坤澔
 *     date 2019-05-27
 */
@Service
@Slf4j
public class TaskIOService extends BaseService<Project, String> {

    @Autowired
    private UserEPDao userEPDao;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserEOService userEOService;


    @Autowired
    private OrgEOService orgEOService;

    /**
     * 截止 date 2020-03-11 ， 一共 658 用户
     */
    private Map<String, String> userNameIdMap;

    @Autowired
    private BudgetEODao budgetEODao;

    /**
     * 全局 Budget Name EO map
     */
    private Map<String, BudgetEO> budgetNameEOMap;

    /**
     * 初始化
     *
     * @return
     */
    private Map<String, BudgetEO> initBudgetMap() {
        if (budgetEODao == null) {
            return new HashMap<>();
        }

        List<BudgetEO> allBudget = budgetEODao.findAll(null);
        Map<String, BudgetEO> map = new HashMap<>(allBudget.size());
        for (BudgetEO eo : allBudget) {
            map.put(eo.getProjectName(), eo);
        }
        return map;
    }

    /**
     * 全局 Project Name EO map
     */
    private Map<String, Project> projectNameEOMap;

    /**
     * 初始化
     *
     * @return
     */
    private Map<String, Project> initProjectMap() {
        if (budgetEODao == null) {
            return new HashMap<>();
        }

        List<Project> allProject = Lists.newArrayList(projectRepository.findAll());
        Map<String, Project> map = new HashMap<>(allProject.size());
        for (Project eo : allProject) {
            map.put(eo.getName(), eo);
        }
        return map;
    }

    /**
     * 清空相关的项目关系
     * 改方法将会调用 2  次数据库
     */
    public void clearAllStrTask(List<TaskDTO> list, String str) {
        if (StringUtils.isEmpty(str)) {
            return;
        }

        Set<String> idSet = new HashSet<>();
        for (TaskDTO data : list) {
            idSet.add(str + data.getExcelId());
        }

        List<UserWithProjects> userWithProjectsList = Lists.newArrayList(userWithProjectsRepository.findAll());

        for (UserWithProjects eo : userWithProjectsList) {
            eo.getTaskIds().removeAll(idSet);
        }


        /*
         * 批量存储
         */
        if (!userWithProjectsList.isEmpty()) {
            userWithProjectsRepository.save(userWithProjectsList);
        }

    }

    /**
     * 导入任务
     *
     * @param is
     * @param params
     * @return
     * @throws Exception
     */
    public Map<String, String> excelImport(InputStream is, ImportParams params, boolean saveDataFlag, String baseIdO)
        throws Exception {
        List<TaskDTO> datas = ExcelImportUtil.importExcel(is, TaskDTO.class, params);
        //导入逻辑：1、校验项目ID是否存在，2、展开参与人员，并查询参与人员是否存在，存在，修改ID，不存在，插入人员，并修改ID，3、导入数据
        if (datas.isEmpty()) {
            throw new AdcDaBaseException("Excel为空");
        }
        Map<String, String> resultMap = new HashMap<>();
        userNameIdMap = ProjectIOService.initUserNameIdMap(userEPDao);
        budgetNameEOMap = initBudgetMap();
        projectNameEOMap = initProjectMap();

        Date createTime = new Date();

        String baseId;
        if (StringUtils.isNotEmpty(baseIdO)) {
            baseId = baseIdO;
            if (saveDataFlag) {
                //清除相关的关联
                clearAllStrTask(datas, baseId);
            }
        } else {
            baseId = UUID.randomUUID() + "_";
        }

        resultMap.put(baseId, "本次导入基础的基础ID");
        for (TaskDTO data : datas) {
            /*
             * 初始化业务 项目 信息
             */
            int x = initBudgetAndProject(data);
            if (x > 0) {
                String reason;
                if (x == 1) {
                    reason = "  budgetName not found";
                } else if (x == 2) {
                    reason = " project not found";
                } else {
                    throw new AdcDaBaseException("case not found");
                }
                resultMap.put(data.getExcelId(), Import.FAILURE + reason);
                continue;
            }

            //负责人处理
            if (!approveUserInit(data)) {
                log.warn("approveUser not found {}", data.getApproveUserNameExcel());
                resultMap.put(data.getExcelId(), Import.FAILURE + " approveUser not found");
                continue;
            }
            /*
             * 成员组装
             */
            if (!memberCheck(data)) {
                resultMap.put(data.getExcelId(), Import.FAILURE_MEMBERS_REASON);
                continue;
            }

            //导入的任务由32位uuid+序号组成
            data.setId(baseId + data.getExcelId());
            //处理创建时间
            data.setCreateTime(createTime);
            //处理开始时间
            Date beginTime = data.getTimeBeginExcel();
            data.setPlanStartTime(beginTime);
            //处理结束时间为当日最后1毫秒
            Date endTime = data.getTimeEndExcel();
            endTime.setTime(endTime.getTime() + 86400000L - 1L);
            data.setPlanEndTime(endTime);

            //todo 交付物
            data.setTaskResultEOList(new ArrayList<TaskResultEO>());

            //状态 进行中
            data.setTaskStatus(ProjectStatusEnums.EXECUTE.getStatus());

            try {
                /*
                 * 进行插入ES操作
                 */
                if (saveDataFlag) {
                    saveUserProject(data);
                    taskRepository.save(data);
                }
                resultMap.put(data.getExcelId(), Import.SUCCESS);

            } catch (Exception e) {
                log.error("{}插入失败：" + data.getId(), e);
                resultMap.put(data.getExcelId(), Import.FAILURE);

            }
        }

        return resultMap;

    }


    /**
     * 导入任务
     *
     * @param is
     * @param params
     * @return
     * @throws Exception
     */
    public void excelImportDailyTask(InputStream is, ImportParams params)throws Exception {
        List<TaskExcelData> taskExcelDataList = ExcelImportUtil.importExcel(is, TaskExcelData.class, params);
        //导入逻辑：1、校验项目ID是否存在，2、展开参与人员，并查询参与人员是否存在，存在，修改ID，不存在，插入人员，并修改ID，3、导入数据
        if (taskExcelDataList.isEmpty()) {
            throw new AdcDaBaseException("Excel为空");
        }
        List<Task> taskList = new ArrayList<>();
        List<Project> projectList = projectRepository.findByProjectType(1);
        Date createTime = new Date();
        try {
            for (TaskExcelData taskExcelData : taskExcelDataList) {
                if (StringUtils.isEmpty(taskExcelData.getProjectNameExcel())) {
                    throw new AdcDaBaseException("Excel为空");
                }
                Task task = new Task();
                task.setName(taskExcelData.getName());
                task.setPriority(taskExcelData.getPriority());
                task.setTaskTarget(taskExcelData.getTaskTarget());
                for (Project project : projectList) { // baseId_cy 是当前日常项目的id头
                    if ((StringUtils.startsWith(project.getId(), "baseId_cy") || StringUtils.equals("其他业务-软件业务部",project.getName()))&& StringUtils.equals(taskExcelData.getProjectNameExcel(), project.getName())) {
                        task.setProjectId(project.getId());
                        task.setProjectName(project.getName());
                        task.setProjectName1(project.getName());
                        task.setProjectLeaderId(project.getProjectLeaderId());
                        task.setApproveUserId(project.getProjectLeaderId());
                        task.setApproveUserName(project.getProjectLeader());
                        task.setPm(project.getPm());
                    }
                }
                if (StringUtils.isEmpty(task.getPm())){
                    throw new AdcDaBaseException("当前excel数据错误！");
                }
                //负责人处理
                UserEO taskApproveUserEO = userEOService.getUserEOListByUserNameEquals(taskExcelData.getApproveUserNameExcel()).get(0);
                task.setApproveUserId(taskApproveUserEO.getUsid());
                task.setApproveUserName(taskApproveUserEO.getUsname());
                /*
                 * 成员组装
                 */
                List<OrgEO> memberOrgEOList = orgEOService.getOrgEOByOrgName(taskExcelData.getMemberNameExcel());
                if (CollectionUtils.isEmpty(memberOrgEOList)) {
                    throw new AdcDaBaseException("成员部门" + taskExcelData.getMemberNameExcel() + "不存在！");
                }
                HashMap<String, List<String>> deptUserIdMap = task.getDeptIdUserIdList();
                List<Map<String, String>> deptInfoListMapList = task.getDeptInfoListMap();// 所选部门ID和类型 如类型=1 只选当前自身 类型=2 包含子部门
                Set<String> tempSet = new HashSet<>();
                Map<String, String> deptInfoListMap = new HashMap<>();
                ////        "deptId":"",
                ////        "type":1,
                ////        "deptName":""
                deptInfoListMap.put("deptId", memberOrgEOList.get(0).getId());
                deptInfoListMap.put("type", "2");
                deptInfoListMap.put("deptName", memberOrgEOList.get(0).getName());
                deptInfoListMapList.add(deptInfoListMap);
                List<UserEO> userEOList = orgEOService.listUserEOByOrgId(memberOrgEOList.get(0).getId());
                for (UserEO userEO : userEOList) {
                    tempSet.add(userEO.getUsid());
                }
                deptUserIdMap.put(memberOrgEOList.get(0).getId(), new ArrayList<>(tempSet));
                userEOList.add(taskApproveUserEO);
                List<OrgEO> allOrgEOList = orgEOService.queryOrgAll();
                task.setMemberIds(CommonUtil.getUserIdArr(userEOList));
                task.setMemberNames(CommonUtil.getUserNameArr(userEOList));
                task.setMemberNameString(StringUtils.join(CommonUtil.getUserNameArr(userEOList), ','));
                task.setMapsList(CommonUtil.userIdUsnameMapKv(userEOList));
                task.setUserIdDeptNameMapList(CommonUtil.userIdDeptNamesMapKv(userEOList, allOrgEOList));

                //导入的任务由32位uuid+序号组成
                task.setId("baseId_cy" + com.adc.da.util.utils.UUID.randomUUID10());
                //处理创建时间
                task.setCreateTime(createTime);
                //处理开始时间
                Date beginTime = taskExcelData.getTimeBeginExcel();
                task.setPlanStartTime(beginTime);
                //处理结束时间为当日最后1毫秒
                Date endTime = taskExcelData.getTimeEndExcel();
                endTime.setTime(endTime.getTime() + 86400000L - 1L);
                task.setPlanEndTime(endTime);
                //状态 进行中
                task.setTaskStatus(ProjectStatusEnums.EXECUTE.getStatus());
                /*
                 * 进行插入ES操作
                 */
                saveUserProject(task);
                taskRepository.save(task);
                taskList.add(task);
            }
        } catch (Exception e) {
            taskRepository.delete(taskList);
            log.error("{}插入失败：" , e);

        }
    }

    /**
     * 初始化业务/项目信息
     */
    private int initBudgetAndProject(TaskDTO data) {
        //所属业务必填
        String budgetName = data.getBudgetNameExcel();
        BudgetEO budgetEO = budgetNameEOMap.get(budgetName);

        if (null == budgetEO) {
            log.warn("budgetName not found {}", budgetName);

            return 1;
        }
        data.setSearchBudgetId(budgetEO.getId());
        //业务经理
        data.setPm(budgetEO.getPm());
        data.setBusinessCreateUserId(budgetEO.getCreateUserId());
        //创建人归为业务创建人
        data.setCreateUserId(budgetEO.getCreateUserId());

        String deptId;
        int projectType;

        if (StringUtils.isEmpty(data.getProjectNameExcel())) {
            //业务任务
            data.setBudgetName(budgetName);
            data.setBudgetName1(budgetName);
            data.setBudgetId(budgetEO.getId());

            //设置部门 设置业务类型
            deptId = (budgetEO.getDeptId());
            projectType = (Integer.parseInt(budgetEO.getProperty()));
        } else {
            //项目任务
            String projectName = data.getProjectNameExcel();
            Project project = projectNameEOMap.get(projectName);
            if (null == project) {
                log.warn("project not found {}", projectName);

                return 2;
            }
            data.setProjectName(projectName);
            data.setProjectName1(projectName);
            data.setProjectId(project.getId());
            data.setProjectLeaderId(project.getProjectLeaderId());

            //设置部门 设置业务类型
            deptId = project.getDeptId();
            projectType = project.getProjectType();
        }
        //设置部门
        data.setDeptId(deptId);
        /*
         * 设置业务类型
         */
        data.setProjectType(projectType);
        return 0;
    }

    @Autowired
    private UserWithProjectsRepository userWithProjectsRepository;

    /**
     * 存储任务与人员的关系
     * 改方法将会调用 2* excel.size 次数据库
     *
     * @throws NoSuchMethodException
     */
    public void saveUserProject(Task task) throws NoSuchMethodException {

        Set<String> idList = new HashSet<>(Arrays.asList(task.getMemberIds()));
        //添加业务经理
        idList.add(task.getPm());
        boolean isProjectTask = StringUtils.isNotEmpty(task.getProjectId());
        //如果是项目任务，任务负责人也要回显 该任务
        if (isProjectTask) {
            idList.add(task.getProjectLeaderId());
        }

        List<UserWithProjects> userWithProjectsList = Lists.newArrayList(userWithProjectsRepository.findAll(idList));
        int len = userWithProjectsList.size();
        List<UserWithProjects> saveList = new ArrayList<>(len);

        //用于搜索的map
        Map<String, UserWithProjects> userIdMap = new HashMap<>(len);
        for (UserWithProjects userWithProjects : userWithProjectsList) {
            userIdMap.put(userWithProjects.getUserId(), userWithProjects);
        }

        for (String memberId : idList) {
            UserWithProjects userWithProjects;

            if (userIdMap.containsKey(memberId)) {
                userWithProjects = userIdMap.get(memberId);
            } else {
                //新用户
                userWithProjects = new UserWithProjects();
                userWithProjects.setUserId(memberId);
            }

            //关联任务id
            userWithProjects.getTaskIds().add(task.getId());

            //需要把业务ID关联上
            userWithProjects.getBusinessIds().add(task.getSearchBudgetId());
            //项目任务关联任务id
            if (isProjectTask) {
                userWithProjects.getProjectIds().add(task.getProjectId());
            }
            //将该记录放入存储
            saveList.add(userWithProjects);

        }
        /*
         * 批量存储
         */
        if (!saveList.isEmpty()) {
            userWithProjectsRepository.save(saveList);
        }

    }

    /**
     * 负责人初始化
     *
     * @param taskDTO
     * @return
     */
    private boolean approveUserInit(TaskDTO taskDTO) {
        String approveUserName = taskDTO.getApproveUserNameExcel().trim();
        try {
            if (StringUtils.isNotEmpty(approveUserName)) {

                String userId = userNameIdMap.get(approveUserName);
                if (userId != null) {
                    /* 只存储负责人id*/
                    taskDTO.setApproveUserId(userId);

                    return true;
                }
            }
        } catch (Exception e) {
            log.error("projectLeaderInit error", e);

        }
        return false;

    }

    /**
     * 组织成员校验
     *
     * @param taskDTO
     * @return
     */
    private boolean memberCheck(TaskDTO taskDTO) {
        String[] projectMembers = ProjectIOService.splitExcelStr(taskDTO.getMemberNameExcel());
        int size = projectMembers.length;
        List<String> memberNames = new ArrayList<>(size);
        List<String> memberIds = new ArrayList<>(size);
        List<Map<String, String>> mapList = new ArrayList<>(size);

        try {

            for (String userName : projectMembers) {
                String usid = userNameIdMap.get(userName);

                if (null == usid) {
                    //找不到一般是离职的，日志记录跳过继续进行
                    log.warn("not found user {} ， excelId is {}", userName, taskDTO.getExcelId());
                    continue;
                }

                String realName = ProjectIOService.getRealName(userName);
                memberNames.add(realName);
                memberIds.add(usid);


                /* 添加mapList */
                Map<String, String> map = new HashMap<>(4);
                map.put("id", usid);
                map.put("name", realName);
                mapList.add(map);
            }
            //负责人不在组内，将负责人加入
            if (!memberIds.contains(taskDTO.getApproveUserId())) {
                String usid = taskDTO.getApproveUserId();
                String name = taskDTO.getApproveUserNameExcel().split("=")[0];
                memberNames.add(name);
                memberIds.add(usid);
                Map<String, String> map = new HashMap<>(4);
                map.put("id", usid);
                map.put("name", name);
                mapList.add(map);
            }

            taskDTO.setMemberNames(memberNames.toArray(new String[0]));
            taskDTO.setMemberIds(memberIds.toArray(new String[0]));
            taskDTO.setMapsList(mapList);
            return true;

        } catch (Exception e) {
            log.error("memberCheck error ", e);
        }
        return false;

    }

}
