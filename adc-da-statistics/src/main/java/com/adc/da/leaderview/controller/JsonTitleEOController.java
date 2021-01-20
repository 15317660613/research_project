package com.adc.da.leaderview.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.leaderview.entity.JsonTitleEO;
import com.adc.da.leaderview.page.JsonTitleEOPage;
import com.adc.da.leaderview.service.JsonTitleEOService;
import com.adc.da.util.exception.AdcDaBaseException;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping("/${restPath}/leaderview/jsonTitle")
@Api(tags = "|领导视角|")
@Slf4j
public class JsonTitleEOController extends BaseController<JsonTitleEO> {

    @Autowired
    private JsonTitleEOService jsonTitleEOService;

    @ApiOperation(value = "|JsonTitleEO|分页查询")
    @GetMapping("/page")
    //@RequiresPermissions("leaderview:jsonTitle:page")
    public ResponseMessage<PageInfo<JsonTitleEO>> page(JsonTitleEOPage page) throws Exception {
        List<JsonTitleEO> rows = jsonTitleEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

    @ApiOperation(value = "|JsonTitleEO|查询")
    @GetMapping("")
    //@RequiresPermissions("leaderview:jsonTitle:list")
    public ResponseMessage<List<JsonTitleEO>> list(JsonTitleEOPage page) throws Exception {
        return Result.success(jsonTitleEOService.queryByList(page));
    }

    @ApiOperation(value = "|JsonTitleEO|详情")
    @GetMapping("/{id}")
    //@RequiresPermissions("leaderview:jsonTitle:get")
    public ResponseMessage<JsonTitleEO> find(@PathVariable String id) throws Exception {
        return Result.success(jsonTitleEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|JsonTitleEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    //@RequiresPermissions("leaderview:jsonTitle:save")
    public ResponseMessage<JsonTitleEO> create(@RequestBody JsonTitleEO jsonTitleEO) throws Exception {
        jsonTitleEOService.insertSelective(jsonTitleEO);
        return Result.success(jsonTitleEO);
    }

    @ApiOperation(value = "|JsonTitleEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    //@RequiresPermissions("leaderview:jsonTitle:update")
    public ResponseMessage<JsonTitleEO> update(@RequestBody JsonTitleEO jsonTitleEO) throws Exception {
        jsonTitleEOService.updateByPrimaryKeySelective(jsonTitleEO);
        return Result.success(jsonTitleEO);
    }

    @ApiOperation(value = "|JsonTitleEO|删除")
    @DeleteMapping("/{id}")
    //@RequiresPermissions("leaderview:jsonTitle:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        jsonTitleEOService.deleteByPrimaryKey(id);
        log.info("delete from ST_JSON_TITLE where id = {}", id);
        return Result.success();
    }

    /**
     * 根据当前登录用户进行领导视角菜单回显校验
     *
     * @return
     */
    @ApiOperation("领导视角菜单")
    @GetMapping("/leaderView")
    public ResponseMessage<Map<String, Object>> listLeaderView() {
        Map<String, Object> resultMap = new HashMap<>();
        ///try {
            resultMap.put("data", jsonTitleEOService.queryByListWithLevelLeaderView());
            return Result.success(resultMap);
        //} catch (AdcDaBaseException e) {
         //   return Result.error("-1", e.getMessage(), null);
        //}
    }
}
