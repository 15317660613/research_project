package com.adc.da.progress.util;

import com.adc.da.budget.entity.Project;
import com.adc.da.budget.repository.ProjectRepository;
import com.adc.da.progress.entity.ProjectRateEO;
import com.adc.da.progress.service.ProjectRateEOService;
import com.adc.da.util.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * describe:
 * 用于PR_Project_Rate 数据初始化
 *
 * @author 李坤澔
 *     date 2019-06-01
 */
@Component
@Slf4j
public class GetProjectId {
    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectRateEOService dao;

    List<String> search = new ArrayList<>();

    Integer loopTime = 0;

    /**
     * 使用时将@Scheduled 注释取消即可，
     * 存入es方法为 dailyRepository.save(dailyList) 默认也是注释的请放开注释
     */
    // @Scheduled(cron = "0/5 * * ? * *")
    public void startTask() throws Exception {

        /**
         * 防止重复调用
         */
        if (loopTime > 0) {
            return;
        } else {
            loopTime++;
        }
        init();
        Date t = DateUtils.getCurrentYearStartTime();
        int i = 0;
        Iterable<Project> aList = queryBuild(search);
        for (Project a : aList) {
            log.warn("{}", i++);

            ProjectRateEO rateEO = new ProjectRateEO();
            rateEO.setProjectId(a.getId());
            rateEO.setCreateTime(t);
            rateEO.setProcInstId("-1");
            rateEO.setProcessNameId("0");
            dao.insert(rateEO);
        }

        return;

    }

    /**
     * 进行es查询
     *
     * @return
     * @author Lee Kwanho 李坤澔
     *     date 2019-06-01
     **/
    private Iterable<Project> queryBuild(List<String> list) {
        BoolQueryBuilder queryBuilder =
            QueryBuilders
                .boolQuery()
                .mustNot(QueryBuilders.termQuery("delFlag", Boolean.TRUE));

        /*
         * 拼接部门条件
         */
        BoolQueryBuilder qb = QueryBuilders.boolQuery();
        for (String budgetId : list) {
            qb.should(QueryBuilders.termQuery("budgetId", budgetId));
        }
        queryBuilder.must(qb);

        return projectRepository.search(queryBuilder);

    }

    private void init() {
        /*
         *  附加budgetId信息
         */
        search.add("QVZAG692E5");
        search.add("M3WD73MU42");
        search.add("SFR3B5XELE");
        search.add("8RTWZHEBVQ");

    }

}


