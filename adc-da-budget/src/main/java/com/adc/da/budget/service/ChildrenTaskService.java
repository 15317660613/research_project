package com.adc.da.budget.service;

import com.adc.da.budget.constant.Role;
import com.adc.da.budget.dao.BudgetEODao;
import com.adc.da.budget.dao.TaskResultEODao;
import com.adc.da.budget.dao.UserEPDao;
import com.adc.da.budget.dao.UserProjectCustomDao;
import com.adc.da.budget.dto.PageDTO;
import com.adc.da.budget.entity.*;
import com.adc.da.budget.enums.ProjectStatusEnums;
import com.adc.da.budget.repository.*;
import com.adc.da.budget.utils.BeanUtils;
import com.adc.da.budget.utils.CommonUtil;
import com.adc.da.budget.vo.ChildrenTaskVO;
import com.adc.da.budget.vo.TaskResultVO;
import com.adc.da.excel.poi.excel.ExcelExportUtil;
import com.adc.da.excel.poi.excel.ExcelImportUtil;
import com.adc.da.excel.poi.excel.entity.ExportParams;
import com.adc.da.excel.poi.excel.entity.ImportParams;
import com.adc.da.file.service.FileEOService;
import com.adc.da.login.util.UserUtils;
import com.adc.da.superadmin.service.SuperAdminService;
import com.adc.da.sys.dao.OrgEODao;
import com.adc.da.sys.dao.UserEODao;
import com.adc.da.sys.entity.OrgEO;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.sys.service.OrgEOService;
import com.adc.da.sys.service.UserEOService;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.BeanMapper;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.StringUtils;
import com.adc.da.workflow.service.ActivitiProcessService;
import com.google.common.collect.Lists;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.*;


import static com.adc.da.budget.constant.ProjectSearchField.NAME;

/**
 * @Auther: ZHAIKAIXUAN
 * @Date: 2018/11/19 14:33
 * @Description:
 */
@Service
public class ChildrenTaskService extends BaseService<ChildrenTask, String> {

    private Logger logger = LoggerFactory.getLogger(ChildrenTaskService.class);

    @Autowired
    private ChildTaskRepository childTaskRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private BeanMapper beanMapper;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private UserEPDao userEPDao;
    @Autowired
    private UserEODao userEODao;
    @Autowired
    private OrgEODao orgEODao;


    @Autowired
    private DailyRepository dailyRepository;
    @Autowired
    private BudgetEODao budgetEODao;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private UserEOService userEOService;
    @Autowired
    private TaskPriorityRepository taskPriorityRepository;
    @Autowired
    private UserWithProjectsRepository userWithProjectsRepository;
    @Autowired
    private ChildrenTaskHistoryEOService childrenTaskHistoryEOService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private ActivitiProcessService activitiProcessService;
    @Autowired
    private TaskResultEODao taskResultEODao ;

    @Autowired
    private TaskResultEOService taskResultEOService ;

    @Autowired
    private OrgEOService orgEOService;
    @Autowired
    private SuperAdminService superAdminService;




