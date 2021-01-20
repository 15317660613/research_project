package com.adc.da.budget.service;

import com.adc.da.budget.dao.BudgetEODao;
import com.adc.da.budget.dao.OrgListDao;
import com.adc.da.budget.entity.BudgetEO;
import com.adc.da.budget.entity.ChildrenTask;
import com.adc.da.budget.entity.Daily;
import com.adc.da.budget.entity.DailyExcelData;
import com.adc.da.budget.entity.OrgWithParentName;
import com.adc.da.budget.entity.Project;
import com.adc.da.budget.entity.Task;
import com.adc.da.budget.factory.DailyExcelDataFactory;
import com.adc.da.budget.repository.ChildTaskRepository;
import com.adc.da.budget.repository.DailyRepository;
import com.adc.da.budget.repository.ProjectRepository;
import com.adc.da.budget.repository.TaskRepository;
import com.adc.da.budget.utils.CommonUtil;
import com.adc.da.budget.vo.DailyDetailExportVO;
import com.adc.da.excel.poi.excel.ExcelExportUtil;
import com.adc.da.excel.poi.excel.entity.ExportParams;
import com.adc.da.excel.poi.excel.entity.enums.ExcelType;
import com.adc.da.sys.dao.UserEODao;
import com.adc.da.sys.entity.OrgEO;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.util.utils.BeanMapper;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.DateUtils;
import com.adc.da.util.utils.StringUtils;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * 日报导出
 *
 * @author liuzixi
 * @date 2019-03-09
 */
@Service
@Slf4j
public class DailyExportService {

    /**
     * 表头
     */
    private static final String[] TITLES = {"一级部门", "部门", "人员", "业务名称", "项目名称",
        "任务名称", "子任务名称", "日期", "时段", "工时/小时", "审批状态", "审批人", "描述"};

    /**
     * dozer
     */
    @Autowired
    private BeanMapper beanMapper;

    /**
     * 业务搜索
     */
    @Autowired
    private BudgetEODao budgetEODao;

    /**
     * 查看部门
     */
    @Autowired
    private OrgListDao orgListDao;

    /**
     * 用户搜索
     */
    @Autowired
    private UserEODao userEODao;

    /**
     * 日报搜索
     */
    @Autowired
    private DailyRepository dailyRepository;

    /**
     * 子任务搜索
     */
    @Autowired
    private ChildTaskRepository childTaskRepository;

    /**
     * 任务搜索
     */
    @Autowired
    private TaskRepository taskRepository;

    /**
     * 项目搜索
     */
    @Autowired
    private ProjectRepository projectRepository;

    /**
     * 部门全称
     */
    @Autowired
    private ImportUsersService importUsersService;

