package com.adc.da.research.config.dao;

import com.adc.da.research.config.entity.FundRulesInfoEO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @description
 * @date 2020/10/28 18:35
 * @auth zhn
 */
public interface FundRulesInfoDao {

    /**
     * 查询单条详情
     * @param id
     * @return
     */
    List<FundRulesInfoEO> selectById(@Param("id") String id);
}
