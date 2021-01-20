package com.adc.da.research.config.service;

import cn.hutool.core.util.ObjectUtil;
import com.adc.da.base.service.BaseService;
import com.adc.da.login.util.UserUtils;
import com.adc.da.research.config.dao.WarnNoticeEODao;
import com.adc.da.research.config.entity.WarnNoticeEO;
import com.adc.da.research.config.page.WarnNoticeEOPage;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 *
 * <br>
 * <b>功能：</b>RS_WARN_NOTICE WarnNoticeEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-10-29 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("warnNoticeEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class WarnNoticeEOService extends BaseService<WarnNoticeEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(WarnNoticeEOService.class);

    @Autowired
    private WarnNoticeEODao dao;

    public WarnNoticeEODao getDao() {
        return dao;
    }

    public void deleteByIds(String[] ids) {
        this.dao.deleteByIds(ids);
    }

    public void saveWarnNotice(WarnNoticeEO warnNoticeEO) throws Exception {
        warnNoticeEO.setId(UUID.randomUUID10());
        warnNoticeEO.setDelFlag(0);

        UserEO user = UserUtils.getUser();
        if (ObjectUtil.isNull(user)){
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }

        WarnNoticeEOPage page=new WarnNoticeEOPage();
        page.setProjectTypeId(warnNoticeEO.getProjectTypeId());
        final int mark = this.queryByCount(page);
        if (mark>0){
            throw new AdcDaBaseException("已存在此项目分类");
        }
        warnNoticeEO.setCreateUserId(user.getUsid());
        warnNoticeEO.setCreateUserName(user.getUsname());
        warnNoticeEO.setCreateTime(new Date());
        this.dao.insert(warnNoticeEO);
    }

    public void update(WarnNoticeEO warnNoticeEO) throws Exception {
        WarnNoticeEOPage page=new WarnNoticeEOPage();
        page.setProjectTypeId(warnNoticeEO.getProjectTypeId());
        final int mark = this.queryByCount(page);
        if (mark>0){
            throw new AdcDaBaseException("已存在此项目分类");
        }
        warnNoticeEO.setModifyTime(new Date());
        this.dao.updateByPrimaryKeySelective(warnNoticeEO);
    }
}
