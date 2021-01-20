package com.adc.da.budget.repository;

import com.adc.da.budget.entity.RevenueExpense;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface RevenueExpenseRepository extends ElasticsearchRepository<RevenueExpense,String> {
}
