package com.adc.da.oa.service;

import com.adc.da.budget.dao.BudgetEODao;
import com.adc.da.budget.entity.Project;
import com.adc.da.budget.repository.ProjectRepository;
import com.adc.da.budget.service.BaseService;
import com.adc.da.excel.poi.excel.ExcelImportUtil;
import com.adc.da.excel.poi.excel.entity.ImportParams;
import com.adc.da.oa.dto.ProjectOADTO;
import com.adc.da.oa.vo.OAProjectVO;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.adc.da.budget.constant.ProjectType.BUSINESS_PROJECT;

/**
 * describe:
 *
 * @author 李坤澔
 *     date 2019-05-27
 */
@Service
@Slf4j
public class ProjectOAIOService extends BaseService<Project, String> {

    @Autowired
    private ProjectRepository projectRepository;

    /**
     * es builder init
     *
     * @return
     */
    private BoolQueryBuilder initQueryBuilders() {
        return QueryBuilders
            .boolQuery()
            .must(QueryBuilders.termQuery("projectType", BUSINESS_PROJECT))
            .must(QueryBuilders.existsQuery("budgetDomainId"));
    }

    @Autowired
    BudgetEODao budgetEODao;

    /**
     * 下列项目编号 不存在，过滤
     *
     * @return
     */
    private Set<String> ignoreSet() {
        Set<String> domainIdIgnoreSet = new HashSet<>();

        domainIdIgnoreSet.add("201811250018");
        domainIdIgnoreSet.add("201811250025");
        domainIdIgnoreSet.add("201811250032");
        domainIdIgnoreSet.add("201811250033");
        domainIdIgnoreSet.add("201811250039");
        domainIdIgnoreSet.add("201811250051");
        return domainIdIgnoreSet;
    }

    /**
     * 导入
     *
     * @param is
     * @param params
     * @throws Exception
     */
    public Map<String, String> excelImport(InputStream is, ImportParams params) throws Exception {
        List<ProjectOADTO> sourceDTOList = ExcelImportUtil.importExcel(is, ProjectOADTO.class, params);
        Map<String, String> resultMap = new HashMap<>();


        /*
         * 不能存在的记录 console
         */
        Set<String> domainIdIgnoreSet = ignoreSet();
        Map<String, ProjectOADTO> sourceProjectNOAndProjectNOMap = new HashMap<>();

        String projectNoSource;

        Map<String, ProjectOADTO> sourceNameProjectNOMap = new HashMap<>();

        for (ProjectOADTO eo : sourceDTOList) {

            projectNoSource = eo.getBudgetDomainId();
            if (!domainIdIgnoreSet.contains(projectNoSource)) {

                sourceProjectNOAndProjectNOMap.put(eo.getContractNoAndDomainId(), eo);
                sourceNameProjectNOMap.put(eo.getProjectName() + eo.getBudgetDomainId(), eo);
            }
        }
        Map<String, ProjectOADTO> newProjectList = new HashMap<>(sourceNameProjectNOMap);

        BoolQueryBuilder queryBuilder = initQueryBuilders();
        List<Project> oldProjectList = ((PageImpl<Project>) projectRepository.search(queryBuilder)).getContent();
        List<Project> oldUpdateList = new ArrayList<>();
        String contractNO;

        String contractAndDomainId;
        String projectName;
        String budgetDomainId;
        Date nowTime = new Date();
        //  旧记录
        for (Project eo : oldProjectList) {
            contractNO = eo.getContractNo();
            projectName = eo.getName();
            budgetDomainId = eo.getBudgetDomainId();

            ProjectOADTO dto = sourceNameProjectNOMap.get(projectName + budgetDomainId);
            if (dto == null) {
                log.warn("L179 dto null sourceNameProjectNOMap {}", projectName + budgetDomainId);
                dto = sourceProjectNOAndProjectNOMap.get(contractNO + budgetDomainId);
                if (dto == null) {
                    log.warn("L123 dto null sourceProjectNOAndProjectNOMap {}", contractNO + budgetDomainId);
                    continue;
                }
            }

            eo.setModifyTime(nowTime);
            /* 更新数据 */
            updateOldProject(dto, eo);
            contractAndDomainId = dto.getContractNoAndDomainId();
            newProjectList.remove(contractAndDomainId);
            resultMap.put(contractAndDomainId, "oldData");
            /*
             * 加入更新队列
             */
            oldUpdateList.add(eo);
        }

        if (CollectionUtils.isNotEmpty(oldUpdateList)) {
            projectRepository.save(oldUpdateList);
        } else {
            log.warn("Not Update");
        }

        Set<Map.Entry<String, ProjectOADTO>> entrySet = newProjectList.entrySet();
        int sum = entrySet.size();
        int i = 1;
        //  new projcet
        for (Map.Entry<String, ProjectOADTO> entry : entrySet) {
            log.warn("new Project total :{}, now: {} ", sum, i++);
            ProjectOADTO dto = entry.getValue();
            OAProjectVO vo = new OAProjectVO();

            vo.setContractName(dto.getProjectName());
            vo.setContractNO(dto.getContractNo());
            vo.setContractCompany(dto.getProjectOwner());
            vo.setContractAmount(dto.getContractAmount());
            vo.setContractCreateTime(dto.getProjectStartTime());
            vo.setContractBeginTime(dto.getProjectBeginTime());
            vo.setContractEndTime(dto.getProjectEndTime());
            vo.setLeaderDept(dto.getDeptName());
            vo.setBudgetNO(dto.getBudgetDomainId());
            vo.setType(dto.getType());

            dataSyncProjectService.checkVO(vo);

            resultMap.put(dto.getContractNoAndDomainId(), "newData");

        }

        log.warn("Sync finished {}", oldProjectList.size());

        return resultMap;
    }

    @Autowired
    DataSyncProjectService dataSyncProjectService;

    @Autowired
    DataSyncBaseService baseService;

    /**
     * 更新旧项目信息
     *
     * @param dto
     * @param eo
     */
    private void updateOldProject(ProjectOADTO dto, Project eo) {

        /* 合同编号 */
        eo.setContractNo(dto.getContractNo());
        /*合同金额*/
        eo.setContractAmountStr(dto.getContractAmount());
        eo.setContractAmount(Float.parseFloat(dto.getContractAmount()));
        /* 项目名称*/
        eo.setName(dto.getProjectName());
        /* 客户名称*/
        eo.setProjectOwner(dto.getProjectOwner());

        /* 业务部门*/
        String leaderDept = dto.getDeptName();
        if (StringUtils.isNotEmpty(leaderDept)) {
            String deptId = baseService.getDeptId(leaderDept);
            if (StringUtils.isNotEmpty(deptId)) {
                eo.setDeptId(deptId);
            } else {
                throw new AdcDaBaseException("L210 Not found  deptName ");
            }
        }
        /* 板块 */
        dataSyncProjectService.setBusinessInfo(dto.getType(), eo);

        /* 时间相关 */
        eo.setProjectBeginTime(dto.getProjectBeginTime());
        eo.setProjectStartTime(dto.getProjectStartTime());
        eo.setProjectEndTime(dto.getProjectEndTime());

    }

}
