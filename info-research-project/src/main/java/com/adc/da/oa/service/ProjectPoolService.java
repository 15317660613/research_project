package com.adc.da.oa.service;

import com.adc.da.base.entity.TreeEntity;
import com.adc.da.budget.constant.Role;
import com.adc.da.budget.dao.BudgetEODao;
import com.adc.da.budget.dao.OrgListDao;
import com.adc.da.budget.entity.*;
import com.adc.da.budget.repository.ProjectRepository;
import com.adc.da.budget.repository.TaskRepository;
import com.adc.da.budget.repository.UserWithProjectsRepository;
import com.adc.da.budget.utils.BeanUtils;
import com.adc.da.budget.utils.CommonUtil;
import com.adc.da.login.util.UserUtils;
import com.adc.da.oa.entity.BaseMemberEO;
import com.adc.da.oa.page.ProjectPoolPage;
import com.adc.da.oa.vo.ClaimDeptVO;
import com.adc.da.oa.vo.ClaimMemberVO;
import com.adc.da.oa.vo.ClaimVO;
import com.adc.da.sys.dao.OrgEODao;
import com.adc.da.sys.dao.UserEODao;
import com.adc.da.sys.entity.DicTypeEO;
import com.adc.da.sys.entity.OrgEO;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.sys.service.DicTypeEOService;
import com.adc.da.sys.service.UserEOService;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.http.PageInfo;
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
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.adc.da.budget.constant.ProjectSearchField.*;

/**
 * 数据同步-业务
 */
@Service
@Slf4j
public class ProjectPoolService {
    private static final String DEL_FLAG = "delFlag";

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private OrgListDao orgListDao;

    @Autowired
    private UserWithProjectsRepository userWithProjectsRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserEOService userEOService;

    @Autowired
    private UserEODao userEODao;

    @Autowired
    private BudgetEODao budgetEODao;
    @Autowired
    private DicTypeEOService dicTypeEOService;



