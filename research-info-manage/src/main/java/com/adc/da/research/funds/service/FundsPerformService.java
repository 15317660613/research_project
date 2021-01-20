package com.adc.da.research.funds.service;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.adc.da.base.service.BaseService;
import com.adc.da.entity.NoticeEO;
import com.adc.da.login.util.UserUtils;
import com.adc.da.research.config.entity.WarnNoticeEO;
import com.adc.da.research.config.entity.WarnRuleEO;
import com.adc.da.research.config.page.WarnNoticeEOPage;
import com.adc.da.research.config.page.WarnRuleEOPage;
import com.adc.da.research.config.service.WarnNoticeEOService;
import com.adc.da.research.config.service.WarnRuleEOService;
import com.adc.da.research.funds.entity.ProjectExpendEO;
import com.adc.da.research.funds.entity.ProjectIncomeEO;
import com.adc.da.research.funds.eum.AmountType;
import com.adc.da.research.funds.eum.BudgetType;
import com.adc.da.research.funds.eum.DelState;
import com.adc.da.research.funds.eum.FundsListType;
import com.adc.da.research.funds.page.ProjectExpendEOPage;
import com.adc.da.research.funds.page.ProjectIncomeEOPage;
import com.adc.da.research.funds.vo.perform.*;
import com.adc.da.research.project.dao.ProjectDataEODao;
import com.adc.da.research.project.entity.BudgetDetailEO;
import com.adc.da.research.project.entity.ProjectDataEO;
import com.adc.da.research.project.entity.ProjectWarnEO;
import com.adc.da.research.project.page.BudgetDetailEOPage;
import com.adc.da.research.project.page.ProjectDataEOPage;
import com.adc.da.research.project.page.ProjectWarnEOPage;
import com.adc.da.research.project.service.BudgetDetailEOService;
import com.adc.da.research.project.service.ProjectDataEOService;
import com.adc.da.research.project.service.ProjectWarnEOService;
import com.adc.da.service.NoticeEOService;
import com.adc.da.util.utils.BeanMapper;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.UUID;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @Auther: yanyujie
 * @Date: 2020/11/22
 * @Description:
 */
