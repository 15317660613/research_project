package com.adc.da.smallprogram.service;

import com.adc.da.base.page.BasePage;
import com.adc.da.smallprogram.dao.ScheduleConfigEODao;
import com.adc.da.smallprogram.dao.ScheduleResearchMarkEODao;
import com.adc.da.smallprogram.dao.ScheduleResearchUserEODao;
import com.adc.da.smallprogram.entity.ScheduleConfigEO;
import com.adc.da.smallprogram.entity.ScheduleResearchMarkEO;
import com.adc.da.smallprogram.entity.ScheduleResearchUserEO;
import com.adc.da.smallprogram.page.MyScheduleResearchEOPage;
import com.adc.da.smallprogram.page.ScheduleConfigEOPage;
import com.adc.da.smallprogram.page.ScheduleResearchEOPage;
import com.adc.da.smallprogram.page.ScheduleResearchUserEOPage;
import com.adc.da.smallprogram.vo.ResearchVO;
import com.adc.da.smallprogram.vo.ScheduleVO;
import com.adc.da.sys.dao.UserEODao;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.StringUtils;
import com.adc.da.util.utils.UUID;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.adc.da.base.service.BaseService;
import com.adc.da.smallprogram.dao.ScheduleResearchEODao;
import com.adc.da.smallprogram.entity.ScheduleResearchEO;

import java.text.SimpleDateFormat;
import java.util.*;


