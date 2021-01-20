package com.adc.da.dashboard.vo;

import lombok.Data;

import java.util.*;

/**
 * @ClassName AreaDashBoardVO
 * @Description: 用于地图看板显示
 * @Author 丁强
 * @Date 2020/4/8
 * @Version V1.0
 **/
@Data
public class AreaDashBoardVO {
    private List<Map<String, Object>> mapData = new ArrayList<>();//用于地图显示

    Set<String> areaSet = new TreeSet<>(); //用于柱状图横坐标显示

    List<Double> currentTimeDataList = new ArrayList<>(); //当前时间数值

    List<Double> lastTimeDataList = new ArrayList<>(); //同期时间数值

    List<Double> rateList = new ArrayList<>(); //同比变化率

    Integer currentYear = 2020;

    Integer lastYear = 2019;
}
