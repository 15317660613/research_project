package com.adc.da.research.config.service;

import com.adc.da.base.service.BaseService;
import com.adc.da.research.config.dao.FundRulesEODao;
import com.adc.da.research.config.entity.FundRulesEO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * <br>
 * <b>功能：</b>RS_FUND_RULES FundRulesEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-10-26 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("fundRulesEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class FundRulesEOService extends BaseService<FundRulesEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(FundRulesEOService.class);

    @Autowired
    private FundRulesEODao dao;

    public FundRulesEODao getDao() {
        return dao;
    }

    /**
     * 根据模板id删除经费科目详情
     *
     * @param ids
     */
    public void deleteByIds(List<String> ids) {
        dao.deleteByIds(ids);
    }

}
