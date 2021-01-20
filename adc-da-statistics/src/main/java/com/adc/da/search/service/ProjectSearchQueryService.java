package com.adc.da.search.service;

import com.adc.da.budget.constant.ProjectSearchField;
import com.adc.da.budget.dao.BudgetEODao;
import com.adc.da.budget.dao.UserEPDao;
import com.adc.da.budget.entity.Project;
import com.adc.da.budget.query.QueryVO;
import com.adc.da.budget.query.project.ProjectQuery;
import com.adc.da.budget.repository.ProjectRepository;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.adc.da.budget.constant.ProjectSearchField.BUDGET_ID;
import static com.adc.da.budget.constant.ProjectSearchField.CREATE_TIME;
import static com.adc.da.budget.constant.ProjectSearchField.CREATE_USER_ID;
import static com.adc.da.budget.constant.ProjectSearchField.EQUAL;
import static com.adc.da.budget.constant.ProjectSearchField.FINISHED_STATUS;
import static com.adc.da.budget.constant.ProjectSearchField.LIKE;
import static com.adc.da.budget.constant.ProjectSearchField.MEMBER_NAMES;
import static com.adc.da.budget.constant.ProjectSearchField.NAME;
import static com.adc.da.budget.constant.ProjectSearchField.NOT_EQUAL;
import static com.adc.da.budget.constant.ProjectSearchField.NOT_LIKE;
import static com.adc.da.budget.constant.ProjectSearchField.PROJECT_BEGIN_TIME;
import static com.adc.da.budget.constant.ProjectSearchField.PROJECT_END_TIME;
import static com.adc.da.budget.constant.ProjectSearchField.PROJECT_LEADER;
import static com.adc.da.budget.constant.ProjectSearchField.PROJECT_MEMBER_NAMES;

/**
 * @author Lee Kwanho 李坤澔
 *     date 2019-10-17
 */
@Service
@Slf4j
public class ProjectSearchQueryService extends AbstractBaseSearchQuery<ProjectQuery,Project> {

    @Autowired
    private UserEPDao userEPDao;

    @Autowired
    private BudgetEODao budgetEODao;

    /**
     * 对所有入参元素进行初始化，预拼接查询条件
     *
     * @param allObject
     * @return
     */
    protected List<QueryBuilder> initAllQuery(List<QueryVO> allObject) {
        List<QueryBuilder> allQuery = new ArrayList<>(allObject.size());
        for (QueryVO vo : allObject) {
            switch (vo.getSearchField()) {
                /*
                 *  项目名称
                 */
                case "projectName":
                    allQuery.add(searchProjectName(vo));
                    break;
                /*
                 * 项目组成员
                 */
                case "projectMemberNames":
                    allQuery.add(searchProjectMember(vo));
                    break;
                /*
                 *  业务所属
                 */
                case "budgetBelong":
                    allQuery.add(searchProjectBudget(vo));
                    break;
                /*
                 * 项目负责人
                 */
                case "projectLeader":
                    allQuery.add(searchProjectLeader(vo));
                    break;
                /*项目开始时间 */
                case "projectCreateDate":
                    allQuery.add(searchTime(vo, PROJECT_BEGIN_TIME));
                    break;
                /*项目结束时间 */
                case "projectEndDate":
                    allQuery.add(searchTime(vo, PROJECT_END_TIME));
                    break;
                /*
                 *  状态
                 */
                case "projectStatus":
                    allQuery.add(searchProjectStatus(vo));
                    break;
                /*
                 *  创建人
                 */
                case "createUser":
                    allQuery.add(searchCreateUser(vo));
                    break;
                /*创建时间*/
                case "createDate":
                    allQuery.add(searchTime(vo, CREATE_TIME));
                    break;
                default:
            }
        }
        return allQuery;
    }

    /**
     * 所属业务名称搜索
     *
     * @param queryVOBudgetBelong
     * @return
     */
    private QueryBuilder searchProjectBudget(QueryVO queryVOBudgetBelong) {
        BoolQueryBuilder result = QueryBuilders.boolQuery();
        List<String> budgetIds;

        switch (queryVOBudgetBelong.getOperator()) {
            case EQUAL:
                budgetIds = budgetEODao.findBudgetIdsByNameEquals(queryVOBudgetBelong.getName());
                result.must(QueryBuilders.termsQuery(BUDGET_ID, budgetIds));
                break;
            case NOT_EQUAL:
                budgetIds = budgetEODao.findBudgetIdsByNameEquals(queryVOBudgetBelong.getName());
                result.mustNot(QueryBuilders.termsQuery(BUDGET_ID, budgetIds));
                break;

            case LIKE:
                budgetIds = budgetEODao.findBudgetIdsByNameLike("%" + queryVOBudgetBelong.getName() + "%");
                result.must(QueryBuilders.termsQuery(BUDGET_ID, budgetIds));
                break;
            case NOT_LIKE:
                budgetIds = budgetEODao.findBudgetIdsByNameLike("%" + queryVOBudgetBelong.getName() + "%");
                result.mustNot(QueryBuilders.termsQuery(BUDGET_ID, budgetIds));
                break;
            default:
                throw new IllegalArgumentException();
        }

        return result;
    }