    @Autowired
    private FileEOService fileEOService ;
    /**
     * @Description: 新增
     * @auther: ZHAIKAIXUAN
     * @params:
     * @return:
     * @date: 2018/11/19 14:37
     */
    public ChildrenTask insert(ChildrenTaskVO childrenTaskVO) throws Exception {
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        List<ChildrenTask> list = childTaskRepository.findByTaskIdAndDelFlagNot(childrenTaskVO.getTaskId(), true);
        String childTaskName = childrenTaskVO.getChildTaskName();
        for (ChildrenTask childrenTask : list) {
            if (childTaskName != null && childTaskName.equals(childrenTask.getChildTaskName())) {
                throw new AdcDaBaseException("新增下级任务时，所属任务相同情况下，任务名称不允许相同");
            }
        }

        Task parentTask = taskRepository.findOne(childrenTaskVO.getTaskId());
        Project project = null;
        if (StringUtils.isNotEmpty(parentTask.getProjectId())) {
            project = projectRepository.findOne(parentTask.getProjectId());
        }
        if (!StringUtils.equals(userId,parentTask.getBusinessAdminId())
            && !StringUtils.equals(userId,parentTask.getProjectAdminId())
            && !StringUtils.equals(userId,parentTask.getProjectLeaderId())
            && !StringUtils.equals(userId,parentTask.getPm())
                &&!superAdminService.isSuperAdmin()
            && CommonUtil.arrayContains(parentTask.getMemberIds(),userId)<0){
            throw new AdcDaBaseException("您无权创建子任务 ！");
        }
        // 新建子任务的时候需要将对应的已完成的 任务状态改成未完成
        //2020-12-01 什么鬼？ 项目都完成了 还让人建子任务？？？
//        if (StringUtils.equals(ProjectStatusEnums.COMPLETE.getStatus(), parentTask.getTaskStatus())
//                && StringUtils.isNotBlank(parentTask.getProjectId())) {
//            taskRepository.delete(childrenTaskVO.getTaskId());
//            parentTask.setTaskStatus(ProjectStatusEnums.EXECUTE.getStatus());
//            parentTask.setModifyTime(new Date());
//            taskRepository.save(parentTask);
//            if (null != project) {
//                // 项目完成状态
//                String projectStatus = ProjectStatusEnums.COMPLETE.getStatus();
//                List<Task> taskList = taskRepository.findByProjectIdEquals(parentTask.getProjectId());
//                for (Task task : taskList) {
//                    if (StringUtils.equals(ProjectStatusEnums.EXECUTE.getStatus(), task.getTaskStatus())) {
//                        projectStatus = ProjectStatusEnums.EXECUTE.getStatus();
//                        break;
//                    }
//                }
//                if (!project.getFinishedStatus().equals(projectStatus)) {
//                    project.setFinishedStatus(projectStatus);
//                    project.setModifyTime(new Date());
//                    projectRepository.save(project);
//                }
//            }
//        }

        if (StringUtils.equals(ProjectStatusEnums.COMPLETE.getStatus(), parentTask.getTaskStatus())) {
            throw new AdcDaBaseException("任务已关闭或过期，创建子任务失败！");
        }
        ChildrenTask childrenTask = new ChildrenTask();
        BeanUtils.copyPropertiesIgnoreNullValue(childrenTaskVO, childrenTask);
        // 查询参与人员 //这段应该不影响，下面有逻辑修改memberIds
        if(StringUtils.isNotEmpty(childrenTaskVO.getPersonId())) {
            UserEPEntity userEPEntity = new UserEPEntity();
            userEPEntity.setUsid(childrenTaskVO.getPersonId());
            UserEPEntity userEPEntities = userEPDao.queryUserByQuery(userEPEntity).get(0);
            // 修改子任务
            childrenTask.setPersonName(userEPEntities.getUsname());
            childrenTask.setCreateUserId(userId);
            childrenTask.setCreateTime(new Date());
        }
        // 修改项目状态未完成
        HashMap<String,List<String>> DeptUserIdMap = new HashMap<>();
        Set<String> memberIdSet = new HashSet<>();
        List<Map<String,String>> deptInfoListMap = childrenTaskVO.getDeptInfoListMap();// 所选部门ID和类型 如类型=1 只选当前自身 类型=2 包含子部门
        for(Map<String,String> map :deptInfoListMap){
            int type =  Integer.parseInt(map.get("type").toString());
            String deptId =  map.get("deptId").toString();
            if(type == 1){
                List<String> deptList = new ArrayList<>();
                deptList.add(deptId);
                List<UserEO> userlist = userEOService.getAllUserEOsByOrgIds(deptList);//根据部门id查询成员
                Set<String> tempSet = new HashSet<>();
                for(UserEO userEO:userlist){
                    memberIdSet.add(userEO.getUsid());
                    tempSet.add(userEO.getUsid());
                }
                DeptUserIdMap.put(deptId,new ArrayList<>(tempSet));
            }else if(type == 2){
                List<UserEO> userlist = orgEOService.listUserEOByOrgId(deptId);//根据部门id查询成员
                Set<String> tempSet = new HashSet<>();
                for(UserEO userEO:userlist){
                    memberIdSet.add(userEO.getUsid());
                    tempSet.add(userEO.getUsid());
                }
                DeptUserIdMap.put(deptId,new ArrayList<>(tempSet));
            }
        }
        childrenTask.setDeptIdUserIdList(DeptUserIdMap);
        List<OrgEO> orgEOList = orgEODao.queryOrgAll();
        String[] memberIds = childrenTask.getMemberIds();
        if(StringUtils.isEmpty(memberIds) && StringUtils.isEmpty(memberIdSet)){
            throw new AdcDaBaseException("未选择成员或选择的组织机构下无成员！");
        }
        if (CollectionUtils.isNotEmpty(memberIdSet)||CollectionUtils.isNotEmpty(memberIds)||StringUtils.isNotEmpty(childrenTask.getPersonId())) {
            Set<String> taskMemberIdSet;
            if (CollectionUtils.isNotEmpty(memberIds)) {
                taskMemberIdSet = new HashSet<>(Arrays.asList(memberIds));
            }else {
                taskMemberIdSet= new HashSet<>();
            }
            if (StringUtils.isNotEmpty(childrenTask.getPersonId())&&!taskMemberIdSet.contains(childrenTask.getPersonId())){
                taskMemberIdSet.add(childrenTask.getPersonId());
            }
            taskMemberIdSet.addAll(memberIdSet);
            List<String> taskMemberIdList = new ArrayList<>(taskMemberIdSet);
            childrenTask.setMemberIds(taskMemberIdSet.toArray(new String[taskMemberIdSet.size()]));
            List<UserEO> userEOList = userEODao.getUserWithRolesByUserIds(taskMemberIdList);
            String[] taskMemberNameArr = new String[userEOList.size()];
            memberIds = new String[userEOList.size()];
            for (int i = 0 ; i < userEOList.size(); i ++){
                taskMemberNameArr[i] = userEOList.get(i).getUsname();
                memberIds[i] = userEOList.get(i).getUsid();
            }
            childrenTask.setMemberIds(memberIds);
            childrenTask.setMemberNames(taskMemberNameArr);
            childrenTask.setMemberNameString(StringUtils.join(taskMemberNameArr,','));
            childrenTask.setMapsList(CommonUtil.userIdUsnameMapKv(userEOList));
            childrenTask.setUserIdDeptNameMapList(CommonUtil.userIdDeptNamesMapKv(userEOList,orgEOList));
        }
        childrenTask.setStatus(ProjectStatusEnums.EXECUTE.getStatus());
        //根据任务的id查询任务名
        //Task belongTask = taskRepository.findOne(task.getTaskId());
        childrenTask.setBelongTaskName(parentTask.getName());
        //设置项目负责人和部门Id
        childrenTask.setProjectLeaderId(parentTask.getProjectLeaderId());
        childrenTask.setDeptId(parentTask.getDeptId());
        childrenTask.setId(UUID.randomUUID().toString());
//        Project project;
        if (StringUtils.isNotBlank(parentTask.getBudgetId())) {
            BudgetEO  budgetEO = budgetEODao.selectByPrimaryKey(parentTask.getBudgetId());
            childrenTask.setPm(budgetEO.getPm());
            childrenTask.setProjectTeam(budgetEO.getProjectTeam());
        } else {
//            project = projectRepository.findOne(belongTask.getProjectId());
            childrenTask.setPm(project.getPm());
            childrenTask.setProjectTeam(project.getProjectTeam());
        }
        //新增的时候保存任务优先级数据
//        String[] ids = {task.getPersonId()};
        String[] ids = childrenTask.getMemberIds(); //走到这里，memberIds肯定不为空
        taskPriorityRepository.save(new TaskPriority(childrenTask.getId(), childrenTask.getChildTaskName(), 0, ids));
        //新增的时候保存子任务ID到用户业务树关联表 子任务创建人、任务创建人、项目经理、业务经理、业务创建人 都需要存储子任务ID到用户业务树关联表
        Set<String> userIds = getUserIds(childrenTask);
        setUserWithProjectsData(userWithProjectsRepository,
                UserWithProjects.class.getMethod("getChildTaskIds"), userIds, true, childrenTask.getId());
        //add by liqiushi 20190417 新增子任务时记录新增动作

        if (CollectionUtils.isNotEmpty(childrenTaskVO.getTaskResultEOList())) {
            taskResultEOService.updateList(childrenTaskVO.getTaskResultEOList(),childrenTask.getId());
        }

//        task.setTaskResultEOList(null);
        return childTaskRepository.save(childrenTask);

    }

    /**
     * @Description: 根据id删除 可以是ids
     * @auther: ZHAIKAIXUAN
     * @params:
     * @return:
     * @date: 2018/11/19 14:44
     */
    public String deleteBatch(String ids) throws NoSuchMethodException {
        if (StringUtils.isBlank(ids)) {
            throw new AdcDaBaseException("删除失败，请选择删除的任务");
        }

        boolean mananger = false;

        //管理员
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        Subject subject = SecurityUtils.getSubject();
        if (subject.hasRole(Role.SUPER_ADMIN) || subject.hasRole(Role.PROJECT_ADMIN) || subject.hasRole(Role.ZHU_REN)) {
            mananger = true;
        }
        String[] idArray = ids.split(",");
        if (!CollectionUtils.isEmpty(idArray)) {
            Task task;
            Project project = new Project();
            BudgetEO budgetEO;
            for (String id : idArray) {
                ChildrenTask childrenTask = childTaskRepository.findOne(id);
                if (null == childrenTask) {
                    continue;
                }
                BoolQueryBuilder boolQueryBuilder2 = QueryBuilders.boolQuery();
                boolQueryBuilder2.should(QueryBuilders.termQuery("taskIdArray", id))
                                 .mustNot(QueryBuilders.termQuery("delFlag", true));
                Iterator<Daily> taskIterator = dailyRepository.search(boolQueryBuilder2).iterator();
                if (taskIterator.hasNext()) {
                    throw new AdcDaBaseException("子任务下存在日报，无法删除");
                }
                boolean delFlag = false;
                //判断删除权限
                task = taskRepository.findOne(childrenTask.getTaskId());
                if (StringUtils.isBlank(task.getBudgetId())) {
                    project = projectRepository.findOne(task.getProjectId());
                    budgetEO = budgetEODao.selectByPrimaryKey(project.getBudgetId());

                } else {
                    budgetEO = budgetEODao.selectByPrimaryKey(task.getBudgetId());
                }
                if (mananger || userId.equals(childrenTask.getCreateUserId())
                        || userId.equals(budgetEO.getPm())
                        || userId.equals(budgetEO.getCreateUserId())
                        || userId.equals(project.getProjectLeaderId())
                        || userId.equals(task.getApproveUserId())
                        || userId.equals(budgetEO.getBusinessAdminId())
                        || userId.equals(project.getProjectAdminId())) {
                    delFlag = true;
                } else {
                    //当前子任务部门的部长
                    UserEO userEO = userEOService.getUserWithRoles(userId);
                    if (StringUtils.isNotEmpty(userEO.getOrgEOList().size())) {
                        if (subject.hasRole(Role.BU_ZHANG) && userEO.getOrgEOList().get(0).getId().equals(childrenTask.getDeptId())) {
                            delFlag = true;
                        }
                    }
                }
                if (subject.hasRole(Role.ZU_ZHANG)) {
                    UserEO userEO = userEOService.getUserWithRoles(userId);
                    String orgId = userEO.getOrgEOList().get(0).getId();
                    if (StringUtils.equals(orgId, childrenTask.getProjectTeam())) {
                        delFlag = true;
                    }
                }
                if (delFlag) {
                    BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery().must(QueryBuilders.termQuery("taskIdArray", id));
                    Iterable<Daily> iterable = dailyRepository.search(queryBuilder);
                    Iterator<Daily> iterator = iterable.iterator();
                    while (iterator.hasNext()) {
                        Daily daily = iterator.next();
                        daily.setDelFlag(true);
                        dailyRepository.save(daily);
                    }
                    childrenTask.setDelFlag(true);
                    //删除子任务时同时删除任务优先级数据
                    taskPriorityRepository.delete(childrenTask.getId());

                    //删除子任务同时删除用户业务树关联中的子任务ID
                    Set<String> userIds = getUserIds(childrenTask);
                    setUserWithProjectsData(userWithProjectsRepository,
                            UserWithProjects.class.getMethod("getChildTaskIds"), userIds, false, childrenTask.getId());

                    childTaskRepository.save(childrenTask);
                    return "删除成功!";
                }

            }
        }
        throw new AdcDaBaseException("您无权删除！");
    }

