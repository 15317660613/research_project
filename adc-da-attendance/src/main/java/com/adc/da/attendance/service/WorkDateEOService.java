package com.adc.da.attendance.service;

import com.adc.da.attendance.entity.ExportExcleDTO;
import com.adc.da.attendance.utils.HolidayUtils;
import com.adc.da.util.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.adc.da.base.service.BaseService;
import com.adc.da.attendance.dao.WorkDateEODao;
import com.adc.da.attendance.entity.WorkDateEO;

import java.util.Date;
import java.util.UUID;


/**
 *
 * <br>
 * <b>功能：</b>WORK_DATE WorkDateEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-01-21 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("workDateEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class WorkDateEOService extends BaseService<WorkDateEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(WorkDateEOService.class);

    @Autowired
    private WorkDateEODao dao;

    public WorkDateEODao getDao() {
        return dao;
    }



}
