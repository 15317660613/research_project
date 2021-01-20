package com.adc.da.research.config.service;

import cn.hutool.core.util.ObjectUtil;
import com.adc.da.base.service.BaseService;
import com.adc.da.entity.NoticeEO;
import com.adc.da.login.util.UserUtils;
import com.adc.da.research.config.dao.WarnRuleEODao;
import com.adc.da.research.config.entity.WarnRuleEO;
import com.adc.da.research.config.page.WarnRuleEOPage;
import com.adc.da.service.NoticeEOService;
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
 * <b>功能：</b>RS_WARN_RULE WarnRuleEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-10-29 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("warnRuleEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class WarnRuleEOService extends BaseService<WarnRuleEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(WarnRuleEOService.class);

    @Autowired
    private WarnRuleEODao dao;

    @Autowired
    private NoticeEOService noticeEOService;

    public WarnRuleEODao getDao() {
        return dao;
    }

    public void save(WarnRuleEO warnRuleEO) throws Exception{
        warnRuleEO.setId(UUID.randomUUID());
        warnRuleEO.setDelFlag(0);

        UserEO user = UserUtils.getUser();
        if (ObjectUtil.isNull(user)){
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }

        WarnRuleEOPage page=new WarnRuleEOPage();
        page.setProjectTypeId(warnRuleEO.getProjectTypeId());
        final int mark = this.queryByCount(page);
        if (mark>0){
            throw new AdcDaBaseException("已存在此项目分类");
        }

        warnRuleEO.setCreateUserId(user.getUsid());
        warnRuleEO.setCreateUserName(user.getUsname());
        warnRuleEO.setCreateTime(new Date());
        dao.insert(warnRuleEO);
    }

    public void update(WarnRuleEO warnRuleEO) throws Exception{

        WarnRuleEOPage page=new WarnRuleEOPage();
        page.setProjectTypeId(warnRuleEO.getProjectTypeId());
        final int mark = this.queryByCount(page);
        if (mark>0){
            throw new AdcDaBaseException("已存在此项目分类");
        }
        warnRuleEO.setDelFlag(0);
        warnRuleEO.setModifyTime(new Date());
        dao.updateByPrimaryKeySelective(warnRuleEO);
    }

    public void deleteByIds(String[] ids) throws Exception{
        dao.deleteByPrimaryKeyArray(ids);
    }

    public void saveNotice() throws Exception{
        NoticeEO noticeEO = new NoticeEO();
        noticeEO.setId(UUID.randomUUID10());
        noticeEO.setCreateTime(new Date());
        noticeEO.setUpdateTime(new Date());
        UserEO user = UserUtils.getUser();
        if (ObjectUtil.isNull(user)){
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }

        noticeEO.setCreateUserId(user.getUsid());
        noticeEO.setDelFlag("0");
        noticeEO.setNoticeName("测试公告财务成本认领通知");
        noticeEO.setNoticeContent("测试公告能否自动生成");
        noticeEO.setNoticeTypeId("RESWHTV8TB");
        //是否是全部人可见：0不是全部可见；1全部可见
        //不是全部可见需要设置消息接受人id，已下未设置
        noticeEO.setNoticeIsLook("0");
        noticeEOService.insertSelective(noticeEO);
    }

}
