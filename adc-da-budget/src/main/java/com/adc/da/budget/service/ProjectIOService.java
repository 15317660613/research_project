package com.adc.da.budget.service;

import com.adc.da.budget.constant.Import;
import com.adc.da.budget.dao.BudgetEODao;
import com.adc.da.budget.dao.OrgListDao;
import com.adc.da.budget.dao.UserEPDao;
import com.adc.da.budget.dto.ProjectDTO;
import com.adc.da.budget.dto.ProjectExcelData;
import com.adc.da.budget.entity.*;
import com.adc.da.budget.enums.ProjectStatusEnums;
import com.adc.da.budget.repository.*;
import com.adc.da.budget.utils.BeanUtils;
import com.adc.da.budget.utils.CommonUtil;
import com.adc.da.excel.poi.excel.ExcelImportUtil;
import com.adc.da.excel.poi.excel.entity.ImportParams;
import com.adc.da.sys.entity.OrgEO;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.sys.service.OrgEOService;
import com.adc.da.sys.service.UserEOService;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.DateUtils;
import com.adc.da.util.utils.StringUtils;
import com.adc.da.util.utils.UUID;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * describe:
 *
 * @author 李坤澔
 *     date 2019-05-27
 */
@Service
@Slf4j
public class ProjectIOService extends BaseService<Project, String> {

    @Autowired
    private OrgListDao orgListDao;

    @Autowired
    private UserEPDao userEPDao;

    @Autowired
    private UserEOService userEOService;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private BusinessRepository businessRepository;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private OrgEOService orgEOService;

    @Autowired
    TaskRepository taskRepository ;

    @Autowired
    ChildTaskRepository childTaskRepository ;
    /**
     * 截止 date 2020-03-11 ， 一共 658 用户
     */
    private Map<String, String> userNameIdMap;

    /**
     * 初始化name关系
     *
     * 用下列sql查重名用户所属组织
     * SELECT usid, USNAME, ORG_NAME
     * FROM TS_USER u
     * LEFT JOIN TR_USER_ORG tr ON u.USID = tr.USER_ID
     * LEFT JOIN TS_ORG o ON tr.ORG_ID = o.ID
     * WHERE USNAME IN (SELECT USNAME
     * FROM TS_USER t
     * WHERE USER_CODE IS NOT NULL
     * GROUP BY USNAME
     * HAVING count(*) > 1)
     * AND USER_CODE IS NOT NULL;
     */
    public static Map<String, String> initUserNameIdMap(UserEPDao userEPDao) {
        Map<String, String> resultMap = new HashMap<>(1024);
        if (null == userEPDao) {

            return new HashMap<>();
        }
        List<UserEPEntity> userList = userEPDao.queryAllUserIdAndName();
        for (UserEPEntity eo : userList) {
            resultMap.put(eo.getUsname(), eo.getUsid());
        }

        resultMap.remove("李康");
        resultMap.remove("王磊");
        resultMap.remove("王雪");
        resultMap.remove("王妍");
        resultMap.remove("王雷");
        resultMap.remove("刘洋");
        resultMap.remove("刘琳");

        /*
         52VE766HUV    刘洋=研发设计组
         52VE766HUV    刘洋=研发信息系统部
         ZRB5XLTAEU    刘洋=法规认证组

         FMWRYMM2CM    刘琳=智能制造组
         FMWRYMM2CM    刘琳=制造信息系统部
         FNB63Q92D6    刘琳=PMO组

         8D48SQ7MJP    李康=市场数据部
         ZRXWJWHWJB    李康=软件测试部

         2BQGN3PJNP    王妍=智能技术应用部
         B6BMQJ4ZWL    王妍=虚拟仿真技术组
         B6BMQJ4ZWL    王妍=软件开发部

         5EEPSNMUKU    王磊=回收利用部
         ADM6SP8345    王磊=智能网联数据部

         754WKFR3NQ    王雪=软件测试部
         AT4S7ZDM8D    王雪=绿色发展部

         ARPM9RLTQ6    王雷=数据技术部
         AZTYWM9GRQ    王雷=材料研究部
         */
        resultMap.put("刘洋=研发", "52VE766HUV");
        resultMap.put("刘洋=软件", "ZRB5XLTAEU");

        resultMap.put("刘琳=制造", "FMWRYMM2CM");
        resultMap.put("刘琳=软件", "FNB63Q92D6");

        resultMap.put("李康=市场", "8D48SQ7MJP");
        resultMap.put("李康=软件", "ZRXWJWHWJB");

        resultMap.put("王妍=智能", "2BQGN3PJNP");
        resultMap.put("王妍=软件", "B6BMQJ4ZWL");

        resultMap.put("王磊=回收", "5EEPSNMUKU");
        resultMap.put("王磊=智能", "ADM6SP8345");

        resultMap.put("王雪=软件", "754WKFR3NQ");
        resultMap.put("王雪=绿色", "AT4S7ZDM8D");

        resultMap.put("王雷=数据", "ARPM9RLTQ6");
        resultMap.put("王雷=材料", "AZTYWM9GRQ");
        return resultMap;
    }

