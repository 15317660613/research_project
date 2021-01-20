package com.adc.da.budget.service;

import com.adc.da.budget.dto.ProcessInstanceIdUpdateDTO;
import com.adc.da.budget.page.BuinessPerfonmanceContractEOPage;
import com.adc.da.excel.poi.excel.ExcelExportUtil;
import com.adc.da.excel.poi.excel.ExcelImportUtil;
import com.adc.da.excel.poi.excel.entity.ExportParams;
import com.adc.da.excel.poi.excel.entity.ImportParams;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.adc.da.base.service.BaseService;
import com.adc.da.budget.dao.BuinessPerfonmanceContractEODao;
import com.adc.da.budget.entity.BuinessPerfonmanceContractEO;

import java.util.Date;
import java.util.List;

import java.io.InputStream;


/**
 *
 * <br>
 * <b>功能：</b>BUINESS_PERFONMANCE_CONTRACT BuinessPerfonmanceContractEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2018-11-08 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("buinessPerfonmanceContractEOService")
@Transactional(value = "transactionManager", readOnly = false,
        propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class BuinessPerfonmanceContractEOService extends BaseService<BuinessPerfonmanceContractEO, String> {


    @Autowired
    private BuinessPerfonmanceContractEODao dao;

    public BuinessPerfonmanceContractEODao getDao() {
        return dao;
    }
    /**
     * 批量删除
     * @param list id 串
     * @return
     */
    public int deleteInBatch(List <String>list){
        return dao.deleteInBatch(list);
    }

    public Workbook excelExport(ExportParams params,BuinessPerfonmanceContractEOPage page) throws Exception {
        List<BuinessPerfonmanceContractEO> eos = queryByList(page);
        return ExcelExportUtil.exportExcel(params,BuinessPerfonmanceContractEO.class,eos);
    }
    public void excelImport(InputStream is, ImportParams params) throws Exception {
        List<BuinessPerfonmanceContractEO> datas =
                ExcelImportUtil.importExcel(is, BuinessPerfonmanceContractEO.class, params );
        for(int i=0;i < datas.size();i ++) {
            if (datas.get(i).getIslock() == null) {
                datas.get(i).setIslock("否");
            }
            datas.get(i).setStartTimre(new Date());

        }
        importData(datas);
    }
    public void importData(List<BuinessPerfonmanceContractEO> datas) {
        for(BuinessPerfonmanceContractEO eo : datas){
            eo.setId(DigestUtils.md5Hex(eo.toString()));
            dao.batchInsert(eo);
        }
    }

    public void updateprocessInstanceIdByIdList
            (ProcessInstanceIdUpdateDTO processInstanceIdUpdateDTO) throws Exception {
        this.getDao().updateprocessInstanceIdByIdList(processInstanceIdUpdateDTO);
    }

}
