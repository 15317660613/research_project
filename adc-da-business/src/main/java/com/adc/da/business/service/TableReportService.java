package com.adc.da.business.service;

import cn.hutool.core.collection.CollectionUtil;
import com.adc.da.business.constant.CommonUtil;
import com.adc.da.business.dao.DeptBudgetEODao;
import com.adc.da.business.dao.DeptEODao;
import com.adc.da.business.dao.OperatingBudgetEODao;
import com.adc.da.business.entity.DeptBudgetEO;
import com.adc.da.business.entity.DeptEO;
import com.adc.da.business.entity.OperatingBudgetEO;
import com.adc.da.business.entity.PairEO;
import com.adc.da.business.entity.Project;
import com.adc.da.business.repository.ProjectRepositoryBusiness;
import com.adc.da.business.vo.DealIncomeVO;
import com.adc.da.business.vo.ReceivableAccountVO;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("tableReportService")
@Slf4j
public class TableReportService {

    @Autowired
    private DeptEODao deptEODao;

    @Autowired
    private DeptBudgetEODao deptBudgetEODao;

    @Autowired
    private OperatingBudgetEODao operatingBudgetEODao;

    public ReceivableAccountVO setReceivableAccountVO(int month, ReceivableAccountVO receivableAccountVO,
        OperatingBudgetEO operatingBudgetEO) {
        switch (month) {
            case 1:
                receivableAccountVO.setCurrentMonth1(operatingBudgetEO.getAccountsReceivable());
                break;
            case 2:
                receivableAccountVO.setCurrentMonth2(operatingBudgetEO.getAccountsReceivable());
                break;
            case 3:
                receivableAccountVO.setCurrentMonth3(operatingBudgetEO.getAccountsReceivable());
                break;
            case 4:
                receivableAccountVO.setCurrentMonth4(operatingBudgetEO.getAccountsReceivable());
                break;
            case 5:
                receivableAccountVO.setCurrentMonth5(operatingBudgetEO.getAccountsReceivable());
                break;
            case 6:
                receivableAccountVO.setCurrentMonth6(operatingBudgetEO.getAccountsReceivable());
                break;
            case 7:
                receivableAccountVO.setCurrentMonth7(operatingBudgetEO.getAccountsReceivable());
                break;
            case 8:
                receivableAccountVO.setCurrentMonth8(operatingBudgetEO.getAccountsReceivable());
                break;
            case 9:
                receivableAccountVO.setCurrentMonth9(operatingBudgetEO.getAccountsReceivable());
                break;
            case 10:
                receivableAccountVO.setCurrentMonth10(operatingBudgetEO.getAccountsReceivable());
                break;
            case 11:
                receivableAccountVO.setCurrentMonth11(operatingBudgetEO.getAccountsReceivable());
                break;
            case 12:
                receivableAccountVO.setCurrentMonth12(operatingBudgetEO.getAccountsReceivable());
                break;
            default:
                break;
        }
        return receivableAccountVO;
    }

    public List<DeptBudgetEO> deptBudgetEOList(String deptTypeId, String year, String startMonth, String endMonth) {
        List<DeptEO> deptEOList = deptEODao.queryAllByOrgType(deptTypeId);
        List<String> orgIdList = new ArrayList<>();
        for (DeptEO deptEO : deptEOList) {
            orgIdList.add(deptEO.getId());
        }
        if (CollectionUtil.isEmpty(orgIdList)){
            log.error("组织机构为空deptTypeId="+deptTypeId);
            return new ArrayList<>();
        }
        return deptBudgetEODao.selectIncomeProfitAndCostByYearAndMonths(orgIdList, year, startMonth, endMonth);
    }

    @Autowired
    private ProjectRepositoryBusiness projectRepositoryBusiness;