    /**
     * 导入
     *
     * @param is
     * @param params
     * @param baseIdO
     * @throws Exception
     */
    public Map<String, String> excelImport(InputStream is, ImportParams params, boolean saveDataFlag,
        String baseIdO) throws Exception {
        List<ProjectDTO> projectDTOS = ExcelImportUtil.importExcel(is, ProjectDTO.class, params);
        Date currentDate = new Date(System.currentTimeMillis());
        Map<String, String> resultMap = new HashMap<>();

        if (CollectionUtils.isEmpty(projectDTOS)) {
            throw new AdcDaBaseException("excel 无数据");
        }
        //进行map初始化
        userNameIdMap = initUserNameIdMap(userEPDao);
        String baseId;
        if (StringUtils.isNotEmpty(baseIdO)) {
            baseId = baseIdO;
            if (saveDataFlag) {
                //  全局清空该id的
                clearAllStrProject(projectDTOS, baseIdO);
            }
        } else {
            baseId = UUID.randomUUID10() + "_";
        }

        resultMap.put(baseId, "本次导入基础的基础ID");
        for (ProjectDTO project : projectDTOS) {

            /*
             * 校验项目名称
             */
            if (StringUtils.isEmpty(project.getName())) {
                resultMap.put(project.getExcelId(), Import.FAILURE_NAME_REASON);
                continue;
            }

            /*
             * 对时间进行初始化
             */
            if (!timeInit(project, currentDate)) {
                resultMap.put(project.getExcelId(), Import.FAILURE_DATE_REASON);
                continue;
            }

            /*
             * 设部门
             */
            if (!departmentInit(project)) {

                resultMap.put(project.getExcelId(), Import.FAILURE_ORG_REASON);
                continue;
            }

            /*
             * 负责人解析
             */
            int countLeader = projectLeaderInit(project);
            if (-1 == countLeader) {
                resultMap.put(project.getExcelId(), Import.FAILURE_LEADER_REASON);
                continue;
            }

            /*
             * 成员组装
             */
            if (!memberCheck(project)) {
                resultMap.put(project.getExcelId(), Import.FAILURE_MEMBERS_REASON);
                continue;
            }

            /*
             *  所属业务校验
             *  以及项目类型校验
             */
            if (!businessInit(project)) {
                resultMap.put(project.getExcelId(), Import.FAILURE_BUSINESS_NAME_REASON);
                continue;
            }

            /*
             *  业务下项目是否已存在校验 , 对于指定的baseIdO清空，不进行校验
             */
            if (StringUtils.isEmpty(baseIdO) && !existProject(project)) {
                resultMap.put(project.getExcelId(), Import.FAILURE_PROJECT_REASON);
                continue;
            }

            /*
             *  业务类型校验
             */
            if (!businessTypeInit(project)) {
                resultMap.put(project.getExcelId(), Import.FAILURE_BUSINESS_TYPE_REASON);
                continue;
            }

            /*
             * 设置uuid，以及默认进行中 baseId_1
             */
            project.setId(baseId + project.getExcelId());

            project.setFinishedStatus(ProjectStatusEnums.EXECUTE.getStatus());

            try {
                /*
                 * 进行插入ES操作
                 */
                if (saveDataFlag) {
                    saveUserProject(project);
                    projectRepository.save(project);
                }

                /*
                 * 对负责人存在重名的情况进行判断
                 */
                if (1 == countLeader) {
                    resultMap.put(project.getExcelId(), Import.SUCCESS);
                } else {
                    resultMap.put(project.getExcelId(), Import.WARN_LEADER_REASON);

                }
            } catch (Exception e) {
                log.error("{}插入失败：" + project.getId(), e);
                resultMap.put(project.getExcelId(), Import.FAILURE);

            }
        }

        return resultMap;

    }

