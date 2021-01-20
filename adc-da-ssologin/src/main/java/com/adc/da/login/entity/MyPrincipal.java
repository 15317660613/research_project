package com.adc.da.login.entity;

import com.adc.da.sys.entity.UserEO;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 授权用户信息
 */
public class MyPrincipal implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    private String loginName;

    private String name;

    private transient Map<String, Object> cacheMap;

    public MyPrincipal(UserEO user) {
        this.id = user.getUsid() == null ? "" : user.getUsid();
        this.loginName = user.getAccount();
        this.name = user.getUsname();
    }

    public String getId() {
        return id;
    }

    public String getLoginName() {
        return loginName;
    }

    public String getName() {
        return name;
    }

    public Map<String, Object> getCacheMap() {
        if (cacheMap == null) {
            cacheMap = new HashMap<>();
        }
        return cacheMap;
    }

}