package com.adc.da.listener.service;

import com.adc.da.budget.entity.ChildrenTask;
import com.adc.da.budget.entity.Project;
import com.adc.da.budget.entity.Task;
import com.adc.da.budget.entity.UserWithProjects;
import com.adc.da.budget.enums.ProjectStatusEnums;
import com.adc.da.budget.repository.ChildTaskRepository;
import com.adc.da.budget.repository.ProjectRepository;
import com.adc.da.budget.repository.TaskRepository;
import com.adc.da.budget.repository.UserWithProjectsRepository;
import com.adc.da.budget.service.ProjectService;
import com.adc.da.budget.utils.ArrayUtils;
import com.adc.da.budget.utils.CommonUtil;
import com.adc.da.budget.vo.ProjectVO;
import com.adc.da.form.dao.AdcFormDao;
import com.adc.da.form.entity.AdcFormEO;
import com.adc.da.listener.entity.MilestoneChangeEO;
import com.adc.da.listener.utils.FormEOInit;
import com.adc.da.listener.utils.FormKeyWord;
import com.adc.da.progress.dao.ProjectBudgetChangeEODao;
import com.adc.da.progress.dao.ProjectMilepostEODao;
import com.adc.da.progress.dao.ProjectMilepostResultEODao;
import com.adc.da.progress.dao.ProjectNameEODao;
import com.adc.da.progress.entity.ProjectBudgetChangeEO;
import com.adc.da.progress.entity.ProjectMilepostEO;
import com.adc.da.progress.entity.ProjectMilepostResultEO;
import com.adc.da.progress.entity.ProjectNameEO;
import com.adc.da.progress.page.ProjectNameEOPage;
import com.adc.da.research.service.ProjectSaveService;
import com.adc.da.sys.dao.OrgEODao;
import com.adc.da.sys.dao.UserEODao;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.DateUtils;
import com.adc.da.util.utils.StringUtils;
import com.adc.da.util.utils.UUID;
import com.adc.da.workflow.utils.contants.Constants;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.activiti.engine.FormService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.form.StartFormData;
import org.apache.commons.collections.ListUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.*;

import static com.adc.da.listener.utils.FormKeyWord.ID;
import static com.adc.da.listener.utils.FormKeyWord.UNDERLINE;
import static com.adc.da.listener.utils.FormKeyWord.US_ID;
import static com.adc.da.listener.utils.FormKeyWord.US_NAME;
import static com.adc.da.listener.utils.FormKeyWord.YES;
import static com.adc.da.listener.utils.FormType.BEGIN_TIME;
import static com.adc.da.listener.utils.FormType.BUDGET_ID;
import static com.adc.da.listener.utils.FormType.END_TIME;
import static com.adc.da.listener.utils.FormType.INT_ID;
import static com.adc.da.listener.utils.FormType.INT_NAME;
import static com.adc.da.listener.utils.FormType.INT_NEW;
import static com.adc.da.listener.utils.FormType.INT_OLD;
import static com.adc.da.listener.utils.FormType.PROJECT_ID;
import static com.adc.da.listener.utils.FormType.PROJECT_NAME;

/**
 * describe:
 * 里程碑 以及 交付物 解析 serviceTask
 * ${projectChangeService}
 *
 * @author 李坤澔
 *     date 2019-08-01
 */
@Component("projectChangeService")
@Slf4j
public class ProjectChangeService implements JavaDelegate {

    /**
     * 里程碑
     */
    @Autowired
    private ProjectMilepostEODao milepostEODao;

    /**
     * 交付物
     */
    @Autowired
    private ProjectMilepostResultEODao milepostResultEODao;

    @Autowired
    private ProjectBudgetChangeEODao projectBudgetChangeEODao;

    private List<Task> taskList;

    private List<ChildrenTask> childrenTaskList;

    private List<UserWithProjects> userWithProjectsList;

    @Autowired
    private AdcFormDao adcFormDao;

    @Autowired
    private FormService formService;

    @Autowired
    private UserEODao userEODao;

    @Autowired
    private OrgEODao orgEODao;

    /**
     * 表单内容数据
     */
    private Map<String, String> formMap;

    private Map<String, Object> jsonMap;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ChildTaskRepository childTaskRepository;

    @Autowired
    private UserWithProjectsRepository userWithProjectsRepository;

    @Autowired
    private ProjectNameEODao projectNameEODao;

