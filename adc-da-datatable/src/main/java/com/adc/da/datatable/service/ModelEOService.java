package com.adc.da.datatable.service;

import com.adc.da.base.service.BaseService;

import com.adc.da.datatable.dao.ModelEODao;
import com.adc.da.datatable.entity.ModelEO;
import com.adc.da.login.util.UserUtils;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.util.utils.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 *
 * <br>
 * <b>功能：</b>TS_MODEL ModelEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-06-03 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("modelEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class ModelEOService extends BaseService<ModelEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(ModelEOService.class);


    public ModelEO insertMenu(ModelEO modelEO) throws Exception {
        modelEO.setMId(UUID.randomUUID(10));
        UserEO user = UserUtils.getUser();
        String name = user.getUsname();
        modelEO.setCreateMan(name);
        modelEO.setMCreate(new Date());
        super.insertSelective(modelEO);
        return modelEO;
    }

    @Autowired
    private ModelEODao dao;

    public ModelEODao getDao() {
        return dao;
    }

    public void saveConfig(String mid, String m_config) {
        this.dao.saveConfig(mid, m_config);
    }
}
