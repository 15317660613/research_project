package com.adc.da.research.project.service;

import cn.hutool.core.util.ObjectUtil;
import com.adc.da.base.service.BaseService;
import com.adc.da.login.util.UserUtils;
import com.adc.da.research.project.dao.AnnexFileEODao;
import com.adc.da.research.project.entity.AnnexFileEO;
import com.adc.da.research.project.entity.ImplementationProcEO;
import com.adc.da.research.project.entity.ImplementationProcFileEO;
import com.adc.da.research.project.entity.ProjectDataEO;
import com.adc.da.research.project.page.ImplementationProcEOPage;
import com.adc.da.research.project.page.ProjectDataEOPage;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *
 * <br>
 * <b>功能：</b>RS_ANNEX_FILE AnnexFileEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-10-30 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("annexFileEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class AnnexFileEOService extends BaseService<AnnexFileEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(AnnexFileEOService.class);

    @Autowired
    private AnnexFileEODao dao;
    @Autowired
    private ProjectDataEOService projectDataEOService;
    @Autowired
    private ImplementationProcEOService implementationProcEOService;
    @Autowired
    private ImplementationProcFileEOService implementationProcFileEOService;


    public AnnexFileEODao getDao() {
        return dao;
    }

    /**
     * 批量新增
     *
     * @param annexFileEOS
     */
    public void batchInsertSelective(List<AnnexFileEO> annexFileEOS) throws Exception {

        ArrayList<Map<String, String>> maps = new ArrayList<>();
        Map<String, String> map=new HashMap<>();
        ProjectDataEOPage projectDataEOPage=new ProjectDataEOPage();
        projectDataEOPage.setId(annexFileEOS.get(0).getProjectId());

        UserEO user = UserUtils.getUser();
        if (ObjectUtil.isNull(user)){
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }

        for (AnnexFileEO m:annexFileEOS) {
            m.setId(UUID.randomUUID10());
            m.setCreateUserId(user.getUsid());
            m.setCreateUserName(user.getUsname());
            m.setCreateTime(new Date());
            map.put("id","'"+m.getFileId()+"'");
            map.put("name","'"+m.getExt1()+"'");
            maps.add(map);
            dao.insertSelective(m);
        }
            //添加项目执行过程文件表
        ImplementationProcFileEO procFileEO = new ImplementationProcFileEO();
        procFileEO.setId(UUID.randomUUID10());
        procFileEO.setProjectId(annexFileEOS.get(0).getProjectId());

        List<ProjectDataEO> projectDataEOS = projectDataEOService.queryByPage(projectDataEOPage);//获取执行过程id
        procFileEO.setProcessId(projectDataEOS.get(0).getImplementationId());
       // procFileEO.setFileId(maps.toString());
        procFileEO.setExt1(maps.toString());
        implementationProcFileEOService.insertSelective(procFileEO);

        //修改项目状态
        ProjectDataEO projectDataEO = new ProjectDataEO();
        projectDataEO.setId(annexFileEOS.get(0).getProjectId());
        if(!annexFileEOS.get(0).getAnnexFileType().equals("项目申报书附件")&&!annexFileEOS.get(0).getAnnexFileType().equals("2")) {
            projectDataEO.setProjectStatus("待评审");
            //查询当前项目所有阶段
            ImplementationProcEOPage implementationProcEOPage = new ImplementationProcEOPage();
            implementationProcEOPage.setProjectId(projectDataEOS.get(0).getId());
            implementationProcEOPage.setOrderBy("status");
            implementationProcEOPage.setOrder("ASC");
            List<ImplementationProcEO> implementationProcEOS = implementationProcEOService.queryByList(implementationProcEOPage);


            for (int j = 0; j < implementationProcEOS.size(); j++) {

                if (implementationProcEOS.get(j).getProceeStageId().equals(projectDataEOS.get(0).getStageId())) {
                    //当包含当前阶段时修改值
                    ImplementationProcEO impEO = new ImplementationProcEO();
                    impEO.setId(implementationProcEOS.get(j).getId());
                    impEO.setEndTime(new Date());
                    impEO.setStatus(0);
                    implementationProcEOService.updateByPrimaryKeySelective(impEO);
                    //下一条修改值
                    /*if (j + 1 < implementationProcEOS.size()) {
                        ImplementationProcEO impEONext = new ImplementationProcEO();
                        impEONext.setId(implementationProcEOS.get(j + 1).getId());
                        impEONext.setStartTime(new Date());
                        impEONext.setStatus(1);
                        implementationProcEOService.updateByPrimaryKeySelective(impEONext);
                   }*/
                }
                if (implementationProcEOS.get(j).getProceeStageId().equals("STAGE01")) {
                    ImplementationProcEO impEONext = new ImplementationProcEO();
                    impEONext.setId(implementationProcEOS.get(j).getId());
                    impEONext.setStartTime(new Date());
                    impEONext.setStatus(1);
                    implementationProcEOService.updateByPrimaryKeySelective(impEONext);
                }

            }

        }
        projectDataEOService.updateByPrimaryKeySelective(projectDataEO);


       // dao.batchInsertSelective(annexFileEOS);
    }

    public void deleteByProjectId(String projectId){
        dao.deleteByProjectId(projectId);
    }

    //批量新增附件
    public void batchInsertAnnexFile(List<AnnexFileEO> annexFileEOList) throws Exception {
        dao.batchInsertSelective(annexFileEOList);
    }

    public AnnexFileEO insertAnnexFileEO(AnnexFileEO annexFileEO) throws Exception {
        UserEO loginUserEO = UserUtils.getUser();
        if (ObjectUtil.isNull(loginUserEO)){
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        annexFileEO.setCreateTime(new Date());
        annexFileEO.setCreateUserId(loginUserEO.getUsid());
        annexFileEO.setCreateUserName(loginUserEO.getUsname());
        annexFileEO.setDelFlag(0);
        annexFileEO.setId(UUID.randomUUID10());
        ImplementationProcEOPage implementationProcEOPage = new ImplementationProcEOPage();
        implementationProcEOPage.setProjectId(annexFileEO.getProjectId());
        implementationProcEOPage.setStatus("1"); //当前阶段的状态为1
        try {
            List<ImplementationProcEO> implementationProcEOList = implementationProcEOService.queryByList(implementationProcEOPage);
            if(CollectionUtils.isNotEmpty(implementationProcEOList)){
                annexFileEO.setProceeStageId(implementationProcEOList.get(0).getId());
            }
        } catch (Exception e) {
            logger.error(e.toString());
        }
        dao.insertSelective(annexFileEO);
        return annexFileEO;
    }

    public List<AnnexFileEO> batchInsertAnnexFileEO(List<AnnexFileEO> annexFileEOList) {
        UserEO loginUserEO = UserUtils.getUser();
        if (ObjectUtil.isNull(loginUserEO)){
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        if(CollectionUtils.isEmpty(annexFileEOList)){
            return new ArrayList<>();
        }
        //批量插入之前 先清空之前已经存在的数据
        StringBuilder annexFileId = new StringBuilder();
        for(AnnexFileEO annexFileEO:annexFileEOList){
            String tmpAnnexFileEOId = annexFileEO.getId();
            if(StringUtils.isNotEmpty(tmpAnnexFileEOId)){
                annexFileId.append("'").append(tmpAnnexFileEOId).append("',");
            }
        }
        String annexFileIdStr = annexFileId.toString();
        if(StringUtils.isNotEmpty(annexFileIdStr)){
            String annexFileIds = "("+annexFileIdStr.substring(0,annexFileIdStr.length()-1)+")";
            dao.deleteByAnnexFileIds(annexFileIds);
        }

        String proceeStageId = "";
        ImplementationProcEOPage implementationProcEOPage = new ImplementationProcEOPage();
        implementationProcEOPage.setProjectId(annexFileEOList.get(0).getProjectId());
        implementationProcEOPage.setStatus("1"); //当前阶段的状态为1
        try {
            List<ImplementationProcEO> implementationProcEOList = implementationProcEOService.queryByList(implementationProcEOPage);
            if(CollectionUtils.isNotEmpty(implementationProcEOList)){
                proceeStageId = implementationProcEOList.get(0).getId();
            }
        } catch (Exception e) {
            logger.error(e.toString());
        }
        for(AnnexFileEO annexFileEO:annexFileEOList){
            annexFileEO.setCreateTime(new Date());
            annexFileEO.setCreateUserId(loginUserEO.getUsid());
            annexFileEO.setCreateUserName(loginUserEO.getUsname());
            annexFileEO.setDelFlag(0);
            annexFileEO.setId(UUID.randomUUID10());
            annexFileEO.setProceeStageId(proceeStageId);
        }
        try{
            dao.batchInsertSelective(annexFileEOList);
        } catch (Exception e){
            logger.error(e.toString());
            throw new AdcDaBaseException("批量新增项目文件失败！");
        }
        return annexFileEOList;
    }
}
