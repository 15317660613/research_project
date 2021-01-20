package com.adc.da.batchSyncData.repository;

import com.adc.da.batchSyncData.entity.ProjectEO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ProjectEORepository extends ElasticsearchRepository<ProjectEO,String> {
    List<ProjectEO> findByProjectType(Integer projectType);

}
