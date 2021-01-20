package com.adc.da.oa.service;

import com.adc.da.budget.entity.Project;
import com.adc.da.budget.repository.ProjectRepository;
import com.adc.da.budget.utils.CommonUtil;
import com.adc.da.oa.vo.ContractInvoiceListVO;
import com.adc.da.oa.vo.ContractInvoiceVO;
import com.adc.da.processform.dao.ProjectContractInvoiceEODao;
import com.adc.da.processform.entity.ProjectContractInvoiceEO;
import com.adc.da.sys.dao.OrgEODao;
import com.adc.da.sys.dao.UserEODao;
import com.adc.da.sys.entity.OrgEO;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.DateUtils;
import com.adc.da.util.utils.StringUtils;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 数据同步-开票信息
 */
@Service
public class DataSyncContractService {

    @Autowired
    private DataSyncBaseService baseService;

    @Autowired
    private ProjectContractInvoiceEODao projectContractInvoiceEODao;

    @Autowired
    private UserEODao userEODao;

    @Autowired
    private OrgEODao orgEODao;

    private static final Logger logger = LoggerFactory.getLogger(DataSyncContractService.class);



    public List<ProjectContractInvoiceEO> save(ContractInvoiceListVO vo) {
        ProjectContractInvoiceEO baseEO = new ProjectContractInvoiceEO();
        //若有合同号和项目编号则按正常逻辑处理
        if(StringUtils.isNotEmpty(vo.getContractNO())&&StringUtils.isNotEmpty(vo.getProjectNO())){
            Project project = baseService.getProjectByContractNO(vo.getContractNO(), vo.getProjectNO());
            baseEO.setContractId(vo.getContractNO());
            baseEO.setProjectId(project.getId());
        }
        List<ProjectContractInvoiceEO> saveList = new ArrayList<>();
        for (ContractInvoiceVO eo : vo.getContractInvoiceVOList()) {
            ProjectContractInvoiceEO contractInvoiceEO = new ProjectContractInvoiceEO();
            try {
                /*复制基础合同*/
                BeanUtils.copyProperties(baseEO, contractInvoiceEO);
                /*只更新开票金额不为空的数据*/
                if (null != eo.getInvoiceAmount() && StringUtils.isNotEmpty(eo.getId())) {
                    contractInvoiceEO.setInvoiceAmount(eo.getInvoiceAmount());
                    contractInvoiceEO.setInvoiceDate(eo.getInvoiceDate());
                    contractInvoiceEO.setRecieveMoneyDate(eo.getRecieveMoneyDate());
                    contractInvoiceEO.setId(eo.getId());
                    contractInvoiceEO.setExt01(eo.getExt01());
                    contractInvoiceEO.setExt02(eo.getExt02());
                    contractInvoiceEO.setExt03(eo.getExt03());
                    //入库时间
                    contractInvoiceEO.setExt04(DateUtils.dateToString(new Date(), DateUtils.YYYY_MM_DD_HH_MM_SS_EN));
                    contractInvoiceEO.setHasContractFlag(eo.getHasContractFlag());
                    contractInvoiceEO.setInvoiceNo(eo.getInvoiceNo());
                    contractInvoiceEO.setContractApplyDate(eo.getContractApplyDate());
                    if (StringUtils.isNotEmpty(eo.getAreaManagerCode())) {
                        List<UserEO> userEOList = userEODao.selectListByUserCode(eo.getAreaManagerCode());
                        if (CollectionUtils.isNotEmpty(userEOList)) {
                            contractInvoiceEO.setAreaManagerId(userEOList.get(0).getUsid());
                            contractInvoiceEO.setAreaManagerName(userEOList.get(0).getUsname());
                        }
                    }
                    contractInvoiceEO.setContractFiledFlag(eo.getContractFiledFlag());
                    contractInvoiceEO.setContractName(eo.getContractName());
                    contractInvoiceEO.setEstimateReturnDate(eo.getEstimateReturnDate());
                    contractInvoiceEO.setContainsInstrumentationFlag(eo.getContainsInstrumentationFlag());
                    contractInvoiceEO.setOutStorehouseNo(eo.getOutStorehouseNo());
                    if (StringUtils.isNotEmpty(eo.getBelongDeptName())) {
                        List<OrgEO> orgEOList = orgEODao.getOrgEOByOrgName(eo.getBelongDeptName());
                        if (CollectionUtils.isNotEmpty(orgEOList)) {
                            contractInvoiceEO.setBelongDeptId(orgEOList.get(0).getId());
                            contractInvoiceEO.setBelongDeptName(orgEOList.get(0).getName());
                        }
                    }
                    contractInvoiceEO.setContractInvoice(eo.getContractInvoice());
                    contractInvoiceEO.setRemark(eo.getRemark());
                    contractInvoiceEO.setInvoiceNumber(eo.getInvoiceNumber());
                    contractInvoiceEO.setBusinessOpportunityNo(eo.getBusinessOpportunityNo());
                    contractInvoiceEO.setBusinessOpportunityName(eo.getBusinessOpportunityName());
                    contractInvoiceEO.setProjectNo(eo.getProjectNo());
                    contractInvoiceEO.setProjectName(eo.getProjectName());
                    if (StringUtils.isNotEmpty(eo.getBusinessDeptName())) {
                        List<OrgEO> bizOrgEOList = orgEODao.getOrgEOByOrgName(eo.getBusinessDeptName());
                        if (CollectionUtils.isNotEmpty(bizOrgEOList)) {
                            contractInvoiceEO.setBusinessDeptId(bizOrgEOList.get(0).getId());
                            contractInvoiceEO.setBusinessDeptName(bizOrgEOList.get(0).getName());
                        }
                    }
                    if (StringUtils.isNotEmpty(eo.getProjectManagerCode())) {
                        List<UserEO> projectUserEOList = userEODao.selectListByUserCode(eo.getProjectManagerCode());
                        if (CollectionUtils.isNotEmpty(projectUserEOList)) {
                            contractInvoiceEO.setProjectManagerId(projectUserEOList.get(0).getUsid());
                            contractInvoiceEO.setProjectManagerName(projectUserEOList.get(0).getUsname());
                        }
                    }
                    contractInvoiceEO.setActualInvoiceAmount(eo.getActualInvoiceAmount());
                    contractInvoiceEO.setActualInvoiceDate(eo.getActualInvoiceDate());
                    contractInvoiceEO.setChangeInvoiceDate(eo.getChangeInvoiceDate());
                    contractInvoiceEO.setChangeInvoiceAmount(eo.getChangeInvoiceAmount());
                    contractInvoiceEO.setBackInvoiceDate(eo.getBackInvoiceDate());
                    contractInvoiceEO.setBackInvoiceAmount(eo.getBackInvoiceAmount());
                    contractInvoiceEO.setOriginInvoiceAmount(eo.getOriginInvoiceAmount());
                    contractInvoiceEO.setAdvanceInvoiceFlag(eo.getAdvanceInvoiceFlag());
                    contractInvoiceEO.setAdvanceInvoiceReason(eo.getAdvanceInvoiceReason());
                    contractInvoiceEO.setInvoiceType(eo.getInvoiceType());
                }
            }catch (Exception  e){
                logger.error("合同开票数据异常" + new Gson().toJson(contractInvoiceEO),e);
            }
                saveList.add(contractInvoiceEO);
        }
        /*存合同信息  */
        if (CollectionUtils.isNotEmpty(saveList)) {
            projectContractInvoiceEODao.insertSelectiveAll(saveList);
        }
        return saveList;
    }


