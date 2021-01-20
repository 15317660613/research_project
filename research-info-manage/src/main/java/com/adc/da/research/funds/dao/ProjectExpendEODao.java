package com.adc.da.research.funds.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.research.funds.entity.ProjectExpendEO;

import java.util.List;

/**
 *ProjectExpendEO DAO层
 */
public interface ProjectExpendEODao extends BaseDao<ProjectExpendEO> {

    /***
    * @Description: 逻辑删除方法
    * @Param: [param]
    * @return: void
    * @Author: yanyujie
    * @Date: 2020/11/16
    */
    void logicDeleteByPrimaryKeys(List<String>param);

}
