package com.adc.da.smallprogram.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

import java.util.List;
import java.util.Map;

import com.adc.da.smallprogram.entity.ScheduleSupportEO;
import com.adc.da.smallprogram.service.ScheduleSupportEOService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.adc.da.base.web.BaseController;
import com.adc.da.smallprogram.entity.ScheduleSupportUserEO;
import com.adc.da.smallprogram.page.ScheduleSupportUserEOPage;
import com.adc.da.smallprogram.service.ScheduleSupportUserEOService;

import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.http.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;

@RestController
@RequestMapping("/${restPath}/smallprogram/scheduleSupportUser")
@Api(description = "|ScheduleSupportUserEO|")
public class ScheduleSupportUserEOController extends BaseController<ScheduleSupportUserEO>{

    private static final Logger logger = LoggerFactory.getLogger(ScheduleSupportUserEOController.class);

    @Autowired
    private ScheduleSupportUserEOService scheduleSupportUserEOService;
    @Autowired
    private ScheduleSupportEOService scheduleSupportEOService ;

	@ApiOperation(value = "|ScheduleSupportUserEO|分页查询")
    @GetMapping("/page")
//    @RequiresPermissions("smallprogram:scheduleSupportUser:page")
    public ResponseMessage<PageInfo<ScheduleSupportUserEO>> page(ScheduleSupportUserEOPage page) throws Exception {
        List<ScheduleSupportUserEO> rows = scheduleSupportUserEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|ScheduleSupportUserEO|查询")
    @GetMapping("")
//    @RequiresPermissions("smallprogram:scheduleSupportUser:list")
    public ResponseMessage<List<ScheduleSupportUserEO>> list(ScheduleSupportUserEOPage page) throws Exception {
        return Result.success(scheduleSupportUserEOService.queryByList(page));
	}

    @ApiOperation(value = "|ScheduleSupportUserEO|详情")
    @GetMapping("/{id}")
//    @RequiresPermissions("smallprogram:scheduleSupportUser:get")
    public ResponseMessage<ScheduleSupportUserEO> find(@PathVariable String id) throws Exception {
        return Result.success(scheduleSupportUserEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|ScheduleSupportUserEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("smallprogram:scheduleSupportUser:save")
    public ResponseMessage<ScheduleSupportUserEO> create(@RequestBody ScheduleSupportUserEO scheduleSupportUserEO) throws Exception {
        scheduleSupportUserEOService.insertSelective(scheduleSupportUserEO);
        return Result.success(scheduleSupportUserEO);
    }

    @ApiOperation(value = "|ScheduleSupportUserEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("smallprogram:scheduleSupportUser:update")
    public ResponseMessage<ScheduleSupportUserEO> update(@RequestBody ScheduleSupportUserEO scheduleSupportUserEO) throws Exception {
        boolean flag = true ;
        String supportId = "";
	    scheduleSupportUserEOService.updateByPrimaryKeySelective(scheduleSupportUserEO);
        ScheduleSupportUserEO supportUser = scheduleSupportUserEOService.selectByPrimaryKey(scheduleSupportUserEO.getId());
        List<ScheduleSupportUserEO> scheduleSupportUserEOList = scheduleSupportUserEOService.selectBySupportId(supportUser.getSupportId());
        for (ScheduleSupportUserEO supportUserEO : scheduleSupportUserEOList){
            if (null!= scheduleSupportUserEO.getStatus() && supportUserEO.getStatus() == 0){
                flag = false;
                break;
            }
            supportId = supportUserEO.getSupportId() ;
        }
        if (flag && null!= scheduleSupportUserEO.getStatus()){
            ScheduleSupportEO scheduleSupportEO =  new ScheduleSupportEO();
            scheduleSupportEO.setId(supportId);
            scheduleSupportEO.setStatus(scheduleSupportUserEO.getStatus());
            scheduleSupportEOService.updateByPrimaryKeySelective(scheduleSupportEO);
        }
        return Result.success(scheduleSupportUserEO);
    }

    @ApiOperation(value = "|ScheduleSupportUserEO|删除")
    @DeleteMapping("/{id}")
//    @RequiresPermissions("smallprogram:scheduleSupportUser:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        scheduleSupportUserEOService.deleteByPrimaryKey(id);
        logger.info("delete from TS_SCHEDULE_SUPPORT_USER where id = {}", id);
        return Result.success();
    }

}
