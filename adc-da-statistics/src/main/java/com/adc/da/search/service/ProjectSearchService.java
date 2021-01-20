package com.adc.da.search.service;

import com.adc.da.budget.constant.Role;
import com.adc.da.budget.dao.BudgetEODao;
import com.adc.da.budget.dao.OrgListDao;
import com.adc.da.budget.dao.UserEPDao;
import com.adc.da.budget.entity.BudgetEO;
import com.adc.da.budget.entity.Project;
import com.adc.da.budget.query.project.ProjectQuery;
import com.adc.da.budget.service.ProjectService;
import com.adc.da.login.util.UserUtils;
import com.adc.da.statistics.dao.ProjectWorktimeEODao;
import com.adc.da.statistics.entity.ProjectWorktimeEO;
import com.adc.da.statistics.page.ProjectWorktimeEOPage;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.adc.da.budget.constant.ProjectSearchField.DEL_FLAG;

/**
 * @author Lee Kwanho 李坤澔
 * @author Ding Qiang  丁强
 *     date 2019-09
 */
@Service
@Slf4j
public class ProjectSearchService {

    @Autowired
    private UserEPDao userEPDao;

    @Autowired
    private BudgetEODao budgetEODao;

    @Autowired
    private ProjectWorktimeEODao workTimeDao;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectSearchQueryService projectSearchOrderedService;

    /**
     * 项目查询页
     *
     * @author weijinjin
     *     date 2019-06-18
     */
    public List<Project> newFindByPage(ProjectQuery page) {
        List<String> deptIds = projectService.getDeptIds();
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("请登录！");
        }

        BoolQueryBuilder boolQueryBuilder = projectSearchOrderedService.initQueryBuilder(page);
        Pageable pageable = new PageRequest(page.getPage() - 1, page.getPageSize());
        SearchQuery searchQuery = initSearchQuery(deptIds, userId, pageable, boolQueryBuilder);

        List<Project> projectList = projectSearchOrderedService.doSearch(searchQuery, page);

