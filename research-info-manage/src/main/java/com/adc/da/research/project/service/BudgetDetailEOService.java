package com.adc.da.research.project.service;

import com.adc.da.base.service.BaseService;
import com.adc.da.login.util.UserUtils;
import com.adc.da.research.config.service.FundDetailsEOService;
import com.adc.da.research.project.dao.BudgetDetailEODao;
import com.adc.da.research.project.entity.BudgetDetailEO;
import com.adc.da.research.project.page.BudgetDetailEOPage;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.CollectionUtils;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 *
 * <br>
 * <b>功能：</b>RS_BUDGET_DETAIL BudgetDetailEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-10-30 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("budgetDetailEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class BudgetDetailEOService extends BaseService<BudgetDetailEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(BudgetDetailEOService.class);

    @Autowired
    private BudgetDetailEODao dao;

    @Autowired
    private FundDetailsEOService fundDetailsEOService;
    public BudgetDetailEODao getDao() {
        return dao;
    }


    //根据projectId删除
    public void deleteByProjectId(String projectId){
        dao.deleteByProjectId(projectId);

    }



    /**
     * 表格数据处理(查看)
     * @param page
     * @return
     * @throws Exception
     */
    public Map<String, List<Map<String,Object>>> queryByArr(BudgetDetailEOPage page) throws Exception {
        //FundDetailsEO fundDetailsEO= fundDetailsEOService.selectByPrimaryKey( r.getBudgetDetailTypeId()) ;
        Map<String,List<Map<String,Object>>> map=new HashMap<>();
        List<Map<String,Object>> yearList =new ArrayList<>();//年份明细列表
        List<Map<String,Object>> querterlyList =new ArrayList<>();//季度明细列表
        List<Map<String,Object>> monthList =new ArrayList<>();//月份明细列表


        List<String> yearArrList =new ArrayList<>();
        List<Map<String,Object>> querterlyArrList =new ArrayList<>();
        List<Map<String,Object>> monthArrList =new ArrayList<>();
        List<BudgetDetailEO> rows = this.queryByList(page);

        if(CollectionUtils.isEmpty(rows)){
            return null;
        }


        for (BudgetDetailEO r:rows) {
            if(r.getBudgetYear()!=null){
                 yearArrList.add(r.getBudgetYear());
            }else if(r.getBudgetQuarterly()!=null){
                Map<String,Object> stringMap=new HashMap<>();
                stringMap.put("name",r.getBudgetDetailTypeName());
                stringMap.put("id",r.getBudgetDetailTypeId());
                querterlyArrList.add(stringMap);
            } else if(r.getBudgetMonth()!=null){
                Map<String,Object> stringMap=new HashMap<>();
                stringMap.put("name",r.getBudgetDetailTypeName());
                stringMap.put("id",r.getBudgetDetailTypeId());
                monthArrList.add(stringMap);
            }
        }
        List<String> uniqueYear = yearArrList.stream().distinct().collect(Collectors.toList());//去重集合-年份
        List<Map<String,Object>> uniqueQuerterly = querterlyArrList.stream().distinct().collect(Collectors.toList());//去重集合-季度
        List<Map<String,Object>> uniqueMonth = monthArrList.stream().distinct().collect(Collectors.toList());//去重集合-月份

        for (int i = 0; i <uniqueYear.size() ; i++) {
        for (BudgetDetailEO r:rows) {

            if(r.getBudgetYear()!=null&&r.getBudgetYear().equals(uniqueYear.get(i))){
                if(i==0){//第一条数据生成
                    Map<String,Object> yearMap=new HashMap<>();
                    yearMap.put("course",r.getBudgetDetailTypeName());//科目名称
                    yearMap.put("courseId",r.getBudgetDetailTypeId());//科目id
                    yearMap.put("year"+i,r.getBudgetAmount());
                    yearMap.put("yearArr",uniqueYear);
                    yearMap.put("budgetType",r.getBudgetType());
                    yearMap.put("projectId",r.getProjectId());
                    yearMap.put("ext1",r.getExt1());
                    yearMap.put("ext2",r.getExt2());
                    yearMap.put("totalNum",r.getBudgetAmount());//合计
                    yearMap.put("parentId",r.getParentId());//父级id
                    yearList.add(yearMap);

                }else{

                    for (int j = 0; j <yearList.size() ; j++) {
                        if(r.getBudgetDetailTypeName().equals(yearList.get(j).get("course"))){
                            yearList.get(j).put("year"+i,r.getBudgetAmount());
                            double total = 0.00;
                            if(StringUtils.isNotEmpty(r.getBudgetAmount())) {
                                 total = this.add(yearList.get(j).get("totalNum").toString(), r.getBudgetAmount());
                            }
                            yearList.get(j).put("totalNum",total);
                        }
                    }
                }
                }
            }

        }
        for (int i = 0; i <uniqueQuerterly.size() ; i++) {
            for (BudgetDetailEO r:rows) {

                if(r.getBudgetQuarterly()!=null&&StringUtils.isNotEmpty(r.getBudgetDetailTypeId())
                        &&r.getBudgetDetailTypeId().equals(uniqueQuerterly.get(i).get("id"))){
                    if(i==0){//第一条数据生成
                        Map<String,Object> querterlyMap=new LinkedHashMap<>();
                        querterlyMap.put("quarter",r.getBudgetQuarterly());//季度
                        if(StringUtils.isNotEmpty(r.getBudgetAmount())){
                            querterlyMap.put("type"+i,r.getBudgetAmount());
                            querterlyMap.put("totalNum",r.getBudgetAmount());
                        }else {
                            querterlyMap.put("type"+i,"");
                        }
                        querterlyMap.put("typeAll",uniqueQuerterly);
                        querterlyMap.put("budgetType",r.getBudgetType());
                        querterlyMap.put("ext2",r.getExt2());
                        querterlyMap.put("projectId",r.getProjectId());
                        querterlyMap.put("parentId",r.getParentId());//父级id
                        querterlyList.add(querterlyMap);

                    }else{

                        for (int j = 0; j <querterlyList.size() ; j++) {
                            if(r.getBudgetQuarterly().equals(querterlyList.get(j).get("quarter"))){
                                querterlyList.get(j).put("type"+i,r.getBudgetAmount());
                                double total = 0.00;
                                if(StringUtils.isNotEmpty(r.getBudgetAmount())) {
                                     total = this.add(querterlyList.get(j).get("totalNum").toString(), r.getBudgetAmount());
                                }
                                querterlyList.get(j).put("totalNum",total);
                            }
                        }
                    }
                }
            }

        }
        for (int i = 0; i <uniqueMonth.size() ; i++) {
            for (BudgetDetailEO r:rows) {

                if(r.getBudgetMonth()!=null
                        &&StringUtils.isNotEmpty(r.getBudgetDetailTypeId())
                        &&r.getBudgetDetailTypeId().equals(uniqueMonth.get(i).get("id"))){
                    if(i==0){//第一条数据生成
                        Map<String,Object> monthMap=new LinkedHashMap<>();

                        monthMap.put("month",r.getBudgetMonth());//月份

                        if(StringUtils.isNotEmpty(r.getBudgetAmount())){
                            monthMap.put("type"+i,r.getBudgetAmount());
                            monthMap.put("totalNum",r.getBudgetAmount());
                        }else {
                            monthMap.put("type"+i,"");
                        }
                        monthMap.put("typeAll",uniqueMonth);
                        monthMap.put("budgetType",r.getBudgetType());
                        monthMap.put("ext2",r.getExt2());
                        monthMap.put("projectId",r.getProjectId());
                        monthMap.put("parentId",r.getParentId());//父级id
                        monthList.add(monthMap);

                    }else{

                        for (int j = 0; j <monthList.size() ; j++) {
                            if(r.getBudgetMonth().equals(monthList.get(j).get("month"))){
                                monthList.get(j).put("type"+i,r.getBudgetAmount());
                                double total = 0.00;
                                if(StringUtils.isNotEmpty(r.getBudgetAmount())) {
                                     total = this.add(monthList.get(j).get("totalNum").toString(), r.getBudgetAmount());
                                }
                                monthList.get(j).put("totalNum",total);
                            }
                        }
                    }
                }
            }

        }
        map.put("year",yearList);//年度明细集合

       map.put("quarterly",querterlyList);//季度明细集合
       map.put("month",monthList);//月份明细集合
       return map;

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
    public Map<String, List<Map<String,Object>>> insertByYear(Map<String, List<Map<String,Object>>> listMap)throws Exception{

       // for(Map.Entry<String, List<Map<String,Object>>> entry:listMap.entrySet())
        UserEO user = UserUtils.getUser();
        if (StringUtils.isEmpty(user)){
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }

        dao.deleteByProjectId((String) listMap.get("year").get(1).get("projectId"));
            //年份明细
            List<Map<String, Object>> year = listMap.get("year");
        List<String> yearArr = (List<String>)year.get(0).get("yearArr");//年份数组
        for (int j = 0; j <yearArr.size() ; j++) {
            for (int i = 0; i <year.size() ; i++) {
                        Map<String, Object> map = year.get(i);


                        BudgetDetailEO budgetDetailEO = new BudgetDetailEO();
                        budgetDetailEO.setBudgetYear(String.valueOf(yearArr.get(j)));//年份
                        budgetDetailEO.setProjectId((String) map.get("projectId"));
                        budgetDetailEO.setId(UUID.randomUUID10());
                        budgetDetailEO.setExt1((String) map.get("ext1"));
                        budgetDetailEO.setExt2(String.valueOf(map.get("ext2")) );//排序字段
                        budgetDetailEO.setBudgetDetailTypeId((String) map.get("courseId"));//科目id
                        budgetDetailEO.setBudgetDetailTypeName((String) map.get("course"));//科目名称
                        budgetDetailEO.setBudgetType((String) map.get("budgetType"));//预算类型
                        budgetDetailEO.setParentId((String) map.get("parentId"));//父级id
                        if(map.get("year"+j)!=null){
                            budgetDetailEO.setBudgetAmount(Double.valueOf(map.get("year"+j).toString()) );//金额
                        }
                        budgetDetailEO.setDelFlag(0);
                        budgetDetailEO.setCreateUserId(user.getUsid());
                        budgetDetailEO.setCreateUserName(user.getUsname());
                        budgetDetailEO.setCreateTime(new Date());
                        this.insertSelective(budgetDetailEO);


                    }

                }
           //季度明细
            List<Map<String, Object>> quarterly = listMap.get("quarterly");
        List<Map<String, Object>> quarterlyArr = (List<Map<String, Object>>) quarterly.get(0).get("typeAll");//获取科目数组
        for (int j = 0; j <quarterlyArr.size() ; j++) {//季度数组
            for (int i = 0; i <quarterly.size() ; i++) {
                Map<String, Object> map = quarterly.get(i);

                    BudgetDetailEO budgetDetailEO = new BudgetDetailEO();
                    budgetDetailEO.setBudgetQuarterly((String) map.get("quarter"));//季度
                    budgetDetailEO.setProjectId((String) map.get("projectId"));
                    budgetDetailEO.setId(UUID.randomUUID10());
                    budgetDetailEO.setExt2(String.valueOf(map.get("ext2")) );//排序字段
                    budgetDetailEO.setBudgetDetailTypeId((String) quarterlyArr.get(j).get("id"));//科目id
                    budgetDetailEO.setBudgetDetailTypeName((String) quarterlyArr.get(j).get("name"));//科目名称
                    budgetDetailEO.setBudgetType((String) map.get("budgetType"));//预算类型
                    budgetDetailEO.setParentId((String) map.get("parentId"));//父级id
                    if(StringUtils.isNotEmpty(map.get("type"+j))){
                        budgetDetailEO.setBudgetAmount(Double.valueOf(map.get("type"+j).toString()));//金额
                    }
                    budgetDetailEO.setDelFlag(0);
                    budgetDetailEO.setCreateUserId(user.getUsid());
                    budgetDetailEO.setCreateUserName(user.getUsname());
                    budgetDetailEO.setCreateTime(new Date());
                    this.insertSelective(budgetDetailEO);

                }

            }

            //月份明细
            List<Map<String, Object>> month = listMap.get("month");
        List<Map<String, Object>> monthArr = (List<Map<String, Object>>)  month.get(0).get("typeAll");//获取科目数组
        for (int j = 0; j <monthArr.size() ; j++) {//月份数组
            for (int i = 0; i <month.size() ; i++) {
                Map<String, Object> map = month.get(i);


                    BudgetDetailEO budgetDetailEO = new BudgetDetailEO();
                    budgetDetailEO.setBudgetMonth((String) map.get("month"));//月份
                    budgetDetailEO.setProjectId((String) map.get("projectId"));
                    budgetDetailEO.setId(UUID.randomUUID10());
                    budgetDetailEO.setExt2(String.valueOf(map.get("ext2")));//排序字段
                    budgetDetailEO.setBudgetDetailTypeId((String) monthArr.get(j).get("id"));//科目id
                    budgetDetailEO.setBudgetDetailTypeName((String) monthArr.get(j).get("name"));//科目名称
                    budgetDetailEO.setBudgetType((String) map.get("budgetType"));//预算类型
                    budgetDetailEO.setParentId((String) map.get("parentId"));//父级id
                    if(StringUtils.isNotEmpty(map.get("type"+j))){
                        budgetDetailEO.setBudgetAmount(Double.valueOf(map.get("type"+j).toString()));//金额
                    }
                    budgetDetailEO.setDelFlag(0);
                    budgetDetailEO.setCreateUserId(user.getUsid());
                    budgetDetailEO.setCreateUserName(user.getUsname());
                    budgetDetailEO.setCreateTime(new Date());
                    this.insertSelective(budgetDetailEO);

                }

            }
            return listMap;

    }

    /**
     * 返回每年合计金额
     * @param page
     * @return
     * @throws Exception
     */
    public   List<BudgetDetailEO> queryAmount(BudgetDetailEOPage page) throws Exception {

        return dao.queryAmount();

    }

    public BudgetDetailEO saveBudgetDetail(BudgetDetailEO budgetDetailEO) throws Exception{
        UserEO loginUserEO = UserUtils.getUser();
        if (StringUtils.isEmpty(loginUserEO)){
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        budgetDetailEO.setId(UUID.randomUUID10());
        budgetDetailEO.setCreateTime(new Date());
        budgetDetailEO.setCreateUserId(loginUserEO.getUsid());
        budgetDetailEO.setCreateUserName(loginUserEO.getUsname());
        budgetDetailEO.setDelFlag(0);
        dao.insertSelective(budgetDetailEO);
        return budgetDetailEO;
    }

    public List<BudgetDetailEO> batchSaveBudgetDetailEO(List<BudgetDetailEO> budgetDetailEOList) throws Exception{
        UserEO loginUserEO = UserUtils.getUser();
        if (StringUtils.isEmpty(loginUserEO)){
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        if(CollectionUtils.isEmpty(budgetDetailEOList)){
            return new ArrayList<>();
        }
        for(BudgetDetailEO budgetDetailEO:budgetDetailEOList){
            budgetDetailEO.setId(UUID.randomUUID10());
            budgetDetailEO.setCreateTime(new Date());
            budgetDetailEO.setCreateUserId(loginUserEO.getUsid());
            budgetDetailEO.setCreateUserName(loginUserEO.getUsname());
            budgetDetailEO.setDelFlag(0);
        }
        dao.deleteByProjectIdAndBudgetType(budgetDetailEOList.get(0).getProjectId(),budgetDetailEOList.get(0).getBudgetType());
        dao.batchInsertSelective(budgetDetailEOList);
        return budgetDetailEOList;
    }
}
