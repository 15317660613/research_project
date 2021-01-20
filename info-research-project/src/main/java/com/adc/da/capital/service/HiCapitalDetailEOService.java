package com.adc.da.capital.service;

import com.adc.da.base.service.BaseService;
import com.adc.da.capital.dao.HiCapitalDetailEODao;
import com.adc.da.capital.entity.CapitalExpenditureDetailEO;
import com.adc.da.capital.entity.HiCapitalDetailEO;
import com.adc.da.research.service.InfoEOService;
import com.adc.da.util.utils.UUID;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import static com.adc.da.research.utils.CompareObject.READ_ONLY;
import static com.adc.da.research.utils.CompareObject.WRITE_DIFFERENT;

/**
 * <br>
 * <b>功能：</b>RS_HI_CAPITAL_DETAIL HiCapitalDetailEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-10-30 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("hiCapitalDetailEOService")
@Transactional(value = "transactionManager", readOnly = false,
    propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class HiCapitalDetailEOService extends BaseService<HiCapitalDetailEO, Void> {

    @Autowired
    private HiCapitalDetailEODao dao;

    public HiCapitalDetailEODao getDao() {
        return dao;
    }

    @Autowired
    private InfoEOService infoEOService;

    @Autowired
    private CapitalExpenditureDetailEOService sourceService;

    @Override
    public int insertSelective(HiCapitalDetailEO eo) throws Exception {
        eo.setId(UUID.randomUUID10());
        /*
         * 设置mask
         */
        eo.initMask(new HiCapitalDetailEO(), READ_ONLY);
        return this.getDao().insertSelective(eo);
    }

    /**
     * 更新，进行校验并且设置mask
     *
     * @param vo
     * @return
     * @throws Exception
     */
    public int updateAndSetMask(HiCapitalDetailEO vo) throws Exception {

        infoEOService.checkAndUpdateStatus(vo);
        String id = vo.getId();

        /*
         *  获取历史数据，进行转换
         */
        CapitalExpenditureDetailEO sourceEO = sourceService.selectByPrimaryKey(id);
        HiCapitalDetailEO target = new HiCapitalDetailEO();
        if (sourceEO != null) {
            /*
             * 证明为非新增数据
             */
            BeanUtils.copyProperties(sourceEO, target);
        }
        /*
         * 更新差异标识，同时选择性更改target
         * 初始化BusinessKey
         */
        target.initMask(vo, WRITE_DIFFERENT);
        target.setId(id);
        target.setProcBusinessKey(vo.getProcBusinessKey());
        target.setResearchProjectId(vo.getResearchProjectId());
        return this.getDao().updateByPrimaryKeySelective(target);
    }

    /**
     * @param id
     * @param key
     * @throws Exception
     */
    public int deleteByPrimaryKey(String id, String key) {
        HiCapitalDetailEO eo = new HiCapitalDetailEO();
        eo.setId(id);
        eo.setProcBusinessKey(key);
        return this.getDao().deleteByPrimaryKey(eo);
    }

}
