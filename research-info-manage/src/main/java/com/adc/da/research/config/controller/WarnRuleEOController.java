package com.adc.da.research.config.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.research.config.entity.WarnRuleEO;
import com.adc.da.research.config.page.WarnRuleEOPage;
import com.adc.da.research.config.service.WarnRuleEOService;
import com.adc.da.util.http.PageInfo;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
@RequestMapping("/${restPath}/research/warnRule")
@Api(tags = "科研系统|项目预警规则|WarnRuleEOController")
public class WarnRuleEOController extends BaseController<WarnRuleEO> {

    private static final Logger logger = LoggerFactory.getLogger(WarnRuleEOController.class);

    @Autowired
    private WarnRuleEOService warnRuleEOService;

	@ApiOperation(value = "|WarnRuleEO|分页查询")
    @GetMapping("/page")
//    @RequiresPermissions("research:warnRule:page")
    public ResponseMessage<PageInfo<WarnRuleEO>> page(WarnRuleEOPage page){
	    try {
            List<WarnRuleEO> rows = warnRuleEOService.queryByPage(page);
            return Result.success(getPageInfo(page.getPager(), rows));
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return Result.error("查询失败!" + e.getMessage());
        }
    }

	@ApiOperation(value = "|WarnRuleEO|查询")
    @GetMapping("")
//    @RequiresPermissions("research:warnRule:list")
    public ResponseMessage<List<WarnRuleEO>> list(WarnRuleEOPage page) throws Exception {
        return Result.success(warnRuleEOService.queryByList(page));
	}

    @ApiOperation(value = "|WarnRuleEO|详情")
    @GetMapping("/{id}")
//    @RequiresPermissions("research:warnRule:get")
    public ResponseMessage<WarnRuleEO> find(@PathVariable String id) throws Exception {
        return Result.success(warnRuleEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|WarnRuleEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("research:warnRule:save")
    public ResponseMessage create(@RequestBody WarnRuleEO warnRuleEO) {
	    try{
            warnRuleEOService.save(warnRuleEO);
            return Result.success("新增成功!");
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return Result.error("新增失败!" + e.getMessage());
        }
    }

    @ApiOperation(value = "|WarnRuleEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("research:warnRule:update")
    public ResponseMessage update(@RequestBody WarnRuleEO warnRuleEO) {
	    try {
            warnRuleEOService.update(warnRuleEO);
            return Result.success("编辑成功!");
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return Result.error("编辑失败!" + e.getMessage());
        }
    }

    @ApiOperation(value = "|WarnRuleEO|删除")
    @DeleteMapping("/{ids}")
//    @RequiresPermissions("research:warnRule:delete")
    public ResponseMessage delete(@ApiParam(value = "ids", required = true)
                                      @PathVariable String[] ids) {
	    try {
            if (ids.length == 0){
                return Result.error("请选择数据!");
            }
            warnRuleEOService.deleteByIds(ids);
            return Result.success("删除成功!");
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return Result.error("删除失败!" + e.getMessage());
        }
    }

    //添加消息通知
    @ApiOperation(value = "设置消息通知")
    @PutMapping("/saveNotice")
    public ResponseMessage saveNotice(){
	    try {
            warnRuleEOService.saveNotice();
            return Result.success("消息新增成功!");
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return Result.error("消息新增失败!" + e.getMessage());
        }
    }
}
