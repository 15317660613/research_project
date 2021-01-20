package com.adc.da.research.service;

import com.adc.da.base.service.BaseService;
import com.adc.da.research.dao.ProgressEODao;
import com.adc.da.research.entity.ProgressEO;
import com.adc.da.research.page.ProgressEOPage;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * <br>
 * <b>功能：</b>RS_PROJECT_PROGRESS ProgressEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-08-23 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("progressEOService")
@Transactional(value = "transactionManager", readOnly = false,
    propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class ProgressEOService extends BaseService<ProgressEO, String> {

    @Autowired
    private ProgressEODao dao;

    public ProgressEODao getDao() {
        return dao;
    }

    @Override
    public int updateByPrimaryKeySelective(ProgressEO progressEO) throws Exception {

        ProgressEO oldProgressEO = dao.selectByPrimaryKey(progressEO.getId());

        if (0 == progressEO.getExtInfo1()
            && !StringUtils.equals(oldProgressEO.getCheckDetail(), progressEO.getCheckDetail())) {

            ProgressEOPage progressEOPage = new ProgressEOPage();
            progressEOPage.setCheckDetail(progressEO.getCheckDetail());
            progressEOPage.setResearchProjectId(progressEO.getResearchProjectId());

            if (dao.queryByCount(progressEOPage) > 0) {
                return -1;
            }
        }

        return dao.updateByPrimaryKeySelective(progressEO);
    }

    /**
     * 强制更新,不进行校验
     *
     * @param progressEO
     * @param forceUpdate
     * @return
     * @throws Exception
     */
    public int updateByPrimaryKeySelective(ProgressEO progressEO, boolean forceUpdate) {

        if (forceUpdate) {
            return dao.updateByPrimaryKeySelective(progressEO);

        } else {
            throw new AdcDaBaseException("forceUpdate is false");
        }
    }
}
