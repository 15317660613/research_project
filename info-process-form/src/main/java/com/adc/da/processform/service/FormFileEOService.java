package com.adc.da.processform.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.adc.da.base.service.BaseService;
import com.adc.da.processform.dao.FormFileEODao;
import com.adc.da.processform.entity.FormFileEO;


/**
 *
 * <br>
 * <b>功能：</b>PF_FORM_FILE FormFileEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-06-30 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("formFileEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class FormFileEOService extends BaseService<FormFileEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(FormFileEOService.class);

    @Autowired
    private FormFileEODao dao;

    public FormFileEODao getDao() {
        return dao;
    }


    public  int  softDelete(String processinstid ,String filebelong){
        return dao.softDelete(processinstid ,filebelong ) ;
    }

    public FormFileEO selectByProcessAndBelong(String processinstid ,String filebelong){
        return dao.selectByProcessAndBelong( processinstid , filebelong ) ;
    }

}
