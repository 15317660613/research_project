package com.adc.da.budget.controller;

import com.adc.da.budget.service.BubbleService;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 气泡控制层
 * created by chenhaidong 2018/11/29
 */
@RestController
@RequestMapping("/${restPath}/bubble")
@Api("|Bubble|")
public class BubbleController {

    @Autowired
    BubbleService bubbleService;

    @ApiOperation(value = "利润情况-项目视角")
    @GetMapping("/projectProfit")
    public ResponseMessage<List<List<List>>> projectProfit() {
        return Result.success(bubbleService.queryBubble(BubbleService.PROJECT_PROFIT));
    }

    @ApiOperation(value = "利润情况-业务视角")
    @GetMapping("/businessProfit")
    public ResponseMessage<List<List<List>>> businessProfit() {
        return Result.success(bubbleService.queryBubble(BubbleService.BUSINESS_PROFIT));
    }

    @ApiOperation(value = "产值情况-项目视角")
    @GetMapping("/projectOuput")
    public ResponseMessage<List<List<List>>> projectOuput() {
        return Result.success(bubbleService.queryBubble(BubbleService.PROJECT_OUTPUT));
    }

    @ApiOperation(value = "产值情况-业务视角")
    @GetMapping("/businessOuput")
    public ResponseMessage<List<List<List>>> businessOuput() {
        return Result.success(bubbleService.queryBubble(BubbleService.BUSINESS_OUTPUT));
    }

}
