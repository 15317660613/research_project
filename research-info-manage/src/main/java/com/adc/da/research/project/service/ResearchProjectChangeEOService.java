package com.adc.da.research.project.service;

import cn.hutool.core.util.ObjectUtil;
import com.adc.da.base.service.BaseService;
import com.adc.da.login.util.UserUtils;
import com.adc.da.research.project.dao.BudgetDetailHistoryEODao;
import com.adc.da.research.project.dao.BudgetFundHistoryEODao;
import com.adc.da.research.project.dao.CheckTargetHistoryEODao;
import com.adc.da.research.project.dao.MemberInfoHistoryEODao;
import com.adc.da.research.project.dao.ResearchProjectChangeEODao;
import com.adc.da.research.project.dao.WorkProgressHistoryEODao;
import com.adc.da.research.project.entity.ProjectDataEO;
import com.adc.da.research.project.entity.ResearchProjectChangeEO;
import com.adc.da.research.project.page.ProjectDataEOPage;
import com.adc.da.research.project.page.ResearchProjectChangeEOPage;
import com.adc.da.research.project.vo.ChangeProjectVO;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.StringUtils;
import com.adc.da.util.utils.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *
 * <br>
 * <b>功能：</b>RS_RESEARCH_PROJECT_CHANGE ResearchProjectChangeEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-11-06 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("researchProjectChangeEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class ResearchProjectChangeEOService extends BaseService<ResearchProjectChangeEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(ResearchProjectChangeEOService.class);

    @Autowired
    private ResearchProjectChangeEODao dao;
    @Autowired
    private ProjectDataEOService projectDataEOService;

    @Autowired
    private BudgetFundHistoryEODao budgetFundHistoryEODao;

    @Autowired
    private BudgetDetailHistoryEODao budgetDetailHistoryEODao;

    @Autowired
    private WorkProgressHistoryEODao workProgressHistoryEODao;

    @Autowired
    private CheckTargetHistoryEODao checkTargetHistoryEODao;

    @Autowired
    private MemberInfoHistoryEODao memberInfoHistoryEODao;


    public ResearchProjectChangeEODao getDao() {
        return dao;
    }

    public List<Map<String,Object>>  queryByTimeList(ResearchProjectChangeEOPage page) throws Exception {
        List<Map<String,Object>> mapList=new ArrayList<>();
        List<ResearchProjectChangeEO> list=   this.queryByList(page);
        Map<String,Object> map =new HashMap<>();
        for (ResearchProjectChangeEO rp:list) {
            SimpleDateFormat sf=new SimpleDateFormat("yyyy-MM-dd");
            String changeTime = sf.format(rp.getChangeTime());
            page.setChangeTime(changeTime);
            List<ResearchProjectChangeEO> Timelist=  this.queryByList(page);
            map.put("time",rp.getChangeTime());
            map.put("children",Timelist);
            mapList.add(map);
        }
        return mapList;
    }

    //    获取项目名称 排除已选过的项目
    public List<ProjectDataEO> getProjectName(ProjectDataEOPage page)throws Exception{
        List<ProjectDataEO> returnList = new ArrayList<>();
        List<ProjectDataEO> list;
        page.setStageName("项目执行");
        list=projectDataEOService.queryByList(page);
        page.setStageName("验收结项");
        list.addAll(projectDataEOService.queryByList(page));
        for(ProjectDataEO p:list){

            if (StringUtils.isEmpty(p.getChangeTime())){

                returnList.add(p);
            }
        }
        return returnList;
    }

    /**
     * 变更数据提交
     * @param listMap
     */
    public void saveChange(Map<String, List<ChangeProjectVO>> listMap,String projectId) throws Exception {
        UserEO user = UserUtils.getUser();
        if (StringUtils.isEmpty(user)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        List<ResearchProjectChangeEO> researchProjectChangeEOList=new ArrayList<>();

        List<ChangeProjectVO>  insertList =new ArrayList<>();
        List<ChangeProjectVO>  workProgressList = listMap.get("项目执行");
        if(CollectionUtils.isNotEmpty(workProgressList)) {
            insertList.addAll(workProgressList);
        }
        List<ChangeProjectVO>  checkTargetList = listMap.get("考核指标");
        if(CollectionUtils.isNotEmpty(checkTargetList)) {
            insertList.addAll(checkTargetList);
        }
        List<ChangeProjectVO>   fundChangeList = listMap.get("经费预算");
        if(CollectionUtils.isNotEmpty(fundChangeList)) {
            insertList.addAll(fundChangeList);
        }
        List<ChangeProjectVO>  memberInfoList = listMap.get("项目成员");
        if(CollectionUtils.isNotEmpty(memberInfoList)) {
            insertList.addAll(memberInfoList);
        }
        if(CollectionUtils.isNotEmpty(insertList)){


            for (ChangeProjectVO c:insertList){
                ResearchProjectChangeEO researchProjectChangeEO=new ResearchProjectChangeEO();
                String id=UUID.randomUUID10();
                researchProjectChangeEO.setChangeAttribute(c.getChangeProperty());
                researchProjectChangeEO.setChangeContent(c.getChangeContext());
                researchProjectChangeEO.setId(id);
                researchProjectChangeEO.setChangeTime(new Date());
                researchProjectChangeEO.setProjectId(projectId);
                researchProjectChangeEO.setChangePersonal(c.getChangeUser());
                researchProjectChangeEO.setCreateTime(new Date());
                researchProjectChangeEOList.add(researchProjectChangeEO);

            }
            this.batchInsertSelective(researchProjectChangeEOList);
        }



        try {
            ProjectDataEO dataEO=new ProjectDataEO();
            dataEO.setId(projectId);
            dataEO.setChangeStatus("审核中");
            dataEO.setChangeTime(new Date());
            projectDataEOService.updateByPrimaryKeySelective(dataEO);
        } catch (Exception e) {
            logger.error(e.toString());
        }

    }

    /**
     * 批量新增
     *
     * @param researchProjectChangeEOS
     */
    public void batchInsertSelective(List<ResearchProjectChangeEO> researchProjectChangeEOS) {

        UserEO user = UserUtils.getUser();
        if (ObjectUtil.isNull(user)){
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }

        dao.batchInsertSelective(researchProjectChangeEOS);
    }

    /**
     * 变更删除
     * @param eo
     */
    public void  deleteChange(ProjectDataEO eo){
        String projectId=eo.getId();
        String checkTypeId="";
        try {
            ProjectDataEO dataEO=new ProjectDataEO();
            dataEO.setId(projectId);
            dataEO.setChangeStatus("");
            dataEO.setChangeTime(null);
            projectDataEOService.updateByPrimaryKeySelective(dataEO);
        } catch (Exception e) {
            logger.error(e.toString());
        }

        //预算表
        budgetFundHistoryEODao.deleteByProjectId(projectId);
        //预算明细
        budgetDetailHistoryEODao.deleteByProjectId(projectId);
        //工作安排
        workProgressHistoryEODao.deleteByProjectId(projectId);
       //考核指标
        checkTargetHistoryEODao.deleteByProjectId(projectId,checkTypeId);
       //项目成员
        memberInfoHistoryEODao.deleteByProjectId(projectId);

        //变更表
        dao.deleteByProjectId(projectId);


    }

}
