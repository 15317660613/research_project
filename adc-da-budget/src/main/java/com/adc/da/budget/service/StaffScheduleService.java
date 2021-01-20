package com.adc.da.budget.service;

import com.adc.da.budget.dao.BudgetEODao;
import com.adc.da.budget.dto.StaffScheduleResponseDTO;
import com.adc.da.budget.entity.ChildrenTask;
import com.adc.da.budget.entity.Daily;
import com.adc.da.budget.entity.Task;
import com.adc.da.budget.entity.UserEPEntity;
import com.adc.da.budget.repository.ChildTaskRepository;
import com.adc.da.budget.repository.DailyRepository;
import com.adc.da.budget.repository.ProjectRepository;
import com.adc.da.budget.repository.TaskRepository;
import com.adc.da.budget.utils.CommonUtil;
import com.adc.da.budget.vo.PersonDailyExportVO;
import com.adc.da.budget.vo.StaffScheduleAuxiliaryVO;
import com.adc.da.budget.vo.StaffScheduleVO;
import com.adc.da.excel.poi.excel.ExcelExportUtil;
import com.adc.da.excel.poi.excel.entity.ExportParams;
import com.adc.da.file.entity.FileEO;
import com.adc.da.file.service.FileEOService;
import com.adc.da.login.util.UserUtils;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.DateUtils;
import com.adc.da.util.utils.StringUtils;
import com.google.common.collect.Lists;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author qichunxu
 */
@Service
public class StaffScheduleService {

    @Autowired
    private DailyRepository dailyRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private BudgetEODao budgetEODao;
    @Autowired
    private ChildTaskRepository childTaskRepository;
    @Autowired
    private FileEOService fileEOService;

    public static final String YYYY_MM_DD_EN = "yyyy-MM-dd";
    /**
     * 查询人员日程信息
     *
     * @param createUserId
     * @param scheduleBeginDate
     * @param scheduleEndDate
     * @return
     */
    public List<StaffScheduleResponseDTO> queryStaffSchedule
        (String createUserId, Date scheduleBeginDate, Date scheduleEndDate) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");


        List<StaffScheduleResponseDTO> resultList = new ArrayList<>();
        //查询时间范围内的项目日报记录
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.matchQuery("createUserId", createUserId))
                .mustNot(QueryBuilders.termQuery("delFlag", true));


        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        int[] approveStateArr = {1,2};
        if (!userId.equals(createUserId)){
            queryBuilder.must(QueryBuilders.termsQuery("approveState", approveStateArr)) ;
        }

        BoolQueryBuilder timeBuilder = QueryBuilders.boolQuery().should(QueryBuilders.rangeQuery("createTime")
                .from(scheduleBeginDate.getTime())
                .to(scheduleEndDate.getTime())
                .includeLower(true)
                .includeUpper(false))
                .should(QueryBuilders.rangeQuery("dailyCreateTime")
                        .from(scheduleBeginDate.getTime())
                        .to(scheduleEndDate.getTime())
                        .includeLower(true)
                        .includeUpper(false));

        queryBuilder.must(timeBuilder);

        Iterable<Daily> dailyIterable = dailyRepository.search(queryBuilder);
        Iterator<Daily> iterator;
        List<Daily> dailyList = new ArrayList<>();
        //如果没有日报记录则直接返回空列表
        if (dailyIterable == null || !(iterator = dailyIterable.iterator()).hasNext()){
            return resultList;
        }
        while (iterator.hasNext()) { dailyList.add(iterator.next()); }


