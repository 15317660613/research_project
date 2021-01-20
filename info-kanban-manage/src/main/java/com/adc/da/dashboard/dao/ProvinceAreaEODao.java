package com.adc.da.dashboard.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.dashboard.entity.ProvinceAreaEO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <br>
 * <b>功能：</b>DB_PROVINCE_AREA ProvinceAreaEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-04-08 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface ProvinceAreaEODao extends BaseDao<ProvinceAreaEO> {
    List<ProvinceAreaEO> queryAllByList();

    //根据省份名称查询省份信息
    ProvinceAreaEO queryOneByProvince(@Param("province") String province);

}
