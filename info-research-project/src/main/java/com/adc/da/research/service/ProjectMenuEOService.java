package com.adc.da.research.service;

import com.adc.da.base.service.BaseService;
import com.adc.da.research.dao.ProjectMenuEODao;
import com.adc.da.research.entity.MenuEO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <br>
 * <b>功能：</b>RS_PROJECT_MENU MenuEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-08-22 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("projectMenuEOService")
@Transactional(value = "transactionManager",
    readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class ProjectMenuEOService extends BaseService<MenuEO, String> {

    @Autowired
    private ProjectMenuEODao dao;

    public ProjectMenuEODao getDao() {
        return dao;
    }

    /**
     * 查询菜单，包含层级关系
     *
     * @param topNodeId
     * @return
     * @throws Exception
     */
    public List<MenuEO> queryByListWithLevel(String topNodeId) {
        return dao.queryByListWithLevel(topNodeId);
    }

}
