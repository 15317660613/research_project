package com.adc.da.leaderview.service.operate;

import com.adc.da.budget.constant.Role;
import com.adc.da.budget.entity.BudgetEO;
import com.adc.da.budget.entity.Daily;
import com.adc.da.budget.entity.Project;
import com.adc.da.budget.query.project.ProjectQuery;
import com.adc.da.budget.repository.DailyRepository;
import com.adc.da.budget.service.BudgetEOService;
import com.adc.da.leaderview.vo.ProjectLeaderViewVO;
import com.adc.da.login.util.UserUtils;
import com.adc.da.progress.entity.ProjectMilepostEO;
import com.adc.da.progress.service.ProjectMilepostEOService;
import com.adc.da.research.entity.ProgressEO;
import com.adc.da.research.page.ProgressEOPage;
import com.adc.da.research.service.ProgressEOService;
import com.adc.da.statistics.dao.ProjectWorktimeEODao;
import com.adc.da.statistics.entity.ProjectWorktimeEO;
import com.adc.da.statistics.service.ComputeUserWorkTimeService;
import com.adc.da.sys.entity.DicTypeEO;
import com.adc.da.sys.entity.OrgEO;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.sys.service.DicTypeEOService;
import com.adc.da.sys.service.OrgEOService;
import com.adc.da.sys.service.UserEOService;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import static com.adc.da.leaderview.service.operate.FindProjectByPageService.USER_JOIN;
import static com.adc.da.leaderview.service.operate.FindProjectByPageService.USER_MANAGER;

@Service
public class ProjectLeaderViewService {

    @Autowired
    private BudgetEOService budgetEOService;

    @Autowired
    private ComputeUserWorkTimeService computeUserWorkTimeService;
    @Autowired
    private ProjectWorktimeEODao projectWorktimeEODao ;

    @Autowired
    private UserEOService userEOService;

    @Autowired
    private OrgEOService orgEOService;

    @Autowired
    private ProjectMilepostEOService projectMilepostEOService;
    @Autowired
    private DailyRepository dailyRepository ;

    @Autowired
    private ProgressEOService progressEOService;

    @Autowired
    private DicTypeEOService dicTypeEOService;


    private static final String LOGIN_EXPIRED = "登陆可能过期，请登录！";

    public List<ProjectLeaderViewVO> findDeptCommitProject(ProjectQuery page) throws Exception {
        List<String> userIdList = getUserIds();
        List<String> deptIds = getOrgIdsByUserIds(userIdList);
        //获取当前用户id
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException(LOGIN_EXPIRED);
        }

        List<Project> projectList = findProjectByPageService.findByPage(page, userIdList, USER_JOIN);
        if (userIdList.size() > 1) {
            return setDataList(projectList, deptIds,USER_JOIN);
        } else{
            List<Daily> dailyList =  dailyRepository.findAllByCreateUserIdInAndDelFlagNot(userIdList,true);
            return setDataList(projectList, userIdList,dailyList);
        }
    }

    public List<ProjectLeaderViewVO> searchJoinPageByUserIdList(ProjectQuery page) throws Exception {
        //获取当前用户id
        String userId = UserUtils.getUserId();
        List<String> deptIds= new ArrayList<>() ;
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException(LOGIN_EXPIRED);
        }

        List<String> userIdList = new ArrayList<>();
        if (page.getSearchFlag() == 0) {
            userIdList.add(UserUtils.getUserId());
        } else {
             userIdList = getUserIds();
             deptIds = getOrgIdsByUserIds(userIdList);
        }
        List<Project> projectList = findProjectByPageService.findByPage(page, userIdList, USER_JOIN);
////        List<Daily> dailyList =  dailyRepository.findAllByCreateUserIdInAndDelFlagNot(userIdList,true);
////        return setDataList(projectList, userIdList,dailyList);
//        List<String> deptIds =getOrgIdsByUserIds(userIdList);
//        return setDataList(projectList,deptIds,USER_JOIN);
        if (userIdList.size() > 1) {
            return setDataList(projectList, deptIds,USER_JOIN);
        } else{
            List<Daily> dailyList =  dailyRepository.findAllByCreateUserIdInAndDelFlagNot(userIdList,true);
            return setDataList(projectList, userIdList,dailyList);
        }
    }

    @Autowired
    FindProjectByPageService findProjectByPageService;

    public List<ProjectLeaderViewVO> searchManagerPageByUserIdList(ProjectQuery page) throws Exception {
        //获取当前用户id
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException(LOGIN_EXPIRED);
        }

        List<String> userIdList = new ArrayList<>();
        List<String> deptIds= new ArrayList<>() ;
        if (page.getSearchFlag() == 0) {
            userIdList.add(userId);
        } else {
            userIdList = getUserIds();
            deptIds = getOrgIdsByUserIds(userIdList);
        }
        List<Project> projectList = findProjectByPageService.findByPage(page, userIdList, USER_MANAGER);
