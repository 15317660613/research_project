package com.adc.da.research.project.service;

import com.adc.da.base.service.BaseService;
import com.adc.da.research.project.dao.ImplementationProcEODao;
import com.adc.da.research.project.entity.ImplementationProcEO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 *
 * <br>
 * <b>功能：</b>RS_IMPLEMENTATION_PROC ImplementationProcEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-10-30 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("implementationProcEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class ImplementationProcEOService extends BaseService<ImplementationProcEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(ImplementationProcEOService.class);

    @Autowired
    private ImplementationProcEODao dao;

    public ImplementationProcEODao getDao() {
        return dao;
    }

    public void batchInsertSelective(List<ImplementationProcEO> implementationProcEOList){
        dao.batchInsertSelective(implementationProcEOList);
    }

}
