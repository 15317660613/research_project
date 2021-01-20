package com.adc.da.research.config.service;

import com.adc.da.base.service.BaseService;
import com.adc.da.research.config.dao.GradingRulesEODao;
import com.adc.da.research.config.entity.GradingRulesEO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * <br>
 * <b>功能：</b>RS_GRADING_RULES GradingRulesEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-10-22 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("gradingRulesEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class GradingRulesEOService extends BaseService<GradingRulesEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(GradingRulesEOService.class);

    @Autowired
    private GradingRulesEODao dao;

    public GradingRulesEODao getDao() {
        return dao;
    }

    public void batchDeleteByIds(List<String> ids) {
        dao.batchDeleteByIds(ids);
    }

    /**
     * 批量新增
     *
     * @param gradingRulesEOS
     */
    public void batchInsertSelective(List<GradingRulesEO> gradingRulesEOS) {
        dao.batchInsertSelective(gradingRulesEOS);
    }

    /**
     * 根据模板id删除评分详情
     *
     * @param ratingRulesId
     */
    public void deleteByRatingRulesId(String ratingRulesId) {
        dao.deleteByRatingRulesId(ratingRulesId);
    }

}
