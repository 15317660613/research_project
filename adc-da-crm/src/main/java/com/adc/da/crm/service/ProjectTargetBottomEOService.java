package com.adc.da.crm.service;

import com.adc.da.base.page.BasePage;
import com.adc.da.crm.dao.MonthBudgetEODao;
import com.adc.da.crm.entity.*;
import com.adc.da.crm.util.AutoMatchMethodArgumentUtil;
import com.adc.da.crm.vo.ProjectTargetBottomVO;
import com.adc.da.form.dao.AdcFormDataDao;
import com.adc.da.form.entity.AdcFormDataEO;
import com.adc.da.util.constant.DeleteFlagEnum;
import com.adc.da.util.utils.BeanMapper;
import com.adc.da.util.utils.DateUtils;
import com.adc.da.util.utils.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.adc.da.base.service.BaseService;
import com.adc.da.crm.dao.ProjectTargetBottomEODao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 *
 * <br>
 * <b>功能：</b>PROJECT_TARGET_BOTTOM ProjectTargetBottomEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2018-11-29 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("projectTargetBottomEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class ProjectTargetBottomEOService extends BaseService<ProjectTargetBottomEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(ProjectTargetBottomEOService.class);

    @Autowired
    private ProjectTargetBottomEODao dao;
    @Autowired
    private AdcFormDataDao formDataDao;
    @Autowired
    private MonthBudgetEODao monthBudgetEODao;
    @Autowired
    BeanMapper beanMapper;

    public ProjectTargetBottomEODao getDao() {
        return dao;
    }

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
        ProjectTargetBottomVO projectTargetBottomVO = (ProjectTargetBottomVO) AutoMatchMethodArgumentUtil.json2entity(adcFormDataEO, ProjectTargetBottomVO.class);
        //vo 转eo
        ProjectTargetBottomEO projectTargetBottomEO = this.beanMapper.map(projectTargetBottomVO, ProjectTargetBottomEO.class);
        projectTargetBottomEO.setId(adcFormDataEO.getId());
        projectTargetBottomEO.setCreatedTime(new Date());
        projectTargetBottomEO.setCreatedUser(adcFormDataEO.getCreateName());
        projectTargetBottomEO.setDelFlag(String.valueOf(DeleteFlagEnum.NORMAL.getValue()));//是否删除 （0- 否 1-是）
        this.dao.insertSelective(projectTargetBottomEO);
        List<MonthBudgetEO> monthBudgetEOList = projectTargetBottomVO.getMonthBudgetEOList();
        for (MonthBudgetEO monthBudgetEO : monthBudgetEOList) {
            monthBudgetEO.setId(UUID.randomUUID10());
            monthBudgetEO.setProTargetBotomId(projectTargetBottomEO.getId());
            monthBudgetEO.setCreatedTime(new Date());
            monthBudgetEO.setCreatedUser(adcFormDataEO.getCreateName());
            monthBudgetEO.setDelFlag(String.valueOf(DeleteFlagEnum.NORMAL.getValue()));//是否删除 （0- 否 1-是）
            this.monthBudgetEODao.insertSelective(monthBudgetEO);
        }

        return adcFormDataEO;
    }

    public int update(String userId, AdcFormDataEO adcFormDataEO) {
        //更新ct_form
        this.formDataDao.updateByPrimaryKeySelective(adcFormDataEO);

        //更新解析后的数据
        ProjectTargetBottomVO projectTargetBottomVO = (ProjectTargetBottomVO) AutoMatchMethodArgumentUtil.json2entity(adcFormDataEO, ProjectTargetBottomVO.class);
        //vo 转eo
        ProjectTargetBottomEO projectTargetBottomEO = this.beanMapper.map(projectTargetBottomVO, ProjectTargetBottomEO.class);
        projectTargetBottomEO.setId(adcFormDataEO.getId());
        projectTargetBottomEO.setModifiedTime(new Date());
        projectTargetBottomEO.setModifiedUser(adcFormDataEO.getCreateName());
        //月份分解 先删除 再新增
        //2.新增
        List<MonthBudgetEO> monthBudgetEOList = projectTargetBottomVO.getMonthBudgetEOList();
        for (MonthBudgetEO monthBudgetEO : monthBudgetEOList) {
            monthBudgetEO.setId(UUID.randomUUID10());
            monthBudgetEO.setProTargetBotomId(projectTargetBottomEO.getId());
            monthBudgetEO.setCreatedTime(new Date());
            monthBudgetEO.setCreatedUser(userId);
            monthBudgetEO.setDelFlag(String.valueOf(DeleteFlagEnum.NORMAL.getValue()));//是否删除 （0- 否 1-是）
            this.monthBudgetEODao.insertSelective(monthBudgetEO);
        }
        return dao.updateByPrimaryKeySelective(projectTargetBottomEO);
    }

    public int delete(String id){
        List<String> ids = new ArrayList<>(1);
        ids.add(id);
        this.formDataDao.deleteLogicInBatch(ids);
        this.monthBudgetEODao.updateByProTargetBotomIdAndDelFlag(id, 1);
        return this.dao.updateByPrimaryKeyAndDelFlag(id, 1);
    }

    public List<ProjectTargetBottomEO> queryCRMListByPage(BasePage page) throws Exception {
        Integer rowCount = this.queryByCount(page);
        page.getPager().setRowCount(rowCount);
        return this.dao.queryCRMListByPage(page);
    }

}
