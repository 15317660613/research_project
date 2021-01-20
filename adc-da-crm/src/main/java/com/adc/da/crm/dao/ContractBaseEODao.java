package com.adc.da.crm.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.base.page.BasePage;
import com.adc.da.crm.entity.ContractBaseEO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * <br>
 * <b>功能：</b>CONTRACT_BASE ContractBaseEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2018-12-04 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface ContractBaseEODao extends BaseDao<ContractBaseEO> {

    int updateByPrimaryKeyAndDelFlag(@Param("primaryKey") String primaryKey, @Param("delFlag") int delFlag);

    List<ContractBaseEO> queryCRMListByPage(BasePage page);

}
