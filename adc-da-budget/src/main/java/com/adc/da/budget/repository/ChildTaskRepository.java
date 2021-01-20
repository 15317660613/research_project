package com.adc.da.budget.repository;

import com.adc.da.budget.entity.ChildrenTask;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface ChildTaskRepository extends ElasticsearchRepository<ChildrenTask, String> {

    List<ChildrenTask> findByTaskIdEquals(String taskId);

    List<ChildrenTask> findByTaskId(String taskId);

    List<ChildrenTask> findByTaskIdIn(Collection<String> taskIds);

    List<ChildrenTask> findByTaskIdInAndDelFlagNot(Collection<String> taskIds, Boolean delFlag);

    List<ChildrenTask> findByIdIn(Collection<String> taskIds);

    List<ChildrenTask> findByIdInAndDelFlagNot(Collection<String> ids, Boolean delFlag);

    int countByTaskIdEqualsAndStatusEqualsAndDelFlagNot(String taskId, String status, Boolean delFlag);

    List<ChildrenTask> findByTaskIdEqualsAndDelFlagNot(String taskId, Boolean delFlag);

    List<ChildrenTask> findByTaskIdAndChildTaskNameAndDelFlagNot(String taskId, String childTaskName, boolean delFlag);

    List<ChildrenTask> findByPersonIdAndDelFlagNot(String userId, boolean delFlag);

    /**
     * add by liqiushi
     * 20190318
     * 根据日报中任务id查询对应项目
     */
    ChildrenTask findById(String id);

//    List<ChildrenTask>  findByMemberIdsIsNullAndDelFlagNot(boolean flag);
    List<ChildrenTask>  findByMemberIdsAndDelFlagNot(String [] ids,boolean flag);

    List<ChildrenTask>  findByMemberIdsInAndDelFlagNot(String id,boolean flag);
    ChildrenTask findByIdAndDelFlagNot(String id, boolean flag);

    List<ChildrenTask> findByTaskIdAndDelFlagNot(String taskId, boolean delFlag);

    List<ChildrenTask> findByTaskIdInAndDelFlagNot(Set<String> taskId, boolean delFlag);

    List<ChildrenTask> findByTaskIdInAndStatusAndDelFlagNot(Set<String> taskId, String status, boolean delFlag);

    List<ChildrenTask> findByTaskIdAndPersonIdAndDelFlagNot(String taskId, String userId, boolean delFlag);

    List<ChildrenTask> findByTaskIdAndMemberIdsInAndDelFlagNot(String taskId, String userId, boolean delFlag);

}
