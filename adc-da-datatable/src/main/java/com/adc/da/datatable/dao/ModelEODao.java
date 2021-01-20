package com.adc.da.datatable.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.datatable.entity.ModelEO;
import org.apache.ibatis.annotations.Param;

public interface ModelEODao extends BaseDao<ModelEO> {
    void saveConfig(@Param("mid") String var1, @Param("m_config") String var2);

}
