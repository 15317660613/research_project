package com.adc.da.smallprogram.service;

import com.adc.da.login.util.UserUtils;
import com.adc.da.smallprogram.constant.Role;
import com.adc.da.smallprogram.dao.ScheduleDetailEODao;
import com.adc.da.smallprogram.dao.ScheduleHourDao;
import com.adc.da.smallprogram.dao.ScheduleHourEODao;
import com.adc.da.smallprogram.dao.ScheudlePermissionEODao;
import com.adc.da.smallprogram.entity.ScheduleDetailEO;
import com.adc.da.smallprogram.entity.ScheduleHourEO;
import com.adc.da.smallprogram.entity.ScheudlePermissionEO;
import com.adc.da.smallprogram.page.ScheduleHourEOPage;
import com.adc.da.smallprogram.vo.ScheduleHourDetailVO;
import com.adc.da.smallprogram.vo.ScheduleHourReqVO;
import com.adc.da.smallprogram.vo.ScheduleHourResVO;
import com.adc.da.sys.dao.UserEODao;
import com.adc.da.sys.entity.RoleEO;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.util.constant.DeleteFlagEnum;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.BeanMapper;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.DateUtils;
import com.adc.da.util.utils.StringUtils;
import com.adc.da.util.utils.UUID;
import com.twelvemonkeys.lang.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import javax.xml.crypto.Data;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.adc.da.smallprogram.service.ScheduleHourService.getDayEndTime;
import static com.adc.da.smallprogram.service.ScheduleHourService.getDayStartTime;

