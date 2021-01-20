package com.adc.da.budget.service;

import com.adc.da.budget.dao.BudgetEODao;
import com.adc.da.budget.dto.PageDTO;
import com.adc.da.budget.entity.BudgetEO;
import com.adc.da.budget.entity.Project;
import com.adc.da.budget.repository.ProjectRepository;
import com.adc.da.budget.utils.CommonUtil;
import com.adc.da.research.entity.MemberEO;
import com.adc.da.research.service.MemberEOService;
import com.adc.da.util.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.adc.da.budget.constant.ProjectSearchField.BUDGET_ID;
import static com.adc.da.budget.constant.ProjectSearchField.CREATE_TIME;
import static com.adc.da.budget.constant.ProjectSearchField.DEL_FLAG;
import static com.adc.da.budget.constant.ProjectSearchField.FINISHED_STATUS;
import static com.adc.da.budget.constant.ProjectSearchField.NAME;
import static com.adc.da.budget.constant.ProjectSearchField.PROJECT_LEADER;
import static com.adc.da.budget.constant.ProjectSearchField.PROJECT_LEADER_ID;
import static com.adc.da.budget.constant.ProjectType.RESEARCH_PROJECT;

/**
 * <br>
 * <b>功能：</b>TS_BUDGET BudgetEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2018-10-25 <br>0.
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Slf4j
@Service("budgetProjectDTOService")
@Transactional(value = "transactionManager", readOnly = true,
    propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class BudgetProjectDTOService {

    @Autowired
    private BudgetEODao dao;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private BudgetEOService budgetEOService;

    public PageDTO getProjectsDTO(int page, int size, String businessId, String projectName,
        String projectStatus, String projectManager) {
        return this.queryByBudgetId(page, size, businessId, projectName,
            projectStatus, projectManager);

    }

    public PageDTO queryByBudgetId(int page, int size, String businessId, String projectName,
        String projectStatus, String projectManager) {

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        BoolQueryBuilder innerBoolQueryBuilder = QueryBuilders.boolQuery();

        boolQueryBuilder.must(QueryBuilders.termQuery(BUDGET_ID, businessId));
        /*
         * 过滤未申领的项目
         */
        boolQueryBuilder.must(QueryBuilders.existsQuery(PROJECT_LEADER_ID));
        boolQueryBuilder.mustNot(QueryBuilders.termQuery(DEL_FLAG, true));

        if (StringUtils.isNotEmpty(projectManager)) {
            innerBoolQueryBuilder.should(QueryBuilders.wildcardQuery(PROJECT_LEADER, "*" + projectManager + "*"));
        }
        if (StringUtils.isNotEmpty(projectName)) {
            innerBoolQueryBuilder.should(QueryBuilders.wildcardQuery(NAME, "*" + projectName + "*"));
        }
        if (StringUtils.isNotEmpty(projectStatus)) {
            innerBoolQueryBuilder.should(QueryBuilders.wildcardQuery(FINISHED_STATUS, "*" + projectStatus + "*"));
        }
        boolQueryBuilder.must(innerBoolQueryBuilder);

        return getPageDTO(boolQueryBuilder, page, size);
    }

    private PageDTO getPageDTO(QueryBuilder queryBuilder, int page, int size) {
        List<Project> queryList;
        Sort sort = new Sort(Sort.Direction.DESC, CREATE_TIME);
        Page<Project> queryPage = projectRepository.search(queryBuilder, new PageRequest(page - 1, size, sort));

        if (queryPage == null || (queryPage.getContent()) == null) {
            queryList = new ArrayList<>();
        } else {
            queryList = queryPage.getContent();
        }

        setDataListSortMapList(queryList);
        long count = (queryPage == null) ? 0 : queryPage.getTotalElements();
        return new PageDTO(count, queryList, page, size);
    }

    public void setDataListSortMapList(List<Project> dataList) {
        List<String> budgetIds = new ArrayList<>();
        for (Project project : dataList) {
            if (StringUtils.isNotEmpty(project.getBudgetId())) {
                budgetIds.add(project.getBudgetId());
            }
            if (RESEARCH_PROJECT == project.getProjectType()) {
                project.setMapList(sortMapList(project.getId()));
            }
        }
        List<List<String>> lists = CommonUtil.splitList(budgetIds, 512);
        List<BudgetEO> budgetEOList = new ArrayList<>();
        for (List<String> ids : lists) {
            budgetEOList.addAll(budgetEOService.selectByPrimaryKeys(ids));
        }

        Map<String, String> budgetNameMap = new HashMap<>(budgetEOList.size());
        for (BudgetEO budgetEO : budgetEOList) {
            budgetNameMap.put(budgetEO.getId(), budgetEO.getProjectName());
        }

        for (Project project : dataList) {
            project.setBudget(budgetNameMap.get(project.getBudgetId()));
            String businessId = project.getBusinessId();
            project.setBusiness(projectService.getBusinessName(businessId));
        }
    }

    @Autowired
    private MemberEOService memberEOService;

    /**
     * 对 科研类 - MapList进行调整
     *
     * @param projectId
     */
    public List<Map<String, String>> sortMapList(String projectId) {
        List<MemberEO> memberEOList = memberEOService.getMemberInfoWithSort(projectId);
        List<Map<String, String>> mapList = new ArrayList<>();

        for (MemberEO user : memberEOList) {
            Map<String, String> map = new HashMap<>();
            map.put("id", user.getMemberNameId());
            map.put("name", user.getMemberName());
            mapList.add(map);
        }

        return mapList;

    }

}
