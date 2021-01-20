package com.adc.da.budget.repository;

import com.adc.da.budget.entity.ExpensesIncurred;
import com.adc.da.budget.entity.Task;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ExpensesIncurredRepository extends ElasticsearchRepository<ExpensesIncurred,String> {
}
