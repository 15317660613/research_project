package com.adc.da.budget.service;

import com.adc.da.budget.entity.ChildrenTask;
import com.adc.da.login.util.UserUtils;
import com.adc.da.util.utils.BeanMapper;
import com.adc.da.util.utils.StringUtils;
import com.adc.da.util.utils.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.adc.da.base.service.BaseService;
import com.adc.da.budget.dao.ChildrenTaskHistoryEODao;
import com.adc.da.budget.entity.ChildrenTaskHistoryEO;

import java.util.Date;


/**
 *
 * <br>
 * <b>功能：</b>_CHILDREN_TASK_HISTORY ChildrenTaskHistoryEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-04-17 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("childrenTaskHistoryEOService")
@Transactional(rollbackFor = Throwable.class)
public class ChildrenTaskHistoryEOService extends BaseService<ChildrenTaskHistoryEO, String> {


    @Autowired
    private ChildrenTaskHistoryEODao dao;
    @Autowired
    private BeanMapper beanMapper;

    public ChildrenTaskHistoryEODao getDao() {
        return dao;
    }

    public int saveChildTaskHistory(ChildrenTask childrenTask,String operateType,String operateTime) {
        ChildrenTaskHistoryEO childrenTaskHistory = beanMapper.map(childrenTask,ChildrenTaskHistoryEO.class);
        childrenTaskHistory.setId(UUID.randomUUID());
        childrenTaskHistory.setChildTaskId(childrenTask.getId());
        childrenTaskHistory.setOperateUser(UserUtils.getUserId());
        childrenTaskHistory.setOperateDate(new Date());//到秒
        childrenTaskHistory.setOperateTime(operateTime);
        childrenTaskHistory.setOperateTime(null);//存毫秒值 现在没用之后做数据恢复时候有用
        if(StringUtils.equals("insert",operateType)) {
            childrenTaskHistory.setOperateType("insert");
        }else if(StringUtils.equals("delete",operateType)) {
            childrenTaskHistory.setOperateType("delete");
        }else if(StringUtils.equals("update1",operateType)) {
            childrenTaskHistory.setOperateType("update1");
        }else if(StringUtils.equals("update2",operateType)) {
            childrenTaskHistory.setOperateType("update2");
        }
        return  dao.insertSelective(childrenTaskHistory);
    }
}
