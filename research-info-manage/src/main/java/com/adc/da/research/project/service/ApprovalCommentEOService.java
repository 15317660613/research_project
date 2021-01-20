package com.adc.da.research.project.service;

import com.adc.da.base.service.BaseService;
import com.adc.da.research.project.dao.ApprovalCommentEODao;
import com.adc.da.research.project.entity.ApprovalCommentEO;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.sys.service.UserEOService;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.RSAUtils;
import com.adc.da.util.utils.StringUtils;
import com.adc.da.util.utils.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;


/**
 *
 * <br>
 * <b>功能：</b>RS_APPROVAL_COMMENT ApprovalCommentEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-10-30 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("approvalCommentEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class ApprovalCommentEOService extends BaseService<ApprovalCommentEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(ApprovalCommentEOService.class);

    @Autowired
    private ApprovalCommentEODao dao;
    @Autowired
    private UserEOService userService;

    @Value("${RAS_PRI_KEY:}")
    private String RSAprivateKey;

    public ApprovalCommentEODao getDao() {
        return dao;
    }

    public void insertCheckToken(ApprovalCommentEO approvalCommentEO) {
        String token =  null;
        try {
            token = RSAUtils.privateDecrypt(approvalCommentEO.getToken(), RSAUtils.getPrivateKey(this.RSAprivateKey));
        } catch (NoSuchAlgorithmException e1) {
            logger.error("rsa解析失败");
            throw new AdcDaBaseException("rsa解析失败");
        } catch (InvalidKeySpecException e2) {
            logger.error("rsa解析失败,私钥非法");
            throw new AdcDaBaseException("rsa解析失败,私钥非法");
        }

        if(StringUtils.isEmpty(token)){
            throw new AdcDaBaseException("用户信息校验失败");
        }

        List<UserEO> tokenUsers = userService.getDao().selectListByUserCode(token);
        if(CollectionUtils.isEmpty(tokenUsers)){
            throw new AdcDaBaseException("用户token信息不存在");
        }

        try {
            approvalCommentEO.setId(UUID.randomUUID10());
            this.insertSelective(approvalCommentEO);
        } catch (Exception e) {
            logger.error("意见信息保存失败");
            throw new AdcDaBaseException("意见信息保存失败");
        }
    }

    public void batchInsertSelective(List<ApprovalCommentEO> approvalCommentEOList) {
        String token =  "";
        try {
            token = RSAUtils.privateDecrypt(approvalCommentEOList.get(0).getToken(), RSAUtils.getPrivateKey(this.RSAprivateKey));
        } catch (NoSuchAlgorithmException e1) {
            logger.error("rsa解析失败");
            throw new AdcDaBaseException("rsa解析失败");
        } catch (InvalidKeySpecException e2) {
            logger.error("rsa解析失败,私钥非法");
            throw new AdcDaBaseException("rsa解析失败,私钥非法");
        }

        if(StringUtils.isEmpty(token)){
            throw new AdcDaBaseException("用户信息校验失败");
        }

        List<UserEO> tokenUsers = userService.getDao().selectListByUserCode(token);
        if(CollectionUtils.isEmpty(tokenUsers)){
            throw new AdcDaBaseException("用户token信息不存在");
        }
        for (ApprovalCommentEO approvalCommentEO:approvalCommentEOList) {
            approvalCommentEO.setId(UUID.randomUUID10());
            approvalCommentEO.setDelFlag(0);
        }
        dao.batchInsertSelective(approvalCommentEOList);
    }
}
