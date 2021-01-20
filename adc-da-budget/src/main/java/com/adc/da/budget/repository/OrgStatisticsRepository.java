package com.adc.da.budget.repository;

import com.adc.da.budget.entity.FinOrgStatisticsEO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface OrgStatisticsRepository extends ElasticsearchRepository<FinOrgStatisticsEO,String> {

    //根据父级机构删除
    void deleteBySuperOrgIdAndType(String orgId,String type);

    void deleteByType(String type);

    //查询
    List<FinOrgStatisticsEO> findBySuperOrgIdAndType(String orgId,String type);



}
