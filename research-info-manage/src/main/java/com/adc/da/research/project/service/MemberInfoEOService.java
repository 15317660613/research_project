package com.adc.da.research.project.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.adc.da.base.page.BasePage;
import com.adc.da.base.service.BaseService;
import com.adc.da.login.util.UserUtils;
import com.adc.da.research.project.dao.MemberInfoEODao;
import com.adc.da.research.project.entity.MemberInfoEO;
import com.adc.da.research.project.page.MemberInfoEOPage;
import com.adc.da.research.project.vo.MemberInfoVO;
import com.adc.da.sys.entity.OrgEO;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.sys.service.UserEOService;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.StringUtils;
import com.adc.da.util.utils.UUID;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


/**
 *
 * <br>
 * <b>功能：</b>RS_MEMBER_INFO MemberInfoEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-11-02 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("memberInfoEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class MemberInfoEOService extends BaseService<MemberInfoEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(MemberInfoEOService.class);

    public static final String COLLEGE = "专科";
    public static final String BACHELOR = "本科";
    public static final String MASTER = "硕士";
    public static final String DOCTOR = "博士";
    public static final String SENIOR = "高级工程师";
    public static final String VICE_SENIOR = "副高级工程师";
    @Autowired
    private MemberInfoEODao dao;

    public MemberInfoEODao getDao() {
        return dao;
    }


    @Autowired
    private UserEOService userEOService;


    //根据projectId删除
    public void deleteByProjectId(String projectId){
        dao.deleteByProjectId(projectId);

    }

    public List<MemberInfoEO> queryByPage(BasePage page) throws Exception {
        Integer rowCount = this.queryByCount(page);
        page.getPager().setRowCount(rowCount);
        List<MemberInfoEO> memberInfoEOList =  this.getDao().queryByPage(page);
        List<MemberInfoEO> resultList =  new ArrayList<>();
        List<String> memberUserIdList = new ArrayList<>();
        for (MemberInfoEO memberInfoEO : memberInfoEOList){
            if (StringUtils.isNotEmpty(memberInfoEO.getMemberUserId())) {
                memberUserIdList.add(memberInfoEO.getMemberUserId());
            }
        }
        if (CollectionUtils.isNotEmpty(memberUserIdList)) {
            List<UserEO> userEOList = userEOService.getDao().getUserWithRolesByUserIds(memberUserIdList);
            for (MemberInfoEO resMemberInfoEO : memberInfoEOList) {
                MemberInfoVO memberInfo = new MemberInfoVO();
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

    /*
    * 查询统计人员职称学历
    * */
    public List<MemberInfoEO> queryCountByJob(MemberInfoEOPage page)throws Exception{
        return dao.queryCountByJob(page);
    }

    public Map<String,Integer> queryCountJobByProjectId(String projectId)throws Exception{
//        public static final String COLLEGE = "专科";
//        public static final String BACHELOR = "本科";
//        public static final String MASTER = "硕士";
//        public static final String DOCTOR = "博士";
//        public static final String SENIOR = "高级工程师";
//        public static final String VICE_SENIOR = "副高级工程师";
        int college = 0;
        int bachelor = 0;
        int master = 0;
        int doctor = 0;
        int senior = 0;
        int vice_senior = 0;
        List<MemberInfoEO> memberInfoEOList = dao.selectAllByProjectId(projectId);
        if (CollectionUtils.isNotEmpty(memberInfoEOList)){
            List<String> memberUserIdList = new ArrayList<>();
            for (MemberInfoEO memberInfoEO : memberInfoEOList){
                if (StringUtils.isNotEmpty(memberInfoEO.getMemberUserId())) {
                    memberUserIdList.add(memberInfoEO.getMemberUserId());
                }
            }
            List<UserEO> userEOList = userEOService.getDao().getUserWithRolesByUserIds(memberUserIdList);
            for (UserEO userEO : userEOList){
                if (StringUtils.equals(userEO.getLastDegree(),COLLEGE)){
                    college ++ ;
                }
                if (StringUtils.equals(userEO.getLastDegree(),BACHELOR)){
                    bachelor ++ ;
                }
                if (StringUtils.equals(userEO.getLastDegree(),MASTER)){
                    master ++ ;
                }
                if (StringUtils.equals(userEO.getLastDegree(),DOCTOR)){
                    doctor ++ ;
                }
                if (StringUtils.equals(userEO.getJobTitle(),SENIOR)){
                    senior ++ ;
                }
                if (StringUtils.equals(userEO.getJobTitle(),VICE_SENIOR)){
                    vice_senior ++ ;
                }
            }
        }
        Map<String,Integer> resultMap = new HashMap<>();
        resultMap.put("college",college);
        resultMap.put("bachelor",bachelor);
        resultMap.put("master",master);
        resultMap.put("doctor",doctor);
        resultMap.put("senior",senior);
        resultMap.put("vice_senior",vice_senior);
        return resultMap;

    }

    /**
     * 批量新增
     *
     * @param memberInfoEOS
     */
    public void batchInsertSelective(List<MemberInfoEO> memberInfoEOS) {

        UserEO user = UserUtils.getUser();
        if (ObjectUtil.isNull(user)){
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }

        for (MemberInfoEO m:memberInfoEOS) {
            dao.deleteByProjectId(m.getProjectId());

            m.setId(UUID.randomUUID10());
            m.setCreateUserId(user.getUsid());
            m.setCreateUserName(user.getUsname());
            m.setCreateTime(new Date());

        }



        dao.batchInsertSelective(memberInfoEOS);
    }

    /**
     * 批量新增
     *
     * @param memberInfoEOList
     */
    public void batchSaveMember(List<MemberInfoEO> memberInfoEOList) {
        dao.batchInsertSelective(memberInfoEOList);
    }

    //新增项目成员信息
    public MemberInfoEO saveMember(MemberInfoVO memberInfoVO){
        UserEO loginUserEO = UserUtils.getUser();
        if(StringUtils.isEmpty(loginUserEO)){
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        MemberInfoEOPage memberInfoEOPage = new MemberInfoEOPage();
        memberInfoEOPage.setProjectId(memberInfoVO.getProjectId());
        memberInfoEOPage.setMemberUserId(memberInfoVO.getMemberUserId());
        int count = dao.queryByCount(memberInfoEOPage);
        if (count > 0){
            logger.error("新增项目成员错误,当前项目成员已经存在于在项目！");
            throw new AdcDaBaseException("新增项目成员错误,当前项目成员已经存在于在项目!");
        }
        updateUserInfo(memberInfoVO);
        MemberInfoEO memberInfoEO = new MemberInfoEO();
        memberInfoEO.setProjectId(memberInfoVO.getProjectId());
        memberInfoEO.setMemberUserId(memberInfoVO.getMemberUserId());
        memberInfoEO.setTaskDivision(memberInfoVO.getTaskDivision());
        memberInfoEO.setMemberProfession(memberInfoVO.getMemberProfession());
        memberInfoEO.setWorkHours(memberInfoVO.getWorkHours());
        memberInfoEO.setDelFlag(0);
        memberInfoEO.setStudyAbroadType(memberInfoVO.getStudyAbroadType());
        memberInfoEO.setUndertakingTypeId(memberInfoVO.getUndertakingTypeId());
        memberInfoEO.setSort(memberInfoVO.getSort());
        memberInfoEO.setId(UUID.randomUUID10());
        memberInfoEO.setCreateTime(new Date());
        memberInfoEO.setCreateUserId(loginUserEO.getUsid());
        memberInfoEO.setCreateUserName(loginUserEO.getUsname());
        try {
            dao.insertSelective(memberInfoEO);
        } catch (Exception e) {
            logger.error("新增项目成员错误 "+ new Gson().toJson(memberInfoEO),e);
            throw new AdcDaBaseException("新增项目成员错误!");
        }
        return memberInfoEO;
    }
    //修改项目成员信息
    public MemberInfoEO updateMember(MemberInfoVO memberInfoVO){
        UserEO loginUserEO = UserUtils.getUser();
        if(StringUtils.isEmpty(loginUserEO)){
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        updateUserInfo(memberInfoVO);
        MemberInfoEO memberInfoEO = new MemberInfoEO();
        memberInfoEO.setProjectId(memberInfoVO.getProjectId());
        memberInfoEO.setMemberUserId(memberInfoVO.getMemberUserId());
        memberInfoEO.setTaskDivision(memberInfoVO.getTaskDivision());
        memberInfoEO.setMemberProfession(memberInfoVO.getMemberProfession());
        memberInfoEO.setWorkHours(memberInfoVO.getWorkHours());
        memberInfoEO.setDelFlag(0);
        memberInfoEO.setStudyAbroadType(memberInfoVO.getStudyAbroadType());
        memberInfoEO.setUndertakingTypeId(memberInfoVO.getUndertakingTypeId());
        memberInfoEO.setSort(memberInfoVO.getSort());
        memberInfoEO.setId(memberInfoVO.getId());
        memberInfoEO.setModifyTime(new Date());
        memberInfoEO.setCreateUserId(loginUserEO.getUsid());
        memberInfoEO.setCreateUserName(loginUserEO.getUsname());
        try {
            dao.updateByPrimaryKeySelective(memberInfoEO);
        } catch (Exception e) {
            logger.error("编辑项目成员错误 "+ new Gson().toJson(memberInfoEO),e);
            throw new AdcDaBaseException("编辑项目成员错误，请联系管理员!");
        }
        return memberInfoEO;
    }

    private void updateUserInfo(MemberInfoVO memberInfoVO){
        UserEO userEO = userEOService.getUserWithRoles(memberInfoVO.getMemberUserId());
        if(StringUtils.isNotEmpty(userEO)){
            userEO.setFinalDegree(memberInfoVO.getFinalDegree());
            userEO.setIdentityNumber(memberInfoVO.getIdentityNumber());
            userEO.setLastDegree(memberInfoVO.getLastDegree());
            userEO.setOfficePhone(memberInfoVO.getOfficePhone());
            userEO.setCellPhoneNumber(memberInfoVO.getCellPhoneNumber());
            userEO.setEmail(memberInfoVO.getEmail());
            userEO.setGender(memberInfoVO.getMemberSex());
            userEO.setJobTitle(memberInfoVO.getJobTitle());
            try {
                userEOService.updateByPrimaryKeySelective(userEO);
            } catch (Exception e) {
                logger.error("更新用户错误 "+ new Gson().toJson(userEO),e);
                throw new AdcDaBaseException("更新用户错误，请联系管理员!");
            }
        }
    }
    //删除负责人
    public void deleteMajorMemberByProjectId(String projectId){
        dao.deleteMajorMemberByProjectId(projectId);
    }
    //删除负责人
    public void deleteByProjectIdAndMemberUserId(String projectId, String memberUserId){
        dao.deleteByProjectIdAndMemberUserId(projectId,memberUserId);
    }

}