//        //遍历日报列表
        for (Daily daily : dailyList) {

            StaffScheduleResponseDTO staffScheduleResponseDTO = new StaffScheduleResponseDTO();
            staffScheduleResponseDTO.setId(daily.getId());
            staffScheduleResponseDTO.setEventName(daily.getEventName());
            staffScheduleResponseDTO.setWorkDescription(daily.getWorkDescription());
            staffScheduleResponseDTO.setWorkTimeArray(daily.getWorktimeArray());
            staffScheduleResponseDTO.setCreateUserId(daily.getCreateUserId());
            staffScheduleResponseDTO.setFinishedStatus(daily.getFinishedStatus());
            staffScheduleResponseDTO.setApproveState(daily.getApproveState());


            staffScheduleResponseDTO.setDailyParentCreateUserName(daily.getDailyParentCreateUserName());
            staffScheduleResponseDTO.setDailyParentId(daily.getDailyParentId());
            staffScheduleResponseDTO.setUserIdDeptNameMapList(daily.getUserIdDeptNameMapList());
            staffScheduleResponseDTO.setDailyParentCreateUserId(daily.getDailyParentCreateUserId());
            staffScheduleResponseDTO.setChildrenDailyCreateUserIds(daily.getChildrenDailyCreateUserIds());
            staffScheduleResponseDTO.setChildrenDailyCreateUserNames(daily.getChildrenDailyCreateUserNames());

            staffScheduleResponseDTO.setChildTaskFlag(daily.getChildTaskFlag());

            String dateStr = "";
            sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            if (daily.getDailyCreateTime() != null) {
                dateStr = sdf.format(daily.getDailyCreateTime());
            } else {
                dateStr = sdf.format(daily.getCreateTime());
            }
            staffScheduleResponseDTO.setStart(dateStr);
            staffScheduleResponseDTO.setDailyType(daily.getDailyType());
            staffScheduleResponseDTO.setTimeSlot(daily.getTimeSlot());
            staffScheduleResponseDTO.setCustomer(daily.getCustomer());
            staffScheduleResponseDTO.setDept(daily.getDept());
            staffScheduleResponseDTO.setContacts(daily.getContacts());
            staffScheduleResponseDTO.setTaskIdArray(daily.getTaskIdArray());
            Set<String> nameSet = new HashSet<>();
            for (int i = 0; i < daily.getTaskIdArray().length; i++) {
                ChildrenTask childrenTask = childTaskRepository.findOne(daily.getTaskIdArray()[i]);
                //子任务为空
                if (null == childrenTask) {
                    Task task = taskRepository.findOne(daily.getTaskIdArray()[i]);
                    if (StringUtils.isBlank(task.getProjectId())) {
                        nameSet.add(task.getBudgetName());
                    } else {
                        nameSet.add(task.getProjectName());
                    }
                    //子任务不为空
                } else {
                    Task task = taskRepository.findOne(childrenTask.getTaskId());
                    if (StringUtils.isBlank(task.getProjectId())) {
                        nameSet.add(task.getBudgetName());
                    } else {
                        nameSet.add(task.getProjectName());
                    }
                }
                staffScheduleResponseDTO.setParentNames(nameSet.toArray(new String[nameSet.size()]));

            }
            resultList.add(staffScheduleResponseDTO);
        }
            Collections.sort(resultList);
            return resultList;
    }


    /**
     * 查询人员日程信息
     *
     * @param createUserId
     * @param scheduleBeginDate
     * @param scheduleEndDate
     * @return
     */
    public Map<String,Object> newQueryStaffSchedule(String createUserId, Date scheduleBeginDate, Date scheduleEndDate) {
        Map<String,Object> resultMap = new HashMap();
        Map[] mapArr = new Map[differentDays(scheduleBeginDate,scheduleEndDate)];
        Map<String,Float> allFillWorkCostTimeMap = new HashMap();
        Map<String,Float> approveFillWorkCostTimeMap = new HashMap();
        List<StaffScheduleResponseDTO> resultList = new ArrayList<>();
        //查询时间范围内的项目日报记录
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.matchQuery("createUserId", createUserId))
                .mustNot(QueryBuilders.termQuery("delFlag", true));


        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        int[] approveStateArr = {1,2};
        if (!userId.equals(createUserId)){
            queryBuilder.must(QueryBuilders.termsQuery("approveState", approveStateArr)) ;
        }

        BoolQueryBuilder timeBuilder = QueryBuilders.boolQuery()
        .should(QueryBuilders.rangeQuery("dailyCreateTime")
                .from(scheduleBeginDate.getTime())
                .to(scheduleEndDate.getTime())
                .includeLower(true)
                .includeUpper(false));
//                .should(QueryBuilders.rangeQuery("dailyCreateTime")
//                        .from(scheduleBeginDate.getTime())
//                        .to(scheduleEndDate.getTime())
//                        .includeLower(true)
//                        .includeUpper(false));

        queryBuilder.must(timeBuilder);

        List<Daily> dailyList = Lists.newArrayList(dailyRepository.search(queryBuilder));

        //如果没有日报记录则直接返回空列表
        if (CollectionUtils.isEmpty(dailyList)){
            resultMap.put("resultList",resultList);
            resultMap.put("workTimeMap",mapArr);
            return resultMap;
        }
