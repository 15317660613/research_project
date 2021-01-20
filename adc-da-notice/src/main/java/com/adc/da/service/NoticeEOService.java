package com.adc.da.service;

import com.adc.da.base.page.BasePage;
import com.adc.da.base.service.BaseService;
import com.adc.da.constant.Role;
import com.adc.da.dao.NoticeEODao;
import com.adc.da.dao.NoticeUserEODao;
import com.adc.da.dto.PageDTO;
import com.adc.da.entity.NoticeEO;
import com.adc.da.entity.NoticeUserEO;
import com.adc.da.login.util.UserUtils;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.StringUtils;
import com.adc.da.util.utils.UUID;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 *
 * <br>
 * <b>功能：</b>Notice NoticeEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2018-07-19 <br>
 * <b>版权所有：<b>版权归天津卡达克数据技术中心所有。<br>
 */
@Service("noticeEoService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED,
        rollbackFor = Throwable.class)
public class NoticeEOService extends BaseService<NoticeEO, Long> {



    @Autowired
    private NoticeEODao noticeEODao;

    @Autowired
    private NoticeUserEODao noticeUserEODao;


    public NoticeEODao getDao() {
        return noticeEODao;
    }

    /**
     * @Description  添加公告信息
     * @param noticeEO
     * @return
     */
    public NoticeEO insertNotice(NoticeEO noticeEO) throws Exception {
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        noticeEO.setId(UUID.randomUUID10());
        noticeEO.setCreateUserId(userId);
        noticeEO.setCreateTime(new Date());
        noticeEO.setUpdateTime(new Date());
        noticeEO.setDelFlag("0");
        noticeEODao.insertSelective(noticeEO);
        return noticeEO;
    }

    /**
     * @Description 修改公告
     * @param noticeEO
     * @return
     */
    public NoticeEO updateNotice(@RequestBody NoticeEO noticeEO){
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        noticeEO.setUpdateTime(new Date());
        noticeEO.setCreateUserId(userId);
        noticeEO.setDelFlag("0");
        noticeEODao.updateByPrimaryKeySelective(noticeEO);
        return noticeEO;
    }


    /**
     * @Description 批量删除公告
     * @param ids
     */
    public void deleteNotices(List<String> ids){
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        //① 逻辑删除公告主表信息
        noticeEODao.deleteLogicInBatch(ids);
        //② 同时物理删除用户公告相关信息
        List<NoticeUserEO> list = noticeUserEODao.findNoticeUserEOByNoticeId(ids);
        if(list.size()>0){
            noticeUserEODao.deleteInBatch(ids);
        }
    }

    public List<NoticeEO> findNotIgnoreNoticeListByUserId(){
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        return noticeEODao.findAllNoticeByReceiveUserIdInList(userId);
    }


    /**
     * @Description 去掉已经忽略的消息
     * @return
     */
    public List<NoticeEO> findIsIgnoreNoticeListByUserId(){
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        return noticeEODao.findIsIgnoreNoticeListByUserId(userId,"1");

    }

    /**
     * @Description 公告详情
     * @param id
     * @return
     */
    public NoticeEO detail(String id){
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        NoticeEO noticeEo = noticeEODao.selectById(id);
        return  noticeEo;
    }

    /**
     * @Description 首页用户登录后收到的公告
     * @return
     */
    public PageDTO noticeUserVOList(BasePage basePage){

        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        Integer page = 1;
        Integer pageSize = 20;
        if(basePage.getPage()!=null){
            page = basePage.getPage();
        }
        if (basePage.getPageSize()!=null){
            pageSize = basePage.getPageSize();
        }
        Integer startIndex = (page-1)*pageSize;
        Integer endIndex = page*pageSize;
        Subject subject = SecurityUtils.getSubject();
        //查询用户公告信息
        if (subject.hasRole(Role.NOTICE_ADMIN)) {
            List<NoticeEO> noticeEOList = noticeEODao.findAllInPage(startIndex, endIndex);
            Integer total = noticeEODao.findAllCountInPage();
            PageDTO result = new PageDTO(total, noticeEOList, page, pageSize);
            return result;
        }else {
            List<NoticeEO> noticeEOList = noticeEODao.findAllNoticeByReceiveUserIdInPage(startIndex, endIndex, userId);
            Integer total = noticeEODao.findAllCountNoticeByReceiveUserIdInPage(userId);
            PageDTO result = new PageDTO(total, noticeEOList, page, pageSize);
            return result;
        }
    }



    /**
     * @Description r如果用户有忽略 ，就记录在noticeuser里
     * @param noticeUserEOList
     * @throws Exception
     */
    public void doIgnoreNotice(List<NoticeUserEO> noticeUserEOList) {
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        if (StringUtils.isEmpty(noticeUserEOList)){
            return;
        }
        List<NoticeUserEO> insertNoticeUserEOList = new ArrayList<>();
        for (NoticeUserEO noticeUserEO : noticeUserEOList){
            if (StringUtils.isEmpty(noticeUserEO.getId())) {
                noticeUserEO.setId(UUID.randomUUID10());
                noticeUserEO.setReceiveUserId(userId);
                noticeUserEO.setOperationStatus("1");
                insertNoticeUserEOList.add(noticeUserEO);
            }
        }
        noticeUserEODao.insertList(insertNoticeUserEOList);

    }



}
