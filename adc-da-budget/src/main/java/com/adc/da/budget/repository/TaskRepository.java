package com.adc.da.budget.repository;

import com.adc.da.budget.entity.Task;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface TaskRepository extends ElasticsearchRepository<Task, String> {
    List<Task> findByProjectId(String projectId);

    List<Task> findByProjectIdAndDelFlagNot(String projectId, boolean flag);

    List<Task> findByProjectIdAndTaskStatusAndDelFlagNot(String projectId, String status, boolean flag);

    /**
     * 根据任务名查询任务
     *
     * @param name
     * @return
     */
    List<Task> findByNameEquals(String name);

    /**
     * 根据项目ID统计所属任务个数
     *
     * @param projectId
     * @return
     */
    long countByProjectIdEquals(String projectId);

    /**
     * 根基项目ID查询任务
     *
     * @param projectId
     * @return
     */
    List<Task> findByProjectIdEquals(String projectId);

    /**
     * 根据业务ID列表查询任务
     *
     * @param budgetIds
     * @return
     */
    List<Task> findByBudgetIdIn(Collection<String> budgetIds);

    List<Task> findByProjectIdIn(Collection<String> budgetIds);

    /**
     * 根据 项目ID数组去重
     *
     * @param projectIds
     * @return
     */
    List<Task> findDistinctByProjectIdIn(Collection<String> projectIds);

    /**
     * 根据项目ID和人员查询
     *
     * @param projectId
     * @param id
     * @return
     */
    int countByProjectIdEqualsAndMemberIdsIn(String projectId, String id);

    List<Task> findByProjectIdInAndDelFlagNot(List<String> projectIds, Boolean delFlag);

    List<Task> findByProjectIdInAndDelFlagNot(Set<String> projectIds, Boolean delFlag);

    /**
     * add by liqiushi
     * 20190318
     * 根据任务查项目
     */
    Task findByIdAndDelFlagNot(String taskId, Boolean delFlag);

    Task findById(String taskId);
    Task findByNameAndDelFlagNot(String name,Boolean delFlag);

    List<Task> findByIdIn(List<String> taskIds);

    List<Task> findByIdInAndDelFlagNot(List<String> taskIds,Boolean delFlag);

    List<Task> findByIdIn(Set<String> taskIds);

    /**
     * 根据项目ID查询所属任务ID列表
     *
     * @param projectId
     * @param delFlag
     * @return
     */
    List<Task> findByProjectIdAndDelFlagNot(String projectId, Boolean delFlag);

    /**
     * 根据业务ID查询所属任务ID列表
     *
     * @param budgetId
     * @param delFlag
     * @return
     */
    List<Task> findByBudgetIdAndDelFlagNot(String budgetId, boolean delFlag);

    List<Task> findByBudgetId(String budgetId);

    /**
     * 根据ID和业务ID查询任务
     *
     * @param id
     * @param budgetId
     * @param delFlag
     * @return
     */
    Task findByIdAndBudgetIdAndDelFlagNot(String id, String budgetId, boolean delFlag);

    Task findByIdAndBudgetId(String id, String budgetId);

    /**
     * 根据业务ID和参与人员查询任务
     *
     * @param budgetId
     * @param userId
     * @param delFlag
     * @return
     */
    List<Task> findByBudgetIdAndMemberIdsInAndDelFlagNot(String budgetId, String userId, Boolean delFlag);

    /**
     * 根据项目ID和参与人员ID查询任务
     *
     * @param projectId
     * @param userId
     * @param delFlag
     * @return
     */
    List<Task> findByProjectIdAndMemberIdsInAndDelFlagNot(String projectId, String userId, Boolean delFlag);


    List<Task> findByMemberIdsInAndDelFlagNot( String userId, Boolean delFlag);

    List<Task> findByMemberIdsInAndDelFlagNot( List<String> userId, Boolean delFlag);

    List<Task> findByCreateUserIdAndDelFlagNot( String userId, Boolean delFlag);


    /**
     * 根据人员ID查询
     *
     * @param id
     * @param status
     * @param delFlag
     * @return
     */
    List<Task> findByMemberIdsInAndTaskStatusAndDelFlagNot(String id, String status, Boolean delFlag);

}
