package com.adc.da.budget.service;

import com.adc.da.base.service.BaseService;
import com.adc.da.budget.constant.ProjectSearchField;
import com.adc.da.budget.constant.Role;
import com.adc.da.budget.dao.BudgetEODao;
import com.adc.da.budget.dao.OrgListDao;
import com.adc.da.budget.dao.UserProjectCustomDao;
import com.adc.da.budget.dto.BusinessDto;
import com.adc.da.budget.entity.*;
import com.adc.da.budget.page.BudgetEOPage;
import com.adc.da.budget.query.QueryVO;
import com.adc.da.budget.query.budget.BudgetQuery;
import com.adc.da.budget.repository.*;
import com.adc.da.budget.utils.BeanUtils;
import com.adc.da.budget.utils.CommonUtil;
import com.adc.da.budget.utils.DateUtil;
import com.adc.da.budget.vo.BudgetVO;
import com.adc.da.budget.vo.TaskStatusVO;
import com.adc.da.crm.dao.UserDao;
import com.adc.da.excel.poi.excel.ExcelImportUtil;
import com.adc.da.excel.poi.excel.entity.ImportParams;
import com.adc.da.excel.poi.excel.entity.result.ExcelImportResult;
import com.adc.da.excel.poi.excel.entity.result.ExcelVerifyHanlderErrorResult;
import com.adc.da.login.util.UserUtils;
import com.adc.da.superadmin.service.SuperAdminService;
import com.adc.da.sys.entity.OrgEO;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.sys.page.UserEOPage;
import com.adc.da.sys.service.OrgEOService;
import com.adc.da.sys.service.UserEOService;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.*;
import com.adc.da.util.utils.UUID;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.bcel.generic.IF_ACMPEQ;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;

import static java.math.BigDecimal.ROUND_HALF_DOWN;