    @Autowired
    private ProjectService projectService;

    @Override
    public void execute(DelegateExecution execution) throws Exception{

        StartFormData startFormData = formService.getStartFormData(execution.getProcessDefinitionId());
        String processDefinitionKey = execution.getProcessDefinitionId().split(":")[0];
        String formKey;

        if (startFormData != null) {
            formKey = startFormData.getFormKey();
        } else {
            throw new AdcDaBaseException("form no Found");
        }
        AdcFormEO adcFormEO = adcFormDao.selectByPrimaryKey(formKey);
        formMap = FormEOInit.initFormMap(adcFormEO);

        /*
         * 获取表单数据
         */
        String jsonData = (String) execution.getVariable(Constants.GLOBAL_FORM_DATA);

        Gson gson = new Gson();
        Type type = new TypeToken<LinkedTreeMap<String, Object>>() {
        }.getType();
        jsonMap = gson.fromJson(jsonData, type);
        /*
         * 项目基本信息，包括项目id以项目名称
         */
        String[] projectInfoLoc = FormEOInit.baseFormDataAnalysis(jsonMap);

        this.projectInfo = new String[3];
        projectInfo[PROJECT_ID] = projectInfoLoc[PROJECT_ID];
        projectInfo[PROJECT_NAME] = projectInfoLoc[PROJECT_NAME];

        Project dbProject = projectRepository.findById(projectInfo[PROJECT_ID]);
        ProjectVO projectVO = new ProjectVO();
        BeanUtils.copyProperties(dbProject,projectVO);


        /*
         * 添加业务id
         */
        projectInfo[BUDGET_ID] = projectVO.getBudgetId();

        projectVO.setModifyTime(new Date());

        if (changeCheck("项目组负责人变更")) {

            setLeaderChange(projectVO);
            /*
             * 存储ES相关属性，包括项目，任务，子任务，以及左侧项目树
             */
//            saveESData(project); 不此处存储，所有修改完毕，统一调用projectService 的 update
            /*
             * 负责人变更不涉及后续变更
             */
//            return;
        }
        /*  部门变更      */
        if (changeCheck("项目承担部门变更")) {
            String deptId = (String) jsonMap.get(formMap.get("新承担部门") + UNDERLINE + ID);
            projectVO.setDeptId(deptId);
        }

        /*基本情况*/
        if (changeCheck("项目基本情况变更")) {
            String description = (String) jsonMap.get(formMap.get("新项目基本情况"));
            projectVO.setProjectDescription(description);
        }

        /*起止时间*/
        if (changeCheck("项目起止时间变更")) {
            Date[] date = setProjectDate();
            projectVO.setProjectBeginTime(date[BEGIN_TIME]);
            projectVO.setProjectEndTime(date[END_TIME]);
        }

        /*  组员变更  */
        if (changeCheck("项目组成员变更")) {
            setProjectMember(projectVO);
        }
        /*
         * 存储ES相关属性，包括项目，任务，子任务，以及左侧项目树
         */
//        saveESData(project);

        /*         * 预算成本         */
        if (changeCheck("项目成本预算变更")) {
            setBudgetChange();
        }
        /*          * 里程碑变更相关结果集          */
        if (changeCheck("项目里程碑变更")) {
            setMilestoneChange(processDefinitionKey);
        }
        projectService.update(projectVO,false);
    }

    /**
     * 负责人变更
     *
     * @param project
     */
    private void setLeaderChange(ProjectVO project) {
        String[] projectLeader = new String[2];
        projectLeader[INT_ID] = (String) jsonMap.get(formMap.get("新负责人") + UNDERLINE + US_ID);
        projectLeader[INT_NAME] = (String) jsonMap.get(formMap.get("新负责人") + UNDERLINE + US_NAME);
        String [] memberIds = project.getProjectMemberIds();
        Set<String> memberIdSet = new HashSet<>(Arrays.asList(memberIds));
        String oldLeaderId = project.getProjectLeaderId();
        if (memberIdSet.contains(oldLeaderId)){
            memberIdSet.remove(oldLeaderId);
        }
        memberIdSet.add( projectLeader[INT_ID]);
        project.setProjectMemberIds(memberIdSet.toArray(new String[memberIdSet.size()]));

        project.setProjectLeaderId(projectLeader[INT_ID]);
        project.setProjectLeader(projectLeader[INT_NAME]);

        //添加负责人进入组成员
//        setProjectMember(project, false,oldLeaderId);
    }