    /**
     * 登录用户
     *
     * @return
     */
    private String getUserId() {
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("登录过期");
        }
        return userId;
    }

    /**
     * 获取部门信息，根据部门权重排序
     *
     * @return
     */
    private List<OrgWithLevelEO> getOrgInfo() {

        List<OrgWithLevelEO> orgList = orgListDao.getUserOrgWhitLeafAndLev(this.getUserId());
        if (CollectionUtils.isEmpty(orgList)) {
            throw new AdcDaBaseException("该用户无所属组织");
        }

        return orgList;
    }

    @Autowired
    private OrgEODao orgEODao;

    /**
     * 返回由 id，Name 组成的map
     *
     * @return
     */
    private Map<String, String> initDeptIdNameMap() {
        List<OrgEO> orgEOList = orgEODao.queryOrgAll();
        return orgEOList.stream().collect(Collectors
            .toMap(TreeEntity::getId, TreeEntity::getName, (a, b) -> b));

    }

    /**
     * @param page
     * @return
     */
    public PageInfo<Project> queryByPage(ProjectPoolPage page) {
        BoolQueryBuilder qb = QueryBuilders.boolQuery();
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        Subject subject = SecurityUtils.getSubject();

        //管理员权限判断
        boolean adminRole = subject.hasRole(Role.SUPER_ADMIN)
            || subject.hasRole(Role.ADMIN)
            || subject.hasRole(Role.PROJECT_ADMIN);

        boolean zhuRenRole = subject.hasRole(Role.ZHU_REN);

        /*
         * 管理员查看所有未申领的项目
         */
        if ((!adminRole && !zhuRenRole)) {

            /*
             * 查询上级组织第一个（即到组级别）
             * 主任级账户不能这样取
             */
            OrgWithLevelEO org = getOrgInfo().get(0);
            /*
             * 所属组织父级id集合
             */
            String[] orgParentIds = org.getParentIds().split(",");
            int parentIdSize = orgParentIds.length;
            String deptId;


            /* oa合同不存在部门以下，即不可能是组 */
            if (4 < parentIdSize) {
                deptId = orgParentIds[4];
            } else {
                deptId = org.getId();
            }
            /*
             * 本部长角色 或 软开成员特殊处理
             */
            List<String> deptIds = new ArrayList<>();
            deptIds.add(deptId);
            if (subject.hasRole(Role.BEN_BU_ZHANG)) {
                /* 查询所有本部下属组织 */
                deptIds.addAll(orgListDao.getOrgAndSubOrgIds(org.getId()));
                // 先从字典找一圈
                List<DicTypeEO> dicTypeEOList = dicTypeEOService.queryByList("FAKE_BENBU");
                if(CollectionUtils.isNotEmpty(dicTypeEOList)) {
                    for (DicTypeEO dicTypeEO : dicTypeEOList) {
                        if (StringUtils.contains(dicTypeEO.getDicTypeName(), userId)) {
                            deptIds.addAll(orgEODao.getSubOrgId(dicTypeEO.getDicTypeCode()));
                        }
                    }
                }
                qb.must(QueryBuilders.termsQuery("deptId", deptIds));
            } else if ("MEGMW98MVJ".equals(deptId)) {
                /* 查询所有软件事业本部下属组织 */
                deptIds.addAll(orgListDao.getOrgAndSubOrgIds(orgParentIds[3]));
                qb.must(QueryBuilders.termsQuery("deptId", deptIds));
            } else {
                qb.must(QueryBuilders.termsQuery("deptId", deptIds));
            }
        }

        if (StringUtils.isNotEmpty(page.getName())) {
            qb.must(QueryBuilders.wildcardQuery(NAME, '*' + page.getName() + '*'));
        }
        if (StringUtils.isNotEmpty(page.getContractNo())) {
            qb.must(QueryBuilders.wildcardQuery(CONTRACT_NO, '*' + page.getContractNo() + '*'));
        }
        if (StringUtils.isNotEmpty(page.getContractAmountStr())) {
            qb.must(QueryBuilders.matchQuery("contractAmountStr", page.getContractAmountStr()));
        }

        qb.mustNot(QueryBuilders.termQuery(DEL_FLAG, true));
        qb.mustNot(QueryBuilders.existsQuery(PROJECT_LEADER_ID));

        Sort sort = new Sort(Sort.Direction.DESC, CREATE_TIME);
        Pageable pageable = new PageRequest(page.getPage() - 1, page.getPageSize(), sort);

        Page<Project> pageResult = projectRepository.search(qb, pageable);

        List<Project> rows = pageResult.getContent();

        /*
         *  主任管理员追加部门名称回显
         */
        if (adminRole || zhuRenRole) {
            Map<String, String> orgIdNameMap = initDeptIdNameMap();
            rows.forEach(eo -> eo.setProjectTeam(orgIdNameMap.get(eo.getDeptId())));
        }

        PageInfo<Project> result = new PageInfo<>();
        result.setCount(pageResult.getTotalElements());

        result.setPageCount((long) rows.size());
        result.setList(rows);
        result.setPageSize(page.getPageSize());
        result.setPageNo(page.getPage());

        /*
         *  返回前端汇报是否是管理员权限
         */
        Map<String, String> extMap = new HashMap<>();
        extMap.put("IsAdmin", String.valueOf(adminRole));
        extMap.put("IsZhuRen", String.valueOf(zhuRenRole));
        result.setExt(extMap);
        return result;

    }

    /**
     * 批量更新部门
     *
     * @param projectVOList
     * @return
     */
    public List<Project> changeDept(List<Project> projectVOList) {
        Subject subject = SecurityUtils.getSubject();

        //管理员权限判断
        boolean adminRole = subject.hasRole(Role.SUPER_ADMIN)
            || subject.hasRole(Role.ADMIN)
            || subject.hasRole(Role.PROJECT_ADMIN);
        if (adminRole) {
            Map<String, String> projectIdDeptIdMap = new HashMap<>(projectVOList.size());
            List<String> idList = new ArrayList<>();
            projectVOList.forEach(eo -> {
                idList.add(eo.getId());
                projectIdDeptIdMap.put(eo.getId(), eo.getDeptId());
            });

            List<Project> projectList = Lists.newArrayList(projectRepository.findAll(idList));

            /* 替换部门id */
            projectList.forEach(project -> project.setDeptId(projectIdDeptIdMap.get(project.getId())));

            if (CollectionUtils.isNotEmpty(projectList)) {
                projectRepository.save(projectList);

            }
            return projectList;
        }
        return new ArrayList<>();

    }

    /**
     * 申领合同
     *
     * @param voList
     * @return
     * @throws Exception
     */
    public List<Project> claimProject(List<ClaimVO> voList) throws Exception {
        List<Project> resultList = new ArrayList<>();

        UserEO createUser = UserUtils.getUser();
        List<OrgEO> orgEOList = createUser.getOrgEOList();

        Date nowTime = new Date();

        if (null == createUser) {
            throw new AdcDaBaseException("登录过期");
        }

        for (ClaimVO vo : voList) {

            BoolQueryBuilder query = QueryBuilders.boolQuery();
            query.mustNot(QueryBuilders.termQuery(DEL_FLAG, true));
            for (String id : vo.getIdSet()) {
                query.should(QueryBuilders.termQuery("id", id));
            }
            List<Project> subResultList = ((PageImpl<Project>) projectRepository.search(query)).getContent();

            for (Project project : subResultList) {
                if (StringUtils.isNotEmpty(project.getBudgetId())) {
                    BudgetEO budgetEO = budgetEODao.selectByPrimaryKey(project.getBudgetId());
                    if (null == budgetEO){
                        throw new AdcDaBaseException("项目"+project.getName()+"所在的业务已被删除或不存在！");
                    }
                }
                List<ClaimMemberVO> memberVOList = vo.getClaimMemberVOList();
                //获取VO里的部门列表
                List<ClaimDeptVO>   claimDeptVOList= vo.getClaimDeptVOList();
                //定义用来保存部门信息Map的 List
                ArrayList<Map<String,String>> deptInfoListMap=new ArrayList<>();
                //定义HashMap来按部门保存人员ID
                HashMap<String,List<String>> deptIdUserIdList = new HashMap<>();

                List<UserEO> userEOListFromDept = new LinkedList<>();
                if(CollectionUtils.isNotEmpty(claimDeptVOList)) {
                    for (ClaimDeptVO claimDeptVO : claimDeptVOList) {
                        if (claimDeptVO.getDeptType() == 1) {
                            List<String> deptIds = new LinkedList<>();
                            deptIds.add(claimDeptVO.getDeptId());
                            List<UserEO> userEOListOfADept = userEOService.getAllUserEOsByOrgIds(deptIds);
                            userEOListFromDept.addAll(userEOListOfADept);

                            List<String> deptMemberIds = new ArrayList<>();
                            for (UserEO userEO : userEOListOfADept) {
                                deptMemberIds.add(userEO.getUsid());
                            }
                            deptIdUserIdList.put(claimDeptVO.getDeptId(), deptMemberIds);
                        }
                        if (claimDeptVO.getDeptType() == 2) {
                            List<UserEO> userEOListOfADept = orgEODao.listUserEOByOrgId(claimDeptVO.getDeptId());
                            userEOListFromDept.addAll(userEOListOfADept);

                            List<String> deptMemberIds = new ArrayList<>();
                            for (UserEO userEO : userEOListOfADept) {
                                deptMemberIds.add(userEO.getUsid());
                            }
                            deptIdUserIdList.put(claimDeptVO.getDeptId(), deptMemberIds);
                        }

                        Map<String, String> deptMap = new HashMap<>();
                        String deptId = claimDeptVO.getDeptId();
                        String deptName = claimDeptVO.getDeptName();
                        int deptType = claimDeptVO.getDeptType();
                        deptMap.put("deptId", deptId);
                        deptMap.put("type", String.valueOf(deptType));
                        deptMap.put("name", deptName);
                        deptInfoListMap.add(deptMap);
                    }
                }


                Set<String> userIdSet = new HashSet<>();
                //20200319 需求说 pm 默认不放在项目成员里
//                if (StringUtils.isNotEmpty(project.getPm())){
//                    userIdSet.add(project.getPm());
//                }
                if (StringUtils.isNotEmpty(project.getBusinessManagerId())){
                    userIdSet.add(project.getBusinessManagerId());
                }
                if (CollectionUtils.isNotEmpty(userIdSet)){
                    List<UserEO> userEOList = userEODao.getUserWithRolesByUserIds(new ArrayList<>(userIdSet));
                    userEOList.addAll(userEOListFromDept);
                    if (StringUtils.isNotEmpty(userEOList)) {
                        for (UserEO userEO : userEOList) {
                            ClaimMemberVO claimMemberVO = new ClaimMemberVO();
                            claimMemberVO.setUserId(userEO.getUsid());
                            claimMemberVO.setUserName(userEO.getUsname());
                            memberVOList.add(claimMemberVO);
                        }
                    }
                }
                BaseMemberEO baseMemberEO = getMember(memberVOList);
                project.setProjectLeader(baseMemberEO.getProjectLeader());
                project.setProjectLeaderId(baseMemberEO.getProjectLeaderId());
                project.setProjectAdminId(baseMemberEO.getProjectAdminId());
                project.setProjectAdminName(baseMemberEO.getProjectAdminName());
                project.setMapList(baseMemberEO.getMapList());

                project.setMemberNames(baseMemberEO.getMemberNames());
                project.setProjectMemberIds(baseMemberEO.getProjectMemberIds());
                project.setProjectMemberNames(baseMemberEO.getProjectMemberNames());
                project.setUserIdDeptNameMapList(baseMemberEO.getUserIdDeptNameMapList());
                project.setDeptInfoListMap(deptInfoListMap);
                project .setDeptIdUserIdList( deptIdUserIdList);

                /*
                 *  更新人力投入与项目描述
                 */
                project.setPersonInput(vo.getPersonInput());
                project.setProjectDescription(vo.getRemark());

                /*
                 * 更新申领人信息
                 */
                project.setCreateUserId(createUser.getUsid());
                project.setCreateUserName(createUser.getUsname());
                /*
                 * 更新时间
                 */
                project.setStartTime(nowTime);
                project.setModifyTime(nowTime);
                //如果商务经理不为空，认领时创建商务任务，否则不创建，取决于oa是否传过来该参数
                if (StringUtils.isNotEmpty(project.getBusinessManagerId())) {
                    Task task = new Task();
                    task.setName("商务");
                    task.setId(UUID.randomUUID10());
                    task.setApproveUserId(project.getBusinessManagerId());
                    task.setApproveUserName(project.getBusinessManagerName());
                    task.setProjectLeaderId(project.getProjectLeaderId());

                    task.setDeptId(project.getDeptId());

                    task.setCreateTime(nowTime);
                    task.setModifyTime(nowTime);
                    task.setPriority("普通");
                    task.setBudgetName(project.getBudget());
                    task.setBudgetName1(project.getBudget());
                    task.setSearchBudgetId(project.getBudgetId());
                    task.setProjectId(project.getId());
                    task.setProjectName(project.getName());
                    task.setProjectName1(project.getName());
                    task.setCreateUserId(project.getBusinessManagerId());
                    task.setCreateUserName(project.getBusinessManagerName());

                    task.setPlanStartTime(project.getProjectStartTime());
                    task.setPlanEndTime(project.getProjectStartTime());

                    task.setManager(project.getManager());
                    task.setPm(project.getPm());

                    String[] nameArr = {task.getApproveUserName()}; // 等价,project.getBusinessManagerName()
                    String[] idArr = {task.getApproveUserId()};
                    HashMap<String, String> map = new HashMap<>();
                    map.put("name", task.getApproveUserName());
                    map.put("id", task.getApproveUserId());

                    List<Map<String, String>> mapsList = new ArrayList<>();
                    mapsList.add(map);

                    task.setMemberNames(nameArr);
                    task.setMemberIds(idArr);
                    task.setProjectType(project.getProjectType());
                    task.setTaskStatus("进行中");
                    task.setMemberNameString(task.getApproveUserName());
                    task.setMapsList(mapsList);
                    task.setTaskType(1); //标记为商务任务

                    saveUserWithProject(
                        project.getProjectMemberIds(),
                        project,
                        task.getId(),
                        project.getBusinessManagerId());
//                    saveUserWithProject(project.getBusinessManagerId(),task);
//                    saveUserWithProject(project.getProjectMemberIds(), project);
                    taskRepository.save(task);
                } else {
                    saveUserWithProject(project.getProjectMemberIds(), project);
                }
            }
            resultList.addAll(subResultList);
        }

        projectRepository.save(resultList);

        return resultList;
    }

    /**
     * 对成员基本信息进行初始化操作
     *
     * @param memberVOList
     * @return
     */
    private BaseMemberEO getMember(List<ClaimMemberVO> memberVOList) {

        BaseMemberEO baseMemberEO  =  new  BaseMemberEO();
        if (CollectionUtils.isEmpty(memberVOList)) {
            throw new AdcDaBaseException("成员信息为空");
        }
        Set<String> projectMemberIdsSet = new HashSet<>();
        //memberVOList.forEach((ClaimMemberVO a) -> memberMap.put(a.getUserId(), a.getUserName()))
        for (ClaimMemberVO member : memberVOList) {
            if(member.getUserType() == 1){
                baseMemberEO.setProjectLeaderId(member.getUserId());
                baseMemberEO.setProjectLeader(member.getUserName());
                projectMemberIdsSet.add(member.getUserId());
            }else if (member.getUserType() == 2){
                baseMemberEO.setProjectAdminId(member.getUserId());
                baseMemberEO.setProjectAdminName(member.getUserName());
                projectMemberIdsSet.add(member.getUserId());
            }
            if (StringUtils.isNotEmpty(member.getUserId())) {
                projectMemberIdsSet.add(member.getUserId());
            }
        }
        if (CollectionUtils.isNotEmpty(projectMemberIdsSet)) {
            List<String> projectMemberIdList = new ArrayList<>(projectMemberIdsSet);
            List<UserEO> userEOList = userEODao.getUserWithRolesByUserIds(projectMemberIdList);
            baseMemberEO.setMapList(CommonUtil.userIdUsnameMapKv(userEOList));
            baseMemberEO.setUserIdDeptNameMapList(CommonUtil.userIdDeptNamesMapKv(userEOList,orgEODao));
            baseMemberEO.setMemberNames(CommonUtil.getUserNameArr(userEOList));
            baseMemberEO.setProjectMemberIds(projectMemberIdsSet.toArray(new String[projectMemberIdsSet.size()]));
            baseMemberEO.setProjectMemberNames(StringUtils.join(CommonUtil.getUserNameArr(userEOList),','));
        }

        return baseMemberEO;
    }

    /**
     * 进行用户相关
     *
     * @param projectMemberIdArr
     * @param project
     */
    public void saveUserWithProject(String[] projectMemberIdArr, Project project, String taskId,
        String businessManagerId) {
        Set<String> projectMemberIdsSet = new HashSet<>(Arrays.asList(projectMemberIdArr));
        if (StringUtils.isNotEmpty(businessManagerId)) {
            projectMemberIdsSet.add(businessManagerId);
        }
        if (StringUtils.isNotEmpty(project.getPm())) {
            projectMemberIdsSet.add(project.getPm());
        }
        List<UserWithProjects> list = userWithProjectsRepository.findByUserIdIn(projectMemberIdsSet);
        List<UserWithProjects> saveList = new ArrayList<>(list);

        Set<String> budgetIdSet;
        Set<String> projectIdSet;
        for (UserWithProjects eo : list) {
            /*
             *  表示
             */
            projectMemberIdsSet.remove(eo.getUserId());

            budgetIdSet = eo.getBusinessIds();
            budgetIdSet.add(project.getBudgetId());
            eo.setBusinessIds(budgetIdSet);
            //是项目经理或者商务经理，才允许看到这个任务
            if (StringUtils.equals(eo.getUserId(),project.getBusinessManagerId())
                    ||StringUtils.equals(eo.getUserId(),project.getProjectLeaderId())
                    || StringUtils.equals(eo.getUserId(), project.getPm())
                    || StringUtils.equals(eo.getUserId(), project.getProjectAdminId())
                    || StringUtils.equals(eo.getUserId(), project.getBusinessId())) { //业务经理要看到项目下所有的任务 已跟需求确认
                eo.getTaskIds().add(taskId);
            }

            projectIdSet = eo.getProjectIds();
            projectIdSet.add(project.getId());
            eo.setProjectIds(projectIdSet);
        }

        /*
         * 针对新用户做下述操作
         */
        projectIdSet = new HashSet<>(2);
        budgetIdSet = new HashSet<>(2);
        projectIdSet.add(project.getId());
        budgetIdSet.add(project.getBudgetId());
        for (String userId : projectMemberIdsSet) {
            UserWithProjects eo = new UserWithProjects();
            eo.setUserId(userId);
            if (StringUtils.equals(eo.getUserId(),project.getBusinessManagerId())
                    ||StringUtils.equals(eo.getUserId(),project.getProjectLeaderId())
                    || StringUtils.equals(eo.getUserId(), project.getPm())
                    || StringUtils.equals(eo.getUserId(), project.getProjectAdminId())
                    || StringUtils.equals(eo.getUserId(), project.getBusinessId())) {//业务经理要看到项目下所有的任务 已跟需求确认
                eo.getTaskIds().add(taskId);
            }
            eo.setBusinessIds(budgetIdSet);
            eo.setProjectIds(projectIdSet);
            saveList.add(eo);
        }
        if (CollectionUtils.isNotEmpty(saveList)) {
            userWithProjectsRepository.save(saveList);
        }
    }
    /**
     * 进行用户相关
     *
     * @param task
     * @param businessManagerId
     */
    public void saveUserWithProject(String businessManagerId,Task task) {
        UserWithProjects userWithProjects = userWithProjectsRepository.findOne(businessManagerId);
        if (null == userWithProjects){
            userWithProjects = new UserWithProjects();
        }
        userWithProjects.getBusinessIds().add(task.getBudgetId());
        userWithProjects.getProjectIds().add(task.getProjectId());
        userWithProjects.getTaskIds().add(task.getId());
        userWithProjectsRepository.save(userWithProjects);
    }

    /**
     * 进行用户相关
     *
     * @param projectMemberIdArr
     * @param project
     */
    public void saveUserWithProject(String[] projectMemberIdArr, Project project) {
        Set<String> projectMemberIdsSet = new HashSet<>(Arrays.asList(projectMemberIdArr));
        if (StringUtils.isNotEmpty(project.getPm())) {
            projectMemberIdsSet.add(project.getPm());
        }
        List<UserWithProjects> list = userWithProjectsRepository.findByUserIdIn(projectMemberIdsSet);
        List<UserWithProjects> saveList = new ArrayList<>(list);

        Set<String> budgetIdSet;
        Set<String> projectIdSet;
        for (UserWithProjects eo : list) {
            /*

             *  表示
             */
            projectMemberIdsSet.remove(eo.getUserId());

            budgetIdSet = eo.getBusinessIds();
            budgetIdSet.add(project.getBudgetId());
            eo.setBusinessIds(budgetIdSet);

            projectIdSet = eo.getProjectIds();
            projectIdSet.add(project.getId());
            eo.setProjectIds(projectIdSet);
        }

        /*
         * 针对新用户做下述操作
         */
        projectIdSet = new HashSet<>(2);
        budgetIdSet = new HashSet<>(2);
        projectIdSet.add(project.getId());
        budgetIdSet.add(project.getBudgetId());
        for (String userId : projectMemberIdsSet) {
            UserWithProjects eo = new UserWithProjects();
            eo.setUserId(userId);
            eo.setBusinessIds(budgetIdSet);
            eo.setProjectIds(projectIdSet);
            saveList.add(eo);
        }
        if (CollectionUtils.isNotEmpty(saveList)) {
            userWithProjectsRepository.save(saveList);
        }
    }

    /**
     * 进行用户相关
     *
     * @param projectMemberIds
     * @param projectIdSet
     * @param taskIdSet
     * @param childTaskIdSet
     * @param leaderId
     */
    public void deleteUserWithProject(Set<String> projectMemberIds, Set<String> projectIdSet,
        Set<String> taskIdSet, Set<String> childTaskIdSet, String leaderId) {
        List<UserWithProjects> list = userWithProjectsRepository.findByUserIdIn(projectMemberIds);
        for (UserWithProjects eo : list) {
            /*
             *  删除相关数据
             */
            eo.getProjectIds().removeAll(projectIdSet);
            eo.getTaskIds().removeAll(taskIdSet);
            eo.getChildTaskIds().removeAll(childTaskIdSet);
        }
        if (CollectionUtils.isNotEmpty(list)) {
            userWithProjectsRepository.save(list);
        }

        /*
         * 对负责人添加相关下任务/子任务
         */
        if (StringUtils.isNotEmpty(leaderId)) {
            UserWithProjects leader = userWithProjectsRepository.findOne(leaderId);
            if (null == leader) {
                /* 新用户 */
                leader = new UserWithProjects();
                leader.setUserId(leaderId);
                leader.setTaskIds(taskIdSet);
                leader.setChildTaskIds(childTaskIdSet);
            } else {
                leader.getTaskIds().addAll(taskIdSet);
                leader.getChildTaskIds().addAll(childTaskIdSet);
            }
            userWithProjectsRepository.save(leader);
        }

    }

}
