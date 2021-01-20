package com.adc.da.research.funds.service;

import com.adc.da.research.funds.eum.BudgetType;
import com.adc.da.research.funds.eum.ComparedDetailType;
import com.adc.da.research.funds.vo.change.BudgetFundVO;
import com.adc.da.research.funds.vo.change.DetailChangeVO;
import com.adc.da.research.funds.vo.change.FundChangeVO;
import com.adc.da.research.project.dao.*;
import com.adc.da.research.project.entity.*;
import com.adc.da.research.project.page.*;
import com.adc.da.research.project.service.*;
import com.adc.da.research.project.vo.ChangeProjectVO;
import com.adc.da.research.project.vo.DifferenceDeliverableVO;
import com.adc.da.research.project.vo.DifferencePeriodicalVO;
import com.adc.da.sys.service.UserEOService;
import com.adc.da.util.utils.BeanMapper;
import com.adc.da.util.utils.CollectionUtils;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created with IntelliJ IDEA.
 *
 * @Auther: yanyujie
 * @Date: 2020/12/04
 * @Description:
 */

@Service
public class ChangeService {

    private static final Logger logger = LoggerFactory.getLogger(ChangeService.class);

    @Autowired
    private BudgetFundEOService budgetFundEOService;
    @Autowired
    private BudgetFundHistoryEOService budgetFundHistoryEOService;
    @Autowired
    private BudgetDetailEOService budgetDetailEOService;
    @Autowired
    private BudgetDetailHistoryEOService budgetDetailHistoryEOService;
    @Autowired
    private WorkProgressEOService workProgressEOService;
    @Autowired
    private WorkProgressHistoryEOService workProgressHistoryEOService;
    @Autowired
    private CheckTargetEOService checkTargetEOService;
    @Autowired
    private CheckTargetHistoryEOService checkTargetHistoryEOService;
    @Autowired
    private MemberInfoEOService memberInfoEOService;
    @Autowired
    private MemberInfoHistoryEOService memberInfoHistoryEOService;
    @Autowired
    private ResearchDetailHistoryEOService researchDetailHistoryEOService;
    @Autowired
    private ResearchBudgetDetailEOService researchBudgetDetailEOService;
    @Autowired
    private UserEOService userEOService;
    @Autowired
    private BeanMapper beanMapper;
    @Autowired
    private ProjectDataEOService projectDataEOService;

    @Autowired
    private BudgetFundEODao budgetFundEODao;
    @Autowired
    private BudgetFundHistoryEODao budgetFundHistoryEODao;
    @Autowired
    private BudgetDetailEODao budgetDetailEODao;
    @Autowired
    private BudgetDetailHistoryEODao budgetDetailHistoryEODao;
    @Autowired
    private WorkProgressEODao workProgressEODao;
    @Autowired
    private WorkProgressHistoryEODao workProgressHistoryEODao;
    @Autowired
    private CheckTargetEODao checkTargetEODao;
    @Autowired
    private CheckTargetHistoryEODao checkTargetHistoryEODao;
    @Autowired
    private MemberInfoEODao memberInfoEODao;
    @Autowired
    private MemberInfoHistoryEODao memberInfoHistoryEODao;
    @Autowired
    private ResearchBudgetDetailEODao researchBudgetDetailEODao;
    @Autowired
    private ResearchDetailHistoryEODao researchDetailHistoryEODao;
    @Autowired
    private ResearchProjectChangeEOService researchProjectChangeEOService;

    /***
     * @Description: history为历史记录（修改前的数据），budgetFundEO为审批修改后的新数据
     * @Param: [projectId]
     * @return: void
     * @Author: yanyujie
     * @Date: 2020/12/3
     */
    public List<FundChangeVO> comparedBudgetFund(String projectId) throws Exception {

        List<FundChangeVO> resultFunds = new ArrayList<>();

        BudgetFundEOPage budgetFundEOPage = new BudgetFundEOPage();
        budgetFundEOPage.setProjectId(projectId);
        final List<BudgetFundEO> budgetFundEOS
                = this.budgetFundEOService.queryByList(budgetFundEOPage);

        BudgetFundHistoryEOPage budgetFundHistoryEOPage = new BudgetFundHistoryEOPage();
        budgetFundHistoryEOPage.setProjectId(projectId);
        final List<BudgetFundHistoryEO> budgetFundHistoryEOS
                = this.budgetFundHistoryEOService.queryByList(budgetFundHistoryEOPage);

        //俩种可能性：
        // 第一种带年份的
        //--------------------------带年份的开始--------------------
        final Map<String, List<BudgetFundHistoryEO>> historyList
                = budgetFundHistoryEOS.stream().filter(history
                -> Objects.nonNull(history.getBudgetYear()))
                .collect(Collectors.groupingBy(BudgetFundHistoryEO::getBudgetYear));

        final Map<String, List<BudgetFundEO>> budgetFundList
                = budgetFundEOS.stream().filter(noHistory
                -> Objects.nonNull(noHistory.getBudgetYear()))
                .collect(Collectors.groupingBy(BudgetFundEO::getBudgetYear));

        historyList.forEach((key, value) -> {
            if (budgetFundList.containsKey(key)) {
                final List<BudgetFundEO> budgetFundItem = budgetFundList.get(key);
                if (budgetFundItem.get(0).getBudgetType().equals(value.get(0).getBudgetType())) {
                    //金额不相等的情况
                    if (value.get(0).getBudgetAmount() != budgetFundItem.get(0).getBudgetAmount()) {
                        FundChangeVO fundChangeVO = new FundChangeVO();
                        fundChangeVO.setYear(key);
                        fundChangeVO.setBudgetType(value.get(0).getBudgetType());
                        fundChangeVO.setOriginalAmount(String.valueOf(value.get(0).getBudgetAmount()));
                        fundChangeVO.setExistingAmount(String.valueOf(budgetFundItem.get(0).getBudgetAmount()));
                        fundChangeVO.setDifferenceAmount(String.valueOf(value.get(0).getBudgetAmount()
                                - budgetFundItem.get(0).getBudgetAmount()));

                        resultFunds.add(fundChangeVO);
                    }
                }
            }
        });
        //--------------------------带年份的结束--------------------


//        第二种情况
//        --------------------------不带年份的情况----------------------------
        final List<BudgetFundHistoryEO> historyListNoYear
                = budgetFundHistoryEOS.stream().filter(history
                -> Objects.isNull(history.getBudgetYear()))
                .collect(Collectors.toList());

        final List<BudgetFundEO> budgetFundListNoYear
                = budgetFundEOS.stream().filter(noHistory
                -> Objects.isNull(noHistory.getBudgetYear()))
                .collect(Collectors.toList());

        historyListNoYear.forEach(item -> {
            for (BudgetFundEO budgetFundEO : budgetFundListNoYear) {
                if (item.getBudgetType().equals(budgetFundEO.getBudgetType())) {
                    if (item.getBudgetAmount() != budgetFundEO.getBudgetAmount()) {
                        FundChangeVO fundChangeVO = new FundChangeVO();
                        fundChangeVO.setBudgetType(item.getBudgetType());
                        fundChangeVO.setOriginalAmount(String.valueOf(item.getBudgetAmount()));
                        fundChangeVO.setExistingAmount(String.valueOf(item.getBudgetAmount()));
                        fundChangeVO.setDifferenceAmount(String.valueOf(item.getBudgetAmount()
                                - budgetFundEO.getBudgetAmount()));
                        resultFunds.add(fundChangeVO);
                    }
                }
            }
        });

        return resultFunds;
    }

