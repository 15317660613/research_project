package com.adc.da.research.config.service;

import com.adc.da.base.service.BaseService;
import com.adc.da.research.config.dao.FundDetailsEODao;
import com.adc.da.research.config.entity.FundDetailsEO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * <br>
 * <b>功能：</b>RS_FUND_DETAILS FundDetailsEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-10-26 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("fundDetailsEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class FundDetailsEOService extends BaseService<FundDetailsEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(FundDetailsEOService.class);

    @Autowired
    private FundDetailsEODao dao;

    public FundDetailsEODao getDao() {
        return dao;
    }

    /**
     * 批量新增经费科目详情
     *
     * @param fundDetailsEOS
     */
    public void batchInsertSelective(List<FundDetailsEO> fundDetailsEOS) {
        dao.batchInsertSelective(fundDetailsEOS);
    }

    /**
     * 根据模板id批量删除经费科目详情
     *
     * @param fundRulesIds
     */
    public void deleteByFundRuleIds(List<String> fundRulesIds) {
        dao.deleteByFundRuleIds(fundRulesIds);
    }

}
