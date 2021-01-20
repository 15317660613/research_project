package com.adc.da.budget.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.budget.dto.ProcessInstanceIdUpdateDTO;
import com.adc.da.budget.entity.BuinessPerfonmanceContractEO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * <br>
 * <b>功能：</b>BUINESS_PERFONMANCE_CONTRACT BuinessPerfonmanceContractEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2018-11-08 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface BuinessPerfonmanceContractEODao extends BaseDao<BuinessPerfonmanceContractEO> {
    /**
     * 批量删除
     * @param list id 串
     * @return
     */
    public int deleteInBatch(List<String> list);
    void batchInsert(BuinessPerfonmanceContractEO eo);
    List<BuinessPerfonmanceContractEO> getData();
    void updateprocessInstanceIdByIdList(@Param("processInstanceIdUpdateDTO") ProcessInstanceIdUpdateDTO processInstanceIdUpdateDTO);

}
