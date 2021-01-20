package com.adc.da.research.project.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.research.project.entity.RContractInfoEO;
import com.adc.da.research.project.page.RContractInfoEOPage;
import com.adc.da.research.project.service.RContractInfoEOService;
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
@RequestMapping("/${restPath}/research/project/contractInfo")
@Api(tags = "科研系统|科研合同信息|RContractInfoEOController")
public class RContractInfoEOController extends BaseController<RContractInfoEO> {

    private static final Logger logger = LoggerFactory.getLogger(RContractInfoEOController.class);

    @Autowired
    private RContractInfoEOService RContractInfoEOService;

	@ApiOperation(value = "|RContractInfoEO|分页查询")
    @GetMapping("/page")
//    @RequiresPermissions("research.project:contractInfo:page")
    public ResponseMessage<PageInfo<RContractInfoEO>> page(RContractInfoEOPage page) throws Exception {
        List<RContractInfoEO> rows = RContractInfoEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|RContractInfoEO|查询")
    @GetMapping("")
//    @RequiresPermissions("research.project:contractInfo:list")
    public ResponseMessage<List<RContractInfoEO>> list(RContractInfoEOPage page) throws Exception {
        return Result.success(RContractInfoEOService.queryByList(page));
	}

    @ApiOperation(value = "|RContractInfoEO|详情")
    @GetMapping("/{id}")
//    @RequiresPermissions("research.project:contractInfo:get")
    public ResponseMessage<RContractInfoEO> find(@PathVariable String id) throws Exception {
        return Result.success(RContractInfoEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|RContractInfoEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("research.project:contractInfo:save")
    public ResponseMessage<RContractInfoEO> create(@RequestBody RContractInfoEO rContractInfoEO) throws Exception {
        RContractInfoEOService.insertSelective(rContractInfoEO);
        return Result.success(rContractInfoEO);
    }

    @ApiOperation(value = "|新增或修改")
    @PostMapping(value = "/insertOrUpdate",consumes = APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage<String> insertOrUpdate(@RequestBody RContractInfoEO rContractInfoEO) throws Exception {
        RContractInfoEOService.insertOrUpdate(rContractInfoEO);
        return Result.success("success");
    }
    @ApiOperation(value = "|RContractInfoEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("research.project:contractInfo:update")
    public ResponseMessage<RContractInfoEO> update(@RequestBody RContractInfoEO RContractInfoEO) throws Exception {
        RContractInfoEOService.updateByPrimaryKeySelective(RContractInfoEO);
        return Result.success(RContractInfoEO);
    }

    @ApiOperation(value = "|RContractInfoEO|修改")
    @PutMapping(value = "/updateRContractInfoEOByProjectId",consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("research.project:contractInfo:update")
    public ResponseMessage<RContractInfoEO> updateRContractInfoEOByProjectId(@RequestBody RContractInfoEO rContractInfoEO) throws Exception {
        RContractInfoEO newRContractInfoEO = RContractInfoEOService.updateRContractInfoEOByProjectId(rContractInfoEO);
        return Result.success(newRContractInfoEO);
    }

    @ApiOperation(value = "|RContractInfoEO|删除")
    @DeleteMapping("/{id}")
//    @RequiresPermissions("research.project:contractInfo:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        RContractInfoEOService.deleteByPrimaryKey(id);
        logger.info("delete from RS_CONTRACT_INFO where id = {}", id);
        return Result.success();
    }

}
