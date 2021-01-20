package com.adc.da.research.project.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.research.project.entity.ResearchProjectMenuEO;
import com.adc.da.research.project.page.ResearchProjectMenuEOPage;
import com.adc.da.research.project.service.ResearchProjectMenuEOService;
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

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping("/${restPath}/project/researchProjectMenu")
@Api(tags = "科研系统|项目菜单|ResearchProjectMenuEOController")
public class ResearchProjectMenuEOController extends BaseController<ResearchProjectMenuEO>{

    private static final Logger logger = LoggerFactory.getLogger(ResearchProjectMenuEOController.class);

    @Autowired
    private ResearchProjectMenuEOService researchProjectMenuEOService;

	@ApiOperation(value = "|ResearchProjectMenuEO|分页查询")
    @GetMapping("/page")
   // @RequiresPermissions("project:researchProjectMenu:page")
    public ResponseMessage<PageInfo<ResearchProjectMenuEO>> page(ResearchProjectMenuEOPage page) throws Exception {
        List<ResearchProjectMenuEO> rows = researchProjectMenuEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|ResearchProjectMenuEO|查询")
    @GetMapping("")
  //  @RequiresPermissions("project:researchProjectMenu:list")
    public ResponseMessage<List<ResearchProjectMenuEO>> list(ResearchProjectMenuEOPage page) throws Exception {
        return Result.success(researchProjectMenuEOService.queryByList(page));
	}

    @ApiOperation(value = "|ResearchProjectMenuEO|详情")
    @GetMapping("/{id}")
   // @RequiresPermissions("project:researchProjectMenu:get")
    public ResponseMessage<ResearchProjectMenuEO> find(@PathVariable String id) throws Exception {
        return Result.success(researchProjectMenuEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|ResearchProjectMenuEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
   // @RequiresPermissions("project:researchProjectMenu:save")
    public ResponseMessage<ResearchProjectMenuEO> create(@RequestBody ResearchProjectMenuEO researchProjectMenuEO) throws Exception {
        researchProjectMenuEOService.insertSelective(researchProjectMenuEO);
        return Result.success(researchProjectMenuEO);
    }

    @ApiOperation(value = "|ResearchProjectMenuEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
   // @RequiresPermissions("project:researchProjectMenu:update")
    public ResponseMessage<ResearchProjectMenuEO> update(@RequestBody ResearchProjectMenuEO researchProjectMenuEO) throws Exception {
        researchProjectMenuEOService.updateByPrimaryKeySelective(researchProjectMenuEO);
        return Result.success(researchProjectMenuEO);
    }

    @ApiOperation(value = "|ResearchProjectMenuEO|删除")
    @DeleteMapping("/{id}")
   // @RequiresPermissions("project:researchProjectMenu:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        researchProjectMenuEOService.deleteByPrimaryKey(id);
        logger.info("delete from RS_RESEARCH_PROJECT_MENU where id = {}", id);
        return Result.success();
    }

}
