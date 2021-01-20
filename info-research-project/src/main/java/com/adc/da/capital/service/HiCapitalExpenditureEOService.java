package com.adc.da.capital.service;

import com.adc.da.base.service.BaseService;
import com.adc.da.capital.dao.HiCapitalExpenditureEODao;
import com.adc.da.capital.entity.CapitalExpenditureEO;
import com.adc.da.capital.entity.HiCapitalExpenditureEO;
import com.adc.da.research.service.InfoEOService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.adc.da.research.utils.CompareObject.WRITE_DIFFERENT;

/**
 * <br>
 * <b>功能：</b>RS_HI_CAPITAL_EXPENDITURE HiCapitalExpenditureEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-10-30 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("hiCapitalExpenditureEOService")
@Transactional(value = "transactionManager", readOnly = false,
    propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class HiCapitalExpenditureEOService extends BaseService<HiCapitalExpenditureEO, Void> {

    @Autowired
    private HiCapitalExpenditureEODao dao;

    public HiCapitalExpenditureEODao getDao() {
        return dao;
    }

    /**
     * 批量新增
     *
     * @param list
     * @return
     */
    public int insertSelectiveAll(List<HiCapitalExpenditureEO> list) {
        return this.getDao().insertSelectiveAll(list);
    }

    @Autowired
    private InfoEOService infoEOService;

    @Autowired
    private CapitalExpenditureEOService capitalExpenditureEOService;

    /**
     * 更新，进行校验并且设置mask
     *
     * @param voList
     * @return
     * @throws Exception
     */
    public int updateAndSetMask(List<HiCapitalExpenditureEO> voList) throws Exception {

        infoEOService.checkAndUpdateStatus(voList.get(0));
        String id;

        for (HiCapitalExpenditureEO vo : voList) {
            id = vo.getId();
            /*
             *  获取历史数据，进行转换
             *  todo 查询放到for外侧
             */
            CapitalExpenditureEO sourceEO = capitalExpenditureEOService.selectByPrimaryKey(id);
            HiCapitalExpenditureEO target = new HiCapitalExpenditureEO();
            BeanUtils.copyProperties(sourceEO, target);
            /*
             * 更新差异标识，同时选择性更改target
             * 初始化BusinessKey
             */
            target.initMask(vo, WRITE_DIFFERENT);
            target.setId(id);
            target.setProcBusinessKey(vo.getProcBusinessKey());
            target.setResearchProjectId(vo.getResearchProjectId());
            this.getDao().updateByPrimaryKeySelective(target);
        }

        return 0;
    }

}
