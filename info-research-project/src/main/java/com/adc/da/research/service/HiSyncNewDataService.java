package com.adc.da.research.service;

import com.adc.da.capital.dao.CapitalBudgetEODao;
import com.adc.da.capital.dao.CapitalExpenditureDetailEODao;
import com.adc.da.capital.dao.CapitalExpenditureEODao;
import com.adc.da.capital.dao.HiCapitalBudgetEODao;
import com.adc.da.capital.dao.HiCapitalDetailEODao;
import com.adc.da.capital.dao.HiCapitalExpenditureEODao;
import com.adc.da.capital.entity.CapitalBudgetEO;
import com.adc.da.capital.entity.CapitalExpenditureDetailEO;
import com.adc.da.capital.entity.CapitalExpenditureEO;
import com.adc.da.capital.entity.HiCapitalBudgetEO;
import com.adc.da.capital.entity.HiCapitalDetailEO;
import com.adc.da.capital.entity.HiCapitalExpenditureEO;
import com.adc.da.capital.page.HiCapitalDetailEOPage;
import com.adc.da.capital.page.HiCapitalExpenditureEOPage;
import com.adc.da.research.dao.HiInfoEODao;
import com.adc.da.research.dao.HiMemberEODao;
import com.adc.da.research.dao.HiProjectProgressEODao;
import com.adc.da.research.dao.InfoEODao;
import com.adc.da.research.dao.MemberEODao;
import com.adc.da.research.dao.ProgressEODao;
import com.adc.da.research.entity.HiMemberEO;
import com.adc.da.research.entity.HiProjectProgressEO;
import com.adc.da.research.entity.InfoEO;
import com.adc.da.research.entity.MemberEO;
import com.adc.da.research.entity.ProgressEO;
import com.adc.da.research.page.HiMemberEOPage;
import com.adc.da.research.page.HiProjectProgressEOPage;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <br>
 * <b>功能：</b>RS_HI_BASE HiBaseEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-10-24 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service
