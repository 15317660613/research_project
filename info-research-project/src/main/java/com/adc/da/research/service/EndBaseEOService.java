package com.adc.da.research.service;

import com.adc.da.base.service.BaseService;
import com.adc.da.capital.dao.CapitalExpenditureEODao;
import com.adc.da.capital.entity.CapitalExpenditureEO;
import com.adc.da.capital.page.CapitalExpenditureEOPage;
import com.adc.da.research.dao.EndBaseEODao;
import com.adc.da.research.dao.EndCapitalExpenditureEODao;
import com.adc.da.research.dao.EndProjectKpiDeliverableEODao;
import com.adc.da.research.dao.EndProjectKpiEODao;
import com.adc.da.research.dao.EndProjectSummaryEODao;
import com.adc.da.research.dao.HiBaseEODao;
import com.adc.da.research.entity.EndBaseEO;
import com.adc.da.research.entity.EndBaseInterface;
import com.adc.da.research.entity.EndCapitalExpenditureEO;
import com.adc.da.research.entity.EndProjectKpiEO;
import com.adc.da.research.entity.EndProjectSummaryEO;
import com.adc.da.research.entity.HiBaseEO;
import com.adc.da.research.page.EndProjectKpiDeliverableEOPage;
import com.adc.da.research.page.HiBaseEOPage;
import com.adc.da.research.utils.HiConstant;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.StringUtils;
import com.adc.da.util.utils.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.adc.da.research.utils.HiConstant.BUSINESS_STATUS;
import static com.adc.da.research.utils.HiConstant.DRAFT_STATUS;

