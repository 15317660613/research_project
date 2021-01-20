package com.adc.da.budget.service;

import com.adc.da.budget.dao.MyDao;
import com.adc.da.superadmin.cache.MapCache;
import com.adc.da.superadmin.dao.SuperAdminDao;
import org.apache.ibatis.annotations.Param;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("myService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class MyService {

    @Autowired
    private MyDao dao;

    public MyDao getDao() {
        return dao;
    }

    /**
     * @param usname
     * @return
     */
    public List<String> getUserIdByUserName(String usname) {
        return dao.getUserIdByUserName(usname);
    }

    public List<String> getUserIdByUserNameLike(String usname) {
        return dao.getUserIdByUserNameLike(usname);
    }

    public String getUserNameByUserId(String usid) {
        return dao.getUserNameByUserId(usid);
    }

    public float getWorkTimeByTaskId(String taskId) {
        Float workTime = dao.getWorkTimeByTaskId(taskId);
        return workTime == null ? 0 : workTime.floatValue();
    }

    public List<String> getTaskIdsByWorkTimeRange(String workTime, String workTimeOperator) {
            return dao.getTaskIdsBy1WorkTime(workTime, workTimeOperator);
    }

}