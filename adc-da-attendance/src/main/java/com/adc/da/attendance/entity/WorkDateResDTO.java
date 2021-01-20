package com.adc.da.attendance.entity;

import com.adc.da.base.entity.BaseEntity;
import lombok.Data;

import java.util.Date;

@Data
public class WorkDateResDTO {
    private String id;
    private String calendar;
    private Integer isWorkDate;
}
