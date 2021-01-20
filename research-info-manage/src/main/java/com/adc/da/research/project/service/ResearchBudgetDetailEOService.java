package com.adc.da.research.project.service;

import cn.hutool.core.util.ObjectUtil;
import com.adc.da.base.service.BaseService;
import com.adc.da.login.util.UserUtils;
import com.adc.da.research.project.dao.ResearchBudgetDetailEODao;
import com.adc.da.research.project.entity.ResearchBudgetDetailEO;
import com.adc.da.sys.entity.UserEO;
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

import java.util.Date;
import java.util.List;


/**
 *
 * <br>
 * <b>功能：</b>RS_RESEARCH_BUDGET_DETAIL ResearchBudgetDetailEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-11-10 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("researchBudgetDetailEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class ResearchBudgetDetailEOService extends BaseService<ResearchBudgetDetailEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(ResearchBudgetDetailEOService.class);

    @Autowired
    private ResearchBudgetDetailEODao dao;

    public ResearchBudgetDetailEODao getDao() {
        return dao;
    }

    /**
     * 批量新增
     *
     * @param researchBudgetDetailEOS
     */
    public void batchInsertSelective(List<ResearchBudgetDetailEO> researchBudgetDetailEOS) {
        if(CollectionUtils.isEmpty(researchBudgetDetailEOS)){
            return;
        }
        if(StringUtils.isNotEmpty(researchBudgetDetailEOS.get(0).getExtInfo3())){
            if(StringUtils.isNotEmpty(researchBudgetDetailEOS.get(0).getTotalPrice())){
            for (ResearchBudgetDetailEO w:researchBudgetDetailEOS) {
                dao.deleteByDetailType(w.getResearchProjectId(),w.getExtInfo3());
                w.setId(UUID.randomUUID10());

            }
            dao.batchInsertSelective(researchBudgetDetailEOS);
            }else {//如果删除最后一条数据
                dao.deleteByDetailType(researchBudgetDetailEOS.get(0).getResearchProjectId(),researchBudgetDetailEOS.get(0).getExtInfo3());
            }

        }else if(StringUtils.isNotEmpty(researchBudgetDetailEOS.get(0).getTotalPrice())){
            for (ResearchBudgetDetailEO w:researchBudgetDetailEOS) {
                dao.deleteByProjectId(w.getResearchProjectId(),w.getDetailType());
                w.setId(UUID.randomUUID10());

            }
            dao.batchInsertSelective(researchBudgetDetailEOS);
        }else {//如果删除最后一条数据
            dao.deleteByProjectId(researchBudgetDetailEOS.get(0).getResearchProjectId(),researchBudgetDetailEOS.get(0).getDetailType());
        }





    }

    public ResearchBudgetDetailEO saveResearchBudgetDetailEO(ResearchBudgetDetailEO researchBudgetDetailEO) throws Exception{
        UserEO loginUserEO = UserUtils.getUser();
        if (ObjectUtil.isNull(loginUserEO)){
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        researchBudgetDetailEO.setId(UUID.randomUUID10());
        researchBudgetDetailEO.setDelFlag(0);
        researchBudgetDetailEO.setCreateTime(new Date());
        researchBudgetDetailEO.setCreateUserId(loginUserEO.getUsid());
        researchBudgetDetailEO.setCreateUserName(loginUserEO.getUsname());
        dao.insertSelective(researchBudgetDetailEO);
        return researchBudgetDetailEO;
    }

    public ResearchBudgetDetailEO updateResearchBudgetDetailEO(ResearchBudgetDetailEO researchBudgetDetailEO) throws Exception{
        UserEO loginUserEO = UserUtils.getUser();
        if (ObjectUtil.isNull(loginUserEO)){
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        researchBudgetDetailEO.setModifyTime(new Date());
        researchBudgetDetailEO.setCreateUserId(loginUserEO.getUsid());
        researchBudgetDetailEO.setCreateUserName(loginUserEO.getUsname());
        dao.updateByPrimaryKeySelective(researchBudgetDetailEO);
        return researchBudgetDetailEO;
    }
}
