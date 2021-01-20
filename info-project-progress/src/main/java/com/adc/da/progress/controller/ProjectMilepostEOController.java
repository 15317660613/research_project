package com.adc.da.progress.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.adc.da.budget.entity.Project;
import com.adc.da.budget.repository.ProjectRepository;
import com.adc.da.file.entity.MyFileEO;
import com.adc.da.file.service.FileEOService;
import com.adc.da.file.service.MyFileEOService;
import com.adc.da.progress.entity.ProjectMilepostResultEO;
import com.adc.da.progress.page.MyProjectMilepostEOPage;
import com.adc.da.progress.service.ProjectMilepostResultEOService;
import com.adc.da.progress.vo.MyVO;
import com.adc.da.progress.vo.ProjectMilepostResultVO;
import com.adc.da.progress.vo.ProjectMilepostVO;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.sys.service.UserEOService;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.utils.BeanMapper;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import com.adc.da.base.web.BaseController;
import com.adc.da.progress.entity.ProjectMilepostEO;
import com.adc.da.progress.page.ProjectMilepostEOPage;
import com.adc.da.progress.service.ProjectMilepostEOService;

import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.http.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/${restPath}/progress/projectMilepost")
@Api(description = "|ProjectMilepostEO|")
public class ProjectMilepostEOController extends BaseController<ProjectMilepostEO>{

    private static final Logger logger = LoggerFactory.getLogger(ProjectMilepostEOController.class);

    @Autowired
    private ProjectMilepostEOService projectMilepostEOService;

    @Autowired
    private ProjectMilepostResultEOService projectMilepostResultEOService;

    @Autowired
    private MyFileEOService fileEOService ;

    @Autowired
    private UserEOService userEOService ;

    @Autowired
    private ProjectRepository projectRepository ;

    @Autowired
    private BeanMapper beanMapper;

    @Value("${file.path}")
    private String basePath;

