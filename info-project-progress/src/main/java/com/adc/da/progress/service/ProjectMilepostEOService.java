package com.adc.da.progress.service;

import com.adc.da.budget.repository.ProjectRepository;
import com.adc.da.file.dao.MyFileEODao;
import com.adc.da.file.entity.MyFileEO;
import com.adc.da.progress.dao.ProjectMilepostResultEODao;
import com.adc.da.progress.entity.ProjectMilepostResultEO;
import com.adc.da.progress.page.MyProjectMilepostEOPage;
import com.adc.da.progress.vo.ProjectMilepostResultVO;
import com.adc.da.sys.dao.UserEODao;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.sys.service.UserEOService;
import com.adc.da.util.http.Result;
import com.adc.da.util.utils.BeanMapper;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.DateUtils;
import com.adc.da.util.utils.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.adc.da.base.service.BaseService;
import com.adc.da.progress.dao.ProjectMilepostEODao;
import com.adc.da.progress.entity.ProjectMilepostEO;

import java.io.File;
import java.util.*;

/**
 *
 * <br>
 * <b>功能：</b>PR_PROJECT_MILEPOST ProjectMilepostEOService<br>
 * <b>作者：</b>code generator<br>
 * <b>日期：</b> 2019-07-29 <br>
 * <b>版权所有：<b>版权归北京卡达克数据技术中心所有。<br>
 */
