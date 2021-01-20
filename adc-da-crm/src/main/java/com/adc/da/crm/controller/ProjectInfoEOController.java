package com.adc.da.crm.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.util.List;
import java.util.Map;

import com.adc.da.crm.annotation.AutoMatch;
import com.adc.da.form.entity.AdcFormDataEO;
import com.adc.da.form.service.FormDataService;
import com.adc.da.form.vo.AdcFormDataVO;
import com.adc.da.util.utils.BeanMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.adc.da.base.web.BaseController;
import com.adc.da.crm.entity.ProjectInfoEO;
import com.adc.da.crm.page.ProjectInfoEOPage;
import com.adc.da.crm.service.ProjectInfoEOService;

import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.http.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/${restPath}/crm/projectInfo")
@Api(description = "|ProjectInfoEO|")
public class ProjectInfoEOController extends BaseController<ProjectInfoEO>{

    private static final Logger logger = LoggerFactory.getLogger(ProjectInfoEOController.class);

    @Autowired
    private ProjectInfoEOService projectInfoEOService;
    @Autowired
    BeanMapper beanMapper;

	@ApiOperation(value = "|ProjectInfoEO|分页查询")
    @GetMapping("/page")
    @RequiresPermissions("crm:projectInfo:page")
    public ResponseMessage<PageInfo<ProjectInfoEO>> page(ProjectInfoEOPage page) throws Exception {
        List<ProjectInfoEO> rows = projectInfoEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|ProjectInfoEO|查询")
    @GetMapping("")
    @RequiresPermissions("crm:projectInfo:list")
    public ResponseMessage<List<ProjectInfoEO>> list(ProjectInfoEOPage page) throws Exception {
        return Result.success(projectInfoEOService.queryByList(page));
	}

    @ApiOperation(value = "|ProjectInfoEO|详情")
    @GetMapping("/{id}")
    @RequiresPermissions("crm:projectInfo:get")
    public ResponseMessage<ProjectInfoEO> find(@PathVariable String id) throws Exception {
        return Result.success(projectInfoEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|ProjectInfoEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
@RequiresPermissions("crm:projectInfo:save")
    public ResponseMessage<AdcFormDataVO> create( @AutoMatch ProjectInfoEO projectInfoEO, @RequestBody AdcFormDataVO adcFormDataVO) throws Exception {
	    //保存到ct_data
        String formTitle = adcFormDataVO.getFormTitle().replaceAll("&#40;", "(").replaceAll("&#41;", ")");
        String formContent = adcFormDataVO.getFormContent().replaceAll("&#39;", "'");
        adcFormDataVO.setFormTitle(formTitle);
        adcFormDataVO.setFormContent(formContent);
        AdcFormDataEO adcFormDataEO = (AdcFormDataEO)this.beanMapper.map(adcFormDataVO, AdcFormDataEO.class);
//        this.formDataService.save(adcFormDataEO);
//        this.formDataService.saveFormAndData(adcFormDataEO);
        this.projectInfoEOService.save(adcFormDataEO);
//        this.projectInfoEOService.insertSelective(projectInfoEO);
        adcFormDataVO.setId(adcFormDataEO.getId());
        return Result.success(adcFormDataVO);
    }

    @ApiOperation(value = "|ProjectInfoEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("crm:projectInfo:update")
    public ResponseMessage<ProjectInfoEO> update(@RequestBody ProjectInfoEO projectInfoEO) throws Exception {
        projectInfoEOService.updateByPrimaryKeySelective(projectInfoEO);
        return Result.success(projectInfoEO);
    }

    @ApiOperation(value = "|ProjectInfoEO|删除")
    @DeleteMapping("/{id}")
    @RequiresPermissions("crm:projectInfo:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        projectInfoEOService.deleteByPrimaryKey(id);
        logger.info("delete from PROJECT_INFO where id = {}", id);
        return Result.success();
    }

}
