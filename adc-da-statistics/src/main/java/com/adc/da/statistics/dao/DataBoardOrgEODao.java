package com.adc.da.statistics.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.statistics.entity.DataBoardOrgEO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * <br>
 * <b>功能：</b>ST_DATA_BOARD_ORG DataBoardOrgEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-11-15 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface DataBoardOrgEODao extends BaseDao<DataBoardOrgEO> {

    /**
     * 批量插入
     *
     * @param list
     * @return
     */
    int insertSelectiveAll(@Param("list") List<DataBoardOrgEO> list);

    /**
     * @param year
     * @param deptId
     * @return
     */
    List<DataBoardOrgEO> querySumGroupByMonth(@Param("year") Integer year, @Param("deptId") String deptId);

    /**
     * PF_PROJECT_CONTRACT_INVOICE
     * 开票
     **/
    Float queryInvoice(@Param("deptId") String deptId,
        @Param("startTime") Date startTime,
        @Param("finishTime") Date finishTime);

    /**
     * 根据部门id查询开票
     *
     * @param deptId
     * @param startTime
     * @param finishTime
     * @return
     */
    List<DataBoardOrgEO> queryBillingGroupByMonth(@Param("deptId") String deptId,
        @Param("startTime") Date startTime,
        @Param("finishTime") Date finishTime);

    /**
     * 根据业务查询开票
     *
     * @param budgetId
     * @param startTime
     * @param finishTime
     * @return
     */
    List<DataBoardOrgEO> queryInvoiceGroupByMonthBudget(@Param("budgetId") String budgetId,
        @Param("startTime") Date startTime,
        @Param("finishTime") Date finishTime);

}
