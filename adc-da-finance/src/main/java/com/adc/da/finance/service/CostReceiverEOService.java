package com.adc.da.finance.service;

import com.adc.da.base.service.BaseService;
import com.adc.da.finance.dao.CostReceiverEODao;
import com.adc.da.finance.entity.CostManagementEO;
import com.adc.da.finance.entity.CostReceiverEO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <br>
 * <b>功能：</b>F_COST_RECEIVER CostReceiverEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-04-17 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("costReceiverEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class CostReceiverEOService extends BaseService<CostReceiverEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(CostReceiverEOService.class);

    @Autowired
    private CostReceiverEODao dao;

    @Autowired
    private CostManagementEOService costManagementEOService;

    public CostReceiverEODao getDao() {
        return dao;
    }

    public CostReceiverEO selectByOrgName(String orgName) {
        return dao.selectByOrgName(orgName);
    }

    public CostReceiverEO myUpdate(CostReceiverEO costReceiverEO) throws Exception {
        this.updateByPrimaryKeySelective(costReceiverEO);
        CostReceiverEO receiverEO = dao.selectByPrimaryKey(costReceiverEO.getId());
        List<CostManagementEO> costManagementEOList = costManagementEOService.queryListByOrgId(receiverEO.getOrgId());
        costManagementEOService.taskMethod(costManagementEOList);
        return costReceiverEO;
    }




}
