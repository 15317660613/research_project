package com.adc.da.budget.service;

import com.adc.da.base.service.BaseService;
import com.adc.da.budget.dao.UserProjectCustomDao;
import com.adc.da.budget.entity.ChildrenTask;
import com.adc.da.budget.entity.Project;
import com.adc.da.budget.entity.Task;
import com.adc.da.budget.entity.UserProjectCustom;
import com.adc.da.budget.entity.UserWithProjects;
import com.adc.da.budget.repository.ChildTaskRepository;
import com.adc.da.budget.repository.ProjectRepository;
import com.adc.da.budget.repository.TaskRepository;
import com.adc.da.budget.repository.UserWithProjectsRepository;
import com.adc.da.budget.utils.CommonUtil;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.StringUtils;
import com.adc.da.util.utils.UUID;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service("userProjectCustomService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class UserProjectCustomService extends BaseService<UserProjectCustom, String> {
    private static final Logger logger = LoggerFactory.getLogger(UserProjectCustomService.class);

    @Autowired
    private UserProjectCustomDao dao;

    @Autowired
    private UserWithProjectsRepository userWithProjectsRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ChildTaskRepository childTaskRepository;


    public UserProjectCustomDao getDao() {
        return dao;
    }

    /* 将某人设置为业务管理员时调用
    * 传参 budgetId  userId   然后判断是否处于隐藏状态
    * 若处于隐藏状态 则重新将与该业务相关的项目 任务  下级任务 更新至隐藏表中
    */
    public void upadteUserProjectCustomEOByBudgetId(String userId, String budgetId) throws Exception {
        UserProjectCustom userProjectCustom = dao.findUserProjectCustomByBussinessId(budgetId,"1",userId);
        //userProjectCustom不为空且 状态为0  属于隐藏
        if(null!=userProjectCustom && StringUtils.equals(userProjectCustom.getStatus(),"0")){
            UserWithProjects userWithProjects = userWithProjectsRepository.findOne(userId);
            Set<String> projectIds = new HashSet<>();
            Set<String> taskIds = new HashSet<>();
            Set<String> childTaskIds = new HashSet<>();
            projectIds.addAll(userWithProjects.getProjectIds());
            taskIds.addAll(userWithProjects.getTaskIds());
            childTaskIds.addAll(userWithProjects.getChildTaskIds());

            projectIds.removeAll(Collections.singleton(null));
            taskIds.removeAll(Collections.singleton(null));
            childTaskIds.removeAll(Collections.singleton(null));
            hideProject(projectIds, budgetId, userId, taskIds, childTaskIds);
        }
    }

    public void upadteUserProjectCustomEOByProjectId(String userId, String projectId,String budgetId) throws Exception {
        UserProjectCustom userProjectCustom = dao.findUserProjectCustomByProjectId(projectId,"2",userId);
        //userProjectCustom不为空且 状态为0  属于隐藏
        if(null!=userProjectCustom && StringUtils.equals(userProjectCustom.getStatus(),"0")){
            UserWithProjects userWithProjects = userWithProjectsRepository.findOne(userId);
            Set<String> projectIds = new HashSet<>();
            Set<String> taskIds = new HashSet<>();
            Set<String> childTaskIds = new HashSet<>();
            taskIds.addAll(userWithProjects.getTaskIds());
            childTaskIds.addAll(userWithProjects.getChildTaskIds());
            projectIds.add(projectId);
            taskIds.removeAll(Collections.singleton(null));
            childTaskIds.removeAll(Collections.singleton(null));
            hideTask(taskIds, projectIds, budgetId, userId, childTaskIds);
        }
    }

    public void hideProject(Set projectIds,String bussinessId,String userId,Set taskIds,Set childTaskIds) throws Exception {
        Set<String> projectIdSet = new HashSet<>();
        if (CollectionUtils.isNotEmpty(projectIds)) {
            List<List<String>> resultList = CommonUtil.splitList(new ArrayList<>(projectIds), 500);
            List<Project> projectList = new ArrayList<>();
            for (List<String> list : resultList) {
                /*
                 * 过滤已删除的项目
                 */
                projectList
                    .addAll(Lists.newArrayList(projectRepository.findByIdInAndDelFlagNot(list, Boolean.TRUE)));
            }
            for (Project project : projectList) {
                // project有budget才能添加
                if (StringUtils.equals(bussinessId,project.getBudgetId())) {
                    String projectId = project.getId();
                    UserProjectCustom tempUserProjectCustom = dao
                        .findUserProjectCustomByProjectId(projectId, "2", userId);
                    UserProjectCustom userProjectCustom = new UserProjectCustom();
                    userProjectCustom.setBusinessid(bussinessId);
                    userProjectCustom.setProjectid(projectId);
                    userProjectCustom.setTaskid("");
                    userProjectCustom.setChildtaskid("");
                    userProjectCustom.setType("2");
                    userProjectCustom.setUserid(userId);
                    userProjectCustom.setStatus("0");
                    if (StringUtils.isNotEmpty(tempUserProjectCustom)) {
                        userProjectCustom.setId(tempUserProjectCustom.getId());
                        this.updateUserProjectCustom(userProjectCustom);
                    } else {
                        //新增加  隐藏操作
                        this.insertUserProjectCustom(userProjectCustom);
                    }
                    projectIdSet.add(project.getId());
                }
            }
        }
        hideTask(taskIds, projectIdSet, bussinessId, userId, childTaskIds);
    }

    public void hideTask(Set taskIds,Set projectIdSet,String bussinessId,String userId,Set childTaskIds) throws Exception {
        Set<String> taskIdSet = new HashSet<>();
        if (CollectionUtils.isNotEmpty(taskIds)) {
            List<List<String>> resultList = CommonUtil.splitList(new ArrayList<>(taskIds), 500);
            List<Task> taskList = new ArrayList<>();
            for (List<String> list : resultList) {
                taskList.addAll(Lists.newArrayList(taskRepository.findByIdInAndDelFlagNot(list, Boolean.TRUE)));
            }
            for (Task task : taskList) {
                //项目任务
                if (StringUtils.isEmpty(task.getBudgetId()) && projectIdSet.contains(task.getProjectId())) {
                    String taskId = task.getId();
                    UserProjectCustom tempUserProjectCustom = dao.findUserProjectCustomByTaskId(taskId, "3", userId);
                    UserProjectCustom userProjectCustom = new UserProjectCustom();
                    userProjectCustom.setBusinessid(bussinessId);
                    userProjectCustom.setProjectid(task.getProjectId());
                    userProjectCustom.setTaskid(taskId);
                    userProjectCustom.setChildtaskid("");
                    userProjectCustom.setType("3");//任务
                    userProjectCustom.setUserid(userId);
                    userProjectCustom.setStatus("0");
                    if (StringUtils.isNotEmpty(tempUserProjectCustom)) {
                        userProjectCustom.setId(tempUserProjectCustom.getId());
                        this.updateUserProjectCustom(userProjectCustom);
                    } else {
                        //新增加  隐藏操作
                        this.insertUserProjectCustom(userProjectCustom);
                    }
                    taskIdSet.add(taskId);
                }
                //业务任务
                if (StringUtils.isNotEmpty(task.getBudgetId()) && bussinessId.equals(task.getBudgetId())) {
                    String taskId = task.getId();
                    UserProjectCustom tempUserProjectCustom = dao.findUserProjectCustomByTaskId(taskId, "3", userId);
                    UserProjectCustom userProjectCustom = new UserProjectCustom();
                    userProjectCustom.setBusinessid(bussinessId);
                    userProjectCustom.setProjectid("");
                    userProjectCustom.setTaskid(taskId);
                    userProjectCustom.setChildtaskid("");
                    userProjectCustom.setType("3");//任务
                    userProjectCustom.setUserid(userId);
                    userProjectCustom.setStatus("0");
                    if (StringUtils.isNotEmpty(tempUserProjectCustom)) {
                        userProjectCustom.setId(tempUserProjectCustom.getId());
                        this.updateUserProjectCustom(userProjectCustom);
                    } else {
                        //新增加  隐藏操作
                        this.insertUserProjectCustom(userProjectCustom);
                    }
                    taskIdSet.add(taskId);
                }
            }
        }
        hideChildTask(childTaskIds,taskIdSet,bussinessId,userId);
    }
    //隐藏下级任务
    public void hideChildTask(Set childTaskIds,Set taskIdSet,String bussinessId,String userId) throws Exception {
        if (CollectionUtils.isNotEmpty(childTaskIds)) {
            List<List<String>> resultList = CommonUtil.splitList(new ArrayList<>(childTaskIds), 500);
            List<ChildrenTask> taskList = new ArrayList<>();
            for (List<String> list : resultList) {
                taskList.addAll(Lists
                    .newArrayList(childTaskRepository.findByIdInAndDelFlagNot(list, Boolean.TRUE)));
            }
            for (ChildrenTask childrenTask : taskList) {
                if (taskIdSet.contains(childrenTask.getTaskId())) {
                    String childTaskId = childrenTask.getId();
                    UserProjectCustom tempUserProjectCustom = dao.findUserProjectCustomByChildtaskId(childTaskId, "4", userId);
                    String taskId = childrenTask.getTaskId();
                    String projectId = "";
                    if (StringUtils.isNotEmpty(taskId)) {
                        Task task = taskRepository.findById(taskId);
                        projectId = task.getProjectId();
                    }
                    UserProjectCustom userProjectCustom = new UserProjectCustom();
                    userProjectCustom.setBusinessid(bussinessId);
                    userProjectCustom.setProjectid(projectId);
                    userProjectCustom.setTaskid(childrenTask.getTaskId());
                    userProjectCustom.setChildtaskid(childTaskId);
                    userProjectCustom.setType("4");//子任务
                    userProjectCustom.setUserid(userId);
                    userProjectCustom.setStatus("0");
                    if (StringUtils.isNotEmpty(tempUserProjectCustom)) {
                        userProjectCustom.setId(tempUserProjectCustom.getId());
                        this.updateUserProjectCustom(userProjectCustom);
                    } else {
                        //新增加  隐藏操作
                        this.insertUserProjectCustom(userProjectCustom);
                    }
                }
            }
        }
    }

    public void insertUserProjectCustom(UserProjectCustom userProjectCustom) throws Exception {
        userProjectCustom.setId(UUID.randomUUID10());
        int result = dao.insert(userProjectCustom);
        if (result <= 0) {
            throw new AdcDaBaseException("隐藏失败");
        }
    }

    public void updateUserProjectCustom(UserProjectCustom userProjectCustom) throws Exception {
        int num = dao.update(userProjectCustom);
        if (num > 0) {} else {
            throw new AdcDaBaseException("更新失败");
        }
    }
}
