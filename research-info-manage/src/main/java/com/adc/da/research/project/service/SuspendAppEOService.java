package com.adc.da.research.project.service;

import com.adc.da.base.service.BaseService;
import com.adc.da.login.util.UserUtils;
import com.adc.da.research.project.dao.ProjectDataEODao;
import com.adc.da.research.project.dao.SuspendAppEODao;
import com.adc.da.research.project.entity.ProjectDataEO;
import com.adc.da.research.project.entity.SuspendAppEO;
import com.adc.da.research.project.page.ProjectDataEOPage;
import com.adc.da.research.project.page.SuspendAppEOPage;
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
import java.util.List;


/**
 *
 * <br>
 * <b>功能：</b>RS_SUSPEND_APP SuspendAppEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-10-30 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("suspendAppEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class SuspendAppEOService extends BaseService<SuspendAppEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(SuspendAppEOService.class);

    @Autowired
    private ProjectDataEODao projectDataEODao;

    @Autowired
    private SuspendAppEODao dao;

    @Autowired
    private ProjectDataEOService projectDataEOService;

    public SuspendAppEODao getDao() {
        return dao;
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
            SuspendAppEOPage suspendAppEOPage=new SuspendAppEOPage();
            suspendAppEOPage.setProjectId(p.getId());
            List<SuspendAppEO> suspendAppEOS = this.queryByList(suspendAppEOPage);
            if (suspendAppEOS.size()==0){

                returnList.add(p);
            }
        }
        return returnList;
    }

    //    分页显示项目终止页面信息
    public List<ProjectDataEO> getSuspendPage(ProjectDataEOPage page) throws Exception{

        List<ProjectDataEO> projectDataEOS = projectDataEODao.getSuspendPage(page);
        return projectDataEOS;
    }

    //    弹窗中的保存功能（新增一条数据，默认状态为待审核）
    public void insertOrUpdate(SuspendAppEO suspendAppEO) throws Exception{
        UserEO loginUserEO = UserUtils.getUser();
        if (StringUtils.isEmpty(loginUserEO)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }

        SuspendAppEOPage suspendAppEOPage =new SuspendAppEOPage();
        suspendAppEOPage.setProjectId(suspendAppEO.getProjectId());
        List<SuspendAppEO> suspendAppEOS = this.queryByList(suspendAppEOPage);
        if(suspendAppEOS.size()>0) {
            suspendAppEO.setId(suspendAppEOS.get(0).getId());
            dao.updateByPrimaryKeySelective(suspendAppEO);
        }else{
            suspendAppEO.setId(UUID.randomUUID10());
            suspendAppEO.setSuspendStage("待提交");
            suspendAppEO.setCreateTime(new Date());
            suspendAppEO.setCreateUserId(loginUserEO.getUsid());
            suspendAppEO.setCreateUserName(loginUserEO.getUsname());
            suspendAppEO.setDelFlag(0);
            dao.insertSelectiveSuspend(suspendAppEO);
        }


    }

    //    操作栏中的删除功能
    public void deleteById( String projectId) throws Exception {
        SuspendAppEOPage suspendAppEOPage=new SuspendAppEOPage();
        suspendAppEOPage.setProjectId(projectId);
        List<SuspendAppEO> suspendAppEOS = this.queryByList(suspendAppEOPage);
        if (suspendAppEOS.size()>0){
            //  修改中止表中的del_flag标识
            dao.deleteById(suspendAppEOS.get(0).getId());
        }


    }

//   弹窗中的提交功能  ----修改状态（审核中）
    public void submitInfo(SuspendAppEOPage suspendAppEO){
        SuspendAppEO sEo=new SuspendAppEO();
        List<SuspendAppEO> suspendAppEOS = dao.queryByList(suspendAppEO);
          if(suspendAppEOS.size()>0){
              sEo.setSuspendStage("审核中");
              sEo.setId(suspendAppEOS.get(0).getId());
              dao.updateByPrimaryKeySelective(sEo);
          }

    }

    //   操作栏中查看数据详情,存放在list当中
    public List<SuspendAppEO> selectInfoByPrimaryKey( SuspendAppEOPage suspendAppEOPage) throws Exception {
            return  this.queryByList(suspendAppEOPage);

    }

//    操作栏中编辑数据详情
    public void updateInfoByPrimaryKey(SuspendAppEO suspendAppEO) throws Exception {
        SuspendAppEOPage suspendAppEOPage =new SuspendAppEOPage();
        suspendAppEOPage.setProjectId(suspendAppEO.getProjectId());
        List<SuspendAppEO> suspendAppEOS = this.queryByList(suspendAppEOPage);
        if(suspendAppEOS.size()>0) {
            suspendAppEO.setId(suspendAppEOS.get(0).getId());
            dao.updateByPrimaryKeySelective(suspendAppEO);
        }


    }

}
