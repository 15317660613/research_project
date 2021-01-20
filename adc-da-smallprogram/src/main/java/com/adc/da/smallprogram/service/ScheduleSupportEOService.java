package com.adc.da.smallprogram.service;

import com.adc.da.base.page.BasePage;
import com.adc.da.smallprogram.dao.ScheduleSupportUserEODao;
import com.adc.da.smallprogram.entity.ScheduleDetailEO;
import com.adc.da.smallprogram.entity.ScheduleHourEO;
import com.adc.da.smallprogram.entity.ScheduleSupportUserEO;
import com.adc.da.smallprogram.page.ScheduleSupportEOPage;
import com.adc.da.smallprogram.page.SupportPage;
import com.adc.da.smallprogram.vo.ScheduleHourDetailVO;
import com.adc.da.smallprogram.vo.SupportVO;
import com.adc.da.util.constant.DeleteFlagEnum;
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
import com.adc.da.smallprogram.dao.ScheduleSupportEODao;
import com.adc.da.smallprogram.entity.ScheduleSupportEO;

import java.text.SimpleDateFormat;
import java.util.*;

import static com.adc.da.smallprogram.service.ScheduleHourService.getDayEndTime;
import static com.adc.da.smallprogram.service.ScheduleHourService.getDayStartTime;


/**
 *
 * <br>
 * <b>功能：</b>TS_SCHEDULE_SUPPORT ScheduleSupportEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-11-26 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("scheduleSupportEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class ScheduleSupportEOService extends BaseService<ScheduleSupportEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(ScheduleSupportEOService.class);
    private static  final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    @Autowired
    private ScheduleSupportEODao scheduleSupportEODao;
    @Autowired
    private ScheduleSupportUserEODao scheduleSupportUserEODao ;

    public ScheduleSupportEODao getDao() {
        return scheduleSupportEODao;
    }

    public ScheduleSupportEO save(ScheduleSupportEO scheduleSupportEO) throws  Exception{
        scheduleSupportEO.setId(UUID.randomUUID10());
        scheduleSupportEO.setDelFlag(DeleteFlagEnum.NORMAL.getValue());
        scheduleSupportEO.setCreateTime(new Date());
        if (StringUtils.isNotEmpty(scheduleSupportEO.getDateSection())){
            String dateDay = scheduleSupportEO.getDateSection().split(" ")[0];
            String[] hourSection = scheduleSupportEO.getDateSection().split(" ")[1].split("-");
            Date beginDate = dateFormat.parse(dateDay+" "+hourSection[0]);
            Date endDate = dateFormat.parse(dateDay+" "+hourSection[1]);
            scheduleSupportEO.setBeginTime(beginDate);
            scheduleSupportEO.setEndTime(endDate);
        }
        ScheduleSupportUserEO scheduleSupportUserEO = new ScheduleSupportUserEO();
        scheduleSupportUserEO.setId(UUID.randomUUID10());
        scheduleSupportUserEO.setSupportId(scheduleSupportEO.getId());
        scheduleSupportUserEO.setReceiveTime(new Date());
        scheduleSupportUserEO.setReceiveUserId(scheduleSupportEO.getReceiveUserId());
        scheduleSupportUserEO.setReceiveUserName(scheduleSupportEO.getReceiveUserName());
        scheduleSupportUserEODao.insertSelective(scheduleSupportUserEO);
        scheduleSupportEODao.insertSelective(scheduleSupportEO);
        return scheduleSupportEO;
    }

    public void logicDelete(String id) {
        scheduleSupportEODao.logicDelete(id);
    }

    public List<ScheduleSupportEO> queryByPageNew(ScheduleSupportEOPage page) throws Exception {
        page.setDelFlag(String.valueOf(DeleteFlagEnum.NORMAL.getValue()));
        Integer rowCount = this.queryByCount(page);
        page.getPager().setRowCount(rowCount);
        return this.getDao().queryByPageNew(page);
    }


    public void saveSupportVO(SupportVO supportVO){
        ScheduleSupportEO scheduleSupportEO = supportVO.getScheduleSupportEO();
        scheduleSupportEO.setId(UUID.randomUUID10());
        scheduleSupportEO.setCreateTime(new Date());
        scheduleSupportEODao.insertSelective(scheduleSupportEO);
        if (CollectionUtils.isNotEmpty(supportVO.getScheduleSupportUserEOList())){
            supportVO.setScheduleSupportEO(scheduleSupportEO);
            send(supportVO);
        }
    }

    public SupportVO send(SupportVO supportVO){

        ScheduleSupportEO scheduleSupportEO = supportVO.getScheduleSupportEO();
        List<ScheduleSupportUserEO> scheduleSupportUserEOList = supportVO.getScheduleSupportUserEOList();
        if(CollectionUtils.isNotEmpty(scheduleSupportUserEOList)){
            for (ScheduleSupportUserEO scheduleSupportUserEO : scheduleSupportUserEOList){
                scheduleSupportUserEO.setId(UUID.randomUUID10());
                scheduleSupportUserEO.setSupportId(scheduleSupportEO.getId());
            }
            scheduleSupportUserEODao.insertList(scheduleSupportUserEOList);
        }
        supportVO.setScheduleSupportUserEOList(scheduleSupportUserEOList);
        return supportVO;
    }


    public List<ScheduleSupportEO> queryByPageWithReceiveUserId(SupportPage page){
        int count = scheduleSupportEODao.queryCountByPageWithReceiveUserId(page);
        page.getPager().setRowCount(count);
        return scheduleSupportEODao.queryByPageWithReceiveUserId(page);

    }

    public List<ScheduleSupportEO>  queryByPageAll(SupportPage page){
        int count = scheduleSupportEODao.queryCountByPageAll(page);
        page.getPager().setRowCount(count);
        return scheduleSupportEODao.queryByPageAll(page);
    }


    public ScheduleSupportEO selectBySupportIdAndReceiveUserId(String supportId ,String receiveUserId){

        return scheduleSupportEODao.selectBySupportIdAndReceiveUserId(supportId,receiveUserId);
    }




     public static void main(String[] args) throws Exception{
        System.out.println(dateFormat.parse("2019-11-28 8:00"));
    }
}
