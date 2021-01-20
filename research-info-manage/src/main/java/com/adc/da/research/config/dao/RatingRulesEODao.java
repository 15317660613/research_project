package com.adc.da.research.config.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.research.config.entity.RatingRulesEO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <br>
 * <b>功能：</b>RS_RATING_RULES RatingRulesEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-10-22 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface RatingRulesEODao extends BaseDao<RatingRulesEO> {
    /**
     * 批量删除
     *
     * @param ids
     */
    void batchDeleteByIds(@Param("ids") List<String> ids);

    /**
     * 批量新增
     *
     * @param ratingRulesEOS
     */
    void batchInsertSelective(@Param("ratingRulesEOS") List<RatingRulesEO> ratingRulesEOS);

    /**
     *
     * @param name
     * @return ratingRuleEO
     */
     List<RatingRulesEO> findByName(String name);
    }
