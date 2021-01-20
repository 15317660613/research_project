package com.adc.da.crm.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.base.page.BasePage;
import com.adc.da.crm.entity.InvoiceApprovalEO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * <br>
 * <b>功能：</b>INVOICE_APPROVAL InvoiceApprovalEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2018-12-04 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface InvoiceApprovalEODao extends BaseDao<InvoiceApprovalEO> {

    int updateByPrimaryKeyAndDelFlag(@Param("primaryKey") String primaryKey, @Param("delFlag") int delFlag);

    List<InvoiceApprovalEO> queryCRMListByPage(BasePage page);
}
