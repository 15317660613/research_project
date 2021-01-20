package com.adc.da.search.service;

import com.adc.da.budget.dao.BudgetEODao;
import com.adc.da.budget.entity.Task;
import com.adc.da.budget.query.QueryVO;
import com.adc.da.budget.query.task.TaskQuery;
import com.adc.da.budget.repository.TaskRepository;
import com.adc.da.budget.service.MyService;
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
import static com.adc.da.budget.constant.ProjectSearchField.LIKE;
import static com.adc.da.budget.constant.ProjectSearchField.MEMBER_NAMES;
import static com.adc.da.budget.constant.ProjectSearchField.MODIFY_TIME;
import static com.adc.da.budget.constant.ProjectSearchField.NAME;
import static com.adc.da.budget.constant.ProjectSearchField.NOT_EQUAL;
import static com.adc.da.budget.constant.ProjectSearchField.NOT_LIKE;
import static com.adc.da.budget.constant.ProjectSearchField.PLAN_END_TIME;
import static com.adc.da.budget.constant.ProjectSearchField.PLAN_START_TIME;
import static com.adc.da.budget.constant.ProjectSearchField.PRIORITY;
import static com.adc.da.budget.constant.ProjectSearchField.SEARCH_BUDGET_ID;
import static com.adc.da.budget.constant.ProjectSearchField.TASK_STATUS;

/**
 * @author Lee Kwanho 李坤澔
 *     date 2019-10-17
 */
@Service
@Slf4j
public class TaskSearchQueryService extends AbstractBaseSearchQuery<TaskQuery, Task> {

    @Autowired
    private MyService myService;

    @Autowired
    private BudgetEODao budgetEODao;

    /**
     * 项目名称搜索
     *
     * @param queryVOProjectName
     * @return
     */
    private QueryBuilder searchTaskName(QueryVO queryVOProjectName) {
        return searchStringField(queryVOProjectName, NAME);
    }

    /**
     * priority
     */
    private QueryBuilder searchPriority(QueryVO queryVOProjectName) {
        return searchStringField(queryVOProjectName, PRIORITY);
    }

    /**
     * 任务状态
     */
    private QueryBuilder searchTaskStatus(QueryVO vo) {
        return searchStringField(vo, TASK_STATUS);
    }

    /**
     * 成员搜索
     *
     * @param vo
     * @return
     */
    private QueryBuilder searchMemberNames(QueryVO vo) {
        return searchStringField(vo, MEMBER_NAMES);
    }

    /**
     * 成员搜索
     *
     * @param vo
     * @return
     */
    private QueryBuilder searchProjectNames(QueryVO vo) {
        return searchStringField(vo, "projectName1");
    }

