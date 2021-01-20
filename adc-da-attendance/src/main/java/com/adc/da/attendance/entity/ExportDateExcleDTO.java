package com.adc.da.attendance.entity;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ExportDateExcleDTO {
    private Date startDate;
    private Date endDate;
    private List<Date> workDateLst;

}
