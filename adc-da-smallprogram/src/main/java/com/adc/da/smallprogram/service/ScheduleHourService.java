package com.adc.da.smallprogram.service;

import com.adc.da.budget.dto.StaffScheduleRequestDTO;
import com.adc.da.login.util.UserUtils;
import com.adc.da.smallprogram.constant.Role;
import com.adc.da.smallprogram.dao.ScheduleDetailEODao;
import com.adc.da.smallprogram.dao.ScheduleHourDao;
import com.adc.da.smallprogram.dao.ScheduleHourEODao;
import com.adc.da.smallprogram.dao.ScheudlePermissionEODao;
import com.adc.da.smallprogram.entity.ScheduleDetailEO;
import com.adc.da.smallprogram.entity.ScheduleHourEO;
import com.adc.da.smallprogram.entity.ScheudlePermissionEO;
import com.adc.da.smallprogram.enums.DeleteCodeEnum;
import com.adc.da.smallprogram.page.ScheduleHourEOPage;
import com.adc.da.smallprogram.vo.*;
import com.adc.da.sys.dao.UserEODao;
import com.adc.da.sys.entity.RoleEO;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.utils.BeanMapper;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("scheduleHourService")
public class ScheduleHourService {

    private static final Logger logger = LoggerFactory.getLogger(ScheduleHourService.class);

    @Autowired
    private BeanMapper beanMapper;

    @Autowired
    private ScheduleHourEOService scheduleHourEOService;

    @Autowired
    private ScheduleHourEODao scheduleHourEODao;

    @Autowired
    private ScheduleHourDao scheduleHourDao;
    @Autowired
    private ScheudlePermissionEODao scheudlePermissionEODao;
    @Autowired
    private ScheduleDetailEODao scheduleDetailEODao;
    @Autowired
    private UserEODao userEODao;

    /**
     * 新增日程
     * @param scheduleHourEO
     * @return
     */
    @Transactional(rollbackFor = Throwable.class)
    public ResponseMessage save(ScheduleHourEO scheduleHourEO)  throws Exception{
        //如果id为空 新增
        if(StringUtils.isBlank(scheduleHourEO.getId())){
            //判断空值
            if(StringUtils.isBlank(scheduleHourEO.getScheduleContent())){
                //内容为空 变成无
                scheduleHourEO.setScheduleContent("无");
            }
            //日程时间
            if(StringUtils.isBlank(scheduleHourEO.getScheduleDate().toString())){
                return Result.error("请输入日程时间");
            }
            //上午或下午
            if(StringUtils.isBlank(scheduleHourEO.getScheduleHour())){
                return Result.error("请输入日常上午或下午");
            }
            scheduleHourEO.setDelFlag(DeleteCodeEnum.NORMAL.getCode());
            scheduleHourEOService.insertSelective(scheduleHourEO);
        }else{
            //更新
            //判断空值
            if(StringUtils.isBlank(scheduleHourEO.getScheduleContent())){
                //内容为空 变成无
                scheduleHourEO.setScheduleContent("无");
            }
            //判断如果修改当前时间之前的数据,不允许修改
            /*if(new Date().getTime() > scheduleHourEO.getScheduleDate().getTime()){
                return Result.error("不允许修改当前时间之前的日程!");
            }*/
            scheduleHourEOService.updateByPrimaryKeySelective(scheduleHourEO);
        }
        Map<String,Object> map = new HashMap<String, Object>();

        Date date = scheduleHourEO.getScheduleDate();
        //计算开始时间
        Calendar calstar = Calendar.getInstance();
        calstar.setTime(date);
        int dayofweekstat = calstar.get(Calendar.DAY_OF_WEEK);
        if (dayofweekstat == 1) {
            dayofweekstat += 7;
        }
        calstar.add(Calendar.DATE, 2 - dayofweekstat);
        //计算结束时间
        Calendar cal = Calendar.getInstance();
        cal.setTime(getDayStartTime(calstar.getTime()));
        cal.add(Calendar.DAY_OF_WEEK, 6);
        Date weekEndSta = cal.getTime();
        //本周时间段
        Map<String,Date> mapnow = new HashMap<String,Date>();
        mapnow.put("startDate",getDayStartTime(calstar.getTime()));
        mapnow.put("endDate",getDayEndTime(weekEndSta));
        map.put("nowWeek",new SimpleDateFormat("MM/dd").format(mapnow.get("startDate")) +
                "-"+new SimpleDateFormat("MM/dd").format(mapnow.get("endDate")));
        //本周数据
        map.put("nowWeek_list",getScheduleHour(scheduleHourEO.getUserId(),mapnow, 0));

        return Result.success(map);
    }

