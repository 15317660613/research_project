package com.adc.da.budget.service;

import com.adc.da.budget.entity.ExpensesIncurred;
import com.adc.da.budget.entity.Project;
import com.adc.da.budget.entity.RevenueExpense;
import com.adc.da.budget.repository.ExpensesIncurredRepository;
import com.adc.da.budget.repository.ProjectRepository;
import com.adc.da.budget.repository.RevenueExpenseRepository;
import com.adc.da.util.utils.CollectionUtils;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 气泡业务层
 * created by chenhaidong 2018/11/29
 */
@Service
public class BubbleService {
    @Autowired
    private  ProjectRepository projectRepository;
    @Autowired
    private  ExpensesIncurredRepository expensesIncurredRepository;
    @Autowired
    private  RevenueExpenseRepository revenueExpenseRepository;

    public final static int PROJECT_PROFIT = 1;
    public final static int BUSINESS_PROFIT = 2;
    public final static int PROJECT_OUTPUT = 3;
    public final static int BUSINESS_OUTPUT = 4;

    /**
     * 查询气泡信息
     * @param type 1-项目利润视角   2-业务利润视角   3-项目产值视角   4-业务产值视角
     * @return
     */
    public List<List<List>> queryBubble(int type){
        List<List<List>> bubbleDTOLists = new ArrayList<>();
        if(type < PROJECT_PROFIT || type > BUSINESS_OUTPUT) {
            return bubbleDTOLists;
        }
        //查询所有项目
        List<Project> projectList = Lists.newArrayList(projectRepository.findAll());
        //如果项目列表为空，则直接返回
        if (CollectionUtils.isEmpty(projectList)) {
            return bubbleDTOLists;
        }
        //查询所有的项目收入信息
        List<ExpensesIncurred> expensesIncurredList = Lists.newArrayList(expensesIncurredRepository.findAll());
        //查询所有的项目支出信息
        List<RevenueExpense> revenueExpenseList = Lists.newArrayList(revenueExpenseRepository.findAll());
        //项目id对应支出Map
        Map<String,Double> expensesIncurredMap = new HashMap<>();
        //项目id对应收入Map
        Map<String,Double> revenueExpenseMap = new HashMap<>();
        String projectId = null;
        Double money = null;
        Integer input = null;
        //获取项目对应的总支出
        expensesIncurred(expensesIncurredList, expensesIncurredMap);
        //获取项目对应的总收入
        revenueExpense(revenueExpenseList, revenueExpenseMap);

        //如果查询的是项目视角
        if((type & 1) == 1){
            projectBubble(bubbleDTOLists, type, projectList, expensesIncurredMap, revenueExpenseMap);
        }

        //如果是业务视角
        if((type & 1) == 0){
            //业务对应项目列表Map
            Map<String,List<Project>> businessMap = getBusinessMap(projectList);
            //遍历业务-项目列表map
            for (String business:businessMap.keySet()) {
                List<List> lists = addBubbleDTOList(businessMap,business,type,expensesIncurredMap,revenueExpenseMap);
                bubbleDTOLists.add(lists);
            }
        }
        return bubbleDTOLists;
    }

    private List<List> addBubbleDTOList(Map<String,List<Project>> businessMap,String business,int type,
                                  Map<String,Double> expensesIncurredMap,Map<String,Double> revenueExpenseMap){
        String projectId = null;
        Double money = null;
        Integer input = null;
        //业务支出总和
        Double expensesIncurredCount = 0.0;
        //业务收入总和
        Double revenueExpenseCount = 0.0;
        //业务利润总和
        //业务投入人力总和
        Integer personInputCount = 0;
        List<List> lists = new ArrayList<>();
        List list = new ArrayList();
        lists.add(list);
        for (Project project : businessMap.get(business)) {
            //如果项目不存在，则直接跳过
            if((projectId = project.getId()) == null) {
                continue;
            }
            personInputCount += ((input = project.getPersonInput()) == null ? 0 : input);
            expensesIncurredCount += ((money = expensesIncurredMap.get(projectId)) == null ? 0.0 : money);
            revenueExpenseCount += ((money = revenueExpenseMap.get(projectId)) == null ? 0.0 : money);

        }
        list.add(personInputCount);
        //如果是产值情况，则增加业务总支出
        if(type == BUSINESS_OUTPUT) {
            list.add(expensesIncurredCount);
        }
        list.add(revenueExpenseCount);
        //如果是利润情况，则增加业务总利润
        if(type == BUSINESS_PROFIT) {
            list.add(revenueExpenseCount - expensesIncurredCount);
        }
        list.add(business);
        return lists;

    }

