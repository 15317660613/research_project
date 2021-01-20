package com.adc.da.finance.service;

import com.adc.da.sys.dao.DicTypeEODao;
import com.adc.da.sys.dao.OrgEODao;
import com.adc.da.util.constant.DeleteFlagEnum;
import com.adc.da.util.utils.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.adc.da.base.service.BaseService;
import com.adc.da.finance.dao.ResearchIssueEODao;
import com.adc.da.finance.entity.ResearchIssueEO;
import java.util.Date;
import java.util.List;


/**
 *
 * <br>
 * <b>功能：</b>F_RESEARCH_ISSUE ResearchIssueEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-04-20 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("researchIssueEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class ResearchIssueEOService extends BaseService<ResearchIssueEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(ResearchIssueEOService.class);

    @Autowired
    private ResearchIssueEODao dao;

    @Autowired
    private OrgEODao orgEODao;

    @Autowired
    private DicTypeEODao dicTypeEODao;

    public ResearchIssueEODao getDao() {
        return dao;
    }

    public ResearchIssueEO save(ResearchIssueEO researchIssueEO) {
        researchIssueEO.setId(UUID.randomUUID10());
        researchIssueEO.setDelFlag(DeleteFlagEnum.NORMAL.getValue());
        researchIssueEO.setCreateTime(new Date());
        researchIssueEO.setUpdateTime(new Date());
        dao.insertSelective(researchIssueEO);
        return researchIssueEO;
    }

    public List<ResearchIssueEO> batchSave(List<ResearchIssueEO> researchIssueEOs, String userId) {
        for (ResearchIssueEO eo : researchIssueEOs) {
            eo.setId(UUID.randomUUID10());
            eo.setDelFlag(DeleteFlagEnum.NORMAL.getValue());
            eo.setOrgId(orgEODao.getOrgIdByOrgName(eo.getOrgName()));
            eo.setRstatus(dicTypeEODao.getDicTypeIdByDicIdAndDicTypeName("TQ6TH65SMQ", eo.getStatusStr()));
            eo.setCreateUserId(userId);
            eo.setCreateTime(new Date());
            eo.setUpdateUserId(userId);
            eo.setUpdateTime(new Date());
            dao.insertSelective(eo);
        }
        return researchIssueEOs;
    }

    /**
     * 批量逻辑删除
     */
    public void deleteLogicInBatch(List<String> ids) {
        dao.deleteLogicInBatch(ids);
    }
}