package com.adc.da.fileTemplate.service;

import com.adc.da.base.page.BasePage;
import com.adc.da.fileTemplate.page.FileTemplateTableEOPage;
import com.adc.da.fileTemplate.page.FileTemplateTableVOPage;
import com.adc.da.fileTemplate.vo.FileTemplateVO;
import com.adc.da.login.util.UserUtils;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.StringUtils;
import com.adc.da.util.utils.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.support.SimpleTriggerContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.adc.da.base.service.BaseService;
import com.adc.da.fileTemplate.dao.FileTemplateTableEODao;
import com.adc.da.fileTemplate.entity.FileTemplateTableEO;

import java.util.*;


/**
 *
 * <br>
 * <b>功能：</b>FILE_TEMPLATE_TABLE FileTemplateTableEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-09-01 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("fileTemplateTableEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class FileTemplateTableEOService extends BaseService<FileTemplateTableEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(FileTemplateTableEOService.class);

    @Autowired
    private FileTemplateTableEODao dao;

    public FileTemplateTableEODao getDao() {
        return dao;
    }


    public void logicDeleteByPrimaryKey(List<String> idList) {
        dao.logicDeleteByPrimaryKeys(idList);
    }

    public int queryByVOCount(BasePage page) throws Exception {
        return this.getDao().queryByVOCount(page);
    }

    public List<FileTemplateVO> queryPageVO(FileTemplateTableVOPage page) throws Exception {
        Integer rowCount = this.queryByVOCount(page);
        page.getPager().setRowCount(rowCount);
        return this.getDao().queryPageVO(page);
    }

    public List<FileTemplateVO> queryByCode(String dicTypeCode) throws Exception {
        List<FileTemplateVO> fileTemplateVOList = dao.queryByCode(dicTypeCode);
        return  fileTemplateVOList;
    }

    public List<FileTemplateVO> queryByTempCode(String tempCode) {
        List<FileTemplateVO> fileTemplateVOList = dao.queryByTempCode(tempCode);
        return  fileTemplateVOList;
    }


    public int insertSelective(FileTemplateTableEO t) throws Exception {
        UserEO userEO = UserUtils.getUser();
        if (null == userEO) {
            throw new AdcDaBaseException("登陆过期，请从新登陆！");
        }

        String tempCode=t.getTempCode();
        String tempType=t.getTempTypeId();

        if(StringUtils.isEmpty(tempType)) {
            throw new AdcDaBaseException("模板类型不能为空");
        }

        List<FileTemplateVO> fileTemplateVOList=queryByTempCode(tempCode);
        if(CollectionUtils.isNotEmpty(fileTemplateVOList)){
           throw new AdcDaBaseException("模板号已存在");
        }

//        List<String> currentTempTypeIdList= dao.getAllFileTemplateIdsInFileTemplateTable();
//        if(CollectionUtils.isNotEmpty(currentTempTypeIdList)) {
//            for (String tempTypeId : currentTempTypeIdList) {
//                if (tempType.equals(tempTypeId)) {
//                    throw new AdcDaBaseException("模板类型重复");
//                }
//            }
//        }

        t.setId(UUID.randomUUID10());
        t.setCreateTime(new Date());
        t.setModifyTime(new Date());
        t.setCreateUserId(userEO.getUsid());
        t.setCreateUserName(userEO.getUsname());

        return this.getDao().insertSelective(t);
    }

    public int updateByPrimaryKeySelective(FileTemplateTableEO t) throws Exception {
        t.setModifyTime(new Date());
        String tempCode=t.getTempCode();
        String newType=t.getTempTypeId();

        String oldTempType="";

        if(StringUtils.isEmpty(newType)) {
            throw new AdcDaBaseException("模板类型不能为空");
        }

        List<FileTemplateVO> fileTemplateVOList=queryByTempCode(tempCode);
        if(CollectionUtils.isNotEmpty(fileTemplateVOList)){
            if (StringUtils.equals(fileTemplateVOList.get(0).getTempCode(),tempCode) && !StringUtils.equals(t.getId(),fileTemplateVOList.get(0).getId()))
            throw new AdcDaBaseException("当前模板号已存在");
        }
//        for(FileTemplateVO fileTemplateVO:fileTemplateVOList) {
//            oldTempType=fileTemplateVO.getTempTypeId();
//        }


//        if(!oldTempType.equals(newType)) {
//            List<String> currentTempTypeIdList = dao.getAllFileTemplateIdsInFileTemplateTable();
//            if (CollectionUtils.isNotEmpty(currentTempTypeIdList)) {
//                for (String tempTypeId : currentTempTypeIdList) {
//                    if (newType.equals(tempTypeId)) {
//                        throw new AdcDaBaseException("模板类型重复");
//                    }
//                }
//            }
//        }

        return this.getDao().updateByPrimaryKeySelective(t);
    }


    public List<FileTemplateVO> queryPageByTemp(FileTemplateTableVOPage page) throws Exception {
        Integer rowCount = this.queryByVOCount(page);
        page.getPager().setRowCount(rowCount);
        return this.getDao().queryPageVO(page);
    }
}
