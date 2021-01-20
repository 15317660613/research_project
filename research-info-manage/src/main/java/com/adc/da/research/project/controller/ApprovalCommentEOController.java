package com.adc.da.research.project.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.research.project.entity.ApprovalCommentEO;
import com.adc.da.research.project.page.ApprovalCommentEOPage;
import com.adc.da.research.project.service.ApprovalCommentEOService;
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
@RequestMapping("/${restPath}/research/project/approvalComment")
@Api(tags = "科研系统|意见信息|ApprovalCommentEOController")
public class ApprovalCommentEOController extends BaseController<ApprovalCommentEO> {

    private static final Logger logger = LoggerFactory.getLogger(ApprovalCommentEOController.class);

    @Autowired
    private ApprovalCommentEOService approvalCommentEOService;

	@ApiOperation(value = "|ApprovalCommentEO|分页查询")
    @GetMapping("/page")
//    @RequiresPermissions("research.project:approvalComment:page")
    public ResponseMessage<PageInfo<ApprovalCommentEO>> page(ApprovalCommentEOPage page) throws Exception {
        List<ApprovalCommentEO> rows = approvalCommentEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|ApprovalCommentEO|查询")
    @GetMapping("")
//    @RequiresPermissions("research.project:approvalComment:list")
    public ResponseMessage<List<ApprovalCommentEO>> list(ApprovalCommentEOPage page) throws Exception {
        return Result.success(approvalCommentEOService.queryByList(page));
	}

    @ApiOperation(value = "|ApprovalCommentEO|详情")
    @GetMapping("/{id}")
//    @RequiresPermissions("research.project:approvalComment:get")
    public ResponseMessage<ApprovalCommentEO> find(@PathVariable String id) throws Exception {
        return Result.success(approvalCommentEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|ApprovalCommentEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("research.project:approvalComment:save")
    public ResponseMessage<ApprovalCommentEO> create(@RequestBody ApprovalCommentEO approvalCommentEO) throws Exception {
        approvalCommentEOService.insertCheckToken(approvalCommentEO);
        return Result.success();
    }

    @ApiOperation(value = "|BudgetFundEO|批量新增")
    @PostMapping("/batchInsertApprovalCommentEO")
    public ResponseMessage<ApprovalCommentEO> batchInsertSelective(@RequestBody List<ApprovalCommentEO> approvalCommentEOList) throws Exception {
        approvalCommentEOService.batchInsertSelective(approvalCommentEOList);
        return Result.success();
    }

    @ApiOperation(value = "|ApprovalCommentEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("research.project:approvalComment:update")
    public ResponseMessage<ApprovalCommentEO> update(@RequestBody ApprovalCommentEO approvalCommentEO) throws Exception {
        approvalCommentEOService.updateByPrimaryKeySelective(approvalCommentEO);
        return Result.success(approvalCommentEO);
    }

    @ApiOperation(value = "|ApprovalCommentEO|删除")
    @DeleteMapping("/{id}")
//    @RequiresPermissions("research.project:approvalComment:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        approvalCommentEOService.deleteByPrimaryKey(id);
        logger.info("delete from RS_APPROVAL_COMMENT where id = {}", id);
        return Result.success();
    }

}
