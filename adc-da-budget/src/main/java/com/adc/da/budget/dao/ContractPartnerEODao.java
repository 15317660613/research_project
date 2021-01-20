package com.adc.da.budget.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.budget.entity.ContractPartnerEO;

import java.util.List;

/**
 *
 * <br>
 * <b>功能：</b>DB_CONTRACT_PARTNER ContractPartnerEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-07-06 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface ContractPartnerEODao extends BaseDao<ContractPartnerEO> {
    List<String> queryAllPartnerName();
}
