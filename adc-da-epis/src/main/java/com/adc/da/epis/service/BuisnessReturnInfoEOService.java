package com.adc.da.epis.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.adc.da.base.service.BaseService;
import com.adc.da.epis.dao.BuisnessReturnInfoEODao;
import com.adc.da.epis.entity.BuisnessReturnInfoEO;


/**
 *
 * <br>
 * <b>功能：</b>BUISNESS_RETURN_INFO BuisnessReturnInfoEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2018-11-07 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("buisnessReturnInfoEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class BuisnessReturnInfoEOService extends BaseService<BuisnessReturnInfoEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(BuisnessReturnInfoEOService.class);

    @Autowired
    private BuisnessReturnInfoEODao dao;

    public BuisnessReturnInfoEODao getDao() {
        return dao;
    }

}
