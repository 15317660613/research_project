package com.adc.da.research.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.research.entity.HiProjectProgressEO;
import com.adc.da.research.page.HiProjectProgressEOPage;
import com.adc.da.research.service.HiProjectProgressEOService;
import com.adc.da.util.http.PageInfo;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/${restPath}/research/hiProjectProgress")
@Api(tags = "|科研类项目模块-变更流程|")
@Slf4j
public class HiProjectProgressEOController extends BaseController<HiProjectProgressEO> {

    @Autowired
    private HiProjectProgressEOService hiProjectProgressEOService;

    @ApiOperation(value = "|HiProjectProgressEO|分页查询")
    @GetMapping("/page")
//    @RequiresPermissions("research:hiProjectProgress:page")
    public ResponseMessage<PageInfo<HiProjectProgressEO>> page(HiProjectProgressEOPage page) throws Exception {

        List<HiProjectProgressEO> rows = hiProjectProgressEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }



    @ApiOperation(value = "|HiProjectProgressEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("research:hiProjectProgress:save")
    public ResponseMessage<HiProjectProgressEO> create(@RequestBody HiProjectProgressEO hiProjectProgressEO)
        throws Exception {

        if (-1 == hiProjectProgressEOService.insertSelective(hiProjectProgressEO)) {
            return Result.error("1", "该检查内容已存在", hiProjectProgressEO);
        }
        return Result.success(hiProjectProgressEO);
    }

    /**
     * 修改-追加重名校验
     *
     * @param hiProjectProgressEO
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "|HiProjectProgressEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("research:hiProjectProgress:update")
    public ResponseMessage<HiProjectProgressEO> update(@RequestBody HiProjectProgressEO hiProjectProgressEO)
        throws Exception {
        if (-1 == hiProjectProgressEOService.updateAndSetMask(hiProjectProgressEO)) {
            return Result.error("1", "该检查内容已存在", hiProjectProgressEO);
        } else {
            return Result.success(hiProjectProgressEO);
        }
    }

    /**
     * 删除。根据key与id
     *
     * @param id
     * @param businessKey
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "|HiProjectProgressEO|删除")
    @DeleteMapping("/{id}/{businessKey}")
//    @RequiresPermissions("research:hiProjectProgress:delete")
    public ResponseMessage delete(@PathVariable String id, @PathVariable String businessKey) {
        hiProjectProgressEOService.deleteByPrimaryKey(id, businessKey);
        log.info("delete from RS_HI_PROJECT_PROGRESS where id = {}, businessKey = {}", id, businessKey);
        return Result.success();
    }

}