/**
 *
 * <br>
 * <b>功能：</b>TS_SCHEDULE_DETAIL ScheduleDetailEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-11-19 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("scheduleHourDetailService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class ScheduleHourDetailService {

    private static final Logger logger = LoggerFactory.getLogger(ScheduleHourDetailService.class);

    @Autowired
    private ScheduleHourEOService scheduleHourEOService;

    @Autowired
    private ScheduleDetailEOService scheduleDetailEOService;

    @Autowired
    private ScheudlePermissionEOService scheudlePermissionEOService;
    @Autowired
    private ScheudlePermissionEODao scheudlePermissionEODao;

    @Autowired
    private ScheduleHourEODao scheduleHourEODao;

    @Autowired
    private  ScheduleHourService scheduleHourService;
    @Autowired
    private ScheduleDetailEODao scheduleDetailEODao;

    @Autowired
    private ScheduleHourDao scheduleHourDao;

    @Autowired
    private BeanMapper beanMapper;
    @Autowired
    private UserEODao userEODao;


    public  ScheduleHourDetailVO  saveOrUpdate(ScheduleHourDetailVO scheduleHourDetailVO) throws Exception {
       // String[] ids = scheduleHourDetailVO.getIds();
        List<String> scheduleIds = new ArrayList<>(Arrays.asList(scheduleHourDetailVO.getIds()));
        if(CollectionUtils.isNotEmpty(scheduleHourDetailVO.getScheduleDetailEOs())){
            for (ScheduleDetailEO tepScheduleDetailEO : scheduleHourDetailVO.getScheduleDetailEOs()) {
                if(StringUtils.isNotEmpty(tepScheduleDetailEO.getId())&&StringUtils.isEmpty(tepScheduleDetailEO.getTimeLimit())
                    &&StringUtils.isEmpty(tepScheduleDetailEO.getScheduleDetail())){
                    scheduleIds.add(tepScheduleDetailEO.getId());
                }
            }
        }
        Date updateTime = new Date();
        if (CollectionUtils.isNotEmpty(scheduleIds)) {
            scheduleDetailEOService.logicDeleteInBatch(scheduleIds);
        }
        ScheduleHourEO scheduleHourEO = beanMapper.map(scheduleHourDetailVO, ScheduleHourEO.class);

        scheduleHourEO.setUpdateFlag(1);
        ScheduleHourEO databaseScheduleHourEO = null;
        List<ScheduleHourEO> databaseScheduleHourEOList = scheduleHourEOService.getDao().selectByUserIdAndScheduleDate(scheduleHourEO.getUserId(),scheduleHourEO.getScheduleDate());
        if (CollectionUtils.isNotEmpty(databaseScheduleHourEOList)){
            databaseScheduleHourEO = databaseScheduleHourEOList.get(0);
        }
        // 新增 两种情况
        if (StringUtils.isEmpty(scheduleHourEO.getId())) {
            scheduleHourEO.setCreateTime(updateTime);
            scheduleHourEO.setUpdateTime(updateTime);

            // 如果当前日期存在安排 报错
            if(null != databaseScheduleHourEO){
                throw new AdcDaBaseException("目标日期已有其他安排，请重新选择日期！");
            }else {
                scheduleHourEO.setDelFlag(String.valueOf(DeleteFlagEnum.NORMAL.getValue()));
                scheduleHourDetailVO.setDelFlag(String.valueOf(DeleteFlagEnum.NORMAL.getValue()));
                scheduleHourEOService.insertSelective(scheduleHourEO);
            }
        }else if (!StringUtils.isEmpty(scheduleHourEO.getId())){
            scheduleHourEO.setUpdateTime(updateTime);
            // 如果只是更新，有两种情况
            // 更新到当天的已有其他数据
            if(null != databaseScheduleHourEO && !StringUtils.equals(scheduleHourEO.getId(),databaseScheduleHourEO.getId())) {
                throw new AdcDaBaseException("目标日期已有其他安排，请重新选择日期！");
            }
            // 更新自身
            if(null != databaseScheduleHourEO && StringUtils.equals(scheduleHourEO.getId(),databaseScheduleHourEO.getId())) {
                scheduleHourEOService.updateByPrimaryKeySelective(scheduleHourEO);
            }else {
                scheduleHourEOService.updateByPrimaryKeySelective(scheduleHourEO);
            }

        }
        String dateYMD = DateUtils.dateToString(scheduleHourEO.getScheduleDate(),DateUtils.YYYY_MM_DD_EN);
        for (ScheduleDetailEO scheduleDetailEO : scheduleHourDetailVO.getScheduleDetailEOs()) {
            String timeLimit = scheduleDetailEO.getTimeLimit();
            if (!StringUtil.isEmpty(timeLimit)){
                String beginDate = dateYMD + " " + timeLimit.substring(0,timeLimit.indexOf(" "));
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                dateFormat.setTimeZone(TimeZone.getTimeZone("Etc/GMT-8"));
                Date date = dateFormat.parse(beginDate);
                scheduleDetailEO.setBeginTime(date);
            }else {
                String beginDate = dateYMD + " " + "06:00";
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                dateFormat.setTimeZone(TimeZone.getTimeZone("Etc/GMT-8"));
                Date date = dateFormat.parse(beginDate);
                scheduleDetailEO.setBeginTime(date);
            }
        }
        Collections.sort(scheduleHourDetailVO.getScheduleDetailEOs());

        List<ScheduleDetailEO> newList = new ArrayList<>();
        for (int i = 0 ; i < scheduleHourDetailVO.getScheduleDetailEOs().size(); i++ ) {
            ScheduleDetailEO scheduleDetailEO = scheduleHourDetailVO.getScheduleDetailEOs().get(i);
            scheduleDetailEO.setOrderNumber(i);
            scheduleDetailEO.setExtInfo4("1");
            if (StringUtils.isEmpty(scheduleDetailEO.getId())) {
                scheduleDetailEO.setId(UUID.randomUUID10());
                scheduleDetailEO.setParentId(scheduleHourEO.getId());
                scheduleDetailEO.setDelFlag(DeleteFlagEnum.NORMAL.getValue());
                scheduleDetailEO.setCreateUserId(scheduleHourEO.getUserId());
                scheduleDetailEOService.insertSelective(scheduleDetailEO);
            } else {
                scheduleDetailEOService.updateByPrimaryKeySelective(scheduleDetailEO);
            }
            newList.add(scheduleDetailEO);
        }

        return scheduleHourDetailVO;
    }


//    public  Map<String,Object>  saveOrUpdate(ScheduleHourDetailVO scheduleHourDetailVO) throws Exception {
//        String[] ids = scheduleHourDetailVO.getIds();
//        if (ids.length > 0) {
//            scheduleDetailEOService.logicDeleteInBatch(Arrays.asList(ids));
//        }
//        ScheduleHourEO scheduleHourEO = beanMapper.map(scheduleHourDetailVO, ScheduleHourEO.class);
//        if (StringUtils.isEmpty(scheduleHourEO.getId())) {
//            scheduleHourEO.setDelFlag(String.valueOf(DeleteFlagEnum.NORMAL.getValue()));
//            scheduleHourDetailVO.setDelFlag(String.valueOf(DeleteFlagEnum.NORMAL.getValue()));
//            scheduleHourEOService.insertSelective(scheduleHourEO);
//        } else {
//            scheduleHourEOService.updateByPrimaryKeySelective(scheduleHourEO);
//        }
//        List<ScheduleDetailEO> newList = new ArrayList<>();
//        for (ScheduleDetailEO scheduleDetailEO : scheduleHourDetailVO.getScheduleDetailEOs()) {
//            if (StringUtils.isEmpty(scheduleDetailEO.getId())) {
//                scheduleDetailEO.setId(UUID.randomUUID10());
//                scheduleDetailEO.setParentId(scheduleHourEO.getId());
//                scheduleDetailEO.setDelFlag(DeleteFlagEnum.NORMAL.getValue());
//                scheduleDetailEO.setCreateUserId(scheduleHourEO.getUserId());
//                scheduleDetailEOService.insertSelective(scheduleDetailEO);
//            } else {
//                scheduleDetailEOService.updateByPrimaryKeySelective(scheduleDetailEO);
//            }
//            newList.add(scheduleDetailEO);
//        }
//        if (StringUtils.isEmpty(scheduleHourEO.getId())) {
//            scheduleHourDetailVO.setScheduleDetailEOs(newList);
//        }
//
//
//        Map<String,Object> map = new HashMap<String, Object>();
//
//        Date date = scheduleHourEO.getScheduleDate();
//        //计算开始时间
//        Calendar calstar = Calendar.getInstance();
//        calstar.setTime(date);
//        int dayofweekstat = calstar.get(Calendar.DAY_OF_WEEK);
//        if (dayofweekstat == 1) {
//            dayofweekstat += 7;
//        }
//        calstar.add(Calendar.DATE, 2 - dayofweekstat);
//        //计算结束时间
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(getDayStartTime(calstar.getTime()));
//        cal.add(Calendar.DAY_OF_WEEK, 6);
//        Date weekEndSta = cal.getTime();
//        //本周时间段
//        Map<String,Date> mapnow = new HashMap<String,Date>();
//        mapnow.put("startDate",getDayStartTime(calstar.getTime()));
//        mapnow.put("endDate",getDayEndTime(weekEndSta));
//        map.put("nowWeek",new SimpleDateFormat("MM/dd").format(mapnow.get("startDate")) +
//                "-"+new SimpleDateFormat("MM/dd").format(mapnow.get("endDate")));
//        //本周数据
//        map.put("nowWeek_list",scheduleHourService.getScheduleHour(scheduleHourEO.getUserId(),mapnow, 0));
//
//        return map;
//    }

    public ScheduleHourDetailVO find(String id, String originUserId,String destUserId)  throws Exception {
        ScheduleHourEO scheduleHourEO = scheduleHourEOService.selectByPrimaryKey(id);
        ScheduleHourDetailVO scheduleHourDetailVO = beanMapper.map(scheduleHourEO, ScheduleHourDetailVO.class);
        List<ScheduleDetailEO> scheduleDetailEOs = new ArrayList<>();
//        if(StringUtils.equals(originUserId,destUserId)){
//            scheduleDetailEOs = scheduleDetailEOService.selectByParentId(id, 0);
//        } else {
//            //detailType=1，判断有无权限，如果没有就将查出来的描述设置为 已占用
//            scheduleDetailEOs = scheduleDetailEOService.selectByParentId(id, 1);
//            if(scheudlePermissionEOService.selectByOriginIdAndDestUserIdLike(originUserId,destUserId)<1){
//                for(ScheduleDetailEO scheduleDetailEO:scheduleDetailEOs){
//                    scheduleDetailEO.setScheduleDetail("已有安排");
//                }
//            }
//        }
        UserEO userEO = userEODao.getUserWithRoles(originUserId);
        List<RoleEO> roleEOList = userEO.getRoleEOList();
        boolean adminFlag = false;
        for (RoleEO roleEO : roleEOList){
            if (StringUtils.equals(roleEO.getName(), Role.SUPER_ADMIN)){
                adminFlag = true;
            }
        }
        if(StringUtils.equals(originUserId,destUserId)){
            scheduleDetailEOs = scheduleDetailEOService.selectByParentId(id, false);
        } else {
                boolean visible = false;
                List<ScheudlePermissionEO> scheudlePermissionEOList = scheudlePermissionEODao.queryByConfigType("0");
                StringBuilder permissionMapSB = new StringBuilder();
                if(CollectionUtils.isNotEmpty(scheudlePermissionEOList)){
                    for (ScheudlePermissionEO scheudlePermissionEO : scheudlePermissionEOList){
                        if (StringUtils.equals(scheudlePermissionEO.getOriginUserId(),originUserId)){
                            permissionMapSB.append(scheudlePermissionEO.getDestUserMap());
                        }
                        if (StringUtils.contains(scheudlePermissionEO.getMaintenancePersonMap(),originUserId)){
                            permissionMapSB.append(scheudlePermissionEO.getOriginUserId());
                        }
                    }
                }
                permissionMapSB.append(originUserId);
                //如果没权限
                if (permissionMapSB.indexOf(scheduleHourDetailVO.getUserId())>-1){
                    visible = true;
                }

            scheduleDetailEOs = scheduleDetailEOService.selectByParentId(id, false);
            if (!adminFlag&&!visible) {
                for (ScheduleDetailEO scheduleDetailEO : scheduleDetailEOs) {
                    scheduleDetailEO.setScheduleDetail("已有安排");
                }
            }
        }
        //scheduleHourDetailVO.setScheduleDateHour(scheduleHourEO.getScheduleDate(),scheduleHourEO.getScheduleHour());
        if (null == scheduleHourDetailVO){
            return  new ScheduleHourDetailVO();
        }
        Collections.sort(scheduleDetailEOs);
        scheduleHourDetailVO.setScheduleDetailEOs(scheduleDetailEOs);

        StringBuilder maintenancePersonSB = new StringBuilder();
            List<ScheudlePermissionEO> scheudlePermissionEOList = scheudlePermissionEODao.queryByConfigType("0");
            if(CollectionUtils.isNotEmpty(scheudlePermissionEOList)){
                for (ScheudlePermissionEO scheudlePermissionEO : scheudlePermissionEOList){
                    if (StringUtils.contains(scheudlePermissionEO.getMaintenancePersonMap(),originUserId)){
                        maintenancePersonSB.append(scheudlePermissionEO.getOriginUserId());
                    }
                }
            }

        maintenancePersonSB.append(scheduleHourDetailVO.getUserId());
        if (!adminFlag
                && !StringUtils.equals(originUserId,scheduleHourDetailVO.getUpdateUserId())
                && maintenancePersonSB.indexOf(scheduleHourDetailVO.getUserId()) > -1){
            List<String> scheduleDetailIdList = new ArrayList<>();
            if(CollectionUtils.isNotEmpty(scheduleHourDetailVO.getScheduleDetailEOs())){
                for (ScheduleDetailEO tepScheduleDetailEO : scheduleHourDetailVO.getScheduleDetailEOs()) {
                        scheduleDetailIdList.add(tepScheduleDetailEO.getId());
                }
            }
            if (CollectionUtils.isNotEmpty(scheduleDetailIdList)) {
                scheduleDetailEODao.removeUpdateFiledByPrimaryKeyList(scheduleDetailIdList);
            }
            if (StringUtils.isNotEmpty(scheduleHourDetailVO.getId())) {
                scheduleHourEODao.resetUpdateFlagByIds(Arrays.asList(scheduleHourDetailVO.getId()));
            }
        }

        return scheduleHourDetailVO;
    }
    public ScheduleHourDetailVO findOther(String originUserId,String destUserId,Date scheduleDate,String flag)  throws Exception {
        ScheduleHourDetailVO scheduleHourDetailVO =null ;
          //根据originUserId、日程时间和上午下午标识找到ID
        //设置查询条件
        ScheduleHourReqVO scheduleHourReqVO = new ScheduleHourReqVO();
        scheduleHourReqVO.setUserId(destUserId);
        scheduleHourReqVO.setScheduleHour(flag);
        scheduleHourReqVO.setScheduleDateStart(getDayBegin(scheduleDate));
        scheduleHourReqVO.setScheduleDateEnd(getDayEnd(scheduleDate));
        //开始查询
        List<ScheduleHourResVO> scheduleHourResVOS = scheduleHourDao.queryByList(scheduleHourReqVO);

        if(CollectionUtils.isNotEmpty(scheduleHourResVOS)){
            scheduleHourDetailVO =  find(scheduleHourResVOS.get(0).getId(),originUserId,destUserId);
        }

        return scheduleHourDetailVO;
    }


    //获取当天的开始时间
    public static Date getDayBegin(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
    //获取当天的结束时间
    public static Date getDayEnd(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        return cal.getTime();
    }

    public ScheduleHourDetailVO find(String id, boolean filter)  throws Exception {
        ScheduleHourEO scheduleHourEO = scheduleHourEOService.selectByPrimaryKey(id);
        ScheduleHourDetailVO scheduleHourDetailVO = beanMapper.map(scheduleHourEO, ScheduleHourDetailVO.class);
        List<ScheduleDetailEO> scheduleDetailEOs = scheduleDetailEOService.selectByParentId(id, filter);
        scheduleHourDetailVO.setScheduleDetailEOs(scheduleDetailEOs);
        return scheduleHourDetailVO;
    }

    public static void main(String[] args) throws ParseException {
        String timeLimit = "08:00 - 11:00";
        if (!StringUtil.isEmpty(timeLimit)){
            String beginDate = "2020-08-01 " + timeLimit.substring(0,timeLimit.indexOf(" "));
            Date data = new SimpleDateFormat("yyyy-MM-dd hh:mm").parse(beginDate);

            System.out.println(data);
            System.out.println(timeLimit.substring(0,timeLimit.indexOf(" ")));
        }
    }
}