    /**
     * 里程碑变更
     *
     * @param processDefinitionKey
     */
    private void setMilestoneChange(String processDefinitionKey) {
        Gson gson = new Gson();

        String jsonString = gson.toJson(jsonMap.get("proMilepostArr"), ArrayList.class);

        Type typeArrayList = new TypeToken<ArrayList<MilestoneChangeEO>>() {
        }.getType();

        List<MilestoneChangeEO> milestoneEOList = gson.fromJson(jsonString, typeArrayList);

        /* 需要存储的结果集 */
        List<ProjectMilepostEO> milepostEOList = new ArrayList<>(milestoneData(milestoneEOList));
        ProjectNameEOPage projectNameEOPage = new ProjectNameEOPage();
        projectNameEOPage.setProcDefKey(processDefinitionKey);
        List<ProjectNameEO> projectNameEOList = projectNameEODao.queryByList(projectNameEOPage);
        String stageId;
        if (CollectionUtils.isNotEmpty(projectNameEOList)) {
            stageId = projectNameEODao.queryByList(projectNameEOPage).get(0).getStageOrderId();
        } else {
            stageId = "";
        }

        //  里程碑存储
        for (ProjectMilepostEO eo : milepostEOList) {
            /*
             * 判断进行更新操作还是插入操作
             */
            if (-1 == eo.getMilepostVersion()) {
                if (StringUtils.isNotEmpty(stageId)) {
                    eo.setStageId(eo.getProjectId() + stageId);
                }
                milepostEODao.insert(eo);
            } else {
                milepostEODao.updateByPrimaryKeySelective(eo);
            }
        }
        // 交付物删除
        if (CollectionUtils.isNotEmpty(milestoneIdList)) {
            milepostResultEODao.deleteByMilepostIdIn(milestoneIdList);
        }
        // 交付物存储
        if (CollectionUtils.isNotEmpty(milestoneResultEOList)) {
            milepostResultEODao.insertList(milestoneResultEOList);
        }
    }

    /**
     * 更新ES 相关数据，项目，任务，子任务，左侧项目树
     *
     * @param project
     */
    private void saveESData(Project project) {

        //被这里覆盖了。。。
        if (CollectionUtils.isNotEmpty(taskList)) {
            //存储 任务
            for (Task task : taskList){
                task.setProjectLeaderId(project.getProjectLeaderId());
            }
            taskRepository.save(taskList);
        }

        if (CollectionUtils.isNotEmpty(childrenTaskList)) {
            //存储 子任务
            for(ChildrenTask childrenTask : childrenTaskList){
                childrenTask.setProjectLeaderId(project.getProjectLeaderId());
            }

            childTaskRepository.save(childrenTaskList);
        }

        if (CollectionUtils.isNotEmpty(userWithProjectsList)) {
            //进行存储
            userWithProjectsRepository.save(userWithProjectsList);
        }

        //更新project
        projectRepository.save(project);

    }


    /**
     * 组装项目组成员变更
     */
    private void setProjectMember(ProjectVO project) {

        String[] projectLeader = new String[2];
        projectLeader[INT_ID] = project.getProjectLeaderId();
        projectLeader[INT_NAME] = project.getProjectLeader();

        String[][] membersForm = new String[2][2];
        if (StringUtils.isNotEmpty((String) jsonMap.get(formMap.get("新项目组成员")))) {
            //新数据
            membersForm[INT_NEW][INT_ID] = (String) jsonMap.get(formMap.get("新项目组成员") + UNDERLINE + US_ID);
            membersForm[INT_NEW][INT_NAME] = (String) jsonMap.get(formMap.get("新项目组成员") + UNDERLINE + US_NAME);
        } else {
            throw new AdcDaBaseException("项目组成员参数异常");
        }
        String[] memberStr = new String[2];
        memberStr[INT_ID] = (membersForm[INT_NEW][INT_ID]);
        memberStr[INT_NAME] = (membersForm[INT_NEW][INT_NAME]);

        /*
         * 用于 组装 mapList 以及 project 所需的String[]
         * 二维数组
         */
        String[][] memberMapList = new String[2][];

        memberMapList[INT_ID] = memberStr[INT_ID].split(",");
        memberMapList[INT_NAME] = memberStr[INT_NAME].split(",");

        List<String> projectMemberIdList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(memberMapList[INT_ID])) {
            for (String userId : memberMapList[INT_ID]){
                projectMemberIdList.add(userId);
            }
        }
        List<Task> taskList = taskRepository.findByProjectId(project.getId());
        if (CollectionUtils.isNotEmpty(taskList)) {
            for (Task task : taskList) {
                if (StringUtils.equals(task.getTaskStatus(), ProjectStatusEnums.COMPLETE.getStatus())) {
                    continue;
                }
                if (null != task.getDelFlag() && task.getDelFlag()) {
                    continue;
                }
                if (CollectionUtils.isNotEmpty(task.getMemberIds())) {
                    for (String userId : task.getMemberIds()){
                        projectMemberIdList.add(userId); //防止用户操作，把任务成员干掉了
                    }
                }
            }
        }
        projectMemberIdList.removeAll(Collections.singleton(null));
        project.setProjectMemberIds(projectMemberIdList.toArray(new String[projectMemberIdList.size()]));
    }


    /**
     * 组装项目组成员变更
     */
