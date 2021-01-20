package com.adc.da.attendance.entity;

import com.adc.da.excel.poi.excel.entity.ExportParams;
import lombok.Data;

import java.util.Date;

@Data
public class ExportExcleDTO {
    private String startDate;
    private String endDate;
    private String fileName;
    private ExportParams params;


}
