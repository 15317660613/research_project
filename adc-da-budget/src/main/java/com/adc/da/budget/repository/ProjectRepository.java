package com.adc.da.budget.repository;

import com.adc.da.budget.entity.Project;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface ProjectRepository extends ElasticsearchRepository<Project, String> {

    List<Project> findByBudgetId(String budgetId);

    List<Project> findByBudgetIdIn(Collection<String> budgetIds);

    List<Project> findByBudgetIdInAndDelFlagNot(Collection<String> budgetIds, Boolean delFlag);

    List<Project> findByIdInAndDelFlagNot(List<String> idList, Boolean delFlag);

    List<Project> findByProjectType(Integer projectType);


    Project findFirstByIdAndAndDelFlagNot(String id, Boolean delFlag);


    Project findByIdAndDelFlagNot(String id, Boolean delFlag);

    Project findById(String id);

    Project findByBudget(String budgetName);

    Project findByContractNoAndBudgetDomainId(String name, String budgetId);

    Project findByNameAndDelFlagNot(String projectName, Boolean delFlag);

    List<Project> findByIdInAndProjectType(Set<String> id,int projectType);
    List<Project> findByIdIn(List<String> id);
    List<Project> findByIdIn(Set<String> id);

    List<Project> findByIdInAndDelFlagNot(Set<String> id, Boolean delFlag);

    List<Project> findByIdInAndDelFlagNot(List<String> id, boolean delFlag);

    List<Project> findByBudgetIdAndDelFlagNot(String budgetId, Boolean delFlag);

    List<Project> findByBudgetIdAndProjectLeaderIdExistsAndDelFlagNot(String budgetId, Boolean delFlag);

    List<Project> findByProjectOwner(String owner);
    void deleteByIdIn(String [] ids);

    /**
     * 根据业务ID和人员查询
     *
     * @param budgetId
     * @param userId
     * @param delFlag
     * @return
     */
    List<Project> findByBudgetIdAndProjectMemberIdsInAndDelFlagNot(String budgetId, String userId, Boolean delFlag);


    List<Project> findByProjectMemberIdsInAndDelFlagNot(String userId, Boolean delFlag);
    List<Project> findByProjectLeaderIdAndDelFlagNot(String userId, Boolean delFlag);

    /**
     * 根据业务ID和项目状态查询
     *
     * @param budgetId
     * @param delFlag
     * @param status
     * @return
     */
    List<Project> findByBudgetIdAndDelFlagNotAndFinishedStatus(String budgetId, Boolean delFlag, String status);


    List<Project> findByContractNoInAndDelFlagNot(Set<String> ids, Boolean delFlag);

    List<Project> findByBudgetIdAndProjectLeaderIdAndDelFlagNot(String budgetId, String projectLeaderId, Boolean delFlag);

    List<Project> findByProjectYearAndDelFlagNot(int year, boolean flag);
    List<Project> findByProjectYear(int year);

    List<Project> findByProjectYearAndProjectTypeAndDelFlagNot(int year, int projectType, boolean flag);

    List<Project> findByProjectTypeAndDelFlagNot(int projectType, boolean flag);
    List<Project> findByProjectType(int projectType);
    List<Project> findByProjectYearAndProjectType(int year, int projectType);

    List<Project> findByProjectYearAndProjectMonthAndDelFlagNot(int year, int month, boolean flag);

    @Query("{\"match\": {\"name\": {\"query\": \"?0\"}}}")
    Project queryProjectByName(String name);


}
