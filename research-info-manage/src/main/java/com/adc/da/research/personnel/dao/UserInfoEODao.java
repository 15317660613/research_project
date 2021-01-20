package com.adc.da.research.personnel.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.research.personnel.entity.UserInfoEO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * <br>
 * <b>功能：</b>RS_USER_INFO UserInfoEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-10-22 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface UserInfoEODao extends BaseDao<UserInfoEO> {


    /**
     * 逻辑删除
     * @param ids
     * @return
     */
    int logicDelete(@Param("ids") List<String> ids);

}