    /**
     * 完成状态
     *
     * @param queryVOProjectStatus
     * @return
     */
    private QueryBuilder searchProjectStatus(QueryVO queryVOProjectStatus) {
        return searchStringField(queryVOProjectStatus, FINISHED_STATUS);
    }

    /**
     * 项目负责人名称搜索
     *
     * @param queryVOProjectLeader
     * @return
     */
    private QueryBuilder searchProjectLeader(QueryVO queryVOProjectLeader) {
        return searchStringField(queryVOProjectLeader, PROJECT_LEADER);
    }

    /**
     * 项目名称搜索
     *
     * @param queryVOProjectName
     * @return
     */
    private QueryBuilder searchProjectName(QueryVO queryVOProjectName) {
        return searchStringField(queryVOProjectName, NAME);
    }

    /**
     * 查询项目组成员
     *
     * 选等于 对
     *
     * @param vo
     * @return
     * @see ProjectSearchField#MEMBER_NAMES 进行搜索
     *
     *     选包含 对
     * @see ProjectSearchField#PROJECT_MEMBER_NAMES 进行搜索
     */
    private QueryBuilder searchProjectMember(QueryVO vo) {
        BoolQueryBuilder result = QueryBuilders.boolQuery();

        switch (vo.getOperator()) {
            case EQUAL:
                result.must(QueryBuilders
                    .termsQuery(MEMBER_NAMES, vo.getName()));
                break;
            case NOT_EQUAL:
                result.mustNot(QueryBuilders
                    .termsQuery(MEMBER_NAMES, vo.getName()));
                break;
            case LIKE:
                result.must(QueryBuilders
                    .wildcardQuery(
                        PROJECT_MEMBER_NAMES, "*" + vo.getName() + "*"));
                break;
            case NOT_LIKE:
                result.mustNot(QueryBuilders
                    .wildcardQuery(
                        PROJECT_MEMBER_NAMES, "*" + vo.getName() + "*"));
                break;
            default:
                throw new IllegalArgumentException();
        }
        return result;

    }

    /**
     * 查询项目创建人
     *
     * @param queryVOCreateUser
     * @return
     */
    private QueryBuilder searchCreateUser(QueryVO queryVOCreateUser) {
        List<String> userIds;
        BoolQueryBuilder result = QueryBuilders.boolQuery();
        switch (queryVOCreateUser.getOperator()) {
            case EQUAL:
                userIds = userEPDao.queryUserIdByNameByEquals(queryVOCreateUser.getName());
                result.must(QueryBuilders
                    .termsQuery(CREATE_USER_ID, userIds));

                break;
            case NOT_EQUAL:
                userIds = userEPDao.queryUserIdByNameByEquals(queryVOCreateUser.getName());
                result.mustNot(QueryBuilders
                    .termsQuery(CREATE_USER_ID, userIds));
                break;
            case LIKE:
                userIds = userEPDao.queryUserIdByNameByLike("%" + queryVOCreateUser.getName() + "%");
                result.must(QueryBuilders
                    .termsQuery(CREATE_USER_ID, userIds));
                break;
            case NOT_LIKE:
                userIds = userEPDao.queryUserIdByNameByLike("%" + queryVOCreateUser.getName() + "%");
                result.mustNot(QueryBuilders
                    .termsQuery(CREATE_USER_ID, userIds));
                break;
            default:
                throw new IllegalArgumentException();
        }

        return result;

    }

    @Autowired
    private ProjectRepository projectRepository;

    /**
     * 执行搜索
     *
     * @param searchQuery
     * @param page
     * @return
     */
    public List<Project> doSearch(SearchQuery searchQuery, ProjectQuery page) {
        PageImpl<Project> result = ((PageImpl<Project>) projectRepository.search(searchQuery));
        page.getPager().setRowCount((int) (result.getTotalElements()));
        page.getPager().setPageCount(result.getTotalPages());
        return result.getContent();
    }
}
