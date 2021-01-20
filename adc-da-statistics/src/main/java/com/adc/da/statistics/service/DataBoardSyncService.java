package com.adc.da.statistics.service;

import com.adc.da.budget.entity.Project;
import com.adc.da.budget.repository.ProjectRepository;
import com.adc.da.statistics.dao.ContractAmountEODao;
import com.adc.da.statistics.entity.ContractAmountEO;
import com.adc.da.util.utils.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.adc.da.budget.constant.ProjectSearchField.CONTRACT_AMOUNT;
import static com.adc.da.budget.constant.ProjectSearchField.PROJECT_TYPE;
import static com.adc.da.budget.constant.ProjectType.BUSINESS_PROJECT;
import static com.adc.da.budget.utils.CommonUtil.splitList;

/**
 * <br>
 * <b>功能：</b>ST_BUSINESS_WORKTIME BusinessWorktimeEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-06-18 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("dataBoardSyncService")
@Transactional(value = "transactionManager", propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class DataBoardSyncService {

    @Autowired
    private ContractAmountEODao contractAmountEODao;

    @Autowired
    private ProjectRepository projectRepository;

    /**
     * 初始化查询条件，筛选合同金额大于10的
     */
    private BoolQueryBuilder initESBuilder() {
        return QueryBuilders.boolQuery()
                            .must(QueryBuilders.termQuery(PROJECT_TYPE, BUSINESS_PROJECT))
                            .must(
                                QueryBuilders.rangeQuery(CONTRACT_AMOUNT)
                                             .from(10.0)
                                             //包含下界
                                             .includeLower(false)
                            );
    }

    /**
     * 同步合同金额到oracle
     *
     * @param saveFlag
     * @return
     */
    public void syncDataFromES(boolean saveFlag) {
        List<Project> sourceList = ((PageImpl<Project>) projectRepository.search(initESBuilder())).getContent();
        List<ContractAmountEO> saveList = new ArrayList<>(sourceList.size());
        sourceList.forEach(eo -> {
            ContractAmountEO.ContractAmountEOBuilder contractAmountEOBuilder
                = ContractAmountEO.builder()
                                  .projectId(eo.getId())
                                  .businessId(eo.getBudgetId())
                                  .contractNo(eo.getContractNo())
                                  .createTime(eo.getStartTime())
                                  .deptId(eo.getDeptId())
                                  .projectBeginTime(eo.getProjectBeginTime())
                                  .extInfo02(eo.getName())
                                  .extInfo03(eo.getProjectOwner());
            if (StringUtils.isNotEmpty(eo.getContractAmountStr())) {
                contractAmountEOBuilder
                    .contractAmount(Double.valueOf(eo.getContractAmountStr()));
            } else {
                contractAmountEOBuilder
                    .contractAmount((double) eo.getContractAmount());
            }
            if (null != eo.getDelFlag() && eo.getDelFlag()) {
                contractAmountEOBuilder.extInfo01("1");
            }
            saveList.add(contractAmountEOBuilder.build());

        });

        if (saveFlag) {
            contractAmountEODao.deleteAll();
            List<List<ContractAmountEO>> saveL = splitList(saveList, 500);
            saveL.forEach(contractAmountEODao::insertSelectiveAll);
        }
    }

}