    /***
     * @Description: 经费详情
     * @Param: [projectId, type, param]
     * @return: java.util.List<com.adc.da.research.funds.vo.change.DetailChangeVO>
     * @Author: yanyujie
     * @Date: 2020/12/7
     */
    public List<DetailChangeVO> comparedDetailItem(String projectId, Integer type, String param) throws Exception {
        final List<DetailChangeVO> detailChangeList = extractDetail(projectId);

        List<DetailChangeVO> result = new ArrayList<>();
        //年度
        if (ComparedDetailType.YEAR.getValue() == type) {
//            final List<DetailChangeVO> detailChangeY = this.comparedBudgetDetail(projectId, type);
            final List<DetailChangeVO> detailChangeY = detailChangeList.stream()
                    .filter(item -> Objects.nonNull(item.getYear())).collect(Collectors.toList());
            final Map<String, List<DetailChangeVO>> collectY = detailChangeY.stream()
                    .collect(Collectors.groupingBy(DetailChangeVO::getYear));
            if (collectY.containsKey(param)) {
                final List<DetailChangeVO> detailChangeVOS = collectY.get(param);
                result.addAll(detailChangeVOS);
            }
        }
        //季度
        if (ComparedDetailType.QUARTER.getValue() == type) {
//            final List<DetailChangeVO> detailChangeQ = this.comparedBudgetDetail(projectId, type);
            final List<DetailChangeVO> detailChangeQ = detailChangeList.stream()
                    .filter(item -> Objects.nonNull(item.getQuarter())).collect(Collectors.toList());
            final Map<String, List<DetailChangeVO>> collectQ = detailChangeQ.stream()
                    .collect(Collectors.groupingBy(DetailChangeVO::getQuarter));
            if (collectQ.containsKey(param)) {
                final List<DetailChangeVO> detailChangeVOS = collectQ.get(param);
                result.addAll(detailChangeVOS);
            }
        }
        //月度
        if (ComparedDetailType.MONTH.getValue() == type) {
//            final List<DetailChangeVO> detailChangeM = this.comparedBudgetDetail(projectId, type);
            final List<DetailChangeVO> detailChangeM = detailChangeList.stream()
                    .filter(item -> Objects.nonNull(item.getMonth())).collect(Collectors.toList());
            final Map<String, List<DetailChangeVO>> collectM = detailChangeM.stream()
                    .collect(Collectors.groupingBy(DetailChangeVO::getMonth));
            if (collectM.containsKey(param)) {
                final List<DetailChangeVO> detailChangeVOS = collectM.get(param);
                result.addAll(detailChangeVOS);
            }
        }
        return result;
    }

    /***
     * @Description: 经费查看
     * @Param: [projectId]
     * @return: java.util.Map<java.lang.String, java.util.List < com.adc.da.research.funds.vo.change.DetailChangeVO>>
     * @Author: yanyujie
     * @Date: 2020/12/7
     */
    public Map<String, List<DetailChangeVO>> comparedDetailAll(String projectId) throws Exception {

        Map<String, List<DetailChangeVO>> resutMap = new HashMap<>();
        final List<DetailChangeVO> detailChangeVOS = extractDetail(projectId);
        final Map<String, List<DetailChangeVO>> collectM = detailChangeVOS.stream().filter(item
                -> Objects.nonNull(item.getMonth())).
                collect(Collectors.groupingBy(DetailChangeVO::getMonth));
        final Map<String, List<DetailChangeVO>> collectQ = detailChangeVOS.stream().filter(item
                -> Objects.nonNull(item.getQuarter())).
                collect(Collectors.groupingBy(DetailChangeVO::getQuarter));
        final Map<String, List<DetailChangeVO>> collectY = detailChangeVOS.stream().filter(item
                -> Objects.nonNull(item.getYear())).
                collect(Collectors.groupingBy(DetailChangeVO::getYear));
        //月
        Map<String, List<DetailChangeVO>> month = new HashMap<>();
        List<DetailChangeVO> monthItem = new ArrayList<>();
        collectM.forEach((key, value) -> {
            monthItem.addAll(value);
        });
        month.put("month", monthItem.stream().filter(item
                -> !item.getBudgetName().contains("资金总额")).collect(Collectors.toList()));
        //季度
        Map<String, List<DetailChangeVO>> quarterly = new HashMap<>();
        List<DetailChangeVO> quarterlyItem = new ArrayList<>();
        collectQ.forEach((key, value) -> {
            quarterlyItem.addAll(value);
        });
        quarterly.put("quarter", quarterlyItem.stream().filter(item
                -> !item.getBudgetName().contains("资金总额")).collect(Collectors.toList()));

        //年度
        Map<String, List<DetailChangeVO>> year = new HashMap<>();
        List<DetailChangeVO> yearItem = new ArrayList<>();
        collectY.forEach((key, value) -> {
            yearItem.addAll(value);
        });
        year.put("year", yearItem.stream().filter(item
                -> !item.getBudgetName().contains("资金总额")).collect(Collectors.toList()));

        resutMap.putAll(quarterly);
        resutMap.putAll(month);
        resutMap.putAll(year);
        return resutMap;
    }


    /***
     * @Description: history为历史记录（修改前的数据），budgetDetailEO为审批修改后的新数据
     * @Param: [projectId, type] type()
     * @return: java.util.List<com.adc.da.research.funds.vo.change.FundChangeVO>
     * @Author: yanyujie
     * @Date: 2020/12/3
     */
    public List<DetailChangeVO> comparedBudgetDetail(String projectId, Integer type) throws Exception {

        List<DetailChangeVO> resultFunds = new ArrayList<>();
        BudgetDetailEOPage budgetDetailEOPage = new BudgetDetailEOPage();
        budgetDetailEOPage.setProjectId(projectId);
        final List<BudgetDetailEO> budgetDetailEOS
                = this.budgetDetailEOService.queryByList(budgetDetailEOPage);

        BudgetDetailHistoryEOPage budgetDetailHistoryEOPage = new BudgetDetailHistoryEOPage();
        budgetDetailHistoryEOPage.setProjectId(projectId);
        final List<BudgetDetailHistoryEO> budgetDetailHistoryEOS
                = this.budgetDetailHistoryEOService.queryByList(budgetDetailHistoryEOPage);

        //年度
        if (ComparedDetailType.YEAR.getValue() == type) {

            //情况一 国拨经费
            changeBudgetByYear(resultFunds, budgetDetailEOS, budgetDetailHistoryEOS, BudgetType.STATE_FUND.getValue());

            //情况二 自筹情况
            changeBudgetByYear(resultFunds, budgetDetailEOS, budgetDetailHistoryEOS, BudgetType.SELF_FUND.getValue());

        }
        //季度
        if (ComparedDetailType.QUARTER.getValue() == type) {

            //情况一 国拨经费
            changeBudgetQuarter(resultFunds, budgetDetailEOS, budgetDetailHistoryEOS, BudgetType.STATE_FUND.getValue());

            //情况二 自筹情况
            changeBudgetQuarter(resultFunds, budgetDetailEOS, budgetDetailHistoryEOS, BudgetType.SELF_FUND.getValue());
        }

        //月度
        if (ComparedDetailType.MONTH.getValue() == type) {
            //情况一 国拨经费
            changeBudgetMonte(resultFunds, budgetDetailEOS, budgetDetailHistoryEOS, BudgetType.STATE_FUND.getValue());
            //情况二 自筹情况
            changeBudgetMonte(resultFunds, budgetDetailEOS, budgetDetailHistoryEOS, BudgetType.SELF_FUND.getValue());
        }

        if (ComparedDetailType.ALL.getValue() == type) {
            changeBudgetByYear(resultFunds, budgetDetailEOS, budgetDetailHistoryEOS, BudgetType.STATE_FUND.getValue());
            changeBudgetByYear(resultFunds, budgetDetailEOS, budgetDetailHistoryEOS, BudgetType.SELF_FUND.getValue());
            changeBudgetQuarter(resultFunds, budgetDetailEOS, budgetDetailHistoryEOS, BudgetType.STATE_FUND.getValue());
            changeBudgetQuarter(resultFunds, budgetDetailEOS, budgetDetailHistoryEOS, BudgetType.SELF_FUND.getValue());
            changeBudgetMonte(resultFunds, budgetDetailEOS, budgetDetailHistoryEOS, BudgetType.STATE_FUND.getValue());
            changeBudgetMonte(resultFunds, budgetDetailEOS, budgetDetailHistoryEOS, BudgetType.SELF_FUND.getValue());

        }

        return resultFunds;
    }

