package com.adc.da.research.project.service;

import com.adc.da.base.service.BaseService;
import com.adc.da.research.project.dao.ImplementationProcFileEODao;
import com.adc.da.research.project.entity.ImplementationProcFileEO;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 * <br>
 * <b>功能：</b>RS_IMPLEMENTATION_PROC_FILE ImplementationProcFileEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-10-30 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("implementationProcFileEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class ImplementationProcFileEOService extends BaseService<ImplementationProcFileEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(ImplementationProcFileEOService.class);

    @Autowired
    private ImplementationProcFileEODao dao;

    public ImplementationProcFileEODao getDao() {
        return dao;
    }

    //    通过键值对<name,id>的形式上传文件
    public ResponseMessage submitFile(ImplementationProcFileEO file) throws Exception{
        Integer count=this.insertSelective(file);
        if(count>=1){
            return Result.success("success");
        }else {
            return Result.success("failed");
        }
    }

    //批量新增项目执行过程文件
    public void batchInsertImplementationProcFileEO(List<ImplementationProcFileEO> implementationProcFileEOList) throws Exception {
        dao.batchInsertSelective(implementationProcFileEOList);
    }

}
