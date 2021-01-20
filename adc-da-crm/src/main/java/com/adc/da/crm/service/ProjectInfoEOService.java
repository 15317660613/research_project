package com.adc.da.crm.service;

import com.adc.da.base.entity.BaseEntity;
import com.adc.da.base.page.BasePage;
import com.adc.da.crm.util.AutoMatchMethodArgumentUtil;
import com.adc.da.form.dao.AdcFormDataDao;
import com.adc.da.form.entity.AdcFormDataEO;
import com.adc.da.util.constant.DeleteFlagEnum;
import com.adc.da.util.utils.DateUtils;
import com.adc.da.util.utils.StringUtils;
import com.adc.da.util.utils.UUID;
import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.adc.da.base.service.BaseService;
import com.adc.da.crm.dao.ProjectInfoEODao;
import com.adc.da.crm.entity.ProjectInfoEO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 *
 * <br>
 * <b>功能：</b>PROJECT_INFO ProjectInfoEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2018-11-28 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("projectInfoEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class ProjectInfoEOService extends BaseService<ProjectInfoEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(ProjectInfoEOService.class);

    @Autowired
    private ProjectInfoEODao dao;
    @Autowired
    private AdcFormDataDao formDataDao;

    public ProjectInfoEODao getDao() {
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
        ProjectInfoEO projectInfoEO = (ProjectInfoEO) AutoMatchMethodArgumentUtil.json2entity(adcFormDataEO, ProjectInfoEO.class);
        projectInfoEO.setId(adcFormDataEO.getId());
        projectInfoEO.setCreatedTime(new Date());
        projectInfoEO.setCreatedUser(adcFormDataEO.getCreateName());
        if(StringUtils.equals(projectInfoEO.getProEndFlag(), "是")){
            projectInfoEO.setProEndFlag("1");
        }else{
            projectInfoEO.setProEndFlag("0");
        }
        projectInfoEO.setDelFlag(String.valueOf(DeleteFlagEnum.NORMAL.getValue()));//是否删除 （0- 否 1-是）
        this.dao.insertSelective(projectInfoEO);
        return adcFormDataEO;
    }

    public int update(String userId, AdcFormDataEO adcFormDataEO) {
        //更新ct_form
        this.formDataDao.updateByPrimaryKeySelective(adcFormDataEO);

        //更新解析后的数据
        ProjectInfoEO projectInfoEO = (ProjectInfoEO) AutoMatchMethodArgumentUtil.json2entity(adcFormDataEO, ProjectInfoEO.class);
        projectInfoEO.setId(adcFormDataEO.getId());
        projectInfoEO.setModifiedTime(new Date());
        projectInfoEO.setModifiedUser(userId);

        return dao.updateByPrimaryKeySelective(projectInfoEO);
    }

    public int delete(String id){
        List<String> ids = new ArrayList<>(1);
        ids.add(id);
        this.formDataDao.deleteLogicInBatch(ids);
        return this.dao.updateByPrimaryKeyAndDelFlag(id, 1);
    }


    public List<ProjectInfoEO> queryCRMListByPage(BasePage page) throws Exception {
        Integer rowCount = this.queryByCount(page);
        page.getPager().setRowCount(rowCount);
        return this.dao.queryCRMListByPage(page);
    }
}
