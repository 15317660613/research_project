package com.adc.da.processform.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.processform.entity.FormFileEO;
import org.apache.ibatis.annotations.Param;

/**
 *
 * <br>
 * <b>功能：</b>PF_FORM_FILE FormFileEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-06-30 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface FormFileEODao extends BaseDao<FormFileEO> {


    int softDelete(@Param("processinstid") String processinstid ,@Param("filebelong") String filebelong);


    FormFileEO selectByProcessAndBelong(@Param("processinstid") String processinstid ,@Param("filebelong") String filebelong);

}
