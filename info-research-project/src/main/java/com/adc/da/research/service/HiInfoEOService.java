package com.adc.da.research.service;

import com.adc.da.base.service.BaseService;
import com.adc.da.research.dao.HiInfoEODao;
import com.adc.da.research.dao.InfoEODao;
import com.adc.da.research.entity.HiInfoEO;
import com.adc.da.research.entity.InfoEO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * <br>
 * <b>功能：</b>RS_HI_PROJECT_INFO HiProjectInfoEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-10-24 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("hiInfoEOService")
@Transactional(value = "transactionManager",
    readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class HiInfoEOService extends BaseService<HiInfoEO, String> {

    @Autowired
    private HiInfoEODao dao;

    public HiInfoEODao getDao() {
        return dao;
    }

    @Autowired
    private InfoEODao infoEODao;

    @Autowired
    private InfoEOService infoEOService;

    /**
     * 更新基本成员信息，并且保存差异标识
     * 同时更新
     *
     * @param vo
     * @return
     * @throws Exception
     */
    public HiInfoEO updateAndSetMask(HiInfoEO vo) {

        InfoEO infoEO = infoEOService.checkAndUpdateStatus(vo);
        HiInfoEO target = new HiInfoEO();
        BeanUtils.copyProperties(infoEO, target);

        /*
         * 更新差异标识，同时选择性更改target
         * 初始化BusinessKey
         */
        target.initMaskAndProcBusinessKey(vo);
        this.getDao().updateByPrimaryKeySelective(target);
        return target;
    }

}
