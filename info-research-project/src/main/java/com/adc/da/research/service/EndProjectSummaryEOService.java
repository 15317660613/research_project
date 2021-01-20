package com.adc.da.research.service;

import com.adc.da.base.service.BaseService;
import com.adc.da.research.dao.EndProjectSummaryEODao;
import com.adc.da.research.dao.SummaryEODao;
import com.adc.da.research.entity.EndProjectSummaryEO;
import com.adc.da.research.entity.SummaryEO;
import com.adc.da.util.exception.AdcDaBaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

/**
 * <br>
 * <b>功能：</b>RS_END_PROJECT_SUMMARY EndProjectSummaryEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-11-06 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("endProjectSummaryEOService")
@Transactional(value = "transactionManager", readOnly = false,
    propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class EndProjectSummaryEOService extends BaseService<EndProjectSummaryEO, String> {

    @Autowired
    private EndProjectSummaryEODao dao;

    @Autowired
    private SummaryEODao summaryEODao;

    public EndProjectSummaryEODao getDao() {
        return dao;
    }

    /**
     * 获取项目简介
     *
     * @param procBusinessKey
     * @param projectId
     * @return
     * @throws Exception
     */
    public List<EndProjectSummaryEO> getSummary(String procBusinessKey, String projectId) {

        EndProjectSummaryEO result = this.getDao().selectByPrimaryKey(procBusinessKey);
        if (null == result) {
            throw new AdcDaBaseException("数据异常");
        }
        SummaryEO source = summaryEODao.selectByProjectId(projectId);
        if (null == source) {
            throw new AdcDaBaseException("源数据异常");
        }
        result.setResearchProjectContent(source.getResearchProjectContent());
        result.setTotalTarget(source.getTotalTarget());

        return Collections.singletonList(result);
    }
}
