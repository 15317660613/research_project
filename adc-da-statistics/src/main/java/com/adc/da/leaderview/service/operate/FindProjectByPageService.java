package com.adc.da.leaderview.service.operate;

import com.adc.da.budget.entity.Project;
import com.adc.da.budget.query.project.ProjectQuery;
import com.adc.da.search.service.ProjectSearchQueryService;
import com.adc.da.util.exception.AdcDaBaseException;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.adc.da.budget.constant.ProjectSearchField.FINISHED_STATUS;
import static com.adc.da.budget.constant.ProjectSearchField.PROJECT_LEADER_ID;
import static com.adc.da.budget.constant.ProjectSearchField.PROJECT_TYPE;

/**
 * describe:
 * 用于领导视角
 *
 * @author 李坤澔
 *     date 2019-10-12
 */
@Service
@Slf4j
public class FindProjectByPageService {

    @Autowired
    private ProjectSearchQueryService projectSearchOrderedService;

    static final int USER_JOIN = 0;

    static final int USER_MANAGER = 1;

    /**
     * 项目查询页
     *
     * @author 丁强
     * @author Lee Kwanho 李坤澔
     *     date 2019-09-24
     */
    public List<Project> findByPage(ProjectQuery page, List<String> userIdList, int type) {
        /*
         * newFindUserManagerByPage
         * newFindUserJoinByPage
         * 组装基本查询条件
         */
        BoolQueryBuilder boolQueryBuilder = projectSearchOrderedService.initQueryBuilder(page);
        SearchQuery searchQuery;
        switch (type) {
            case USER_JOIN:
                searchQuery = joinByPage(page, userIdList, boolQueryBuilder);
                break;
            case USER_MANAGER:
                searchQuery = managerByPage(page, userIdList, boolQueryBuilder);
                break;
            default:
                log.warn("type error: {}", type);
                throw new AdcDaBaseException("接口类型错误");
        }
        log.debug(boolQueryBuilder.toString());
        /*
         *  进行查询并且返回
         */
        return projectSearchOrderedService.doSearch(searchQuery, page);
    }

    /**
     * 负责项目搜索
     *
     * @param page
     * @param userIdList
     * @param boolQueryBuilder
     * @return
     */
    private SearchQuery managerByPage(ProjectQuery page, List<String> userIdList, BoolQueryBuilder boolQueryBuilder) {
        Pageable pageable = new PageRequest(page.getPage() - 1, page.getPageSize());
        if (null != page.getFinishedStatus()) {
            boolQueryBuilder.must(QueryBuilders.termQuery(FINISHED_STATUS, page.getFinishedStatus()));
        } else {
            //只显示自己参与和创建的项目
            //只显示自己负责项目
            boolQueryBuilder.must(QueryBuilders.termsQuery(PROJECT_LEADER_ID, userIdList));
        }

        if (null != page.getProjectType()) {
            boolQueryBuilder.must(QueryBuilders.termQuery(PROJECT_TYPE, page.getProjectType()));
        }
        // 分数、分页
        return new NativeSearchQueryBuilder().withPageable(pageable).withQuery(boolQueryBuilder)
                                             .build();

    }

    /**
     * @param page
     * @param userIdList
     * @param boolQueryBuilder
     * @return
     */
    private SearchQuery joinByPage(ProjectQuery page, List<String> userIdList, BoolQueryBuilder boolQueryBuilder) {
        // 分页参数
        Pageable pageable = new PageRequest(page.getPage() - 1, page.getPageSize());
        if (null != page.getFinishedStatus()) {
            boolQueryBuilder.must(QueryBuilders.termQuery(FINISHED_STATUS, page.getFinishedStatus()));
        } else {
            //只显示自己参与和创建的项目
            BoolQueryBuilder bq;
            bq = QueryBuilders.boolQuery().should(QueryBuilders.termsQuery(PROJECT_LEADER_ID, userIdList))
                              .should(QueryBuilders.termsQuery("projectMemberIds", userIdList));

            boolQueryBuilder.must(bq);
        }

        if (null != page.getProjectType()) {
            boolQueryBuilder.must(QueryBuilders.termQuery(PROJECT_TYPE, page.getProjectType()));
        }

        // 分数、分页
        return new NativeSearchQueryBuilder().withPageable(pageable).withQuery(boolQueryBuilder)
                                             .build();

    }

}
