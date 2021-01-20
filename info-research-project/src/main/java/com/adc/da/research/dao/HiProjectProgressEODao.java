package com.adc.da.research.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.research.entity.HiProjectProgressEO;

import java.util.List;

/**
 * <br>
 * <b>功能：</b>RS_HI_PROJECT_PROGRESS HiProjectProgressEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-10-30 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface HiProjectProgressEODao extends BaseDao<HiProjectProgressEO> {
    /**
     * 批量插入
     *
     * @param list
     * @return
     */
    int insertSelectiveAll(List<HiProjectProgressEO> list);

}
