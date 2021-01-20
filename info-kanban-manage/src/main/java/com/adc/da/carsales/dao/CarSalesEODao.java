package com.adc.da.carsales.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.carsales.entity.CarSalesEO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * <br>
 * <b>功能：</b>DB_CAR_SALES CarSalesEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-04-03 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface CarSalesEODao extends BaseDao<CarSalesEO> {

    int logicDeleteByPrimaryKey(@Param("id") String id);

    int batchLogicDeleteByIds(@Param("ids") List<String> ids);

    //逻辑删除全部数据
    int batchDeleteAll();

    /**
     * 车企销量排行榜
     * @return
     */
    List<CarSalesEO> carSalesRanking();



}
