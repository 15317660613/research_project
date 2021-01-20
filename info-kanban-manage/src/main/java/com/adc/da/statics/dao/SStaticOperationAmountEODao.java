package com.adc.da.statics.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.dashboard.vo.ContractDashBoardHeaderVO;
import com.adc.da.dashboard.vo.OrgContractInvoiceVO;
import com.adc.da.statics.entity.SStaticOperationAmountEO;
import com.adc.da.statics.vo.*;

import java.util.List;
import java.util.Map;

/**
 *
 * <br>
 * <b>功能：</b>S_STATIC_OPERATION_AMOUNT SStaticOperationAmountEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-09-04 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface SStaticOperationAmountEODao extends BaseDao<SStaticOperationAmountEO> {


    /**
     * 看板查询
     * @param
     * @return
     */

    List<StaticOperationAmountVO> findByProjectAmountStatics(StaticOperationAmountQueryVO staticOperationAmountQueryVO);

    List<StaticOperationAmountVO> findByProjectAmountAreaStatics(SStaticOperationAmountEO sStaticOperationAmountEO);

    ContractDashBoardHeaderVO queryDashboardTop(SStaticOperationAmountEO sStaticOperationAmountEO);

    StaticOperationDashBoardHeaderVO queryDashboardTopVo(SStaticOperationAmountEO sStaticOperationAmountEO);

    Map<String,Object> queryDashboardTopMap(SStaticOperationAmountEO sStaticOperationAmountEO);

    List<StaticOperationAmountVO> queryDashBoardBody(StaticOperationAmountQueryVO queryVO);

    List<StaticOperationAmountChartVO> queryChartDataYear(StaticOperationAmountQueryVO queryVO);

    List<StaticOperationOrgContractInvoiceVO> queryDeptChartDataList(StaticOperationAmountQueryVO queryVO);

    List<StaticOperationCompanyVO> queryCompanyBusiness(SStaticOperationAmountEO query);

    List<StaticOperationCompanyVO> queryCompanyContract(SStaticOperationAmountEO query);

    List<StaticOperationCompanyVO> querySalesVolumeVO(SStaticOperationAmountEO query);

    List<StaticOperationCompanyVO> queryDepartmentVO(SStaticOperationAmountEO query);

    List<StaticOperationCompanyVO> queryCompanyVO(SStaticOperationAmountEO query);

    void logicDeleteByPrimaryKeys(List<String> idList);
}
