package com.adc.da.crm.service;

import com.adc.da.base.page.BasePage;
import com.adc.da.crm.entity.ContractBaseEO;
import com.adc.da.crm.entity.ProjectInfoEO;
import com.adc.da.crm.util.AutoMatchMethodArgumentUtil;
import com.adc.da.form.dao.AdcFormDataDao;
import com.adc.da.form.entity.AdcFormDataEO;
import com.adc.da.util.constant.DeleteFlagEnum;
import com.adc.da.util.utils.DateUtils;
import com.adc.da.util.utils.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.adc.da.base.service.BaseService;
import com.adc.da.crm.dao.SalesVlaueEODao;
import com.adc.da.crm.entity.SalesVlaueEO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * <br>
 * <b>功能：</b>SALES_VLAUE SalesVlaueEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2018-12-04 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("salesVlaueEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class SalesVlaueEOService extends BaseService<SalesVlaueEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(SalesVlaueEOService.class);

    @Autowired
    private SalesVlaueEODao dao;
    @Autowired
    private AdcFormDataDao formDataDao;

    public SalesVlaueEODao getDao() {
        return dao;
    }

    @Transactional(
            rollbackFor = {Exception.class}
    )
    public AdcFormDataEO save(AdcFormDataEO adcFormDataEO) {
        //保存到ct_form
        //1.保存ct_data
        adcFormDataEO.setId(UUID.randomUUID10());
        adcFormDataEO.setDelFlag(DeleteFlagEnum.NORMAL.getValue());
        adcFormDataEO.setCreateTime(DateUtils.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
        this.formDataDao.insert(adcFormDataEO);
        //2.保存关联 ct_form_data
        this.formDataDao.deleteFormAndDataById(adcFormDataEO.getId());
        this.formDataDao.saveFormAndData(adcFormDataEO.getFormId(), adcFormDataEO.getId());
        //保存解析后的数据
        SalesVlaueEO salesVlaueEO = (SalesVlaueEO) AutoMatchMethodArgumentUtil.json2entity(adcFormDataEO,
                SalesVlaueEO.class);
        salesVlaueEO.setId(adcFormDataEO.getId());
        salesVlaueEO.setCreatedTime(new Date());
        salesVlaueEO.setCreatedUser(adcFormDataEO.getCreateName());
        salesVlaueEO.setDelFlag(String.valueOf(DeleteFlagEnum.NORMAL.getValue()));//是否删除 （0- 否 1-是）
        this.dao.insertSelective(salesVlaueEO);
        return adcFormDataEO;
    }

    public int update(String userId, AdcFormDataEO adcFormDataEO) {
        this.formDataDao.updateByPrimaryKeySelective(adcFormDataEO);
        //更新解析后的数据
        SalesVlaueEO salesVlaueEO = (SalesVlaueEO) AutoMatchMethodArgumentUtil.json2entity(adcFormDataEO,
                SalesVlaueEO.class);
        salesVlaueEO.setId(adcFormDataEO.getId());
        salesVlaueEO.setModifiedTime2(new Date());
        salesVlaueEO.setModifiedUser2(adcFormDataEO.getCreateName());

        return this.dao.updateByPrimaryKeySelective(salesVlaueEO);
    }

    public int delete(String id){
        List<String> ids = new ArrayList<>(1);
        ids.add(id);
        this.formDataDao.deleteLogicInBatch(ids);
        return this.dao.updateByPrimaryKeyAndDelFlag(id, 1);
    }

    public List<SalesVlaueEO> queryCRMListByPage(BasePage page) throws Exception {
        Integer rowCount = this.queryByCount(page);
        page.getPager().setRowCount(rowCount);
        return this.dao.queryCRMListByPage(page);
    }
}
