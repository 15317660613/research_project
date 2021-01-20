package com.adc.da.research.company.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.adc.da.base.service.BaseService;
import com.adc.da.research.company.dao.CompanyEODao;
import com.adc.da.research.company.entity.CompanyEO;


/**
 *
 * <br>
 * <b>功能：</b>RS_COMPANY CompanyEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-11-26 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("companyEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class CompanyEOService extends BaseService<CompanyEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(CompanyEOService.class);

    @Autowired
    private CompanyEODao dao;

    public CompanyEODao getDao() {
        return dao;
    }

}
