package com.adc.da.ext.sys.userdetail.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.ext.sys.userdetail.entity.UserDetailEO;
import com.adc.da.login.util.UserUtils;
import com.adc.da.ext.sys.userdetail.dao.UserDetailEODao;
import com.adc.da.ext.sys.userdetail.page.UserDetailEOPage;
import com.adc.da.ext.sys.userdetail.service.UserDetailEOService;
import com.adc.da.ext.sys.userdetail.vo.PasswordStatusVO;
import com.adc.da.ext.sys.userdetail.vo.PasswordVO;
import com.adc.da.sys.service.UserEOService;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.http.PageInfo;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.utils.Encodes;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.NotNull;


import static com.adc.da.util.utils.StringUtils.isEmpty;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping("/${restPath}/ext/userDetail")
@Api(description = "|UserDetailEO|")
public class UserDetailEOController extends BaseController<UserDetailEO>{

    private static final Logger logger = LoggerFactory.getLogger(UserDetailEOController.class);

    @Autowired
    private UserDetailEOService userDetailEOService;

	@ApiOperation(value = "|UserDetailEO|分页查询")
    @GetMapping("/page")
    @RequiresPermissions("ext:userDetail:page")
    public ResponseMessage<PageInfo<UserDetailEO>> page(UserDetailEOPage page) throws Exception {
        List<UserDetailEO> rows = userDetailEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

	@ApiOperation(value = "|UserDetailEO|查询")
    @GetMapping("")
    @RequiresPermissions("ext:userDetail:list")
    public ResponseMessage<List<UserDetailEO>> list(UserDetailEOPage page) throws Exception {
        return Result.success(userDetailEOService.queryByList(page));
	}

    @ApiOperation(value = "|UserDetailEO|详情")
    @GetMapping("/{id}")
    @RequiresPermissions("ext:userDetail:get")
    public ResponseMessage<UserDetailEO> find(@PathVariable String id) throws Exception {
        return Result.success(userDetailEOService.selectByPrimaryKey(id));
    }

    @ApiOperation(value = "|UserDetailEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("ext:userDetail:save")
    public ResponseMessage<UserDetailEO> create(@RequestBody UserDetailEO userDetailEO) throws Exception {
        userDetailEOService.insertSelective(userDetailEO);
        return Result.success(userDetailEO);
    }

    @ApiOperation(value = "|UserDetailEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions("ext:userDetail:update")
    public ResponseMessage<UserDetailEO> update(@RequestBody UserDetailEO userDetailEO) throws Exception {
        userDetailEOService.updateByPrimaryKeySelective(userDetailEO);
        return Result.success(userDetailEO);
    }

    @ApiOperation(value = "|UserDetailEO|删除")
    @DeleteMapping("/{id}")
    @RequiresPermissions("ext:userDetail:delete")
    public ResponseMessage delete(@PathVariable String id) throws Exception {
        userDetailEOService.deleteByPrimaryKey(id);
        logger.info("delete from TS_USER_DETAIL where id = {}", id);
        return Result.success();
    }


    @GetMapping("/getUserStatusDetail")
    @ApiOperation(value = "查询用户状态 -remainingDays 为用户已更改密码的时间 当为-1时 为新用户，从没修改过")
    public  ResponseMessage<PasswordStatusVO> getUserStatusDetail() {
        PasswordStatusVO passwordNeedsUpdating= userDetailEOService.getUserStatusDetail();
        return Result.success(passwordNeedsUpdating);
    }

    @ApiOperation("修改密码(新)")
    @PutMapping({"/updatePasswordNew"})
    public ResponseMessage updatePasswordNew(@RequestBody PasswordVO passwordVO) {
            return userDetailEOService.updatePasswordNew(passwordVO.getOldPassword(),passwordVO.getNewPassword());
        }
    }