/**
 *
 * <br>
 * <b>功能：</b>TS_SCHEDULE_RESEARCH ScheduleResearchEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-05-26 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("scheduleResearchEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class ScheduleResearchEOService extends BaseService<ScheduleResearchEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(ScheduleResearchEOService.class);

    @Autowired
    private ScheduleConfigEODao scheduleConfigEODao ;
    @Autowired
    private ScheduleResearchEODao scheduleResearchEODao ;
    @Autowired
    private ScheduleResearchUserEODao scheduleResearchUserEODao ;
    @Autowired
    private UserEODao userEODao;
    @Autowired
    private ScheduleResearchMarkEODao scheduleResearchMarkEODao;


    public ScheduleResearchEODao getDao() {
        return scheduleResearchEODao;
    }
    public List<ResearchVO> getResearchVOByPage(MyScheduleResearchEOPage page)throws Exception{
        Integer rowCount = this.getDao().queryByMyCount(page);
        page.getPager().setRowCount(rowCount);
        List<ResearchVO> researchVOList = this.getDao().queryVOByPage(page);
        List<ResearchVO> resResearchVOList = new ArrayList<>();
        List<String> researchIdList = new ArrayList<>();
        for (ResearchVO researchVO : researchVOList){
            if (!researchIdList.contains(researchVO.getId())) {
                researchIdList.add(researchVO.getId());
                resResearchVOList.add(researchVO);
            }
        }
        if (CollectionUtils.isNotEmpty(researchIdList)) {
            List<ScheduleResearchUserEO> scheduleResearchUserEOList = scheduleResearchUserEODao.selectByResearchIdList(researchIdList);
            for (ResearchVO researchVO : resResearchVOList) {
                for (ScheduleResearchUserEO scheduleResearchUserEO : scheduleResearchUserEOList) {
                    if (page.getSearchMyselfFlag()==1 && !StringUtils.equals(scheduleResearchUserEO.getCreateUserId(),page.getUserId()) ){
                        continue;
                    }
                    if (StringUtils.equals(researchVO.getId(), scheduleResearchUserEO.getResearchId()) &&
                            StringUtils.isNotEmpty(scheduleResearchUserEO.getContent())) {
                        if (page.getSearchMyselfFlag()==1){
                            researchVO.setStatus(scheduleResearchUserEO.getStatus());
                        }
                        if (StringUtils.isNotEmpty(researchVO.getContent())) {
                            researchVO.setContent(researchVO.getContent() + "\r\n" + scheduleResearchUserEO.getContent());
                        }else {
                            researchVO.setContent(researchVO.getContent() + scheduleResearchUserEO.getContent());
                        }
                    }
                }
            }
        }
        return resResearchVOList;
    }
    public void doTask()throws Exception{
        List<ScheduleConfigEO> scheduleConfigEOList = scheduleConfigEODao.queryByList(new ScheduleConfigEOPage());
        SimpleDateFormat yMDHMS = new SimpleDateFormat("yyyy-MM-dd");
        String SCHEDULE_RESEARCH_DATE = null;
        for (ScheduleConfigEO scheduleConfigEO : scheduleConfigEOList){
            if (StringUtils.equals(scheduleConfigEO.getConfigName(),"SCHEDULE_RESEARCH_DATE")){
                SCHEDULE_RESEARCH_DATE = scheduleConfigEO.getConfigValueString();
            }
        }
        if (StringUtils.isEmpty(SCHEDULE_RESEARCH_DATE)){
            return;
        }
        Date date = yMDHMS.parse(SCHEDULE_RESEARCH_DATE);
        DateTime confDateTime = new DateTime(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(confDateTime.toDate());
        DateTime nowDateTime = new DateTime();
        int confYear = confDateTime.getYear();
        if (nowDateTime.getYear()!=confYear){
            confYear = nowDateTime.getYear();
        }
        int confMonth = confDateTime.getMonthOfYear();
        if (nowDateTime.getMonthOfYear()!=confMonth){
            confMonth = nowDateTime.getMonthOfYear();
        }
        int confDay= confDateTime.getDayOfMonth();

        calendar.set(confYear,confMonth-1,confDay,8,0);
        ScheduleResearchEOPage page = new ScheduleResearchEOPage();
        page.setYear(String.valueOf(confYear));
        page.setMonth(String.valueOf(confMonth));
        ScheduleResearchEO scheduleResearchEO = this.queryBySingle(page);
        //如果当月不存在，那么插角色用户创建所有
        List<String> roleCodeList= new ArrayList<>();
//        roleCodeList.add("RS_ADMIN");
        roleCodeList.add("RS_MEMBER");
        roleCodeList.add("RS_LEADER");
        roleCodeList.add("SCHEDULE_RESEARCH");
        roleCodeList.add("SCHEDULE_RESEARCH_MEMBER");
        List<UserEO> userEOList = userEODao.getAllUserEOsByRoleCode(roleCodeList);
        if (null == scheduleResearchEO && confDay <= nowDateTime.getDayOfMonth() ){
            scheduleResearchEO = new ScheduleResearchEO();
            scheduleResearchEO.setYear(Long.valueOf(confYear));
            scheduleResearchEO.setMonth(confMonth);
            scheduleResearchEO.setCreateTime(calendar.getTime());
            scheduleResearchEO.setUpdateTime(calendar.getTime());
            String title = confYear+"年"+confMonth+"月工作要点";
            scheduleResearchEO.setTitle(title);
            scheduleResearchEO.setStatus(0);
            scheduleResearchEO.setDelFlag(0);
            scheduleResearchEO.setId(UUID.randomUUID10());
            List<ScheduleResearchUserEO> scheduleResearchUserEOList = new ArrayList<>();
            List<ScheduleResearchMarkEO> scheduleResearchMarkEOList = new ArrayList<>();
            for (UserEO userEO : userEOList){
                ScheduleResearchUserEO researchUserEO = new ScheduleResearchUserEO();
                researchUserEO.setId(UUID.randomUUID10());
                researchUserEO.setCreateTime(calendar.getTime());
                //researchUserEO.setUpdateTime(calendar.getTime());
                researchUserEO.setResearchId(scheduleResearchEO.getId());
                researchUserEO.setCreateUserId(userEO.getUsid());
                researchUserEO.setCreateUserName(userEO.getUsname());
                researchUserEO.setStatus(0);
                scheduleResearchUserEOList.add(researchUserEO);

                ScheduleResearchMarkEO scheduleResearchMarkEO = new ScheduleResearchMarkEO();
                scheduleResearchMarkEO.setId(UUID.randomUUID10());
                scheduleResearchMarkEO.setUserId(userEO.getUsid());
                scheduleResearchMarkEO.setUserName(userEO.getUsname());
                scheduleResearchMarkEO.setResearchId(researchUserEO.getResearchId());
                scheduleResearchMarkEO.setCollect(0);
                scheduleResearchMarkEO.setTop(0);
                scheduleResearchMarkEOList.add(scheduleResearchMarkEO);
            }
            scheduleResearchEODao.insertSelective(scheduleResearchEO);
            if(CollectionUtils.isNotEmpty(scheduleResearchUserEOList)) {
                scheduleResearchUserEODao.insertList(scheduleResearchUserEOList);
            }
            if(CollectionUtils.isNotEmpty(scheduleResearchMarkEOList)) {
                scheduleResearchMarkEODao.insertList(scheduleResearchMarkEOList);
            }
        }else if(null != scheduleResearchEO &&scheduleResearchEO.getStatus()!=1) {
            ScheduleResearchUserEOPage scheduleResearchUserEOPage = new ScheduleResearchUserEOPage();
            scheduleResearchUserEOPage.setResearchId(scheduleResearchEO.getId());
            List<ScheduleResearchUserEO> scheduleResearchUserEOList = scheduleResearchUserEODao.queryByList(scheduleResearchUserEOPage);
            for (UserEO userEO : userEOList){
                boolean createFlag = false;
                for (ScheduleResearchUserEO scheduleResearchUserEO : scheduleResearchUserEOList) {
                    if (StringUtils.equals(userEO.getUsid(), scheduleResearchUserEO.getCreateUserId())) {
                        createFlag = true;
                    }
                }
                if (!createFlag) {
                    ScheduleResearchUserEO researchUserEO = new ScheduleResearchUserEO();
                    researchUserEO.setId(UUID.randomUUID10());
                    researchUserEO.setCreateTime(calendar.getTime());
                    //researchUserEO.setUpdateTime(calendar.getTime());
                    researchUserEO.setResearchId(scheduleResearchEO.getId());
                    researchUserEO.setCreateUserId(userEO.getUsid());
                    researchUserEO.setCreateUserName(userEO.getUsname());
                    researchUserEO.setStatus(0);
                    scheduleResearchUserEODao.insertSelective(researchUserEO);
                    ScheduleResearchMarkEO scheduleResearchMarkEO = new ScheduleResearchMarkEO();
                    scheduleResearchMarkEO.setId(UUID.randomUUID10());
                    scheduleResearchMarkEO.setUserId(userEO.getUsid());
                    scheduleResearchMarkEO.setUserName(userEO.getUsname());
                    scheduleResearchMarkEO.setResearchId(researchUserEO.getResearchId());
                    scheduleResearchMarkEO.setCollect(0);
                    scheduleResearchMarkEO.setTop(0);
                    scheduleResearchMarkEODao.insertSelective(scheduleResearchMarkEO);
                }
            }
        }
    }

}