    /**
     * 导入
     *
     * @param is
     * @param params
     * @throws Exception
     */
    public void excelImportDaily(InputStream is, ImportParams params) throws Exception {
        List<ProjectExcelData> projectExcelDataList = ExcelImportUtil.importExcel(is, ProjectExcelData.class, params);
        Date currentDate = new Date();
       List<Project> projectList = new ArrayList<>();

        if (CollectionUtils.isEmpty(projectExcelDataList)) {
            throw new AdcDaBaseException("excel 无数据");
        }
        //进行map初始化
        try {
            for (ProjectExcelData projectExcelData : projectExcelDataList) {
                /*
                 * 对时间进行初始化
                 */
                Project project = new Project();
                BeanUtils.copyPropertiesIgnoreNullValue(projectExcelData,project);

                Date projectStartTime = DateUtils
                        .stringToDate(projectExcelData.getProjectStartTimeStr(), DateUtils.YYYY_MM_DD_EN);
                project.setProjectStartTime(projectStartTime);
                project.setModifyTime(currentDate);
                project.setCreateTime(projectStartTime);
                project.setStartTime(projectStartTime);

                if (StringUtils.isNotEmpty(projectExcelData.getProjectBeginTimeStr())) {
                    Date projectBeginTime = DateUtils
                            .stringToDate(projectExcelData.getProjectBeginTimeStr(), DateUtils.YYYY_MM_DD_EN);
                    project.setProjectBeginTime(projectBeginTime);
                }
                if (StringUtils.isNotEmpty(projectExcelData.getProjectEndTimeStr())) {
                    Date projectEndTime = DateUtils
                            .stringToDate(projectExcelData.getProjectEndTimeStr(), DateUtils.YYYY_MM_DD_EN);
                    project.setProjectEndTime(projectEndTime);
                }
                /*
                 * 设部门
                 */
                List<OrgEO> deptOrgEOList = orgEOService.getOrgEOByOrgName(projectExcelData.getDeptName());
                if (CollectionUtils.isEmpty(deptOrgEOList)) {
                    throw new AdcDaBaseException("部门" + projectExcelData.getDeptName() + "不存在！");
                }
                project.setDeptId(deptOrgEOList.get(0).getId());

                /*
                 * 负责人解析
                 */
                UserEO projectLeaderUserEO = userEOService.getUserEOListByUserNameEquals(project.getProjectLeader()).get(0);
                project.setProjectLeaderId(projectLeaderUserEO.getUsid());
                /*
                 * 成员组装(表里存的是部门)
                 */
                List<OrgEO> memberOrgEOList = orgListDao.getOrgEOByName(project.getProjectMemberNames());
                if (CollectionUtils.isEmpty(memberOrgEOList)) {
                    throw new AdcDaBaseException("成员部门" + projectExcelData.getDeptName() + "不存在！");
                }
                HashMap<String,List<String>> deptUserIdMap = project.getDeptIdUserIdList();
                List<Map<String,String>> deptInfoListMapList = project.getDeptInfoListMap();// 所选部门ID和类型 如类型=1 只选当前自身 类型=2 包含子部门
                Set<String> tempSet =new HashSet<>();
                Map<String,String> deptInfoListMap = new HashMap<>();
                ////        "deptId":"",
                ////        "type":1,
                ////        "deptName":""
                deptInfoListMap.put("deptId",memberOrgEOList.get(0).getId());
                deptInfoListMap.put("type","2");
                deptInfoListMap.put("deptName",memberOrgEOList.get(0).getName());
                deptInfoListMapList.add(deptInfoListMap);
                List<UserEO> userEOList = orgEOService.listUserEOByOrgId(memberOrgEOList.get(0).getId());
                if (CollectionUtils.isEmpty(userEOList)) {
                    throw new AdcDaBaseException("部门" + memberOrgEOList.get(0).getName() + "不存人员！");
                }
                for(UserEO userEO:userEOList){
                    tempSet.add(userEO.getUsid());
                }
                deptUserIdMap.put(memberOrgEOList.get(0).getId(),new ArrayList<>(tempSet));
                userEOList.add(projectLeaderUserEO);
                List<OrgEO> allOrgEOList = orgEOService.queryOrgAll();
                project.setProjectMemberIds(CommonUtil.getUserIdArr(userEOList));
                project.setMemberNames(CommonUtil.getUserNameArr(userEOList));
                project.setProjectMemberNames(StringUtils.join(CommonUtil.getUserNameArr(userEOList), ','));
                project.setMapList(CommonUtil.userIdUsnameMapKv(userEOList));
                project.setUserIdDeptNameMapList(CommonUtil.userIdDeptNamesMapKv(userEOList, allOrgEOList));
                /*
                 *  所属业务校验
                 *  以及项目类型校验
                 */
                List<BudgetEO> results = budgetEODao.selectByBudgetName(project.getBudget());
                if (StringUtils.isNotEmpty(results)) {
                    BudgetEO result = results.get(0);
                    project.setPm(result.getPm());
                    project.setProjectTeam(result.getProjectTeam());
                    project.setBudgetId(result.getId());
                    //项目类型与业务类型一致
                    project.setProjectType(Integer.parseInt(result.getProperty()));
                    project.setBusinessCreateUserId(result.getCreateUserId());
                }else {
                    throw new AdcDaBaseException("业务未找到" + projectExcelData.getBudget() + "不存在！");
                }
                /*
                 *  业务类型校验
                 */
                project.setProjectType(1);
                /*
                 * 设置uuid，以及默认进行中 baseId_1
                 */
                project.setId("baseId_cy" + UUID.randomUUID10());

                project.setFinishedStatus(ProjectStatusEnums.EXECUTE.getStatus());

                project.setBusiness("综合业务");
                project.setBusinessId("AWgnL5McRknjKpC4IgKb");
                saveUserProject(project);
                projectRepository.save(project);
                projectList.add(project);
            }
        }catch (Exception e){
            projectRepository.delete(projectList);
            log.error(e.getMessage());
        }
    }

