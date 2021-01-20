package com.adc.da.research.project.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.adc.da.base.page.BasePage;
import com.adc.da.base.service.BaseService;
import com.adc.da.login.util.UserUtils;
import com.adc.da.research.project.dao.MemberInfoHistoryEODao;
import com.adc.da.research.project.entity.MemberInfoHistoryEO;
import com.adc.da.sys.entity.OrgEO;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.sys.service.UserEOService;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.StringUtils;
import com.adc.da.util.utils.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


/**
 *
 * <br>
 * <b>功能：</b>RS_MEMBER_INFO_HISTORY MemberInfoHistoryEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-12-04 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("memberInfoHistoryEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class MemberInfoHistoryEOService extends BaseService<MemberInfoHistoryEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(MemberInfoHistoryEOService.class);

    @Autowired
    private MemberInfoHistoryEODao dao;

    public MemberInfoHistoryEODao getDao() {
        return dao;
    }

    @Autowired
    private UserEOService userEOService;

    //根据projectId删除
    public void deleteByProjectId(String projectId){
        dao.deleteByProjectId(projectId);

    }
    /**
     * 批量新增
     *
     * @param memberInfoEOS
     */
    public void batchInsertSelective(List<MemberInfoHistoryEO> memberInfoEOS) {

        UserEO user = UserUtils.getUser();
        if (ObjectUtil.isNull(user)){
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }

        for (MemberInfoHistoryEO m:memberInfoEOS) {
            dao.deleteByProjectId(m.getProjectId());

            if(StringUtils.isEmpty(m.getId())) {
                m.setId(UUID.randomUUID10());
            }
            m.setCreateUserId(user.getUsid());
            m.setCreateUserName(user.getUsname());
            m.setCreateTime(new Date());
            m.setDelFlag(0);

        }



        dao.batchInsertSelective(memberInfoEOS);
    }
    public List<MemberInfoHistoryEO> queryByPage(BasePage page) throws Exception {
        Integer rowCount = this.queryByCount(page);
        page.getPager().setRowCount(rowCount);
        List<MemberInfoHistoryEO> memberInfoEOList =  this.getDao().queryByPage(page);
        List<MemberInfoHistoryEO> resultList =  new ArrayList<>();
        List<String> memberUserIdList = new ArrayList<>();
        for (MemberInfoHistoryEO memberInfoEO : memberInfoEOList){
            if (StringUtils.isNotEmpty(memberInfoEO.getMemberUserId())) {
                memberUserIdList.add(memberInfoEO.getMemberUserId());
            }
        }
        if (CollectionUtils.isNotEmpty(memberUserIdList)) {
            List<UserEO> userEOList = userEOService.getDao().getUserWithRolesByUserIds(memberUserIdList);
            for (MemberInfoHistoryEO resMemberInfoEO : memberInfoEOList) {
                MemberInfoHistoryEO memberInfo = new MemberInfoHistoryEO();
                BeanUtil.copyProperties(resMemberInfoEO, memberInfo);
                for (UserEO userEO : userEOList) {
                    if (StringUtils.equals(memberInfo.getMemberUserId(), userEO.getUsid())) {
                        memberInfo.setUsname(userEO.getUsname());
                        memberInfo.setJobTitle(userEO.getJobTitle());
                        memberInfo.setMemberSex(userEO.getGender());
                        memberInfo.setLastDegree(userEO.getLastDegree()); //最后学历
                        memberInfo.setFinalDegree(userEO.getFinalDegree()); //最后学位
                        memberInfo.setOfficePhone(userEO.getOfficePhone());
                        memberInfo.setCellPhoneNumber(userEO.getCellPhoneNumber());
                        memberInfo.setIdentityNumber(userEO.getIdentityNumber());
                        memberInfo.setEmail(userEO.getEmail());
                        if (CollectionUtils.isNotEmpty(userEO.getOrgEOList())) {
                            OrgEO orgEO = getMiniOrgEO(userEO.getOrgEOList());
                            memberInfo.setDeptName(orgEO.getName());
                            memberInfo.setDeptId(orgEO.getId());
                        }
                    }
                }
                resultList.add(memberInfo);
            }
        }
        return resultList;
    }
    private OrgEO getMiniOrgEO(List<OrgEO> orgEOList){
        sortOrgEOList(orgEOList);
        for (OrgEO orgEO : orgEOList){
            if (StringUtils.equals(orgEO.getLayer(),"4")||StringUtils.equals(orgEO.getLayer(),"3")){
                return orgEO;
            }
        }
        return orgEOList.get(orgEOList.size()-1);
    }
    private List<OrgEO> sortOrgEOList(List<OrgEO> orgEOList) {
        Collections.sort(orgEOList, new Comparator<OrgEO>() {
            @Override
            public int compare(OrgEO o1, OrgEO o2) {
                if (StringUtils.isEmpty(o1.getLayer())){
                    return -1;
                }
                if (StringUtils.isEmpty(o2.getLayer())){
                    return 0;
                }
                return o1.getLayer().compareTo(o2.getLayer());
            }
        });
        return orgEOList;
    }

}
