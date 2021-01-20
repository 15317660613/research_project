package com.adc.da.crm.service;

import com.adc.da.base.page.BasePage;
import com.adc.da.crm.entity.ProjectEstablishApprovalEO;
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
import com.adc.da.crm.dao.ContractBaseEODao;
import com.adc.da.crm.entity.ContractBaseEO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * <br>
 * <b>功能：</b>CONTRACT_BASE ContractBaseEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2018-12-04 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("contractBaseEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class ContractBaseEOService extends BaseService<ContractBaseEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(ContractBaseEOService.class);

    @Autowired
    private ContractBaseEODao dao;
    @Autowired
    private AdcFormDataDao formDataDao;

    public ContractBaseEODao getDao() {
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
        ContractBaseEO contractBaseEO = (ContractBaseEO) AutoMatchMethodArgumentUtil.json2entity(adcFormDataEO,
                ContractBaseEO.class);
        contractBaseEO.setId(adcFormDataEO.getId());
        contractBaseEO.setCreatedTime(new Date());
        contractBaseEO.setCreatedUser(adcFormDataEO.getCreateName());
        contractBaseEO.setDelFlag(String.valueOf(DeleteFlagEnum.NORMAL.getValue()));//是否删除 （0- 否 1-是）
        this.dao.insertSelective(contractBaseEO);
        return adcFormDataEO;
    }

    public int update(String userId, AdcFormDataEO adcFormDataEO) {
        //更新ct_form
        this.formDataDao.updateByPrimaryKeySelective(adcFormDataEO);
        //更新解析后的数据
        ContractBaseEO contractBaseEO = (ContractBaseEO) AutoMatchMethodArgumentUtil.json2entity(adcFormDataEO,
                ContractBaseEO.class);
        contractBaseEO.setId(adcFormDataEO.getId());
        contractBaseEO.setModifiedTime2(new Date());
        contractBaseEO.setModifiedUser2(userId);
        return this.dao.updateByPrimaryKeySelective(contractBaseEO);
    }

    public int delete(String id){
        List<String> ids = new ArrayList<>(1);
        ids.add(id);
        this.formDataDao.deleteLogicInBatch(ids);
        return this.dao.updateByPrimaryKeyAndDelFlag(id, 1);
    }

    public List<ContractBaseEO> queryCRMListByPage(BasePage page) throws Exception {
        Integer rowCount = this.queryByCount(page);
        page.getPager().setRowCount(rowCount);
        return this.dao.queryCRMListByPage(page);
    }
}
