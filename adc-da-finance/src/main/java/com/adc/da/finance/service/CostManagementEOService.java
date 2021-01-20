package com.adc.da.finance.service;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import cn.hutool.core.date.DateTime;
import com.adc.da.base.service.BaseService;
import com.adc.da.dao.NoticeEODao;
import com.adc.da.entity.NoticeEO;
import com.adc.da.finance.dao.BusinessGonfigEODao;
import com.adc.da.finance.dao.CostManagementEODao;
import com.adc.da.finance.dao.CostReceiverEODao;
import com.adc.da.finance.entity.BusinessGonfigEO;
import com.adc.da.finance.entity.CostManagementEO;
import com.adc.da.finance.entity.CostReceiverEO;
import com.adc.da.finance.handler.CostManagementHandler;
import com.adc.da.finance.page.MyCostManagementEOPage;
import com.adc.da.login.util.UserUtils;
import com.adc.da.sys.dao.OrgEODao;
import com.adc.da.sys.dao.UserEODao;
import com.adc.da.sys.entity.OrgEO;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.StringUtils;
import com.adc.da.util.utils.UUID;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.*;

/**
 * <br>
 * <b>功能：</b>F_COST_MANAGEMENT CostManagementEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-04-17 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("costManagementEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class CostManagementEOService extends BaseService<CostManagementEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(CostManagementEOService.class);

    @Autowired
    private CostManagementEODao dao;

    @Autowired
    private OrgEODao orgEODao;

    @Autowired
    private UserEODao userEODao;

    @Autowired
    private CostReceiverEODao costReceiverEODao;

    @Autowired
    private NoticeEODao noticeEODao;

    @Autowired
    private BusinessGonfigEODao businessGonfigEODao;

    @Autowired
    private ProfitManagementEOService profitManagementEOService;

    @Autowired
    private CashflowManagementEOService cashflowManagementEOService;


    public CostManagementEODao getDao() {
        return dao;
    }

    public void logicDeleteByPrimaryKey(List<String> idList) throws Exception {
        List<CostManagementEO> costManagementEOList = dao.selectByPrimaryKeys(idList);
        dao.logicDeleteByPrimaryKeys(idList);
        for (CostManagementEO costManagementEO : costManagementEOList) {
            if (StringUtils.isEmpty(costManagementEO.getBusinessId())) {
                continue;
            }
            BusinessGonfigEO businessGonfigEO = businessGonfigEODao.selectByPrimaryKey(costManagementEO.getBusinessId());
            if (StringUtils.equals(businessGonfigEO.getBgType(), "0")) {
                if (StringUtils.isNotEmpty(costManagementEO.getYear())
                        && StringUtils.isNotEmpty(costManagementEO.getMonth())
                        && StringUtils.isNotEmpty(costManagementEO.getBusinessId())) {
                    profitManagementEOService.updateProfitByBusinessGonfigIdAndYearAndMonth(
                            costManagementEO.getBusinessId(),
                            String.valueOf(costManagementEO.getYear()),
                            String.valueOf(costManagementEO.getMonth()));
                    cashflowManagementEOService.updateCashflowByBusinessGonfigIdAndYearAndMonth(
                            costManagementEO.getBusinessId(),
                            String.valueOf(costManagementEO.getYear()),
                            String.valueOf(costManagementEO.getMonth()));
                }
            }
        }

    }

    public void goBackByPrimaryKey(String id) throws Exception {
        UserEO userEO = UserUtils.getUser();
        if (null == userEO) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        DateTime dateTime = new DateTime();
        CostManagementEO costManagementEO = dao.selectByPrimaryKey(id);
        if (null == costManagementEO) {
            throw new AdcDaBaseException("退回不存在！");
        }
        List<NoticeEO> noticeEOList = new ArrayList<>();
        Map<String, String> userIdStatusMap = new HashMap<>();

        String status = "正常";
        if (!dateTime.isBeforeOrEquals(costManagementEO.getDeadlineTime())) {
            status = "逾期";
            costManagementEO.setStatus(-1);
        }
        List<CostReceiverEO> costReceiverEOList = costReceiverEODao.queryAll();
        Map<String, CostReceiverEO> orgIdCostReceiverEOMap = getOrgIdCostReceiverEOMap(costReceiverEOList);
        CostReceiverEO costReceiverEO = orgIdCostReceiverEOMap.get(costManagementEO.getOrgId());
        if (null == costReceiverEO) {
            throw new AdcDaBaseException("组织机构 " + costManagementEO.getOrgName() + " 没有设置接收人,请检查！");
        }

        String userIds = costReceiverEO.getUserIds();
        if (StringUtils.isNotEmpty(userIds)) {
            String[] userIdArr = userIds.split(",");
            for (String userId : userIdArr) {
                noticeEODao.deleteNoticeByReceiveUserIdEqualAndNoticeTypeEqual(userId, "finance_cost");
                String stat = userIdStatusMap.get(userId);
                if (null == stat) {
                    userIdStatusMap.put(userId, status);
                } else if (StringUtils.equals(stat, "正常")) {
                    userIdStatusMap.put(userId, status);
                }
            }
        }
        dao.goBackByPrimaryKey(id);
        //dao.updateByPrimaryKeySelective()
        BusinessGonfigEO businessGonfigEO = businessGonfigEODao.selectByPrimaryKey(costManagementEO.getBusinessId());
        if (StringUtils.equals(businessGonfigEO.getBgType(), "0")) {
            profitManagementEOService.updateProfitByBusinessGonfigIdAndYearAndMonth(
                    costManagementEO.getBusinessId(),
                    String.valueOf(costManagementEO.getYear()),
                    String.valueOf(costManagementEO.getMonth()));
            cashflowManagementEOService.updateCashflowByBusinessGonfigIdAndYearAndMonth(
                    costManagementEO.getBusinessId(),
                    String.valueOf(costManagementEO.getYear()),
                    String.valueOf(costManagementEO.getMonth()));
        }
        Set<String> userIdSet = userIdStatusMap.keySet();
        if (CollectionUtils.isNotEmpty(userIdSet)) {
            Map<String, String> userIdNameMap = getUserIdNameMap(userIdSet);
            for (String userId : userIdSet)
                noticeEOList.add(
                        NoticeEO.builder()
                                .id(UUID.randomUUID10())
                                .createTime(new DateTime())
                                .updateTime(new DateTime())
                                .createUserId(userEO.getUsid())
                                .noticeContent(userIdStatusMap.get(userId) + "财务成本认领通知")
                                .noticeName("财务成本认领通知")
                                .receiveUserIds(userId)
                                .noticeTypeId("finance_cost")
                                .receiveUserNames(userIdNameMap.get(userId))
                                .build()
                );

        }
        noticeEODao.insertList(noticeEOList);


    }
    /**
     * 导入
     */

    public void excelImport(InputStream is) throws Exception {
        UserEO userEO = UserUtils.getUser();
        if (null == userEO) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        DateTime dateTime = new DateTime();
        List<OrgEO> orgEOList = orgEODao.queryOrgAll();
        Map<String, String> orgNameIdMap = getOrgNameIdMap(orgEOList);

        List<CostReceiverEO> costReceiverEOList = costReceiverEODao.queryAll();
        Map<String, CostReceiverEO> orgIdCostReceiverEOMap = getOrgIdCostReceiverEOMap(costReceiverEOList);

        List<NoticeEO> noticeEOList = new ArrayList<>();
        Map<String, String> userIdStatusMap = new HashMap<>();

        ImportParams params = new ImportParams();
        params.setNeedVerfiy(true);
        params.setVerifyHandler(new CostManagementHandler());
        ExcelImportResult<CostManagementEO> result = ExcelImportUtil.importExcelMore(is, CostManagementEO.class, params);
        // 如果校验不通过，返回错误信息
        if (result.isVerfiyFail()) {
            List<Integer> rowindexs = new ArrayList<>();
            logger.error("excel校验不通过！");
            List<CostManagementEO> errors = result.getFailList();
            StringBuilder sb = new StringBuilder();
            for (CostManagementEO error : errors) {
                rowindexs.add(error.getRowNum() + 1);
//                sb.append("第 " + error.getRowNum() + "、");
            }
            sb.append("第 ");
            sb.append(StringUtils.join(rowindexs, '、'));
            sb.append(" 行数据不合法，请检查！");
            throw new AdcDaBaseException(sb.toString());
        }
        List<CostManagementEO> costManagementEOList = result.getList();

        //导入
        for (CostManagementEO costManagementEO : costManagementEOList) {
            costManagementEO.setId(UUID.randomUUID10());
            String orgId = orgNameIdMap.get(costManagementEO.getOrgName());
            if (null == orgId) {
                throw new AdcDaBaseException("组织机构 " + costManagementEO.getOrgName() + " 不存在,请检查！");
            }
            CostReceiverEO costReceiverEO = orgIdCostReceiverEOMap.get(orgId);
            if (null == costReceiverEO) {
                throw new AdcDaBaseException("组织机构 " + costManagementEO.getOrgName() + " 没有设置接收人,请检查！");
            }

            String status = "正常";
            if (!dateTime.isBeforeOrEquals(costManagementEO.getDeadlineTime())) {
                status = "逾期";
                costManagementEO.setStatus(-1);
            }
            String userIds = costReceiverEO.getUserIds();
            if (StringUtils.isNotEmpty(userIds)) {
                String[] userIdArr = userIds.split(",");
                for (String userId : userIdArr) {
                    noticeEODao.deleteNoticeByReceiveUserIdEqualAndNoticeTypeEqual(userId, "finance_cost");
                    String stat = userIdStatusMap.get(userId);
                    if (null == stat) {
                        userIdStatusMap.put(userId, status);
                    } else if (StringUtils.equals(stat, "正常")) {
                        userIdStatusMap.put(userId, status);
                    }
                }
            }
            costManagementEO.setDistributionUserId(userEO.getUsid());
            costManagementEO.setDistributionUserName(userEO.getUsname());
            costManagementEO.setDistributionTime(dateTime);
            costManagementEO.setOrgId(orgId);
        }

        dao.insertList(costManagementEOList);
        Set<String> userIdSet = userIdStatusMap.keySet();
        if (CollectionUtils.isNotEmpty(userIdSet)) {
            Map<String, String> userIdNameMap = getUserIdNameMap(userIdSet);
            for (String userId : userIdSet)
                noticeEOList.add(
                        NoticeEO.builder()
                                .id(UUID.randomUUID10())
                                .createTime(new DateTime())
                                .updateTime(new DateTime())
                                .createUserId(userEO.getUsid())
                                .noticeContent(userIdStatusMap.get(userId) + "财务成本认领通知")
                                .noticeName("财务成本认领通知")
                                .receiveUserIds(userId)
                                .noticeTypeId("finance_cost")
                                .receiveUserNames(userIdNameMap.get(userId))
                                .build()
                );

        }
        noticeEODao.insertList(noticeEOList);
    }

    public void taskMethod(List<CostManagementEO> costManagementEOList) throws Exception {

        DateTime dateTime = new DateTime();
        List<OrgEO> orgEOList = orgEODao.queryOrgAll();
        Map<String, String> orgNameIdMap = getOrgNameIdMap(orgEOList);
        List<CostReceiverEO> costReceiverEOList = costReceiverEODao.queryAll();
        Map<String, CostReceiverEO> orgIdCostReceiverEOMap = getOrgIdCostReceiverEOMap(costReceiverEOList);
        List<NoticeEO> noticeEOList = new ArrayList<>();
        Map<String, String> userIdStatusMap = new HashMap<>();
        //导入
        for (CostManagementEO costManagementEO : costManagementEOList) {
            String orgId = orgNameIdMap.get(costManagementEO.getOrgName());
            CostReceiverEO costReceiverEO = orgIdCostReceiverEOMap.get(orgId);
            String status = "正常";
            if (!dateTime.isBeforeOrEquals(costManagementEO.getDeadlineTime())) {
                status = "逾期";
                costManagementEO.setStatus(-1);
            }
            if (null == costReceiverEO) {
                continue;
            }
            String userIds = costReceiverEO.getUserIds();
            if (StringUtils.isNotEmpty(userIds)) {
                String[] userIdArr = userIds.split(",");
                for (String userId : userIdArr) {
                    noticeEODao.deleteNoticeByReceiveUserIdEqualAndNoticeTypeEqual(userId, "finance_cost");
                    String stat = userIdStatusMap.get(userId);
                    if (null == stat) {
                        userIdStatusMap.put(userId, status);
                    } else if (StringUtils.equals(stat, "正常")) {
                        userIdStatusMap.put(userId, status);
                    }
                }
            }
        }
        Set<String> userIdSet = userIdStatusMap.keySet();
        if (CollectionUtils.isNotEmpty(userIdSet)) {
            Map<String, String> userIdNameMap = getUserIdNameMap(userIdSet);
            for (String userId : userIdSet) {
                noticeEOList.add(
                        NoticeEO.builder()
                                .id(UUID.randomUUID10())
                                .createTime(new DateTime())
                                .updateTime(new DateTime())
                                .createUserId("GHVRTMA9H2")
                                .noticeContent(userIdStatusMap.get(userId) + "财务成本认领通知")
                                .noticeName("财务成本认领通知")
                                .receiveUserIds(userId)
                                .noticeTypeId("finance_cost")
                                .receiveUserNames(userIdNameMap.get(userId))
                                .build()
                );
            }
            noticeEODao.insertList(noticeEOList);
        }
    }

    List<CostManagementEO> queryListByOrgId(String orgId) {
        return dao.queryListByOrgId(orgId);
    }



    public Map<String, String> getUserIdNameMap(Set<String> userIdSet) {
        List<UserEO> userEOList = userEODao.getUserWithRolesByUserIds(new ArrayList<String>(userIdSet));
        Map<String, String> map = new HashMap<>();
        for (UserEO userEO : userEOList) {
            map.put(userEO.getUsid(), userEO.getUsname());
        }
        return map;
    }

    /**
     * 导出
     *
     * @param costManagementEOPage
     * @return
     */
    public Workbook excelExport(MyCostManagementEOPage costManagementEOPage) {
        List<CostManagementEO> costManagementEOList = dao.pageByLoginUser(costManagementEOPage);
        ExportParams params = new ExportParams();
        params.setType(ExcelType.XSSF);
        return ExcelExportUtil.exportExcel(params, CostManagementEO.class, costManagementEOList);
    }

    public void clamList(CostManagementEO[] costManagementEOArr) throws Exception {
        UserEO userEO = UserUtils.getUser();
        if (null == userEO) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        for (CostManagementEO costManagementEO : costManagementEOArr) {
            costManagementEO.setClaimTime(new DateTime());
            costManagementEO.setClaimUserId(userEO.getUsid());
            costManagementEO.setClaimUserName(userEO.getUsname());
            costManagementEO.setStatus(1);
            dao.updateByPrimaryKeySelective(costManagementEO);
            BusinessGonfigEO businessGonfigEO = businessGonfigEODao.selectByPrimaryKey(costManagementEO.getBusinessId());
            if (StringUtils.equals(businessGonfigEO.getBgType(), "0")) {
                profitManagementEOService.updateProfitByBusinessGonfigIdAndYearAndMonth(
                        costManagementEO.getBusinessId(),
                        String.valueOf(costManagementEO.getYear()),
                        String.valueOf(costManagementEO.getMonth()));
                cashflowManagementEOService.updateCashflowByBusinessGonfigIdAndYearAndMonth(
                        costManagementEO.getBusinessId(),
                        String.valueOf(costManagementEO.getYear()),
                        String.valueOf(costManagementEO.getMonth()));
            }
        }
    }

    public CostManagementEO myCreate(CostManagementEO costManagementEO) {
        UserEO userEO = UserUtils.getUser();
        if (null == userEO) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        DateTime dateTime = new DateTime();
        List<OrgEO> orgEOList = orgEODao.queryOrgAll();
        Map<String, String> orgNameIdMap = getOrgNameIdMap(orgEOList);

        List<NoticeEO> noticeEOList = new ArrayList<>();
        Map<String, String> userIdStatusMap = new HashMap<>();

        List<CostReceiverEO> costReceiverEOList = costReceiverEODao.queryAll();
        Map<String, CostReceiverEO> orgIdCostReceiverEOMap = getOrgIdCostReceiverEOMap(costReceiverEOList);

        costManagementEO.setId(UUID.randomUUID10());
        String orgId = orgNameIdMap.get(costManagementEO.getOrgName());
        if (null == orgId) {
            throw new AdcDaBaseException("组织机构 " + costManagementEO.getOrgName() + " 不存在,请检查！");
        }
        CostReceiverEO costReceiverEO = orgIdCostReceiverEOMap.get(orgId);
        if (null == costReceiverEO) {
            throw new AdcDaBaseException("组织机构 " + costManagementEO.getOrgName() + " 没有设置接收人,请检查！");
        }
        String status = "正常";
        if (!dateTime.isBeforeOrEquals(costManagementEO.getDeadlineTime())) {
            status = "逾期";
            costManagementEO.setStatus(-1);
        }
        String userIds = costReceiverEO.getUserIds();
        if (StringUtils.isNotEmpty(userIds)) {
            String[] userIdArr = userIds.split(",");
            for (String userId : userIdArr) {
                noticeEODao.deleteNoticeByReceiveUserIdEqualAndNoticeTypeEqual(userId, "finance_cost");
                String stat = userIdStatusMap.get(userId);
                if (null == stat) {
                    userIdStatusMap.put(userId, status);
                } else if (StringUtils.equals(stat, "正常")) {
                    userIdStatusMap.put(userId, status);
                }
            }
        }
        costManagementEO.setDistributionUserId(userEO.getUsid());
        costManagementEO.setDistributionUserName(userEO.getUsname());
        costManagementEO.setDistributionTime(dateTime);
        dao.insertSelective(costManagementEO);

        Set<String> userIdSet = userIdStatusMap.keySet();
        if (CollectionUtils.isNotEmpty(userIdSet)) {
            Map<String, String> userIdNameMap = getUserIdNameMap(userIdSet);
            for (String userId : userIdSet)
                noticeEOList.add(
                        NoticeEO.builder()
                                .id(UUID.randomUUID10())
                                .createTime(new DateTime())
                                .updateTime(new DateTime())
                                .createUserId(userEO.getUsid())
                                .noticeContent(userIdStatusMap.get(userId) + "财务成本认领通知")
                                .noticeName("财务成本认领通知")
                                .receiveUserIds(userId)
                                .noticeTypeId("finance_cost")
                                .receiveUserNames(userIdNameMap.get(userId))
                                .build()
                );

        }
        noticeEODao.insertList(noticeEOList);
        return costManagementEO;
    }

    public int myUpdateByPrimaryKeySelective(CostManagementEO costManagementEO) throws Exception {
        UserEO userEO = UserUtils.getUser();
        if (null == userEO) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        DateTime dateTime = new DateTime();
        List<OrgEO> orgEOList = orgEODao.queryOrgAll();
        Map<String, String> orgNameIdMap = getOrgNameIdMap(orgEOList);

        List<CostReceiverEO> costReceiverEOList = costReceiverEODao.queryAll();
        Map<String, CostReceiverEO> orgIdCostReceiverEOMap = getOrgIdCostReceiverEOMap(costReceiverEOList);
        String orgId = orgNameIdMap.get(costManagementEO.getOrgName());
        if (null == orgId) {
            throw new AdcDaBaseException("组织机构 " + costManagementEO.getOrgName() + " 不存在,请检查！");
        }
        CostReceiverEO costReceiverEO = orgIdCostReceiverEOMap.get(orgId);
        if (null == costReceiverEO) {
            throw new AdcDaBaseException("组织机构 " + costManagementEO.getOrgName() + " 没有设置接收人,请检查！");
        }
        if (!dateTime.isBeforeOrEquals(costManagementEO.getDeadlineTime())) {
            costManagementEO.setStatus(-1);
        } else {
            costManagementEO.setStatus(0);
        }

        return dao.updateByPrimaryKeySelective(costManagementEO);
    }

    public List<CostManagementEO> pageByLoginUser(MyCostManagementEOPage page) {
        Integer count = dao.pageByLoginUserCount(page);
        page.getPager().setRowCount(count);
        return dao.pageByLoginUser(page);
    }

    public Integer pageByLoginUserCount(MyCostManagementEOPage page) {
        return dao.pageByLoginUserCount(page);
    }

    public Map<String, String> getOrgNameIdMap(List<OrgEO> orgEOList) {
        Map<String, String> nameIdMap = new HashMap<>();
        for (OrgEO orgEO : orgEOList) {
            nameIdMap.put(orgEO.getName(), orgEO.getId());
        }
        return nameIdMap;
    }

    public static Map<String, CostReceiverEO> getOrgIdCostReceiverEOMap(List<CostReceiverEO> costReceiverEOList) {
        Map<String, CostReceiverEO> orgIdCostReceiverEOMap = new HashMap<>();
        for (CostReceiverEO costReceiverEO : costReceiverEOList) {
            if (StringUtils.isEmpty(costReceiverEO.getUserIds())) {
                continue;
            }
            orgIdCostReceiverEOMap.put(costReceiverEO.getOrgId(), costReceiverEO);
        }
        return orgIdCostReceiverEOMap;
    }

    public Map<String, String> getBusinessNameIdMap(List<BusinessGonfigEO> businessGonfigEOList) {
        Map<String, String> businessNameIdMap = new HashMap<>();
        for (BusinessGonfigEO businessGonfigEO : businessGonfigEOList) {
            businessNameIdMap.put(businessGonfigEO.getBgName(), businessGonfigEO.getId());
        }
        return businessNameIdMap;
    }
}
