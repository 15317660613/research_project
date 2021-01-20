package com.adc.da.crm.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.base.page.BasePage;
import com.adc.da.crm.entity.CustomerRecordEO;
import com.adc.da.crm.entity.IdNameInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 *
 * <br>
 * <b>功能：</b>CUSTOMER_RECORD CustomerRecordEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2018-11-28 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface CustomerRecordEODao extends BaseDao<CustomerRecordEO> {

    int updateByPrimaryKeyAndDelFlag(@Param("primaryKey")String primaryKey, @Param("delFlag")int delFlag);

    List<CustomerRecordEO> queryCRMListByPage(BasePage page);

    List<IdNameInfo> getUserByName(@Param("names") Set<String> names);

    List<IdNameInfo> getDeptByName(@Param("names") Set<String> names);
}