    @Autowired
    private UserWithProjectsRepository userWithProjectsRepository;

    /**
     * 存储项目与人员的关系
     * 改方法将会调用 2* excel.size 次数据库
     */
    public void saveUserProject(Project project) {
        try {
            Set<String> idList = new HashSet<>(Arrays.asList(project.getProjectMemberIds()));
            //添加业务经理
            idList.add(project.getPm());

            List<UserWithProjects> userWithProjectsList = Lists
                    .newArrayList(userWithProjectsRepository.findAll(idList));
            int len = userWithProjectsList.size();
            List<UserWithProjects> saveList = new ArrayList<>(len);

            //用于搜索的map
            Map<String, UserWithProjects> userIdMap = new HashMap<>(len);
            for (UserWithProjects userWithProjects : userWithProjectsList) {
                userIdMap.put(userWithProjects.getUserId(), userWithProjects);
            }

            for (String memberId : idList) {
                UserWithProjects userWithProjects;

                if (userIdMap.containsKey(memberId)) {
                    userWithProjects = userIdMap.get(memberId);
                } else {
                    //新用户
                    userWithProjects = new UserWithProjects();
                    userWithProjects.setUserId(memberId);
                }

                //关联项目id
                userWithProjects.getProjectIds().add(project.getId());

                //需要把业务ID关联上
                userWithProjects.getBusinessIds().add(project.getBudgetId());

                //将该记录放入存储
                saveList.add(userWithProjects);

            }
            /*
             * 批量存储
             */
            if (!saveList.isEmpty()) {
                userWithProjectsRepository.save(saveList);
            }
        }catch (Exception e){
            log.error("保存关系出错"+e.fillInStackTrace(),e);
            throw new AdcDaBaseException("保存关系出错"+e.fillInStackTrace());
        }

    }

