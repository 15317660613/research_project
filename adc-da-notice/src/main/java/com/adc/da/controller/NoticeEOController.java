package com.adc.da.controller;

import com.adc.da.base.page.BasePage;
import com.adc.da.base.web.BaseController;
import com.adc.da.dto.NoticeUserVO;
import com.adc.da.dto.PageDTO;
import com.adc.da.entity.NoticeEO;
import com.adc.da.entity.NoticeUserEO;
import com.adc.da.login.util.UserUtils;
import com.adc.da.page.NoticeEOPage;
import com.adc.da.service.NoticeEOService;
import com.adc.da.util.http.PageInfo;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import com.adc.da.util.utils.StringUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@RequestMapping("/${restPath}/notice")
@Api(tags = "公告管理|NoticeEO|")
public class NoticeEOController extends BaseController<NoticeEO>{

    private static final Logger logger = LoggerFactory.getLogger(NoticeEOController.class);

    @Autowired
    private NoticeEOService noticeEOService;

    @ApiOperation(value = "|NoticeEO|新增公告")
    @PostMapping(value = "/create",consumes = APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage<NoticeEO> create(@RequestBody NoticeEO noticeEO) throws Exception{
        noticeEOService.insertNotice(noticeEO);
        return Result.success(noticeEO);
    }

    @ApiOperation(value = "|NoticeEO|修改公告")
    @PutMapping(value = "/update",consumes = APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage<NoticeEO> update(@RequestBody NoticeEO noticeEO){
        noticeEOService.updateNotice(noticeEO);
        return  Result.success(noticeEO);
    }

    @ApiOperation(value = "|NoticeEO|批量删除公告")
    @DeleteMapping("/delete/{ids}")
    public ResponseMessage delete(@NotNull @PathVariable("ids") String[] ids){
        List<String> noticeIds = Arrays.asList(ids);
        noticeEOService.deleteNotices(noticeIds);
        return Result.success();
    }

//    @ApiOperation(value = "|NoticeEO|查询提示窗口用户公告列表")
//    @GetMapping(value = "/tipUserNoticeList")
//    public ResponseMessage<List<NoticeEO>> tipUserNoticeList() throws Exception{
//        return Result.success(noticeEOService.findIsIgnoreNoticeListByUserId());
//    }
    @ApiOperation(value = "|NoticeEO|查询提示窗口用户公告列表")
    @GetMapping(value = "/tipUserNoticeList")
    public ResponseMessage<List<NoticeEO>> tipUserNoticeList() {
        return Result.success(noticeEOService.findNotIgnoreNoticeListByUserId());
    }



    @ApiOperation(value = "|NoticeEO|公告详情")
    @GetMapping(value = "/detail/{id}")
    public ResponseMessage<NoticeEO> detail(@PathVariable @NotNull String id){
        return Result.success(noticeEOService.detail(id));
    }

    @ApiOperation(value = "|NoticeEO|首页用户登录显示的公告信息列表-分页")
    @PostMapping(value = "/noticeUserVOList")
    public ResponseMessage<PageDTO> noticeUserVOList(@RequestBody BasePage basePage){
        return Result.success(noticeEOService.noticeUserVOList(basePage));
    }

    @ApiOperation(value = "|NoticeEO|忽略公告消息")
    @PutMapping(value = "/ignoreNotice",consumes = APPLICATION_JSON_UTF8_VALUE)
    public ResponseMessage ignoreNotice(@RequestBody List<NoticeUserEO> noticeUserEOList) throws Exception{
        //List<NoticeEO> noticeEOList= JSONArray.fromObject(noticeEOListStr);
        noticeEOService.doIgnoreNotice(noticeUserEOList);
        return Result.success();
    }




}