    /**
     * 导出日报Excel
     *
     * @return
     */
    public Workbook getDailyWorkbook(String deptId, Long startLong, Long endLong) {
        // 1. 生成表头
        Workbook workbook = getTemplate();
        Sheet sheet = workbook.getSheetAt(0);
        // 2. 查找所有日报、所有部门
        List<Daily> dailyList = getDailyList(deptId, startLong, endLong);
        // 3. 查找每条日报数据所对应的任务、子任务关系数据
        List<DailyExcelData> excelDataList = getExcelData(dailyList, deptId);
        // 4. 写入excel
        if (CollectionUtils.isNotEmpty(excelDataList)) {
            for (int i = 1, len = excelDataList.size(); i <= len; i++) {
                DailyExcelData excelData = excelDataList.get(i - 1);
                Row row = sheet.createRow(i);
                // 一级部门、部门
                String deptName = excelData.getDept();
                String[] depts = {""};
                if (StringUtils.isNotEmpty(deptName)) {
                    depts = deptName.split("-");
                }
                String firstDeptName ="", thisDeptName="";
                if (depts.length <= 1) {
                    firstDeptName = deptName;
                    thisDeptName = deptName;
                } else if (depts.length == 2) {
                    firstDeptName = depts[0];
                    thisDeptName = depts[1];
                } else {
                    firstDeptName = depts[1];
                    thisDeptName = depts[2];
                }
                Cell firstDeptCell = row.createCell(0);
                firstDeptCell.setCellValue(firstDeptName);
                Cell deptCell = row.createCell(1);
                deptCell.setCellValue(thisDeptName);
                // 人员
                Cell personsCell = row.createCell(2);
                personsCell.setCellValue(excelData.getPersons());
                // 业务名称
                Cell budgetCell = row.createCell(3);
                budgetCell.setCellValue( CommonUtil.unescapeHtml(excelData.getBudgetName()));
                // 项目名称
                Cell projectCell = row.createCell(4);
                projectCell.setCellValue(CommonUtil.unescapeHtml(excelData.getProjectName()));
                // 任务名称
                Cell taskCell = row.createCell(5);
                taskCell.setCellValue(CommonUtil.unescapeHtml(excelData.getTaskName()));
                // 子任务名称
                Cell childrenTaskCell = row.createCell(6);
                childrenTaskCell.setCellValue(CommonUtil.unescapeHtml(excelData.getChildrenTaskName()));
                // 日期
                Cell dateCell = row.createCell(7);
                dateCell.setCellValue(DateUtils.dateToString(
                        excelData.getDailyCreateTime(),
                        "yyyy-MM-dd"));
                // 时段
                Cell timeSlotCell = row.createCell(8);
                timeSlotCell.setCellValue(excelData.getTimeSlot());
                // 工时
                Cell workTimeCell = row.createCell(9);
                workTimeCell.setCellValue(excelData.getWorkTime());
                //审批状态
                Cell statusCell = row.createCell(10);
                statusCell.setCellValue(excelData.getStatus());
                // 审批人
                Cell userCell = row.createCell(11);
                userCell.setCellValue(excelData.getApproveUserNameES());
                // 描述
                Cell description = row.createCell(12);
                description.setCellValue(excelData.getWorkDescription());
            }
        }
        return workbook;
    }

    /**
     * 导出日报Excel
     *
     * @return
     */
    public Workbook getDailyWorkbookNew(String deptId, Long startLong, Long endLong) {
        ExportParams params = new ExportParams();
        params.setType(ExcelType.XSSF);
        // 2. 查找所有日报、所有部门
        List<Daily> dailyList = getDailyList(deptId, startLong, endLong);
        // 3. 查找每条日报数据所对应的任务、子任务关系数据
        List<DailyDetailExportVO> excelDataList = getExcelDataNew(dailyList, deptId);
        // 4. 写入excel
        return ExcelExportUtil.exportExcel(params, DailyDetailExportVO.class, excelDataList);
    }


    /**
     * 生成带表头excel
     * 部门  人员  业务名称  项目名称   任务名称   子任务名称  日期  时段  工时
     *
     * @return
     */
    private Workbook getTemplate() {
        Workbook workbook = new HSSFWorkbook();
        Sheet sheet = workbook.createSheet("日报信息");
        Row titleRow = sheet.createRow(0); // 创建表头
        for (int i = 0, len = TITLES.length; i < len; i++) {
            Cell cell = titleRow.createCell(i);
            cell.setCellValue(TITLES[i]);
        }
        return workbook;
    }

