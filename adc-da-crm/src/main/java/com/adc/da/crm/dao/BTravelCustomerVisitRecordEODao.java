package com.adc.da.crm.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.crm.entity.BTravelCustomerVisitRecordEO;
import org.apache.ibatis.annotations.Param;

/**
 *
 * <br>
 * <b>功能：</b>B_TRAVEL_CUSTOMER_VISIT_RECORD BTravelCustomerVisitRecordEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2018-11-28 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface BTravelCustomerVisitRecordEODao extends BaseDao<BTravelCustomerVisitRecordEO> {

    int deleteByTravelApprovalId(String id);
    int updateByTravelApprovalIdAndDelFlag(@Param("travelApprovalId") String travelApprovalId, @Param("delFlag") int delFlag);
}