    /**
     * 清空相关的项目关系
     * 改方法将会调用 2  次数据库
     *
     * @throws NoSuchMethodException
     */
    public void clearAllStrProject(List<ProjectDTO> datas, String str) {
        if (StringUtils.isEmpty(str)) {
            return;
        }

        Set<String> idSet = new HashSet<>();
        for (ProjectDTO data : datas) {
            idSet.add(str + data.getExcelId());
        }

        List<UserWithProjects> userWithProjectsList = Lists.newArrayList(userWithProjectsRepository.findAll());

        for (UserWithProjects eo : userWithProjectsList) {
            eo.getProjectIds().removeAll(idSet);
        }


        /*
         * 批量存储
         */
        if (!userWithProjectsList.isEmpty()) {
            userWithProjectsRepository.save(userWithProjectsList);
        }

    }

    /**
     * 时间初始化，项目修改时间为导入时间，其余为Excel内的值
     *
     * @param projectDTO
     * @param currentDate
     * @return 成功返回true，否则返回false
     * @author Lee Kwanho 李坤澔
     *     date 2019-05-27
     **/
    private boolean timeInit(ProjectDTO projectDTO, Date currentDate) {
        if (StringUtils.isNotEmpty(projectDTO.getProjectStartTimeStr())) {
            try {

                Date projectStartTime = DateUtils
                    .stringToDate(projectDTO.getProjectStartTimeStr(), DateUtils.YYYY_MM_DD_EN);
                projectDTO.setProjectStartTime(projectStartTime);
                projectDTO.setModifyTime(currentDate);
                projectDTO.setCreateTime(projectStartTime);
                projectDTO.setStartTime(projectStartTime);

                if (StringUtils.isNotEmpty(projectDTO.getProjectBeginTimeStr())) {
                    Date projectBeginTime = DateUtils
                        .stringToDate(projectDTO.getProjectBeginTimeStr(), DateUtils.YYYY_MM_DD_EN);
                    projectDTO.setProjectBeginTime(projectBeginTime);
                }
                if (StringUtils.isNotEmpty(projectDTO.getProjectEndTimeStr())) {
                    Date projectEndTime = DateUtils
                        .stringToDate(projectDTO.getProjectEndTimeStr(), DateUtils.YYYY_MM_DD_EN);
                    projectDTO.setProjectEndTime(projectEndTime);
                }
                return true;
            } catch (Exception e) {
                log.error("timeInit error", e);
                return false;
            }
        }
        return false;

    }

    /**
     * 组织机构初始化
     *
     * @param projectDTO
     * @return
     */
    private boolean departmentInit(ProjectDTO projectDTO) {
        String departName = projectDTO.getDeptName();
        try {
            if (StringUtils.isNotEmpty(departName)) {

                List<StatisticsEntity> orgEO = orgListDao.queryOrgByName(departName);
                if (CollectionUtils.isNotEmpty(orgEO)) {
                    projectDTO.setDeptId(orgEO.get(0).getId());
                    return true;
                }
            }
        } catch (Exception e) {
            log.error("departmentInit error", e);

        }

        return false;

    }

    /**
     * 负责人初始化
     *
     * @param projectDTO
     * @return
     */
    private int projectLeaderInit(ProjectDTO projectDTO) {
        String projectLeaderName = projectDTO.getProjectLeader();
        try {
            if (StringUtils.isNotEmpty(projectLeaderName)) {
                //date 2020-03-10
                // 取消和部门联查，直接查询姓名
                String userId = userNameIdMap.get(projectLeaderName);
                if (userId != null) {

                    projectDTO.setProjectLeaderId(userId);
                    projectDTO.setMemberNames(new String[] {projectLeaderName});
                    projectDTO.setCreateUserId(userId);
                    //设置姓名为真实姓名
                    projectDTO.setProjectLeader(projectLeaderName.split("=")[0]);
                    return 1;
                }
            }
        } catch (Exception e) {
            log.error("projectLeaderInit error", e);

        }
        return -1;

    }

    /**
     * 业务类型校验,全结果后进行对照
     *
     * @param projectDTO
     * @return
     */
    private boolean businessTypeInit(ProjectDTO projectDTO) {
        String businessTypeName = projectDTO.getBusiness();
        try {
            Iterable<Business> businessIterable = businessRepository.findAll();
            Iterator<Business> businessIterator = businessIterable.iterator();
            Business business = null;
            while (businessIterator.hasNext()) {
                business = businessIterator.next();
                if (businessTypeName.equals(business.getName())) {
                    projectDTO.setBusinessId(business.getId());
                    return true;
                }
            }
        } catch (Exception e) {
            log.error("businessTypeInit error", e);
        }

        return false;

    }