//    private void setProjectMember(Project project, boolean isNotLeaderUpdate,String oldLeaderId) {
//
//        String[] projectLeader = new String[2];
//        projectLeader[INT_ID] = project.getProjectLeaderId();
//        projectLeader[INT_NAME] = project.getProjectLeader();
//
//        String[][] membersForm = new String[2][2];
//        /*
//         * 记录已经被删除的用户，以及新增的用户
//         */
//        Set<String> deleteUserIdSet = new TreeSet<>();
//        Set<String> addUserIdSet;
//        if (isNotLeaderUpdate) {
//
//            if (StringUtils.isNotEmpty((String) jsonMap.get(formMap.get("新项目组成员")))) {
//                //新数据
//                membersForm[INT_NEW][INT_ID] = (String) jsonMap.get(formMap.get("新项目组成员") + UNDERLINE + US_ID);
//                membersForm[INT_NEW][INT_NAME] = (String) jsonMap.get(formMap.get("新项目组成员") + UNDERLINE + US_NAME);
//
//                //获取所有旧的项目组成员
//                String[] oldMemberIds = project.getProjectMemberIds();
//
//
//                //初始化 addedId deletedId 新增用户中不包含项目负责人
//                addUserIdSet = new TreeSet<>(Arrays.asList(membersForm[INT_NEW][INT_ID].split(",")));
//
//                for (String oldUserId : oldMemberIds) {
//                    //新用户id中不包含   该用户
//                    if (!membersForm[INT_NEW][INT_ID].contains(oldUserId)
//                            &&!StringUtils.equals(oldUserId,project.getPm())
//                            &&!StringUtils.equals(oldUserId,project.getApproveUserId())) {
//                        //该用户不存在于 新的成员组中，添加进删除组
//                        deleteUserIdSet.add(oldUserId);
//                    }
//                }
//                //不处理负责人的关系
//                deleteUserIdSet.remove(projectLeader[INT_ID]);
//
//            } else {
//                throw new AdcDaBaseException("项目组成员参数异常");
//            }
//        } else {
//            //涉及负责人变更
//            addUserIdSet = new TreeSet<>();
//            addUserIdSet.add(projectLeader[INT_ID]);
//
//            String[] memberIds = project.getProjectMemberIds();
//            StringBuilder builder = new StringBuilder();
//            for (String s : memberIds) {
//                builder.append(s).append(",");
//            }
//            builder.deleteCharAt(builder.length() - 1);
//            membersForm[INT_NEW][INT_ID] = builder.toString();
//            String projectMemberNameStr = project.getProjectMemberNames() ;
//            if (!projectMemberNameStr.contains(projectLeader[INT_NAME])){
//                projectMemberNameStr = projectMemberNameStr+","+project.getProjectLeader() ;
//                project.setProjectMemberNames(projectMemberNameStr);
//            }
//            membersForm[INT_NEW][INT_NAME] = project.getProjectMemberNames();
//            //deletedId.add(oldLeaderId);
//        }
//        // 针对被删除的用户进行处理  // 如果不在项目组里，那么也不必要出现在任务里了？
//        taskList = taskRepository.findByProjectIdAndDelFlagNot(projectInfo[PROJECT_ID], Boolean.TRUE);
//        //项目经理应该看到项目下所有的任务及子任务
//        List<Task> taskList = taskRepository.findByProjectIdAndDelFlagNot(project.getId(),true);
//        Set<String> taskIdSetForProLeader = new HashSet<>();
//        for (Task task : taskList){
//            taskIdSetForProLeader.add(task.getId());
//        }
//        Set<String> childTaskIdSetForProLeader = new HashSet<>();
//        if(CollectionUtils.isNotEmpty(taskIdSetForProLeader)){
//            childrenTaskList = childTaskRepository.findByTaskIdInAndDelFlagNot(taskIdSetForProLeader,true);
//            for (ChildrenTask myChildrenTask : childrenTaskList){
//                childTaskIdSetForProLeader.add(myChildrenTask.getId());
//            }
//        }
//        Set<String> budgetIdSet= new HashSet<>();
//        Set<String> projectIdSet= new HashSet<>();
//        budgetIdSet.add(project.getBudgetId());
//        projectIdSet.add(project.getId());
//        saveUserWithProjectInTask(project.getProjectLeaderId(),taskIdSetForProLeader,childTaskIdSetForProLeader);
//        /*处理任务*/
//        if (CollectionUtils.isNotEmpty(deleteUserIdSet)&&CollectionUtils.isNotEmpty(taskList)) {
//            projectSaveService.doTaskNew(deleteUserIdSet, projectLeader, taskList);
//        }
//        /*
//         * 处理子任务
//         */
//        if (CollectionUtils.isNotEmpty(deleteUserIdSet)&&CollectionUtils.isNotEmpty(childrenTaskList)) {
//            projectSaveService.doChildTaskNew(deleteUserIdSet, childrenTaskList, projectLeader);
//        }
//
//        projectSaveService.doBudgetDelNew(deleteUserIdSet,project);
//        String[] memberStr = new String[2];
//        if (membersForm[INT_NEW][INT_ID].contains(projectLeader[INT_ID])) {
//            memberStr[INT_ID] = (membersForm[INT_NEW][INT_ID]);
//            memberStr[INT_NAME] = (membersForm[INT_NEW][INT_NAME]);
//        } else {
//            memberStr[INT_ID] = (projectLeader[INT_ID] + "," + membersForm[INT_NEW][INT_ID]);
//            memberStr[INT_NAME] = (projectLeader[INT_NAME] + "," + membersForm[INT_NEW][INT_NAME]);
//        }
//            /*
//             * 用于 组装 mapList 以及 project 所需的String[]
//             * 二维数组
//             */
//            String[][] memberMapList = new String[2][];
//
//            memberMapList[INT_ID] = memberStr[INT_ID].split(",");
//            memberMapList[INT_NAME] = memberStr[INT_NAME].split(",");
//
//            project.setProjectMemberIds(memberMapList[INT_ID]);
//            project.setMemberNames(memberMapList[INT_NAME]);
//            project.setProjectMemberNames(memberStr[INT_NAME]);
////从表单取map有问题
//            //  project.getMapList()
////            List<Map<String, String>> mapList = new ArrayList<>();
////            Map<String, String> map;
//
//            //int length = memberMapList[INT_ID].length;
//
////            for (int i = 0; i < length; i++) {
////                map = new HashMap<>();
////                map.put("name", memberMapList[INT_NAME][i]);
////                map.put("id", memberMapList[INT_ID][i]);
////                mapList.add(map);
////            }
//
////            project.setMapList(mapList);
//
//            //把关联部门加上
//            List<Map<String, String>> userIdDeptNameKvList = new ArrayList<>();
//          List<Map<String, String>> memberMapKvList = new ArrayList<>();
//            List<String> userNameList = new ArrayList<>();
//            if (CollectionUtils.isNotEmpty(project.getProjectMemberIds())) {
//                Set<String> projectMemberIdSet = new HashSet<>(Arrays.asList(project.getProjectMemberIds()));
//                projectMemberIdSet.add(project.getProjectLeaderId()); //防止leaderId 没放在任务成员中
//                List<UserEO> userEOList = userEODao.getUserWithRolesByUserIds(new ArrayList<>(projectMemberIdSet));
//                userIdDeptNameKvList = CommonUtil.userIdDeptNamesMapKv(userEOList,orgEODao);
//                memberMapKvList = CommonUtil.userIdUsnameMapKv(userEOList);
//                for (UserEO userEO: userEOList){
//                    userNameList.add(userEO.getUsname());
//                }
//                project.setProjectMemberNames(StringUtils.join(userNameList,','));
//                project.setMemberNames(userNameList.toArray(new String[userNameList.size()]));
//            }
//            project.setUserIdDeptNameMapList(userIdDeptNameKvList);
//            project.setMapList(memberMapKvList);
//
//        /*
//         * 新增的人都应该看到项目的业务，项目
//         */
//        addUserIdSet.add(project.getProjectLeaderId());
//        saveUserWithProject(addUserIdSet,budgetIdSet,projectIdSet);
//
//    }

    /**
     * 进行用户相关
     *
     * @param userId
     * @param taskIds
     */
    public void saveUserWithProjectInTask(String userId,  Set<String> taskIds,Set<String> childTaskIds) {
        UserWithProjects userWithProjects = userWithProjectsRepository.findOne(userId);
        if (null != userWithProjects) {
            userWithProjects.getTaskIds().addAll(taskIds);
            userWithProjects.getChildTaskIds().addAll(childTaskIds);
        }else {
            userWithProjects = new UserWithProjects();
            userWithProjects.getTaskIds().addAll(taskIds);
            userWithProjects.getChildTaskIds().addAll(childTaskIds);
        }
        userWithProjectsRepository.save(userWithProjects);
    }

    /**
     * 判断
     *
     * @param projectIdAllSet
     * @param projectIds
     * @return
     */
    private boolean checkConstant(Set<String> projectIdAllSet, Set<String> projectIds) {
        for (String s : projectIdAllSet) {
            if (projectIds.contains(s)) {
                return false;
            }
        }
        return true;
    }



    /**
     * 处理用户项目树 新加入的用户都应该看项目和业务
     * @param addedUserIdSet
     * @param budgetIdSet
     * @param projectIdSet
     */
    private void saveUserWithProject(Set<String> addedUserIdSet,Set<String> budgetIdSet,Set<String> projectIdSet) {
            List<UserWithProjects> userWithProjectsList = userWithProjectsRepository.findByUserIdIn(addedUserIdSet);
            for (UserWithProjects userWithProjects : userWithProjectsList){
                userWithProjects.getBusinessIds().addAll(budgetIdSet);
                userWithProjects.getProjectIds().addAll(projectIdSet);
            }
            userWithProjectsRepository.save(userWithProjectsList);
        }

    /**
     * 处理用户项目树
     * 新用户进行 project 新增关连
     * 旧用户 进行 project task childTask 删除关联
     *
     * @param deletedId
     * @param taskIdSet
     * @param childTaskIdSet
     */
    private void doUserWithProject(Set<String> deletedId, Set<String> taskIdSet, Set<String> childTaskIdSet,
        Set<String> addedId, Set<String> projectIdAllSet) {
        /*
         * 清空list
         */
        userWithProjectsList = new ArrayList<>();

        /*
         * 针对被删去的用户，同步删除项目，任务，子任务对应的记录
         *
         */
        if (CollectionUtils.isNotEmpty(deletedId)) {
            List<UserWithProjects> userWithProjectsListDelete = userWithProjectsRepository.findByUserIdIn(deletedId);

            for (UserWithProjects userWithProjects : userWithProjectsListDelete) {
                /*
                 * 处理项目
                 */
                Set<String> projectIds = userWithProjects.getProjectIds();
                projectIds.remove(projectInfo[PROJECT_ID]);
                userWithProjects.setProjectIds(projectIds);
                /*
                 * 对业务进行处理
                 */
                if (checkConstant(projectIdAllSet, projectIds)) {
                    /*
                     *  删除 该项目对应的budgetId
                     */
                    Set<String> budgetIds = userWithProjects.getBusinessIds();
                    log.warn("{}", budgetIds.contains(projectInfo[BUDGET_ID]));
                    budgetIds.remove(projectInfo[BUDGET_ID]);
                    userWithProjects.setBusinessIds(budgetIds);
                }
                /*
                 * 处理任务
                 */
                if (CollectionUtils.isNotEmpty(taskList)) {
                    Set<String> taskIds = userWithProjects.getTaskIds();
                    taskIds.removeAll(taskIdSet);
                    userWithProjects.setTaskIds(taskIds);
                }
                /*
                 * 处理子任务
                 */
                if (CollectionUtils.isNotEmpty(childrenTaskList)) {
                    Set<String> childTaskIds = userWithProjects.getChildTaskIds();
                    childTaskIds.removeAll(childTaskIdSet);
                    userWithProjects.setChildTaskIds(childTaskIds);
                }
            }
            userWithProjectsList.addAll(userWithProjectsListDelete);
        }

        /*
         * 针对新增的用户，只用更新project信息
         */
        if (CollectionUtils.isNotEmpty(addedId)) {
            List<UserWithProjects> userWithProjectsListNewUser = userWithProjectsRepository.findByUserIdIn(addedId);
            for (UserWithProjects userWithProjects : userWithProjectsListNewUser) {

                Set<String> businessIds = userWithProjects.getBusinessIds();
                businessIds.add(projectInfo[BUDGET_ID]);
                userWithProjects.setBusinessIds(businessIds);

                Set<String> projectIds = userWithProjects.getProjectIds();
                projectIds.add(projectInfo[PROJECT_ID]);
                userWithProjects.setProjectIds(projectIds);
            }
            userWithProjectsList.addAll(userWithProjectsListNewUser);

        }
    }

    /**
     * 进行成本预算的存储
     */
    private void setBudgetChange() {

        ProjectBudgetChangeEO[] budgetChangeEO = new ProjectBudgetChangeEO[2];
        budgetChangeEO[INT_OLD] = projectBudgetChangeEODao.selectByProjectId(projectInfo[PROJECT_ID]);
        budgetChangeEO[INT_NEW] = new ProjectBudgetChangeEO();

        if (null == budgetChangeEO[INT_OLD]) {
            budgetChangeEO[INT_NEW].setProjectId(projectInfo[PROJECT_ID]);
            budgetChangeEO[INT_NEW].setId(UUID.randomUUID10());

        } else {
            BeanUtils.copyProperties(budgetChangeEO[INT_OLD], budgetChangeEO[INT_NEW]);
        }

        /*
         * 更新项目名称信息
         */
        budgetChangeEO[INT_NEW].setProjectName(projectInfo[PROJECT_NAME]);

        String[] costStr = new String[5];
        BigDecimal[] cost = new BigDecimal[5];
        /*  *  人工    */
        int labor = 0;
        /*   * 采购     */
        int purchase = 1;
        /*    * 跨部门         */
        int crossSectoral = 2;
        /*   * 其他费用         */
        int other = 3;
        /*   * 合计         */
        int amount = 4;

        costStr[labor] = "新人工成本";
        costStr[purchase] = "新采购成本";
        costStr[crossSectoral] = "新跨部门协作成本";
        costStr[other] = "新其他费用";
        costStr[amount] = "新成本预算总金额";

        int i = 0;
        String s;
        for (String str : costStr) {
            s = (String) jsonMap.get(formMap.get(str));
            if (StringUtils.isNotEmpty(s)) {
                cost[i] = new BigDecimal(s);
            } else {
                cost[i] = BigDecimal.valueOf(0.00);
            }
            i++;
        }
        budgetChangeEO[INT_NEW].setAmountCount(cost[amount]);
        budgetChangeEO[INT_NEW].setCooperationCost(cost[crossSectoral]);
        budgetChangeEO[INT_NEW].setPersonCost(cost[labor]);
        budgetChangeEO[INT_NEW].setPurchaseCost(cost[purchase]);
        budgetChangeEO[INT_NEW].setOtherCost(cost[other]);

        /*
         * 判断进行更新操作还是插入操作
         */
        if (null == budgetChangeEO[INT_OLD]) {
            //  预算成本 新增
            projectBudgetChangeEODao.insert(budgetChangeEO[INT_NEW]);

        } else {
            //  预算成本 修改
            projectBudgetChangeEODao.updateByPrimaryKeySelective(budgetChangeEO[INT_NEW]);
        }
    }

    /**
     * 针对项目 时间变更进行调整
     *
     * @return
     */
    private Date[] setProjectDate() {
        String[] time = new String[2];

        /*
         * 校验开始时间
         */
        if (StringUtils.isNotEmpty(jsonMap.get(formMap.get("新开始时间")))) {
            time[BEGIN_TIME] = (String) jsonMap.get(formMap.get("新开始时间"));
        } else {
            time[BEGIN_TIME] = (String) jsonMap.get(formMap.get("原开始时间"));

        }

        /*
         * 校验结束时间
         */
        if (StringUtils.isNotEmpty(jsonMap.get(formMap.get("新结束时间")))) {
            time[END_TIME] = (String) jsonMap.get(formMap.get("新结束时间"));

        } else {
            time[END_TIME] = (String) jsonMap.get(formMap.get("原结束时间"));

        }
        Date[] date = new Date[2];

        try {
            date[BEGIN_TIME] = DateUtils.stringToDate(time[BEGIN_TIME], DateUtils.YYYY_MM_DD_EN);
            date[END_TIME] = DateUtils.stringToDate(time[END_TIME], DateUtils.YYYY_MM_DD_EN);
        } catch (Exception e) {
            throw new AdcDaBaseException("项目时间变更，项目起止时间输入异常");
        }

        return date;
    }

    /**
     * 用于判断变更的单选框
     *
     * @param field
     * @return
     */
    private boolean changeCheck(String field) {
        return YES.equals(jsonMap.get(formMap.get(field)));
    }

    /**
     * 表单基本数据，项目id与名称
     */
    private String[] projectInfo;

    private List<String> milestoneIdList;

    /**
     * 构造里程碑数据集
     *
     * @param milestoneEOList
     * @return
     */
    private List<ProjectMilepostEO> milestoneData(List<MilestoneChangeEO> milestoneEOList) {
        String milestoneId;
        milestoneIdList = new ArrayList<>();
        List<ProjectMilepostEO> res = new ArrayList<>(milestoneEOList.size());
        milestoneResultEOList = new ArrayList<>();

        for (MilestoneChangeEO eo : milestoneEOList) {
            ProjectMilepostEO resultEO = new ProjectMilepostEO();

            if (StringUtils.isNotEmpty(eo.getId())) {
                /*
                 *   属于已有的里程碑
                 */
                milestoneId = eo.getId();
                milestoneIdList.add(milestoneId);

                ProjectMilepostEO oriEO = milepostEODao.selectByPrimaryKey(milestoneId);
                if (oriEO == null) {
                    throw new AdcDaBaseException("ProjectMilepostEO error");
                }

                String extInfo = oriEO.getExtInfo5();
                String eonInfoNew;
                if (StringUtils.isNotEmpty(extInfo)) {
                    eonInfoNew = extInfo + "," + eo.getOldName().get(FormKeyWord.VALUE);
                } else {
                    eonInfoNew = eo.getOldName().get(FormKeyWord.VALUE);
                }
                resultEO.setExtInfo5(eonInfoNew);
            } else {
                /*
                 * 属于新增的里程碑
                 */
                resultEO.setMilepostVersion(-1);
                milestoneId = UUID.randomUUID10();
            }

            resultEO.setId(milestoneId);

            /* 组装时间 */
            Date[] dateArray = eo.getDateValue();
            resultEO.setMilepostBeginTime(dateArray[BEGIN_TIME]);
            resultEO.setMilepostEndTime(dateArray[END_TIME]);

            /* 组装负责人 */
            resultEO.setMilepostManagerId(eo.getUserIdValue());
            resultEO.setMilepostManagerName(eo.getUserNameValue());

            /* 目标 名称 */
            resultEO.setMilepostTarget(eo.getTargetValue());
            resultEO.setMilepostName(eo.getNameValue());

            /*项目信息*/
            resultEO.setProjectId(projectInfo[PROJECT_ID]);
            resultEO.setProjectName(projectInfo[PROJECT_NAME]);

            res.add(resultEO);
            /*
             * 成果物
             * 交付物初始化
             */
            List<String> outcomesValue = eo.getOutcomesValue();
            ProjectMilepostResultEO subBaseEO = new ProjectMilepostResultEO();
            subBaseEO.setMilepostId(milestoneId);
            milestoneResultEOList.addAll(setSubResult(subBaseEO, outcomesValue));

        }
        return res;
    }

    /**
     * 交付物结果集
     */
    private List<ProjectMilepostResultEO> milestoneResultEOList;

    /**
     * 对交付物进行初始化,
     *
     * @param subBaseEO
     * @param outcomesValue
     */
    private List<ProjectMilepostResultEO> setSubResult(ProjectMilepostResultEO subBaseEO, List<String> outcomesValue) {
        List<ProjectMilepostResultEO> subResultList = new ArrayList<>();

        for (String outcomeValue : outcomesValue) {
            ProjectMilepostResultEO subResult = new ProjectMilepostResultEO();

            BeanUtils.copyProperties(subBaseEO, subResult);

            subResult.setId(UUID.randomUUID10());
            subResult.setFileName(outcomeValue);
            subResultList.add(subResult);
        }
        return subResultList;
    }

}
