package com.adc.da.industymeeting.service;

import com.adc.da.sys.dao.OrgEODao;
import com.adc.da.sys.service.OrgEOService;
import com.adc.da.util.constant.DeleteFlagEnum;
import com.adc.da.util.utils.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.adc.da.base.service.BaseService;
import com.adc.da.industymeeting.dao.BudgetManagementInfoEODao;
import com.adc.da.industymeeting.entity.BudgetManagementInfoEO;

import java.util.Date;
import java.util.List;

/**
 *
 * <br>
 * <b>功能：</b>BUDGET_MANAGEMENT_INFO BudgetManagementInfoEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-03-31 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("budgetManagementInfoEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class BudgetManagementInfoEOService extends BaseService<BudgetManagementInfoEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(BudgetManagementInfoEOService.class);

    @Autowired
    private BudgetManagementInfoEODao dao;

    @Autowired
    private OrgEODao orgEODao;

    public BudgetManagementInfoEODao getDao() {
        return dao;
    }

    public BudgetManagementInfoEO save(BudgetManagementInfoEO budgetManagementInfoEO) {
        budgetManagementInfoEO.setId(UUID.randomUUID10());
        budgetManagementInfoEO.setDelFlag(DeleteFlagEnum.NORMAL.getValue());
        budgetManagementInfoEO.setCreateTime(new Date());
        budgetManagementInfoEO.setUpdateTime(new Date());
        dao.insertSelective(budgetManagementInfoEO);
        return budgetManagementInfoEO;
    }

    public List<BudgetManagementInfoEO> batchSave(List<BudgetManagementInfoEO> budgetManagementInfoEOs, String userId) {
        for (BudgetManagementInfoEO eo : budgetManagementInfoEOs) {
            eo.setId(UUID.randomUUID10());
            eo.setDelFlag(DeleteFlagEnum.NORMAL.getValue());
            eo.setDepartmentId(orgEODao.getOrgIdByOrgName(eo.getDepartment()));
            eo.setHeadquartersId(orgEODao.getOrgIdByOrgName(eo.getHeadquarters()));
            eo.setCreateUserId(userId);
            eo.setCreateTime(new Date());
            eo.setUpdateUserId(userId);
            eo.setUpdateTime(new Date());
            dao.insertSelective(eo);
        }
        return budgetManagementInfoEOs;
    }

    /**
     * 批量逻辑删除
     */
    public void deleteLogicInBatch(List<String> ids) {
        dao.deleteLogicInBatch(ids);
    }

    /**
     * 逻辑清空
     */
    public void empty() {
        dao.empty();
    }
}