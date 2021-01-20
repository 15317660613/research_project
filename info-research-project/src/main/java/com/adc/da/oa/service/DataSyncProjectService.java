package com.adc.da.oa.service;

import com.adc.da.budget.entity.BudgetEO;
import com.adc.da.budget.entity.Business;
import com.adc.da.budget.entity.Project;
import com.adc.da.budget.enums.ProjectStatusEnums;
import com.adc.da.budget.repository.ProjectRepository;
import com.adc.da.budget.service.ProjectService;
import com.adc.da.budget.utils.CommonUtil;
import com.adc.da.oa.vo.OAProjectVO;
import com.adc.da.progress.service.ProjectRateEOService;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.sys.service.UserEOService;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.StringUtils;
import com.adc.da.util.utils.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Set;

import static com.adc.da.budget.constant.ProjectType.BUSINESS_PROJECT;

/**
 * 数据同步-项目
 */
@Service
@Slf4j
public class DataSyncProjectService {
    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectRateEOService projectRateEOService;

    @Autowired
    private DataSyncBaseService baseService;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserEOService userEOService;

    /**
     * 设置业务信息
     *
     * @param getBudgetNO
     * @param resultEO
     * @throws Exception
     */
    private void setBudgetInfo(String getBudgetNO, Project resultEO) throws Exception {
        BudgetEO budgetEO = baseService.getBudget(getBudgetNO);
        if (budgetEO != null) {
            resultEO.setBudgetId(budgetEO.getId());
            resultEO.setBudget(budgetEO.getProjectName());
            resultEO.setPm(budgetEO.getPm());
            resultEO.setProjectTeam(budgetEO.getProjectTeam());
            resultEO.setBusinessCreateUserId(budgetEO.getCreateUserId());
            resultEO.setBudgetDomainId(budgetEO.getDomainId());
        } else {
            log.warn("budgetId not found {} ", getBudgetNO);
            throw new AdcDaBaseException("业务编号错误（OA项目）");
        }
    }

    /**
     * 新增
     *
     * @param vo
     * @return
     * @throws Exception
     */
    public Project save(OAProjectVO vo, Project updateEO) throws Exception {

        log.warn("OA project sync : {} ", vo.toString());
        if (StringUtils.isEmpty(vo.getLeaderDept())) {
            log.error("dept is null");
            throw new AdcDaBaseException("部门信息为空");
        }
        log.warn("OA dept is {}, len is {}", vo.getLeaderDept(), vo.getLeaderDept().length());

        String projectId = UUID.randomUUID10();
        Project resultEO;
        /*
         * 用于区分是新增还是更新
         */
        if (null == updateEO) {
            resultEO = new Project();
            resultEO.setId(projectId);
            resultEO.setProjectType(BUSINESS_PROJECT);
        } else {
            resultEO = updateEO;
        }

        /*
         * 设置合同创建 开始 结束 时间
         */
        resultEO.setProjectBeginTime(vo.getContractBeginTime());
        resultEO.setProjectEndTime(vo.getContractEndTime());
        resultEO.setStartTime(vo.getContractCreateTime());
        resultEO.setProjectStartTime(vo.getContractCreateTime());

        resultEO.setProjectYear(vo.getProjectYear()); //存同步的年份
        resultEO.setProjectMonth(vo.getProjectMonth()); // 存同步的月份

        resultEO.setProvince(vo.getProvince());
        resultEO.setPartyB(vo.getPartyB());

        /*
         * 设置项目名称与编号
         */
        resultEO.setName(vo.getContractName());
        resultEO.setContractNo(vo.getContractNO());

        //业务类型
        setBusinessInfo(vo.getType(), resultEO);

        //业务id
        setBudgetInfo(vo.getBudgetNO(), resultEO);

        //业务部门
        String leaderDept = vo.getLeaderDept();
        if (StringUtils.isNotEmpty(leaderDept)) {
            String deptId = baseService.getDeptId(leaderDept);
            if (StringUtils.isNotEmpty(deptId)) {
                resultEO.setDeptId(deptId);
            }
        }

        String amount = vo.getContractAmount();
        if (StringUtils.isNotEmpty(amount)) {
            resultEO.setContractAmountStr(amount);
            resultEO.setContractAmount(Float.parseFloat(amount));
        }

        //合同企业名称  业务方
        resultEO.setProjectOwner(vo.getContractCompany());
        if ("作废".equals(vo.getState())) {
            resultEO.setDelFlag(true);
        } else {
            resultEO.setDelFlag(null);
        }

        if (StringUtils.isNotEmpty(vo.getBusinessManagerId())) {
            List<UserEO> userEOList = userEOService.selectListByUserCode(vo.getBusinessManagerId());
            if (CollectionUtils.isNotEmpty(userEOList)) {
                resultEO.setBusinessManagerId(userEOList.get(0).getUsid());
                resultEO.setBusinessManagerName(userEOList.get(0).getUsname());
            }
        }

        /*
         * 存入ES
         */
        this.insertOrUpdate(resultEO);



        /*
         * 初始化项目进度信息
         */
        projectRateEOService.initProjectProgressData(projectId, BUSINESS_PROJECT.toString());

        return (resultEO);

    }

