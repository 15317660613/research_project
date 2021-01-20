package com.adc.da.business.repository;

import com.adc.da.business.entity.Task;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface MyTaskRepository extends ElasticsearchRepository<Task, String> {

}
