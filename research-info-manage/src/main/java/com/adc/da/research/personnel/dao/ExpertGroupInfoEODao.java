package com.adc.da.research.personnel.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.research.personnel.entity.ExpertGroupInfoEO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * <br>
 * <b>功能：</b>RS_EXPERT_GROUP_INFO ExpertGroupInfoEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-10-22 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface ExpertGroupInfoEODao extends BaseDao<ExpertGroupInfoEO> {

    List<ExpertGroupInfoEO> queryByIds(@Param("ids")String[] ids);
    void deleteByIds(@Param("ids")String[] ids);

    List<String> getBindExpertGroupList(@Param("id")String id);

    List<ExpertGroupInfoEO> getExpertGroupList(@Param("id")String id);

}
