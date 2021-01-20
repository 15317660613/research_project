package com.adc.da.budget.service;

import com.adc.da.base.service.BaseService;
import com.adc.da.budget.dao.MyBudgetEODao;
import com.adc.da.business.entity.BudgetEO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("MyBudgetEOService")
public class MyBudgetEOService extends BaseService<BudgetEO, String> {

    @Autowired
    private MyBudgetEODao dao;


    @Override
    public MyBudgetEODao getDao() {
        return dao;
    }

    public String getBudgetEOIdByBudgetEOName( String budgetName){
        return this.dao.getBudgetEOIdByBudgetEOName(budgetName);
    }
    public List<String> queryBudgetEOIdsByBudgetEONames( List<String> budgetNames){
        return this.dao.queryBudgetEOIdsByBudgetEONames(budgetNames);
    }




}