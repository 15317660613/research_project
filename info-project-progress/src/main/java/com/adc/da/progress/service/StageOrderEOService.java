package com.adc.da.progress.service;

import com.adc.da.base.service.BaseService;
import com.adc.da.excel.poi.excel.ExcelImportUtil;
import com.adc.da.excel.poi.excel.entity.ImportParams;
import com.adc.da.progress.dao.StageOrderEODao;
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
 * <b>功能：</b>PR_STAGE_ORDER StageOrderEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-07-18 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("stageOrderEOService")
@Transactional(value = "transactionManager", readOnly = false,
    propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
@Slf4j
public class StageOrderEOService extends BaseService<StageOrderEO, String> {

    @Autowired
    private StageOrderEODao dao;

    public StageOrderEODao getDao() {
        return dao;
    }

    public List<StageOrderEO> excelImport(InputStream is, ImportParams params) throws Exception {
        List<StageOrderEO> dataList = ExcelImportUtil.importExcel(is, StageOrderEO.class, params);
        List<StageOrderEO> returnDataList = new ArrayList<>();
        //导入
        for (StageOrderEO stageOrderEO : dataList) {
            stageOrderEO.setId(UUID.randomUUID10());

            returnDataList.add(stageOrderEO);
            dao.insertSelective(stageOrderEO);
        }
        return returnDataList;
    }

    public void deleteByPrimaryKeysInFlag(List<String> ids) {
        dao.deleteByPrimaryKeysInFlag(ids);
    }

}
