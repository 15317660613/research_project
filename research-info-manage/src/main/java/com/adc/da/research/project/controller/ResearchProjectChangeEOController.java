package com.adc.da.research.project.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.research.funds.service.ChangeService;
import com.adc.da.research.project.entity.ProjectDataEO;
import com.adc.da.research.project.entity.ResearchProjectChangeEO;
import com.adc.da.research.project.page.ProjectDataEOPage;
import com.adc.da.research.project.page.ResearchProjectChangeEOPage;
import com.adc.da.research.project.service.ResearchProjectChangeEOService;
import com.adc.da.research.project.vo.ChangeProjectVO;
import com.adc.da.util.http.PageInfo;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping("/${restPath}/project/researchProjectChange")
@Api(tags = "科研系统|项目变更|ResearchProjectChangeEOController")
public class ResearchProjectChangeEOController extends BaseController<ResearchProjectChangeEO>{

    private static final Logger logger = LoggerFactory.getLogger(ResearchProjectChangeEOController.class);

    @Autowired
    private ResearchProjectChangeEOService researchProjectChangeEOService;
    @Autowired
    private ChangeService changeService;

	@ApiOperation(value = "|ResearchProjectChangeEO|分页查询")
    @GetMapping("/page")
    //@RequiresPermissions("project:researchProjectChange:page")
    public ResponseMessage<PageInfo<ResearchProjectChangeEO>> page(ResearchProjectChangeEOPage page) throws Exception {
        List<ResearchProjectChangeEO> rows = researchProjectChangeEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|ResearchProjectChangeEO|查询")
    @GetMapping("")
    //@RequiresPermissions("project:researchProjectChange:list")
    public ResponseMessage<List<Map<String,Object>>> list(ResearchProjectChangeEOPage page) throws Exception {

        List<Map<String,Object>> map=  researchProjectChangeEOService.queryByTimeList( page);

        return Result.success(map);
	}

    @ApiOperation(value = "|ResearchProjectChangeEO|详情")
    @GetMapping("/{id}")
   // @RequiresPermissions("project:researchProjectChange:get")
    public ResponseMessage<ResearchProjectChangeEO> find(@PathVariable String id) throws Exception {
        return Result.success(researchProjectChangeEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|ResearchProjectChangeEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
   // @RequiresPermissions("project:researchProjectChange:save")
    public ResponseMessage<ResearchProjectChangeEO> create(@RequestBody ResearchProjectChangeEO researchProjectChangeEO) throws Exception {
        researchProjectChangeEOService.insertSelective(researchProjectChangeEO);
        return Result.success(researchProjectChangeEO);
    }

    @ApiOperation(value = "|ResearchProjectChangeEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    //@RequiresPermissions("project:researchProjectChange:update")
    public ResponseMessage<ResearchProjectChangeEO> update(@RequestBody ResearchProjectChangeEO researchProjectChangeEO) throws Exception {
        researchProjectChangeEOService.updateByPrimaryKeySelective(researchProjectChangeEO);
        return Result.success(researchProjectChangeEO);
    }

    @ApiOperation(value = "|ResearchProjectChangeEO|删除")
    @DeleteMapping("/{id}")
   // @RequiresPermissions("project:researchProjectChange:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        researchProjectChangeEOService.deleteByPrimaryKey(id);
        logger.info("delete from RS_RESEARCH_PROJECT_CHANGE where id = {}", id);
        return Result.success();
    }

    @ApiOperation(value = "|ResearchProjectChangeEO|变更下拉框查询")
    @GetMapping("/getProjectName")
    public ResponseMessage< List<ProjectDataEO>> getProjectName(ProjectDataEOPage page) throws Exception {

        List<ProjectDataEO> rows=  researchProjectChangeEOService.getProjectName(page);

        return Result.success(rows);
    }

    @ApiOperation(value = "|ResearchProjectChangeEO|变更提交")
    @PostMapping("/submitChange")
    public ResponseMessage submitChange(@RequestBody ProjectDataEO eo) throws Exception {

        //changeService.allChange("29SYMREQTE");
        Map<String, List<ChangeProjectVO>> stringListMap = changeService.allChange(eo.getId());
        researchProjectChangeEOService.saveChange(stringListMap,eo.getId());

        return Result.success();
    }

    @ApiOperation(value = "|ResearchProjectChangeEO|变更删除")
    @PostMapping("/deleteChange")
    public ResponseMessage deleteChange(@RequestBody ProjectDataEO eo) throws Exception {

        researchProjectChangeEOService.deleteChange(eo);

        return Result.success();
    }

    @ApiOperation(value = "|ResearchProjectChangeEO|变更保存")
    @PostMapping("/saveChange")
    public ResponseMessage saveChange(@RequestBody ProjectDataEO eo) throws Exception {

        changeService.saveChange(eo);
        return Result.success();
    }




}
