package com.adc.da.budget.service;

import com.adc.da.base.service.BaseService;
import com.adc.da.budget.constant.Role;
import com.adc.da.budget.dao.BudgetEODao;
import com.adc.da.budget.dao.OrgListDao;
import com.adc.da.budget.dao.UserEPDao;
import com.adc.da.budget.dao.UserProjectCustomDao;
import com.adc.da.budget.entity.*;
import com.adc.da.budget.enums.ProjectStatusEnums;
import com.adc.da.budget.page.BudgetEOPage;
import com.adc.da.budget.page.TreeCustomPage;
import com.adc.da.budget.repository.ChildTaskRepository;
import com.adc.da.budget.repository.ProjectRepository;
import com.adc.da.budget.repository.TaskRepository;
import com.adc.da.budget.repository.UserWithProjectsRepository;
import com.adc.da.budget.utils.CommonUtil;
import com.adc.da.budget.vo.ListTreeVO;
import com.adc.da.budget.vo.TreeNode;
import com.adc.da.budget.vo.UserProjectCustomVO;
import com.adc.da.crm.dao.UserDao;
import com.adc.da.login.util.UserUtils;
import com.adc.da.sys.entity.DicTypeEO;
import com.adc.da.sys.entity.OrgEO;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.sys.service.DicTypeEOService;
import com.adc.da.sys.service.UserEOService;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.http.PageInfo;
import com.adc.da.util.utils.BeanMapper;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.StringUtils;
import com.adc.da.util.utils.UUID;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.adc.da.budget.constant.ProjectType.BUSINESS_PROJECT;
import static com.adc.da.budget.constant.ProjectType.NO_BUSINESS_PROJECT;
import static com.adc.da.budget.constant.ProjectType.RESEARCH_PROJECT;

/**
 * <br>
 * <b>功能：</b>TS_BUDGET BudgetEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2018-10-25 <br>0.
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Slf4j
@Service("budgetTreeService")
@Transactional(value = "transactionManager", readOnly = true,
    propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class BudgetTreeService extends BaseService<BudgetEO, String> {

    private static final String DEL_FLAG = "delFlag";

    @Autowired
    BeanMapper beanMapper;

    @Autowired
    private BudgetEODao dao;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserEOService userEOService;

    @Autowired
    private ChildTaskRepository childTaskRepository;

    @Autowired
    private BudgetEODao budgetEODao;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserWithProjectsRepository userWithProjectsRepository;

    @Autowired
    private OrgListDao orgListDao;

    @Autowired
    private UserProjectCustomDao userProjectCustomDao;

    @Autowired
    private UserEPDao userEPDao;
    @Autowired
    private DicTypeEOService dicTypeEOService;

    @Override
    public BudgetEODao getDao() {
        return dao;
    }

    /**
     * 中心下的机构树
     *
     * @param keyword
     * @return
     */
    public List<ListTreeVO> getOrgTreeVO(String keyword, Integer type) {
        List<ListTreeVO> resultList = new ArrayList<>();
        //获取中心下的本部
        List<OrgEO> allAdcOrg = orgListDao.getOrgEOByPidAsc("MH8JQV5TSN");
        for (OrgEO orgEO : allAdcOrg) {
            resultList.add(new ListTreeVO(orgEO.getId(), orgEO.getName(), orgEO.getParentId(), 1, "", ""));
            List<OrgEO> orgEOList = orgListDao.getOrgEOByPidAsc(orgEO.getId());
            if (CollectionUtils.isNotEmpty(orgEOList)) {
                //获取本部下的部门
                for (OrgEO org : orgEOList) {
                    resultList.add(new ListTreeVO(org.getId(), org.getName(), org.getParentId(), 2, "", ""));
                    List<String> orgIds = new ArrayList<>();
                    orgIds.add(org.getId());
                    List<UserEO> userEOS = userDao.selectByOrgIdAndUsName(orgIds, null);

                    //拿到部门下的所有业务
                    for (UserEO userEO : userEOS) {
                        getUserListTree(userEO.getUsid(), org.getId(), resultList, type);
                    }

                }
            } else {
                List<String> orgIds = new ArrayList<>();
                orgIds.add(orgEO.getId());
                List<UserEO> userEOS = userDao.selectByOrgIdAndUsName(orgIds, null);
                if (CollectionUtils.isNotEmpty(userEOS)) {
                    //拿到部门下的所有业务
                    for (UserEO userEO : userEOS) {
                        getUserListTree(userEO.getUsid(), orgEO.getId(), resultList, type);
                    }
                }
            }
        }
        return getKeywordFilter(resultList, keyword);
    }

    private void getUserListTree(String userId, String orgId, List<ListTreeVO> list, Integer type) {
        List<BudgetEO> budgetEOS = dao.selectByPM(userId);
        if (CollectionUtils.isNotEmpty(budgetEOS)) {
            for (BudgetEO budgetEO : budgetEOS) {
                List<Project> projects;
                //拿到业务下所有项目
                if (null == type) {
                    projects = projectRepository.findByBudgetIdAndDelFlagNot(budgetEO.getId(), Boolean.TRUE);
                    list.add(new ListTreeVO(
                        budgetEO.getId(),
                        budgetEO.getProjectName(),
                        orgId,
                        3,
                        budgetEO.getPm(),
                        budgetEO.getUsname()));
                } else {
                    projects = projectRepository.findByBudgetIdAndDelFlagNotAndFinishedStatus(
                        budgetEO.getId(),
                        Boolean.TRUE,
                        ProjectStatusEnums.getStatus(type));
                    if (CollectionUtils.isNotEmpty(projects)) {
                        list.add(new ListTreeVO(
                            budgetEO.getId(),
                            budgetEO.getProjectName(),
                            orgId,
                            3,
                            budgetEO.getPm(),
                            budgetEO.getUsname()));
                    }
                }
                if (CollectionUtils.isNotEmpty(projects)) {
                    for (Project project : projects) {
                        list.add(new ListTreeVO(
                            project.getId(),
                            project.getName(),
                            project.getBudgetId(),
                            4,
                            project.getProjectLeaderId(),
                            project.getProjectLeader()));
                    }
                }
            }
        }
    }
    /**
     * 增加关键字模糊查询功能
     *
     * @param keyword
     * @return
     * @author liuzixi
     *     date 2019-03-01
     */
    public List<ListTreeVO> getListTree(String keyword, Integer type, String status, String property,
        boolean hidePageFlag) {
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        Subject subject = SecurityUtils.getSubject();

        //超级管理员和主任以及项目管理员看中心所有的业务
        Set<String> businessIds = new HashSet<>();
        Set<String> projectIds = new HashSet<>();
        Set<String> taskIds = new HashSet<>();
        Set<String> childTaskIds = new HashSet<>();
        //根据userId获取被隐藏的BusinessId
        List<String> hideBusinessIds = userProjectCustomDao.findAllHideBusinessId(userId);
        List<String> hideProjectIds = userProjectCustomDao.findAllHideProjectId(userId);
        if (subject.hasRole(Role.SUPER_ADMIN) || subject.hasRole(Role.ZHU_REN) || subject.hasRole(Role.PROJECT_ADMIN)
            || subject.hasRole(Role.OPERATE_BUDGET_ADMIN)) {
            List<BudgetEO> budgetEOList = queryAllByDeptId(null, property);
            List<ListTreeVO> listTreeVOList = setTreeListData(budgetEOList, type);

            List<ListTreeVO> listTreeVOKeywordFilter = getKeywordFilter(listTreeVOList, keyword);
            List<ListTreeVO> result = getHideFilter(listTreeVOKeywordFilter, status, userId);
            return result;
            //return getHideFilter(listTreeVOKeywordFilter, status);
        } else if (subject.hasRole(Role.COMMITTEE_MEMBER) || subject.hasRole(Role.RESEARCH_ADMIN)) {
            /*
             *  科委会与科研管理员可以查看所有的科研项目信息
             */
            setCommitMember(businessIds, projectIds, taskIds, childTaskIds);

        }


        /*
         * 科委会与部门存在交叉关系
         * 本部长看本部下所有部门业务树       部长看部门下所有业务树
         */
        if (subject.hasRole(Role.BEN_BU_ZHANG) || subject.hasRole(Role.BU_ZHANG)) {

            UserEO userWithRoles = userEOService.getUserWithRoles(userId);

            List<OrgEO> myOrgList = userWithRoles.getOrgEOList();
            //获取当前组织下的所有子机构
            List<String> orgIds = orgListDao.getOrgAndSubOrgIds(myOrgList.get(0).getId());
            // 先从字典找一圈
            List<DicTypeEO> dicTypeEOList = dicTypeEOService.queryByList("FAKE_BENBU");
            if(CollectionUtils.isNotEmpty(dicTypeEOList)) {
                for (DicTypeEO dicTypeEO : dicTypeEOList) {
                    if (StringUtils.contains(dicTypeEO.getDicTypeName(), userId)) {
                        orgIds.addAll(orgListDao.getOrgAndSubOrgIds(dicTypeEO.getDicTypeCode()));
                    }
                }
            }
            List<UserEO> userEOList = userDao.selectByOrgIdAndUsName(orgIds, null);
            Set<String> userIdSet = new HashSet<>();
            for (UserEO eo : userEOList) {
                userIdSet.add(eo.getUsid());
            }
            List<UserWithProjects> userWithProjectsList = userWithProjectsRepository.findByUserIdIn(userIdSet);
            for (UserWithProjects userWithProjects : userWithProjectsList) {
                if (!hidePageFlag) {
                    userWithProjects.getBusinessIds().removeAll(new HashSet<>(hideBusinessIds));
                    userWithProjects.getProjectIds().removeAll(new HashSet<>(hideProjectIds));
                }
                businessIds.addAll(userWithProjects.getBusinessIds());
                projectIds.addAll(userWithProjects.getProjectIds());
                taskIds.addAll(userWithProjects.getTaskIds());
                childTaskIds.addAll(userWithProjects.getChildTaskIds());
            }
            //拿到所有的业务项目任务子任务ID
            List<ListTreeVO> listTreeVOList = setTreeListForUserWithProjects(
                businessIds,
                projectIds,
                taskIds,
                childTaskIds,
                type, property);
            List<ListTreeVO> listTreeVOKeywordFilter = getKeywordFilter(listTreeVOList, keyword);

            return getHideFilter(listTreeVOKeywordFilter, status, userId);

            //return getKeywordFilter(listTreeVOList, keyword);
        }

        //员工看自己的业务树
        UserWithProjects userWithProjects = userWithProjectsRepository.findOne(userId);
        if (null != userWithProjects) {
            if (!hidePageFlag) {
                userWithProjects.getBusinessIds().removeAll(new HashSet<>(hideBusinessIds));
                userWithProjects.getProjectIds().removeAll(new HashSet<>(hideProjectIds));
            }
            businessIds.addAll(userWithProjects.getBusinessIds());
            projectIds.addAll(userWithProjects.getProjectIds());
            taskIds.addAll(userWithProjects.getTaskIds());
            childTaskIds.addAll(userWithProjects.getChildTaskIds());

            List<ListTreeVO> listTreeVOList = setTreeListForUserWithProjects(
                businessIds,
                projectIds,
                taskIds,
                childTaskIds,
                type, property);
            //return getKeywordFilter(listTreeVOList, keyword);
            List<ListTreeVO> listTreeVOKeywordFilter = getKeywordFilter(listTreeVOList, keyword);

            return getHideFilter(listTreeVOKeywordFilter, status, userId);
        }
        return new ArrayList<>();
    }

    /**
     * @param list
     * @param status 业务的状态  all全部，1显示中  0 已隐藏  新增功能  20200303 zyh
     * @return
     */
    private List<ListTreeVO> getHideFilter(List<ListTreeVO> list, String status, String userId) {
        List<ListTreeVO> removeList = new ArrayList<>();

        if (StringUtils.equals(status, "main") || "1".equals(status)) {
            List<UserProjectCustom> userProjectCustoms = userProjectCustomDao.findByStatus("0", userId);
            if (StringUtils.isNotEmpty(userProjectCustoms)) {
                for (UserProjectCustom userProjectCustom : userProjectCustoms) {
                    String publicId = "";
                    String parentPublicId = "";
                    String treeType = userProjectCustom.getType() + "";//1业务2 项目 3任务 4子任务
                    if (StringUtils.equals(treeType, "1")) {
                        //业务
                        publicId = userProjectCustom.getBusinessid();
                        parentPublicId = "0";
                    } else if (StringUtils.equals(treeType, "2")) {
                        //项目
                        publicId = userProjectCustom.getProjectid();
                        parentPublicId = userProjectCustom.getBusinessid();
                    } else if (StringUtils.equals(treeType, "3")) {
                        //任务
                        publicId = userProjectCustom.getTaskid();
                        parentPublicId = userProjectCustom.getProjectid();
                        if (StringUtils.isEmpty(parentPublicId)) {
                            parentPublicId = userProjectCustom.getBusinessid();
                        }
                    } else if (StringUtils.equals(treeType, "4")) {
                        //子任务
                        publicId = userProjectCustom.getChildtaskid();
                        parentPublicId = userProjectCustom.getTaskid();
                    }

                    for (ListTreeVO treeVO : list) {
                        String id = treeVO.getId();
                        String parentId = treeVO.getParentId();
                        if (id.equals(publicId) && parentId.equals(parentPublicId)) {
                            removeList.add(treeVO);
                            break;
                        }
                    }
                }
            }

            if (removeList.size() > 0) {
                list.removeAll(removeList);

            }
            return list;
        }
        return list;

    }

    /**
     * 获取
     *
     * @param deptIds
     * @return
     */
    public List<BudgetEO> queryAllByDeptId(List<String> deptIds, String property) {
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        Subject subject = SecurityUtils.getSubject();
        UserEO userEO = userEOService.getUserWithRoles(userId);
        List<OrgEO> orgList = userEO.getOrgEOList();
        OrgEO orgEO = orgList.get(orgList.size() - 1);

        if (subject.hasRole(Role.SUPER_ADMIN)
            || subject.hasRole(Role.ADMIN)
            || subject.hasRole(Role.ZHU_REN)
            || subject.hasRole(Role.PROJECT_ADMIN)) {
            return dao.findAll(property);

        } else if ("市场发展部".equals(orgEO.getName())) {
            return dao.findAll(property);
        }
        return dao.selectByDeptIds(deptIds, property);
    }

    /**
     * 查询并添加科委会的项目
     * Set<String> businessIds = new HashSet<>();
     * Set<String> projectIds = new HashSet<>();
     * Set<String> taskIds = new HashSet<>();
     * Set<String> childTaskIds = new HashSet<>();
     *
     * @param businessIds
     */
    private void setCommitMember(Set<String> businessIds, Set<String> projectIds, Set<String> taskIds,
        Set<String> childTaskIds) {
        /*
         * 科委会成员
         * subject.hasRole(Role.COMMITTEE_MEMBER)
         */
        BudgetEOPage page = new BudgetEOPage();
        Calendar cale = Calendar.getInstance();
        int year = cale.get(Calendar.YEAR);

        page.setProjectName(String.valueOf(year) + '%');
        page.setProjectNameOperator("like");
        page.setProperty("2");
        List<BudgetEO> budgetEOList = budgetEODao.queryByList(page);
        if (CollectionUtils.isEmpty(budgetEOList)) {
            throw new AdcDaBaseException("科研业务信息获取失败");
        }

        String budgetId = budgetEOList.get(0).getId();
        businessIds.add(budgetId);
        /*
         * 查项目
         */
        List<Project> projectList = projectRepository.findByBudgetIdAndDelFlagNot(budgetId, Boolean.TRUE);
        for (Project p : projectList) {
            projectIds.add(p.getId());
        }

        if (CollectionUtils.isNotEmpty(projectIds)) {
            /*
             *  查业务任务
             */
            List<Task> taskList = taskRepository.findByBudgetIdAndDelFlagNot(budgetId, Boolean.TRUE);
            for (Task t : taskList) {
                taskIds.add(t.getId());
            }
            /*
             *  查项目任务
             */
            List<Task> projectTaskList = taskRepository.findByProjectIdInAndDelFlagNot(projectIds, Boolean.TRUE);
            for (Task t : projectTaskList) {
                taskIds.add(t.getId());
            }
            /*
             *  查子任务
             */
            if (CollectionUtils.isNotEmpty(taskIds)) {
                List<ChildrenTask> childrenTaskList = childTaskRepository
                    .findByTaskIdInAndDelFlagNot(taskIds, Boolean.TRUE);
                for (ChildrenTask c : childrenTaskList) {
                    childTaskIds.add(c.getId());
                }
            }
        }
    }

    /**
     * 从用户关联业务树表拿数据组合
     *
     * @param businessIds
     * @param projectIds
     * @param taskIds
     * @param childTaskIds
     * @return
     */
    private List<ListTreeVO> setTreeListForUserWithProjects(Set<String> businessIds, Set<String> projectIds,
        Set<String> taskIds, Set<String> childTaskIds, Integer type, String property) {
        Set<String> usedBudgetIdSet =  new HashSet<>();
        /*
         * 去除set中的null元素
         */
        businessIds.removeAll(Collections.singleton(null));
        projectIds.removeAll(Collections.singleton(null));
        taskIds.removeAll(Collections.singleton(null));
        childTaskIds.removeAll(Collections.singleton(null));

        List<ListTreeVO> listTreeVOList = new ArrayList<>();


        Set<String> budgetIds = new HashSet<>();
        Set<String> projectIdSet = new HashSet<>();
        Set<String> taskIdSet = new HashSet<>();
        List<UserEPEntity> userEPEntityList = userEPDao.queryAllUserIdAndName();

        if (CollectionUtils.isNotEmpty(businessIds)) {
            businessIds.removeAll(Collections.singleton(null));
            List<BudgetEO> budgetEOList = dao.findByIds(businessIds, property);
            List<Project> projects;
            for (BudgetEO budgetEO : budgetEOList) {
                if (null != type) {
                    projects = projectRepository.findByBudgetIdAndDelFlagNotAndFinishedStatus(
                        budgetEO.getId(),
                        Boolean.TRUE,
                        ProjectStatusEnums.getStatus(type));
                    //如果下属没有筛选后的状态的项目 不添加
                    if (CollectionUtils.isEmpty(projects)) {
                        continue;
                    }
                }
                String pm = budgetEO.getPm();
                String pmName = "";
                if (StringUtils.isNotEmpty(pm)) {
                    for (UserEPEntity userEPEntity : userEPEntityList) {
                        if (pm.equals(userEPEntity.getUsid())) {
                            pmName = userEPEntity.getUsname();
                        }
                    }
                }
                listTreeVOList.add(new ListTreeVO(budgetEO.getId(), budgetEO.getProjectName(),
                    "0", 1, pm, pmName));
                budgetIds.add(budgetEO.getId());
            }
        }
        if (CollectionUtils.isNotEmpty(projectIds)) {

            List<List<String>> resultList = CommonUtil.splitList(new ArrayList<>(projectIds), 500);
            List<Project> projectList = new ArrayList<>();
            for (List<String> list : resultList) {
                /*
                 * 过滤已删除的项目
                 */
                projectList.addAll(Lists.newArrayList(projectRepository.findByIdInAndDelFlagNot(list, Boolean.TRUE)));
            }

            for (Project project : projectList) {

                //筛选项目状态
                if (null != type
                    && !StringUtils.equals(project.getFinishedStatus(), ProjectStatusEnums.getStatus(type))) {
                    continue;
                }
                usedBudgetIdSet.add(project.getBudgetId());
                // project有budget才能添加
                if (budgetIds.contains(project.getBudgetId())) {
                    String projectType;
                    /*
                     * 判断项目类型
                     */
                    if (project.getBusinessId().contains("rs_")) {
                        projectType = RESEARCH_PROJECT.toString();
                    } else if (null != project.getProjectBeginTime()) {
                        projectType = BUSINESS_PROJECT.toString();
                    } else {
                        projectType = NO_BUSINESS_PROJECT.toString();
                    }
                    String projectLeaderId = project.getProjectLeaderId();
                    String projectLeaderName = "";
                    if (StringUtils.isNotEmpty(projectLeaderId)) {
                        for (UserEPEntity userEPEntity : userEPEntityList) {
                            if (projectLeaderId.equals(userEPEntity.getUsid())) {
                                projectLeaderName = userEPEntity.getUsname();
                            }
                        }
                    }
                    if (project.getProjectType() == 0) {
                        listTreeVOList
                            .add(new ListTreeVO(
                                project.getId(),
                                project.getName(),
                                project.getBudgetId(),
                                2,
                                projectType,
                                project.getContractNo(),
                                project.getName(),
                                project.getProjectOwner(), projectLeaderId, projectLeaderName));

                        projectIdSet.add(project.getId());
                    } else {
                        listTreeVOList
                            .add(new ListTreeVO(
                                project.getId(),
                                project.getName(),
                                project.getBudgetId(),
                                2,
                                projectType, projectLeaderId, projectLeaderName));

                        projectIdSet.add(project.getId());
                    }
                }
            }
        }
        if (CollectionUtils.isNotEmpty(taskIds)) {

            List<List<String>> resultList = CommonUtil.splitList(new ArrayList<>(taskIds), 500);
            List<Task> taskList = new ArrayList<>();
            for (List<String> list : resultList) {
                taskList.addAll(Lists.newArrayList(taskRepository.findByIdIn(list)));
            }
            for (Task task : taskList) {
                if(StringUtils.isEmpty(task.getProjectId())&&StringUtils.isNotEmpty(task.getBudgetId())) {
                    usedBudgetIdSet.add(task.getBudgetId());
                }

                //筛选任务状态
                if (null != type && !StringUtils.equals(task.getTaskStatus(), ProjectStatusEnums.getStatus(type))) {
                    continue;
                }
                String approveUserId = task.getApproveUserId();
                String approveUserName = "";
                if (StringUtils.isNotEmpty(approveUserId)) {
                    for (UserEPEntity userEPEntity : userEPEntityList) {
                        if (approveUserId.equals(userEPEntity.getUsid())) {
                            approveUserName = userEPEntity.getUsname();
                            break;
                        }
                    }
                }
                //项目任务
                if (StringUtils.isEmpty(task.getBudgetId()) && projectIdSet.contains(task.getProjectId())) {
                    listTreeVOList.add(new ListTreeVO(
                        task.getId(),
                        task.getName(),
                        task.getProjectId(),
                        3,
                        approveUserId,
                        approveUserName));
                    taskIdSet.add(task.getId());
                }
                //业务任务
                if (StringUtils.isNotEmpty(task.getBudgetId()) && budgetIds.contains(task.getBudgetId())) {
                    listTreeVOList.add(new ListTreeVO(
                        task.getId(),
                        task.getName(),
                        task.getBudgetId(),
                        3,
                        approveUserId,
                        approveUserName));
                    taskIdSet.add(task.getId());
                }
            }
        }
        if (CollectionUtils.isNotEmpty(childTaskIds)) {

            List<List<String>> resultList = CommonUtil.splitList(new ArrayList<>(childTaskIds), 500);
            List<ChildrenTask> childrenTaskList = new ArrayList<>();
            for (List<String> list : resultList) {
                childrenTaskList.addAll(Lists.newArrayList(childTaskRepository.findByIdIn(list)));
            }

            for (ChildrenTask childrenTask : childrenTaskList) {
                //筛选下级任务状态
                if (null != type &&
                    !StringUtils.equals(childrenTask.getStatus(), ProjectStatusEnums.getStatus(type))) {
                    continue;
                }
                if (taskIdSet.contains(childrenTask.getTaskId())) {
                    listTreeVOList.add(new ListTreeVO(
                        childrenTask.getId(),
                        childrenTask.getChildTaskName(),
                        childrenTask.getTaskId(),
                        4, "", ""));
                }
            }
        }
        if (null == type) {
            return listTreeVOList;
        } else {
            return checkResultTreeList(listTreeVOList,usedBudgetIdSet);
        }
    }

