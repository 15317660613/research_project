package com.adc.da.crm.service;

import com.adc.da.base.page.BasePage;
import com.adc.da.crm.entity.ProjectInfoEO;
import com.adc.da.crm.util.AutoMatchMethodArgumentUtil;
import com.adc.da.form.dao.AdcFormDataDao;
import com.adc.da.form.entity.AdcFormDataEO;
import com.adc.da.util.constant.DeleteFlagEnum;
import com.adc.da.util.utils.DateUtils;
import com.adc.da.util.utils.StringUtils;
import com.adc.da.util.utils.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.adc.da.base.service.BaseService;
import com.adc.da.crm.dao.ProjectClosureApprovalEODao;
import com.adc.da.crm.entity.ProjectClosureApprovalEO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 *
 * <br>
 * <b>功能：</b>PROJECT_CLOSURE_APPROVAL ProjectClosureApprovalEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2018-11-28 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("projectClosureApprovalEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class ProjectClosureApprovalEOService extends BaseService<ProjectClosureApprovalEO, Void> {

    private static final Logger logger = LoggerFactory.getLogger(ProjectClosureApprovalEOService.class);

    @Autowired
    private ProjectClosureApprovalEODao dao;
    @Autowired
    private AdcFormDataDao formDataDao;

    public ProjectClosureApprovalEODao getDao() {
        return dao;
    }
    @Transactional(
            rollbackFor = {Exception.class}
    )
    public AdcFormDataEO save(AdcFormDataEO adcFormDataEO) {
        //保存到ct_form
        //1.保存ct_data
        adcFormDataEO.setId(UUID.randomUUID10());
        adcFormDataEO.setDelFlag(DeleteFlagEnum.NORMAL.getValue());
        adcFormDataEO.setCreateTime(DateUtils.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
        this.formDataDao.insert(adcFormDataEO);
        //2.保存关联 ct_form_data
        this.formDataDao.deleteFormAndDataById(adcFormDataEO.getId());
        this.formDataDao.saveFormAndData(adcFormDataEO.getFormId(), adcFormDataEO.getId());
        //保存解析后的数据
        ProjectClosureApprovalEO projectClosureApprovalEO = (ProjectClosureApprovalEO) AutoMatchMethodArgumentUtil.json2entity(adcFormDataEO, ProjectClosureApprovalEO.class);
        projectClosureApprovalEO.setId(adcFormDataEO.getId());
        projectClosureApprovalEO.setCreatedTime(new Date());
        projectClosureApprovalEO.setCreatedUser(adcFormDataEO.getCreateName());
        projectClosureApprovalEO.setDelFlag(String.valueOf(DeleteFlagEnum.NORMAL.getValue()));//是否删除 （0- 否 1-是）
        this.dao.insertSelective(projectClosureApprovalEO);
        return adcFormDataEO;
    }

    public int update(String userId, AdcFormDataEO adcFormDataEO) {
        //更新ct_form
        this.formDataDao.updateByPrimaryKeySelective(adcFormDataEO);

        //更新解析后的数据
        ProjectClosureApprovalEO projectClosureApprovalEO = (ProjectClosureApprovalEO) AutoMatchMethodArgumentUtil.json2entity(adcFormDataEO, ProjectClosureApprovalEO.class);
        projectClosureApprovalEO.setId(adcFormDataEO.getId());
        projectClosureApprovalEO.setModifiedTime(new Date());
        projectClosureApprovalEO.setModifiedUser(userId);
        return this.dao.updateByPrimaryKeySelective(projectClosureApprovalEO);
    }

    public int delete(String id){
        List<String> ids = new ArrayList<>(1);
        ids.add(id);
        this.formDataDao.deleteLogicInBatch(ids);
        return this.dao.updateByPrimaryKeyAndDelFlag(id, 1);
    }

    public List<ProjectClosureApprovalEO> queryCRMListByPage(BasePage page) throws Exception {
        Integer rowCount = this.queryByCount(page);
        page.getPager().setRowCount(rowCount);
        return this.dao.queryCRMListByPage(page);
    }
}
