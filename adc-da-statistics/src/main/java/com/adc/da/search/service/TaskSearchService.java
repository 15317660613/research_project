package com.adc.da.search.service;

import com.adc.da.budget.entity.Task;
import com.adc.da.budget.query.task.TaskQuery;
import com.adc.da.budget.service.ProjectService;
import com.adc.da.login.util.UserUtils;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Lee Kwanho 李坤澔
 * @author Ding Qiang  丁强
 *     date 2019-09
 */
@Service
@Slf4j
public class TaskSearchService {

    @Autowired
    private ProjectSearchService projectSearchService;

    @Autowired
    private TaskSearchQueryService taskSearchQueryService;

    @Autowired
    private ProjectService projectService;

    /**
     * 项目查询页
     *
     * @author weijinjin
     * @author Lee Kwanho 李坤澔
     *     date 2019-06-18
     */
    public List<Task> findByPage(TaskQuery page) {
        //获取当前用户部门
        List<String> deptIds = projectService.getDeptIds();
        //当前用户的id
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        //如果当前用户是部长可以看见本部门所有任务
        BoolQueryBuilder boolQueryBuilder = taskSearchQueryService.initQueryBuilder(page);
        Pageable pageable = new PageRequest(page.getPage() - 1, page.getPageSize());
        SearchQuery searchQuery = projectSearchService.initSearchQuery(
            deptIds,
            userId,
            pageable,
            boolQueryBuilder);

        return taskSearchQueryService.doSearch(searchQuery, page);
    }
}
