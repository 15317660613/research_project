package com.adc.da.crm.service;

import com.adc.da.base.page.BasePage;
import com.adc.da.crm.dao.ContractInfoEODao;
import com.adc.da.crm.dao.InvoiceInfoEODao;
import com.adc.da.crm.entity.*;
import com.adc.da.crm.util.AutoMatchMethodArgumentUtil;
import com.adc.da.crm.vo.ContractApprovalVO;
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
import com.adc.da.crm.dao.ContractApprovalEODao;
import com.adc.da.crm.entity.ContractApprovalEO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * <br>
 * <b>功能：</b>CONTRACT_APPROVAL ContractApprovalEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2018-12-04 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("contractApprovalEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class ContractApprovalEOService extends BaseService<ContractApprovalEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(ContractApprovalEOService.class);

    @Autowired
    private ContractApprovalEODao dao;
    @Autowired
    private AdcFormDataDao formDataDao;
    @Autowired
    private InvoiceInfoEODao invoiceInfoEODao;
    @Autowired
    private ContractInfoEODao contractInfoEODao;
    @Autowired
    BeanMapper beanMapper;

    public ContractApprovalEODao getDao() {
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
        ContractApprovalVO contractApprovalVO = (ContractApprovalVO) AutoMatchMethodArgumentUtil.json2entity(adcFormDataEO, ContractApprovalVO.class);
        //vo 转eo
        ContractApprovalEO contractApprovalEO = this.beanMapper.map(contractApprovalVO, ContractApprovalEO.class);

        contractApprovalEO.setId(adcFormDataEO.getId());
        contractApprovalEO.setCreatedTime(new Date());
        contractApprovalEO.setCreatedUser(adcFormDataEO.getCreateName());
        contractApprovalEO.setDelFlag(String.valueOf(DeleteFlagEnum.NORMAL.getValue()));//是否删除 （0- 否 1-是）
        this.dao.insertSelective(contractApprovalEO);
        List<InvoiceInfoEO> invoiceInfoEOList = contractApprovalVO.getInvoiceInfoEOList();
        for (InvoiceInfoEO invoiceInfoEO : invoiceInfoEOList) {
            invoiceInfoEO.setId(UUID.randomUUID10());
            invoiceInfoEO.setType("1"); //1-- 合同申请表 2- 发票申请表
            invoiceInfoEO.setContractId(contractApprovalEO.getId());
            invoiceInfoEO.setCreatedTime(new Date());
            invoiceInfoEO.setCreatedUser(adcFormDataEO.getCreateName());
            invoiceInfoEO.setDelFlag(String.valueOf(DeleteFlagEnum.NORMAL.getValue()));//是否删除 （0- 否 1-是）
            this.invoiceInfoEODao.insertSelective(invoiceInfoEO);
        }
        List<ContractInfoEO> contractInfoEOList = contractApprovalVO.getContractInfoEOList();
        for (ContractInfoEO contractInfoEO : contractInfoEOList) {
            contractInfoEO.setId(UUID.randomUUID10());
            contractInfoEO.setContractId(contractApprovalEO.getId());
            contractInfoEO.setCreatedTime(new Date());
            contractInfoEO.setCreatedUser(adcFormDataEO.getCreateName());
            contractInfoEO.setDelFlag(String.valueOf(DeleteFlagEnum.NORMAL.getValue()));//是否删除 （0- 否 1-是）
            this.contractInfoEODao.insertSelective(contractInfoEO);
        }

        return adcFormDataEO;
    }

    public int update(String userId, AdcFormDataEO adcFormDataEO) {
        //更新ct_form
        this.formDataDao.updateByPrimaryKeySelective(adcFormDataEO);
        //更新解析后的数据
        ContractApprovalVO contractApprovalVO = (ContractApprovalVO) AutoMatchMethodArgumentUtil.json2entity(adcFormDataEO, ContractApprovalVO.class);
        //vo 转eo
        ContractApprovalEO contractApprovalEO = this.beanMapper.map(contractApprovalVO, ContractApprovalEO.class);
        contractApprovalEO.setId(adcFormDataEO.getId());
        contractApprovalEO.setCreatedTime(new Date());
        contractApprovalEO.setCreatedUser(adcFormDataEO.getCreateName());
        contractApprovalEO.setDelFlag(String.valueOf(DeleteFlagEnum.NORMAL.getValue()));//是否删除 （0- 否 1-是）

        //一对多子类 先删除再新增
        this.invoiceInfoEODao.deleteByContractId(adcFormDataEO.getId(), "1");
        List<InvoiceInfoEO> invoiceInfoEOList = contractApprovalVO.getInvoiceInfoEOList();
        for (InvoiceInfoEO invoiceInfoEO : invoiceInfoEOList) {
            invoiceInfoEO.setId(UUID.randomUUID10());
            invoiceInfoEO.setType("1"); //1-- 合同申请表 2- 发票申请表
            invoiceInfoEO.setContractId(contractApprovalEO.getId());
            invoiceInfoEO.setCreatedTime(new Date());
            invoiceInfoEO.setCreatedUser(adcFormDataEO.getCreateName());
            invoiceInfoEO.setDelFlag(String.valueOf(DeleteFlagEnum.NORMAL.getValue()));//是否删除 （0- 否 1-是）
            this.invoiceInfoEODao.insertSelective(invoiceInfoEO);
        }
        this.contractInfoEODao.deleteByContractId(adcFormDataEO.getId());
        List<ContractInfoEO> contractInfoEOList = contractApprovalVO.getContractInfoEOList();
        for (ContractInfoEO contractInfoEO : contractInfoEOList) {
            contractInfoEO.setId(UUID.randomUUID10());
            contractInfoEO.setContractId(contractApprovalEO.getId());
            contractInfoEO.setCreatedTime(new Date());
            contractInfoEO.setCreatedUser(adcFormDataEO.getCreateName());
            contractInfoEO.setDelFlag(String.valueOf(DeleteFlagEnum.NORMAL.getValue()));//是否删除 （0- 否 1-是）
            this.contractInfoEODao.insertSelective(contractInfoEO);
        }
        return this.dao.updateByPrimaryKeySelective(contractApprovalEO);
    }

    public int delete(String id){
        List<String> ids = new ArrayList<>(1);
        ids.add(id);
        this.formDataDao.deleteLogicInBatch(ids);
        this.invoiceInfoEODao.updateByContractIdAndDelFlag(id, 1);
        this.contractInfoEODao.updateByContractIdAndDelFlag(id, 1);
        return this.dao.updateByPrimaryKeyAndDelFlag(id, 1);
    }

    public List<ContractApprovalEO> queryCRMListByPage(BasePage page) throws Exception {
        Integer rowCount = this.queryByCount(page);
        page.getPager().setRowCount(rowCount);
        return this.dao.queryCRMListByPage(page);
    }
}
