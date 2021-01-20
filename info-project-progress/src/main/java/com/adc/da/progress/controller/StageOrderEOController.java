package com.adc.da.progress.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.excel.poi.excel.entity.ImportParams;
import com.adc.da.progress.entity.ProjectNameEO;
import com.adc.da.progress.entity.StageOrderEO;
import com.adc.da.progress.page.ProjectNameEOPage;
import com.adc.da.progress.page.StageOrderEOPage;
import com.adc.da.progress.service.ProjectNameEOService;
import com.adc.da.progress.service.StageOrderEOService;
import com.adc.da.util.http.PageInfo;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.utils.UUID;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

//@RestController
//@RequestMapping("/${restPath}/progress/stageOrder")
//@Api(description = "|StageOrderEO|")
public class StageOrderEOController extends BaseController<StageOrderEO> {

    private static final Logger logger = LoggerFactory.getLogger(StageOrderEOController.class);

    @Autowired
    private StageOrderEOService stageOrderEOService;

    @Autowired
    private ProjectNameEOService projectNameEOService;

    @ApiOperation(value = "|StageOrderEO|分页查询")
    @GetMapping("/com/adc/da/file/page")
//    @RequiresPermissions("progress:stageOrder:page")
    public ResponseMessage<PageInfo<StageOrderEO>> page(StageOrderEOPage page) throws Exception {
        List<StageOrderEO> rows = stageOrderEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

    @ApiOperation(value = "|StageOrderEO|查询")
    @GetMapping("")
//    @RequiresPermissions("progress:stageOrder:list")
    public ResponseMessage<List<StageOrderEO>> list(StageOrderEOPage page) throws Exception {
        return Result.success(stageOrderEOService.queryByList(page));
    }

    @ApiOperation(value = "|StageOrderEO|详情")
    @GetMapping("/{id}")
//    @RequiresPermissions("progress:stageOrder:get")
    public ResponseMessage<StageOrderEO> find(@PathVariable String id) throws Exception {
        return Result.success(stageOrderEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|StageOrderEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("progress:stageOrder:save")
    public ResponseMessage<StageOrderEO> create(@RequestBody StageOrderEO stageOrderEO) throws Exception {
        stageOrderEO.setId(UUID.randomUUID10());
        stageOrderEOService.insertSelective(stageOrderEO);
        return Result.success(stageOrderEO);
    }

    @ApiOperation(value = "|StageOrderEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("progress:stageOrder:update")
    public ResponseMessage<StageOrderEO> update(@RequestBody StageOrderEO stageOrderEO) throws Exception {
        stageOrderEOService.updateByPrimaryKeySelective(stageOrderEO);
        return Result.success(stageOrderEO);
    }

    @ApiOperation(value = "|StageOrderEO|物理删除，慎用")
    @DeleteMapping("/{id}")
    @RequiresPermissions("progress:stageOrder:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        stageOrderEOService.deleteByPrimaryKey(id);
        logger.info("delete from PR_STAGE_ORDER where id = {}", id);
        return Result.success();
    }

    @ApiOperation(value = "|StageOrderEO|逻辑删除，flag值为1")
    @DeleteMapping(value = "/deleteByPrimaryKeysInFlag/{ids}")
    @RequiresPermissions("progress:stageOrder:delete")
    public ResponseMessage deleteByPrimaryKeysInFlag(@NotNull @PathVariable("ids") String[] ids) throws Exception {
        List<String> idList = Arrays.asList(ids);
        stageOrderEOService.deleteByPrimaryKeysInFlag(idList);
        logger.info("delete from PR_STAGE_ORDER where ids = {}", idList);
        return Result.success();
    }

    @ApiOperation(value = "Excel单sheet导入")
    @PostMapping("/excelImport")
    public ResponseMessage excelImport(@RequestParam("mybatis/mapper/file") MultipartFile file) throws Exception {
        InputStream is = file.getInputStream();
        ImportParams params = new ImportParams();
        return Result.success(stageOrderEOService.excelImport(is, params));
    }

    @Deprecated
    @ApiOperation(value = "|StageOrderEO|弃用方法")
    @PostMapping(value = "/update2")
//    @RequiresPermissions("progress:stageOrder:save")
    public ResponseMessage update2() throws Exception {
        List<StageOrderEO> stageOrderEOList = stageOrderEOService.queryByList(new StageOrderEOPage());
        List<ProjectNameEO> projectNameEOList = projectNameEOService.queryByList(new ProjectNameEOPage());
        for (StageOrderEO stageOrderEO : stageOrderEOList) {
            String id = stageOrderEO.getId();
            String newId = UUID.randomUUID10();
            stageOrderEO.setId(newId);
            stageOrderEOService.deleteByPrimaryKey(id);
            stageOrderEOService.insert(stageOrderEO);
            for (ProjectNameEO projectNameEO : projectNameEOList) {
                if (id.equals(projectNameEO.getStageOrderId())) {
                    String tempId = projectNameEO.getId();
                    projectNameEOService.deleteByPrimaryKey(tempId);
                    projectNameEO.setId(UUID.randomUUID10());
                    projectNameEO.setStageOrderId(newId);
                    projectNameEOService.insert(projectNameEO);
                }
            }
        }

        return Result.success();
    }

}
