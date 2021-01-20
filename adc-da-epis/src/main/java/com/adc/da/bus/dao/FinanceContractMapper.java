package com.adc.da.bus.dao;

import com.adc.da.bus.vo.input.FinanceInputParam;
import com.adc.da.bus.vo.output.FinanceViewEntity;

import java.util.List;

public interface FinanceContractMapper {
    List<FinanceViewEntity> findFinanceViewInfo(FinanceInputParam financeInputParam);

    List<FinanceViewEntity> findTurnFinanceViewInfo(FinanceInputParam financeInputParam);
}
