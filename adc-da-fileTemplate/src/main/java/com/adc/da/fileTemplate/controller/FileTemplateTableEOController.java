package com.adc.da.fileTemplate.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.util.Arrays;
import java.util.List;

import com.adc.da.base.page.Pager;
import com.adc.da.fileTemplate.entity.FileTemplateEO;
import com.adc.da.fileTemplate.page.FileTemplateTableVOPage;
import com.adc.da.fileTemplate.vo.FileTemplateVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.adc.da.base.web.BaseController;
import com.adc.da.fileTemplate.entity.FileTemplateTableEO;
import com.adc.da.fileTemplate.page.FileTemplateTableEOPage;
import com.adc.da.fileTemplate.service.FileTemplateTableEOService;

import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.http.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/${restPath}/fileTemplate/fileTemplateTable")
@Api(description = "|FileTemplateTableEO|")
public class FileTemplateTableEOController extends BaseController<FileTemplateTableEO>{

    private static final Logger logger = LoggerFactory.getLogger(FileTemplateTableEOController.class);

    @Autowired
    private FileTemplateTableEOService fileTemplateTableEOService;

	@ApiOperation(value = "|FileTemplateTableEO|分页查询")
    @GetMapping("/page")
   // @RequiresPermissions("fileTemplate:fileTemplateTable:page")
    public ResponseMessage<PageInfo<FileTemplateTableEO>> page(FileTemplateTableEOPage page) throws Exception {
        List<FileTemplateTableEO> rows = fileTemplateTableEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

    public PageInfo<FileTemplateVO> getPageVO(Pager pager, List<FileTemplateVO> rows) {
        PageInfo<FileTemplateVO> pageInfo = new PageInfo();
        pageInfo.setList(rows);
        pageInfo.setCount((long)pager.getRowCount());
        pageInfo.setPageSize(pager.getPageSize());
        pageInfo.setPageCount((long)pager.getPageCount());
        pageInfo.setPageNo(pager.getPageId());
        return pageInfo;
    }


    @ApiOperation(value = "|FileTemplateTableVO|条件分页查询")
    @GetMapping("/pagepart")
    public ResponseMessage<PageInfo<FileTemplateVO>> queryPageByVO(FileTemplateTableVOPage page) throws Exception {
        List<FileTemplateVO> rows = fileTemplateTableEOService.queryPageVO(page);
        return Result.success(getPageVO(page.getPager(),rows));
    }

	@ApiOperation(value = "|FileTemplateTableEO|查询")
    @GetMapping("")
    //@RequiresPermissions("fileTemplate:fileTemplateTable:list")
    public ResponseMessage<List<FileTemplateTableEO>> list(FileTemplateTableEOPage page) throws Exception {
        return Result.success(fileTemplateTableEOService.queryByList(page));
	}

    @ApiOperation(value = "|FileTemplateTableEO|详情")
    @GetMapping("/{id}")
    //@RequiresPermissions("fileTemplate:fileTemplateTable:get")
    public ResponseMessage<FileTemplateTableEO> find(@PathVariable String id) throws Exception {
        return Result.success(fileTemplateTableEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|FileTemplateTableEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    //@RequiresPermissions("fileTemplate:fileTemplateTable:save")
    public ResponseMessage<FileTemplateTableEO> create(@RequestBody FileTemplateTableEO t) throws Exception {
        fileTemplateTableEOService.insertSelective(t);
        return Result.success(t);
    }

    @ApiOperation(value = "|FileTemplateTableEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    //@RequiresPermissions("fileTemplate:fileTemplateTable:update")
    public ResponseMessage<FileTemplateTableEO> update(@RequestBody FileTemplateTableEO t) throws Exception {
        fileTemplateTableEOService.updateByPrimaryKeySelective(t);
        return Result.success(t);
    }

    @ApiOperation(value = "|FileTemplateTableEO|删除")
    @DeleteMapping("/{id}")
    //@RequiresPermissions("fileTemplate:fileTemplateTable:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        fileTemplateTableEOService.deleteByPrimaryKey(id);
        logger.info("delete from FILE_TEMPLATE_TABLE where id = {}", id);
        return Result.success();
    }

    @ApiOperation(value = "|SalaryManagementEO|批量逻辑删除")
    @DeleteMapping("/logicDelete/{ids}")
//    @RequiresPermissions("finance:salaryManagement:delete")
    public ResponseMessage delete(@PathVariable String[] ids) throws Exception {
        fileTemplateTableEOService.logicDeleteByPrimaryKey(Arrays.asList(ids));
        logger.info("logic delete from FILE_TEMPLATE_TABLE where id = {}", ids);
        return Result.success();
    }



    @ApiOperation(value = "|FileTemplateTableEO|按照字典关联信息查询")
    @GetMapping("/dicTypeCode/{dicTypeCode}")
    //@RequiresPermissions("file:fileTemplateTable:message")
    public ResponseMessage<List<FileTemplateVO>> findDicMessage(@PathVariable String dicTypeCode) throws Exception {
        List<FileTemplateVO> fileTemplateVOList = fileTemplateTableEOService.queryByCode(dicTypeCode);
        return Result.success(fileTemplateVOList);
    }

    @ApiOperation(value = "|FileTemplateTableEO|按照模板编号查询")
    @GetMapping("/tempCode/{tempCode}")
    //@RequiresPermissions("file:fileTemplateTable:message")
    public ResponseMessage<List<FileTemplateVO>> queryByTempCode(@PathVariable String tempCode) throws Exception {
        List<FileTemplateVO> fileTemplateVOList = fileTemplateTableEOService.queryByTempCode(tempCode);
        return Result.success(fileTemplateVOList);
    }




}