    /**
     * 删除
     *
     * @param projectId
     * @return
     * @throws Exception
     */
    public int delete(List<String> projectId) {
        logger.info("oa同步数据（delete）转为json====" + new Gson().toJson(projectId));
        return projectContractInvoiceEODao.deleteByProjectId(projectId);
    }

    @Autowired
    ProjectRepository projectRepository;

    /**
     * 新建-更新-二合一，先删除
     *
     * @param vo
     * @return
     */
    public List<ProjectContractInvoiceEO> update(ContractInvoiceListVO vo) {

        logger.info("oa同步数据数据转为json====" + new Gson().toJson(vo));
        //若有合同号和项目编号则按正常逻辑处理
//        if(StringUtils.isNotEmpty(vo.getContractNO())&&StringUtils.isNotEmpty(vo.getProjectNO())){
//            List<Project> projectList = ((PageImpl<Project>) projectRepository
//                .search(CommonUtil.queryByContractNoAndBudgetDomainId(vo.getContractNO(), vo.getProjectNO())))
//                .getContent();
//
//            List<String> idList = new ArrayList<>(projectList.size());
//            for (Project eo : projectList) {
//                idList.add(eo.getId());
//            }
//            if (CollectionUtils.isNotEmpty(idList)) {
//                delete(idList);
//            }
//
//        } else {
//            if (CollectionUtils.isNotEmpty(vo.getContractInvoiceVOList())) {
//                Set<String> contractInvoiceIdSet = new HashSet<>();
//                for (ContractInvoiceVO eo : vo.getContractInvoiceVOList()) {
//                    contractInvoiceIdSet.add(eo.getId());
//                }
//                deleteByContractInvoiceId(new ArrayList<>(contractInvoiceIdSet));
//            }
//        }
        if (CollectionUtils.isNotEmpty(vo.getContractInvoiceVOList())) {
            Set<String> contractInvoiceIdSet = new HashSet<>();
            for (ContractInvoiceVO eo : vo.getContractInvoiceVOList()) {
                contractInvoiceIdSet.add(eo.getId());
            }
            if (CollectionUtils.isNotEmpty(contractInvoiceIdSet)) {
                deleteByContractInvoiceId(new ArrayList<>(contractInvoiceIdSet));
            }
        }
        return save(vo);
    }

    public int deleteByContractInvoiceId(List<String> ids) {
        logger.info("oa同步数据（deleteByContractInvoiceId）转为json====" + new Gson().toJson(ids));
        return projectContractInvoiceEODao.deleteByContractInvoiceId(ids);
    }
}
