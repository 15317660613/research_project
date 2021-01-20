package com.adc.da.ext.sys.userdetail.service;

import com.adc.da.ext.sys.InitialPassword;
import com.adc.da.ext.sys.userdetail.dao.UserDetailEODao;
import com.adc.da.sys.dao.UserEODao;
import com.adc.da.util.utils.PasswordUtils;
import com.adc.da.util.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * 重置密码
 */
@Service
public class ResetPasswordService {

    @Autowired
    private UserEODao userEODao;


    @Autowired
    private UserDetailEODao userDetailEODao;

    @Transactional
    public void resetPassword(List<String> idList) {
        if (StringUtils.isEmpty(idList)) {
            throw new IllegalArgumentException("参数不能为空！");
        }
        userDetailEODao.deleteByUserId(idList);
        for (String userId : idList) {
            userEODao.updatePassword(userId, PasswordUtils.encryptPassword(InitialPassword.DEFAULT_M));
        }
    }

}