/**
 * <br>
 * <b>功能：</b>RS_END_BASE EndBaseEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-11-06 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("endBaseEOService")
@Transactional(value = "transactionManager", readOnly = false,
    propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class EndBaseEOService extends BaseService<EndBaseEO, String> {

    @Autowired
    private EndBaseEODao dao;

    @Autowired
    private HiBaseEODao hiBaseEODao;

    public EndBaseEODao getDao() {
        return dao;
    }

    /**
     * 初始化id
     * 结项 流程Key RSE开头
     *
     * @return
     */
    private String initBusinessKey() {
        return "RSE_" + UUID.randomUUID10();
    }

    /**
     * @param businessKey
     * @param expectedValue
     * @see HiConstant
     */
    public void updateHiBaseEOStatus(String businessKey, Integer expectedValue, boolean isHiBaseDao) {

        if (isHiBaseDao) {
            /*
             * 变更
             */
            HiBaseEO hiBaseEO = hiBaseEODao.selectByPrimaryKey(businessKey);
            Integer status = hiBaseEO.getExtInfo1();
            /*
             * 不符合进行更新
             */
            if (!status.equals(expectedValue)) {
                hiBaseEO.setExtInfo1(expectedValue);
                hiBaseEODao.updateByPrimaryKeySelective(hiBaseEO);
            }
        } else {
            /*
             *  结项
             */
            EndBaseEO hiBaseEO = dao.selectByPrimaryKey(businessKey);
            Integer status = hiBaseEO.getExtInfo1();
            /*
             * 不符合进行更新
             */
            if (!status.equals(expectedValue)) {
                hiBaseEO.setExtInfo1(expectedValue);
                dao.updateByPrimaryKeySelective(hiBaseEO);
            }
        }

    }

    /**
     * 获取businessKey
     *
     * @param projectId
     * @return
     */
    public String getLastBusinessKey(String projectId) {
        String businessKey;
        HiBaseEOPage queryPae = new HiBaseEOPage();
        queryPae.setResearchProjectId(projectId);
        queryPae.setExtInfo1(DRAFT_STATUS);
        int count = dao.queryByCount(queryPae);

        if (0 != count) {

            /*
             *  获取现有的Key
             */
            queryPae.setOrder("version desc");
            List<EndBaseEO> list = this.dao.queryByList(queryPae);
            businessKey = list.get(0).getProcBusinessKey();

        } else {
            /*
             * 查询已有的记录 业务状态的记录 条数，算出版本号 新建一条记录
             */
            queryPae.setExtInfo1(BUSINESS_STATUS);
            int version = dao.queryByCount(queryPae) + 1;
            businessKey = initBusinessKey();

            EndBaseEO eo = new EndBaseEO();
            eo.setVersion(version);
            eo.setProcBusinessKey(businessKey);
            eo.setResearchProjectId(projectId);
            this.getDao().insertSelective(eo);

            syncSummary(projectId, businessKey);
            syncKpi(projectId, businessKey);
            syncCapitalExpenditure(projectId, businessKey);

        }
        return businessKey;
    }

    @Autowired
    private EndProjectSummaryEODao summaryEODao;

    private void syncSummary(String projectId, String businessKey) {
        EndProjectSummaryEO eo = new EndProjectSummaryEO();
        setProjectIdAndBusinessKey(eo, projectId, businessKey);
        summaryEODao.insert(eo);
    }

    /**
     * 设置projectId 与 businessKey
     *
     * @param x
     * @param projectId
     * @param businessKey
     * @param <X>
     */
    private <X extends EndBaseInterface> void setProjectIdAndBusinessKey(X x, String projectId, String businessKey) {
        x.setProcBusinessKey(businessKey);
        x.setResearchProjectId(projectId);
    }

    @Autowired
    private EndProjectKpiEODao kpiEODao;

    /**
     * 同步考核指标
     *
     * @param projectId
     * @param businessKey
     */
    private void syncKpi(String projectId, String businessKey) {
        EndProjectKpiEO eo = new EndProjectKpiEO();
        setProjectIdAndBusinessKey(eo, projectId, businessKey);
        kpiEODao.insert(eo);
    }

    @Autowired
    private CapitalExpenditureEODao capitalExpenditureEODao;

    @Autowired
    private EndCapitalExpenditureEODao endCapitalExpenditureEODao;

    /**
     * 同步资金结算
     *
     * @param projectId
     * @param businessKey
     */
    private void syncCapitalExpenditure(String projectId, String businessKey) {
        CapitalExpenditureEOPage queryPage = new CapitalExpenditureEOPage();
        queryPage.setResearchProjectId(projectId);
        List<CapitalExpenditureEO> list = capitalExpenditureEODao.queryByList(queryPage);
        List<EndCapitalExpenditureEO> saveList = new ArrayList<>();
        list.forEach(sourceEO -> {
            EndCapitalExpenditureEO target = new EndCapitalExpenditureEO();
            target.setBudget0(sourceEO.getBudget0());
            target.setId(sourceEO.getId());
            target.setExtInfo2(sourceEO.getExtInfo2());
            target.setExtInfo3(sourceEO.getExtInfo3());
            target.setResearchProjectId(sourceEO.getResearchProjectId());
            target.setProcBusinessKey(businessKey);
            saveList.add(target);
        });
        if (CollectionUtils.isNotEmpty(saveList)) {
            endCapitalExpenditureEODao.insertSelectiveAll(saveList);
        }
    }

    @Autowired
    private EndProjectKpiDeliverableEODao endProjectKpiDeliverableEODao;

    @Autowired
    private EndProjectKpiEODao endProjectKpiEODao;

    @Autowired
    private EndProjectSummaryEODao endProjectSummaryEODao;

    /**
     * 校验表单填写情况
     *
     * @param businessKey
     * @return
     */
    public String check(String businessKey) {
        EndProjectSummaryEO summaryEO = endProjectSummaryEODao.selectByPrimaryKey(businessKey);
        if (StringUtils.isEmpty(summaryEO.getTotalTargetAct())) {
            return ("项目简介 未填写");
        }

        EndProjectKpiEO kpi = endProjectKpiEODao.selectByPrimaryKey(businessKey);
        if (null == kpi || StringUtils.isEmpty(kpi.getNumAppearancePatents())) {
            return "考核指标 指标类别 未填写";
        }
        EndProjectKpiDeliverableEOPage queryPage = new EndProjectKpiDeliverableEOPage();
        queryPage.setProcBusinessKey(businessKey);
        int kpiDeliverableCount = endProjectKpiDeliverableEODao.queryByCount(queryPage);
        if (0 == kpiDeliverableCount) {
            return ("考核指标 交付物的技术指标（实际完成） 未填写");
        }

        return "";
    }

}
