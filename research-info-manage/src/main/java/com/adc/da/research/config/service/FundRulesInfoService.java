package com.adc.da.research.config.service;

import cn.hutool.core.util.ObjectUtil;
import com.adc.da.login.util.UserUtils;
import com.adc.da.research.config.dao.FundRulesInfoDao;
import com.adc.da.research.config.entity.FundDetailsEO;
import com.adc.da.research.config.entity.FundRulesEO;
import com.adc.da.research.config.entity.FundRulesInfoEO;
import com.adc.da.research.config.vo.FundRulesInfoVO;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @description
 * @date 2020/10/26 9:05
 * @auth zhn
 */
@Service
@Slf4j
public class FundRulesInfoService {

    @Autowired
    private FundRulesEOService fundRulesEOService;

    @Autowired
    private FundDetailsEOService fundDetailsEOService;

    @Autowired
    private FundRulesInfoDao fundRulesInfoDao;

    public void insertOrUpdate(List<FundRulesInfoVO> fundRulesInfoVOS) {
        String fundRulesId = fundRulesInfoVOS.get(0).getFundRulesId();
        if (fundRulesId == null) {
            //新增
            //新增经费科目规则表(RS_FUND_RULES)信息
            FundRulesEO fundRulesEO = new FundRulesEO();
            fundRulesEO.setId(UUID.randomUUID10());

            UserEO user = UserUtils.getUser();
            if (ObjectUtil.isNull(user)){
                throw new AdcDaBaseException("登陆可能过期，请登录！");
            }

            fundRulesEO.setCreateUserId(user.getUsid());
            fundRulesEO.setCreateUserName(user.getUsname());
            fundRulesEO.setCreateTime(fundRulesInfoVOS.get(0).getFundRulesCreateTime());
            fundRulesEO.setFundTemplateName(fundRulesInfoVOS.get(0).getFundTemplateName());
            try {
                fundRulesEOService.insertSelective(fundRulesEO);
            } catch (Exception e) {
                log.error("新增经费科目规则失败" + e.getMessage());
                throw new AdcDaBaseException("新增经费科目规则失败:模板名称已存在");
            }

            //新增经费科目详情表(RS_FUND_DETAILS)信息
            List<FundDetailsEO> fundDetailsEOS = getFundDetailsEOS(fundRulesInfoVOS, fundRulesEO);
            //批量新增经费科目详情
            fundDetailsEOService.batchInsertSelective(fundDetailsEOS);

        } else {
            //编辑
            //编辑经费科目规则表(RS_FUND_RULES)信息
            FundRulesEO fundRulesEO = new FundRulesEO();
            fundRulesEO.setFundTemplateName(fundRulesInfoVOS.get(0).getFundTemplateName());
            fundRulesEO.setModifyTime(fundRulesInfoVOS.get(0).getFundRulesModifyTime());
            fundRulesEO.setId(fundRulesInfoVOS.get(0).getFundRulesId());
            try {
                fundRulesEOService.updateByPrimaryKeySelective(fundRulesEO);
            } catch (Exception e) {
                log.error("编辑经费科目规则失败" + e.getMessage());
                throw new AdcDaBaseException("编辑经费科目规则失败，请检查模板名是否重复");
            }
            //编辑经费科目详情表(RS_FUND_DETAILS)信息
            List<FundDetailsEO> fundDetailsEOS = getFundDetailsEOS(fundRulesInfoVOS, fundRulesEO);
            //因为编辑经费科目详情是既可以新增也可以编辑,所以采用现将之前保存的删除(根据模板id删除),再次重新新增
            List<String> fundRulesIds = new ArrayList<>();
            fundRulesIds.add(fundRulesId);
            fundDetailsEOService.deleteByFundRuleIds(fundRulesIds);
            fundDetailsEOService.batchInsertSelective(fundDetailsEOS);

        }

    }

    /**
     * 经费科目详情信息替换
     *
     * @param fundRulesInfoVOS
     * @return
     */
    private List<FundDetailsEO> getFundDetailsEOS(List<FundRulesInfoVO> fundRulesInfoVOS, FundRulesEO fundRulesEO) {
        List<FundDetailsEO> fundDetailsEOS = new ArrayList<>();

        UserEO user = UserUtils.getUser();
        if (ObjectUtil.isNull(user)){
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }

        for (FundRulesInfoVO fundRulesInfoVO : fundRulesInfoVOS) {
            FundDetailsEO fundDetailsEO = new FundDetailsEO();
            if (fundRulesInfoVO.getFundDetailsId() == null) {
                fundDetailsEO.setId(UUID.randomUUID10());
            } else {
                fundDetailsEO.setId(fundRulesInfoVO.getFundDetailsId());
            }
            fundDetailsEO.setCreateUserId(user.getUsid());
            fundDetailsEO.setCreateUserName(user.getUsname());
            fundDetailsEO.setBudgetAccountName(fundRulesInfoVO.getBudgetAccountName());
            fundDetailsEO.setCappedBudget(fundRulesInfoVO.getCappedBudget());
            fundDetailsEO.setModifiedFlag(fundRulesInfoVO.getModifiedFlag());
            fundDetailsEO.setParentId(fundRulesInfoVO.getFundDetailsParentId());
            fundDetailsEO.setSort(fundRulesInfoVO.getFundDetailsSort());
            fundDetailsEO.setAllowTransferFund(fundRulesInfoVO.getAllowTransferFund());
            fundDetailsEO.setFundRulesId(fundRulesEO.getId());
            fundDetailsEO.setCreateTime(fundRulesInfoVO.getFundDetailsCreateTime());
            fundDetailsEO.setModifyTime(fundRulesInfoVO.getFundDetailsModifyTime());
            fundDetailsEOS.add(fundDetailsEO);
        }
        return fundDetailsEOS;
    }

    /**
     * @param ids
     */
    public void batchDeleteByIds(List<String> ids) {
        //批量删除经费科目规则表(RS_FUND_RULES)中信息
        fundRulesEOService.deleteByIds(ids);

        //根据经费科目规则id批量删除经费科目详情表(RS_FUND_DETAILS)中信息
        fundDetailsEOService.deleteByFundRuleIds(ids);
    }

    /**
     * 查询单条详情
     *
     * @param id
     * @return
     */
    public List<FundRulesInfoEO> selectById(String id) {
        return fundRulesInfoDao.selectById(id);
    }
}
