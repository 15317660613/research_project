package com.adc.da.budget.repository;

import com.adc.da.budget.entity.Business;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface BusinessRepository extends ElasticsearchRepository<Business,String> {
    List<Business> findByNameLike(String name);
}
