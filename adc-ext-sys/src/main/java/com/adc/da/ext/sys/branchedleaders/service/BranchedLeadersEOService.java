package com.adc.da.ext.sys.branchedleaders.service;

import com.adc.da.ext.sys.branchedleaders.page.BranchedLeadersEOPage;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.UUID;
import lombok.val;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.adc.da.base.service.BaseService;
import com.adc.da.ext.sys.branchedleaders.dao.BranchedLeadersEODao;
import com.adc.da.ext.sys.branchedleaders.entity.BranchedLeadersEO;

import java.util.List;
import java.util.Map;


/**
 *
 * <br>
 * <b>功能：</b>TS_BRANCHED_LEADERS BranchedLeadersEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-10-09 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("branchedLeadersEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class BranchedLeadersEOService extends BaseService<BranchedLeadersEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(BranchedLeadersEOService.class);

    @Autowired
    private BranchedLeadersEODao dao;

    public BranchedLeadersEODao getDao() {
        return dao;
    }

    public List<BranchedLeadersEO> getPage(BranchedLeadersEOPage page) {
        List<BranchedLeadersEO> list=dao.getPage(page);
        if(CollectionUtils.isEmpty(list))return null;
        for(BranchedLeadersEO branchedLeadersEO:list) {
            String parentOrgName=dao.selectParentOrgNameByOrgId(branchedLeadersEO.getOrgId());
           branchedLeadersEO.setParentOrgName(parentOrgName);
        }
        return list;
    }

    public  void deleteByOrgName(String orgName){dao.deleteByOrgName(orgName);}

    public String queryOrgIdByName(String orgName){return dao.queryOrgIdByName(orgName);}

    public Map<String,Object> getEmployeeInfoByNameAndOrgId(String usname, String oid){
        return dao.getEmployeeInfoByNameAndOrgId(usname,oid);
    }

    public BranchedLeadersEO add(BranchedLeadersEO branchedLeadersEO){

        String id= UUID.randomUUID().toString();
        String orgName=branchedLeadersEO.getOrgName();
        String orgId=queryOrgIdByName(orgName);

        String manager=branchedLeadersEO.getUserName();
        String[] assiantantNames=branchedLeadersEO.getAssiantantNames().split(" ");
        String[] contractManagers=branchedLeadersEO.getContractsManagers().split(" ");
        String managerId="";
        String assistantIds="";
        String contractManagerIds="";
        Map<String,Object> aManager=getEmployeeInfoByNameAndOrgId(manager,orgId);
        managerId=aManager.get("USID").toString();
        for(String assistant:assiantantNames) {
            String usid=getEmployeeInfoByNameAndOrgId(assistant,orgId).get("USID").toString();
            assistantIds+=usid;
            assistantIds+=" ";
        }
        for(String contractManager:contractManagers) {
            String usid=getEmployeeInfoByNameAndOrgId(contractManager,orgId).get("USID").toString();
            contractManagerIds+=usid;
            contractManagerIds+=" ";
        }

        branchedLeadersEO.setId(id);
        branchedLeadersEO.setOrgId(orgId);
        branchedLeadersEO.setUserId(managerId);
        branchedLeadersEO.setAssiantantIds(assistantIds);
        branchedLeadersEO.setContractsManagerIds(contractManagerIds);
        branchedLeadersEO.setConfigType("0");

        dao.insertSelective(branchedLeadersEO);
        return branchedLeadersEO;
    }



    public  int save(BranchedLeadersEO branchedLeadersEO){

        String orgName=branchedLeadersEO.getOrgName();
        String orgId=queryOrgIdByName(orgName);
        String id=dao.selectIdByOrgId(orgId);

        String manager=branchedLeadersEO.getUserName();
        String[] assiantantNames=branchedLeadersEO.getAssiantantNames().split(" ");
        String[] contractManagers=branchedLeadersEO.getContractsManagers().split(" ");
        String managerId="";
        String assistantIds="";
        String contractManagerIds="";
        Map<String,Object> aManager=getEmployeeInfoByNameAndOrgId(manager,orgId);
        managerId=aManager.get("USID").toString();
        for(String assistant:assiantantNames) {
            String usid=getEmployeeInfoByNameAndOrgId(assistant,orgId).get("USID").toString();
            assistantIds+=usid;
            assistantIds+=" ";
        }
        for(String contractManager:contractManagers) {
            String usid=getEmployeeInfoByNameAndOrgId(contractManager,orgId).get("USID").toString();
            contractManagerIds+=usid;
            contractManagerIds+=" ";
        }


        branchedLeadersEO.setId(id);
        branchedLeadersEO.setOrgId(orgId);
        branchedLeadersEO.setUserId(managerId);
        branchedLeadersEO.setAssiantantIds(assistantIds);
        branchedLeadersEO.setContractsManagerIds(contractManagerIds);
        branchedLeadersEO.setConfigType("0");


        return dao.updateByPrimaryKey(branchedLeadersEO);
    }



}