    private void updateUserWithChildrenTask(String personId,Boolean removeFlag,String childTaskId){
        UserWithProjects userWithProjects = userWithProjectsRepository.findOne(personId);
        if(removeFlag){
            userWithProjects.getChildTaskIds().remove(childTaskId);
        } else {
            userWithProjects.getChildTaskIds().add(childTaskId);
        }
        userWithProjectsRepository.save(userWithProjects);
    }

    private void updateUserWithChildTask(ChildrenTask childrenTaskOri,ChildrenTaskVO childrenTaskVO){
        Set<String> memberIdSet = new HashSet<>();
        if (CollectionUtils.isNotEmpty(childrenTaskOri.getMemberIds())) {
             memberIdSet = new HashSet<>(Arrays.asList(childrenTaskOri.getMemberIds()));
        }
        if(StringUtils.isEmpty(childrenTaskOri.getProjectLeaderId())){
            if(!StringUtils.equals(childrenTaskOri.getPersonId(),childrenTaskOri.getPm())){
                updateUserWithChildrenTask(childrenTaskOri.getPersonId(),true,childrenTaskOri.getId());
            }
            //保存新的数据
            if(!StringUtils.equals(childrenTaskVO.getPersonId(),childrenTaskOri.getPm())){
                updateUserWithChildrenTask(childrenTaskVO.getPersonId(),false,childrenTaskVO.getId());
            }
        }else {
            if(!StringUtils.equals(childrenTaskOri.getPersonId(),childrenTaskOri.getProjectLeaderId())){
                updateUserWithChildrenTask(childrenTaskOri.getPersonId(),true,childrenTaskOri.getId());
            }
            //保存新的数据
            if(!StringUtils.equals(childrenTaskVO.getPersonId(),childrenTaskOri.getProjectLeaderId())){
                updateUserWithChildrenTask(childrenTaskVO.getPersonId(),false,childrenTaskVO.getId());
            }
        }
    }

    private void deleteUserWithProject(ChildrenTask childrenTaskOri){
        Set<String> memberIdSet = new HashSet<>();
        if (CollectionUtils.isNotEmpty(childrenTaskOri.getMemberIds())) {
            memberIdSet = new HashSet<>(Arrays.asList(childrenTaskOri.getMemberIds()));
        }
        if (StringUtils.isNotEmpty(childrenTaskOri.getPersonId())){
            memberIdSet.add(childrenTaskOri.getPersonId());
        }
        if(CollectionUtils.isEmpty(memberIdSet)){
            return;
        }
        Task task = taskRepository.findById(childrenTaskOri.getTaskId());
        List<UserWithProjects> userWithProjectsList = userWithProjectsRepository.findByUserIdIn(memberIdSet);
        for (UserWithProjects userWithProjects : userWithProjectsList){
            String userId = userWithProjects.getUserId();
            //如果是项目经理，或者任务负责人 马么就不清除子任务关系
            if (StringUtils.equals(userId,childrenTaskOri.getPm())
                    || StringUtils.equals(userId, childrenTaskOri.getProjectLeaderId())
                    || StringUtils.equals(userId, task.getApproveUserId())) {
                continue;
            }else {
                userWithProjects.getChildTaskIds().remove(childrenTaskOri.getId());
            }
        }
        userWithProjectsRepository.save(userWithProjectsList);
    }
    //重构关系 因为是set 直接全塞进去不影响最终业务树
    private void rebuildUserWithProject(ChildrenTask childrenTaskOri){
        Set<String> memberIdSet = new HashSet<>();
        if (CollectionUtils.isNotEmpty(childrenTaskOri.getMemberIds())) {
            memberIdSet = new HashSet<>(Arrays.asList(childrenTaskOri.getMemberIds()));
        }
        if (StringUtils.isNotEmpty(childrenTaskOri.getPersonId())){
            memberIdSet.add(childrenTaskOri.getPersonId());
        }
        if (CollectionUtils.isNotEmpty(memberIdSet)) {
            List<UserWithProjects> userWithProjectsList = userWithProjectsRepository.findByUserIdIn(memberIdSet);
            for (UserWithProjects userWithProjects : userWithProjectsList) {
                userWithProjects.getBusinessIds().add(childrenTaskOri.getBudgetId());
                userWithProjects.getProjectIds().add(childrenTaskOri.getProjectId());
                userWithProjects.getTaskIds().add(childrenTaskOri.getTaskId());
                userWithProjects.getChildTaskIds().add(childrenTaskOri.getId());
            }
            userWithProjectsRepository.save(userWithProjectsList);
        }
    }


    /**
     * @Description: 修改
     * @auther: ZHAIKAIXUAN
     * @params:
     * @return:
     * @date: 2018/11/19 14:44
     */

