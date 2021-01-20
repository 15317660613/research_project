package com.adc.da.business.service;

import com.adc.da.base.page.BasePage;
import com.adc.da.business.dao.OperatingBudgetEODao;
import com.adc.da.business.entity.BudgetIncomeCalculateEO;
import com.adc.da.business.entity.Project;
import com.adc.da.business.entity.Task;
import com.adc.da.business.repository.MyTaskRepository;
import com.adc.da.business.repository.ProjectRepositoryBusiness;
import com.adc.da.statistics.dao.BusinessWorktimeDao;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.StringUtils;
import com.google.common.collect.Lists;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

/**
 * describe:
 * 1.8 业务经营情况
 *
 * @author 李坤澔
 *     date 2019-06-12
 */
@Service
public class BudgetIncomeCalculateService {

    @Autowired
    private OperatingBudgetEODao dao;

    @Autowired
    private BusinessWorktimeDao businessWorktimeDao;

    /**
     * 1.8 业务经营情况
     *
     * @param businessName
     * @param year
     * @param startMonth
     * @param endMonth
     * @param page
     * @return
     * @author 丁强
     *     date 2019-07-19
     **/
    public List<BudgetIncomeCalculateEO> newQeryInvoiceAndBudgetByNameYearAndMonths(String businessName, String year,
        String startMonth, String endMonth, BasePage page) {

        List<BudgetIncomeCalculateEO> resultList = this.dao
            .queryInvoiceAndBudgetByNameYearAndMonths(businessName, year, startMonth, endMonth, page);

        List<String> idList = new ArrayList<>();
        for (BudgetIncomeCalculateEO eo : resultList) {
            String id = eo.getBusinessId();
            if (StringUtils.isNotEmpty(id)) {
                idList.add(id);
                Double workTime = businessWorktimeDao.queryBusinessWorkTimeByYearAndMonth(
                    Integer.valueOf(year),
                    Integer.valueOf(startMonth),
                    Integer.valueOf(endMonth),
                    id);
                if (null != workTime) {
                    BigDecimal workTimeTep = new BigDecimal(workTime);
                    workTime = workTimeTep.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                }
                eo.setWorkTime(workTime);
            }
        }
        //  查询ES /api/budget/budget/2HBX5DQ5VX/getProjects
        initResultMember(resultList, idList);

        return resultList;
    }

    public Integer countInvoiceAndBudgetByNameYearAndMonths(String businessName, String year,
        String startMonth, String endMonth) {
        return this.dao.countInvoiceAndBudgetByNameYearAndMonths(businessName, year, startMonth, endMonth);
    }

    @Autowired
    private ProjectRepositoryBusiness projectRepositoryBusiness;

    @Autowired
    private MyTaskRepository myTaskRepository;

    /**
     * 设置员工人数 /api/budget/budget/2HBX5DQ5VX/getProjects
     *
     * @param resultList
     * @return
     * @author Lee Kwanho 李坤澔
     *     date 2019-06-12
     **/
    private Boolean initResultMember(List<BudgetIncomeCalculateEO> resultList, List<String> idList) {

        BoolQueryBuilder queryBuilder = budgetIdQueryBuilder(idList);

        List<Project> projectList = this.projectQueryByBudgetId(queryBuilder);
        List<Task> taskList = this.taskQueryByBudgetId(queryBuilder);

        Map<String, TreeSet<String>> membersResultMap = new HashMap<>();
        TreeSet<String> memberMapList;

        /*
         *  计算项目任务的人员数量
         */
        for (Project project : projectList) {
            String budgetId = project.getBudgetId();
            memberMapList = new TreeSet<>();
            if (CollectionUtils.isNotEmpty(project.getProjectMemberIds())) {
                memberMapList.addAll(Arrays.asList(project.getProjectMemberIds()));
                if (membersResultMap.containsKey(budgetId)) {

                    memberMapList.addAll(membersResultMap.get(budgetId));
                }
                membersResultMap.put(budgetId, memberMapList);

            }

        }
        /*
         * 计算业务任务的人员数量
         */
        for (Task task : taskList) {
            String budgetId = task.getBudgetId();
            memberMapList = new TreeSet<>();
            if (CollectionUtils.isNotEmpty(task.getMemberIds())) {
                memberMapList.addAll(Arrays.asList(task.getMemberIds()));
                if (membersResultMap.containsKey(budgetId)) {

                    memberMapList.addAll(membersResultMap.get(budgetId));
                }
                membersResultMap.put(budgetId, memberMapList);

            }

        }

        for (BudgetIncomeCalculateEO result : resultList) {
            String id = result.getBusinessId();
            if (membersResultMap.containsKey(id)) {
                result.setPersonCount(membersResultMap.get(id).size());

            }
        }

        return true;
    }

    /**
     * 组装搜索条件
     *
     * @param idList
     * @return
     */
    private BoolQueryBuilder budgetIdQueryBuilder(List<String> idList) {

        BoolQueryBuilder qb = QueryBuilders.boolQuery();
        for (String budgetId : idList) {
            qb.should(QueryBuilders.termQuery("budgetId", budgetId));
        }

        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
                                                     .mustNot(QueryBuilders.termQuery("delFlag", true));
        return queryBuilder.must(qb);

    }

    /**
     * 查询   项目任务的员工数据
     *
     * @param queryBuilder
     * @return
     * @author Lee Kwanho 李坤澔
     *     date 2019-06-13
     **/
    private List<Project> projectQueryByBudgetId(BoolQueryBuilder queryBuilder) {

        return Lists.newArrayList(projectRepositoryBusiness.search(queryBuilder));

    }

    /**
     * 查询   业务任务的员工数据
     *
     * @param queryBuilder
     * @return
     */
    private List<Task> taskQueryByBudgetId(BoolQueryBuilder queryBuilder) {

        return Lists.newArrayList(myTaskRepository.search(queryBuilder));

    }

}
