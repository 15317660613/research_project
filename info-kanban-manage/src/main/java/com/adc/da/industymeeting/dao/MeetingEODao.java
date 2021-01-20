package com.adc.da.industymeeting.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.industymeeting.entity.MeetingEO;
import org.apache.ibatis.annotations.Param;

/**
 *
 * <br>
 * <b>功能：</b>INDUSTY_MEETING MeetingEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-03-30 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface MeetingEODao extends BaseDao<MeetingEO> {

    int logicDeleteByPrimaryKey(@Param("id") String id);

}