    /**
     * 更新修改二合一
     *
     * @param vo
     * @return
     * @throws Exception
     */
    public Project checkVO(OAProjectVO vo) throws Exception {
        List<Project> projectList =
            ((PageImpl<Project>) projectRepository
                .search(CommonUtil.queryByContractNoAndBudgetDomainId(vo.getContractNO(), vo.getBudgetNO())))
                .getContent();
        if (CollectionUtils.isEmpty(projectList)) {
            /*
             * 新增
             */
            return save(vo, null);
        } else {
            /*
             * 更新
             */
            return save(vo, projectList.get(0));
        }
    }

    /**
     * 根据板块名，获取板块相关信息
     *
     * @param type
     * @param project
     */
    public void setBusinessInfo(String type, Project project) {
        Business business = baseService.getBusinessName(type);
        project.setBusinessId(business.getId());
        project.setBusiness(business.getName());
    }

    /**
     * 存储es
     *
     * @param projectEO
     * @return
     */
    private void insertOrUpdate(Project projectEO) {
        if (projectEO == null || StringUtils.isEmpty(projectEO.getId())) {
            throw new AdcDaBaseException("项目信息不全");
        }

        if (projectEO.getCreateTime() == null) {
            projectEO.setCreateTime(new Date());
            projectEO.setFinishedStatus(ProjectStatusEnums.EXECUTE.getStatus());
        }
        projectEO.setModifyTime(new Date());

        //新增项目时，添加项目和人的关联关系
        projectRepository.save(projectEO);
    }

    /**
     * 删除
     *
     * @param ids
     * @return
     */
    public void deleteBudgetIdByDomainId(Set<String> ids) throws Exception {
        List<Project> list = projectRepository.findByContractNoInAndDelFlagNot(ids, Boolean.TRUE);

        StringBuilder builder = new StringBuilder();
        for (Project eo : list) {
            builder.append(eo.getId()).append(",");
        }
        if (StringUtils.isNotEmpty(builder)) {
            projectService.deleteBatch(builder.substring(0, builder.length() - 1), false);
        }
    }

    /**
     * 删除 - 结合 项目编号与合同编号进行删除
     *
     * @param projectNO  oa项目编号
     * @param contractNO oa合同编号
     * @return
     */
    public void deleteBudgetIdByDomainId(String contractNO, String projectNO) throws Exception {
        List<Project> list = ((PageImpl<Project>) projectRepository
            .search(CommonUtil.queryByContractNoAndBudgetDomainId(contractNO, projectNO)))
            .getContent();
        StringBuilder builder = new StringBuilder();
        for (Project eo : list) {
            builder.append(eo.getId()).append(",");
        }
        if (StringUtils.isNotEmpty(builder)) {
            projectService.deleteBatch(builder.substring(0, builder.length() - 1), false);
        }
    }
}
