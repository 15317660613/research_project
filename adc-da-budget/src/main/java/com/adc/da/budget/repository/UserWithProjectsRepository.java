package com.adc.da.budget.repository;

import com.adc.da.budget.entity.UserWithProjects;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;
import java.util.Set;

/**
 * @author qichunxu
 */
public interface UserWithProjectsRepository extends ElasticsearchRepository<UserWithProjects, String> {
    //根据

    List<UserWithProjects> findByUserIdIn(Set<String> userId);

    List<UserWithProjects> findByUserIdIn(String[] userId);

}
