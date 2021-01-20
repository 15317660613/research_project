package com.adc.da.progress.service;

import com.adc.da.base.service.BaseService;
import com.adc.da.excel.poi.excel.ExcelImportUtil;
import com.adc.da.excel.poi.excel.entity.ImportParams;
import com.adc.da.progress.dao.ProjectNameEODao;
import com.adc.da.progress.entity.ProjectNameEO;
import com.adc.da.progress.entity.StageOrderEO;
import com.adc.da.util.utils.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <br>
 * <b>功能：</b>PR_PROJECT_NAME ProjectNameEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-07-18 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("projectNameEOService")
@Transactional(value = "transactionManager", readOnly = false,
    propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
@Slf4j
public class ProjectNameEOService extends BaseService<ProjectNameEO, String> {

    @Autowired
    private ProjectNameEODao dao;

    public ProjectNameEODao getDao() {
        return dao;
    }

    public List<ProjectNameEO> excelImport(InputStream is, ImportParams params) throws Exception {
        List<ProjectNameEO> dataList = ExcelImportUtil.importExcel(is, StageOrderEO.class, params);
        List<ProjectNameEO> returnDataList = new ArrayList<>();
        //导入
        for (ProjectNameEO projectNameEO : dataList) {
            projectNameEO.setId(UUID.randomUUID10());

            returnDataList.add(projectNameEO);
            dao.insertSelective(projectNameEO);
        }
        return returnDataList;
    }

    public void deleteByPrimaryKeysInFlag(List<String> ids) {
        dao.deleteByPrimaryKeysInFlag(ids);
    }
}
