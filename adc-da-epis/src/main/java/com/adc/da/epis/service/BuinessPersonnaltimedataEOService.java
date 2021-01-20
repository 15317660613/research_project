package com.adc.da.epis.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.adc.da.base.service.BaseService;
import com.adc.da.epis.dao.BuinessPersonnaltimedataEODao;
import com.adc.da.epis.entity.BuinessPersonnaltimedataEO;


/**
 *
 * <br>
 * <b>功能：</b>BUINESS_PERSONNALTIMEDATA BuinessPersonnaltimedataEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2018-11-07 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("buinessPersonnaltimedataEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class BuinessPersonnaltimedataEOService extends BaseService<BuinessPersonnaltimedataEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(BuinessPersonnaltimedataEOService.class);

    @Autowired
    private BuinessPersonnaltimedataEODao dao;

    public BuinessPersonnaltimedataEODao getDao() {
        return dao;
    }

}
