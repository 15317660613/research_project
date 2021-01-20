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
import com.adc.da.knowledge.dao.PrizeEODao;
import com.adc.da.knowledge.entity.PrizeEO;

import java.util.List;


/**
 *
 * <br>
 * <b>功能：</b>K_PRIZE PrizeEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-08-09 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("prizeEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class PrizeEOService extends BaseService<PrizeEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(PrizeEOService.class);

    @Autowired
    private PrizeEODao dao;

    public PrizeEODao getDao() {
        return dao;
    }

    public Integer queryByMyCount(BasePage page) throws Exception{
        Integer rowCount = dao.queryByMyCount(page);
        return this.getDao().queryByMyCount(page);
    }

    public List<PrizeEO> queryByMyPage(BasePage page) throws Exception {
        Integer rowCount = dao.queryByMyCount(page);
        page.getPager().setRowCount(rowCount);
        return this.getDao().queryByMyPage(page);
    }

    public Workbook excelExport(ExportParams params, List<String> prizeIdList) {

        List<PrizeEO> prizeEOList = this.queryPrizeByPrizeIdsIn(prizeIdList);
        for (PrizeEO temp : prizeEOList){
            temp.setPrizeAbstract(CommonUtil.unescapeHtml(temp.getPrizeAbstract()));
            temp.setPrizeName(CommonUtil.unescapeHtml(temp.getPrizeName()));
            temp.setProjectName(CommonUtil.unescapeHtml(temp.getProjectName()));
            temp.setBelongedUserName(CommonUtil.unescapeHtml(temp.getBelongedUserName()));
            temp.setIssuedDept(CommonUtil.unescapeHtml(temp.getIssuedDept()));
            temp.setLevel(CommonUtil.unescapeHtml(temp.getLevel()));
        }

        return ExcelExportUtil.exportExcel(params, PrizeEO.class, prizeEOList);
    }


    public List<PrizeEO> queryPrizeByPrizeIdsIn(List<String> prizeIdList){

        return  dao.queryPrizeByPrizeIdsIn(prizeIdList);
    }
}
