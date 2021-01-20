package com.adc.da.budget.repository;

import com.adc.da.budget.entity.TaskPriority;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * @description:
 * @author: qichunxu
 * @create: 2019-03-18 10:51
 **/
public interface TaskPriorityRepository extends ElasticsearchRepository<TaskPriority,String> {

    /**
     * 根据任务成员查询
     * @param userId
     * @return
     */
    List<TaskPriority> findByMemberIdsIn(String userId);
}