    public List<ChangeProjectVO> compareWorkProgress(String projectId) throws Exception {
        List<ChangeProjectVO> result = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        WorkProgressHistoryEOPage workProgressHistoryEO = new WorkProgressHistoryEOPage();
        workProgressHistoryEO.setProjectId(projectId);
        final List<WorkProgressHistoryEO> workProgressHistoryEOS
                = this.workProgressHistoryEOService.queryByList(workProgressHistoryEO);

        WorkProgressEOPage workProgressEOPage = new WorkProgressEOPage();
        workProgressEOPage.setProjectId(projectId);
        final List<WorkProgressEO> workProgressEOS = this.workProgressEOService.queryByList(workProgressEOPage);
        workProgressHistoryEOS.forEach(item -> {

            for (WorkProgressEO workProgressEO : workProgressEOS) {
                if (workProgressEO.getId().equals(item.getId())) {
                    if (!StringUtils.equals(item.getExamineContent(), workProgressEO.getExamineContent())) {
                        ChangeProjectVO changeProjectVO = new ChangeProjectVO();
                        changeProjectVO.setChangeProperty("项目执行");
                        changeProjectVO.setChangeUser(workProgressEO.getCreateUserName() == null ? ""
                                : workProgressEO.getCreateUserName());
                        changeProjectVO.setChangeContext("检查内容变更为：" + workProgressEO.getExamineContent());
                        result.add(changeProjectVO);
                    }
                    if (!StringUtils.equals(item.getExamineType(), workProgressEO.getExamineType())) {
                        ChangeProjectVO changeProjectVO = new ChangeProjectVO();
                        changeProjectVO.setChangeProperty("项目执行");
                        changeProjectVO.setChangeUser(workProgressEO.getCreateUserName() == null ? ""
                                : workProgressEO.getCreateUserName());
                        changeProjectVO.setChangeContext("检查类型变更为：" + workProgressEO.getExamineType());
                        result.add(changeProjectVO);
                    }

                    if (!StringUtils.equals(item.getNodeGoals(), workProgressEO.getNodeGoals())) {
                        ChangeProjectVO changeProjectVO = new ChangeProjectVO();
                        changeProjectVO.setChangeProperty("项目执行");
                        changeProjectVO.setChangeUser(workProgressEO.getCreateUserName() == null ? ""
                                : workProgressEO.getCreateUserName());
                        changeProjectVO.setChangeContext("节点研究目标变更为：" + workProgressEO.getExamineType());
                        result.add(changeProjectVO);
                    }

                    if (!StringUtils.equals(format.format(item.getExamineTime() == null ? date : item.getExamineTime())
                            , format.format(workProgressEO.getExamineTime()
                                    == null ? date : workProgressEO.getExamineTime()))) {
                        ChangeProjectVO changeProjectVO = new ChangeProjectVO();
                        changeProjectVO.setChangeProperty("项目执行");
                        changeProjectVO.setChangeUser(workProgressEO.getCreateUserName() == null ? ""
                                : workProgressEO.getCreateUserName());
                        changeProjectVO.setChangeContext("检查时间变更为："
                                + format.format(workProgressEO.getExamineTime()));
                        result.add(changeProjectVO);
                    }
                }
            }
        });

        return result;
    }

    public List<ChangeProjectVO> compareCheckTarget(String projectId) throws Exception {
        List<ChangeProjectVO> result = new ArrayList<>();

        CheckTargetHistoryEOPage checkTargetHistoryEOPage = new CheckTargetHistoryEOPage();
        checkTargetHistoryEOPage.setProjectId(projectId);
        final List<CheckTargetHistoryEO> checkTargetHistoryEOS
                = this.checkTargetHistoryEOService.queryByList(checkTargetHistoryEOPage);

        CheckTargetEOPage checkTargetEOPage = new CheckTargetEOPage();
        checkTargetEOPage.setProjectId(projectId);
        final List<CheckTargetEO> checkTargetEOS
                = this.checkTargetEOService.queryByList(checkTargetEOPage);
        try {
            checkTargetHistoryEOS.forEach(item -> {
                for (CheckTargetEO checkTargetEO : checkTargetEOS) {
                    if (item.getId().equals(checkTargetEO.getId())) {
                        //发明专利....等等一些列指标
                        if (StringUtils.equals(item.getCheckName(), checkTargetEO.getCheckName())) {
                            if (!StringUtils.equals(String.valueOf(item.getCheckNum() == null ? 0d : item.getCheckNum())
                                    , String.valueOf(checkTargetEO.getCheckNum() == null ? 0d : checkTargetEO.getCheckNum()))) {
                                ChangeProjectVO changeProjectVO = new ChangeProjectVO();
                                changeProjectVO.setChangeProperty("考核指标");
                                changeProjectVO.setChangeUser(checkTargetEO.getCreateUserName() == null ? ""
                                        : checkTargetEO.getCreateUserName());
                                changeProjectVO.setChangeContext(checkTargetEO.getCheckName() + "数量变化为："
                                        + (checkTargetEO.getCheckNum() == null ? 0d : checkTargetEO.getCheckNum()));
                                result.add(changeProjectVO);
                            }
                        }
                    }

                }
            });
        } catch (Exception e) {
            logger.warn("发明专利....等等一些列指标对比失败");
        }
        //预期交付物及技术指标
        try {
            final List<CheckTargetHistoryEO> checkTargetHistoryList = checkTargetHistoryEOS.stream()
                    .filter(checkTargetHistory
                            -> Objects.nonNull(checkTargetHistory.getCheckTypeId())
                            && checkTargetHistory.getCheckTypeId().equals("SUE4G9GWQN")).collect(Collectors.toList());
            final List<CheckTargetEO> checkTargetList = checkTargetEOS.stream().filter(checkTarget
                    -> Objects.nonNull(checkTarget.getCheckTypeId())
                    && checkTarget.getCheckTypeId().equals("SUE4G9GWQN")).collect(Collectors.toList());
            //取差值 统一类型
            final List<DifferenceDeliverableVO> differenceDeliverableHisVOS
                    = beanMapper.mapList(checkTargetHistoryList, DifferenceDeliverableVO.class);

            final List<DifferenceDeliverableVO> differenceDeliverableVOS
                    = beanMapper.mapList(checkTargetList, DifferenceDeliverableVO.class);
            differenceDeliverableVOS.removeAll(differenceDeliverableHisVOS);

            differenceDeliverableVOS.forEach(checkTargetEO -> {
                ChangeProjectVO changeProjectVO = new ChangeProjectVO();
                changeProjectVO.setChangeProperty("考核指标");
                changeProjectVO.setChangeUser(checkTargetEO.getCreateUserName() == null ? ""
                        : checkTargetEO.getCreateUserName());
                changeProjectVO.setChangeContext("预期交付物及技术指标：" + checkTargetEO.getDeliverableName()
                        == null ? "" : checkTargetEO.getDeliverableName() + ":"
                        + (checkTargetEO.getDeliverableTarget() == null ? "" : checkTargetEO.getDeliverableTarget()));
                result.add(changeProjectVO);
            });
        } catch (Exception e) {
            logger.warn("预期交付物及技术指标对比失败");
        }
        //目标刊物
        try {
            final List<CheckTargetHistoryEO> checkTargetHistoryList
                    = checkTargetHistoryEOS.stream().filter(checkTargetHistory
                    -> Objects.nonNull(checkTargetHistory.getCheckTypeId())
                    && checkTargetHistory.getCheckTypeId().equals("3V57HSDVX5")).collect(Collectors.toList());
            final List<CheckTargetEO> checkTargetList = checkTargetEOS.stream().filter(checkTarget
                    -> Objects.nonNull(checkTarget.getCheckTypeId())
                    && checkTarget.getCheckTypeId().equals("3V57HSDVX5")).collect(Collectors.toList());
            //取差值 统一类型
            final List<DifferencePeriodicalVO> differencePeriodicalVOS
                    = beanMapper.mapList(checkTargetList, DifferencePeriodicalVO.class);
            final List<DifferencePeriodicalVO> differencePeriodicalHisVOS
                    = beanMapper.mapList(checkTargetHistoryList, DifferencePeriodicalVO.class);
            differencePeriodicalVOS.removeAll(differencePeriodicalHisVOS);

            differencePeriodicalVOS.forEach(checkTargetEO -> {
                ChangeProjectVO changeProjectVO = new ChangeProjectVO();
                changeProjectVO.setChangeProperty("考核指标");
                changeProjectVO.setChangeUser(checkTargetEO.getCreateUserName() == null ? ""
                        : checkTargetEO.getCreateUserName());
                changeProjectVO.setChangeContext("目标刊物：" + checkTargetEO.getExt1()
                        == null ? "" : checkTargetEO.getExt1() + ":"
                        + (checkTargetEO.getExt2() == null ? "" : checkTargetEO.getExt2()));
                result.add(changeProjectVO);
            });
        } catch (Exception e) {
            logger.warn("目标刊物对比失败");
        }

        return result;
    }


