package com.adc.da.finance.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.finance.entity.BusinessGonfigEO;
import com.adc.da.finance.page.MyBusinessGonfigEOPage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * <br>
 * <b>功能：</b>F__BUSINESS_GONFIG BusinessGonfigEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-04-17 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface BusinessGonfigEODao extends BaseDao<BusinessGonfigEO> {

    /**
     * 逻辑删除
     * @param ids
     * @return
     */
    int logicDelete(@Param("ids")List<String> ids);

    List<BusinessGonfigEO> getBusinessListByLoginUser(@Param("userId") String userId);

    /**
     * 根据业务编号查询经营业务
     * @param bgNumber
     * @return
     */
    BusinessGonfigEO getBusinessGonfigEOByBgNumber(@Param("bgNumber") String bgNumber);

    /**
     * 根据业务名称查询经营业务
     * @param bgName
     * @return
     */
    BusinessGonfigEO getBusinessGonfigEOByBgName(@Param("bgName") String bgName);

    /**
     * 根据业务名称和状态查询经营业务
     * @param bgName
     * @param bgType
     * @return
     */
    BusinessGonfigEO getBusinessGonfigEOByBgNameAndBgType(@Param("bgName") String bgName,@Param("bgType") String bgType);

    List<BusinessGonfigEO> getBusinessPage(MyBusinessGonfigEOPage page);

    Integer getBusinessPageCount(MyBusinessGonfigEOPage page);

}
