package com.adc.da.smallprogram.service;

import com.adc.da.base.page.BasePage;
import com.adc.da.excel.poi.excel.ExcelExportUtil;
import com.adc.da.excel.poi.excel.entity.ExportParams;
import com.adc.da.excel.poi.excel.entity.enums.ExcelType;
import com.adc.da.smallprogram.dao.ScheduleResearchEODao;
import com.adc.da.smallprogram.entity.ScheduleResearchEO;
import com.adc.da.smallprogram.page.ScheduleResearchUserEOPage;
import org.apache.ibatis.annotations.Param;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.adc.da.base.service.BaseService;
import com.adc.da.smallprogram.dao.ScheduleResearchUserEODao;
import com.adc.da.smallprogram.entity.ScheduleResearchUserEO;

import java.util.Date;
import java.util.List;

/**
 *
 * <br>
 * <b>功能：</b>TS_SCHEDULE_RESEARCH_USER ScheduleResearchUserEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-05-26 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("scheduleResearchUserEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class ScheduleResearchUserEOService extends BaseService<ScheduleResearchUserEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(ScheduleResearchUserEOService.class);

    @Autowired
    private ScheduleResearchUserEODao scheduleResearchUserEODao;
    @Autowired
    private ScheduleResearchEODao scheduleResearchEODao;

    public ScheduleResearchUserEODao getDao() {
        return scheduleResearchUserEODao;
    }
    public int insertList( List<ScheduleResearchUserEO> list){
        return scheduleResearchUserEODao.insertList(list);
    }
    public ScheduleResearchUserEO myUpdateByPrimaryKeySelective(ScheduleResearchUserEO scheduleResearchUserEO)throws Exception{
        scheduleResearchUserEO.setUpdateTime(new Date());
        this.updateByPrimaryKeySelective(scheduleResearchUserEO);
        if (scheduleResearchUserEO.getStatus()==1){ // 每次有子工作完成时，要去检查下其他子工作有没有写完，写完就将主表记录置为1
            boolean allFinishFlag = true;
            ScheduleResearchUserEOPage scheduleResearchUserEOPage = new ScheduleResearchUserEOPage();
            scheduleResearchUserEOPage.setResearchId(scheduleResearchUserEO.getResearchId());
            List<ScheduleResearchUserEO> scheduleResearchUserEOList = scheduleResearchUserEODao.queryByList(scheduleResearchUserEOPage);
            for (ScheduleResearchUserEO researchUserEO:scheduleResearchUserEOList){
                if (researchUserEO.getStatus()!=1){
                    allFinishFlag = false;
                    break;
                }
            }
            if (allFinishFlag){
                ScheduleResearchEO scheduleResearchEO = new ScheduleResearchEO();
                scheduleResearchEO.setId(scheduleResearchUserEO.getResearchId());
                scheduleResearchEO.setStatus(1);
                scheduleResearchEODao.updateByPrimaryKeySelective(scheduleResearchEO);
            }

        }
        return scheduleResearchUserEO;
    }
    public List<ScheduleResearchUserEO> queryFinishList(BasePage page){
        return scheduleResearchUserEODao.queryFinishList(page);
    }
    public List<ScheduleResearchUserEO> selectByResearchIdAndStatus(String researchId , Integer status){
        return scheduleResearchUserEODao.selectByResearchIdAndStatus(researchId,status);
    }
    /**
     * 导出
     * @param page
     * @return
     */
    public Workbook excelExport(ScheduleResearchUserEOPage page, ExportParams params ) {
        List<ScheduleResearchUserEO> costManagementEOList = scheduleResearchUserEODao.queryFinishList(page);
        return ExcelExportUtil.exportExcel(params, ScheduleResearchUserEO.class, costManagementEOList);
    }

}
