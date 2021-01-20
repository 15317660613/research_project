package com.adc.da.ext.sys.log.service;

import com.adc.da.base.service.BaseService;
import com.adc.da.ext.sys.log.dao.LogEOExtDao;
import com.adc.da.ext.sys.log.entity.LogEOExt;
import com.adc.da.log.dao.LogEODao;
import com.adc.da.log.entity.LogEO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * <br>
 * <b>功能：</b>TS_LOG LogEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2017-12-24 <br>
 * <b>版权所有：<b>版权归天津卡达克数据技术中心所有。<br>
 *
 * @see LogEODao
 */
@Service("LogEOExtService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED,
        rollbackFor = Throwable.class)
public class LogEOExtService extends BaseService<LogEOExt, String> {

    /**
     * @see LogEODao
     * @see mybatis.mapper.log
     */
    @Autowired
    private LogEOExtDao dao;

    public LogEOExtDao getDao() {
        return dao;
    }

}
