package com.adc.da.business.repository;

import com.adc.da.business.entity.Project;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ProjectRepositoryBusiness extends ElasticsearchRepository<Project, String> {

}
