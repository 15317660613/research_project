package com.adc.da.batchSyncData.service;

import com.adc.da.batchSyncData.entity.ProjectEO;
import com.adc.da.batchSyncData.entity.TaskEO;
import com.adc.da.batchSyncData.repository.ProjectEORepository;
import com.adc.da.batchSyncData.repository.TaskEORepository;
import com.adc.da.budget.dao.BudgetEODao;
import com.adc.da.budget.dao.OrgListDao;
import com.adc.da.budget.entity.BudgetEO;
import com.adc.da.budget.entity.OrgWithLevelEO;
import com.adc.da.budget.entity.Project;
import com.adc.da.budget.entity.UserWithProjects;
import com.adc.da.budget.repository.ProjectRepository;
import com.adc.da.budget.repository.UserWithProjectsRepository;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.StringUtils;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.adc.da.budget.constant.ProjectType.BUSINESS_PROJECT;
import static com.adc.da.budget.constant.ProjectType.RESEARCH_PROJECT;

@Service
@Slf4j
public class ProjectEOService {
    @Autowired
    private ProjectEORepository projectEORepository;

    @Autowired
    private TaskEORepository taskEORepository;

    @Autowired
    private BudgetEODao budgetEODao;

    public Boolean batchUpdateProject(Integer projectType) {
        //List<ProjectEO> projectEOList = projectEORepository.findByProjectType(projectType);
        List<ProjectEO> projectEOList = Lists.newArrayList(projectEORepository.findAll());
        for (ProjectEO project : projectEOList) {
            BudgetEO budgetEO = budgetEODao.selectByPrimaryKey(project.getBudgetId());
            if (null != budgetEO) {
                project.setProjectType(Integer.valueOf(budgetEO.getProperty()));
            }
            if (null == project.getProjectBeginTime() && null != project.getProjectStartTime()) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(project.getProjectStartTime());
                calendar.add(Calendar.YEAR, 1);
                project.setProjectBeginTime(project.getProjectStartTime());
                if (projectType != 1) {
                    project.setProjectEndTime(calendar.getTime());
                }
            }
        }
        projectEORepository.save(projectEOList);
        return true;
    }

    public List<String> getProjectIdsByProjectType(Integer projectType) {
        List<String> projectIds = new ArrayList<>();
        // List<ProjectEO> projectEOS =projectEORepository.findByProjectType(projectType);
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
                                                     .must(QueryBuilders.termQuery("projectType", projectType));
        List<ProjectEO> projectList = Lists.newArrayList(projectEORepository.search(queryBuilder));
        for (ProjectEO projectEO : projectList) {
            projectIds.add(projectEO.getId());
        }
        return projectIds;
    }

    public void batchUpdateTaskType() {
        List<TaskEO> taskEOList = Lists.newArrayList(taskEORepository.findAll());
        for (TaskEO taskEO : taskEOList) {
            if (StringUtils.isNotEmpty(taskEO.getBudgetId())) {
                BudgetEO budgetEO = budgetEODao.selectByPrimaryKey(taskEO.getBudgetId());
                if (null != budgetEO) {
                    taskEO.setProjectType(Integer.valueOf(budgetEO.getProperty()));
                } else {
                    log.warn("budget not found {}", taskEO.getBudgetId());
                }
            } else if (StringUtils.isNotEmpty(taskEO.getProjectId())) {
                ProjectEO projectEO = projectEORepository.findOne(taskEO.getProjectId());
                if (null != projectEO) {
                    taskEO.setProjectType(projectEO.getProjectType());
                } else {
                    log.warn("project not found {}", taskEO.getProjectId());
                }
            }
        }
        taskEORepository.save(taskEOList);
    }

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private OrgListDao orgListDao;

    public void updateProjectDept() {
        List<Project> rows = projectRepository.findByProjectType(BUSINESS_PROJECT);

        List<OrgWithLevelEO> orgEOList = orgListDao.getMinUserOrgIdInfo();

        Map<String, String> leadDeptMap = new HashMap<>(orgEOList.size());

        for (OrgWithLevelEO eo : orgEOList) {
            leadDeptMap.put(eo.getUserId(), eo.getId());
        }
        List<Project> updateList = new ArrayList<>();
        String formDeptId;
        String leaderId;
        for (Project project : rows) {
            formDeptId = project.getDeptId();
            leaderId = project.getProjectLeaderId();
            /* 找寻负责人与实际部门id不符的记录 */
            try {
                if (null != leaderId && !(formDeptId).equals(leadDeptMap.get(leaderId))) {
                    project.setDeptId(leadDeptMap.get(leaderId));
                    updateList.add(project);
                }
            } catch (Exception e) {
                log.warn(e.getMessage(), e);
                log.warn("leaderId : {}", leaderId);
            }
        }

        log.warn("total need update {} ", updateList.size());

        if (true) {
            throw new AdcDaBaseException("该方法仅开发人员使用");
        } else {
            projectRepository.save(rows);
        }

    }

    public void updateDomainId() {
        List<Project> rows = projectRepository.findByProjectType(BUSINESS_PROJECT);
        List<BudgetEO> budgetEOList = budgetEODao.findByDomainIdNotNull();

        Map<String, String> budgetIdDomainIdMap = new HashMap<>(budgetEOList.size());
        for (BudgetEO eo : budgetEOList) {
            budgetIdDomainIdMap.put(eo.getId(), eo.getDomainId());
        }

        List<Project> updateList = new ArrayList<>();
        String budgetId;
        String domainId;
        String contractNO;
        for (Project project : rows) {
            /* 找寻负责人与实际部门id不符的记录 */
            try {
                budgetId = project.getBudgetId();
                if (budgetId != null) {
                    domainId = budgetIdDomainIdMap.get(budgetId);
                    if (null != domainId) {
                        project.setBudgetDomainId(domainId);
                        updateList.add(project);
//                        contractNO = project.getContractNo();
//                        if (StringUtils.isNotEmpty(contractNO)) {
//                            project.setContractNoDomainId(contractNO + domainId);
//                        } else {
//                            log.warn("contractNO is null projectId is {}", project.getId());
//                        }
                    } else {
                        log.warn("domainId is null projectId is {}", project.getId());
                    }
                } else {
                    log.warn("budgetId is null projectId is {}", project.getId());
                }
            } catch (Exception e) {
                log.warn(e.getMessage(), e);
            }
        }

        log.warn("total need update {} ", updateList.size());

        if (false) {
            throw new AdcDaBaseException("该方法仅开发人员使用");
        } else {
            projectRepository.save(rows);
        }
        log.warn("complete  {} ", updateList.size());

    }

    public void a() {
        List<String> x = new ArrayList<>();
        x.add("GPLHFBSLLJ");
        x.add("6MSN23FYVZ");
        x.add("ZMTE476TX3");
        x.add("23M8A7SN6Y");
        x.add("R6GHU5D283");

        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery()
                                                     .must(QueryBuilders.termQuery("projectType", RESEARCH_PROJECT));

        queryBuilder.mustNot(QueryBuilders.termsQuery("id", x));
        List<Project> projectList = Lists.newArrayList(projectRepository.search(queryBuilder));
        List<String> projectIds = new ArrayList<>();

        Set<String> userIds = new HashSet<>();

        for (Project projectEO : projectList) {
            projectEO.setDelFlag(true);
            projectIds.add(projectEO.getId());
            userIds.addAll(Arrays.asList(projectEO.getProjectMemberIds()));
        }
        List<UserWithProjects> xxa = ((LinkedList<UserWithProjects>) userWithProjectsRepository.findAll(userIds));

        Set<String> projectIdSet;
        for (UserWithProjects userWithProjects : xxa) {
            projectIdSet = userWithProjects.getProjectIds();
            projectIdSet.removeAll(projectIds);
            userWithProjects.setProjectIds(projectIdSet);
        }
        if (false) {
//
        }
        userWithProjectsRepository.save(xxa);
//            projectRepository.save(projectList);

    }

    public void initUserWithProject() {
        List<String> x = new ArrayList<>();
        x.add("7ZNEQ3YQWS");
        x.add("8H3W7ZTD2K");

        List<UserWithProjects> xxx = new ArrayList<>();
        for (String userId : x) {

            BoolQueryBuilder qb2 = QueryBuilders.boolQuery()
                                                .mustNot(QueryBuilders.termQuery("delFlag", Boolean.TRUE))
                                                .must(QueryBuilders.termsQuery("projectMemberIds", userId));

            List<Project> projectList = Lists.newArrayList(projectRepository.search(qb2));
            Set<String> p = new HashSet<>();
            Set<String> b = new HashSet<>();
            for (Project project1 : projectList) {
                p.add(project1.getId());
                b.add(project1.getBudgetId());
            }
            UserWithProjects userWithProjects = userWithProjectsRepository.findOne(userId);
            userWithProjects.setProjectIds(p);
            userWithProjects.setBusinessIds(b);
            xxx.add(userWithProjects);
        }

    }

    @Autowired
    private UserWithProjectsRepository userWithProjectsRepository;

    public void checkDuplicateData() {
        List<Project> rows = projectRepository.findByProjectType(BUSINESS_PROJECT);
        List<BudgetEO> budgetEOList = budgetEODao.findByDomainIdNotNull();
        Map<String, String> budgetIndexMap = new HashMap<>();

        budgetEOList.forEach(budgetEO -> budgetIndexMap.put(budgetEO.getId(), budgetEO.getDomainId()));

        Map<String, List<String>> source = new HashMap<>();
        String contractNoAndDomainId;
        String contractNo;
        List<String> idList;
        Map<String, Project> projectIndexMap = new HashMap<>();
        for (Project row : rows) {
            contractNo = row.getContractNo();
            if (StringUtils.isNotEmpty(contractNo) && contractNo.length() > 10
                && StringUtils.isNotEmpty(row.getBudgetId())
                && null != budgetIndexMap.get(row.getBudgetId())) {
                contractNoAndDomainId = row.getContractNo() + budgetIndexMap.get(row.getBudgetId());

                if (source.containsKey(contractNoAndDomainId)) {
                    idList = source.get(contractNoAndDomainId);
                } else {
                    idList = new ArrayList<>();
                }
                idList.add(row.getId());
                source.put(contractNoAndDomainId, idList);
                projectIndexMap.put(row.getId(), row);
//                log.warn(
//                    "name {} , id {}, contractNo {} ,budgetId {} ,domainId {}",
//                    row.getName(),
//                    row.getId(),
//                    row.getContractNo(),
//                    row.getBudgetId(), row.getBudgetDomainId());
            }

        }

        source.forEach((k, v) -> {
            if (v.size() > 1) {
                log.error("contractNoAndDomainId is {}  count {}", k, v);
                v.forEach(s -> {
                    Project project = projectIndexMap.get(s);
                    log.error("{} ", project.getName());
                });
//            } else {
//                log.warn("contractNoAndDomainId is {}  count {}", k, v);
            }

        });
    }
}
