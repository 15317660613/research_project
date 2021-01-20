package com.adc.da.bus.service;

import com.adc.da.bus.vo.input.FinanceInputParam;
import com.adc.da.bus.vo.output.FinanceShowInfo;

public interface FinanceViewService {
    FinanceShowInfo showMoneyInfo(FinanceInputParam financeInputParam);
}