    public ChildrenTask update(ChildrenTaskVO childrenTaskVO)throws Exception {
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        List<ChildrenTask> list = childTaskRepository.findByTaskIdAndDelFlagNot(childrenTaskVO.getTaskId(), true);
        String childTaskName = childrenTaskVO.getChildTaskName();
        String id = childrenTaskVO.getId();
        for (ChildrenTask childrenTask : list) {
            if (childTaskName != null && childTaskName.equals(childrenTask.getChildTaskName()) && !id.equals(childrenTask.getId())) {
                throw new AdcDaBaseException("修改下级任务时，所属任务相同情况下，任务名称不允许相同");
            }
        }
        // 查询出子任务
        ChildrenTask childrenTaskOri = childTaskRepository.findOne(childrenTaskVO.getId());
        String oldBelongTaskId = childrenTaskOri.getTaskId();
        Set<String> memberIdSet = new HashSet<>();
        List<Map<String,String>> deptInfoListMap = childrenTaskVO.getDeptInfoListMap();// 所选部门ID和类型 如类型=1 只选当前自身 类型=2 包含子部门
        HashMap<String,List<String>> DeptUserIdMap = new HashMap<>();
        for(Map<String,String> map :deptInfoListMap){
            int type =  Integer.parseInt(map.get("type").toString());
            String deptId =  map.get("deptId").toString();
            Set<String> temSet = new HashSet<>();
            if(type == 1){
                List<String> deptList = new ArrayList<>();
                deptList.add(deptId);
                List<UserEO> userlist = userEOService.getAllUserEOsByOrgIds(deptList);//根据部门id查询成员
                for(UserEO userEO:userlist){
                    memberIdSet.add(userEO.getUsid());
                    temSet.add(userEO.getUsid());
                }
                DeptUserIdMap.put(deptId,new ArrayList<>(temSet));
                /// pro.setDeptIdUserIdList(DeptUserIdMap);

            }else if(type == 2){
                List<UserEO> userlist = orgEOService.listUserEOByOrgId(deptId);//根据部门id查询成员
                for(UserEO userEO:userlist){
                    memberIdSet.add(userEO.getUsid());
                    temSet.add(userEO.getUsid());
                }
                DeptUserIdMap.put(deptId,new ArrayList<>(temSet));
                //pro.setDeptIdUserIdList(DeptUserIdMap);
            }
        }
        childrenTaskVO.setDeptIdUserIdList(DeptUserIdMap);
        deleteUserWithProject(childrenTaskOri); //把原来的关系干掉
        // 转换子任务
        BeanUtils.copyPropertiesIgnoreNullValue(childrenTaskVO, childrenTaskOri);
        if (CollectionUtils.isEmpty(childrenTaskVO.getMemberIds())){
            childrenTaskOri.setMemberIds(null);
        }
        if(CollectionUtils.isEmpty(childrenTaskVO.getMemberIds()) && CollectionUtils.isEmpty(memberIdSet)){
//            throw new AdcDaBaseException("未选择成员或选择的组织机构下无成员！");
            childrenTaskOri.setMapsList(null);
            childrenTaskOri.setMemberNames(null);
            childrenTaskOri.setMemberNameString(null);
            childrenTaskOri.setPersonName(null);
            //return childTaskRepository.save(childrenTaskOri);
        }
        // 根据任务的ＩＤ查询任务
        Task parentTask = taskRepository.findOne(childrenTaskVO.getTaskId());
        childrenTaskOri.setBelongTaskName(parentTask.getName());
        childrenTaskOri.setModifyTime(new Date());
        childrenTaskOri.setCreateTime(childrenTaskOri.getCreateTime());
        // 查询子任务中的所属人员
        // todo下面重新查了一下 这里就冗余吧，等到子任务全部更新后，在注diao
        if(StringUtils.isNotEmpty(childrenTaskVO.getPersonId())) {
            UserEPEntity userEPEntity = new UserEPEntity();
            userEPEntity.setUsid(childrenTaskVO.getPersonId());
            UserEPEntity userEPEntities = userEPDao.queryUserByQuery(userEPEntity).get(0);
            // 修改子任务
            childrenTaskOri.setPersonName(userEPEntities.getUsname());
        }
        childrenTaskOri.setProjectLeaderId(parentTask.getProjectLeaderId());
        childrenTaskOri.setDeptId(parentTask.getDeptId());

        String[] memberIds = childrenTaskVO.getMemberIds();
        // 判断有根据部门选人 或直接选人
        if (CollectionUtils.isNotEmpty(memberIdSet)||CollectionUtils.isNotEmpty(memberIds)||StringUtils.isNotEmpty(childrenTaskVO.getPersonId())) {
            Set<String> taskMemberIdSet;
            if (CollectionUtils.isNotEmpty(memberIds)) {
                taskMemberIdSet = new HashSet<>(Arrays.asList(memberIds));
            }else {
                taskMemberIdSet= new HashSet<>();
            }
            if (StringUtils.isNotEmpty(childrenTaskVO.getPersonId())&&!taskMemberIdSet.contains(childrenTaskVO.getPersonId())){
                taskMemberIdSet.add(childrenTaskVO.getPersonId());
            }
            taskMemberIdSet.addAll(memberIdSet);
            childrenTaskOri.setMemberIds(taskMemberIdSet.toArray(new String[taskMemberIdSet.size()]));
            List<OrgEO> orgEOList = orgEODao.queryOrgAll();
            List<String> taskMemberIdList = new ArrayList<>(taskMemberIdSet);
            List<UserEO> userEOList = userEODao.getUserWithRolesByUserIds(new ArrayList<String>(taskMemberIdList));
            String[] taskMemberNameArr = new String[userEOList.size()];
            memberIds = new String[userEOList.size()];
            for (int i = 0 ; i < userEOList.size(); i ++){
                taskMemberNameArr[i] = userEOList.get(i).getUsname();
                memberIds[i] = userEOList.get(i).getUsid();

            }
            childrenTaskOri.setMemberNames(taskMemberNameArr);
            childrenTaskOri.setMemberNameString(StringUtils.join(taskMemberNameArr,','));
            childrenTaskOri.setMapsList(CommonUtil.userIdUsnameMapKv(userEOList));
            childrenTaskOri.setUserIdDeptNameMapList(CommonUtil.userIdDeptNamesMapKv(userEOList,orgEOList)); //此处存在循环调用数据库
        }
        rebuildUserWithProject(childrenTaskOri); //重新构建关系
        //add by 丁强
        if (CollectionUtils.isNotEmpty(childrenTaskVO.getTaskResultEOList())) {
            taskResultEOService.updateList(childrenTaskVO.getTaskResultEOList() ,childrenTaskVO.getId());

            //同时修改数据库 PF_TASK_RESULT表
            String taskId=childrenTaskOri.getId();
            deleteTaskResultByTaskId(taskId);
            List<TaskResultEO> taskResultEOList=childrenTaskVO.getTaskResultEOList();
            if(CollectionUtils.isNotEmpty(taskResultEOList)){
                for(TaskResultEO taskResultEO: taskResultEOList){
                    //设置ID
                    String taskResultId=taskResultEO.getId();
                    if(StringUtils.isEmpty(taskResultId)){
                        taskResultId=com.adc.da.util.utils.UUID.randomUUID10();
                    }
                    taskResultEO.setId(taskResultId);
                    taskResultEODao.insert(taskResultEO);
                }
            }
        }else {
            childrenTaskOri.setTaskResultEOList(null);

            //同时修改数据库 PF_TASK_RESULT表
            String taskId=childrenTaskOri.getId();
             deleteTaskResultByTaskId(taskId);
        }

        childrenTaskOri.setProjectMemberUpdatingTime(null);
        // 保存
        ChildrenTask bui = childTaskRepository.save(childrenTaskOri);
        //记录对子任务的更新操作
        String taskId = childrenTaskOri.getTaskId();
        //检查任务的完成状态修改
        if (ProjectStatusEnums.COMPLETE.getStatus().equals(bui.getStatus())) {
            checkTaskStatus(taskId);
        }
        //判断是否变更子任务所属
        if (!StringUtils.equals(bui.getTaskId(), oldBelongTaskId)) {
            checkBelongTaskId(bui);
        }
        return bui;
    }

    //根据taskId 删除 PF_TASK_RESULT中的记录
    void deleteTaskResultByTaskId(String taskId){
        List<TaskResultEO> taskResultEOList=taskResultEODao.selectByTaskId(taskId);
        if(CollectionUtils.isNotEmpty(taskResultEOList)){
            List<String> taskResultIdList=new ArrayList<>();
            for(TaskResultEO taskResultEO: taskResultEOList){
                String taskResultId=taskResultEO.getId();
                taskResultIdList.add(taskResultId);
                //taskResultEODao.setNullResultNameById(taskResultId);
                taskResultEODao.deleteByPrimaryKeys(taskResultIdList);
            }
        }

    }

