package com.adc.da.smallprogram.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.smallprogram.entity.ScheduleDetailEO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * <br>
 * <b>功能：</b>TS_SCHEDULE_DETAIL ScheduleDetailEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-11-19 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface ScheduleDetailEODao extends BaseDao<ScheduleDetailEO> {

    void logicDelete(String id);

    void logicDeleteInBatch(List<String> ids);

    List<ScheduleDetailEO> selectByParentId(@Param("id") String id, @Param("detailType") int detailType);

    List<ScheduleDetailEO> selectByParentIdNew(@Param("id") String id, @Param("filter") boolean filter);

    List<ScheduleDetailEO> selectByParentIdList(@Param("list") List<String> list);

    void removeUpdateFiledByPrimaryKeyList(@Param("list") List<String> list);

}
