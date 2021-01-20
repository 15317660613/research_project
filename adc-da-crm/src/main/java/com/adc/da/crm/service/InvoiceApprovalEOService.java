package com.adc.da.crm.service;

import com.adc.da.base.page.BasePage;
import com.adc.da.crm.dao.InvoiceInfoEODao;
import com.adc.da.crm.entity.*;
import com.adc.da.crm.util.AutoMatchMethodArgumentUtil;
import com.adc.da.crm.vo.InvoiceApprovalVO;
import com.adc.da.form.dao.AdcFormDataDao;
import com.adc.da.form.entity.AdcFormDataEO;
import com.adc.da.util.constant.DeleteFlagEnum;
import com.adc.da.util.utils.BeanMapper;
import com.adc.da.util.utils.DateUtils;
import com.adc.da.util.utils.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.adc.da.base.service.BaseService;
import com.adc.da.crm.dao.InvoiceApprovalEODao;
import com.adc.da.crm.entity.InvoiceApprovalEO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * <br>
 * <b>功能：</b>INVOICE_APPROVAL InvoiceApprovalEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2018-12-04 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("invoiceApprovalEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class InvoiceApprovalEOService extends BaseService<InvoiceApprovalEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(InvoiceApprovalEOService.class);

    @Autowired
    private InvoiceApprovalEODao dao;
    @Autowired
    private AdcFormDataDao formDataDao;
    @Autowired
    private InvoiceInfoEODao invoiceInfoEODao;
    @Autowired
    BeanMapper beanMapper;


    public InvoiceApprovalEODao getDao() {
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
        InvoiceApprovalVO invoiceApprovalVO = (InvoiceApprovalVO) AutoMatchMethodArgumentUtil.json2entity(adcFormDataEO, InvoiceApprovalVO.class);
        //vo 转eo
        InvoiceApprovalEO invoiceApprovalEO = this.beanMapper.map(invoiceApprovalVO, InvoiceApprovalEO.class);
        invoiceApprovalEO.setId(adcFormDataEO.getId());
        invoiceApprovalEO.setCreatedTime(new Date());
        invoiceApprovalEO.setCreatedUser(adcFormDataEO.getCreateName());
        invoiceApprovalEO.setDelFlag(String.valueOf(DeleteFlagEnum.NORMAL.getValue()));//是否删除 （0- 否 1-是）
        this.dao.insertSelective(invoiceApprovalEO);

        List<InvoiceInfoEO> invoiceInfoEOList = invoiceApprovalVO.getInvoiceInfoEOList();
        for (InvoiceInfoEO invoiceInfoEO : invoiceInfoEOList) {
            invoiceInfoEO.setId(UUID.randomUUID10());
            invoiceInfoEO.setType("2"); //1-- 合同申请表 2- 发票申请表
            invoiceInfoEO.setContractId(invoiceApprovalEO.getId());
            invoiceInfoEO.setCreatedTime(new Date());
            invoiceInfoEO.setCreatedUser(adcFormDataEO.getCreateName());
            invoiceInfoEO.setDelFlag(String.valueOf(DeleteFlagEnum.NORMAL.getValue()));//是否删除 （0- 否 1-是）
            this.invoiceInfoEODao.insertSelective(invoiceInfoEO);
        }

        return adcFormDataEO;
    }

    public int update(String userId, AdcFormDataEO adcFormDataEO) {
        this.formDataDao.updateByPrimaryKeySelective(adcFormDataEO);

        //保存解析后的数据
        InvoiceApprovalVO invoiceApprovalVO = (InvoiceApprovalVO) AutoMatchMethodArgumentUtil.json2entity(adcFormDataEO, InvoiceApprovalVO.class);
        //vo 转eo
        InvoiceApprovalEO invoiceApprovalEO = this.beanMapper.map(invoiceApprovalVO, InvoiceApprovalEO.class);
        invoiceApprovalEO.setId(adcFormDataEO.getId());
        invoiceApprovalEO.setModifiedTime2(new Date());
        invoiceApprovalEO.setModifiedUser2(userId);

        this.invoiceInfoEODao.deleteByContractId(adcFormDataEO.getId(), "2");
        List<InvoiceInfoEO> invoiceInfoEOList = invoiceApprovalVO.getInvoiceInfoEOList();
        for (InvoiceInfoEO invoiceInfoEO : invoiceInfoEOList) {
            invoiceInfoEO.setId(UUID.randomUUID10());
            invoiceInfoEO.setType("2"); //1-- 合同申请表 2- 发票申请表
            invoiceInfoEO.setContractId(invoiceApprovalEO.getId());
            invoiceInfoEO.setCreatedTime(new Date());
            invoiceInfoEO.setCreatedUser(adcFormDataEO.getCreateName());
            invoiceInfoEO.setDelFlag(String.valueOf(DeleteFlagEnum.NORMAL.getValue()));//是否删除 （0- 否 1-是）
            this.invoiceInfoEODao.insertSelective(invoiceInfoEO);
        }
        return this.dao.updateByPrimaryKeySelective(invoiceApprovalEO);
    }

    public int delete(String id){
        List<String> ids = new ArrayList<>(1);
        ids.add(id);
        this.formDataDao.deleteLogicInBatch(ids);
        this.invoiceInfoEODao.updateByContractIdAndDelFlag(id, 1);
        return this.dao.updateByPrimaryKeyAndDelFlag(id, 1);
    }

    public List<InvoiceApprovalEO> queryCRMListByPage(BasePage page) throws Exception {
        Integer rowCount = this.queryByCount(page);
        page.getPager().setRowCount(rowCount);
        return this.dao.queryCRMListByPage(page);
    }
}
