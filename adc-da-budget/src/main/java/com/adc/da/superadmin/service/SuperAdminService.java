package com.adc.da.superadmin.service;

import com.adc.da.superadmin.cache.MapCache;
import com.adc.da.superadmin.dao.SuperAdminDao;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service("superAdminService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class SuperAdminService {

    @Autowired
    private SuperAdminDao dao;

    public SuperAdminDao getDao() {
        return dao;
    }

    /**
     * 判断当前登陆用户是否超级管理员角色
     */
    public Boolean isSuperAdmin() {
        Subject subject = SecurityUtils.getSubject();
        if (subject.hasRole(dao.getRoleNameByRoleCode("SUPER_ADMIN"))) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 根据角色编码（使用扩展字段extInfo）获取角色名称，无缓存
     */
    public String getRoleNameByRoleCode(String extInfo) {
        return dao.getRoleNameByRoleCode(extInfo);
    }

    /**
     * 根据角色编码（使用扩展字段extInfo）获取角色名称，使用缓存
     */
    public String getRoleNameByRoleCodeWithCache(String extInfo) {
        if (null == MapCache.single().get(extInfo)) {
            String roleName = dao.getRoleNameByRoleCode(extInfo);
            MapCache.single().set(extInfo, roleName, 60);
            return roleName;
        } else {
            return MapCache.single().get(extInfo);
        }
    }
}