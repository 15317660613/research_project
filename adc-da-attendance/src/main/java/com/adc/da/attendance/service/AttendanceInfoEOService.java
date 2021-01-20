package com.adc.da.attendance.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.adc.da.base.service.BaseService;
import com.adc.da.attendance.dao.AttendanceInfoEODao;
import com.adc.da.attendance.entity.AttendanceInfoEO;


/**
 *
 * <br>
 * <b>功能：</b>ATTENDANCE_INFO AttendanceInfoEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-01-18 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("attendanceInfoEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class AttendanceInfoEOService extends BaseService<AttendanceInfoEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(AttendanceInfoEOService.class);

    @Autowired
    private AttendanceInfoEODao dao;

    public AttendanceInfoEODao getDao() {
        return dao;
    }

}
