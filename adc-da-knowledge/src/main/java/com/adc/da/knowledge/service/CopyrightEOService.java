package com.adc.da.knowledge.service;

import com.adc.da.base.page.BasePage;
import com.adc.da.excel.poi.excel.ExcelExportUtil;
import com.adc.da.excel.poi.excel.entity.ExportParams;
import com.adc.da.knowledge.util.CommonUtil;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.adc.da.base.service.BaseService;
import com.adc.da.knowledge.dao.CopyrightEODao;
import com.adc.da.knowledge.entity.CopyrightEO;

import java.util.List;


/**
 *
 * <br>
 * <b>功能：</b>K_COPYRIGHT CopyrightEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-08-09 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("copyrightEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class CopyrightEOService extends BaseService<CopyrightEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(CopyrightEOService.class);

    @Autowired
    private CopyrightEODao dao;

    public CopyrightEODao getDao() {
        return dao;
    }

    public List<CopyrightEO> queryByMyPage(BasePage page) throws Exception {
        Integer rowCount = dao.queryByMyCount(page);
        page.getPager().setRowCount(rowCount);
        return this.getDao().queryByMyPage(page);
    }

    public Integer queryByMyCount(BasePage page) throws Exception{
        return this.getDao().queryByMyCount(page);
    }

    public Workbook excelExport(ExportParams params, List<String> copyrightIdList) {

        List<CopyrightEO> copyrightEOList = this.queryCopyrightByCopyrightIdsIn(copyrightIdList);
        for (CopyrightEO temp : copyrightEOList){
            temp.setSoftwareName(CommonUtil.unescapeHtml(temp.getSoftwareName()));
            temp.setBelongUserName(CommonUtil.unescapeHtml(temp.getBelongUserName()));
            temp.setRegisterNum(CommonUtil.unescapeHtml(temp.getRegisterNum()));
        }

        return ExcelExportUtil.exportExcel(params, CopyrightEO.class, copyrightEOList);
    }


    public List<CopyrightEO> queryCopyrightByCopyrightIdsIn(List<String> copyrightIdList){

        return  dao.queryCopyrightByCopyrightIdsIn(copyrightIdList);
    }
}
