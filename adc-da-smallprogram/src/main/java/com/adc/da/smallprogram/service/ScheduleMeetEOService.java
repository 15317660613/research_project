package com.adc.da.smallprogram.service;

import com.adc.da.smallprogram.dao.ScheduleMeetUserEODao;
import com.adc.da.smallprogram.entity.ScheduleMeetUserEO;
import com.adc.da.smallprogram.page.MeetPage;
import com.adc.da.smallprogram.vo.MeetVO;
import com.adc.da.sys.dao.UserEODao;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.sys.service.UserEOService;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.StringUtils;
import com.adc.da.util.utils.UUID;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.adc.da.base.service.BaseService;
import com.adc.da.smallprogram.dao.ScheduleMeetEODao;
import com.adc.da.smallprogram.entity.ScheduleMeetEO;

import java.util.*;

/**
 *
 * <br>
 * <b>功能：</b>TS_SCHEDULE_MEET ScheduleMeetEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-11-26 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("scheduleMeetEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class ScheduleMeetEOService extends BaseService<ScheduleMeetEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(ScheduleMeetEOService.class);

    @Autowired
    private ScheduleMeetEODao scheduleMeetEODao;
    @Autowired
    private ScheduleMeetUserEODao scheduleMeetUserEODao ;

    @Autowired
    private UserEODao userEODao;

    public ScheduleMeetEODao getDao() {
        return scheduleMeetEODao;
    }

    public void saveMeetVO(MeetVO meetVO){
        ScheduleMeetEO scheduleMeetEO = meetVO.getScheduleMeetEO();
        scheduleMeetEO.setId(UUID.randomUUID10());
        scheduleMeetEO.setCreateTime(new Date());
        scheduleMeetEODao.insertSelective(scheduleMeetEO);
        if (CollectionUtils.isNotEmpty(meetVO.getScheduleMeetUserEOList())){
            meetVO.setScheduleMeetEO(scheduleMeetEO);
            send(meetVO);
        }
    }

    public MeetVO send(MeetVO meetVO){
        List<String> roleCodeList = new ArrayList<>();
        roleCodeList.add("SCHEDULE_SENIOR_LEADER");
        HashSet<String> set = new HashSet<>();

        List<UserEO> userEOList = userEODao.getAllUserEOsByRoleCode(roleCodeList);



        ScheduleMeetEO scheduleMeetEO = meetVO.getScheduleMeetEO();
        List<ScheduleMeetUserEO> scheduleMeetUserEOList = meetVO.getScheduleMeetUserEOList();
        if(CollectionUtils.isNotEmpty(scheduleMeetUserEOList)){
            for (ScheduleMeetUserEO scheduleMeetUserEO : scheduleMeetUserEOList){
                set.add(scheduleMeetUserEO.getReceiveUserId());
                scheduleMeetUserEO.setId(UUID.randomUUID10());
                scheduleMeetUserEO.setMeetId(scheduleMeetEO.getId());
            }
            for (UserEO userEO : userEOList){
                if (set.add(userEO.getUsid())) {
                    ScheduleMeetUserEO scheduleMeetUserEO = new ScheduleMeetUserEO();
                    scheduleMeetUserEO.setId(UUID.randomUUID10());
                    scheduleMeetUserEO.setMeetId(scheduleMeetEO.getId());
                    scheduleMeetUserEO.setStatus(-1);
                    scheduleMeetUserEO.setReceiveUserId(userEO.getUsid());
                    scheduleMeetUserEO.setReceiveUserName(userEO.getUsname());
                    scheduleMeetUserEOList.add(scheduleMeetUserEO);
                }
            }
            scheduleMeetUserEODao.insertList(scheduleMeetUserEOList);
        }
        meetVO.setScheduleMeetUserEOList(scheduleMeetUserEOList);
    return meetVO;
    }

    public List<ScheduleMeetEO> selectByReceiveUserId(String receiveUserId){
        return scheduleMeetEODao.selectByReceiveUserId(receiveUserId);
    }


    public List<ScheduleMeetEO> queryByPageWithReceiveUserId(MeetPage page){
        int count = scheduleMeetEODao.queryCountByPageWithReceiveUserId(page);
        page.getPager().setRowCount(count);
        return scheduleMeetEODao.queryByPageWithReceiveUserId(page);

    }

    public List<ScheduleMeetEO>  queryByPageAll(MeetPage page){
        int count = scheduleMeetEODao.queryCountByPageAll(page);
        page.getPager().setRowCount(count);
        return scheduleMeetEODao.queryByPageAll(page);
    }



    public List<ScheduleMeetEO>  queryByPageAdmin(MeetPage page){
        int count = scheduleMeetEODao.queryCountByPageAdmin(page);
        page.getPager().setRowCount(count);
        return scheduleMeetEODao.queryByPageAdmin(page);
    }




    public MeetVO selectById(String id){
        ScheduleMeetEO scheduleMeetEO ;
        MeetVO meetVO = new MeetVO();
        scheduleMeetEO = scheduleMeetEODao.selectById(id);
        List<ScheduleMeetUserEO> scheduleMeetUserEOList = scheduleMeetUserEODao.selectByMeetId(id);
        meetVO.setScheduleMeetUserEOList(scheduleMeetUserEOList);
        meetVO.setScheduleMeetEO(scheduleMeetEO);
        return meetVO ;
    }


    public MeetVO selectById(String id,String userId){
        ScheduleMeetEO scheduleMeetEO ;
        ScheduleMeetUserEO scheduleMeetUserEO =   scheduleMeetUserEODao.selectByMeetIdAndUserId(id,userId);
        MeetVO meetVO = new MeetVO();
        scheduleMeetEO = scheduleMeetEODao.selectById(scheduleMeetUserEO.getMeetId());
        scheduleMeetEO.setStatus(scheduleMeetUserEO.getStatus());
        scheduleMeetEO.setExtInfo1(scheduleMeetUserEO.getTop().toString());
        scheduleMeetEO.setExtInfo2(scheduleMeetUserEO.getCollected().toString());
        List<ScheduleMeetUserEO> scheduleMeetUserEOList = scheduleMeetUserEODao.selectByMeetId(id);
        meetVO.setScheduleMeetUserEOList(scheduleMeetUserEOList);
        meetVO.setScheduleMeetEO(scheduleMeetEO);
        return meetVO ;
    }

    public void softDelete(String meetId){
        scheduleMeetEODao.deleteByPrimaryKey(meetId);
        scheduleMeetUserEODao.deleteByMeetId(meetId);
    }


}