    /**
     * 更换所属任务
     * @param childrenTask
     */
    private void checkBelongTaskId(ChildrenTask childrenTask ) {
        if (CollectionUtils.isEmpty(childrenTask.getMemberIds())&&StringUtils.isEmpty(childrenTask.getPersonId())) {
            return;
        }
        if (CollectionUtils.isEmpty(childrenTask.getMemberIds())) {
            UserWithProjects userWithProjects = userWithProjectsRepository.findOne(childrenTask.getPersonId());
            userWithProjects.getTaskIds().add(childrenTask.getTaskId());
            userWithProjectsRepository.save(userWithProjects);
        }else {
            List<UserWithProjects> userWithProjectsList = userWithProjectsRepository.findByUserIdIn(childrenTask.getMemberIds());
            for (UserWithProjects userWithProjects : userWithProjectsList) {
                userWithProjects.getTaskIds().add(childrenTask.getTaskId());
            }
            userWithProjectsRepository.save(userWithProjectsList);
        }
    }



    public boolean setStatus(String id, boolean status) {
        ChildrenTask childrenTask = childTaskRepository.findByIdAndDelFlagNot(id, Boolean.TRUE);
        if (childrenTask == null) {
            return false;
        }
        if (status) {
            childrenTask.setStatus(ProjectStatusEnums.COMPLETE.getStatus());
            childrenTask.setActualFinishedTime(new Date());
        } else {
            childrenTask.setStatus(ProjectStatusEnums.EXECUTE.getStatus());
            childrenTask.setActualFinishedTime(null);
        }
        childTaskRepository.save(childrenTask);
        return true;
    }



    /**
     * @Description: 查找
     * @auther: ZHAIKAIXUAN
     * @params:
     * @return:
     * @date: 2018/11/19 14:45
     */
    public ChildrenTask querySingle(String id) throws Exception {
        ChildrenTask childrenTask = childTaskRepository.findByIdAndDelFlagNot(id, Boolean.TRUE);
        if (null == childrenTask) {
            throw new AdcDaBaseException("下级任务不存在");
        }
        Task taskVO = taskService.querySingle(childrenTask.getTaskId());
        childrenTask.setManager(taskVO.getManager());
        childrenTask.setBudgetId(taskVO.getBudgetId());
        String[] memeberIds = taskVO.getMemberIds();
        if(StringUtils.isNotEmpty(taskVO.getProjectId())) {
            Project project = projectRepository.findByIdAndDelFlagNot(taskVO.getProjectId(), true);
            childrenTask.setProjectName(project.getName());
            //项目经理审批
            childrenTask.setApproveUserId(project.getProjectLeaderId());
            taskVO.setProjectAdminId(project.getProjectAdminId());
        }
        if(StringUtils.isNotEmpty(taskVO.getBudgetId())) {
            BudgetEO budgetEO = budgetEODao.selectByPrimaryKey(taskVO.getBudgetId());
            childrenTask.setBudgetName(budgetEO.getProjectName());
            childrenTask.setApproveUserId(budgetEO.getPm());
            taskVO.setPm(budgetEO.getPm());
            taskVO.setBusinessAdminId(budgetEO.getBusinessAdminId());
        }
        if (StringUtils.isNotEmpty(childrenTask.getPersonId())) {
            childrenTask.setPersonName(userEPDao.queryUserById(childrenTask.getPersonId()).getUsname());
        }

        if (CollectionUtils.isNotEmpty(memeberIds)) {
            if ((Arrays.asList(memeberIds).contains(UserUtils.getUserId())
                    || StringUtils.equals(UserUtils.getUserId(), taskVO.getApproveUserId())
                    || StringUtils.equals(UserUtils.getUserId(), taskVO.getProjectAdminId())
                    || StringUtils.equals(UserUtils.getUserId(), taskVO.getProjectLeaderId())
                    || StringUtils.equals(taskVO.getBusinessAdminId(), UserUtils.getUserId())
                    || StringUtils.equals(taskVO.getPm(), UserUtils.getUserId())
                    || StringUtils.equals(UserUtils.getUserId(), taskVO.getApproveUserId())
                    || StringUtils.equals(UserUtils.getUserId(), taskVO.getProjectAdminId())
                    || StringUtils.equals(UserUtils.getUserId(), taskVO.getProjectLeaderId())
            ) && StringUtils.equals(childrenTask.getStatus(), ProjectStatusEnums.EXECUTE.getStatus()) &&
                    StringUtils.isEmpty(childrenTask.getBtnFlag())) {
                childrenTask.setManager(true);
                childrenTask.setBtnFlag("0");
            }
        }


        List<TaskResultEO> taskResultEOList = taskResultEODao.selectByTaskId(childrenTask.getId());
        childrenTask.setTaskResultEOList(taskResultEOList);

//        ChildrenTaskWithResult childrenTaskWithResult =  beanMapper.map(childrenTask,ChildrenTaskWithResult.class);
//
//        List<com.adc.da.file.entity.FileEO> retFileList = new ArrayList<>() ;
//        for (TaskResultEO eo : taskResultEOList) {
//            List<com.adc.da.file.entity.FileEO> fileEOList  =   fileEOService.selectByForeignId(eo.getId());
//            if (CollectionUtils.isNotEmpty(fileEOList)){
//                FileEO fileEO = fileEOList.get(0);
//                retFileList.add(fileEO);
//            }
//        }
//        retFileList.addAll(fileEOService.selectByForeignId(id));
//
//        childrenTaskWithResult.setFileEOList(retFileList);

        return childrenTask;
    }

    /**
     * 导入
     *
     * @param is
     * @param params
     * @throws Exception
     */
    public void excelImport(InputStream is, ImportParams params) throws Exception {
        List<ChildrenTask> datas = ExcelImportUtil.importExcel(is, ChildrenTask.class, params);
        for (ChildrenTask childrenTask : datas) {
            //去数据库校验人员信息是否存在
            String[] array = null;
            if (null != childrenTask.getPersonName()) {
                array = childrenTask.getPersonName().split("，"); }
            List<UserEPEntity> userEPEntities = userEPDao.checkUserExist(array);
            if (StringUtils.isNotEmpty(userEPEntities)) {  //这个地方没用过吧 怎么能用StringUtils判空？
                childrenTask.setPersonId(userEPEntities.get(0).getUsid());
                List<Task> task = taskRepository.findByNameEquals(childrenTask.getBelongTaskName());
                if (null != task) {
                    childrenTask.setTaskId(task.get(0).getId());
                    //如果导入的子任务是未完成状态，修改所属任务的状态为未完成并清除实际完成时间。
                    if (ProjectStatusEnums.EXECUTE.getStatus().equals(childrenTask.getStatus())) {
                        task.get(0).setFinishedActualTime(null);
                        task.get(0).setTaskStatus(childrenTask.getStatus());
                        taskRepository.save(task.get(0));
                    }
                }
                if (StringUtils.isEmpty(childrenTask.getId())) {
                    childrenTask.setId(UUID.randomUUID().toString());
                }
                if (StringUtils.isEmpty(childrenTask.getCreateTime())) {
                    childrenTask.setCreateTime(new Date(System.currentTimeMillis()));
                }

                childTaskRepository.save(childrenTask);
            } else {
                logger.info("===============  人员名称不合法 :   ======================");
                throw new Exception("人员名称不合法");
            }

        }

    }


    public Workbook excelExport(ExportParams params, int type) {
        return ExcelExportUtil.exportExcel(params, ChildrenTask.class, queryByPage(1, 10000, null, type).getDataList());
    }


