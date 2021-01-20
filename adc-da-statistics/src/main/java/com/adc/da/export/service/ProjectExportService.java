package com.adc.da.export.service;

import com.adc.da.base.entity.TreeEntity;
import com.adc.da.budget.dao.BudgetEODao;
import com.adc.da.budget.dao.UserEPDao;
import com.adc.da.budget.entity.BudgetEO;
import com.adc.da.budget.entity.Project;
import com.adc.da.budget.entity.UserEPEntity;
import com.adc.da.budget.page.BudgetEOPage;
import com.adc.da.budget.repository.ProjectRepository;
import com.adc.da.budget.service.ProjectService;
import com.adc.da.budget.utils.CommonUtil;
import com.adc.da.excel.poi.excel.ExcelExportUtil;
import com.adc.da.excel.poi.excel.entity.ExportParams;
import com.adc.da.export.dto.ProjectExportDTO;
import com.adc.da.statistics.dao.ProjectWorktimeEODao;
import com.adc.da.statistics.entity.ProjectWorktimeEO;
import com.adc.da.statistics.page.ProjectWorktimeEOPage;
import com.adc.da.sys.dao.OrgEODao;
import com.adc.da.sys.entity.OrgEO;
import com.adc.da.util.utils.DateUtils;
import com.adc.da.util.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * describe:
 * todo 项目导出追加 项目进度   已完成里程碑/当前里程碑
 *
 * @author 李坤澔
 *     date 2019-09-27
 */
@Service
@Slf4j
public class ProjectExportService {
    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private ProjectWorktimeEODao workTimeDao;

    /**
     * 导出
     *
     * @param params
     * @return
     * @throws Exception
     */
    public Workbook excelExport(ExportParams params, Long beginLong, Long endLong) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.mustNot(QueryBuilders.termQuery("delFlag", true));
        queryBuilder.must(
            QueryBuilders.rangeQuery("createTime")
                         .from(beginLong)
                         .to(endLong)
                         //包含下界
                         .includeLower(true)
                         //不包含上界
                         .includeUpper(false));

        List<Project> sourceList = ((PageImpl<Project>) projectRepository.search(queryBuilder)).getContent();

        Set<String> ids = new HashSet<>();
        Set<String> budgetIds = new HashSet<>();
        Set<String> userIds = new HashSet<>();
        for (Project source : sourceList) {
            ids.add(source.getId());
            budgetIds.add(source.getBudgetId());
            userIds.add(source.getCreateUserId());
        }

