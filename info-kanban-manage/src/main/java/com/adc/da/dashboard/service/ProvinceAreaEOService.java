package com.adc.da.dashboard.service;

import com.adc.da.base.service.BaseService;
import com.adc.da.dashboard.dao.ProvinceAreaEODao;
import com.adc.da.dashboard.entity.ProvinceAreaEO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * <br>
 * <b>功能：</b>DB_PROVINCE_AREA ProvinceAreaEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-04-08 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("provinceAreaEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class ProvinceAreaEOService extends BaseService<ProvinceAreaEO, Integer> {

    private static final Logger logger = LoggerFactory.getLogger(ProvinceAreaEOService.class);

    @Autowired
    private ProvinceAreaEODao dao;

    public ProvinceAreaEODao getDao() {
        return dao;
    }

}
