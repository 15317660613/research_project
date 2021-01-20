package com.adc.da.bus.service.impl;

import com.adc.da.bus.dao.FinanceContractMapper;
import com.adc.da.bus.service.FinanceViewService;
import com.adc.da.bus.vo.input.FinanceInputParam;
import com.adc.da.bus.vo.output.FinanceShowInfo;
import com.adc.da.bus.vo.output.FinanceViewEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Auther: ZHAIKAIXUAN
 * @Date: 2018/11/9 15:26
 * @Description:
 */
@Service
public class FinanceViewServiceImpl implements FinanceViewService {



    @Autowired
    private FinanceContractMapper financeContractMapper;



    @Override
    public FinanceShowInfo showMoneyInfo(FinanceInputParam financeInputParam) {
        FinanceShowInfo financeShowInfo = new FinanceShowInfo();

        //获取收入金额一览信息
        List<FinanceViewEntity> financeViewInfo = financeContractMapper.findFinanceViewInfo(financeInputParam);
        // 获取收入划转一览信息
        List<FinanceViewEntity> turnFinanceViewInfo = financeContractMapper.findTurnFinanceViewInfo(financeInputParam);

        financeShowInfo.setTurnList(turnFinanceViewInfo);
        financeShowInfo.setMoneyList(turnFinanceViewInfo);


        return financeShowInfo;
    }
}
