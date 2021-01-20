package com.adc.da.pad.vo;

import lombok.Data;

import java.util.*;

@Data
public class DashBoardBarVO {
    private int currentYear  = 2020;
    private int lastYear = 2019;

    List<String> xIndexList = new ArrayList<>();

    List<Double> currentTimeDataList = new ArrayList<>(); //当前时间数值

    List<Double> lastTimeDataList = new ArrayList<>(); //同期时间数值

    List<Double> rateList = new ArrayList<>(); //同比变化率
}