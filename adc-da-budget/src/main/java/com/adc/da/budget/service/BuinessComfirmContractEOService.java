package com.adc.da.budget.service;

import com.adc.da.base.page.BasePage;
import com.adc.da.budget.dto.ProcessInstanceIdUpdateDTO;
import com.adc.da.budget.page.BuinessComfirmContractEOPage;
import com.adc.da.excel.poi.excel.ExcelExportUtil;
import com.adc.da.excel.poi.excel.ExcelImportUtil;
import com.adc.da.excel.poi.excel.entity.ExportParams;
import com.adc.da.excel.poi.excel.entity.ImportParams;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.adc.da.base.service.BaseService;
import com.adc.da.budget.dao.BuinessComfirmContractEODao;
import com.adc.da.budget.entity.BuinessComfirmContractEO;

import java.util.Date;
import java.util.List;

import java.io.InputStream;

/**
 *
 * <br>
 * <b>功能：</b>BUINESS_COMFIRM_CONTRACT BuinessComfirmContractEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2018-11-08 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("buinessComfirmContractEOService")
@Transactional(value = "transactionManager", readOnly = false,
        propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class BuinessComfirmContractEOService extends BaseService<BuinessComfirmContractEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(BuinessComfirmContractEOService.class);

    @Autowired
    private BuinessComfirmContractEODao dao;

    public BuinessComfirmContractEODao getDao() {
        return dao;
    }

    /**
     * 批量删除
     * @param list
     * @return
     */
    public int deleteInBatch(List<String> list){

        return dao.deleteInBatch(list);
    }
    public Workbook excelExport(ExportParams params, BuinessComfirmContractEOPage page) throws Exception {
        List<BuinessComfirmContractEO> eos = queryByList(page);
        return ExcelExportUtil.exportExcel(params,BuinessComfirmContractEO.class,eos);
    }
    public void excelImport(InputStream is, ImportParams params) throws Exception {
        List<BuinessComfirmContractEO> datas = ExcelImportUtil.importExcel(is, BuinessComfirmContractEO.class, params );
        for(int i=0; i< datas.size(); i++){
            datas.get(i).setStratTime(new Date());
            if(datas.get(i).getIslock() == null){
                datas.get(i).setIslock("否");
            }
        }
        importData(datas);
    }
    public void importData(List<BuinessComfirmContractEO> datas){
        for(BuinessComfirmContractEO eo : datas){
            eo.setId(DigestUtils.md5Hex(eo.toString()));
            try {
                dao.batchInsert(eo);
            }catch (Exception e){
                logger.error(e.getMessage(), e);
            }
        }
    }
    
    public List<BuinessComfirmContractEO> queryByList(BasePage page) throws Exception {
        return this.getDao().queryByList(page);
    }


    public void updateprocessInstanceIdByIdList
            (ProcessInstanceIdUpdateDTO processInstanceIdUpdateDTO) throws Exception {
        this.getDao().updateprocessInstanceIdByIdList(processInstanceIdUpdateDTO);
    }
}
