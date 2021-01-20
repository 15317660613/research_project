package com.adc.da.research.funds.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.research.funds.entity.ProjectIncomeEO;

import java.util.List;

/**
 *ProjectIncomeEODao DAO层
 */
public interface ProjectIncomeEODao extends BaseDao<ProjectIncomeEO> {

    /***
     * @Description: 逻辑删除方法
     * @Param: [param]
     * @return: void
     * @Author: yanyujie
     * @Date: 2020/11/16
     */
    void logicDeleteByPrimaryKeys(List<String>param);

}