    /**
     * 成员搜索
     *
     * @param vo
     * @return
     */
    private QueryBuilder searchCreateUserName(QueryVO vo) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        List<String> ids;
        switch (vo.getOperator()) {
            case EQUAL:
                ids = myService.getUserIdByUserName(vo.getName());
                boolQueryBuilder.must(QueryBuilders.termsQuery(CREATE_USER_ID, ids));
                break;
            case NOT_EQUAL:
                ids = myService.getUserIdByUserName(vo.getName());
                boolQueryBuilder.mustNot(QueryBuilders.termsQuery(CREATE_USER_ID, ids));
                break;
            case LIKE:
                ids = myService.getUserIdByUserNameLike(vo.getName());
                boolQueryBuilder.must(QueryBuilders.termsQuery(CREATE_USER_ID, ids));
                break;
            case NOT_LIKE:
                /*
                 * 同like 然后取反
                 */
                ids = myService.getUserIdByUserNameLike(vo.getName());
                boolQueryBuilder.mustNot(QueryBuilders.termsQuery(CREATE_USER_ID, ids));
                break;
            default:
        }
        return boolQueryBuilder;
    }

    /**
     * 业务类型
     *
     * @param vo
     * @return
     */
    private QueryBuilder searchType(QueryVO vo) {
        BoolQueryBuilder result = QueryBuilders.boolQuery();
        switch (vo.getOperator()) {
            case EQUAL:
                if ("0".equals(vo.getName())) {
                    result.must(QueryBuilders.existsQuery(BUDGET_ID));
                } else {
                    result.mustNot(QueryBuilders.existsQuery(BUDGET_ID));
                }
                break;
            case NOT_EQUAL:
                if ("0".equals(vo.getName())) {
                    result.mustNot(QueryBuilders.existsQuery(BUDGET_ID));
                } else {
                    result.must(QueryBuilders.existsQuery(BUDGET_ID));
                }
                break;
            default:
                throw new IllegalArgumentException();
        }
        return result;
    }

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
                /* 任务名称*/
                case "names":
                    allQuery.add(searchTaskName(vo));
                    break;
                case "prioritys":
                    allQuery.add(searchPriority(vo));
                    break;
                /*任务类型*/
                case "types":
                    allQuery.add(searchType(vo));
                    break;
                case "memberNames":
                    allQuery.add(searchMemberNames(vo));
                    break;
                case "planStartTimes":
                    allQuery.add(searchTime(vo, PLAN_START_TIME));
                    break;
                case "planEndTimes":
                    allQuery.add(searchTime(vo, PLAN_END_TIME));
                    break;
                case "taskStatus":
                    allQuery.add(searchTaskStatus(vo));
                    break;
                case "budgetNames":
                    allQuery.add(budgetNameSearch(vo));
                    break;
                case "projectNames":
                    allQuery.add(searchProjectNames(vo));
                    break;
                case "createUserNames":
                    allQuery.add(searchCreateUserName(vo));
                    break;
                case "createTimes":
                    allQuery.add(searchTime(vo, CREATE_TIME));
                    break;
                case "updateTimes":
                    allQuery.add(searchTime(vo, MODIFY_TIME));
                    break;
                default:
            }
        }
        return allQuery;
    }

    /**
     * 任务 - 所属业务名称搜索
     *
     * @param budgetName
     */
    private QueryBuilder budgetNameSearch(QueryVO budgetName) {
        String value = budgetName.getName();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        List<String> budgetEOList;

        switch (budgetName.getOperator()) {
            case EQUAL:
                budgetEOList = budgetEODao
                    .selectBudgetIdAndBudgetNameByBudgetName(value, EQUAL);
                boolQueryBuilder.must(QueryBuilders.termsQuery(SEARCH_BUDGET_ID, budgetEOList));
                break;
            case NOT_EQUAL:
                /*
                 * 进行EQUAL 然后取反
                 */
                budgetEOList = budgetEODao
                    .selectBudgetIdAndBudgetNameByBudgetName(value, EQUAL);
                boolQueryBuilder.mustNot(QueryBuilders.termsQuery(SEARCH_BUDGET_ID, budgetEOList));
                break;
            case LIKE:
                budgetEOList = budgetEODao
                    .selectBudgetIdAndBudgetNameByBudgetName(value, LIKE);
                boolQueryBuilder.must(QueryBuilders.termsQuery(SEARCH_BUDGET_ID, budgetEOList));
                break;
            case NOT_LIKE:
                /*
                 * 进行like 然后取反
                 */
                budgetEOList = budgetEODao
                    .selectBudgetIdAndBudgetNameByBudgetName(value, LIKE);
                boolQueryBuilder.mustNot(QueryBuilders.termsQuery(SEARCH_BUDGET_ID, budgetEOList));
                break;
            default:
        }
        return boolQueryBuilder;
    }

    @Autowired
    private TaskRepository taskRepository;

    public List<Task> doSearch(SearchQuery searchQuery, TaskQuery page) {
        PageImpl<Task> result = ((PageImpl<Task>) taskRepository.search(searchQuery));
        page.getPager().setRowCount((int) (result.getTotalElements()));
        page.getPager().setPageCount(result.getTotalPages());
        return result.getContent();
    }
}
