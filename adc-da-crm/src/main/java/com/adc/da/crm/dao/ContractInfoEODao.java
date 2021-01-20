package com.adc.da.crm.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.crm.entity.ContractInfoEO;
import org.apache.ibatis.annotations.Param;

/**
 *
 * <br>
 * <b>功能：</b>CONTRACT_INFO ContractInfoEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2018-12-04 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface ContractInfoEODao extends BaseDao<ContractInfoEO> {

    int updateByContractIdAndDelFlag(@Param("contractId") String contractId, @Param("delFlag") int delFlag);
    int deleteByContractId(String id);
}
