package com.adc.da.budget.query;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class QueryVO {

    private String name;

    private Date time;

    private String operator = "=";

    private String logic = "and";

    /**
     * 查询字段名
     */
    private String searchField = "";
}
