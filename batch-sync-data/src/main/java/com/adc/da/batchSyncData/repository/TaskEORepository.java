package com.adc.da.batchSyncData.repository;

import com.adc.da.batchSyncData.entity.TaskEO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface TaskEORepository extends ElasticsearchRepository<TaskEO, String> {

}