@Transactional(value = "transactionManager",
    readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class HiSyncNewDataService {

    @Autowired
    private HiInfoEODao hiProjectInfoEODao;

    @Autowired
    private InfoEODao infoEODao;

    @Autowired
    private MemberEODao memberEODao;

    @Autowired
    private HiMemberEODao hiMemberEODao;

    @Autowired
    private CapitalExpenditureDetailEODao capitalExpenditureDetailEODao;

    @Autowired
    private HiCapitalDetailEODao hiCapitalDetailEODao;

    /**
     * 同步数据
     *
     * @param projectId
     * @param businessKey
     */
    public void syncNewData(String projectId, String businessKey) {
        syncInfoDataNew(projectId, businessKey);
        syncMemberDataNew(projectId, businessKey);
        syncProgressDataNew(projectId, businessKey);

        sysCapitalBudgetDataNew(projectId, businessKey);
        sysCapitalExpenditureDataNew(projectId, businessKey);
        sysCapitalDetailDataNew(projectId, businessKey);

    }

    /**
     * 资金支出二级菜单
     *
     * @param projectId
     * @param businessKey
     */
    private void sysCapitalDetailDataNew(String projectId, String businessKey) {
        HiCapitalDetailEOPage queryPage = new HiCapitalDetailEOPage();
        queryPage.setResearchProjectId(projectId);
        queryPage.setProcBusinessKey(businessKey);

        List<HiCapitalDetailEO> list = hiCapitalDetailEODao.queryByList(queryPage);

        List<CapitalExpenditureDetailEO> resultList = new ArrayList<>();
        CapitalExpenditureDetailEO subEO;
        for (HiCapitalDetailEO eo : list) {
            subEO = new CapitalExpenditureDetailEO();
            BeanUtils.copyProperties(eo, subEO);
            resultList.add(subEO);
        }
        if (!resultList.isEmpty()) {
            capitalExpenditureDetailEODao.deleteByProjectId(projectId);
            capitalExpenditureDetailEODao.insertSelectiveAll(resultList);
        }
    }

    @Autowired
    private CapitalExpenditureEODao capitalExpenditureEODao;

    @Autowired
    private HiCapitalExpenditureEODao hiCapitalExpenditureEODao;

    /**
     * 资金支出  18条记录
     *
     * @param projectId
     * @param businessKey
     */
    private void sysCapitalExpenditureDataNew(String projectId, String businessKey) {
        HiCapitalExpenditureEOPage queryPage = new HiCapitalExpenditureEOPage();
        queryPage.setResearchProjectId(projectId);
        queryPage.setProcBusinessKey(businessKey);

        List<HiCapitalExpenditureEO> list = hiCapitalExpenditureEODao.queryByList(queryPage);

        List<CapitalExpenditureEO> resultList = new ArrayList<>();
        CapitalExpenditureEO subEO;
        for (HiCapitalExpenditureEO eo : list) {
            subEO = new CapitalExpenditureEO();
            BeanUtils.copyProperties(eo, subEO);
            resultList.add(subEO);
        }
        if (!resultList.isEmpty()) {
            capitalExpenditureEODao.deleteByProjectId(projectId);
            capitalExpenditureEODao.insertSelectiveAll(resultList);
        }

    }

    @Autowired
    private CapitalBudgetEODao capitalBudgetEODao;

    @Autowired
    private HiCapitalBudgetEODao hiCapitalBudgetEODao;

    /**
     * * 基本信息复制
     *
     * @param businessKey
     */
    private void sysCapitalBudgetDataNew(String projectId, String businessKey) {
        HiCapitalBudgetEO source = hiCapitalBudgetEODao.selectByPrimaryKey(businessKey);
        if (null != source) {
            CapitalBudgetEO result = new CapitalBudgetEO();
            BeanUtils.copyProperties(source, result);
            capitalBudgetEODao.deleteByPrimaryKey(projectId);
            capitalBudgetEODao.insertSelective(result);
        }
    }

    @Autowired
    private ProgressEODao progressEODao;

    @Autowired
    private HiProjectProgressEODao hiProgressEODao;

    @Autowired
    private HiProjectProgressEODao hiProjectProgressEODao;

    /**
     * 项目进度
     * 对 已完成的不做处理
     *
     * @param projectId
     * @param businessKey
     */
    private void syncProgressDataNew(String projectId, String businessKey) {
        HiProjectProgressEOPage queryPage = new HiProjectProgressEOPage();
        queryPage.setResearchProjectId(projectId);
        queryPage.setProcBusinessKey(businessKey);

        List<HiProjectProgressEO> list = hiProjectProgressEODao.queryByList(queryPage);

        List<ProgressEO> sourceList = progressEODao.getRunningProjectInfo(projectId);
        Map<String, ProgressEO> sourceMap = new HashMap<>();
        sourceList.forEach(progressEO -> sourceMap.put(progressEO.getId(), progressEO));
        List<ProgressEO> resultList = new ArrayList<>();
        /*
         *  针对已有记录做额外处理
         */
        list.forEach(eo -> {
            ProgressEO subEO = new ProgressEO();
            String id = eo.getId();
            ProgressEO sourceEO = sourceMap.get(id);
            /*
             * 该记录已完成
             */
            if (null != sourceEO) {
                /*
                 *  采用原始记录，内容采用新内容
                 */
                BeanUtils.copyProperties(sourceEO, subEO);
                subEO.setCheckDetail(eo.getCheckDetail());
                subEO.setDate(eo.getDate());
            } else {
                BeanUtils.copyProperties(eo, subEO);
            }
            resultList.add(subEO);
        });

        if (!resultList.isEmpty()) {
            progressEODao.deleteByProjectId(projectId);
            progressEODao.insertSelectiveAll(resultList);
        }
    }

    /**
     * * 基本信息复制
     *
     * @param businessKey
     */
    private void syncInfoDataNew(String projectId, String businessKey) {
        InfoEO source = hiProjectInfoEODao.selectByPrimaryKey(businessKey);
        if (null != source) {
            InfoEO result = new InfoEO();
            BeanUtils.copyProperties(source, result);
            infoEODao.deleteByPrimaryKey(projectId);
            infoEODao.insertSelective(result);
        }
    }

    /**
     * 同步成员数据
     *
     * @param projectId
     * @param businessKey
     */
    private void syncMemberDataNew(String projectId, String businessKey) {
        /*
         *  成员数据复制
         */
        HiMemberEOPage memberEOPage = new HiMemberEOPage();
        memberEOPage.setProcBusinessKey(businessKey);
        memberEOPage.setResearchProjectId(projectId);

        List<HiMemberEO> memberEOList = hiMemberEODao.queryByList(memberEOPage);

        List<MemberEO> targetList = new ArrayList<>();
        MemberEO subEO;
        for (HiMemberEO memberEO : memberEOList) {
            subEO = new MemberEO();
            BeanUtils.copyProperties(memberEO, subEO);
            targetList.add(subEO);
        }
        if (!targetList.isEmpty()) {
            memberEODao.deleteByProjectId(projectId);

            memberEODao.insertSelectiveAll(targetList);
        }
    }

}
