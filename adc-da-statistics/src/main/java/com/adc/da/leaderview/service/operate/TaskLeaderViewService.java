package com.adc.da.leaderview.service.operate;

import com.adc.da.budget.dao.UserEPDao;
import com.adc.da.budget.entity.BudgetEO;
import com.adc.da.budget.entity.Daily;
import com.adc.da.budget.entity.Project;
import com.adc.da.budget.entity.Task;
import com.adc.da.budget.entity.UserEPEntity;
import com.adc.da.budget.query.task.TaskQuery;
import com.adc.da.budget.repository.DailyRepository;
import com.adc.da.budget.repository.ProjectRepository;
import com.adc.da.budget.service.BudgetEOService;
import com.adc.da.leaderview.vo.TaskLeaderViewVO;
import com.adc.da.login.util.UserUtils;
import com.adc.da.statistics.service.ComputeUserWorkTimeService;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TaskLeaderViewService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private BudgetEOService budgetEOService;

    @Autowired
    private ComputeUserWorkTimeService computeUserWorkTimeService;

    private static final String LOGIN_EXPIRED = "登陆可能过期，请登录！";

    @Autowired
    private FindTaskByPageService findTaskByPageService;

    @Autowired
    private DailyRepository dailyRepository;

    public List<TaskLeaderViewVO> searchByLoginUser(TaskQuery page) {

        //获取当前用户部门
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException(LOGIN_EXPIRED);
        }
        List<Task> taskList = findTaskByPageService.findByPage(page, userId);

        List<String> userIdList = new ArrayList<>();
        userIdList.add(userId);
        return setDataList(taskList, userIdList);
    }

    @Autowired
    private UserEPDao userEPDao;

    /**
     * 初始化用户map
     *
     * @param userIds
     * @return
     */
    private Map<String, String> initUserIdNameMap(List<String> userIds) {
        Map<String, String> userIdNameMap;
        if (CollectionUtils.isNotEmpty(userIds)) {
            List<UserEPEntity> list = userEPDao.queryUserIdAndNameByIdList(userIds);
            userIdNameMap = list.stream().collect(Collectors
                .toMap(UserEPEntity::getUsid, UserEPEntity::getUsname, (a, b) -> b));
        } else {
            userIdNameMap = new HashMap<>();
        }
        return userIdNameMap;
    }

    /**
     * 组装数据
     *
     * @param dataList
     * @param userIdList
     * @return
     */
    private List<TaskLeaderViewVO> setDataList(List<Task> dataList, List<String> userIdList) {
        List<Daily> dailyList = dailyRepository.findAllByCreateUserIdInAndDelFlagNot(userIdList, true);
        List<TaskLeaderViewVO> taskLeaderViewVOList = new ArrayList<>();
        List<String> taskUserIds = new ArrayList<>();
        Set<String> budgetIdSet = new HashSet<>();
        Set<String> projectIdSet = new HashSet<>();
        for (Task task : dataList) {
            if (StringUtils.isNotEmpty(task.getBudgetId())) {
                budgetIdSet.add(task.getBudgetId());
            }
            if (StringUtils.isNotEmpty(task.getProjectId())) {
                projectIdSet.add(task.getProjectId());
            }
            if (StringUtils.isEmpty(task.getApproveUserId())) {
                if (StringUtils.isEmpty(task.getProjectId())) {
                    //业务任务
                    task.setApproveUserId(task.getPm());
                } else {
                    //项目任务
                    task.setApproveUserId(task.getProjectLeaderId());
                }
            }
            taskUserIds.add(task.getApproveUserId());
        }
        Map<String, String> userIdNameMap = initUserIdNameMap(taskUserIds);

        List<String> budgetIds = new ArrayList<>(budgetIdSet);
        List<String> projectIds = new ArrayList<>(projectIdSet);

        List<Project> projectList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(projectIds)) {
            projectList = projectRepository.findByIdIn(projectIds);
        }

        for (Project project : projectList) {
            budgetIds.add(project.getBudgetId());
        }
        List<BudgetEO> budgetEOList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(budgetIds)) {
            budgetEOList = budgetEOService.selectByPrimaryKeys(budgetIds);
        }
        for (Task task : dataList) {
            for (Project project : projectList) {
                if (StringUtils.equals(project.getId(), task.getProjectId())) {
                    task.setProjectName(project.getName());
                    task.setBudgetId(project.getBudgetId());
                }
            }

            for (BudgetEO budgetEO : budgetEOList) {
                if (StringUtils.equals(budgetEO.getId(), task.getBudgetId())) {
                    task.setBudgetName(budgetEO.getProjectName());
                }
            }

            TaskLeaderViewVO taskLeaderViewVO = new TaskLeaderViewVO();
            if (StringUtils.isEmpty(task.getProjectName())) {
                taskLeaderViewVO.setTypes("业务任务");
            } else {
                taskLeaderViewVO.setProjectNames(task.getProjectName());
                taskLeaderViewVO.setTypes("项目任务");
            }
            /* 回显负责人 */
            String approveUserId = task.getApproveUserId();
            taskLeaderViewVO.setApproveUserId(approveUserId);
            taskLeaderViewVO.setApproveUserName(userIdNameMap.get(approveUserId));

            taskLeaderViewVO.setProjectNames(task.getProjectName());
            taskLeaderViewVO.setBudgetNames(task.getBudgetName());
            taskLeaderViewVO.setMemberNames(StringUtils.join(task.getMemberNames(), ","));
            taskLeaderViewVO.setPlanStartTimes(task.getPlanStartTime());
            taskLeaderViewVO.setPlanEndTimes(task.getPlanEndTime());
            taskLeaderViewVO.setTaskStatus(task.getTaskStatus());
            taskLeaderViewVO.setTaskId(task.getId());
            taskLeaderViewVO.setNames(task.getName());

            double totalWorkTime = computeUserWorkTimeService
                .getTaskWorkTimeByTaskIdAndUserId(task, userIdList, dailyList);

            //5.累加得到项目的工时数（保留两位小数）

            BigDecimal bg = BigDecimal.valueOf(totalWorkTime).divide(new BigDecimal(8), 2, RoundingMode.HALF_UP);
            taskLeaderViewVO.setTaskWorkTime(bg.toString());
            taskLeaderViewVOList.add(taskLeaderViewVO);
        }
        return taskLeaderViewVOList;
    }

}
