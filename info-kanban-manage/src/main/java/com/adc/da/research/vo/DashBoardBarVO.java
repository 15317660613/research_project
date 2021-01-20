package com.adc.da.research.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class DashBoardBarVO {
    private int currentYear  = 2020;
    private int lastYear = 2019;

    List<String> xIndexList = new ArrayList<>();

    List<Integer> budgetDataList = new ArrayList<>(); //当前时间数值
    List<Integer> useDataList = new ArrayList<>(); //当前时间数值

}
