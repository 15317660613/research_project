package com.adc.da.research.project.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.research.project.entity.JudgeInfoEO;
import com.adc.da.research.project.page.JudgeInfoEOPage;
import com.adc.da.research.project.service.JudgeInfoEOService;
import com.adc.da.research.project.vo.JudgeInfoVO;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping("/${restPath}/research/project/judgeInfo")
@Api(tags = "科研系统|科研项目评审|JudgeInfoEOController")
public class JudgeInfoEOController extends BaseController<JudgeInfoEO> {

    private static final Logger logger = LoggerFactory.getLogger(JudgeInfoEOController.class);

    @Autowired
    private JudgeInfoEOService judgeInfoEOService;

	@ApiOperation(value = "|JudgeInfoEO|分页查询")
    @GetMapping("/page")
//    @RequiresPermissions("research.project:judgeInfo:page")
    public ResponseMessage<PageInfo<JudgeInfoEO>> page(JudgeInfoEOPage page) throws Exception {
        List<JudgeInfoEO> rows = judgeInfoEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|JudgeInfoEO|查询")
    @GetMapping("")
//    @RequiresPermissions("research.project:judgeInfo:list")
    public ResponseMessage<List<JudgeInfoEO>> list(JudgeInfoEOPage page) throws Exception {
        return Result.success(judgeInfoEOService.queryByList(page));
	}

    @ApiOperation(value = "|JudgeInfoEO|详情")
    @GetMapping("/{id}")
//    @RequiresPermissions("research.project:judgeInfo:get")
    public ResponseMessage<JudgeInfoEO> find(@PathVariable String id) throws Exception {
        return Result.success(judgeInfoEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|JudgeInfoEO|新增或修改")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("research.project:judgeInfo:save")
    public ResponseMessage<JudgeInfoVO> create(@RequestBody JudgeInfoVO judgeInfoVO) throws Exception {
        judgeInfoEOService.insertJudgeInfoVO(judgeInfoVO);
        return Result.success(judgeInfoVO);
    }

    //@ApiOperation(value = "|JudgeInfoEO|修改")
    //@PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("research.project:judgeInfo:update")
    public ResponseMessage<JudgeInfoEO> update(@RequestBody JudgeInfoEO judgeInfoEO) throws Exception {
        judgeInfoEOService.updateByPrimaryKeySelective(judgeInfoEO);
        return Result.success(judgeInfoEO);
    }

    @ApiOperation(value = "|JudgeInfoEO|删除")
    @DeleteMapping("/{id}")
//    @RequiresPermissions("research.project:judgeInfo:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        judgeInfoEOService.deleteByPrimaryKey(id);
        logger.info("delete from RS_JUDGE_INFO where id = {}", id);
        return Result.success();
    }

    @ApiOperation(value = "|JudgeInfoEO|批量新增")
    @PostMapping("/batchInsertSelective")
    public ResponseMessage<JudgeInfoEO> batchInsertSelective(@RequestBody JudgeInfoVO judgeInfoVO) throws Exception {
        judgeInfoEOService.batchInsertSelective(judgeInfoVO);
        return Result.success();
    }

}
