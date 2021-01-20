package com.adc.da.crm.service;


import com.adc.da.login.util.UserUtils;
import com.adc.da.sys.dao.OrgEODao;
import com.adc.da.sys.dao.UserEODao;
import com.adc.da.sys.entity.DicTypeEO;
import com.adc.da.sys.entity.OrgEO;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.sys.service.DicTypeEOService;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.StringUtils;
import com.google.common.collect.Sets;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("myUserService")
public class UserService {

    @Autowired
    private UserEODao userEODao;
    @Autowired
    private OrgEODao orgEODao;

    @Autowired
    private DicTypeEOService dicTypeEOService;

    public List<UserEO> selectAllUserByOrgId(String orgId,String usName){
        if (StringUtils.isEmpty(usName)){
            usName = null ;
        }

        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        UserEO userEO = userEODao.getUserWithRoles(userId);

        List<OrgEO> orgEOList =  userEO.getOrgEOList();
        //取parentId最短的那个组织机构
        sortOrgEOList(orgEOList);
        Set<String>   myOrgIdSet = new HashSet<>();
        Set<String>   myParentOrgIdSet = new HashSet<>();
        for (OrgEO orgEO : orgEOList){
            myOrgIdSet.add(orgEO.getId());
            if (StringUtils.isNotEmpty(orgEO.getParentIds())) {
                myParentOrgIdSet.addAll(new HashSet<String>(Arrays.asList(orgEO.getParentIds().split(","))));
            }
        }
        // 先从字典找一圈
        List<DicTypeEO> dicTypeEOList = dicTypeEOService.queryByList("FAKE_BENBU");
        if(CollectionUtils.isNotEmpty(dicTypeEOList)) {
            for (DicTypeEO dicTypeEO : dicTypeEOList) {
                if (StringUtils.contains(dicTypeEO.getDicTypeName(), userId)) {
                    myOrgIdSet.add(dicTypeEO.getDicTypeCode());
                }
            }
        }
        myParentOrgIdSet.removeAll(myOrgIdSet);
        Subject subject = SecurityUtils.getSubject();
        if (!myOrgIdSet.contains("USW7ASDVED")){
            myParentOrgIdSet.add("USW7ASDVED") ;
        }
        // 如果属于管理员，那么将他的组织机构强行提到数据资源中心组织机构下
        if (subject.hasRole("项目管理员") || subject.hasRole("超级管理员")){
            myOrgIdSet.add("USW7ASDVED");
            myOrgIdSet.add("MH8JQV5TSN");
            myParentOrgIdSet.remove("USW7ASDVED") ;
            myParentOrgIdSet.remove(orgId);
        }

        if ( myParentOrgIdSet.contains(orgId) && !myOrgIdSet.contains("USW7ASDVED")
                && !subject.hasRole("项目管理员")
                && !subject.hasRole("超级管理员")
        ) {
            throw  new AdcDaBaseException("您没有权限查看该组织机构下的成员！");
        }
        if (StringUtils.isNotEmpty(orgId)){
            myOrgIdSet.clear();
            myOrgIdSet.add(orgId);
            return getUserEOList(myOrgIdSet, usName);
        }else {
            return getUserEOList(myOrgIdSet, usName);
        }

    }

    private List<UserEO> getUserEOList(String orgId,String usName){

        List<String> searchOrgIdList  = orgEODao.getSubOrgId(orgId);
        searchOrgIdList.add(orgId);
        List<UserEO> userEOList = userEODao.getAllUserEOWithOrgsAndRolesByOrgIdsAndNameLike(searchOrgIdList,usName);

        Collections.sort(userEOList, new Comparator<UserEO>() {
            @Override
            public int compare(UserEO o1, UserEO o2) {
                if(StringUtils.isEmpty(o2.getExtInfo())||StringUtils.isEmpty(o1.getExtInfo())){
                    return 0;
                }
                return Integer.parseInt(o2.getExtInfo())-Integer.parseInt(o1.getExtInfo());
            }
        });
        return userEOList ;
    }

    private List<UserEO> getUserEOList(Set<String> orgIdSet,String usName){
        List<String> searchOrgIdList =  new ArrayList<>();
        List<OrgEO> orgEOList = orgEODao.queryOrgAll();
        for (OrgEO orgEO : orgEOList){
            for(String myOrgId : orgIdSet) {
                if (StringUtils.isNotEmpty(orgEO.getParentIds()) && orgEO.getParentIds().contains(myOrgId)) {
                    searchOrgIdList.add(orgEO.getId());
                }
            }
        }
        searchOrgIdList.addAll(new ArrayList<>(orgIdSet));
        List<UserEO> userEOList = userEODao.getAllUserEOWithOrgsAndRolesByOrgIdsAndNameLike(searchOrgIdList,usName);

        Collections.sort(userEOList, new Comparator<UserEO>() {
            @Override
            public int compare(UserEO o1, UserEO o2) {
                return Integer.parseInt(o2.getExtInfo())-Integer.parseInt(o1.getExtInfo());
            }
        });
        return userEOList ;
    }


    //倒序
    private List<OrgEO> sortOrgEOList(List<OrgEO> orgEOList){


        Collections.sort(orgEOList, new Comparator<OrgEO>() {
            @Override
            public int compare(OrgEO o1, OrgEO o2) {
                return o1.getParentIds().length()- o2.getParentIds().length();
            }
        });
        return orgEOList ;
    }



}
