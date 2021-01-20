package com.adc.da.crm.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.crm.entity.InvoiceInfoEO;
import org.apache.ibatis.annotations.Param;

/**
 *
 * <br>
 * <b>功能：</b>INVOICE_INFO InvoiceInfoEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2018-12-04 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface InvoiceInfoEODao extends BaseDao<InvoiceInfoEO> {

    int updateByContractIdAndDelFlag(@Param("contractId") String contractId, @Param("delFlag") int delFlag);

    int deleteByContractId(@Param("contract_id") String id, @Param("type") String type);
}
