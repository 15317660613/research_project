package com.adc.da.progress.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.adc.da.base.service.BaseService;
import com.adc.da.progress.dao.ProjectMilepostResultEODao;
import com.adc.da.progress.entity.ProjectMilepostResultEO;

import java.util.List;

/**
 *
 * <br>
 * <b>功能：</b>PR_PROJECT_MILEPOST_RESULT ProjectMilepostResultEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-07-29 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("projectMilepostResultEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class ProjectMilepostResultEOService extends BaseService<ProjectMilepostResultEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(ProjectMilepostResultEOService.class);

    @Autowired
    private ProjectMilepostResultEODao dao;

    public ProjectMilepostResultEODao getDao() {
        return dao;
    }


    public List<ProjectMilepostResultEO> selectByMilepostId(String milepostId){
        return  dao.selectByMilepostId(milepostId);
    }

}
