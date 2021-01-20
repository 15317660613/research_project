package com.adc.da.research.project.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.research.project.entity.AnnexFileEO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * <br>
 * <b>功能：</b>RS_ANNEX_FILE AnnexFileEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-10-30 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface AnnexFileEODao extends BaseDao<AnnexFileEO> {

    /**
     * 批量新增
     *
     * @param annexFileEOS
     */
    void batchInsertSelective(@Param("annexFileEOS") List<AnnexFileEO> annexFileEOS);
    void deleteByProjectId(String projectId);

    void deleteByAnnexFileIds(@Param("annexFileIds") String annexFileIds);
}
