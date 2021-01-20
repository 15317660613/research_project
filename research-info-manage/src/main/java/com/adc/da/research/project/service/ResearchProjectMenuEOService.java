package com.adc.da.research.project.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.adc.da.base.service.BaseService;
import com.adc.da.research.project.dao.ResearchProjectMenuEODao;
import com.adc.da.research.project.entity.ResearchProjectMenuEO;


/**
 *
 * <br>
 * <b>功能：</b>RS_RESEARCH_PROJECT_MENU ResearchProjectMenuEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-11-06 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("researchProjectMenuEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class ResearchProjectMenuEOService extends BaseService<ResearchProjectMenuEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(ResearchProjectMenuEOService.class);

    @Autowired
    private ResearchProjectMenuEODao dao;

    public ResearchProjectMenuEODao getDao() {
        return dao;
    }

}