    /**
     * 整理数据
     *
     * @param dailyList
     * @param deptId
     * @return
     * @author liuzixi
     *     date 2019-03-11
     */
    private List<DailyExcelData> getExcelData(List<Daily> dailyList, String deptId) {
        List<DailyExcelData> excelDataList = new ArrayList<>();
        if (CollectionUtils.isEmpty(dailyList)) {
            return excelDataList;
        }
        List<OrgEO> allOrgs = orgListDao.listAllOrg();
        int i = 0;
        for (Daily daily : dailyList) {
            // 3.1 部门
            OrgEO dept;
            if (StringUtils.isNotBlank(deptId)) {
                dept = getDept(deptId, allOrgs);
            } else {
                dept = getDept(daily.getDeptId(), allOrgs);
            }
            if (null == dept || 1 == dept.getDelFlag()) {
                log.warn("部门不存在: " + dept + "日报: " + daily.getId());
                continue;
            }
            OrgWithParentName orgWithParentName = importUsersService.getOrgWithParentName(dept, allOrgs);
            daily.setDept(orgWithParentName.getNameWithParent());
            // 3.2 创建人
            String createUserName = daily.getCreateUserName();
            if (StringUtils.isBlank(createUserName)) {
                String userId = daily.getCreateUserId();
                if (StringUtils.isNotBlank(userId)) {
                    UserEO userEO = userEODao.get(userId);
                    if (userEO != null && userEO.getDelFlag() == 0) {
                        daily.setCreateUserName(userEO.getUsname());
                    }
                }
            }
            // 3.3 日期
            Date createDate = daily.getDailyCreateTime();
            if (null == createDate) {
                daily.setDailyCreateTime(daily.getCreateTime());
            }
            excelDataList.addAll(getData(daily));

            /* 处理标识位 */
            log.warn("dailySize : {}, now:{}", dailyList.size(), i++);
        }
        return excelDataList;
    }
    public Map<String,UserEO> getUserIdAndEOMap(List<UserEO> userEOList){
        Map<String,UserEO> map = new HashMap<>();
        for (UserEO userEO : userEOList){
            map.put(userEO.getUsid(),userEO);
        }
        return map;
    }

    public Map<String,Project> getProjectIdAndEOMap(List<Project> projectList){
        Map<String,Project> map = new HashMap<>();
        for (Project project : projectList){
            map.put(project.getId(),project);
        }
        return map;
    }

    public Map<String,BudgetEO> getBudgetIdAndEOMap(List<BudgetEO> budgetEOList){
        Map<String,BudgetEO> map = new HashMap<>();
        for (BudgetEO budgetEO : budgetEOList){
            map.put(budgetEO.getId(),budgetEO);
        }
        return map;
    }

    public Map<String,Task> getTaskIdAndEOMap(List<Task> taskList){
        Map<String,Task> map = new HashMap<>();
        for (Task task : taskList){
            map.put(task.getId(),task);
        }
        return map;
    }

    public Map<String,ChildrenTask> getChildTaskIdAndEOMap(List<ChildrenTask> childrenTaskList){
        Map<String,ChildrenTask> map = new HashMap<>();
        for (ChildrenTask task : childrenTaskList){
            map.put(task.getId(),task);
        }
        return map;
    }


