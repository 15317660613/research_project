package com.adc.da.industymeeting.service;

import com.adc.da.industymeeting.entity.BudgetManagementInfoEO;
import com.adc.da.util.constant.DeleteFlagEnum;
import com.adc.da.util.utils.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.adc.da.base.service.BaseService;
import com.adc.da.industymeeting.dao.CommunicationFrequencyEODao;
import com.adc.da.industymeeting.entity.CommunicationFrequencyEO;

import java.util.Date;
import java.util.List;


/**
 *
 * <br>
 * <b>功能：</b>COMMUNICATION_FREQUENCY CommunicationFrequencyEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-04-02 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("communicationFrequencyEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class CommunicationFrequencyEOService extends BaseService<CommunicationFrequencyEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(CommunicationFrequencyEOService.class);

    @Autowired
    private CommunicationFrequencyEODao dao;

    public CommunicationFrequencyEODao getDao() {
        return dao;
    }

    public CommunicationFrequencyEO save(CommunicationFrequencyEO eo, String userId) {
        eo.setId(UUID.randomUUID10());
        eo.setDelFlag(DeleteFlagEnum.NORMAL.getValue());
        eo.setCreateUserId(userId);
        eo.setCreateTime(new Date());
        eo.setUpdateUserId(userId);
        eo.setUpdateTime(new Date());
        dao.insertSelective(eo);
        return eo;
    }

    public List<CommunicationFrequencyEO> batchSave(List<CommunicationFrequencyEO> communicationFrequencyEOs, String userId) {
        empty();
        for (CommunicationFrequencyEO eo : communicationFrequencyEOs) {
            eo.setId(UUID.randomUUID10());
            eo.setDelFlag(DeleteFlagEnum.NORMAL.getValue());
            eo.setCreateUserId(userId);
            eo.setCreateTime(new Date());
            eo.setUpdateUserId(userId);
            eo.setUpdateTime(new Date());
            dao.insertSelective(eo);
        }
        return communicationFrequencyEOs;
    }

    /**
     * 批量逻辑删除
     */
    public void deleteLogicInBatch(List<String> ids) {
//        dao.deleteLogicInBatch(ids);
    }

    /**
     * 逻辑清空
     */
    public void empty() {
        dao.empty();
    }
}