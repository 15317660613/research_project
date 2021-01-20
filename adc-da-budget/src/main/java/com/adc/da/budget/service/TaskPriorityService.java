package com.adc.da.budget.service;

import com.adc.da.budget.enums.ProjectStatusEnums;
import com.adc.da.budget.repository.TaskPriorityRepository;
import com.adc.da.budget.entity.ChildrenTask;
import com.adc.da.budget.entity.Task;
import com.adc.da.budget.entity.TaskPriority;
import com.adc.da.budget.repository.ChildTaskRepository;
import com.adc.da.budget.repository.TaskRepository;
import com.adc.da.login.util.UserUtils;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.StringUtils;
import com.google.common.collect.Lists;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: qichunxu
 * @create: 2019-03-18 10:49
 **/
@Service
public class TaskPriorityService {

    @Autowired
    private TaskPriorityRepository taskPriorityRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private ChildTaskRepository childTaskRepository;

    /**
    * @Description: 获取当前用户的任务优先级列表
    * @Param:
    * @return:
    * @Author: qichunxu
    * @Date: 2019/3/18
    */
    public List<Map<String,List<TaskPriority>>> findAll() {
        //获取登录用户ID
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        List<Map<String,List<TaskPriority>>> resultList = new ArrayList<>();
        Map<String,List<TaskPriority>> map = new HashMap<>();
        List<TaskPriority> list = new ArrayList<>();
        List<TaskPriority> taskPriorities = taskPriorityRepository.findByMemberIdsIn(userId);
        //如果没存去Task和ChildrenTask获取
        BoolQueryBuilder taskBq = QueryBuilders.boolQuery()
                    .must(QueryBuilders.termQuery("memberIds", userId))
                    .must(QueryBuilders.termQuery("taskStatus", ProjectStatusEnums.EXECUTE.getStatus()))
                    .mustNot(QueryBuilders.termQuery("delFlag", Boolean.TRUE));
        ArrayList<Task> tasks = Lists.newArrayList(taskRepository.search(taskBq));
            //遍历任务集合
        if (CollectionUtils.isNotEmpty(taskPriorities) && CollectionUtils.isNotEmpty(tasks)) {
            if (taskPriorities.size() < tasks.size()) {
                /*if (CollectionUtils.isNotEmpty(tasks)) {
                    for (Task task : tasks) {
                        TaskPriority taskPriority = new TaskPriority
                                (task.getId(), task.getName(), 0, task.getMemberIds());
                        list.add(taskPriority);
                    }
                    BoolQueryBuilder childTaskBq = QueryBuilders.boolQuery()
                            .must(QueryBuilders.termQuery("personId", userId))
                            .must(QueryBuilders.termQuery("status", ProjectStatusEnums.COMPLETE.getStatus()))
                            .mustNot(QueryBuilders.termQuery("delFlag", Boolean.TRUE));
                    ArrayList<ChildrenTask> childTasks = Lists.newArrayList(childTaskRepository.search(childTaskBq));
                    //遍历子任务集合
                    for (ChildrenTask childrenTask : childTasks) {
                        String[] ids = {childrenTask.getPersonId()};
                        TaskPriority taskPriority = new TaskPriority
                                (childrenTask.getId(), childrenTask.getChildTaskName(), 0, ids);
                        list.add(taskPriority);
                    }
                    map.put("0", list);
                    resultList.add(map);
                    taskPriorityRepository.save(list);
                    return resultList;
                }*/
                return getResultListNew(tasks,list,userId);

            } else {
                //取到任务优先级集合
                for (TaskPriority taskPriority : taskPriorities) {
                    if (!map.containsKey(String.valueOf(taskPriority.getPriority()))) {
                        map.put(String.valueOf(taskPriority.getPriority()), new ArrayList<TaskPriority>());
                    }
                    map.get(String.valueOf(taskPriority.getPriority())).add(taskPriority);
                }
                resultList.add(map);
            }
        }
        return resultList;
    }

    private List<Map<String,List<TaskPriority>>> getResultList(ArrayList<Task> tasks,List<TaskPriority> list,String userId){
        List<Map<String,List<TaskPriority>>> resultList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(tasks)) {
            Map<String,List<TaskPriority>> map = new HashMap<>();
            for (Task task : tasks) {
                TaskPriority taskPriority = new TaskPriority
                        (task.getId(), task.getName(), 0, task.getMemberIds());
                list.add(taskPriority);
            }
            BoolQueryBuilder childTaskBq = QueryBuilders.boolQuery()
                    .must(QueryBuilders.termQuery("personId", userId))
                    .must(QueryBuilders.termQuery("status", ProjectStatusEnums.COMPLETE.getStatus()))
                    .mustNot(QueryBuilders.termQuery("delFlag", Boolean.TRUE));
            ArrayList<ChildrenTask> childTasks = Lists.newArrayList(childTaskRepository.search(childTaskBq));
            //遍历子任务集合
            for (ChildrenTask childrenTask : childTasks) {
                String[] ids = {childrenTask.getPersonId()};
                TaskPriority taskPriority = new TaskPriority
                        (childrenTask.getId(), childrenTask.getChildTaskName(), 0, ids);
                list.add(taskPriority);
            }
            map.put("0", list);
            resultList.add(map);
            taskPriorityRepository.save(list);
        }
        return resultList;
    }

    private List<Map<String,List<TaskPriority>>> getResultListNew(ArrayList<Task> tasks,List<TaskPriority> list,String userId){
        List<Map<String,List<TaskPriority>>> resultList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(tasks)) {
            Map<String,List<TaskPriority>> map = new HashMap<>();
            for (Task task : tasks) {
                TaskPriority taskPriority = new TaskPriority
                        (task.getId(), task.getName(), 0, task.getMemberIds());
                list.add(taskPriority);
            }
            BoolQueryBuilder childTaskBq = QueryBuilders.boolQuery()
                    .must(QueryBuilders.termsQuery("memberIds", userId))
                    .must(QueryBuilders.termQuery("status", ProjectStatusEnums.COMPLETE.getStatus()))
                    .mustNot(QueryBuilders.termQuery("delFlag", Boolean.TRUE));
            ArrayList<ChildrenTask> childTasks = Lists.newArrayList(childTaskRepository.search(childTaskBq));
            //遍历子任务集合
            String[] ids = {""};
            for (ChildrenTask childrenTask : childTasks) {
                if (CollectionUtils.isEmpty(childrenTask.getMemberIds())){
                    ids = new String[]{childrenTask.getPersonId()};
                }else {
                  ids = childrenTask.getMemberIds();
                }
                TaskPriority taskPriority = new TaskPriority
                        (childrenTask.getId(), childrenTask.getChildTaskName(), 0, ids);
                list.add(taskPriority);
            }
            map.put("0", list);
            resultList.add(map);
            taskPriorityRepository.save(list);
        }
        return resultList;
    }


    public TaskPriority update(TaskPriority taskPriority) {
        TaskPriority priority = taskPriorityRepository.findOne(taskPriority.getId());
        taskPriority.setMemberIds(priority.getMemberIds());
        return taskPriorityRepository.save(taskPriority);
    }
    public String deleteAll() {
        taskPriorityRepository.deleteAll();
        return "删除成功";
    }
}
