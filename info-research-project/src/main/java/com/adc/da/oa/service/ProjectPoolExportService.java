package com.adc.da.oa.service;

import com.adc.da.base.entity.TreeEntity;
import com.adc.da.budget.entity.Project;
import com.adc.da.budget.repository.ProjectRepository;
import com.adc.da.excel.poi.excel.ExcelExportUtil;
import com.adc.da.excel.poi.excel.entity.ExportParams;
import com.adc.da.oa.entity.ProjectPoolDTO;
import com.adc.da.sys.dao.OrgEODao;
import com.adc.da.sys.entity.OrgEO;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.adc.da.budget.constant.ProjectSearchField.DEL_FLAG;
import static com.adc.da.budget.constant.ProjectSearchField.PROJECT_LEADER_ID;

/**
 * describe:
 *
 * @author 李坤澔
 *     date 2019-09-27
 */
@Service
@Slf4j
public class ProjectPoolExportService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private OrgEODao orgEODao;

    /**
     * 导出
     *
     * @param params
     * @return
     * @throws Exception
     */
    public Workbook excelExport(ExportParams params) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.mustNot(QueryBuilders.termQuery(DEL_FLAG, true));
        queryBuilder.mustNot(QueryBuilders.existsQuery(PROJECT_LEADER_ID));
        List<Project> sourceList = ((PageImpl<Project>) projectRepository.search(queryBuilder)).getContent();

        List<OrgEO> orgEOList = orgEODao.queryOrgAll();
        Map<String, String> orgIdNameMap = orgEOList.stream().collect(Collectors
            .toMap(TreeEntity::getId, TreeEntity::getName, (a, b) -> b));

        ProjectPoolDTO result;
        ArrayList<ProjectPoolDTO> resultList = new ArrayList<>(sourceList.size());

        int i = sourceList.size();
        for (Project source : sourceList) {
            log.warn("Remaining ：{} ", i--);
            String amount =
                source.getContractAmountStr() == null ? String.valueOf(source.getContractAmount())
                                                      : source.getContractAmountStr();
            result = ProjectPoolDTO.builder()
                                   .id(source.getId())
                                   .amount(amount)
                                   .contractName(source.getName())
                                   .contractNo(source.getContractNo())
                                   .contractCompany(source.getProjectOwner())
                                   .deptName(orgIdNameMap.get(source.getDeptId()))
                                   .projectBeginDate(source.getProjectBeginTime())
                                   .build();

            resultList.add(result);

        }
        return ExcelExportUtil.exportExcel(params, ProjectPoolDTO.class, resultList);
    }

}
