package com.adc.da.budget.service;

import com.adc.da.budget.dao.BudgetEODao;
import com.adc.da.budget.dao.TaskResultEODao;
import com.adc.da.budget.dao.UserEPDao;
import com.adc.da.budget.entity.*;
import com.adc.da.budget.enums.ProjectStatusEnums;
import com.adc.da.budget.repository.ChildTaskRepository;
import com.adc.da.budget.repository.ProjectRepository;
import com.adc.da.budget.repository.TaskPriorityRepository;
import com.adc.da.budget.repository.TaskRepository;
import com.adc.da.budget.repository.UserWithProjectsRepository;
import com.adc.da.budget.utils.ArrayUtils;
import com.adc.da.budget.utils.BeanUtils;
import com.adc.da.budget.utils.CommonUtil;
import com.adc.da.budget.vo.TaskVO;
import com.adc.da.login.util.UserUtils;
import com.adc.da.superadmin.service.SuperAdminService;
import com.adc.da.sys.dao.OrgEODao;
import com.adc.da.sys.dao.UserEODao;
import com.adc.da.sys.entity.OrgEO;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.sys.service.OrgEOService;
import com.adc.da.sys.service.UserEOService;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.http.Result;
import com.adc.da.util.utils.BeanMapper;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.adc.da.budget.constant.ProjectSearchField.NAME;

/**
 * @Auther: ZHAIKAIXUAN
 * @Date: 2018/11/19 14:33
 * @Description:
 */
@Service
@Slf4j
public class TaskInsertUpdateService extends BaseService<Task, String> {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserEPDao userEPDao;

    @Autowired
    private UserEODao userEODao;
    @Autowired
    private OrgEODao orgEODao;

    @Autowired
    private BeanMapper beanMapper;

    @Autowired
    private ChildTaskRepository childTaskRepository;

    @Autowired
    private BudgetEOService budgetEOService;

    @Autowired
    private ChildrenTaskService childrenTaskService;

    @Autowired
    private TaskPriorityRepository taskPriorityRepository;

    @Autowired
    private UserWithProjectsRepository userWithProjectsRepository;

    @Autowired
    private BudgetEODao budgetEODao;

    @Autowired
    private SuperAdminService superAdminService;

    @Autowired
    private TaskResultEOService taskResultEOService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private OrgEOService orgEOService;

    @Autowired
    private UserEOService userEOService;

    @Autowired
    private TaskResultEODao taskResultEODao;

    private String permissionCheck(TaskVO taskVO) {
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("请登录！");
        }
        //判断是否选择所属业务或者项目
        if (StringUtils.isBlank(taskVO.getBudgetId()) && StringUtils.isBlank(taskVO.getProjectId())) {
            throw new AdcDaBaseException("请选择所属业务或项目！");
        }
        //判断任务名称是否重复
        //根据项目id查询所有的任务信息
        if(StringUtils.isNotEmpty(taskVO.getProjectId())){
            List<Task> taskList = taskRepository.findByProjectIdAndDelFlagNot(taskVO.getProjectId(),true);
            for (Task task : taskList){
                if(StringUtils.equals(taskVO.getName(),task.getName())&&!StringUtils.equals(task.getId(),taskVO.getId())){
                    throw new AdcDaBaseException("该任务已存在！");
                }
                continue;
            }
            //判断任务名称是否重复
            //如果是业务任务
        }else if(StringUtils.isNotEmpty(taskVO.getBudgetId())){
            List<Task> taskList = taskRepository.findByBudgetIdAndDelFlagNot(taskVO.getBudgetId(),true);
            for (Task task : taskList){
                if(StringUtils.equals(taskVO.getName(),task.getName())&&!StringUtils.equals(task.getId(),taskVO.getId())){
                    throw new AdcDaBaseException("该任务已存在！");
                }
                continue;
            }
        }