//        //遍历日报列表
        for (Daily daily : dailyList) {
            Float allFillWorkCostTime = 0.0f  ;
            Float approveFillWorkCostTime = 0.0f;

            if (null == daily.getDailyCreateTime()){
                continue;
            }
            SimpleDateFormat simpleDateFormatyMd = new SimpleDateFormat("yyyy-MM-dd");
            String dateStr = simpleDateFormatyMd.format(daily.getDailyCreateTime());
            //日期的总工时
            if(null != allFillWorkCostTimeMap.get(dateStr)){ {
                allFillWorkCostTime =(float) allFillWorkCostTimeMap.get(dateStr);
            }
            //日期的审批工时
            if(null != approveFillWorkCostTimeMap.get(dateStr))
                approveFillWorkCostTime =(float) approveFillWorkCostTimeMap.get(dateStr);
            }
            if (null != daily.getTimeSlot()){
                approveFillWorkCostTime  = approveFillWorkCostTime + 4 ;
                allFillWorkCostTime = allFillWorkCostTime + 4 ;
            }else if(daily.getApproveState() == 1 ){
                approveFillWorkCostTime = approveFillWorkCostTime + daily.getWorkCostTime();
                allFillWorkCostTime = allFillWorkCostTime + daily.getWorkCostTime() ;
            }else if(daily.getApproveState() != 1) {
                allFillWorkCostTime = allFillWorkCostTime + daily.getWorkCostTime() ;
            }
            allFillWorkCostTimeMap.put(dateStr, BigDecimal.valueOf(allFillWorkCostTime).setScale(2,BigDecimal.ROUND_HALF_UP).floatValue());
            approveFillWorkCostTimeMap.put(dateStr, BigDecimal.valueOf(approveFillWorkCostTime).setScale(2,BigDecimal.ROUND_HALF_UP).floatValue());

            StaffScheduleResponseDTO staffScheduleResponseDTO = new StaffScheduleResponseDTO();
            staffScheduleResponseDTO.setId(daily.getId());
            staffScheduleResponseDTO.setEventName(daily.getEventName());
            staffScheduleResponseDTO.setWorkDescription(daily.getWorkDescription());
            staffScheduleResponseDTO.setWorkTimeArray(daily.getWorktimeArray());
            staffScheduleResponseDTO.setCreateUserId(daily.getCreateUserId());
            staffScheduleResponseDTO.setFinishedStatus(daily.getFinishedStatus());
            staffScheduleResponseDTO.setApproveState(daily.getApproveState());
            staffScheduleResponseDTO.setTaskResultFileId(daily.getTaskResultFileId());
            staffScheduleResponseDTO.setDailyParentCreateUserName(daily.getDailyParentCreateUserName());
            staffScheduleResponseDTO.setDailyParentId(daily.getDailyParentId());
            staffScheduleResponseDTO.setUserIdDeptNameMapList(daily.getUserIdDeptNameMapList());
            staffScheduleResponseDTO.setDailyParentCreateUserId(daily.getDailyParentCreateUserId());
            staffScheduleResponseDTO.setChildrenDailyCreateUserIds(daily.getChildrenDailyCreateUserIds());
            staffScheduleResponseDTO.setChildrenDailyCreateUserNames(daily.getChildrenDailyCreateUserNames());
            staffScheduleResponseDTO.setChildTaskFlag(daily.getChildTaskFlag());
            try {
                if(StringUtils.isNotEmpty(daily.getTaskResultFileId())) {
                    FileEO fileEO = fileEOService.selectByPrimaryKey(daily.getTaskResultFileId());
                    staffScheduleResponseDTO.setResultFileName(fileEO.getFileName() + "." + fileEO.getFileType());
                }
            } catch (Exception e){
                e.printStackTrace();
            }
            if (null !=daily.getTaskName()) {
                staffScheduleResponseDTO.setTaskName(daily.getTaskName());
            }
            if (null !=daily.getApproveUserId()) {
                staffScheduleResponseDTO.setApproveUserId(daily.getApproveUserId());
            }else{
                staffScheduleResponseDTO.setApproveUserId("");
            }
            if (null !=daily.getApproveUserName()) {
                staffScheduleResponseDTO.setApproveUserName(daily.getApproveUserName());
            }else {
                staffScheduleResponseDTO.setApproveUserName("");
            }
            if (null !=daily.getWorkCostTime()) {
                staffScheduleResponseDTO.setWorkCostTime(daily.getWorkCostTime());
            }
            if (null !=daily.getApproveUserName()) {
                staffScheduleResponseDTO.setApproveUserName(daily.getApproveUserName());
            }
            staffScheduleResponseDTO.setProjectId(daily.getProjectId());
            staffScheduleResponseDTO.setBudgetId(daily.getBudgetId());

            staffScheduleResponseDTO.setStart(dateStr);
            staffScheduleResponseDTO.setDailyType(daily.getDailyType());
            staffScheduleResponseDTO.setTimeSlot(daily.getTimeSlot());
            staffScheduleResponseDTO.setCustomer(daily.getCustomer());
            staffScheduleResponseDTO.setDept(daily.getDept());
            staffScheduleResponseDTO.setContacts(daily.getContacts());
            staffScheduleResponseDTO.setTaskIdArray(daily.getTaskIdArray());
            Set<String> nameSet = new HashSet<>();
            for (int i = 0; i < daily.getTaskIdArray().length; i++) {
                ChildrenTask childrenTask = childTaskRepository.findOne(daily.getTaskIdArray()[i]);
                //子任务为空
                if (null == childrenTask) {
                    Task task = taskRepository.findOne(daily.getTaskIdArray()[i]);
                    if (StringUtils.isBlank(task.getProjectId())) {
                        nameSet.add(task.getBudgetName());
                    } else {
                        nameSet.add(task.getProjectName());
                    }
                    //子任务不为空
                } else {
                    Task task = taskRepository.findOne(childrenTask.getTaskId());
                    if (StringUtils.isBlank(task.getProjectId())) {
                        nameSet.add(task.getBudgetName());
                    } else {
                        nameSet.add(task.getProjectName());
                    }
                }
                staffScheduleResponseDTO.setParentNames(nameSet.toArray(new String[nameSet.size()]));

            }
            resultList.add(staffScheduleResponseDTO);
        }
        Collections.sort(resultList);
