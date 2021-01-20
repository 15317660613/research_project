package com.adc.da.knowledge.service;

import com.adc.da.base.page.BasePage;
import com.adc.da.excel.poi.excel.ExcelExportUtil;
import com.adc.da.excel.poi.excel.entity.ExportParams;
import com.adc.da.knowledge.entity.PaperEO;
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
import com.adc.da.knowledge.dao.PatentEODao;
import com.adc.da.knowledge.entity.PatentEO;

import java.util.List;


/**
 *
 * <br>
 * <b>功能：</b>K_PATENT PatentEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-08-09 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("patentEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class PatentEOService extends BaseService<PatentEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(PatentEOService.class);

    @Autowired
    private PatentEODao dao;

    public PatentEODao getDao() {
        return dao;
    }

    public Integer queryByMyCount(BasePage page) throws Exception{
        return this.getDao().queryByMyCount(page);
    }

    public List<PatentEO> queryByMyPage(BasePage page) throws Exception {
        Integer rowCount = dao.queryByMyCount(page);
        page.getPager().setRowCount(rowCount);
        return this.getDao().queryByMyPage(page);
    }

    public Workbook excelExport(ExportParams params, List<String> patentIdList) {

        List<PatentEO> patentEOList = this.queryPatentByPatentIdsIn(patentIdList);
        for (PatentEO temp : patentEOList){
            temp.setName(CommonUtil.unescapeHtml(temp.getName()));
            temp.setBelongUserName(CommonUtil.unescapeHtml(temp.getBelongUserName()));
            temp.setAuthorizeNum(CommonUtil.unescapeHtml(temp.getAuthorizeNum()));
            temp.setAutoNumber(CommonUtil.unescapeHtml(temp.getAutoNumber()));
            temp.setCertificateNum(CommonUtil.unescapeHtml(temp.getCertificateNum()));
            temp.setDesignerUserNames(CommonUtil.unescapeHtml(temp.getDesignerUserNames()));
            temp.setNum(CommonUtil.unescapeHtml(temp.getNum()));
        }

        return ExcelExportUtil.exportExcel(params, PatentEO.class, patentEOList);
    }


    public List<PatentEO> queryPatentByPatentIdsIn(List<String> patentIdList){

        return  dao.queryPatentByPatentIdsIn(patentIdList);
    }
}