    /**
     * 合同金额
     *
     * @param year
     * @return
     */
    private Map<String, BigDecimal> getContractData(Integer year) {
        List<Project> projectsList = initProjectList(year);
        List<DeptEO> list = deptEODao.queryChildGroup();
        Map<String, String> deptMap = new HashMap<>();
        for (DeptEO eo : list) {
            deptMap.put(eo.getId(), eo.getParentIds().split(",")[4]);
        }

        Map<String, BigDecimal> deptInvoiceMap = new HashMap<>();

        String deptId;
        BigDecimal projectDeptInvoice;
        BigDecimal projectInvoice;
        BigDecimal tenThousand = new BigDecimal(10000);

        for (Project project : projectsList) {
            if (deptMap.containsKey(project.getDeptId())) {
                deptId = deptMap.get(project.getDeptId());
            } else {
                deptId = project.getDeptId();
            }

            projectInvoice = BigDecimal.valueOf(project.getContractAmount());
            if (deptInvoiceMap.containsKey(deptId)) {
                projectDeptInvoice = projectInvoice.add(deptInvoiceMap.get(deptId));
            } else {
                projectDeptInvoice = projectInvoice;
            }
            deptInvoiceMap.put(deptId, projectDeptInvoice);
        }

        /*
         * 对数据单位改为万元 ， 原为元
         */
        for (Map.Entry<String, BigDecimal> entry : deptInvoiceMap.entrySet()) {
            BigDecimal value = entry.getValue();
            entry.setValue(value.divide(tenThousand, 4, RoundingMode.HALF_UP));
        }

        return deptInvoiceMap;
    }

    /**
     * 开票信息统计-New
     *
     * @param year
     * @return
     */
    private Map<String, BigDecimal> getIncomeData(Integer year) {

        /*
         * 初始化开票信息
         */
        List<PairEO> projectAmountList = initAmountList(year);

        int size = projectAmountList.size();
        List<String> projectIds = new ArrayList<>(size);
        Map<String, BigDecimal> projectInvoiceMap = new HashMap<>(size);
        for (PairEO pairEO : projectAmountList) {
            projectIds.add(pairEO.getKey());
            projectInvoiceMap.put(pairEO.getKey(), (BigDecimal) pairEO.getValue());
        }
        /*
         * 初始化项目信息
         */
        List<Project> projectsList = initProjectList(projectIds, projectAmountList.size());
        Map<String, BigDecimal> deptInvoiceMap = new HashMap<>(size);

        String deptId;
        BigDecimal projectDeptInvoice;
        BigDecimal projectInvoice;
        BigDecimal tenThousand = new BigDecimal(10000);
        for (Project project : projectsList) {
            deptId = project.getDeptId();
            projectInvoice = projectInvoiceMap.get(project.getId()).divide(tenThousand, 4, RoundingMode.HALF_UP);
            if (deptInvoiceMap.containsKey(deptId)) {
                projectDeptInvoice = projectInvoice.add(deptInvoiceMap.get(deptId));
            } else {
                projectDeptInvoice = projectInvoice.divide(tenThousand, 4, RoundingMode.HALF_UP);
            }
            deptInvoiceMap.put(deptId, projectDeptInvoice);
        }
        return deptInvoiceMap;
    }

    /**
     * 对开票数据初始化
     *
     * @param year
     * @return
     */
    private List<PairEO> initAmountList(int year) {
        Date[] date = getYearRange(year);
        /*
         * 项目数与部门数 不对等，因此不能进行 pageSize限制，只能引入时间范围限制
         */
        return operatingBudgetEODao.queryAllInvoiceByYearGroupByDeptIdNew(date[BEGIN], date[END]);
    }

    private static final int BEGIN = 0;

    private static final int END = 1;

