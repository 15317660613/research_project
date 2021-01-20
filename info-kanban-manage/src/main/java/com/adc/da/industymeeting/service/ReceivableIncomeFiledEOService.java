package com.adc.da.industymeeting.service;

import com.adc.da.industymeeting.entity.ReceivableIncomeEO;
import com.adc.da.sys.dao.DicTypeEODao;
import com.adc.da.sys.entity.DicTypeEO;
import com.adc.da.util.constant.DeleteFlagEnum;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.adc.da.base.service.BaseService;
import com.adc.da.industymeeting.dao.ReceivableIncomeFiledEODao;
import com.adc.da.industymeeting.entity.ReceivableIncomeFiledEO;

import java.util.*;


/**
 *
 * <br>
 * <b>功能：</b>RECEIVABLE_INCOME_FILED ReceivableIncomeFiledEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-04-03 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("receivableIncomeFiledEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class ReceivableIncomeFiledEOService extends BaseService<ReceivableIncomeFiledEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(ReceivableIncomeFiledEOService.class);

    @Autowired
    private ReceivableIncomeFiledEODao dao;

    @Autowired
    private DicTypeEODao dicTypeEODao;

    public ReceivableIncomeFiledEODao getDao() {
        return dao;
    }

    public ReceivableIncomeFiledEO save(ReceivableIncomeFiledEO eo, String userId) {
        eo.setId(UUID.randomUUID10());
        eo.setDelFlag(DeleteFlagEnum.NORMAL.getValue());
        eo.setCreateUserId(userId);
        eo.setCreateTime(new Date());
        eo.setUpdateUserId(userId);
        eo.setUpdateTime(new Date());
        dao.insertSelective(eo);
        return eo;
    }
    public Set<String> getDicTypeNameSet( List<DicTypeEO> dicTypeEOList){
        Set<String> set = new HashSet<>();
        for (DicTypeEO dicTypeEO : dicTypeEOList){
            set.add(dicTypeEO.getDicTypeName());
        }
        return set;
    }

    public Map<String,String> getDicTypeNameIdMap(List<DicTypeEO> dicTypeEOList){
        Map<String,String> map = new HashMap<>();
        for (DicTypeEO dicTypeEO : dicTypeEOList){
            map.put(dicTypeEO.getDicTypeName(),dicTypeEO.getId());
        }
        return map;
    }

    public List<ReceivableIncomeFiledEO> batchSave(List<ReceivableIncomeFiledEO> receivableIncomeFiledEOs, String userId) {
        // 导入数据前是否需要逻辑删除全部？
//        empty();
        List<DicTypeEO> companyDicTypeEOList = dicTypeEODao.queryByList("companyListData");
        Map<String,String> companyMap = getDicTypeNameIdMap(companyDicTypeEOList);

        List<DicTypeEO> areaDicTypeEOList = dicTypeEODao.queryByList("area");
        Map<String,String> areaMap = getDicTypeNameIdMap(areaDicTypeEOList);

        for (ReceivableIncomeFiledEO eo : receivableIncomeFiledEOs) {
            eo.setId(UUID.randomUUID10());
            eo.setDelFlag(DeleteFlagEnum.NORMAL.getValue());
            //eo.setCompany(dicTypeEODao.getDicTypeIdByDicIdAndDicTypeName("ECGGB823JL", eo.getCompany()));
            eo.setCreateUserId(userId);
            eo.setCreateTime(new Date());
            eo.setUpdateUserId(userId);
            eo.setUpdateTime(new Date());
            if (null == companyMap.get(eo.getCompany())){
                throw new  AdcDaBaseException(eo.getCompany()+" 不在系统中，请检查！");
            }else {
                eo.setCompany(companyMap.get(eo.getCompany()));
            }
            if (null == areaMap.get(eo.getArea())){
                throw new  AdcDaBaseException(eo.getArea()+" 不在系统中，请检查！");
            }else {
                eo.setArea(areaMap.get(eo.getArea()));
            }
            //dao.insertSelective(eo);
        }
        dao.insertList(receivableIncomeFiledEOs);
        return receivableIncomeFiledEOs;
    }

    /**
     * 批量逻辑删除
     */
    public void deleteLogicInBatch(List<String> ids) {
        dao.deleteLogicInBatch(ids);
    }

    public void deleteAll(){
        dao.deleteAll();
    }

    /**
     * 逻辑清空
     */
//    public void empty() {
//        dao.empty();
//    }
}
