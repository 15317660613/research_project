package com.adc.da.research.personnel.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.research.personnel.entity.ExpertUserInfoEO;
import com.adc.da.research.personnel.page.ExpertUserInfoEOPage;
import com.adc.da.research.personnel.service.ExpertUserInfoEOService;
import com.adc.da.research.personnel.service.ExpertUserInfoTempService;
import com.adc.da.research.personnel.vo.ExpertUserInfoVO;
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

@RestController
@RequestMapping("/${restPath}/personnel/expertUserInfo")
@Api(tags = "科研系统|专家人员|ExpertUserInfoEOController")
public class ExpertUserInfoEOController extends BaseController<ExpertUserInfoEO> {

    private static final Logger logger = LoggerFactory.getLogger(ExpertUserInfoEOController.class);

    @Autowired
    private ExpertUserInfoEOService expertUserInfoEOService;
    @Autowired
    private ExpertUserInfoTempService expertUserInfoTempService;


    @ApiOperation(value = "|ExpertUserInfoEO|查询")
    @GetMapping("")
//    @RequiresPermissions("personnel:expertUserInfo:list")
    public ResponseMessage<List<ExpertUserInfoEO>> list(ExpertUserInfoEOPage page) throws Exception {
        return Result.success(expertUserInfoEOService.queryByList(page));
    }

    @ApiOperation(value = "专家人员信息新增或编辑")
    @PostMapping("/insertOrUpdateExpertUserInfo")
//    @RequiresPermissions("personnel:expertUserInfo:save")
    public ResponseMessage insertOrUpdateExpertUserInfo(@RequestBody ExpertUserInfoVO expertUserInfoVO) throws Exception {
        expertUserInfoEOService.insertOrUpdateExpertUserInfo(expertUserInfoVO);
        return Result.success();
    }

    @ApiOperation(value = "查询单条详情")
    @GetMapping("/selectInfoById/{id}")
//    @RequiresPermissions("personnel:expertUserInfo:get")
    public ResponseMessage<ExpertUserInfoVO> selectInfoById(@PathVariable String id) throws Exception {
        ExpertUserInfoVO expertUserInfoVOTemp = expertUserInfoEOService.selectInfoById(id);
        return Result.success(expertUserInfoVOTemp);
    }

    @ApiOperation(value = "批量删除")
    @DeleteMapping("/batchDelete/{ids}")
//    @RequiresPermissions("personnel:expertUserInfo:delete")
    public ResponseMessage batchDelete(@PathVariable List<String> ids) throws Exception {
        expertUserInfoEOService.batchDelete(ids);
        return Result.success();
    }

    @ApiOperation(value = "获取专家组加用户绑定列表")
    @GetMapping("/getBindUserInfoList")
    public ResponseMessage<PageInfo<ExpertUserInfoEO>> getBindUserInfoList(ExpertUserInfoEOPage  page){
        try{

            List<ExpertUserInfoEO> bindUserInfoList = expertUserInfoEOService.queryByExpertGroupPage(page);
            return Result.success(getPageInfo(page.getPager(), bindUserInfoList));
        }catch (Exception e){
            logger.error(e.getMessage(), e);
            return Result.error("error");
        }

    }

}