@Service("projectMilepostEOService")
@Transactional(value = "transactionManager", readOnly = false, propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
public class ProjectMilepostEOService extends BaseService<ProjectMilepostEO, String> {

    private static final Logger logger = LoggerFactory.getLogger(ProjectMilepostEOService.class);

    @Autowired
    private ProjectMilepostEODao projectMilepostEODao;

    @Autowired
    private ProjectMilepostResultEODao projectMilepostResultEODao;

    @Autowired
    private UserEODao userEODao ;

    @Autowired
    private ProjectRepository projectRepository ;

    @Autowired
    private BeanMapper beanMapper;

    @Autowired
    private MyFileEODao fileEODao ;


    @Value("${file.path}")
    private String basePath;



    public ProjectMilepostEODao getDao() {
        return projectMilepostEODao;
    }


    public List<ProjectMilepostEO> page4Tips(MyProjectMilepostEOPage page)throws Exception{

        Integer rowCount = projectMilepostEODao.query4TipsByCount(page);
        page.getPager().setRowCount(rowCount);

        List<ProjectMilepostEO> projectMilepostEOList =  projectMilepostEODao.page4Tips(page);
        Date currentDate = DateUtils.getOnlyYMD(new Date());
        for (ProjectMilepostEO projectMilepostEO : projectMilepostEOList){
            List<ProjectMilepostResultEO> projectMilepostResultEOList = projectMilepostResultEODao.selectByMilepostId(projectMilepostEO.getId());
            StringBuilder stringBuilder = new StringBuilder();
            for (ProjectMilepostResultEO resultEO : projectMilepostResultEOList){
                stringBuilder.append(resultEO.getFileName()).append("、");
            }
            if (stringBuilder.lastIndexOf("、")>0) {
                stringBuilder.deleteCharAt(stringBuilder.lastIndexOf("、"));
            }
            projectMilepostEO.setResultName(stringBuilder.toString());

            projectMilepostEO.setProjectMilepostResultEOList(projectMilepostResultEOList);
            if(projectMilepostEO.getFinishStatus()==9){
                continue;
            }

            int compareToBegin = DateUtils.getOnlyYMD(projectMilepostEO.getMilepostBeginTime()).compareTo(currentDate);
            int compareToEnd = DateUtils.getOnlyYMD(projectMilepostEO.getMilepostEndTime()).compareTo(currentDate);
            //起始日期大于当前日期    未开始
            if(compareToBegin == 1){
                projectMilepostEO.setFinishStatus(0);
            }
            //起日期 ≤ 当前日期 且 止日期 ≥ 当前日期       未完成
            if((compareToBegin == -1 || compareToBegin == 0)&&(compareToEnd == 1 || compareToEnd == 0)){
                projectMilepostEO.setFinishStatus(3);
            }
            //未完成（已有风险）：起日期 ≤ 当前日期 且 止日期 ＜ 当前日期
            if((compareToBegin == -1 || compareToBegin == 0)&&(compareToEnd == -1)){
                projectMilepostEO.setFinishStatus(6);
            }

        }

//        Collections.sort(projectMilepostEOList, new Comparator<ProjectMilepostEO>() {
//            @Override
//            public int compare(ProjectMilepostEO o1, ProjectMilepostEO o2) {
//                 if (o1.getMilepostBeginTime().getTime() != o2.getMilepostBeginTime().getTime()){
//                     return o1.getMilepostBeginTime().getTime() > o2.getMilepostBeginTime().getTime() ? 1 : -1;
//                 }else {
//                     return o1.getMilepostEndTime().getTime() > o2.getMilepostEndTime().getTime() ? 1 : -1;
//                 }
//
//            }
//        });


        return projectMilepostEOList;

    }


    public List<ProjectMilepostEO> selectByStageId(String stageId){
        return  projectMilepostEODao.selectByStageId(stageId);
    }


    public ProjectMilepostEO getMilepostAndResult(String id) throws  Exception{
        List<ProjectMilepostResultVO> projectMilepostResultVOList = new ArrayList<>();

        ProjectMilepostEO projectMilepostEO = new ProjectMilepostEO() ;

        List<ProjectMilepostEO> projectMilepostEOList =  projectMilepostEODao.selectByStageId(id);
        if (CollectionUtils.isNotEmpty(projectMilepostEOList)){
            for (ProjectMilepostEO milepostEO : projectMilepostEOList){
                this.fillProjectMilepostEO(milepostEO,projectMilepostResultVOList);
            }
            List<MyFileEO> myStageFileEOList = fileEODao.selectByForeignId(id);
            if (CollectionUtils.isNotEmpty(myStageFileEOList)){
                for (MyFileEO fileEO : myStageFileEOList){
                    ProjectMilepostResultVO projectMilepostResultVO = new ProjectMilepostResultVO();
                    projectMilepostResultVO.setForeignId(id);
                    this.fillProjectMilepostResultVO(fileEO,projectMilepostResultVO);
                    projectMilepostResultVOList.add(projectMilepostResultVO);
                }
            }
        }else {
            projectMilepostEO = projectMilepostEODao.selectByPrimaryKey(id);
            if (null == projectMilepostEO){
                return null;
            }
            this.fillProjectMilepostEO(projectMilepostEO,projectMilepostResultVOList);
        }

        projectMilepostEO.setProjectMilepostResultVOList(projectMilepostResultVOList);

        return projectMilepostEO ;

    }

    private ProjectMilepostEO fillProjectMilepostEO(ProjectMilepostEO projectMilepostEO,List<ProjectMilepostResultVO> projectMilepostResultVOList) throws  Exception{


        List<ProjectMilepostResultEO> projectMilepostResultEOList = projectMilepostResultEODao.selectByMilepostId(projectMilepostEO.getId()) ;
        for (ProjectMilepostResultEO resultEO: projectMilepostResultEOList){

            ProjectMilepostResultVO projectMilepostResultVO = new ProjectMilepostResultVO();
            projectMilepostResultVO.setId(resultEO.getId());
            projectMilepostResultVO.setResultFileName(resultEO.getFileName());

            List<MyFileEO> fileEOList =  fileEODao.selectByForeignId(resultEO.getId());
            if (CollectionUtils.isEmpty( fileEOList )){
                projectMilepostResultVOList.add(projectMilepostResultVO);
                continue;
            }
            for (MyFileEO fileEO :fileEOList) {
                this.fillProjectMilepostResultVO(fileEO,projectMilepostResultVO);
            }
            projectMilepostResultVOList.add(projectMilepostResultVO);
        }

        List<MyFileEO> myFileEOList = fileEODao.selectByForeignId(projectMilepostEO.getId());
        if (CollectionUtils.isNotEmpty(myFileEOList)){
            for (MyFileEO fileEO : myFileEOList){
                ProjectMilepostResultVO projectMilepostResultVO = new ProjectMilepostResultVO();
                projectMilepostResultVO.setForeignId(projectMilepostEO.getId());
                this.fillProjectMilepostResultVO(fileEO,projectMilepostResultVO);
                projectMilepostResultVOList.add(projectMilepostResultVO);
            }
        }


        return projectMilepostEO ;
    }

    public ProjectMilepostEO getMilepostResultAndFile(String id) throws  Exception{
        List<ProjectMilepostResultEO> milepostResultEOList = projectMilepostResultEODao.selectByMilepostId(id);
        ProjectMilepostEO projectMilepostEO =projectMilepostEODao.selectByPrimaryKey(id);
        List<ProjectMilepostResultVO> projectMilepostResultVOList = new ArrayList<>();

        if (CollectionUtils.isNotEmpty(milepostResultEOList)) {
            for (ProjectMilepostResultEO resultEO : milepostResultEOList){
                ProjectMilepostResultVO resultVO = new ProjectMilepostResultVO();
                resultVO.setId(resultEO.getId());
                resultVO.setResultFileName(resultEO.getFileName());
                if (StringUtils.isNotEmpty(resultEO.getFileId())) {
                    MyFileEO fileEO = fileEODao.selectByPrimaryKey(resultEO.getFileId());
                    if (null != fileEO) {
                        this.fillProjectMilepostResultVO(fileEO, resultVO);
                    }
                }
                projectMilepostResultVOList.add(resultVO);
            }
            projectMilepostEO.setProjectMilepostResultVOList(projectMilepostResultVOList);
        }
        return projectMilepostEO ;
    }




    private ProjectMilepostResultVO fillProjectMilepostResultVO( MyFileEO fileEO , ProjectMilepostResultVO projectMilepostResultVO) throws Exception{
        String uploadUserName = "" ;
        if (StringUtils.isNotEmpty(fileEO.getUserId())){
            UserEO userEO = userEODao.selectByPrimaryKey(fileEO.getUserId());
            if (null != userEO){
                uploadUserName = userEO.getUsname();
            }
        }
        projectMilepostResultVO.setFileId(fileEO.getFileId());
        projectMilepostResultVO.setFileName(fileEO.getFileName());
        projectMilepostResultVO.setUploadUserId(fileEO.getUserId());
        projectMilepostResultVO.setUploadUserName(uploadUserName);
        projectMilepostResultVO.setUploadTime(fileEO.getCreateTime());
        projectMilepostResultVO.setFileSize(fileEO.getFileSize());

        return  projectMilepostResultVO ;
    }


    public Integer queryCountByName(String milepostName,String projectId){
        return  projectMilepostEODao.queryCountByName(milepostName,projectId) ;
    }



    public List<ProjectMilepostEO> selectByManagerId( String managerId){
        return projectMilepostEODao.selectByManagerId(managerId);
    }

    public List<ProjectMilepostEO> getByProjectIdList(List<String> projectIdList){
        return  projectMilepostEODao.getByProjectIdList(projectIdList);

    }

    public List<ProjectMilepostEO> getByProjectId(String projectId){
        return  projectMilepostEODao.getByProjectId(projectId);

    }

    public List<String> getProjectIdListByManagerId( String managerId ){

        return projectMilepostEODao.getProjectIdListByManagerId(managerId) ;

    }

}
