package com.adc.da.crm.service;

import com.adc.da.base.page.BasePage;
import com.adc.da.crm.dao.BTravelCustomerVisitRecordEODao;
import com.adc.da.crm.dao.BTravelProjectVisitRecordEODao;

import com.adc.da.crm.entity.BTravelApprovalEO;
import com.adc.da.crm.entity.BTravelCustomerVisitRecordEO;
import com.adc.da.crm.entity.BTravelProjectVisitRecordEO;
import com.adc.da.crm.util.AutoMatchMethodArgumentUtil;
import com.adc.da.crm.vo.BTravelApprovalVO;
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
import com.adc.da.crm.dao.BTravelApprovalEODao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * <br>
 * <b>功能：</b>B_TRAVEL_APPROVAL BTravelApprovalEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2018-11-28 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("bTravelApprovalEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class BTravelApprovalEOService extends BaseService<BTravelApprovalEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(BTravelApprovalEOService.class);

    @Autowired
    private BTravelApprovalEODao dao;
    @Autowired
    private AdcFormDataDao formDataDao;
    @Autowired
    private BTravelCustomerVisitRecordEODao bTravelCustomerVisitRecordEODao;
    @Autowired
    private BTravelProjectVisitRecordEODao bTravelProjectVisitRecordEODao;
    @Autowired
    BeanMapper beanMapper;

    public BTravelApprovalEODao getDao() {
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
        BTravelApprovalVO bTravelApprovalVO = (BTravelApprovalVO) AutoMatchMethodArgumentUtil.json2entity(adcFormDataEO, BTravelApprovalVO.class);
        //vo 转eo
        BTravelApprovalEO bTravelApprovalEO = this.beanMapper.map(bTravelApprovalVO, BTravelApprovalEO.class);
        bTravelApprovalEO.setId(adcFormDataEO.getId());
        bTravelApprovalEO.setCreatedTime(new Date());
        bTravelApprovalEO.setCreatedUser(adcFormDataEO.getCreateName());
        bTravelApprovalEO.setDelFlag(String.valueOf(DeleteFlagEnum.NORMAL.getValue()));//是否删除 （0- 否 1-是）
        this.dao.insertSelective(bTravelApprovalEO);
        List<BTravelCustomerVisitRecordEO> bTravelCustomerVisitRecordEOList = bTravelApprovalVO.getbTravelCustomerVisitRecordEOList();
        doInsertBTravelCustomerVisitRecord(bTravelCustomerVisitRecordEOList, adcFormDataEO.getCreateName(), bTravelApprovalEO);
        List<BTravelProjectVisitRecordEO> bTravelProjectVisitRecordEOList = bTravelApprovalVO.getbTravelProjectVisitRecordEOList();
        doInsertBTravelProjectVisitRecord(bTravelProjectVisitRecordEOList, adcFormDataEO.getCreateName(), bTravelApprovalEO);

        return adcFormDataEO;
    }


    public int update(String userId, AdcFormDataEO adcFormDataEO) {
        //更新到ct_form
        this.formDataDao.insert(adcFormDataEO);
        //更新解析后的数据
        BTravelApprovalVO bTravelApprovalVO = (BTravelApprovalVO) AutoMatchMethodArgumentUtil.json2entity(adcFormDataEO, BTravelApprovalVO.class);
        //vo 转eo
        BTravelApprovalEO bTravelApprovalEO = this.beanMapper.map(bTravelApprovalVO, BTravelApprovalEO.class);
        bTravelApprovalEO.setId(adcFormDataEO.getId());
        bTravelApprovalEO.setModifiedTime(new Date());
        bTravelApprovalEO.setModifiedUser(userId);
        //子类先删除 再新增
        this.bTravelCustomerVisitRecordEODao.deleteByTravelApprovalId(adcFormDataEO.getId());
        List<BTravelCustomerVisitRecordEO> bTravelCustomerVisitRecordEOList = bTravelApprovalVO.getbTravelCustomerVisitRecordEOList();
        doInsertBTravelCustomerVisitRecord(bTravelCustomerVisitRecordEOList, adcFormDataEO.getCreateName(), bTravelApprovalEO);
        this.bTravelProjectVisitRecordEODao.deleteByTravelApprovalId(adcFormDataEO.getId());
        List<BTravelProjectVisitRecordEO> bTravelProjectVisitRecordEOList = bTravelApprovalVO.getbTravelProjectVisitRecordEOList();
        doInsertBTravelProjectVisitRecord(bTravelProjectVisitRecordEOList, userId, bTravelApprovalEO);
        return this.dao.updateByPrimaryKeySelective(bTravelApprovalEO);
    }

    private void doInsertBTravelProjectVisitRecord(List<BTravelProjectVisitRecordEO> bTravelProjectVisitRecordEOList, String userId,
                                                   BTravelApprovalEO bTravelApprovalEO) {
        for (BTravelProjectVisitRecordEO bTravelProjectVisitRecordEO : bTravelProjectVisitRecordEOList) {
            bTravelProjectVisitRecordEO.setId(UUID.randomUUID10());
            bTravelProjectVisitRecordEO.setTravelApprovalId(bTravelApprovalEO.getId());
            bTravelProjectVisitRecordEO.setCreatedTime(new Date());
            bTravelProjectVisitRecordEO.setCreatedUser(userId);
            bTravelProjectVisitRecordEO.setDelFlag(String.valueOf(DeleteFlagEnum.NORMAL.getValue()));//是否删除 （0- 否 1-是）
            this.bTravelProjectVisitRecordEODao.insertSelective(bTravelProjectVisitRecordEO);
        }
    }

    private void doInsertBTravelCustomerVisitRecord(List<BTravelCustomerVisitRecordEO> bTravelCustomerVisitRecordEOList, String userId,
                                                    BTravelApprovalEO bTravelApprovalEO) {
        for (BTravelCustomerVisitRecordEO bTravelCustomerVisitRecordEO : bTravelCustomerVisitRecordEOList) {
            bTravelCustomerVisitRecordEO.setId(UUID.randomUUID10());
            bTravelCustomerVisitRecordEO.setTravelApprovalId(bTravelApprovalEO.getId());
            bTravelCustomerVisitRecordEO.setCreatedTime(new Date());
            bTravelCustomerVisitRecordEO.setCreatedUser(userId);
            bTravelCustomerVisitRecordEO.setDelFlag(String.valueOf(DeleteFlagEnum.NORMAL.getValue()));//是否删除 （0- 否 1-是）
            this.bTravelCustomerVisitRecordEODao.insertSelective(bTravelCustomerVisitRecordEO);
        }
    }

    public int delete(String id) {
        List<String> ids = new ArrayList<>(1);
        ids.add(id);
        this.formDataDao.deleteLogicInBatch(ids);
        this.bTravelCustomerVisitRecordEODao.updateByTravelApprovalIdAndDelFlag(id, 1);
        this.bTravelProjectVisitRecordEODao.updateByTravelApprovalIdAndDelFlag(id, 1);
        int i = this.dao.updateByPrimaryKeyAndDelFlag(id, 1);
        return i;
    }

    public List<BTravelApprovalEO> queryCRMListByPage(BasePage page) throws Exception {
        Integer rowCount = this.queryByCount(page);
        page.getPager().setRowCount(rowCount);
        return this.dao.queryCRMListByPage(page);
    }
}
