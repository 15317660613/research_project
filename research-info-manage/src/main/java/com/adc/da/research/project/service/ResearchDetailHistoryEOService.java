package com.adc.da.research.project.service;

import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.StringUtils;
import com.adc.da.util.utils.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.adc.da.base.service.BaseService;
import com.adc.da.research.project.dao.ResearchDetailHistoryEODao;
import com.adc.da.research.project.entity.ResearchDetailHistoryEO;

import java.util.List;


/**
 *
 * <br>
 * <b>功能：</b>RS_RESEARCH_DETAIL_HISTORY ResearchDetailHistoryEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-12-05 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("researchDetailHistoryEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class ResearchDetailHistoryEOService extends BaseService<ResearchDetailHistoryEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(ResearchDetailHistoryEOService.class);

    @Autowired
    private ResearchDetailHistoryEODao dao;

    public ResearchDetailHistoryEODao getDao() {
        return dao;
    }

    /**
     * 批量新增
     *
     * @param researchDetailHistoryEOEOS
     */
    public void batchInsertSelective(List<ResearchDetailHistoryEO> researchDetailHistoryEOEOS) {
        if(CollectionUtils.isEmpty(researchDetailHistoryEOEOS)){
            return;
        }
        if(StringUtils.isNotEmpty(researchDetailHistoryEOEOS.get(0).getExtInfo3())){
            if(StringUtils.isNotEmpty(researchDetailHistoryEOEOS.get(0).getTotalPrice())){
                for (ResearchDetailHistoryEO w:researchDetailHistoryEOEOS) {
                    dao.deleteByDetailType(w.getResearchProjectId(),w.getExtInfo3());
                    w.setId(UUID.randomUUID10());
                    w.setDelFlag(0);

                }
                dao.batchInsertSelective(researchDetailHistoryEOEOS);
            }else {//如果删除最后一条数据
                dao.deleteByDetailType(researchDetailHistoryEOEOS.get(0).getResearchProjectId(),researchDetailHistoryEOEOS.get(0).getExtInfo3());
            }

        }else if(StringUtils.isNotEmpty(researchDetailHistoryEOEOS.get(0).getTotalPrice())){
            for (ResearchDetailHistoryEO w:researchDetailHistoryEOEOS) {
                dao.deleteByProjectId(w.getResearchProjectId(),w.getDetailType());
                w.setId(UUID.randomUUID10());
                w.setDelFlag(0);

            }
            dao.batchInsertSelective(researchDetailHistoryEOEOS);
        }else {//如果删除最后一条数据
            dao.deleteByProjectId(researchDetailHistoryEOEOS.get(0).getResearchProjectId(),researchDetailHistoryEOEOS.get(0).getDetailType());
        }





    }

}
