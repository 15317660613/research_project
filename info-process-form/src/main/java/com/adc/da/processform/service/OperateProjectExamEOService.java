package com.adc.da.processform.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.adc.da.base.service.BaseService;
import com.adc.da.processform.dao.OperateProjectExamEODao;
import com.adc.da.processform.entity.OperateProjectExamEO;


/**
 *
 * <br>
 * <b>功能：</b>PF_OPERATE_PROJECT_EXAM OperateProjectExamEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-06-26 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("operateProjectExamEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class OperateProjectExamEOService extends BaseService<OperateProjectExamEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(OperateProjectExamEOService.class);

    @Autowired
    private OperateProjectExamEODao dao;

    public OperateProjectExamEODao getDao() {
        return dao;
    }

}