    @Autowired
    private BudgetEODao budgetEODao;

    private boolean businessInit(ProjectDTO projectDTO) {
        try {
            List<BudgetEO> results = budgetEODao.selectByBudgetName(projectDTO.getBudget());
            if (StringUtils.isNotEmpty(results)) {
                BudgetEO result = results.get(0);
                projectDTO.setPm(result.getPm());
                projectDTO.setProjectTeam(result.getProjectTeam());
                projectDTO.setBudgetId(result.getId());
                //项目类型与业务类型一致
                projectDTO.setProjectType(Integer.parseInt(result.getProperty()));
                projectDTO.setBusinessCreateUserId(result.getCreateUserId());
                return true;

            }
        } catch (Exception e) {
            log.error("businessInit error ", e);
        }
        return false;

    }

    private boolean existProject(ProjectDTO projectDTO) {
        boolean flag = true;
        if (null != projectDTO.getBudgetId()) {
            List<Project> projectList = projectRepository.findByBudgetIdAndDelFlagNot(projectDTO.getBudgetId(), true);
            for (Project p : projectList) {
                if (StringUtils.equals(p.getName(), projectDTO.getName())) {
                    flag = false;
                    break;
                }
            }
        }
        return flag;
    }

    /**
     * 切割excel的字符串
     *
     * @param s
     * @return
     */
    public static String[] splitExcelStr(String s) {
        if (StringUtils.isEmpty(s)) {
            throw new AdcDaBaseException("members is  null");
        }
        //将英文逗号改为中文逗号
        // 将顿号改为 中文逗号
        return s.trim()
                .replace(",", "，")
                .replace("、", "，")
                .split("，");
    }

    /**
     * 去除名字中 = 部分
     *
     * @param s
     * @return
     */
    public static String getRealName(String s) {
        String realName;
        if (s.contains("=")) {
            realName = s.split("=")[0];
        } else {
            realName = s;
        }
        return realName;
    }

    /**
     * 组织成员校验
     *
     * @param project
     * @return
     */
    private boolean memberCheck(ProjectDTO project) {
        String[] projectMembers = splitExcelStr(project.getProjectMemberNames());
        int size = projectMembers.length;
        List<String> memberNames = new ArrayList<>(size);
        List<String> memberIds = new ArrayList<>(size);
        List<Map<String, String>> mapList = new ArrayList<>(size);

        try {

            for (String userName : projectMembers) {
                String usid = userNameIdMap.get(userName);

                if (null == usid) {
                    //找不到一般是离职的，日志记录跳过继续进行
                    log.warn("not found user {} ， excelId is {}", userName, project.getExcelId());
                    continue;
                }

                String realName = getRealName(userName);
                memberNames.add(realName);
                memberIds.add(usid);

                /* 添加mapList */
                Map<String, String> map = new HashMap<>(4);
                map.put("id", usid);
                map.put("name", realName);
                mapList.add(map);
            }

            //负责人不在组内，将负责人加入
            if (!memberIds.contains(project.getProjectLeaderId())) {
                String usid = project.getProjectLeaderId();
                String name = project.getProjectLeader();
                memberIds.add(usid);
                memberNames.add(name);
                Map<String, String> map = new HashMap<>(4);
                map.put("id", usid);
                map.put("name", name);
                mapList.add(map);
            }

            project.setMemberNames(memberNames.toArray(new String[0]));
            project.setProjectMemberIds(memberIds.toArray(new String[0]));
            project.setMapList(mapList);
            return true;

        } catch (Exception e) {
            log.error("memberCheck error ", e);
        }
        return false;

    }

    public void closeProjectAndTaskWithoutWorkFlow(String projectId) {
        Project project = projectRepository.findById(projectId);
        if (null == project){
            throw new AdcDaBaseException("项目不存在，请检查");
        }
        project.setFinishedStatus(ProjectStatusEnums.COMPLETE.getStatus());
        List<Task> taskList = taskRepository.findByProjectId(projectId);
        Set<String> taskIdSet = new HashSet<>();
        if(CollectionUtils.isNotEmpty(taskList)) {
            for (Task task : taskList) {
                task.setTaskStatus(ProjectStatusEnums.COMPLETE.getStatus());
                taskIdSet.add(task.getId());
            }
            List<ChildrenTask> childrenTaskList = childTaskRepository.findByTaskIdIn(taskIdSet);
            if (CollectionUtils.isNotEmpty(childrenTaskList)) {
                for (ChildrenTask childrenTask : childrenTaskList) {
                    childrenTask.setActualFinishedTime(new Date());
                    childrenTask.setStatus(ProjectStatusEnums.COMPLETE.getStatus());
                }
                childTaskRepository.save(childrenTaskList);
            }
            taskRepository.save(taskList);
            projectRepository.save(project);
        }
    }

