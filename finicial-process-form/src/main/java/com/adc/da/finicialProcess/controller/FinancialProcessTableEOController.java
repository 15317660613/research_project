package com.adc.da.finicialProcess.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.finicialProcess.entity.FinancialProcessTableEO;
import com.adc.da.finicialProcess.page.FinancialProcessTableEOPage;
import com.adc.da.finicialProcess.service.FinancialProcessTableEOService;
import com.adc.da.login.util.UserUtils;
import com.adc.da.smallprogram.entity.ScheudlePermissionEO;
import com.adc.da.smallprogram.page.ScheudlePermissionEOPage;
import com.adc.da.smallprogram.service.ScheudlePermissionEOService;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.http.PageInfo;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.utils.StringUtils;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping("/${restPath}/finicialProcess/financialProcessTable")
@Api(description = "|FinancialProcessTableEO|")
public class FinancialProcessTableEOController extends BaseController<FinancialProcessTableEO>{

    private static final Logger logger = LoggerFactory.getLogger(FinancialProcessTableEOController.class);

    @Autowired
    private FinancialProcessTableEOService financialProcessTableEOService;

    @Autowired
    private ScheudlePermissionEOService scheudlePermissionEOService;

	@ApiOperation(value = "|FinancialProcessTableEO|分页查询")
    @GetMapping("/page")
    //@RequiresPermissions("finicialProcess:financialProcessTable:page")
    public ResponseMessage<PageInfo<FinancialProcessTableEO>> page(FinancialProcessTableEOPage page) throws Exception {
        String userId = UserUtils.getUserId();
        if (StringUtils.isEmpty(userId)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
	    page.setOrder("desc");
	    page.setOrderBy("receive_time");

        ScheudlePermissionEOPage scheudlePermissionEOPage = new ScheudlePermissionEOPage();
        scheudlePermissionEOPage.setDestUserId(userId);
        scheudlePermissionEOPage.setConfigType("1");
        List<ScheudlePermissionEO> scheudlePermissionEOList = scheudlePermissionEOService.queryByList(scheudlePermissionEOPage);
        List<String> originUserIdList = new ArrayList<>();
        for (ScheudlePermissionEO scheudlePermissionEO : scheudlePermissionEOList){
            originUserIdList.add(scheudlePermissionEO.getOriginUserId());
        }
        originUserIdList.add(userId);
	    page.setReceiveUserIdList(originUserIdList);
	    List<FinancialProcessTableEO> rows = financialProcessTableEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|FinancialProcessTableEO|查询")
    @GetMapping("")
    //@RequiresPermissions("finicialProcess:financialProcessTable:list")
    public ResponseMessage<List<FinancialProcessTableEO>> list(FinancialProcessTableEOPage page) throws Exception {
        return Result.success(financialProcessTableEOService.queryByList(page));
	}

    @ApiOperation(value = "|FinancialProcessTableEO|详情")
    @GetMapping("/{id}")
    //@RequiresPermissions("finicialProcess:financialProcessTable:get")
    public ResponseMessage<FinancialProcessTableEO> find(@PathVariable String id) throws Exception {
        return Result.success(financialProcessTableEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|FinancialProcessTableEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    //@RequiresPermissions("finicialProcess:financialProcessTable:save")
    public ResponseMessage<FinancialProcessTableEO> create(@RequestBody FinancialProcessTableEO financialProcessTableEO) throws Exception {
        financialProcessTableEO.setState(0);
	    financialProcessTableEOService.save(financialProcessTableEO);
        return Result.success(financialProcessTableEO);
    }

    @ApiOperation(value = "|FinancialProcessTableEO|发送")
    @PostMapping(value = "/send",consumes = APPLICATION_JSON_UTF8_VALUE)
    //@RequiresPermissions("finicialProcess:financialProcessTable:save")
    public ResponseMessage<FinancialProcessTableEO> send(@RequestBody FinancialProcessTableEO financialProcessTableEO) throws Exception {
        financialProcessTableEOService.send(financialProcessTableEO);
        return Result.success(financialProcessTableEO);
    }

    @ApiOperation(value = "|FinancialProcessTableEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    //@RequiresPermissions("finicialProcess:financialProcessTable:update")
    public ResponseMessage<FinancialProcessTableEO> update(@RequestBody FinancialProcessTableEO financialProcessTableEO) throws Exception {
        financialProcessTableEOService.updateByPrimaryKeySelective(financialProcessTableEO);
        return Result.success(financialProcessTableEO);
    }

    @ApiOperation(value = "|FinancialProcessTableEO|删除")
    @DeleteMapping("/{ids}")
    //@RequiresPermissions("finicialProcess:financialProcessTable:delete")
    public ResponseMessage delete(@NotNull @PathVariable("ids") String[] ids) throws Exception {
        List<String> idList = Arrays.asList(ids);
        financialProcessTableEOService.deleteByKey(idList);
        return Result.success();
    }

}