    /**
     * 通过id获取日程信息
     * @param scheduleHourId
     * @return
     * @throws Exception
     */
    public ResponseMessage getById(String scheduleHourId) throws Exception{
        if(StringUtils.isBlank(scheduleHourId)){
            return Result.error("请输入日程id");
        }
        ScheduleHourEO scheduleHourEO = scheduleHourEOService.selectByPrimaryKey(scheduleHourId);
        return Result.success(scheduleHourEO);
    }

    /**
     *  通过日程id删除日程
     * @param scheduleHourId
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Throwable.class)
    public ResponseMessage deleteById(String scheduleHourId) throws Exception{
        if(StringUtils.isBlank(scheduleHourId)){
            return Result.error("请输入日程id");
        }
        scheduleHourEOService.deleteByPrimaryKey(scheduleHourId);
        return Result.success();
    }

    /**
     * 根绝条件获取日程信息数据
     * @param userId
     * @param weeknum  代表当前查询第几周  0:当前周, -1:上一周 1:下一周 -2:上两周 2:下两周 以此类推
     * @return
     * @throws Exception
     */
    public ResponseMessage list(String userId,Integer weeknum) throws Exception{
        //判断用户id不为空
        if(StringUtils.isBlank(userId)){
            return Result.error("输入用户id 为空!");
        }
        /*if(StringUtils.isBlank(weeknum.toString())){
            return Result.error("输入时间段为空!");
        }*/
        //组装返回消息中的日期
        Map<String,Object> map = new HashMap<String, Object>();

        //上2周时间段
        Map<String,Date> mapbefore2 = getDateList(weeknum-2);
        map.put("before2",new SimpleDateFormat("MM/dd").format(mapbefore2.get("startDate")) +
                "-"+new SimpleDateFormat("MM/dd").format(mapbefore2.get("endDate")));
        //上2周数据
        map.put("before2_list",getScheduleHour(userId,mapbefore2, weeknum-2));
        //上1周时间段
        Map<String,Date> mapbefore1 = getDateList(weeknum-1);
        map.put("before1",new SimpleDateFormat("MM/dd").format(mapbefore1.get("startDate")) +
                "-"+new SimpleDateFormat("MM/dd").format(mapbefore1.get("endDate")));
        //上1周数据
        map.put("before1_list",getScheduleHour(userId,mapbefore1, weeknum-1));
        //本周时间段
        Map<String,Date> mapnow = getDateList(weeknum);
        map.put("nowWeek",new SimpleDateFormat("MM/dd").format(mapnow.get("startDate")) +
                "-"+new SimpleDateFormat("MM/dd").format(mapnow.get("endDate")));
        //本周数据
        map.put("nowWeek_list",getScheduleHour(userId,mapnow, weeknum));
        //下1周时间段
        Map<String,Date> mapnext1 = getDateList(weeknum+1);
        map.put("next1",new SimpleDateFormat("MM/dd").format(mapnext1.get("startDate")) +
                "-"+new SimpleDateFormat("MM/dd").format(mapnext1.get("endDate")));
        //下1周数据
        map.put("next1_list",getScheduleHour(userId,mapnext1, weeknum+1));
        //下2周时间段
        Map<String,Date> mapnext2 = getDateList(weeknum+2);
        map.put("next2",new SimpleDateFormat("MM/dd").format(mapnext2.get("startDate")) +
                "-"+new SimpleDateFormat("MM/dd").format(mapnext2.get("endDate")));
        //下2周数据
        map.put("next2_list",getScheduleHour(userId,mapnext2, weeknum+2));
        return Result.success(map);
    }
    public List<Map<String,Object>> getScheduleHour(String userId,Map<String,Date> star_end, int weekNum){
        //设置查询条件
        ScheduleHourReqVO scheduleHourReqVO = new ScheduleHourReqVO();
        scheduleHourReqVO.setUserId(userId);
        scheduleHourReqVO.setScheduleDateStart(star_end.get("startDate"));
        scheduleHourReqVO.setScheduleDateEnd(star_end.get("endDate"));
        //开始查询
        List<ScheduleHourResVO> scheduleHourResVOS = scheduleHourDao.queryByList(scheduleHourReqVO);
        //处理数据
        //获取当前周时间间隔内的日期列表
        List<Date> dates = findDates(star_end.get("startDate"),star_end.get("endDate"));
        //循环时间
        List<Map<String,Object>> mapList = new ArrayList<Map<String,Object>>();
        for(Date data : dates){
            Map<String,Object> mapday = new HashMap<>();
            mapday.put("weeknum" ,getWeekNum(data));
            mapday.put("AM",null);
            mapday.put("PM",null);
            mapday.put("day",new SimpleDateFormat("MM/dd").format(data));
            mapday.put("year", getYear(weekNum, data));
            //循环数据
            for(ScheduleHourResVO scheduleHourResVO : scheduleHourResVOS ){
                //日期相等,上午
                if(DateUtils.isSameDay(data,scheduleHourResVO.getScheduleDate()) && scheduleHourResVO.getScheduleHour().equals("0")){
                    mapday.put("AM",scheduleHourResVO);
                }
                //日期相等,下午
                if(DateUtils.isSameDay(data,scheduleHourResVO.getScheduleDate()) && scheduleHourResVO.getScheduleHour().equals("1")){
                    mapday.put("PM",scheduleHourResVO);
                }
            }
            mapList.add(mapday);
        }
        return mapList;
    }
    //获取时间段内的所有日期
    public List<Date> findDates(Date dBegin, Date dEnd){
        List<Date> lDate = new ArrayList<Date>();
        lDate.add(dBegin);
        Calendar calBegin = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calBegin.setTime(dBegin);
        Calendar calEnd = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calEnd.setTime(dEnd);
        // 测试此日期是否在指定日期之后
        for(int i=0;i<6;i++)
        {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            lDate.add(calBegin.getTime());
        }
        return lDate;
    }

