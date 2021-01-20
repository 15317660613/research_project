package com.adc.da.group.service;

import com.adc.da.base.service.BaseService;
import com.adc.da.group.dao.CustomGroupEODao;
import com.adc.da.group.dao.UserCustomEODao;
import com.adc.da.group.entity.CustomGroupEO;
import com.adc.da.group.entity.UserCustomEO;
import com.adc.da.group.entity.UserGroupVO;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.sys.service.UserEOService;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <br>
 * <b>功能：</b>TR_USER_CUSTOM UserCustomEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-07-25 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("userCustomEOService")
@Transactional(value = "transactionManager",
    readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class UserCustomEOService extends BaseService<UserCustomEO, String> {

    @Autowired
    private UserCustomEODao userCustomEODao;

    @Autowired
    private CustomGroupEODao customGroupEODao;

    @Autowired
    private UserEOService userEOService;

    public UserCustomEODao getDao() {
        return userCustomEODao;
    }

    public List<UserCustomEO> addGroupAndUsers(String[] userIds, CustomGroupEO customGroupEO) {

        List<CustomGroupEO> customGroupEOS = customGroupEODao
            .queryByCreateUserIdAndGroupNameLike(customGroupEO.getGroupName(), customGroupEO.getCreateUserId());

        for (CustomGroupEO eo : customGroupEOS) {
            if (StringUtils.equals(customGroupEO.getGroupName(), eo.getGroupName())) {
                throw new AdcDaBaseException("组名已存在,不可创建相同的组名！");
            }
        }

        customGroupEODao.insertSelective(customGroupEO);

        List<UserCustomEO> userCustomEOList = new ArrayList<>();
        for (String uid : userIds) {
            UserCustomEO userCustomEO = new UserCustomEO();
            userCustomEO.setUserId(uid);
            userCustomEO.setGroupId(customGroupEO.getId());
            userCustomEODao.insertSelective(userCustomEO);
            userCustomEOList.add(userCustomEO);
        }
        return userCustomEOList;
    }

    public UserGroupVO getUserByGroupId(String groupId) throws Exception {
        List<UserCustomEO> userCustomEOList = userCustomEODao.getUserByGroupId(groupId);
        List<UserEO> userEOList = new ArrayList<>();

        UserGroupVO userGroupVO = new UserGroupVO();
        if (null == userCustomEOList) {
            throw new AdcDaBaseException("当前查找的组不存在！");
        }

        for (UserCustomEO userCustomEO : userCustomEOList) {
            UserEO userEO = userEOService.selectByPrimaryKey(userCustomEO.getUserId());
            if (null == userEO) {
                continue;
            }
            userEOList.add(userEO);
        }
        userGroupVO.setUserEOList(userEOList);
        return userGroupVO;
    }

}