@Service
public class FundsPerformService extends BaseService<ProjectDataEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(FundsPerformService.class);

    @Autowired
    private ProjectDataEOService projectDataEOService;

    @Autowired
    private ProjectExpendEOService projectExpendEOService;

    @Autowired
    private ProjectIncomeEOService projectIncomeEOService;

    @Autowired
    private BudgetDetailEOService budgetDetailEOService;

    @Autowired
    private ProjectWarnEOService projectWarnEOService;

    @Autowired
    private NoticeEOService noticeEOService;

    @Autowired
    private ProjectDataEODao dao;

    @Autowired
    private WarnRuleEOService warnRuleEOService;

    @Autowired
    private BeanMapper beanMapper;
    @Autowired
    private WarnNoticeEOService warnNoticeEOService;

    public ProjectDataEODao getDao() {
        return dao;
    }

    private final Integer ALARM = 1;

    //数字格式
    DecimalFormat format = new DecimalFormat("#.##");

    /***
     * @Description:
     * @Param: [page]
     * @return: List<FundsListVO>
     * @Author: yanyujie
     * @Date: 2020/11/22
     */

    @Deprecated
    @SuppressWarnings("此方法暂时不在使用，page新方法没有type参数")
    public List<FundsPerformVO> page(ProjectDataEOPage page, Integer type) throws Exception {
        //四舍五入
        format.setRoundingMode(RoundingMode.HALF_UP);
        //返回的结果集
        List<FundsPerformVO> resultList = new ArrayList<>();

        //判断当前季度，整理查询参数
        LocalDate localDate = LocalDate.now();
        final int month = localDate.getMonth().getValue();
        int quarter = month % 3 == 0 ? month / 3 : month / 3 + 1;
        //季度日期参数
        String paramByQuarter = "";
        //准确日期参数
        String paramDateBegin = "";
        String paramDateEnd = "";
        if (quarter == 1) {
            paramByQuarter = localDate.getYear() + ".1-" + localDate.getYear() + ".3";
            paramDateBegin = localDate.getYear() + "-01-01";
            paramDateEnd = localDate.getYear() + "-03-31";
        }
        if (quarter == 2) {
            paramByQuarter = localDate.getYear() + ".4-" + localDate.getYear() + ".6";
            paramDateBegin = localDate.getYear() + "-04-01";
            paramDateEnd = localDate.getYear() + "-06-30";
        }
        if (quarter == 3) {
            paramByQuarter = localDate.getYear() + ".7-" + localDate.getYear() + ".9";
            paramDateBegin = localDate.getYear() + "-07-01";
            paramDateEnd = localDate.getYear() + "-09-30";
        }
        if (quarter == 4) {
            paramByQuarter = localDate.getYear() + ".10-" + localDate.getYear() + ".12";
            paramDateBegin = localDate.getYear() + "-10-01";
            paramDateEnd = localDate.getYear() + "-12-31";
        }
        final List<ProjectDataEO> projectDataEOS = new ArrayList<>();
        if (type == FundsListType.PAGE.getValue()) {
            projectDataEOS.addAll(projectDataEOService.queryByPageForFunds(page));
        } else {
            projectDataEOS.addAll(projectDataEOService.queryByListForFunds(page));
        }
        String finalParamByQuarter = paramByQuarter;
        String finalParamDateBegin = paramDateBegin;
        String finalParamDateEnd = paramDateEnd;


        projectDataEOS.stream().forEach(item -> {
            FundsPerformVO fundsPerformVO = beanMapper.map(item, FundsPerformVO.class);
            //计算到账率 同一分类下所有项目到账率平均值项目到账金额/项目总预算金额，最高100%
            ProjectIncomeEOPage pageByArrivalRate = new ProjectIncomeEOPage();
            pageByArrivalRate.setProjectId(item.getId());
            List<ProjectIncomeEO> projectIncomeEOS = new ArrayList<>();
            try {
                projectIncomeEOS = projectIncomeEOService.queryByList(pageByArrivalRate);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }

            if (CollectionUtils.isNotEmpty(projectIncomeEOS)) {
                final double sum = projectIncomeEOS.stream().mapToDouble(ProjectIncomeEO::getIncomeAmount).sum();
                Double arrivalRate = Double.parseDouble(format.format(sum / item.getTotalFunding() * 100));
                fundsPerformVO.setArrivalRate(arrivalRate + "%");
            }

            //计算国拨经费执行率
            //国拨经费执行率，项目中国拨经费支出/总国拨经费*100%
            ProjectExpendEOPage pageByStateExecutions = new ProjectExpendEOPage();
            pageByStateExecutions.setProjectId(item.getId());
            pageByStateExecutions.setExpendType(AmountType.STATE_FUND.getLabel());
            List<ProjectExpendEO> projectByStateExecutions = new ArrayList<>();
            try {
                projectByStateExecutions = projectExpendEOService.queryByList(pageByStateExecutions);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
            if (CollectionUtils.isNotEmpty(projectByStateExecutions)) {
                final double sum =
                        projectByStateExecutions.stream().mapToDouble(ProjectExpendEO::getExpendAmount).sum();
                Double stateExecutions = Double.parseDouble(format.format(sum / item.getStateFunding() * 100));
                fundsPerformVO.setStateExecutions(stateExecutions + "%");
            }

            //计算本季度国拨经费执行率（本季度国拨支出经费/本季度国拨总经费 *100%）
            //本季度国拨总经费
            BudgetDetailEOPage pageByQuarter = new BudgetDetailEOPage();
            pageByQuarter.setBudgetQuarterly(finalParamByQuarter);
            pageByQuarter.setBudgetType(BudgetType.STATE_FUND.getValue());
            Double stateQuarterSum = 0.00;
            try {
                List<BudgetDetailEO> budgetDetailEOS = budgetDetailEOService.queryByList(pageByQuarter);
                //本季度国拨经费
                stateQuarterSum = budgetDetailEOS.stream().mapToDouble(BudgetDetailEO::getBudgetAmount).sum();
                //保存国拨经费信息
                fundsPerformVO.setStateFundQuarter(stateQuarterSum);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }

            //本季度国拨支出经费
            ProjectExpendEOPage pageByStateQuarter = new ProjectExpendEOPage();
            pageByStateQuarter.setProjectId(item.getId());
            pageByStateQuarter.setExpendDateBegin(finalParamDateBegin);
            pageByStateQuarter.setExpendDateEnd(finalParamDateEnd);
            pageByStateQuarter.setExpendType(AmountType.STATE_FUND.getLabel());
            List<ProjectExpendEO> projectExpendEOSByStateQuarter = new ArrayList<>();
            Double expendEOSByStateQuarter = 0.00;
            try {
                projectExpendEOSByStateQuarter = projectExpendEOService.queryByList(pageByQuarter);
                expendEOSByStateQuarter = projectExpendEOSByStateQuarter.stream()
                        .mapToDouble(ProjectExpendEO::getExpendAmount).sum();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
            if (Objects.nonNull(stateQuarterSum) && stateQuarterSum != 0.00) {
                final double stateExecutions =
                        Double.parseDouble(format.format(expendEOSByStateQuarter / stateQuarterSum * 100));
                fundsPerformVO.setStateExecutions(stateExecutions + "%");
            }

            //计算自筹经费执行率  项目中自筹经费支出/总自筹经费*100%
            ProjectExpendEOPage pageBySelfExecutions = new ProjectExpendEOPage();
            pageBySelfExecutions.setProjectId(item.getId());
            pageBySelfExecutions.setExpendType(AmountType.SELF_FUND.getLabel());
            List<ProjectExpendEO> projectExpendEOSBySelfExecutions = new ArrayList<>();
            try {
                projectExpendEOSBySelfExecutions = projectExpendEOService.queryByList(pageBySelfExecutions);
                final double sum = projectExpendEOSBySelfExecutions
                        .stream().mapToDouble(ProjectExpendEO::getExpendAmount).sum();

                final double selfExecutions = Double.
                        parseDouble(format.format(sum / item.getSelfFunded() * 100));
                fundsPerformVO.setSelfExecutions(selfExecutions + "%");
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }

            //本季度自筹经费执行率=本季度自筹支出经费除本季度自筹总经费 *100%
            //本季度自筹总经费
            BudgetDetailEOPage pageBySelfQuarter = new BudgetDetailEOPage();
            pageBySelfQuarter.setBudgetQuarterly(finalParamByQuarter);
            pageBySelfQuarter.setBudgetType(BudgetType.SELF_FUND.getValue());
            List<BudgetDetailEO> budgetDetailEOSBySelfQuarter = new ArrayList<>();
            try {
                budgetDetailEOSBySelfQuarter = budgetDetailEOService.queryByList(pageBySelfQuarter);
                final double sumBySelfQuarter = budgetDetailEOSBySelfQuarter.stream()
                        .mapToDouble(BudgetDetailEO::getBudgetAmount).sum();
                //保存本季度自筹经费
                fundsPerformVO.setSelfFundedQuarter(sumBySelfQuarter);
                if (Objects.nonNull(sumBySelfQuarter) && sumBySelfQuarter > 0) {
                    //本季度自筹支出经费
                    ProjectExpendEOPage pageBySelfExpendQuarter = new ProjectExpendEOPage();
                    pageBySelfExpendQuarter.setProjectId(item.getId());
                    pageBySelfExpendQuarter.setExpendDateBegin(finalParamDateBegin);
                    pageBySelfExpendQuarter.setExpendDateEnd(finalParamDateEnd);
                    pageBySelfExpendQuarter.setExpendType(AmountType.SELF_FUND.getLabel());
                    final List<ProjectExpendEO> projectExpendEOS =
                            projectExpendEOService.queryByList(pageBySelfExpendQuarter);
                    final double sumBySelfExpendQuarter =
                            projectExpendEOS.stream().mapToDouble(ProjectExpendEO::getExpendAmount).sum();
                    final double selfExecutionsQuarter =
                            Double.parseDouble(format.format(sumBySelfExpendQuarter / sumBySelfQuarter * 100));
                    fundsPerformVO.setSelfExecutionsQuarter(selfExecutionsQuarter + "%");
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
            resultList.add(fundsPerformVO);
        });


        return resultList;
    }

    /***
     * @Description: 生成workBOOK
     * @Param: [exportParams, page]
     * @return: org.apache.poi.ss.usermodel.Workbook
     * @Author: yanyujie
     * @Date: 2020/11/22
     */
    public Workbook exportProjectData(ExportParams exportParams, ProjectDataEOPage page) throws Exception {
        List<FundsPerformVO> fundsPerformVOList = new ArrayList<>();
        if (StringUtils.isNotBlank(page.getIds())) {
            final String[] split = page.getIds().split(",", -1);
            for (String idStr : split) {
                ProjectDataEOPage idPage = new ProjectDataEOPage();
                idPage.setId(idStr);
                final PageFundsVO pageFundsVO = this.page(idPage);
                fundsPerformVOList.addAll(pageFundsVO.getFundsPerformVOS());
            }
        } else {
            final PageFundsVO pageFundsVO = this.page(page);
            fundsPerformVOList = pageFundsVO.getFundsPerformVOS();

        }
        if (CollectionUtils.isEmpty(fundsPerformVOList)){
            fundsPerformVOList=new ArrayList<>();
        }
        return ExcelExportUtil.exportExcel(exportParams, FundsPerformVO.class, fundsPerformVOList);
    }

    /***
     * @Description:
     * @Param: [projectId]
     * @return: java.util.List<com.adc.da.research.funds.vo.perform.SubjectProgressVO>
     * @Author: yanyujie
     * @Date: 2020/11/24
     */
    public List<SubjectProgressVO> subjectProgress(String projectId) throws Exception {
        //四舍五入
        format.setRoundingMode(RoundingMode.HALF_UP);
        List<SubjectProgressVO> resultList = new LinkedList<>();
        AtomicInteger resultIndex = new AtomicInteger();
        BudgetDetailEOPage BudgetPageByProjectId = new BudgetDetailEOPage();
        BudgetPageByProjectId.setProjectId(projectId);
        //将所有含有年份信息的数据过滤出来
        final List<BudgetDetailEO> BudgetDetails = budgetDetailEOService.queryByList(BudgetPageByProjectId)
                .parallelStream().filter(item -> Objects.nonNull(item.getBudgetYear())).collect(Collectors.toList());

        //联合分组（先按费用类型分后按具体账目分）
        final Map<String, Map<String, List<BudgetDetailEO>>> budgetDetailsGroup
                = BudgetDetails.stream().collect(Collectors.groupingBy(BudgetDetailEO::getBudgetType
                , Collectors.groupingBy(BudgetDetailEO::getBudgetDetailTypeName)));
        //目前只设计两种费用类型（国拨经费 or 自筹经费）

        //情况一：国拨经费
        if (budgetDetailsGroup.containsKey(BudgetType.STATE_FUND.getValue())) {
            final Map<String, List<BudgetDetailEO>> budgetDetailState
                    = budgetDetailsGroup.get(BudgetType.STATE_FUND.getValue());
            budgetDetailState.forEach((k, v) -> {
                SubjectProgressVO subjectProgressVO = null;

                //查看resultList中是否存在含有k值得信息，如果有则进行追加
                if (CollectionUtils.isNotEmpty(resultList)) {
                    for (SubjectProgressVO subject : resultList) {
                        if (subject.getSubjcetName().contains(k)) {
                            resultIndex.set(resultList.indexOf(subject));
                            subjectProgressVO = subject;
                            break;
                        } else {
                            subjectProgressVO = new SubjectProgressVO();
                        }
                    }
                } else {
                    subjectProgressVO = new SubjectProgressVO();
                }

                //根据项目id与金额类型查询出账记录
                ProjectExpendEOPage expendEOPageByProjectIdAndState = new ProjectExpendEOPage();
                expendEOPageByProjectIdAndState.setProjectId(projectId);
                expendEOPageByProjectIdAndState.setExpendType(AmountType.STATE_FUND.getLabel());
                List<ProjectExpendEO> projectExpendEOS = new ArrayList<>();
                try {
                    projectExpendEOS = projectExpendEOService.queryByList(expendEOPageByProjectIdAndState);
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
                //（条件：科目名称包含K所含的内容）获取支出合计
                final double expendSum = projectExpendEOS.stream().filter(item -> item.getSubjectName().contains(k))
                        .collect(Collectors.toList()).stream().mapToDouble(ProjectExpendEO::getExpendAmount).sum();
                //获取Budget中的国拨经费中--K项目的预算合计
                final double budgetSum = v.stream().mapToDouble(BudgetDetailEO::getBudgetAmount).sum();
                subjectProgressVO.setProjectId(projectId);
                subjectProgressVO.setSubjcetName(k);
                subjectProgressVO.setStateBudgetFunds(budgetSum);
                subjectProgressVO.setStateExecuteFunds(expendSum);

                final double stateRate = Double.parseDouble(format.format(expendSum / budgetSum * 100));
                subjectProgressVO.setStateRate(stateRate + "%");
                resultList.remove(subjectProgressVO);


                resultList.add(subjectProgressVO);
                resultIndex.set(0);
            });

        }
        //情况二：自筹经费
        if (budgetDetailsGroup.containsKey(BudgetType.SELF_FUND.getValue())) {
            final Map<String, List<BudgetDetailEO>> budgetDetailSelf
                    = budgetDetailsGroup.get(BudgetType.SELF_FUND.getValue());
            budgetDetailSelf.forEach((k, v) -> {
                SubjectProgressVO subjectProgressVO = null;
                //查看resultList中是否存在含有k值得信息，如果有则进行追加
                if (CollectionUtils.isNotEmpty(resultList)) {
                    for (SubjectProgressVO subject : resultList) {
                        if (subject.getSubjcetName().contains(k)) {
                            subjectProgressVO = subject;
                            resultIndex.set(resultList.indexOf(subject));
                            break;
                        } else {
                            subjectProgressVO = new SubjectProgressVO();
                        }
                    }
                } else {
                    subjectProgressVO = new SubjectProgressVO();
                }

                //根据项目id与金额类型查询出账记录
                ProjectExpendEOPage expendEOPageByProjectIdAndSelf = new ProjectExpendEOPage();
                expendEOPageByProjectIdAndSelf.setProjectId(projectId);
                expendEOPageByProjectIdAndSelf.setExpendType(AmountType.SELF_FUND.getLabel());
                List<ProjectExpendEO> projectExpendEOS = new ArrayList<>();
                try {
                    projectExpendEOS = projectExpendEOService.queryByList(expendEOPageByProjectIdAndSelf);
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
                //（条件：科目名称包含K所含的内容）获取支出合计
                final double expendSum
                        = projectExpendEOS.stream().filter(item -> item.getSubjectName().contains(k))
                        .collect(Collectors.toList()).stream().mapToDouble(ProjectExpendEO::getExpendAmount).sum();
                //获取Budget中的国拨经费中--K项目的预算合计
                final double budgetSum = v.stream().mapToDouble(BudgetDetailEO::getBudgetAmount).sum();
                subjectProgressVO.setProjectId(projectId);
                subjectProgressVO.setSubjcetName(k);
                subjectProgressVO.setSelfBudgetFunds(budgetSum);
                subjectProgressVO.setSelfExecuteFunds(expendSum);

                final double selfRate = Double.parseDouble(format.format(expendSum / budgetSum * 100));
                subjectProgressVO.setSelfRate(selfRate + "%");
                resultList.remove(subjectProgressVO);
                resultList.add(resultIndex.get(), subjectProgressVO);
                resultIndex.set(0);
            });
        }

        //计算合计情况
        SubjectProgressVO totolProgressVO = new SubjectProgressVO();
        totolProgressVO.setSubjcetName("合计");
        try {
            final double stateBudgetTotol
                    = resultList.stream().mapToDouble(SubjectProgressVO::getStateBudgetFunds).sum();
            totolProgressVO.setStateBudgetFunds(stateBudgetTotol);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        try {
            final double stateExecuteTotol
                    = resultList.stream().mapToDouble(SubjectProgressVO::getStateExecuteFunds).sum();
            totolProgressVO.setStateExecuteFunds(stateExecuteTotol);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        try {
            final double selfBudgetTotol
                    = resultList.stream().mapToDouble(SubjectProgressVO::getSelfBudgetFunds).sum();
            totolProgressVO.setSelfBudgetFunds(selfBudgetTotol);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        try {
            final double selfExecuteTotal
                    = resultList.stream().mapToDouble(SubjectProgressVO::getSelfExecuteFunds).sum();
            totolProgressVO.setSelfExecuteFunds(selfExecuteTotal);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        if (Objects.nonNull(totolProgressVO.getStateBudgetFunds()) && totolProgressVO.getStateBudgetFunds() > 0) {
            final double stateRateTotal
                    = Double.parseDouble(format.format
                    (totolProgressVO.getStateExecuteFunds() / totolProgressVO.getStateBudgetFunds() * 100));
            totolProgressVO.setStateRate(stateRateTotal + "%");
        }
        if (Objects.nonNull(totolProgressVO.getSelfBudgetFunds()) && totolProgressVO.getSelfBudgetFunds() > 0) {
            final double selfRateTotal
                    = Double.parseDouble(format.format
                    (totolProgressVO.getSelfExecuteFunds() / totolProgressVO.getSelfBudgetFunds() * 100));
            totolProgressVO.setSelfRate(selfRateTotal + "%");
        }

        resultList.add(0, totolProgressVO);
        return resultList;
    }

    public PageFundsVO page(ProjectDataEOPage page, Integer... types) throws Exception {
        //四舍五入
        format.setRoundingMode(RoundingMode.HALF_UP);
        //返回的结果集
        List<FundsPerformVO> performVOList = new ArrayList<>();

        //判断当前季度，整理查询参数
        LocalDate localDate = LocalDate.now();
        final int month = localDate.getMonth().getValue();
        int quarter = month % 3 == 0 ? month / 3 : month / 3 + 1;
        //季度日期参数
        String paramByQuarter = "";
        //准确日期参数
        String paramDateBegin = "";
        String paramDateEnd = "";
        //本年度的日期
        LocalDateTime localDateTime = LocalDateTime.now();
        final int currentYear = localDateTime.getYear();
        String yearBegin = currentYear + "-01-01";
        String yearEnd = currentYear + "-12-31";


        if (quarter == 1) {
//            paramByQuarter = localDate.getYear() + ".1-" + localDate.getYear() + ".3";
            paramByQuarter = localDate.getYear() + "-第一季度";
            paramDateBegin = localDate.getYear() + "-01-01";
            paramDateEnd = localDate.getYear() + "-03-31";
        }
        if (quarter == 2) {
//            paramByQuarter = localDate.getYear() + ".4-" + localDate.getYear() + ".6";
            paramByQuarter = localDate.getYear() + "-第二季度";
            paramDateBegin = localDate.getYear() + "-04-01";
            paramDateEnd = localDate.getYear() + "-06-30";
        }
        if (quarter == 3) {
//            paramByQuarter = localDate.getYear() + ".7-" + localDate.getYear() + ".9";
            paramByQuarter = localDate.getYear() + "-第三季度";
            paramDateBegin = localDate.getYear() + "-07-01";
            paramDateEnd = localDate.getYear() + "-09-30";
        }
        if (quarter == 4) {
//            paramByQuarter = localDate.getYear() + ".10-" + localDate.getYear() + ".12";
            paramByQuarter = localDate.getYear() + "-第四季度";
            paramDateBegin = localDate.getYear() + "-10-01";
            paramDateEnd = localDate.getYear() + "-12-31";
        }


        final List<ProjectDataEO> projectDataEOS = new ArrayList<>();
        projectDataEOS.addAll(projectDataEOService.queryByListForFunds(page));
        String finalParamByQuarter = paramByQuarter;
        String finalParamDateBegin = paramDateBegin;
        String finalParamDateEnd = paramDateEnd;


        projectDataEOS.stream().forEach(item -> {
            FundsPerformVO fundsPerformVO = beanMapper.map(item, FundsPerformVO.class);
            //计算到账率 同一分类下所有项目到账率平均值项目到账金额/项目总预算金额，最高100%
            ProjectIncomeEOPage pageByArrivalRate = new ProjectIncomeEOPage();
            pageByArrivalRate.setProjectId(item.getId());
            List<ProjectIncomeEO> projectIncomeEOS = new ArrayList<>();
            try {
                projectIncomeEOS = projectIncomeEOService.queryByList(pageByArrivalRate);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }

            if (CollectionUtils.isNotEmpty(projectIncomeEOS)) {
                final double sum = projectIncomeEOS.stream()
                        .filter(incomes -> Objects.nonNull(incomes.getIncomeAmount()))
                        .collect(Collectors.toList()).stream().mapToDouble(ProjectIncomeEO::getIncomeAmount).sum();
                Double arrivalRate = null;
                try {
                    arrivalRate = Double.parseDouble(format.format(sum / item.getStateFunding() * 100));
                } catch (Exception e) {
                    arrivalRate = 0d;
                }
                fundsPerformVO.setArrivalRate(arrivalRate + "%");
            }

            //计算国拨经费执行率
            //国拨经费执行率，项目中国拨经费支出/总国拨经费*100%
            ProjectExpendEOPage pageByStateExecutions = new ProjectExpendEOPage();
            pageByStateExecutions.setProjectId(item.getId());
            pageByStateExecutions.setExpendType(AmountType.STATE_FUND.getLabel());
            List<ProjectExpendEO> projectByStateExecutions = new ArrayList<>();
            try {
                projectByStateExecutions = projectExpendEOService.queryByList(pageByStateExecutions);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
            if (CollectionUtils.isNotEmpty(projectByStateExecutions)) {
                final double sum =
                        projectByStateExecutions.stream().mapToDouble(ProjectExpendEO::getExpendAmount).sum();
                Double stateExecutions = Double.parseDouble(format.format(sum / item.getStateFunding() * 100));
                fundsPerformVO.setStateExecutions(stateExecutions + "%");
            }

            //计算本季度国拨经费执行率（本季度国拨支出经费/本季度国拨总经费 *100%）
            //本季度国拨总经费
            BudgetDetailEOPage pageByQuarter = new BudgetDetailEOPage();
            pageByQuarter.setBudgetQuarterly(finalParamByQuarter);
            pageByQuarter.setBudgetType(BudgetType.STATE_FUND.getValue());
            pageByQuarter.setProjectId(item.getId());
            Double stateQuarterSum = 0.00;
            try {
                List<BudgetDetailEO> budgetDetailEOS = budgetDetailEOService.queryByList(pageByQuarter);
                //本季度国拨经费
                stateQuarterSum = budgetDetailEOS.stream().
                        filter(detailEOS -> Objects.nonNull(detailEOS.getBudgetAmount())).collect(Collectors.toList())
                        .stream().mapToDouble(BudgetDetailEO::getBudgetAmount).sum();
                //保存国拨经费信息
                fundsPerformVO.setStateFundQuarter(stateQuarterSum);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }

            //本季度国拨支出经费
            ProjectExpendEOPage pageByStateQuarter = new ProjectExpendEOPage();
            pageByStateQuarter.setProjectId(item.getId());
            pageByStateQuarter.setExpendDateBegin(finalParamDateBegin);
            pageByStateQuarter.setExpendDateEnd(finalParamDateEnd);
            pageByStateQuarter.setExpendType(AmountType.STATE_FUND.getLabel());
            List<ProjectExpendEO> projectExpendEOSByStateQuarter = new ArrayList<>();
            Double expendEOSByStateQuarter = 0.00;
            try {
                projectExpendEOSByStateQuarter = projectExpendEOService.queryByList(pageByQuarter);
                expendEOSByStateQuarter = projectExpendEOSByStateQuarter.stream()
                        .filter(stateQuarter -> Objects.nonNull(stateQuarter.getExpendAmount()))
                        .collect(Collectors.toList()).stream()
                        .mapToDouble(ProjectExpendEO::getExpendAmount).sum();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
            if (Objects.nonNull(stateQuarterSum) && stateQuarterSum != 0.00) {
                final double stateQuarterExecutions =
                        Double.parseDouble(format.format(expendEOSByStateQuarter / stateQuarterSum * 100));
                fundsPerformVO.setStateQuarterExecutions(stateQuarterExecutions + "%");
            }

            //计算本年度国拨经费执行率（本季度国拨支出经费/本季度国拨总经费 *100%）
            //本季度国拨总经费
            BudgetDetailEOPage pageByYear = new BudgetDetailEOPage();
            pageByYear.setBudgetYear(String.valueOf(currentYear));
            pageByYear.setBudgetType(BudgetType.STATE_FUND.getValue());
            pageByYear.setProjectId(item.getId());
            Double stateYearSum = 0.00;
            try {
                List<BudgetDetailEO> budgetDetailEOS = budgetDetailEOService.queryByList(pageByYear);
                //本季度国拨经费
                stateYearSum = budgetDetailEOS.stream().filter(sumList -> Objects.nonNull(sumList.getBudgetAmount()))
                        .collect(Collectors.toList()).stream().
                                mapToDouble(BudgetDetailEO::getBudgetAmount).sum();
                //保存年度国拨经费信息
                fundsPerformVO.setStateFundYeah(Double.valueOf(format.format(stateYearSum)));
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }

            ProjectExpendEOPage pageByStateYear = new ProjectExpendEOPage();
            pageByStateYear.setProjectId(item.getId());
            pageByStateYear.setExpendDateBegin(yearBegin);
            pageByStateYear.setExpendDateEnd(yearEnd);
            pageByStateYear.setExpendType(AmountType.STATE_FUND.getLabel());
            List<ProjectExpendEO> projectExpendEOSByStateYear = new ArrayList<>();
            Double expendEOSByStateYear = 0.00;
            try {
                projectExpendEOSByStateYear = projectExpendEOService.queryByList(pageByStateYear);
                expendEOSByStateYear = projectExpendEOSByStateYear.stream()
                        .filter(yearList -> Objects.nonNull(yearList.getExpendAmount())).collect(Collectors.toList()).stream()
                        .mapToDouble(ProjectExpendEO::getExpendAmount).sum();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
            if (Objects.nonNull(stateYearSum) && stateYearSum != 0.00) {
                final double stateYearExecutions =
                        Double.parseDouble(format.format(expendEOSByStateYear / stateYearSum * 100));
                fundsPerformVO.setStateYearExecutions(stateYearExecutions + "%");
            }


            //计算自筹经费执行率  项目中自筹经费支出/总自筹经费*100%
            ProjectExpendEOPage pageBySelfExecutions = new ProjectExpendEOPage();
            pageBySelfExecutions.setProjectId(item.getId());
            pageBySelfExecutions.setExpendType(AmountType.SELF_FUND.getLabel());
            List<ProjectExpendEO> projectExpendEOSBySelfExecutions = new ArrayList<>();
            try {
                projectExpendEOSBySelfExecutions = projectExpendEOService.queryByList(pageBySelfExecutions);
                final double sum = projectExpendEOSBySelfExecutions
                        .stream().filter(selfList -> Objects.nonNull(selfList.getExpendAmount()))
                        .collect(Collectors.toList()).stream().mapToDouble(ProjectExpendEO::getExpendAmount).sum();
                if (Objects.nonNull(item.getSelfFunded())) {
                    final double selfExecutions = Double.
                            parseDouble(format.format(sum / item.getSelfFunded() * 100));
                    fundsPerformVO.setSelfExecutions(selfExecutions + "%");
                }

            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }

            //本季度自筹经费执行率=本季度自筹支出经费除本季度自筹总经费 *100%
            //本季度自筹总经费
            BudgetDetailEOPage pageBySelfQuarter = new BudgetDetailEOPage();
            pageBySelfQuarter.setBudgetQuarterly(finalParamByQuarter);
            pageBySelfQuarter.setBudgetType(BudgetType.SELF_FUND.getValue());
            List<BudgetDetailEO> budgetDetailEOSBySelfQuarter = new ArrayList<>();
            try {
                budgetDetailEOSBySelfQuarter = budgetDetailEOService.queryByList(pageBySelfQuarter);
                final double sumBySelfQuarter = budgetDetailEOSBySelfQuarter.stream()
                        .filter(quarterList -> Objects.nonNull(quarterList.getBudgetAmount())).collect(Collectors.toList()).stream()
                        .mapToDouble(BudgetDetailEO::getBudgetAmount).sum();
                //保存本季度自筹经费
                fundsPerformVO.setSelfFundedQuarter(sumBySelfQuarter);
                if (Objects.nonNull(sumBySelfQuarter) && sumBySelfQuarter > 0) {
                    //本季度自筹支出经费
                    ProjectExpendEOPage pageBySelfExpendQuarter = new ProjectExpendEOPage();
                    pageBySelfExpendQuarter.setProjectId(item.getId());
                    pageBySelfExpendQuarter.setExpendDateBegin(finalParamDateBegin);
                    pageBySelfExpendQuarter.setExpendDateEnd(finalParamDateEnd);
                    pageBySelfExpendQuarter.setExpendType(AmountType.SELF_FUND.getLabel());
                    final List<ProjectExpendEO> projectExpendEOS =
                            projectExpendEOService.queryByList(pageBySelfExpendQuarter);
                    final double sumBySelfExpendQuarter =
                            projectExpendEOS.stream().filter(querterList -> Objects.nonNull(querterList.getExpendAmount()))
                                    .collect(Collectors.toList()).stream().mapToDouble(ProjectExpendEO::getExpendAmount).sum();
                    final double selfExecutionsQuarter =
                            Double.parseDouble(format.format(sumBySelfExpendQuarter / sumBySelfQuarter * 100));
                    fundsPerformVO.setSelfExecutionsQuarter(selfExecutionsQuarter + "%");
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }


            //本年度自筹经费执行率=本年度自筹支出经费除本年度自筹总经费 *100%
            //本年度自筹总经费
            BudgetDetailEOPage pageBySelfYear = new BudgetDetailEOPage();
            pageBySelfYear.setBudgetQuarterly(String.valueOf(currentYear));
            pageBySelfYear.setBudgetType(BudgetType.SELF_FUND.getValue());
            List<BudgetDetailEO> budgetDetailEOSBySelfYear = new ArrayList<>();
            try {
                budgetDetailEOSBySelfYear = budgetDetailEOService.queryByList(pageBySelfYear);
                final double sumBySelfYear = budgetDetailEOSBySelfYear.stream()
                        .mapToDouble(BudgetDetailEO::getBudgetAmount).sum();
                //保存本季度自筹经费
                fundsPerformVO.setSelfFundedYear(sumBySelfYear);
                if (Objects.nonNull(sumBySelfYear) && sumBySelfYear > 0) {
                    //本季度自筹支出经费
                    ProjectExpendEOPage pageBySelfExpendYear = new ProjectExpendEOPage();
                    pageBySelfExpendYear.setProjectId(item.getId());
                    pageBySelfExpendYear.setExpendDateBegin(yearBegin);
                    pageBySelfExpendYear.setExpendDateEnd(yearEnd);
                    pageBySelfExpendYear.setExpendType(AmountType.SELF_FUND.getLabel());
                    final List<ProjectExpendEO> projectExpendEOS =
                            projectExpendEOService.queryByList(pageBySelfExpendYear);
                    final double sumBySelfExpendYear =
                            projectExpendEOS.stream().mapToDouble(ProjectExpendEO::getExpendAmount).sum();
                    final double selfExecutionsYear =
                            Double.parseDouble(format.format(sumBySelfExpendYear / sumBySelfYear * 100));
                    fundsPerformVO.setSelfExecutionsYear(selfExecutionsYear + "%");
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
            performVOList.add(fundsPerformVO);
        });

        //对各种计算数据返回的结果集
        List<FundsPerformVO> resultList = new ArrayList<>(performVOList);
        //对各种计算数据查询的标识
        Boolean calculated = Boolean.FALSE;

        //假数据开始
/*        final List<FundsPerformVO> rs20113015 = resultList.stream().filter(item -> item.getProjectName().equals("科研项目(勿动测试001)")).collect(Collectors.toList());
        rs20113015.get(0).setStateExecutions("70%");
        final List<FundsPerformVO> a123 = resultList.stream().filter(item -> item.getProjectName().equals("测试科研项目5")).collect(Collectors.toList());
        a123.get(0).setStateExecutions("72%");
        final List<FundsPerformVO> a2 = resultList.stream().filter(item -> item.getProjectName().equals("测试科研项目11")).collect(Collectors.toList());
        a2.get(0).setStateExecutions("73%");*/
        //假数据结束

//        -----------------------------一大坨查询（计算）的数据--------------------------------
        PageFundsVO pageFundsVO = new PageFundsVO();
        pageFundsVO.setFundsPerformVOS(resultList);
        if (types.length > 0) {

            return pageFundsVO;
        }
        final List<WarnColorVO> warnColorVOS = warnColor(pageFundsVO);

        //到账率
        if (StringUtils.isNotBlank(page.getArrivalRateBefore())) {
            calculated = Boolean.TRUE;
            //增加"%"后面做格式转换使用
            page.setArrivalRateBefore(page.getArrivalRateBefore() + "%");
            NumberFormat format = NumberFormat.getPercentInstance();
            final double arrivalRateDouble = format.parse(page.getArrivalRateBefore()).doubleValue();
            final List<FundsPerformVO> collect
                    = performVOList.stream()
                    .filter(item -> item.getArrivalRateDouble() >= arrivalRateDouble).collect(Collectors.toList());
            resultList.retainAll(collect);
        }

        if (StringUtils.isNotBlank(page.getArrivalRateEnd())) {
            calculated = Boolean.TRUE;
            //增加"%"后面做格式转换使用
            page.setArrivalRateEnd(page.getArrivalRateEnd() + "%");
            NumberFormat format = NumberFormat.getPercentInstance();
            final double arrivalRateDouble = format.parse(page.getArrivalRateEnd()).doubleValue();
            final List<FundsPerformVO> collect
                    = performVOList.stream()
                    .filter(item -> item.getArrivalRateDouble() <= arrivalRateDouble).collect(Collectors.toList());
            resultList.retainAll(collect);
        }

        //国拨经费执行率
        if (StringUtils.isNotBlank(page.getStateExecutionsBefore())) {
            calculated = Boolean.TRUE;
            //增加"%"后面做格式转换使用
            page.setStateExecutionsBefore(page.getStateExecutionsBefore() + "%");
            NumberFormat format = NumberFormat.getPercentInstance();
            final double rateDouble = format.parse(page.getStateExecutionsBefore()).doubleValue();
            final List<FundsPerformVO> collect
                    = performVOList.stream()
                    .filter(item -> item.getStateExecutionsDouble() >= rateDouble).collect(Collectors.toList());
            resultList.retainAll(collect);
        }

        if (StringUtils.isNotBlank(page.getStateExecutionsEnd())) {
            calculated = Boolean.TRUE;
            //增加"%"后面做格式转换使用
            page.setStateExecutionsEnd(page.getStateExecutionsEnd() + "%");
            NumberFormat format = NumberFormat.getPercentInstance();
            final double rateDouble = format.parse(page.getStateExecutionsEnd()).doubleValue();
            final List<FundsPerformVO> collect
                    = performVOList.stream()
                    .filter(item -> item.getStateExecutionsDouble() <= rateDouble).collect(Collectors.toList());
            resultList.retainAll(collect);
        }

        //国拨本季度经费执行率
        if (StringUtils.isNotBlank(page.getStateQuarterExecutionsBefore())) {
            calculated = Boolean.TRUE;
            //增加"%"后面做格式转换使用
            page.setStateQuarterExecutionsBefore(page.getStateQuarterExecutionsBefore() + "%");
            NumberFormat format = NumberFormat.getPercentInstance();
            final double rateDouble = format.parse(page.getStateQuarterExecutionsBefore()).doubleValue();
            final List<FundsPerformVO> collect
                    = performVOList.stream()
                    .filter(item -> item.getStateQuarterExecutionsDouble() >= rateDouble).collect(Collectors.toList());
            resultList.retainAll(collect);
        }

        if (StringUtils.isNotBlank(page.getStateQuarterExecutionsEnd())) {
            calculated = Boolean.TRUE;
            //增加"%"后面做格式转换使用
            page.setStateQuarterExecutionsEnd(page.getStateQuarterExecutionsEnd() + "%");
            NumberFormat format = NumberFormat.getPercentInstance();
            final double rateDouble = format.parse(page.getStateQuarterExecutionsEnd()).doubleValue();
            final List<FundsPerformVO> collect
                    = performVOList.stream()
                    .filter(item -> item.getStateQuarterExecutionsDouble() <= rateDouble).collect(Collectors.toList());
            resultList.retainAll(collect);
        }

        //国拨本年度执行率
        if (StringUtils.isNotBlank(page.getStateYearExecutionsBefore())) {
            calculated = Boolean.TRUE;
            //增加"%"后面做格式转换使用
            page.setStateYearExecutionsBefore(page.getStateYearExecutionsBefore() + "%");
            NumberFormat format = NumberFormat.getPercentInstance();
            final double rateDouble = format.parse(page.getStateYearExecutionsBefore()).doubleValue();
            final List<FundsPerformVO> collect
                    = performVOList.stream()
                    .filter(item -> item.getStateYearExecutionsDouble() >= rateDouble).collect(Collectors.toList());
            resultList.retainAll(collect);
        }

        if (StringUtils.isNotBlank(page.getStateYearExecutionsEnd())) {
            calculated = Boolean.TRUE;
            //增加"%"后面做格式转换使用
            page.setStateYearExecutionsEnd(page.getStateYearExecutionsEnd() + "%");
            NumberFormat format = NumberFormat.getPercentInstance();
            final double rateDouble = format.parse(page.getStateYearExecutionsEnd()).doubleValue();
            final List<FundsPerformVO> collect
                    = performVOList.stream()
                    .filter(item -> item.getStateYearExecutionsDouble() <= rateDouble).collect(Collectors.toList());
            resultList.retainAll(collect);
        }

        //国拨本季度经费
        if (StringUtils.isNotBlank(page.getStateFundQuarterBefore())) {
            calculated = Boolean.TRUE;
            //增加"%"后面做格式转换使用
            page.setStateFundQuarterBefore(page.getStateFundQuarterBefore() + "%");
            NumberFormat format = NumberFormat.getPercentInstance();
            final double rateDouble = format.parse(page.getStateFundQuarterBefore()).doubleValue();
            final List<FundsPerformVO> collect
                    = performVOList.stream()
                    .filter(item -> item.getStateFundQuarter() >= rateDouble).collect(Collectors.toList());
            resultList.retainAll(collect);
        }

        if (StringUtils.isNotBlank(page.getStateFundQuarterEnd())) {
            calculated = Boolean.TRUE;
            //增加"%"后面做格式转换使用
            page.setStateFundQuarterEnd(page.getStateFundQuarterEnd() + "%");
            NumberFormat format = NumberFormat.getPercentInstance();
            final double rateDouble = format.parse(page.getStateFundQuarterEnd()).doubleValue();
            final List<FundsPerformVO> collect
                    = performVOList.stream()
                    .filter(item -> item.getStateFundQuarter() <= rateDouble).collect(Collectors.toList());
            resultList.retainAll(collect);
        }


        //国拨本年度经费
        if (StringUtils.isNotBlank(page.getStateFundYeahBefore())) {
            calculated = Boolean.TRUE;
            //增加"%"后面做格式转换使用
            page.setStateFundYeahBefore(page.getStateFundYeahBefore() + "%");
            NumberFormat format = NumberFormat.getPercentInstance();
            final double rateDouble = format.parse(page.getStateFundYeahBefore()).doubleValue();
            final List<FundsPerformVO> collect
                    = performVOList.stream()
                    .filter(item -> item.getStateFundYeah() >= rateDouble).collect(Collectors.toList());
            resultList.retainAll(collect);
        }

        if (StringUtils.isNotBlank(page.getStateFundYeahEnd())) {
            calculated = Boolean.TRUE;
            //增加"%"后面做格式转换使用
            page.setStateFundYeahEnd(page.getStateFundYeahEnd() + "%");
            NumberFormat format = NumberFormat.getPercentInstance();
            final double rateDouble = format.parse(page.getStateFundYeahEnd()).doubleValue();
            final List<FundsPerformVO> collect
                    = performVOList.stream()
                    .filter(item -> item.getStateFundYeah() <= rateDouble).collect(Collectors.toList());
            resultList.retainAll(collect);
        }

        //自筹本季度经费
        if (StringUtils.isNotBlank(page.getSelfFundedQuarterBefore())) {
            calculated = Boolean.TRUE;
            //增加"%"后面做格式转换使用
            page.setSelfFundedQuarterBefore(page.getSelfFundedQuarterBefore() + "%");
            NumberFormat format = NumberFormat.getPercentInstance();
            final double rateDouble = format.parse(page.getSelfFundedQuarterBefore()).doubleValue();
            final List<FundsPerformVO> collect
                    = performVOList.stream()
                    .filter(item -> item.getSelfFundedQuarter() >= rateDouble).collect(Collectors.toList());
            resultList.retainAll(collect);
        }

        if (StringUtils.isNotBlank(page.getSelfFundedQuarterEnd())) {
            calculated = Boolean.TRUE;
            //增加"%"后面做格式转换使用
            page.setSelfFundedQuarterEnd(page.getSelfFundedQuarterEnd() + "%");
            NumberFormat format = NumberFormat.getPercentInstance();
            final double rateDouble = format.parse(page.getSelfFundedQuarterEnd()).doubleValue();
            final List<FundsPerformVO> collect
                    = performVOList.stream()
                    .filter(item -> item.getSelfFundedQuarter() <= rateDouble).collect(Collectors.toList());
            resultList.retainAll(collect);
        }


        //自筹本季度经费
        if (StringUtils.isNotBlank(page.getSelfFundedYearBefore())) {
            calculated = Boolean.TRUE;
            //增加"%"后面做格式转换使用
            page.setSelfFundedYearBefore(page.getSelfFundedYearBefore() + "%");
            NumberFormat format = NumberFormat.getPercentInstance();
            final double rateDouble = format.parse(page.getSelfFundedYearBefore()).doubleValue();
            final List<FundsPerformVO> collect
                    = performVOList.stream()
                    .filter(item -> item.getSelfFundedYear() >= rateDouble).collect(Collectors.toList());
            resultList.retainAll(collect);
        }

        if (StringUtils.isNotBlank(page.getSelfFundedYearEnd())) {
            calculated = Boolean.TRUE;
            //增加"%"后面做格式转换使用
            page.setSelfFundedYearEnd(page.getSelfFundedYearEnd() + "%");
            NumberFormat format = NumberFormat.getPercentInstance();
            final double rateDouble = format.parse(page.getSelfFundedYearEnd()).doubleValue();
            final List<FundsPerformVO> collect
                    = performVOList.stream()
                    .filter(item -> item.getSelfFundedYear() <= rateDouble).collect(Collectors.toList());
            resultList.retainAll(collect);
        }


        //自筹经费执行率
        if (StringUtils.isNotBlank(page.getSelfExecutionsBefore())) {
            calculated = Boolean.TRUE;
            //增加"%"后面做格式转换使用
            page.setSelfExecutionsBefore(page.getSelfExecutionsBefore() + "%");
            NumberFormat format = NumberFormat.getPercentInstance();
            final double rateDouble = format.parse(page.getSelfExecutionsBefore()).doubleValue();
            final List<FundsPerformVO> collect
                    = performVOList.stream()
                    .filter(item -> item.getSelfExecutionsDouble() >= rateDouble).collect(Collectors.toList());
            resultList.retainAll(collect);
        }

        if (StringUtils.isNotBlank(page.getSelfExecutionsEnd())) {
            calculated = Boolean.TRUE;
            //增加"%"后面做格式转换使用
            page.setSelfExecutionsEnd(page.getSelfExecutionsEnd() + "%");
            NumberFormat format = NumberFormat.getPercentInstance();
            final double rateDouble = format.parse(page.getSelfExecutionsEnd()).doubleValue();
            final List<FundsPerformVO> collect
                    = performVOList.stream()
                    .filter(item -> item.getSelfExecutionsDouble() <= rateDouble).collect(Collectors.toList());
            resultList.retainAll(collect);
        }

        //自筹本季度经费执行率
        if (StringUtils.isNotBlank(page.getSelfExecutionsQuarterBefore())) {
            calculated = Boolean.TRUE;
            //增加"%"后面做格式转换使用
            page.setSelfExecutionsQuarterBefore(page.getSelfExecutionsQuarterBefore() + "%");
            NumberFormat format = NumberFormat.getPercentInstance();
            final double rateDouble = format.parse(page.getSelfExecutionsQuarterBefore()).doubleValue();
            final List<FundsPerformVO> collect
                    = performVOList.stream()
                    .filter(item -> item.getSelfExecutionsQuarterDouble() >= rateDouble).collect(Collectors.toList());
            resultList.retainAll(collect);
        }

        if (StringUtils.isNotBlank(page.getSelfExecutionsQuarterEnd())) {
            calculated = Boolean.TRUE;
            //增加"%"后面做格式转换使用
            page.setSelfExecutionsQuarterEnd(page.getSelfExecutionsQuarterEnd() + "%");
            NumberFormat format = NumberFormat.getPercentInstance();
            final double rateDouble = format.parse(page.getSelfExecutionsQuarterEnd()).doubleValue();
            final List<FundsPerformVO> collect
                    = performVOList.stream()
                    .filter(item -> item.getSelfExecutionsQuarterDouble() <= rateDouble).collect(Collectors.toList());
            resultList.retainAll(collect);
        }


        //本年度自筹经费执行率
        if (StringUtils.isNotBlank(page.getSelfExecutionsYearBefore())) {
            calculated = Boolean.TRUE;
            //增加"%"后面做格式转换使用
            page.setSelfExecutionsYearBefore(page.getSelfExecutionsYearBefore() + "%");
            NumberFormat format = NumberFormat.getPercentInstance();
            final double rateDouble = format.parse(page.getSelfExecutionsYearBefore()).doubleValue();
            final List<FundsPerformVO> collect
                    = performVOList.stream()
                    .filter(item -> item.getSelfExecutionsYearDouble() >= rateDouble).collect(Collectors.toList());
            resultList.retainAll(collect);
        }

        if (StringUtils.isNotBlank(page.getSelfExecutionsYearEnd())) {
            calculated = Boolean.TRUE;
            //增加"%"后面做格式转换使用
            page.setSelfExecutionsYearEnd(page.getSelfExecutionsYearEnd() + "%");
            NumberFormat format = NumberFormat.getPercentInstance();
            final double rateDouble = format.parse(page.getSelfExecutionsYearEnd()).doubleValue();
            final List<FundsPerformVO> collect
                    = performVOList.stream()
                    .filter(item -> item.getSelfExecutionsYearDouble() <= rateDouble).collect(Collectors.toList());
            resultList.retainAll(collect);
        }


//--------------------------------------一大坨计算的数据结束----------------------------------------------------

        PageFundsVO fundsVO = new PageFundsVO();

        if (calculated) {
            fundsVO.setSize(resultList.stream().count());
            final List<List<FundsPerformVO>> partition = Lists.partition(resultList,
                    page.getPage() * page.getPageSize());
            if (partition.size() == 0) {
                return fundsVO;
            }
            final List<FundsPerformVO> fundsPerformVOS = partition.get(0);
            final List<FundsPerformVO> fundsPerformVOSList = resultList.subList
                    ((page.getPage() - 1) * page.getPageSize(), fundsPerformVOS.size());
            fundsVO.setFundsPerformVOS(fundsPerformVOSList);
        } else {
            fundsVO.setSize(performVOList.stream().count());
            final List<List<FundsPerformVO>> partition = Lists.partition(performVOList,
                    page.getPage() * page.getPageSize());
            if (partition.size() == 0) {
                return fundsVO;
            }
            final List<FundsPerformVO> fundsPerformVOS = partition.get(0);
            final List<FundsPerformVO> fundsPerformVOSList = fundsPerformVOS.subList
                    ((page.getPage() - 1) * page.getPageSize(), fundsPerformVOS.size());
            fundsVO.setFundsPerformVOS(fundsPerformVOSList);
        }
        //增加颜色信息
        final List<FundsPerformVO> fundsPerformVOS = fundsVO.getFundsPerformVOS();
        warnColorVOS.forEach(color->{
            for (FundsPerformVO fundsPerformVO : fundsPerformVOS) {
                if (color.getProjectId().equals(fundsPerformVO.getId())){
                    fundsPerformVO.setColor(color.getColor());
                }
            }
        });
        return fundsVO;
    }

    public void earlyWarning(WarnVO warnVO) throws Exception {

        final ProjectDataEO projectDataEO = projectDataEOService.selectByPrimaryKey(warnVO.getProjectId());
        AtomicReference<String> technicalIds = new AtomicReference<>("");
        //解析项目负责人(或许有多个)
        final ArrayList technicalList = JSON.parseObject(projectDataEO.getTechnicalDirector(), ArrayList.class);
        if (CollectionUtils.isNotEmpty(technicalList)) {
            technicalList.forEach(i -> {
                Map<String, String> technicalerInfo = (Map<String, String>) i;
                if (Objects.nonNull(technicalerInfo.get("TECHNICAL_DIRECTOR_NAME"))) {
                    if (projectDataEO.getTechnicalDirector().contains("}")) {
                        projectDataEO.setTechnicalDirector(technicalerInfo.get("TECHNICAL_DIRECTOR_NAME"));
                    } else {
                        projectDataEO.setTechnicalDirector(projectDataEO.getTechnicalDirector() == null
                                ? "" : (projectDataEO.getTechnicalDirector() + ",")
                                + technicalerInfo.get("TECHNICAL_DIRECTOR_NAME"));
                    }
                }
                if (Objects.nonNull(technicalerInfo.get("TECHNICAL_DIRECTOR_ID"))) {
                    if (StringUtils.isBlank(technicalIds.get())) {
                        technicalIds.set(technicalerInfo.get("TECHNICAL_DIRECTOR_ID"));
                    } else {
                        technicalIds.set(technicalIds.get() + ","
                                + technicalerInfo.get("TECHNICAL_DIRECTOR_ID"));
                    }
                }
            });
        }

        Date date = new Date();
        //向projectWarnEOService插入数据（起到日志作用，可查）
        ProjectWarnEO projectWarnEO = new ProjectWarnEO();
        projectWarnEO.setId(UUID.randomUUID10());
        projectWarnEO.setCreateUserId(UserUtils.getUserId());
        projectWarnEO.setProjectId(warnVO.getProjectId());
        projectWarnEO.setProjectTypeName(projectDataEO.getProjectTypeName());
        projectWarnEO.setProjectName(warnVO.getProjectName());
        projectWarnEO.setProjectCode(warnVO.getProjectCode());
        projectWarnEO.setReceivedContent(warnVO.getContent());
        projectWarnEO.setReceivedUser(projectDataEO.getTechnicalDirector());
        projectWarnEO.setCreateTime(date);
        projectWarnEO.setSendTime(date);
        projectWarnEO.setSendStatus("1");
        projectWarnEOService.insert(projectWarnEO);

        //向noticeEOService插入数据（为了发送数据）
        NoticeEO noticeEO = new NoticeEO();
        noticeEO.setId(UUID.randomUUID10());
        noticeEO.setCreateTime(date);
        noticeEO.setUpdateTime(date);
        noticeEO.setNoticeName("经费执行预警信息");
        //其他人不可见
        noticeEO.setNoticeIsLook("0");
        //系统通知
        noticeEO.setNoticeTypeId("RESWHTV8TB");
        noticeEO.setNoticeContent(warnVO.getContent());
        noticeEO.setDelFlag(String.valueOf(DelState.NOT_DELETED.getCode()));
        noticeEO.setCreateUserId(UserUtils.getUserId());
        noticeEO.setReceiveUserIds(technicalIds.get());
        noticeEOService.insertNotice(noticeEO);
    }

    @Scheduled(cron = "1 1 1 * * ? ")
    public void doWarn() throws Exception {
        Date date = new Date();
        List<ProjectWarnEO> projectWarnEOList = new ArrayList<>();
        WarnNoticeEOPage warnNoticeEOPage = new WarnNoticeEOPage();
        warnNoticeEOPage.setTitleOperator("LIKE");
        warnNoticeEOPage.setWarnCotentOperator("LIKE");
        warnNoticeEOPage.setDelFlag("0");

        List<WarnNoticeEO> warnList = warnNoticeEOService.queryByPage(warnNoticeEOPage);

        ProjectDataEOPage projectDataEOPage = new ProjectDataEOPage();
        Integer[] types = new Integer[1];
        types[0] = ALARM;
        final PageFundsVO pageFundsVO = this.page(projectDataEOPage, types);
        List<FundsPerformVO> fundsPerformVOS = pageFundsVO.getFundsPerformVOS();

        NumberFormat format = NumberFormat.getPercentInstance();

        List<FundsPerformVO> finalFundsPerformVOS = fundsPerformVOS;
        warnList.forEach(warn -> {
            finalFundsPerformVOS.forEach(funds -> {
                if (warn.getProjectTypeId().equals(funds.getProjectTypeName())) {

                    //国拨
                    if (Objects.nonNull(warn.getNationalLowestProgress())
                            && !warn.getNationalLowestProgress().contains("%")) {
                        warn.setNationalLowestProgress(warn.getNationalLowestProgress() + "%");
                    }
                    if (Objects.nonNull(funds.getSelfExecutions()) && !funds.getSelfExecutions().contains("%")) {
                        funds.setSelfExecutions(funds.getSelfExecutions() + "%");
                    }
                    double warnStateRate = 0d;
                    double fundsStateRate = 0d;

                    try {
                        warnStateRate
                                = format.parse(warn.getNationalLowestProgress() == null
                                ? "0%" : warn.getNationalLowestProgress()).doubleValue();
                    } catch (ParseException e) {
                        logger.warn("warn国拨解析问题");
                    }
                    try {
                        fundsStateRate
                                = format.parse(funds.getStateExecutions() == null
                                ? "0%" : funds.getStateExecutions()).doubleValue();
                    } catch (ParseException e) {
                        logger.warn("funds国拨解析问题");

                    }

                    if (fundsStateRate != 0 && warnStateRate > fundsStateRate) {
                        try {
                            ProjectDataEO projectDataEO = new ProjectDataEO();
                            try {

                                projectDataEO = this.projectDataEOService.selectByPrimaryKey(funds.getId());
                            } catch (Exception e) {
                                logger.error("项目id读取失败");
                            }
                            AtomicReference<String> technicalIds = new AtomicReference<>("");
                            //解析项目负责人(或许有多个)
                            final ArrayList technicalList = JSON.parseObject(projectDataEO.getTechnicalDirector(), ArrayList.class);
                            if (CollectionUtils.isNotEmpty(technicalList)) {
                                ProjectDataEO finalProjectDataEO = projectDataEO;
                                technicalList.forEach(i -> {
                                    Map<String, String> technicalerInfo = (Map<String, String>) i;
                                    if (Objects.nonNull(technicalerInfo.get("TECHNICAL_DIRECTOR_NAME"))) {
                                        if (finalProjectDataEO.getTechnicalDirector().contains("}")) {
                                            finalProjectDataEO.setTechnicalDirector(technicalerInfo.get("TECHNICAL_DIRECTOR_NAME"));
                                        } else {
                                            finalProjectDataEO.setTechnicalDirector(finalProjectDataEO.getTechnicalDirector() == null
                                                    ? "" : (finalProjectDataEO.getTechnicalDirector() + ",")
                                                    + technicalerInfo.get("TECHNICAL_DIRECTOR_NAME"));
                                        }
                                    }
                                    if (Objects.nonNull(technicalerInfo.get("TECHNICAL_DIRECTOR_ID"))) {
                                        if (StringUtils.isBlank(technicalIds.get())) {
                                            technicalIds.set(technicalerInfo.get("TECHNICAL_DIRECTOR_ID"));
                                        } else {
                                            technicalIds.set(technicalIds.get() + ","
                                                    + technicalerInfo.get("TECHNICAL_DIRECTOR_ID"));
                                        }
                                    }
                                });
                            }

                            ProjectWarnEO projectWarnEO = new ProjectWarnEO();
                            projectWarnEO.setCreateUserId(UserUtils.getUserId());
                            projectWarnEO.setProjectId(funds.getId());
                            projectWarnEO.setProjectTypeName(projectDataEO.getProjectTypeName());
                            projectWarnEO.setProjectName(funds.getProjectName());
                            projectWarnEO.setProjectCode(funds.getProjectCode());
                            projectWarnEO.setReceivedContent(warn.getWarnCotent());
                            projectWarnEO.setReceivedUser(projectDataEO.getTechnicalDirector());
                            projectWarnEO.setCreateTime(date);
                            projectWarnEO.setSendTime(date);
                            projectWarnEO.setSendStatus("1");
                            projectWarnEO.setTitle(warn.getTitle());
                            projectWarnEO.setExpendProgress(warn.getNationalLowestProgress());
                            projectWarnEO.setTechnicalIds(technicalIds.get());
                            projectWarnEOList.add(projectWarnEO);
                        } catch (Exception e) {
                            logger.error("发送失败");
                        }
                    }

                    //自筹
                    if (Objects.nonNull(warn.getSelfLowestProgress())
                            && !warn.getSelfLowestProgress().contains("%")) {
                        warn.setSelfLowestProgress(warn.getSelfLowestProgress() + "%");
                    }
                    if (Objects.nonNull(funds.getSelfExecutions()) && !funds.getSelfExecutions().contains("%")) {
                        funds.setSelfExecutions(funds.getSelfExecutions() + "%");
                    }
                    double warnSelfRate = 0d;
                    double fundsSelfRate = 0d;

                    try {
                        warnSelfRate
                                = format.parse(warn.getSelfLowestProgress() == null
                                ? "0%" : warn.getSelfLowestProgress()).doubleValue();
                    } catch (ParseException e) {
                        logger.warn("warn自筹解析问题");
                    }
                    try {
                        fundsSelfRate
                                = format.parse(funds.getSelfExecutions() == null
                                ? "0%" : funds.getSelfExecutions()).doubleValue();
                    } catch (ParseException e) {
                        logger.warn("funds自筹解析问题");

                    }
                    if (fundsSelfRate != 0 && warnSelfRate > fundsSelfRate) {
                        try {
                            ProjectDataEO projectDataEO = new ProjectDataEO();
                            try {

                                projectDataEO = this.projectDataEOService.selectByPrimaryKey(funds.getId());
                            } catch (Exception e) {
                                logger.error("项目id读取失败");
                            }
                            AtomicReference<String> technicalIds = new AtomicReference<>("");
                            //解析项目负责人(或许有多个)
                            final ArrayList technicalList = JSON.parseObject(projectDataEO.getTechnicalDirector(), ArrayList.class);
                            if (CollectionUtils.isNotEmpty(technicalList)) {
                                ProjectDataEO finalProjectDataEO = projectDataEO;
                                technicalList.forEach(i -> {
                                    Map<String, String> technicalerInfo = (Map<String, String>) i;
                                    if (Objects.nonNull(technicalerInfo.get("TECHNICAL_DIRECTOR_NAME"))) {
                                        if (finalProjectDataEO.getTechnicalDirector().contains("}")) {
                                            finalProjectDataEO.setTechnicalDirector(technicalerInfo.get("TECHNICAL_DIRECTOR_NAME"));
                                        } else {
                                            finalProjectDataEO.setTechnicalDirector(finalProjectDataEO.getTechnicalDirector() == null
                                                    ? "" : (finalProjectDataEO.getTechnicalDirector() + ",")
                                                    + technicalerInfo.get("TECHNICAL_DIRECTOR_NAME"));
                                        }
                                    }
                                    if (Objects.nonNull(technicalerInfo.get("TECHNICAL_DIRECTOR_ID"))) {
                                        if (StringUtils.isBlank(technicalIds.get())) {
                                            technicalIds.set(technicalerInfo.get("TECHNICAL_DIRECTOR_ID"));
                                        } else {
                                            technicalIds.set(technicalIds.get() + ","
                                                    + technicalerInfo.get("TECHNICAL_DIRECTOR_ID"));
                                        }
                                    }
                                });
                            }

                            ProjectWarnEO projectWarnEO = new ProjectWarnEO();
                            projectWarnEO.setCreateUserId(UserUtils.getUserId());
                            projectWarnEO.setProjectId(funds.getId());
                            projectWarnEO.setProjectTypeName(projectDataEO.getProjectTypeName());
                            projectWarnEO.setProjectName(funds.getProjectName());
                            projectWarnEO.setProjectCode(funds.getProjectCode());
                            projectWarnEO.setReceivedContent(warn.getWarnCotent());
                            projectWarnEO.setReceivedUser(projectDataEO.getTechnicalDirector());
                            projectWarnEO.setCreateTime(date);
                            projectWarnEO.setSendTime(date);
                            projectWarnEO.setSendStatus("1");
                            projectWarnEO.setTitle(warn.getTitle());
                            projectWarnEO.setExpendProgress(warn.getSelfLowestProgress());
                            projectWarnEO.setTechnicalIds(technicalIds.get());
                            projectWarnEOList.add(projectWarnEO);
                        } catch (Exception e) {
                            logger.error("发送失败");
                        }
                    }
                }
            });
        });
        //去重
        final List<ProjectWarnEO> collect = projectWarnEOList.stream().distinct().collect(Collectors.toList());
        send(collect);

    }

    private void send(List<ProjectWarnEO> projectWarnEOList) throws Exception {
        projectWarnEOList.forEach(item -> {
            try {
                ProjectWarnEOPage page = new ProjectWarnEOPage();
                page.setProjectCode(item.getProjectCode());
                page.setTitle(item.getTitle());
                page.setProjectName(item.getProjectName());
                page.setProjectTypeName(item.getProjectTypeName());
                page.setExpendProgress(item.getExpendProgress());
                page.setReceivedUser(item.getReceivedUser());
                final int count = this.projectWarnEOService.queryByCount(page);
                if (count == 0) {
                    item.setId(UUID.randomUUID10());
                    item.setExpendProgress(item.getExpendProgress().replace("%", ""));
                    projectWarnEOService.insert(item);
                    //向noticeEOService插入数据（为了发送数据）
                    NoticeEO noticeEO = new NoticeEO();
                    noticeEO.setId(UUID.randomUUID10());
                    noticeEO.setCreateTime(item.getSendTime());
                    noticeEO.setNoticeName("经费执行预警信息");
                    //其他人不可见
                    noticeEO.setNoticeIsLook("0");
                    //系统通知
                    noticeEO.setNoticeTypeId("RESWHTV8TB");
                    noticeEO.setNoticeContent(item.getReceivedContent());
                    noticeEO.setDelFlag(String.valueOf(DelState.NOT_DELETED.getCode()));
                    noticeEO.setCreateUserId(UserUtils.getUserId());
                    noticeEO.setReceiveUserIds(item.getTechnicalIds());
                    noticeEOService.insertNotice(noticeEO);
                } else {
                    logger.warn("经费警告相同，不进行发送");
                }

            } catch (Exception e) {
                logger.error("经费警告发送失败");
            }

        });

    }

    public List<WarnColorVO> warnColor(PageFundsVO pageFundsVO) throws Exception {
        List<WarnColorVO> result = new ArrayList<>();

        final List<WarnRuleEO> warnRuleEOS = warnRuleEOService.queryByList(new WarnRuleEOPage());

        Integer[] types = new Integer[1];
        types[0] = ALARM;
        List<FundsPerformVO> fundsPerformVOS = pageFundsVO.getFundsPerformVOS();

        warnRuleEOS.forEach(warnList -> {
            for (FundsPerformVO fundsPerformVO : fundsPerformVOS) {
                if (warnList.getProjectTypeId().equals(fundsPerformVO.getProjectTypeName())) {


                    //自筹
                    if (Objects.nonNull(warnList.getSelfLowestProgress())
                            && !warnList.getSelfLowestProgress().contains("%")) {
                        warnList.setSelfLowestProgress(warnList.getSelfLowestProgress() + "%");
                    }
                    if (Objects.nonNull(fundsPerformVO.getSelfExecutions()) && !fundsPerformVO.getSelfExecutions().contains("%")) {
                        fundsPerformVO.setSelfExecutions(fundsPerformVO.getSelfExecutions() + "%");
                    }
                    double warnSelfRate = 0d;
                    double fundsSelfRate = 0d;

                    try {
                        warnSelfRate
                                = format.parse(warnList.getSelfLowestProgress() == null
                                ? "0%" : warnList.getSelfLowestProgress()).doubleValue();
                    } catch (ParseException e) {
                        logger.warn("warn自筹解析问题");
                    }
                    try {
                        fundsSelfRate
                                = format.parse(fundsPerformVO.getSelfExecutions() == null
                                ? "0%" : fundsPerformVO.getSelfExecutions()).doubleValue();
                    } catch (ParseException e) {
                        logger.warn("funds自筹解析问题");
                    }
                    if (fundsSelfRate != 0 && warnSelfRate > fundsSelfRate) {
                        WarnColorVO warnColorVO = new WarnColorVO();
                        warnColorVO.setProjectId(fundsPerformVO.getId());
                        warnColorVO.setColor(warnList.getWarnColor());
                        result.add(warnColorVO);
                    }


                    //国拨
                    if (Objects.nonNull(warnList.getNationalLowestProgress())
                            && !warnList.getNationalLowestProgress().contains("%")) {
                        warnList.setNationalLowestProgress(warnList.getNationalLowestProgress() + "%");
                    }
                    if (Objects.nonNull(fundsPerformVO.getStateExecutions())
                            && !fundsPerformVO.getStateExecutions().contains("%")) {
                        fundsPerformVO.setStateExecutions(fundsPerformVO.getStateExecutions() + "%");
                    }
                    double warnStateRate = 0d;
                    double fundsStateRate = 0d;

                    try {
                        warnStateRate
                                = format.parse(warnList.getNationalLowestProgress() == null
                                ? "0%" : warnList.getNationalLowestProgress()).doubleValue();
                    } catch (ParseException e) {
                        logger.warn("warn自筹解析问题");
                    }
                    try {
                        fundsStateRate
                                = format.parse(fundsPerformVO.getStateExecutions() == null
                                ? "0%" : fundsPerformVO.getStateExecutions()).doubleValue();
                    } catch (ParseException e) {
                        logger.warn("funds自筹解析问题");
                    }
                    if (fundsStateRate != 0 && warnStateRate > fundsStateRate) {
                        WarnColorVO warnColorVO = new WarnColorVO();
                        warnColorVO.setProjectId(fundsPerformVO.getId());
                        warnColorVO.setColor(warnList.getWarnColor());
                        result.add(warnColorVO);
                    }

                }
            }


        });

        return result;
    }


}