	@ApiOperation(value = "|ProjectMilepostEO|分页查询")
    @GetMapping("/page")
//    @RequiresPermissions("progress:projectMilepost:page")
    public ResponseMessage<PageInfo<ProjectMilepostEO>> page(ProjectMilepostEOPage page) throws Exception {
        List<ProjectMilepostEO> rows = projectMilepostEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

    @ApiOperation(value = "|ProjectMilepostEO|分页查询根据各种条件")
    @PostMapping("/page4Tips")
//    @RequiresPermissions("progress:projectMilepost:page")
    public ResponseMessage<PageInfo<ProjectMilepostEO>> page4Tips(@RequestBody MyProjectMilepostEOPage page) throws Exception {
        List<ProjectMilepostEO> rows = projectMilepostEOService.page4Tips(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }


	@ApiOperation(value = "|ProjectMilepostEO|查询")
    @GetMapping("")
//    @RequiresPermissions("progress:projectMilepost:list")
    public ResponseMessage<List<ProjectMilepostEO>> list(ProjectMilepostEOPage page) throws Exception {
        return Result.success(projectMilepostEOService.queryByList(page));
	}

    @ApiOperation(value = "|ProjectMilepostEO|里程碑详情")
    @GetMapping("/{id}")
//    @RequiresPermissions("progress:projectMilepost:get")
    public ResponseMessage<ProjectMilepostVO> find(@PathVariable String id) throws Exception {
        ProjectMilepostEO projectMilepostEO =  projectMilepostEOService.selectByPrimaryKey(id);

        if (null == projectMilepostEO){
            throw  new AdcDaBaseException( id + " 的里程碑不存在！");
        }
        ProjectMilepostVO projectMilepostVO = beanMapper.map(projectMilepostEO,ProjectMilepostVO.class);
        Project project = projectRepository.findById(projectMilepostEO.getProjectId());
        projectMilepostVO.setProjectLeaderId(project.getProjectLeaderId());

        List<ProjectMilepostResultEO> projectMilepostResultEOList = projectMilepostResultEOService.selectByMilepostId(projectMilepostEO.getId()) ;
        projectMilepostVO.setProjectMilepostResultEOList(projectMilepostResultEOList);
        return Result.success(projectMilepostVO);
    }


//    public ResponseMessage<ProjectMilepostVO> find1(@PathVariable String id) throws Exception {
//        ProjectMilepostEO projectMilepostEO =  projectMilepostEOService.selectByPrimaryKey(id);
//
//        ProjectMilepostVO projectMilepostVO = beanMapper.map(projectMilepostEO,ProjectMilepostVO.class);
//        Project project = projectRepository.findById(projectMilepostEO.getProjectId());
//        projectMilepostVO.setProjectLeaderId(project.getProjectLeaderId());
//        return Result.success(projectMilepostVO);
//    }

    @ApiOperation(value = "|ProjectMilepostEO|根据里程碑/阶段id查询里程碑详情及里程碑下的成果物及其文件")
    @GetMapping("/getMilepostAndResult/{id}")
//    @RequiresPermissions("progress:projectMilepost:get")
    public ResponseMessage<ProjectMilepostEO> getMilepostAndResult(@PathVariable String id) throws Exception {


        return Result.success(projectMilepostEOService.getMilepostAndResult(id));
    }

    @ApiOperation(value = "|ProjectMilepostEO|根据里程碑/阶段id查询里程碑详情及里程碑下的成果物及其文件")
    @GetMapping("/getMilepostResultAndFile/{id}")
//    @RequiresPermissions("progress:projectMilepost:get")
    public ResponseMessage<ProjectMilepostEO> getMilepostResultAndFile(@PathVariable String id) throws Exception {
        return Result.success(projectMilepostEOService.getMilepostResultAndFile(id));
    }



//    private ProjectMilepostEO fillProjectMilepostEO(ProjectMilepostEO projectMilepostEO,List<ProjectMilepostResultVO> projectMilepostResultVOList) throws  Exception{
//
//
//        List<ProjectMilepostResultEO> projectMilepostResultEOList = projectMilepostResultEOService.selectByMilepostId(projectMilepostEO.getId()) ;
//        for (ProjectMilepostResultEO resultEO: projectMilepostResultEOList){
//
//            ProjectMilepostResultVO projectMilepostResultVO = new ProjectMilepostResultVO();
//            projectMilepostResultVO.setId(resultEO.getId());
//            projectMilepostResultVO.setResultFileName(resultEO.getFileName());
//
//            List<MyFileEO> fileEOList =  fileEOService.selectByForeignId(resultEO.getId());
//            if (CollectionUtils.isEmpty( fileEOList )){
//                projectMilepostResultVOList.add(projectMilepostResultVO);
//                continue;
//            }
//            for (MyFileEO fileEO :fileEOList) {
//                this.fillProjectMilepostResultVO(fileEO,projectMilepostResultVO);
//            }
//            projectMilepostResultVOList.add(projectMilepostResultVO);
//        }
//
//        List<MyFileEO> myFileEOList = fileEOService.selectByForeignId(projectMilepostEO.getId());
//        if (CollectionUtils.isNotEmpty(myFileEOList)){
//            for (MyFileEO fileEO : myFileEOList){
//                ProjectMilepostResultVO projectMilepostResultVO = new ProjectMilepostResultVO();
//                projectMilepostResultVO.setForeignId(projectMilepostEO.getId());
//                this.fillProjectMilepostResultVO(fileEO,projectMilepostResultVO);
//                projectMilepostResultVOList.add(projectMilepostResultVO);
//            }
//        }
//
//
//        return projectMilepostEO ;
//    }
//
//
//
//
//
//    private ProjectMilepostResultVO fillProjectMilepostResultVO( MyFileEO fileEO , ProjectMilepostResultVO projectMilepostResultVO) throws Exception{
//
//        UserEO userEO = userEOService.selectByPrimaryKey(fileEO.getUserId());
//        String uploadUserName = "" ;
//        if (null != userEO){
//            uploadUserName = userEO.getUsname();
//        }
//        String savePath = fileEO.getSavePath();
//        String fullPath = basePath + savePath;
//        String fileSize = "0";
//        File file = new File(fullPath);
//        long fileLength = file.length();
//        long length = 0;
//        if (fileLength >= 1000000) {
//            length = fileLength >> 20;
//            fileSize = String.valueOf(length) + "MB";
//        } else if (fileLength < 1000000 && fileLength >= 1000) {
//            length = fileLength >> 10;
//            fileSize = String.valueOf(length) + "KB";
//        } else {
//            fileSize = String.valueOf(fileLength) + "B";
//        }
//        projectMilepostResultVO.setFileId(fileEO.getFileId());
//        projectMilepostResultVO.setFileName(fileEO.getFileName());
//        projectMilepostResultVO.setUploadUserId(fileEO.getUserId());
//        projectMilepostResultVO.setUploadUserName(uploadUserName);
//        projectMilepostResultVO.setUploadTime(fileEO.getCreateTime());
//        projectMilepostResultVO.setFileSize(fileSize);
//
//        return  projectMilepostResultVO ;
//    }





    @ApiOperation(value = "|ProjectMilepostEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("progress:projectMilepost:save")
    public ResponseMessage<ProjectMilepostEO> create(@RequestBody ProjectMilepostEO projectMilepostEO) throws Exception {
	    projectMilepostEO.setId(UUID.randomUUID10());
        projectMilepostEOService.insertSelective(projectMilepostEO);
        return Result.success(projectMilepostEO);
    }




    @ApiOperation(value = "|ProjectMilepostEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("progress:projectMilepost:update")
    public ResponseMessage<ProjectMilepostEO> update(@RequestBody ProjectMilepostEO projectMilepostEO) throws Exception {
        projectMilepostEOService.updateByPrimaryKeySelective(projectMilepostEO);
        return Result.success(projectMilepostEO);
    }

    @ApiOperation(value = "|ProjectMilepostEO|删除")
    @DeleteMapping("/{id}")
//    @RequiresPermissions("progress:projectMilepost:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        projectMilepostEOService.deleteByPrimaryKey(id);
        logger.info("delete from PR_PROJECT_MILEPOST where id = {}", id);
        return Result.success();
    }

    @ApiOperation(value = "|ProjectMilepostEO|查询里程碑是否重名")
    @PostMapping("/queryCountByNameList")
    public ResponseMessage queryCountByNameList(@RequestBody MyVO myVO){
	    if ( CollectionUtils.isEmpty(myVO.getMilepostNameArr())){
            return Result.success();
        }
        StringBuilder repeatName = new StringBuilder();
        for (String name : myVO.getMilepostNameArr() ) {
            Integer size = projectMilepostEOService.queryCountByName(name,myVO.getProjectId());
            if (size>0){
                repeatName.append(name+",");
            }
        }
        String result = "" ;
        if (repeatName.length()>0){
            result =  repeatName.substring(0,repeatName.lastIndexOf(","));
        }

        return Result.success(result);
    }

}