    public void deleteAll() {
        childTaskRepository.deleteAll();
    }

    public List<ChildrenTask> queryAll() {
        //获取当前用户部门
        List<String> deptList = projectService.getDeptIds();
        if (deptList == null) {
            throw new IllegalArgumentException();
        }

        //当前用户的id
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }

        //如果当前用户是部长可以看见本部门所有任务
        Subject subject = SecurityUtils.getSubject();
        if (subject.hasRole(Role.SUPER_ADMIN) || subject.hasRole(Role.ADMIN) || subject.hasRole(Role.ZHU_REN)) {
            ArrayList<ChildrenTask> childrenTasks = Lists.newArrayList(childTaskRepository.findAll());
            for (ChildrenTask childrenTask : childrenTasks) {
                Task task = taskRepository.findOne(childrenTask.getTaskId());
                childrenTask.setStatus(ProjectStatusEnums.EXECUTE.getStatus());
                if (null != task.getBudgetId()) {
                    BudgetEO budgetEO = budgetEODao.selectByPrimaryKey(task.getBudgetId());
                    if (null != budgetEO) {
                        childrenTask.setPm(budgetEO.getPm());
                    }
                }
                childTaskRepository.save(childrenTask);
            }
            return childrenTasks;
        }
        if (subject.hasRole(Role.BU_ZHANG)) {
            BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
                    .mustNot(QueryBuilders.termQuery("delFlag", true));
            for (String deptId : deptList) {
                queryBuilder.should(QueryBuilders.termQuery("deptId", deptId));
            }
            //当前部门下的所有任务任务
            ArrayList<ChildrenTask> taskList = Lists.newArrayList(childTaskRepository.search(queryBuilder));
            if (StringUtils.isNotEmpty(taskList)) { return new ArrayList<>();}
            return taskList;
        }
        //不是部长的情况
        BoolQueryBuilder qb = QueryBuilders.boolQuery();
        qb.mustNot(QueryBuilders.termQuery("delFlag", true));
        qb.should(QueryBuilders.termQuery("projectLeaderId", userId))
                .should(QueryBuilders.termQuery("personId", userId))
                .should(QueryBuilders.termsQuery("memberIds",userId));

        return Lists.newArrayList(childTaskRepository.search(qb));
    }

    /**
     * 分页查询
     *
     * @param page
     * @param size
     * @param taskId 任务id
     * @param type   0 - 业务下任务的子任务    1 - 项目下任务的子任务
     * @return
     */
    public PageDTO queryByPage(int page, int size, String taskId, int type) {
        //当前用户的id
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        //获取当前用户部门
        List<String> deptList = projectService.getDeptIds();
        if (deptList == null) {
            throw new AdcDaBaseException("部门为空！");
        }
        Subject subject = SecurityUtils.getSubject();
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.mustNot(QueryBuilders.termQuery("delFlag", true));
        if (taskId != null && !taskId.trim().isEmpty()) {
            queryBuilder.must(QueryBuilders.matchQuery("taskId", taskId));
        } else {
            Set<String> taskIds = getTaskIdsFromParent(type);
            //根据任务id列表查询
            queryBuilder.must(QueryBuilders.termsQuery("taskId", taskIds));
        }

        List<ChildrenTask> queryList = null;
        if (subject.hasRole("部长")) {
            BoolQueryBuilder queryBuilder2 = QueryBuilders.boolQuery();
            for (String deptId : deptList) {
                queryBuilder2.should(QueryBuilders.termQuery("deptId", deptId));
            }
            queryBuilder.must(queryBuilder2);
            Page<ChildrenTask> queryPage = childTaskRepository.search(queryBuilder, new PageRequest(page - 1, size, sort));
            queryList = (queryPage == null || (queryList = queryPage.getContent()) == null) ? new ArrayList<ChildrenTask>() : queryList;
            long count = queryPage == null ? 0 : queryPage.getTotalElements();
            return new PageDTO(count, queryList, page, size);
        }
        if (subject.hasRole("超级管理员") || subject.hasRole("管理员") || subject.hasRole("主任")) {
            Page<ChildrenTask> queryPage = childTaskRepository.search(queryBuilder, new PageRequest(page - 1, size, sort));
            queryList = (queryPage == null || (queryList = queryPage.getContent()) == null) ? new ArrayList<ChildrenTask>() : queryList;
            long count = queryPage == null ? 0 : queryPage.getTotalElements();
            return new PageDTO(count, queryList, page, size);
        }
        BoolQueryBuilder queryBuilder1 = QueryBuilders.boolQuery();
        BoolQueryBuilder qb = QueryBuilders.boolQuery();
        qb.should(QueryBuilders.termQuery("projectLeaderId", userId))
                .should(QueryBuilders.termQuery("personId", userId))
                .should(QueryBuilders.termQuery("createUserId", userId))
                .should(QueryBuilders.termsQuery("memberIds",userId));
        queryBuilder.must(qb);
        queryBuilder1.must(queryBuilder)
                .mustNot(QueryBuilders.termQuery("delFlag", true));

        Page<ChildrenTask> queryPage = childTaskRepository.search(queryBuilder1, new PageRequest(page - 1, size, sort));
        queryList = (queryPage == null || (queryList = queryPage.getContent()) == null) ? new ArrayList<ChildrenTask>() : queryList;
        long count = queryPage == null ? 0 : queryPage.getTotalElements();
        return new PageDTO(count, queryList, page, size);
    }

    /**
     * 获取当前任务下的所有子任务
     *
     * @return
     */
    public List<ChildrenTask> queryAllByTaskId(String taskId, List<String> deptIds, String flag, String userId,Integer type) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.must(QueryBuilders.termQuery("taskId", taskId));
        // 增加删除标记
        queryBuilder.mustNot(QueryBuilders.termQuery("delFlag", true));
        if (null != type){
            queryBuilder.must(QueryBuilders.termQuery("status",ProjectStatusEnums.getStatus(type)));

        }
        if (flag.equals(Role.SUPER_ADMIN) || flag.equals(Role.ADMIN) || flag.equals(Role.ZHU_REN) || flag.equals(Role.PROJECT_ADMIN)) {
            return Lists.newArrayList(childTaskRepository.search(queryBuilder));
        }

        if (flag.equals(Role.BU_ZHANG)) {
            BoolQueryBuilder qb = QueryBuilders.boolQuery();
            for (String deptId : deptIds) {
                qb.should(QueryBuilders.termQuery("deptId", deptId));
            }
            queryBuilder.must(qb);
            //当前部门下的所有任务任务
            ArrayList<ChildrenTask> taskList = Lists.newArrayList(childTaskRepository.search(queryBuilder));
            if (StringUtils.isEmpty(taskList)) { return new ArrayList<>(); }
            return taskList;
        }
        //不是部长的情况
        BoolQueryBuilder qb = QueryBuilders.boolQuery();
        qb.should(QueryBuilders.termQuery("projectLeaderId", userId))
                .should(QueryBuilders.termQuery("personId", userId))
                .should(QueryBuilders.termsQuery("memberIds",userId))
                .should(QueryBuilders.termQuery("pm", userId));
        queryBuilder.must(qb);
        return Lists.newArrayList(childTaskRepository.search(queryBuilder));
    }

    /**
     * 获取指定上级任务id列表
     *
     * @param type
     * @return
     */
    Set<String> getTaskIdsFromParent(int type) {
        Set<String> taskIds = new HashSet<>();
        String queryField = type == 0 ? "budgetId" : "projectId";
        Iterable<Task> taskIterable = taskRepository.search(
                QueryBuilders.boolQuery().must(QueryBuilders.existsQuery(queryField))
                        .mustNot(QueryBuilders.termQuery("delFlag", true)));
        if (taskIterable != null) {
            for (Task task : Lists.newArrayList(taskIterable)) {
                if (task.getId() != null) { taskIds.add(task.getId()); }
            }
        }
        return taskIds;

    }




    /**
     * 修改任务名称
     */
    public void updateBelongTaskName(Task task) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery("taskId", task.getId()))
                .mustNot(QueryBuilders.termQuery("delFlag", true));
        Iterable<ChildrenTask> childrenTaskIterable = childTaskRepository.search(boolQueryBuilder);
        ArrayList<ChildrenTask> childrenTasks = Lists.newArrayList(childrenTaskIterable);
        for (ChildrenTask childrenTask : childrenTasks) {
            childrenTask.setBelongTaskName(task.getName());
        }
        if(CollectionUtils.isNotEmpty(childrenTasks)){
            childTaskRepository.save(childrenTasks);
        }
    }


    /**
     * 子任务完成后，判断当前子任务所属任务的其他子任务是否完成，如果全部完成改变任务的完成状态
     *
     * @param taskId
     */
    private void checkTaskStatus(String taskId) {
        //        List<ChildrenTask> childrenTaskList = childTaskRepository.findByTaskIdEquals(taskId);
        QueryBuilder builder = QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery("taskId", taskId))
                .mustNot(QueryBuilders.termQuery("delFlag", true));
        Iterable<ChildrenTask> childrenTaskList = childTaskRepository.search(builder);
        String taskProjectStatus = ProjectStatusEnums.COMPLETE.getStatus();
