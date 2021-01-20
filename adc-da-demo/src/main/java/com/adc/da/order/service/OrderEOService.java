package com.adc.da.order.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.adc.da.base.service.BaseService;
import com.adc.da.order.dao.OrderEODao;
import com.adc.da.order.entity.OrderEO;


/**
 *
 * <br>
 * <b>功能：</b>TS_ORDER OrderEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2018-07-19 <br>
 * <b>版权所有：<b>版权归天津卡达克数据技术中心所有。<br>
 */
@Service("orderEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class OrderEOService extends BaseService<OrderEO, Long> {



    @Autowired
    private OrderEODao dao;

    public OrderEODao getDao() {
        return dao;
    }

}
