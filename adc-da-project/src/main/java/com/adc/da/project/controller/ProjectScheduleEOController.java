package com.adc.da.project.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.adc.da.login.entity.MyPrincipal;
import com.adc.da.login.security.SystemAuthorizingRealm;
import com.adc.da.project.dao.OrgProDao;
import com.adc.da.project.entity.ProjectPlanEO;
import com.adc.da.sys.dao.UserEODao;
import com.adc.da.sys.entity.OrgEO;
import com.adc.da.sys.entity.RoleEO;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.sys.service.UserEOService;
import com.adc.da.util.utils.DateUtils;
import com.adc.da.util.utils.StringUtils;
import com.adc.da.util.utils.UUID;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.adc.da.base.web.BaseController;
import com.adc.da.project.entity.ProjectScheduleEO;
import com.adc.da.project.page.ProjectScheduleEOPage;
import com.adc.da.project.service.ProjectScheduleEOService;

import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.http.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;

@RestController
@RequestMapping("/${restPath}/project/projectSchedule")
@Api(description = "|ProjectScheduleEO|")
public class ProjectScheduleEOController extends BaseController<ProjectScheduleEO>{

    private static final Logger logger = LoggerFactory.getLogger(ProjectScheduleEOController.class);

    @Autowired
    private ProjectScheduleEOService projectScheduleEOService;

    @Autowired
    private UserEOService userEOService ;



    @Autowired
    private UserEODao userEODao ;


    @Autowired
    private OrgProDao orgProDao ;

	@ApiOperation(value = "|ProjectScheduleEO|分页查询")
    @GetMapping("/page")
    //@RequiresPermissions("order:projectSchedule:page")
    public ResponseMessage<PageInfo<ProjectScheduleEO>> page(ProjectScheduleEOPage page) throws Exception {
        List<ProjectScheduleEO> rows = projectScheduleEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|ProjectScheduleEO|查询")
    @GetMapping("")
    //@RequiresPermissions("order:projectSchedule:list")
    public ResponseMessage<List<ProjectScheduleEO>> list(ProjectScheduleEOPage page) throws Exception {
        return Result.success(projectScheduleEOService.queryByList(page));
	}

    @ApiOperation(value = "|ProjectScheduleEO|详情")
    @GetMapping("/{id}")
    //@RequiresPermissions("order:projectSchedule:get")
    public ResponseMessage<ProjectScheduleEO> find(@PathVariable String id) throws Exception {
        return Result.success(projectScheduleEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|ProjectScheduleEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    //@RequiresPermissions("order:projectSchedule:save")
    public ResponseMessage<ProjectScheduleEO> create(@RequestBody ProjectScheduleEO projectScheduleEO) throws Exception {
        projectScheduleEOService.insertSelective(projectScheduleEO);
        return Result.success(projectScheduleEO);
    }

    @ApiOperation(value = "|ProjectScheduleEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    //@RequiresPermissions("order:projectSchedule:update")
    public ResponseMessage<ProjectScheduleEO> update(@RequestBody ProjectScheduleEO projectScheduleEO) throws Exception {
        projectScheduleEOService.updateByPrimaryKeySelective(projectScheduleEO);
        return Result.success(projectScheduleEO);
    }

    @ApiOperation(value = "|ProjectScheduleEO|删除")
    @DeleteMapping("/{id}")
    //@RequiresPermissions("order:projectSchedule:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        projectScheduleEOService.deleteByPrimaryKey(id);
        logger.info("delete from PROJECT_SCHEDULE where id = {}", id);
        return Result.success();
    }

    @ApiOperation(value = "|ProjectScheduleEO|保存或修改")
    @PostMapping(value = "/saveOrUpdate")
    //@RequiresPermissions("order:projectPlan:update")
    public ResponseMessage<ProjectScheduleEO> saveOrUpdate(@RequestBody ProjectScheduleEO projectScheduleEO) throws Exception {
        logger.info("saveOrUpdate from PROJECT_SCHEDULE projectScheduleEO = {}", projectScheduleEO.toString());

        Subject subject = SecurityUtils.getSubject();
//        System.out.println(subject.getPrincipal().toString());
        MyPrincipal principal =  (MyPrincipal)subject.getPrincipals().getPrimaryPrincipal();
        String userId = principal.getId();
        if (null == userId){
            return  Result.error("登陆可能过期，或没有登陆！");
        }
        UserEO userEO  = userEODao.getUserWithRoles(userId);
        String myOrgId = null ;


        for (OrgEO orgEO : userEO.getOrgEOList()) {
            if(!orgEO.getName().contains("本部") && orgEO.getName().contains("部")){
                myOrgId = orgEO.getId() ;
                break ;
            }
        }


        if (StringUtils.isEmpty(projectScheduleEO.getId())){
            String createTime = DateUtils.dateToString(new Date(),DateUtils.YYYY_MM_DD_HH_MM_SS_EN);
            projectScheduleEO.setCreateTime(createTime);
            projectScheduleEO.setId(UUID.randomUUID10());
            projectScheduleEO.setDepartment(myOrgId);
            projectScheduleEOService.insert(projectScheduleEO);
            return Result.success(projectScheduleEO);
        }else{
            projectScheduleEO.setCreateTime(null);
            projectScheduleEO.setDepartment(myOrgId);
            projectScheduleEOService.updateByPrimaryKeySelective(projectScheduleEO);
            return Result.success(projectScheduleEO);
        }
    }


//    @ApiOperation(value = "|ProjectScheduleEO|根据部门及项目类型获取数据")
//    @PostMapping(value = "/selectByDeptAndProType")
//    //@RequiresPermissions("order:projectPlan:update")
//    public ResponseMessage<List<ProjectScheduleEO>> selectByDeptAndProType( String department , String projectType , String processInstId ){
//
//        return Result.success( projectScheduleEOService.selectByDeptProTypeAndProcessInstId(department ,  projectType ,  processInstId));
//    }



    @ApiOperation(value = "|ProjectScheduleEO|根据部门及项目类型获取数据")
    @PostMapping(value = "/selectByUserDeptAndProType")
    //@RequiresPermissions("order:projectPlan:update")
    public ResponseMessage<List<ProjectScheduleEO>> selectByUserDeptAndProType( String projectType ,String classType, String  processInstId){
        Subject subject = SecurityUtils.getSubject();
        String userId = null  ;
        //System.out.println(subject.getPrincipal().toString());
        if (null != subject.getPrincipals() ) {
            MyPrincipal principal = (MyPrincipal) subject.getPrincipals().getPrimaryPrincipal();
            userId = principal.getId();
            if (null == userId){
                return  Result.error("登陆可能过期，或没有登陆！");
            }
        }

        if(projectType.contains("1-")){
            //获取年月用
            String projectType_temp = projectType.substring(2);
            return Result.success(projectScheduleEOService.selectProTypeAndProcessInstId(projectType_temp,classType,processInstId));
        }else{

            return Result.success(projectScheduleEOService.selectByDeptProTypeAndProcessInstId(userId, classType,projectType, processInstId));
        }
    }





}
