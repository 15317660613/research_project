package com.adc.da.group.service;

import com.adc.da.base.service.BaseService;
import com.adc.da.group.dao.CustomGroupEODao;
import com.adc.da.group.entity.CustomGroupEO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <br>
 * <b>功能：</b>TS_CUSTOM_GROUP CustomGroupEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-07-25 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("customGroupEOService")
@Transactional(value = "transactionManager",
    readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class CustomGroupEOService extends BaseService<CustomGroupEO, String> {

    @Autowired
    private CustomGroupEODao dao;

    public CustomGroupEODao getDao() {
        return dao;
    }

    public List<CustomGroupEO> queryByCreateUserId(String userId) {
        return dao.queryByCreateUserId(userId);
    }

    public List<CustomGroupEO> queryByCreateUserIdAndGroupNameLike(String groupName, String userId) {
        return dao.queryByCreateUserIdAndGroupNameLike(groupName, userId);
    }

}
