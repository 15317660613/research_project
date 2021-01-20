package com.adc.da.research.config.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.adc.da.base.service.BaseService;
import com.adc.da.research.config.dao.ProcessStageEODao;
import com.adc.da.research.config.entity.ProcessStageEO;


/**
 *
 * <br>
 * <b>功能：</b>RS_PROCESS_STAGE ProcessStageEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-11-19 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("processStageEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class ProcessStageEOService extends BaseService<ProcessStageEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(ProcessStageEOService.class);

    @Autowired
    private ProcessStageEODao dao;

    public ProcessStageEODao getDao() {
        return dao;
    }

}
