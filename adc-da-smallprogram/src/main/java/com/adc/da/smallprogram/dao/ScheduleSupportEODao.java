package com.adc.da.smallprogram.dao;

import com.adc.da.base.dao.BaseDao;

import com.adc.da.smallprogram.entity.ScheduleSupportEO;
import com.adc.da.smallprogram.page.ScheduleSupportEOPage;
import com.adc.da.smallprogram.page.SupportPage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * <br>
 * <b>功能：</b>TS_SCHEDULE_SUPPORT ScheduleSupportEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-11-26 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface ScheduleSupportEODao extends BaseDao<ScheduleSupportEO> {

    void logicDelete(String id);

    List<ScheduleSupportEO> queryByPageNew(ScheduleSupportEOPage page);


    List<ScheduleSupportEO> queryByPageWithReceiveUserId(SupportPage page);

    Integer queryCountByPageWithReceiveUserId(SupportPage page);

    ScheduleSupportEO selectById(String id);

    Integer queryCountByPageAll(SupportPage page);

    List<ScheduleSupportEO>  queryByPageAll(SupportPage page);

    ScheduleSupportEO selectBySupportIdAndReceiveUserId(@Param("supportId") String supportId , @Param("receiveUserId")String receiveUserId);

}
