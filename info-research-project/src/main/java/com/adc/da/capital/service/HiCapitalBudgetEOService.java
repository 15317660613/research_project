package com.adc.da.capital.service;

import com.adc.da.base.service.BaseService;
import com.adc.da.capital.dao.HiCapitalBudgetEODao;
import com.adc.da.capital.entity.CapitalBudgetEO;
import com.adc.da.capital.entity.HiCapitalBudgetEO;
import com.adc.da.research.service.InfoEOService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static com.adc.da.research.utils.CompareObject.WRITE_DIFFERENT;

/**
 * <br>
 * <b>功能：</b>RS_HI_CAPITAL_BUDGET HiCapitalBudgetEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-10-30 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("hiCapitalBudgetEOService")
@Transactional(value = "transactionManager", readOnly = false,
    propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class HiCapitalBudgetEOService extends BaseService<HiCapitalBudgetEO, String> {

    @Autowired
    private HiCapitalBudgetEODao dao;

    public HiCapitalBudgetEODao getDao() {
        return dao;
    }

    @Autowired
    InfoEOService infoEOService;

    @Autowired
    CapitalBudgetEOService capitalBudgetEOService;

    /**
     * 更新，进行校验并且设置mask
     *
     * @param vo
     * @return
     * @throws Exception
     */
    public int updateAndSetMask(HiCapitalBudgetEO vo) throws Exception {

        infoEOService.checkAndUpdateStatus(vo);
        String projectId = vo.getResearchProjectId();

        /*
         *  获取历史数据，进行转换
         */
        CapitalBudgetEO sourceEO = capitalBudgetEOService.selectByPrimaryKey(projectId);
        HiCapitalBudgetEO target = new HiCapitalBudgetEO();
        BeanUtils.copyProperties(sourceEO, target);
        /*
         * 更新差异标识，同时选择性更改target
         * 初始化BusinessKey
         */
        target.initMask(vo, WRITE_DIFFERENT);
        target.setProcBusinessKey(vo.getProcBusinessKey());
        target.setResearchProjectId(vo.getResearchProjectId());
        return this.getDao().updateByPrimaryKeySelective(target);
    }

}
