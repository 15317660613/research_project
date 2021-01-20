package com.adc.da.budget.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * 人员行程对象
 * @author qichunxu
 */
@Data
public class StaffScheduleVO {

    private Integer day;
    private List<Map<String,List<StaffScheduleAuxiliaryVO>>> timeSlot;
}
