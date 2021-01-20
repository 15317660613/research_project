package com.adc.da.research.project.service;

import com.adc.da.research.project.page.ResearchProjectBudgetEOPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.adc.da.base.service.BaseService;
import com.adc.da.research.project.dao.ResearchProjectBudgetEODao;
import com.adc.da.research.project.entity.ResearchProjectBudgetEO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *
 * <br>
 * <b>功能：</b>RS_RESEARCH_PROJECT_BUDGET ResearchProjectBudgetEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-11-10 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("researchProjectBudgetEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class ResearchProjectBudgetEOService extends BaseService<ResearchProjectBudgetEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(ResearchProjectBudgetEOService.class);

    @Autowired
    private ResearchProjectBudgetEODao dao;

    public ResearchProjectBudgetEODao getDao() {
        return dao;
    }

    public  Map<String,List<ResearchProjectBudgetEO>> queryByArr(ResearchProjectBudgetEOPage page) throws Exception {
        Map<String,List<ResearchProjectBudgetEO>> map=new HashMap<>();
        List<ResearchProjectBudgetEO> yearList =new ArrayList<>();//年份明细列表
        List<ResearchProjectBudgetEO> querterlyList =new ArrayList<>();//季度明细列表
        List<ResearchProjectBudgetEO> monthList =new ArrayList<>();//月份明细列表
        List<ResearchProjectBudgetEO> rows = this.queryByPage(page);
        for (ResearchProjectBudgetEO r:rows) {
            if(r.getBudgetYear()!=null){
                yearList.add(r);
            }else if(r.getBudgetQuarterly()!=null){

                querterlyList.add(r);
            } else if(r.getBudgetMonth()!=null){
                monthList.add(r);
            }

        }
//        map.put("year",yearList);
        map.put("querterly",querterlyList);
        map.put("month",monthList);
        return map;

    }


}
