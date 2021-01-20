package com.adc.da.knowledge.service;

import com.adc.da.base.page.BasePage;
import com.adc.da.excel.poi.excel.ExcelExportUtil;
import com.adc.da.excel.poi.excel.entity.ExportParams;
import com.adc.da.knowledge.entity.CopyrightEO;
import com.adc.da.knowledge.util.CommonUtil;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.adc.da.base.service.BaseService;
import com.adc.da.knowledge.dao.PaperEODao;
import com.adc.da.knowledge.entity.PaperEO;

import java.util.List;


/**
 *
 * <br>
 * <b>功能：</b>K_PAPER PaperEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-08-09 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("paperEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class PaperEOService extends BaseService<PaperEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(PaperEOService.class);

    @Autowired
    private PaperEODao dao;

    public PaperEODao getDao() {
        return dao;
    }

    public Integer queryByMyCount(BasePage page) throws Exception{
        return this.getDao().queryByMyCount(page);
    }

    public List<PaperEO> queryByMyPage(BasePage page) throws Exception {
        Integer rowCount = dao.queryByMyCount(page);
        page.getPager().setRowCount(rowCount);
        return this.getDao().queryByMyPage(page);
    }

    public Workbook excelExport(ExportParams params, List<String> paperIdList) {

        List<PaperEO> paperEOList = this.queryPaperByPaperIdsIn(paperIdList);

        for (PaperEO temp : paperEOList){
            temp.setKeywords(CommonUtil.unescapeHtml(temp.getKeywords()));
            temp.setName(CommonUtil.unescapeHtml(temp.getName()));
            temp.setPublishedJournals(CommonUtil.unescapeHtml(temp.getPublishedJournals()));
            temp.setPublishedIssue(CommonUtil.unescapeHtml(temp.getPublishedIssue()));
            temp.setCompany(CommonUtil.unescapeHtml(temp.getCompany()));
            temp.setAuthorUserNames(CommonUtil.unescapeHtml(temp.getAuthorUserNames()));
            temp.setPaperAbstract(CommonUtil.unescapeHtml(temp.getPaperAbstract()));
        }

        return ExcelExportUtil.exportExcel(params, PaperEO.class, paperEOList);
    }


    public List<PaperEO> queryPaperByPaperIdsIn(List<String> paperIdList){

        return  dao.queryPaperByPaperIdsIn(paperIdList);
    }


}
