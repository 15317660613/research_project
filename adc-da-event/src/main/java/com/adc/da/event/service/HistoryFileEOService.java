package com.adc.da.event.service;

import com.adc.da.event.entity.EventEO;
import com.adc.da.event.page.MyEventEOPage;
import com.adc.da.event.page.MyHistoryFileEOPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.adc.da.base.service.BaseService;
import com.adc.da.event.dao.HistoryFileEODao;
import com.adc.da.event.entity.HistoryFileEO;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 *
 * <br>
 * <b>功能：</b>WR_HISTORY_FILE HistoryFileEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-08-26 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("historyFileEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class HistoryFileEOService extends BaseService<HistoryFileEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(HistoryFileEOService.class);

    @Autowired
    private HistoryFileEODao dao;

    public HistoryFileEODao getDao() {
        return dao;
    }

    public List<HistoryFileEO> queryByMyPage(MyHistoryFileEOPage page){
        return dao.queryByMyPage(page);
    }




}
