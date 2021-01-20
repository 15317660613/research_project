package com.adc.da.business.dao;

import com.adc.da.business.entity.DeptEO;
import com.adc.da.sys.entity.OrgEO;

import java.util.List;

public interface DeptEODao {
    //查出本部下所有部门

    List<DeptEO> queryAllByOrgType(String orgType);

    List<DeptEO> listAllOrg();
    List<DeptEO> queryChildGroup();
}