    /**
     * 获取项目对应的总支出
     * @param expensesIncurredList
     * @param expensesIncurredMap
     * @return
     * @author liuzixi
     * date 2019-03-11
     */
    private void expensesIncurred(List<ExpensesIncurred> expensesIncurredList,
                                  Map<String,Double> expensesIncurredMap) {
        for (ExpensesIncurred expensesIncurred:expensesIncurredList) {
            String projectId;
            Double money;
            if(expensesIncurredMap.get((projectId = expensesIncurred.getParentProjectId())) == null) {
                expensesIncurredMap.put(projectId,0.0);
            }
            if((money = expensesIncurred.getExpensesAmount()) != null) {
                expensesIncurredMap.put(projectId,expensesIncurredMap.get(projectId) + money);
            }
        }
    }

    /**
     * 获取项目对应的总收入
     * @param revenueExpenseList
     * @param revenueExpenseMap
     * @return
     * @author liuzixi
     * date 2019-03-11
     */
    private void revenueExpense(List<RevenueExpense> revenueExpenseList,
                       Map<String,Double> revenueExpenseMap) {
        for (RevenueExpense revenueExpense:revenueExpenseList) {
            String projectId;
            if(revenueExpenseMap.get((projectId = revenueExpense.getParentProjectId())) == null) {
                revenueExpenseMap.put(projectId,0.0);
            }
            Double money;
            if((money = revenueExpense.getRevenueAmount()) != null) {
                revenueExpenseMap.put(projectId,revenueExpenseMap.get(projectId) + money);
            }
        }
    }

    /**
     * 项目视角
     * @param bubbleDTOLists
     * @param type
     * @param projectList
     * @param expensesIncurredMap
     * @param revenueExpenseMap
     * @return
     * @author liuzixi
     * date 2019-03-11
     */
    private void projectBubble(List<List<List>> bubbleDTOLists, int type, List<Project> projectList,
                               Map<String,Double> expensesIncurredMap,
                               Map<String,Double> revenueExpenseMap) {
        String projectId;
        Double money;
        Integer input;
        for (Project project:projectList) {
            if((projectId = project.getId()) == null) {
                continue;
            }
            List<List> lists = new ArrayList<>();
            List list = new ArrayList();
            lists.add(list);
            //人力投入
            list.add((input = project.getPersonInput()) == null ? 0 : input);
            //如果是产值情况,增加项目支出
            if(type == PROJECT_OUTPUT) {
                list.add((money = expensesIncurredMap.get(projectId)) == null ? 0.0 : money);
            }
            //项目收入
            list.add((money = revenueExpenseMap.get(projectId)) == null ? 0.0 : money);
            //如果是利润情况，增加项目利润
            if(type ==  PROJECT_PROFIT) {
                list.add((money == null ? 0.0 : money) - (expensesIncurredMap.get(projectId) == null
                        ? 0.0 : expensesIncurredMap.get(projectId)));
            }
            //项目名
            list.add(project.getName());
            //业务名
            list.add(project.getBusinessId());
            bubbleDTOLists.add(lists);
        }
    }

    private Map<String,List<Project>> getBusinessMap(List<Project> projectList) {
        Map<String,List<Project>> businessMap = new HashMap<>();
        String businessName;
        for (Project project:projectList) {
            //如果业务名为空，直接跳过
            if((businessName = project.getBusinessId()) == null) {
                continue;
            }
            if(businessMap.get(businessName) == null) {
                businessMap.put(businessName,new ArrayList<Project>());
            }
            businessMap.get(businessName).add(project);
        }
        return businessMap;
    }
}
