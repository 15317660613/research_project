package com.adc.da.research.personnel.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.research.personnel.entity.ExpertUserInfoListEO;

import java.util.List;

/**
 * @description
 * @date 2020/10/27 17:14
 * @auth zhn
 */
public interface ExpertUserInfoTempDao extends BaseDao<ExpertUserInfoListEO> {

    //根据id集合获取信息
    List<ExpertUserInfoListEO> selectByIdS(String[] idS);
}
