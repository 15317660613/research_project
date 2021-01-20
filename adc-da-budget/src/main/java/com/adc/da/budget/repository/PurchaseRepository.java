package com.adc.da.budget.repository;

import com.adc.da.budget.entity.Purchase;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface PurchaseRepository extends ElasticsearchRepository<Purchase,String> {
}
