package com.adc.da.budget.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.budget.entity.BranchEO;

import java.util.List;

public interface BranchDao extends BaseDao<BranchEO> {

    List<BranchEO> queryAll();
}
