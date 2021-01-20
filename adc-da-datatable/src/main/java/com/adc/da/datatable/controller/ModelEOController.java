package com.adc.da.datatable.controller;

import com.adc.da.base.web.BaseController;

import com.adc.da.datatable.entity.ModelEO;
import com.adc.da.datatable.page.ModelEOPage;
import com.adc.da.datatable.service.ModelEOService;
import com.adc.da.datatable.vo.ModelVO;
import com.adc.da.login.util.UserUtils;
import com.adc.da.sys.entity.UserEO;
import com.adc.da.util.http.PageInfo;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.utils.BeanMapper;
import com.adc.da.util.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping("/${restPath}/model/model")
@Api( tags = {"模型管理"})
public class ModelEOController extends BaseController<ModelEO> {

    private static final Logger logger = LoggerFactory.getLogger(ModelEOController.class);

    @Autowired
    private ModelEOService modelEOService;
    @Autowired
    private BeanMapper beanMapper;

	@ApiOperation(value = "|ModelEO|分页查询")
    @GetMapping("/page")
    //@RequiresPermissions("model:model:page")
    public ResponseMessage<PageInfo<ModelEO>> page(ModelEOPage page) throws Exception {
        List<ModelEO> rows = modelEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|ModelEO|查询")
    @GetMapping("")
    //@RequiresPermissions("model:model:list")
    public ResponseMessage<List<ModelEO>> list(ModelEOPage page) throws Exception {
        return Result.success(modelEOService.queryByList(page));
	}

    @ApiOperation(value = "|ModelEO|详情")
    @GetMapping("/{mId}")
    //@RequiresPermissions("model:model:get")
    public ResponseMessage<ModelEO> find(@PathVariable String mId) throws Exception {
        return Result.success(modelEOService.selectByPrimaryKey(mId));
    }

    @ApiOperation(value = "模型新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    //@RequiresPermissions("model:model:save")
    public ResponseMessage<ModelVO> create(@RequestBody ModelVO modelVO){
        if (StringUtils.isEmpty(modelVO.getMName())) {
            return Result.error("模型名不能为空");
        } else {
            try {
                ModelEO modelEO = this.modelEOService.insertMenu((ModelEO) this.beanMapper.map(modelVO, ModelEO.class));
                return Result.success(this.beanMapper.map(modelEO, ModelVO.class));
            } catch (Exception var3) {
                logger.error("新增模型失败", var3);
                return Result.error("新增模型失败:");
            }
        }
    }

    @ApiOperation(value = "模型修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    //@RequiresPermissions("model:model:update")
    public ResponseMessage<ModelEO> update(@RequestBody ModelEO modelEO) throws Exception {
        UserEO user = UserUtils.getUser();
        String name = user.getUsname();
        modelEO.setChangeMan(name);
        modelEO.setMChange(new Date());
        modelEOService.updateByPrimaryKeySelective(modelEO);
        return Result.success(modelEO);
    }

    @ApiOperation(value = "|ModelEO|删除")
    @DeleteMapping("/{mId}")
    //@RequiresPermissions("model:model:delete")
    public ResponseMessage delete(@PathVariable String mId) throws Exception {
        modelEOService.deleteByPrimaryKey(mId);
        logger.info("delete from TS_MODEL where mId = {}", mId);
        return Result.success();
    }

    @ApiOperation("透视表配置")
    @PostMapping({"/saveConfig/{mid}/{m_config}"})
    // @RequiresPermissions({"sys:user:saveMultiUserRole"})
    public ResponseMessage saveDetail(@NotNull @PathVariable("mid") String mid, @NotNull @PathVariable("m_config") String mconfig) {
        this.modelEOService.saveConfig(mid, mconfig);
        return Result.success();
    }

}