    public List<ChangeProjectVO> compareMemberInfo(String projectId) throws Exception {

        List<ChangeProjectVO> result = new ArrayList<>();

        MemberInfoHistoryEOPage memberInfoHistoryEOPage = new MemberInfoHistoryEOPage();
        memberInfoHistoryEOPage.setProjectId(projectId);
        final List<MemberInfoHistoryEO> memberInfoHistoryEOS
                = this.memberInfoHistoryEOService.queryByList(memberInfoHistoryEOPage);

        MemberInfoEOPage memberInfoEOPage = new MemberInfoEOPage();
        memberInfoEOPage.setProjectId(projectId);
        final List<MemberInfoEO> memberInfoEOS = this.memberInfoEOService.queryByList(memberInfoEOPage);


        //判断人员增加与删减
        final List<String> memberHisIds
                = memberInfoHistoryEOS.stream().map(MemberInfoHistoryEO::getMemberUserId).collect(Collectors.toList());
        final List<String> memberIds
                = memberInfoEOS.stream().map(MemberInfoEO::getMemberUserId).collect(Collectors.toList());
        List<String> newlyMemberHisIds = new ArrayList<>(memberHisIds);
        List<String> newlyMemberIds = new ArrayList<>(memberIds);

        //新增人员
        newlyMemberIds.removeAll(newlyMemberHisIds);
        newlyMemberIds.forEach(item -> {
            ChangeProjectVO changeProjectVO = new ChangeProjectVO();
            changeProjectVO.setChangeProperty("项目成员");
            try {
                changeProjectVO.setChangeContext("增加成员：" + userEOService.selectByPrimaryKey(item).getUsname());
            } catch (Exception e) {
                logger.warn("成员名称解析失败");
            }
            result.add(changeProjectVO);
        });

        //减少人员
        memberHisIds.removeAll(memberIds);
        memberHisIds.forEach(item -> {
            ChangeProjectVO changeProjectVO = new ChangeProjectVO();
            changeProjectVO.setChangeProperty("项目成员");
            try {
                changeProjectVO.setChangeContext("减少人员：" + userEOService.selectByPrimaryKey(item).getUsname());
            } catch (Exception e) {
                logger.warn("成员名称解析失败");
            }
            result.add(changeProjectVO);
        });

        //整理字段对比

        memberInfoHistoryEOS.forEach(item -> {
            for (MemberInfoEO memberInfoEO : memberInfoEOS) {
                if (item.getId().equals(memberInfoEO.getId())) {
                    if (!StringUtils.equals(String.valueOf(memberInfoEO.getWorkHours()
                                    == null ? 0d : memberInfoEO.getWorkHours())
                            , String.valueOf(item.getWorkHours() == null ? 0d : item.getWorkHours()))) {
                        ChangeProjectVO changeProjectVO = new ChangeProjectVO();
                        changeProjectVO.setChangeProperty("项目成员");
                        try {
                            changeProjectVO.setChangeContext(userEOService.selectByPrimaryKey(item.getMemberUserId())
                                    .getUsname() + "研究时间变更为："
                                    + memberInfoEO.getWorkHours() == null ? "" : String.valueOf(memberInfoEO.getWorkHours()));
                        } catch (Exception e) {
                            logger.warn("成员名称解析失败");
                        }
                        result.add(changeProjectVO);
                    }
                }

                if (!StringUtils.equals(item.getTaskDivision(), memberInfoEO.getTaskDivision())) {
                    ChangeProjectVO changeProjectVO = new ChangeProjectVO();
                    changeProjectVO.setChangeProperty("项目成员");
                    try {
                        changeProjectVO.setChangeContext(userEOService.selectByPrimaryKey(item.getMemberUserId())
                                .getUsname() + "项目组分工变更为："
                                + memberInfoEO.getTaskDivision() == null ? "" : memberInfoEO.getTaskDivision());
                    } catch (Exception e) {
                        logger.warn("成员名称解析失败");
                    }
                    result.add(changeProjectVO);
                }
            }
        });

        return result;
    }


    /***
     * @Description: 月度为维度查找Budget替换内容
     * @Param: [resultFunds, budgetDetailEOS, budgetDetailHistoryEOS, budgetType]
     * @return: void
     * @Author: yanyujie
     * @Date: 2020/12/4
     */
    private void changeBudgetMonte(List<DetailChangeVO> resultFunds, List<BudgetDetailEO> budgetDetailEOS
            , List<BudgetDetailHistoryEO> budgetDetailHistoryEOS, String budgetType) {
        final Map<String, List<BudgetDetailEO>> detailStateMonte
                = budgetDetailEOS.stream().filter(item -> Objects.nonNull(item.getBudgetMonth())
                && item.getBudgetType().contains(budgetType))
                .collect(Collectors.groupingBy(BudgetDetailEO::getBudgetMonth));

        final Map<String, List<BudgetDetailHistoryEO>> detailHistoryStateMonte
                = budgetDetailHistoryEOS.stream().filter(item -> Objects.nonNull(item.getBudgetMonth())
                && item.getBudgetType().contains(budgetType))
                .collect(Collectors.groupingBy(BudgetDetailHistoryEO::getBudgetMonth));

        detailHistoryStateMonte.forEach((key, value) -> {
            if (detailStateMonte.containsKey(key)) {
                final List<BudgetDetailEO> budgetDetail = detailStateMonte.get(key);
                for (BudgetDetailHistoryEO budgetDetailHistoryEO : value) {
                    for (BudgetDetailEO budgetDetailEO : budgetDetail) {
                        if (budgetDetailHistoryEO.getBudgetDetailTypeName()
                                .equals(budgetDetailEO.getBudgetDetailTypeName())) {
                            if (Objects.isNull(budgetDetailHistoryEO.getBudgetAmount())) {
                                budgetDetailHistoryEO.setBudgetAmount(0d);
                            }
                            if (Objects.isNull(budgetDetailEO.getBudgetAmount())) {
                                budgetDetailEO.setBudgetAmount(0d);
                            }
                            if (!budgetDetailHistoryEO.getBudgetAmount()
                                    .equals(budgetDetailEO.getBudgetAmount())) {
                                DetailChangeVO detailChangeVO = new DetailChangeVO();
                                detailChangeVO.setMonth(key);
                                detailChangeVO.setBudgetType(budgetType);
                                detailChangeVO.setChangeTime(budgetDetailHistoryEO.getCreateTime());//调整日期
                                detailChangeVO.setBudgetName(budgetDetailHistoryEO.getBudgetDetailTypeName());
                                detailChangeVO.setOriginalAmount(String.valueOf
                                        (budgetDetailHistoryEO.getBudgetAmount()));
                                detailChangeVO.setExistingAmount(String.valueOf
                                        (budgetDetailEO.getBudgetAmount()));
                                detailChangeVO.setDifferenceAmount(String.valueOf(
                                        budgetDetailHistoryEO.getBudgetAmount()
                                                - budgetDetailEO.getBudgetAmount()
                                ));
                                resultFunds.add(detailChangeVO);
                            }
                        }
                    }
                }
            }
        });
    }

