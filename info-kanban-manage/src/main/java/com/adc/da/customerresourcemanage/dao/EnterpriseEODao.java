package com.adc.da.customerresourcemanage.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.customerresourcemanage.entity.EnterpriseEO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * <br>
 * <b>功能：</b>DB_ENTERPRISE EnterpriseEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-04-01 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface EnterpriseEODao extends BaseDao<EnterpriseEO> {

    int logicDeleteByPrimaryKey(@Param("id") String id);

    int batchLogicDeleteByPrimaryKeys(@Param("ids") List<String> ids);

    EnterpriseEO findEnterpriseByName(@Param("enterpriseName") String enterpriseName);


    List<EnterpriseEO> queryAll();

}
