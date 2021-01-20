package com.adc.da.ext.sys.userdetail.service;

import com.adc.da.base.service.BaseService;
import com.adc.da.login.util.UserUtils;
import com.adc.da.sys.dao.UserEODao;
import com.adc.da.ext.sys.userdetail.dao.UserDetailEODao;
import com.adc.da.ext.sys.userdetail.entity.UserDetailEO;
import com.adc.da.ext.sys.userdetail.vo.PasswordStatusVO;
import com.adc.da.sys.service.UserEOService;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.http.Result;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.Encodes;
import com.adc.da.util.utils.RSAUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.adc.da.util.http.ResponseMessage;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotNull;

import static com.adc.da.util.utils.StringUtils.*;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;


/**
 *
 * <br>
 * <b>功能：</b>TS_USER_DETAIL UserDetailEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-09-27 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("userDetailEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class UserDetailEOService extends BaseService<UserDetailEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(UserDetailEOService.class);

    @Autowired
    private UserDetailEODao dao;
    @Autowired
    private UserEOService userEOService;
    @Value("${RAS_PRI_KEY:}")
    private String RSAprivateKey;

    public UserDetailEODao getDao() {
        return dao;
    }

    //更新账户修改时间： 更新或插入
    public int  updateUserPasswordChangingTime(String userId,Date  updatedDate) {
        if(isNotEmpty(dao.getUserPasswordChangingTime(userId)))
            return dao.updateUserPasswordChangingTime(userId,updatedDate);
        else return insertUserPasswordChangingTime(userId,updatedDate);
    }

    //插入账户修改时间记录
    public int insertUserPasswordChangingTime(String userId,Date updatePwdDate) {
        UserDetailEO userDetailEO=new UserDetailEO();
        userDetailEO.setId(UUID.randomUUID().toString());
        userDetailEO.setUserId(userId);
        userDetailEO.setUpdatePwdTime(updatePwdDate);

        return dao.saveUserPasswordChangingTime(userDetailEO);
    }

    //获取密码修改时间
    public String getUserPasswordChangingTime(String userId) {
        return dao.getUserPasswordChangingTime(userId);
    }

    //获取账户状态

    public PasswordStatusVO getUserStatusDetail() {
        String userId = UserUtils.getUserId();
        if (isEmpty(userId)) {
            throw new AdcDaBaseException("登陆过期，请重新登陆！");
        }
        List<UserDetailEO> userDetailEOList = dao.selectByUserId(userId);
        if (CollectionUtils.isNotEmpty(userDetailEOList)){
            UserDetailEO userDetailEO = userDetailEOList.get(0);
            if (null == userDetailEO.getUpdatePwdTime()){
                return new PasswordStatusVO(true,-1);
            }
            DateTime updateTime = new DateTime(userDetailEO.getUpdatePwdTime());
            DateTime nowTime = new DateTime();
            int differentDays =  Days.daysBetween(updateTime, nowTime).getDays();
            //不用想那么复杂，直接由前端判断是否过期
            return new PasswordStatusVO(true,differentDays);
        }
        return new PasswordStatusVO(true,-1);
    }

    @Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public  ResponseMessage updatePasswordNew(String oldPassword, String newPassword) {
        String userId = UserUtils.getUserId();
        if (isEmpty(userId)) {
            throw new AdcDaBaseException("登陆过期，请重新登陆！");
        }
        try {
            oldPassword = RSAUtils.privateDecrypt(oldPassword, RSAUtils.getPrivateKey(this.RSAprivateKey));
            newPassword = RSAUtils.privateDecrypt(newPassword, RSAUtils.getPrivateKey(this.RSAprivateKey));
        } catch (NoSuchAlgorithmException e1) {
            logger.error("rsa解析失败");
            return Result.error("rsa解析失败");
        } catch (InvalidKeySpecException e2) {
            logger.error("rsa解析失败,私钥非法");
            return Result.error("rsa解析失败,私钥非法");
        }
        int  check = 0;
        if(newPassword.matches(".*[A-Z]+.*")){
            check ++ ;
        }
        if(newPassword.matches(".*[a-z]+.*")){
            check ++ ;
        }
        if(newPassword.matches(".*[0-9]+.*")){
            check ++ ;
        }
        if(newPassword.matches(".*[~!@#$^&*()=|{}':;',\\[\\].<>/?~！@#￥……&*（）——|{}【】‘；：”“'。，、？+-]+.*")){
            check ++ ;
        }
        if (newPassword.length() <6 || newPassword.length()>16 ||   check < 3) {
            return Result.error("r0018", "密码长度8位至16位，并包括数字、小写字母、大写字母和特殊符号4类中至少3类");
        } else {
            userEOService.updatePassword(UserUtils.getUserId(), oldPassword, newPassword);
            List<UserDetailEO> userDetailEOList = dao.selectByUserId(userId);
            if(CollectionUtils.isEmpty(userDetailEOList)) {
                UserDetailEO userDetailEO = new UserDetailEO();
                userDetailEO.setId(com.adc.da.util.utils.UUID.randomUUID10());
                userDetailEO.setUserId(userId);
                userDetailEO.setUpdatePwdTime(new Date());
                dao.insertSelective(userDetailEO);
            }else {
                UserDetailEO userDetailEO = userDetailEOList.get(0);
                userDetailEO.setUpdatePwdTime(new Date());
                dao.updateByPrimaryKeySelective(userDetailEO);
            }
            return Result.success();
        }

    }
}