    /***
     * @Description: 季度为维度查找Budget替换内容
     * @Param: [resultFunds, budgetDetailEOS, budgetDetailHistoryEOS, budgetType]
     * @return: void
     * @Author: yanyujie
     * @Date: 2020/12/4
     */
    private void changeBudgetQuarter(List<DetailChangeVO> resultFunds
            , List<BudgetDetailEO> budgetDetailEOS
            , List<BudgetDetailHistoryEO> budgetDetailHistoryEOS
            , String budgetType) {
        final Map<String, List<BudgetDetailEO>> detailStateQuarter
                = budgetDetailEOS.stream().filter(item -> Objects.nonNull(item.getBudgetQuarterly())
                && item.getBudgetType().contains(budgetType))
                .collect(Collectors.groupingBy(BudgetDetailEO::getBudgetQuarterly));

        final Map<String, List<BudgetDetailHistoryEO>> detailHistoryStateQuarter
                = budgetDetailHistoryEOS.stream().filter(item -> Objects.nonNull(item.getBudgetQuarterly())
                && item.getBudgetType().contains(budgetType))
                .collect(Collectors.groupingBy(BudgetDetailHistoryEO::getBudgetQuarterly));

        detailHistoryStateQuarter.forEach((key, value) -> {
            if (detailStateQuarter.containsKey(key)) {
                final List<BudgetDetailEO> budgetDetail = detailStateQuarter.get(key);
                for (BudgetDetailHistoryEO budgetDetailHistoryEO : value) {
                    for (BudgetDetailEO budgetDetailEO : budgetDetail) {
                        if (budgetDetailHistoryEO.getBudgetDetailTypeName()
                                .equals(budgetDetailEO.getBudgetDetailTypeName())) {
                            if (Objects.isNull(budgetDetailHistoryEO.getBudgetAmount())) {
                                budgetDetailHistoryEO.setBudgetAmount(0d);
                            }
                            if (Objects.isNull(budgetDetailEO.getBudgetAmount())) {
                                budgetDetailEO.setBudgetAmount(0d);
                            }
                            if (!budgetDetailHistoryEO.getBudgetAmount()
                                    .equals(budgetDetailEO.getBudgetAmount())) {
                                DetailChangeVO detailChangeVO = new DetailChangeVO();
                                detailChangeVO.setQuarter(key);
                                detailChangeVO.setBudgetType(budgetType);
                                detailChangeVO.setChangeTime(budgetDetailHistoryEO.getCreateTime());//调整日期
                                detailChangeVO.setBudgetName(budgetDetailHistoryEO.getBudgetDetailTypeName());
                                detailChangeVO.setOriginalAmount(String.valueOf
                                        (budgetDetailHistoryEO.getBudgetAmount()));
                                detailChangeVO.setExistingAmount(String.valueOf
                                        (budgetDetailEO.getBudgetAmount()));
                                detailChangeVO.setDifferenceAmount(String.valueOf(
                                        budgetDetailHistoryEO.getBudgetAmount()
                                                - budgetDetailEO.getBudgetAmount()
                                ));
                                resultFunds.add(detailChangeVO);
                            }
                        }
                    }
                }
            }
        });
    }


    public Map<String, List<ChangeProjectVO>> allChange(String id) throws Exception {

        List<ChangeProjectVO> result = new ArrayList<>();
        final List<ChangeProjectVO> memberInfo = this.compareMemberInfo(id);
        final List<ChangeProjectVO> checkTarget = this.compareCheckTarget(id);
        final List<ChangeProjectVO> workProgress = this.compareWorkProgress(id);
        result.addAll(workProgress);
        result.addAll(checkTarget);
        result.addAll(memberInfo);

        final List<FundChangeVO> fundChangeVOS = this.comparedBudgetFund(id);

        for (FundChangeVO fundChangeVO : fundChangeVOS) {
            ChangeProjectVO changeProjectVO = new ChangeProjectVO();
            changeProjectVO.setChangeProperty("经费预算");

            BudgetFundVO budgetFundVO = new BudgetFundVO();
            budgetFundVO.setType("FUND");
            budgetFundVO.setYear(fundChangeVO.getYear());
            budgetFundVO.setAfterNum(fundChangeVO.getOriginalAmount());
            budgetFundVO.setBeforeNum(fundChangeVO.getExistingAmount());
            budgetFundVO.setNum(String.valueOf(Double.parseDouble(budgetFundVO.getAfterNum())
                    - Double.parseDouble(budgetFundVO.getBeforeNum())));
            budgetFundVO.setBudgetType(fundChangeVO.getBudgetType());

            final String jsonInfo = JSON.toJSONString(budgetFundVO);
            if (StringUtils.isNotBlank(jsonInfo)) {
                changeProjectVO.setChangeContext(jsonInfo);
            }
            result.add(changeProjectVO);
        }
        final List<DetailChangeVO> detailChangeVOS = this.comparedBudgetDetail(id, ComparedDetailType.ALL.getValue());
        detailChangeVOS.forEach(item -> {
            item.setType("DETAIL");
            ChangeProjectVO changeProjectVO = new ChangeProjectVO();
            changeProjectVO.setChangeProperty("经费预算");
            final String jsonInfo = JSON.toJSONString(item);
            if (StringUtils.isNotBlank(jsonInfo)){
                changeProjectVO.setChangeContext(jsonInfo);
            }
            result.add(changeProjectVO);
        });
        final Map<String, List<ChangeProjectVO>> collect
                = result.stream().collect(Collectors.groupingBy(ChangeProjectVO::getChangeProperty));

        return collect;


    }

