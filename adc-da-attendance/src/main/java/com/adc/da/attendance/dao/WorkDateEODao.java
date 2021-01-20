package com.adc.da.attendance.dao;

import com.adc.da.attendance.entity.WorkDateEO;
import com.adc.da.base.dao.BaseDao;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * <br>
 * <b>功能：</b>WORK_DATE WorkDateEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-01-21 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface WorkDateEODao extends BaseDao<WorkDateEO> {

    /**
     * 用于 查询工作日，导入数据
     *
     * @return
     * @author Lee Kwanho 李坤澔
     *     date 2019-12-13
     **/
    List<WorkDateEO> getNotWorkDate(@Param("beginDate") Date beginDate, @Param("endDate") Date endDate);

    /**
     * 获取用户 code 与 用户id
     * code 存于 week字段
     * @return
     * @author Lee Kwanho 李坤澔
     *     date 2019-12-16
     **/
    List<WorkDateEO> getUserCode();
}
