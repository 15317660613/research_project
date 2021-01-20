package com.adc.da.research.service;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.base.page.BasePage;
import com.adc.da.base.service.BaseService;
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
import com.adc.da.capital.page.CapitalExpenditureDetailEOPage;
import com.adc.da.capital.page.CapitalExpenditureEOPage;
import com.adc.da.capital.page.HiCapitalBudgetEOPage;
import com.adc.da.capital.page.HiCapitalDetailEOPage;
import com.adc.da.capital.page.HiCapitalExpenditureEOPage;
import com.adc.da.research.dao.HiBaseEODao;
import com.adc.da.research.dao.HiInfoEODao;
import com.adc.da.research.dao.HiMemberEODao;
import com.adc.da.research.dao.HiProjectProgressEODao;
import com.adc.da.research.dao.InfoEODao;
import com.adc.da.research.dao.MemberEODao;
import com.adc.da.research.dao.ProgressEODao;
import com.adc.da.research.entity.HiBaseEO;
import com.adc.da.research.entity.HiBaseInterface;
import com.adc.da.research.entity.HiInfoEO;
import com.adc.da.research.entity.HiMemberEO;
import com.adc.da.research.entity.HiProjectProgressEO;
import com.adc.da.research.entity.InfoEO;
import com.adc.da.research.entity.MemberEO;
import com.adc.da.research.entity.ProgressEO;
import com.adc.da.research.page.HiBaseEOPage;
import com.adc.da.research.page.HiBasePageInterface;
import com.adc.da.research.page.HiInfoEOPage;
import com.adc.da.research.page.HiMemberEOPage;
import com.adc.da.research.page.HiProjectProgressEOPage;
import com.adc.da.research.page.MemberEOPage;
import com.adc.da.research.page.ProgressEOPage;
import com.adc.da.research.page.RSBasePage;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.StringUtils;
import com.adc.da.util.utils.UUID;
import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.adc.da.research.utils.HiConstant.BUSINESS_STATUS;
import static com.adc.da.research.utils.HiConstant.DRAFT_STATUS;

