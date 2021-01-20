package com.adc.da.progress.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.budget.entity.Project;
import com.adc.da.budget.repository.ProjectRepository;
import com.adc.da.progress.entity.ProjectBudgetChangeEO;
import com.adc.da.progress.page.ProjectBudgetChangeEOPage;
import com.adc.da.progress.service.ProjectBudgetChangeEOService;
import com.adc.da.progress.vo.MyProjectVO;
import com.adc.da.sys.dao.UserEODao;
import com.adc.da.sys.entity.OrgEO;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.sys.service.OrgEOService;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.http.PageInfo;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping("/${restPath}/progress/projectBudgetChange")
@Api(tags = "|ProjectBudgetChangeEO|")
public class ProjectBudgetChangeEOController extends BaseController<ProjectBudgetChangeEO> {

    private static final Logger logger = LoggerFactory.getLogger(ProjectBudgetChangeEOController.class);

    @Autowired
    private ProjectBudgetChangeEOService projectBudgetChangeEOService;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private OrgEOService orgEOService;

    @Autowired
    private UserEODao userEODao;

    @ApiOperation(value = "|MyProjectVO|详情")
    @GetMapping("/getByProjectId/{id}")
//    @RequiresPermissions("progress:projectBudgetChange:get")
    public ResponseMessage<MyProjectVO> find1(@PathVariable String id) throws Exception {
        Project project = projectRepository.findByIdAndDelFlagNot(id, true);
        if (null== project){
            throw new AdcDaBaseException(id + " 项目不存在不存在！");
        }
        ProjectBudgetChangeEO projectBudgetChangeEO = projectBudgetChangeEOService.selectByProjectId(id);
        MyProjectVO myProjectVO = new MyProjectVO();
        myProjectVO.setMapList(project.getMapList());
        myProjectVO.setProjectLeaderId(project.getProjectLeaderId());
        myProjectVO.setProjectLeaderName(project.getProjectLeader());

        OrgEO orgEO = orgEOService.getOrgEOById(project.getDeptId());
        if (null == orgEO) {
            throw new AdcDaBaseException(id + " 组织机构不存在！");
        }
        myProjectVO.setBearDeptName(orgEO.getName());
        myProjectVO.setBearDeptId(orgEO.getId());
        myProjectVO.setProjectBeginTime(project.getProjectBeginTime());
        myProjectVO.setProjectEndTime(project.getProjectEndTime());
        myProjectVO.setProjectDescription(project.getProjectDescription());
        myProjectVO.setProjectBudgetChangeEO(projectBudgetChangeEO);

        return Result.success(myProjectVO);
    }
    @ApiOperation(value = "|MyProjectVO|详情")
    @GetMapping("/getByProjectIdNew")
//    @RequiresPermissions("progress:projectBudgetChange:get")
    public ResponseMessage<MyProjectVO> newfind1(@RequestParam("id") String id, @RequestParam("usname") String usname) throws Exception {
        Project project = projectRepository.findByIdAndDelFlagNot(id, true);
        if (null== project){
            throw new AdcDaBaseException(id + " 项目不存在不存在！");
        }
        boolean delflag = true;
        if (StringUtils.isNotEmpty(usname)){
            for (Map<String,String> map : project.getMapList() ) {
                for (Map.Entry<String, String> entry : map.entrySet()){
                    if (StringUtils.equals(entry.getValue(),usname)){
                        delflag = false;
                    }
                }
            }
        }
        if (delflag){
           Map<String, String> usrMap = new HashMap<>();
           usrMap.put("id","deleted");
            usrMap.put("name",usname);
           project.getMapList().add(usrMap);
        }
        ProjectBudgetChangeEO projectBudgetChangeEO = projectBudgetChangeEOService.selectByProjectId(id);
        MyProjectVO myProjectVO = new MyProjectVO();
        myProjectVO.setMapList(project.getMapList());
        myProjectVO.setProjectLeaderId(project.getProjectLeaderId());
        myProjectVO.setProjectLeaderName(project.getProjectLeader());

        OrgEO orgEO = orgEOService.getOrgEOById(project.getDeptId());
        if (null == orgEO) {
            throw new AdcDaBaseException(id + " 组织机构不存在！");
        }
        myProjectVO.setBearDeptName(orgEO.getName());
        myProjectVO.setBearDeptId(orgEO.getId());
        myProjectVO.setProjectBeginTime(project.getProjectBeginTime());
        myProjectVO.setProjectEndTime(project.getProjectEndTime());
        myProjectVO.setProjectDescription(project.getProjectDescription());
        myProjectVO.setProjectBudgetChangeEO(projectBudgetChangeEO);

        return Result.success(myProjectVO);
    }

    @ApiOperation(value = "|ProjectBudgetChangeEO|分页查询")
    @GetMapping("/page")
//    @RequiresPermissions("progress:projectBudgetChange:page")
    public ResponseMessage<PageInfo<ProjectBudgetChangeEO>> page(ProjectBudgetChangeEOPage page) throws Exception {
        List<ProjectBudgetChangeEO> rows = projectBudgetChangeEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|ProjectBudgetChangeEO|查询")
    @GetMapping("")
//    @RequiresPermissions("progress:projectBudgetChange:list")
    public ResponseMessage<List<ProjectBudgetChangeEO>> list(ProjectBudgetChangeEOPage page) throws Exception {
        return Result.success(projectBudgetChangeEOService.queryByList(page));
	}

    @ApiOperation(value = "|ProjectBudgetChangeEO|详情")
    @GetMapping("/{id}")
//    @RequiresPermissions("progress:projectBudgetChange:get")
    public ResponseMessage<ProjectBudgetChangeEO> find(@PathVariable String id) throws Exception {
        return Result.success(projectBudgetChangeEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|ProjectBudgetChangeEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("progress:projectBudgetChange:save")
    public ResponseMessage<ProjectBudgetChangeEO> create(@RequestBody ProjectBudgetChangeEO projectBudgetChangeEO) throws Exception {
        projectBudgetChangeEOService.insertSelective(projectBudgetChangeEO);
        return Result.success(projectBudgetChangeEO);
    }

    @ApiOperation(value = "|ProjectBudgetChangeEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("progress:projectBudgetChange:update")
    public ResponseMessage<ProjectBudgetChangeEO> update(@RequestBody ProjectBudgetChangeEO projectBudgetChangeEO) throws Exception {
        projectBudgetChangeEOService.updateByPrimaryKeySelective(projectBudgetChangeEO);
        return Result.success(projectBudgetChangeEO);
    }

    @ApiOperation(value = "|ProjectBudgetChangeEO|删除")
    @DeleteMapping("/{id}")
//    @RequiresPermissions("progress:projectBudgetChange:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        projectBudgetChangeEOService.deleteByPrimaryKey(id);
        logger.info("delete from PR_PROJECT_BUDGET_CHANGE where id = {}", id);
        return Result.success();
    }

}