    public void undoCloseProjectAndTaskWithoutWorkFlow(String projectId) {
        Project project = projectRepository.findById(projectId);
        if (null == project){
            throw new AdcDaBaseException("项目不存在，请检查");
        }
        project.setFinishedStatus(ProjectStatusEnums.EXECUTE.getStatus());
        List<Task> taskList = taskRepository.findByProjectId(projectId);
        Set<String> taskIdSet = new HashSet<>();
        if(CollectionUtils.isNotEmpty(taskList)) {
            for (Task task : taskList) {
                task.setTaskStatus(ProjectStatusEnums.EXECUTE.getStatus());
                taskIdSet.add(task.getId());
            }
            List<ChildrenTask> childrenTaskList = childTaskRepository.findByTaskIdIn(taskIdSet);
            if (CollectionUtils.isNotEmpty(childrenTaskList)) {
                for (ChildrenTask childrenTask : childrenTaskList) {
                    childrenTask.setActualFinishedTime(new Date());
                    childrenTask.setStatus(ProjectStatusEnums.EXECUTE.getStatus());
                }
                childTaskRepository.save(childrenTaskList);
            }
            taskRepository.save(taskList);
            projectRepository.save(project);
        }
    }

    public void closeProjectAndTaskWithoutWorkFlowByProjectType(int projectType,String name) {
        List<Project> projectList = projectRepository.findByProjectType(projectType);
        for (Project project : projectList) {
            if (!StringUtils.startsWith(project.getName(),name)){
                continue;
            }
            project.setFinishedStatus(ProjectStatusEnums.COMPLETE.getStatus());
            List<Task> taskList = taskRepository.findByProjectId(project.getId());
            Set<String> taskIdSet = new HashSet<>();
            if (CollectionUtils.isNotEmpty(taskList)) {
                for (Task task : taskList) {
                    task.setTaskStatus(ProjectStatusEnums.COMPLETE.getStatus());
                    taskIdSet.add(task.getId());
                }
                List<ChildrenTask> childrenTaskList = childTaskRepository.findByTaskIdIn(taskIdSet);
                if (CollectionUtils.isNotEmpty(childrenTaskList)) {
                    for (ChildrenTask childrenTask : childrenTaskList) {
                        childrenTask.setActualFinishedTime(new Date());
                        childrenTask.setStatus(ProjectStatusEnums.COMPLETE.getStatus());
                    }
                    childTaskRepository.save(childrenTaskList);
                }
                taskRepository.save(taskList);
                projectRepository.save(project);
            }
        }
    }