//    private List<ListTreeVO>  checkResultTreeList(List<ListTreeVO> listTreeVOList){
//        boolean allIsBudget = true ;
//        for (ListTreeVO listTreeVO : listTreeVOList){
//            if (listTreeVO.getType()!= 1){
//                allIsBudget = false ;
//                break;
//            }
//        }
//        if (allIsBudget){
//            return new ArrayList<>();
//        }else {
//            return listTreeVOList ;
//        }
//    }

    private List<ListTreeVO> checkResultTreeList(List<ListTreeVO> listTreeVOList,Set<String> usedBudgetIdSet) {
        List<ListTreeVO> resultListTreeVOList = new ArrayList<>();
        for (ListTreeVO listTreeVO : listTreeVOList) {
            if (listTreeVO.getType()==1 && !usedBudgetIdSet.contains(listTreeVO.getId())){
                continue;
            }
            resultListTreeVOList.add(listTreeVO);

        }

        Collections.sort(resultListTreeVOList, new Comparator<ListTreeVO>() {
            public int compare(ListTreeVO o1, ListTreeVO o2) {
                return o2.getType() - o1.getType();
            }
        });
        return resultListTreeVOList;
    }

    private boolean checkHasChildren(ListTreeVO checkListTreeVO, List<ListTreeVO> listTreeVOList) {
        for (ListTreeVO listTreeVO : listTreeVOList) {
            if (StringUtils.equals(listTreeVO.getParentId(), checkListTreeVO.getId())) {
                return true;
            }

        }
        return false;

    }

    /**
     * 关键词筛选
     *
     * @param list
     * @param keyword
     * @return
     * @author liuzixi
     *     date 2019-03-01
     */
    private List<ListTreeVO> getKeywordFilter(List<ListTreeVO> list, String keyword) {
        if (StringUtils.isBlank(keyword)) {
            return list;
        }
        Set<ListTreeVO> newList = new HashSet<>();
        int i = 0;
        int len = list.size();
        for (i = 0; i < len; i++) {
            ListTreeVO listTreeVO = list.get(i);
            if (null == listTreeVO.getName()){
                continue;
            }
            if (listTreeVO.getName().contains(keyword)) {
                int type = listTreeVO.getType();
                newList.add(listTreeVO);
                if (type == 1) {
                    for (ListTreeVO treeVO : list) {
                        if (StringUtils.equals(listTreeVO.getId(), treeVO.getParentId())) {
                            //添加项目与业务任务
                            newList.add(treeVO);
                            for (ListTreeVO treeVO1 : list) {
                                if (StringUtils.equals(treeVO.getId(), treeVO1.getParentId())) {
                                    //添加项目任务与业务任务子任务
                                    newList.add(treeVO1);
                                    for (ListTreeVO treeVO2 : list) {
                                        if (StringUtils.equals(treeVO1.getId(), treeVO2.getParentId())) {
                                            //添加项目任务子任务
                                            newList.add(treeVO2);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (type == 2) {
                    for (ListTreeVO treeVO : list) {
                        if (StringUtils.equals(listTreeVO.getParentId(), treeVO.getId())) {
                            //添加业务
                            newList.add(treeVO);
                        }
                        if (StringUtils.equals(listTreeVO.getId(), treeVO.getParentId())) {
                            //添加项目任务
                            newList.add(treeVO);
                            for (ListTreeVO treeVO1 : list) {
                                if (StringUtils.equals(treeVO.getId(), treeVO1.getParentId())) {
                                    //添加项目任务子任务
                                    newList.add(treeVO1);
                                }
                            }
                        }
                    }
                }
                if (type == 3) {
                    for (ListTreeVO treeVO : list) {
                        if (StringUtils.equals(listTreeVO.getParentId(), treeVO.getId())) {
                            //添加项目或者是业务
                            newList.add(treeVO);
                            for (ListTreeVO treeVO1 : list) {
                                if (StringUtils.equals(treeVO.getParentId(), treeVO1.getId())) {
                                    //添加业务
                                    newList.add(treeVO1);
                                }
                            }
                        }
                        if (StringUtils.equals(listTreeVO.getId(), treeVO.getParentId())) {
                            //添加项目任务子任务或者业务任务子任务
                            newList.add(treeVO);
                            for (ListTreeVO treeVO1 : list) {
                                if (StringUtils.equals(treeVO.getId(), treeVO1.getParentId())) {
                                    //添加项目任务子任务
                                    newList.add(treeVO1);
                                }
                            }
                        }
                    }
                }
                if (type == 4) {
                    for (ListTreeVO treeVO : list) {
                        if (StringUtils.equals(listTreeVO.getParentId(), treeVO.getId())) {
                            //父任务
                            newList.add(treeVO);
                            for (ListTreeVO treeVO1 : list) {
                                if (StringUtils.equals(treeVO.getParentId(), treeVO1.getId())) {
                                    //添加业务或者项目
                                    newList.add(treeVO1);
                                    for (ListTreeVO treeVO2 : list) {
                                        if (StringUtils.equals(treeVO1.getParentId(), treeVO2.getId())) {
                                            newList.add(treeVO2);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return new ArrayList<>(newList);
    }

    /**
     * 主任/管理员的业务树数据
     * 实地阿基DHIUSAHJDPIO
     *
     * @return LZ4LNLVWGD
     *     true
     *     null
     */
    private List<ListTreeVO> setTreeListData(List<BudgetEO> budgetEOList, Integer type) {
        Set<String> budgetEOSet = new HashSet<>();
        Set<String> usedBudgetIdSet =  new HashSet<>();


        ArrayList<ListTreeVO> listTreeVOArrayList = new ArrayList<>();
        //所有项目
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.mustNot(QueryBuilders.termQuery(DEL_FLAG, true));
        //所有业务任务
        BoolQueryBuilder queryBuilder =
            QueryBuilders
                .boolQuery()
                .mustNot(QueryBuilders.termQuery(DEL_FLAG, Boolean.TRUE));

        //所有子任务
        BoolQueryBuilder queryBuilderChildTask = QueryBuilders.boolQuery();

        if (null != type) {
            boolQueryBuilder.must(QueryBuilders.termQuery("finishedStatus", ProjectStatusEnums.getStatus(type)));
            //所有项目任务
            queryBuilder.must(QueryBuilders.termQuery("taskStatus", ProjectStatusEnums.getStatus(type)));

            queryBuilderChildTask.must(QueryBuilders.termQuery("status", ProjectStatusEnums.getStatus(type)));
        }
        List<Project> projects = Lists.newArrayList(projectRepository.search(boolQueryBuilder));

        List<Task> allTaskList = Lists.newArrayList(taskRepository.search(queryBuilder).iterator());
        List<Task> projectTasks = new ArrayList<>();
        List<Task> bizTaskList = new ArrayList<>();

        Map<String, Task> taskHashMap = new HashMap<>(allTaskList.size());

        for (Task task : allTaskList) {
            //既不是项目任务，也不是业务任务，不处理
            if (StringUtils.isNotEmpty(task.getProjectId())) {
                projectTasks.add(task);
            } else if (StringUtils.isNotEmpty(task.getBudgetId())) {
                bizTaskList.add(task);
            }
        }

        //所有子任务
        // 增加删除标记
        queryBuilderChildTask.mustNot(QueryBuilders.termQuery(DEL_FLAG, true));
        List<ChildrenTask> childrenTaskList = Lists.newArrayList(childTaskRepository.search(queryBuilderChildTask));

        //所有用户
        List<UserEPEntity> allUserList = userEPDao.queryAllUserIdAndName();
        Map<String, String> userIdNameMap = new HashMap<>(allUserList.size());
        for (UserEPEntity eo : allUserList) {
            userIdNameMap.put(eo.getUsid(), eo.getUsname());
        }

        //所有业务
        Map<String, BudgetEO> budgetEOMap = new HashMap<>(budgetEOList.size());
        for (BudgetEO budgetEO : budgetEOList) {

            if (!budgetEOSet.add(budgetEO.getId())) {
                continue;
            }

            budgetEOMap.put(budgetEO.getId(), budgetEO);

            String pm = budgetEO.getPm();
            String pmName;

            if (userIdNameMap.containsKey(pm)) {
                pmName = userIdNameMap.get(pm);
            } else {
                pmName = "";
            }
            ListTreeVO budgetListTreeVO = new ListTreeVO(budgetEO.getId(), budgetEO.getProjectName(),
                "0", 1, pm, pmName);
            // 业务加入到树中
            listTreeVOArrayList.add(budgetListTreeVO);
        }

        //所有项目
        Map<String, Project> projectMap = new HashMap<>(projects.size());
        for (Project project : projects) {
            BudgetEO budgetEO = budgetEOMap.get(project.getBudgetId());
            if (null == budgetEO) {
                continue;
            }
            //项目有效 加入到 map中
            projectMap.put(project.getId(), project);
            usedBudgetIdSet.add(project.getBudgetId());
            if (StringUtils.equals(budgetEO.getId(), project.getBudgetId())) {
                //管理员，针对经营类项目 追加合同合
                ListTreeVO projectListTreeVO = createVO(project, budgetEO, userIdNameMap);

                //加入到树
                listTreeVOArrayList.add(projectListTreeVO);

            }
        }

        //项目任务
        for (Task task : projectTasks) {
            Project project = projectMap.get(task.getProjectId());
            if (null == project) {
                continue;
            }
            //任务有效，加入到map中
            taskHashMap.put(task.getId(), task);
            String approveUserId = task.getApproveUserId();
            String approveUserName;
            if (StringUtils.isEmpty(approveUserId)) {
                approveUserId = project.getProjectLeaderId();
            }

            if (userIdNameMap.containsKey(approveUserId)) {
                approveUserName = userIdNameMap.get(approveUserId);
            } else {
                approveUserName = "";
            }

            ListTreeVO taskListTreeVO = new ListTreeVO(
                task.getId(),
                task.getName(),
                project.getId(),
                3, approveUserId, approveUserName);
            //加入到树
            listTreeVOArrayList.add(taskListTreeVO);

        }

        //业务任务
        for (Task task : bizTaskList) {
            BudgetEO budgetEO = budgetEOMap.get(task.getBudgetId());
            if (null == budgetEO) {
                continue;
            }
            if(StringUtils.isEmpty(task.getProjectId())&&StringUtils.isNotEmpty(task.getBudgetId())) {
                usedBudgetIdSet.add(task.getBudgetId());
            }
            //任务有效，加入到map中
            taskHashMap.put(task.getId(), task);

            String approveUserId = task.getApproveUserId();
            String approveUserName;
            if (StringUtils.isEmpty(approveUserId)) {
                approveUserId = budgetEO.getPm();
            }

            if (userIdNameMap.containsKey(approveUserId)) {
                approveUserName = userIdNameMap.get(approveUserId);
            } else {
                approveUserName = "";
            }

            ListTreeVO taskListTreeVO = new ListTreeVO(
                task.getId(),
                task.getName(),
                budgetEO.getId(),
                3, approveUserId, approveUserName);
            //加入到树
            listTreeVOArrayList.add(taskListTreeVO);

        }

        //子任务
        for (ChildrenTask childrenTask : childrenTaskList) {

            Task task = taskHashMap.get(childrenTask.getTaskId());
            if (null == task) {
                continue;
            }

            ListTreeVO childrenTaskListTreeVO = new ListTreeVO(
                childrenTask.getId(),
                childrenTask.getChildTaskName(),
                task.getId(),
                4, "", "");
            listTreeVOArrayList.add(childrenTaskListTreeVO);

        }

//        return listTreeVOArrayList;
        if (null != type) {
            return checkResultTreeList(listTreeVOArrayList, usedBudgetIdSet);
        }else {
            return listTreeVOArrayList;
        }
    }

    /**
     * //管理员，针对经营类项目 追加合同合
     *
     * @param project
     * @param budgetEO
     * @return
     */
    private ListTreeVO createVO(Project project, BudgetEO budgetEO, Map<String, String> userIdNameMap) {
        String projectLeaderId = project.getProjectLeaderId();
        String projectLeaderName;
        if (userIdNameMap.containsKey(projectLeaderId)) {
            projectLeaderName = userIdNameMap.get(projectLeaderId);
        } else {
            projectLeaderName = "";
        }

        if (project.getProjectType() == 0) {
            return new ListTreeVO(
                project.getId(),
                project.getName(),
                project.getBudgetId(),
                2,
                "0",
                project.getContractNo(),
                project.getName(),
                project.getProjectOwner(), projectLeaderId, projectLeaderName);
        } else {
            return new ListTreeVO(
                project.getId(),
                project.getName(),
                budgetEO.getId(),
                2,
                budgetEO.getProperty(), projectLeaderId, projectLeaderName);
        }
    }

    /***
     * 用户自定义树信息
     */
    public List<UserProjectCustomVO> getUserProjectCustomTree(String keyword, Integer type, String status) {
        List<ListTreeVO> listAll = this.getListTree(keyword, type, status, null, true);
        List<UserProjectCustomVO> listNew = new ArrayList<>();

        String userId = UserUtils.getUserId();

        List<UserProjectCustom> userProjectCustomsAll = userProjectCustomDao.findAll(userId);

        List<UserProjectCustom> userProjectCustomsBudget = new ArrayList<>();
        List<UserProjectCustom> userProjectCustomsProject = new ArrayList<>();
        List<UserProjectCustom> userProjectCustomsTask = new ArrayList<>();
        List<UserProjectCustom> userProjectCustomsChildTask = new ArrayList<>();
        for (UserProjectCustom userProjectCustom : userProjectCustomsAll) {
            switch (userProjectCustom.getType()) {
                case "1":
                    userProjectCustomsBudget.add(userProjectCustom);
                    break;
                case "2":
                    userProjectCustomsProject.add(userProjectCustom);
                    break;
                case "3":
                    userProjectCustomsTask.add(userProjectCustom);
                    break;
                case "4":
                    userProjectCustomsChildTask.add(userProjectCustom);
                    break;
                default:
                    log.warn("UserProjectCustom type is null  id {}", userProjectCustom.getId());
            }
        }

        if ("all".equals(status)) {
            String rootId = "0";

            for (ListTreeVO listTree : listAll) {
                String statusNew = "1";//默认状态为显示
                String id = listTree.getId();
                //1业务2 项目 3任务 4子任务
                int treeType = listTree.getType();
                String parentId = listTree.getParentId();

                switch (treeType) {
                    case 1:

                        //业务
                        for (UserProjectCustom userProjectCustom : userProjectCustomsBudget) {
                            String publicId = userProjectCustom.getBusinessid();

                            if (id.equals(publicId) && rootId.equals(parentId)) {
                                statusNew = userProjectCustom.getStatus();
                                break;
                            }
                        }
                        break;
                    case 2:
                        //项目
                        for (UserProjectCustom userProjectCustom : userProjectCustomsProject) {
                            String publicId = userProjectCustom.getProjectid();
                            String parentPublicId = userProjectCustom.getBusinessid();
                            if (id.equals(publicId) && parentId.equals(parentPublicId)) {
                                statusNew = userProjectCustom.getStatus();
                                break;
                            }
                        }
                        break;
                    case 3:
                        //任务
                        for (UserProjectCustom userProjectCustom : userProjectCustomsTask) {
                            String publicId = userProjectCustom.getTaskid();
                            String parentPublicId = userProjectCustom.getProjectid();
                            if (StringUtils.isEmpty(parentPublicId)) {
                                //业务任务
                                parentPublicId = userProjectCustom.getBusinessid();
                            }
                            if (id.equals(publicId) && parentId.equals(parentPublicId)) {
                                statusNew = userProjectCustom.getStatus();
                                break;
                            }
                        }
                        break;
                    case 4:
                        //子任务
                        for (UserProjectCustom userProjectCustom : userProjectCustomsChildTask) {
                            String publicId = userProjectCustom.getChildtaskid();
                            String parentPublicId = userProjectCustom.getTaskid();
                            if (id.equals(publicId) && parentId.equals(parentPublicId)) {
                                statusNew = userProjectCustom.getStatus();
                                break;
                            }
                        }
                        break;
                    default:
                        log.warn("treeType   is null  id {}", listTree.getId());
                }

                UserProjectCustomVO userProjectCustomVO = new UserProjectCustomVO(
                    id,
                    listTree.getName(),
                    listTree.getParentId(),
                    listTree.getUserId(),
                    listTree.getUserName(),
                    statusNew,
                    treeType,
                    listTree.getProjectType(),
                    listTree.getContractNO(),
                    listTree.getContractName(),
                    listTree.getContractOwner());
                // 业务加入到树中
                listNew.add(userProjectCustomVO);
            }
        } else if ("1".equals(status)) {
            for (ListTreeVO listTree : listAll) {
                String id = listTree.getId();
                int treetype = listTree.getType();
                UserProjectCustomVO userProjectCustomVO = new UserProjectCustomVO(id, listTree.getName(),
                    listTree.getParentId(), listTree.getUserId(), listTree.getUserName(), status, treetype,
                    listTree.getProjectType(), listTree.getContractNO(), listTree.getContractName(),
                    listTree.getContractOwner());
                // 业务加入到树中
                listNew.add(userProjectCustomVO);
            }

        } else {
            Set<String> showIdSet = new HashSet<>();
            List<UserProjectCustom> userProjectCustoms = userProjectCustomDao.findByStatus(status, userId);
            for (UserProjectCustom userProjectCustom : userProjectCustoms) {
                String publicId = "";
                String treeType = userProjectCustom.getType();
                String parentPublicId = "";
                if (StringUtils.equals(treeType, "1")) {
                    //业务
                    publicId = userProjectCustom.getBusinessid();
                    parentPublicId = "0";
                }
                if (StringUtils.equals(treeType, "2")) {
                    //项目
                    publicId = userProjectCustom.getProjectid();
                    parentPublicId = userProjectCustom.getBusinessid();
                }
                if (StringUtils.equals(treeType, "3")) {
                    //任务
                    publicId = userProjectCustom.getTaskid();
                    parentPublicId = userProjectCustom.getProjectid();
                    if (StringUtils.isEmpty(parentPublicId)) {
                        parentPublicId = userProjectCustom.getBusinessid();
                    }
                }
                if (StringUtils.equals(treeType, "4")) {
                    //子任务
                    publicId = userProjectCustom.getChildtaskid();
                    parentPublicId = userProjectCustom.getTaskid();
                }
                for (ListTreeVO listTree : listAll) {
                    String id = listTree.getId();
                    String parentId = listTree.getParentId();
                    if (id.equals(publicId) && parentId.equals(parentPublicId)) {
                        UserProjectCustomVO userProjectCustomVO = new UserProjectCustomVO(
                            id,
                            listTree.getName(),
                            listTree.getParentId(),
                            listTree.getUserId(),
                            listTree.getUserName(),
                            status,
                            Integer.parseInt(treeType),
                            listTree.getProjectType(),
                            listTree.getContractNO(),
                            listTree.getContractName(),
                            listTree.getContractOwner());
                        // 业务加入到树中
                        listNew.add(userProjectCustomVO);
                        showIdSet.add(id);
                        break;
                    }
                }
            }
            //如果只有子任务被隐藏，则需要执行3次下面的方法addParentNode找到 业务节点
            addParentNode(listAll, listNew, showIdSet);
            addParentNode(listAll, listNew, showIdSet);
            addParentNode(listAll, listNew, showIdSet);
        }
        return listNew;
    }

    private void addParentNode(List<ListTreeVO> listAll, List<UserProjectCustomVO> listNew, Set<String> showIdSet) {
        if (CollectionUtils.isNotEmpty(listNew)) {
            List<UserProjectCustomVO> tepListNew = new ArrayList<>();
            for (UserProjectCustomVO userProjectCustom : listNew) {
                for (ListTreeVO listTree : listAll) {
                    if (StringUtils.equals(userProjectCustom.getParentId(), listTree.getId()) && showIdSet
                        .add(listTree.getId())) {
                        UserProjectCustomVO userProjectCustomVO = new UserProjectCustomVO(
                            listTree.getId(),
                            listTree.getName(),
                            listTree.getParentId(),
                            listTree.getUserId(),
                            listTree.getUserName(),
                            "1",
                            listTree.getType(),
                            listTree.getProjectType(),
                            listTree.getContractNO(),
                            listTree.getContractName(),
                            listTree.getContractOwner());
                        // 业务加入到树中
                        tepListNew.add(userProjectCustomVO);
                        break;
                    }
                }
            }
            listNew.addAll(tepListNew);
        }
    }

    /***
     * 分页返回自定义树inx
     * @return
     * @param treeCustomPage
     */
    public PageInfo<UserProjectCustomVO> getCustomTreePage(TreeCustomPage treeCustomPage) {

        List<UserProjectCustomVO> customTreeVOList =
            this.getUserProjectCustomTree(
                treeCustomPage.getKeyword(),
                treeCustomPage.getType(),
                treeCustomPage.getStatus());
        //排序后返回
        List<UserProjectCustomVO> sortedList = sortList(customTreeVOList, treeCustomPage.getCloseIdList());
        //分页 移除不需要的叶子
        return pageList(sortedList, treeCustomPage);

    }

    //修改任务负责人为空的接口
    public void updateTaskApproveUser () throws Exception {
        List<UserProjectCustomVO> customTreeVOList =
                this.getUserProjectCustomTree(null, null, "all");
        for (UserProjectCustomVO vo : customTreeVOList) {
            if (vo.getType() == 3 && StringUtils.isEmpty(vo.getUserId())) {
                Task task = taskRepository.findById(vo.getId());
                //给任务负责人为空的设置默认的负责人
                String parentId = vo.getParentId();
                Project project = projectRepository.findById(parentId);
                if (StringUtils.isEmpty(project)) {
                    //业务任务
                    BudgetEO budgetEO = budgetEODao.selectByPrimaryKey(parentId);
                    task.setApproveUserId(budgetEO.getPm());
                    task.setApproveUserName(budgetEO.getUsname());
                } else {
                    //项目任务
                    task.setApproveUserId(project.getProjectLeaderId());
                    task.setApproveUserName(project.getProjectLeader());
                }
                taskRepository.save(task);
            }
        }
    }

    /**
     * 添加树关系
     *
     * @param treeNodeMap
     * @param id
     * @param parentId
     */
    private void addTreeNode(Map<String, TreeNode> treeNodeMap, String id, String parentId) {
        TreeNode treeNode = new TreeNode(id);
        TreeNode parentNode = treeNodeMap.get(parentId);
        if (null != parentNode) {
            parentNode.getChild().add(treeNode);
            treeNodeMap.put(id, treeNode);
        }
    }

    /**
     * 获取结果集的id顺序
     *
     * @param treeNode
     * @param idList
     */
    private void getAllNodeId(TreeNode treeNode, List<String> idList) {
        if (treeNode == null) {
            return;
        }
        idList.add(treeNode.getId());
        List<TreeNode> child = treeNode.getChild();
        for (TreeNode node : child) {
            getAllNodeId(node, idList);
        }
    }

    /**
     * 设置项目的 level ， leafFlag 和 树信息
     *
     * @param treeNodeMap
     * @param budgetMap
     * @param projectList
     */
    private void doSortListProjectList(Map<String, TreeNode> treeNodeMap,
        Map<String, UserProjectCustomVO> budgetMap,
        List<UserProjectCustomVO> projectList) {
        //所有项目
        for (UserProjectCustomVO vo : projectList) {
            String parentId = vo.getParentId();
            String id = vo.getId();
            int level;
            UserProjectCustomVO parentVO;

            level = 2;
            parentVO = budgetMap.get(parentId);
            if (null == parentVO) {
                //找不到父级，标记-1，后续进行删除
                vo.setTreeLevel(-1);
                log.error("查询业务异常  id {}  ", parentId);
                continue;
            }

            //父级设为非叶子节点
            parentVO.setLeafFlag(false);
            vo.setTreeLevel(level);
            /*
             * 添加 树 关系
             */
            addTreeNode(treeNodeMap, id, parentId);
        }
    }

    /**
     * 设置任务的 level ， leafFlag 和 树信息
     *
     * @param treeNodeMap
     * @param budgetMap
     * @param projectMap
     * @param taskList
     */
    private void doSortListTaskList(Map<String, TreeNode> treeNodeMap,
        Map<String, UserProjectCustomVO> budgetMap, Map<String, UserProjectCustomVO> projectMap,
        List<UserProjectCustomVO> taskList) {
        //所有任务
        for (UserProjectCustomVO vo : taskList) {
            String parentId = vo.getParentId();
            String id = vo.getId();
            int level;
            UserProjectCustomVO parentVO;
            //判断是否是业务任务
            if (budgetMap.containsKey(parentId)) {
                //业务任务层级为2
                level = 2;
                parentVO = budgetMap.get(parentId);
            } else {
                //项目任务层级为3
                level = 3;
                parentVO = projectMap.get(parentId);
            }

            if (null == parentVO) {
                //找不到父级，标记-1，后续进行删除
                vo.setTreeLevel(-1);
                String errorMassage;
                if (level == 2) {
                    errorMassage = " 业务";
                } else {
                    errorMassage = " 项目";
                }
                log.error("查询{}异常  id {}  ", errorMassage, parentId);
                continue;
            }

            //父级设为非叶子节点
            parentVO.setLeafFlag(false);
            vo.setTreeLevel(level);
            /*
             * 添加 树 关系
             */
            addTreeNode(treeNodeMap, id, parentId);

        }
    }

    /**
     * 设置子任务的 level ， leafFlag 和 树信息
     *
     * @param treeNodeMap
     * @param taskMap
     * @param childTaskList
     */
    private void doSortListChildTaskList(Map<String, TreeNode> treeNodeMap,
        Map<String, UserProjectCustomVO> taskMap,
        List<UserProjectCustomVO> childTaskList) {
        /*子任务一定是叶子*/
        for (UserProjectCustomVO vo : childTaskList) {
            String parentId = vo.getParentId();
            String id = vo.getId();
            int level;
            UserProjectCustomVO parentVO;
            parentVO = taskMap.get(parentId);

            if (null == parentVO) {
                //找不到父级，标记-1，后续进行删除
                vo.setTreeLevel(-1);
                log.error("查询任务异常  id {}  ", parentId);
                continue;
            }
            //判断是否是业务任务 , 业务任务下属子任务 层级为 3
            if (parentVO.getTreeLevel() == 2) {
                level = 3;
            } else {
                // 项目任务 下属层级 为 4
                level = 4;
            }
            //父级设为非叶子节点
            parentVO.setLeafFlag(false);
            vo.setTreeLevel(level);
            /*
             * 添加 树 关系
             */
            addTreeNode(treeNodeMap, id, parentId);
        }
    }

    /**
     * 将结果集进行分页，用于隐藏显示菜单
     *
     * @param sourceList
     * @param treeCustomPage
     * @return
     */
    public PageInfo<UserProjectCustomVO> pageList(List<UserProjectCustomVO> sourceList,
        TreeCustomPage treeCustomPage) {

        int pageSize = treeCustomPage.getPageSize();
        int pageNo = treeCustomPage.getPageNo();

        long count;
        long pageCount;
        int startIndex = (pageNo - 1) * pageSize;
        int endIndex = startIndex + pageSize;

        List<UserProjectCustomVO> resultList = new ArrayList<>();
        /*是否为业务分页*/
        if (treeCustomPage.isPageByBusiness()) {
            int i = 0;
            count = 0;
            Map<Integer, Integer> indexMap = new HashMap<>();
            //基于业务的分页逻辑
            for (UserProjectCustomVO vo : sourceList) {
                if (vo.getTreeLevel() == 1) {
                    indexMap.put((int) count, i);
                    //业务索引
                    count++;
                }
                //数组索引
                i++;
            }

            //计算总页数
            pageCount = count / pageSize + 1;

            if (indexMap.containsKey(startIndex)) {
                //有开始节点
                startIndex = indexMap.get(startIndex);
                if (indexMap.containsKey(endIndex)) {
                    //有结束节点
                    endIndex = indexMap.get(endIndex);
                } else {
                    //无结束节点
                    endIndex = sourceList.size();
                }
            } else {
                //无开始节点,直接将结束节点设为0
                endIndex = 0;
            }
        } else {
            //全量分页
            count = sourceList.size();
            pageCount = count / pageSize + 1;
            if (count < endIndex) {
                endIndex = (int) count;
            }
        }

        for (int i = startIndex; i < endIndex; i++) {
            resultList.add(sourceList.get(i));
        }

        PageInfo<UserProjectCustomVO> result = new PageInfo<>();
        result.setCount(count);
        result.setPageNo(pageNo);
        result.setPageSize(pageSize);
        result.setPageCount(pageCount);
        result.setList(resultList);

        return result;
    }

    /**
     * 将结果集进行排序，用于隐藏显示菜单
     *
     * @param sourceList
     * @return
     */
    public List<UserProjectCustomVO> sortList(List<UserProjectCustomVO> sourceList, List<String> closedIdList) {

        /*
         * 先按照 类型  和  首字母排序
         */
        Collections.sort(sourceList, new Comparator<UserProjectCustomVO>() {
            public int compare(UserProjectCustomVO o1, UserProjectCustomVO o2) {
                int type = o1.getType() - o2.getType();
                if (type != 0) {
                    //不同层级  业务-》项目-》任务-》子任务
                    return type;
                } else {
                    // 相同层级
                    // 根据首字母排序
                    return o1.getFirstSpell()
                             .compareTo(o2.getFirstSpell());

                }
            }
        });

        Map<String, UserProjectCustomVO> budgetMap = new HashMap<>();
        Map<String, UserProjectCustomVO> projectMap = new HashMap<>();
        Map<String, UserProjectCustomVO> taskMap = new HashMap<>();
        Map<String, UserProjectCustomVO> childTaskMap = new HashMap<>();

        List<UserProjectCustomVO> projectList = new ArrayList<>();
        List<UserProjectCustomVO> taskList = new ArrayList<>();
        List<UserProjectCustomVO> childTaskList = new ArrayList<>();



        /*
         *  索引
         */
        Map<String, TreeNode> treeNodeMap = new HashMap<>();
        TreeNode root = new TreeNode("0");
        treeNodeMap.put("0", root);


        /*初始化map*/
        for (UserProjectCustomVO vo : sourceList) {
            int level;
            //默认所有节点都是叶子
            vo.setLeafFlag(true);
            String id = vo.getId();
            vo.setName(vo.getName().trim());
            switch (vo.getType()) {
                case 1:
                    level = 1;
                    vo.setTreeLevel(level);
                    budgetMap.put(vo.getId(), vo);
                    /*
                     * 添加 树 关系
                     */
                    addTreeNode(treeNodeMap, id, "0");

                    break;
                case 2:
                    level = 2;
                    vo.setTreeLevel(level);
                    projectMap.put(vo.getId(), vo);
                    projectList.add(vo);

                    break;
                case 3:
                    taskMap.put(vo.getId(), vo);
                    taskList.add(vo);

                    break;
                case 4:
                    childTaskMap.put(vo.getId(), vo);
                    childTaskList.add(vo);

                    break;
                default:
            }
        }

        //所有项目
        doSortListProjectList(treeNodeMap, budgetMap, projectList);

        //所有任务
        doSortListTaskList(treeNodeMap, budgetMap, projectMap, taskList);

        //所有子任务
        doSortListChildTaskList(treeNodeMap, taskMap, childTaskList);

        List<UserProjectCustomVO> resultList = new ArrayList<>(sourceList.size());

        //移除关闭的节点,节点本身保留，只删除下级节点
        for (String s : closedIdList) {
            if (treeNodeMap.containsKey(s)) {
                treeNodeMap.get(s).getChild().clear();
            }
        }

        //将树的 id 转成list
        List<String> idList = new ArrayList<>(sourceList.size());
        List<TreeNode> child = root.getChild();
        for (TreeNode node : child) {
            getAllNodeId(node, idList);
        }

        //组装结果集
        Map<String, UserProjectCustomVO> voMap = new HashMap<>(sourceList.size());
        voMap.putAll(budgetMap);
        voMap.putAll(projectMap);
        voMap.putAll(taskMap);
        voMap.putAll(childTaskMap);
        for (String id : idList) {
            resultList.add(voMap.get(id));
        }

        return resultList;
    }

    /***
     * author zyh
     * @param UserProjectCustomVO
     * @param
     * @throws Exception
     */
    public void userProjectCustomInsertUpdate(UserProjectCustomVO UserProjectCustomVO) throws Exception {
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }

        if (StringUtils.isNotEmpty(UserProjectCustomVO)) {
            String updateStatus = UserProjectCustomVO.getStatus();
            if ("0".equals(updateStatus)) {
                //隐藏操作
                this.hideUserProjectCustomNew(UserProjectCustomVO, userId);
            } else {
                //显示操作
                this.showUserProjectCustom(UserProjectCustomVO, updateStatus, userId);
            }
        } else {
            throw new AdcDaBaseException("请选择要操作的业务数据！");
        }
    }

    private void showUserProjectCustom(UserProjectCustomVO userProjectCustomVO,
        String updateStatus, String userId) throws Exception {
        String treeType = userProjectCustomVO.getType() + "";//1业务2 项目 3任务 4子任务
        if (StringUtils.equals(treeType, "1")) {
            //业务
            String bussinessId = userProjectCustomVO.getId();
            this.showUserProjectCustomByBussinessId(bussinessId, userId, updateStatus);
        } else if (StringUtils.equals(treeType, "2")) {
            //项目
            //显示操作  那么查询上级状态上级状态修改为显示
            String projectId = userProjectCustomVO.getId();
            String bussinessId = userProjectCustomVO.getParentId();
            this.showUserProjectCustomByProjectId(projectId, userId, updateStatus);
            this.showUserProjectCustomByBussinessId(bussinessId, userId, updateStatus);
        } else if (StringUtils.equals(treeType, "3")) {
            //任务
            String taskId = userProjectCustomVO.getId();
            String parentId = userProjectCustomVO.getParentId();
            Project project = projectRepository.findById(parentId);
            this.showUserProjectCustomByTaskId(taskId, userId, updateStatus);
            if (StringUtils.isNotEmpty(project)) {
                //项目任务
                this.showUserProjectCustomByProjectId(parentId, userId, updateStatus);
                String bussinessId = project.getBudgetId();
                this.showUserProjectCustomByBussinessId(bussinessId, userId, updateStatus);
            } else {
                //业务任务
                this.showUserProjectCustomByBussinessId(parentId, userId, updateStatus);
            }
        } else if (StringUtils.equals(treeType, "4")) {
            //子任务
            String childTaskId = userProjectCustomVO.getId();
            ChildrenTask childrenTask = childTaskRepository.findById(childTaskId);
            String taskId = childrenTask.getTaskId();
            String projectId = "";
            String businessId = "";
            if (StringUtils.isNotEmpty(taskId)) {
                Task task = taskRepository.findById(taskId);
                projectId = task.getProjectId();
                businessId = task.getBudgetId();
                if (StringUtils.isEmpty(businessId)) {
                    Project project = projectRepository.findById(projectId);
                    businessId = (null != project) ? project.getBudgetId() : "";
                }
            }
            this.showUserProjectCustomByChildTaskId(childTaskId, userId, updateStatus);
            this.showUserProjectCustomByTaskId(taskId, userId, updateStatus);
            if (StringUtils.isNotEmpty(projectId)) {
                this.showUserProjectCustomByProjectId(projectId, userId, updateStatus);
            }
            this.showUserProjectCustomByBussinessId(businessId, userId, updateStatus);
        }
    }

    /**
     * @param bussinessId
     * @param userId
     * @author zyh
     * @description 业务显示操作
     */
    private void showUserProjectCustomByBussinessId(String bussinessId, String userId,
        String updateStatus) throws Exception {
        if (StringUtils.isEmpty(bussinessId)) {
            return;
        }
        UserProjectCustom userProjectCustom = userProjectCustomDao.
                                                                      findUserProjectCustomByBussinessId(
                                                                          bussinessId,
                                                                          "1",
                                                                          userId);
        if (StringUtils.isNotEmpty(userProjectCustom)) {
            userProjectCustom.setStatus(updateStatus);
            this.updateUserProjectCustom(userProjectCustom);
        } else {
            //没有在表中  插入表中
            //①  根据业务id查询业务
            //BudgetEO budgetEO = budgetEODao.selectByPrimaryKey(bussinessId);
            userProjectCustom = new UserProjectCustom();
            userProjectCustom.setUserid(userId);
            userProjectCustom.setBusinessid(bussinessId);
            userProjectCustom.setProjectid("");
            userProjectCustom.setTaskid("");
            userProjectCustom.setChildtaskid("");
            userProjectCustom.setStatus(updateStatus);
            userProjectCustom.setType("1");
            this.insertUserProjectCustom(userProjectCustom);
        }

    }

    /**
     * @param projectId
     * @param userId
     * @author zyh
     * @description 业务显示操作
     */
    private void showUserProjectCustomByProjectId(String projectId, String userId,
        String updateStatus) throws Exception {
        UserProjectCustom userProjectCustom = userProjectCustomDao.
                                                                      findUserProjectCustomByProjectId(
                                                                          projectId,
                                                                          "2",
                                                                          userId);
        if (StringUtils.isNotEmpty(userProjectCustom)) {
            userProjectCustom.setStatus(updateStatus);
            this.updateUserProjectCustom(userProjectCustom);
        } else {
            //表中没有数据  插入数据
            Project project = projectRepository.findById(projectId);
            userProjectCustom = new UserProjectCustom();
            userProjectCustom.setUserid(userId);
            userProjectCustom.setBusinessid(project.getBudgetId());
            userProjectCustom.setProjectid(projectId);
            userProjectCustom.setTaskid("");
            userProjectCustom.setChildtaskid("");
            userProjectCustom.setStatus(updateStatus);
            userProjectCustom.setType("2");
            this.insertUserProjectCustom(userProjectCustom);
        }

    }

    /**
     * @param taskId
     * @param userId
     * @author zyh
     * @description 业务显示操作
     */
    private void showUserProjectCustomByTaskId(String taskId, String userId,
        String updateStatus) throws Exception {
        UserProjectCustom userProjectCustom = userProjectCustomDao.
                                                                      findUserProjectCustomByTaskId(
                                                                          taskId,
                                                                          "3",
                                                                          userId);
        if (StringUtils.isNotEmpty(userProjectCustom)) {
            userProjectCustom.setStatus(updateStatus);
            this.updateUserProjectCustom(userProjectCustom);
        } else {
            //表中没有数据 插入数据
            Task task = taskRepository.findById(taskId);
            userProjectCustom = new UserProjectCustom();
            userProjectCustom.setUserid(userId);
            userProjectCustom.setBusinessid(task.getBudgetId());
            userProjectCustom.setProjectid(task.getProjectId());
            userProjectCustom.setTaskid(taskId);
            userProjectCustom.setChildtaskid("");
            userProjectCustom.setStatus(updateStatus);
            userProjectCustom.setType("3");
            this.insertUserProjectCustom(userProjectCustom);
        }

    }

    /**
     * @param childTaskId
     * @param userId
     * @author zyh
     * @description 业务显示操作
     */
    private void showUserProjectCustomByChildTaskId(String childTaskId, String userId,
        String updateStatus) throws Exception {
        UserProjectCustom userProjectCustom = userProjectCustomDao.
                                                                      findUserProjectCustomByChildtaskId(
                                                                          childTaskId,
                                                                          "4",
                                                                          userId);
        if (StringUtils.isNotEmpty(userProjectCustom)) {
            userProjectCustom.setStatus(updateStatus);
            this.updateUserProjectCustom(userProjectCustom);
        } else {
            //表中没有数据  插入
            ChildrenTask childrenTask = childTaskRepository.findById(childTaskId);
            userProjectCustom = new UserProjectCustom();
            userProjectCustom.setUserid(userId);
            userProjectCustom.setBusinessid(childrenTask.getBudgetId());
            userProjectCustom.setProjectid(childrenTask.getProjectId());
            userProjectCustom.setTaskid(childrenTask.getTaskId());
            userProjectCustom.setChildtaskid(childTaskId);
            userProjectCustom.setStatus(updateStatus);
            userProjectCustom.setType("4");
            this.insertUserProjectCustom(userProjectCustom);

        }

    }

    private void hideUserProjectCustomAll(UserProjectCustomVO userProjectCustomVO, String userId) throws Exception {
        //超级管理员和主任以及项目管理员看中心所有的业务
        Set<String> businessIds = new HashSet<>();
        Set<String> projectIds = new HashSet<>();
        Set<String> taskIds = new HashSet<>();
        Set<String> childTaskIds = new HashSet<>();

        String treeType = userProjectCustomVO.getType() + "";//1业务2 项目 3任务 4子任务
        UserProjectCustom userProjectCustom = null;
        String bussinessId = "";
        String projectId = "";
        String taskId = "";
        String childTaskId = "";

        UserProjectCustom tempUserProjectCustom = null;
        if (StringUtils.equals(treeType, "1")) {
            //业务
            bussinessId = userProjectCustomVO.getId();
            userProjectCustom = userProjectCustomDao.findUserProjectCustomByBussinessId(bussinessId, treeType, userId);
            if (StringUtils.isNotEmpty(userProjectCustom)) {
                userProjectCustom.setStatus("0");
                this.updateUserProjectCustom(userProjectCustom);
            } else {
                //新增加
                //表中没有数据  新添加
                userProjectCustom = new UserProjectCustom();
                userProjectCustom.setBusinessid(bussinessId);
                userProjectCustom.setProjectid(projectId);
                userProjectCustom.setTaskid(taskId);
                userProjectCustom.setChildtaskid(childTaskId);
                userProjectCustom.setType(treeType);
                userProjectCustom.setUserid(userId);
                userProjectCustom.setStatus("0");
                this.insertUserProjectCustom(userProjectCustom);
            }
            //该业务下的所有项目并且隐藏
            List<Project> projectList = projectRepository.findByBudgetId(bussinessId);
            if (projectList.size() > 0) {
                for (Project project : projectList) {
                    projectIds.add(project.getId());
                    projectId = project.getId();
                    tempUserProjectCustom = userProjectCustomDao
                        .findUserProjectCustomByProjectId(projectId, "2", userId);
                    userProjectCustom = new UserProjectCustom();
                    userProjectCustom.setBusinessid(bussinessId);
                    userProjectCustom.setProjectid(projectId);
                    userProjectCustom.setTaskid("");
                    userProjectCustom.setChildtaskid("");
                    userProjectCustom.setType("2");
                    userProjectCustom.setUserid(userId);
                    userProjectCustom.setStatus("0");
                    if (StringUtils.isNotEmpty(tempUserProjectCustom)) {
                        userProjectCustom.setId(tempUserProjectCustom.getId());
                        this.updateUserProjectCustom(userProjectCustom);
                    } else {
                        //新增加  隐藏操作
                        this.insertUserProjectCustom(userProjectCustom);
                    }
                }
                //该项目下的所有任务隐藏
                List<Task> projectTaskList = taskRepository.findByProjectIdInAndDelFlagNot(projectIds, Boolean.TRUE);
                for (Task projectTask : projectTaskList) {
                    taskIds.add(projectTask.getId());
                    tempUserProjectCustom = userProjectCustomDao
                        .findUserProjectCustomByTaskId(projectTask.getId(), "3", userId);
                    userProjectCustom = new UserProjectCustom();
                    userProjectCustom.setBusinessid(projectTask.getBudgetId());
                    userProjectCustom.setProjectid(projectTask.getProjectId());
                    userProjectCustom.setTaskid(projectTask.getId());
                    userProjectCustom.setChildtaskid("");
                    userProjectCustom.setType("3");
                    userProjectCustom.setUserid(userId);
                    userProjectCustom.setStatus("0");
                    if (StringUtils.isNotEmpty(tempUserProjectCustom)) {
                        userProjectCustom.setId(tempUserProjectCustom.getId());
                        this.updateUserProjectCustom(userProjectCustom);
                    } else {
                        //新增加
                        this.insertUserProjectCustom(userProjectCustom);
                    }
                }

            }
            //该业务下的所有任务隐藏
            List<Task> budgetTaskList = taskRepository.findByBudgetIdAndDelFlagNot(bussinessId, Boolean.TRUE);
            if (budgetTaskList.size() > 0) {
                for (Task budgetTask : budgetTaskList) {
                    taskIds.add(budgetTask.getId());
                    tempUserProjectCustom = userProjectCustomDao
                        .findUserProjectCustomByTaskId(budgetTask.getId(), "3", userId);
                    userProjectCustom = new UserProjectCustom();
                    userProjectCustom.setBusinessid(budgetTask.getBudgetId());
                    userProjectCustom.setProjectid(budgetTask.getProjectId());
                    userProjectCustom.setTaskid(budgetTask.getId());
                    userProjectCustom.setChildtaskid("");
                    userProjectCustom.setType("3");
                    userProjectCustom.setUserid(userId);
                    userProjectCustom.setStatus("0");
                    if (StringUtils.isNotEmpty(tempUserProjectCustom)) {
                        userProjectCustom.setId(tempUserProjectCustom.getId());
                        this.updateUserProjectCustom(userProjectCustom);
                    } else {
                        //新增加
                        this.insertUserProjectCustom(userProjectCustom);
                    }
                }
            }
            //所有任务下的子任务隐藏
            List<ChildrenTask> childTaskList = childTaskRepository.findByTaskIdInAndDelFlagNot(taskIds, Boolean.TRUE);
            for (ChildrenTask childrenTask : childTaskList) {
                tempUserProjectCustom = userProjectCustomDao
                    .findUserProjectCustomByChildtaskId(childrenTask.getId(), "4", userId);
                String taskId1 = childrenTask.getTaskId();
                String projectId1 = "";
                String budgetId = "";
                if (StringUtils.isNotEmpty(taskId1)) {
                    Task task = taskRepository.findById(taskId1);
                    projectId1 = task.getProjectId();
                    budgetId = task.getBudgetId();
                }

                userProjectCustom = new UserProjectCustom();
                userProjectCustom.setBusinessid(budgetId);
                userProjectCustom.setProjectid(projectId1);
                userProjectCustom.setTaskid(taskId1);
                userProjectCustom.setChildtaskid(childrenTask.getId());
                userProjectCustom.setType("4");
                userProjectCustom.setUserid(userId);
                userProjectCustom.setStatus("0");
                if (StringUtils.isNotEmpty(tempUserProjectCustom)) {
                    userProjectCustom.setId(tempUserProjectCustom.getId());
                    this.updateUserProjectCustom(userProjectCustom);
                } else {
                    //新增加
                    this.insertUserProjectCustom(userProjectCustom);
                }
            }
        } else if (StringUtils.equals(treeType, "2")) {
            //项目
            projectId = userProjectCustomVO.getId();
            bussinessId = userProjectCustomVO.getParentId();
            userProjectCustom = userProjectCustomDao.findUserProjectCustomByProjectId(projectId, treeType, userId);
            if (StringUtils.isNotEmpty(userProjectCustom)) {
                userProjectCustom.setStatus("0");
                this.updateUserProjectCustom(userProjectCustom);
            } else {
                //新增加
                //表中没有数据  新添加
                userProjectCustom = new UserProjectCustom();
                userProjectCustom.setBusinessid(bussinessId);
                userProjectCustom.setProjectid(projectId);
                userProjectCustom.setTaskid(taskId);
                userProjectCustom.setChildtaskid(childTaskId);
                userProjectCustom.setType(treeType);
                userProjectCustom.setUserid(userId);
                userProjectCustom.setStatus("0");
                this.insertUserProjectCustom(userProjectCustom);
            }
            //项目下的所有任务进行隐藏
            List<Task> taskList = taskRepository.findByProjectIdAndDelFlagNot(projectId, Boolean.TRUE);
            if (taskList.size() > 0) {
                for (Task task : taskList) {
                    taskIds.add(task.getId());
                    tempUserProjectCustom = userProjectCustomDao
                        .findUserProjectCustomByTaskId(task.getId(), "3", userId);
                    userProjectCustom = new UserProjectCustom();
                    userProjectCustom.setBusinessid(bussinessId);
                    userProjectCustom.setProjectid(projectId);
                    userProjectCustom.setTaskid(task.getId());
                    userProjectCustom.setChildtaskid("");
                    userProjectCustom.setType("3");
                    userProjectCustom.setUserid(userId);
                    userProjectCustom.setStatus("0");
                    if (StringUtils.isNotEmpty(tempUserProjectCustom)) {
                        userProjectCustom.setId(tempUserProjectCustom.getId());
                        this.updateUserProjectCustom(userProjectCustom);
                    } else {
                        //新增加
                        this.insertUserProjectCustom(userProjectCustom);
                    }
                }
                //任务下的所有子任务进行隐藏
                List<ChildrenTask> childrenTaskList = childTaskRepository
                    .findByTaskIdInAndDelFlagNot(taskIds, Boolean.TRUE);
                for (ChildrenTask childrenTask : childrenTaskList) {
                    tempUserProjectCustom = userProjectCustomDao
                        .findUserProjectCustomByChildtaskId(childrenTask.getId(), "4", userId);
                    String taskId1 = childrenTask.getTaskId();
                    String projectId1 = "";
                    String budgetId = "";
                    if (StringUtils.isNotEmpty(taskId1)) {
                        Task task = taskRepository.findById(taskId1);
                        projectId1 = task.getProjectId();
                        budgetId = task.getBudgetId();
                    }
                    userProjectCustom = new UserProjectCustom();
                    userProjectCustom.setBusinessid(budgetId);
                    userProjectCustom.setProjectid(projectId1);
                    userProjectCustom.setTaskid(taskId1);
                    userProjectCustom.setChildtaskid(childrenTask.getId());
                    userProjectCustom.setType("4");
                    userProjectCustom.setUserid(userId);
                    userProjectCustom.setStatus("0");
                    if (StringUtils.isNotEmpty(tempUserProjectCustom)) {
                        userProjectCustom.setId(tempUserProjectCustom.getId());
                        this.updateUserProjectCustom(userProjectCustom);
                    } else {
                        //新增加
                        this.insertUserProjectCustom(userProjectCustom);
                    }
                }
            }

        } else if (StringUtils.equals(treeType, "3")) {
            //任务
            taskId = userProjectCustomVO.getId();
            Task task = taskRepository.findById(taskId);
            bussinessId = task.getBudgetId();
            projectId = task.getProjectId();
            userProjectCustom = userProjectCustomDao.findUserProjectCustomByTaskId(taskId, treeType, userId);
            if (StringUtils.isNotEmpty(userProjectCustom)) {
                userProjectCustom.setStatus("0");
                this.updateUserProjectCustom(userProjectCustom);
            } else {
                //新增加
                //表中没有数据  新添加
                userProjectCustom = new UserProjectCustom();
                userProjectCustom.setBusinessid(bussinessId);
                userProjectCustom.setProjectid(projectId);
                userProjectCustom.setTaskid(taskId);
                userProjectCustom.setChildtaskid(childTaskId);
                userProjectCustom.setType(treeType);
                userProjectCustom.setUserid(userId);
                userProjectCustom.setStatus("0");
                this.insertUserProjectCustom(userProjectCustom);
            }
            //任务下的所有子任务
            List<ChildrenTask> childrenTaskList = childTaskRepository.findByTaskIdAndDelFlagNot(taskId, Boolean.TRUE);
            if (childrenTaskList.size() > 0) {
                for (ChildrenTask childrenTask : childrenTaskList) {
                    tempUserProjectCustom = userProjectCustomDao
                        .findUserProjectCustomByChildtaskId(childrenTask.getId(), "4", userId);
                    String taskId1 = childrenTask.getTaskId();
                    String projectId1 = "";
                    String budgetId = "";
                    if (StringUtils.isNotEmpty(taskId1)) {
                        Task task1 = taskRepository.findById(taskId1);
                        projectId1 = task1.getProjectId();
                        budgetId = task1.getBudgetId();
                    }
                    userProjectCustom = new UserProjectCustom();
                    userProjectCustom.setBusinessid(budgetId);
                    userProjectCustom.setProjectid(projectId1);
                    userProjectCustom.setTaskid(taskId1);
                    userProjectCustom.setChildtaskid(childrenTask.getId());
                    userProjectCustom.setType("4");
                    userProjectCustom.setUserid(userId);
                    userProjectCustom.setStatus("0");
                    if (StringUtils.isNotEmpty(tempUserProjectCustom)) {
                        userProjectCustom.setId(tempUserProjectCustom.getId());
                        this.updateUserProjectCustom(userProjectCustom);
                    } else {
                        //新增加
                        this.insertUserProjectCustom(userProjectCustom);
                    }
                }
            }
        } else if (StringUtils.equals(treeType, "4")) {
            //子任务
            childTaskId = userProjectCustomVO.getId();
            ChildrenTask childrenTask = childTaskRepository.findById(childTaskId);
            taskId = childrenTask.getTaskId();
            if (StringUtils.isNotEmpty(taskId)) {
                Task task = taskRepository.findById(taskId);
                bussinessId = task.getBudgetId();
                projectId = task.getProjectId();
            }
            userProjectCustom = userProjectCustomDao.findUserProjectCustomByChildtaskId(childTaskId, treeType, userId);
            if (StringUtils.isNotEmpty(userProjectCustom)) {
                userProjectCustom.setStatus("0");
                this.updateUserProjectCustom(userProjectCustom);
            } else {
                //新增加
                //表中没有数据  新添加
                userProjectCustom = new UserProjectCustom();
                userProjectCustom.setBusinessid(bussinessId);
                userProjectCustom.setProjectid(projectId);
                userProjectCustom.setTaskid(taskId);
                userProjectCustom.setChildtaskid(childTaskId);
                userProjectCustom.setType(treeType);
                userProjectCustom.setUserid(userId);
                userProjectCustom.setStatus("0");
                this.insertUserProjectCustom(userProjectCustom);
            }
        }
    }

    private String hideUserProjectCustomNew(UserProjectCustomVO userProjectCustomVO, String userId) throws Exception {
        Subject subject = SecurityUtils.getSubject();

        //超级管理员和主任以及项目管理员看中心所有的业务
        Set<String> businessIds = new HashSet<>();
        Set<String> projectIds = new HashSet<>();
        Set<String> taskIds = new HashSet<>();
        Set<String> childTaskIds = new HashSet<>();

        String treeType = userProjectCustomVO.getType() + "";//1业务2 项目 3任务 4子任务
        UserProjectCustom userProjectCustom = null;
        String bussinessId = "";
        String projectId = "";
        String taskId = "";
        String childTaskId = "";

        UserProjectCustom tempUserProjectCustom = null;

        /***
         * 超级管理员、主任、项目管理员、科委会成员、科研管理员都可以看到全部的子级
         */
        if (subject.hasRole(Role.SUPER_ADMIN) || subject.hasRole(Role.ZHU_REN) || subject.hasRole(Role.PROJECT_ADMIN)) {
            this.hideUserProjectCustomAll(userProjectCustomVO, userId);
            return "success";
        } else if (subject.hasRole(Role.COMMITTEE_MEMBER) || subject.hasRole(Role.RESEARCH_ADMIN)) {

            this.hideUserProjectCustomAll(userProjectCustomVO, userId);

        }

        if (subject.hasRole(Role.BEN_BU_ZHANG) || subject.hasRole(Role.BU_ZHANG)) {
            UserEO userWithRoles = userEOService.getUserWithRoles(userId);
            List<OrgEO> adcOwnOrgList = userWithRoles.getOrgEOList();
            //获取当前组织下的所有子机构
            List<String> orgIds = orgListDao.getOrgAndSubOrgIds(adcOwnOrgList.get(0).getId());

            List<UserEO> userEOList = userDao.selectByOrgIdAndUsName(orgIds, null);

            Set<String> userIdSet = new HashSet<>();

            for (UserEO eo : userEOList) {
                userIdSet.add(eo.getUsid());
            }
            List<UserWithProjects> userWithProjectsList = userWithProjectsRepository.findByUserIdIn(userIdSet);
            if (StringUtils.equals(treeType, "1")) {
                bussinessId = userProjectCustomVO.getId();
                userProjectCustom = userProjectCustomDao
                    .findUserProjectCustomByBussinessId(bussinessId, treeType, userId);
                if (StringUtils.isNotEmpty(userProjectCustom)) {
                    userProjectCustom.setStatus("0");
                    this.updateUserProjectCustom(userProjectCustom);
                } else {
                    //新增加
                    //表中没有数据  新添加
                    userProjectCustom = new UserProjectCustom();
                    userProjectCustom.setBusinessid(bussinessId);
                    userProjectCustom.setProjectid(projectId);
                    userProjectCustom.setTaskid(taskId);
                    userProjectCustom.setChildtaskid(childTaskId);
                    userProjectCustom.setType(treeType);
                    userProjectCustom.setUserid(userId);
                    userProjectCustom.setStatus("0");
                    this.insertUserProjectCustom(userProjectCustom);
                }

                for (UserWithProjects userWithProjects : userWithProjectsList) {
                    projectIds.addAll(userWithProjects.getProjectIds());
                    taskIds.addAll(userWithProjects.getTaskIds());
                    childTaskIds.addAll(userWithProjects.getChildTaskIds());
                }
                projectIds.removeAll(Collections.singleton(null));
                taskIds.removeAll(Collections.singleton(null));
                childTaskIds.removeAll(Collections.singleton(null));
                Set<String> projectIdSet = new HashSet<>();
                Set<String> taskIdSet = new HashSet<>();
                //所有项目
                if (CollectionUtils.isNotEmpty(projectIds)) {

                    List<List<String>> resultList = CommonUtil.splitList(new ArrayList<>(projectIds), 500);
                    List<Project> projectList = new ArrayList<>();
                    for (List<String> list : resultList) {
                        /*
                         * 过滤已删除的项目
                         */
                        projectList
                            .addAll(Lists.newArrayList(projectRepository.findByIdInAndDelFlagNot(list, Boolean.TRUE)));
                    }

                    for (Project project : projectList) {

                        // project有budget才能添加
                        if (bussinessId.equals(project.getBudgetId())) {
                            projectId = project.getId();
                            tempUserProjectCustom = userProjectCustomDao
                                .findUserProjectCustomByProjectId(projectId, "2", userId);
                            userProjectCustom = new UserProjectCustom();
                            userProjectCustom.setBusinessid(bussinessId);
                            userProjectCustom.setProjectid(projectId);
                            userProjectCustom.setTaskid("");
                            userProjectCustom.setChildtaskid("");
                            userProjectCustom.setType("2");
                            userProjectCustom.setUserid(userId);
                            userProjectCustom.setStatus("0");
                            if (StringUtils.isNotEmpty(tempUserProjectCustom)) {
                                userProjectCustom.setId(tempUserProjectCustom.getId());
                                this.updateUserProjectCustom(userProjectCustom);
                            } else {
                                //新增加  隐藏操作
                                this.insertUserProjectCustom(userProjectCustom);
                            }
                            projectIdSet.add(project.getId());
                        }
                    }
                }
                //所有任务
                if (CollectionUtils.isNotEmpty(taskIds)) {

                    List<List<String>> resultList = CommonUtil.splitList(new ArrayList<>(taskIds), 500);
                    List<Task> taskList = new ArrayList<>();
                    for (List<String> list : resultList) {
                        taskList.addAll(Lists.newArrayList(taskRepository.findByIdIn(list)));
                    }
                    for (Task task : taskList) {
                        //项目任务
                        if (StringUtils.isEmpty(task.getBudgetId()) && projectIdSet.contains(task.getProjectId())) {
                            taskId = task.getId();
                            tempUserProjectCustom = userProjectCustomDao
                                .findUserProjectCustomByTaskId(taskId, "3", userId);
                            userProjectCustom = new UserProjectCustom();
                            userProjectCustom.setBusinessid(bussinessId);
                            userProjectCustom.setProjectid(task.getProjectId());
                            userProjectCustom.setTaskid(taskId);
                            userProjectCustom.setChildtaskid("");
                            userProjectCustom.setType("3");//任务
                            userProjectCustom.setUserid(userId);
                            userProjectCustom.setStatus("0");
                            if (StringUtils.isNotEmpty(tempUserProjectCustom)) {
                                userProjectCustom.setId(tempUserProjectCustom.getId());
                                this.updateUserProjectCustom(userProjectCustom);
                            } else {
                                //新增加  隐藏操作
                                this.insertUserProjectCustom(userProjectCustom);
                            }
                            taskIdSet.add(taskId);
                        }
                        //业务任务
                        if (StringUtils.isNotEmpty(task.getBudgetId()) && bussinessId.equals(task.getBudgetId())) {
                            taskId = task.getId();
                            tempUserProjectCustom = userProjectCustomDao
                                .findUserProjectCustomByTaskId(taskId, "3", userId);
                            userProjectCustom = new UserProjectCustom();
                            userProjectCustom.setBusinessid(bussinessId);
                            userProjectCustom.setProjectid("");
                            userProjectCustom.setTaskid(taskId);
                            userProjectCustom.setChildtaskid("");
                            userProjectCustom.setType("3");//任务
                            userProjectCustom.setUserid(userId);
                            userProjectCustom.setStatus("0");
                            if (StringUtils.isNotEmpty(tempUserProjectCustom)) {
                                userProjectCustom.setId(tempUserProjectCustom.getId());
                                this.updateUserProjectCustom(userProjectCustom);
                            } else {
                                //新增加  隐藏操作
                                this.insertUserProjectCustom(userProjectCustom);
                            }
                            taskIdSet.add(taskId);
                        }
                    }
                }
                //所有子任务
                if (CollectionUtils.isNotEmpty(childTaskIds)) {

                    List<List<String>> resultList = CommonUtil.splitList(new ArrayList<>(childTaskIds), 500);
                    List<ChildrenTask> taskList = new ArrayList<>();
                    for (List<String> list : resultList) {
                        taskList.addAll(Lists.newArrayList(childTaskRepository.findByIdIn(list)));
                    }

                    for (ChildrenTask childrenTask : taskList) {

                        if (taskIdSet.contains(childrenTask.getTaskId())) {
                            childTaskId = childrenTask.getId();
                            tempUserProjectCustom = userProjectCustomDao
                                .findUserProjectCustomByChildtaskId(childTaskId, "4", userId);
                            taskId = childrenTask.getTaskId();
                            if (StringUtils.isNotEmpty(taskId)) {
                                Task task = taskRepository.findById(taskId);
                                projectId = task.getProjectId();
                                bussinessId = task.getBudgetId();
                            }
                            userProjectCustom = new UserProjectCustom();
                            userProjectCustom.setBusinessid(bussinessId);
                            userProjectCustom.setProjectid(projectId);
                            userProjectCustom.setTaskid(childrenTask.getTaskId());
                            userProjectCustom.setChildtaskid(childTaskId);
                            userProjectCustom.setType("4");//子任务
                            userProjectCustom.setUserid(userId);
                            userProjectCustom.setStatus("0");
                            if (StringUtils.isNotEmpty(tempUserProjectCustom)) {
                                userProjectCustom.setId(tempUserProjectCustom.getId());
                                this.updateUserProjectCustom(userProjectCustom);
                            } else {
                                //新增加  隐藏操作
                                this.insertUserProjectCustom(userProjectCustom);
                            }
                        }
                    }
                }

            } else if (StringUtils.equals(treeType, "2")) {
                //项目
                projectId = userProjectCustomVO.getId();
                bussinessId = userProjectCustomVO.getParentId();
                userProjectCustom = userProjectCustomDao.findUserProjectCustomByProjectId(projectId, "2", userId);
                if (StringUtils.isNotEmpty(userProjectCustom)) {
                    userProjectCustom.setStatus("0");
                    this.updateUserProjectCustom(userProjectCustom);
                } else {
                    //表中没有数据  新添加
                    userProjectCustom = new UserProjectCustom();
                    userProjectCustom.setBusinessid(bussinessId);
                    userProjectCustom.setProjectid(projectId);
                    userProjectCustom.setTaskid("");
                    userProjectCustom.setChildtaskid("");
                    userProjectCustom.setType("2");
                    userProjectCustom.setUserid(userId);
                    userProjectCustom.setStatus("0");
                    this.insertUserProjectCustom(userProjectCustom);
                }

                for (UserWithProjects userWithProjects : userWithProjectsList) {
                    taskIds.addAll(userWithProjects.getTaskIds());
                    childTaskIds.addAll(userWithProjects.getChildTaskIds());
                }
                taskIds.removeAll(Collections.singleton(null));
                childTaskIds.removeAll(Collections.singleton(null));
                Set<String> taskIdSet = new HashSet<>();
                //所有任务
                if (CollectionUtils.isNotEmpty(taskIds)) {
                    List<List<String>> resultList = CommonUtil.splitList(new ArrayList<>(taskIds), 500);
                    List<Task> taskList = new ArrayList<>();
                    for (List<String> list : resultList) {
                        taskList.addAll(Lists.newArrayList(taskRepository.findByIdIn(list)));
                    }
                    for (Task task : taskList) {
                        //项目任务
                        if (StringUtils.isEmpty(task.getBudgetId()) && projectId.equals(task.getProjectId())) {
                            taskId = task.getId();
                            tempUserProjectCustom = userProjectCustomDao
                                .findUserProjectCustomByTaskId(taskId, "3", userId);
                            userProjectCustom = new UserProjectCustom();
                            userProjectCustom.setBusinessid(bussinessId);
                            userProjectCustom.setProjectid(projectId);
                            userProjectCustom.setTaskid(taskId);
                            userProjectCustom.setChildtaskid("");
                            userProjectCustom.setType("3");//任务
                            userProjectCustom.setUserid(userId);
                            userProjectCustom.setStatus("0");
                            if (StringUtils.isNotEmpty(tempUserProjectCustom)) {
                                userProjectCustom.setId(tempUserProjectCustom.getId());
                                this.updateUserProjectCustom(userProjectCustom);
                            } else {
                                //新增加  隐藏操作
                                this.insertUserProjectCustom(userProjectCustom);
                            }
                            taskIdSet.add(taskId);
                        }
                    }
                }
                //所有子任务
                if (CollectionUtils.isNotEmpty(childTaskIds)) {

                    List<List<String>> resultList = CommonUtil.splitList(new ArrayList<>(childTaskIds), 500);
                    List<ChildrenTask> taskList = new ArrayList<>();
                    for (List<String> list : resultList) {
                        taskList.addAll(Lists.newArrayList(childTaskRepository.findByIdIn(list)));
                    }

                    for (ChildrenTask childrenTask : taskList) {

                        if (taskIdSet.contains(childrenTask.getTaskId())) {
                            childTaskId = childrenTask.getId();
                            tempUserProjectCustom = userProjectCustomDao
                                .findUserProjectCustomByChildtaskId(childTaskId, "4", userId);
                            taskId = childrenTask.getTaskId();
                            if (StringUtils.isNotEmpty(taskId)) {
                                Task task = taskRepository.findById(taskId);
                                projectId = task.getProjectId();
                                bussinessId = task.getBudgetId();
                            }
                            userProjectCustom = new UserProjectCustom();
                            userProjectCustom.setBusinessid(bussinessId);
                            userProjectCustom.setProjectid(projectId);
                            userProjectCustom.setTaskid(childrenTask.getTaskId());
                            userProjectCustom.setChildtaskid(childTaskId);
                            userProjectCustom.setType("4");//子任务
                            userProjectCustom.setUserid(userId);
                            userProjectCustom.setStatus("0");
                            if (StringUtils.isNotEmpty(tempUserProjectCustom)) {
                                userProjectCustom.setId(tempUserProjectCustom.getId());
                                this.updateUserProjectCustom(userProjectCustom);
                            } else {
                                //新增加  隐藏操作
                                this.insertUserProjectCustom(userProjectCustom);
                            }
                        }
                    }
                }

            } else if (StringUtils.equals(treeType, "3")) {
                //任务
                taskId = userProjectCustomVO.getId();
                userProjectCustom = userProjectCustomDao.findUserProjectCustomByTaskId(taskId, "3", userId);
                if (StringUtils.isNotEmpty(userProjectCustom)) {
                    userProjectCustom.setStatus("0");
                    this.updateUserProjectCustom(userProjectCustom);
                } else {
                    //表中没有数据  新添加
                    Task task = taskRepository.findById(taskId);
                    userProjectCustom = new UserProjectCustom();
                    userProjectCustom.setBusinessid(task.getBudgetId());
                    userProjectCustom.setProjectid(task.getProjectId());
                    userProjectCustom.setTaskid(taskId);
                    userProjectCustom.setChildtaskid("");
                    userProjectCustom.setType("3");
                    userProjectCustom.setUserid(userId);
                    userProjectCustom.setStatus("0");
                    this.insertUserProjectCustom(userProjectCustom);
                }

                for (UserWithProjects userWithProjects : userWithProjectsList) {
                    childTaskIds.addAll(userWithProjects.getChildTaskIds());
                }
                childTaskIds.removeAll(Collections.singleton(null));
                //所有子任务
                if (CollectionUtils.isNotEmpty(childTaskIds)) {

                    List<List<String>> resultList = CommonUtil.splitList(new ArrayList<>(childTaskIds), 500);
                    List<ChildrenTask> taskList = new ArrayList<>();
                    for (List<String> list : resultList) {
                        taskList.addAll(Lists.newArrayList(childTaskRepository.findByIdIn(list)));
                    }

                    for (ChildrenTask childrenTask : taskList) {

                        if (taskId.equals(childrenTask.getTaskId())) {
                            childTaskId = childrenTask.getId();
                            tempUserProjectCustom = userProjectCustomDao
                                .findUserProjectCustomByChildtaskId(childTaskId, "4", userId);
                            taskId = childrenTask.getTaskId();
                            if (StringUtils.isNotEmpty(taskId)) {
                                Task task = taskRepository.findById(taskId);
                                projectId = task.getProjectId();
                                bussinessId = task.getBudgetId();
                            }
                            userProjectCustom = new UserProjectCustom();
                            userProjectCustom.setBusinessid(bussinessId);
                            userProjectCustom.setProjectid(projectId);
                            userProjectCustom.setTaskid(childrenTask.getTaskId());
                            userProjectCustom.setChildtaskid(childTaskId);
                            userProjectCustom.setType("4");//子任务
                            userProjectCustom.setUserid(userId);
                            userProjectCustom.setStatus("0");
                            if (StringUtils.isNotEmpty(tempUserProjectCustom)) {
                                userProjectCustom.setId(tempUserProjectCustom.getId());
                                this.updateUserProjectCustom(userProjectCustom);
                            } else {
                                //新增加  隐藏操作
                                this.insertUserProjectCustom(userProjectCustom);
                            }
                        }
                    }
                }
            } else if (StringUtils.equals(treeType, "4")) {
                //子任务
                childTaskId = userProjectCustomVO.getId();
                userProjectCustom = userProjectCustomDao.findUserProjectCustomByChildtaskId(childTaskId, "4", userId);
                if (StringUtils.isNotEmpty(userProjectCustom)) {
                    userProjectCustom.setStatus("0");
                    this.updateUserProjectCustom(userProjectCustom);
                } else {
                    //表中没有数据  新添加
                    ChildrenTask childrenTask = childTaskRepository.findById(childTaskId);
                    taskId = childrenTask.getTaskId();
                    if (StringUtils.isNotEmpty(taskId)) {
                        Task task = taskRepository.findById(taskId);
                        projectId = task.getProjectId();
                        bussinessId = task.getBudgetId();
                    }
                    userProjectCustom = new UserProjectCustom();
                    userProjectCustom.setBusinessid(bussinessId);
                    userProjectCustom.setProjectid(projectId);
                    userProjectCustom.setTaskid(childrenTask.getTaskId());
                    userProjectCustom.setChildtaskid(childTaskId);
                    userProjectCustom.setType("4");
                    userProjectCustom.setUserid(userId);
                    userProjectCustom.setStatus("0");
                    this.insertUserProjectCustom(userProjectCustom);
                }

            }
            return "success";
        } else {
            UserWithProjects userWithProjects = userWithProjectsRepository.findOne(userId);
            //员工查看自己的
            if (treeType.equals("1")) {
                bussinessId = userProjectCustomVO.getId();
                userProjectCustom = userProjectCustomDao
                    .findUserProjectCustomByBussinessId(bussinessId, treeType, userId);
                if (null != userProjectCustom) {
                    userProjectCustom.setStatus("0");
                    this.updateUserProjectCustom(userProjectCustom);
                } else {
                    //新增加
                    //表中没有数据  新添加
                    userProjectCustom = new UserProjectCustom();
                    userProjectCustom.setBusinessid(bussinessId);
                    userProjectCustom.setProjectid(projectId);
                    userProjectCustom.setTaskid(taskId);
                    userProjectCustom.setChildtaskid(childTaskId);
                    userProjectCustom.setType(treeType);
                    userProjectCustom.setUserid(userId);
                    userProjectCustom.setStatus("0");
                    this.insertUserProjectCustom(userProjectCustom);
                }
                projectIds.addAll(userWithProjects.getProjectIds());
                taskIds.addAll(userWithProjects.getTaskIds());
                childTaskIds.addAll(userWithProjects.getChildTaskIds());

                projectIds.removeAll(Collections.singleton(null));
                taskIds.removeAll(Collections.singleton(null));
                childTaskIds.removeAll(Collections.singleton(null));

                Set<String> projectIdSet = new HashSet<>();
                Set<String> taskIdSet = new HashSet<>();
                //所有项目
                if (CollectionUtils.isNotEmpty(projectIds)) {

                    List<List<String>> resultList = CommonUtil.splitList(new ArrayList<>(projectIds), 500);
                    List<Project> projectList = new ArrayList<>();
                    for (List<String> list : resultList) {
                        /*
                         * 过滤已删除的项目
                         */
                        projectList
                            .addAll(Lists.newArrayList(projectRepository.findByIdInAndDelFlagNot(list, Boolean.TRUE)));
                    }

                    for (Project project : projectList) {

                        // project有budget才能添加
                        if (bussinessId.equals(project.getBudgetId())) {
                            projectId = project.getId();
                            tempUserProjectCustom = userProjectCustomDao
                                .findUserProjectCustomByProjectId(projectId, "2", userId);
                            userProjectCustom = new UserProjectCustom();
                            userProjectCustom.setBusinessid(bussinessId);
                            userProjectCustom.setProjectid(projectId);
                            userProjectCustom.setTaskid("");
                            userProjectCustom.setChildtaskid("");
                            userProjectCustom.setType("2");
                            userProjectCustom.setUserid(userId);
                            userProjectCustom.setStatus("0");
                            if (StringUtils.isNotEmpty(tempUserProjectCustom)) {
                                userProjectCustom.setId(tempUserProjectCustom.getId());
                                this.updateUserProjectCustom(userProjectCustom);
                            } else {
                                //新增加  隐藏操作
                                this.insertUserProjectCustom(userProjectCustom);
                            }
                            projectIdSet.add(project.getId());
                        }
                    }
                }
                //所有任务
                if (CollectionUtils.isNotEmpty(taskIds)) {

                    List<List<String>> resultList = CommonUtil.splitList(new ArrayList<>(taskIds), 500);
                    List<Task> taskList = new ArrayList<>();
                    for (List<String> list : resultList) {
                        taskList.addAll(Lists.newArrayList(taskRepository.findByIdInAndDelFlagNot(list, Boolean.TRUE)));
                    }
                    for (Task task : taskList) {
                        //项目任务
                        if (StringUtils.isEmpty(task.getBudgetId()) && projectIdSet.contains(task.getProjectId())) {
                            taskId = task.getId();
                            tempUserProjectCustom = userProjectCustomDao
                                .findUserProjectCustomByTaskId(taskId, "3", userId);
                            userProjectCustom = new UserProjectCustom();
                            userProjectCustom.setBusinessid(bussinessId);
                            userProjectCustom.setProjectid(task.getProjectId());
                            userProjectCustom.setTaskid(taskId);
                            userProjectCustom.setChildtaskid("");
                            userProjectCustom.setType("3");//任务
                            userProjectCustom.setUserid(userId);
                            userProjectCustom.setStatus("0");
                            if (StringUtils.isNotEmpty(tempUserProjectCustom)) {
                                userProjectCustom.setId(tempUserProjectCustom.getId());
                                this.updateUserProjectCustom(userProjectCustom);
                            } else {
                                //新增加  隐藏操作
                                this.insertUserProjectCustom(userProjectCustom);
                            }
                            taskIdSet.add(taskId);
                        }
                        //业务任务
                        if (StringUtils.isNotEmpty(task.getBudgetId()) && bussinessId.equals(task.getBudgetId())) {
                            taskId = task.getId();
                            tempUserProjectCustom = userProjectCustomDao
                                .findUserProjectCustomByTaskId(taskId, "3", userId);
                            userProjectCustom = new UserProjectCustom();
                            userProjectCustom.setBusinessid(bussinessId);
                            userProjectCustom.setProjectid("");
                            userProjectCustom.setTaskid(taskId);
                            userProjectCustom.setChildtaskid("");
                            userProjectCustom.setType("3");//任务
                            userProjectCustom.setUserid(userId);
                            userProjectCustom.setStatus("0");
                            if (StringUtils.isNotEmpty(tempUserProjectCustom)) {
                                userProjectCustom.setId(tempUserProjectCustom.getId());
                                this.updateUserProjectCustom(userProjectCustom);
                            } else {
                                //新增加  隐藏操作
                                this.insertUserProjectCustom(userProjectCustom);
                            }
                            taskIdSet.add(taskId);
                        }
                    }
                }
                //所有子任务
                if (CollectionUtils.isNotEmpty(childTaskIds)) {

                    List<List<String>> resultList = CommonUtil.splitList(new ArrayList<>(childTaskIds), 500);
                    List<ChildrenTask> taskList = new ArrayList<>();
                    for (List<String> list : resultList) {
                        taskList.addAll(Lists
                            .newArrayList(childTaskRepository.findByIdInAndDelFlagNot(list, Boolean.TRUE)));
                    }

                    for (ChildrenTask childrenTask : taskList) {

                        if (taskIdSet.contains(childrenTask.getTaskId())) {
                            childTaskId = childrenTask.getId();
                            tempUserProjectCustom = userProjectCustomDao
                                .findUserProjectCustomByChildtaskId(childTaskId, "4", userId);
                            taskId = childrenTask.getTaskId();
                            if (StringUtils.isNotEmpty(taskId)) {
                                Task task = taskRepository.findById(taskId);
                                projectId = task.getProjectId();
                            }
                            userProjectCustom = new UserProjectCustom();
                            userProjectCustom.setBusinessid(bussinessId);
                            userProjectCustom.setProjectid(projectId);
                            userProjectCustom.setTaskid(childrenTask.getTaskId());
                            userProjectCustom.setChildtaskid(childTaskId);
                            userProjectCustom.setType("4");//子任务
                            userProjectCustom.setUserid(userId);
                            userProjectCustom.setStatus("0");
                            if (StringUtils.isNotEmpty(tempUserProjectCustom)) {
                                userProjectCustom.setId(tempUserProjectCustom.getId());
                                this.updateUserProjectCustom(userProjectCustom);
                            } else {
                                //新增加  隐藏操作
                                this.insertUserProjectCustom(userProjectCustom);
                            }
                        }
                    }
                }
            } else if (treeType.equals("2")) {
                //项目
                projectId = userProjectCustomVO.getId();
                bussinessId = userProjectCustomVO.getParentId();
                userProjectCustom = userProjectCustomDao.findUserProjectCustomByProjectId(projectId, treeType, userId);
                if (StringUtils.isNotEmpty(userProjectCustom)) {
                    userProjectCustom.setStatus("0");
                    this.updateUserProjectCustom(userProjectCustom);
                } else {
                    //表中没有数据  新添加
                    userProjectCustom = new UserProjectCustom();
                    userProjectCustom.setBusinessid(bussinessId);
                    userProjectCustom.setProjectid(projectId);
                    userProjectCustom.setTaskid(taskId);
                    userProjectCustom.setChildtaskid(childTaskId);
                    userProjectCustom.setType(treeType);
                    userProjectCustom.setUserid(userId);
                    userProjectCustom.setStatus("0");
                    this.insertUserProjectCustom(userProjectCustom);
                }
                taskIds.addAll(userWithProjects.getTaskIds());
                childTaskIds.addAll(userWithProjects.getChildTaskIds());

                taskIds.removeAll(Collections.singleton(null));
                childTaskIds.removeAll(Collections.singleton(null));

                Set<String> taskIdSet = new HashSet<>();

                //所有任务
                if (CollectionUtils.isNotEmpty(taskIds)) {

                    List<List<String>> resultList = CommonUtil.splitList(new ArrayList<>(taskIds), 500);
                    List<Task> taskList = new ArrayList<>();
                    for (List<String> list : resultList) {
                        taskList.addAll(Lists.newArrayList(taskRepository.findByIdInAndDelFlagNot(list,Boolean.TRUE)));
                    }
                    for (Task task : taskList) {
                        //项目任务
                        if (StringUtils.isEmpty(task.getBudgetId()) && projectId.equals(task.getProjectId())) {
                            taskId = task.getId();
                            tempUserProjectCustom = userProjectCustomDao
                                .findUserProjectCustomByTaskId(taskId, "3", userId);
                            userProjectCustom = new UserProjectCustom();
                            userProjectCustom.setBusinessid(bussinessId);
                            userProjectCustom.setProjectid(projectId);
                            userProjectCustom.setTaskid(taskId);
                            userProjectCustom.setChildtaskid("");
                            userProjectCustom.setType("3");//任务
                            userProjectCustom.setUserid(userId);
                            userProjectCustom.setStatus("0");
                            if (StringUtils.isNotEmpty(tempUserProjectCustom)) {
                                userProjectCustom.setId(tempUserProjectCustom.getId());
                                this.updateUserProjectCustom(userProjectCustom);
                            } else {
                                //新增加  隐藏操作
                                this.insertUserProjectCustom(userProjectCustom);
                            }
                            taskIdSet.add(taskId);
                        }
                    }
                }
                //所有子任务
                if (CollectionUtils.isNotEmpty(childTaskIds)) {
                    List<List<String>> resultList = CommonUtil.splitList(new ArrayList<>(childTaskIds), 500);
                    List<ChildrenTask> taskList = new ArrayList<>();
                    for (List<String> list : resultList) {
                        taskList.addAll(Lists.newArrayList(childTaskRepository.findByIdInAndDelFlagNot(list,Boolean.TRUE)));
                    }

                    for (ChildrenTask childrenTask : taskList) {

                        if (taskIdSet.contains(childrenTask.getTaskId())) {
                            childTaskId = childrenTask.getId();
                            tempUserProjectCustom = userProjectCustomDao
                                .findUserProjectCustomByChildtaskId(childTaskId, "4", userId);
                            taskId = childrenTask.getTaskId();
                            if (StringUtils.isNotEmpty(taskId)) {
                                Task task = taskRepository.findById(taskId);
                                projectId = task.getProjectId();
                            }
                            userProjectCustom = new UserProjectCustom();
                            userProjectCustom.setBusinessid(bussinessId);
                            userProjectCustom.setProjectid(projectId);
                            userProjectCustom.setTaskid(childrenTask.getTaskId());
                            userProjectCustom.setChildtaskid(childTaskId);
                            userProjectCustom.setType("4");//子任务
                            userProjectCustom.setUserid(userId);
                            userProjectCustom.setStatus("0");
                            if (StringUtils.isNotEmpty(tempUserProjectCustom)) {
                                userProjectCustom.setId(tempUserProjectCustom.getId());
                                this.updateUserProjectCustom(userProjectCustom);
                            } else {
                                //新增加  隐藏操作
                                this.insertUserProjectCustom(userProjectCustom);
                            }
                        }
                    }
                }

            } else if (treeType.equals("3")) {
                taskId = userProjectCustomVO.getId();
                userProjectCustom = userProjectCustomDao.findUserProjectCustomByTaskId(taskId, treeType, userId);
                if (StringUtils.isNotEmpty(userProjectCustom)) {
                    userProjectCustom.setStatus("0");
                    this.updateUserProjectCustom(userProjectCustom);
                } else {
                    //表中没有数据  新添加
                    Task task = taskRepository.findById(taskId);
                    userProjectCustom = new UserProjectCustom();
                    userProjectCustom.setBusinessid(task.getBudgetId());
                    userProjectCustom.setProjectid(task.getProjectId());
                    userProjectCustom.setTaskid(taskId);
                    userProjectCustom.setChildtaskid("");
                    userProjectCustom.setType(treeType);
                    userProjectCustom.setUserid(userId);
                    userProjectCustom.setStatus("0");
                    this.insertUserProjectCustom(userProjectCustom);
                }
                childTaskIds.addAll(userWithProjects.getChildTaskIds());

                childTaskIds.removeAll(Collections.singleton(null));
                //所有子任务
                if (CollectionUtils.isNotEmpty(childTaskIds)) {

                    List<List<String>> resultList = CommonUtil.splitList(new ArrayList<>(childTaskIds), 500);
                    List<ChildrenTask> taskList = new ArrayList<>();
                    for (List<String> list : resultList) {
                        taskList.addAll(Lists.newArrayList(childTaskRepository.findByIdIn(list)));
                    }

                    for (ChildrenTask childrenTask : taskList) {

                        if (taskId.equals(childrenTask.getTaskId())) {
                            childTaskId = childrenTask.getId();
                            tempUserProjectCustom = userProjectCustomDao
                                .findUserProjectCustomByChildtaskId(childTaskId, "4", userId);
                            taskId = childrenTask.getTaskId();
                            if (StringUtils.isNotEmpty(taskId)) {
                                Task task = taskRepository.findById(taskId);
                                projectId = task.getProjectId();
                                bussinessId = task.getBudgetId();
                            }
                            userProjectCustom = new UserProjectCustom();
                            userProjectCustom.setBusinessid(bussinessId);
                            userProjectCustom.setProjectid(projectId);
                            userProjectCustom.setTaskid(taskId);
                            userProjectCustom.setChildtaskid(childTaskId);
                            userProjectCustom.setType("4");//子任务
                            userProjectCustom.setUserid(userId);
                            userProjectCustom.setStatus("0");
                            if (StringUtils.isNotEmpty(tempUserProjectCustom)) {
                                userProjectCustom.setId(tempUserProjectCustom.getId());
                                this.updateUserProjectCustom(userProjectCustom);
                            } else {
                                //新增加  隐藏操作
                                this.insertUserProjectCustom(userProjectCustom);
                            }
                        }
                    }
                }

            } else if (treeType.equals("4")) {
                childTaskId = userProjectCustomVO.getId();
                userProjectCustom = userProjectCustomDao
                    .findUserProjectCustomByChildtaskId(childTaskId, treeType, userId);
                if (StringUtils.isNotEmpty(userProjectCustom)) {
                    userProjectCustom.setStatus("0");
                    this.updateUserProjectCustom(userProjectCustom);
                } else {
                    //表中没有数据  新添加
                    ChildrenTask childrenTask = childTaskRepository.findById(childTaskId);
                    taskId = childrenTask.getTaskId();
                    if (StringUtils.isNotEmpty(taskId)) {
                        Task task = taskRepository.findById(taskId);
                        projectId = task.getProjectId();
                        bussinessId = task.getBudgetId();
                    }
                    userProjectCustom = new UserProjectCustom();
                    userProjectCustom.setBusinessid(bussinessId);
                    userProjectCustom.setProjectid(projectId);
                    userProjectCustom.setTaskid(taskId);
                    userProjectCustom.setChildtaskid(childTaskId);
                    userProjectCustom.setType(treeType);
                    userProjectCustom.setUserid(userId);
                    userProjectCustom.setStatus("0");
                    this.insertUserProjectCustom(userProjectCustom);
                }
            }
            return "success";
        }
    }

    public void insertUserProjectCustom(UserProjectCustom userProjectCustom) throws Exception {
        userProjectCustom.setId(UUID.randomUUID10());
        int result = userProjectCustomDao.insert(userProjectCustom);
        if (result > 0) {} else {
            throw new AdcDaBaseException("隐藏失败");
        }
    }

    public void updateUserProjectCustom(UserProjectCustom userProjectCustom) throws Exception {
        int num = userProjectCustomDao.update(userProjectCustom);
        if (num > 0) {} else {
            throw new AdcDaBaseException("更新失败");
        }
    }

}
