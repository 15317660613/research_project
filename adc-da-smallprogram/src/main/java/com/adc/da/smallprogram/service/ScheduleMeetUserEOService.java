package com.adc.da.smallprogram.service;

import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.adc.da.base.service.BaseService;
import com.adc.da.smallprogram.dao.ScheduleMeetUserEODao;
import com.adc.da.smallprogram.entity.ScheduleMeetUserEO;

import java.util.Date;
import java.util.List;

/**
 *
 * <br>
 * <b>功能：</b>TS_SCHEDULE_MEET_USER ScheduleMeetUserEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-11-26 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("scheduleMeetUserEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class ScheduleMeetUserEOService extends BaseService<ScheduleMeetUserEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(ScheduleMeetUserEOService.class);

    @Autowired
    private ScheduleMeetUserEODao dao;

    public ScheduleMeetUserEODao getDao() {
        return dao;
    }

    public int updateByMeetIdAndUserIdSelective(ScheduleMeetUserEO scheduleMeetUserEO){
        boolean finishedFlag = true ;
        if (null!=scheduleMeetUserEO.getStatus() && scheduleMeetUserEO.getStatus()==1){
            scheduleMeetUserEO.setFinishedTime(new Date());
        }
        dao.updateByMeetIdAndUserIdSelective(scheduleMeetUserEO);
        List<ScheduleMeetUserEO> scheduleMeetUserEOList = dao.selectByMeetId(scheduleMeetUserEO.getMeetId());
        for (ScheduleMeetUserEO meetUserEO : scheduleMeetUserEOList){
            if (meetUserEO.getStatus() == 0){
                finishedFlag = false ;
                break;
            }
        }
        if (finishedFlag){
            dao.updateStatusByMeetId(scheduleMeetUserEO.getMeetId());
        }

        return 1;

    }

}