        return userId;
    }

    private void permissionCheckUpdate(TaskVO taskVO) {
//        if (StringUtils.isEmpty(UserUtils.getUserId())) {
//            throw new AdcDaBaseException("请登录！");
//        }
        //判断是否选择所属业务或者项目
        if (StringUtils.isBlank(taskVO.getBudgetId()) && StringUtils.isBlank(taskVO.getProjectId())) {
            throw new AdcDaBaseException("请选择所属业务或项目！");
        }
        //判断任务名称是否重复
        //根据项目id查询所有的任务信息
        if(StringUtils.isNotEmpty(taskVO.getProjectId())){
            List<Task> taskList = taskRepository.findByProjectIdAndDelFlagNot(taskVO.getProjectId(),true);
            for (Task task : taskList){
                if((StringUtils.equals(taskVO.getName(),task.getName()))
                        && (!StringUtils.equals(taskVO.getId(),task.getId())) ){
                    throw new AdcDaBaseException("该任务已存在！");
                }
                continue;
            }
            //判断任务名称是否重复
            //如果是业务任务
        }else if(StringUtils.isNotEmpty(taskVO.getBudgetId())){
            List<Task> taskList = taskRepository.findByBudgetIdAndDelFlagNot(taskVO.getBudgetId(),true);
            for (Task task : taskList){
                if((StringUtils.equals(taskVO.getName(),task.getName()))
                        && (!StringUtils.equals(taskVO.getId(),task.getId())) ){
                    throw new AdcDaBaseException("该任务已存在！");
                }
                continue;
            }
        }
    }

    /**
     * 初始化Task用于新增，区分业务任务与项目任务
     */
    private void initTaskInfo(Task task, String userId) throws Exception {

        if (StringUtils.isNotEmpty(task.getProjectId())) {
            /*
             * 项目任务
             */
            initProjectTask(task);
        } else {
            /*
             * 业务任务
             */
            initBudgetTask(task);
        }

        task.setCreateUserId(userId);
        task.setCreateTime(new Date());
        task.setId(UUID.randomUUID().toString());
        task.setTaskStatus(ProjectStatusEnums.EXECUTE.getStatus());
    }

    /**
     * 项目任务
     *
     * @param task
     * @throws Exception
     */
    private void initBudgetTask(Task task) throws Exception {
        UserEO userEO = UserUtils.getUser();
        if (null == userEO) {
            throw new AdcDaBaseException("请登录！");
        }

        BudgetEO budgetEO = budgetEOService.selectByPrimaryKey(task.getBudgetId());
        if (budgetEO != null) {
            if (!StringUtils.equals(userEO.getUsid(),budgetEO.getBusinessAdminId())
                    &&!StringUtils.equals(budgetEO.getPm(),userEO.getUsid())
                    && !superAdminService.isSuperAdmin()){
                throw new AdcDaBaseException("您不是" + budgetEO.getProjectName() + " 业务的负责人或管理员，无权创建任务 ！");
            }
            task.setDeptId(budgetEO.getDeptId());
            task.setPm(budgetEO.getPm());
            task.setBusinessCreateUserId(budgetEO.getCreateUserId());
            task.setProjectTeam(budgetEO.getProjectTeam());
            task.setBudgetName(budgetEO.getProjectName());
            task.setBudgetName1(budgetEO.getProjectName());
            task.setProjectType(Integer.valueOf(budgetEO.getProperty()));
            /*
             * 若负责人未设置，将业务负责人设置为负责人
             */
            if (StringUtils.isEmpty(task.getApproveUserId())) {
                task.setApproveUserId(budgetEO.getPm());
            }
        } else {
            throw new AdcDaBaseException("业务查询失败" + task.getBudgetId());
        }
        /*
         * 设置业务名称搜索字段
         */
        task.setSearchBudgetId(task.getBudgetId());
    }

    /**
     * 处理项目任务信息
     *
     * @param task
     * @return
     * @throws Exception
     */
    private void initProjectTask(Task task) throws Exception {
        Project project = projectRepository.findByIdAndDelFlagNot(task.getProjectId(), Boolean.TRUE);
        if (project != null) {
            if (null != project && StringUtils.equals(ProjectStatusEnums.COMPLETE.getStatus(), project.getFinishedStatus())) {
                throw new AdcDaBaseException("项目已关闭或过期，创建任务失败！");
            }

            BudgetEO budgetEO = budgetEOService.selectByPrimaryKey(project.getBudgetId());
            //加了一个判空，但是感觉用处不大
            if (budgetEO != null) {
                task.setDeptId(project.getDeptId());
                task.setProjectName(project.getName());
                task.setProjectName1(project.getName());
                task.setPm(project.getPm());
                task.setProjectTeam(project.getProjectTeam());
                task.setProjectLeaderId(project.getProjectLeaderId());
                task.setBusinessCreateUserId(budgetEO.getCreateUserId());
                task.setProjectType(project.getProjectType());
                /*
                 * 若负责人未设置，将项目负责人设置为负责人
                 */
                if (StringUtils.isEmpty(task.getApproveUserId())) {
                    task.setApproveUserId(project.getProjectLeaderId());
                }
            } else {
                throw new AdcDaBaseException("业务查询失败" + project.getBudgetId());
            }
        } else {
            throw new AdcDaBaseException("项目查询失败" + task.getProjectId());
        }

        /*
         * 设置业务名称搜索字段
         */
        task.setSearchBudgetId(project.getBudgetId());

    }

    /**
     * @Description: 新增
     * @auther: ZHAIKAIXUAN
     * @params:
     * @return:
     * @date: 2018/11/19 14:37
     */
    public Task insert(TaskVO taskVO) throws Exception {
        String userId = permissionCheck(taskVO);
        Task task = BeanUtils.map(taskVO, Task.class);
        if (null == task) {
            throw new AdcDaBaseException("传参异常");
        }

        initTaskInfo(task, userId);

        //如果任务创建人是业务任务同时是新用户需要关联到业务
        UserWithProjects uwp = userWithProjectsRepository.findOne(userId);
        if (null == uwp) {
            uwp = new UserWithProjects();
            uwp.setUserId(userId);
            if (StringUtils.isEmpty(task.getProjectId())) {
                BudgetEO budgetEO = budgetEOService.selectByPrimaryKey(task.getBudgetId());
                uwp.getBusinessIds().add(budgetEO.getId());
            }
            userWithProjectsRepository.save(uwp);
        }
        Set<String> memberIdSet = new HashSet<>();
        HashMap<String,List<String>> DeptUserIdMap = new HashMap<>();
        List<Map<String,String>> deptInfoListMap = taskVO.getDeptInfoListMap();// 所选部门ID和类型 如类型=1 只选当前自身 类型=2 包含子部门
        for(Map<String,String> map :deptInfoListMap){
            int type =  Integer.parseInt(map.get("type").toString());
            String deptId =  map.get("deptId").toString();
            Set<String> temSet = new HashSet<>();
            if(type == 1){
                List<String> deptList = new ArrayList<>();
                deptList.add(deptId);
                List<UserEO> list = userEOService.getAllUserEOsByOrgIds(deptList);//根据部门id查询成员
                for(UserEO userEO:list){
                    memberIdSet.add(userEO.getUsid());
                    temSet.add(userEO.getUsid());
                }
                DeptUserIdMap.put(deptId,new ArrayList<>(temSet));
                /// pro.setDeptIdUserIdList(DeptUserIdMap);
            }else if(type == 2){
                List<UserEO> list = orgEOService.listUserEOByOrgId(deptId);//根据部门id查询成员
                for(UserEO userEO:list){
                    memberIdSet.add(userEO.getUsid());
                    temSet.add(userEO.getUsid());
                }
                DeptUserIdMap.put(deptId,new ArrayList<>(temSet));
            }
        }
        task.setDeptIdUserIdList(DeptUserIdMap);
        String[] memberIds = task.getMemberIds();
        if (CollectionUtils.isNotEmpty(memberIdSet)||CollectionUtils.isNotEmpty(memberIds)) {
            Set<String> taskMemberIdSet = new HashSet<>(Arrays.asList(memberIds));
            taskMemberIdSet.addAll(memberIdSet);

            task.setMemberIds(taskMemberIdSet.toArray(new String[taskMemberIdSet.size()]));

            List<String> taskMemberIdList = new ArrayList<>(taskMemberIdSet);
            List<UserEO> userEOList = userEODao.getUserWithRolesByUserIds(taskMemberIdList);
            String[] taskMemberNameArr = new String[userEOList.size()];
            for (int i = 0 ; i < userEOList.size(); i ++){
                taskMemberNameArr[i] = userEOList.get(i).getUsname();
                //如果是新用户，新增数据到用户关联
                UserWithProjects userWithProjects = userWithProjectsRepository.findOne(userEOList.get(i).getUsid());
                if (null == userWithProjects) {
                    userWithProjects = new UserWithProjects();
                    userWithProjects.setUserId(userEOList.get(i).getUsid());
                }
                userWithProjects.getTaskIds().add(task.getId());
                if (StringUtils.isEmpty(task.getBudgetId())) {
                    userWithProjects.getProjectIds().add(task.getProjectId());
                    Project one = projectRepository.findOne(task.getProjectId());
                    userWithProjects.getBusinessIds().add(one.getBudgetId());
                } else {
                    userWithProjects.getBusinessIds().add(task.getBudgetId());

                }
                userWithProjectsRepository.save(userWithProjects);
            }
            task.setMemberNames(taskMemberNameArr);
            task.setMemberNameString(StringUtils.join(taskMemberNameArr,','));
            task.setMapsList(CommonUtil.userIdUsnameMapKv(userEOList));
            task.setUserIdDeptNameMapList(CommonUtil.userIdDeptNamesMapKv(userEOList,orgEODao));
        }



        //新增的时候保存任务ID到用户业务树关联表 任务创建人、项目经理、业务经理、业务创建人 都需要存储任务ID到用户业务树关联表

        Set<String> userIds = taskService.getUserIds(task);
        childrenTaskService.setUserWithProjectsData(userWithProjectsRepository,
                UserWithProjects.class.getMethod("getTaskIds"), userIds, true, task.getId());

//        task.setMemberNames(userName);
//        task.setMapsList(CommonUtil.userMapKv(userEPEntities));
        //新增的时候保存任务优先级数据
        taskPriorityRepository.save(new TaskPriority(task.getId(), task.getName(), 0, task.getMemberIds()));

        /*
         * 插入任务成果物
         */
        if (CollectionUtils.isNotEmpty(task.getTaskResultEOList())) {
            taskResultEOService.insertList(task.getTaskResultEOList(), task.getId());
        }
        addTaskApproveUserName(task);
        return taskRepository.save(task);
    }
    private  void addTaskApproveUserName(Task task){
        if (StringUtils.isNotEmpty(task.getApproveUserId())){
            UserEPEntity  userEPEntity = userEPDao.queryUserById(task.getApproveUserId());
            if (null != userEPEntity ){
                task.setApproveUserName(userEPEntity.getUsname());
            }
        }
    }

    public Task update(TaskVO taskVO) {
        UserEO loginUserEO = UserUtils.getUser();
        if (null == loginUserEO) {
            throw new AdcDaBaseException("请登录！");
        }
        try {
            //add 编辑的时候判断任务是否存在
            permissionCheckUpdate(taskVO);
            /*
             * 需要判断人员信息是否有变化  如果有变化需要去比对增加的人或者修改的人  同步到用户关联表数据
             *
             * 如果任务的所属项目或者所属业务变更也需要同步
             *
             * 如果是业务任务，需要判断当前人是否是业务经理或者业务创建人
             * 只是成员需要判断当前业务下是否还有他的任务存在，如果没有移除当前业务在用户关联表
             */

            Task dbTask = taskRepository.findOne(taskVO.getId());
            if (StringUtils.equals(dbTask.getTaskStatus(),ProjectStatusEnums.COMPLETE.getStatus())){
                throw new AdcDaBaseException("当前任务已完成，不能再次修改");
            }
            List<String> deleteUserIdList = ArrayUtils.compare(taskVO.getMemberIds(),dbTask.getMemberIds());
            checkChildTaskStatus(dbTask.getId() , deleteUserIdList);
            String budgetId = null ;
            if (StringUtils.isEmpty(taskVO.getProjectId())) {
                BudgetEO budgetEO  = budgetEODao.selectByPrimaryKey(taskVO.getBudgetId()); // 只用查询一次即可
                if (budgetEO != null) {
                    budgetId = budgetEO.getId();
                    if (!StringUtils.equals(loginUserEO.getUsid(), budgetEO.getBusinessAdminId())
                            && !StringUtils.equals(budgetEO.getPm(), loginUserEO.getUsid())
                            && !superAdminService.isSuperAdmin()) {
                        throw new AdcDaBaseException("您不是" + budgetEO.getProjectName() + " 业务的负责人或管理员，无权编辑任务 ！");
                    }
                }
            }
            Task newTask = new Task();
            BeanUtils.copyPropertiesIgnoreNullValue(taskVO, newTask);

//            // 获取人员创建数组的名字
//            String[] memberIds = taskVO.getMemberIds();
//            List<UserEPEntity> userEPEntities = userEPDao.checkUserExistById(memberIds);
//            String[] userName = new String[userEPEntities.size()];
//            for (int i = 0; i < userEPEntities.size(); i++) {
//                userName[i] = userEPEntities.get(i).getUsname();
//            }
            Set<String> memberIdSet = new HashSet<>();
            HashMap<String,List<String>> DeptUserIdMap = new HashMap<>();

            List<Map<String,String>> deptInfoListMap = taskVO.getDeptInfoListMap();// 所选部门ID和类型 如类型=1 只选当前自身 类型=2 包含子部门
            for(Map<String,String> map :deptInfoListMap){
                int type =  Integer.parseInt(map.get("type").toString());
                String deptId1 =  map.get("deptId").toString();
                Set<String> tempSet =new HashSet<>();
                if(type == 1){
                    List<String> deptList = new ArrayList<>();
                    deptList.add(deptId1);
                    List<UserEO> list = userEOService.getAllUserEOsByOrgIds(deptList);//根据部门id查询成员
                    for(UserEO userEO:list){
                        memberIdSet.add(userEO.getUsid());
                        tempSet.add(userEO.getUsid());
                    }
                    DeptUserIdMap.put(deptId1,new ArrayList<>(tempSet));
                    /// pro.setDeptIdUserIdList(DeptUserIdMap);
                }else if(type == 2){
                    List<UserEO> userlist = orgEOService.listUserEOByOrgId(deptId1);//根据部门id查询成员
                    for(UserEO userEO:userlist){
                        memberIdSet.add(userEO.getUsid());
                        tempSet.add(userEO.getUsid());
                    }
                    DeptUserIdMap.put(deptId1,new ArrayList<>(tempSet));
                    //pro.setDeptIdUserIdList(DeptUserIdMap);
                }
            }
            newTask.setDeptIdUserIdList(DeptUserIdMap);
            List<UserWithProjects> userWithProjectsList = new ArrayList<>();
            String[] memberIds = taskVO.getMemberIds();
            // 判断有根据部门选人 或直接选人
            if (CollectionUtils.isNotEmpty(memberIdSet) || CollectionUtils.isNotEmpty(memberIds)) {
                Set<String> taskMemberIdSet;
                if (CollectionUtils.isNotEmpty(memberIds)) {
                    taskMemberIdSet = new HashSet<>(Arrays.asList(memberIds));
                }else {
                    taskMemberIdSet= new HashSet<>();
                }
                taskMemberIdSet.addAll(memberIdSet);
                List<String> taskMemberIdList = new ArrayList<>(taskMemberIdSet);

                //添加对任务的操作记录
                //比对增加了哪些成员  同步到用户关联表
                List<String> addedUserIdList = ArrayUtils.compare(dbTask.getMemberIds(),taskMemberIdList);
                if (CollectionUtils.isNotEmpty(addedUserIdList)) {
                    for (String userId : addedUserIdList) {
                        UserWithProjects userWithProjects = userWithProjectsRepository.findOne(userId);
                        //如果是新用户，新增数据到用户关联
                        if (null == userWithProjects) {
                            userWithProjects = new UserWithProjects();
                            userWithProjects.setUserId(userId);
                        }
                        userWithProjects.getTaskIds().add(dbTask.getId());
                        if (StringUtils.isNotEmpty(budgetId)) {
                            userWithProjects.getBusinessIds().add(budgetId);
                        }
                        userWithProjectsList.add(userWithProjects);
                    }
                }
                if (CollectionUtils.isNotEmpty(userWithProjectsList)) {
                    userWithProjectsRepository.save(userWithProjectsList);  // 使用saveList 优化
                }

                newTask.setMemberIds(taskMemberIdSet.toArray(new String[taskMemberIdSet.size()]));
                List<UserEO> userEOList = userEODao.getUserWithRolesByUserIds(taskMemberIdList);
                List<OrgEO> orgEOList = orgEODao.queryOrgAll();
                String[] taskMemberNameArr = new String[userEOList.size()];
                for (int i = 0; i < userEOList.size(); i++) {
                    taskMemberNameArr[i] = userEOList.get(i).getUsname();
                    newTask.setMemberNames(taskMemberNameArr);
                    newTask.setMemberNameString(StringUtils.join(taskMemberNameArr,','));
                    newTask.setMapsList(CommonUtil.userIdUsnameMapKv(userEOList));
                    newTask.setUserIdDeptNameMapList(CommonUtil.userIdDeptNamesMapKv(userEOList,orgEOList)); //此处存在循环调用数据库
                }
            }
            //原项目ID
            //String budgetId = business.getBudgetId();
            newTask.setModifyTime(new Date());
            // 查询
            Task taskOri = taskRepository.findOne(taskVO.getId());
            String oldName = taskOri.getName();
            // 复制属性
            BeanUtils.copyPropertiesIgnoreNullValue(newTask, taskOri);
            //解决复制对象时null不会被复制问题
            taskOri.setBudgetId(taskVO.getBudgetId());
            if (StringUtils.isNotEmpty(taskVO.getBudgetId())) {
                taskOri.setSearchBudgetId(taskVO.getBudgetId());
            }
            taskOri.setProjectId(taskVO.getProjectId());
//            taskOri.setMemberNames(userName);
            //设置部门ID为项目的部门ID
            String deptId = null;


            if (taskOri.getProjectId() != null) {
                Project project = projectRepository.findOne(taskOri.getProjectId());
                if (project != null && !Boolean.TRUE.equals(project.getDelFlag())) {
                    //项目都被完成了，还更新个锤子任务
//                    project.setFinishedStatus(ProjectStatusEnums.EXECUTE.getStatus());
//                    projectRepository.save(project);
                    deptId = project.getDeptId();
                    taskOri.setProjectLeaderId(project.getProjectLeaderId());
                    //项目是否变更    原来这判断写错了啊 !projectId.equals(taskOri.getProjectId())
                    if (!StringUtils.equals(dbTask.getProjectId(), taskVO.getProjectId())) {
                        taskOri.setProjectName(project.getName());
                        taskOri.setProjectName1(project.getName());
                        taskOri.setProjectType(project.getProjectType());
                        //添加校验，验证任务中的成员是否是转移之后项目的项目组成员
                        checkTaskSwitch(taskRepository.findOne(taskVO.getId()), newTask, "project");
                    }
                }
                //判断是否修改项目状态
                checkProjectStatus(taskOri.getProjectId());
            } else {
                BudgetEO budgetEO = budgetEOService.selectByPrimaryKey(taskOri.getBudgetId());
                if (budgetEO != null) {
                    deptId = budgetEO.getDeptId();
                    //业务是否变更
                    if (!budgetId.equals(taskOri.getBudgetId())) {
                        taskOri.setBudgetName(budgetEO.getProjectName());
                        taskOri.setBudgetName1(budgetEO.getProjectName());
                        taskOri.setProjectType(Integer.valueOf(budgetEO.getProperty()));
                        checkTaskSwitch(taskRepository.findOne(taskVO.getId()), newTask, "budget");
                    }
                }

            }
            if (deptId == null) {
                throw new AdcDaBaseException("该任务的上级项目或业务不存在所属部门！");
            }
            taskOri.setDeptId(deptId);
            // 保存
            //taskOri.setMapsList(CommonUtil.userMapKv(userEPEntities));
            if (ProjectStatusEnums.COMPLETE.getStatus().equals(taskOri.getTaskStatus())) {
                taskOri.setFinishedActualTime(new Date());
            } else {
                taskOri.setFinishedActualTime(null);
            }
            //add by 丁强
            if (CollectionUtils.isNotEmpty(taskVO.getTaskResultEOList())) {
                //taskResultEOService.updateList(taskVO.getTaskResultEOList(), taskVO.getId());
                taskResultEOService.deleteAllByTaskId(taskVO.getId());
                List<TaskResultEO> taskResultEOList=taskVO.getTaskResultEOList();
                for(TaskResultEO taskResultEO: taskResultEOList){
                    //设置ID
                    String taskResultId=taskResultEO.getId();
                    if(StringUtils.isEmpty(taskResultId)){
                        taskResultId=com.adc.da.util.utils.UUID.randomUUID10();
                    }
                    taskResultEO.setId(taskResultId);
                    taskResultEODao.insertSelective(taskResultEO);
                }
            }
            else{
                taskResultEOService.deleteAllByTaskId(taskVO.getId());
                taskOri.setTaskResultEOList(null);
            }
            addTaskApproveUserName(taskOri);
            taskOri.setProjectMemberUpdatingTime(null);
            Task bui = taskRepository.save(taskOri);
            //添加对任务的操作记录
            //比对删除了哪些成员  同步到用户关联表
            //  需要判断如果是业务任务  当前业务下是否还有参与的任务，如果没有 删除业务数据
            List<String> delIds = ArrayUtils.compare(taskVO.getMemberIds(), dbTask.getMemberIds());
            BudgetEO budgetEO = null;
            if (StringUtils.isNotEmpty(dbTask.getBudgetId())) {
                budgetEO = budgetEODao.selectByPrimaryKey(dbTask.getBudgetId());
            }
            userWithProjectsList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(delIds)) {
                for (String id : delIds) {
                    if (StringUtils.equals(dbTask.getCreateUserId(), id)) {
                        continue;
                    }
                    UserWithProjects userWithProjects = userWithProjectsRepository.findOne(id);
                    if(userWithProjects==null){
                        continue;
                    }
                    userWithProjects.getTaskIds().remove(dbTask.getId());
                    if (StringUtils.isEmpty(dbTask.getProjectId())) {
//                        BudgetEO budgetEO = budgetEODao.selectByPrimaryKey(task.getBudgetId());
                        if (null!= budgetEO && !StringUtils.equals(id, budgetEO.getPm())
                                && !StringUtils.equals(id, budgetEO.getCreateUserId())) {
                            List<Task> tasks = taskRepository
                                    .findByBudgetIdAndMemberIdsInAndDelFlagNot(budgetEO.getId(), id, Boolean.TRUE);
                            if (CollectionUtils.isEmpty(tasks)) {
//                                userWithProjects = userWithProjectsRepository.findOne(id);
                                userWithProjects.getBusinessIds().remove(budgetEO.getId());
                            }
                        }
                    }
                    userWithProjectsList.add(userWithProjects);
//                    userWithProjectsRepository.save(userWithProjects);
                }
                if (CollectionUtils.isNotEmpty(userWithProjectsList)){
                    userWithProjectsRepository.save(userWithProjectsList);
                }
            }
            if (!oldName.equals(bui.getName())) {
                childrenTaskService.updateBelongTaskName(bui);
            }

            taskPriorityRepository.save(new TaskPriority(dbTask.getId(), dbTask.getName(), 0, dbTask.getMemberIds()));
            return bui;
        } catch (Exception e) {
            log.error("update budget/task error ", e);
            throw new AdcDaBaseException(e.getMessage());
        }
    }


    private void checkChildTaskStatus(String taskId , List<String> deleteUserIdList){
        List<ChildrenTask> childrenTaskList = childTaskRepository.findByTaskIdAndDelFlagNot(taskId,true);
        if (CollectionUtils.isNotEmpty(childrenTaskList)&&CollectionUtils.isNotEmpty(deleteUserIdList)){
            List<UserEO> userEOList = userEODao.getUserWithRolesByUserIds(deleteUserIdList);
            Map<String, UserEO> map= CommonUtil.userIdUserEOMap(userEOList);
            for (String userId : deleteUserIdList) {
                for (ChildrenTask childrenTask : childrenTaskList) {
                    if (!StringUtils.equals(childrenTask.getStatus(), "已完成")) {
//                        childTaskName = childrenTask.getChildTaskName();
                           //UserEO userEO = userEODao.selectByPrimaryKey(userId);
                           if (null != map.get(userId) && 0== map.get(userId).getDelFlag()&& Arrays.asList(childrenTask.getMemberIds()).contains( map.get(userId).getUsid())) {
//                            throw new AdcDaBaseException("移除任务组成员:" + userName + "有所属" + childTaskName + "子任务未完成，请先将其移除！");
                               throw new AdcDaBaseException( map.get(userId).getUsname() + "已有下级任务，请先将其移除下级任务");
                           }
                    }
                }

            }

        }

    }



    /**
     * 任务完成后，判断当前任务所属项目的其他任务是否完成，如果全部完成改变项目的完成状态
     *
     * @param projectId
     */
    private void checkProjectStatus(String projectId) {
        List<Task> taskList = taskRepository.findByProjectIdAndDelFlagNot(projectId, Boolean.TRUE);
        String projectStatus = ProjectStatusEnums.COMPLETE.getStatus();
        for (Task task : taskList) {
            if (ProjectStatusEnums.EXECUTE.getStatus().equals(task.getTaskStatus())) {
                projectStatus = ProjectStatusEnums.EXECUTE.getStatus();
                break;
            }
        }

        Project project = projectRepository.findOne(projectId);
        if (null != project && !Boolean.TRUE.equals(project.getDelFlag())) {
            project.setFinishedStatus(projectStatus);
            projectRepository.save(project);
        }
    }

    /**
     * add by liqiushi 20190422
     * 修改项目任务的上级项目时，判断任务中成员在原本的项目这一整个分支中是否还在别的节点具有角色，
     * 没有的话整个删除。给转到的项目这帮人加上关联关系
     */
    public void checkTaskSwitch(Task oldTask, Task newTask, String type) {
        BudgetEO oldBudget;
        BudgetEO newBudget;
        Project newProject = null;
        if (StringUtils.equals(type, "project")) {
            //项目任务，先去项目，再取业务
            Project oldProject = projectRepository.findByIdAndDelFlagNot(oldTask.getProjectId(), true);
            oldBudget = budgetEOService.getDao().selectByPrimaryKey(oldProject.getBudgetId());
            newProject = projectRepository.findByIdAndDelFlagNot(newTask.getProjectId(), true);
            newBudget = budgetEOService.getDao().selectByPrimaryKey(newProject.getBudgetId());
        } else {
            //业务任务直接取业务ID
            oldBudget = budgetEOService.getDao().selectByPrimaryKey(oldTask.getBudgetId());
            newBudget = budgetEOService.getDao().selectByPrimaryKey(newTask.getBudgetId());
        }
        List<Project> oldProjectList = projectRepository.findByBudgetId(oldBudget.getId());
        List<String> oldProjectIds = new ArrayList<>();
        List<Task> oldTaskList = new ArrayList<>(taskRepository.findByBudgetIdAndDelFlagNot(oldBudget.getId(), true));

        if (CollectionUtils.isNotEmpty(oldProjectList)) {
            for (Project projectBean : oldProjectList) {
                oldTaskList.addAll(taskRepository.findByProjectIdAndDelFlagNot(projectBean.getId(), true));
                oldProjectIds.add(projectBean.getId());
            }
        }


        /*
         * 获取用户，进行遍历
         */
        String[] userIds = oldTask.getMemberIds();

        if (CollectionUtils.isNotEmpty(userIds)) {
            for (String userId : userIds) {
                UserWithProjects userWithProjects = userWithProjectsRepository.findOne(userId);
                userWithProjects.getBusinessIds().remove(oldBudget.getId());
                if (StringUtils.equals(type, "project")) {
                    userWithProjects.getProjectIds().removeAll(oldProjectIds);
                }
                //还没保存呢昂
                if (StringUtils.equals(userId, oldBudget.getPm())
                    || StringUtils.equals(userId, oldBudget.getCreateUserId())) {
                    continue;
                }
                //判断这些任务成员是否还在老一套的其他项目里有角色
                if (CollectionUtils.isNotEmpty(oldProjectList)) {
                    for (Project project : oldProjectList) {
                        if (StringUtils.equals(project.getCreateUserId(), userId)
                            || StringUtils.equals(project.getProjectLeaderId(), userId)) {
                            userWithProjects.getBusinessIds().add(oldBudget.getId());
                            userWithProjects.getProjectIds().add(project.getId());
                        }
                    }
                }
                //判断目标任务中的成员是否在其他任务中占有角色
                if (CollectionUtils.isNotEmpty(oldTaskList)) {
                    for (Task task : oldTaskList) {
                        if (StringUtils.equals(task.getId(), oldTask.getId())) {
                            //排除目标任务，遍历其他任务
                            continue;
                        }
                        if (StringUtils.equals(task.getCreateUserId(), userId)
                            || (
                            CollectionUtils.isNotEmpty(task.getMemberIds())
                                && Arrays.asList(task.getMemberIds()).contains(userId))) {
                            //项目任务和业务任务通用
                            userWithProjects.getBusinessIds().add(oldBudget.getId());
                            userWithProjects.getProjectIds().add(task.getProjectId());
                            userWithProjects.getTaskIds().add(task.getId());
                        }
                    }
                }
                //给目标任务中成员添加转移之后的业务，项目关联关系
                if (StringUtils.equals(type, "project")) {
                    userWithProjects.getBusinessIds().add(newBudget.getId());
                    userWithProjects.getProjectIds().add(newProject.getId());
                } else {
                    userWithProjects.getBusinessIds().add(newBudget.getId());
                }
                userWithProjectsRepository.save(userWithProjects);
                //给目标任务中这几个人加上新转到的项目，业务的关联关系
            }
        }
        //给新转过去的项目和上级业务的负责人，创建人，项目组成员加上目标任务和子任务的关联关系
        Set<String> userIdSet = new HashSet<>();
        userIdSet.add(newBudget.getPm());
        userIdSet.add(newBudget.getCreateUserId());
        List<Project> newProjectList = projectRepository.findByBudgetId(newBudget.getId());
        if (CollectionUtils.isNotEmpty(newProjectList)) {
            //业务任务转换的时候直接过
            for (Project project : newProjectList) {
                userIdSet.add(project.getCreateUserId());
                userIdSet.add(project.getProjectLeaderId());
                if (CollectionUtils.isNotEmpty(project.getProjectMemberIds())) {
                    userIdSet.addAll(Arrays.asList(project.getProjectMemberIds()));
                }
            }
        }
        //目标任务的子任务
        List<ChildrenTask> newChildTaskList = childTaskRepository
            .findByTaskIdEqualsAndDelFlagNot(oldTask.getId(), true);
        List<String> oldChildTaskIds = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(newChildTaskList)) {
            for (ChildrenTask childrenTaskBean : newChildTaskList) {
                oldChildTaskIds.add(childrenTaskBean.getId());
            }
        }
        for (String userId : userIdSet) {
            if (StringUtils.isNotEmpty(userId)) {
                UserWithProjects userWithProjects1 = userWithProjectsRepository.findOne(userId);
                userWithProjects1.getTaskIds().add(oldTask.getId());
                userWithProjects1.getChildTaskIds().addAll(oldChildTaskIds);
                userWithProjectsRepository.save(userWithProjects1);
            }
        }
    }



    public Task updateWithoutShiroAuthentication(TaskVO taskVO) {
        try {
            //add 编辑的时候判断任务是否存在
            //permissionCheckUpdate(taskVO);
            /*
             * 需要判断人员信息是否有变化  如果有变化需要去比对增加的人或者修改的人  同步到用户关联表数据
             *
             * 如果任务的所属项目或者所属业务变更也需要同步
             *
             * 如果是业务任务，需要判断当前人是否是业务经理或者业务创建人
             * 只是成员需要判断当前业务下是否还有他的任务存在，如果没有移除当前业务在用户关联表
             */

            Task task = taskRepository.findOne(taskVO.getId());

//            List<String> deleteUserIdList = ArrayUtils.compare(taskVO.getMemberIds(),task.getMemberIds());
//
//            checkChildTaskStatus(task.getId() , deleteUserIdList);
            Task newTask = new Task();
            BeanUtils.copyPropertiesIgnoreNullValue(taskVO, newTask);
            Set<String> memberIdSet = new HashSet<>();
            HashMap<String,List<String>> DeptUserIdMap = new HashMap<>();
            List<Map<String,String>> deptInfoListMap = taskVO.getDeptInfoListMap();// 所选部门ID和类型 如类型=1 只选当前自身 类型=2 包含子部门
            for(Map<String,String> map :deptInfoListMap){
                int type =  Integer.parseInt(map.get("type").toString());
                String deptId =  map.get("deptId").toString();
                Set<String> temSet = new HashSet<>();
                if(type == 1){
                    List<String> deptList = new ArrayList<>();
                    deptList.add(deptId);
                    List<UserEO> list = userEOService.getAllUserEOsByOrgIds(deptList);//根据部门id查询成员
                    for(UserEO userEO:list){
                        memberIdSet.add(userEO.getUsid());
                        temSet.add(userEO.getUsid());
                    }
                    DeptUserIdMap.put(deptId,new ArrayList<>(temSet));
                }else if(type == 2){
                    List<UserEO> userlist = orgEOService.listUserEOByOrgId(deptId);//根据部门id查询成员
                    for(UserEO userEO:userlist){
                        memberIdSet.add(userEO.getUsid());
                        temSet.add(userEO.getUsid());
                    }
                    DeptUserIdMap.put(deptId,new ArrayList<>(temSet));
                }
            }
            newTask.setDeptIdUserIdList(DeptUserIdMap);
            List<String> tempIdList=new ArrayList<>(memberIdSet);
            String[] allIds = tempIdList.toArray(new String[0]);
            List<UserWithProjects> userWithProjectsList = new ArrayList<>();
            //添加对任务的操作记录
            //比对增加了哪些成员  同步到用户关联表
            List<String> ids = ArrayUtils.compare(task.getMemberIds(),allIds );
            if (CollectionUtils.isNotEmpty(ids)) {
                for (String id : ids) {
                    UserWithProjects userWithProjects = userWithProjectsRepository.findOne(id);
                    //如果是新用户，新增数据到用户关联
                    if (null == userWithProjects) {
                        userWithProjects = new UserWithProjects();
                        userWithProjects.setUserId(id);
                    }
                    userWithProjects.getTaskIds().add(task.getId());
                    if (StringUtils.isNotEmpty(newTask.getBudgetId())) {
                        userWithProjects.getBusinessIds().add(newTask.getBudgetId());
                    }
                    userWithProjectsList.add(userWithProjects);
                }
            }
            if (CollectionUtils.isNotEmpty(userWithProjectsList)) {
                userWithProjectsRepository.save(userWithProjectsList);  // 使用saveList 优化
            }
            // 判断有根据部门选人 或直接选人
            if (CollectionUtils.isNotEmpty(memberIdSet) || CollectionUtils.isNotEmpty(taskVO.getMemberIds())) {
                Set<String> taskMemberIdSet;
                if (CollectionUtils.isNotEmpty(taskVO.getMemberIds())) {
                    taskMemberIdSet = new HashSet<>(Arrays.asList(taskVO.getMemberIds()));
                }else {
                    taskMemberIdSet= new HashSet<>();
                }
                taskMemberIdSet.addAll(memberIdSet);
                List<String> taskMemberIdList = new ArrayList<>(taskMemberIdSet);
                newTask.setMemberIds(taskMemberIdSet.toArray(new String[taskMemberIdSet.size()]));
                List<UserEO> userEOList = userEODao.getUserWithRolesByUserIds(taskMemberIdList);
                List<OrgEO> orgEOList = orgEODao.queryOrgAll();
                String[] taskMemberNameArr = new String[userEOList.size()];
                for (int i = 0; i < userEOList.size(); i++) {
                    taskMemberNameArr[i] = userEOList.get(i).getUsname();
                    newTask.setMemberNames(taskMemberNameArr);
                    newTask.setMemberNameString(StringUtils.join(taskMemberNameArr,','));
                    newTask.setMapsList(CommonUtil.userIdUsnameMapKv(userEOList));
                    newTask.setUserIdDeptNameMapList(CommonUtil.userIdDeptNamesMapKv(userEOList,orgEOList)); //此处存在循环调用数据库
                }
            }
            //原项目ID
            //String budgetId = business.getBudgetId();
            newTask.setModifyTime(new Date());
            // 查询
            Task taskOri = taskRepository.findOne(taskVO.getId());
            String oldName = taskOri.getName();
            // 复制属性
            BeanUtils.copyPropertiesIgnoreNullValue(newTask, taskOri);
            //解决复制对象时null不会被复制问题
            taskOri.setBudgetId(taskVO.getBudgetId());
            if (StringUtils.isNotEmpty(taskVO.getBudgetId())) {
                taskOri.setSearchBudgetId(taskVO.getBudgetId());
            }
            Task bui = taskRepository.save(taskOri);
            //添加对任务的操作记录
            //比对删除了哪些成员  同步到用户关联表
            //  需要判断如果是业务任务  当前业务下是否还有参与的任务，如果没有 删除业务数据
            List<String> delIds = ArrayUtils.compare(taskVO.getMemberIds(), task.getMemberIds());

            userWithProjectsList = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(delIds)) {
                for (String id : delIds) {
                    if (StringUtils.equals(task.getCreateUserId(), id)) {
                        continue;
                    }
                    UserWithProjects userWithProjects = userWithProjectsRepository.findOne(id);
                    if(userWithProjects==null){
                        continue;
                    }
                    userWithProjects.getTaskIds().remove(task.getId());
                    if (StringUtils.isEmpty(task.getProjectId())) {
                        BudgetEO budgetEO = budgetEODao.selectByPrimaryKey(task.getBudgetId());
                        if (null!= budgetEO && !StringUtils.equals(id, budgetEO.getPm())
                                && !StringUtils.equals(id, budgetEO.getCreateUserId())) {
                            List<Task> tasks = taskRepository
                                    .findByBudgetIdAndMemberIdsInAndDelFlagNot(budgetEO.getId(), id, Boolean.TRUE);
                            if (CollectionUtils.isEmpty(tasks)) {
//                                userWithProjects = userWithProjectsRepository.findOne(id);
                                userWithProjects.getBusinessIds().remove(budgetEO.getId());
                            }
                        }
                    }else if (StringUtils.isNotEmpty(task.getProjectId())){
                        List<Task> tasks = taskRepository
                                .findByProjectIdAndMemberIdsInAndDelFlagNot(task.getProjectId(), id, Boolean.TRUE);
                        Project project = projectRepository.findById(task.getProjectId());
                        if (CollectionUtils.isNotEmpty(project.getProjectMemberIds())){
                            if (!Arrays.asList(project.getProjectMemberIds()).contains(id)&&CollectionUtils.isEmpty(tasks)){
                                userWithProjects.getProjectIds().remove(task.getProjectId());
                            }
                        }
                    }
                    userWithProjectsList.add(userWithProjects);
                }
                if (CollectionUtils.isNotEmpty(userWithProjectsList)){
                    userWithProjectsRepository.save(userWithProjectsList);
                }
            }
            taskPriorityRepository.save(new TaskPriority(task.getId(), task.getName(), 0, task.getMemberIds()));
            return bui;
        } catch (Exception e) {
            log.error("update budget/task error ", e);
            throw new AdcDaBaseException(e.getMessage());
        }
    }

    public boolean setProjectMemberUpdatingTime(String id, Date date) {
        Task task = taskRepository.findByIdAndDelFlagNot(id, Boolean.TRUE);
        if (task == null) {
            return false;
        }
        task.setProjectMemberUpdatingTime(date);
        taskRepository.save(task);
        return true;
    }

}
