package com.adc.da.research.config.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.research.config.entity.WarnNoticeEO;
import org.apache.ibatis.annotations.Param;

/**
 *
 * <br>
 * <b>功能：</b>RS_WARN_NOTICE WarnNoticeEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-10-29 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface WarnNoticeEODao extends BaseDao<WarnNoticeEO> {

    void deleteByIds(@Param("ids")String[] ids);

}