//        List<Daily> dailyList =  dailyRepository.findAllByCreateUserIdInAndDelFlagNot(userIdList,true);
//        return setDataList(projectList, userIdList,dailyList);
        if (userIdList.size() > 1) {
            return setDataList(projectList, deptIds,USER_MANAGER);
        } else{
            List<Daily> dailyList =  dailyRepository.findAllByCreateUserIdInAndDelFlagNot(userIdList,true);
            return setDataList(projectList, userIdList,dailyList);
        }
    }

    private double countWorkTime(String projectId , List<ProjectWorktimeEO> worktimeEOList){
        Double sum = 0.0 ;
        for (ProjectWorktimeEO worktimeEO : worktimeEOList){
            if (StringUtils.equals(projectId,worktimeEO.getProjectId())){
                sum = sum + worktimeEO.getWorktime();
            }
        }
        return  sum ;
    }



    private List<ProjectLeaderViewVO> setDataList(List<Project> dataList,List<String> deptIds,int type) throws Exception {
        List<ProjectLeaderViewVO> projectLeaderViewVOList = new ArrayList<>();
        List<String> projectIds = new ArrayList<>();
        Set<String> budgetIdSet = new HashSet<>();
        for (Project project : dataList) {
            projectIds.add(project.getId());
            if (StringUtils.isNotEmpty(project.getBudgetId())) {
                budgetIdSet.add(project.getBudgetId());
            }
        }

        deptIds.removeAll(Collections.singleton(null));
        if (CollectionUtils.isEmpty(projectIds)){
            return projectLeaderViewVOList;
        }
        List<ProjectWorktimeEO> projectWorktimeEOList ;
        if (type == USER_JOIN) {
            projectWorktimeEOList = projectWorktimeEODao.queryByDeptIdsAndProjectIds(deptIds, projectIds);
        }else {
            projectWorktimeEOList = projectWorktimeEODao.queryByProjectIds(projectIds);
        }
        List<String> budgetIds = new ArrayList<>(budgetIdSet);
        budgetIds.add(" ");
        List<BudgetEO> budgetEOList = budgetEOService.selectByPrimaryKeys(budgetIds);

        for (Project project : dataList) {
            for (BudgetEO budgetEO : budgetEOList) {
                if (budgetEO.getId().equals(project.getBudgetId())) {
                    project.setBudget(budgetEO.getProjectName());
                }
            }
            ProjectLeaderViewVO projectLeaderViewVO = new ProjectLeaderViewVO();
            projectLeaderViewVO.setProjectId(project.getId());
            projectLeaderViewVO.setProjectName(project.getName());
            projectLeaderViewVO.setProjectLeader(project.getProjectLeader());
            projectLeaderViewVO.setProjectMemberNames(project.getProjectMemberNames());

            if (null == project.getProjectBeginTime() && null != project.getProjectStartTime()) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(project.getProjectStartTime());
                calendar.add(Calendar.YEAR, 1);

                projectLeaderViewVO.setProjectCreateDate(project.getProjectStartTime());
                projectLeaderViewVO.setProjectEndTime(calendar.getTime());
            } else {
                projectLeaderViewVO.setProjectCreateDate(project.getProjectBeginTime());
                projectLeaderViewVO.setProjectEndTime(project.getProjectEndTime());
            }
            projectLeaderViewVO.setProjectStatus(project.getFinishedStatus());
            projectLeaderViewVO.setProjectContractAmount(project.getContractAmountStr());

//            double totalWorkTime = computeUserWorkTimeService
//                    .getProjectWorkTimeByProjectIdAndUserId(project, userIdList,dailyList);
            double totalWorkTime = countWorkTime(project.getId(),projectWorktimeEOList);

            if (project.getProjectType() != 2) {
                projectLeaderViewVO.setProcessPercent(getProcess(project.getId()));
            } else {
                ProgressEOPage progressEOPage = new ProgressEOPage();
                progressEOPage.setResearchProjectId(project.getId());
                List<ProgressEO> progressEOList = progressEOService.queryByPage(progressEOPage);
                if (CollectionUtils.isNotEmpty(progressEOList)) {
                    int listSize = progressEOList.size();
                    int finishedSize = 0;
                    for (ProgressEO progressEO : progressEOList) {
                        if (progressEO.getExtInfo1() == 1) {
                            finishedSize++;
                        }
                    }
                    double result = finishedSize * 1.0 / listSize;
                    BigDecimal bg = BigDecimal.valueOf(result).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal result3 = bg.multiply(new BigDecimal(100));
                    String percentResult = result3.toString() + "%";
                    projectLeaderViewVO.setProcessPercent(percentResult);
                }
            }
            BigDecimal bg = BigDecimal.valueOf(totalWorkTime).divide(new BigDecimal(8), 2, RoundingMode.HALF_UP);
            projectLeaderViewVO.setProjectWorkTime(bg.toString());
            projectLeaderViewVOList.add(projectLeaderViewVO);
        }
        return projectLeaderViewVOList;
    }





    private List<ProjectLeaderViewVO> setDataList(List<Project> dataList, List<String> userIdList,List<Daily> dailyList) throws Exception {
        List<ProjectLeaderViewVO> projectLeaderViewVOList = new ArrayList<>();

        Set<String> budgetIdSet = new HashSet<>();
        for (Project project : dataList) {
            if (StringUtils.isNotEmpty(project.getBudgetId())) {
                budgetIdSet.add(project.getBudgetId());
            }
        }
        List<String> budgetIds = new ArrayList<>(budgetIdSet);
        budgetIds.add(" ");
        List<BudgetEO> budgetEOList = budgetEOService.selectByPrimaryKeys(budgetIds);

        for (Project project : dataList) {
            for (BudgetEO budgetEO : budgetEOList) {
                if (budgetEO.getId().equals(project.getBudgetId())) {
                    project.setBudget(budgetEO.getProjectName());
                }
            }
            ProjectLeaderViewVO projectLeaderViewVO = new ProjectLeaderViewVO();
            projectLeaderViewVO.setProjectId(project.getId());
            projectLeaderViewVO.setProjectName(project.getName());
            projectLeaderViewVO.setProjectLeader(project.getProjectLeader());
            projectLeaderViewVO.setProjectMemberNames(project.getProjectMemberNames());

            if (null == project.getProjectBeginTime() && null != project.getProjectStartTime()) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(project.getProjectStartTime());
                calendar.add(Calendar.YEAR, 1);

                projectLeaderViewVO.setProjectCreateDate(project.getProjectStartTime());
                projectLeaderViewVO.setProjectEndTime(calendar.getTime());
            } else {
                projectLeaderViewVO.setProjectCreateDate(project.getProjectBeginTime());
                projectLeaderViewVO.setProjectEndTime(project.getProjectEndTime());
            }
            projectLeaderViewVO.setProjectStatus(project.getFinishedStatus());
            projectLeaderViewVO.setProjectContractAmount(project.getContractAmountStr());

            double totalWorkTime = computeUserWorkTimeService
                .getProjectWorkTimeByProjectIdAndUserId(project, userIdList,dailyList);

            if (project.getProjectType() != 2) {
                projectLeaderViewVO.setProcessPercent(getProcess(project.getId()));
            } else {
                ProgressEOPage progressEOPage = new ProgressEOPage();
                progressEOPage.setResearchProjectId(project.getId());
                List<ProgressEO> progressEOList = progressEOService.queryByPage(progressEOPage);
                if (CollectionUtils.isNotEmpty(progressEOList)) {
                    int listSize = progressEOList.size();
                    int finishedSize = 0;
                    for (ProgressEO progressEO : progressEOList) {
                        if (progressEO.getExtInfo1() == 1) {
                            finishedSize++;
                        }
                    }
                    double result = finishedSize * 1.0 / listSize;
                    BigDecimal bg = BigDecimal.valueOf(result).setScale(2, RoundingMode.HALF_UP);
                    BigDecimal result3 = bg.multiply(new BigDecimal(100));
                    String percentResult = result3.toString() + "%";
                    projectLeaderViewVO.setProcessPercent(percentResult);
                }
            }
            BigDecimal bg = BigDecimal.valueOf(totalWorkTime).divide(new BigDecimal(8), 2, RoundingMode.HALF_UP);
            projectLeaderViewVO.setProjectWorkTime(bg.toString());
            projectLeaderViewVOList.add(projectLeaderViewVO);
        }
        return projectLeaderViewVOList;
    }

    public List<String> getUserIds() throws Exception {
        List<String> userIdList = new ArrayList<>();
        UserEO userEO = UserUtils.getUser();
        if (null == userEO) {
            throw new AdcDaBaseException(LOGIN_EXPIRED);
        }

        Subject subject = SecurityUtils.getSubject();

        if (subject.hasRole(Role.SUPER_ADMIN) || subject.hasRole(Role.ADMIN) || subject.hasRole(Role.ZHU_REN)) {
            return userEOService.getAllUserIdsWithDeleted(); //根据要求将离职的人也算进去
        } else if (subject.hasRole(Role.BEN_BU_ZHANG)) {
            String parentOrgId = "blank";
            List<OrgEO> orgEOList = userEO.getOrgEOList();
            for (OrgEO orgEO : orgEOList) {
                if (StringUtils.equals(orgEO.getLayer(),"3")) { //本部级
                    parentOrgId = orgEO.getId();
                    break;
                }
            }
            List<String> subOrgIdList = orgEOService.getSubOrgId(parentOrgId);
            subOrgIdList.add(parentOrgId);
            userIdList = userEOService.getAllUserIdsByOrgIds(subOrgIdList);
            // 先从字典找一圈
            List<DicTypeEO> dicTypeEOList = dicTypeEOService.queryByList("FAKE_BENBU");
            if(CollectionUtils.isNotEmpty(dicTypeEOList)) {
                for (DicTypeEO dicTypeEO : dicTypeEOList) {
                    if (StringUtils.contains(dicTypeEO.getDicTypeName(), userEO.getUsid())) {
                        String orgId = dicTypeEO.getDicTypeCode();
                        List<UserEO> fakeUserEOList = orgEOService.listUserEOByOrgId(orgId);
                        if (CollectionUtils.isNotEmpty(fakeUserEOList)){
                            for (UserEO user : fakeUserEOList){
                                userIdList.add(user.getUsid());
                            }
                        }
                    }
                }
            }
            return userIdList;
        } else if (subject.hasRole(Role.BU_ZHANG)) {
            String parentOrgId = "blank";
            List<OrgEO> orgEOList = userEO.getOrgEOList();
            for (OrgEO orgEO : orgEOList) {
                if (StringUtils.equals(orgEO.getLayer(),"4")) {
                    parentOrgId = orgEO.getId();
                    break;
                }
            }
            List<String> subOrgIdList = orgEOService.getSubOrgId(parentOrgId);
            subOrgIdList.add(parentOrgId);
            userIdList = userEOService.getAllUserIdsByOrgIds(subOrgIdList);
            return userIdList;
        }
        userIdList.add(userEO.getUsid());

        return userIdList;
    }

    public List<String> getOrgIdsByUserIds(List<String> userIdList) throws Exception {
        List<String> orgIdList =  orgEOService.getOrgIdsByUserIds(userIdList);
        return orgIdList;
    }


    public String getProcess(String projectId) {
        String percentResult = "";
        List<ProjectMilepostEO> projectMilepostEOList = projectMilepostEOService.getByProjectId(projectId);
        int listSize = projectMilepostEOList.size();
        int finishedSize = 0;
        if (listSize == 0) {
            return "0.00%";
        } else {
            for (ProjectMilepostEO projectMilepostEO : projectMilepostEOList) {
                if (projectMilepostEO.getFinishStatus() == 9) {
                    finishedSize++;
                }
            }
        }
        Double result = finishedSize * 1.0 / listSize;
        BigDecimal bg = BigDecimal.valueOf(result).setScale(2, RoundingMode.HALF_UP);
        BigDecimal result3 = bg.multiply(new BigDecimal(100));
        percentResult = result3.toString() + "%";

        return percentResult;
    }

}
