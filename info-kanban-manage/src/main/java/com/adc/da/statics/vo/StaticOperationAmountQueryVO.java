package com.adc.da.statics.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StaticOperationAmountQueryVO {

    private Integer year;
    private Integer lastYear;
    private Integer month;
    private Integer amountType;

    public StaticOperationAmountQueryVO(Integer year, Integer lastYear, Integer amountType) {
        this.year = year;
        this.lastYear = lastYear;
        this.amountType = amountType;
    }
}
