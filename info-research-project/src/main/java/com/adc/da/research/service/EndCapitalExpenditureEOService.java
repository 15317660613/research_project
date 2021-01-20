package com.adc.da.research.service;

import com.adc.da.base.service.BaseService;
import com.adc.da.capital.dao.CapitalExpenditureEODao;
import com.adc.da.capital.entity.CapitalExpenditureEO;
import com.adc.da.capital.page.CapitalExpenditureEOPage;
import com.adc.da.research.dao.EndCapitalExpenditureEODao;
import com.adc.da.research.entity.EndCapitalExpenditureEO;
import com.adc.da.research.page.EndCapitalExpenditureEOPage;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <br>
 * <b>功能：</b>RS_END_CAPITAL_EXPENDITURE EndCapitalExpenditureEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-11-06 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("endCapitalExpenditureEOService")
@Transactional(value = "transactionManager", readOnly = false,
    propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class EndCapitalExpenditureEOService extends BaseService<EndCapitalExpenditureEO, String> {

    @Autowired
    private EndCapitalExpenditureEODao dao;

    public EndCapitalExpenditureEODao getDao() {
        return dao;
    }

    /**
     * @param procBusinessKey
     * @return
     * @throws Exception
     */
    public List<EndCapitalExpenditureEO> getList(String procBusinessKey) {

        EndCapitalExpenditureEOPage page = new EndCapitalExpenditureEOPage();
        page.setProcBusinessKey(procBusinessKey);
        page.setOrderBy("EXT_INFO_3_");
        List<EndCapitalExpenditureEO> resultList = this.getDao().queryByList(page);

        if (CollectionUtils.isEmpty(resultList)) {
            throw new AdcDaBaseException("结算数据异常");
        }
        String projectId = resultList.get(0).getResearchProjectId();

        CapitalExpenditureEOPage sourceQueryPage = new CapitalExpenditureEOPage();
        sourceQueryPage.setResearchProjectId(projectId);
        sourceQueryPage.setOrderBy("EXT_INFO_3_");
        List<CapitalExpenditureEO> sourceList = capitalExpenditureEODao.queryByList(sourceQueryPage);

        int length = resultList.size();
        /*
         * 回显实时数据
         */
        for (int i = 0; i < length; i++) {
            resultList.get(i).setBudget0(sourceList.get(i).getBudget0());
        }

        return resultList;

    }

    @Autowired
    private CapitalExpenditureEODao capitalExpenditureEODao;
}