    /***
     * @Description: 年度为维度查找Budget替换内容
     * @Param: [resultFunds, budgetDetailEOS, budgetDetailHistoryEOS, budgetType]
     * budgetType填写（国拨或者自筹）
     * @return: void
     * @Author: yanyujie
     * @Date: 2020/12/3
     */
    private void changeBudgetByYear(List<DetailChangeVO> resultFunds
            , List<BudgetDetailEO> budgetDetailEOS
            , List<BudgetDetailHistoryEO> budgetDetailHistoryEOS
            , String budgetType) {
        final Map<String, List<BudgetDetailEO>> detailStateYear
                = budgetDetailEOS.stream().filter(item -> Objects.nonNull(item.getBudgetYear())
                && item.getBudgetType().contains(budgetType))
                .collect(Collectors.groupingBy(BudgetDetailEO::getBudgetYear));

        final Map<String, List<BudgetDetailHistoryEO>> detailHistoryStateYear
                = budgetDetailHistoryEOS.stream().filter(item -> Objects.nonNull(item.getBudgetYear())
                && item.getBudgetType().contains(budgetType))
                .collect(Collectors.groupingBy(BudgetDetailHistoryEO::getBudgetYear));

        detailHistoryStateYear.forEach((key, value) -> {
            if (detailStateYear.containsKey(key)) {
                final List<BudgetDetailEO> budgetDetail = detailStateYear.get(key);
                for (BudgetDetailHistoryEO budgetDetailHistoryEO : value) {
                    for (BudgetDetailEO budgetDetailEO : budgetDetail) {
                        if (budgetDetailHistoryEO.getBudgetDetailTypeName()
                                .equals(budgetDetailEO.getBudgetDetailTypeName())) {
                            if (Objects.isNull(budgetDetailHistoryEO.getBudgetAmount())) {
                                budgetDetailHistoryEO.setBudgetAmount(0d);
                            }
                            if (Objects.isNull(budgetDetailEO.getBudgetAmount())) {
                                budgetDetailEO.setBudgetAmount(0d);
                            }
                            if (!budgetDetailHistoryEO.getBudgetAmount().equals(budgetDetailEO.getBudgetAmount())) {
                                DetailChangeVO detailChangeVO = new DetailChangeVO();
                                detailChangeVO.setYear(key);
                                detailChangeVO.setChangeTime(budgetDetailHistoryEO.getCreateTime());//调整日期
                                detailChangeVO.setBudgetType(budgetType);
                                detailChangeVO.setBudgetName(budgetDetailHistoryEO.getBudgetDetailTypeName());
                                detailChangeVO.setOriginalAmount(String.valueOf
                                        (budgetDetailHistoryEO.getBudgetAmount()));
                                detailChangeVO.setExistingAmount(String.valueOf
                                        (budgetDetailEO.getBudgetAmount()));
                                detailChangeVO.setDifferenceAmount(String.valueOf(
                                        budgetDetailHistoryEO.getBudgetAmount()
                                                - budgetDetailEO.getBudgetAmount()
                                ));
                                resultFunds.add(detailChangeVO);
                            }
                        }
                    }
                }
            }
        });
    }

    /**
     * 新增变更表数据  复制原始表
     *
     * @param projectData
     * @throws Exception
     */
    public void saveChange(ProjectDataEO projectData) throws Exception {
        //修改项目状态
        projectDataEOService.updateByPrimaryKeySelective(projectData);
        String projectId = projectData.getId();

        //项目成员表
        MemberInfoHistoryEOPage memberInfoHistoryEOPage = new MemberInfoHistoryEOPage();
        memberInfoHistoryEOPage.setProjectId(projectId);
        List<MemberInfoHistoryEO> memberInfoHistoryEOS
                = this.memberInfoHistoryEOService.queryByList(memberInfoHistoryEOPage);

        MemberInfoEOPage memberInfoEOPage = new MemberInfoEOPage();
        memberInfoEOPage.setProjectId(projectId);
        List<MemberInfoEO> memberInfoEOS = this.memberInfoEOService.queryByList(memberInfoEOPage);
        if (CollectionUtils.isNotEmpty(memberInfoHistoryEOS)) {
            //先删除
            memberInfoHistoryEOService.deleteByProjectId(memberInfoHistoryEOS.get(0).getProjectId());
        }
        if (CollectionUtils.isNotEmpty(memberInfoEOS)) {
            //添加
            List<MemberInfoHistoryEO> newMemberInfoHistoryEOS = beanMapper.mapList(memberInfoEOS, MemberInfoHistoryEO.class);
            memberInfoHistoryEODao.batchInsertSelective(newMemberInfoHistoryEOS);
        }
        //考核指标表
        CheckTargetHistoryEOPage checkTargetHistoryEOPage = new CheckTargetHistoryEOPage();
        checkTargetHistoryEOPage.setProjectId(projectId);
        List<CheckTargetHistoryEO> checkTargetHistoryEOS
                = this.checkTargetHistoryEOService.queryByList(checkTargetHistoryEOPage);

        CheckTargetEOPage checkTargetEOPage = new CheckTargetEOPage();
        checkTargetEOPage.setProjectId(projectId);
        List<CheckTargetEO> checkTargetEOS
                = this.checkTargetEOService.queryByList(checkTargetEOPage);
        if (CollectionUtils.isNotEmpty(checkTargetHistoryEOS)) {
            String checkTypeId = "";
            //先删除
            checkTargetHistoryEODao.deleteAllByProjectId(checkTargetHistoryEOS.get(0).getProjectId());
        }
        if (CollectionUtils.isNotEmpty(checkTargetEOS)) {
            //分别添加
            List<CheckTargetHistoryEO> newCheckTargetHistoryEOS = beanMapper.mapList(checkTargetEOS, CheckTargetHistoryEO.class);
            checkTargetHistoryEODao.batchInsertSelective(newCheckTargetHistoryEOS);
        }
        //工作进度安排
        WorkProgressHistoryEOPage workProgressHistoryEO = new WorkProgressHistoryEOPage();
        workProgressHistoryEO.setProjectId(projectId);
        List<WorkProgressHistoryEO> workProgressHistoryEOS
                = this.workProgressHistoryEOService.queryByList(workProgressHistoryEO);

        WorkProgressEOPage workProgressEOPage = new WorkProgressEOPage();
        workProgressEOPage.setProjectId(projectId);
        List<WorkProgressEO> workProgressEOS = this.workProgressEOService.queryByList(workProgressEOPage);
        if (CollectionUtils.isNotEmpty(workProgressHistoryEOS)) {
            //先删除
            workProgressHistoryEOService.deleteByProjectId(workProgressHistoryEOS.get(0).getProjectId());
        }
        if (CollectionUtils.isNotEmpty(workProgressEOS)) {

            //分别添加
            List<WorkProgressHistoryEO> newWorkProgressHistoryEOS = beanMapper.mapList(workProgressEOS, WorkProgressHistoryEO.class);
            workProgressHistoryEODao.batchInsertSelective(newWorkProgressHistoryEOS);
        }


        //预算表
        BudgetFundEOPage budgetFundEOPage = new BudgetFundEOPage();
        budgetFundEOPage.setProjectId(projectId);
        List<BudgetFundEO> budgetFundEOS
                = this.budgetFundEOService.queryByList(budgetFundEOPage);

        BudgetFundHistoryEOPage budgetFundHistoryEOPage = new BudgetFundHistoryEOPage();
        budgetFundHistoryEOPage.setProjectId(projectId);
        List<BudgetFundHistoryEO> budgetFundHistoryEOS
                = this.budgetFundHistoryEOService.queryByList(budgetFundHistoryEOPage);
        if (CollectionUtils.isNotEmpty(budgetFundHistoryEOS)) {
            //先删除
            budgetFundHistoryEOService.deleteByProjectId(budgetFundHistoryEOS.get(0).getProjectId());
        }
        if (CollectionUtils.isNotEmpty(budgetFundEOS)) {

            //分别添加
            List<BudgetFundHistoryEO> newBudgetFundHistoryEOS = beanMapper.mapList(budgetFundEOS, BudgetFundHistoryEO.class);
            budgetFundHistoryEODao.batchInsertSelective(newBudgetFundHistoryEOS);
        }

        //预算明细表
        BudgetDetailEOPage budgetDetailEOPage = new BudgetDetailEOPage();
        budgetDetailEOPage.setProjectId(projectId);
        List<BudgetDetailEO> budgetDetailEOS
                = this.budgetDetailEOService.queryByList(budgetDetailEOPage);

        BudgetDetailHistoryEOPage budgetDetailHistoryEOPage = new BudgetDetailHistoryEOPage();
        budgetDetailHistoryEOPage.setProjectId(projectId);
        List<BudgetDetailHistoryEO> budgetDetailHistoryEOS
                = this.budgetDetailHistoryEOService.queryByList(budgetDetailHistoryEOPage);
        if (CollectionUtils.isNotEmpty(budgetDetailHistoryEOS)) {
            //先删除
            budgetDetailHistoryEOService.deleteByProjectId(budgetDetailHistoryEOS.get(0).getProjectId());
        }
        if (CollectionUtils.isNotEmpty(budgetDetailEOS)) {

            //分别添加
            List<BudgetDetailHistoryEO> newBudgetDetailHistoryEOS = beanMapper.mapList(budgetDetailEOS, BudgetDetailHistoryEO.class);
            budgetDetailHistoryEODao.batchInsertSelective(newBudgetDetailHistoryEOS);
        }

        //经费测算依据表
        ResearchDetailHistoryEOPage researchDetailHistoryEOPage = new ResearchDetailHistoryEOPage();
        researchDetailHistoryEOPage.setResearchProjectId(projectId);
        List<ResearchDetailHistoryEO> researchDetailHistoryEOList = researchDetailHistoryEOService.queryByList(researchDetailHistoryEOPage);

        ResearchBudgetDetailEOPage researchBudgetDetailEOPage = new ResearchBudgetDetailEOPage();
        researchBudgetDetailEOPage.setResearchProjectId(projectId);
        List<ResearchBudgetDetailEO> researchBudgetDetailEOList = researchBudgetDetailEOService.queryByList(researchBudgetDetailEOPage);
        if (CollectionUtils.isNotEmpty(researchDetailHistoryEOList)) {
            //先删除
            researchDetailHistoryEODao.deleteAllByProjectId(researchDetailHistoryEOList.get(0).getResearchProjectId());
        }
        if (CollectionUtils.isNotEmpty(researchBudgetDetailEOList)) {

            //分别添加
            List<ResearchDetailHistoryEO> newResearchDetailHistoryEOS = beanMapper.mapList(researchBudgetDetailEOList, ResearchDetailHistoryEO.class);
            researchDetailHistoryEODao.batchInsertSelective(newResearchDetailHistoryEOS);
        }
    }

