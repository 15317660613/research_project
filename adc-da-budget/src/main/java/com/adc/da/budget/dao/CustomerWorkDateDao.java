package com.adc.da.budget.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.budget.entity.BranchEO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

public interface CustomerWorkDateDao extends BaseDao<BranchEO> {

    Integer countWorkDate(@Param("startDate") Date startDate,@Param("endDate")Date endDate);

}
