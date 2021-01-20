package com.adc.da.event.controller;

import com.adc.da.base.web.BaseController;
import com.adc.da.event.entity.EventEO;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/**
 * 获取ip信息 ，用于onlyOffice回调
 */
@RestController
@RequestMapping("/${restPath}/event/event")
@Api(tags = "工作简报|EventEO|")
public class EventIPController extends BaseController<EventEO> {

    @Value("${onlyOffice.java.IpAddress:1}")
    private String serverIpAddress;

    @Value("${onlyOffice.server.IpAddress:1}")
    private String onlyOfficeIpAddress;

    @ApiOperation(value = "获取后端ip与onlyOfficeIP信息，用于onlyOffice回调")
    @GetMapping("/ipInfo")
    public ResponseMessage<Map<String, Object>> getIpInfo(HttpServletRequest request) throws UnknownHostException {

        Map<String, Object> res = new HashMap<>();

        if ("1".equals(serverIpAddress)) {
            //非服务器环境，返回本机ip
            res.put("ServerIP", "192.168.13.34:81");
        } else {
            //服务器环境，直接读取配置文件
            res.put("ServerIP", serverIpAddress);
        }
        if ("1".equals(serverIpAddress)) {
            //若 配置文件不指定， ip 为 192.168.13.231
            res.put("OnlyOfficeIP", "192.168.13.231");
        } else {
            res.put("OnlyOfficeIP", onlyOfficeIpAddress);
        }

        return Result.success(res);

    }

}