/**
 * <br>
 * <b>功能：</b>TS_BUDGET BudgetEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2018-10-25 <br>0.
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Slf4j
@Service("budgetEOService")
@Transactional(value = "transactionManager", readOnly = false,
    propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class BudgetEOService extends BaseService<BudgetEO, String> {

    private static final String DEL_FLAG = "delFlag";

    @Autowired
    BeanMapper beanMapper;

    @Autowired
    private BudgetEODao dao;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserEOService userEOService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private ChildrenTaskService childrenTaskService;

    @Autowired
    private ChildTaskRepository childTaskRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserWithProjectsRepository userWithProjectsRepository;

    @Autowired
    private OrgListDao orgListDao;

    @Autowired
    private DailyRepository dailyRepository;

    @Autowired
    private OrgEOService orgEOService;

    @Autowired
    private SuperAdminService superAdminService;

    @Autowired
    private UserProjectCustomDao userProjectCustomDao;

    @Override
    public BudgetEODao getDao() {
        return dao;
    }

    public BudgetEO save(BudgetEO budgetEO) throws Exception {
        budgetEO.setId(UUID.randomUUID10());
        budgetEO.setCreateUserId(UserUtils.getUserId());
        dao.insertSelective(budgetEO);
//        saveUserBudgetPM(budgetEO);
//        saveUserBudgetBusinessAdmin(budgetEO);
        //budgetHistoryEOService.operateBudgetHistory(budgetEO, "insert");
        saveUserBudgetInCreate(budgetEO);
        return budgetEO;
    }

    /**
     * 批量删除进行权限校验
     *
     * @param ids
     */
    public void deleteInBatch(List<String> ids) {
        deleteInBatch(ids, true);
    }

    /**
     * 批量删除
     *
     * @param ids id组
     */
    public void deleteInBatch(List<String> ids, boolean checkPermission) {
        String userId = UserUtils.getUserId();
        if (checkPermission && StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        BoolQueryBuilder builder = QueryBuilders.boolQuery()
                                                .mustNot(QueryBuilders.termQuery(DEL_FLAG, true));
        //项目id是空，budgetId存在ids里
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
                                                         .mustNot(QueryBuilders.termQuery(DEL_FLAG, true))
                                                         .mustNot(QueryBuilders.existsQuery("projectId"));
        BoolQueryBuilder qb = QueryBuilders.boolQuery();
        Subject subject = SecurityUtils.getSubject();
        List<BudgetEO> budgetEOList = dao.selectByPrimaryKeys(ids);
        for (BudgetEO budgetEntity : budgetEOList) {
            //只有业务创建人超级管理员以及主任可以删除业务
            if (checkPermission
                && !StringUtils.equals(userId, budgetEntity.getCreateUserId())
                && !subject.hasRole(Role.ZHU_REN)
                && !subject.hasRole(Role.SUPER_ADMIN)
                && !subject.hasRole(Role.PROJECT_ADMIN)) {
                throw new AdcDaBaseException("您无权删除业务：" + budgetEntity.getProjectName());
            }
            builder.should(QueryBuilders.termsQuery("budgetId", budgetEntity.getId()));
            qb.should(QueryBuilders.termQuery("budgetId", budgetEntity.getId()));
        }
        boolQueryBuilder.must(qb);
        Iterator<Project> projectIterator = projectRepository.search(builder).iterator();
        Iterator<Task> taskIterator = taskRepository.search(boolQueryBuilder).iterator();
        if (checkPermission && taskIterator.hasNext()) {
            throw new AdcDaBaseException("所属业务已被任务使用");
        }
        if (checkPermission && projectIterator.hasNext()) {
            throw new AdcDaBaseException("所属业务已被项目使用");
        }
        for (BudgetEO budgetEntity : budgetEOList) {
            //在这通过budgetEO里的信息，删除user和businessIds的关系
            deleteUserBudget(budgetEntity);
        }
        dao.deleteLogicInBatch(ids);
    }

    /**
     * 查询单个
     */

    public BudgetVO findOne(String id) {
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        BudgetEO budgetEO = dao.selectByPrimaryKey(id);
        boolean manager = false;
        // 超级管理员可以编辑任何业务
        if (superAdminService.isSuperAdmin()) {
            manager = true;
        } else if (StringUtils.equals(userId, budgetEO.getPm())
            || StringUtils.equals(userId, budgetEO.getCreateUserId())
            || StringUtils.equals(userId, budgetEO.getBusinessAdminId())) {
            manager = true;
        }
        BudgetVO budgetVO = BeanUtils.map(budgetEO, BudgetVO.class);
        budgetVO.setManager(manager);
        budgetVO.setBusinessAdminId(budgetEO.getBusinessAdminId());
        budgetVO.setBusinessAdminName(budgetEO.getBusinessAdminName());
        return budgetVO;
    }

    /**
     * 根据ids获取列表数据
     *
     * @param value
     * @return
     */
    public List<BudgetEO> selectByPrimaryKeys(List<String> value) {
        return this.getDao().selectByPrimaryKeys(value);
    }

    public List<BudgetEO> queryAll() {
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        Subject subject = SecurityUtils.getSubject();
        if (subject.hasRole(Role.SUPER_ADMIN) || subject.hasRole(Role.ADMIN) || subject.hasRole(Role.ZHU_REN) || subject
            .hasRole(Role.OPERATE_BUDGET_ADMIN)) {
            return dao.findAll(null);
        }
        if (subject.hasRole(Role.BU_ZHANG) || subject.hasRole(Role.BEN_BU_ZHANG)) {
//            List<OrgEO> orgEOList = projectService.newQueryOrgByADCown1();
//            List<String> orgIds = new ArrayList<>();
//            for (OrgEO orgEO : orgEOList) {
//                orgIds.add(orgEO.getId());
//            }
//            List<UserEO> userEOS = userDao.selectByOrgIdAndUsName(orgIds, null);
            UserEO loginUserEO = UserUtils.getUser();
            if (null==loginUserEO) {
                throw new AdcDaBaseException("登陆可能过期，请登录！");
            }
            List<OrgEO> orgEOList = loginUserEO.getOrgEOList();
            List<UserEO> userEOS = new ArrayList<>();
            userEOS.add(loginUserEO);
            List<String> orgIds = new ArrayList<>();
            if (subject.hasRole(Role.BU_ZHANG)){
                 for (OrgEO orgEO : orgEOList) {
                     if (StringUtils.equals(orgEO.getLayer(),"4")) {
                         orgIds.add(orgEO.getId());
                     }
                 }
            }
            if (subject.hasRole(Role.BEN_BU_ZHANG)){
                for (OrgEO orgEO : orgEOList) {
                    if (StringUtils.equals(orgEO.getLayer(),"3")) {
                        orgIds.add(orgEO.getId());
                    }
                }
            }
            Set<String> userIdSet = new HashSet<>();
            Set<String> budgetIdSet = new HashSet<>();
            //拿到所有的业务项目任务子任务ID
            for (UserEO userEO : userEOS) {
                userIdSet.add(userEO.getUsid());
            }
            List<UserWithProjects> userWithProjectsList = userWithProjectsRepository.findByUserIdIn(userIdSet);
            for(UserWithProjects userWithProjects : userWithProjectsList){
                budgetIdSet.addAll(userWithProjects.getBusinessIds());
            }
            return dao.selectByPrimaryKeysNotOld(Lists.newArrayList(budgetIdSet));
        }
        UserWithProjects userWithProjects = userWithProjectsRepository.findOne(userId);
        if (null != userWithProjects) {
            Set<String> businessIds = userWithProjects.getBusinessIds();
            if (CollectionUtils.isNotEmpty(businessIds)) {
                List<BudgetEO> budgetEOS = dao.selectByPrimaryKeysNotOld(Lists.newArrayList(businessIds));
                return budgetEOS;
            }
        }
        return new ArrayList<>();
    }

    /**
     * 返回所有数据，无权限限制
     *
     * @return
     * @author Lee Kwanho 李坤澔
     *     date 2019-05-10
     **/
    public List<BudgetEO> listForForm() {
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }

        return dao.findAll(null);

    }

    public List<BudgetEO> queryByPage(BudgetEOPage page) throws Exception {
        Integer rowCount = this.queryByCount(page);
        page.getPager().setRowCount(rowCount);
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        page.setOrderBy("CREATE_TIME");
        page.setOrder("DESC");
        Subject subject = SecurityUtils.getSubject();
        List<String> depIds = new ArrayList<>();
        String deptId = projectService.getDeptId(userId);
        depIds.add(deptId);
        if (subject.hasRole(Role.SUPER_ADMIN) || subject.hasRole(Role.ADMIN) || subject.hasRole(Role.ZHU_REN)) {
            return dao.queryByPage(page);
        }
        //if (subject.hasRole("部长")) {
        page.setDeptIds(depIds);
        return dao.queryByPage(page);
        //}
        //创建人或经理
        //page.setQ(userId);
        //page.getPager().setRowCount(dao.queryByCount(page));
        //return dao.queryByPage(page);
    }

    public List<String> queryAllBudgetByType(String property) {
        return dao.queryAllBudgetByType(property);
    }

    public List<String> queryAllBudgetByTypeName(String property, String name) {
        return dao.queryAllBudgetByTypeName(property, name);
    }

    public List<BudgetEO> queryAllBudget(String[] property) {

        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        Subject subject = SecurityUtils.getSubject();
        if (subject.hasRole(Role.SUPER_ADMIN) || subject.hasRole(Role.ADMIN) || subject.hasRole(Role.ZHU_REN)) {
            List<BudgetEO> budgetEOList = dao.findAllBudgetNameNotLike("旧-%", property);
            //按中文首字母排序
            Collections.sort(budgetEOList);
            return budgetEOList;
        }
        if (subject.hasRole(Role.BU_ZHANG) || subject.hasRole(Role.BEN_BU_ZHANG)) {
            List<OrgEO> orgEOList = projectService.queryOrgByADCown();
            List<String> orgIds = new ArrayList<>();
            for (OrgEO orgEO : orgEOList) {
                orgIds.add(orgEO.getId());
            }
            List<UserEO> userEOS = userDao.selectByOrgIdAndUsName(orgIds, null);
            Set<String> ids = new HashSet<>();
            //拿到所有的业务项目任务子任务ID
            for (UserEO userEO : userEOS) {
                UserWithProjects userWithProjects = userWithProjectsRepository.findOne(userEO.getUsid());
                if (null != userWithProjects) {
                    ids.addAll(userWithProjects.getBusinessIds());
                }
            }
            List<BudgetEO> budgetEOList = dao.findAllBudgetNameNotLikeAndKeys(Lists.newArrayList(ids), "旧-%", property);
            //按中文首字母排序
            Collections.sort(budgetEOList);
            return budgetEOList;
        }
        UserWithProjects userWithProjects = userWithProjectsRepository.findOne(userId);
        if (null != userWithProjects) {
            Set<String> businessIds = userWithProjects.getBusinessIds();
            if (CollectionUtils.isNotEmpty(businessIds)) {
                List<BudgetEO> budgetEOList = dao
                    .findAllBudgetNameNotLikeAndKeys(Lists.newArrayList(businessIds), "旧-%", property);
                //按中文首字母排序
                Collections.sort(budgetEOList);
                return budgetEOList;
            }
        }
        return new ArrayList<>();

    }

    /**
     * 查询业务部门下的所有成员map
     *
     * @return
     */
    public List<Map<String, String>> queryUserByBudgetId(String budgetId) throws Exception {
        BudgetEO budgetEO = getDao().selectByPrimaryKey(budgetId);
        if (budgetEO == null) {
            return new ArrayList<>();
        }
        return getUserListByDeptId(budgetEO.getDeptId());
    }

    /**
     * 查询当前用户部门所有成员map
     *
     * @return
     */
    public List<Map<String, String>> queryUserOrg() throws Exception {
//        String userId = UserUtils.getUserId();
//        List<OrgEO> orgEOList = userEOService.getUserWithRoles(userId).getOrgEOList();
//        String deptId = orgEOList.get(orgEOList.size() - 1).getId();
        //add by qichunxu 改成选择全中心所有成员
        return getUserListByDeptId(null);
    }

    private List<Map<String, String>> getUserListByDeptId(String deptId) throws Exception {
        List<Map<String, String>> mapList = new ArrayList<>();
        UserEOPage userEOPage = new UserEOPage();
        userEOPage.setOrgId(deptId);
        userEOPage.setPageSize(1000);
        userEOPage.setDelFlag("0");
        List<UserEO> userEOList = userEOService.queryByPage(userEOPage);
        for (UserEO userEO : userEOList) {
            Map<String, String> map = new HashMap<>();
            map.put("id", userEO.getUsid());
            map.put("name", userEO.getUsname());
            List<OrgEO> orgEOList = userEO.getOrgEOList();
            StringBuilder tepOrgName = new StringBuilder();
            for (OrgEO orgEO : orgEOList) {
                tepOrgName.append(orgEO.getName() + "，");
            }
            String orgName = tepOrgName.toString();
            String orgNames = "";
            if (StringUtils.isNotEmpty(orgName)) {
                orgNames = orgName.substring(0, orgName.length() - 1);
            }
            map.put("orgName", orgNames);
            mapList.add(map);
        }
        return mapList;
    }

    /**
     * 我的业务
     *
     * @param page
     * @return
     * @author liuzixi
     *     date 2019-03-11
     */
    public List<BudgetEO> getMyBudget(BudgetEOPage page) throws Exception {
        List<BudgetEO> budgetEOS;
        if (StringUtils.isEmpty(page.getProjectTeam())) {
            Integer rowCount = this.queryByCount(page);
            page.getPager().setRowCount(rowCount);
            budgetEOS = dao.queryByPage(page);
        } else {
            budgetEOS = dao.selectByPM(page.getPm());
        }

        return budgetEOS == null ? new ArrayList<BudgetEO>() : budgetEOS;

    }

    /**
     * 根据PM与BudgetName查询业务
     *
     * @author Wei Jinjin
     */
    public int selectByPmAndBudgetName(String id, String userId, String budgetName) {
        List<BudgetEO> list = dao.selectByPmAndBudgetName(id, userId, budgetName);
        return list.size();
    }

    /**
     * 业务查询页
     *
     * @author weijinjin
     *     date 2019-06-17
     */
    public List<BudgetEO> findByPage(BudgetQuery page) {
        Subject subject = SecurityUtils.getSubject();
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId) || null == subject) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        //信息化查询拼SQL
        String sql = "";
        String logic = "";
        if (StringUtils.isNotEmpty(page.getAllObject())) {
            for (QueryVO queryVO : page.getAllObject()) {
                switch (queryVO.getSearchField()) {
                    case "projectNames":
                        StringBuilder sb = new StringBuilder();
                        // 第一个条件拼接时logic为空
                        if (StringUtils.isNotEmpty(logic)) {
                            sb.append("(").append(sql).append(") ").append(logic);
                        }
                        sb.append(" t.project_name ").append(queryVO.getOperator()).append("'")
                          .append(queryVO.getName()).append("'");
                        logic = queryVO.getLogic();
                        sql = sb.toString();
                        break;
                    case "propertys":
                        sb = new StringBuilder();
                        // 第一个条件拼接时logic为空
                        if (StringUtils.isNotEmpty(logic)) {
                            sb.append("(").append(sql).append(") ").append(logic);
                        }
                        sb.append(" t.property ").append(queryVO.getOperator()).append("'").append(queryVO.getName())
                          .append("'");
                        logic = queryVO.getLogic();
                        sql = sb.toString();
                        break;
                    case "usnames":
                        sb = new StringBuilder();
                        // 第一个条件拼接时logic为空
                        if (StringUtils.isNotEmpty(logic)) {
                            sb.append("(").append(sql).append(") ").append(logic);
                        }
                        sb.append(" u.usname ").append(queryVO.getOperator()).append("'").append(queryVO.getName())
                          .append("'");
                        logic = queryVO.getLogic();
                        sql = sb.toString();
                        break;
                    case "propertyIds":
                        sb = new StringBuilder();
                        // 第一个条件拼接时logic为空
                        if (StringUtils.isNotEmpty(logic)) {
                            sb.append("(").append(sql).append(") ").append(logic);
                        }
                        sb.append(" t.property_id ").append(queryVO.getOperator()).append("'").append(queryVO.getName())
                          .append("'");
                        logic = queryVO.getLogic();
                        sql = sb.toString();
                        break;
                    case "cycleBegins":
                        sb = new StringBuilder();
                        // 第一个条件拼接时logic为空
                        if (StringUtils.isNotEmpty(logic)) {
                            sb.append("(").append(sql).append(") ").append(logic);
                        }
                        sb.append(" to_char(t.cycle_begin, 'yyyy-mm-dd') ").append(ProjectSearchField.convertOperator(queryVO.getOperator())).append("'")
                          .append(queryVO.getName()).append("'");
                        logic = queryVO.getLogic();
                        sql = sb.toString();
                        break;
                    case "cycleEnds":
                        sb = new StringBuilder();
                        // 第一个条件拼接时logic为空
                        if (StringUtils.isNotEmpty(logic)) {
                            sb.append("(").append(sql).append(") ").append(logic);
                        }
                        sb.append(" to_char(t.cycle_end, 'yyyy-mm-dd') ").append(ProjectSearchField.convertOperator(queryVO.getOperator())).append("'")
                          .append(queryVO.getName()).append("'");
                        logic = queryVO.getLogic();
                        sql = sb.toString();
                        break;
                    case "createUserNames":
                        sb = new StringBuilder();
                        // 第一个条件拼接时logic为空
                        if (StringUtils.isNotEmpty(logic)) {
                            sb.append("(").append(sql).append(") ").append(logic);
                        }
                        sb.append(" v.usname ").append(queryVO.getOperator()).append("'")
                          .append(queryVO.getName()).append("'");
                        logic = queryVO.getLogic();
                        sql = sb.toString();
                        break;
                    case "createTimes":
                        sb = new StringBuilder();
                        // 第一个条件拼接时logic为空
                        if (StringUtils.isNotEmpty(logic)) {
                            sb.append("(").append(sql).append(") ").append(logic);
                        }
                        sb.append(" to_char(t.create_time, 'yyyy-mm-dd') ").append(ProjectSearchField.convertOperator(queryVO.getOperator())).append("'")
                          .append(queryVO.getName()).append("'");
                        logic = queryVO.getLogic();
                        sql = sb.toString();
                        break;
                    default:
                }
            }
        }
        if (StringUtils.isNotEmpty(sql)) {
            sql = "(" + sql + ")";
            page.setSql_filter(sql);
        }
        //市场发展部也可以看中心所有的业务
        UserEO userEO = userEOService.getUserWithRoles(userId);
        List<OrgEO> orgList = userEO.getOrgEOList();
        OrgEO orgEO = orgList.get(orgList.size() - 1);
        if ("市场发展部".equals(orgEO.getName())) {
            Integer rowCount = dao.findByCount(page);
            page.getPager().setRowCount(rowCount);
            return dao.findByPage(page);
        }
        //超级管理员和主任以及项目管理员看中心所有的业务
        if (subject.hasRole(Role.SUPER_ADMIN) || subject.hasRole(Role.ZHU_REN) || subject.hasRole(Role.PROJECT_ADMIN)) {
            Integer rowCount = dao.findByCount(page);
            page.getPager().setRowCount(rowCount);
            return dao.findByPage(page);
        }
        //本部长看本部下所有部门业务树       部长看部门下所有业务树
        if (subject.hasRole(Role.BEN_BU_ZHANG) || subject.hasRole(Role.BU_ZHANG)) {
            Integer rowCount = dao.findByCount(page);
            page.getPager().setRowCount(rowCount);
//            page.setDeptIds(orgIds);
            return dao.findByPage(page);
//            return dao.findByIds(businessIds);
        }
        //预留代码：员工看自己的业务
        UserWithProjects userWithProjects = userWithProjectsRepository.findOne(userId);
        if (null != userWithProjects) {
            page.setBusinessIds(userWithProjects.getBusinessIds());
            Integer rowCount = dao.findByCount(page);
            page.getPager().setRowCount(rowCount);
            return dao.findByPage(page);
        }
        return new ArrayList<>();
    }



    public List<Project> getProjects(String bussinessId, String projectName,
        String projectStatus, String projectManager) {
        List<Project> projects = projectService.queryByBudgetId(bussinessId, projectName,
            projectStatus, projectManager);
        return projects == null ? new ArrayList<Project>() : projects;
    }

    public List<TaskStatusVO> getTaskStatus(String budgetId) {
        QueryBuilder queryBuilder = QueryBuilders.boolQuery()
                                                 .must(QueryBuilders.termQuery("budgetId", budgetId))
                                                 .mustNot(QueryBuilders.termQuery(DEL_FLAG, true));
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder().withQuery(queryBuilder);
        FieldSortBuilder sortBuilder = SortBuilders.fieldSort("planStartTime").order(SortOrder.ASC);
        nativeSearchQueryBuilder.withSort(sortBuilder);
        SearchQuery searchQuery = nativeSearchQueryBuilder.build();

        List<Task> tasks = Lists.newArrayList(taskRepository.search(searchQuery));
        List<TaskStatusVO> list = new ArrayList<>();
        for (Task task : tasks) {
            List<Integer> schedule = new ArrayList<>();
            int days = DateUtil.differentDaysByMillisecond(task.getPlanStartTime(), task.getPlanEndTime());
            schedule.add(days);
            TaskStatusVO taskStatusVO = new TaskStatusVO();
            taskStatusVO.setPriority(task.getPriority());
            taskStatusVO.setId(task.getId());
            taskStatusVO.setName(task.getName());
            taskStatusVO.setTaskName(task.getBudgetName());
            taskStatusVO.setStart(task.getPlanStartTime());
            taskStatusVO.setEnd(task.getPlanEndTime());
            taskStatusVO.setSchedule(schedule);
            list.add(taskStatusVO);
        }
        return list;
    }

    public String[] getPersonInput(String budgetId, String year) {
        BudgetEO budgetEO = this.dao.selectByPrimaryKey(budgetId);

        String[] array = new String[12];

        if (null != budgetEO && null != budgetEO.getProjectName()) {
            List<Map<String, Object>> mapList = this.dao
                .selectEveryMonthInvoiceDataByBudgetName(budgetEO.getProjectName(), year);
            if (CollectionUtils.isEmpty(mapList)) {
                for (int i = 0; i < 12; i++) {
                    array[i] = "0.00";
                }
                return array;
            }

            for (int i = 0; i < 12; i++) {
                for (Map subMap : mapList) {
                    Object obj = subMap.get("KEY");
                    if (null != obj && String.valueOf(i + 1).equals(obj.toString())) {
                        array[i] = subMap.get("VALUE").toString();
                        break;
                    } else {
                        array[i] = "0.00";
                    }
                }
            }

            return array;
        }
        for (int i = 0; i < 12; i++) {
            array[i] = "0.00";
        }
        return array;
    }

    /**
     * add by liqiushi 20190410 新增业务时，同步添加用户和业务的关联关系
     */
    public void saveUserBudgetPM(BudgetEO budgetEO) throws NoSuchMethodException {
        UserWithProjects userWithProjects = userWithProjectsRepository.findOne(budgetEO.getPm());
        Set<String> pms = new HashSet<>();
        if (userWithProjects == null) {
            userWithProjects = new UserWithProjects();
            userWithProjects.setUserId(budgetEO.getPm());
            userWithProjectsRepository.save(userWithProjects);
        }
        //老PM，新增关联关系
        pms.add(budgetEO.getPm());
        childrenTaskService.setUserWithProjectsData(userWithProjectsRepository,
            UserWithProjects.class.getMethod("getBusinessIds"), pms, true, budgetEO.getId());
        if (!StringUtils.equals(budgetEO.getPm(), budgetEO.getCreateUserId())) {
            //以createUserId 作为条件
            UserWithProjects userWithProjects1 = userWithProjectsRepository.findOne(budgetEO.getCreateUserId());
            Set<String> createUser = new HashSet<>();
            createUser.add(budgetEO.getCreateUserId());
            if (userWithProjects1 == null) {
                userWithProjects1 = new UserWithProjects();
                userWithProjects1.setUserId(budgetEO.getCreateUserId());
                userWithProjectsRepository.save(userWithProjects1);
            }
            childrenTaskService.setUserWithProjectsData(userWithProjectsRepository,
                UserWithProjects.class.getMethod("getBusinessIds"), createUser, true, budgetEO.getId());
        }
    }

    /**
     * add by liqiushi 20190410 新增业务时，同步添加用户和业务的关联关系
     */
    public void saveUserBudgetInCreate(BudgetEO budgetEO) {
        Set<String> userIdSet = new HashSet<>();
        if (StringUtils.isNotEmpty(budgetEO.getCreateUserId())) {
            userIdSet.add(budgetEO.getCreateUserId());
        }
        if (StringUtils.isNotEmpty(budgetEO.getPm())) {
            userIdSet.add(budgetEO.getPm());
        }

        if (StringUtils.isNotEmpty(budgetEO.getBusinessAdminId())) {
            userIdSet.add(budgetEO.getBusinessAdminId());
        }
        for (String userId : userIdSet) {
            UserWithProjects userWithProjects = userWithProjectsRepository.findOne(userId);
            if (userWithProjects == null) {
                userWithProjects = new UserWithProjects();
                userWithProjects.setUserId(userId);
            }
            userWithProjects.getBusinessIds().add(budgetEO.getId());
            userWithProjectsRepository.save(userWithProjects);
        }

    }

    /**
     * add by dingqiang  新增业务时，同步添加用户和业务的关联关系
     */
    public void saveUserBudgetBusinessAdmin(BudgetEO budgetEO) throws NoSuchMethodException {
        if (StringUtils.isEmpty(budgetEO.getBusinessAdminId())) {
            return;
        }
        UserWithProjects userWithProjects = userWithProjectsRepository.findOne(budgetEO.getBusinessAdminId());
        Set<String> pms = new HashSet<>();
        if (userWithProjects == null) {
            userWithProjects = new UserWithProjects();
            userWithProjects.setUserId(budgetEO.getBusinessAdminId());
            userWithProjectsRepository.save(userWithProjects);
        }
        //老PM，新增关联关系
        pms.add(budgetEO.getBusinessAdminId());
        childrenTaskService.setUserWithProjectsData(userWithProjectsRepository,
            UserWithProjects.class.getMethod("getBusinessIds"), pms, true, budgetEO.getId());
        if (!StringUtils.equals(budgetEO.getBusinessAdminId(), budgetEO.getCreateUserId())) {
            //以createUserId 作为条件
            UserWithProjects userWithProjects1 = userWithProjectsRepository.findOne(budgetEO.getCreateUserId());
            Set<String> createUser = new HashSet<>();
            createUser.add(budgetEO.getCreateUserId());
            if (userWithProjects1 == null) {
                userWithProjects1 = new UserWithProjects();
                userWithProjects1.setUserId(budgetEO.getCreateUserId());
                userWithProjectsRepository.save(userWithProjects1);
            }
            childrenTaskService.setUserWithProjectsData(userWithProjectsRepository,
                UserWithProjects.class.getMethod("getBusinessIds"), createUser, true, budgetEO.getId());
        }
    }

    /**
     * add by liqiushi 20190410 删除业务时，解除用户和业务
     */
    public void deleteUserBudget(BudgetEO budgetEO) {
        UserWithProjects userWithProjects = userWithProjectsRepository.findOne(budgetEO.getPm());
        if (userWithProjects != null) {
            userWithProjects.getBusinessIds().remove(budgetEO.getId());
            userWithProjectsRepository.save(userWithProjects);
        }
        UserWithProjects userWithProjects1 = userWithProjectsRepository.findOne(budgetEO.getCreateUserId());
        if (userWithProjects1 != null) {
            userWithProjects1.getBusinessIds().remove(budgetEO.getId());
            userWithProjectsRepository.save(userWithProjects1);
        }
    }

    /**
     * add by dingqiang 更新业务的PM时，更新用户和业务的关联关系,
     * 更新业务下的项目，任务，子任务和用户的关联关系
     */
    public void updateUserBudgetPMNew(BudgetEO newBudgetEO, BudgetEO oldBudgetEO) {

        if (StringUtils.isEmpty(newBudgetEO.getPm())) {
            throw new AdcDaBaseException("新业务负责人不能为空");
        }

        //TODO 先写在这里，回头加上service方法
        //budgetHistoryEOService.operateBudgetHistory(budgetEO, "update");
        if (!StringUtils.equals(newBudgetEO.getPm(), oldBudgetEO.getPm())) {
            UserWithProjects newUserWithProjects = userWithProjectsRepository.findOne(newBudgetEO.getPm());
            if (null == newUserWithProjects ){
                newUserWithProjects = new UserWithProjects();
                newUserWithProjects.setUserId(newBudgetEO.getPm());
            }
            UserWithProjects oldUserWithProjects = userWithProjectsRepository.findOne(oldBudgetEO.getPm());

            Set<String> budgetIdSet = new HashSet<>();
            budgetIdSet.add(newBudgetEO.getId());
            newUserWithProjects.getBusinessIds().addAll(budgetIdSet);
            oldUserWithProjects.getBusinessIds().remove(newBudgetEO.getId());
            List<Project> projectList = projectRepository.findByBudgetIdAndDelFlagNot(newBudgetEO.getId(), true);
            Set<String> projectIdSet = new HashSet<>();
            Set<String> taskIdsSet = new HashSet<>();
            if (CollectionUtils.isNotEmpty(projectList)) {
                for (Project project : projectList) {
                    project.setPm(newBudgetEO.getPm());
                    if (StringUtils.isNotEmpty(project.getProjectLeaderId())) {
                        projectIdSet.add(project.getId());
                    }
                    //下面做两个事儿
                    // 1.如果老管理员不是成员，那么直接干掉关系
                    // 2.直接给新管理员建立关系
                    if (CommonUtil.arrayContains(project.getProjectMemberIds(), oldBudgetEO.getPm()) < 0) {
                        oldUserWithProjects.getProjectIds().remove(project.getId());
                    }
                    newUserWithProjects.getProjectIds().add(project.getId());
                }
                if(CollectionUtils.isNotEmpty(projectList)) {
                    projectRepository.save(projectList);
                }
            }
            if (CollectionUtils.isNotEmpty(projectIdSet)) {
                List<Task> projectTaskList = taskRepository.findByProjectIdInAndDelFlagNot(projectIdSet, true);
                if (CollectionUtils.isNotEmpty(projectTaskList)) {
                    for (Task task : projectTaskList) {
                        taskIdsSet.add(task.getId());
                        task.setPm(newBudgetEO.getPm());
                        //下面做两个事儿
                        // 1.如果老管理员不是成员，那么直接干掉关系
                        // 2.直接给新管理员建立关系
                        if (CommonUtil.arrayContains(task.getMemberIds(), oldBudgetEO.getPm()) < 0) {
                            oldUserWithProjects.getProjectIds().remove(task.getId());
                        }
                        newUserWithProjects.getTaskIds().add(task.getId());
                    }
                    if(CollectionUtils.isNotEmpty(projectTaskList)) {
                        taskRepository.save(projectTaskList);
                    }
                }
            }
            List<Task> budgetTaskList = taskRepository.findByBudgetIdAndDelFlagNot(newBudgetEO.getId(), true);
            if (CollectionUtils.isNotEmpty(budgetTaskList)) {
                for (Task task : budgetTaskList) {
                    taskIdsSet.add(task.getId());
                    task.setPm(newBudgetEO.getPm());
                    //下面做两个事儿
                    // 1.如果老管理员不是成员，那么直接干掉关系
                    // 2.直接给新管理员建立关系
                    if (CommonUtil.arrayContains(task.getMemberIds(), oldBudgetEO.getPm()) < 0) {
                        oldUserWithProjects.getProjectIds().remove(task.getId());
                    }
                    newUserWithProjects.getTaskIds().add(task.getId());
                }
                taskRepository.save(budgetTaskList);
            }
            Set<String> childTaskIdSet = new HashSet<>();
            if (CollectionUtils.isNotEmpty(taskIdsSet)) {
                List<ChildrenTask> childrenTaskList = childTaskRepository.findByTaskIdIn(taskIdsSet);
                if (CollectionUtils.isNotEmpty(childrenTaskList)) {
                    for (ChildrenTask childrenTask : childrenTaskList) {
                        childTaskIdSet.add(childrenTask.getId());
                        childrenTask.setPm(newBudgetEO.getPm());
                        //下面做两个事儿
                        // 1.如果老管理员不是成员，那么直接干掉关系
                        // 2.直接给新管理员建立关系
                        if (CommonUtil.arrayContains(childrenTask.getMemberIds(), oldBudgetEO.getPm()) < 0) {
                            oldUserWithProjects.getProjectIds().remove(childrenTask.getId());
                        }
                        newUserWithProjects.getTaskIds().add(childrenTask.getId());
                    }
                }
                childTaskRepository.save(childrenTaskList);
            }
            if (StringUtils.equals(oldBudgetEO.getPm(), oldBudgetEO.getCreateUserId())
                || StringUtils.equals(oldBudgetEO.getPm(), newBudgetEO.getBusinessAdminId())) {
                oldUserWithProjects.getBusinessIds().addAll(budgetIdSet);
                oldUserWithProjects.getProjectIds().addAll(projectIdSet);
                oldUserWithProjects.getTaskIds().addAll(taskIdsSet);
                oldUserWithProjects.getChildTaskIds().addAll(childTaskIdSet);
            }

            if (CommonUtil.setContainsSetOne(oldUserWithProjects.getProjectIds(),projectIdSet)
                    ||CommonUtil.setContainsSetOne(oldUserWithProjects.getTaskIds(),taskIdsSet)
                    ||CommonUtil.setContainsSetOne(oldUserWithProjects.getChildTaskIds(),childTaskIdSet)){
                oldUserWithProjects.getBusinessIds().add(newBudgetEO.getId());
            }
            userWithProjectsRepository.save(newUserWithProjects);
            userWithProjectsRepository.save(oldUserWithProjects);
        }
    }

    /**
     * add by dingqiang 更新业务的PM时，更新用户和业务的关联关系,
     * 更新业务下的项目，任务，子任务和用户的关联关系
     */
    public void updateUserBudgetAdminNew(BudgetEO newBudgetEO, BudgetEO oldBudgetEO) {
        //TODO 先写在这里，回头加上service方法
        //budgetHistoryEOService.operateBudgetHistory(budgetEO, "update");
        if (!StringUtils.equals(newBudgetEO.getBusinessAdminId(), oldBudgetEO.getBusinessAdminId())) {
            UserWithProjects newUserWithProjects = userWithProjectsRepository.findOne(newBudgetEO.getBusinessAdminId());
            if (null == newUserWithProjects ){
                newUserWithProjects = new UserWithProjects();
                newUserWithProjects.setUserId(newBudgetEO.getBusinessAdminId());
            }
            newUserWithProjects.getBusinessIds().add(newBudgetEO.getId());
            UserWithProjects oldUserWithProjects = userWithProjectsRepository.findOne(oldBudgetEO.getBusinessAdminId());
            oldUserWithProjects.getBusinessIds().remove(newBudgetEO.getId());
            List<Project> projectList = projectRepository.findByBudgetIdAndDelFlagNot(newBudgetEO.getId(), true);
            Set<String> projectIdSet = new HashSet<>();
            Set<String> taskIdsSet = new HashSet<>();
            Set<String> childTaskIdSet = new HashSet<>();
            if (CollectionUtils.isNotEmpty(projectList)) {
                for (Project project : projectList) {
                    project.setBusinessAdminId(newBudgetEO.getBusinessAdminId());
                    project.setBusinessAdminName(newBudgetEO.getBusinessAdminName());
                    if (StringUtils.isNotEmpty(project.getProjectLeaderId())) {
                        projectIdSet.add(project.getId());
                    }
                    //下面做两个事儿
                    // 1.如果老管理员不是成员，那么直接干掉关系
                    // 2.直接给新管理员建立关系
                    if (CommonUtil.arrayContains(project.getProjectMemberIds(), oldBudgetEO.getBusinessAdminId()) < 0) {
                        oldUserWithProjects.getProjectIds().remove(project.getId());
                    }
                    newUserWithProjects.getProjectIds().add(project.getId());
                }
                projectRepository.save(projectList);
            }
            if (CollectionUtils.isNotEmpty(projectIdSet)) {
                List<Task> projectTaskList = taskRepository.findByProjectIdInAndDelFlagNot(projectIdSet, true);
                if (CollectionUtils.isNotEmpty(projectTaskList)) {
                    for (Task task : projectTaskList) {
                        taskIdsSet.add(task.getId());
                        task.setBusinessAdminId(newBudgetEO.getBusinessAdminId());
                        task.setBusinessAdminName(newBudgetEO.getBusinessAdminName());
                        //下面做两个事儿
                        // 1.如果老管理员不是成员，那么直接干掉关系
                        // 2.直接给新管理员建立关系
                        if (CommonUtil.arrayContains(task.getMemberIds(), oldBudgetEO.getBusinessAdminId()) < 0) {
                            oldUserWithProjects.getTaskIds().remove(task.getId());
                        }
                        newUserWithProjects.getTaskIds().add(task.getId());
                    }
                    taskRepository.save(projectTaskList);
                }
            }
            List<Task> budgetTaskList = taskRepository.findByBudgetIdAndDelFlagNot(newBudgetEO.getId(), true);
            if (CollectionUtils.isNotEmpty(budgetTaskList)) {
                for (Task task : budgetTaskList) {
                    task.setBusinessAdminId(newBudgetEO.getBusinessAdminId());
                    task.setBusinessAdminName(newBudgetEO.getBusinessAdminName());
                    taskIdsSet.add(task.getId());
                    //下面做两个事儿
                    // 1.如果老管理员不是成员，那么直接干掉关系
                    // 2.直接给新管理员建立关系
                    if (CommonUtil.arrayContains(task.getMemberIds(), oldBudgetEO.getBusinessAdminId()) < 0) {
                        oldUserWithProjects.getTaskIds().remove(task.getId());
                    }
                    newUserWithProjects.getTaskIds().add(task.getId());
                }
                taskRepository.save(budgetTaskList);
            }
            if (CollectionUtils.isNotEmpty(taskIdsSet)) {
                List<ChildrenTask> childrenTaskList = childTaskRepository.findByTaskIdIn(taskIdsSet);
                if (CollectionUtils.isNotEmpty(childrenTaskList)) {
                    for (ChildrenTask childrenTask : childrenTaskList) {
                        childTaskIdSet.add(childrenTask.getId());
                        childrenTask.setBusinessAdminId(newBudgetEO.getBusinessAdminId());
                        childrenTask.setBusinessAdminName(newBudgetEO.getBusinessAdminName());
                        //下面做两个事儿
                        // 1.如果老管理员不是成员，那么直接干掉关系
                        // 2.直接给新管理员建立关系
                        if (CommonUtil.arrayContains(childrenTask.getMemberIds(), oldBudgetEO.getBusinessAdminId()) < 0) {
                            oldUserWithProjects.getChildTaskIds().remove(childrenTask.getId());
                        }
                        newUserWithProjects.getChildTaskIds().add(childrenTask.getId());
                    }
                    childTaskRepository.save(childrenTaskList);
                }
            }
            // 如果老管理员 是创建人 或者 PM 那么相关关系得都挂上
            if (StringUtils.equals(oldBudgetEO.getBusinessAdminId(), oldBudgetEO.getCreateUserId()) ||
                    StringUtils.equals(oldBudgetEO.getBusinessAdminId(), newBudgetEO.getPm())) {
                oldUserWithProjects.getBusinessIds().add(newBudgetEO.getId());
                oldUserWithProjects.getProjectIds().addAll(projectIdSet);
                oldUserWithProjects.getTaskIds().addAll(taskIdsSet);
                oldUserWithProjects.getChildTaskIds().addAll(childTaskIdSet);
            }
            if (CommonUtil.setContainsSetOne(oldUserWithProjects.getProjectIds(),projectIdSet)
                    ||CommonUtil.setContainsSetOne(oldUserWithProjects.getTaskIds(),taskIdsSet)
                    ||CommonUtil.setContainsSetOne(oldUserWithProjects.getChildTaskIds(),childTaskIdSet)){
                oldUserWithProjects.getBusinessIds().add(newBudgetEO.getId());
            }
            userWithProjectsRepository.save(newUserWithProjects);
            userWithProjectsRepository.save(oldUserWithProjects);
        }
    }

    public void moveOldToNewBudget(String oldBudgetId, String newBudgetId) {
        //1. 查出旧的业务任务
        List<Task> taskList = taskRepository.findByBudgetId(oldBudgetId);
        BudgetEO newBudgetEO = dao.selectByPrimaryKey(newBudgetId);
        String newPm = newBudgetEO.getPm();
        boolean isTaskMemberFlag = false;

        BudgetEO oldBudgetEO = dao.selectByPrimaryKey(oldBudgetId);
        String oldPm = oldBudgetEO.getPm();
        Set<String> memberIdSet = new HashSet<>();
        Set<String> taskIdSet = new HashSet<>();
        //找出所有的任务成员  为下面 人物成员看到新的业务做准备 并将任务的业务Id 改成新的
        for (Task task : taskList) {
            taskIdSet.add(task.getId());
            task.setBudgetId(newBudgetId);
            memberIdSet.addAll(getMemberIdSet(task));
        }

        memberIdSet.add(newPm);
        if (memberIdSet.contains(oldPm)) {
            isTaskMemberFlag = true;
        } else {
            memberIdSet.add(oldPm);
        }

        List<UserWithProjects> userWithProjectsList = userWithProjectsRepository.findByUserIdIn(memberIdSet);

        for (UserWithProjects userWithProjects : userWithProjectsList) {
            memberIdSet.remove(userWithProjects.getUserId());

            userWithProjects.getBusinessIds().remove(oldBudgetId);
            if (StringUtils.equals(userWithProjects.getUserId(), oldPm) && !isTaskMemberFlag) {
                continue;
            }
            userWithProjects.getBusinessIds().add(newBudgetId);
            if (StringUtils.equals(userWithProjects.getUserId(), newPm)) {
                userWithProjects.getTaskIds().addAll(taskIdSet);
            }
        }
        if (CollectionUtils.isNotEmpty(memberIdSet)) {
            for (String userId : memberIdSet) {
                UserWithProjects userWithProjects = new UserWithProjects();
                userWithProjects.setUserId(userId);
                userWithProjects.getBusinessIds().add(newBudgetId);
                if (StringUtils.equals(userWithProjects.getUserId(), newPm)) {
                    userWithProjects.getTaskIds().addAll(taskIdSet);
                }
                userWithProjectsList.add(userWithProjects);
            }
        }

        dao.deleteByPrimaryKey(oldBudgetId);
        taskRepository.save(taskList);
        userWithProjectsRepository.save(userWithProjectsList);
    }

    private Set<String> getMemberIdSet(Task task) {
        Set<String> memberIdSet = new HashSet<>();
        if (StringUtils.isNotEmpty(task.getApproveUserId())) {
            memberIdSet.add(task.getApproveUserId());
        }
        if (StringUtils.isNotEmpty(task.getCreateUserId())) {
            memberIdSet.add(task.getCreateUserId());
        }
        if (CollectionUtils.isNotEmpty(task.getMemberIds())) {
            for (String userId : task.getMemberIds()) {
                memberIdSet.add(userId);
            }
        }

        return memberIdSet;
    }

    public String createUserWithProjectByUserId(String userId) {
        Set<String> resBudgetIdSet = new HashSet<>();
        Set<String> resProjectIdSet = new HashSet<>();
        Set<String> resTaskIdSet = new HashSet<>();
        Set<String> resChildTaskIdSet = new HashSet<>();

        // 1. 查询该用户的userwithproject
        UserWithProjects userWithProjects = userWithProjectsRepository.findOne(userId);
        // 2.查询该用户的业务
        List<BudgetEO> budgetEOList = dao.selectByPM(userId);
        for (BudgetEO budgetEO : budgetEOList) {
            resBudgetIdSet.add(budgetEO.getId());
        }
        if (CollectionUtils.isNotEmpty(resBudgetIdSet)) {
            //是这个用户创建的所有业务的项目任务子任务都得给这个用户挂上
            List<Project> managerPro = projectRepository.findByBudgetIdIn(resBudgetIdSet);
            for (Project project : managerPro) {
                resProjectIdSet.add(project.getId());
            }
        }
        if (CollectionUtils.isNotEmpty(resBudgetIdSet)) {
            List<Task> managerTask1 = taskRepository.findByBudgetIdIn(resBudgetIdSet); //业务任务的
            if (CollectionUtils.isNotEmpty(managerTask1)) {
                for (Task task : managerTask1) {
                    resTaskIdSet.add(task.getId());
                }
            }
        }
        if (CollectionUtils.isNotEmpty(resProjectIdSet)) {
            List<Task> managerTask2 = taskRepository.findByProjectIdIn(resProjectIdSet); //项目任务的
            if (CollectionUtils.isNotEmpty(managerTask2)) {
                for (Task task : managerTask2) {
                    resTaskIdSet.add(task.getId());
                }
            }
        }
        if (CollectionUtils.isNotEmpty(resTaskIdSet)) {
            List<ChildrenTask> managerChildrenTaskList = childTaskRepository.findByTaskIdIn(resTaskIdSet);
            for (ChildrenTask childrenTask : managerChildrenTaskList) {
                resChildTaskIdSet.add(childrenTask.getId());
            }
        }
        // 3.查询该用户管理或参与的项目
        List<Project> memProList = projectRepository.findByProjectMemberIdsInAndDelFlagNot(userId, true);
        if (CollectionUtils.isNotEmpty(memProList)) {
            for (Project project : memProList) {
                resProjectIdSet.add(project.getId());
                resBudgetIdSet.add(project.getBudgetId());
            }
        }
        List<Project> leaderProList = projectRepository.findByProjectMemberIdsInAndDelFlagNot(userId, true);
        if (CollectionUtils.isNotEmpty(leaderProList)) {
            for (Project project : leaderProList) {
                resProjectIdSet.add(project.getId());
                resBudgetIdSet.add(project.getBudgetId());
            }
        }
        // 4.查询该用户管理或参与的task
        List<Task> memTaskList = taskRepository.findByMemberIdsInAndDelFlagNot(userId, true);
        if (CollectionUtils.isNotEmpty(memTaskList)) {
            for (Task task : memTaskList) {
                resTaskIdSet.add(task.getId());
                resProjectIdSet.add(task.getProjectId());
                resBudgetIdSet.add(task.getBudgetId());
            }
        }
        List<Task> leaderTaskList = taskRepository.findByCreateUserIdAndDelFlagNot(userId, true);
        if (CollectionUtils.isNotEmpty(leaderTaskList)) {
            for (Task task : leaderTaskList) {
                resTaskIdSet.add(task.getId());
                resProjectIdSet.add(task.getProjectId());
                resBudgetIdSet.add(task.getBudgetId());
            }
        }
        // 5.查询该用户管理或参与的childtask
        List<ChildrenTask> childrenTaskList = childTaskRepository.findByPersonIdAndDelFlagNot(userId, true);
        if (CollectionUtils.isNotEmpty(childrenTaskList)) {
            for (ChildrenTask childrenTask : childrenTaskList) {
                resChildTaskIdSet.add(childrenTask.getId());
                resTaskIdSet.add(childrenTask.getTaskId());
                resProjectIdSet.add(childrenTask.getProjectId());
                resBudgetIdSet.add(childrenTask.getBudgetId());
            }
        }
        if (null == userWithProjects) {
            userWithProjects = new UserWithProjects();
            userWithProjects.setUserId(userId);
        }
        userWithProjects.setBusinessIds(resBudgetIdSet);
        userWithProjects.setProjectIds(resProjectIdSet);
        userWithProjects.setTaskIds(resTaskIdSet);
        userWithProjects.setChildTaskIds(resChildTaskIdSet);
        userWithProjectsRepository.save(userWithProjects);

        return "重新构建成功！";
    }

    public void setPM(BudgetEO formBudgetEO) {
        BudgetEO budgetEO = dao.selectByPrimaryKey(formBudgetEO.getId());
        String newPM = formBudgetEO.getPm();
        if (!StringUtils.equals(budgetEO.getPm(), newPM)) {
            //不查出项目池里的
            List<Project> projects = projectRepository.findByBudgetIdAndDelFlagNot(formBudgetEO.getId(), Boolean.TRUE);
            if (CollectionUtils.isNotEmpty(projects)) {
                for (Project project : projects) {
                    if (StringUtils.isNotEmpty(project.getProjectLeaderId())) {
                        project.setPm(newPM);
                        projectRepository.save(project);
                        List<Task> taskList = taskRepository
                            .findByProjectIdAndDelFlagNot(project.getId(), Boolean.TRUE);
                        if (CollectionUtils.isNotEmpty(taskList)) {
                            for (Task task : taskList) {
                                task.setPm(newPM);
                                taskRepository.save(task);
                                List<ChildrenTask> childrenTasks = childTaskRepository
                                    .findByTaskIdEqualsAndDelFlagNot(task.getId(), Boolean.TRUE);
                                if (CollectionUtils.isNotEmpty(childrenTasks)) {
                                    for (ChildrenTask childrenTask : childrenTasks) {
                                        childrenTask.setPm(newPM);
                                        childTaskRepository.save(childrenTask);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            //业务任务
            List<Task> tasks = taskRepository.findByBudgetIdAndDelFlagNot(formBudgetEO.getId(), Boolean.TRUE);
            if (CollectionUtils.isNotEmpty(tasks)) {
                for (Task task : tasks) {
                    task.setPm(newPM);
                    taskRepository.save(task);
                    List<ChildrenTask> childrenTasks = childTaskRepository
                        .findByTaskIdEqualsAndDelFlagNot(task.getId(), Boolean.TRUE);
                    if (CollectionUtils.isNotEmpty(childrenTasks)) {
                        for (ChildrenTask childrenTask : childrenTasks) {
                            childrenTask.setPm(newPM);
                            childTaskRepository.save(childrenTask);
                        }
                    }
                }
            }
        }
    }

    public List<BudgetEO> getBelongBudget() {
        /**
         * 超级管理员应当查询出来所有业务
         */
        //查询每个人隐藏的业务
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("登录过期，请重新登录！");
        }
        List<UserProjectCustom> userProjectCustomList =
            userProjectCustomDao.findHideByStatusAndTypeAndUserId("0", "1", userId);
        List<BudgetEO> resultBudgetEOList = new ArrayList<>();

        if (superAdminService.isSuperAdmin()) {
            //return dao.findAll(null);
            resultBudgetEOList = dao.findAll(null);
        } else {
            //budgetEOList = dao.selectByCreateUserId(userId);
            //小猴子:
            //不能这么查了
            //得从UserWithProject 里根据userId 拿到businessIds
            //然后再根据这个ID 去查业务
            UserWithProjects userWithProjects = userWithProjectsRepository.findOne(userId);
            if (null != userWithProjects && CollectionUtils.isNotEmpty(userWithProjects.getBusinessIds())) {
                userWithProjects.getBusinessIds().removeAll(Collections.singleton(null));
                resultBudgetEOList = dao.findByIds(userWithProjects.getBusinessIds(), null);
//                List<BudgetEO> budgetEOList = dao.findByIds(userWithProjects.getBusinessIds(), null);
//                for (BudgetEO budgetEO : budgetEOList) {
//                    if (StringUtils.equals(userId, budgetEO.getBusinessAdminId()) || StringUtils
//                        .equals(userId, budgetEO.getPm())) {
//                        resultBudgetEOList.add(budgetEO);
//                    }
//                }
            }
        }
        //过滤隐藏的业务
        List<BudgetEO> removeList = new ArrayList<>();
        if (StringUtils.isNotEmpty(userProjectCustomList) && StringUtils.isNotEmpty(resultBudgetEOList)) {
            for (UserProjectCustom userProjectCustom : userProjectCustomList) {
                String hideBudgetId = userProjectCustom.getBusinessid();
                for (BudgetEO budgetEO : resultBudgetEOList) {
                    if (hideBudgetId.equals(budgetEO.getId())) {
                        removeList.add(budgetEO);
                        break;
                    }
                }
            }
            if (removeList.size() > 0) {
                resultBudgetEOList.removeAll(removeList);
            }
        }
        return resultBudgetEOList;
    }

    /**
     * 统计业务的每个月的人力投入情况
     *
     * @param budgetId
     * @return
     */
    public Map<Integer, Double> getManDayByMonth(String budgetId) {
        Map<Integer, Double> resultMap = new HashMap<>(12);
        Set<String> ids = new HashSet<>();
        //拿到当前业务下的所有任务成员和项目成员
        List<Project> projects = projectRepository.findByBudgetIdAndDelFlagNot(budgetId, Boolean.TRUE);
        if (CollectionUtils.isNotEmpty(projects)) {
            for (Project project : projects) {
                ids.addAll(Lists.newArrayList(project.getProjectMemberIds()));
            }
        }
        List<Task> tasks = taskRepository.findByBudgetIdAndDelFlagNot(budgetId, Boolean.TRUE);
        if (CollectionUtils.isNotEmpty(tasks)) {
            for (Task task : tasks) {
                ids.addAll(Lists.newArrayList(task.getMemberIds()));
            }
        }
        //根据人员信息按月统计日报信息，计算出每个人的人力投入
        BoolQueryBuilder builder = QueryBuilders.boolQuery();
        BoolQueryBuilder qb = QueryBuilders.boolQuery();
        BigDecimal eight = BigDecimal.valueOf(8.0);
        BigDecimal four = BigDecimal.valueOf(4.0);
        String[] idArray;
        ChildrenTask childrenTask;
        Task task;
        Project project;
        for (int i = 1; i < 13; i++) {
            long start = DateUtil.getTimesMonthmorning(i);
            long end = DateUtil.getTimesMonthnight(i);
            if (CollectionUtils.isNotEmpty(ids)) {
                for (String id : ids) {
                    qb.should(QueryBuilders.termQuery("createUserId", id));
                }
                builder.must(
                    QueryBuilders.rangeQuery("dailyCreateTime")
                                 .from(start)
                                 .to(end)
                                 //包含下界
                                 .includeLower(true)
                                 //不包含上界
                                 .includeUpper(false));
                builder.must(qb);
                ArrayList<Daily> dailies = Lists.newArrayList(dailyRepository.search(builder));
                //剔除日报信息中不属于当前业务的工时
                double allManDay = 0;
                if (CollectionUtils.isNotEmpty(dailies)) {
                    for (Daily daily : dailies) {
                        idArray = daily.getTaskIdArray();
                        BigDecimal num = BigDecimal.valueOf(idArray.length);
                        BigDecimal workTime = four.divide(num, 4, ROUND_HALF_DOWN);
                        for (String id : idArray) {
                            childrenTask = childTaskRepository.findByIdAndDelFlagNot(id, Boolean.TRUE);
                            if (null == childrenTask) {
                                task = taskRepository.findByIdAndDelFlagNot(id, Boolean.TRUE);
                                if (null == task) {
                                    continue;
                                }
                                //业务任务 同时不是当前业务的任务跳过
                                if (StringUtils.isEmpty(task.getProjectId())) {
                                    if (!StringUtils.equals(budgetId, task.getBudgetId())) {
                                        continue;
                                    }
                                } else {
                                    //项目任务  不是当前业务下的项目任务跳过
                                    project = projectRepository
                                        .findByIdAndDelFlagNot(task.getProjectId(), Boolean.TRUE);
                                    if (!StringUtils.equals(budgetId, project.getBudgetId())) {
                                        continue;
                                    }
                                }
                            } else {
                                //子任务ID
                                task = taskRepository.findByIdAndDelFlagNot(childrenTask.getTaskId(), Boolean.TRUE);
                                //业务任务 同时不是当前业务的任务跳过
                                if (null != task) {
                                    if (StringUtils.isEmpty(task.getProjectId())) {
                                        if (!StringUtils.equals(budgetId, task.getBudgetId())) {
                                            continue;
                                        }
                                    } else {
                                        //项目任务  不是当前业务下的项目任务跳过
                                        project = projectRepository
                                            .findByIdAndDelFlagNot(task.getProjectId(), Boolean.TRUE);
                                        if (null != project && !StringUtils.equals(budgetId, project.getBudgetId())) {
                                            continue;
                                        }
                                    }
                                }
                            }
                            allManDay += workTime.doubleValue();
                        }
                    }
                }

                if (allManDay != 0) {
                    BigDecimal bd = BigDecimal.valueOf(allManDay);
                    allManDay = bd.divide(eight, 2, ROUND_HALF_DOWN).doubleValue();
                    resultMap.put(i, allManDay);
                }
            } else {
                resultMap.put(i, 0.0);
            }
        }
        return resultMap;
    }

    //有验证的Excel导入
    public List<ExcelVerifyHanlderErrorResult> ExcelImportVerify(InputStream is, ImportParams params) throws Exception {
        ExcelImportResult<BusinessDto> result = ExcelImportUtil.importExcelVerify(is, BusinessDto.class, params);
        List<ExcelVerifyHanlderErrorResult> errors = result.getErrors();
        List<BusinessDto> datas = result.getList();
        importData(datas);
        return errors;
    }

    public void importData(List<BusinessDto> datas) throws Exception {
        for (BusinessDto dto : datas) {
            BudgetEO eo;
            eo = beanMapper.map(dto, BudgetEO.class);
            eo.setId(UUID.randomUUID10());
            eo.setCreateUserId("GHVRTMA9H2");
            eo.setCurrentYear(DateUtils.dateToString(new Date(), "yyyy"));
            dao.insertSelective(eo);
            saveUserBudgetPM(eo);
            saveUserBudgetBusinessAdmin(eo);
        }
    }

    public List<BudgetEO> findByIds(Set<String> idList) {
        idList.removeAll(Collections.singleton(null));
        return dao.findByIds(idList, null);
    }

    /**
     * 根据业务名称模糊查询Budget List
     *
     * @author Wei Jinjin
     * @date 2020-04-08
     */
    public List<BudgetEO> findAllBudgetNameLikeAndByType(String budgetName) {
        return dao.findAllBudgetNameLikeAndByType(budgetName, "0");
    }
}
