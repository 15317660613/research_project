package com.adc.da.research.project.service;

import com.adc.da.base.service.BaseService;
import com.adc.da.login.util.UserUtils;
import com.adc.da.research.project.dao.CheckTargetHistoryEODao;
import com.adc.da.research.project.entity.CheckTargetHistoryEO;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.StringUtils;
import com.adc.da.util.utils.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;


/**
 *
 * <br>
 * <b>功能：</b>RS_CHECK_TARGET_HISTORY CheckTargetHistoryEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-12-04 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("checkTargetHistoryEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class CheckTargetHistoryEOService extends BaseService<CheckTargetHistoryEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(CheckTargetHistoryEOService.class);

    @Autowired
    private CheckTargetHistoryEODao dao;

    public CheckTargetHistoryEODao getDao() {
        return dao;
    }
    //根据projectId和checkTypeId删除
    public void deleteByProjectId(String projectId,String checkTypeId){
        dao.deleteByProjectId(projectId,checkTypeId);

    }
    /**
     * 批量新增
     *
     * @param checkTargetEOS
     */
    public void batchInsertSelective(List<CheckTargetHistoryEO> checkTargetEOS) {

        UserEO user = UserUtils.getUser();
        if (StringUtils.isEmpty(user)){
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }

        if(StringUtils.isNotEmpty(checkTargetEOS.get(0).getCheckName())){
            for (CheckTargetHistoryEO m:checkTargetEOS) {
                dao.deleteByProjectId(m.getProjectId(),m.getCheckTypeId());
                if(StringUtils.isEmpty(m.getId())) {
                     m.setId(UUID.randomUUID10());
                }
                m.setCreateUserId(user.getUsid());
                m.setCreateUserName(user.getUsname());
                m.setCreateTime(new Date());
                m.setDelFlag(0);
            }

            dao.batchInsertSelective(checkTargetEOS);

        }else {

            dao.deleteByProjectId(checkTargetEOS.get(0).getProjectId(),checkTargetEOS.get(0).getCheckTypeId());
        }


    }


}
