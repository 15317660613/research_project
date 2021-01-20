package com.adc.da.research.project.service;

import com.adc.da.base.service.BaseService;
import com.adc.da.login.util.UserUtils;
import com.adc.da.research.project.dao.CheckTargetEODao;
import com.adc.da.research.project.entity.CheckTargetEO;
import com.adc.da.research.project.page.CheckTargetEOPage;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.StringUtils;
import com.adc.da.util.utils.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *
 * <br>
 * <b>功能：</b>RS_CHECK_TARGET CheckTargetEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-10-30 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("checkTargetEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class CheckTargetEOService extends BaseService<CheckTargetEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(CheckTargetEOService.class);

    @Autowired
    private CheckTargetEODao dao;

    public CheckTargetEODao getDao() {
        return dao;
    }

    //根据projectId和checkTypeId删除
    public void deleteByProjectId(String projectId,String checkTypeId){
        dao.deleteByProjectId(projectId,checkTypeId);

    }

    public CheckTargetEO insertCheckTargetEO(CheckTargetEO checkTargetEO){
        UserEO loginUserEO = UserUtils.getUser();
        if(StringUtils.isEmpty(loginUserEO)){
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        checkTargetEO.setId(UUID.randomUUID10());
        checkTargetEO.setCreateTime(new Date());
        checkTargetEO.setCreateUserId(loginUserEO.getUsid());
        checkTargetEO.setCreateUserName(loginUserEO.getUsname());
        checkTargetEO.setDelFlag(0);
        try {
            dao.insertSelective(checkTargetEO);
        }catch (Exception e){
            logger.info(e.toString());
        }
        return checkTargetEO;
    }

    public CheckTargetEO updateCheckTargetEO(CheckTargetEO checkTargetEO){
        UserEO loginUserEO = UserUtils.getUser();
        if(StringUtils.isEmpty(loginUserEO)){
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        checkTargetEO.setModifyTime(new Date());
        checkTargetEO.setCreateUserId(loginUserEO.getUsid());
        checkTargetEO.setCreateUserName(loginUserEO.getUsname());
        checkTargetEO.setDelFlag(0);
        try {
            dao.updateByPrimaryKeySelective(checkTargetEO);
        }catch (Exception e){
            logger.info(e.toString());
        }
        return checkTargetEO;
    }

    public Map<String,List<CheckTargetEO>> queryByPart(CheckTargetEOPage page)throws Exception{

        List<CheckTargetEO> topList= new ArrayList<>();
        List<CheckTargetEO> bottomList= new ArrayList<>();
        List<CheckTargetEO> centerList= new ArrayList<>();
                List<CheckTargetEO> list= this.queryByList(page);
        for (CheckTargetEO c:list) {
            if(c.getCheckTypeId().equals("SUE4G9GWQN")){
                bottomList.add(c);
            }else if(c.getCheckTypeId().equals("3V57HSDVX5")){
                centerList.add(c);
            }else {
                topList.add(c);
            }

        }

        Map<String,List<CheckTargetEO>>  map=new HashMap<>();
        map.put("topList",topList);
        map.put("centerList",centerList);
        map.put("bottomList",bottomList);
        return map;
    }


    /**
     * 批量新增
     *
     * @param checkTargetEOS
     */
    public void batchInsertSelective(List<CheckTargetEO> checkTargetEOS) {

        UserEO user = UserUtils.getUser();
        if (StringUtils.isEmpty(user)){
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }

        if(StringUtils.isNotEmpty(checkTargetEOS.get(0).getCheckName())){
            for (CheckTargetEO m:checkTargetEOS) {
                dao.deleteByProjectId(m.getProjectId(),m.getCheckTypeId());

                m.setId(UUID.randomUUID10());
                m.setCreateUserId(user.getUsid());
                m.setCreateUserName(user.getUsname());
                m.setCreateTime(new Date());

            }

            dao.batchInsertSelective(checkTargetEOS);

        }else {

            dao.deleteByProjectId(checkTargetEOS.get(0).getProjectId(),checkTargetEOS.get(0).getCheckTypeId());
        }


    }

    /**
     * 批量新增基础指标数
     *
     * @param checkTargetEOList
     */
    public List<CheckTargetEO> batchInsertBasicTarget(List<CheckTargetEO> checkTargetEOList) {
        UserEO loginUserEO = UserUtils.getUser();
        if(StringUtils.isEmpty(loginUserEO)){
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        if(CollectionUtils.isEmpty(checkTargetEOList)){
            return new ArrayList<>();
        }
        //先清除基础考核类型的相关数据，PAQPUHXZPL是基础考核类型的字典ID
        dao.deleteByProjectId(checkTargetEOList.get(0).getProjectId(),"PAQPUHXZPL");
        for (CheckTargetEO checkTargetEO:checkTargetEOList) {
            checkTargetEO.setId(UUID.randomUUID10());
            checkTargetEO.setCheckTypeId("PAQPUHXZPL");
            checkTargetEO.setCreateUserId(loginUserEO.getUsid());
            checkTargetEO.setCreateUserName(loginUserEO.getUsname());
            checkTargetEO.setCreateTime(new Date());
        }
        dao.batchInsertSelective(checkTargetEOList);
        return checkTargetEOList;
    }

}
