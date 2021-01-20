package com.adc.da.budget.schedule;

import com.adc.da.budget.annotation.MatchField;
import com.adc.da.budget.dao.BudgetEODao;
import com.adc.da.budget.entity.BudgetEO;
import com.adc.da.budget.entity.ChildrenTask;
import com.adc.da.budget.entity.Project;
import com.adc.da.budget.entity.Task;
import com.adc.da.budget.repository.ChildTaskRepository;
import com.adc.da.budget.repository.ProjectRepository;
import com.adc.da.budget.repository.TaskRepository;
import com.adc.da.budget.utils.CommonUtil;
import com.adc.da.sys.dao.OrgEODao;
import com.adc.da.sys.dao.UserEODao;
import com.adc.da.sys.entity.OrgEO;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @ClassName ScheduledService
 * @Description: TODO
 * @Author 丁强
 * @Date 2020/4/24
 * @Version V1.0
 **/
@Component
public class BudgetScheduleService {
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ChildTaskRepository childTaskRepository;
    @Autowired
    private UserEODao userEODao;
    @Autowired
    private OrgEODao orgEODao;

    @Autowired
    private BudgetEODao budgetEODao;


   @Scheduled(cron = "0 0 2 * * ?")
//    @Scheduled(cron = "0 0/5 * * * ?")
    public void doTask() throws Exception {
        List<UserEO> userEOList = userEODao.getUserEOListByDelFlag("1");
        List<OrgEO> orgEOList = orgEODao.queryOrgAll();

        //开始遍历离职用户
        for (UserEO userEO : userEOList){
            List<Project> projectList = projectRepository.findByProjectMemberIdsInAndDelFlagNot(userEO.getUsid(),true);
            //开始处理子任务
            List<ChildrenTask> childrenTaskList = childTaskRepository.findByMemberIdsInAndDelFlagNot(userEO.getUsid(),true);
            for (ChildrenTask childrenTask : childrenTaskList){
                if (StringUtils.equals(userEO.getUsid(),childrenTask.getApproveUserId())){
                    continue;
                }
//                private String memberNameString;
//                private String[] memberNames;
//                //参与人员ID列表
//                private String[] memberIds;
//                private List<Map<String, String>> mapsList;
//                private List<Map<String,String>> userIdDeptNameMapList;
                Set<String> childrenTaskMemberIdSet= new HashSet<>();
                if (CollectionUtils.isNotEmpty(childrenTask.getMemberIds())){
                    childrenTaskMemberIdSet.addAll(Arrays.asList(childrenTask.getMemberIds()));
                }
                childrenTaskMemberIdSet.remove(userEO.getUsid());
                if (CollectionUtils.isEmpty(childrenTaskMemberIdSet)){
                    childrenTask.setMapsList(null);
                    childrenTask.setUserIdDeptNameMapList(null);
                    childrenTask.setMemberNames(null);
                    childrenTask.setMemberNameString(null);
                    childrenTask.setMemberIds(null);
                    continue;
                }
                List<UserEO> userEOS = userEODao.getUserWithRolesByUserIdsNotDeleted(new ArrayList<>(childrenTaskMemberIdSet));
                childrenTask.setMapsList(CommonUtil.userIdUsnameMapKv(userEOS));
                childrenTask.setUserIdDeptNameMapList( CommonUtil.userIdDeptNamesMapKv(userEOS,orgEOList));
                String[] childrenTaskMemberNames = CommonUtil.getUserNameArr(userEOS);
                childrenTask.setMemberNames(childrenTaskMemberNames);
                childrenTask.setMemberNameString(StringUtils.join(childrenTaskMemberNames,","));
                childrenTask.setMemberIds(CommonUtil.getUserIdArr(userEOS));
            }
            if (CollectionUtils.isNotEmpty(childrenTaskList)) {
                childTaskRepository.save(childrenTaskList);
            }//结束处理子任务


            //定义Map保存已离职但是为项目管理员的人员Id、项目Id Map
            Map<String,List<UserEO>> projectIdAndTaskApproveUserIdList=new HashMap<>();

            //开始处理任务
            List<Task> taskList = taskRepository.findByMemberIdsInAndDelFlagNot(userEO.getUsid(),true);
            for (Task task : taskList){
                if (StringUtils.equals(userEO.getUsid(),task.getApproveUserId())){
                    if(StringUtils.isNotEmpty(task.getProjectId())){
                        List<UserEO> approveUserEOList = projectIdAndTaskApproveUserIdList.get(task.getProjectId());
                        if (null == approveUserEOList){
                            approveUserEOList = new ArrayList<>();
                        }
                        approveUserEOList.add(userEO);
                        projectIdAndTaskApproveUserIdList.put(task.getProjectId(),approveUserEOList);
                    }
                    continue;
                }
                if (StringUtils.equals(userEO.getUsid(),task.getProjectAdminId())){
                    task.setProjectAdminId(null);
                    task.setProjectAdminName(null);
                }
//                private String memberNameString;
//                private String[] memberNames;
//                //参与人员ID列表
//                private String[] memberIds;
//                private List<Map<String, String>> mapsList;
//                private List<Map<String,String>> userIdDeptNameMapList;

                Set<String> taskMemberIdSet= new HashSet<>();
               if (CollectionUtils.isNotEmpty(task.getMemberIds())){
                   taskMemberIdSet.addAll(Arrays.asList(task.getMemberIds()));
               }
                taskMemberIdSet.remove(userEO.getUsid());
                if (CollectionUtils.isEmpty(taskMemberIdSet)){
                    task.setMapsList(null);
                    task.setUserIdDeptNameMapList(null);
                    task.setMemberNames(null);
                    task.setMemberNameString(null);
                    task.setMemberIds(null);
                    continue;
                }
                List<UserEO> userEOS = userEODao.getUserWithRolesByUserIdsNotDeleted(new ArrayList<>(taskMemberIdSet));
                task.setMapsList(CommonUtil.userIdUsnameMapKv(userEOS));
                task.setUserIdDeptNameMapList( CommonUtil.userIdDeptNamesMapKv(userEOS,orgEOList));
                String[] memberNames = CommonUtil.getUserNameArr(userEOS);
                task.setMemberNames(memberNames);
                task.setMemberNameString(StringUtils.join(memberNames,","));
                task.setMemberIds(CommonUtil.getUserIdArr(userEOS));
            }
            if (CollectionUtils.isNotEmpty(taskList)) {
                taskRepository.save(taskList);
            } //结束处理任务



            //开始处理项目
            for (Project project : projectList){
                if (StringUtils.equals(userEO.getUsid(),project.getProjectLeaderId())
//                    ||StringUtils.equals(userEO.getUsid(),project.getProjectAdminId()) //新需求
                ){
                    continue;
                }
                if (StringUtils.equals(userEO.getUsid(),project.getProjectAdminId())){
                        project.setProjectAdminId(null);
                        project.setProjectAdminName(null);
                }

                if(StringUtils.equals(userEO.getUsid(),project.getBusinessAdminId())){
                    BudgetEO budgetEO = budgetEODao.selectByPrimaryKey(project.getBudgetId());
                    budgetEO.setBusinessAdminId("");
                    budgetEO.setBusinessAdminName("");
                    budgetEODao.updateByPrimaryKeySelective(budgetEO);
                }
                Set<String> projectMemberIdSet = new HashSet<>();
                if (CollectionUtils.isNotEmpty(project.getProjectMemberIds())){
                    projectMemberIdSet.addAll(Arrays.asList(project.getProjectMemberIds()));
                }
                Map<String,List<String>> deptIdUserIdList=new HashMap<>();
                for(String deptId: deptIdUserIdList.keySet()){
                    List<String> userIdList=deptIdUserIdList.get(deptId);
                    if(CollectionUtils.isNotEmpty(userIdList)){
                        userIdList.remove(userEO.getUsid());
                    }
                }
                // 不管怎样 先清除一下
                projectMemberIdSet.remove(userEO.getUsid());
                if (CollectionUtils.isEmpty(projectMemberIdSet)){
                    project.setMapList(null);
                    project.setUserIdDeptNameMapList(null);
                    project.setProjectMemberNames(null);
                    project.setMemberNames(null);
                    project.setProjectMemberIds(null);
                    continue;
                }
                List<UserEO> userEOS = userEODao.getUserWithRolesByUserIdsNotDeleted(new ArrayList<>(projectMemberIdSet));
                // 只要部位空 说明上面一定有某个任务的负责人时list里面的  所以都要填进去
                if (projectIdAndTaskApproveUserIdList.keySet().contains(project.getId())){
                    List<UserEO> approveUserEOList = projectIdAndTaskApproveUserIdList.get(project.getId());
                    if (CollectionUtils.isNotEmpty(approveUserEOList)){
                        userEOS.addAll(approveUserEOList);
                    }
                }
                project.setMapList( CommonUtil.userIdUsnameMapKv(userEOS));
                project.setUserIdDeptNameMapList( CommonUtil.userIdDeptNamesMapKv(userEOS,orgEOList));
                String [] userNameArr = CommonUtil.getUserNameArr(userEOS);
                project.setMemberNames(userNameArr);
                project.setProjectMemberNames(StringUtils.join(userNameArr,","));
                project.setProjectMemberIds( CommonUtil.getUserIdArr(userEOS));
            }
            if (CollectionUtils.isNotEmpty(projectList)) {
                projectRepository.save(projectList);
            }//结束处理项目

        List<BudgetEO> budgetEOList = budgetEODao.selectByAdminId(userEO.getUsid());
            if (CollectionUtils.isNotEmpty(budgetEOList)){
                for (BudgetEO budgetEO : budgetEOList){
                    budgetEO.setBusinessAdminId("");
                    budgetEO.setBusinessAdminName("");
                    budgetEODao.updateByPrimaryKeySelective(budgetEO);
                }
            }

        }//结束遍历离职用户
    }
}
