package com.adc.da.budget.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.base.page.BasePage;
import com.adc.da.budget.dto.ProcessInstanceIdUpdateDTO;
import com.adc.da.budget.entity.BuinessComfirmContractEO;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;

/**
 *
 * <br>
 * <b>功能：</b>BUINESS_COMFIRM_CONTRACT BuinessComfirmContractEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2018-11-08 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface BuinessComfirmContractEODao extends BaseDao<BuinessComfirmContractEO> {
    /**
     * 批量删除
     * @param list
     * @return
     */
    public int deleteInBatch(List<String> list);
    void batchInsert(BuinessComfirmContractEO eo);
    List<BuinessComfirmContractEO> getData();
    List<BuinessComfirmContractEO> queryByList(BasePage var1);
    void updateprocessInstanceIdByIdList(@Param("processInstanceIdUpdateDTO") ProcessInstanceIdUpdateDTO processInstanceIdUpdateDTO);

}
