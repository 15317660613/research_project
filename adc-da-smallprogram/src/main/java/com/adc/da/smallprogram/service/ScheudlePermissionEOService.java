package com.adc.da.smallprogram.service;

import com.adc.da.budget.constant.Role;
import com.adc.da.crm.dao.UserDao;
import com.adc.da.login.util.UserUtils;
import com.adc.da.smallprogram.entity.UserOrgEO;
import com.adc.da.sys.dao.UserEODao;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.StringUtils;
import com.adc.da.util.utils.UUID;
import org.apache.ibatis.annotations.Param;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.adc.da.base.service.BaseService;
import com.adc.da.smallprogram.dao.ScheudlePermissionEODao;
import com.adc.da.smallprogram.entity.ScheudlePermissionEO;

import java.util.*;

/**
 *
 * <br>
 * <b>功能：</b>TS_SCHEUDLE_PERMISSION ScheudlePermissionEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-11-20 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("scheudlePermissionEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class ScheudlePermissionEOService extends BaseService<ScheudlePermissionEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(ScheudlePermissionEOService.class);

    @Autowired
    private ScheudlePermissionEODao dao;
    @Autowired
    private UserEODao userEODao;

    public ScheudlePermissionEODao getDao() {
        return dao;
    }


    public void createAndUpdate(ScheudlePermissionEO scheudlePermissionEO){
        if (StringUtils.isNotEmpty(scheudlePermissionEO.getId())){
            dao.updateByPrimaryKeySelective(scheudlePermissionEO);
        }else {
            scheudlePermissionEO.setId(UUID.randomUUID10());
            dao.insertSelective(scheudlePermissionEO);
        }
    }

    public Integer selectByOriginIdAndDestUserIdLike(String originId , String destUserId){
        return dao.selectByOriginIdAndDestUserIdLike(originId,destUserId);
    }

    public  List<UserEO> getMaintenanceUserList(){
        UserEO userEO = UserUtils.getUser();
        if (null == userEO) {
            throw new AdcDaBaseException("登陆过期，请从新登陆！");
        }
        List<String> leaderUserIdList = new ArrayList<>();
        Subject subject = SecurityUtils.getSubject();
        List<ScheudlePermissionEO> scheudlePermissionEOList = null;
        if(subject.hasRole(Role.SUPER_ADMIN)){
             scheudlePermissionEOList = dao.queryByConfigType("0");
        }else {
             scheudlePermissionEOList = dao.queryByUserIdLikeMaintenancePersonMapAndConfigType(userEO.getUsid(),"0");
        }
        if (StringUtils.isNotEmpty(userEO.getExtInfo())){
            int weight = Integer.valueOf(userEO.getExtInfo());
            if (weight >= 7000 ){
                leaderUserIdList.add(userEO.getUsid());
            }
        }
        for (ScheudlePermissionEO scheudlePermissionEO : scheudlePermissionEOList){
                leaderUserIdList.addAll(Arrays.asList(scheudlePermissionEO.getOriginUserId()));
        }
        if (CollectionUtils.isEmpty(leaderUserIdList)){
            return new ArrayList<>();
        }
        List<UserEO> userEOList = userEODao.getUserWithRolesByUserIdsNotDeleted(leaderUserIdList);
        userEOList.sort(new Comparator<UserEO>() {
            @Override
            public int compare(UserEO o1, UserEO o2) {
                if (null== o2.getExtInfo()||null== o1.getExtInfo()||StringUtils.equals(o1.getExtInfo(), o2.getExtInfo())){
                    return -1 ;
                }
                return Integer.valueOf(o2.getExtInfo())- Integer.valueOf(o1.getExtInfo());
            }
        });

        return userEOList;
    }


}