/**
 * <br>
 * <b>功能：</b>RS_HI_BASE HiBaseEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-10-24 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("hiBaseEOService")
@Transactional(value = "transactionManager",
    readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class HiBaseEOService extends BaseService<HiBaseEO, String> {

    @Autowired
    private HiBaseEODao dao;

    @Autowired
    private HiInfoEODao hiProjectInfoEODao;

    @Autowired
    private InfoEODao infoEODao;

    @Autowired
    private MemberEODao memberEODao;

    @Autowired
    private HiMemberEODao hiMemberEODao;

    public HiBaseEODao getDao() {
        return dao;
    }

    /**
     * 科研变更流程 id 追加 RSC_前缀
     *
     * @param t
     * @return
     * @throws Exception
     */
    @Override
    public int insertSelective(HiBaseEO t) throws Exception {
        t.setProcBusinessKey(initBusinessKey());
        return this.getDao().insertSelective(t);
    }

    /**
     * 初始化id
     *
     * @return
     */
    private String initBusinessKey() {
        return "RSC_" + UUID.randomUUID10();
    }

    @Autowired
    private HistoryService historyService;

    /**
     * 根据流程实例id获取businessKey
     *
     * @param procInstId
     * @return
     */
    public String getBusinessKeyByProcInstId(String procInstId) {
        HistoricProcessInstance result = historyService.createHistoricProcessInstanceQuery()
                                                       .processInstanceId(procInstId)
                                                       .singleResult();
        if (null != result) {
            return result.getBusinessKey();
        } else {
            throw new AdcDaBaseException("流程id错误");
        }
    }

    /**
     * 获取 项目对应的key值
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
            List<HiBaseEO> list = this.dao.queryByList(queryPae);
            businessKey = list.get(0).getProcBusinessKey();

        } else {
            /*
             * 查询已有的记录 业务状态的记录 条数，算出版本号 新建一条记录
             */
            queryPae.setExtInfo1(BUSINESS_STATUS);
            int version = dao.queryByCount(queryPae) + 1;
            businessKey = initBusinessKey();

            HiBaseEO eo = new HiBaseEO();
            eo.setVersion(version);
            eo.setProcBusinessKey(businessKey);
            eo.setResearchProjectId(projectId);
            this.getDao().insertSelective(eo);

            /*
             * 基本信息复制
             */
            syncInfoData(projectId, businessKey);

            /*
             *  成员数据复制
             */
            syncMemberData(projectId, businessKey);

            /*
             * 项目进度
             */
            syncProgressData(projectId, businessKey);

            /*
             * 资金来源
             */
            sysCapitalBudgetData(projectId, businessKey);

            /*
             * 资金支出
             */
            sysCapitalExpenditureData(projectId, businessKey);

            /*
             * 资金支出 二级菜单
             */
            sysCapitalExpenditureDetailData(projectId, businessKey);
        }
        return businessKey;
    }

    @Autowired
    private CapitalExpenditureDetailEODao capitalExpenditureDetailEODao;

    @Autowired
    private HiCapitalDetailEODao hiCapitalDetailEODao;

    /**
     * 资金支出二级菜单
     *
     * @param projectId
     * @param businessKey
     */
    private void sysCapitalExpenditureDetailData(String projectId, String businessKey) {
        CapitalExpenditureDetailEOPage queryPage = new CapitalExpenditureDetailEOPage();
        queryPage.setResearchProjectId(projectId);

        List<CapitalExpenditureDetailEO> list = capitalExpenditureDetailEODao.queryByList(queryPage);

        List<HiCapitalDetailEO> resultList = new ArrayList<>();
        HiCapitalDetailEO subEO;
        for (CapitalExpenditureDetailEO memberEO : list) {
            subEO = new HiCapitalDetailEO();
            BeanUtils.copyProperties(memberEO, subEO);
            subEO.setProcBusinessKey(businessKey);
            resultList.add(subEO);
        }
        if (!resultList.isEmpty()) {
            hiCapitalDetailEODao.insertSelectiveAll(resultList);
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
    private void sysCapitalExpenditureData(String projectId, String businessKey) {
        CapitalExpenditureEOPage queryPage = new CapitalExpenditureEOPage();
        queryPage.setResearchProjectId(projectId);

        List<CapitalExpenditureEO> list = capitalExpenditureEODao.queryByList(queryPage);

        List<HiCapitalExpenditureEO> resultList = new ArrayList<>();
        HiCapitalExpenditureEO subEO;
        for (CapitalExpenditureEO memberEO : list) {
            subEO = new HiCapitalExpenditureEO();
            BeanUtils.copyProperties(memberEO, subEO);
            subEO.setProcBusinessKey(businessKey);
            resultList.add(subEO);
        }
        if (!resultList.isEmpty()) {
            hiCapitalExpenditureEODao.insertSelectiveAll(resultList);
        }

    }

    @Autowired
    private CapitalBudgetEODao capitalBudgetEODao;

    @Autowired
    private HiCapitalBudgetEODao hiCapitalBudgetEODao;

    /**
     * * 基本信息复制
     *
     * @param projectId
     * @param businessKey
     */
    private void sysCapitalBudgetData(String projectId, String businessKey) {

        CapitalBudgetEO source = capitalBudgetEODao.selectByPrimaryKey(projectId);

        if (null != source) {
            HiCapitalBudgetEO result = new HiCapitalBudgetEO();
            copyAndSetBusinessKey(businessKey, source, result);
            hiCapitalBudgetEODao.insertSelective(result);

        }
    }

    /**
     * 初始化对象
     *
     * @param businessKey
     * @param source
     * @param result
     * @param <X>
     */
    private <X extends HiBaseInterface> void copyAndSetBusinessKey(String businessKey, Object source, X result) {
        BeanUtils.copyProperties(source, result);
        result.setProcBusinessKey(businessKey);
    }

    @Autowired
    private ProgressEODao progressEODao;

    @Autowired
    private HiProjectProgressEODao hiProgressEODao;

    /**
     * 项目进度
     *
     * @param projectId
     * @param businessKey
     */
    private void syncProgressData(String projectId, String businessKey) {
        ProgressEOPage queryPage = new ProgressEOPage();
        queryPage.setResearchProjectId(projectId);

        List<ProgressEO> list = progressEODao.queryByList(queryPage);

        List<HiProjectProgressEO> resultList = new ArrayList<>();
        HiProjectProgressEO subEO;
        for (ProgressEO memberEO : list) {
            subEO = new HiProjectProgressEO();
            BeanUtils.copyProperties(memberEO, subEO);
            subEO.setProcBusinessKey(businessKey);
            resultList.add(subEO);
        }
        if (!resultList.isEmpty()) {
            hiProgressEODao.insertSelectiveAll(resultList);
        }
    }

    /**
     * * 基本信息复制
     *
     * @param projectId
     * @param businessKey
     */
    private void syncInfoData(String projectId, String businessKey) {
        InfoEO source = infoEODao.selectByPrimaryKey(projectId);
        if (null != source) {
            HiInfoEO result = new HiInfoEO();
            copyAndSetBusinessKey(businessKey, source, result);
            hiProjectInfoEODao.insertSelective(result);
        }
    }

    /**
     * 同步成员数据
     *
     * @param projectId
     * @param businessKey
     */
    private void syncMemberData(String projectId, String businessKey) {
        /*
         *  成员数据复制
         */
        MemberEOPage memberEOPage = new MemberEOPage();
        memberEOPage.setResearchProjectId(projectId);

        List<MemberEO> memberEOList = memberEODao.queryByList(memberEOPage);

        List<HiMemberEO> hiMemberEOList = new ArrayList<>();
        HiMemberEO subEO;
        for (MemberEO memberEO : memberEOList) {
            subEO = new HiMemberEO();
            BeanUtils.copyProperties(memberEO, subEO);
            subEO.setProcBusinessKey(businessKey);
            hiMemberEOList.add(subEO);
        }
        if (!hiMemberEOList.isEmpty()) {
            hiMemberEODao.insertSelectiveAll(hiMemberEOList);
        }
    }

    /** 基本信息 */
    private static final int INFO = 0;

    /** 成员信息 */
    private static final int MEMBER = 1;

    /** 进度信息 */
    private static final int PROGRESS = 2;

    /** 资金来源信息 */
    private static final int BUDGET = 3;

    /** 资金支出信息 */
    private static final int EXPENDITURE = 4;

    /** 资金支出二级菜单信息 */
    private static final int DETAIL = 5;

    /**
     * 查询条件
     *
     * @param businessKey
     * @return
     */
    private HiBasePageInterface[] initQueryPage(String businessKey) {
        /*
         * 初始化page 数组
         */
        HiBasePageInterface[] queryPage = new HiBasePageInterface[6];
        queryPage[INFO] = new HiInfoEOPage();
        queryPage[BUDGET] = new HiCapitalBudgetEOPage();
        queryPage[MEMBER] = new HiMemberEOPage();
        queryPage[PROGRESS] = new HiProjectProgressEOPage();
        queryPage[EXPENDITURE] = new HiCapitalExpenditureEOPage();
        queryPage[DETAIL] = new HiCapitalDetailEOPage();
        /* 设置查询条件 */
        for (HiBasePageInterface page : queryPage) {
            page.setProcBusinessKey(businessKey);
        }
        return queryPage;
    }

    /**
     * 查询条件 - 源数据
     *
     * @param projectId
     * @return
     */
    private RSBasePage[] initSourceQueryPage(String projectId) {
        /*
         * 初始化page 数组  ,与 initQueryPage保持一致，因此数组长度为6,
         * 0,3,4为null
         */
        RSBasePage[] queryPage = new RSBasePage[6];
        /* 存在删除 */
        queryPage[MEMBER] = new MemberEOPage();
        /* 存在删除 */
        queryPage[PROGRESS] = new ProgressEOPage();
        /*
         * 二级菜单，另外处理
         */
        queryPage[DETAIL] = new CapitalExpenditureDetailEOPage();
        /* 设置查询条件 */
        for (RSBasePage page : queryPage) {
            if (null != page) {
                page.setResearchProjectId(projectId);
            }
        }
        return queryPage;
    }

    /**
     * 菜单大小
     */
    private static final int MENU_SIZE = 16;

    /**
     *
     */
    private static final int FIRST_MENU_DAO_SIZE = 5;

    /**
     * @param businessKey
     * @return
     */
    public int[] getMenu(String businessKey, String projectId) {
        /*
         * 初始化page 数组
         */
        HiBasePageInterface[] queryPage = initQueryPage(businessKey);
        RSBasePage[] sourceQueryPage = initSourceQueryPage(projectId);

        /*
         * 初始化dao数组
         */
        BaseDao[] daoArray = new BaseDao[FIRST_MENU_DAO_SIZE];
        daoArray[INFO] = hiProjectInfoEODao;
        daoArray[MEMBER] = hiMemberEODao;
        daoArray[PROGRESS] = hiProgressEODao;
        daoArray[BUDGET] = hiCapitalBudgetEODao;
        daoArray[EXPENDITURE] = hiCapitalExpenditureEODao;

        BaseDao[] sourceDaoArray = new BaseDao[FIRST_MENU_DAO_SIZE];
        sourceDaoArray[INFO] = null;
        sourceDaoArray[MEMBER] = memberEODao;
        sourceDaoArray[PROGRESS] = progressEODao;
        sourceDaoArray[BUDGET] = null;
        sourceDaoArray[EXPENDITURE] = null;
        /*
         * 非变更字段，始终为0
         * 0 "基本信息"
         * 1 "成员信息"
         *  "合同简介"
         *  "考核指标"
         * 4 "工作进度安排"
         * 5 "资金来源"
         * 6 "资金支出"
         * 7 "设备费"
         * 8 "材料费"
         * 9 "测试化验加工费"
         * 10 "燃料动力费"
         * 11 "会议费"
         * 12 "国际合作与交流费"
         * 13 "资料事务费"
         * 14 "软件购置费"
         *  "变更说明"
         *
         * For Special Page:
         *  FormCount-NowCount+2*ChangeCount
         *  FC - NC + 2 * CC
         *
         */
        int[] index = {0, 1, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14};
        int[] count = new int[MENU_SIZE];
        for (int i = 0; i < daoArray.length; i++) {
            if (null == sourceDaoArray[i]) {
                queryPage[i].setMask("1");
                queryPage[i].setMaskOperator("!=");
                count[index[i]] += daoArray[i].queryByCount((RSBasePage) queryPage[i]);
            } else {
                @SuppressWarnings("unchecked")
                List<HiBaseInterface> z = daoArray[i].queryByList((RSBasePage) queryPage[i]);
                /* NC */
                count[index[i]] -= z.size();
                for (HiBaseInterface eo : z) {
                    /* 2 * CC */
                    if (StringUtils.isNotEmpty(eo.getMask())) {
                        count[index[i]] += 2;
                    }
                }

                /* FC */
                count[index[i]] += sourceDaoArray[i].queryByCount(sourceQueryPage[i]);
            }
        }

        /* 二级菜单额外处理 */
        List<CapitalExpenditureDetailEO> sourceSecondaryMenuList = capitalExpenditureDetailEODao
            .queryByList(sourceQueryPage[DETAIL]);
        List<HiCapitalDetailEO> secondaryMenuList = hiCapitalDetailEODao.queryByList((BasePage) queryPage[DETAIL]);
        countSecondaryMenu(sourceSecondaryMenuList, secondaryMenuList, count, index);

        return count;
    }

    /**
     * 二级菜单额外处理
     *
     * @param sourceSecondaryMenuList 原菜单
     * @param secondaryMenuList       现菜单
     * @param count                   计数
     * @param index                   索引
     */
    private void countSecondaryMenu(List<CapitalExpenditureDetailEO> sourceSecondaryMenuList,
        List<HiCapitalDetailEO> secondaryMenuList,
        int[] count, int[] index) {

        String[] detailType = {"设备费", "材料费", "加工费", "动力费", "会议费", "交流费", "事务费", "购置费"};

        List<String> menu = new ArrayList<>(Arrays.asList(detailType));
        int indexIndex;
        /*
         *  前置偏移
         * For Special Page:
         *  FormCount-NotChangeCount+ChangeCount
         *  FC - NCC +  CC
         */
        int prefix = 5;
        for (HiCapitalDetailEO eo : secondaryMenuList) {
            /*
             * 对于null的记录做减法
             */
            if (StringUtils.isEmpty(eo.getMask())) {
                /* NCC */
                indexIndex = menu.indexOf(eo.getDetailType()) + prefix;
                count[index[indexIndex]]--;
            } else {
                /*此处为存在变更的数据 CC */
                indexIndex = menu.indexOf(eo.getDetailType()) + prefix;
                count[index[indexIndex]]++;
            }
        }
        for (CapitalExpenditureDetailEO eo : sourceSecondaryMenuList) {
            /*
             * 对应字段做加法 FC
             */
            indexIndex = menu.indexOf(eo.getDetailType()) + prefix;
            count[index[indexIndex]]++;
        }

    }
}