    private Date[] getYearRange(int year) {
        Date[] date = new Date[2];
        Calendar cal = Calendar.getInstance();
        cal.set(year, Calendar.JANUARY, 1, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        date[BEGIN] = cal.getTime();

        cal.add(Calendar.YEAR, 1);
        cal.add(Calendar.SECOND, -1);
        cal.set(Calendar.MILLISECOND, 999);
        date[END] = cal.getTime();
        return date;
    }

    /**
     * 根据当前年筛选项目
     *
     * @return
     */
    private List<Project> initProjectList(int year) {
        Date[] date = getYearRange(year);

        BoolQueryBuilder queryBuilder = QueryBuilders
            .boolQuery()
            .mustNot(QueryBuilders.termQuery("delFlag", Boolean.TRUE))
            .mustNot(QueryBuilders.termQuery("contractAmount", 0.0))
            .must(QueryBuilders.existsQuery("contractAmount"))
            //过滤日常事务类
            .mustNot(QueryBuilders.termQuery("projectType", 1));
        queryBuilder.must(QueryBuilders.rangeQuery("projectBeginTime")
                                       .from(date[BEGIN].getTime())
                                       .to(date[END].getTime())
                                       .includeLower(true)
                                       .includeUpper(true));
        return (((PageImpl<Project>) (projectRepositoryBusiness.search(queryBuilder)))
            .getContent());
    }

    /**
     * 对projectList初始化
     *
     * @param projectIds
     * @param size
     * @return
     */
    private List<Project> initProjectList(List<String> projectIds, int size) {
        List<Project> projectsList = new ArrayList<>(size);

        /*
         * 对于超过512元素的查询，进行拆分
         */
        List<List<String>> lists = CommonUtil.splitList(projectIds, 512);
        for (List<String> ids : lists) {
            projectsList.addAll((List<Project>) (projectRepositoryBusiness.findAll(ids)));
        }
        return projectsList;
    }

    /**
     * 数据
     *
     * @return
     * @author liuzixi
     *     date 2019-03-19
     */
    public Map<String, List<DealIncomeVO>> getDataFromDataBase(Integer year, int size, boolean oldData) {

        Map<String, List<DealIncomeVO>> resultMap = new HashMap<>();
        List<DealIncomeVO> invoiceList = new ArrayList<>();
        List<DealIncomeVO> costList = new ArrayList<>();
        Map<String, BigDecimal> invoiceMap;
        Map<String, Object> costMap = new HashMap<>();

        /*
         * 创收（开票信息）
         */

        if (oldData) {
            invoiceMap = new HashMap<>();
            /* 旧数据 */
            List<PairEO> invoiceMapList = operatingBudgetEODao.queryAllInvoiceByYearGroupByDeptId(year);
            for (PairEO pairEO : invoiceMapList) {
                invoiceMap.put(pairEO.getKey(), (BigDecimal) pairEO.getValue());
            }

            /*
             * 支出
             */
            List<PairEO> costMapList = deptBudgetEODao.queryAllCostByYearGroupByDeptId(year);

            for (PairEO pairEO : costMapList) {
                costMap.put(pairEO.getKey(), pairEO.getValue());
            }

        } else {
            /* 新数据 */
            invoiceMap = getContractData(year);
        }

        List<DeptEO> orgEOList = deptEODao.listAllOrg();

        String deptId;
        String deptName;
        for (DeptEO eo : orgEOList) {
            deptId = eo.getId();
            deptName = eo.getOrgName();
            if (invoiceMap.containsKey(deptId)) {
                DealIncomeVO dealIncomeVO =
                    DealIncomeVO.builder()
                                .amount(invoiceMap.get(deptId).toString())
                                .name(deptName)
                                .id(deptId)
                                .build();
                invoiceList.add(dealIncomeVO);
            }
            if (costMap.containsKey(deptId)) {
                DealIncomeVO dealCostVO =
                    DealIncomeVO.builder()
                                .amount(costMap.get(deptId).toString())
                                .name(deptName)
                                .id(deptId)
                                .build();
                costList.add(dealCostVO);
            }
        }

        Comparator<DealIncomeVO> sortCost = new Comparator<DealIncomeVO>() {
            @Override
            public int compare(DealIncomeVO s1, DealIncomeVO s2) {
                return (int) (Float.parseFloat(s2.getAmount()) - Float.parseFloat(s1.getAmount()));
            }
        };

        Collections.sort(costList, sortCost);
        Collections.sort(invoiceList, sortCost);

        if (costList.size() >= size) {
            resultMap.put("deal", costList.subList(0, size));
        } else {
            resultMap.put("deal", costList);
        }

        if (invoiceList.size() >= size) {
            resultMap.put("income", invoiceList.subList(0, size));
        } else {
            resultMap.put("income", invoiceList);
        }
        return resultMap;
    }

}