        addBudgetNameAndWorkTime(projectList);
        return projectList;
    }

    /**
     * 项目查询页
     *
     * @author weijinjin
     *     date 2019-06-18
     */
    public List<Project> newFindByPage4Form(ProjectQuery page) {
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("请登录！");
        }
        List<String> budgetIdList  = budgetEODao.findAllBudgetIdByNameNotLikeAndPropertyArr("旧-%",page.getProjectTypeArr());
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        BoolQueryBuilder subBoolQueryBuilder = QueryBuilders.boolQuery();
        //普通人员
        subBoolQueryBuilder.should(QueryBuilders.termQuery("projectLeaderId", userId))
                .should(QueryBuilders.termQuery("projectMemberIds", userId));
        boolQueryBuilder.mustNot(QueryBuilders.termQuery(DEL_FLAG, true));
        boolQueryBuilder.must(QueryBuilders.termsQuery("budgetId", budgetIdList));
        if (StringUtils.isNotEmpty(page.getSearchName())) {
            boolQueryBuilder.must(QueryBuilders.wildcardQuery("name", "*" + page.getSearchName() + "*"));
        }
        boolQueryBuilder.must(subBoolQueryBuilder);
        Pageable pageable = new PageRequest(page.getPage() - 1, page.getPageSize());
        SearchQuery searchQuery = new NativeSearchQueryBuilder().withPageable(pageable)
                .withQuery(boolQueryBuilder)
                .build();

        List<Project> projectList = projectSearchOrderedService.doSearch(searchQuery, page);

        addBudgetNameAndWorkTime(projectList);
        return projectList;
    }

    @Autowired
    private OrgListDao orgListDao;

    /**
     * 根据用户角色组装搜索数据
     *
     * @param deptIds
     * @param userId
     * @param pageable
     * @param boolQueryBuilder
     * @return
     */
    public SearchQuery initSearchQuery(List<String> deptIds, String userId, Pageable pageable,
        BoolQueryBuilder boolQueryBuilder) {
        SearchQuery searchQuery;
        BoolQueryBuilder bq;
        Subject subject = SecurityUtils.getSubject();
        if (subject.hasRole(Role.SUPER_ADMIN) || subject.hasRole(Role.ADMIN) || subject.hasRole(Role.ZHU_REN)) {
            // 分数、分页
            // 无需做处理
            // 直接返回
            return new NativeSearchQueryBuilder().withPageable(pageable)
                                                 .withQuery(boolQueryBuilder)
                                                 .build();
        } else if (subject.hasRole(Role.BEN_BU_ZHANG)) {
            Set<String> allSubOrgIds = new HashSet<>();
            /*
             * 拿所有子部门 deptIds.length() 基本都是1，极个别是2，所以不用管这个for循环
             */
            for (String deptId : deptIds) {
                allSubOrgIds.addAll(orgListDao.getOrgAndSubOrgIds(deptId));
            }
            bq = QueryBuilders.boolQuery()
                              .should(QueryBuilders.termsQuery("deptId", allSubOrgIds));
        } else if (subject.hasRole(Role.BU_ZHANG)) {
            bq = QueryBuilders.boolQuery()
                              .should(QueryBuilders.termsQuery("deptId", deptIds));
        } else {
            //普通人员
            bq = QueryBuilders.boolQuery()
                              .should(QueryBuilders.termQuery("projectLeaderId", userId))
                              .should(QueryBuilders.termQuery("projectMemberIds", userId));
            // 分数、分页
        }
        boolQueryBuilder.must(bq);
        searchQuery = new NativeSearchQueryBuilder().withPageable(pageable)
                                                    .withQuery(boolQueryBuilder)
                                                    .build();
        return searchQuery;
    }

    /**
     * 组织业务名称
     *
     * @param projectList
     */
    public void addBudgetNameAndWorkTime(List<Project> projectList) {

        List<String> budgetIds = new ArrayList<>();
        List<String> createUserIds = new ArrayList<>();
        List<String> projectIds = new ArrayList<>(projectList.size());
        for (Project project : projectList) {
            if (null != project.getBudgetId()) {
                budgetIds.add(project.getBudgetId());
            }
            if (null != project.getCreateUserId()) {
                createUserIds.add(project.getCreateUserId());
            }
            /*
             * 添加id集合
             */
            projectIds.add(project.getId());
        }
        List<BudgetEO> budgetEOList;
        if (CollectionUtils.isNotEmpty(budgetIds)) {
            budgetEOList = budgetEODao.selectByPrimaryKeys(budgetIds);
        } else {
            budgetEOList = new ArrayList<>(1);
        }
        Map userEOMap;
        if (CollectionUtils.isNotEmpty(createUserIds)) {
            userEOMap = userEPDao.queryUserIdAndNameByIds(createUserIds);
        } else {
            userEOMap = new HashMap<>(2);
        }
        Map<String, String> workTimeMap;
        if (CollectionUtils.isNotEmpty(projectIds)) {
            workTimeMap = initWorkTime(projectIds);
        } else {
            workTimeMap = new HashMap<>(2);
        }

        Map<String, String> budgetIdNameMap = new HashMap<>();
        for (BudgetEO budgetEO : budgetEOList) {
            budgetIdNameMap.put(budgetEO.getId(), budgetEO.getProjectName());
        }

        for (Project project : projectList) {
            project.setBudget(budgetIdNameMap.get(project.getBudgetId()));
            Object map = userEOMap.get(project.getCreateUserId());
            if (null != map) {
                String userName = ((Map) map).get("USNAME").toString();
                project.setCreateUserName(userName);
            }
            /*
             * 用这个字段回显工时
             */
            project.setContractAmountStr(workTimeMap.get(project.getId()));
        }

    }

    /**
     * 初始化工时数据
     *
     * @param ids
     * @return
     */
    private Map<String, String> initWorkTime(List<String> ids) {
        List<ProjectWorktimeEO> workTimeEOList;
        ProjectWorktimeEOPage queryPage = new ProjectWorktimeEOPage();
        queryPage.setProjectIds(new ArrayList<>(ids));
        workTimeEOList = (workTimeDao.querySumWorkTime(queryPage));

        Map<String, String> workTimeMap = new HashMap<>(workTimeEOList.size());

        for (ProjectWorktimeEO eo : workTimeEOList) {
            workTimeMap.put(eo.getProjectId(), eo.getWorktime().toString());
        }

        return workTimeMap;
    }
}
