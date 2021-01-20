package com.adc.da.research.funds.dao;

import com.adc.da.base.dao.BaseDao;
import com.adc.da.base.page.BasePage;
import com.adc.da.research.funds.entity.ProjectOverEO;
/**
 * ProjectOver DAO层
 */
public interface ProjectOverEODao extends BaseDao<ProjectOverEO> {

    /***
    * @Description: 根据参数删除相关数据 （物理删除，小心使用！）
    * @Param: [page]
    * @return: java.lang.Integer
    * @Author: yanyujie
    * @Date: 2020/11/17
    */
    Integer deleteByParam(BasePage page);
}
