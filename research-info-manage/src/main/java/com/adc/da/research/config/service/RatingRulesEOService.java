package com.adc.da.research.config.service;

import com.adc.da.base.service.BaseService;
import com.adc.da.research.config.dao.RatingRulesEODao;
import com.adc.da.research.config.entity.RatingRulesEO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * <br>
 * <b>功能：</b>RS_RATING_RULES RatingRulesEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-10-22 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("ratingRulesEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class RatingRulesEOService extends BaseService<RatingRulesEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(RatingRulesEOService.class);

    @Autowired
    private RatingRulesEODao dao;

    public RatingRulesEODao getDao() {
        return dao;
    }

    /**
     * 批量删除
     *
     * @param ids
     */
    public void batchDeleteByIds(List<String> ids) {
        dao.batchDeleteByIds(ids);
    }

    /**
     * 批量新增
     *
     * @param ratingRulesEOS
     */
    public void batchInsertSelective(List<RatingRulesEO> ratingRulesEOS) {
        dao.batchInsertSelective(ratingRulesEOS);
    }

    /**
     * 根据名称查询模板
     *
     * @param name
     */

    public List<RatingRulesEO> findByName(String name){
        return dao.findByName(name);

    }

}