    public void updateDailyProject() {
        List<Project> projectList = projectRepository.findByProjectType(1);
        List<Project> saveProjectList = new ArrayList<>();
        List<String> projectIdList = new ArrayList<>();
        List<OrgEO> allOrgEOList = orgEOService.queryOrgAll();
        for (Project project : projectList) {
            if (!StringUtils.startsWith(project.getId(),"baseId_cy")) {
                continue;
            }
            String[] projectMemberIds = project.getProjectMemberIds();
            if (CollectionUtils.isNotEmpty(projectMemberIds)) {
                Set<String> projectMemberIdSet = new HashSet<>(Arrays.asList(projectMemberIds));
                project.setProjectMemberIds(projectMemberIdSet.toArray(new String[projectMemberIdSet.size()]));
                if (StringUtils.contains(project.getName(),"基础研究部")){
                    projectMemberIdSet.add("JL7MUFEB8G");
                }
                List<String> projectMemberIdList = new ArrayList<>(projectMemberIdSet);
                List<UserEO> userEOList = userEOService.getDao().getUserWithRolesByUserIds(projectMemberIdList);
                project.setProjectMemberIds(CommonUtil.getUserIdArr(userEOList));
                project.setMemberNames(CommonUtil.getUserNameArr(userEOList));
                project.setProjectMemberNames(StringUtils.join(CommonUtil.getUserNameArr(userEOList), ','));
                project.setMapList(CommonUtil.userIdUsnameMapKv(userEOList));
                project.setUserIdDeptNameMapList(CommonUtil.userIdDeptNamesMapKv(userEOList, allOrgEOList));
            }
            projectIdList.add(project.getId());
            saveProjectList.add(project);
        }
        projectRepository.save(saveProjectList);
        List<Task> taskList = taskRepository.findByProjectIdIn(projectIdList);
        for (Task task : taskList){
            String[] memberIds = task.getMemberIds();
            // 判断有根据部门选人 或直接选人
            if (CollectionUtils.isNotEmpty(memberIds)) {
                Set<String> taskMemberIdSet;
                if (CollectionUtils.isNotEmpty(memberIds)) {
                    taskMemberIdSet = new HashSet<>(Arrays.asList(memberIds));
                }else {
                    taskMemberIdSet= new HashSet<>();
                }
                if (StringUtils.contains(task.getProjectName(),"基础研究部")){
                    taskMemberIdSet.add("JL7MUFEB8G"); // 将陈辰插入基础研究部的任务或项目里
                }
                List<String> taskMemberIdList = new ArrayList<>(taskMemberIdSet);
                task.setMemberIds(taskMemberIdSet.toArray(new String[taskMemberIdSet.size()]));
                List<UserEO> userEOList = userEOService.getDao().getUserWithRolesByUserIds(taskMemberIdList);
                String[] taskMemberNameArr = new String[userEOList.size()];
                for (int i = 0; i < userEOList.size(); i++) {
                    taskMemberNameArr[i] = userEOList.get(i).getUsname();
                    task.setMemberNames(taskMemberNameArr);
                    task.setMemberNameString(StringUtils.join(taskMemberNameArr,','));
                    task.setMapsList(CommonUtil.userIdUsnameMapKv(userEOList));
                    task.setUserIdDeptNameMapList(CommonUtil.userIdDeptNamesMapKv(userEOList,allOrgEOList)); //此处存在循环调用数据库
                }
            }
        }
        taskRepository.save(taskList);
    }

    public void refreshDailyProject(String projectId, String deptId) {
        Project project = projectRepository.findById(projectId);
        List<OrgEO> memberOrgEOList = orgListDao.getOrgEOByName(deptId);
        if (CollectionUtils.isEmpty(memberOrgEOList)) {
            throw new AdcDaBaseException("成员部门" + deptId + "不存在！");
        }
        HashMap<String,List<String>> deptUserIdMap = project.getDeptIdUserIdList();
        List<Map<String,String>> deptInfoListMapList = project.getDeptInfoListMap();// 所选部门ID和类型 如类型=1 只选当前自身 类型=2 包含子部门
        Set<String> tempSet =new HashSet<>();
        Map<String,String> deptInfoListMap = new HashMap<>();
        ////        "deptId":"",
        ////        "type":1,
        ////        "deptName":""
        deptInfoListMap.put("deptId",memberOrgEOList.get(0).getId());
        deptInfoListMap.put("type","2");
        deptInfoListMap.put("deptName",memberOrgEOList.get(0).getName());
        deptInfoListMapList.add(deptInfoListMap);
        List<UserEO> userEOList = orgEOService.listUserEOByOrgId(memberOrgEOList.get(0).getId());
        for(UserEO userEO:userEOList){
            tempSet.add(userEO.getUsid());
        }
        deptUserIdMap.put(memberOrgEOList.get(0).getId(),new ArrayList<>(tempSet));
        List<OrgEO> allOrgEOList = orgEOService.queryOrgAll();
        project.setProjectMemberIds(CommonUtil.getUserIdArr(userEOList));
        project.setMemberNames(CommonUtil.getUserNameArr(userEOList));
        project.setProjectMemberNames(StringUtils.join(CommonUtil.getUserNameArr(userEOList), ','));
        project.setMapList(CommonUtil.userIdUsnameMapKv(userEOList));
        project.setUserIdDeptNameMapList(CommonUtil.userIdDeptNamesMapKv(userEOList, allOrgEOList));
        projectRepository.save(project);
    }

}
