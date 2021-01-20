package com.adc.da.budget.repository;

import com.adc.da.budget.entity.ReportEO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ReportEORepository extends ElasticsearchRepository<ReportEO,String> {

}