    public Map<String,Date> getDateList(Integer weeknum){
        Map<String,Date> map = new HashMap<String,Date>();
        //获取当前时间
        Date date = new Date();
        if (date == null) {
            return null;
        }
        //计算开始时间
        Calendar calstar = Calendar.getInstance();
        calstar.setTime(date);
        int dayofweekstat = calstar.get(Calendar.DAY_OF_WEEK);
        if (dayofweekstat == 1) {
            dayofweekstat += 7;
        }
        calstar.add(Calendar.DATE, 2 - dayofweekstat + (weeknum*7));
        map.put("startDate",getDayStartTime(calstar.getTime()));
        //计算结束时间
        Calendar cal = Calendar.getInstance();
        cal.setTime(getDayStartTime(calstar.getTime()));
        cal.add(Calendar.DAY_OF_WEEK, 6);
        Date weekEndSta = cal.getTime();
        map.put("endDate",getDayEndTime(weekEndSta));
        return map;
    }

    // 获取某个日期的开始时间
    public static Timestamp getDayStartTime(Date d) {
        Calendar calendar = Calendar.getInstance();
        if (null != d)
            calendar.setTime(d);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return new Timestamp(calendar.getTimeInMillis());
    }

    // 获取某个日期的结束时间
    public static Timestamp getDayEndTime(Date d) {
        Calendar calendar = Calendar.getInstance();
        if (null != d)
            calendar.setTime(d);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), 23, 59, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return new Timestamp(calendar.getTimeInMillis());
    }

    //获取指定日期的星期数
    public String getWeekNum(Date date ){
        Calendar c=Calendar.getInstance();
        c.setTime(date);
        int weekday=c.get(Calendar.DAY_OF_WEEK);
        switch (weekday){
            case 1 : return "周日";
            case 2 : return "周一";
            case 3 : return "周二";
            case 4 : return "周三";
            case 5 : return "周四";
            case 6 : return "周五";
            case 7 : return "周六";
            default:
                break;
        }
        return null;
    }

    /**
     * 查询日程
     * @param scheduleGetVO
     * @return
     */
    public ScheduleVO getSchedule(ScheduleGetVO scheduleGetVO) {
        // sql查询条件
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        ScheduleHourEOPage page = new ScheduleHourEOPage();
        page.setDelFlag("0");
        page.setScheduleDate(sdf.format(scheduleGetVO.getScheduleDate()));
        page.setScheduleHour(scheduleGetVO.getScheduleHour());
        page.setUserId(scheduleGetVO.getUserId());
        // 查询
        List<ScheduleHourEO> list = scheduleHourEODao.queryByList(page);
        // 返回
        ScheduleVO scheduleVO;
        if (CollectionUtils.isNotEmpty(list)) {
            ScheduleHourEO scheduleHourEO = list.get(0);
            scheduleVO = beanMapper.map(scheduleHourEO, ScheduleVO.class);
        } else {
            scheduleVO = new ScheduleVO();
        }
        scheduleVO.setScheduleDateHour(scheduleGetVO.getScheduleDate(), scheduleGetVO.getScheduleHour());
        return scheduleVO;
    }

    /**
     * 查询人员日程信息
     *
     * @param staffScheduleRequestDTO
     * @return
     */
    public List<ScheduleHourVO> newQueryStaffSchedule(StaffScheduleRequestDTO staffScheduleRequestDTO) {

        if (staffScheduleRequestDTO == null
                || staffScheduleRequestDTO.getScheduleBeginDate() == null
                || staffScheduleRequestDTO.getScheduleEndDate() == null) {
            throw new AdcDaBaseException("参数不能为空");
        }
        if (staffScheduleRequestDTO.getScheduleBeginDate().getTime() > staffScheduleRequestDTO.getScheduleEndDate()
                .getTime()) {
            throw new AdcDaBaseException("开始的时间不能晚于结束时间");
        }


        DateTime startTime = new DateTime(staffScheduleRequestDTO.getScheduleBeginDate());
        startTime = startTime.minusSeconds(1);

        Calendar endCal = Calendar.getInstance();
        endCal.setTime(staffScheduleRequestDTO.getScheduleEndDate());


        endCal.set(Calendar.HOUR,23);
        endCal.set(Calendar.MINUTE,59);
        endCal.set(Calendar.SECOND,59);

        DateTime beginTime = new DateTime(staffScheduleRequestDTO.getScheduleBeginDate()) ;
        DateTime endTime = new DateTime(staffScheduleRequestDTO.getScheduleEndDate());
        int differentDays =  Days.daysBetween(beginTime, endTime).getDays();


        List<ScheduleHourEO> scheduleHourEOList = scheduleHourEODao.queryListByUserIdTimeBetween(staffScheduleRequestDTO.getCreateUserId(),
               startTime.toDate(),endCal.getTime());
        List<String> parentIdList = new ArrayList<>();
        for (ScheduleHourEO scheduleHourEO : scheduleHourEOList){
            parentIdList.add(scheduleHourEO.getId());
        }
        List<ScheduleHourVO> list = new ArrayList<>();

        List<ScheduleDetailEO> scheduleDetailEOList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(parentIdList)) {
            scheduleDetailEOList = scheduleDetailEODao.selectByParentIdList(parentIdList);
        }
        for (ScheduleHourEO scheduleHourEO : scheduleHourEOList) {
            ScheduleHourVO scheduleHourVO = beanMapper.map(scheduleHourEO, ScheduleHourVO.class);
            List<ScheduleDetailEO> scheduleDetailEOS = scheduleHourVO.getScheduleDetailEOList();
            for (ScheduleDetailEO scheduleDetailEO : scheduleDetailEOList) {
                if (StringUtils.equals(scheduleDetailEO.getParentId(), scheduleHourVO.getId())) {
                    scheduleDetailEOS.add(scheduleDetailEO);
                }
            }
            scheduleHourVO.setScheduleDetailEOList(scheduleDetailEOS);
            list.add(scheduleHourVO);
        }

        StringBuilder maintenancePersonSB = new StringBuilder();
        maintenancePersonSB.append(staffScheduleRequestDTO.getCreateUserId());
