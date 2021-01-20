package com.adc.da.research.project.service;

import com.adc.da.base.service.BaseService;
import com.adc.da.login.util.UserUtils;
import com.adc.da.research.project.dao.WorkProgressHistoryEODao;
import com.adc.da.research.project.entity.WorkProgressHistoryEO;
import com.adc.da.research.project.page.WorkProgressHistoryEOPage;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.StringUtils;
import com.adc.da.util.utils.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *
 * <br>
 * <b>功能：</b>RS_WORK_PROGRESS_HISTORY WorkProgressHistoryEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-12-04 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("workProgressHistoryEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class WorkProgressHistoryEOService extends BaseService<WorkProgressHistoryEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(WorkProgressHistoryEOService.class);

    @Autowired
    private WorkProgressHistoryEODao dao;

    public WorkProgressHistoryEODao getDao() {
        return dao;
    }


    //根据projectId删除
    public void deleteByProjectId(String projectId){
        dao.deleteByProjectId(projectId);

    }
    /**
     * 批量新增
     *
     * @param workProgressEOS
     */
    public void batchInsertSelective(List<WorkProgressHistoryEO> workProgressEOS) {

        UserEO user = UserUtils.getUser();
        if (StringUtils.isEmpty(user)){
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        if(StringUtils.isNotEmpty(workProgressEOS.get(0).getExamineType())){
            for (WorkProgressHistoryEO w:workProgressEOS) {
                dao.deleteByProjectId(w.getProjectId());
                if(StringUtils.isEmpty(w.getId())) {
                     w.setId(UUID.randomUUID10());
                }
                w.setCreateUserId(user.getUsid());
                w.setCreateUserName(user.getUsname());
                w.setCreateTime(new Date());
                w.setExt3("0");
                w.setDelFlag((long) 0);
            }
            dao.batchInsertSelective(workProgressEOS);
        }else {

            dao.deleteByProjectId(workProgressEOS.get(0).getProjectId());
        }

    }
    /**
     * ADC项目合同书考核指标
     * @param page
     * @return
     */
    public Map<String,List<WorkProgressHistoryEO>> getCheckTargetList(WorkProgressHistoryEOPage page) throws Exception {

        List<WorkProgressHistoryEO> workProgressEOS = this.queryByList(page);

        List<WorkProgressHistoryEO> topList= new ArrayList<>();
        List<WorkProgressHistoryEO> bottomList= new ArrayList<>();
        if(workProgressEOS.size()>0) {
            for (WorkProgressHistoryEO c : workProgressEOS) {
                if (c.getExamineType().equals("结项验收")) {
                    bottomList.add(c);
                } else if (!c.getExamineType().equals("项目验收")) {
                    topList.add(c);
                }

            }
        }

        Map<String,List<WorkProgressHistoryEO>>  map=new HashMap<>();
        map.put("topList",topList);
        map.put("bottomList",bottomList);
        return map;



    }

}
