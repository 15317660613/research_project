package com.adc.da.research.service;

import com.adc.da.base.service.BaseService;
import com.adc.da.research.dao.ProjectDocEODao;
import com.adc.da.research.entity.ProjectDocEO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * <br>
 * <b>功能：</b>RS_PROJECT_DOC ProjectDocEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-08-26 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("projectDocEOService")
@Transactional(value = "transactionManager",
    readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class ProjectDocEOService extends BaseService<ProjectDocEO, String> {

    @Autowired
    private ProjectDocEODao dao;

    public ProjectDocEODao getDao() {
        return dao;
    }

}