//        Calendar calendar = Calendar.getInstance();
        Float allApproveFillWorkCostTime = 0.00f ;
        Boolean start = false;
        DateTime beginTime = new DateTime(scheduleBeginDate) ;
        DateTime endTime = new DateTime(scheduleEndDate);
        int differentDays =  Days.daysBetween(beginTime, endTime).getDays();
        for (int i = 0; i < differentDays ; i ++ ){
           DateTime tmpTime = beginTime.plusDays(i);
            HashMap<String,Float> hashMap = new HashMap<>();
            // 从第一个1号开始计算有效投入2，第二个1号停止计算
            int day =tmpTime.getDayOfMonth();
            if (day == 1) {
                start = (!start ? true : false);
            }
            SimpleDateFormat simpleDateFormatyMd = new SimpleDateFormat("yyyy-MM-dd");
            String dateStr = simpleDateFormatyMd.format(tmpTime.toDate());
            if(null != approveFillWorkCostTimeMap.get(dateStr)){
                if (null == allFillWorkCostTimeMap.get(dateStr)){
                    hashMap.put("allFillWorkCostTime",0.0f);
                }else {
                    hashMap.put("allFillWorkCostTime",allFillWorkCostTimeMap.get(dateStr));
                }
                Float tmpApproveFillWorkCostTime = approveFillWorkCostTimeMap.get(dateStr) ;
                hashMap.put("approveFillWorkCostTime", tmpApproveFillWorkCostTime);

                if(7==differentDays&&null!=tmpApproveFillWorkCostTime){
                    allApproveFillWorkCostTime = allApproveFillWorkCostTime  + tmpApproveFillWorkCostTime;
                } else {
                    if (start&&null!=tmpApproveFillWorkCostTime) {
                        allApproveFillWorkCostTime = allApproveFillWorkCostTime + tmpApproveFillWorkCostTime;
                    }
                }
                mapArr[i] = hashMap;
            }
        }

        resultMap.put("allApproveFillWorkCostTime",allApproveFillWorkCostTime/8);
        resultMap.put("resultList",resultList);
        resultMap.put("workTimeMap",mapArr);
