package com.adc.da.budget.query;

import lombok.Data;

@Data
public class RangeVO {

    private String start;

    private String startOperator = ">";

    private String end;

    private String endOperator = "<";

}