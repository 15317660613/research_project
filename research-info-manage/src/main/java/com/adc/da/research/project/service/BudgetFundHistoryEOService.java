package com.adc.da.research.project.service;

import com.adc.da.base.service.BaseService;
import com.adc.da.login.util.UserUtils;
import com.adc.da.research.project.dao.BudgetFundEODao;
import com.adc.da.research.project.dao.BudgetFundHistoryEODao;
import com.adc.da.research.project.entity.BudgetFundHistoryEO;
import com.adc.da.research.project.page.BudgetFundEOPage;
import com.adc.da.research.project.page.BudgetFundHistoryEOPage;
import com.adc.da.research.project.vo.BudgetFundVO;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.StringUtils;
import com.adc.da.util.utils.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 *
 * <br>
 * <b>功能：</b>RS_BUDGET_FUND_HISTORY BudgetFundHistoryEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-12-01 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("budgetFundHistoryEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class BudgetFundHistoryEOService extends BaseService<BudgetFundHistoryEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(BudgetFundHistoryEOService.class);

    @Autowired
    private BudgetFundHistoryEODao dao;

    public BudgetFundHistoryEODao getDao() {
        return dao;
    }

    @Autowired
    private BudgetFundEODao fundEODao;


    //根据projectId删除
    public void deleteByProjectId(String projectId){
        dao.deleteByProjectId(projectId);

    }
    /**
     * 查询所有年份预算列表
     * @param page
     * @return
     * @throws Exception
     */
    public  List<Map<String,Object>>  queryByBudgetFundYear(BudgetFundHistoryEOPage page) throws Exception {

        List<Map<String,Object>> mapList=new ArrayList<>();
        List<String> yearArrList =new ArrayList<>();//年份集合

        List<BudgetFundHistoryEO> budgetFundEOList = this.queryByList(page);
        for (BudgetFundHistoryEO s:budgetFundEOList) {
            if(s.getBudgetYear()!=null){
                yearArrList.add(s.getBudgetYear());
            }
        }
        List<String> uniqueYear = yearArrList.stream().distinct().collect(Collectors.toList());//去重集合-年份


        for (int i = 0; i <uniqueYear.size() ; i++) {
            for (BudgetFundHistoryEO r:budgetFundEOList) {

                if(r.getBudgetYear()!=null&&r.getBudgetYear().equals(uniqueYear.get(i))){
                    if(i==0){//第一条数据生成
                        Map<String,Object> yearMap=new HashMap<>();

                        yearMap.put("budgetType",r.getBudgetType());
                        yearMap.put("year"+i,r.getBudgetAmount());//对应的分值
                        yearMap.put("yearArr",uniqueYear);
                        yearMap.put("projectId",r.getProjectId());
                        yearMap.put("totalNum",r.getBudgetAmount());
                        yearMap.put("ext1",r.getExt1());
                        mapList.add(yearMap);

                    }else{

                        for (int j = 0; j <mapList.size() ; j++) {
                            if(r.getBudgetType().equals(mapList.get(j).get("budgetType"))){
                                mapList.get(j).put("year"+i,r.getBudgetAmount());

                                double total = this.add(mapList.get(j).get("totalNum").toString(), r.getBudgetAmount());
                                mapList.get(j).put("totalNum",total);
                            }
                        }
                    }
                }
            }



        }




        return mapList;
    }
    /**
     * 提供精确的加法运算。
     * @param v1 被加数
     * @param v2 加数
     * @return 两个参数的和
     */
    public  double add(String v1, double v2) {

        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }


    /**
     * 表格新增
     * @param listMap
     * @throws Exception
     */
    public List<Map<String,Object>> insertByYear(List<Map<String,Object>> listMap)throws Exception{
        UserEO user = UserUtils.getUser();
        if (StringUtils.isEmpty(user)){
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }

        // for(Map.Entry<String, List<Map<String,Object>>> entry:listMap.entrySet())
        dao.deleteByProjectId((String) listMap.get(0).get("projectId"));
        List<String> yearArr = (List<String>) listMap.get(0).get("yearArr");
        for (int j = 0; j <yearArr.size() ; j++) {//年份数组
            for (int i = 0; i <listMap.size() ; i++) {
                Map<String, Object> map = listMap.get(i);

                BudgetFundHistoryEO budgetFundEO = new BudgetFundHistoryEO();
                budgetFundEO.setBudgetYear(String.valueOf(yearArr.get(j)));//年份
                budgetFundEO.setProjectId((String) map.get("projectId"));
                budgetFundEO.setId(UUID.randomUUID10());
                budgetFundEO.setDelFlag((long) 0);

                budgetFundEO.setBudgetAmount(Double.valueOf( map.get("year"+j).toString()));//金额
                budgetFundEO.setCreateUserId(user.getUsid());
                budgetFundEO.setBudgetType((String) map.get("budgetType"));//预算类型
                budgetFundEO.setCreateUserName(user.getUsname());
                budgetFundEO.setCreateTime(new Date());
                budgetFundEO.setExt1((String) map.get("ext1"));


                this.insertSelective(budgetFundEO);


            }

        }



        return listMap;

    }

    public List<BudgetFundVO> queryChange(BudgetFundHistoryEOPage page){
        BudgetFundHistoryEOPage historyEOPage=new BudgetFundHistoryEOPage();
        historyEOPage.setProjectId(page.getProjectId());
        List<BudgetFundVO> historyEOList = dao.queryByCompareList(historyEOPage);

        BudgetFundEOPage budgetFundEOPage=new BudgetFundEOPage();
        budgetFundEOPage.setProjectId(page.getProjectId());
        List<BudgetFundVO> budgetFundEOS = fundEODao.queryByCompareList(budgetFundEOPage);

        List<BudgetFundVO> diffrentFund = getDiffrentFund(historyEOList, budgetFundEOS);

        return diffrentFund;

    }

    // 数据比对
    private static List<BudgetFundVO> getDiffrentFund(List<BudgetFundVO> list1, List<BudgetFundVO> list2) {
        List<BudgetFundVO> diff = new ArrayList<BudgetFundVO>();

        Map<BudgetFundVO, Integer> map = new HashMap<BudgetFundVO, Integer>(list1.size() + list2.size());
        List<BudgetFundVO> maxList = list1;
        List<BudgetFundVO> minList = list2;
        if (list2.size() > list1.size()) {
            maxList = list2;
            minList = list1;
        }
        for (BudgetFundVO string : maxList) {
            map.put(string, 1);
        }
        for (BudgetFundVO string : minList) {
            Integer count = map.get(string);
            if (count != null) {
                map.put(string, ++count);
                continue;
            }
            map.put(string, 1);
        }
        for (Map.Entry<BudgetFundVO, Integer> entry : map.entrySet()) {
            if (entry.getValue() == 1) {
                diff.add(entry.getKey());
            }
        }

        return diff;

    }


}
