package com.adc.da.smallprogram.service;

import com.adc.da.base.service.BaseService;
import com.adc.da.smallprogram.dao.UserOpenidEODao;
import com.adc.da.smallprogram.entity.UserOpenidEO;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 * <br>
 * <b>功能：</b>TS_USER_OPENID UserOpenidEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-03-18 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("tsUserOpenidEOService")
@Transactional(rollbackFor = Throwable.class)
public class UserOpenidEOService extends BaseService<UserOpenidEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(UserOpenidEOService.class);

    @Autowired
    private UserOpenidEODao dao;

    public UserOpenidEODao getDao() {
        return dao;
    }


    public int deleteByUserId(String userId){
        return dao.deleteByUserId(userId);
    }

    public int deleteByUserIdList(List<String> userIdList){
        return dao.deleteByUserIdList(userIdList);
    }

}
