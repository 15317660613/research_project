package com.adc.da.project.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.util.*;

import com.adc.da.login.entity.MyPrincipal;
import com.adc.da.login.security.SystemAuthorizingRealm;
import com.adc.da.project.dao.OrgProDao;
import com.adc.da.project.service.ProjectScheduleEOService;
import com.adc.da.sys.dao.UserEODao;
import com.adc.da.sys.entity.OrgEO;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.util.utils.*;
import com.adc.da.util.utils.UUID;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.*;

import com.adc.da.base.web.BaseController;
import com.adc.da.project.entity.ProjectPlanEO;
import com.adc.da.project.page.ProjectPlanEOPage;
import com.adc.da.project.service.ProjectPlanEOService;

import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.http.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/${restPath}/project/projectPlan")
@Api(description = "|ProjectPlanEO|")
public class ProjectPlanEOController extends BaseController<ProjectPlanEO>{

    private static final Logger logger = LoggerFactory.getLogger(ProjectPlanEOController.class);

    @Autowired
    private ProjectPlanEOService projectPlanEOService;

    @Autowired
    private ProjectScheduleEOService projectScheduleEOService;

    @Autowired
    private UserEODao userEODao ;


    @Autowired
    private OrgProDao orgProDao ;


    @Autowired
    ResourceLoader resourceLoader;

	@ApiOperation(value = "|ProjectPlanEO|分页查询")
    @GetMapping("/page")
//    //@RequiresPermissions("order:projectPlan:page")
    public ResponseMessage<PageInfo<ProjectPlanEO>> page(ProjectPlanEOPage page) throws Exception {
        List<ProjectPlanEO> rows = projectPlanEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|ProjectPlanEO|查询")
    @GetMapping("")
    //@RequiresPermissions("order:projectPlan:list")
    public ResponseMessage<List<ProjectPlanEO>> list(ProjectPlanEOPage page) throws Exception {
        return Result.success(projectPlanEOService.queryByList(page));
	}

    @ApiOperation(value = "|ProjectPlanEO|详情")
    @GetMapping("/{id}")
//    //@RequiresPermissions("order:projectPlan:get")
    public ResponseMessage<ProjectPlanEO> find(@PathVariable String id) throws Exception {
        return Result.success(projectPlanEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|ProjectPlanEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    //@RequiresPermissions("order:projectPlan:save")
    public ResponseMessage<ProjectPlanEO> create(@RequestBody ProjectPlanEO projectPlanEO) throws Exception {
        projectPlanEOService.insertSelective(projectPlanEO);
        return Result.success(projectPlanEO);
    }

    @ApiOperation(value = "|ProjectPlanEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    //@RequiresPermissions("order:projectPlan:update")
    public ResponseMessage<ProjectPlanEO> update(@RequestBody ProjectPlanEO projectPlanEO) throws Exception {
        projectPlanEOService.updateByPrimaryKeySelective(projectPlanEO);
        return Result.success(projectPlanEO);
    }

    @ApiOperation(value = "|ProjectPlanEO|保存或修改,月计划info2字段应当是0，季度计划info2字段为1")
    @PostMapping(value = "/saveOrUpdate")
    //@RequiresPermissions("order:projectPlan:update")
    public ResponseMessage<ProjectPlanEO> saveOrUpdate(@RequestBody ProjectPlanEO projectPlanEO) throws Exception {
        Subject subject = SecurityUtils.getSubject();
//        System.out.println(subject.getPrincipal().toString());
        MyPrincipal  principal =  (MyPrincipal)subject.getPrincipals().getPrimaryPrincipal();
        String userId = principal.getId();
        if (null == userId){
            return  Result.error("登陆可能过期，或没有登陆！");
        }
        UserEO userEO  = userEODao.getUserWithRoles(userId);

        String myOrgId = null ;

        for (OrgEO orgEO : userEO.getOrgEOList()) {
            if(!orgEO.getName().contains("本部")&& orgEO.getName().contains("部")){
                myOrgId = orgEO.getId() ;
                break ;
            }
        }


        if (StringUtils.isEmpty(projectPlanEO.getId())){
            String createTime = DateUtils.dateToString(new Date(),DateUtils.YYYY_MM_DD_HH_MM_SS_EN);
            projectPlanEO.setExtInf03(createTime);
            projectPlanEO.setId(UUID.randomUUID10());
            projectPlanEO.setDepartment(myOrgId);
            projectPlanEOService.insertSelective(projectPlanEO);
            return Result.success(projectPlanEO);
        }else{
            projectPlanEO.setExtInf03(null);
            projectPlanEO.setDepartment(myOrgId);
            projectPlanEOService.updateByPrimaryKeySelective(projectPlanEO);
            return Result.success(projectPlanEO);
        }
    }



    @ApiOperation(value = "|ProjectPlanEO|删除")
    @DeleteMapping("/{id}")
    //@RequiresPermissions("order:projectPlan:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        projectPlanEOService.deleteByPrimaryKey(id);
        logger.info("delete from PROJECT_PLAN where id = {}", id);
        return Result.success();
    }

    @ApiOperation(value = "|ProjectPlanEO|根据部门及项目类型获取数据修改月计划或者周计划")
    @PostMapping(value = "/selectByUserDeptAndProType")
    //@RequiresPermissions("order:projectPlan:update")
    public ResponseMessage<List<ProjectPlanEO>> selectByUserDeptAndProType( String projectType , String classType ,String  processInstId) {
        Subject subject = SecurityUtils.getSubject();
//        System.out.println(subject.getPrincipal().toString());
        MyPrincipal principal = (MyPrincipal) subject.getPrincipals().getPrimaryPrincipal();
        String userId = principal.getId();
        if (null == userId) {
            return Result.error("登陆可能过期，或没有登陆！");
        }

        if (projectType.contains("1-")) {
            String projectType_temp = projectType.substring(2);
            return Result.success(projectPlanEOService.selectProTypeAndProcessInstId(projectType_temp, classType, processInstId));
        } else {

            return Result.success(projectPlanEOService.selectByDeptAndProType(userId, classType, projectType, processInstId));
        }
    }

}
