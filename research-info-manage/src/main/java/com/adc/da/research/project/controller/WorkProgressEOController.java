package com.adc.da.research.project.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.research.project.entity.WorkProgressEO;
import com.adc.da.research.project.page.WorkProgressEOPage;
import com.adc.da.research.project.service.ImplementationProcFileEOService;
import com.adc.da.research.project.service.WorkProgressEOService;
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

import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping("/${restPath}/research/project/workProgress")
@Api(tags = "科研系统|科研项目工作进度|WorkProgressEOController")
public class WorkProgressEOController extends BaseController<WorkProgressEO> {

    private static final Logger logger = LoggerFactory.getLogger(WorkProgressEOController.class);

    @Autowired
    private WorkProgressEOService workProgressEOService;

    @Autowired
    private ImplementationProcFileEOService implementationProcFileEOService;

	@ApiOperation(value = "|WorkProgressEO|分页查询")
    @GetMapping("/page")
//    @RequiresPermissions("research.project:workProgress:page")
    public ResponseMessage<PageInfo<WorkProgressEO>> page(WorkProgressEOPage page) throws Exception {
	    List<WorkProgressEO> rows = workProgressEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|WorkProgressEO|查询")
    @GetMapping("")
//    @RequiresPermissions("research.project:workProgress:list")
    public ResponseMessage<List<WorkProgressEO>> list(WorkProgressEOPage page) throws Exception {
	    return Result.success(workProgressEOService.queryByList(page));
	}


    @ApiOperation(value = "|WorkProgressEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("research.project:workProgress:save")
    public ResponseMessage<WorkProgressEO> create(@RequestBody WorkProgressEO workProgressEO) throws Exception {
        workProgressEOService.saveWorkProgress(workProgressEO);
        return Result.success(workProgressEO);
    }

    @ApiOperation(value = "|WorkProgressEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("research.project:workProgress:update")
    public ResponseMessage<WorkProgressEO> update(@RequestBody WorkProgressEO workProgressEO) throws Exception {
        workProgressEOService.updateWorkProgress(workProgressEO);
        return Result.success(workProgressEO);
    }

    @ApiOperation(value = "|WorkProgressEO|删除")
    @DeleteMapping("/{id}")
//    @RequiresPermissions("research.project:workProgress:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        workProgressEOService.deleteByPrimaryKey(id);
        logger.info("delete from RS_WORK_PROGRESS where id = {}", id);
        return Result.success();
    }

    @ApiOperation(value = "|WorkProgressEO|逻辑删除")
    @DeleteMapping("/deleteLogic/{ids}")
//    @RequiresPermissions("research.project:workProgress:delete")
    public ResponseMessage deleteLogic(@NotNull @PathVariable("ids") String[] ids) throws Exception {
        List<String> idList = Arrays.asList(ids);
        workProgressEOService.deleteLogicInBatch(idList);
        logger.info("delete from RS_WORK_PROGRESS where id = {}", ids);
        return Result.success();
    }


    @ApiOperation(value = "|WorkProgressEO|批量新增")
    @PostMapping("/batchInsertSelective")
    public ResponseMessage<WorkProgressEO> batchInsertSelective(@RequestBody List<WorkProgressEO> workProgressEOS) throws Exception {
        workProgressEOService.batchInsertSelective(workProgressEOS);
        return Result.success();
    }


    @ApiOperation(value = "|WorkProgressEO|根据id获取检查详情")
    @GetMapping(value = "/getCheckById",consumes = APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage<WorkProgressEO> getCheckById( WorkProgressEO progressEO) throws Exception {
        return Result.success(workProgressEOService.getCheckById(progressEO));
    }

    @ApiOperation(value = "|WorkProgressEO|发起检查弹窗中的确定按钮")
    @PutMapping("/submitInfo")
    public ResponseMessage submitInfo(@RequestBody WorkProgressEO workProgressEO) throws Exception{

	    workProgressEOService.submitComment(workProgressEO);
	    return Result.success("success");
    }

    @ApiOperation(value = "|WorkProgressEO|修改提交弹窗中的确定按钮")
    @PutMapping("/submitUpdateInfo")
    public ResponseMessage submitUpdateInfo(@RequestBody List<WorkProgressEO> workProgressEOList) throws Exception{

        workProgressEOService.submitUpdateInfo(workProgressEOList);
        return Result.success("success");
    }

    /**
     *项目执行 检查页面查看所有发起检查 参数项目id,执行过程id
     * @return
     */
    @ApiOperation(value = "|WorkProgressEO|项目执行 检查页面查看所有发起检查")
    @GetMapping("/getSubmitCheckList")
    public  ResponseMessage<List<WorkProgressEO>> getSubmitCheckList(WorkProgressEOPage page) throws Exception {
        return Result.success(workProgressEOService.getSubmitCheckList(page));

    }
    /**
     *项目执行 检查提交按钮 参数项目id,执行过程id
     * @return
     */
    @ApiOperation(value = "|项目执行 检查提交按钮|")
    @PostMapping(value = "/insertSubmitCheckList",consumes = APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage  insertSubmitCheckList(@RequestBody List<WorkProgressEO> eoList) throws Exception {
        workProgressEOService.insertSubmitCheckList(eoList);
        return Result.success();
    }



    @ApiOperation(value = "|ADC项目合同书考核指标")
    @GetMapping("/getCheckTargetList")
    public ResponseMessage<Map<String,List<WorkProgressEO>>> getCheckTargetList(WorkProgressEOPage page) throws Exception {
        return Result.success(workProgressEOService.getCheckTargetList(page));
    }

    @ApiOperation(value = "|项目执行发起检查下拉框")
    @GetMapping("/sponsorCheckDownList")
    public ResponseMessage<List<WorkProgressEO>> sponsorCheckDownList(WorkProgressEOPage page) throws Exception {
        return Result.success(workProgressEOService.sponsorCheckDownList(page));
    }
    @ApiOperation(value = "|项目执行撤回发起检查|")
    @PostMapping("/recall")
    public ResponseMessage recall(@RequestBody WorkProgressEO progressEO) throws Exception {
        workProgressEOService.recall(progressEO);
        return Result.success();
    }


    @ApiOperation(value="|WorkProgressEO|项目验收-->验收申请")
    @PutMapping("/checkApply")
    public ResponseMessage checkApply(@RequestBody WorkProgressEOPage page) throws Exception{
	    return Result.success(workProgressEOService.checkApply(page));
    }
    //	查询详情
    @ApiOperation(value = "|WorkProgressEO|验收详情")
    @GetMapping("/getAllInfo")
//    @RequiresPermissions("research.project:workProgress:get")
    public ResponseMessage<WorkProgressEO> find(WorkProgressEOPage page) throws Exception {

        return Result.success(workProgressEOService.getInfo(page));
    }


    @ApiOperation(value = "|WorkProgressEO|项目验收-->验收结项-->通过按钮")
    @PostMapping("/checkProjectPass")
    public ResponseMessage checkProjectPass( @RequestBody WorkProgressEOPage page) throws Exception{
	    return Result.success(workProgressEOService.checkProjectPass(page));
    }

    @ApiOperation(value="|WorkProgressEO|项目验收-->验收结项-->不通过按钮")
    @PostMapping("/checkProjectReject")
    public ResponseMessage checkProjectReject( @RequestBody WorkProgressEOPage page) throws Exception{
	    return Result.success(workProgressEOService.checkProjectReject(page));
    }

    @ApiOperation(value = "|WorkProgressEO|项目验收-->操作栏的撤回操作")
    @PutMapping("/reBack")
    public ResponseMessage reBack(@RequestBody WorkProgressEOPage page) throws Exception{
        workProgressEOService.reBack(page);
	    return Result.success();
    }


    @ApiOperation(value = "|WorkProgressEO|项目验收-->修改提交-->提交按钮 Temporary don't have to use ")
    @PutMapping("/updateSubmit")
    public ResponseMessage updateSubmit( @RequestBody WorkProgressEOPage page){
	    return Result.success(workProgressEOService.updateSubmit(page));
    }


}
