package com.adc.da.fileTemplate.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.base.page.BasePage;
import com.adc.da.fileTemplate.entity.FileTemplateTableEO;
import com.adc.da.fileTemplate.page.FileTemplateTableVOPage;
import com.adc.da.fileTemplate.vo.FileTemplateVO;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 *
 * <br>
 * <b>功能：</b>FILE_TEMPLATE_TABLE FileTemplateTableEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-09-01 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface FileTemplateTableEODao extends BaseDao<FileTemplateTableEO> {

    List<FileTemplateTableEO> selectByPrimaryKeys(@Param("list") List<String> list);

    void logicDeleteByPrimaryKeys(List<String> idList);

    List<FileTemplateVO> queryPageVO(FileTemplateTableVOPage page);

    int queryByVOCount(BasePage page);

    List<FileTemplateVO> queryByCode(String dicTypeCode);

    List<FileTemplateVO> queryByTempCode(String tempCode);
    List<String> getAllFileTemplateIdsInFileTemplateTable();
}
