package com.adc.da.research.project.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.research.project.entity.ScoreInfoEO;
import com.adc.da.research.project.page.ScoreInfoEOPage;
import com.adc.da.research.project.service.ScoreInfoEOService;
import com.adc.da.research.project.vo.ScoreInfoVO;
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
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping("/${restPath}/research/project/scoreInfo")
@Api(tags = "科研系统|项目评分细则|ScoreInfoEOController")
public class ScoreInfoEOController extends BaseController<ScoreInfoEO> {

    private static final Logger logger = LoggerFactory.getLogger(ScoreInfoEOController.class);

    @Autowired
    private ScoreInfoEOService scoreInfoEOService;

	@ApiOperation(value = "|ScoreInfoEO|分页查询")
    @GetMapping("/page")
//    @RequiresPermissions("research.project:scoreInfo:page")
    public ResponseMessage<PageInfo<ScoreInfoEO>> page(ScoreInfoEOPage page) throws Exception {
        List<ScoreInfoEO> rows = scoreInfoEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|ScoreInfoEO|查询")
    @GetMapping("")
//    @RequiresPermissions("research.project:scoreInfo:list")
    public ResponseMessage<Map<String,Object>> list(ScoreInfoEOPage page) throws Exception {
        return Result.success(scoreInfoEOService.queryByArr(page));
	}

    @ApiOperation(value = "|ScoreInfoEO|查询")
    @GetMapping("/queryByUser")
//    @RequiresPermissions("research.project:scoreInfo:list")
    public ResponseMessage<List<Map<String,Object>>> queryByUser(ScoreInfoEOPage page) throws Exception {
        return Result.success(scoreInfoEOService.queryByUser(page));
    }

    @ApiOperation(value = "|ScoreInfoEO|详情")
    @GetMapping("/{id}")
//    @RequiresPermissions("research.project:scoreInfo:get")
    public ResponseMessage<ScoreInfoEO> find(@PathVariable String id) throws Exception {
        return Result.success(scoreInfoEOService.selectByPrimaryKey(id));
    }

   /* @ApiOperation(value = "|ScoreInfoEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage<ScoreInfoEO> create(@RequestBody ScoreInfoEO scoreInfoEO) throws Exception {
        scoreInfoEOService.insertSelective(scoreInfoEO);
        return Result.success(scoreInfoEO);
    }*/

    @ApiOperation(value = "|ScoreInfoEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("research.project:scoreInfo:update")
    public ResponseMessage<ScoreInfoEO> update(@RequestBody ScoreInfoEO scoreInfoEO) throws Exception {
        scoreInfoEOService.updateByPrimaryKeySelective(scoreInfoEO);
        return Result.success(scoreInfoEO);
    }

    @ApiOperation(value = "|ScoreInfoEO|删除")
    @DeleteMapping("/{id}")
//    @RequiresPermissions("research.project:scoreInfo:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        scoreInfoEOService.deleteByPrimaryKey(id);
        logger.info("delete from RS_SCORE_INFO where id = {}", id);
        return Result.success();
    }
    @ApiOperation(value = "|新增或修改项目评分|")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage<ScoreInfoEO> create(@RequestBody List<ScoreInfoEO> ScoreInfoEOS) throws Exception {
        scoreInfoEOService.insertOrUpdate(ScoreInfoEOS);
        return Result.success();
    }
    @ApiOperation(value = "|管理员审核线下|")
    @PostMapping(value = "/adminUpdate",consumes = APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage adminUpdate(@RequestBody List<ScoreInfoEO> ScoreInfoEOS) throws Exception {
        scoreInfoEOService.adminUpdate(ScoreInfoEOS);
        return Result.success();
    }
    @ApiOperation(value = "|管理员审核线上|")
    @PostMapping(value = "/adminUpdateOnLine",consumes = APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage adminUpdateOnLine(@RequestBody List<ScoreInfoEO> ScoreInfoEOS) throws Exception {
        scoreInfoEOService.adminUpdateOnLine(ScoreInfoEOS);
        return Result.success();
    }
    @ApiOperation(value = "|撤回|")
    @PostMapping("/recall")
    public ResponseMessage recall(@RequestBody ScoreInfoEO scoreInfoEO) throws Exception {
        scoreInfoEOService.recall(scoreInfoEO);
        return Result.success();
    }
    @ApiOperation(value = "|专家评分|")
    @PostMapping("/submitScore")
    public ResponseMessage submitScore(@RequestBody ScoreInfoVO scoreInfoVO) throws Exception {
        scoreInfoEOService.submitScore(scoreInfoVO);
        return Result.success();
    }

    @ApiOperation(value = "|ScoreInfoEO|查询所有专家评分--管理员审核页")
    @GetMapping("/queryByexpertUser")
    public ResponseMessage<List<Map<String,Object>>> queryByexpertUser(ScoreInfoEOPage page) throws Exception {
        return Result.success(scoreInfoEOService.queryByexpertUser(page));
    }


}
