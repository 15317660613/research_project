package com.adc.da.leaderview.service.operate;

import com.adc.da.budget.entity.Task;
import com.adc.da.budget.query.task.TaskQuery;
import com.adc.da.search.service.TaskSearchQueryService;
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

import static com.adc.da.budget.constant.ProjectSearchField.PROJECT_TYPE;

/**
 * 用于领导视角
 *
 * @author Lee Kwanho 李坤澔
 *     date 2019-10-18
 * @return
 **/
@Service
@Slf4j
public class FindTaskByPageService {

    @Autowired
    private TaskSearchQueryService taskSearchQueryService;

    /**
     * 任务查询页
     */
    public List<Task> findByPage(TaskQuery page, String userId) {
        BoolQueryBuilder boolQueryBuilder = taskSearchQueryService.initQueryBuilder(page);
        SearchQuery searchQuery = initSearchQuery(page, userId, boolQueryBuilder);
        log.debug(boolQueryBuilder.toString());

        /*
         *  进行查询并且返回
         */
        return taskSearchQueryService.doSearch(searchQuery, page);
    }

    /**
     * 组装分页参数等
     *
     * @param page
     * @param userId
     * @param boolQueryBuilder
     * @return
     */
    private SearchQuery initSearchQuery(TaskQuery page, String userId, BoolQueryBuilder boolQueryBuilder) {
        // 分页参数
        Pageable pageable = new PageRequest(page.getPage() - 1, page.getPageSize());
        boolQueryBuilder.must(QueryBuilders.termQuery("memberIds", userId));
        // 分数、分页
        if (null != page.getProjectType()) {
            boolQueryBuilder.must(QueryBuilders.termQuery(PROJECT_TYPE, page.getProjectType()));
        }
        return new NativeSearchQueryBuilder().withPageable(pageable).withQuery(boolQueryBuilder)
                                             .build();
    }

}
