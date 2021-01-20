package com.adc.da.research.project.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.research.project.entity.RContractInfoEO;
import org.apache.ibatis.annotations.Param;

/**
 *
 * <br>
 * <b>功能：</b>RS_CONTRACT_INFO RContractInfoEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-11-02 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface RContractInfoEODao extends BaseDao<RContractInfoEO> {

    int updateRContractInfoEOByProjectId(RContractInfoEO rContractInfoEO);

    void deleteByProjectId(@Param("projectId")String projectId);
}
