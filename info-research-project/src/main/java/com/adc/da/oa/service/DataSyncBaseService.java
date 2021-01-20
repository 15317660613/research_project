package com.adc.da.oa.service;

import com.adc.da.budget.dao.UserEPDao;
import com.adc.da.budget.entity.BudgetEO;
import com.adc.da.budget.entity.Business;
import com.adc.da.budget.entity.Project;
import com.adc.da.budget.page.BudgetEOPage;
import com.adc.da.budget.repository.BusinessRepository;
import com.adc.da.budget.repository.ProjectRepository;
import com.adc.da.budget.service.BudgetEOService;
import com.adc.da.budget.utils.CommonUtil;
import com.adc.da.sys.dao.OrgEODao;
import com.adc.da.sys.entity.OrgEO;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 数据同步-业务
 */
@Service
@Slf4j
public class DataSyncBaseService {

    @Autowired
    private OrgEODao orgEODao;

    /**
     * 获取部门id
     *
     * @param deptName
     * @return
     */
    public String getDeptId(String deptName) {

        List<OrgEO> orgEOList = orgEODao.getOrgEOByOrgNameWithDeleted(deptName);
        if (CollectionUtils.isNotEmpty(orgEOList)) {
            return orgEOList.get(0).getId();
        } else {
            throw new AdcDaBaseException("deptName not found " + deptName);
        }
    }

    @Autowired
    private UserEPDao userEPDao;

    /**
     * 获取用户id
     *
     * @param userName
     * @param deptId
     * @return
     */
    public String getUserId(String userName, String deptId) {
        List<String> userIdList = userEPDao.queryUserIdByNameAndOrgId(userName, deptId);
        if (CollectionUtils.isNotEmpty(userIdList)) {
            return userIdList.get(0);
        } else {
            List<String> userIdListAll = userEPDao.queryUserIdByNameAndOrgId(userName, "MH8JQV5TSN");
            if (CollectionUtils.isNotEmpty(userIdListAll)) {
                return userIdListAll.get(0);
            } else {
                throw new AdcDaBaseException("User not found : " + userName);
            }
        }
    }

    @Autowired
    private ProjectRepository projectRepository;

    /**
     * 根据合同编号获取项目信息
     *
     * @param contractNO
     * @return
     */
    public Project getProjectByContractNO(String contractNO, String budgetDomainId) {
        List<Project> projectList = ((PageImpl<Project>) projectRepository
            .search(CommonUtil.queryByContractNoAndBudgetDomainId(contractNO, budgetDomainId)))
            .getContent();
        if (CollectionUtils.isEmpty(projectList)) {
            throw new AdcDaBaseException("project contractNO error");
        }
        return projectList.get(0);
    }

    @Autowired
    private BusinessRepository businessRepository;

    /**
     * 获取所属业务
     *
     * @param type
     * @return
     */
    public Business getBusinessName(String type) {
        char[] typeChar = type.toCharArray();

        String name = String.valueOf(typeChar, 0, 2);

        List<Business> business = businessRepository.findByNameLike(name);
        if (CollectionUtils.isNotEmpty(business)) {
            return business.get(0);
        }
        log.warn("type not found:  {}", type);
        throw new AdcDaBaseException("业务类型 type 错误" + type);
    }

    @Autowired
    private BudgetEOService budgetEOService;

    /**
     * 获取业务信息
     *
     * @param domainId
     * @return
     * @throws Exception
     */
    public BudgetEO getBudget(String domainId) throws Exception {
        if (StringUtils.isEmpty(domainId)) {
            return null;
        }
        BudgetEOPage queryPage = new BudgetEOPage();
        queryPage.setDomainId(domainId);
        List<BudgetEO> budgetEOFormList = budgetEOService.queryByList(queryPage);

        if (CollectionUtils.isNotEmpty(budgetEOFormList)) {
            return budgetEOFormList.get(0);
        } else {
            return null;
        }
    }
}
