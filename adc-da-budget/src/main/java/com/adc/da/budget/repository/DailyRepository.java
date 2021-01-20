package com.adc.da.budget.repository;

import com.adc.da.budget.entity.Daily;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;
import java.util.Set;

public interface DailyRepository extends ElasticsearchRepository<Daily, String> {

    /**
     * 统计用户的日报数
     *
     * @return
     */
    int countByCreateUserIdAndDelFlagNot(String userId, Boolean delFlag);

    Daily findFirstByIdAndDelFlagNot(String id, Boolean delFlag);

    List<Daily> findAllByDelFlagNot(Boolean delFlag);
    List<Daily> findAllByIdIn(List<String> idList);

    List<Daily> findByTaskIdArrayInAndDelFlagNot(String taskId, Boolean delFlag);

    List<Daily> findByTaskIdArrayInAndDelFlagNot(Set<String> taskIds, Boolean delFlag);

    List<Daily> findByBudgetIdInAndDelFlagNot(Set<String> budgetIdSet, Boolean delFlag);

    List<Daily> findAllByCreateUserIdInAndDelFlagNot(List<String> createUserId, Boolean delFlag);
    List<Daily> findByDailyParentIdAndAndApproveState(String parentId , Integer approveState);
    List<Daily> findByDailyParentIdAndAndApproveStateNotContaining(String parentId , Integer approveState);
    List<Daily> deleteByDailyParentId(String parentId);

    List<Daily> deleteByDailyParentIdAndAndApproveState(String parentId,Integer approveState);
}