    /**
     * 变更表交换数据
     *
     * @param projectData
     * @throws Exception
     */
    public void changePass(ProjectDataEO projectData) throws Exception {
        String projectId = projectData.getId();

        //项目成员表
        MemberInfoHistoryEOPage memberInfoHistoryEOPage = new MemberInfoHistoryEOPage();
        memberInfoHistoryEOPage.setProjectId(projectId);
        List<MemberInfoHistoryEO> memberInfoHistoryEOS
                = this.memberInfoHistoryEOService.queryByList(memberInfoHistoryEOPage);

        MemberInfoEOPage memberInfoEOPage = new MemberInfoEOPage();
        memberInfoEOPage.setProjectId(projectId);
        List<MemberInfoEO> memberInfoEOS = this.memberInfoEOService.queryByList(memberInfoEOPage);

        if (CollectionUtils.isNotEmpty(memberInfoHistoryEOS) && CollectionUtils.isNotEmpty(memberInfoEOS)) {
            //先删除
            memberInfoHistoryEOService.deleteByProjectId(memberInfoHistoryEOS.get(0).getProjectId());
            memberInfoEOService.deleteByProjectId(memberInfoEOS.get(0).getProjectId());

            //分别添加
            List<MemberInfoEO> newMemberInfoEOS = beanMapper.mapList(memberInfoHistoryEOS, MemberInfoEO.class);
            //  List<MemberInfoEO> newMemberInfoEOS = beanMapper.mapList(memberInfoHistoryChangeVOS, MemberInfoEO.class);
            memberInfoEODao.batchInsertSelective(newMemberInfoEOS);

            List<MemberInfoHistoryEO> newMemberInfoHistoryEOS = beanMapper.mapList(memberInfoEOS, MemberInfoHistoryEO.class);
            //  List<MemberInfoHistoryEO> newMemberInfoHistoryEOS = beanMapper.mapList(memberInfoChangeVOS, MemberInfoHistoryEO.class);
            memberInfoHistoryEODao.batchInsertSelective(newMemberInfoHistoryEOS);
        }


        //考核指标表
        CheckTargetHistoryEOPage checkTargetHistoryEOPage = new CheckTargetHistoryEOPage();
        checkTargetHistoryEOPage.setProjectId(projectId);
        List<CheckTargetHistoryEO> checkTargetHistoryEOS
                = this.checkTargetHistoryEOService.queryByList(checkTargetHistoryEOPage);

        CheckTargetEOPage checkTargetEOPage = new CheckTargetEOPage();
        checkTargetEOPage.setProjectId(projectId);
        List<CheckTargetEO> checkTargetEOS
                = this.checkTargetEOService.queryByList(checkTargetEOPage);

        if (CollectionUtils.isNotEmpty(checkTargetHistoryEOS) && CollectionUtils.isNotEmpty(checkTargetEOS)) {
            String checkTypeId = "";
            //先删除
            checkTargetHistoryEOService.deleteByProjectId(checkTargetHistoryEOS.get(0).getProjectId(), checkTypeId);
            checkTargetEOService.deleteByProjectId(checkTargetEOS.get(0).getProjectId(), checkTypeId);

            //分别添加
            List<CheckTargetEO> newCheckTargetEOS = beanMapper.mapList(checkTargetHistoryEOS, CheckTargetEO.class);
            //   List<CheckTargetEO> newCheckTargetEOS = beanMapper.mapList(checkTargetHistoryChangeVOS, CheckTargetEO.class);
            checkTargetEODao.batchInsertSelective(newCheckTargetEOS);

            List<CheckTargetHistoryEO> newCheckTargetHistoryEOS = beanMapper.mapList(checkTargetEOS, CheckTargetHistoryEO.class);
            //  List<CheckTargetHistoryEO> newCheckTargetHistoryEOS = beanMapper.mapList(checkTargetVOS, CheckTargetHistoryEO.class);
            checkTargetHistoryEODao.batchInsertSelective(newCheckTargetHistoryEOS);
        }
        //工作进度安排
        WorkProgressHistoryEOPage workProgressHistoryEO = new WorkProgressHistoryEOPage();
        workProgressHistoryEO.setProjectId(projectId);
        List<WorkProgressHistoryEO> workProgressHistoryEOS
                = this.workProgressHistoryEOService.queryByList(workProgressHistoryEO);

        WorkProgressEOPage workProgressEOPage = new WorkProgressEOPage();
        workProgressEOPage.setProjectId(projectId);
        List<WorkProgressEO> workProgressEOS = this.workProgressEOService.queryByList(workProgressEOPage);
        if (CollectionUtils.isNotEmpty(workProgressHistoryEOS) && CollectionUtils.isNotEmpty(workProgressEOS)) {
            //先删除
            workProgressHistoryEOService.deleteByProjectId(workProgressHistoryEOS.get(0).getProjectId());
            workProgressEOService.deleteByProjectId(workProgressEOS.get(0).getProjectId());

            //分别添加
            List<WorkProgressEO> newWorkProgressEOS = beanMapper.mapList(workProgressHistoryEOS, WorkProgressEO.class);
            //   List<WorkProgressEO> newWorkProgressEOS = beanMapper.mapList(workProgressHistoryChangeVOS, WorkProgressEO.class);
            workProgressEODao.batchInsertSelective(newWorkProgressEOS);

            List<WorkProgressHistoryEO> newWorkProgressHistoryEOS = beanMapper.mapList(workProgressEOS, WorkProgressHistoryEO.class);
            //   List<WorkProgressHistoryEO> newWorkProgressHistoryEOS = beanMapper.mapList(workProgressVOS, WorkProgressHistoryEO.class);
            workProgressHistoryEODao.batchInsertSelective(newWorkProgressHistoryEOS);
        }


        //预算表
        BudgetFundEOPage budgetFundEOPage = new BudgetFundEOPage();
        budgetFundEOPage.setProjectId(projectId);
        List<BudgetFundEO> budgetFundEOS
                = this.budgetFundEOService.queryByList(budgetFundEOPage);

        BudgetFundHistoryEOPage budgetFundHistoryEOPage = new BudgetFundHistoryEOPage();
        budgetFundHistoryEOPage.setProjectId(projectId);
        List<BudgetFundHistoryEO> budgetFundHistoryEOS
                = this.budgetFundHistoryEOService.queryByList(budgetFundHistoryEOPage);
        if (CollectionUtils.isNotEmpty(budgetFundHistoryEOS) && CollectionUtils.isNotEmpty(budgetFundEOS)) {
            //先删除
            budgetFundHistoryEOService.deleteByProjectId(budgetFundHistoryEOS.get(0).getProjectId());
            budgetFundEOService.deleteByProjectId(budgetFundEOS.get(0).getProjectId());

            //分别添加
            List<BudgetFundEO> newBudgetFundEOS = beanMapper.mapList(budgetFundHistoryEOS, BudgetFundEO.class);
            // List<BudgetFundEO> newBudgetFundEOS = beanMapper.mapList(budgetFundHistoryChangeVOS, BudgetFundEO.class);
            budgetFundEODao.batchInsertSelective(newBudgetFundEOS);

            List<BudgetFundHistoryEO> newBudgetFundHistoryEOS = beanMapper.mapList(budgetFundEOS, BudgetFundHistoryEO.class);
            // List<BudgetFundHistoryEO> newBudgetFundHistoryEOS = beanMapper.mapList(budgetFundVOS, BudgetFundHistoryEO.class);
            budgetFundHistoryEODao.batchInsertSelective(newBudgetFundHistoryEOS);
        }

        //预算明细表
        BudgetDetailEOPage budgetDetailEOPage = new BudgetDetailEOPage();
        budgetDetailEOPage.setProjectId(projectId);
        List<BudgetDetailEO> budgetDetailEOS
                = this.budgetDetailEOService.queryByList(budgetDetailEOPage);

        BudgetDetailHistoryEOPage budgetDetailHistoryEOPage = new BudgetDetailHistoryEOPage();
        budgetDetailHistoryEOPage.setProjectId(projectId);
        List<BudgetDetailHistoryEO> budgetDetailHistoryEOS
                = this.budgetDetailHistoryEOService.queryByList(budgetDetailHistoryEOPage);
        if (CollectionUtils.isNotEmpty(budgetDetailHistoryEOS) && CollectionUtils.isNotEmpty(budgetDetailEOS)) {
            //先删除
            budgetDetailHistoryEOService.deleteByProjectId(budgetDetailHistoryEOS.get(0).getProjectId());
            budgetDetailEOService.deleteByProjectId(budgetDetailEOS.get(0).getProjectId());

            //分别添加
            List<BudgetDetailEO> newBudgetDetailEOS = beanMapper.mapList(budgetDetailHistoryEOS, BudgetDetailEO.class);
            //  List<BudgetDetailEO> newBudgetDetailEOS = beanMapper.mapList(budgetDetailHistoryChangeVOS, BudgetDetailEO.class);
            budgetDetailEODao.batchInsertSelective(newBudgetDetailEOS);

            List<BudgetDetailHistoryEO> newBudgetDetailHistoryEOS = beanMapper.mapList(budgetDetailEOS, BudgetDetailHistoryEO.class);
            //    List<BudgetDetailHistoryEO> newBudgetDetailHistoryEOS = beanMapper.mapList(budgetDetailVOS, BudgetDetailHistoryEO.class);
            budgetDetailHistoryEODao.batchInsertSelective(newBudgetDetailHistoryEOS);
        }

    }


