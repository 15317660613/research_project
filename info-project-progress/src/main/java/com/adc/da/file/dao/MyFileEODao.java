package com.adc.da.file.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.file.entity.MyFileEO;
import com.adc.da.progress.page.MyFilePage;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * <br>
 * <b>功能：</b>TS_FILE FileEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2017-12-24 <br>
 * <b>版权所有：<b>版权归天津卡达克数据技术中心所有。<br>
 *  @see mybatis.mapper.file
 */
public interface MyFileEODao extends BaseDao<MyFileEO> {

    List<MyFileEO> selectByForeignId(@Param("value") String value);

    void  deleteByFileId(@Param("value") String value);

    void moveFile(@Param("fileIdList") List<String> fileIdList,@Param("foreignId") String foreignId) ;

    List<MyFileEO> queryFileByPage(MyFilePage page);


}
