package com.adc.da.customerresourcemanage.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.customerresourcemanage.entity.ContactsEO;
import com.adc.da.customerresourcemanage.page.ContactsEOPage;
import com.adc.da.customerresourcemanage.vo.ContactsVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * <br>
 * <b>功能：</b>DB_CONTACTS ContactsEODao<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-04-01 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
public interface ContactsEODao extends BaseDao<ContactsEO> {

    /**
     * 根据企业id逻辑删除关联用户
     * @param enterpriseId
     * @return
     */
    int logicDeleteByEnterpriseId(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据多个企业id逻辑删除所有关联用户
     * @param enterpriseIds
     * @return
     */
    int batchLogicDeleteByEnterpriseIds(@Param("enterpriseIds") List<String> enterpriseIds);

    List<ContactsEO> queryByPageVO(ContactsEOPage eoPage);
    Integer queryByCountVO(ContactsEOPage eoPage);

    int logicDeleteByPrimaryKey(@Param("id") String id);

    int batchLogicDeleteByPrimaryKeys(@Param("ids") List<String> ids);

    int contactsStatistics(@Param("createTimeStart") String createTimeStart,
                           @Param("createTimeEnd") String createTimeEnd,
                           @Param("effectId") String effectId);

}