//        System.out.println("----------------------------+"+ DateUtils.getOnlyYMD(scheduleBeginDate) + DateUtils.getOnlyYMD(scheduleEndDate)+"+-------------------------------------------------------");
//        System.out.println(allApproveFillWorkCostTime/8);
//        System.out.println(dailyList.size());
//        System.out.println("allFillWorkCostTimeMap"+allFillWorkCostTimeMap);
//        System.out.println("approveFillWorkCostTimeMap"+approveFillWorkCostTimeMap);
//        System.out.println("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
//        String dateStr = DateUtils.dateToString(scheduleBeginDate,DateUtils.YYYY_MM_DD_EN);
//        if (StringUtils.equals("2020-03-30",dateStr)&&allApproveFillWorkCostTime/8 <5.2){
//            System.out.println();
//        }
        return resultMap;
    }


    public Workbook exportExcelByCreateUserIdAndTime(String createUserId , Long scheduleBeginDate, Long scheduleEndDate){
        ExportParams params = new ExportParams();
        List<PersonDailyExportVO> personDailyExportVOList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(scheduleBeginDate);
        Date beginDate = DateUtils.getOnlyYMD(calendar.getTime());

        calendar.setTimeInMillis(scheduleEndDate);
        Date endDate = DateUtils.getOnlyYMD(calendar.getTime());
        String loginUserId = UserUtils.getUserId();
        if (StringUtils.isEmpty(loginUserId)){
            throw new AdcDaBaseException("登陆过期，请登录！");
        }

        //查询时间范围内的项目日报记录
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.termQuery("createUserId", createUserId))
                .mustNot(QueryBuilders.termQuery("delFlag", true));
        List<Integer> approveStateArr = Arrays.asList(1,2);
        if(StringUtils.equals(createUserId,loginUserId)){
            approveStateArr = Arrays.asList(0,1,2,3,4,5,6);
        }
        queryBuilder.must(QueryBuilders.termsQuery("approveState", approveStateArr));

        BoolQueryBuilder timeBuilder = QueryBuilders.boolQuery()
                .should(QueryBuilders.rangeQuery("dailyCreateTime")
                        .from(beginDate.getTime())
                        .to(endDate.getTime() + 3600 * 24 * 1000)
                        .includeLower(true)
                        .includeUpper(false));

        queryBuilder.must(timeBuilder);

        List<Daily> dailyList = Lists.newArrayList(dailyRepository.search(queryBuilder));
        //如果没有日报记录则直接返回空列表
        if (CollectionUtils.isEmpty(dailyList)) {
            return ExcelExportUtil.exportExcel(params, PersonDailyExportVO.class, personDailyExportVOList);
        }
        Set<String> taskIdSet = new HashSet<>();
        for (Daily daily : dailyList){
            if (CollectionUtils.isNotEmpty(daily.getTaskIdArray())&&StringUtils.isNotEmpty(daily.getTaskIdArray()[0])) {
                taskIdSet.add(daily.getTaskIdArray()[0]);
            }
        }
        List<ChildrenTask>  childrenTaskList = childTaskRepository.findByIdIn(taskIdSet);


        for (Daily daily : dailyList) {
            String taskName = "";
            String childTaskName = "";

            String approveStatus = "已审批";
            float dailyWorkTime = 0.0f ;
            PersonDailyExportVO personDailyExportVO = new PersonDailyExportVO();
            if (null == daily.getDailyCreateTime()){
                continue;
            }
            if (StringUtils.isNotEmpty( daily.getTimeSlot() )){
                dailyWorkTime = 4 ;
            }else if(null != daily.getApproveState()&& daily.getApproveState() == 1 ){
                dailyWorkTime =  daily.getWorkCostTime() ;
            }else if(null != daily.getApproveState()&& daily.getApproveState() != 1) {
                dailyWorkTime =  daily.getWorkCostTime() ;
                switch (daily.getApproveState()){
                    case 2:
                        approveStatus= "已提交";
                        break;
                    case 3:
                        approveStatus= "未提交";
                        break;
                    case 4:
                        approveStatus= "已撤回";
                        break;
                    case 5:
                        approveStatus= "已驳回";
                        break;
                    case 6:
                        approveStatus= "未确认";
                        break;
                }
            }
            for (ChildrenTask childrenTask : childrenTaskList){
                if (CollectionUtils.isNotEmpty(daily.getTaskIdArray())&&StringUtils.equals(daily.getTaskIdArray()[0],childrenTask.getId())) {
                    taskName = childrenTask.getBelongTaskName();
                    childTaskName = childrenTask.getChildTaskName();
                    childTaskName = CommonUtil.unescapeHtml(childTaskName);
                }
            }
            if (StringUtils.isEmpty(taskName)){
                taskName = daily.getTaskName();
                taskName = CommonUtil.unescapeHtml(taskName);
            }
            if (StringUtils.isNotEmpty(daily.getBudgetNameES())){
                personDailyExportVO.setBudgetNameES(CommonUtil.unescapeHtml(daily.getBudgetNameES()));
            }
            if (StringUtils.isNotEmpty(daily.getProjectNameES())){
                personDailyExportVO.setProjectNameES(CommonUtil.unescapeHtml(daily.getProjectNameES()));
            }
            personDailyExportVO.setTaskNameES(taskName);
            personDailyExportVO.setChildTaskNameES(childTaskName);
            personDailyExportVO.setDailyCreateTime(daily.getDailyCreateTime());
            personDailyExportVO.setApproveUserNameES(StringUtils.isNotEmpty(daily.getApproveUserNameES())?daily.getApproveUserNameES():daily.getApproveUserName());
            personDailyExportVO.setCreateUserNameES(StringUtils.isNotEmpty(daily.getCreateUserNameES())?daily.getCreateUserNameES():daily.getCreateUserName());
            personDailyExportVO.setWorkCostTime(dailyWorkTime);
            personDailyExportVO.setWorkDescription(CommonUtil.unescapeHtml(daily.getWorkDescription()));
            personDailyExportVO.setApproveUserStatus(approveStatus);

            personDailyExportVOList.add(personDailyExportVO);

        }
        return ExcelExportUtil.exportExcel(params, PersonDailyExportVO.class, personDailyExportVOList);
    }






    /**
     * 查询人员日程信息
     *
     * @param userEPEntityList
     * @param scheduleBeginDate
     * @param scheduleEndDate
     * @return
     */
    public  List<UserEPEntity> newQueryStaffScheduleWithUserList(  List<UserEPEntity> userEPEntityList, Date scheduleBeginDate, Date scheduleEndDate) {
        if (CollectionUtils.isEmpty(userEPEntityList)) { return userEPEntityList ; }
        List<String> userIdList = new ArrayList<>();
        for (UserEPEntity userEPEntity : userEPEntityList) {
            if (StringUtils.isNotEmpty(userEPEntity.getUsid())) {
                userIdList.add(userEPEntity.getUsid());
            }
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(scheduleBeginDate);
        Date beginDate = DateUtils.getOnlyYMD(cal.getTime());

        cal.setTime(scheduleEndDate);
        Date endDate = DateUtils.getOnlyYMD(cal.getTime());

        //查询时间范围内的项目日报记录
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.termsQuery("createUserId", userIdList))
                .mustNot(QueryBuilders.termQuery("delFlag", true));
        int[] approveStateArr = {1};
        queryBuilder.must(QueryBuilders.termsQuery("approveState", approveStateArr));

        BoolQueryBuilder timeBuilder = QueryBuilders.boolQuery()
                .should(QueryBuilders.rangeQuery("dailyCreateTime")
                        .from(beginDate.getTime())
                        .to(endDate.getTime())
                        .includeLower(true)
                        .includeUpper(false));

        queryBuilder.must(timeBuilder);

        List<Daily> dailyList = Lists.newArrayList(dailyRepository.search(queryBuilder));
        //如果没有日报记录则直接返回空列表
        if (CollectionUtils.isEmpty(dailyList)) {
            return userEPEntityList ;
        }


        for (UserEPEntity userEPEntity : userEPEntityList) {

            Map<String, Float> tmpMap = new HashMap<>();
            Float approveFillWorkCostTime = 0.0f;

//        //遍历日报列表
            for (Daily daily : dailyList) {
                if (!StringUtils.equals(daily.getCreateUserId(),userEPEntity.getUsid())){
                    continue;
                }
                if (null == daily.getDailyCreateTime()) {
                    continue;
                }
                SimpleDateFormat simpleDateFormatyMd = new SimpleDateFormat("yyyy-MM-dd");
                String dateStr = simpleDateFormatyMd.format(daily.getDailyCreateTime());

                if (null == tmpMap.get(dateStr + "approveFillWorkCostTime")) {
                    approveFillWorkCostTime = 0.0f;
                } else {
                    approveFillWorkCostTime = (float) tmpMap.get(dateStr + "approveFillWorkCostTime");
                }

                if (null != daily.getTimeSlot()) {
                    approveFillWorkCostTime = approveFillWorkCostTime + 4;
                } else if (daily.getApproveState() == 1) {
                    approveFillWorkCostTime = approveFillWorkCostTime + daily.getWorkCostTime();

                }
                tmpMap.put(dateStr + "approveFillWorkCostTime", 1.00f * Math.round(approveFillWorkCostTime * 100) / 100);
            }
            Calendar calendar = Calendar.getInstance();
            Float allApproveFillWorkCostTime = 0.00f;
            Boolean start = false;
            for (int i = 0; i < differentDays(scheduleBeginDate, scheduleEndDate); i++) {
                /*calendar.setTime(scheduleBeginDate);
                calendar.add(Calendar.DAY_OF_YEAR, i );
                if (null != tmpMap.get(sdf.format(calendar.getTime()) + "approveFillWorkCostTime")) {
                    Float tmpApproveFillWorkCostTime = tmpMap.get(sdf.format(calendar.getTime()) + "approveFillWorkCostTime");
                    allApproveFillWorkCostTime += tmpApproveFillWorkCostTime;

                }*/
                calendar.setTime(scheduleBeginDate);
                HashMap<String,Float> hashMap = new HashMap<>();
                calendar.add(Calendar.DAY_OF_YEAR, i);
                // 从第一个1号开始计算有效投入2，第二个1号停止计算
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                if (day == 1) {
                    start = (!start ? true : false);
                }
                SimpleDateFormat simpleDateFormatyMd = new SimpleDateFormat("yyyy-MM-dd");
                String dateTime = simpleDateFormatyMd.format(calendar.getTime());
                if(null != tmpMap.get(dateTime+"approveFillWorkCostTime")){
                   // hashMap.put("allFillWorkCostTime",(Float) tmpMap.get(dateTime+"allFillWorkCostTime"));
                    Float tmpApproveFillWorkCostTime = tmpMap.get(dateTime+"approveFillWorkCostTime");
                    hashMap.put("approveFillWorkCostTime",tmpApproveFillWorkCostTime);
                    if(7==differentDays(scheduleBeginDate,scheduleEndDate)&&null!=tmpApproveFillWorkCostTime){
                        allApproveFillWorkCostTime += tmpApproveFillWorkCostTime;
                    } else {
                        if (start&&null!=tmpApproveFillWorkCostTime) {
                            allApproveFillWorkCostTime += tmpApproveFillWorkCostTime;
                        }
                    }
                }
            }
                userEPEntity.setWorkTime(allApproveFillWorkCostTime / 8);
        }

        return userEPEntityList;
    }



    /**
     * 当前人每周的日报，按时段筛选
     *
     * @return
     * @throws ParseException
     */
    public List<StaffScheduleVO> getWeekView() throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        List<StaffScheduleVO> resultList = new ArrayList<>();
        //本周上午的所有日报
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        List<String> weekDays = getWeekDays(new Date(), "yyyy-MM-dd", Boolean.TRUE);
        Date start = sdf.parse(weekDays.get(0));
        Date end = sdf.parse(weekDays.get(6));

        //本周所有的日报
        Map<String, List<Daily>> map = new HashMap<>();
        List<Daily> dailies = getDailyList(userId,start,end);
        if (CollectionUtils.isEmpty(dailies)){
            return resultList;
        }
        for (Daily daily : dailies) {
            String dateStr = sdf.format(daily.getDailyCreateTime());
            if (!map.containsKey(dateStr)) {
                map.put(dateStr, new ArrayList<Daily>());
            }
            map.get(dateStr).add(daily);
        }
        //按时段筛选
        for (int i = 0; i < weekDays.size(); i++) {
            StaffScheduleVO staffScheduleVO = new StaffScheduleVO();
            staffScheduleVO.setDay(i + 1);
            List<Map<String, List<StaffScheduleAuxiliaryVO>>> timeSlot = new ArrayList<>();
            List<Daily> dailyList = map.get(weekDays.get(i));
            if (CollectionUtils.isEmpty(dailyList)) { continue; }
            for (Daily daily : dailyList) {
                Map<String, List<StaffScheduleAuxiliaryVO>> timeMap = new HashMap<>();
                List<StaffScheduleAuxiliaryVO> list = getProjectNameOrBudgetName(daily.getTimeSlot(), daily);
                timeMap.put(daily.getTimeSlot(), list);
                timeSlot.add(timeMap);
            }
            staffScheduleVO.setTimeSlot(timeSlot);
            resultList.add(staffScheduleVO);
        }

        return resultList;
    }

    /**
     * 获得某个日期周一到周五的日期列表
     *
     * @param date    待查询的日期字符串
     * @param isChina 是否按国内的星期格式
     * @return 周一到周五的日期字符串列表
     * @throws ParseException
     */
    private static List<String> getWeekDays(Date date, String dateFormat, boolean isChina) {
        List<String> list = new ArrayList<>();
        Calendar c = Calendar.getInstance(Locale.CHINA);
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        c.setTime(date);
        int currentYear = c.get(Calendar.YEAR);
        int weekIndex = c.get(Calendar.WEEK_OF_YEAR);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == 1 && isChina) {
            c.add(Calendar.DAY_OF_MONTH, -1);
            list = getWeekDays(c.getTime(), dateFormat, isChina);
        } else {
            c.setWeekDate(currentYear, weekIndex, 1);
            for (int i = 1; i <= 7; i++) {
                c.add(Calendar.DATE, 1);
                String dateStr = sdf.format(c.getTime());
                list.add(dateStr);
            }
        }
        return list;
    }

    private List<StaffScheduleAuxiliaryVO> getProjectNameOrBudgetName(String timeSlot, Daily daily) {
        List<StaffScheduleAuxiliaryVO> list = new ArrayList<>();
        if (StringUtils.equals(timeSlot, daily.getTimeSlot())) {
            String[] taskIdArray = daily.getTaskIdArray();
            for (String id : taskIdArray) {
                StaffScheduleAuxiliaryVO staffScheduleAuxiliaryVO = new StaffScheduleAuxiliaryVO();
                ChildrenTask childrenTask = childTaskRepository.findOne(id);
                //子任务为空
                if (null == childrenTask) {
                    Task task = taskRepository.findOne(id);
                    if (StringUtils.isBlank(task.getProjectId())) {
                        String budgetName = budgetEODao.selectByPrimaryKey(task.getBudgetId()).getProjectName();
                        staffScheduleAuxiliaryVO.setId(daily.getId());
                        staffScheduleAuxiliaryVO.setName(budgetName);
                    } else {
                        String projectName = projectRepository.findOne(task.getProjectId()).getName();
                        staffScheduleAuxiliaryVO.setId(daily.getId());
                        staffScheduleAuxiliaryVO.setName(projectName);
                    }
                    list.add(staffScheduleAuxiliaryVO);
                    //子任务不为空
                } else {
                    Task task = taskRepository.findOne(childrenTask.getTaskId());
                    if (StringUtils.isBlank(task.getProjectId())) {
                        String budgetName = budgetEODao.selectByPrimaryKey(task.getBudgetId()).getProjectName();
                        staffScheduleAuxiliaryVO.setId(daily.getId());
                        staffScheduleAuxiliaryVO.setName(budgetName);
                    } else {
                        String projectName = projectRepository.findOne(task.getProjectId()).getName();
                        staffScheduleAuxiliaryVO.setId(daily.getId());
                        staffScheduleAuxiliaryVO.setName(projectName);
                    }
                    list.add(staffScheduleAuxiliaryVO);
                }
            }
        }
        return list;
    }

    public List<Daily> getDailyList(String userId,Date start,Date end){
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.matchQuery("createUserId", userId))
                .mustNot(QueryBuilders.termQuery("delFlag", Boolean.TRUE));
        BoolQueryBuilder timeBuilder = QueryBuilders.boolQuery();


        timeBuilder.must(QueryBuilders.rangeQuery("dailyCreateTime")
                        .from(start.getTime())
                        .to(end.getTime())
                        .includeLower(true)
                        .includeUpper(true));
        queryBuilder.must(timeBuilder);
        return Lists.newArrayList(dailyRepository.search(queryBuilder));
    }



    private static int differentDays(Date date1,Date date2){
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);

        int day1 = calendar1.get(Calendar.DAY_OF_YEAR);
//        System.out.println(day1);
        int day2 = calendar2.get(Calendar.DAY_OF_YEAR);
//        System.out.println(day2);
        int year1 = calendar1.get(Calendar.YEAR);
        int year2 = calendar2.get(Calendar.YEAR);

        if (year1 != year2)  //不同年
        {
            int timeDistance = 0;
            for (int i = year1 ; i < year2 ;i++){ //闰年
                if (i%4==0 && i%100!=0||i%400==0){
                    timeDistance += 366;
                }else { // 不是闰年
                    timeDistance += 365;
                }
            }
            return  timeDistance + (day2-day1);
        }else{// 同年
            return day2-day1;
        }

    }

}
