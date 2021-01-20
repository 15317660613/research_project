package com.adc.da.research.project.service;

import com.adc.da.base.service.BaseService;
import com.adc.da.login.util.UserUtils;
import com.adc.da.research.project.dao.RContractInfoEODao;
import com.adc.da.research.project.entity.RContractInfoEO;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.StringUtils;
import com.adc.da.util.utils.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;


/**
 *
 * <br>
 * <b>功能：</b>RS_CONTRACT_INFO RContractInfoEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-11-02 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("RContractInfoEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class RContractInfoEOService extends BaseService<RContractInfoEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(RContractInfoEOService.class);

    @Autowired
    private RContractInfoEODao dao;

    public RContractInfoEODao getDao() {
        return dao;
    }



    public void  insertOrUpdate(RContractInfoEO rContractInfoEO){
        UserEO user = UserUtils.getUser();
        if (StringUtils.isEmpty(user)){
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }

        if (StringUtils.isEmpty(rContractInfoEO.getId())){
            rContractInfoEO.setId(UUID.randomUUID10());
            rContractInfoEO.setCreateUserId(user.getUsid());
            rContractInfoEO.setCreateUserName(user.getUsname());
            rContractInfoEO.setCreateTime(new Date());
            dao.insertSelective(rContractInfoEO);

        }else {//修改
            rContractInfoEO.setModifyTime(new Date());
            dao.updateByPrimaryKeySelective(rContractInfoEO);
        }

       /* dao.deleteByProjectId(rContractInfoEO.getProjectId());
        rContractInfoEO.setId(UUID.randomUUID10());
        rContractInfoEO.setCreateUserId(user.getUsid());
        rContractInfoEO.setCreateUserName(user.getUsname());
        rContractInfoEO.setCreateTime(new Date());
        dao.insertSelective(rContractInfoEO);*/


    }

    public RContractInfoEO updateRContractInfoEOByProjectId(RContractInfoEO rContractInfoEO){
        rContractInfoEO.setModifyTime(new Date());
        try{
            dao.updateRContractInfoEOByProjectId(rContractInfoEO);
        }catch (Exception e){
            logger.error(e.toString());
            throw new AdcDaBaseException("修改合同信息错误，请联系管理员!");
        }
        return rContractInfoEO;
    }

}
