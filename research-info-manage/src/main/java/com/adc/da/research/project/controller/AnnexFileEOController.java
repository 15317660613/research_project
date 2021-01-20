package com.adc.da.research.project.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.research.project.entity.AnnexFileEO;
import com.adc.da.research.project.page.AnnexFileEOPage;
import com.adc.da.research.project.service.AnnexFileEOService;
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
@RequestMapping("/${restPath}/research/project/annexFile")
@Api(tags = "科研系统|AnnexFileEO|科研项目附件|AnnexFileEOController")
public class AnnexFileEOController extends BaseController<AnnexFileEO> {

    private static final Logger logger = LoggerFactory.getLogger(AnnexFileEOController.class);

    @Autowired
    private AnnexFileEOService annexFileEOService;

	@ApiOperation(value = "|AnnexFileEO|分页查询")
    @GetMapping("/page")
//    @RequiresPermissions("research.project:annexFile:page")
    public ResponseMessage<PageInfo<AnnexFileEO>> page(AnnexFileEOPage page) throws Exception {
        List<AnnexFileEO> rows = annexFileEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|AnnexFileEO|查询")
    @GetMapping("")
//    @RequiresPermissions("research.project:annexFile:list")
    public ResponseMessage<List<AnnexFileEO>> list(AnnexFileEOPage page) throws Exception {
        return Result.success(annexFileEOService.queryByList(page));
	}

    @ApiOperation(value = "|AnnexFileEO|详情")
    @GetMapping("/{id}")
//    @RequiresPermissions("research.project:annexFile:get")
    public ResponseMessage<AnnexFileEO> find(@PathVariable String id) throws Exception {
        return Result.success(annexFileEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|AnnexFileEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("research.project:annexFile:save")
    public ResponseMessage<AnnexFileEO> create(@RequestBody AnnexFileEO annexFileEO) throws Exception {
        annexFileEOService.insertSelective(annexFileEO);
        return Result.success(annexFileEO);
    }

    @ApiOperation(value = "|AnnexFileEO|新增")
    @PostMapping("/saveAnnexFileEO")
//    @RequiresPermissions("research.project:annexFile:save")
    public ResponseMessage<AnnexFileEO> saveAnnexFileEO(@RequestBody AnnexFileEO annexFileEO) throws Exception {
        annexFileEOService.insertAnnexFileEO(annexFileEO);
        return Result.success(annexFileEO);
    }

    @ApiOperation(value = "|AnnexFileEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("research.project:annexFile:update")
    public ResponseMessage<AnnexFileEO> update(@RequestBody AnnexFileEO annexFileEO) throws Exception {
        annexFileEOService.updateByPrimaryKeySelective(annexFileEO);
        return Result.success(annexFileEO);
    }

    @ApiOperation(value = "|AnnexFileEO|删除")
    @DeleteMapping("/{id}")
//    @RequiresPermissions("research.project:annexFile:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        annexFileEOService.deleteByPrimaryKey(id);
        logger.info("delete from RS_ANNEX_FILE where id = {}", id);
        return Result.success();
    }


    @ApiOperation(value = "|AnnexFileEO|批量新增")
    @PostMapping("/batchInsertSelective")
    public ResponseMessage<AnnexFileEO> batchInsertSelective(@RequestBody List<AnnexFileEO> annexFileEOS) throws Exception {
        annexFileEOService.batchInsertSelective(annexFileEOS);
        return Result.success();
    }

    @ApiOperation(value = "|AnnexFileEO|批量新增")
    @PostMapping("/batchInsertAnnexFileEO")
    public ResponseMessage<List<AnnexFileEO>> batchInsertAnnexFileEO(@RequestBody List<AnnexFileEO> annexFileEOList) throws Exception {
        List<AnnexFileEO> annexFileEOS = annexFileEOService.batchInsertAnnexFileEO(annexFileEOList);
        return Result.success(annexFileEOS);
    }

}