//        if (null != childrenTaskList && childrenTaskList.size() > 0) {
//            for (ChildrenTask childrenTask : childrenTaskList) {
//                if (ENUM_IF_COMPLETE.NO.getLabel().equals(childrenTask.getStatus())) {
//                    taskProjectStatus = ENUM_IF_COMPLETE.NO.getLabel();
//                    break;
//                }
//            }
//        }
        Iterator<ChildrenTask> childrenTaskIterator = childrenTaskList.iterator();
        while (childrenTaskIterator.hasNext()) {
            ChildrenTask childrenTask = childrenTaskIterator.next();
            if (ProjectStatusEnums.EXECUTE.getStatus().equals(childrenTask.getStatus())) {
                taskProjectStatus = ProjectStatusEnums.EXECUTE.getStatus();
                break;
            }
        }
        Task task = taskRepository.findOne(taskId);
        task.setTaskStatus(taskProjectStatus);
        if (ProjectStatusEnums.COMPLETE.getStatus().equals(taskProjectStatus)) {
            task.setFinishedActualTime(new Date());
        } else {
            task.setFinishedActualTime(null);
        }

        taskRepository.save(task);
    }

    /**
     * 执行add or remove
     *
     * @param repository
     * @param method     执行的方法
     * @param userIds
     * @param isAdd      是否是add操作
     * @param id         增加或移除的ID
     * @throws Exception
     */
    protected <E> void setUserWithProjectsData(ElasticsearchRepository repository, Method method, Set<E> userIds, Boolean isAdd, String id) {
        try {
            userIds.remove(null);
            List<E> obj = (List<E>) repository.findAll(userIds);
            for (E e : obj) {
                Set set = (Set) method.invoke(e);
                if (isAdd) {
                    set.add(id);
                } else {
                    set.remove(id);
                }
            }
            repository.save(obj);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    private Set<String> getUserIds(ChildrenTask childrenTask) {
        Set<String> userIds = new HashSet<>();
        userIds.add(childrenTask.getPersonId());
        userIds.add(childrenTask.getCreateUserId());
        if (CollectionUtils.isNotEmpty(childrenTask.getMemberIds())) {
            userIds.addAll(Arrays.asList(childrenTask.getMemberIds()));
        }
        Task task = taskRepository.findOne(childrenTask.getTaskId());
        userIds.add(task.getCreateUserId());
        userIds.add(task.getApproveUserId());
        BudgetEO budgetEO;
        if (StringUtils.isEmpty(task.getBudgetId())) {
            Project project = projectRepository.findOne(task.getProjectId());
            budgetEO = budgetEODao.selectByPrimaryKey(project.getBudgetId());
            userIds.add(project.getProjectLeaderId());
            //update by liqiushi 20190416 加上当前子任务上级项目的创建人
            userIds.add(project.getCreateUserId());
            userIds.add(budgetEO.getPm());
            userIds.add(budgetEO.getCreateUserId());

            userIds.add(budgetEO.getBusinessAdminId());
            userIds.add(project.getProjectAdminId());

            task.setProjectAdminId(project.getProjectAdminId());
            task.setProjectAdminName(project.getProjectAdminName());
            task.setBusinessAdminId(budgetEO.getBusinessAdminId());
            task.setBusinessAdminName(budgetEO.getBusinessAdminName());
        } else {
            budgetEO = budgetEODao.selectByPrimaryKey(task.getBudgetId());
            userIds.add(budgetEO.getPm());
            userIds.add(budgetEO.getCreateUserId());
            userIds.add(budgetEO.getBusinessAdminId());
            task.setBusinessAdminId(budgetEO.getBusinessAdminId());
            task.setBusinessAdminName(budgetEO.getBusinessAdminName());
        }
        userIds.removeAll(Collections.singleton(null));
        return userIds;
    }

    public String changeChildTaskStatus(String childTaskId, String btnType) {
        ChildrenTask childrenTask = childTaskRepository.findByIdAndDelFlagNot(childTaskId,true);
        String status = "-1";
        if(null!=childrenTask){
            if(StringUtils.equals(btnType,"进行中")) {
                childrenTask.setStatus(ProjectStatusEnums.AUDIT.getStatus());
                childrenTask.setBtnFlag("1");
            }else if(StringUtils.equals(btnType,"err")) {
                //当工作流接口出险异常时，将状态变回为进行中，因为不知道工作流接口的参数格式，加上这判断保证事务性
                childrenTask.setStatus(ProjectStatusEnums.EXECUTE.getStatus());
                childrenTask.setBtnFlag("0");
            }
            childTaskRepository.save(childrenTask);
            status = childrenTask.getStatus();
        } else {
            Task task = taskRepository.findByIdAndDelFlagNot(childTaskId,true);
            if(StringUtils.equals(btnType,"进行中")) {
                task.setTaskStatus(ProjectStatusEnums.AUDIT.getStatus());
                task.setBtnFlag("1");
            }else if(StringUtils.equals(btnType,"err")) {
                //当工作流接口出险异常时，将状态变回为进行中，因为不知道工作流接口的参数格式，加上这判断保证事务性
                task.setTaskStatus(ProjectStatusEnums.EXECUTE.getStatus());
                task.setBtnFlag("0");
            }
            taskRepository.save(task);
            status = task.getTaskStatus();
        }
        return status;
    }

    public String refreshChildTask(){
        List<ChildrenTask> childrenTaskList = Lists.newArrayList(childTaskRepository.findAll());
        for (ChildrenTask childrenTask : childrenTaskList){
            if (CollectionUtils.isEmpty(childrenTask.getMemberIds())){
                String [] memberIdArr = new String[]{childrenTask.getPersonId()};
                childrenTask.setMemberIds(memberIdArr);
                addField(childrenTask);
            }

            //rebuildUserWithProject(childrenTask);
        }
        childTaskRepository.save(childrenTaskList);
        return  "更新成功" ;
    }
    public String refreshChildTask(String childTaskId){
        ChildrenTask childrenTask = childTaskRepository.findById(childTaskId);
        if (CollectionUtils.isEmpty(childrenTask.getMemberIds())){
            String [] memberIdArr = new String[]{childrenTask.getPersonId()};
            childrenTask.setMemberIds(memberIdArr);
            addField(childrenTask);
        }
        rebuildUserWithProject(childrenTask);
        childTaskRepository.save(childrenTask);
        return "更新成功";
    }

    public void addField(ChildrenTask childrenTask){
        List<Map<String, String>> userIdDeptNameKv = new ArrayList<>();
        List<Map<String, String>> userIdUserNamesKv = new ArrayList<>();
        List<String> userNameList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(childrenTask.getMemberIds())) {
            List<String> projectMemberIdList = new ArrayList<>(Arrays.asList(childrenTask.getMemberIds()));
            List<UserEO> userEOList = userEODao.getUserWithRolesByUserIds(projectMemberIdList);
            userIdDeptNameKv = CommonUtil.userIdDeptNamesMapKv(userEOList,orgEODao);
            userIdUserNamesKv = CommonUtil.userIdUsnameMapKv(userEOList);
            for (UserEO userEO: userEOList){
                userNameList.add(userEO.getUsname());
            }
        }
        childrenTask.setMapsList(userIdUserNamesKv);
        childrenTask.setUserIdDeptNameMapList(userIdDeptNameKv);
        childrenTask.setMemberNames(userNameList.toArray(new String[userNameList.size()]));
        childrenTask.setMemberNameString(StringUtils.join(userNameList,','));
    }

    @Autowired
    UserProjectCustomDao userProjectCustomDao;
    public Integer queryTaskStatus(String taskId) {
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        // 0 是不显示
        List<UserProjectCustom> userProjectCustomList = userProjectCustomDao.findByStatus( "0",userId);
        for (UserProjectCustom userProjectCustom : userProjectCustomList){
            //如果所选任务在不显示状态，那么下面的查询就不用走了
            if (StringUtils.equals(taskId,userProjectCustom.getTaskid())||StringUtils.equals(taskId,userProjectCustom.getChildtaskid())){
                return 1; // 已隐藏
            }
        }
        ChildrenTask childrenTask =  childTaskRepository.findById(taskId);
        if (null!= childrenTask){
            // 如果这个人被从成员中移除 那么返回3
            if (CommonUtil.arrayContains(childrenTask.getMemberIds(),userId) < 0){
                return 3;
            }
            if (StringUtils.equals(ProjectStatusEnums.COMPLETE.getStatus(),childrenTask.getStatus())){
                return 2; // 已完成
            }else {
                return 0;
            }
        }
        Task task = taskRepository.findById(taskId);
        if (null!= task){
            // 如果这个人被从成员中移除 那么返回3
            if (CommonUtil.arrayContains(task.getMemberIds(),userId) < 0){
                return 3;
            }
            if (StringUtils.equals(ProjectStatusEnums.COMPLETE.getStatus(),task.getTaskStatus())){
                return 2; // 已完成
            }else {
                return 0;
            }
        }
        return -1;

    }

    public ChildrenTask updateWithoutShiroAuthentication(ChildrenTaskVO childrenTaskVO)throws Exception {
        // 查询出子任务
        ChildrenTask childrenTaskOri = childTaskRepository.findOne(childrenTaskVO.getId());
        Set<String> memberIdSet = new HashSet<>();
        List<Map<String,String>> deptInfoListMap = childrenTaskVO.getDeptInfoListMap();// 所选部门ID和类型 如类型=1 只选当前自身 类型=2 包含子部门
        HashMap<String,List<String>> DeptUserIdMap = new HashMap<>();
        for(Map<String,String> map :deptInfoListMap){
            int type =  Integer.parseInt(map.get("type").toString());
            List<String> userIdList = new ArrayList();
            String deptId =  map.get("deptId").toString();
            if(type == 1){
                List<String> deptList = new ArrayList<>();
                deptList.add(deptId);
                List<UserEO> userEOList = userEOService.getAllUserEOsByOrgIds(deptList);//根据部门id查询成员
                for(UserEO userEO:userEOList){
                    userIdList.add(userEO.getUsid());
                    memberIdSet.add(userEO.getUsid());
                }
                DeptUserIdMap.put(deptId,userIdList);
            }else if(type == 2){
                List<UserEO> userEOList = orgEOService.listUserEOByOrgId(deptId);//根据部门id查询成员
                for(UserEO userEO:userEOList){
                    userIdList.add(userEO.getUsid());
                    memberIdSet.add(userEO.getUsid());
                }
                DeptUserIdMap.put(deptId,userIdList);
            }
        }
        childrenTaskVO.setDeptIdUserIdList(DeptUserIdMap);
        String[] childrenTaskVOMemberIds = childrenTaskVO.getMemberIds();
        if (CollectionUtils.isNotEmpty(childrenTaskVOMemberIds)||CollectionUtils.isNotEmpty(memberIdSet)) {
            Set<String> childrenTaskVOMemberIdSet = new HashSet<>();
           if (CollectionUtils.isNotEmpty(childrenTaskVOMemberIds)){
               childrenTaskVOMemberIdSet = new HashSet<>(Arrays.asList(childrenTaskVOMemberIds));
           }
            childrenTaskVOMemberIdSet.addAll(memberIdSet);
            if (StringUtils.isNotEmpty(childrenTaskVO.getProjectLeaderId())) {
                childrenTaskVOMemberIdSet.add(childrenTaskVO.getProjectLeaderId());
            }
            List<UserEO> userEOList = userEODao.getUserWithRolesByUserIds(new ArrayList<String>(childrenTaskVOMemberIdSet));
            String[] projectMemberNameArr = new String[userEOList.size()];
            for (int i = 0; i < userEOList.size(); i++) {
                UserEO userEO = userEOList.get(i);
                projectMemberNameArr[i] = userEO.getUsname();
                memberIdSet.add(userEO.getUsid());
            }
            childrenTaskVO.setMemberNames(projectMemberNameArr);
            childrenTaskVO.setMemberIds(memberIdSet.toArray(new String[memberIdSet.size()]));
            childrenTaskVO.setProjectMemberNames(StringUtils.join(projectMemberNameArr, ','));
            childrenTaskVO.setMapsList(CommonUtil.userIdUsnameMapKv(userEOList));
            childrenTaskVO.setUserIdDeptNameMapList(CommonUtil.userIdDeptNamesMapKv(userEOList, orgEODao));
        }
        //如果子任务的所属人变更，修改子任务同时删除用户业务树关联中的子任务ID
//        if (!StringUtils.equals(childrenTaskVO.getPersonId(), childrenTaskOri.getPersonId())) {
//            //删除之前子任务所属人的子任务ID
//            //业务任务
//            updateUserWithChildTask(childrenTaskOri,childrenTaskVO);
//        }
        deleteUserWithProject(childrenTaskOri); //把原来的关系干掉
        // 转换子任务
        BeanUtils.copyPropertiesIgnoreNullValue(childrenTaskVO, childrenTaskOri);
        rebuildUserWithProject(childrenTaskOri); //重新构建关系
        // 保存
        ChildrenTask bui = childTaskRepository.save(childrenTaskOri);
        return bui;
    }

    public boolean setProjectMemberUpdatingTime(String id, Date date) {
        ChildrenTask childrenTask = childTaskRepository.findByIdAndDelFlagNot(id, Boolean.TRUE);
        if (childrenTask == null) {
            return false;
        }
        childrenTask.setProjectMemberUpdatingTime(date);
        childTaskRepository.save(childrenTask);
        return true;
    }


}
