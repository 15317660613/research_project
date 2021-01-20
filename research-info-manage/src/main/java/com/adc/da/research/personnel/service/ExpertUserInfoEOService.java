package com.adc.da.research.personnel.service;

import cn.hutool.core.util.ObjectUtil;
import com.adc.da.base.service.BaseService;
import com.adc.da.login.util.UserUtils;
import com.adc.da.research.personnel.dao.ExpertGroupUserDao;
import com.adc.da.research.personnel.dao.ExpertUserInfoEODao;
import com.adc.da.research.personnel.entity.ExpertUserInfoEO;
import com.adc.da.research.personnel.page.ExpertUserInfoEOPage;
import com.adc.da.research.personnel.vo.ExpertGroupUserVO;
import com.adc.da.research.personnel.vo.ExpertUserInfoVO;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.sys.service.UserEOService;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.StringUtils;
import com.adc.da.util.utils.UUID;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


/**
 * <br>
 * <b>功能：</b>RS_EXPERT_USER_INFO ExpertUserInfoEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-10-27 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("expertUserInfoEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
@Slf4j
public class ExpertUserInfoEOService extends BaseService<ExpertUserInfoEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(ExpertUserInfoEOService.class);

    @Autowired
    private ExpertUserInfoEODao dao;
    @Autowired
    private ExpertGroupUserDao expertGroupUserDao;

    @Autowired
    private UserEOService userEOService;
    @Autowired
    private ExpertGroupInfoEOService expertGroupInfoEOService;

    public ExpertUserInfoEODao getDao() {
        return dao;
    }

    /**
     * 专家人员信息新增或修改
     *
     * @param expertUserInfoVO
     */
    public void insertOrUpdateExpertUserInfo(ExpertUserInfoVO expertUserInfoVO) {
        String id = expertUserInfoVO.getId();
        //保留专家组-专家人员集合
        List<ExpertGroupUserVO> expertGroupUsers = new ArrayList<>();
        List<String> expertGroupIds=expertUserInfoVO.getExpertGroupId();
        //if(CollectionUtils.isEmpty(expertGroupIds)){
            //throw new AdcDaBaseException("专家组id为空");
        //}
        if (ObjectUtil.isNull(id)) {
            //新增信息到专家人员信息表(RS_EXPERT_USER_INFO)
            ExpertUserInfoEO expertUserInfoEO = getExpertUserInfoEO(expertUserInfoVO);
            dao.insertSelective(expertUserInfoEO);
            //新增专家人员id和专家组id到关联表
            //专家组合专家人员关联表
            if(CollectionUtils.isNotEmpty(expertGroupIds)){
                for (String expertGroupId : expertGroupIds) {
                    if(StringUtils.isEmpty(expertGroupId)){
                        continue;
                    }
                    ExpertGroupUserVO expertGroupUserVO = new ExpertGroupUserVO();
                    expertGroupUserVO.setId(UUID.randomUUID10());
                    expertGroupUserVO.setGroupId(expertGroupId);
                    expertGroupUserVO.setUserId(expertUserInfoEO.getId());
                    expertGroupUsers.add(expertGroupUserVO);
                }
                expertGroupUserDao.insertInfoBatch(expertGroupUsers);
            }

        } else {
            //修改专家人员信息表(RS_EXPERT_USER_INFO)
            ExpertUserInfoEO expertUserInfoEO = getExpertUserInfoEO(expertUserInfoVO);
            dao.updateByPrimaryKeySelective(expertUserInfoEO);

            //删除之前关联关系
            expertGroupInfoEOService.deleteByUserId(id);
            //专家组合专家人员关联表
            if(CollectionUtils.isNotEmpty(expertGroupIds)){
                for (String expertGroupId : expertGroupIds) {
                    if(StringUtils.isEmpty(expertGroupId)){
                        continue;
                    }
                    ExpertGroupUserVO expertGroupUserVO = new ExpertGroupUserVO();
                    expertGroupUserVO.setId(UUID.randomUUID10());
                    expertGroupUserVO.setGroupId(expertGroupId);
                    expertGroupUserVO.setUserId(expertUserInfoEO.getId());
                    expertGroupUsers.add(expertGroupUserVO);
                }
                expertGroupUserDao.insertInfoBatch(expertGroupUsers);
            }
        }

        //修改信息到TS_USER表(根据人员id去修改TS_USER表信息)
        //因为选择的人员信息来自TS_USER表,所有此人员的信息已经存在,所以无需新增
        UserEO userEO = getUserEO(expertUserInfoVO);
        try {
            userEOService.updateByPrimaryKeySelective(userEO);
        } catch (Exception e) {
            log.error("新增专家人员失败" + e.getMessage());
            throw new AdcDaBaseException("新增专家人员失败");
        }
    }

    /**
     * TS_USER表信息替换
     *
     * @param expertUserInfoVO
     * @return
     */
    private UserEO getUserEO(ExpertUserInfoVO expertUserInfoVO) {
        UserEO userEO = new UserEO();
        userEO.setUsid(expertUserInfoVO.getUserId());
        userEO.setGender(expertUserInfoVO.getGender());
        userEO.setIdentityNumber(expertUserInfoVO.getIdentityNumber());
        userEO.setBirthDate(expertUserInfoVO.getBirthDate());
        userEO.setLastDegree(expertUserInfoVO.getLastDegree());
        userEO.setFinalDegree(expertUserInfoVO.getFinalDegree());
        userEO.setJobTitle(expertUserInfoVO.getJobTitle());
        userEO.setCellPhoneNumber(expertUserInfoVO.getCellphoneNumber());
        return userEO;
    }

    /**
     * 专家人员信息表(RS_EXPERT_USER_INFO)信息替换
     *
     * @param expertUserInfoVO
     * @return
     */
    private ExpertUserInfoEO getExpertUserInfoEO(ExpertUserInfoVO expertUserInfoVO) {
        ExpertUserInfoEO expertUserInfoEO = new ExpertUserInfoEO();
        if (ObjectUtil.isNull(expertUserInfoVO.getId())) {
            expertUserInfoEO.setId(UUID.randomUUID10());
            expertUserInfoEO.setCreateTime(expertUserInfoVO.getCreateTime());
        } else {
            expertUserInfoEO.setId(expertUserInfoVO.getId());
            expertUserInfoEO.setModifyTime(expertUserInfoVO.getModifyTime());
        }

        UserEO user = UserUtils.getUser();
        if (ObjectUtil.isNull(user)){
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        expertUserInfoEO.setCreateUserId(user.getUsid());
        expertUserInfoEO.setCreateUserName(user.getUsname());
        expertUserInfoEO.setUserId(expertUserInfoVO.getUserId());
        expertUserInfoEO.setUserName(expertUserInfoVO.getUserName());
        expertUserInfoEO.setCompanyName(expertUserInfoVO.getCompanyName());
//        expertUserInfoEO.setExpertGroupId(expertUserInfoVO.getExpertGroupId());
        expertUserInfoEO.setResume(expertUserInfoVO.getResume());
        expertUserInfoEO.setResearchDirection(expertUserInfoVO.getResearchDirection());
        expertUserInfoEO.setExpertTypeId(expertUserInfoVO.getExpertTypeId());
        return expertUserInfoEO;
    }

    /**
     * 专家人员批量删除
     *
     * @param ids
     */
    public void batchDelete(List<String> ids) {
        //批量删除专家人员信息表(RS_EXPERT_USER_INFO)信息
        dao.batchDelete(ids);
        //批量删除专家组与人员关系表(TR_GROUP_USER)信息
        dao.batchDeleteGroupUser(ids);
    }

    /**
     * 查询单条详情
     *
     * @param id
     */
    public ExpertUserInfoVO selectInfoById(String id) {
        ExpertUserInfoVO expertUserInfoVO = dao.selectInfoById(id);
        //获取专家组集合
        try {
            List<String> bindUserInfoList = expertGroupInfoEOService.getBindExpertGroupList(id);
            if(CollectionUtils.isNotEmpty(bindUserInfoList)){
                expertUserInfoVO.setExpertGroupId(bindUserInfoList);
            }
        } catch (Exception e) {
            throw new AdcDaBaseException("专家组列表查询失败");
        }
        return expertUserInfoVO;

    }


    public List<ExpertUserInfoEO> getBindUserInfoList(String expertGroupId) throws Exception {
        return dao.getBindUserInfoList(expertGroupId);
    }

    public List<ExpertUserInfoEO> getUnBindUserInfoList(String expertGroupId) throws Exception {
        return dao.getUnBindUserInfoList(expertGroupId);
    }

    public void deleteBindUser(String expertGroupId) throws Exception {
        dao.deleteBindUser(expertGroupId);
    }

    public void saveBindUser(String id, String expertGroupId, String expertUserId) throws Exception {
        dao.saveBindUser(id, expertGroupId, expertUserId);
    }

    /**
     * 查询带专家组的专家人员
     * @param page
     * @return
     * @throws Exception
     */
    public List<ExpertUserInfoEO> queryByExpertGroupPage(ExpertUserInfoEOPage page) throws Exception{
        Integer count = dao.queryByExpertGroupCount(page);
        page.getPager().setRowCount(count);
        return dao.queryByExpertGroupPage(page);
    }

}
