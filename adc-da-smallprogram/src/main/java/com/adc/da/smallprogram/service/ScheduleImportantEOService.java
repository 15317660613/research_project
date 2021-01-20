package com.adc.da.smallprogram.service;

import com.adc.da.file.entity.FileEO;
import com.adc.da.file.service.FileEOService;
import com.adc.da.file.store.IFileStore;
import com.adc.da.smallprogram.page.ScheduleImportantEOPage;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.FileUtil;
import com.adc.da.util.utils.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.adc.da.base.service.BaseService;
import com.adc.da.smallprogram.dao.ScheduleImportantEODao;
import com.adc.da.smallprogram.entity.ScheduleImportantEO;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Date;

/**
 *
 * <br>
 * <b>功能：</b>TS_SCHEDULE_IMPORTANT ScheduleImportantEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-12-11 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("scheduleImportantEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class ScheduleImportantEOService extends BaseService<ScheduleImportantEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(ScheduleImportantEOService.class);

    @Autowired
    private ScheduleImportantEODao dao;
    @Autowired
    private FileEOService fileEOService;
    @Autowired
    private IFileStore iFileStore;

    public ScheduleImportantEODao getDao() {
        return dao;
    }

    public ScheduleImportantEO insertSelectiveNew(ScheduleImportantEO scheduleImportantEO , MultipartFile file) throws Exception{
        scheduleImportantEO.setCreateTime(new Date());
        InputStream is = file.getInputStream();
        String fileName = FileUtil.getFileName(file.getOriginalFilename()) ;
        checkSameName(fileName);

        String fileExtension = FileUtil.getFileExtension(file.getOriginalFilename());
        scheduleImportantEO.setId(UUID.randomUUID10());
        String path = this.iFileStore.storeFile(is, fileExtension, "");
        FileEO fileEO = new FileEO();
        fileEO.setFileId(UUID.randomUUID());
        fileEO.setFileName(fileName);
        fileEO.setFileType(fileExtension);
        fileEO.setContentType(file.getContentType());
        fileEO.setSavePath(path);
        fileEO.setCreateTime(new Date());
        fileEO.setUserId(scheduleImportantEO.getCreateUserId());
        fileEOService.insertSelective(fileEO);
        scheduleImportantEO.setExtInfo1(fileExtension);
        scheduleImportantEO.setName(fileEO.getFileName());
        scheduleImportantEO.setFileId(fileEO.getFileId());
        dao.insertSelective(scheduleImportantEO);
        return scheduleImportantEO ;
    }
    public void checkSameName(String name)throws Exception{
        ScheduleImportantEOPage scheduleImportantEOPage = new ScheduleImportantEOPage();
        scheduleImportantEOPage.setName(name);
        int count = dao.queryByCount(scheduleImportantEOPage);
        if (count>0){
            throw new AdcDaBaseException("相同文件名的文件已存在");
        }
    }
    public void softDeleteByPrimaryKey(String id){
        dao.softDeleteByPrimaryKey(id);
    }

}
