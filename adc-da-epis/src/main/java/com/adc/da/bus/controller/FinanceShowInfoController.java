package com.adc.da.bus.controller;

import com.adc.da.bus.service.FinanceViewService;
import com.adc.da.bus.vo.input.FinanceInputParam;
import com.adc.da.bus.vo.output.FinanceShowInfo;
import com.adc.da.util.http.ResponseMessage;
import com.adc.da.util.http.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: ZHAIKAIXUAN
 * @Date: 2018/11/9 15:54
 * @Description: 收入支出一览表控制器
 */
@Api(description = "收入支出一览表控制器")
@RestController
public class FinanceShowInfoController {

    @Autowired
    private FinanceViewService service;

    @ApiOperation("收入金额和划转金额一览表：月")
    @PostMapping("/finance/amount")
    public ResponseMessage<FinanceShowInfo> queryFinanceInfo(FinanceInputParam financeInputParam){
        return Result.success(service.showMoneyInfo(financeInputParam));
    }


}