    private List<DailyDetailExportVO> getExcelDataNew(List<Daily> dailyList, String deptId) {
        List<DailyDetailExportVO> excelDataList = new ArrayList<>();
        if (CollectionUtils.isEmpty(dailyList)) {
            return excelDataList;
        }
        List<OrgEO> allOrgs = orgListDao.listAllOrgWithDeleted();
        Set<String> projectIdSet = new HashSet<>();
        Set<String> budgetIdSet = new HashSet<>();
        Set<String> userIdSet = new HashSet<>();
        Set<String> taskIdSet = new HashSet<>();
        for (Daily daily : dailyList) {
            if (StringUtils.isEmpty(daily.getProjectName())&&StringUtils.isNotEmpty(daily.getProjectId())){
                projectIdSet.add(daily.getProjectId());
            }
            if (StringUtils.isEmpty(daily.getBudgetName())&&StringUtils.isNotEmpty(daily.getBudgetId())){
                budgetIdSet.add(daily.getBudgetId());
            }
            if (StringUtils.isNotEmpty(daily.getCreateUserId())&&StringUtils.isEmpty(daily.getCreateUserName())){
                userIdSet.add(daily.getCreateUserId());
            }

            if (CollectionUtils.isNotEmpty(daily.getTaskIdArray())){
                taskIdSet.add(daily.getTaskIdArray()[0]);
            }
        }
        List<ChildrenTask> childTaskList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(taskIdSet)) {
            childTaskList = childTaskRepository.findByIdIn(taskIdSet);
            for (ChildrenTask childrenTask : childTaskList){
                if (StringUtils.isNotEmpty(childrenTask.getTaskId())){
                    taskIdSet.add(childrenTask.getTaskId());
                }
            }
        }
        List<Task> taskList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(taskIdSet)) {
            taskList = taskRepository.findByIdIn(taskIdSet);
            for (Task task : taskList){
                if (StringUtils.isNotEmpty(task.getProjectId())){
                    projectIdSet.add(task.getProjectId());
                }
                if (StringUtils.isNotEmpty(task.getBudgetId())){
                    budgetIdSet.add(task.getBudgetId());
                }
            }
        }
        List<Project> projectList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(projectIdSet)) {
            projectList = projectRepository.findByIdIn(projectIdSet);
        }
        for (Project project : projectList){
            if (StringUtils.isNotEmpty(project.getBudgetId())){
                budgetIdSet.add(project.getBudgetId());
            }
        }
        List<BudgetEO> budgetEOList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(budgetIdSet)) {
            budgetEOList = budgetEODao.selectByIdList(new ArrayList<>(budgetIdSet));
        }
        List<UserEO> userEOList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(userIdSet)) {
            userEOList = userEODao.getUserWithRolesByUserIds(new ArrayList<>(userIdSet));
        }
        Map<String,UserEO> userIdAndEOMap = getUserIdAndEOMap(userEOList);
        Map<String,Project> projectIdAndEOMap = getProjectIdAndEOMap(projectList);
        Map<String,BudgetEO> budgetIdAndEOMap = getBudgetIdAndEOMap(budgetEOList);
        Map<String,Task> taskIdAndEOMap = getTaskIdAndEOMap(taskList);
        Map<String,ChildrenTask> childIdAndEOMap = getChildTaskIdAndEOMap(childTaskList);


        for (Daily daily : dailyList) {
            // 3.1 部门
            OrgEO orgEO;
            if (StringUtils.isNotBlank(deptId)) {
                orgEO = getDept(deptId, allOrgs);
            } else {
                orgEO = getDept(daily.getDeptId(), allOrgs);
            }
            if (null == orgEO ) { // 不过滤被删除的部门了
                log.warn("日报部门不存在: " + new Gson().toJson(daily) + "日报: " + daily.getId());
                continue;
            }
            daily.setDept(getNameParentName(orgEO, allOrgs));
            // 3.2 创建人
            if (StringUtils.isEmpty( daily.getCreateUserName()) && StringUtils.isNotEmpty(daily.getCreateUserId())) {
                UserEO userEO = userIdAndEOMap.get(daily.getCreateUserId());
                daily.setCreateUserName(userEO != null?userEO.getUsname():"");
            }
            String taskId = CollectionUtils.isNotEmpty(daily.getTaskIdArray())?daily.getTaskIdArray()[0]:"";
            Task task = taskIdAndEOMap.get(taskId);
            ChildrenTask childrenTask = null;
            if (null == task){
                childrenTask = childIdAndEOMap.get(taskId);
                task = taskIdAndEOMap.get(childrenTask.getTaskId());
                if(null == task){
                    task  = taskRepository.findById(childrenTask.getTaskId());
                    taskIdAndEOMap.put(task.getId(),task);
                }
            }else {
                childrenTask = childIdAndEOMap.get(taskId);
            }
            DailyDetailExportVO dailyDetailExportVO = beanMapper.map(daily,DailyDetailExportVO.class);
            dailyDetailExportVO.setTaskName(task!=null? CommonUtil.unescapeHtml(task.getName()):"");
            dailyDetailExportVO.setChildrenTaskName(childrenTask!=null?CommonUtil.unescapeHtml(childrenTask.getChildTaskName()):"");
            dailyDetailExportVO.setWorkDescription(CommonUtil.unescapeHtml(daily.getWorkDescription()));
            String projectId = task!=null?task.getProjectId():"";
            String budgetId =  task!=null?task.getBudgetId():"";
            Project project = null;
            BudgetEO budgetEO = null;
            if (StringUtils.isEmpty(budgetId) && StringUtils.isNotEmpty(projectId)){ //如果budget空 那么是项目任务
                project  = projectIdAndEOMap.get(projectId);
                budgetId = null!=project ? project.getBudgetId() : "";
            }
            budgetEO = budgetIdAndEOMap.get(budgetId);
            if (null != budgetEO){
                dailyDetailExportVO.setBudgetName(budgetEO.getProjectName());
                dailyDetailExportVO.setBudgetNO(budgetEO.getDomainId());
            }
            if (null != project){
                dailyDetailExportVO.setProjectName(project.getName());
                dailyDetailExportVO.setProjectNO(project.getContractNo());
            }
            // 一级部门、部门
            String deptName =daily.getDept();
            String[] depts = {""};
            if (StringUtils.isNotEmpty(deptName)) {
                depts = deptName.split("-");
            }
            String firstDeptName ="", thisDeptName="";
            if (depts.length <= 1) {
                firstDeptName = deptName;
                thisDeptName = deptName;
            } else if (depts.length == 2) {
                firstDeptName = depts[0];
                thisDeptName = depts[1];
            } else {
                firstDeptName = depts[1];
                thisDeptName = depts[2];
            }
            dailyDetailExportVO.setFirstDeptName(firstDeptName);
            dailyDetailExportVO.setThisDeptName(thisDeptName);

            String status;
            switch (daily.getApproveState()) {
                case 1:
                    status = "已审批";
                    break;
                case 2:
                    status = "未审批";
                    break;
                case 3:
                    status = "未提交";
                    break;
                case 4:
                    status = "已撤回";
                    break;
                case 5:
                    status = "已驳回";
                    break;
                case 6:
                    status = "未确认";
                    break;
                default:
                    status = "其他";
            }
            dailyDetailExportVO.setStatus(status);
            dailyDetailExportVO.setApproveUserName(daily.getApproveUserName());
            excelDataList.add(dailyDetailExportVO);
        }
        return excelDataList;
    }

    String getNameParentName(OrgEO orgEO, List<OrgEO> allOrgs) {
        String orgName = orgEO.getName();
        String parentId = orgEO.getParentId();
        StringBuilder builder = new StringBuilder(orgName);
        /*
         * 循环判断条件：找到不含上级id的部门为止
         */
        while (StringUtils.isNotBlank(parentId) && !StringUtils.equals("0", parentId)) {
            OrgEO parent = getParent(parentId, allOrgs);
            if (parent == null) {
                // 内层跳出条件：当上级组织为唯一的最上级组织时，返回null，此时直接跳出字符串拼接
                break;
            }
            builder.insert(0, "-");
            builder.insert(0, parent.getName());

            parentId = parent.getParentId();
        }
        return builder.toString();
    }
    private OrgEO getParent(String parentId, List<OrgEO> allOrgs) {
        for (OrgEO org : allOrgs) {
            if (StringUtils.equals(parentId, org.getId())
                    && StringUtils.isNotBlank(org.getParentId())
                    && !StringUtils.equals("0", org.getParentId())) {
                return org;
            }
        }
        return null;
    }


    /**
     * 获取所有日报数据
     * 暂无排序
     *
     * @return
     */
    private List<Daily> getDailyList(String deptId, Long startLong, Long endLong) {
        // 所有未删除日报
        BoolQueryBuilder builder = QueryBuilders.boolQuery().mustNot(QueryBuilders.termQuery("delFlag", true));
        // 是否要筛选部门
        if (StringUtils.isNotBlank(deptId)) {
            builder.should(QueryBuilders.termQuery("deptId", deptId));
        }
        long dayMills = 24*60*60*1000-1;

        builder.must(
            QueryBuilders.rangeQuery("dailyCreateTime")
                         .from(startLong-1)
                         .to(endLong+dayMills)
                         //包含下界
                         .includeLower(true)
                         //不包含上界
                         .includeUpper(false));
        Iterable<Daily> dailies = dailyRepository.search(builder);
        return Lists.newArrayList(dailies);
    }

    private BigDecimal getWorkTime(Daily daily,String[] taskIds){
        BigDecimal workTime = BigDecimal.valueOf(0);
        if (null == daily.getWorkCostTime()) {
            //旧日报
            BigDecimal four = BigDecimal.valueOf(4.0);
            BigDecimal num = BigDecimal.valueOf(taskIds.length);
            workTime = four.divide(num, 2, BigDecimal.ROUND_HALF_EVEN);
        } else if (1 == daily.getApproveState()) {
            //新日报，且已审批
            workTime = BigDecimal.valueOf(daily.getWorkCostTime());
        } else {
            log.warn("日报未审批 {}", daily.getId());
            workTime = BigDecimal.valueOf(daily.getWorkCostTime());
        }
        return  workTime;
    }

    /**
     * 根据一条日报数据，获取导出excel所需数据
     *
     * 一个日报可能关联多个任务/子任务，每个任务均为一行数据
     *
     * @param daily
     * @return
     */
    private List<DailyExcelData> getData(Daily daily) {
        List<DailyExcelData> list = new ArrayList<>();
        // 查看日报中的任务id
        String[] taskIds = daily.getTaskIdArray();
        // 1. 根据任务id数量，分配工时
        BigDecimal workTime = getWorkTime(daily, taskIds);

        // 2. 根据id查看任务
        for (String id : taskIds) {
            //DailyExcelData dailyExcelData = beanMapper.map(daily, DailyExcelData.class);
            DailyExcelData dailyExcelData = DailyExcelDataFactory.map(daily);
            // 2.1 先作为子任务查询
            ChildrenTask childrenTask = childTaskRepository.findOne(id);
            String taskId;
            if (null == childrenTask || Boolean.TRUE.equals(childrenTask.getDelFlag())) {
                // 2.2 作为任务查询
                taskId = id;
            } else {
                dailyExcelData.setChildrenTaskName(childrenTask.getChildTaskName());
                // 2.3 查询此子任务的上级任务
                taskId = childrenTask.getTaskId();
            }
            Task task = taskRepository.findOne(taskId);
            if (null == task || Boolean.TRUE.equals(task.getDelFlag())) {
                log.warn("任务不存在: " + taskId + ", 子任务id: " + id + ", 日报: " + daily.getId());
                continue;
            }
            dailyExcelData.setTaskName(task.getName());
            // 3. 查询任务的上级项目、业务
            String projectId = task.getProjectId();
            String budgetId;
            if (StringUtils.isNotBlank(projectId)) {
                Project project = projectRepository.findOne(projectId);
                if (project == null || Boolean.TRUE.equals(project.getDelFlag())) {
                    log.warn("项目不存在: " + projectId + ", 任务id: " + taskId + ", 日报: " + daily.getId());
                    continue;
                }
                dailyExcelData.setProjectName(project.getName());
                budgetId = project.getBudgetId();
            } else {
                budgetId = task.getBudgetId();
            }
            BudgetEO budget = budgetEODao.selectByPrimaryKey(budgetId);
            if (null == budget) {
                log.warn(
                    "业务不存在: " + budgetId + ", 任务id: " + taskId + ", 项目id: " + projectId + ", 日报: " + daily.getId());
                continue;
            }
            dailyExcelData.setBudgetName(budget.getProjectName());
            // 4. 设置工时
            dailyExcelData.setWorkTime(workTime.toPlainString());
            String status;
            switch (daily.getApproveState()) {
                case 1:
                    status = "已审批";
                    break;
                case 2:
                    status = "未审批";
                    break;
                case 3:
                    status = "未提交";
                    break;
                case 4:
                    status = "已撤回";
                    break;
                case 5:
                    status = "已驳回";
                    break;
                case 6:
                    status = "未确认";
                    break;
                default:
                    status = "其他";
            }
            dailyExcelData.setStatus(status);
            dailyExcelData.setApproveUserNameES(daily.getApproveUserNameES());
            dailyExcelData.setWorkDescription(daily.getWorkDescription());
            list.add(dailyExcelData);
        }
        return list;
    }

    /**
     * 查看部门
     *
     * @param deptId
     * @param allOrgs
     * @return
     */
    private OrgEO getDept(String deptId, List<OrgEO> allOrgs) {
        for (OrgEO orgEO : allOrgs) {
            if (StringUtils.endsWith(deptId, orgEO.getId())) {
                return orgEO;
            }
        }
        return null;
    }
}