//
        boolean visible = false;
        if(!StringUtils.equals(staffScheduleRequestDTO.getCreateUserId(),staffScheduleRequestDTO.getResearchUserId())){
            List<ScheudlePermissionEO> scheudlePermissionEOList = scheudlePermissionEODao.queryByConfigType("0");
            StringBuilder permissionMapSB = new StringBuilder();
            if(CollectionUtils.isNotEmpty(scheudlePermissionEOList)){
                for (ScheudlePermissionEO scheudlePermissionEO : scheudlePermissionEOList){
                    if (StringUtils.equals(scheudlePermissionEO.getOriginUserId(),staffScheduleRequestDTO.getResearchUserId())){
                        permissionMapSB.append(scheudlePermissionEO.getDestUserMap());
                    }
                    if (StringUtils.contains(scheudlePermissionEO.getMaintenancePersonMap(),staffScheduleRequestDTO.getResearchUserId())){
                        permissionMapSB.append(scheudlePermissionEO.getOriginUserId());
                        maintenancePersonSB.append(scheudlePermissionEO.getOriginUserId());
                    }
                }
            }
            permissionMapSB.append(staffScheduleRequestDTO.getResearchUserId());  // 自己能看自己的
            maintenancePersonSB.append(staffScheduleRequestDTO.getResearchUserId());  // 自己是自己的维护人
            //如果没权限
            if (permissionMapSB.indexOf(staffScheduleRequestDTO.getCreateUserId())>-1){
                visible = true;
            }
        }else {
            visible = true;
        }
        UserEO userEO = userEODao.getUserWithRoles(staffScheduleRequestDTO.getResearchUserId());
        List<RoleEO> roleEOList = userEO.getRoleEOList();
        boolean adminFlag = false;
        for (RoleEO roleEO : roleEOList){
            if (com.adc.da.util.utils.StringUtils.equals(roleEO.getName(), Role.SUPER_ADMIN)){
                adminFlag = true;
            }
        }
        List<String> scheduleDetailEOIdList = new ArrayList<>();
        List<String> scheduleHourEOIdList = new ArrayList<>();
        for (ScheduleHourVO scheduleHourVO : list) {
            List<ScheduleDetailEO> tempList = scheduleHourVO.getScheduleDetailEOList();
            for (ScheduleDetailEO scheduleDetailEO : tempList){
                if (!adminFlag&&!visible) {
                    scheduleDetailEO.setScheduleDetail("已有安排");
                    scheduleDetailEO.setDetailType(1);
                }
                if (StringUtils.isNotEmpty(scheduleDetailEO.getExtInfo2()) ){ //更新人id
                    // 如果当前查询人属于维护人或自己 且 修改人不是当前查询人  那么显示 “更新”
                    if (maintenancePersonSB.indexOf(scheduleHourVO.getUserId())>-1
                            && !StringUtils.equals(scheduleDetailEO.getExtInfo2(),staffScheduleRequestDTO.getResearchUserId())
                            && StringUtils.equals("1",scheduleDetailEO.getExtInfo4())
                    ){
                        scheduleDetailEO.setExtInfo5("更新");
                        scheduleDetailEOIdList.add(scheduleDetailEO.getId());
                    }
                }
            }
            if (StringUtils.isNotEmpty(scheduleHourVO.getUpdateUserId())) {
                if (maintenancePersonSB.indexOf(staffScheduleRequestDTO.getResearchUserId())>-1
                    && !StringUtils.equals(scheduleHourVO.getUpdateUserId(),staffScheduleRequestDTO.getResearchUserId())
                    && scheduleHourVO.getUpdateFlag() == 1) {
                    // scheduleHourVO.setUpdateFlag(1);
                    scheduleHourEOIdList.add(scheduleHourVO.getId());
                }
                if(StringUtils.equals(scheduleHourVO.getUpdateUserId(),staffScheduleRequestDTO.getResearchUserId())) {
                    scheduleHourVO.setUpdateFlag(0);
                }
            }
        }
        list = doMerge(list);
        for (int i = 0; i <= differentDays ; i ++ ){
            DateTime tmpTime = beginTime.plusDays(i);
            tmpTime  = tmpTime.plus(3600); // 增加一个小时 避免是日期的0点0分0秒 导致前端无法绑定
            boolean hasDayFlag = false;
            for (ScheduleHourVO scheduleHourVO : list){
                Date date = DateUtils.getOnlyYMD(scheduleHourVO.getScheduleDate());
                if (isDateYMDEquals(tmpTime.toDate(),date)){
                    hasDayFlag = true;
                    break;
                }
            }
            if (!hasDayFlag){
                ScheduleHourVO scheduleHourVO = new ScheduleHourVO();
                scheduleHourVO.setScheduleDate(tmpTime.toDate());
                list.add(scheduleHourVO);
            }
        }
        for(ScheduleHourVO scheduleHourVO : list){
            if (CollectionUtils.isNotEmpty(scheduleHourVO.getScheduleDetailEOList())){
                Collections.sort(scheduleHourVO.getScheduleDetailEOList());
            }
        }
        Collections.sort(list);
        return list;
    }
    private List<ScheduleHourVO> doMerge(List<ScheduleHourVO> list){
        List<ScheduleHourVO> resultList = new ArrayList<>();
        // key 为scheduleDate 转的时间串
        HashSet<String> dateStrSet = new HashSet<>();
        for(ScheduleHourVO scheduleHourVO : list){
            if (null != scheduleHourVO.getScheduleDate()){
                String dateStr = String.valueOf(scheduleHourVO.getScheduleDate().getTime());
                if (dateStrSet.add(dateStr)){
                    resultList.add(scheduleHourVO);
                }
            }

        }
        return resultList;
    }

    public boolean isDateYMDEquals(Date date1 , Date date2){
        date1 = DateUtils.getOnlyYMD(date1);
        date2 = DateUtils.getOnlyYMD(date2);
        return date1.equals(date2);
    }

    public void logicDelete(String id) {
        scheduleHourEODao.deleteByPrimaryKey(id);
    }


    /**
     * 获取年
     * @param weekNum
     * @param date
     * @return
     */
    private Integer getYear(int weekNum, Date date) {
//        Date today = new Date();
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(today);
//        calendar.add(Calendar.WEEK_OF_YEAR, weekNum);
        //
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date);
//        calendar.set(Calendar.DAY_OF_WEEK, calendar1.get(Calendar.DAY_OF_WEEK));
        return calendar1.get(Calendar.YEAR);
    }
}
