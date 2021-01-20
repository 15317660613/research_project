package com.adc.da.smallprogram.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.adc.da.base.service.BaseService;
import com.adc.da.smallprogram.dao.ScheduleSupportUserEODao;
import com.adc.da.smallprogram.entity.ScheduleSupportUserEO;

import java.util.List;

/**
 *
 * <br>
 * <b>功能：</b>TS_SCHEDULE_SUPPORT_USER ScheduleSupportUserEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-12-04 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("scheduleSupportUserEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class ScheduleSupportUserEOService extends BaseService<ScheduleSupportUserEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(ScheduleSupportUserEOService.class);

    @Autowired
    private ScheduleSupportUserEODao dao;

    public ScheduleSupportUserEODao getDao() {
        return dao;
    }

    public List<ScheduleSupportUserEO> selectBySupportId(String value) throws Exception {
        return dao.selectBySupportId(value);
    }
}
