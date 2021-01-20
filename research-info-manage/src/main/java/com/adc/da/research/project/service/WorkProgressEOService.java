package com.adc.da.research.project.service;

import cn.hutool.core.util.ObjectUtil;
import com.adc.da.base.service.BaseService;
import com.adc.da.login.util.UserUtils;
import com.adc.da.research.project.dao.ImplementationProcFileEODao;
import com.adc.da.research.project.dao.ProjectDataEODao;
import com.adc.da.research.project.dao.WorkProgressEODao;
import com.adc.da.research.project.entity.ImplementationProcFileEO;
import com.adc.da.research.project.entity.ProjectDataEO;
import com.adc.da.research.project.entity.WorkProgressEO;
import com.adc.da.research.project.page.ImplementationProcFileEOPage;
import com.adc.da.research.project.page.ProjectDataEOPage;
import com.adc.da.research.project.page.WorkProgressEOPage;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
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
 * <b>功能：</b>RS_WORK_PROGRESS WorkProgressEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2020-10-30 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("workProgressEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class WorkProgressEOService extends BaseService<WorkProgressEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(WorkProgressEOService.class);

    @Autowired
    private WorkProgressEODao dao;

    @Autowired
    private ProjectDataEODao projectDataEODao;

    @Autowired
    private WorkProgressEODao workProgressEODao;

    @Autowired
    private ImplementationProcFileEODao fileDao;
    @Autowired
    private ImplementationProcFileEOService implementationProcFileEOService;

    @Autowired
    private ImplementationProcFileEODao implementationProcFileEODao;

    public WorkProgressEODao getDao() {
        return dao;
    }

    //根据projectId删除
    public void deleteByProjectId(String projectId){
        dao.deleteByProjectId(projectId);

    }

//    项目执行-->发起检查弹窗中-->确定按钮
    public void submitComment(WorkProgressEO progressEO) throws Exception{
        UserEO user = UserUtils.getUser();
        if (StringUtils.isEmpty(user)){
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        progressEO.setCreateUserName(user.getUsname());
        progressEO.setCreateUserId(user.getUsid());
        progressEO.setCreateTime(new Date());
        progressEO.setExt3("0");
        this.updateByPrimaryKeySelective(progressEO);

        //新增或修改附件
        ImplementationProcFileEOPage fileEOPage=new ImplementationProcFileEOPage();
        fileEOPage.setProcessId(progressEO.getProcessId());
        fileEOPage.setProjectId(progressEO.getProjectId());
        fileEOPage.setExt2("发起检查");
        fileEOPage.setExt3(progressEO.getId());
        List<ImplementationProcFileEO> implementationProcFileEOS = implementationProcFileEOService.queryByList(fileEOPage);
        if(implementationProcFileEOS.size()>0){
            ImplementationProcFileEO fileEO=new ImplementationProcFileEO();
            fileEO.setId(implementationProcFileEOS.get(0).getId());
            fileEO.setFileId(progressEO.getFileArr());
            implementationProcFileEOService.updateByPrimaryKeySelective(fileEO);
        }else {
            ImplementationProcFileEO fileEO=new ImplementationProcFileEO();
            fileEO.setId(UUID.randomUUID10());
            fileEO.setProjectId(progressEO.getProjectId());
            fileEO.setProcessId(progressEO.getProcessId());
            fileEO.setFileId(progressEO.getFileArr());
            fileEO.setExt2("发起检查");
            fileEO.setExt3(progressEO.getId());
            fileEO.setExt4("0");//撤回状态
            implementationProcFileEOService.insertSelective(fileEO);

        }
        projectDataEODao.updateProjectStatusByProjectId(progressEO.getProjectId(),"执行中");


    }

    /**
     * 修改提交弹窗中的确定按钮
     * @param workProgressEOList
     * @throws Exception
     */
    public void  submitUpdateInfo(List<WorkProgressEO> workProgressEOList) throws Exception{

        if (workProgressEOList.size()>0){
            for (WorkProgressEO w:workProgressEOList) {
                submitComment(w);

            }
        }

    }
    //    项目执行-->撤回
    public void recall(WorkProgressEO progressEO) throws Exception {

        projectDataEODao.updateProjectStatusByProjectId(progressEO.getProjectId(),"执行中");

        WorkProgressEOPage workProgressEOPage=new WorkProgressEOPage();
        workProgressEOPage.setProjectId(progressEO.getProjectId());
        workProgressEOPage.setProcessId(progressEO.getProcessId());
        List<WorkProgressEO> workProgressEOList = this.queryByList(workProgressEOPage);
       if(workProgressEOList.size()>0) {
           for (WorkProgressEO w : workProgressEOList) {
               w.setExt3("1");
               this.updateByPrimaryKeySelective(w);
           }
       }


    }
    //根据id获取检查详情
    public  WorkProgressEO getCheckById(WorkProgressEO progressEO) throws Exception {

        WorkProgressEO workProgressEO = this.selectByPrimaryKey(progressEO.getId());
        ImplementationProcFileEOPage fileEO=new ImplementationProcFileEOPage();
        fileEO.setProcessId(workProgressEO.getProcessId());
        fileEO.setProjectId(workProgressEO.getProjectId());
        fileEO.setExt2("发起检查");
        fileEO.setExt3(workProgressEO.getId());
        List<ImplementationProcFileEO> implementationProcFileEOS = implementationProcFileEOService.queryByList(fileEO);
        if(implementationProcFileEOS.size()>0){
            workProgressEO.setFileArr(implementationProcFileEOS.get(0).getFileId());
        }

        return workProgressEO;

    }

    /**
     *项目执行 修改提交 检查页面查看所有发起检查 参数项目id,执行过程id
     * @return
     */
    public  List<WorkProgressEO> getSubmitCheckList(WorkProgressEOPage page) throws Exception {
        List<WorkProgressEO> returnList = new ArrayList<>();
        page.setExamineType("检查");
        List<WorkProgressEO> workProgressEOList = this.queryByList(page);

        for (WorkProgressEO w:workProgressEOList) {

            if (w.getExt3().equals("0")&&StringUtils.isEmpty(w.getCheckResult())){
                ImplementationProcFileEOPage fileEO=new ImplementationProcFileEOPage();
                fileEO.setProcessId(w.getProcessId());
                fileEO.setProjectId(w.getProjectId());
                fileEO.setExt2("发起检查");
                fileEO.setExt3(w.getId());
                List<ImplementationProcFileEO> implementationProcFileEOS = implementationProcFileEOService.queryByList(fileEO);
                if(implementationProcFileEOS.size()>0){
                    w.setFileArr(implementationProcFileEOS.get(0).getFileId());
                }
                returnList.add(w);
            }else   if (w.getExt3().equals("0")&&w.getCheckResult().equals("不通过")){

                ImplementationProcFileEOPage fileEO=new ImplementationProcFileEOPage();
                fileEO.setProcessId(w.getProcessId());
                fileEO.setProjectId(w.getProjectId());
                fileEO.setExt2("发起检查");
                fileEO.setExt3(w.getId());
                List<ImplementationProcFileEO> implementationProcFileEOS = implementationProcFileEOService.queryByList(fileEO);
                if(implementationProcFileEOS.size()>0){
                    w.setFileArr(implementationProcFileEOS.get(0).getFileId());
                }
                ImplementationProcFileEOPage checkFileEO=new ImplementationProcFileEOPage();
                checkFileEO.setProcessId(w.getProcessId());
                checkFileEO.setProjectId(w.getProjectId());
                checkFileEO.setExt2("检查");
                checkFileEO.setExt3(w.getId());
                List<ImplementationProcFileEO> checkFileEOS = implementationProcFileEOService.queryByList(checkFileEO);
                if(checkFileEOS.size()>0){
                    w.setCheckFileArr(checkFileEOS.get(0).getFileId());
                }
                returnList.add(w);
            }

        }
        return returnList;

    }

    /**
     *项目执行 检查提交按钮 参数项目id,执行过程id
     * @return
     */
    public void  insertSubmitCheckList( List<WorkProgressEO> eoList) throws Exception {
        UserEO user = UserUtils.getUser();
        if (StringUtils.isEmpty(user)){
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
            if(eoList.size()>0){
                int i=0;
                for(WorkProgressEO w:eoList){

                    ImplementationProcFileEOPage fileEOPage=new ImplementationProcFileEOPage();
                    fileEOPage.setProjectId(w.getProjectId());
                    fileEOPage.setProcessId(w.getProcessId());
                    fileEOPage.setExt2("检查");
                    fileEOPage.setExt3(w.getId());
                    List<ImplementationProcFileEO> implementationProcFileEOS = implementationProcFileEOService.queryByList(fileEOPage);
                    if(implementationProcFileEOS.size()>0){
                        ImplementationProcFileEO fileEO=new ImplementationProcFileEO();
                        fileEO.setId(implementationProcFileEOS.get(0).getId());
                        fileEO.setFileId(w.getCheckFileArr());
                        implementationProcFileEOService.updateByPrimaryKeySelective(fileEO);
                    }else{
                        ImplementationProcFileEO fileEO=new ImplementationProcFileEO();
                        fileEO.setId(UUID.randomUUID10());
                        fileEO.setProjectId(w.getProjectId());
                        fileEO.setProcessId(w.getProcessId());
                        fileEO.setFileId(w.getCheckFileArr());
                        fileEO.setExt2("检查");
                        fileEO.setExt3(w.getId());
                        fileEO.setExt4("0");//撤回状态
                        implementationProcFileEOService.insertSelective(fileEO);

                    }

                    w.setCheckUser(user.getUsname());
                    w.setCheckUserId(user.getUsid());
                    w.setActualExamineTime(new Date());//实际检查时间
                    this.updateByPrimaryKeySelective(w);
                    if(w.getCheckResult().equals("不通过")){
                        i=1;
                    }
                }
                if(i==0){
                    projectDataEODao.updateProjectStatusByProjectId(eoList.get(0).getProjectId(),"检查通过");
                }else{
                    projectDataEODao.updateProjectStatusByProjectId(eoList.get(0).getProjectId(),"检查未通过");
                }
            }
    }





//  项目验收  修改提交 查询详情
    public WorkProgressEO getInfo(WorkProgressEOPage page) throws Exception {
        page.setExamineType("结项验收");
        List<WorkProgressEO> list = this.queryByList(page);
        WorkProgressEO workProgressEO = list.get(0);
        ImplementationProcFileEOPage fileEO=new ImplementationProcFileEOPage();
        fileEO.setProcessId(page.getProcessId());
        fileEO.setProjectId(page.getProjectId());

        List<ImplementationProcFileEO> implementationProcFileEO = implementationProcFileEODao.queryByList(fileEO);
        if(implementationProcFileEO.size()>0) {
            for (ImplementationProcFileEO s : implementationProcFileEO) {
                if (s.getExt2().equals("验收申请")) {
                    workProgressEO.setFileArr(s.getFileId());
                } else if (s.getExt2().equals("验收结项")) {
                    workProgressEO.setCheckFileArr(s.getFileId());
                }
            }
        }
        return workProgressEO;
    }


    //    项目验收-->验收申请的确定按钮（将状态待验收修改为结项未答辩）
    public ResponseMessage checkApply(WorkProgressEOPage page) throws Exception{
        UserEO user = UserUtils.getUser();
        if (ObjectUtil.isNull(user)){
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }


        WorkProgressEOPage workProgressEOPage=new WorkProgressEOPage();
        workProgressEOPage.setProjectId(page.getProjectId());
        workProgressEOPage.setExamineType("结项验收");
        List<WorkProgressEO> workProgressEOList = workProgressEODao.queryByList(workProgressEOPage);
        if(workProgressEOList.size()>0){
            WorkProgressEO workProgressEO =new WorkProgressEO();
            workProgressEO.setId(workProgressEOList.get(0).getId());
            workProgressEO.setProjectId(page.getProjectId());
            workProgressEO.setProcessId(page.getProcessId());
            workProgressEO.setCreateUserId(user.getUsid());
            workProgressEO.setCreateUserName(user.getUsname());
            workProgressEO.setCreateTime(new Date());
            workProgressEO.setCheckMethod(page.getCheckMethod());
            workProgressEO.setExt2(page.getExt2());
            workProgressEO.setExt3("0");
            workProgressEO.setReviewRemark(page.getReviewRemark());
            workProgressEODao.updateByPrimaryKeySelective(workProgressEO);
            //更新文件表
            ImplementationProcFileEOPage fileEOPage=new ImplementationProcFileEOPage();
            fileEOPage.setProcessId(workProgressEO.getProcessId());
            fileEOPage.setProjectId(workProgressEO.getProjectId());
            fileEOPage.setExt2("验收申请");
            fileEOPage.setExt3(workProgressEO.getId());
            List<ImplementationProcFileEO> implementationProcFileEOS = implementationProcFileEOService.queryByList(fileEOPage);
            if(implementationProcFileEOS.size()>0){
                ImplementationProcFileEO fileEO=new ImplementationProcFileEO();
                fileEO.setId(implementationProcFileEOS.get(0).getId());
                fileEO.setFileId(page.getFileArr());
                implementationProcFileEOService.updateByPrimaryKeySelective(fileEO);
            }else {

                ImplementationProcFileEO fileEO=new ImplementationProcFileEO();
                fileEO.setId(UUID.randomUUID10());
                fileEO.setProjectId(page.getProjectId());
                fileEO.setProcessId(page.getProcessId());
                fileEO.setFileId(page.getFileArr());
                fileEO.setExt2("验收申请");
                fileEO.setExt3(workProgressEO.getId());
                fileEO.setExt4("0");//撤回状态
                implementationProcFileEOService.insertSelective(fileEO);
            }
        }



        projectDataEODao.updateProjectStatusByProjectId(page.getProjectId(),"结项未答辩");
        return Result.success("success");
    }

    //    项目验收-->验收结项-->通过按钮（判断项目类型，内部项目状态设置为验收通过，外部项目设置为内部验收通过）
    public ResponseMessage checkProjectPass(WorkProgressEOPage page) throws  Exception{
        UserEO user = UserUtils.getUser();
        if (ObjectUtil.isNull(user)){
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }

        ProjectDataEO projectData=new ProjectDataEO();

        WorkProgressEO work=new WorkProgressEO();
        ProjectDataEOPage projectDataEOPage=new ProjectDataEOPage();
        projectDataEOPage.setId(page.getProjectId());
        //判断项目类型为内部项目还是外部项目
        List<ProjectDataEO> list=projectDataEODao.queryByList(projectDataEOPage);

            //获取项目类型
            String type=list.get(0).getProjectTypeName();
            String id=list.get(0).getId();
            if(type.equals("国家级")||type.equals("省部级")||type.equals("国际合作")||type.equals("其他")){
                // 项目类型为外部项目
                projectData.setId(page.getProjectId());
                projectData.setProjectStatus("内部验收通过");
                projectDataEODao.updateByPrimaryKeySelective(projectData);
            }else {
            // 项目类型为内部项目
                projectData.setId(page.getProjectId());
                projectData.setProjectStatus("验收通过");
                projectDataEODao.updateByPrimaryKeySelective(projectData);
            }


        submitApplicationFile(page);



            //获取项目验收的评论，赋给ApproveComment
            work.setApproveComment(page.getApproveComment());
            work.setId(page.getId());
            work.setCheckUser(user.getUsname());
            work.setCheckUserId(user.getUsid());
            work.setActualExamineTime(new Date());//实际结项时间
            workProgressEODao.updateByPrimaryKeySelective(work);

        return Result.success("success");
    }

    // 项目验收-->验收结果-->不通过按钮(将项目状态修改为延期验收,并且上传附件)
    public ResponseMessage checkProjectReject(WorkProgressEOPage page) throws Exception{
        UserEO user = UserUtils.getUser();
        if (ObjectUtil.isNull(user)){
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }

        WorkProgressEO work=new WorkProgressEO();
        projectDataEODao.updateProjectStatusByProjectId(page.getProjectId(),"延期验收");

        // 上传验收不通过的文件,设置文件的备注
        submitApplicationFile(page);

        work.setApproveComment(page.getApproveComment());
        work.setId(page.getId());
        work.setCheckUser(user.getUsname());
        work.setCheckUserId(user.getUsid());
        work.setActualExamineTime(new Date());//实际结项时间
        workProgressEODao.updateByPrimaryKeySelective(work);
        return Result.success("success");
    }

    public void submitApplicationFile(WorkProgressEOPage page) throws Exception {

        //新增或修改对应附件
        ImplementationProcFileEOPage fileEOPage=new ImplementationProcFileEOPage();
        fileEOPage.setProcessId(page.getProcessId());
        fileEOPage.setProjectId(page.getProjectId());
        fileEOPage.setExt2("验收结项");
        fileEOPage.setExt3(page.getId());
        List<ImplementationProcFileEO> implementationProcFileEOS = implementationProcFileEOService.queryByList(fileEOPage);
        if(implementationProcFileEOS.size()>0){
            ImplementationProcFileEO fileEO=new ImplementationProcFileEO();
            fileEO.setId(implementationProcFileEOS.get(0).getId());
            fileEO.setFileId(page.getCheckFileArr());
            implementationProcFileEOService.updateByPrimaryKeySelective(fileEO);
        }else {
            ImplementationProcFileEO file=new ImplementationProcFileEO();
            file.setId(UUID.randomUUID10());
            file.setExt2("验收结项");
            file.setExt3(page.getId());
            file.setFileId(page.getCheckFileArr());
            file.setProjectId(page.getProjectId());
            file.setProcessId(page.getProcessId());
            fileDao.insertSelective(file);
        }
    }

    // 项目验收-->操作栏的撤回操作
    public  void reBack(WorkProgressEOPage page) throws Exception{
        projectDataEODao.updateProjectStatusByProjectId(page.getProjectId(),"待验收");

    }

    //项目验收-->修改提交-->提交按钮 Temporary don't have to use
    public ResponseMessage updateSubmit(WorkProgressEOPage page){
        WorkProgressEO work=new WorkProgressEO();
        ImplementationProcFileEO file=new ImplementationProcFileEO();
        String projectId=page.getProjectId();
        // 获取到work表中修改的id值,ext2是验收申请的备注
        WorkProgressEO workId=workProgressEODao.getWorkId(projectId);
        ImplementationProcFileEO fileId=implementationProcFileEODao.getFileId(projectId);
        work.setId(workId.getId());
        work.setCheckMethod(page.getCheckMethod());
        work.setExamineTime(new Date());
        work.setExt2(page.getExt2());
//        获取到file表中修改的id值
        file.setId(fileId.getId());
        file.setFileId(page.getCheckFileArr());
        file.setExt2(page.getExt2());
        workProgressEODao.updateByPrimaryKeySelective(work);
        fileDao.updateByPrimaryKeySelective(file);
        return Result.success("success");
    }

    /**
     * 批量新增
     *
     * @param workProgressEOS
     */
    public void batchInsertSelective(List<WorkProgressEO> workProgressEOS) {

        UserEO user = UserUtils.getUser();
        if (StringUtils.isEmpty(user)){
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        if(StringUtils.isNotEmpty(workProgressEOS.get(0).getExamineType())){
            for (WorkProgressEO w:workProgressEOS) {
                dao.deleteByProjectId(w.getProjectId());
                w.setId(UUID.randomUUID10());
                w.setCreateUserId(user.getUsid());
                w.setCreateUserName(user.getUsname());
                w.setCreateTime(new Date());
                w.setExt3("0");

            }
            dao.batchInsertSelective(workProgressEOS);
        }else {

            dao.deleteByProjectId(workProgressEOS.get(0).getProjectId());
        }

    }

    public WorkProgressEO saveWorkProgress(WorkProgressEO workProgressEO){
        UserEO loginUserEO = UserUtils.getUser();
        if(StringUtils.isEmpty(loginUserEO)){
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        workProgressEO.setId(UUID.randomUUID10());
        workProgressEO.setCreateTime(new Date());
        workProgressEO.setCreateUserId(loginUserEO.getUsid());
        workProgressEO.setCreateUserName(loginUserEO.getUsname());
        workProgressEO.setDelFlag(0);
        dao.insertSelective(workProgressEO);
        return workProgressEO;
    }

    public WorkProgressEO updateWorkProgress(WorkProgressEO workProgressEO){
        UserEO loginUserEO = UserUtils.getUser();
        if(StringUtils.isEmpty(loginUserEO)){
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        workProgressEO.setModifyTime(new Date());
        workProgressEO.setCreateUserId(loginUserEO.getUsid());
        workProgressEO.setCreateUserName(loginUserEO.getUsname());
        dao.updateByPrimaryKeySelective(workProgressEO);
        return workProgressEO;
    }

    public void deleteLogicInBatch(List<String> ids) {
        this.dao.deleteLogicInBatch(ids);
    }


    /**
     * ADC项目合同书考核指标
     * @param page
     * @return
     */
    public Map<String,List<WorkProgressEO>> getCheckTargetList(WorkProgressEOPage page) throws Exception {

        List<WorkProgressEO> workProgressEOS = this.queryByList(page);

        List<WorkProgressEO> topList= new ArrayList<>();
        List<WorkProgressEO> bottomList= new ArrayList<>();
        if(workProgressEOS.size()>0) {
            for (WorkProgressEO c : workProgressEOS) {
                if (c.getExamineType().equals("结项验收")) {
                    bottomList.add(c);
                } else if (!c.getExamineType().equals("项目验收")) {
                    topList.add(c);
                }

            }
        }

        Map<String,List<WorkProgressEO>>  map=new HashMap<>();
        map.put("topList",topList);
        map.put("bottomList",bottomList);
        return map;



    }

    /**
     * 项目执行发起检查下拉框
     * @param page
     * @return
     * @throws Exception
     */
    public List<WorkProgressEO> sponsorCheckDownList(WorkProgressEOPage page) throws Exception {

        List<WorkProgressEO> workProgressEOS = this.queryByList(page);

        List<WorkProgressEO> downList= new ArrayList<>();

        if(workProgressEOS.size()>0) {
            for (WorkProgressEO c : workProgressEOS) {
                if ((!c.getExamineType().equals("结项验收") && !c.getExamineType().equals("项目验收") && StringUtils.isEmpty(c.getCheckMethod()) )|| c.getExt3().equals("1")) {
                    downList.add(c);
                }

            }
        }

        return downList;



    }

}
