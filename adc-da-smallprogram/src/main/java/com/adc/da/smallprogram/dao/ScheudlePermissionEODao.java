package com.adc.da.smallprogram.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.smallprogram.entity.ScheudlePermissionEO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * <br>
 * <b>功能：</b>TS_SCHEUDLE_PERMISSION ScheudlePermissionEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-11-20 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface ScheudlePermissionEODao extends BaseDao<ScheudlePermissionEO> {

     Integer selectByOriginIdAndDestUserIdLike(@Param("originId") String originId, @Param("destUserId") String destUserId);

     List<ScheudlePermissionEO>  queryByUserIdLikeMaintenancePersonMap(@Param("value") String value);
     List<ScheudlePermissionEO>  queryByOriginUserIdAndConfigType(@Param("originUserId") String originUserId,@Param("configType") String configType);
     List<ScheudlePermissionEO>  queryByConfigType(@Param("configType") String configType);

    List<ScheudlePermissionEO> queryByUserIdLikeMaintenancePersonMapAndConfigType(@Param("usid")String usid, @Param("configType")String configType);
}
