package com.adc.da.group.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.group.entity.CustomGroupEO;
import com.adc.da.group.entity.CustomGroupVO;
import com.adc.da.group.entity.UserCustomEO;
import com.adc.da.group.entity.UserGroupVO;
import com.adc.da.group.page.UserCustomEOPage;
import com.adc.da.group.service.UserCustomEOService;
import com.adc.da.login.util.UserUtils;
import com.adc.da.util.exception.AdcDaBaseException;
import com.adc.da.util.http.PageInfo;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.utils.CollectionUtils;
import com.adc.da.util.utils.StringUtils;
import com.adc.da.util.utils.UUID;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping("/${restPath}/group/userCustom")
@Api(tags = "|自定义组|")
public class UserCustomEOController extends BaseController<UserCustomEO> {

    @Autowired
    private UserCustomEOService userCustomEOService;



    @ApiOperation(value = "|UserCustomEO|分页查询")
    @GetMapping("/page")
//    @RequiresPermissions("group:userCustom:page")
    public ResponseMessage<PageInfo<UserCustomEO>> page(UserCustomEOPage page) throws Exception {
        List<UserCustomEO> rows = userCustomEOService.queryByPage(page);
        return Result.success(getPageInfo(page.getPager(), rows));
    }

    @ApiOperation(value = "|UserCustomEO|查询")
    @GetMapping("")
//    @RequiresPermissions("group:userCustom:list")
    public ResponseMessage<List<UserCustomEO>> list(UserCustomEOPage page) throws Exception {
        return Result.success(userCustomEOService.queryByList(page));
    }

    @ApiOperation(value = "|UserCustomEO|根据组id查询所有的组成员")
    @GetMapping("/getUserByGroupId")
//    @RequiresPermissions("group:userCustom:list")
    public ResponseMessage<UserGroupVO> getUserByGroupId(String groupId) throws Exception {
        return Result.success(userCustomEOService.getUserByGroupId(groupId));
    }

    @ApiOperation(value = "|UserCustomEO|详情")
    @GetMapping("/{userId}")
//    @RequiresPermissions("group:userCustom:get")
    public ResponseMessage<UserCustomEO> find(@PathVariable String userId) throws Exception {
        return Result.success(userCustomEOService.selectByPrimaryKey(userId));
    }

    @ApiOperation(value = "|UserCustomEO|新增")
    @PostMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("group:userCustom:save")
    public ResponseMessage<UserCustomEO> create(@RequestBody UserCustomEO userCustomEO) throws Exception {
        userCustomEOService.insertSelective(userCustomEO);
        return Result.success(userCustomEO);
    }

    @ApiOperation(value = "|UserCustomEO|新增组内成员")
    @PostMapping(value = "/addGroupAndUsers")
//    @RequiresPermissions("group:userCustom:save")
    public ResponseMessage<UserGroupVO> addGroupUsers(@RequestBody CustomGroupVO customGroupVO) {
        String loginUserId = UserUtils.getUserId();
        if (StringUtils.isEmpty(loginUserId)) {
            throw new AdcDaBaseException("登陆可能过期，请登录！");
        }
        if (CollectionUtils.isEmpty(customGroupVO.getUserIds())) {
            throw new AdcDaBaseException("不能创建空组！");
        }

        CustomGroupEO customGroupEO = new CustomGroupEO();
        customGroupEO.setId(UUID.randomUUID10());
        customGroupEO.setCreateTime(new Date());
        customGroupEO.setCreateUserId(loginUserId);
        customGroupEO.setGroupName(customGroupVO.getGroupName());

        List<UserCustomEO> userCustomEOList = userCustomEOService
            .addGroupAndUsers(customGroupVO.getUserIds(), customGroupEO);
        UserGroupVO userGroupVO = new UserGroupVO();
        userGroupVO.setGroupName(customGroupEO.getGroupName());
        userGroupVO.setUserCustomEOList(userCustomEOList);

        return Result.success(userGroupVO);
    }

    @ApiOperation(value = "|UserCustomEO|修改")
    @PutMapping(consumes = APPLICATION_JSON_UTF8_VALUE)
//    @RequiresPermissions("group:userCustom:update")
    public ResponseMessage<UserCustomEO> update(@RequestBody UserCustomEO userCustomEO) throws Exception {
        userCustomEOService.updateByPrimaryKeySelective(userCustomEO);
        return Result.success(userCustomEO);
    }

}