    public List<BudgetFundVO> fundChangeFund(String id) throws Exception {
        List<BudgetFundVO> tempList = new ArrayList<>();
        ResearchProjectChangeEOPage changeEOPage = new ResearchProjectChangeEOPage();
        changeEOPage.setProjectId(id);
        changeEOPage.setChangeAttribute("经费预算");
        changeEOPage.setChangeStatus("审核通过");

        final List<ResearchProjectChangeEO> changeEOS = this.researchProjectChangeEOService.queryByList(changeEOPage);
        for (ResearchProjectChangeEO changeEO : changeEOS) {
            BudgetFundVO budgetFundVO = new BudgetFundVO();
            try {
                budgetFundVO = JSON.parseObject(changeEO.getChangeContent(), BudgetFundVO.class);
            } catch (Exception e) {
                logger.warn("不是BudgetFundVO类数据，不能解析");
            }
/*            if (!StringUtils.equals(budgetFundVO.getType(),"DETAIL")){
                break;
            }*/
            budgetFundVO.setGroup(budgetFundVO.getYear() + "_" + budgetFundVO.getBudgetType());
            tempList.add(budgetFundVO);
        }
        final Map<String, List<BudgetFundVO>> collect
                = tempList.stream().collect(Collectors.groupingBy(BudgetFundVO::getGroup));

        List<BudgetFundVO> resultList = new ArrayList<>();
        collect.forEach((key, value) -> {
            if (value.size() > 1) {
                BudgetFundVO budgetFundVO = new BudgetFundVO();
                final OptionalDouble max
                        = value.stream().mapToDouble(item -> Double.parseDouble(item.getAfterNum())).max();
                final OptionalDouble min
                        = value.stream().mapToDouble(item -> Double.parseDouble(item.getBeforeNum())).min();
                budgetFundVO.setAfterNum(String.valueOf(max.getAsDouble()));
                budgetFundVO.setBeforeNum(String.valueOf(min.getAsDouble()));
                budgetFundVO.setNum(String.valueOf((max.getAsDouble() - min.getAsDouble())));
                budgetFundVO.setBudgetType(value.get(0).getBudgetType());
                budgetFundVO.setYear(value.get(0).getYear());
                resultList.add(budgetFundVO);
            } else {
                resultList.addAll(value);
            }
        });
        return resultList;
    }

    public List<BudgetFundVO> fundChangeFundDetails(String id,String year) throws Exception {
        List<BudgetFundVO> resultList = new ArrayList<>();
        ResearchProjectChangeEOPage changeEOPage = new ResearchProjectChangeEOPage();
        changeEOPage.setProjectId(id);
        changeEOPage.setChangeAttribute("经费预算");
        changeEOPage.setChangeStatus("审核通过");

        final List<ResearchProjectChangeEO> changeEOS = this.researchProjectChangeEOService.queryByList(changeEOPage);
        for (ResearchProjectChangeEO changeEO : changeEOS) {
            BudgetFundVO budgetFundVO = new BudgetFundVO();
            try {
                budgetFundVO = JSON.parseObject(changeEO.getChangeContent(), BudgetFundVO.class);
            } catch (Exception e) {
                logger.warn("不是BudgetFundVO类数据，不能解析");
            }
            if (!StringUtils.equals(budgetFundVO.getType(),"FUND")){
                break;
            }
            if (StringUtils.equals(budgetFundVO.getYear(),year)){
                resultList.add(budgetFundVO);
            }
        }
        return resultList;

    }

    /***
    * @Description: 提取detail信息
    * @Param: [id]
    * @return: java.util.List<com.adc.da.research.funds.vo.change.DetailChangeVO>
    * @Author: yanyujie
    * @Date: 2020/12/9
    */
    private List<DetailChangeVO> extractDetail(String id) throws Exception {
        List<DetailChangeVO> resultList=new ArrayList<>();
        ResearchProjectChangeEOPage changeEOPage=new ResearchProjectChangeEOPage();
        changeEOPage.setProjectId(id);
        changeEOPage.setChangeAttribute("经费预算");
        changeEOPage.setChangeStatus("审核通过");
        final List<ResearchProjectChangeEO> changeEOS = this.researchProjectChangeEOService.queryByList(changeEOPage);
        for (ResearchProjectChangeEO changeEO : changeEOS) {
            DetailChangeVO detailChangeVO = new DetailChangeVO();
            try {
                detailChangeVO = JSON.parseObject(changeEO.getChangeContent(), DetailChangeVO.class);
            } catch (Exception e) {
                logger.warn("DetailChangeVO，不能解析");
            }
            if (!StringUtils.equals(detailChangeVO.getType(),"DETAIL")){
                continue;
            }
            resultList.add(detailChangeVO);
        }
        return resultList;
    }
}
