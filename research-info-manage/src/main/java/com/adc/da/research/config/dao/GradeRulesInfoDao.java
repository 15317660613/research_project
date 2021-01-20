package com.adc.da.research.config.dao;

import com.adc.da.research.config.entity.GradeRulesInfoEO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @description
 * @date 2020/10/28 17:15
 * @auth zhn
 */
public interface GradeRulesInfoDao {

    /**
     * 根据id查询单条信息
     * @param id
     * @return
     */
    List<GradeRulesInfoEO> selectById(@Param("id") String id);
}
