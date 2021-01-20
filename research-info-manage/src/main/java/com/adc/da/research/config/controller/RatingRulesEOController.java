package com.adc.da.research.config.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.research.config.entity.RatingRulesEO;
import com.adc.da.research.config.page.RatingRulesEOPage;
import com.adc.da.research.config.service.RatingRulesEOService;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.http.PageInfo;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import javax.validation.constraints.NotNull;
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
@RequestMapping("/${restPath}/research/ratingRules")
@Api(tags = "科研系统|评分规则信息|RatingRulesEOController")
public class RatingRulesEOController extends BaseController<RatingRulesEO> {

    private static final Logger logger = LoggerFactory.getLogger(RatingRulesEOController.class);

    @Autowired
    private RatingRulesEOService ratingRulesEOService;

    @ApiOperation(value = "|RatingRulesEO|分页查询")
    @GetMapping("/page")
//    @RequiresPermissions("research:ratingRules:page")
    public ResponseMessage<PageInfo<RatingRulesEO>> page(RatingRulesEOPage page) throws Exception {
        page.setRatingRulesNameOperator("LIKE");
        List<RatingRulesEO> rows = ratingRulesEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

    @ApiOperation(value = "|RatingRulesEO|查询")
    @GetMapping("")
//    @RequiresPermissions("research:ratingRules:list")
    public ResponseMessage<List<RatingRulesEO>> list(RatingRulesEOPage page) throws Exception {
        return Result.success(ratingRulesEOService.queryByList(page));
    }

    @ApiOperation(value = "|RatingRulesEO|详情")
    @GetMapping("/{id}")
//    @RequiresPermissions("research:ratingRules:get")
    public ResponseMessage<RatingRulesEO> find(@PathVariable String id) throws Exception {
        return Result.success(ratingRulesEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|RatingRulesEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("research:ratingRules:save")
    public ResponseMessage<RatingRulesEO> create(@RequestBody RatingRulesEO ratingRulesEO) throws Exception {
        String templateName=ratingRulesEO.getRatingRulesName();
        if(StringUtils.isEmpty(templateName)){
            throw new AdcDaBaseException("空的模板名");
        }
        try{
            List<RatingRulesEO> ratingRulesEOList=ratingRulesEOService.findByName(templateName);
            if(CollectionUtils.isNotEmpty(ratingRulesEOList)){
                throw new AdcDaBaseException("操作失败：重复的模板名称");
            }
            ratingRulesEOService.insertSelective(ratingRulesEO);
            return Result.success(ratingRulesEO);
        }catch (Exception ex){
            return Result.error("操作失败："+ex.getMessage());
        }

    }

    @ApiOperation(value = "|RatingRulesEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("research:ratingRules:update")
    public ResponseMessage<RatingRulesEO> update(@RequestBody RatingRulesEO ratingRulesEO) throws Exception {
        ratingRulesEOService.updateByPrimaryKeySelective(ratingRulesEO);
        return Result.success(ratingRulesEO);
    }

    @ApiOperation(value = "|RatingRulesEO|删除")
    @DeleteMapping("/{id}")
//    @RequiresPermissions("research:ratingRules:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        ratingRulesEOService.deleteByPrimaryKey(id);
        logger.info("delete from RS_RATING_RULES where id = {}", id);
        return Result.success();
    }

    @ApiOperation(value = "|RatingRulesEO|批量删除")
    @DeleteMapping("/batchDeleteByIds/{ids}")
    public ResponseMessage batchDeleteByIds(@NotNull @PathVariable("ids") List<String> ids) throws Exception {
        ratingRulesEOService.batchDeleteByIds(ids);
        return Result.success();
    }

    @ApiOperation(value = "|RatingRulesEO|批量新增")
    @PostMapping("/batchInsertSelective")
    public ResponseMessage<RatingRulesEO> create(@RequestBody List<RatingRulesEO> ratingRulesEOS) throws Exception {
        ratingRulesEOService.batchInsertSelective(ratingRulesEOS);
        return Result.success();
    }

}
