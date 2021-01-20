package com.adc.da.statistics.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.statistics.entity.ContractAmountEO;
import com.adc.da.statistics.entity.DataBoardTreeEO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.Date;
import java.util.List;

/**
 * <br>
 * <b>功能：</b>ST_CONTRACT_AMOUNT ContractAmountEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-11-18 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface ContractAmountEODao extends BaseDao<ContractAmountEO> {

    /**
     * 清空合同表金额
     *
     * @return
     */
    @Update("TRUNCATE TABLE ST_CONTRACT_AMOUNT ")
    int deleteAll();

    /**
     * 批量插入
     *
     * @param list
     * @return
     */
    int insertSelectiveAll(@Param("list") List<ContractAmountEO> list);

    /**
     * 统计经营类项目个数
     *
     * @return
     */
    ContractAmountEO countProjectByDeptId(@Param("deptId") String deptId,
        @Param("startTime") Date startTime,
        @Param("finishTime") Date finishTime);

    /**
     * 根据业务id统计
     *
     * @param budgetId
     * @return
     */
    List<ContractAmountEO> countProjectByBudgetId(@Param("budgetId") String budgetId,
        @Param("startTime") Date startTime,
        @Param("finishTime") Date finishTime);

    /**
     * 合同数据详情
     * @param budgetId
     * @param startTime
     * @param finishTime
     * @return
     */
    List<ContractAmountEO> queryProjectByBudgetId(@Param("budgetId") String budgetId,
        @Param("startTime") Date startTime,
        @Param("finishTime") Date finishTime);
    /**
     * 获取 业务维度 树列表
     *
     * @param pageSize
     * @param startTime
     * @param finishTime
     * @param type       0 表示查询开票 ，1表示查询合同金额
     * @return
     */
    List<DataBoardTreeEO> getBudgetTree(@Param("pageSize") Integer pageSize, @Param("startTime") Date startTime,
        @Param("finishTime") Date finishTime, @Param("type") Integer type);

    /**
     * 首页饼图
     *
     * @param startTime
     * @param finishTime
     * @param type
     * @param deptIds
     * @return
     * @author Lee Kwanho 李坤澔
     *     date 2019-11-22
     **/
    List<DataBoardTreeEO> getMainBoardPieChart(@Param("startTime") Date startTime,
        @Param("finishTime") Date finishTime, @Param("type") Integer type, @Param("deptIds") String[] deptIds);
}