        Map<String, String> workTimeMap = initWorkTime(ids);
        Map<String, String> budgetNameMap = initBudgetNameMap(budgetIds);
        Map<String, String> userNameMap = getUserNameMap(userIds);
        Map<String, String> orgIdNameMap = getOrgIdNameMap();
        ProjectExportDTO result;
        ArrayList<ProjectExportDTO> resultList = new ArrayList<>(sourceList.size());
        String budgetName;
        int i = sourceList.size();
        for (Project source : sourceList) {
            log.warn("Remaining ：{} ", i--);

            String type;
            String workTime = workTimeMap.get(source.getId());
            switch (source.getProjectType()) {
                case 0:
                    type = "经营类";
                    break;
                case 1:
                    type = "日常事务类";
                    break;
                case 2:
                    type = "科研类";
                    break;
                default:
                    type = "未定义";
            }

            budgetName = budgetNameMap.get(source.getBudgetId());
            if (budgetName == null) {
                continue;
            }
            String amount = source.getContractAmountStr();
            if (StringUtils.isEmpty(amount)) {
                amount = Float.toString(source.getContractAmount());
            }
            result = ProjectExportDTO.builder()
                                     .contractNo(source.getContractNo())
                                     .budget(budgetName)
                                     .createUserName(userNameMap.get(source.getCreateUserId()))
                                     .finishedStatus(source.getFinishedStatus())
                                     .name(source.getName())
                                     .personInput(workTime)
                                     .projectLeader(source.getProjectLeader())
                                     .projectMemberNames(source.getProjectMemberNames())
                                     .projectStartTimeStr(DateUtils
                                         .dateToString(source.getProjectBeginTime(), "yyyy-MM-dd"))
                                     .startTime(DateUtils.dateToString(source.getStartTime(), "yyyy-MM-dd"))
                                     .type(type)
                                     .depName(orgIdNameMap.get(source.getDeptId()))
                                     .amount(amount)
                                     .build();

            resultList.add(result);

            //中文逗号切英文
            String names = StringUtils.join(source.getMemberNames(), ",");
            source.setProjectMemberNames(names);
            source.setBusiness(projectService.getBusinessName(source.getBusinessId()));
        }
        return ExcelExportUtil.exportExcel(params, ProjectExportDTO.class, resultList);
    }

    /**
     * 初始化工时Map
     *
     * @param idSet
     * @return
     */
    private Map<String, String> initWorkTime(Set<String> idSet) {
        idSet.removeAll(Collections.singleton(null));

        List<String> ids = new ArrayList<>(idSet);
        List<List<String>> resultList = CommonUtil.splitList(new ArrayList<>(ids), 500);
        List<ProjectWorktimeEO> workTimeEOList = new ArrayList<>(ids.size());

        ProjectWorktimeEOPage queryPage = new ProjectWorktimeEOPage();
        for (List<String> queryList : resultList) {
            queryPage.setProjectIds(queryList);
            workTimeEOList.addAll(workTimeDao.querySumWorkTime(queryPage));
        }

        Map<String, String> workTimeMap = new HashMap<>(workTimeEOList.size());

        for (ProjectWorktimeEO eo : workTimeEOList) {
            workTimeMap.put(eo.getProjectId(), eo.getWorktime().toString());
        }

        return workTimeMap;
    }

    @Autowired
    private BudgetEODao budgetEODao;

    /**
     * 初始化项目进度 map
     *
     * @return
     */
    private Map<String, String> initProjectProgressMap() {

        return new HashMap<>();
    }

    /**
     * 初始化BudgetMap
     *
     * @param idSet
     * @return
     */
    private Map<String, String> initBudgetNameMap(Set<String> idSet) {

        /*
         * 去除null元素
         */
        idSet.removeAll(Collections.singleton(null));

        List<String> ids = new ArrayList<>(idSet);
        List<BudgetEO> list = new ArrayList<>();

        List<List<String>> resultList = CommonUtil.splitList(new ArrayList<>(ids), 500);

        BudgetEOPage queryPage = new BudgetEOPage();
        for (List<String> queryList : resultList) {
            queryPage.setBusinessIds(new HashSet<>(queryList));
            list.addAll(budgetEODao.queryByList(queryPage));

        }

        Map<String, String> resultMap = new HashMap<>(list.size());
        for (BudgetEO eo : list) {
            resultMap.put(eo.getId(), eo.getProjectName());
        }
        return resultMap;
    }

    @Autowired
    private UserEPDao userEODao;

    @Autowired
    private OrgEODao orgEODao;

    /**
     * 初始化部门 id ， name map
     *
     * @return
     */
    private Map<String, String> getOrgIdNameMap() {
        List<OrgEO> orgEOList = orgEODao.queryOrgAll();
        return orgEOList.stream().collect(Collectors
            .toMap(TreeEntity::getId, TreeEntity::getName, (a, b) -> b));
    }

    /**
     * 初始化UserMap
     */
    private Map<String, String> getUserNameMap(Set<String> idSet) {
        idSet.removeAll(Collections.singleton(null));
        List<String> ids = new ArrayList<>(idSet);

        List<List<String>> resultList = CommonUtil.splitList(new ArrayList<>(ids), 500);
        List<UserEPEntity> list = new ArrayList<>(ids.size());
        for (List<String> queryList : resultList) {
            list.addAll(userEODao.queryUserIdAndNameByIdList(queryList));
        }

        Map<String, String> resultMap = new HashMap<>(list.size());
        for (UserEPEntity eo : list) {
            resultMap.put(eo.getUsid(), eo.getUsname());
        }

        return resultMap;
    }
}
