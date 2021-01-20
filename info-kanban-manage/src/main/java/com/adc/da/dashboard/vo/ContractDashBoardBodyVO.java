package com.adc.da.dashboard.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName ContractDashBoardBodyVO
 * @Description: 经营数据看板 主体展示数据包装类
 * @Author 丁强
 * @Date 2020/4/1
 * @Version V1.0
 **/
@Data
public class ContractDashBoardBodyVO {
    List<Double> currentTimeDataList = new ArrayList<>(); //当前时间数值
    List<Double> lastTimeDataList = new ArrayList<>(); //同期时间数值

    List<Double> rateList = new ArrayList<>(); //同比变化率

    Integer currentYear = 2020;

    Integer lastYear = 2019;

}
